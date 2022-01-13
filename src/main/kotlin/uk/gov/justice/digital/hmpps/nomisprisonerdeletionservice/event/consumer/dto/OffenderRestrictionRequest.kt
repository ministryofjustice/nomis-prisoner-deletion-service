package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.consumer.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@JsonIgnoreProperties(ignoreUnknown = true)
data class OffenderRestrictionRequest(

  @JsonProperty("offenderIdDisplay")
  @field:NotBlank(message = "No offender ID specified in request")
  val offenderIdDisplay: String,

  @JsonProperty("retentionCheckId")
  @field:NotNull(message = "No retention check ID specified in request")
  val retentionCheckId: Long? = null,

  @JsonProperty("restrictionCodes")
  @field:NotNull(message = "No retention check ID specified in request")
  @field:NotEmpty(message = "No retention check ID specified in request")
  val restrictionCodes: Set<String> = HashSet(),

  @JsonProperty("regex")
  @field:NotBlank(message = "No regex provided in request")
  val regex: String? = null
)
