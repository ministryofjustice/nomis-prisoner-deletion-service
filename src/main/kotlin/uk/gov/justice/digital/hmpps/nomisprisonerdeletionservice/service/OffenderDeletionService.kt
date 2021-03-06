package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.config.DataComplianceProperties
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.DataComplianceEventPublisher
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderDeletionComplete
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.logging.DeletionEvent
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.logging.Event
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.OffenderDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.connection.AppModuleName
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.OffenderAliasPendingDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderAliasPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderBookingPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.utils.deepEqualToIgnoreOrder
import java.time.Clock
import java.time.LocalDateTime
import java.util.stream.Collectors

@Service
@Transactional
class OffenderDeletionService(
  val properties: DataComplianceProperties,

  val offenderAliasPendingDeletionRepository: OffenderAliasPendingDeletionRepository,

  val offenderDeletionRepository: OffenderDeletionRepository,

  val dataComplianceEventPublisher: DataComplianceEventPublisher,

  val applicationEventPublisher: ApplicationEventPublisher,

  val clock: Clock
) {
  fun deleteOffender(grant: OffenderDeletionGrant) {

    check(properties.deletionEnabled) { "Unable to delete offender. Deletion is not enabled!" }
    checkRequestedDeletion(grant)

    offenderDeletionRepository.setContext(AppModuleName.MERGE)
    val offenderIds = offenderDeletionRepository.cleanseOffenderDataExcludingBaseRecord(grant.offenderNo)
    offenderDeletionRepository.setContext(AppModuleName.NOMIS_DELETION_SERVICE)

    dataComplianceEventPublisher.send(OffenderDeletionComplete(grant.offenderNo, grant.referralId))

    applicationEventPublisher.publishEvent(DeletionEvent(Event.OFFENDER_DELETION, offenderIds, grant.offenderNo, grant.referralId, LocalDateTime.now(clock)))
  }

  private fun checkRequestedDeletion(grant: OffenderDeletionGrant) {
    val offenderAliases =
      offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(grant.offenderNo)

    val offenderIds = offenderAliases.map(OffenderAliasPendingDeletion::offenderId)

    val offenderBookIds = offenderAliases.stream()
      .map(OffenderAliasPendingDeletion::offenderBookings)
      .flatMap { it.stream() }
      .map(OffenderBookingPendingDeletion::bookingId)
      .collect(Collectors.toSet())

    check(offenderIds.deepEqualToIgnoreOrder(grant.offenderIds)) {
      String.format(
        "The requested offender ids (%s) do not match those currently linked to offender '%s' (%s)",
        grant.offenderIds,
        grant.offenderNo,
        offenderIds
      )
    }

    check(offenderBookIds.deepEqualToIgnoreOrder(grant.offenderBookIds)) {
      String.format(
        "The requested offender book ids (%s) do not match those currently linked to offender '%s' (%s)",
        grant.offenderBookIds,
        grant.offenderNo,
        offenderBookIds
      )
    }
  }

  data class OffenderDeletionGrant(
    val offenderNo: String,
    val referralId: Long,
    val offenderIds: Set<Long> = HashSet(),
    val offenderBookIds: Set<Long> = HashSet(),
  )
}
