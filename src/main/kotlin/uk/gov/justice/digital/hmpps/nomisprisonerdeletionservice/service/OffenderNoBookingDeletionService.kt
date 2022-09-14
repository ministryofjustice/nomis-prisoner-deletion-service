package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.config.DataComplianceProperties
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.DataComplianceEventPublisher
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderNoBookingDeletionResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderNoBookingDeletionResult.Offender
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderNoBookingDeletionResult.OffenderAlias
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.logging.DeletionEvent
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.logging.Event
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.OffenderDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.OffenderAliasPendingDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.OffenderNoBookingPendingDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderAliasPendingDeletion
import java.time.Clock
import java.time.LocalDateTime

@Service
@Transactional
class OffenderNoBookingDeletionService(
  val offenderNoBookingPendingDeletionRepository: OffenderNoBookingPendingDeletionRepository,

  val offenderAliasPendingDeletionRepository: OffenderAliasPendingDeletionRepository,

  val offenderDeletionRepository: OffenderDeletionRepository,

  val eventPublisher: DataComplianceEventPublisher,

  val applicationEventPublisher: ApplicationEventPublisher,

  val properties: DataComplianceProperties,

  val clock: Clock
) {

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }

  fun deleteOffendersWithNoBookings(batchId: Long, excludedOffenders: Set<String>, pageable: Pageable) {

    check(properties.offenderNoBookingDeletionEnabled) { "Offender No bookings deletion is not enabled!" }

    var offenders: MutableList<Offender> = mutableListOf()

    offendersWithNoBookings(excludedOffenders, pageable)
      .forEach {
        val (offenderNumber, offenderAliases) = it

        log.info("Offender with no bookings {} has been identified for deletion", offenderNumber)
        val rootOffenderAlias = getRootOffender(offenderNumber, offenderAliases)
        val offenderIds = offenderDeletionRepository.deleteAllOffenderDataExcludingBookings(offenderNumber)
        offenders.add(buildOffender(offenderNumber, rootOffenderAlias, offenderAliases))
        applicationEventPublisher.publishEvent(
          DeletionEvent(
            Event.OFFENDER_NO_BOOKING_DELETION,
            offenderIds,
            offenderNumber,
            batchId,
            LocalDateTime.now(clock)
          )
        )
      }

    if (offenders.isNotEmpty()) eventPublisher.send(OffenderNoBookingDeletionResult(batchId, offenders))
  }

  private fun offendersWithNoBookings(excludedOffenders: Set<String>, pageable: Pageable) =
    offenderNoBookingPendingDeletionRepository.findOffendersWithNoBookingsDueForDeletion(pageable)
      .filter { !excludedOffenders.contains(it.offenderNumber) }
      .map { it.offenderNumber to getOffenderAliases(it.offenderNumber) }
      .filter { it.second.all { aliasPendingDeletion -> aliasPendingDeletion.offenderBookings.isEmpty() } }

  private fun getOffenderAliases(offenderNumber: String): List<OffenderAliasPendingDeletion> {
    val offenderAliases =
      offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber)
    check(offenderAliases.isNotEmpty()) { "Offender not found: '$offenderNumber'" }
    return offenderAliases
  }

  private fun getRootOffender(
    offenderNumber: String,
    offenderAliases: List<OffenderAliasPendingDeletion>
  ): OffenderAliasPendingDeletion {
    val rootOffender: OffenderAliasPendingDeletion? = offenderAliases.find { it.offenderId == it.rootOffenderId }
    return requireNotNull(rootOffender) { "Cannot find root offender alias for '$offenderNumber'" }
  }

  private fun buildOffender(
    offenderNumber: String,
    rootAlias: OffenderAliasPendingDeletion,
    offenderAliases: List<OffenderAliasPendingDeletion>,
  ): Offender {

    return Offender(
      offenderIdDisplay = offenderNumber,
      firstName = rootAlias.firstName,
      middleName = rootAlias.middleName,
      lastName = rootAlias.lastName,
      birthDate = rootAlias.birthDate,
      deletionDateTime = LocalDateTime.now(clock),
      offenderAliases = offenderAliases.map { alias ->
        OffenderAlias(
          offenderId = alias.offenderId,
        )
      },
    )
  }
}
