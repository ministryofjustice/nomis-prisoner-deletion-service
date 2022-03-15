package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service

import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.config.DataComplianceProperties
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.DataComplianceEventPublisher
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderNoBookingDeletionResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderNoBookingDeletionResult.Offender
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderNoBookingDeletionResult.OffenderAlias
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.logging.DeletionEvent
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.OffenderDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.OffenderAliasPendingDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.OffenderNoBookingPendingDeletionRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderAliasPendingDeletion

@Service
@Transactional
class OffenderNoBookingDeletionService(
  val offenderNoBookingPendingDeletionRepository: OffenderNoBookingPendingDeletionRepository,

  val offenderAliasPendingDeletionRepository: OffenderAliasPendingDeletionRepository,

  val offenderDeletionRepository: OffenderDeletionRepository,

  val eventPublisher: DataComplianceEventPublisher,

  val applicationEventPublisher: ApplicationEventPublisher,

  val properties: DataComplianceProperties,
) {

  fun deleteOffendersWithNoBookings(batchId: Long, pageable: Pageable) {

    check(properties.offenderNoBookingDeletionEnabled) { "Offender No bookings deletion is not enabled!" }

    var offenders: MutableList<Offender> = mutableListOf()

    offenderNoBookingPendingDeletionRepository.findOffendersWithNoBookingsDueForDeletion(pageable)
      .forEach {

        val offenderNumber = it.offenderNumber
        val offenderAliases: List<OffenderAliasPendingDeletion> = getOffenderAliases(offenderNumber)
        val rootOffenderAlias: OffenderAliasPendingDeletion = getRootOffender(offenderNumber, offenderAliases)

        val offenderIds = offenderDeletionRepository.deleteAllOffenderDataExcludingBookings(offenderNumber)

        offenders.add(buildOffender(offenderNumber, rootOffenderAlias, offenderAliases))
        applicationEventPublisher.publishEvent(DeletionEvent("OffenderNoBookingDelete", offenderIds, offenderNumber))
      }

    eventPublisher.send(OffenderNoBookingDeletionResult(batchId, offenders))
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

  private fun buildOffender(
    offenderNumber: String,
    rootAlias: OffenderAliasPendingDeletion,
    offenderAliases: List<OffenderAliasPendingDeletion>
  ): Offender {

    return Offender(
      offenderIdDisplay = offenderNumber,
      firstName = rootAlias.firstName,
      middleName = rootAlias.middleName,
      lastName = rootAlias.lastName,
      birthDate = rootAlias.birthDate,
      offenderAliases = offenderAliases.map { alias ->
        OffenderAlias(
          offenderId = alias.offenderId,
        )
      },
    )
  }
}
