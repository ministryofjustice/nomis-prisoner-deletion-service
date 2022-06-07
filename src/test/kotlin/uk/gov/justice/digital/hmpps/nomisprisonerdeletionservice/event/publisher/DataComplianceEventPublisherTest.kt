package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.GetQueueAttributesResult
import com.amazonaws.services.sqs.model.GetQueueUrlResult
import com.amazonaws.services.sqs.model.SendMessageResult
import com.fasterxml.jackson.databind.ObjectMapper
import net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.kotlin.any
import org.mockito.kotlin.check
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.DataDuplicateResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.DeceasedOffenderDeletionResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.DeceasedOffenderDeletionResult.DeceasedOffender
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.FreeTextSearchResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderDeletionComplete
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderNoBookingDeletionResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderPendingDeletionReferralComplete
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.OffenderRestrictionResult
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.publisher.dto.ProvisionalDeletionReferralResult
import uk.gov.justice.hmpps.sqs.HmppsQueue
import uk.gov.justice.hmpps.sqs.HmppsQueueService
import java.time.LocalDate
import java.time.LocalDateTime

internal class DataComplianceEventPublisherTest {

  private val hmppsQueueService = mock<HmppsQueueService>()
  private val responseSqsClient = mock<AmazonSQS>()
  private val responseSqsDlqClient = mock<AmazonSQS>()
  private lateinit var dataComplianceEventPublisher: DataComplianceEventPublisher

  @BeforeEach
  internal fun setUp() {
    whenever(hmppsQueueService.findByQueueId("datacomplianceresponse")).thenReturn(
      HmppsQueue(
        "datacomplianceresponse",
        responseSqsClient,
        "response-queue",
        responseSqsDlqClient,
        "response-dlq"
      )
    )
    whenever(responseSqsClient.getQueueUrl("response-queue")).thenReturn(GetQueueUrlResult().withQueueUrl("arn:eu-west-1:response-queue"))
    whenever(responseSqsDlqClient.getQueueUrl("response-dlq")).thenReturn(GetQueueUrlResult().withQueueUrl("arn:eu-west-1:response-dlq"))
    dataComplianceEventPublisher = DataComplianceEventPublisher(hmppsQueueService, objectMapper = ObjectMapper())

    whenever(responseSqsClient.sendMessage(any())).thenReturn(SendMessageResult().withMessageId("abc"))
  }

  @Test
  fun `will send offender pending deletion event`() {

    dataComplianceEventPublisher.send(
      OffenderPendingDeletion(
        offenderIdDisplay = "G0913VR",
        batchId = 1,
        firstName = "John",
        middleName = "Middle",
        lastName = "Thompson",
        birthDate = LocalDate.of(1966, 11, 11),
        cros = setOf("11/1X"),
        agencyLocationId = "LEI",
        offenderAliases = listOf(
          OffenderPendingDeletion.OffenderAlias(
            123,
            listOf(
              OffenderPendingDeletion.Booking(
                offenderBookId = 1,
                bookingNo = "B07236",
                offenceCodes = setOf("someOffence"),
                alertCodes = setOf("someAlert")
              )
            )
          )
        )
      )
    )

    verify(responseSqsClient).sendMessage(
      check {
        assertThatJson(it.messageBody).isEqualTo(
          """
            {
               "offenderIdDisplay":"G0913VR",
               "batchId":1,
               "firstName":"John",
               "middleName":"Middle",
               "lastName":"Thompson",
               "birthDate":"1966-11-11",
               "agencyLocationId":"LEI",
               "cros":[ "11/1X" ],
               "pncs":[],
               "offenderAliases":[
                  {
                     "offenderId":123,
                     "bookings":[
                        {
                           "offenderBookId":1,
                           "bookingNo":"B07236",
                           "offenceCodes":[
                              "someOffence"
                           ],
                           "alertCodes":[
                              "someAlert"
                           ]
                        }
                     ]
                  }
               ]
            }
          """.trimIndent()
        )
      }
    )
  }

  @Test
  fun `will send provisional referral deletion result event`() {

    dataComplianceEventPublisher.send(
      ProvisionalDeletionReferralResult(
        1, "G0913VR", false, "LEI",
        setOf("someOffenceCode"), setOf("someAlertCode")
      )
    )

    verify(responseSqsClient).sendMessage(
      check {
        assertThatJson(it.messageBody).isEqualTo(
          """{
                "referralId":1,
                "offenderIdDisplay":"G0913VR",
                "subsequentChangesIdentified":false,
                "agencyLocationId":"LEI",
                "offenceCodes":[
                   "someOffenceCode"
                ],
                "alertCodes":[
                   "someAlertCode"
                ]
              }
          """.trimIndent()
        )
      }
    )
  }

