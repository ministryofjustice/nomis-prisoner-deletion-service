package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.e2e

import com.microsoft.applicationinsights.TelemetryClient
import org.awaitility.kotlin.await
import org.awaitility.kotlin.matches
import org.awaitility.kotlin.untilCallTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockReset
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.andVerifyBodyContains
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.executeScripts
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.OffenderDeletionRepository
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import javax.sql.DataSource

@Import(DataComplianceMessageTest.TestClockConfiguration::class)
class DataComplianceMessageTest : IntegrationTestBase() {

  @SpyBean
  lateinit var offenderDeletionRepository: OffenderDeletionRepository

  @SpyBean(reset = MockReset.BEFORE)
  lateinit var telemetryClient: TelemetryClient

  @TestConfiguration
  class TestClockConfiguration {
    @Bean
    @Primary
    fun clock(): Clock {
      return Clock.fixed(Instant.parse("2027-03-25T00:00:00.00Z"), ZoneId.systemDefault())
    }
  }

  @BeforeEach
  fun init() {
    await untilCallTo { getNumberOfMessagesCurrentlyOnRequestQueue() } matches { it == 0 }
  }

  @Test
  fun `should handle referral request`() {

    requestAwsSqsClient.sendMessage(
      messageHelper.requestMessageWithEventType("DATA_COMPLIANCE_REFERRAL-REQUEST")
        .withMessageBody(
          """
        {
           "batchId":123,
           "dueForDeletionWindowStart":"2027-03-24",
           "dueForDeletionWindowEnd":"2027-04-24",
           "limit":10
        }
          """.trimIndent()
        )
    )

    messageHelper.verifyAtLeastOneResponseOfEventType("DATA_COMPLIANCE_OFFENDER-PENDING-DELETION")
      .andVerifyBodyContains(
        """
      {
         "offenderIdDisplay":"Z0020ZZ",
         "batchId":123,
         "firstName":"BURT",
         "lastName":"REYNOLDS",
         "pncs":["99/1234567B","20/9N"],
         "cros":["99/123456L","11/1X","99/12345M"],         
         "birthDate":"1966-01-01",
         "offenderAliases":[
            {
               "offenderId":-1020,
               "bookings":[
                  {
                     "offenderBookId":-20,
                     "bookingNo":"Z00020",
                     "offenceCodes":[
                        "RC86355"
                     ],
                     "alertCodes":[
                        
                     ]
                  }
               ]
            }
         ]
      }
        """.trimIndent()
      )

    messageHelper.verifyAtLeastOneResponseOfEventType("DATA_COMPLIANCE_OFFENDER-PENDING-DELETION-REFERRAL-COMPLETE")
      .andVerifyBodyContains(
        """
       {
         "batchId":123,
         "numberReferred":1,
         "totalInWindow":1
       }
        """.trimIndent()
      )
  }

  @Test
  fun `handle AdHoc referral request`() {

    requestAwsSqsClient.sendMessage(
      messageHelper.requestMessageWithEventType("DATA_COMPLIANCE_AD-HOC-REFERRAL-REQUEST")
        .withMessageBody(
          """
         {
           "offenderIdDisplay":"Z0020ZZ",
           "batchId":123
         }
          """.trimIndent()
        )
    )
    messageHelper.verifyAtLeastOneResponseOfEventType("DATA_COMPLIANCE_OFFENDER-PENDING-DELETION")
      .andVerifyBodyContains(
        """
      {
         "offenderIdDisplay":"Z0020ZZ",
         "batchId":123,
         "firstName":"BURT",
         "lastName":"REYNOLDS",
         "pncs":["99/1234567B","20/9N"],
         "cros":["99/123456L","11/1X","99/12345M"],
         "birthDate":"1966-01-01",
         "offenderAliases":[
            {
               "offenderId":-1020,
               "bookings":[
                  {
                     "offenderBookId":-20,
                     "bookingNo":"Z00020",
                     "offenceCodes":[
                        "RC86355"
                     ],
                     "alertCodes":[
                        
                     ]
                  }
               ]
            }
         ]
      }
        """.trimIndent()
      )
  }

  @Test
  fun `handle provisional deletion referral request`() {

    requestAwsSqsClient.sendMessage(
      messageHelper.requestMessageWithEventType("PROVISIONAL_DELETION_REFERRAL_REQUEST")
        .withMessageBody(
          """
      {
         "offenderIdDisplay":"Z0020ZZ",
         "referralId":123
      }
          """.trimIndent()
        )
    )

    messageHelper.verifyAtLeastOneResponseOfEventType("DATA_COMPLIANCE_OFFENDER_PROVISIONAL_DELETION_REFERRAL")
      .andVerifyBodyContains(
        """
     {
         "referralId":123,
         "offenderIdDisplay":"Z0020ZZ",
         "subsequentChangesIdentified":false,
         "offenceCodes":[
            "RC86355"
         ],
         "alertCodes":[
           
         ]
    }
        """.trimIndent()
      )
  }

