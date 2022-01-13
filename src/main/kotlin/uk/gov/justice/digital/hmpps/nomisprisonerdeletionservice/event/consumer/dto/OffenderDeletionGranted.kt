package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.consumer.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@JsonIgnoreProperties(ignoreUnknown = true)
data class OffenderDeletionGranted(

  @JsonProperty("offenderIdDisplay")
  @field:NotBlank(message = "No offender ID specified in request")
  val offenderIdDisplay: String? = null,

  @JsonProperty("referralId")
  @field:NotNull(message = "No referral ID specified in request")
  val referralId: Long? = null,

  @JsonProperty("offenderIds")
  val offenderIds: Set<Long> = HashSet(),

  @JsonProperty("offenderBookIds")
  val offenderBookIds: Set<Long> = HashSet()
)
