package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service

import com.microsoft.applicationinsights.TelemetryClient
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.config.DataComplianceProperties
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.DataComplianceEventPublisher
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.DeceasedOffenderDeletionResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.DeceasedOffenderDeletionResult.DeceasedOffender
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.DeceasedOffenderDeletionResult.OffenderAlias
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

  val telemetryClient: TelemetryClient,

  val movementsService: MovementsService,

  val deceasedOffenderPendingDeletionRepository: DeceasedOffenderPendingDeletionRepository,

  val properties: DataComplianceProperties,

  val clock: Clock
) {

  fun deleteDeceasedOffenders(batchId: Long, pageable: Pageable) {
    check(properties.deceasedDeletionEnabled) { "Deceased deletion is not enabled!" }

    val telemetryLog = arrayListOf<MutableMap<String, String>>()
    var deceasedOffenders: MutableList<DeceasedOffender> = mutableListOf()

    offenderDeletionRepository.setContext(AppModuleName.MERGE)

    deceasedOffenderPendingDeletionRepository.findDeceasedOffendersDueForDeletion(LocalDate.now(clock), pageable)
      .forEach {
        val offenderNumber = it.offenderNumber
        val offenderAliases: List<OffenderAliasPendingDeletion> = getOffenderAliases(offenderNumber)
        val rootOffenderAlias: OffenderAliasPendingDeletion = getRootOffender(offenderNumber, offenderAliases)

        val offenderIds = offenderDeletionRepository.deleteAllOffenderDataIncludingBaseRecord(offenderNumber)

        deceasedOffenders.add(buildDeceasedOffender(offenderNumber, rootOffenderAlias, offenderAliases))
        addTelemetryLog(telemetryLog, offenderNumber, offenderIds)
      }

    offenderDeletionRepository.setContext(AppModuleName.NOMIS_DELETION_SERVICE)
    eventPublisher.send(DeceasedOffenderDeletionResult(batchId, deceasedOffenders))
    telemetryLog.forEach { telemetryClient.trackEvent("DeceasedOffenderDelete", it, null) }
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

  private fun getDeceasedMovement(offenderNumber: String): Movement? {
    return movementsService.getMovementsByOffenders(
      listOf(offenderNumber),
      listOf("DEC"),
      latestOnly = true,
      allBookings = true
    ).firstOrNull()
  }

  private fun addTelemetryLog(
    telemetryLog: ArrayList<MutableMap<String, String>>,
    offenderNumber: String,
    offenderIds: Set<Long>?
  ) {
    telemetryLog.add(
      mutableMapOf(
        "offenderNo" to offenderNumber,
        "count" to offenderIds?.size.toString()
      )
    )
  }

  private fun buildDeceasedOffender(
    offenderNumber: String,
    rootAlias: OffenderAliasPendingDeletion,
    offenderAliases: List<OffenderAliasPendingDeletion>
  ): DeceasedOffender {

    val deceasedMovement = getDeceasedMovement(offenderNumber)
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
