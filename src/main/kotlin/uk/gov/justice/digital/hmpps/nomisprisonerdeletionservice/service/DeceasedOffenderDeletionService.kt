package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service

import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.config.DataComplianceProperties
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.DataComplianceEventPublisher
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.DeceasedOffenderDeletionResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.DeceasedOffenderDeletionResult.DeceasedOffender
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.DeceasedOffenderDeletionResult.OffenderAlias
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.logging.DeletionEvent
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.OffenderDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.connection.AppModuleName
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.DeceasedOffenderPendingDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.OffenderAliasPendingDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.Movement
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderAliasPendingDeletion
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime

@Service
@Transactional
class DeceasedOffenderDeletionService(
  val offenderAliasPendingDeletionRepository: OffenderAliasPendingDeletionRepository,

  val offenderDeletionRepository: OffenderDeletionRepository,

  val eventPublisher: DataComplianceEventPublisher,

  val applicationEventPublisher: ApplicationEventPublisher,

  val movementsService: MovementsService,

  val deceasedOffenderPendingDeletionRepository: DeceasedOffenderPendingDeletionRepository,

  val properties: DataComplianceProperties,

  val clock: Clock
) {

  fun deleteDeceasedOffenders(batchId: Long, pageable: Pageable) {
    check(properties.deceasedDeletionEnabled) { "Deceased deletion is not enabled!" }

    var deceasedOffenders: MutableList<DeceasedOffender> = mutableListOf()

    offenderDeletionRepository.setContext(AppModuleName.MERGE)

    deceasedOffenderPendingDeletionRepository.findDeceasedOffendersDueForDeletion(LocalDate.now(clock), pageable)
      .forEach {
        val offenderNumber = it.offenderNumber
        val offenderAliases: List<OffenderAliasPendingDeletion> = getOffenderAliases(offenderNumber)
        val rootOffenderAlias: OffenderAliasPendingDeletion = getRootOffender(offenderNumber, offenderAliases)

        val deceasedMovement = movementsService.getDeceasedMovementByOffenders(listOf(offenderNumber))
        val offenderIds = offenderDeletionRepository.deleteAllOffenderDataIncludingBaseRecord(offenderNumber)

        deceasedOffenders.add(buildDeceasedOffender(offenderNumber, rootOffenderAlias, offenderAliases, deceasedMovement))
        applicationEventPublisher.publishEvent(DeletionEvent("DeceasedOffenderDelete", offenderIds, offenderNumber))
      }

    offenderDeletionRepository.setContext(AppModuleName.NOMIS_DELETION_SERVICE)
    eventPublisher.send(DeceasedOffenderDeletionResult(batchId, deceasedOffenders))
  }

  private fun getOffenderAliases(offenderNumber: kotlin.String): List<OffenderAliasPendingDeletion> {
    val offenderAliases =
      offenderAliasPendingDeletionRepository.findOffenderAliasPendingDeletionByOffenderNumber(offenderNumber)
    check(offenderAliases.isNotEmpty()) { "Offender not found: '$offenderNumber'" }
    return offenderAliases
  }

  private fun getRootOffender(
    offenderNumber: String,
    offenderAliases: List<OffenderAliasPendingDeletion>
  ): OffenderAliasPendingDeletion {
    val rootOffender: OffenderAliasPendingDeletion? = offenderAliases.find { it.offenderId == it.rootOffenderId }
    requireNotNull { "Cannot find root offender alias for '$offenderNumber'" }
    return rootOffender!!
  }

  private fun buildDeceasedOffender(
    offenderNumber: String,
    rootAlias: OffenderAliasPendingDeletion,
    offenderAliases: List<OffenderAliasPendingDeletion>,
    deceasedMovement: Movement?
  ): DeceasedOffender {

    return DeceasedOffender(
      offenderIdDisplay = offenderNumber,
      firstName = rootAlias.firstName,
      middleName = rootAlias.middleName,
      lastName = rootAlias.lastName,
      birthDate = rootAlias.birthDate,
      deletionDateTime = LocalDateTime.now(clock),
      offenderAliases = offenderAliases.map { alias ->
        OffenderAlias(
          offenderId = alias.offenderId,
          offenderBookIds = alias.offenderBookings.map { it.bookingId }
        )
      },
      deceasedDate = deceasedMovement?.movementDate,
      agencyLocationId = deceasedMovement?.fromAgency
    )
  }
}
