package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.sns

import com.amazonaws.services.sns.AmazonSNS
import com.fasterxml.jackson.databind.ObjectMapper
import net.javacrumbs.jsonunit.assertj.assertThatJson
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.kotlin.check
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.justice.hmpps.sqs.HmppsQueueService
import uk.gov.justice.hmpps.sqs.HmppsTopic
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class SnsPublisherTest {

  val hmppsQueueService = mock<HmppsQueueService>()
  val snsClient = mock<AmazonSNS>()
  val clock = Clock.fixed(Instant.parse("2027-03-25T00:00:00.00Z"), ZoneId.systemDefault())

  lateinit var snsPublisher: SnsPublisher

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class DomainEventTopicExists {
    @BeforeEach
    fun setup() {
      val hmppsTopic = mock<HmppsTopic>()
      whenever(hmppsQueueService.findByTopicId("domainevents")).thenReturn(hmppsTopic)
      whenever(hmppsTopic.snsClient).thenReturn(snsClient)
      snsPublisher = SnsPublisher(hmppsQueueService, ObjectMapper())
    }

    @Test
    fun `send offender deletion event`() {
      snsPublisher.sendOffenderDeletionEvent(
        "someEventType", "someDescription", "AA1234A", setOf(1, 2, 3), 123, LocalDateTime.now(clock)
      )

      verify(snsClient).publish(
        check {
          assertThatJson(it.message).isEqualTo(
            """
       {
          "eventType":"someEventType",
          "additionalInformation":{
             "id":123
          },
          "personReference":{
             "identifiers":[
                {
                   "type":"NOMS Number",
                   "value":"AA1234A"
                },
                {
                   "type":"offenderId",
                   "value":"1"
                },
                {
                   "type":"offenderId",
                   "value":"2"
                },
                {
                   "type":"offenderId",
                   "value":"3"
                }
             ]
          },
          "version":1,
          "occurredAt":"2027-03-25T00:00:00Z",
          "description":"someDescription"
       }
            """.trimIndent()

          )
        }
      )
    }
  }

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class DomainEventTopicDoesNotExist {

    @BeforeEach
    fun setup() {
      whenever(hmppsQueueService.findByTopicId("domainevents")).thenReturn(null)
    }

    @Test
    fun `send offender deletion event throws when topic does not exist`() {

      snsPublisher = SnsPublisher(hmppsQueueService, ObjectMapper())

      assertThatThrownBy {
        snsPublisher.sendOffenderDeletionEvent(
          "someEventType",
          "someDescription",
          "AA1234A",
          setOf(1, 2, 3),
          123,
          LocalDateTime.now(clock)
        )
      }.isInstanceOf(RuntimeException::class.java)
        .hasMessageContaining("Topic with name domainevents doesn't exist")
    }
  }
}
