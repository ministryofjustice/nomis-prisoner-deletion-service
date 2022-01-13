package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.consumer.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ProvisionalDeletionReferralRequest(

  @JsonProperty("offenderIdDisplay")
  @field:NotBlank(message = "No offender ID specified in request")
  val offenderIdDisplay: String? = null,

  @JsonProperty("referralId")
  @field:NotNull(message = "No referralId specified in request")
  val referralId: Long? = null
)
