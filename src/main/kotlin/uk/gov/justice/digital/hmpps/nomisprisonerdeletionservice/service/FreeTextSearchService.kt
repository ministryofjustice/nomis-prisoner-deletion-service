package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.DataComplianceEventPublisher
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.FreeTextSearchResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.FreeTextRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.OffenderAliasPendingDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.FreeTextMatch
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderAliasPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderBookingPendingDeletion
import java.util.function.Consumer
import java.util.stream.Collectors

@Service
@Transactional
class FreeTextSearchService(
  val offenderAliasPendingDeletionRepository: OffenderAliasPendingDeletionRepository,
  val freeTextRepository: FreeTextRepository,
  val dataComplianceEventPublisher: DataComplianceEventPublisher
) {
  fun checkForMatchingContent(offenderNumber: String, retentionCheckId: Long, regex: List<String>) {

    dataComplianceEventPublisher.send(
      FreeTextSearchResult(
        offenderIdDisplay = offenderNumber,
        retentionCheckId = retentionCheckId,
        matchingTables = getTablesWithMatchingContent(offenderNumber, regex)
          .toList()
      )
    )
  }

  fun getTablesWithMatchingContent(offenderNumber: String, regex: List<String>): Set<String> {
    val offenderAliases =
      offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber)

    check(offenderAliases.isNotEmpty()) { "Expecting to find at least one offender id for offender: '$offenderNumber'" }

    val tables = HashSet<String>()

    regex.forEach(
      Consumer { rx: String ->
        tables.addAll(getTableNames(freeTextRepository.findMatchUsingOffenderIds(getOffenderIds(offenderAliases), rx)))
        tables.addAll(getTableNames(freeTextRepository.findMatchUsingBookIds(getBookIds(offenderAliases), rx)))
      }
    )
    return tables
  }

  fun getOffenderIds(offenderAliases: Collection<OffenderAliasPendingDeletion>): Set<Long> {
    return offenderAliases
      .mapNotNull(OffenderAliasPendingDeletion::offenderId)
      .toSet()
  }

  fun getBookIds(offenderAliases: Collection<OffenderAliasPendingDeletion>): Set<Long> {
    return offenderAliases
      .stream()
      .map(OffenderAliasPendingDeletion::offenderBookings)
      .flatMap { it.stream() }
      .map(OffenderBookingPendingDeletion::bookingId)
      .collect(Collectors.toSet())
      .filterNotNull()
      .toSet()
  }

  fun getTableNames(matches: Collection<FreeTextMatch>): Set<String> {
    return matches.map(FreeTextMatch::tableName).toSet()
  }
}
