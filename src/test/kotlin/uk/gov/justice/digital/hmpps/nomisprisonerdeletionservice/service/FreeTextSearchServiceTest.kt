package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.DataComplianceEventPublisher
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.FreeTextSearchResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.bookingId
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.buildOffenderAliasPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.matchingTable1
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.matchingTable2
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.offenderId1
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.offenderNumber1
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.regex
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.retentionCheckId
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.FreeTextRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.OffenderAliasPendingDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.FreeTextMatch

internal class FreeTextSearchServiceTest {

  private val offenderAliasPendingDeletionRepository = mock<OffenderAliasPendingDeletionRepository>()
  private val freeTextRepository = mock<FreeTextRepository>()
  private val dataComplianceEventPublisher = mock<DataComplianceEventPublisher>()

  lateinit var freeTextSearchService: FreeTextSearchService

  @BeforeEach
  fun setUp() {
    freeTextSearchService =
      FreeTextSearchService(offenderAliasPendingDeletionRepository, freeTextRepository, dataComplianceEventPublisher)
  }

  @Test
  fun `check for matching content`() {
    whenever(freeTextRepository.findMatchUsingOffenderIds(setOf(offenderId1), regex))
      .thenReturn(listOf(FreeTextMatch(matchingTable1)))

    whenever(freeTextRepository.findMatchUsingBookIds(setOf(bookingId), regex))
      .thenReturn(listOf(FreeTextMatch(matchingTable2)))

    whenever(offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber1))
      .thenReturn(listOf(buildOffenderAliasPendingDeletion(offenderId1, offenderNumber1)))

    freeTextSearchService.checkForMatchingContent(offenderNumber1, retentionCheckId, listOf(regex))

    verify(dataComplianceEventPublisher).send(
      FreeTextSearchResult(
        offenderIdDisplay = offenderNumber1,
        retentionCheckId = retentionCheckId,
        matchingTables = listOf(matchingTable2, matchingTable1)
      )
    )
  }

  @Test
  fun `check for matching content finds no match`() {
    whenever(freeTextRepository.findMatchUsingOffenderIds(setOf(offenderId1), regex))
      .thenReturn(emptyList())

    whenever(freeTextRepository.findMatchUsingBookIds(setOf(bookingId), regex))
      .thenReturn(emptyList())

    whenever(offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber1))
      .thenReturn(listOf(buildOffenderAliasPendingDeletion(offenderId1, offenderNumber1)))

    freeTextSearchService.checkForMatchingContent(offenderNumber1, retentionCheckId, listOf(regex))

    verify(dataComplianceEventPublisher).send(
      FreeTextSearchResult(
        offenderIdDisplay = offenderNumber1,
        retentionCheckId = retentionCheckId,
        matchingTables = emptyList()
      )
    )
  }

  @Test
  fun `check for matching content throws if offender not found`() {
    whenever(offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber1))
      .thenReturn(emptyList())

    Assertions.assertThatThrownBy {
      freeTextSearchService.checkForMatchingContent(
        offenderNumber1,
        retentionCheckId,
        listOf(regex)
      )
    }
      .isInstanceOf(IllegalStateException::class.java)
      .hasMessage("Expecting to find at least one offender id for offender: 'A1234AA'")

    verifyNoInteractions(dataComplianceEventPublisher)
  }
}
