package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.Movement

@ActiveProfiles("test")
@DataJdbcTest
class MovementsRepositoryTest {

  @Autowired
  lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

  lateinit var repository: MovementsRepository

  @BeforeEach
  fun setUp() {
    repository = MovementsRepository(namedParameterJdbcTemplate)
  }

  @Test
  fun `can retrieve movements by offenders and movement types`() {
    val movements: List<Movement> = repository.getMovementsByOffenders(listOf("A6676RS"), listOf("TRN"), false, false)
    assertThat(movements).extracting<String>(Movement::toAgency).containsOnly("BMI", "MDI")
  }

  @Test
  fun `can retrieve recent movements by offenders`() {
    val movements: List<Movement> = repository.getMovementsByOffenders(listOf("A6676RS"), listOf(), true, false)
    assertThat(movements).extracting<String>(Movement::toCity).containsExactly("Wadhurst")
  }

  @Test
  fun `can retrieve movements by offenders`() {
    val movements: List<Movement> = repository.getMovementsByOffenders(listOf("A6676RS"), listOf(), false, false)
    assertThat(movements).extracting<String>(Movement::fromAgency).containsOnly("BMI", "LEI")
  }
}
