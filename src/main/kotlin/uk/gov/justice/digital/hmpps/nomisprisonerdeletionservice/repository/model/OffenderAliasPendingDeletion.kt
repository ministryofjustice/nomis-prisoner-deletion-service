package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model

import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "OFFENDERS")
data class OffenderAliasPendingDeletion(
  @Id
  @Column(name = "OFFENDER_ID", nullable = false)
  val offenderId: Long? = null,

  @Column(name = "OFFENDER_ID_DISPLAY", nullable = false)
  val offenderNumber: String? = null,

  @Column(name = "ROOT_OFFENDER_ID")
  val rootOffenderId: Long? = null,

  @Column(name = "FIRST_NAME")
  val firstName: String? = null,

  @Column(name = "MIDDLE_NAME")
  val middleName: String? = null,

  @Column(name = "LAST_NAME", nullable = false)
  val lastName: String? = null,

  @Column(name = "BIRTH_DATE")
  val birthDate: LocalDate? = null,
) {
  @OneToMany(mappedBy = "offenderAlias")
  var offenderBookings: List<OffenderBookingPendingDeletion> = ArrayList()

  @OneToMany(mappedBy = "offenderAlias")
  var offenderIdentifiers: List<OffenderIdentifierPendingDeletion> = ArrayList()
}
