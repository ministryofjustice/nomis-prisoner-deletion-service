package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.logging

import com.microsoft.applicationinsights.TelemetryClient
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.sns.SnsPublisher
import java.time.LocalDateTime

@Component
class ApplicationEventListener(
  val telemetryClient: TelemetryClient,
  val snsPublisher: SnsPublisher
) {

  @TransactionalEventListener
  fun deletionEventListener(deletionEvent: DeletionEvent) {
    logDeletionApplicationInsights(deletionEvent)
    publishDeletionEventSns(deletionEvent)
  }

  private fun publishDeletionEventSns(deletionEvent: DeletionEvent) {
    with(deletionEvent) {
      snsPublisher.sendOffenderDeletionEvent(
        event.eventType,
        event.description,
        offenderNumber,
        offenderIds,
        identifier,
        occurredAt
      )
    }
  }

  private fun logDeletionApplicationInsights(deletionEvent: DeletionEvent) {
    with(deletionEvent) {
      telemetryClient.trackEvent(
        event.event,
        mutableMapOf("offenderNo" to offenderNumber, "count" to offenderIds.size.toString()),
        null
      )
    }
  }
}

data class DeletionEvent(
  val event: Event,
  val offenderIds: Set<Long>,
  val offenderNumber: String,
  val identifier: Long,
  val occurredAt: LocalDateTime
)

enum class Event(val event: String, val eventType: String, val description: String) {
  OFFENDER_DELETION(
    "OffenderDelete",
    "gdpr-data-compliance.offender.deleted",
    "This offender's data has been deleted with exception to the 'Base Record' from NOMIS"
  ),
  DECEASED_OFFENDER_DELETION(
    "DeceasedOffenderDelete",
    "gdpr-data-compliance.deceased-offender.deleted",
    "All of this offender's data has been deleted from NOMIS"
  ),
  OFFENDER_NO_BOOKING_DELETION(
    "OffenderNoBookingDelete",
    "gdpr-data-compliance.offender-no-booking.deleted",
    "All of this offender's data has been deleted from NOMIS"
  )
}
