package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageImpl
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.DataComplianceEventPublisher
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.ProvisionalDeletionReferralResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.batchId
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.buildOffenderAliasPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.expectedPendingDeletionEvent
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.expectedProvisionalDeletionReferralResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.expectedReferralCompleteEvent
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.offenderId1
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.offenderNumber1
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.offenderNumber2
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.offendersLastMovement
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.pageRequest
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.referralId
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.totalInWindow
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.windowEnd
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.windowStart
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.OffenderAliasPendingDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.OffenderPendingDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderAliasPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderPendingDeletion
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

internal class ReferralServiceTest {

  private val offenderPendingDeletionRepository = mock<OffenderPendingDeletionRepository>()
  private val offenderAliasPendingDeletionRepository = mock<OffenderAliasPendingDeletionRepository>()
  private val movementsService = mock<MovementsService>()
  private val eventPusher = mock<DataComplianceEventPublisher>()
  private val clock = Clock.fixed(Instant.ofEpochMilli(0), ZoneId.systemDefault())

  private lateinit var service: ReferralService

  @BeforeEach
  fun setUp() {
    service = ReferralService(
      offenderPendingDeletionRepository = offenderPendingDeletionRepository,
      offenderAliasPendingDeletionRepository = offenderAliasPendingDeletionRepository,
      dataComplianceEventPusher = eventPusher,
      movementsService = movementsService,
      clock = clock
    )
  }

  @Test
  fun `accept offenders pending deletion`() {

    whenever(offenderPendingDeletionRepository.getOffendersDueForDeletionBetween(windowStart, windowEnd, pageRequest))
      .thenReturn(
        PageImpl(
          listOf(OffenderPendingDeletion(offenderNumber1), OffenderPendingDeletion(offenderNumber2)),
          pageRequest,
          totalInWindow
        )
      )

    whenever(offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber1))
      .thenReturn(listOf(buildOffenderAliasPendingDeletion(1, offenderNumber1)))