  @Test
  fun `handle duplicate id check`() {

    requestAwsSqsClient.sendMessage(
      messageHelper.requestMessageWithEventType("DATA_COMPLIANCE_DATA-DUPLICATE-ID-CHECK")
        .withMessageBody(
          """
       {
          "offenderIdDisplay":"A1234AA",
          "retentionCheckId":123
       }
          """.trimIndent()
        )
    )

    messageHelper.verifyAtLeastOneResponseOfEventType("DATA_COMPLIANCE_DATA-DUPLICATE-ID-RESULT")
      .andVerifyBodyContains(
        """
       {
          "offenderIdDisplay":"A1234AA",
          "retentionCheckId":123,
          "duplicateOffenders":[]
       }
        """.trimIndent()
      )
  }

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class DataDuplicateTests {
    @BeforeAll
    fun setup(@Autowired dataSource: DataSource) {
      executeScripts(
        dataSource,
        "define_regexp_substr.sql",
        "define_jaro_winkler_similarity.sql",
        "mv_offender_match_details.sql"
      )
    }

    @Test
    fun `handle duplicate data check`() {

      requestAwsSqsClient.sendMessage(
        messageHelper.requestMessageWithEventType("DATA_COMPLIANCE_DATA-DUPLICATE-DB-CHECK")
          .withMessageBody(
            """
       {
          "offenderIdDisplay":"A1234AA",
          "retentionCheckId":123
       }
            """.trimIndent()
          )
      )

      messageHelper.verifyAtLeastOneResponseOfEventType("DATA_COMPLIANCE_DATA-DUPLICATE-DB-RESULT")
        .andVerifyBodyContains(
          """
      {
           "offenderIdDisplay":"A1234AA",
           "retentionCheckId":123,
           "duplicateOffenders":[
              "B1234BB",
              "C1234CC",
              "D1234DD",
              "E1234EE"
           ]
      }
          """.trimIndent()
        )
    }
  }

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class RegexTests {
    @BeforeAll
    fun setup(@Autowired dataSource: DataSource) {
      executeScripts(dataSource, "define_regexp_like.sql")
    }

    @Test
    fun `handle free text check`() {

      requestAwsSqsClient.sendMessage(
        messageHelper.requestMessageWithEventType("DATA_COMPLIANCE_FREE-TEXT-MORATORIUM-CHECK")
          .withMessageBody(
            """
       {
          "offenderIdDisplay":"A1234AA",
          "retentionCheckId":123,
          "regex":["^(regex|1)$","^(regex|2)$"]
       }
            """.trimIndent()
          )
      )

      messageHelper.verifyAtLeastOneResponseOfEventType("DATA_COMPLIANCE_FREE-TEXT-MORATORIUM-RESULT")
        .andVerifyBodyContains(
          """
       {
          "offenderIdDisplay":"A1234AA",
          "retentionCheckId":123,
          "matchingTables":[]
       }
          """.trimIndent()
        )
    }

    @Test
    fun `handle offender restriction check`() {

      requestAwsSqsClient.sendMessage(
        messageHelper.requestMessageWithEventType("DATA_COMPLIANCE_OFFENDER-RESTRICTION-CHECK")
          .withMessageBody(
            """
       {
            "offenderIdDisplay":"A1234AA",
            "retentionCheckId":123,
            "restrictionCodes":["CHILD"],
            "regex":"^(regex|1)$"
       }
            """.trimIndent()
          )
      )

      messageHelper.verifyAtLeastOneResponseOfEventType("DATA_COMPLIANCE_OFFENDER-RESTRICTION-RESULT")
        .andVerifyBodyContains(
          """
       {
            "offenderIdDisplay":"A1234AA",
            "retentionCheckId":123,
            "restricted":true
       }
          """.trimIndent()
        )
    }
  }

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class DeleteOffenderData {

    @BeforeEach
    fun setup() {
      doNothing().whenever(offenderDeletionRepository).setContext(any())
    }

    @AfterEach
    fun afterAll() {
      telemetryClient.flush()
    }

    @Test
    fun `handle offender deletion event`() {

      requestAwsSqsClient.sendMessage(
        messageHelper.requestMessageWithEventType("DATA_COMPLIANCE_OFFENDER-DELETION-GRANTED")
          .withMessageBody(
            """
       {
           "offenderIdDisplay":"A1234AA",
           "referralId":123,
           "offenderIds":[-1001],
           "offenderBookIds":[-1]}
       }
            """.trimIndent()
          )
      )

      messageHelper.verifyAtLeastOneResponseOfEventType("DATA_COMPLIANCE_OFFENDER-DELETION-COMPLETE")
        .andVerifyBodyContains(
          """
       {
           "offenderIdDisplay":"A1234AA",
           "referralId":123
       }
          """.trimIndent()
        )

      verify(telemetryClient).trackEvent(
        "OffenderDelete",
        mapOf(
          "offenderNo" to "A1234AA",
          "count" to "1"
        ),
        null
      )
    }
  }

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class DeleteDeceasedOffender {

    @BeforeEach
    fun setup() {
      doNothing().whenever(offenderDeletionRepository).setContext(any())
    }

    @Test
    fun `handle deceased offender deletion`() {

      requestAwsSqsClient.sendMessage(
        messageHelper.requestMessageWithEventType("DATA_COMPLIANCE_DECEASED-OFFENDER-DELETION-REQUEST")
          .withMessageBody(
            """
       {
           "batchId":987,
           "limit":10
       }
            """.trimIndent()
          )
      )

      messageHelper.verifyAtLeastOneResponseOfEventType("DATA_COMPLIANCE_DECEASED-OFFENDER-DELETION-RESULT")
        .andVerifyBodyContains(
          """
                    {
   "batchId":987,
   "deceasedOffenders":[
      {
         "offenderIdDisplay":"Z0023ZZ",
         "firstName":"RICHARD",
         "middleName":null,
         "lastName":"GRAYSON",
         "birthDate":"1960-01-01",
         "deceasedDate":"1988-05-01",
         "deletionDateTime":"2027-03-25 00:00:00",
         "agencyLocationId":"NHS014",
         "offenderAliases":[
            {
               "offenderId":-1023,
               "offenderBookIds":[
                  
               ]
            }
         ]
      },
      {
         "offenderIdDisplay":"Z0017ZZ",
         "firstName":"MICHEAL",
         "middleName":null,
         "lastName":"PETERS",
         "birthDate":"1972-01-01",
         "deceasedDate":"1986-05-01",
         "deletionDateTime":"2027-03-25 00:00:00",
         "agencyLocationId":"NHS014",
         "offenderAliases":[
            {
               "offenderId":-1017,
               "offenderBookIds":[
                  
               ]
            }
         ]
      }
   ]
}
          """.trimIndent()
        )
    }
  }

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class DeleteOffenderWithNoBooking {

    @BeforeEach
    fun setup() {
      doNothing().whenever(offenderDeletionRepository).setContext(any())
    }

    @Test
    fun `handle offender no booking deletion`() {

      requestAwsSqsClient.sendMessage(
        messageHelper.requestMessageWithEventType("DATA_COMPLIANCE_OFFENDER-NO_BOOKING-DELETION-REQUEST")
          .withMessageBody(
            """
       {
           "batchId":987,
           "limit":10
       }
            """.trimIndent()
          )
      )

      messageHelper.verifyAtLeastOneResponseOfEventType("DATA_COMPLIANCE_OFFENDER-NO_BOOKING-DELETION-RESULT")
        .andVerifyBodyContains(
          """
           {
             "batchId":987,
             "offenders":[
                {
                   "offenderIdDisplay":"A1234AN",
                   "firstName":"CHESNEY",
                   "middleName":null,
                   "lastName":"THOMSON",
                   "birthDate":"1980-01-02",
                   "offenderAliases":[
                      {
                         "offenderId":-1096
                      }
                   ]
                },
                {
                   "offenderIdDisplay":"A1234AO",
                   "firstName":"TREVOR",
                   "middleName":null,
                   "lastName":"SMITH",
                   "birthDate":"1998-11-01",
                   "offenderAliases":[
                      {
                         "offenderId":-1092
                      },
                      {
                         "offenderId":-1093
                      },
                      {
                         "offenderId":-1094
                      },
                      {
                         "offenderId":-1095
                      }
                   ]
                },
                {
                   "offenderIdDisplay":"A9880GH",
                   "firstName":"BRIAN",
                   "middleName":null,
                   "lastName":"JONES",
                   "birthDate":"1961-01-01",
                   "offenderAliases":[
                      {
                         "offenderId":-1059
                      }
                   ]
                },
                                {
                                   "offenderIdDisplay":"A1234DD",
                                   "firstName":"JOHN",
                                   "middleName":null,
                                   "lastName":"DOE",
                                   "birthDate":"1989-03-02",
                                   "offenderAliases":[
                                      {
                                         "offenderId":-1056
                                      },
                                      {
                                         "offenderId":-1057
                                      },
                                      {
                                         "offenderId":-1058
                                      }
                                   ]
                                }
             ]
          }
          """.trimIndent()
        )
    }
  }
}
