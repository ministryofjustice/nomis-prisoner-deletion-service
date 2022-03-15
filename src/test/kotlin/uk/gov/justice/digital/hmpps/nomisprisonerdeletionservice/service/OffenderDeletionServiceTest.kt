package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.InOrder
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import org.springframework.context.ApplicationEventPublisher
import org.springframework.jdbc.SQLWarningException
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.config.DataComplianceProperties
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.DataComplianceEventPublisher
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderDeletionComplete
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.bookingId
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.buildOffenderAliasPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.offenderId1
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.offenderNumber1
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.referralId
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.logging.DeletionEvent
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.OffenderDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.connection.AppModuleName
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.OffenderAliasPendingDeletionRepository
import java.sql.SQLWarning

internal class OffenderDeletionServiceTest {

  private val offenderAliasPendingDeletionRepository = mock<OffenderAliasPendingDeletionRepository>()
  private val offenderDeletionRepository = mock<OffenderDeletionRepository>()
  private val eventPublisher = mock<DataComplianceEventPublisher>()
  private val applicationEventPublisher = mock<ApplicationEventPublisher>()

  lateinit var service: OffenderDeletionService

  @BeforeEach
  fun setUp() {
    service = OffenderDeletionService(
      DataComplianceProperties(
        deletionEnabled = true,
        deceasedDeletionEnabled = false,
        offenderNoBookingDeletionEnabled = false
      ),
      offenderAliasPendingDeletionRepository,
      offenderDeletionRepository,
      eventPublisher,
      applicationEventPublisher,
    )
  }

  @Test
  fun deleteOffender() {
    mockOffenderIds()

    whenever(offenderDeletionRepository.cleanseOffenderDataExcludingBaseRecord(offenderNumber1)).thenReturn(
      setOf(
        offenderId1
      )
    )

    service.deleteOffender(
      OffenderDeletionService.OffenderDeletionGrant(
        offenderNo = offenderNumber1,
        referralId = referralId,
        offenderIds = setOf(offenderId1),
        offenderBookIds = setOf(bookingId)
      )
    )

    val orderVerifier: InOrder = inOrder(offenderDeletionRepository)
    orderVerifier.verify(offenderDeletionRepository).setContext(AppModuleName.MERGE)
    orderVerifier.verify(offenderDeletionRepository).setContext(AppModuleName.NOMIS_DELETION_SERVICE)

    verify(eventPublisher).send(OffenderDeletionComplete(offenderNumber1, referralId))
    verify(applicationEventPublisher).publishEvent(
      DeletionEvent(
        "OffenderDelete", setOf(offenderId1), offenderNumber1
      )
    )
  }

  @Test
  fun `delete offender throws if offender ids do not match`() {
    mockOffenderIds()

    assertThatThrownBy {
      service.deleteOffender(
        OffenderDeletionService.OffenderDeletionGrant(
          offenderNo = offenderNumber1,
          referralId = referralId,
          offenderIds = setOf(999L),
          offenderBookIds = setOf(bookingId)
        )
      )
    }
      .isInstanceOf(java.lang.IllegalStateException::class.java)
      .hasMessage("The requested offender ids ([999]) do not match those currently linked to offender 'A1234AA' ([2])")

    verifyNoInteractions(offenderDeletionRepository)
  }

  @Test
  fun `delete offender throws if offender book ids do not match`() {
    mockOffenderIds()

    assertThatThrownBy {
      service.deleteOffender(
        OffenderDeletionService.OffenderDeletionGrant(
          offenderNo = offenderNumber1,
          referralId = referralId,
          offenderIds = setOf(offenderId1),
          offenderBookIds = setOf(999L)
        )
      )
    }
      .isInstanceOf(java.lang.IllegalStateException::class.java)
      .hasMessage("The requested offender book ids ([999]) do not match those currently linked to offender 'A1234AA' ([2])")

    verifyNoInteractions(offenderDeletionRepository)
  }

  @Nested
  inner class DeletionNotEnabled() {
    @BeforeEach
    fun setUp() {
      service = OffenderDeletionService(
        DataComplianceProperties(
          deletionEnabled = false,
          deceasedDeletionEnabled = false,
          offenderNoBookingDeletionEnabled = false
        ),
        offenderAliasPendingDeletionRepository,
        offenderDeletionRepository,
        eventPublisher,
        applicationEventPublisher,
      )
    }

    @Test
    fun `delete offender throws if deletion not enabled`() {
      mockOffenderIds()

      assertThatThrownBy {
        service.deleteOffender(
          OffenderDeletionService.OffenderDeletionGrant(
            offenderNo = offenderNumber1,
            referralId = referralId,
            offenderIds = setOf(offenderId1),
            offenderBookIds = setOf(bookingId)
          )
        )
      }
        .isInstanceOf(java.lang.IllegalStateException::class.java)
        .hasMessage("Unable to delete offender. Deletion is not enabled!")

      verifyNoInteractions(offenderDeletionRepository)
    }
  }

  @Test
  fun `delete offender throws if unable to update context`() {
    mockOffenderIds()

    whenever(offenderDeletionRepository.setContext(AppModuleName.MERGE))
      .doThrow(SQLWarningException("Some Exception", SQLWarning("SQL warning")))

    assertThatThrownBy {
      service.deleteOffender(
        OffenderDeletionService.OffenderDeletionGrant(
          offenderNo = offenderNumber1,
          referralId = referralId,
          offenderIds = setOf(offenderId1),
          offenderBookIds = setOf(bookingId)
        )
      )
    }
      .isInstanceOf(SQLWarningException::class.java)
  }

  private fun mockOffenderIds() {
    whenever(offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber1))
      .thenReturn(listOf(buildOffenderAliasPendingDeletion(offenderId1, offenderNumber1)))
  }
}
