package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import java.time.LocalDate
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class DeceasedOffenderDeletionResult(
  @JsonProperty("batchId")
  val batchId: Long? = null,

  @JsonProperty("deceasedOffenders")
  val deceasedOffenders: List<DeceasedOffender>? = emptyList()
) {
  data class DeceasedOffender(
    @JsonProperty("offenderIdDisplay")
    val offenderIdDisplay: String? = null,

    @JsonProperty("firstName")
    val firstName: String? = null,

    @JsonProperty("middleName")
    val middleName: String? = null,

    @JsonProperty("lastName")
    val lastName: String? = null,

    @JsonProperty("birthDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer::class)
    val birthDate: LocalDate? = null,

    @JsonProperty("deceasedDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer::class)
    val deceasedDate: LocalDate? = null,

    @JsonProperty("deletionDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer::class)
    val deletionDateTime: LocalDateTime? = null,

    @JsonProperty("agencyLocationId")
    val agencyLocationId: String? = null,

    @JsonProperty("offenderAliases")
    val offenderAliases: List<OffenderAlias>? = emptyList()
  )

  data class OffenderAlias(
    @JsonProperty("offenderId")
    val offenderId: Long? = null,

    @JsonProperty("offenderBookIds")
    val offenderBookIds: List<Long?> = emptyList()
  )
}
