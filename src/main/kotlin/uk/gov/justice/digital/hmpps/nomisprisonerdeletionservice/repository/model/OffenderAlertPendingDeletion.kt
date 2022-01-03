package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "OFFENDER_ALERTS")
class OffenderAlertPendingDeletion {

  @Embeddable
  class OffenderAlertPK : Serializable {
    @Column(name = "OFFENDER_BOOK_ID", nullable = false)
    val offenderId: Long? = null

    @Column(name = "ALERT_SEQ", nullable = false)
    val offenderIdSeq: Long? = null
  }

  @EmbeddedId
  lateinit var offenderAlertPK: OffenderAlertPK

  @ManyToOne
  @JoinColumn(name = "OFFENDER_BOOK_ID", insertable = false, updatable = false)
  lateinit var offenderBooking: OffenderBookingPendingDeletion

  @Column(name = "ALERT_CODE", nullable = false)
  lateinit var alertCode: String
}
