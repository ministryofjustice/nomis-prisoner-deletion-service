package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Pageable
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.config.DataComplianceProperties
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.DataComplianceEventPublisher
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderNoBookingDeletionResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.batchId
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.buildOffender
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.buildOffenderAliasPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.offenderId1
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.offenderId2
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.offenderNumber1
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.offenderNumber2
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.logging.DeletionEvent
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.logging.Event
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.OffenderDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.OffenderAliasPendingDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.OffenderNoBookingPendingDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderPendingDeletion
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class OffenderNoBookingDeletionServiceTest {

  private val offenderNoBookingPendingDeletionRepository = mock<OffenderNoBookingPendingDeletionRepository>()
  private val offenderAliasPendingDeletionRepository = mock<OffenderAliasPendingDeletionRepository>()
  private val offenderDeletionRepository = mock<OffenderDeletionRepository>()
  private val eventPublisher = mock<DataComplianceEventPublisher>()
  private val applicationEventPublisher = mock<ApplicationEventPublisher>()
  private val clock = Clock.fixed(Instant.ofEpochMilli(0), ZoneId.systemDefault())

  lateinit var service: OffenderNoBookingDeletionService

  @BeforeEach
  fun setup() {
    service = OffenderNoBookingDeletionService(
      offenderNoBookingPendingDeletionRepository,
      offenderAliasPendingDeletionRepository,
      offenderDeletionRepository,
      eventPublisher,
      applicationEventPublisher,
      DataComplianceProperties(
        deletionEnabled = false,
        deceasedDeletionEnabled = false,
        offenderNoBookingDeletionEnabled = true
      ),
      clock
    )
  }

  @Test
  fun `delete offenders with no bookings`() {

    whenever(
      offenderNoBookingPendingDeletionRepository.findOffendersWithNoBookingsDueForDeletion(Pageable.ofSize(2))
    ).thenReturn(
      listOf(
        OffenderPendingDeletion(offenderNumber1),
        OffenderPendingDeletion(offenderNumber2)
      )
    )

    whenever(offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber1))
      .thenReturn(
        listOf(
          buildOffenderAliasPendingDeletion(
            offenderId = offenderId1,
            offenderNumber = offenderNumber1,
            hasBooking = false
          )
        )
      )

    whenever(offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber2))
      .thenReturn(
        listOf(
          buildOffenderAliasPendingDeletion(
            offenderId = offenderId2,
            offenderNumber = offenderNumber2,
            hasBooking = false
          )
        )
      )

    whenever(
      offenderDeletionRepository.deleteAllOffenderDataExcludingBookings(offenderNumber1)
    ).thenReturn(setOf(offenderId1))

    whenever(offenderDeletionRepository.deleteAllOffenderDataExcludingBookings(offenderNumber2))
      .thenReturn(setOf(offenderId2))

    service.deleteOffendersWithNoBookings(batchId, Pageable.ofSize(2))

    verify(eventPublisher).send(
      OffenderNoBookingDeletionResult(
        batchId = batchId,
        offenders = listOf(
          buildOffender(offenderId1, offenderNumber1),
          buildOffender(offenderId2, offenderNumber2)
        )
      )
    )
    verify(applicationEventPublisher).publishEvent(
      DeletionEvent(
        Event.OFFENDER_NO_BOOKING_DELETION, setOf(offenderId1), offenderNumber1, batchId, LocalDateTime.now(clock)
      )
    )
    verify(applicationEventPublisher).publishEvent(
      DeletionEvent(
        Event.OFFENDER_NO_BOOKING_DELETION, setOf(offenderId2), offenderNumber2, batchId, LocalDateTime.now(clock)
      )
    )
  }

  @Test
  fun `delete offenders with no bookings excludes offenders with alias bookings`() {

    whenever(
      offenderNoBookingPendingDeletionRepository.findOffendersWithNoBookingsDueForDeletion(Pageable.ofSize(2))
    ).thenReturn(
      listOf(
        OffenderPendingDeletion(offenderNumber1),
        OffenderPendingDeletion(offenderNumber2)
      )
    )

    whenever(offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber1))
      .thenReturn(
        listOf(
          buildOffenderAliasPendingDeletion(
            offenderId = offenderId1,
            offenderNumber = offenderNumber1,
            hasBooking = false
          )
        )
      )

    whenever(offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber2))
      .thenReturn(
        listOf(
          buildOffenderAliasPendingDeletion(
            offenderId = offenderId2,
            offenderNumber = offenderNumber2,
            hasBooking = false
          ),
          buildOffenderAliasPendingDeletion(
            offenderId = offenderId2,
            offenderNumber = offenderNumber2,
            hasBooking = true
          )
        )
      )

    whenever(
      offenderDeletionRepository.deleteAllOffenderDataExcludingBookings(offenderNumber1)
    ).thenReturn(setOf(offenderId1))

    service.deleteOffendersWithNoBookings(batchId, Pageable.ofSize(2))

    verify(eventPublisher).send(
      OffenderNoBookingDeletionResult(
        batchId = batchId,
        offenders = listOf(
          buildOffender(offenderId1, offenderNumber1),
        )
      )
    )

    verifyNoMoreInteractions(eventPublisher)

    verify(applicationEventPublisher).publishEvent(
      DeletionEvent(
        Event.OFFENDER_NO_BOOKING_DELETION, setOf(offenderId1), offenderNumber1, batchId, LocalDateTime.now(clock)
      )
    )
    verifyNoMoreInteractions(applicationEventPublisher)
  }

  @Nested
  inner class NoBookingOffenderDeletionNotEnabled {

    @BeforeEach
    fun setup() {
      service = OffenderNoBookingDeletionService(
        offenderNoBookingPendingDeletionRepository,
        offenderAliasPendingDeletionRepository,
        offenderDeletionRepository,
        eventPublisher,
        applicationEventPublisher,
        DataComplianceProperties(
          deletionEnabled = true,
          deceasedDeletionEnabled = true,
          offenderNoBookingDeletionEnabled = false
        ),
        clock
      )
    }

    @Test
    fun `should throw when deletion for offenders with no bookings is disabled`() {
      Assertions.assertThatThrownBy {
        service.deleteOffendersWithNoBookings(
          batchId, Pageable.unpaged()
        )
      }
        .isInstanceOf(IllegalStateException::class.java)
        .hasMessage("Offender No bookings deletion is not enabled!")
    }
  }
}
