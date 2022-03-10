package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service

import com.microsoft.applicationinsights.TelemetryClient
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
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
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.OffenderDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.OffenderAliasPendingDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.OffenderNoBookingPendingDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderPendingDeletion

class OffenderNoBookingDeletionServiceTest {

  private val offenderNoBookingPendingDeletionRepository = mock<OffenderNoBookingPendingDeletionRepository>()
  private val offenderAliasPendingDeletionRepository = mock<OffenderAliasPendingDeletionRepository>()
  private val offenderDeletionRepository = mock<OffenderDeletionRepository>()
  private val eventPublisher = mock<DataComplianceEventPublisher>()
  private val telemetryClient = mock<TelemetryClient>()

  lateinit var service: OffenderNoBookingDeletionService

  @BeforeEach
  fun setup() {
    service = OffenderNoBookingDeletionService(
      offenderNoBookingPendingDeletionRepository,
      offenderAliasPendingDeletionRepository,
      offenderDeletionRepository,
      eventPublisher,
      telemetryClient,
      DataComplianceProperties(
        deletionEnabled = false,
        deceasedDeletionEnabled = false,
        offenderNoBookingEnabled = true
      )
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
      .thenReturn(listOf(buildOffenderAliasPendingDeletion(offenderId1, offenderNumber1)))

    whenever(offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber2))
      .thenReturn(listOf(buildOffenderAliasPendingDeletion(offenderId2, offenderNumber2)))

    whenever(
      offenderDeletionRepository.deleteAllOffenderDataIncludingBaseRecord(offenderNumber1)
    ).thenReturn(setOf(offenderId1))

    whenever(offenderDeletionRepository.deleteAllOffenderDataIncludingBaseRecord(offenderNumber2))
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
    verify(telemetryClient).trackEvent(
      "OffenderNoBookingDelete",
      mapOf(
        "offenderNo" to offenderNumber1,
        "count" to "0"
      ),
      null
    )
    verify(telemetryClient).trackEvent(
      "OffenderNoBookingDelete",
      mapOf(
        "offenderNo" to offenderNumber2,
        "count" to "0"
      ),
      null
    )
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
        telemetryClient,
        DataComplianceProperties(
          deletionEnabled = true,
          deceasedDeletionEnabled = true,
          offenderNoBookingEnabled = false
        )
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
