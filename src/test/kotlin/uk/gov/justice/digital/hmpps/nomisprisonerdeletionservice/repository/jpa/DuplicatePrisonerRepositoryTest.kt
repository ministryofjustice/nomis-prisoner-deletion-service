package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.executeScripts
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.DuplicateOffender
import javax.sql.DataSource

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(PER_CLASS)
class DuplicatePrisonerRepositoryTest {

  @Autowired
  lateinit var repository: uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.DuplicatePrisonerRepository

  @BeforeAll
  fun setup(@Autowired dataSource: DataSource) {
    executeScripts(
      dataSource,
      "define_regexp_substr.sql",
      "define_jaro_winkler_similarity.sql",
      "mv_offender_match_details.sql"
    )
  }

  @AfterAll
  fun cleanup(@Autowired dataSource: DataSource) {
    executeScripts(
      dataSource,
      "drop_mv_offender_match_details.sql",
      "drop_regexp_substr.sql",
      "drop_jaro_winkler_similarity.sql"
    )
  }

  @Test
  fun `can retrieve offenders with matching pnc numbers`() {
    assertThat(repository.getOffendersWithMatchingPncNumbers("Z0020ZZ", setOf("99/1234567B", "20/9N")))
      .extracting<String>(DuplicateOffender::offenderNumber)
      .containsExactlyInAnyOrder("A1184JR", "A1184MA")
  }

  @Test
  fun `offenders with matching pnc numbers should be commutative`() {
    assertThat(repository.getOffendersWithMatchingPncNumbers("A1184JR", setOf("99/1234567B")))
      .extracting<String>(DuplicateOffender::offenderNumber)
      .containsOnly("Z0020ZZ")
  }

  @Test
  fun `can retrieve offenders with matching pnc numbers returns empty`() {
    assertThat(repository.getOffendersWithMatchingPncNumbers("A1234AA", setOf("NOTHING-MATCHES-THIS"))).isEmpty()
  }

  @Test
  fun `can retrieve offenders with matching cro numbers`() {
    assertThat(repository.getOffendersWithMatchingCroNumbers("Z0020ZZ", setOf("99/123456L", "11/1X", "99/12345M")))
      .extracting<String>(DuplicateOffender::offenderNumber)
      .containsExactlyInAnyOrder("A1184JR", "A1184MA", "A1183CW")
  }

  @Test
  fun `offenders with matching cro numbers is commutative`() {
    assertThat(repository.getOffendersWithMatchingCroNumbers("A1184JR", setOf("99/123456L")))
      .extracting<String>(DuplicateOffender::offenderNumber)
      .containsOnly("Z0020ZZ")
  }

  @Test
  fun `can retrieve offenders with matching cro numbers returns empty`() {
    assertThat(repository.getOffendersWithMatchingCroNumbers("A1234AA", setOf("NOTHING-MATCHES-THIS"))).isEmpty()
  }

  @Test
  fun `can retrieve offenders with matching ids numbers`() {
    assertThat(repository.getOffendersWithMatchingLidsNumbers("A1184JR"))
      .extracting<String>(DuplicateOffender::offenderNumber)
      .containsExactlyInAnyOrder("A1183CW")
  }

  @Test
  fun `can retrieve offenders with matching details`() {
    assertThat(repository.getOffendersWithMatchingDetails("A1234AA"))
      .extracting<String>(DuplicateOffender::offenderNumber)
      .containsExactlyInAnyOrder(
        "B1234BB",
        "C1234CC",
        "D1234DD",
        "E1234EE"
      )
  }

  @Test
  fun `can retrieve offenders with matching details is commutative`() {
    assertThat(repository.getOffendersWithMatchingDetails("B1234BB"))
      .extracting<String>(DuplicateOffender::offenderNumber)
      .containsExactlyInAnyOrder(
        "A1234AA",
        "C1234CC",
        "D1234DD",
        "E1234EE"
      )
  }

  @Test
  fun `can retrieve offenders with matching details returns empty`() {
    assertThat(repository.getOffendersWithMatchingDetails("Z1234ZZ")).isEmpty()
  }
}
