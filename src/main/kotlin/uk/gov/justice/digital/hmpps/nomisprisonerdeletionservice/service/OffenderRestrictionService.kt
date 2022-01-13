package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.consumer.dto.OffenderRestrictionRequest
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.DataComplianceEventPublisher
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderRestrictionResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.OffenderAliasPendingDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.OffenderRestrictionsRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.OffenderSentConditionsRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderAliasPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderBookingPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderRestrictions
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderSentConditions
import java.util.stream.Collectors

@Service
@Transactional
class OffenderRestrictionService(
  val offenderAliasPendingDeletionRepository: OffenderAliasPendingDeletionRepository,
  val offenderRestrictionsRepository: OffenderRestrictionsRepository,
  val offenderSentConditionsRepository: OffenderSentConditionsRepository,
  val dataComplianceEventPublisher: DataComplianceEventPublisher
) {
  fun checkForOffenderRestrictions(offenderRestrictionRequest: OffenderRestrictionRequest) {
    val offenderNumber: String = offenderRestrictionRequest.offenderIdDisplay
    val offenderAliases =
      offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber)

    check(offenderAliases.isNotEmpty()) { "Expecting to find at least one offender id for offender: '$offenderNumber'" }

    val bookIds = getBookIds(offenderAliases)

    val offenderRestrictions: List<OffenderRestrictions> = offenderRestrictionsRepository.findOffenderRestrictions(
      bookIds = bookIds,
      offenderRestrictionCodes = offenderRestrictionRequest.restrictionCodes,
      regex = offenderRestrictionRequest.regex!!
    )

    val childRelatedConditionsByBookings: List<OffenderSentConditions> =
      offenderSentConditionsRepository.findChildRelatedConditionsByBookings(bookIds.toList())

    pushOffenderRestrictionResult(
      offenderRestrictionRequest,
      isRestricted(offenderRestrictions, childRelatedConditionsByBookings)
    )
  }

  fun pushOffenderRestrictionResult(offenderRestrictionRequest: OffenderRestrictionRequest, restricted: Boolean) {
    dataComplianceEventPublisher.send(
      OffenderRestrictionResult(
        offenderIdDisplay = offenderRestrictionRequest.offenderIdDisplay,
        retentionCheckId = offenderRestrictionRequest.retentionCheckId,
        restricted = restricted
      )
    )
  }

  fun getBookIds(offenderAliases: Collection<OffenderAliasPendingDeletion>): Set<Long> {
    return offenderAliases.stream()
      .map(OffenderAliasPendingDeletion::offenderBookings)
      .flatMap { it.stream() }
      .map(OffenderBookingPendingDeletion::bookingId)
      .collect(Collectors.toList())
      .filterNotNull()
      .toSet()
  }

  fun isRestricted(
    offenderRestrictions: List<OffenderRestrictions>,
    childRelatedConditionsByBookings: List<OffenderSentConditions>
  ): Boolean {
    return (offenderRestrictions.isNotEmpty() || childRelatedConditionsByBookings.isNotEmpty())
  }
}
