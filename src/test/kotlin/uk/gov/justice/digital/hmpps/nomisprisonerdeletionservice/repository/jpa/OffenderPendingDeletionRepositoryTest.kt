package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import java.time.LocalDate
import java.util.stream.Stream
import java.util.stream.Stream.of

@DataJpaTest
@ActiveProfiles("test")
class OffenderPendingDeletionRepositoryTest {

  val sentenceEndData: LocalDate = LocalDate.of(2020, 3, 24)
  val deletionDueDate: LocalDate = sentenceEndData.plusYears(7)
  val pageRequest: PageRequest = PageRequest.of(0, 1)

  @Autowired
  lateinit var repository: OffenderPendingDeletionRepository

  @Test
  fun `can retrieve offenders due for deletion (with paging)`() {
    val offenders = repository.getOffendersDueForDeletionBetween(
      deletionDueDate,
      deletionDueDate.plusDays(1),
      pageRequest
    )

    assertThat(offenders).hasSize(1)
    assertThat(offenders).extracting<String> { it.offenderNumber }.containsOnly("Z0020ZZ")
  }

  @Test
  fun `can retrieve offenders due for deletion (un-paged)`() {
    val offenders = repository.getOffendersDueForDeletionBetween(
      deletionDueDate,
      deletionDueDate.plusDays(1),
      Pageable.unpaged()
    )

    assertThat(offenders).hasSize(1)
    assertThat(offenders).extracting<String> { it.offenderNumber }.containsOnly("Z0020ZZ")
  }

  @Test
  fun `retrieve offenders due for deletion works with exclusive dates`() {
    val offenders = repository.getOffendersDueForDeletionBetween(
      deletionDueDate,
      deletionDueDate,
      Pageable.unpaged()
    )

    assertThat(offenders).hasSize(0)
  }

  @ParameterizedTest()
  @MethodSource("providedParameters")
  fun `retrieve offenders due for deletion returns empty when the dates due not match data`(
    from: LocalDate,
    to: LocalDate
  ) {
    assertThat(repository.getOffendersDueForDeletionBetween(from, to, pageRequest)).isEmpty()
  }

  @Test
  fun `retrieve offenders due for deletion returns empty for dates WrongWayRound`() {
    assertThat(
      repository.getOffendersDueForDeletionBetween(
        deletionDueDate.plusDays(1),
        deletionDueDate.minusDays(1),
        pageRequest
      )
    ).isEmpty()
  }

  @Test
  @Sql("add_iwp_document.sql")
  fun `retrieve offenders due for deletion filters out those with documents`() {
    assertThat(
      repository.getOffendersDueForDeletionBetween(
        deletionDueDate,
        deletionDueDate.plusDays(1),
        pageRequest
      )
    ).isEmpty()
  }

  @Test
  fun `can retrieve offender pending deletion`() {
    val offenders = repository.findOffenderPendingDeletion("Z0020ZZ", deletionDueDate.plusDays(1))

    assertThat(offenders).isPresent()
    assertThat(offenders.get().offenderNumber).isEqualTo("Z0020ZZ")
  }

  @Test
  fun `retrieve offender pending deletion returns empty if deletion not due`() {
    assertThat(repository.findOffenderPendingDeletion("Z0020ZZ", deletionDueDate.minusDays(1))).isEmpty
  }

  @Test
  fun `retrieve offender pending deletion returns empty if offender not found`() {
    assertThat(repository.findOffenderPendingDeletion("UNKNOWN", deletionDueDate.plusDays(1))).isEmpty
  }

  companion object {
    @JvmStatic
    fun providedParameters(): Stream<Arguments> {
      return of(
        Arguments.of(LocalDate.of(1970, 1, 1), LocalDate.of(1970, 1, 2)),
        Arguments.of(LocalDate.of(1960, 1, 1), LocalDate.of(1970, 1, 2))
      )
    }
  }
}
