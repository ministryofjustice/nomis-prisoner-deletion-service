package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import java.time.LocalDate

/**
 * This event signifies that an offender's
 * data is eligible for deletion, subject to
 * further checks by the Data Compliance Service.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
data class OffenderPendingDeletion(
  @JsonProperty("offenderIdDisplay")
  val offenderIdDisplay: String? = null,

  @JsonProperty("batchId")
  val batchId: Long? = null,

  @JsonProperty("firstName")
  val firstName: String? = null,

  @JsonProperty("middleName")
  val middleName: String? = null,

  @JsonProperty("lastName")
  val lastName: String? = null,

  @JsonProperty("pncs")
  val pncs: Set<String> = HashSet(),

  @JsonProperty("cros")
  val cros: Set<String> = HashSet(),

  @JsonProperty("birthDate")
  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonSerialize(using = LocalDateSerializer::class)
  val birthDate: LocalDate? = null,

  @JsonProperty("agencyLocationId")
  val agencyLocationId: String? = null,

  @JsonProperty("offenderAliases")
  val offenderAliases: List<OffenderAlias> = ArrayList(),

) {
  data class OffenderAlias(
    @JsonProperty("offenderId")
    val offenderId: Long? = null,

    @JsonProperty("bookings")
    val bookings: List<Booking> = ArrayList()
  )

  data class Booking(
    @JsonProperty("offenderBookId")
    val offenderBookId: Long? = null,

    @JsonProperty("bookingNo")
    val bookingNo: String? = null,

    @JsonProperty("offenceCodes")
    val offenceCodes: Set<String> = HashSet(),

    @JsonProperty("alertCodes")
    val alertCodes: Set<String> = HashSet()
  )
}
