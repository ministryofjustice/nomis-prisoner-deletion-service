package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.e2e

import org.awaitility.kotlin.await
import org.awaitility.kotlin.matches
import org.awaitility.kotlin.untilCallTo
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.andVerifyBodyContains
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.executeScripts
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.integration.IntegrationTestBase
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import javax.sql.DataSource

@Import(DataComplianceMessageTest.TestClockConfiguration::class)
class DataComplianceMessageTest : IntegrationTestBase() {

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
         "birthDate":"1966-01-01",
         "offenderAliases":[
            {
               "offenderId":-1020,
               "bookings":[
                  {
                     "offenderBookId":-20,
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
         "birthDate":"1966-01-01",
         "offenderAliases":[
            {
               "offenderId":-1020,
               "bookings":[
                  {
                     "offenderBookId":-20,
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
         "offenderIdDisplay":"Z0020ZZ",
         "batchId":123,
         "firstName":"BURT",
         "lastName":"REYNOLDS",
         "birthDate":"1966-01-01",
         "offenderAliases":[
            {
               "offenderId":-1020,
               "bookings":[
                  {
                     "offenderBookId":-20,
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
          "retentionCheckId":123
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
          "retentionCheckId":123
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
          "retentionCheckId":123
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
            "restricted":false
       }
          """.trimIndent()
        )
    }
  }

  // @Test TODO: Find solution to hsqldb oracle transaction issue
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
  }

  // @Test TODO: Find solution to hsqldb oracle transaction issue & create a deceased offender
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
                 "batchId":1,
                 "deceasedOffenders":[
                    {
                       "offenderIdDisplay":"Z0023ZZ",
                       "firstName":"GRAYSON",
                       "middleName":"RICHARD",
                       "lastName":"GRAYSON",
                       "birthDate":"1960-01-01",
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
                    {
                       "offenderIdDisplay":"Z0017ZZ",
                       "firstName":"MICHEAL",
                       "middleName":"RICHARD",
                       "lastName":"PETERS",
                       "birthDate":"1972-01-01",
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
}
