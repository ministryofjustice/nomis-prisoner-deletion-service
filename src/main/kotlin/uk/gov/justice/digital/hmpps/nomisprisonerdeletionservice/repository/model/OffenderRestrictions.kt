package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "OFFENDER_RESTRICTIONS")
data class OffenderRestrictions(

  @Id
  @Column(name = "OFFENDER_RESTRICTION_ID")
  val offenderRestrictionId: Long? = null,

  @Column(name = "OFFENDER_BOOK_ID")
  val offenderBookId: Long? = null,

  @Column(name = "RESTRICTION_TYPE")
  val restrictionType: String? = null,

  @Column(name = "COMMENT_TEXT")
  val commentText: String? = null
)
