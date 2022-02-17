package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.base.Preconditions.checkState
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Pageable.unpaged
import org.springframework.jms.annotation.JmsListener
import org.springframework.messaging.Message
import org.springframework.messaging.MessageHeaders
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.consumer.dto.AdHocReferralRequest
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.consumer.dto.DataDuplicateCheck
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.consumer.dto.DeceasedOffenderDeletionRequest
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.consumer.dto.FreeTextCheck
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.consumer.dto.OffenderDeletionGranted
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.consumer.dto.OffenderRestrictionRequest
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.consumer.dto.ProvisionalDeletionReferralRequest
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.consumer.dto.ReferralRequest
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service.DataDuplicateService
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service.DeceasedOffenderDeletionService
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service.FreeTextSearchService
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service.OffenderDeletionService
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service.OffenderDeletionService.OffenderDeletionGrant
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service.OffenderRestrictionService
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service.ReferralService
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.validation.Validator
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.validation.validateRegex
import java.io.IOException

@Service
final class DataComplianceEventListener(
  val referralService: ReferralService,
  val dataDuplicateService: DataDuplicateService,
  val freeTextSearchService: FreeTextSearchService,
  val offenderRestrictionService: OffenderRestrictionService,
  val offenderDeletionService: OffenderDeletionService,
  val deceasedOffenderDeletionService: DeceasedOffenderDeletionService,
  val mapper: ObjectMapper,
  val validator: Validator
) {
  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  @JmsListener(destination = "datacompliancerequest", containerFactory = "hmppsQueueContainerFactoryProxy")
  fun handleEvent(message: Message<String>) {
    val eventType = getEventType(message.headers)
    log.info("Handling incoming data compliance event of type: {}", eventType)
    messageHandlers[eventType]?.invoke(message)
  }

  fun handleReferralRequest(message: Message<String>) {
    val event: ReferralRequest = parseEvent(message.payload, ReferralRequest::class.java)
    val pageRequest: Pageable = (event.limit?.let { PageRequest.of(0, it) } ?: unpaged())

    referralService.referOffendersForDeletion(
      event.batchId!!,
      event.dueForDeletionWindowStart!!,
      event.dueForDeletionWindowEnd!!,
      pageRequest
    )
  }

  fun handleAdHocReferralRequest(message: Message<String>) {
    val event: AdHocReferralRequest = parseEvent(message.payload, AdHocReferralRequest::class.java)

    referralService.referAdHocOffenderDeletion(
      event.offenderIdDisplay!!,
      event.batchId!!
    )
  }

  fun handleProvisionalDeletionReferralRequest(message: Message<String>) {
    val event: ProvisionalDeletionReferralRequest =
      parseEvent(message.payload, ProvisionalDeletionReferralRequest::class.java)

    referralService.referProvisionalDeletion(event.offenderIdDisplay!!, event.referralId!!)
  }

  fun handleFreeTextMoratoriumCheck(message: Message<String>) {
    val event: FreeTextCheck = parseEvent(message.payload, FreeTextCheck::class.java)
    event.regex.forEach { regex -> validateRegex(regex, message.payload) }

    freeTextSearchService.checkForMatchingContent(event.offenderIdDisplay!!, event.retentionCheckId!!, event.regex)
  }

  fun handleOffenderRestrictionCheck(message: Message<String>) {
    val event: OffenderRestrictionRequest = parseEvent(message.payload, OffenderRestrictionRequest::class.java)
    event.regex?.let { validateRegex(it, message.payload) }

    offenderRestrictionService.checkForOffenderRestrictions(event)
  }

  fun handleDuplicateIdCheck(message: Message<String>) {
    val event: DataDuplicateCheck = parseEvent(message.payload, DataDuplicateCheck::class.java)

    dataDuplicateService.checkForDuplicateIds(event.offenderIdDisplay!!, event.retentionCheckId!!)
  }

  fun handleDuplicateDataCheck(message: Message<String>) {
    val event: DataDuplicateCheck = parseEvent(message.payload, DataDuplicateCheck::class.java)

    dataDuplicateService.checkForDataDuplicates(event.offenderIdDisplay!!, event.retentionCheckId!!)
  }

  fun handleDeletionGranted(message: Message<String>) {
    val event: OffenderDeletionGranted = parseEvent(message.payload, OffenderDeletionGranted::class.java)

    offenderDeletionService.deleteOffender(
      OffenderDeletionGrant(
        offenderNo = event.offenderIdDisplay!!,
        referralId = event.referralId!!,
        offenderIds = event.offenderIds,
        offenderBookIds = event.offenderBookIds
      )
    )
  }

  fun handleDeceasedOffenderDeletionRequest(message: Message<String>) {
    val event: DeceasedOffenderDeletionRequest =
      parseEvent(message.payload, DeceasedOffenderDeletionRequest::class.java)
    val pageRequest: Pageable = (event.limit?.let { PageRequest.of(0, it) } ?: unpaged())

    deceasedOffenderDeletionService.deleteDeceasedOffenders(event.batchId!!, pageRequest)
  }

  private fun getEventType(messageHeaders: MessageHeaders): String {
    val eventType = messageHeaders.get("eventType", String::class.java)
    checkNotNull(eventType) { "Message event type not found" }
    checkState(
      messageHandlers.containsKey(eventType),
      "Unexpected message event type: '$eventType', expecting one of: %s",
      messageHandlers.keys
    )
    return eventType
  }

  val messageHandlers = mapOf(
    "DATA_COMPLIANCE_REFERRAL-REQUEST" to this::handleReferralRequest,
    "DATA_COMPLIANCE_AD-HOC-REFERRAL-REQUEST" to this::handleAdHocReferralRequest,
    "PROVISIONAL_DELETION_REFERRAL_REQUEST" to this::handleProvisionalDeletionReferralRequest,
    "DATA_COMPLIANCE_DATA-DUPLICATE-ID-CHECK" to this::handleDuplicateIdCheck,
    "DATA_COMPLIANCE_DATA-DUPLICATE-DB-CHECK" to this::handleDuplicateDataCheck,
    "DATA_COMPLIANCE_FREE-TEXT-MORATORIUM-CHECK" to this::handleFreeTextMoratoriumCheck,
    "DATA_COMPLIANCE_OFFENDER-RESTRICTION-CHECK" to this::handleOffenderRestrictionCheck,
    "DATA_COMPLIANCE_OFFENDER-DELETION-GRANTED" to this::handleDeletionGranted,
    "DATA_COMPLIANCE_DECEASED-OFFENDER-DELETION-REQUEST" to this::handleDeceasedOffenderDeletionRequest,
  )

  private fun <T> parseEvent(requestJson: String, eventType: Class<T>): T {
    return try {
      mapper.readValue(requestJson, eventType)
    } catch (e: IOException) {
      throw IllegalStateException("Failed to parse request: $requestJson", e)
    }
      .also { validator.validate(it) }
  }
}
