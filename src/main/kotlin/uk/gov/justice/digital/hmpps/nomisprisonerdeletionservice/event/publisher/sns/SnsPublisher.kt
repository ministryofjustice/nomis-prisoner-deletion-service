package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.sns

import com.amazonaws.services.sns.model.MessageAttributeValue
import com.amazonaws.services.sns.model.PublishRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import uk.gov.justice.hmpps.sqs.HmppsQueueService
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Component
class SnsPublisher(
  val hmppsQueueService: HmppsQueueService,
  val objectMapper: ObjectMapper
) {
  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  private val domaineventsTopic by lazy {
    hmppsQueueService.findByTopicId("domainevents")
      ?: throw RuntimeException("Topic with name domainevents doesn't exist")
  }
  private val domaineventsTopicClient by lazy { domaineventsTopic.snsClient }

  fun sendOffenderDeletionEvent(
    eventType: String,
    eventDescription: String,
    offenderNumber: String,
    offenderIds: Set<Long>,
    eventId: Long,
    occurredAt: LocalDateTime
  ) {
    publishToDomainEventsTopic(
      HMPPSDomainEvent(
        eventType,
        eventId,
        occurredAt.atZone(ZoneId.systemDefault()).toInstant(),
        listOf(Identifier("NOMS Number", offenderNumber)) + offenderIds.map { Identifier("offenderId", it.toString()) },
        eventDescription
      )
    )
  }

  private fun publishToDomainEventsTopic(payload: HMPPSDomainEvent) {
    log.debug("Event {} for id {}", payload.eventType, payload.additionalInformation.id)
    domaineventsTopicClient.publish(
      PublishRequest(domaineventsTopic.arn, objectMapper.writeValueAsString(payload))
        .withMessageAttributes(
          mapOf(
            "eventType" to MessageAttributeValue().withDataType("String").withStringValue(payload.eventType)
          )
        )
        .also { log.info("Published event $payload to outbound topic ${domaineventsTopic.arn}") }
    )
  }
}

data class AdditionalInformation(
  val id: Long

)

data class PersonReference(
  val identifiers: List<Identifier>
)

data class Identifier(
  val type: String,
  val value: String
)

data class HMPPSDomainEvent(
  val eventType: String,
  val additionalInformation: AdditionalInformation,
  val personReference: PersonReference,
  val version: Int,
  val occurredAt: String,
  val description: String
) {
  constructor(
    eventType: String,
    eventId: Long,
    occurredAt: Instant,
    identifiers: List<Identifier>,
    description: String
  ) : this(
    eventType,
    AdditionalInformation(eventId),
    PersonReference(identifiers),
    1,
    occurredAt.toOffsetDateFormat(),
    description
  )
}

fun Instant.toOffsetDateFormat(): String =
  atZone(ZoneId.of("Europe/London")).toOffsetDateTime().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
