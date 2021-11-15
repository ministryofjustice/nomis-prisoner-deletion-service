package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class FreeTextMatch(

  @Id
  @Column(name = "TABLE_NAME", nullable = false)
  val tableName: String

)
