package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class DataDuplicateResult(
  @JsonProperty("offenderIdDisplay")
  val offenderIdDisplay: String? = null,

  @JsonProperty("retentionCheckId")
  val retentionCheckId: Long? = null,

  @JsonProperty("duplicateOffenders")
  val duplicateOffenders: List<String>? = emptyList()
)
