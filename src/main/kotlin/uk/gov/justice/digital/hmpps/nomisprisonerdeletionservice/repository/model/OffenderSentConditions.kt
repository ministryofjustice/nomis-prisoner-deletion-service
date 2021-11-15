package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "OFFENDER_SENT_CONDITIONS")
data class OffenderSentConditions(
  @Id
  @Column(name = "OFFENDER_SENT_CONDITION_ID")
  val offenderSentConditionId: Long,

  @Column(name = "OFFENDER_BOOK_ID")
  val bookingId: Long? = null,

  @Column(name = "GROOMING_FLAG")
  val groomingFlag: String? = null,

  @Column(name = "NO_WORK_WITH_UNDER_AGE")
  val noWorkWithUnderAge: String? = null
)
