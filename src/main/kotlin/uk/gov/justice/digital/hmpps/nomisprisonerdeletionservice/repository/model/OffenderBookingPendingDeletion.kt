package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "OFFENDER_BOOKINGS")
data class OffenderBookingPendingDeletion(

  @Id
  @Column(name = "OFFENDER_BOOK_ID")
  val bookingId: Long? = null,

  @ManyToOne
  @JoinColumn(name = "OFFENDER_ID", nullable = false)
  val offenderAlias: OffenderAliasPendingDeletion? = null,

  @OneToMany(mappedBy = "offenderBooking")
  val offenderCharges: List<OffenderChargePendingDeletion> = ArrayList(),

  @OneToMany(mappedBy = "offenderBooking")
  val offenderAlerts: List<OffenderAlertPendingDeletion> = ArrayList()
)
