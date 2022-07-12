package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper

import com.amazonaws.services.sqs.model.Message
import com.amazonaws.services.sqs.model.MessageAttributeValue
import com.amazonaws.services.sqs.model.ReceiveMessageRequest
import com.amazonaws.services.sqs.model.SendMessageRequest
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uk.gov.justice.hmpps.sqs.HmppsQueue
import uk.gov.justice.hmpps.sqs.HmppsQueueService

@Component
class MessageHelper {

  @Autowired
  private lateinit var hmppsQueueService: HmppsQueueService

  private var messages = mutableSetOf<Message>()

  internal val requestQueue by lazy { hmppsQueueService.findByQueueId("datacompliancerequest") as HmppsQueue }
  internal val responseQueue by lazy { hmppsQueueService.findByQueueId("datacomplianceresponse") as HmppsQueue }

  internal val requestQueueUrl by lazy { requestQueue.queueUrl }

  internal val responseClient by lazy { responseQueue.sqsClient }
  internal val responseQueueUrl by lazy { responseQueue.queueUrl }

  fun verifyAtLeastOneResponseOfEventType(eventType: String?): Message {
    updateMessages()
    val responseMessages = messages.filter { m: Message -> isMatch(eventType, m) }
    assertThat(responseMessages).hasSizeGreaterThanOrEqualTo(1)
    return responseMessages[0]
  }

  fun verifyNoMessagesSentOfEventType(eventType: String?) {
    updateMessages()
    val responseMessages = messages.filter { m: Message -> isMatch(eventType, m) }
    assertThat(responseMessages).hasSize(0)
  }

  fun requestMessageWithEventType(eventType: String): SendMessageRequest {
    return SendMessageRequest().withQueueUrl(requestQueueUrl)
      .withMessageAttributes(
        mapOf("eventType" to attributeOf(eventType))
      )
  }

  fun attributeOf(eventType: String): MessageAttributeValue =
    MessageAttributeValue().withDataType("String").withStringValue(eventType)

  private fun isMatch(eventType: String?, message: Message): Boolean {
    return message.messageAttributes["eventType"]!!.stringValue == eventType
  }

  fun updateMessages() {
    messages.addAll(
      responseClient.receiveMessage(
        ReceiveMessageRequest()
          .withQueueUrl(responseQueueUrl)
          .withWaitTimeSeconds(15)
          .withMessageAttributeNames("eventType")
      ).messages
    )
  }

  fun clearMessages() {
    messages.clear()
  }
}

infix fun Message.andVerifyBodyContains(json: String): Message {
  assertThat(this.body).isEqualToIgnoringWhitespace(json)
  return this
}
