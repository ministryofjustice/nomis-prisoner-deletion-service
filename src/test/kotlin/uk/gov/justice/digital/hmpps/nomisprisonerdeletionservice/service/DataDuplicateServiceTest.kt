package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.DataComplianceEventPublisher
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.DataDuplicateResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.duplicate1
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.duplicate2
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.duplicate3
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.formattedOffenderCro
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.formattedOffenderPnc
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.offenderCro
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.offenderNumber1
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.offenderPnc
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.retentionCheckId
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.DuplicatePrisonerRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.OffenderAliasPendingDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.DuplicateOffender
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderAliasPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderIdentifierPendingDeletion

internal class DataDuplicateServiceTest {

  private val offenderAliasPendingDeletionRepository = mock<OffenderAliasPendingDeletionRepository>()
  private val duplicatePrisonerRepository = mock<DuplicatePrisonerRepository>()
  private val eventPublisher = mock<DataComplianceEventPublisher>()

  private lateinit var service: DataDuplicateService

  @BeforeEach
  fun setUp() {
    service = DataDuplicateService(
      offenderAliasPendingDeletionRepository = offenderAliasPendingDeletionRepository,
      eventPublisher = eventPublisher,
      duplicatePrisonerRepository = duplicatePrisonerRepository
    )
  }

  @Test
  fun `check for duplicate id's`() {

    mockIdentifiers(
      offenderNumber1,
      mapOf(
        "PNC" to offenderPnc,
        "CRO" to offenderCro
      )
    )

    whenever(
      duplicatePrisonerRepository.getOffendersWithMatchingPncNumbers(
        offenderNumber1,
        setOf(formattedOffenderPnc)
      )
    )
      .thenReturn(listOf(DuplicateOffender(duplicate1)))
    whenever(
      duplicatePrisonerRepository.getOffendersWithMatchingCroNumbers(
        offenderNumber1,
        setOf(formattedOffenderCro)
      )
    )
      .thenReturn(listOf(DuplicateOffender(duplicate2)))
    whenever(duplicatePrisonerRepository.getOffendersWithMatchingLidsNumbers(offenderNumber1))
      .thenReturn(listOf(DuplicateOffender(duplicate3)))

    service.checkForDuplicateIds(offenderNumber1, retentionCheckId)

    verify(eventPublisher).sendDuplicateIdResult(
      DataDuplicateResult(
        offenderIdDisplay = offenderNumber1,
        retentionCheckId = retentionCheckId,
        duplicateOffenders = listOf(duplicate3, duplicate2, duplicate1)
      )
    )
  }

  @Test
  fun `check for duplicate ids returns empty if no matching identifiers`() {

    mockIdentifiers(
      offenderNumber1,
      mapOf(
        "PNC" to offenderPnc,
        "CRO" to offenderCro
      )
    )

    whenever(
      duplicatePrisonerRepository.getOffendersWithMatchingPncNumbers(
        offenderNumber1,
        setOf(formattedOffenderPnc)
      )
    )
      .thenReturn(emptyList())
    whenever(
      duplicatePrisonerRepository.getOffendersWithMatchingCroNumbers(
        offenderNumber1,
        setOf(formattedOffenderCro)
      )
    )
      .thenReturn(emptyList())
    whenever(duplicatePrisonerRepository.getOffendersWithMatchingLidsNumbers(offenderNumber1))
      .thenReturn(emptyList())

    service.checkForDuplicateIds(offenderNumber1, retentionCheckId)

    verify(eventPublisher).sendDuplicateIdResult(
      DataDuplicateResult(
        offenderIdDisplay = offenderNumber1,
        retentionCheckId = retentionCheckId,
        duplicateOffenders = emptyList()
      )
    )
  }

  @Test
  fun `check for duplicate ids returns empty if no valid identifiers`() {
    mockIdentifiers(
      offenderNumber1,
      mapOf(
        "PNC" to "AN_INVALID_PNC",
        "CRO" to "AN_INVALID_CRO"
      )
    )

    whenever(duplicatePrisonerRepository.getOffendersWithMatchingLidsNumbers(offenderNumber1))
      .thenReturn(emptyList())

    service.checkForDuplicateIds(offenderNumber1, retentionCheckId)

    verify(eventPublisher).sendDuplicateIdResult(
      DataDuplicateResult(
        offenderIdDisplay = offenderNumber1,
        retentionCheckId = retentionCheckId,
        duplicateOffenders = emptyList()
      )
    )
  }

  @Test
  fun `check for data duplicates`() {
    whenever(duplicatePrisonerRepository.getOffendersWithMatchingDetails(offenderNumber1))
      .thenReturn(listOf(DuplicateOffender(duplicate1)))

    service.checkForDataDuplicates(offenderNumber1, retentionCheckId)

    verify(eventPublisher).sendDuplicateIdResult(
      DataDuplicateResult(
        offenderIdDisplay = offenderNumber1,
        retentionCheckId = retentionCheckId,
        duplicateOffenders = listOf(duplicate1)
      )
    )
  }

  @Test
  fun `check for data duplicates when there are no offenders with matching details`() {
    whenever(duplicatePrisonerRepository.getOffendersWithMatchingDetails(offenderNumber1))
      .thenReturn(emptyList())

    service.checkForDataDuplicates(offenderNumber1, retentionCheckId)

    verify(eventPublisher).sendDuplicateIdResult(
      DataDuplicateResult(
        offenderIdDisplay = offenderNumber1,
        retentionCheckId = retentionCheckId,
        duplicateOffenders = emptyList()
      )
    )
  }

  private fun mockIdentifiers(offenderNo: String, identifierMap: Map<String, String>) {

    val identifiers = identifierMap.entries
      .map {
        OffenderIdentifierPendingDeletion()
          .apply {
            identifierType = it.key
            identifier = it.value
          }
      }.toSet()

    val offenderAliasPendingDeletion =
      OffenderAliasPendingDeletion().apply { offenderIdentifiers = identifiers.toList() }

    whenever(offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNo))
      .thenReturn(listOf(offenderAliasPendingDeletion))
  }
}