    whenever(offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber2))
      .thenReturn(listOf(buildOffenderAliasPendingDeletion(2, offenderNumber2)))

    whenever(
      movementsService.getMovementsByOffenders(listOf(offenderNumber1), listOf("REL"), true, true)
    ).thenReturn(listOf(offendersLastMovement(offenderNumber1)))

    whenever(movementsService.getMovementsByOffenders(listOf(offenderNumber2), listOf("REL"), true, true)).thenReturn(
      listOf(offendersLastMovement(offenderNumber2))
    )

    service.referOffendersForDeletion(batchId, windowStart, windowEnd, pageRequest)

    verify(eventPusher).send(expectedPendingDeletionEvent(1L, offenderNumber1, true))
    verify(eventPusher).send(expectedPendingDeletionEvent(2L, offenderNumber2, true))
    verify(eventPusher).send(expectedReferralCompleteEvent(batchId, 2L, totalInWindow))
    verifyNoMoreInteractions(eventPusher)
  }

  @Test
  fun `accept offenders pending deletion when no movement is found`() {

    whenever(offenderPendingDeletionRepository.getOffendersDueForDeletionBetween(windowStart, windowEnd, pageRequest))
      .thenReturn(PageImpl(listOf(OffenderPendingDeletion(offenderNumber1)), pageRequest, totalInWindow))

    whenever(offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber1))
      .thenReturn(listOf(buildOffenderAliasPendingDeletion(1, offenderNumber1)))

    whenever(
      movementsService.getMovementsByOffenders(listOf(offenderNumber1), listOf("REL"), true, true)
    ).thenReturn(emptyList())

    service.referOffendersForDeletion(batchId, windowStart, windowEnd, pageRequest)

    verify(eventPusher).send(expectedPendingDeletionEvent(1L, offenderNumber1, false))
    verify(eventPusher).send(expectedReferralCompleteEvent(batchId, 1L, totalInWindow))
    verifyNoMoreInteractions(eventPusher)
  }

  @Test
  fun `accept offenders pending deletion throws if offender aliases not found`() {
    whenever(offenderPendingDeletionRepository.getOffendersDueForDeletionBetween(windowStart, windowEnd, pageRequest))
      .thenReturn(PageImpl(listOf(OffenderPendingDeletion(offenderNumber1)), pageRequest, totalInWindow))

    whenever(offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber1))
      .thenReturn(emptyList())

    assertThatThrownBy { service.referOffendersForDeletion(batchId, windowStart, windowEnd, pageRequest) }
      .isInstanceOf(IllegalStateException::class.java)
      .hasMessageContaining("Offender not found: 'A1234AA'")
  }

  @Test
  fun `accept offenders pending deletion throws if no root offender alias found`() {
    whenever(offenderPendingDeletionRepository.getOffendersDueForDeletionBetween(windowStart, windowEnd, pageRequest))
      .thenReturn(PageImpl(listOf(OffenderPendingDeletion(offenderNumber1)), pageRequest, totalInWindow))

    whenever(offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber1))
      .thenReturn(listOf(OffenderAliasPendingDeletion(offenderId = 1L, rootOffenderId = 2L)))

    assertThatThrownBy { service.referOffendersForDeletion(batchId, windowStart, windowEnd, pageRequest) }
      .isInstanceOf(java.lang.IllegalStateException::class.java)
      .hasMessageContaining("Cannot find root offender alias for 'A1234AA'")
  }

  @Test
  fun `refer ad hoc offender deletion`() {
    whenever(offenderPendingDeletionRepository.findOffenderPendingDeletion(offenderNumber1, LocalDate.now(clock)))
      .thenReturn(OffenderPendingDeletion(offenderNumber1))

    whenever(offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber1))
      .thenReturn(listOf(buildOffenderAliasPendingDeletion(offenderId1, offenderNumber1)))

    whenever(
      movementsService.getMovementsByOffenders(listOf(offenderNumber1), listOf("REL"), true, true)
    ).thenReturn(listOf(offendersLastMovement(offenderNumber1)))

    service.referAdHocOffenderDeletion(offenderNumber1, batchId)

    verify(eventPusher).send(expectedPendingDeletionEvent(offenderId1, offenderNumber1))
    verifyNoMoreInteractions(eventPusher)
  }

  @Test
  fun `refer ad hoc offender deletion throws if offender pending deletion is not found`() {
    whenever(offenderPendingDeletionRepository.findOffenderPendingDeletion(offenderNumber1, LocalDate.now(clock)))
      .thenReturn(null)

    whenever(offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber1))
      .thenReturn(listOf(buildOffenderAliasPendingDeletion(offenderId1, offenderNumber1)))

    assertThatThrownBy {
      service.referAdHocOffenderDeletion(offenderNumber1, batchId)
    }
      .isInstanceOf(java.lang.IllegalStateException::class.java)
      .hasMessage("Unable to find offender that qualifies for deletion with number: 'A1234AA'")

    verifyNoInteractions(eventPusher)
  }

  @Test
  fun `refer provisional deletion`() {
    whenever(offenderPendingDeletionRepository.findOffenderPendingDeletion(offenderNumber1, LocalDate.now(clock)))
      .thenReturn((OffenderPendingDeletion(offenderNumber1)))

    whenever(offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber1))
      .thenReturn(listOf(buildOffenderAliasPendingDeletion(offenderId1, offenderNumber1)))

    whenever(
      movementsService.getMovementsByOffenders(listOf(offenderNumber1), listOf("REL"), true, true)
    ).thenReturn(listOf(offendersLastMovement(offenderNumber1)))

    service.referProvisionalDeletion(offenderNumber1, referralId)

    verify(eventPusher).send(expectedProvisionalDeletionReferralResult(offenderId1, offenderNumber1, referralId, false))
    verifyNoMoreInteractions(eventPusher)
  }

  @Test
  fun `refer provisional deletion subsequent changes identified`() {
    whenever(
      offenderPendingDeletionRepository.findOffenderPendingDeletion(offenderNumber1, LocalDate.now(clock))
    )
      .thenReturn(null)

    service.referProvisionalDeletion(offenderNumber1, referralId)

    verify(eventPusher).send(ProvisionalDeletionReferralResult(referralId, offenderNumber1, true))
    verifyNoMoreInteractions(eventPusher)
  }
}
