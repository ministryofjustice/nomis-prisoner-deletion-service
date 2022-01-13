package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.consumer.dto

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class AdHocReferralRequest(

  @JsonProperty("offenderIdDisplay")
  @field:NotBlank(message = "No offender ID specified in request")
  val offenderIdDisplay: String? = null,

  @JsonProperty("batchId")
  @field:NotNull(message = "No batch ID specified in request")
  val batchId: Long? = null
)
