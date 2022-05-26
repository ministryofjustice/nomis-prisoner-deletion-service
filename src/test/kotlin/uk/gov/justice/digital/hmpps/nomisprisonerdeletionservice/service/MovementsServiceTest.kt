package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.MovementsRepository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.Movement

class MovementsServiceTest {

  private val offenderNumber = "AA1234A"

  private val offenderNumbers = listOf(offenderNumber)

  @Test
  fun `can get movements by offenders`() {
    val movements: List<Movement> = listOf(
      Movement(
        offenderNo = offenderNumber,
        fromAgencyDescription = "LEEDS",
        toAgencyDescription = "BLACKBURN"
      )
    )

    val movementsRepository: MovementsRepository = mock {
      on {
        getMovementsByOffenders(offenderNumbers, null, true, false)
      } doReturn movements
    }

    val movementsService = MovementsService(movementsRepository)
    val processedMovements = movementsService.getMovementsByOffenders(offenderNumbers, null, true, false)
    assertThat(processedMovements).extracting("toAgencyDescription").containsExactly("Blackburn")
    assertThat(processedMovements).extracting("fromAgencyDescription").containsExactly("Leeds")
  }

  @Test
  fun `can get movements by offenders with Null descriptions`() {
    val movements = listOf(Movement(offenderNo = offenderNumber))

    val offenderNoList = listOf(offenderNumber)

    val movementsRepository: MovementsRepository = mock {
      on {
        getMovementsByOffenders(offenderNumbers, null, true, false)
      } doReturn movements
    }

    val movementsService = MovementsService(movementsRepository)
    val processedMovements = movementsService.getMovementsByOffenders(offenderNoList, null, true, false)

    assertThat(processedMovements).hasSize(1)
    assertThat(processedMovements[0].fromAgencyDescription).isEmpty()
  }

  @Test
  fun `can get deceased movement`() {
    val movement =
      Movement(
        offenderNo = offenderNumber,
        fromAgencyDescription = "LEEDS",
        toAgencyDescription = "BLACKBURN"
      )

    val movementsRepository: MovementsRepository = mock {
      on {
        getDeceasedMovementByOffenders(offenderNumbers)
      } doReturn movement
    }

    val movementsService = MovementsService(movementsRepository)
    val processedMovements = movementsService.getDeceasedMovementByOffenders(offenderNumbers)
    assertThat(processedMovements?.toAgencyDescription).isEqualTo("Blackburn")
    assertThat(processedMovements?.fromAgencyDescription).isEqualTo("Leeds")
  }
}