  @Test
  fun `will send Offender pending deletion referral complete event`() {

    dataComplianceEventPublisher.send(
      OffenderPendingDeletionReferralComplete(1, 1, 1)
    )

    verify(responseSqsClient).sendMessage(
      check {
        assertThat(it.messageAttributes.getValue("eventType").toString())
          .contains("DATA_COMPLIANCE_OFFENDER-PENDING-DELETION")

        assertThatJson(it.messageBody).isEqualTo(
          """{
                "batchId": 1,
                "numberReferred":1,
                "totalInWindow":1
             }
          """.trimIndent()
        )
      }
    )
  }

  @Test
  fun `will send Offender deletion complete event`() {

    dataComplianceEventPublisher.send(
      OffenderDeletionComplete("G0913VR", 1)
    )

    verify(responseSqsClient).sendMessage(
      check {

        assertThat(it.messageAttributes.getValue("eventType").toString())
          .contains("DATA_COMPLIANCE_OFFENDER-DELETION-COMPLETE")

        assertThatJson(it.messageBody).isEqualTo(
          """{
                "offenderIdDisplay":"G0913VR",
                "referralId":1
             }
          """.trimIndent()
        )
      }
    )
  }

  @Test
  fun `will send duplicate ID result`() {

    dataComplianceEventPublisher.sendDuplicateIdResult(
      DataDuplicateResult("G0913VR", 1)
    )

    verify(responseSqsClient).sendMessage(
      check {

        assertThat(it.messageAttributes.getValue("eventType").toString())
          .contains("DATA_COMPLIANCE_DATA-DUPLICATE-ID-RESULT")

        assertThatJson(it.messageBody).isEqualTo(
          """{
                "offenderIdDisplay":"G0913VR",
                "retentionCheckId":1,
                "duplicateOffenders":[]
             }
          """.trimIndent()
        )
      }
    )
  }

  @Test
  fun `will send duplicate data result`() {

    dataComplianceEventPublisher.sendDuplicateDataResult(
      DataDuplicateResult("G0913VR", 1)
    )

    verify(responseSqsClient).sendMessage(
      check {

        assertThat(it.messageAttributes.getValue("eventType").toString())
          .contains("DATA_COMPLIANCE_DATA-DUPLICATE-DB-RESULT")

        assertThatJson(it.messageBody).isEqualTo(
          """{
                "offenderIdDisplay":"G0913VR",
                "retentionCheckId":1,
                "duplicateOffenders":[]
             }
          """.trimIndent()
        )
      }
    )
  }

  @Test
  fun `will send Offender restriction result`() {

    dataComplianceEventPublisher.send(
      OffenderRestrictionResult("G0913VR", 1, true)
    )

    verify(responseSqsClient).sendMessage(
      check {

        assertThat(it.messageAttributes.getValue("eventType").toString())
          .contains("DATA_COMPLIANCE_OFFENDER-RESTRICTION-RESULT")

        assertThatJson(it.messageBody).isEqualTo(
          """{
                "offenderIdDisplay":"G0913VR",
                "retentionCheckId":1,
                "restricted":true
             }
          """.trimIndent()
        )
      }
    )
  }

  @Test
  fun `will send Free text search result`() {

    dataComplianceEventPublisher.send(
      FreeTextSearchResult("G0913VR", 1, listOf("someTable, someOtherTable"))
    )

    verify(responseSqsClient).sendMessage(
      check {

        assertThat(it.messageAttributes.getValue("eventType").toString())
          .contains("DATA_COMPLIANCE_FREE-TEXT-MORATORIUM-RESULT")

        assertThatJson(it.messageBody).isEqualTo(
          """{
                "offenderIdDisplay":"G0913VR",
                "retentionCheckId":1,
                "matchingTables":["someTable, someOtherTable"]
             }
          """.trimIndent()
        )
      }
    )
  }

  @Test
  fun `will send offender no booking deletion result`() {

    dataComplianceEventPublisher.send(
      OffenderNoBookingDeletionResult(
        1,
        listOf(
          OffenderNoBookingDeletionResult.Offender(
            "G0913VR", "John", "Middle", "Thompson", LocalDate.of(1966, 11, 11),
            LocalDateTime.of(2000, 11, 11, 1, 1, 1),
            listOf(
              OffenderNoBookingDeletionResult.OffenderAlias(
                123
              )
            )
          )
        )
      )
    )

    verify(responseSqsClient).sendMessage(
      check {

        assertThat(it.messageAttributes.getValue("eventType").toString())
          .contains("DATA_COMPLIANCE_OFFENDER-NO_BOOKING-DELETION-RESULT")

        assertThatJson(it.messageBody).isEqualTo(
          """{
                 "batchId":1,
                 "offenders":[
                    {
                       "offenderIdDisplay":"G0913VR",
                       "firstName":"John",
                       "middleName":"Middle",
                       "lastName":"Thompson",
                       "birthDate":"1966-11-11",
                       "deletionDateTime":"2000-11-11 01:01:01",
                       "offenderAliases":[
                          {
                             "offenderId":123
                          }
                       ]
                    }
                 ]
              }
          """.trimIndent()
        )
      }
    )
  }

