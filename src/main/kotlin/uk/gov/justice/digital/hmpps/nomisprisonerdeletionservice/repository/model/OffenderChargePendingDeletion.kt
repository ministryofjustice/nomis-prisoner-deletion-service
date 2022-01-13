package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "OFFENDER_CHARGES")
data class OffenderChargePendingDeletion(
  @Id
  @Column(name = "OFFENDER_CHARGE_ID")
  val offenderChargeId: Long? = null,

  @Column(name = "OFFENCE_CODE", nullable = false)
  val offenceCode: @NotNull String,

  @ManyToOne
  @JoinColumn(name = "OFFENDER_BOOK_ID", nullable = false)
  val offenderBooking: @NotNull OffenderBookingPendingDeletion
)
