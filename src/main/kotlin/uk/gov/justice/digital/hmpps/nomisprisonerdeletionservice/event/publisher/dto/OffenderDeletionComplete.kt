package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class OffenderDeletionComplete(
  @JsonProperty("offenderIdDisplay")
  val offenderIdDisplay: String? = null,

  @JsonProperty("referralId")
  val referralId: Long? = null
)
