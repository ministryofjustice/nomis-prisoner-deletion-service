package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.DataComplianceEventPublisher
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.DataDuplicateResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.DuplicatePrisonerRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.OffenderAliasPendingDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.DuplicateOffender
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderAliasPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.utils.getFormattedCroNumbersFrom
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.utils.getFormattedPncNumbersFrom
import javax.transaction.Transactional

@Service
@Transactional
class DataDuplicateService(
  val offenderAliasPendingDeletionRepository: OffenderAliasPendingDeletionRepository,
  val duplicatePrisonerRepository: DuplicatePrisonerRepository,
  val eventPublisher: DataComplianceEventPublisher
) {

  fun checkForDuplicateIds(offenderNo: String, retentionCheckId: Long) {
    val offenderAliases =
      offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNo)

    val duplicateOffenders = mutableSetOf<String>()
      .apply {
        addAll(getOffendersWithMatchingLidsNumbers(offenderNo))
        addAll(getOffendersWithMatchingCroNumbers(offenderNo, offenderAliases))
        addAll(getOffendersWithMatchingPncNumbers(offenderNo, offenderAliases))
      }.toList()

    eventPublisher.sendDuplicateIdResult(
      DataDuplicateResult(
        offenderIdDisplay = offenderNo,
        retentionCheckId = retentionCheckId,
        duplicateOffenders = duplicateOffenders
      )
    )
  }

  fun checkForDataDuplicates(offenderNo: String, retentionCheckId: Long) {
    eventPublisher.sendDuplicateDataResult(
      DataDuplicateResult(
        offenderIdDisplay = offenderNo,
        retentionCheckId = retentionCheckId,
        duplicateOffenders = getOffendersWithMatchingDetails(offenderNo).toList()
      )
    )
  }

  private fun getOffendersWithMatchingCroNumbers(
    offenderNo: String,
    offenderAliases: List<OffenderAliasPendingDeletion>
  ): Set<String> {

    val croNumbers = getFormattedCroNumbersFrom(offenderAliases)

    if (croNumbers.isEmpty()) {
      return emptySet()
    }

    val duplicates =
      duplicatePrisonerRepository.getOffendersWithMatchingCroNumbers(offenderNo, croNumbers)
        .map(DuplicateOffender::offenderNumber)
        .toSet()

    if (duplicates.isNotEmpty()) {
      log.info("Found offender(s) ({}) with matching CROs for offender '{}'", duplicates, offenderNo)
    }
    return duplicates
  }

  fun getOffendersWithMatchingLidsNumbers(offenderNo: String): Set<String> {
    val duplicates = duplicatePrisonerRepository.getOffendersWithMatchingLidsNumbers(offenderNo)
      .map(DuplicateOffender::offenderNumber)
      .toSet()

    if (duplicates.isNotEmpty()) {
      log.info("Found offender(s) ({}) with matching LIDS numbers for offender '{}'", duplicates, offenderNo)
    }
    return duplicates
  }

  fun getOffendersWithMatchingPncNumbers(
    offenderNo: String,
    offenderAliases: List<OffenderAliasPendingDeletion>
  ): Set<String> {
    val pncNumbers = getFormattedPncNumbersFrom(offenderAliases)
    if (pncNumbers.isEmpty()) {
      return emptySet()
    }
    val duplicates = duplicatePrisonerRepository.getOffendersWithMatchingPncNumbers(offenderNo, pncNumbers)
      .map(DuplicateOffender::offenderNumber)
      .toSet()

    if (duplicates.isNotEmpty()) {
      log.info("Found offender(s) ({}) with matching PNCs for offender '{}'", duplicates, offenderNo)
    }
    return duplicates
  }

  fun getOffendersWithMatchingDetails(offenderNo: String): Set<String> {
    val duplicates = duplicatePrisonerRepository.getOffendersWithMatchingDetails(offenderNo)
      .map(DuplicateOffender::offenderNumber)
      .toSet()

    if (duplicates.isNotEmpty()) {
      log.info("Found offender(s) ({}) with matching details for offender '{}'", duplicates, offenderNo)
    }
    return duplicates
  }

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }
}
