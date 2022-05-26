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
import org.mockito.kotlin.whenever
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.SQLWarningException
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.config.DataComplianceProperties
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.DataComplianceEventPublisher
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.DeceasedOffenderDeletionResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.batchId
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.buildDeceasedOffender
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.buildOffenderAliasPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.offenderId1
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.offenderId2
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.offenderNumber1
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.offenderNumber2
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.offendersLastMovement
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.logging.DeletionEvent
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.OffenderDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.connection.AppModuleName
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.DeceasedOffenderPendingDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.OffenderAliasPendingDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderPendingDeletion
import java.sql.SQLWarning
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

internal class DeceasedOffenderDeletionServiceTest {

  private val offenderAliasPendingDeletionRepository = mock<OffenderAliasPendingDeletionRepository>()
  private val offenderDeletionRepository = mock<OffenderDeletionRepository>()
  private val eventPublisher = mock<DataComplianceEventPublisher>()
  private val applicationEventPublisher = mock<ApplicationEventPublisher>()
  private val movementsService = mock<MovementsService>()
  private val deceasedOffenderPendingDeletionRepository = mock<DeceasedOffenderPendingDeletionRepository>()
  private val clock = Clock.fixed(Instant.ofEpochMilli(0), ZoneId.systemDefault())

  lateinit var service: DeceasedOffenderDeletionService

  @BeforeEach
  fun setUp() {
    service = DeceasedOffenderDeletionService(
      offenderAliasPendingDeletionRepository,
      offenderDeletionRepository,
      eventPublisher,
      applicationEventPublisher,
      movementsService,
      deceasedOffenderPendingDeletionRepository,
      DataComplianceProperties(
        deletionEnabled = false,
        deceasedDeletionEnabled = true,
        offenderNoBookingDeletionEnabled = false
      ),
      clock
    )
  }

  @Test
  fun `delete deceased offenders`() {

    whenever(
      deceasedOffenderPendingDeletionRepository.findDeceasedOffendersDueForDeletion(
        LocalDate.now(clock),
        Pageable.ofSize(2)
      )
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

    whenever(
      movementsService.getDeceasedMovementByOffenders(listOf(offenderNumber1))
    ).thenReturn(offendersLastMovement(offenderNumber1, clock))

    whenever(movementsService.getDeceasedMovementByOffenders(listOf(offenderNumber2))).thenReturn(
      offendersLastMovement(offenderNumber2, clock)
    )

    service.deleteDeceasedOffenders(batchId, Pageable.ofSize(2))

    val orderVerifier: InOrder = inOrder(offenderDeletionRepository)
    orderVerifier.verify(offenderDeletionRepository).setContext(AppModuleName.MERGE)
    orderVerifier.verify(offenderDeletionRepository).setContext(AppModuleName.NOMIS_DELETION_SERVICE)
    verify(eventPublisher).send(
      DeceasedOffenderDeletionResult(
        batchId = batchId,
        deceasedOffenders = listOf(
          buildDeceasedOffender(offenderId1, offenderNumber1, true, clock),
          buildDeceasedOffender(offenderId2, offenderNumber2, true, clock)
        )
      )
    )
    verify(applicationEventPublisher).publishEvent(
      DeletionEvent(
        "DeceasedOffenderDelete", setOf(offenderId1), offenderNumber1
      )
    )
    verify(applicationEventPublisher).publishEvent(
      DeletionEvent(
        "DeceasedOffenderDelete", setOf(offenderId2), offenderNumber2
      )
    )
  }

  @Test
  fun `delete deceased offenders when no movements are found`() {

    whenever(
      deceasedOffenderPendingDeletionRepository.findDeceasedOffendersDueForDeletion(
        LocalDate.now(clock),
        Pageable.ofSize(2)
      )
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

    whenever(
      movementsService.getDeceasedMovementByOffenders(listOf(offenderNumber1))
    ).thenReturn(null)

    whenever(movementsService.getDeceasedMovementByOffenders(listOf(offenderNumber2)))
      .thenReturn(null)

    service.deleteDeceasedOffenders(batchId, Pageable.ofSize(2))

    val orderVerifier: InOrder = inOrder(offenderDeletionRepository)
    orderVerifier.verify(offenderDeletionRepository).setContext(AppModuleName.MERGE)
    orderVerifier.verify(offenderDeletionRepository).setContext(AppModuleName.NOMIS_DELETION_SERVICE)

    verify(eventPublisher).send(
      DeceasedOffenderDeletionResult(
        batchId = batchId,
        deceasedOffenders = listOf(
          buildDeceasedOffender(offenderId1, offenderNumber1, false, clock),
          buildDeceasedOffender(offenderId2, offenderNumber2, false, clock)
        )
      )
    )
    verify(applicationEventPublisher).publishEvent(
      DeletionEvent(
        "DeceasedOffenderDelete", setOf(offenderId1), offenderNumber1
      )
    )
    verify(applicationEventPublisher).publishEvent(
      DeletionEvent(
        "DeceasedOffenderDelete", setOf(offenderId2), offenderNumber2
      )
    )
  }

  @Test
  fun `delete deceased offender throws when unable to update context`() {
    doThrow(SQLWarningException("Some Exception", SQLWarning("SQL warning")))
      .whenever(offenderDeletionRepository).setContext(AppModuleName.MERGE)
    assertThatThrownBy {
      service.deleteDeceasedOffenders(
        batchId,
        Pageable.unpaged()
      )
    }.isInstanceOf(SQLWarningException::class.java)
  }

  @Test
  fun `delete deceased offender throws when offender aliases not found`() {

    whenever(
      deceasedOffenderPendingDeletionRepository.findDeceasedOffendersDueForDeletion(
        LocalDate.now(clock),
        Pageable.ofSize(2)
      )
    ).thenReturn(listOf(OffenderPendingDeletion(offenderNumber1)))

    whenever(offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber1))
      .thenReturn(emptyList())

    assertThatThrownBy {
      service.deleteDeceasedOffenders(
        batchId,
        Pageable.ofSize(2)
      )
    }
      .isInstanceOf(java.lang.IllegalStateException::class.java)
      .hasMessageContaining("Offender not found: 'A1234AA'")
  }

  @Nested
  inner class DeceasedDeletionNotEnabled {
    @BeforeEach
    fun setUp() {
      service = DeceasedOffenderDeletionService(
        offenderAliasPendingDeletionRepository,
        offenderDeletionRepository,
        eventPublisher,
        applicationEventPublisher,
        movementsService,
        deceasedOffenderPendingDeletionRepository,
        DataComplianceProperties(
          deletionEnabled = false,
          deceasedDeletionEnabled = false,
          offenderNoBookingDeletionEnabled = false
        ),
        clock
      )
    }

    @Test
    fun `delete deceased offenders throws when deletion is not enabled`() {

      assertThatThrownBy {
        service.deleteDeceasedOffenders(
          batchId,
          Pageable.unpaged()
        )
      }
        .isInstanceOf(IllegalStateException::class.java)
        .hasMessage("Deceased deletion is not enabled!")
    }
  }
}