  @Test
  fun `will send deceased offender deletion result`() {

    dataComplianceEventPublisher.send(
      DeceasedOffenderDeletionResult(
        1,
        listOf(
          DeceasedOffender(
            "G0913VR", "John", "Middle", "Thompson", LocalDate.of(1966, 11, 11),
            LocalDate.of(2000, 11, 11), LocalDateTime.of(2000, 11, 11, 1, 1, 1),
            "LEI",
            listOf(
              DeceasedOffenderDeletionResult.OffenderAlias(
                123, listOf(1, 2, 3)
              )
            )
          )
        )
      )
    )

    verify(responseSqsClient).sendMessage(
      check {

        assertThat(it.messageAttributes.getValue("eventType").toString())
          .contains("DATA_COMPLIANCE_DECEASED-OFFENDER-DELETION-RESULT")

        assertThatJson(it.messageBody).isEqualTo(
          """{
                 "batchId":1,
                 "deceasedOffenders":[
                    {
                       "offenderIdDisplay":"G0913VR",
                       "firstName":"John",
                       "middleName":"Middle",
                       "lastName":"Thompson",
                       "birthDate":"1966-11-11",
                       "deceasedDate":"2000-11-11",
                       "deletionDateTime":"2000-11-11 01:01:01",
                       "agencyLocationId":"LEI",
                       "offenderAliases":[
                          {
                             "offenderId":123,
                             "offenderBookIds":[
                                1,
                                2,
                                3
                             ]
                          }
                       ]
                    }
                 ]
              }
          """.trimIndent()
        )
      }
    )
  }

  @Nested
  inner class QueueMessageAttributes {
    @Test
    internal fun `async calls for queue status successfully complete`() {
      whenever(responseSqsDlqClient.getQueueUrl("response-dlq"))
        .thenReturn(GetQueueUrlResult().withQueueUrl("arn:eu-west-1:response-dlq"))

      val responseQueueResult = GetQueueAttributesResult().withAttributes(
        mapOf(
          "ApproximateNumberOfMessages" to "7",
          "ApproximateNumberOfMessagesNotVisible" to "2"
        )
      )
      whenever(
        responseSqsClient.getQueueAttributes(
          "arn:eu-west-1:response-queue",
          listOf("ApproximateNumberOfMessages", "ApproximateNumberOfMessagesNotVisible")
        )
      )
        .thenReturn(responseQueueResult)

      val responseDlqResult = GetQueueAttributesResult().withAttributes((mapOf("ApproximateNumberOfMessages" to "5")))
      whenever(
        responseSqsDlqClient.getQueueAttributes(
          "arn:eu-west-1:response-dlq",
          listOf("ApproximateNumberOfMessages")
        )
      )
        .thenReturn(responseDlqResult)

      val queueStatus = dataComplianceEventPublisher.getResponseQueueStatus()
      assertThat(queueStatus.messagesOnQueue).isEqualTo(7)
      assertThat(queueStatus.messagesInFlight).isEqualTo(2)
      assertThat(queueStatus.messagesOnDlq).isEqualTo(5)
    }
  }

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class DataComplianceResponseStatusActive {
    fun activeTestSource() = listOf(
      Arguments.of(0, 0, 0, false),
      Arguments.of(1, 0, 0, true),
      Arguments.of(0, 1, 0, true),
      Arguments.of(0, 0, 1, true),
      Arguments.of(0, 1, 1, true),
      Arguments.of(1, 1, 0, true),
      Arguments.of(0, 1, 1, true),
      Arguments.of(1, 0, 1, true),
      Arguments.of(1, 1, 1, true)
    )

    @ParameterizedTest
    @MethodSource("activeTestSource")
    fun ` queue status active`(
      messagesOnQueue: Int,
      messagesOnDlq: Int,
      messagesInFlight: Int,
      expectedActive: Boolean
    ) {
      assertThat(DataComplianceResponseStatus(messagesOnQueue, messagesOnDlq, messagesInFlight).active).isEqualTo(
        expectedActive
      )
    }
  }
}
