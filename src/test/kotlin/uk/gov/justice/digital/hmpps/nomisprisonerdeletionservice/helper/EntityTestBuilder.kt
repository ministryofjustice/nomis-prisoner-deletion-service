package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper

import org.springframework.data.domain.PageRequest
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.DeceasedOffenderDeletionResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderNoBookingDeletionResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderPendingDeletionReferralComplete
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.ProvisionalDeletionReferralResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.Movement
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderAlertPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderAliasPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderBookingPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderChargePendingDeletion
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

fun buildOffenderAliasPendingDeletion(
  offenderId: Long,
  offenderNumber: String,
  rootOffenderId: Long = offenderId,
  firstName: String = "Tom",
  middleName: String = "Williamson",
  lastName: String = "Jones",
  hasBooking: Boolean = true,
): OffenderAliasPendingDeletion {
  return OffenderAliasPendingDeletion(
    offenderId, offenderNumber, rootOffenderId, firstName, middleName, lastName, LocalDate.of(1993, 11, 12)
  ).also {
    if (hasBooking) {
      it.offenderBookings = listOf(
        OffenderBookingPendingDeletion(
          bookingId = it.offenderId,
          offenderCharges = listOf(
            OffenderChargePendingDeletion(
              offenceCode = "offence$offenderId",
              offenderChargeId = 324L,
              offenderBooking = OffenderBookingPendingDeletion()
            )
          ),
          offenderAlerts = listOf(OffenderAlertPendingDeletion().also { it.alertCode = "alert$offenderId" })
        )
      )
    }
  }
}

fun expectedPendingDeletionEvent(
  offenderId: Long,
  offenderNumber: String,
  hasAgencyLocation: Boolean = true,
  firstName: String = "Tom",
  middleName: String = "Williamson",
  lastName: String = "Jones",
): OffenderPendingDeletion {
  return OffenderPendingDeletion(
    offenderIdDisplay = offenderNumber,
    batchId = batchId,
    firstName = firstName,
    middleName = middleName,
    lastName = lastName,
    birthDate = LocalDate.of(1993, 11, 12),
    agencyLocationId = if (hasAgencyLocation) "LEI" else null,
    offenderAliases =
    listOf(
      OffenderPendingDeletion.OffenderAlias(
        offenderId = offenderId,
        bookings =
        listOf(
          OffenderPendingDeletion.Booking(
            offenderBookId = offenderId,
            offenceCodes = setOf("offence$offenderId"),
            alertCodes = setOf("alert$offenderId")
          )
        )
      )
    )
  )
}

fun expectedProvisionalDeletionReferralResult(
  offenderId: Long = offenderId1,
  offenderNumber: String = offenderNumber1,
  referralId: Long,
  subsequentChangesIdentified: Boolean = false
): ProvisionalDeletionReferralResult {
  return ProvisionalDeletionReferralResult(
    referralId = referralId,
    offenderIdDisplay = offenderNumber,
    subsequentChangesIdentified = subsequentChangesIdentified,
    agencyLocationId = "LEI",
    offenceCodes = setOf("offence$offenderId"),
    alertCodes = setOf("alert$offenderId")
  )
}

fun buildOffender(
  offenderId: Long,
  offenderNumber: String,
  firstName: String = "Tom",
  middleName: String = "Williamson",
  lastName: String = "Jones",
  clock: Clock = Clock.fixed(Instant.ofEpochMilli(0), ZoneId.systemDefault())
): OffenderNoBookingDeletionResult.Offender {
  return OffenderNoBookingDeletionResult.Offender(
    offenderIdDisplay = offenderNumber,
    firstName = firstName,
    middleName = middleName,
    lastName = lastName,
    birthDate = LocalDate.of(1993, 11, 12),
    offenderAliases =
    listOf(
      OffenderNoBookingDeletionResult.OffenderAlias(
        offenderId = offenderId,
      )
    ),
    deletionDateTime = LocalDateTime.now(clock)
  )
}

fun buildDeceasedOffender(
  offenderId: Long,
  offenderNumber: String,
  hasMovement: Boolean,
  clock: Clock,
  firstName: String = "Tom",
  middleName: String = "Williamson",
  lastName: String = "Jones"
): DeceasedOffenderDeletionResult.DeceasedOffender {
  return DeceasedOffenderDeletionResult.DeceasedOffender(
    offenderIdDisplay = offenderNumber,
    firstName = firstName,
    middleName = middleName,
    lastName = lastName,
    birthDate = LocalDate.of(1993, 11, 12),
    agencyLocationId = if (hasMovement) "LEI" else null,
    offenderAliases =
    listOf(
      DeceasedOffenderDeletionResult.OffenderAlias(
        offenderId = offenderId,
        offenderBookIds = listOf(offenderId)
      )
    ),
    deceasedDate = if (hasMovement) LocalDate.now(clock) else null,
    deletionDateTime = LocalDateTime.now(clock)
  )
}

fun offendersLastMovement(
  offenderNo: String,
  clock: Clock = Clock.fixed(Instant.ofEpochMilli(0), ZoneId.systemDefault()),
  fromAgency: String = "LEI",
  fromAgencyDescription: String = "lei prison",
  toAgency: String = "OUT",
  toAgencyDescription: String = "out of prison",
  commentText: String = "Some comment text",
  movementType: String = "REL"
): Movement {
  return Movement(
    offenderNo = offenderNo,
    fromAgency = fromAgency,
    fromAgencyDescription = fromAgencyDescription,
    toAgency = toAgency,
    toAgencyDescription = toAgencyDescription,
    movementDate = LocalDate.now(clock),
    movementTime = LocalTime.now(clock),
    commentText = commentText,
    movementType = movementType
  )
}

fun expectedReferralCompleteEvent(
  batchId: Long,
  numberReferred: Long,
  totalInWindow: Long
): OffenderPendingDeletionReferralComplete {
  return OffenderPendingDeletionReferralComplete(batchId, numberReferred, totalInWindow)
}

val windowStart = LocalDate.now()
val windowEnd = windowStart.plusDays(1)
val pageRequest = PageRequest.of(0, 2)

const val batchId = 123L
const val totalInWindow: Long = 10
const val offenderNumber1 = "A1234AA"
const val offenderNumber2 = "B4321BB"
const val offenderNumber3 = "C4361CC"
const val offenderId1 = 2L
const val offenderId2 = 9L
const val offenderId3 = 99L
const val referralId = 123L
const val bookingId = 2L
const val offenderPnc = "1999/0123456X"
const val formattedOffenderPnc = "99/123456X"
const val offenderCro = "000001/11X"
const val formattedOffenderCro = "11/1X"
const val offenderSentConditionId = 1109L
const val duplicate1 = "B1234BB"
const val duplicate2 = "C1234CC"
const val duplicate3 = "D1234DD"
const val retentionCheckId = 123L
const val regex = "^(some|regex)$"
const val matchingTable1 = "table1"
const val matchingTable2 = "table2"
