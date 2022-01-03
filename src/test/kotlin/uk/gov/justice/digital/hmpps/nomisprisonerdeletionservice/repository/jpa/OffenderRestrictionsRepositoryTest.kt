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
import javax.sql.DataSource

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(PER_CLASS)
class OffenderRestrictionsRepositoryTest {

  @Autowired
  lateinit var repository: OffenderRestrictionsRepository

  @BeforeAll
  fun setup(@Autowired dataSource: DataSource) {
    executeScripts(dataSource, "define_regexp_like.sql")
  }

  @AfterAll
  fun cleanup(@Autowired dataSource: DataSource) {
    executeScripts(dataSource, "drop_regexp_like.sql")
  }

  @Test
  fun `can find offender restrictions`() {
    val restrictions = repository.findOffenderRestrictions(setOf(-1L), setOf("RESTRICTION"), ".*Text.*")

    assertThat(restrictions).extracting<Long> { it.offenderRestrictionId }.containsOnly(-1L)
    assertThat(restrictions).extracting<Long> { it.offenderBookId }.containsOnly(-1L)
    assertThat(restrictions).extracting<String> { it.restrictionType }.containsOnly("RESTRICTION")
    assertThat(restrictions).extracting<String> { it.commentText }.containsOnly("Some Comment Text")
  }

  @Test
  fun `can find offender restrictions when restriction code does not match and regex matches`() {
    val restrictions = repository.findOffenderRestrictions(setOf(-1L), setOf("CHILD"), ".*Text.*")

    assertThat(restrictions).extracting<Long> { it.offenderRestrictionId }.containsOnly(-1L)
    assertThat(restrictions).extracting<Long> { it.offenderBookId }.containsOnly(-1L)
    assertThat(restrictions).extracting<String> { it.restrictionType }.containsOnly("RESTRICTION")
    assertThat(restrictions).extracting<String> { it.commentText }.containsOnly("Some Comment Text")
  }

  @Test
  fun `findOffenderRestrictions returns empty when restriction code and regex matches does not match`() {
    assertThat(repository.findOffenderRestrictions(setOf(-1L), setOf("CHILD"), ".*NoMatchRegex.*")).isEmpty()
  }

  @Test
  fun `findOffenderRestrictions returns empty when using an invalid BookingId`() {
    assertThat(repository.findOffenderRestrictions(setOf(-100L), setOf("RESTRICTION"), ".*Text.*")).isEmpty()
  }
}
