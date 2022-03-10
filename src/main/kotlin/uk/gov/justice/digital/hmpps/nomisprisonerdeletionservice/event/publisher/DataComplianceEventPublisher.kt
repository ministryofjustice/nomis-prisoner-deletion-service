package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.MessageAttributeValue
import com.amazonaws.services.sqs.model.SendMessageRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.DataDuplicateResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.DeceasedOffenderDeletionResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.FreeTextSearchResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderDeletionComplete
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderNoBookingDeletionResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderPendingDeletionReferralComplete
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderRestrictionResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.ProvisionalDeletionReferralResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.utils.toJson
import uk.gov.justice.hmpps.sqs.HmppsQueue
import uk.gov.justice.hmpps.sqs.HmppsQueueService

@Service
class DataComplianceEventPublisher(
  val hmppsQueueService: HmppsQueueService,
  val objectMapper: ObjectMapper
) {

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  val responseQueue by lazy { hmppsQueueService.findByQueueId("datacomplianceresponse") as HmppsQueue }
  val responseSqsClient by lazy { responseQueue.sqsClient }
  val responseSqsDlqClient by lazy { responseQueue.sqsDlqClient as AmazonSQS }
  val responseQueueUrl by lazy { responseQueue.queueUrl }
  val responseDlqUrl by lazy { responseQueue.dlqUrl as String }

  fun send(event: OffenderPendingDeletion) {
    log.info("Sending referral of offender pending deletion: {}", event.offenderIdDisplay)
    responseSqsClient.sendMessage(generateRequest("DATA_COMPLIANCE_OFFENDER-PENDING-DELETION", event))
  }

  fun send(event: ProvisionalDeletionReferralResult) {
    log.info("Sending referral of provisional deletion referral result: {}", event.offenderIdDisplay)
    responseSqsClient.sendMessage(generateRequest("DATA_COMPLIANCE_OFFENDER_PROVISIONAL_DELETION_REFERRAL", event))
  }

  fun send(event: OffenderPendingDeletionReferralComplete) {
    log.info("Sending process completed event for request: {}", event.batchId)
    responseSqsClient.sendMessage(generateRequest("DATA_COMPLIANCE_OFFENDER-PENDING-DELETION-REFERRAL-COMPLETE", event))
  }

  fun send(event: OffenderDeletionComplete) {
    log.info("Sending offender deletion complete event: {}", event.offenderIdDisplay)
    responseSqsClient.sendMessage(generateRequest("DATA_COMPLIANCE_OFFENDER-DELETION-COMPLETE", event))
  }

  fun sendDuplicateIdResult(event: DataDuplicateResult) {
    log.info("Sending duplicate ID result for offender: {}", event.offenderIdDisplay)
    responseSqsClient.sendMessage(generateRequest("DATA_COMPLIANCE_DATA-DUPLICATE-ID-RESULT", event))
  }

  fun sendDuplicateDataResult(event: DataDuplicateResult) {
    log.info("Sending duplicate data result for offender: {}", event.offenderIdDisplay)
    responseSqsClient.sendMessage(generateRequest("DATA_COMPLIANCE_DATA-DUPLICATE-DB-RESULT", event))
  }

  fun send(event: OffenderRestrictionResult) {
    log.info("Sending offender restriction result for offender: {}", event.offenderIdDisplay)
    responseSqsClient.sendMessage(generateRequest("DATA_COMPLIANCE_OFFENDER-RESTRICTION-RESULT", event))
  }

  fun send(event: FreeTextSearchResult) {
    log.info("Sending free text search result for offender: {}", event.offenderIdDisplay)
    responseSqsClient.sendMessage(generateRequest("DATA_COMPLIANCE_FREE-TEXT-MORATORIUM-RESULT", event))
  }

  fun send(event: DeceasedOffenderDeletionResult) {
    log.info("Sending deceased offender result for batch: {}", event.batchId)
    responseSqsClient.sendMessage(generateRequest("DATA_COMPLIANCE_DECEASED-OFFENDER-DELETION-RESULT", event))
  }

  fun send(event: OffenderNoBookingDeletionResult) {
    log.info("Sending offenders with no bookings deletion result for batch: {}", event.batchId)
    responseSqsClient.sendMessage(generateRequest("DATA_COMPLIANCE_OFFENDER-NO_BOOKING-DELETION-RESULT", event))
  }

  fun generateRequest(eventType: String, messageBody: Any): SendMessageRequest? {
    return SendMessageRequest()
      .withQueueUrl(responseQueueUrl)
      .withMessageAttributes(
        mapOf(
          "eventType" to stringAttribute(eventType),
          "contentType" to stringAttribute("application/json;charset=UTF-8")
        )
      )
      .withMessageBody(messageBody.toJson(objectMapper))
  }

  fun getResponseQueueStatus(): DataComplianceResponseStatus {
    val queueAttributes = responseSqsClient.getQueueAttributes(
      responseQueueUrl,
      listOf("ApproximateNumberOfMessages", "ApproximateNumberOfMessagesNotVisible")
    )
    val dlqAttributes = responseSqsDlqClient.getQueueAttributes(responseDlqUrl, listOf("ApproximateNumberOfMessages"))

    return DataComplianceResponseStatus(
      messagesOnQueue = queueAttributes.attributes["ApproximateNumberOfMessages"].toIntOrZero(),
      messagesInFlight = queueAttributes.attributes["ApproximateNumberOfMessagesNotVisible"].toIntOrZero(),
      messagesOnDlq = dlqAttributes.attributes["ApproximateNumberOfMessages"].toIntOrZero(),
    )
  }

  fun stringAttribute(value: String): MessageAttributeValue {
    return MessageAttributeValue()
      .withDataType("String")
      .withStringValue(value)
  }

  fun String?.toIntOrZero() =
    this?.toInt() ?: 0
}

data class DataComplianceResponseStatus(val messagesOnQueue: Int, val messagesOnDlq: Int, val messagesInFlight: Int) {
  val active
    get() = messagesOnQueue > 0 || messagesOnDlq > 0 || messagesInFlight > 0
}
