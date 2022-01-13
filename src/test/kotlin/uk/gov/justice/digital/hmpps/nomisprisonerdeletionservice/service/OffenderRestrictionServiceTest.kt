package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.consumer.dto.OffenderRestrictionRequest
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.DataComplianceEventPublisher
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderRestrictionResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.bookingId
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.buildOffenderAliasPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.offenderId1
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.offenderNumber1
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.offenderSentConditionId
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.regex
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.retentionCheckId
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.OffenderAliasPendingDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.OffenderRestrictionsRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.OffenderSentConditionsRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderRestrictions
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderSentConditions

class OffenderRestrictionServiceTest {

  private val offenderAliasPendingDeletionRepository = mock<OffenderAliasPendingDeletionRepository>()
  private val offenderRestrictionsRepository = mock<OffenderRestrictionsRepository>()
  private val offenderSentConditionsRepository = mock<OffenderSentConditionsRepository>()
  private val eventPublisher = mock<DataComplianceEventPublisher>()

  lateinit var service: OffenderRestrictionService

  @BeforeEach
  fun setUp() {
    service = OffenderRestrictionService(
      offenderAliasPendingDeletionRepository,
      offenderRestrictionsRepository,
      offenderSentConditionsRepository,
      eventPublisher,
    )
  }

  @Test
  fun `check for offender restrictions`() {
    whenever(offenderRestrictionsRepository.findOffenderRestrictions(setOf(bookingId), setOf("CHILD"), regex))
      .thenReturn(listOf(OffenderRestrictions(bookingId, offenderId1, "CHILD", "some comment text")))

    whenever(offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber1))
      .thenReturn(listOf(buildOffenderAliasPendingDeletion(offenderId1, offenderNumber1)))

    whenever(offenderSentConditionsRepository.findChildRelatedConditionsByBookings(listOf(bookingId)))
      .thenReturn(listOf(OffenderSentConditions(offenderSentConditionId, bookingId, "N", "Y")))

    service.checkForOffenderRestrictions(
      OffenderRestrictionRequest(
        offenderIdDisplay = offenderNumber1,
        retentionCheckId = retentionCheckId,
        restrictionCodes = setOf("CHILD"),
        regex = regex
      )
    )

    verify(eventPublisher).send(
      OffenderRestrictionResult(
        offenderIdDisplay = offenderNumber1,
        retentionCheckId = retentionCheckId,
        restricted = true
      )
    )
  }

  @Test
  fun `check for offender restrictions and sent conditions match`() {
    whenever(offenderRestrictionsRepository.findOffenderRestrictions(setOf(bookingId), setOf("CHILD"), regex))
      .thenReturn(emptyList())

    whenever(offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber1))
      .thenReturn(listOf(buildOffenderAliasPendingDeletion(offenderId1, offenderNumber1)))

    whenever(offenderSentConditionsRepository.findChildRelatedConditionsByBookings(listOf(bookingId)))
      .thenReturn(listOf(OffenderSentConditions(offenderSentConditionId, bookingId, "Y", "N")))

    service.checkForOffenderRestrictions(
      OffenderRestrictionRequest(
        offenderIdDisplay = offenderNumber1,
        retentionCheckId = retentionCheckId,
        restrictionCodes = setOf("CHILD"),
        regex = regex
      )
    )

    verify(eventPublisher).send(
      OffenderRestrictionResult(
        offenderIdDisplay = offenderNumber1,
        retentionCheckId = retentionCheckId,
        restricted = true
      )
    )
  }

  @Test
  fun `check for offender restrictions no match`() {
    whenever(offenderRestrictionsRepository.findOffenderRestrictions(setOf(bookingId), setOf("CHILD"), regex))
      .thenReturn(emptyList())

    whenever(offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber1))
      .thenReturn(listOf(buildOffenderAliasPendingDeletion(offenderId1, offenderNumber1)))

    whenever(offenderSentConditionsRepository.findChildRelatedConditionsByBookings(listOf(bookingId)))
      .thenReturn(emptyList())

    service.checkForOffenderRestrictions(
      OffenderRestrictionRequest(
        offenderIdDisplay = offenderNumber1,
        retentionCheckId = retentionCheckId,
        restrictionCodes = setOf("CHILD"),
        regex = regex
      )
    )

    verify(eventPublisher).send(
      OffenderRestrictionResult(
        offenderIdDisplay = offenderNumber1,
        retentionCheckId = retentionCheckId,
        restricted = false
      )
    )
  }

  @Test
  fun `check for offender restrictions throws if offender not found`() {

    val offenderRestrictionRequest = OffenderRestrictionRequest(
      offenderIdDisplay = offenderNumber1,
      retentionCheckId = retentionCheckId,
      restrictionCodes = setOf("CHILD"),
      regex = regex
    )

    whenever(offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber1))
      .thenReturn(emptyList())

    assertThatThrownBy {
      service.checkForOffenderRestrictions(offenderRestrictionRequest)
    }
      .isInstanceOf(IllegalStateException::class.java)
      .hasMessage("Expecting to find at least one offender id for offender: 'A1234AA'")

    verifyNoInteractions(eventPublisher)
  }
}
