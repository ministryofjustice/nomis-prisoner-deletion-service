package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto

import com.fasterxml.jackson.annotation.JsonInclude

/**
 * This event signifies that the process of publishing
 * events for offenders pending deletion is complete.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
data class OffenderPendingDeletionReferralComplete(
  val batchId: Long? = null,

  val numberReferred: Long? = null,

  val totalInWindow: Long? = null
)
