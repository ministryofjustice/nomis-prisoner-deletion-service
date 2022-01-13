package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.Movement

class MovementsRepositoryTest : IntegrationTestBase() {

  @Autowired
  lateinit var repository: MovementsRepository

  @Test
  fun canRetrieveMovementsByOffendersAndMovementTypes() {
    val movements: List<Movement> = repository.getMovementsByOffenders(listOf("A6676RS"), listOf("TRN"), false, false)
    assertThat(movements).extracting<String>(Movement::toAgency).containsOnly("BMI", "MDI")
  }

  @Test
  fun canRetrieveRecentMovementsByOffenders() {
    val movements: List<Movement> = repository.getMovementsByOffenders(listOf("A6676RS"), listOf(), true, false)
    assertThat(movements).extracting<String>(Movement::toCity).containsExactly("Wadhurst")
  }

  @Test
  fun canRetrieveMovementsByOffenders() {
    val movements: List<Movement> = repository.getMovementsByOffenders(listOf("A6676RS"), listOf(), false, false)
    assertThat(movements).extracting<String>(Movement::fromAgency).containsOnly("BMI", "LEI")
  }
}
