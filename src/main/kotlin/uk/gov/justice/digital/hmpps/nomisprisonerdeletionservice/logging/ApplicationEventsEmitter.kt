package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.logging

import com.microsoft.applicationinsights.TelemetryClient
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener

@Component
class ApplicationInsightsEventEmitter(
  val telemetryClient: TelemetryClient
) {

  @TransactionalEventListener
  fun deletionEventListener(deletionEvent: DeletionEvent) {
    deletionEvent.apply {
      telemetryClient.trackEvent(
        event,
        mutableMapOf("offenderNo" to offenderNumber, "count" to offenderIds?.size.toString()),
        null
      )
    }
  }
}

data class DeletionEvent(
  val event: String,
  val offenderIds: Set<Long>?,
  val offenderNumber: String
)
