package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "OFFENDERS")
data class OffenderWithImage(

  @Id
  @Column(name = "OFFENDER_ID_DISPLAY", nullable = false)
  val offenderNumber: String
)
