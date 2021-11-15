package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderPendingDeletion
import java.time.LocalDate

@DataJpaTest
@ActiveProfiles("test")
class DeceasedOffenderPendingDeletionRepositoryTest {

  @Autowired
  lateinit var repository: uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.DeceasedOffenderPendingDeletionRepository

  @Test
  fun `can find deceased offenders due for deletion with paging`() {
    val offenders = repository.findDeceasedOffendersDueForDeletion(LocalDate.now(), PageRequest.of(0, 1))
    assertThat(offenders).hasSize(1)
    assertThat(offenders).extracting<String>(OffenderPendingDeletion::offenderNumber).containsOnly("Z0023ZZ")
  }

  @Test
  fun `can find deceased offenders due for deletion un-paged`() {
    val offenders = repository.findDeceasedOffendersDueForDeletion(LocalDate.now(), Pageable.unpaged())
    assertThat(offenders).hasSize(2)
    assertThat(offenders).extracting<String>(OffenderPendingDeletion::offenderNumber)
      .containsExactly("Z0023ZZ", "Z0017ZZ")
  }

  @Test
  fun `can find deceased offenders when no offenders meet 22 years criteria`() {
    val offenders = repository.findDeceasedOffendersDueForDeletion(LocalDate.now().minusYears(50), Pageable.unpaged())
    assertThat(offenders).hasSize(0)
  }

  @Test
  fun `can find deceased offenders due for deletion with paging offset`() {
    val offenders = repository.findDeceasedOffendersDueForDeletion(LocalDate.now(), PageRequest.of(1, 1))
    assertThat(offenders).hasSize(1)
    assertThat(offenders).extracting<String>(OffenderPendingDeletion::offenderNumber).containsExactly("Z0017ZZ")
  }
}
