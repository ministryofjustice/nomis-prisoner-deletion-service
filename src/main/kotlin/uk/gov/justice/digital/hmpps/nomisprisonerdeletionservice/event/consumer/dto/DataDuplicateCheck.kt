package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.consumer.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@JsonIgnoreProperties(ignoreUnknown = true)
data class DataDuplicateCheck(

  @JsonProperty("offenderIdDisplay")
  @field:NotBlank(message = "No offender ID specified in request")
  val offenderIdDisplay: String? = null,

  @JsonProperty("retentionCheckId")
  @field:NotNull(message = "No retention check ID specified in request")
  val retentionCheckId: Long? = null
)
