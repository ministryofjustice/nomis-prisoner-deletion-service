package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
class OffenderSentConditionsRepositoryTest {

  @Autowired
  private lateinit var repsotiory: OffenderSentConditionsRepository

  @Test
  internal fun `can find child related conditions by Bookings`() {

    val conditions = repsotiory.findChildRelatedConditionsByBookings(listOf(-1L))

    assertThat(conditions).extracting<Long> { it.offenderSentConditionId }.containsOnly(-1L)
    assertThat(conditions).extracting<Long> { it.bookingId }.containsOnly(-1L)
    assertThat(conditions).extracting<String> { it.groomingFlag }.containsOnly("N")
    assertThat(conditions).extracting<String> { it.noWorkWithUnderAge }.containsOnly("Y")
  }
}
