package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model

import com.fasterxml.jackson.annotation.JsonInclude
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.utils.capitalize
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.utils.formatLocation
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Movement(

  @get:NotBlank
  val offenderNo: String? = null,

  @get:NotNull
  val createDateTime: LocalDateTime? = null,

  @get:NotBlank
  val fromAgency: String? = null,

  val fromAgencyDescription: String? = null,

  @get:NotBlank
  val toAgency: String? = null,

  val toAgencyDescription: String? = null,

  val fromCity: String? = null,

  val toCity: String? = null,

  @get:NotBlank
  val movementType: String? = null,

  val movementTypeDescription: String? = null,

  @get:NotBlank
  val directionCode: String? = null,

  val movementDate: LocalDate? = null,

  val movementTime: LocalTime? = null,

  val movementReason: String? = null,

  val commentText: String? = null
) {
  fun formattedCopy() =
    this.copy(
      fromAgencyDescription = this.fromAgencyDescription?.formatLocation()?.trim() ?: "",
      toAgencyDescription = this.toAgencyDescription?.formatLocation()?.trim() ?: "",
      toCity = this.toCity?.trim()?.capitalize() ?: "",
      fromCity = this.fromCity?.trim()?.capitalize() ?: "",
    )
}
