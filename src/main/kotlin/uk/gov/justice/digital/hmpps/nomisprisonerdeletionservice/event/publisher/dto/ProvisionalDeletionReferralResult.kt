package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ProvisionalDeletionReferralResult(
  @JsonProperty("referralId")
  val referralId: Long? = null,

  @JsonProperty("offenderIdDisplay")
  val offenderIdDisplay: String? = null,

  @JsonProperty("subsequentChangesIdentified")
  val subsequentChangesIdentified: Boolean = false,

  @JsonProperty("agencyLocationId")
  val agencyLocationId: String? = null,

  @JsonProperty("offenceCodes")
  val offenceCodes: Set<String>? = null,

  @JsonProperty("alertCodes")
  val alertCodes: Set<String>? = null

) {
  companion object {
    fun changesIdentifiedResult(referralId: Long, offenderNumber: String): ProvisionalDeletionReferralResult {
      return ProvisionalDeletionReferralResult(
        referralId = referralId,
        offenderIdDisplay = offenderNumber,
        subsequentChangesIdentified = true
      )
    }
  }
}
