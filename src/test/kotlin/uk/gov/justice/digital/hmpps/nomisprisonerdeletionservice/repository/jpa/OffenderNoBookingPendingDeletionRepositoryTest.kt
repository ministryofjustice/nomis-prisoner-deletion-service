package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderPendingDeletion

@DataJpaTest
@ActiveProfiles("test")
class OffenderNoBookingPendingDeletionRepositoryTest {

  @Autowired
  lateinit var repository: OffenderNoBookingPendingDeletionRepository

  @Test
  fun `should retrieve offenders with no bookings`() {

    val offendersWithNoBookings = repository.findOffendersWithNoBookingsDueForDeletion(Pageable.unpaged())

    assertThat(offendersWithNoBookings).hasSize(4)
    assertThat(offendersWithNoBookings)
      .extracting<String>(OffenderPendingDeletion::offenderNumber)
      .containsExactly("A1234AN", "A1234AO", "A9880GH", "A1234DD")
  }

  @Test
  fun `should retrieve offenders with no bookings - paged`() {

    val offendersWithNoBookings = repository.findOffendersWithNoBookingsDueForDeletion(Pageable.ofSize(2))

    assertThat(offendersWithNoBookings).hasSize(2)
    assertThat(offendersWithNoBookings)
      .extracting<String>(OffenderPendingDeletion::offenderNumber)
      .containsExactly("A1234AN", "A1234AO")
  }

  @Test
  @Sql("data/insert_tagged_bail_identifier.sql")
  fun `should exclude offenders tagged bail when retrieving offenders with no bookings`() {

    val offendersWithNoBookings = repository.findOffendersWithNoBookingsDueForDeletion(Pageable.unpaged())

    assertThat(offendersWithNoBookings).hasSize(2)
    assertThat(offendersWithNoBookings)
      .extracting<String>(OffenderPendingDeletion::offenderNumber)
      .containsExactly("A9880GH", "A1234DD")
  }
}
