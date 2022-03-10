package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderAliasPendingDeletion
import java.time.LocalDate

@DataJpaTest
@ActiveProfiles("test")
class OffenderAliasPendingDeletionRepositoryTest {

  @Autowired
  lateinit var repository: OffenderAliasPendingDeletionRepository

  @Test
  fun `can find offender alias for pending deletion`() {

    val offenders = repository.findOffenderAliasPendingDeletionByOffenderNumber("A1234AA")

    assertThat(offenders).hasSize(1)
    val offender: OffenderAliasPendingDeletion = offenders.get(0)

    assertThat(offender.offenderNumber).isEqualTo("A1234AA")
    assertThat(offender.offenderId).isEqualTo(-1001L)
    assertThat(offender.rootOffenderId).isEqualTo(-1001L)
    assertThat(offender.firstName).isEqualTo("ARTHUR")
    assertThat(offender.middleName).isEqualTo("BORIS")
    assertThat(offender.lastName).isEqualTo("ANDERSON")
    assertThat(offender.birthDate).isEqualTo(LocalDate.of(1969, 12, 30))

    assertThat(offender.offenderBookings).hasSize(1)
    assertThat(offender.offenderBookings[0].bookingId).isEqualTo(-1)
    assertThat(offender.offenderBookings[0].offenderCharges)
      .extracting<String> { it.offenceCode }
      .containsExactlyInAnyOrder("RC86356", "RV98011")

    assertThat(offender.offenderBookings[0].offenderAlerts)
      .extracting<String> { it.alertCode }
      .containsExactlyInAnyOrder("XA", "HC", "RSS", "XTACT")

    val offenderIdentifiers = offender.offenderIdentifiers[0]
    assertThat(offenderIdentifiers.identifier).isEqualTo("A3333AB")

    val offenderAlias = offenderIdentifiers.offenderAlias
    assertThat(offenderAlias?.offenderNumber).isEqualTo("A1234AA")
    assertThat(offenderAlias?.rootOffenderId).isEqualTo(-1001L)
    assertThat(offenderAlias?.firstName).isEqualTo("ARTHUR")
    assertThat(offenderAlias?.middleName).isEqualTo("BORIS")
    assertThat(offenderAlias?.lastName).isEqualTo("ANDERSON")
    assertThat(offenderAlias?.birthDate).isEqualTo(LocalDate.of(1969, 12, 30))
  }
}
