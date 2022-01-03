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
@Table(name = "OFFENDER_IDENTIFIERS")
class OffenderIdentifierPendingDeletion {

  @Embeddable
  class OffenderIdentifierPK : Serializable {
    @Column(name = "OFFENDER_ID", nullable = false)
    val offenderId: Long? = null

    @Column(name = "OFFENDER_ID_SEQ", nullable = false)
    val offenderIdSeq: Long? = null
  }

  @EmbeddedId
  val offenderIdentifierPK: OffenderIdentifierPK? = null

  @ManyToOne
  @JoinColumn(name = "OFFENDER_ID", insertable = false, updatable = false)
  val offenderAlias: OffenderAliasPendingDeletion? = null

  @Column(name = "IDENTIFIER_TYPE", nullable = false)
  val identifierType: String? = null

  @Column(name = "IDENTIFIER", nullable = false)
  val identifier: String? = null

  val isPnc: Boolean
    get() = "PNC".equals(identifierType, ignoreCase = true)

  val isCro: Boolean
    get() = "CRO".equals(identifierType, ignoreCase = true)
}
