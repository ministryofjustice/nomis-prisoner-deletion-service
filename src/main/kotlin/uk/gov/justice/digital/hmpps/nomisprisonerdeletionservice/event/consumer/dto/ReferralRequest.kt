package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.consumer.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import java.time.LocalDate
import javax.validation.constraints.NotNull

@JsonIgnoreProperties(ignoreUnknown = true)
data class ReferralRequest(

  @field:NotNull(message = "No batch ID specified in request")
  @JsonProperty("batchId")
  val batchId: Long? = null,

  @field:NotNull(message = "No window start date specified in request")
  @JsonProperty("dueForDeletionWindowStart")
  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonDeserialize(using = LocalDateDeserializer::class)
  val dueForDeletionWindowStart: LocalDate? = null,

  @field:NotNull(message = "No window end date specified in request")
  @JsonProperty("dueForDeletionWindowEnd")
  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonDeserialize(using = LocalDateDeserializer::class)
  val dueForDeletionWindowEnd: LocalDate? = null,

  @JsonProperty("limit")
  val limit: Int? = null
)
