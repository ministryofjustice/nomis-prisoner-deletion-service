package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.MovementsRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.Movement

@Service
@Validated
@Transactional(readOnly = true)
class MovementsService(
  val movementsRepository: MovementsRepository
) {

  fun getMovementsByOffenders(
    offenderNumbers: List<String>,
    movementTypes: List<String>?,
    latestOnly: Boolean,
    allBookings: Boolean
  ): List<Movement> {
    return movementsRepository.getMovementsByOffenders(offenderNumbers, movementTypes, latestOnly, allBookings)
      .map { it.formattedCopy() }
  }

  fun getDeceasedMovementByOffenders(
    offenderNumbers: List<String>,
  ): Movement? {
    return movementsRepository.getDeceasedMovementByOffenders(offenderNumbers)?.formattedCopy()
  }
}
