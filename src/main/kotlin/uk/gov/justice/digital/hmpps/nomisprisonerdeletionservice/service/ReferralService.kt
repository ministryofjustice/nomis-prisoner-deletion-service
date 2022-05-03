package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service

import com.google.common.base.Preconditions.checkState
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.data.OffenderNumber
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.DataComplianceEventPublisher
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderPendingDeletion.OffenderAlias
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderPendingDeletionReferralComplete
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.ProvisionalDeletionReferralResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.OffenderAliasPendingDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.OffenderPendingDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderAlertPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderAliasPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderBookingPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderChargePendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.utils.getFormattedCroNumbersFrom
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.utils.getFormattedPncNumbersFrom
import java.lang.String.format
import java.time.Clock
import java.time.LocalDate
import java.util.stream.Collectors
import javax.transaction.Transactional

@Service
@Transactional
class ReferralService(
  val offenderPendingDeletionRepository: OffenderPendingDeletionRepository,
  val offenderAliasPendingDeletionRepository: OffenderAliasPendingDeletionRepository,
  val dataComplianceEventPusher: DataComplianceEventPublisher,
  val movementsService: MovementsService,
  val clock: Clock
) {

  fun referOffendersForDeletion(batchId: Long, from: LocalDate, to: LocalDate, pageable: Pageable) {
    val offenderNumbers: Page<OffenderNumber> = getOffendersPendingDeletion(from, to, pageable)

    offenderNumbers.forEach { offenderNo: OffenderNumber ->
      dataComplianceEventPusher.send(generateOffenderPendingDeletionEvent(offenderNo, batchId))
    }

    dataComplianceEventPusher.send(
      OffenderPendingDeletionReferralComplete(
        batchId = batchId,
        numberReferred = offenderNumbers.numberOfElements.toLong(),
        totalInWindow = offenderNumbers.totalElements
      )
    )
  }

  fun referAdHocOffenderDeletion(offenderNumber: String, batchId: Long) {

    val offenderPendingDeletion =
      offenderPendingDeletionRepository.findOffenderPendingDeletion(offenderNumber, LocalDate.now(clock))
        ?.let { OffenderNumber(it.offenderNumber) }

    checkNotNull(offenderPendingDeletion) { "Unable to find offender that qualifies for deletion with number: '$offenderNumber'" }

    dataComplianceEventPusher.send(generateOffenderPendingDeletionEvent(offenderPendingDeletion, batchId))
  }

  fun referProvisionalDeletion(offenderNumber: String, referralId: Long) {

    val offenderPendingDeletion =
      offenderPendingDeletionRepository.findOffenderPendingDeletion(offenderNumber, LocalDate.now(clock))
        ?.let { OffenderNumber(it.offenderNumber) }

    val provisionalDeletionReferralResult = (
      offenderPendingDeletion?.let
      { generateProvisionalDeletionReferralResultEvent(offenderNumber, referralId) }
        ?: ProvisionalDeletionReferralResult.changesIdentifiedResult(referralId, offenderNumber)
      )

    dataComplianceEventPusher.send(provisionalDeletionReferralResult)
  }

  private fun getOffendersPendingDeletion(from: LocalDate, to: LocalDate, pageable: Pageable): Page<OffenderNumber> {
    return offenderPendingDeletionRepository
      .getOffendersDueForDeletionBetween(from, to, pageable)
      .map { OffenderNumber(it.offenderNumber) }
  }

  private fun generateOffenderPendingDeletionEvent(
    offenderNumber: OffenderNumber,
    batchId: Long
  ): OffenderPendingDeletion {
    val offenderAliases =
      offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber.offenderNumber)

    checkState(offenderAliases.isNotEmpty(), "Offender not found: '%s'", offenderNumber.offenderNumber)

    val offenderAliasPendingDeletion =
      offenderAliases.find { it.offenderId == it.rootOffenderId } ?: throw IllegalStateException(
        format("Cannot find root offender alias for '%s'", offenderNumber.offenderNumber)
      )

    return offenderAliasPendingDeletion.let {
      OffenderPendingDeletion(
        offenderIdDisplay = it.offenderNumber,
        batchId = batchId,
        firstName = it.firstName,
        middleName = it.middleName,
        lastName = it.lastName,
        birthDate = it.birthDate,
        agencyLocationId = getLatestLocationId(it.offenderNumber!!),
        pncs = getFormattedPncNumbersFrom(offenderAliases),
        cros = getFormattedCroNumbersFrom(offenderAliases),
        offenderAliases = offenderAliases.map { alias ->
          OffenderAlias(
            offenderId = alias.offenderId,
            bookings = alias.offenderBookings.map
            { booking ->
              OffenderPendingDeletion.Booking(
                offenderBookId = booking.bookingId,
                bookingNo = booking.bookNumber,
                offenceCodes = booking.offenderCharges.map { charge -> charge.offenceCode.orEmpty() }.toSet(),
                alertCodes = booking.offenderAlerts.map { alert -> alert.alertCode }.toSet()
              )
            }
          )
        }
      )
    }
  }

  private fun getLatestLocationId(offenderNumber: String): String? {
    return movementsService.getMovementsByOffenders(listOf(offenderNumber), listOf("REL"), true, true)
      .firstNotNullOfOrNull { it.fromAgency }
  }

  private fun generateProvisionalDeletionReferralResultEvent(
    offenderNo: String,
    referralId: Long
  ): ProvisionalDeletionReferralResult? {
    val offenderAliases =
      offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNo)
    check(offenderAliases.isNotEmpty()) { "Offender not found: '$offenderNo'" }
    return transform(offenderNo, offenderAliases, referralId)
  }

  private fun transform(
    offenderNumber: String,
    offenderAliases: List<OffenderAliasPendingDeletion>,
    referralId: Long
  ): ProvisionalDeletionReferralResult {
    return ProvisionalDeletionReferralResult(
      referralId = referralId,
      offenderIdDisplay = offenderNumber,
      subsequentChangesIdentified = false,
      agencyLocationId = getLatestLocationId(offenderNumber),
      offenceCodes = getOffenceCodes(offenderAliases),
      alertCodes = getAlertCodes(offenderAliases)
    )
  }

  private fun getOffenceCodes(offenderAliases: List<OffenderAliasPendingDeletion>): Set<String> {
    return offenderAliases.stream()
      .map(OffenderAliasPendingDeletion::offenderBookings)
      .flatMap { it.stream() }
      .map(OffenderBookingPendingDeletion::offenderCharges)
      .flatMap { it.stream() }
      .map(OffenderChargePendingDeletion::offenceCode)
      .collect(Collectors.toSet())
  }

  private fun getAlertCodes(offenderAliases: List<OffenderAliasPendingDeletion>): Set<String>? {
    return offenderAliases.stream()
      .map(OffenderAliasPendingDeletion::offenderBookings)
      .flatMap { it.stream() }
      .map(OffenderBookingPendingDeletion::offenderAlerts)
      .flatMap { it.stream() }
      .map(OffenderAlertPendingDeletion::alertCode)
      .collect(Collectors.toSet())
  }
}
