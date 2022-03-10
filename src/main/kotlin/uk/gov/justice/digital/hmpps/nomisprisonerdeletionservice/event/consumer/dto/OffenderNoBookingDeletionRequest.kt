package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.consumer.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.validation.constraints.NotNull

@JsonIgnoreProperties(ignoreUnknown = true)
data class OffenderNoBookingDeletionRequest(
  @field:NotNull(message = "No batch ID specified in the request")
  val batchId: Long? = null,
  val limit: Int? = null
)
