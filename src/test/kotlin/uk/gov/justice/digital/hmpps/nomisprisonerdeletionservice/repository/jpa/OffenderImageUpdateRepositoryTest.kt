package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@DataJpaTest
@ActiveProfiles("test")
class OffenderImageUpdateRepositoryTest {

  @Autowired
  lateinit var repository: OffenderImageUpdateRepository

  @Test
  fun `can retrieve offenders with images`() {
    val offenders =
      repository.getOffendersWithImagesCapturedAfter(LocalDateTime.of(2020, 1, 1, 0, 0), Pageable.unpaged())

    assertThat(offenders).hasSize(5)
    assertThat(offenders).extracting<String> { it.offenderNumber }
      .containsExactlyInAnyOrder("Z0026ZZ", "Z0025ZZ", "Z0024ZZ", "Z0023ZZ", "Z0022ZZ")
  }

  @Test
  fun `can retrieve offenders with images with paging`() {
    val offenders = repository.getOffendersWithImagesCapturedAfter(
      LocalDateTime.of(2020, 1, 1, 0, 0),
      PageRequest.of(1, 3, Sort.by(Sort.Direction.ASC, "offender_id_display"))
    )

    assertThat(offenders).hasSize(2)
    assertThat(offenders).extracting<String> { it.offenderNumber }.containsExactly("Z0025ZZ", "Z0026ZZ")
  }
}
