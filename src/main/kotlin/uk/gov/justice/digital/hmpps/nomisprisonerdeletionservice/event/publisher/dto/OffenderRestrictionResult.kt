package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class OffenderRestrictionResult(
  @JsonProperty("offenderIdDisplay")
  val offenderIdDisplay: String? = null,

  @JsonProperty("retentionCheckId")
  val retentionCheckId: Long? = null,

  @JsonProperty("restricted")
  val restricted: Boolean = false
)
