package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.NullSource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.messaging.Message
import org.springframework.messaging.MessageHeaders
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.event.consumer.dto.OffenderRestrictionRequest
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service.DataDuplicateService
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service.DeceasedOffenderDeletionService
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service.FreeTextSearchService
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service.OffenderDeletionService
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service.OffenderRestrictionService
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service.ReferralService
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.validation.Validator
import java.time.LocalDate
import javax.validation.ConstraintViolationException
import javax.validation.Validation

internal class DataComplianceEventListenerTest {
  private val referralService = mock<ReferralService>()
  private val dataDuplicateService = mock<DataDuplicateService>()
  private val freeTextSearchService = mock<FreeTextSearchService>()
  private val offenderRestrictionService = mock<OffenderRestrictionService>()
  private val offenderDeletionService = mock<OffenderDeletionService>()
  private val deceasedOffenderDeletionService = mock<DeceasedOffenderDeletionService>()

  private val listener =
    DataComplianceEventListener(
      referralService = referralService,
      dataDuplicateService = dataDuplicateService,
      freeTextSearchService = freeTextSearchService,
      offenderRestrictionService = offenderRestrictionService,
      offenderDeletionService = offenderDeletionService,
      deceasedOffenderDeletionService = deceasedOffenderDeletionService,
      mapper = ObjectMapper(),
      validator = Validator(Validation.buildDefaultValidatorFactory().validator)
    )

  @Test
  fun `handle referral request`() {
    handleMessage(
      """
        {
           "batchId":123,
           "dueForDeletionWindowStart":"2020-01-01",
           "dueForDeletionWindowEnd":"2020-02-02",
           "limit":10
        }
      """.trimIndent(),
      mapOf("eventType" to "DATA_COMPLIANCE_REFERRAL-REQUEST")
    )

    verify(referralService).referOffendersForDeletion(
      123L,
      LocalDate.of(2020, 1, 1),
      LocalDate.of(2020, 2, 2),
      PageRequest.of(0, 10)
    )
  }

  @Test
  fun `handle referral request when optional limit omitted`() {
    handleMessage(
      """
        {
           "batchId":123,
           "dueForDeletionWindowStart":"2020-01-01",
           "dueForDeletionWindowEnd":"2020-02-02"
        }
      """.trimIndent(),
      mapOf("eventType" to "DATA_COMPLIANCE_REFERRAL-REQUEST")
    )
    verify(referralService).referOffendersForDeletion(
      123L,
      LocalDate.of(2020, 1, 1),
      LocalDate.of(2020, 2, 2),
      Pageable.unpaged()
    )
  }

  @Test
  fun `handle referral request throws exception when batch id is null`() {
    assertThatThrownBy {
      handleMessage(
        """
          {
             "dueForDeletionWindowStart":"2020-01-01",
             "dueForDeletionWindowEnd":"2020-02-02"
          }
        """.trimIndent(),
        mapOf("eventType" to "DATA_COMPLIANCE_REFERRAL-REQUEST")
      )
    }
      .isInstanceOf(ConstraintViolationException::class.java)
      .hasMessageContaining("batchId: No batch ID specified in request")
    verifyNoInteractions(referralService)
  }

  @Test
  fun `handle referral request throws exception when window start time is null`() {
    assertThatThrownBy {
      handleMessage(
        """
          {
             "batchId" : 123,
             "dueForDeletionWindowEnd":"2020-02-02",
             "limit":10
          }
        """.trimIndent(),
        mapOf("eventType" to "DATA_COMPLIANCE_REFERRAL-REQUEST")
      )
    }
      .isInstanceOf(ConstraintViolationException::class.java)
      .hasMessageContaining("dueForDeletionWindowStart: No window start date specified in request")
    verifyNoInteractions(referralService)
  }

  @Test
  fun `handle referral request throws exception when window end time is null`() {
    assertThatThrownBy {
      handleMessage(
        """
        {
           "batchId":123,
           "dueForDeletionWindowStart":"2020-01-01",
           "limit":10
        }
        """.trimIndent(),
        mapOf("eventType" to "DATA_COMPLIANCE_REFERRAL-REQUEST")
      )
    }
      .isInstanceOf(ConstraintViolationException::class.java)
      .hasMessageContaining("dueForDeletionWindowEnd: No window end date specified in request")
    verifyNoInteractions(referralService)
  }

  @Test
  fun `handle AdHoc referral request`() {
    handleMessage(
      """
        {"offenderIdDisplay":"A1234AA",
        "batchId":123}"""
        .trimIndent(),

      mapOf("eventType" to "DATA_COMPLIANCE_AD-HOC-REFERRAL-REQUEST")
    )
    verify(referralService).referAdHocOffenderDeletion("A1234AA", 123L)
  }

  @Test
  fun `handle AdHoc referral request throws if OffenderIdDisplay is empty`() {
    assertThatThrownBy {
      handleMessage(
        """
          {"offenderIdDisplay":"",
          "batchId":123}
        """.trimIndent(),
        mapOf("eventType" to "DATA_COMPLIANCE_AD-HOC-REFERRAL-REQUEST")
      )
    }
      .isInstanceOf(ConstraintViolationException::class.java)
      .hasMessageContaining("No offender ID specified in request")
    verifyNoInteractions(referralService)
  }

  @Test
  fun `handle AdHoc referral request throws if Batch Id is null`() {
    assertThatThrownBy {
      handleMessage(
        """{"offenderIdDisplay":"A1234AA"}""",
        mapOf("eventType" to "DATA_COMPLIANCE_AD-HOC-REFERRAL-REQUEST")
      )
    }
      .isInstanceOf(ConstraintViolationException::class.java)
      .hasMessageContaining("No batch ID specified in request")
    verifyNoInteractions(referralService)
  }

  @Test
  fun `handle provisional deletion referral request`() {
    handleMessage(
      "{\"offenderIdDisplay\":\"A1234AA\",\"referralId\":123}",
      mapOf("eventType" to "PROVISIONAL_DELETION_REFERRAL_REQUEST")
    )
    verify(referralService).referProvisionalDeletion("A1234AA", 123L)
  }

  @ParameterizedTest
  @NullAndEmptySource
  fun `handle provisional deletion referral request throws if OffenderIdDisplay null or empty`(offenderIdDisplay: String?) {
    assertThatThrownBy {
      handleMessage(
        "{\"offenderIdDisplay\":\"$offenderIdDisplay\"}",
        mapOf("eventType" to "PROVISIONAL_DELETION_REFERRAL_REQUEST")
      )
    }
      .isInstanceOf(ConstraintViolationException::class.java)
    verifyNoInteractions(offenderRestrictionService)
  }

  @ParameterizedTest
  @NullSource
  fun `handle provisional deletion referral request throws if referral id null or empty`(referralId: String?) {
    assertThatThrownBy {
      handleMessage(
        """
          {"offenderIdDisplay":"A1234AA","referralId":$referralId}
        """.trimIndent(),
        mapOf("eventType" to "PROVISIONAL_DELETION_REFERRAL_REQUEST")
      )
    }.isInstanceOf(ConstraintViolationException::class.java)
    verifyNoInteractions(offenderRestrictionService)
  }

  @Test
  fun `handle duplicate id check`() {
    handleMessage(
      """
       {"offenderIdDisplay":"A1234AA","retentionCheckId":123}
      """.trimIndent(),
      mapOf("eventType" to "DATA_COMPLIANCE_DATA-DUPLICATE-ID-CHECK")
    )
    verify(dataDuplicateService).checkForDuplicateIds("A1234AA", 123L)
  }

  @Test
  fun `handle duplicate id check throws if offender id display empty`() {
    assertThatThrownBy {
      handleMessage(
        """
          {"offenderIdDisplay":"","retentionCheckId":123}
        """.trimIndent(),
        mapOf("eventType" to "DATA_COMPLIANCE_DATA-DUPLICATE-ID-CHECK")
      )
    }
      .isInstanceOf(ConstraintViolationException::class.java)
      .hasMessageContaining("No offender ID specified in request")
    verifyNoInteractions(offenderDeletionService)
  }

  @Test
  fun `handle duplicate id check throws if retention check id null`() {
    assertThatThrownBy {
      handleMessage(
        """
          {"offenderIdDisplay":"A1234AA"}
        """.trimIndent(),
        mapOf("eventType" to "DATA_COMPLIANCE_DATA-DUPLICATE-ID-CHECK")
      )
    }
      .isInstanceOf(ConstraintViolationException::class.java)
      .hasMessageContaining("No retention check ID specified in request")
    verifyNoInteractions(offenderDeletionService)
  }

  @Test
  fun `handle duplicate data check`() {
    handleMessage(
      """
        {"offenderIdDisplay":"A1234AA",
        "retentionCheckId":123}
      """.trimIndent(),
      mapOf("eventType" to "DATA_COMPLIANCE_DATA-DUPLICATE-DB-CHECK")
    )
    verify(dataDuplicateService).checkForDataDuplicates("A1234AA", 123L)
  }

  @Test
  fun `handle duplicate data check throws if offender id display empty`() {
    assertThatThrownBy {
      handleMessage(
        """
          {"offenderIdDisplay":"","retentionCheckId":123}
        """.trimIndent(),
        mapOf("eventType" to "DATA_COMPLIANCE_DATA-DUPLICATE-DB-CHECK")
      )
    }
      .isInstanceOf(ConstraintViolationException::class.java)
      .hasMessageContaining("No offender ID specified in request")
    verifyNoInteractions(dataDuplicateService)
  }

  @Test
  fun `handle duplicate data check throws if retention check id null`() {
    assertThatThrownBy {
      handleMessage(
        """
          {"offenderIdDisplay":"A1234AA"}
        """.trimIndent(),
        mapOf("eventType" to "DATA_COMPLIANCE_DATA-DUPLICATE-DB-CHECK")
      )
    }
      .isInstanceOf(ConstraintViolationException::class.java)
      .hasMessageContaining("No retention check ID specified in request")
    verifyNoInteractions(dataDuplicateService)
  }

  @Test
  fun `handle free text check`() {
    handleMessage(
      """
        {"offenderIdDisplay":"A1234AA",
        "retentionCheckId":123,
        "regex":["^(regex|1)$","^(regex|2)$"]}
      """.trimIndent(),
      mapOf("eventType" to "DATA_COMPLIANCE_FREE-TEXT-MORATORIUM-CHECK")
    )
    verify(freeTextSearchService)
      .checkForMatchingContent("A1234AA", 123L, listOf("^(regex|1)$", "^(regex|2)$"))
  }

  @Test
  fun `handle free text check throws if offender id display empty`() {
    assertThatThrownBy {
      handleMessage(
        """
          {"offenderIdDisplay":"",
          "retentionCheckId":123,"regex":["^(some|regex)$"]}
        """.trimIndent(),
        mapOf("eventType" to "DATA_COMPLIANCE_FREE-TEXT-MORATORIUM-CHECK")
      )
    }
      .isInstanceOf(ConstraintViolationException::class.java)
      .hasMessageContaining("No offender ID specified in request")
    verifyNoInteractions(freeTextSearchService)
  }

  @Test
  fun `handle free text check throws if retention check id null`() {
    assertThatThrownBy {
      handleMessage(
        """
          {"offenderIdDisplay":"A1234AA",
          "regex":["^(some|regex)$"]}
        """.trimIndent(),
        mapOf("eventType" to "DATA_COMPLIANCE_FREE-TEXT-MORATORIUM-CHECK")
      )
    }
      .isInstanceOf(ConstraintViolationException::class.java)
      .hasMessageContaining("No retention check ID specified in request")
    verifyNoInteractions(freeTextSearchService)
  }

  @Test
  fun `handle free text check throws if regex list is empty`() {
    assertThatThrownBy {
      handleMessage(
        """
          {"offenderIdDisplay":"A1234AA",
          "retentionCheckId":123,
          "regex":[]}
        """.trimIndent(),
        mapOf("eventType" to "DATA_COMPLIANCE_FREE-TEXT-MORATORIUM-CHECK")
      )
    }
      .isInstanceOf(ConstraintViolationException::class.java)
      .hasMessageContaining("No regex provided in request")
    verifyNoInteractions(freeTextSearchService)
  }

  @Test
  fun `handle free text check throws if regex is empty`() {
    assertThatThrownBy {
      handleMessage(
        """
          {"offenderIdDisplay":"A1234AA",
          "retentionCheckId":123,
          "regex":[""]}
        """.trimIndent(),
        mapOf("eventType" to "DATA_COMPLIANCE_FREE-TEXT-MORATORIUM-CHECK")
      )
    }
      .isInstanceOf(IllegalStateException::class.java)
      .hasMessageContaining("Empty regex provided in request")
    verifyNoInteractions(freeTextSearchService)
  }

  @Test
  fun `handle free text check throws if regex invalid`() {
    assertThatThrownBy {
      handleMessage(
        """
          {"offenderIdDisplay":"A1234AA",
          "retentionCheckId":123,
          "regex":[".**INVALID"]}
        """.trimIndent(),
        mapOf("eventType" to "DATA_COMPLIANCE_FREE-TEXT-MORATORIUM-CHECK")
      )
    }
      .isInstanceOf(IllegalStateException::class.java)
      .hasMessageContaining("Invalid regex provided in request")
    verifyNoInteractions(freeTextSearchService)
  }

  @Test
  fun `handle offender restriction check`() {
    handleMessage(
      """
        {"offenderIdDisplay":"A1234AA",
        "retentionCheckId":123,
        "restrictionCodes":["CHILD"],
        "regex":"^(regex|1)$"}
      """.trimIndent(),
      mapOf("eventType" to "DATA_COMPLIANCE_OFFENDER-RESTRICTION-CHECK")
    )
    verify(offenderRestrictionService).checkForOffenderRestrictions(
      OffenderRestrictionRequest(
        offenderIdDisplay = "A1234AA",
        retentionCheckId = 123L,
        restrictionCodes = setOf("CHILD"),
        regex = "^(regex|1)$"
      )
    )
  }

  @ParameterizedTest
  @NullAndEmptySource
  fun `handle offender restriction check if offender id is null or empty`(offenderId: String?) {
    assertThatThrownBy {
      handleMessage(
        """
          {"offenderIdDisplay":$offenderId,
          "retentionCheckId":123,
          "restrictionCodes":["CHILD"],
          "regex":"^(regex|1)$"}
        """.trimIndent(),
        mapOf("eventType" to "DATA_COMPLIANCE_OFFENDER-RESTRICTION-CHECK")
      )
    }
      .isInstanceOf(IllegalStateException::class.java)
    verifyNoInteractions(offenderRestrictionService)
  }

  @ParameterizedTest
  @NullAndEmptySource
  fun `handle offender restriction check if retention check id is null or empty`(retentionCheckId: String?) {
    assertThatThrownBy {
      handleMessage(
        """
          {"offenderIdDisplay":"A1234AA",
          "retentionCheckId":$retentionCheckId,
          "restrictionCodes":["CHILD"],
          "regex":"^(regex|1)$"}
        """.trimIndent(),
        mapOf("eventType" to "DATA_COMPLIANCE_OFFENDER-RESTRICTION-CHECK")
      )
    }
      .isInstanceOf(RuntimeException::class.java)
    verifyNoInteractions(offenderRestrictionService)
  }

  @ParameterizedTest
  @NullAndEmptySource
  fun `handle offender restriction check if restriction codes are null or empty`(restrictionCodes: String?) {
    assertThatThrownBy {
      handleMessage(
        """
          {"offenderIdDisplay":"A1234AA",
          "retentionCheckId":123,
          "restrictionCodes":$restrictionCodes,
          "regex":"^(regex|1)$"}
        """.trimIndent(),
        mapOf("eventType" to "DATA_COMPLIANCE_OFFENDER-RESTRICTION-CHECK")
      )
    }
      .isInstanceOf(IllegalStateException::class.java)
    verifyNoInteractions(offenderRestrictionService)
  }

  @ParameterizedTest
  @ValueSource(strings = [".**SOME_INVALID_REGEX", "..*SOME_INVALID_REGEX", "?></|'&^%$£@"])
  fun `handle offender restriction check if regex invalid`(regex: String?) {
    assertThatThrownBy {
      handleMessage(
        "{\"offenderIdDisplay\":\"A1234AA\",\"retentionCheckId\":123,\"restrictionCodes\":[\"CHILD\"],\"regex\":$regex}",
        mapOf("eventType" to "DATA_COMPLIANCE_OFFENDER-RESTRICTION-CHECK")
      )
    }
      .isInstanceOf(IllegalStateException::class.java)
    verifyNoInteractions(offenderRestrictionService)
  }

  @ParameterizedTest
  @NullSource
  fun `handle offender restriction check if regex is null`(regex: String?) {
    assertThatThrownBy {
      handleMessage(
        """
          {"offenderIdDisplay":"A1234AA",
          "retentionCheckId":123,
          "restrictionCodes":["CHILD"],
          "regex":$regex}
        """.trimIndent(),
        mapOf("eventType" to "DATA_COMPLIANCE_OFFENDER-RESTRICTION-CHECK")
      )
    }
      .isInstanceOf(ConstraintViolationException::class.java)
    verifyNoInteractions(offenderRestrictionService)
  }

  @Test
  fun `handle offender deletion event`() {
    handleMessage(
      """
        {"offenderIdDisplay":"A1234AA",
        "referralId":123,
        "offenderIds":[456],
        "offenderBookIds":[789]}
      """.trimIndent(),
      mapOf("eventType" to "DATA_COMPLIANCE_OFFENDER-DELETION-GRANTED")
    )
    verify(offenderDeletionService).deleteOffender(
      OffenderDeletionService.OffenderDeletionGrant(
        offenderNo = "A1234AA",
        referralId = 123L,
        offenderIds = setOf(456L),
        offenderBookIds = setOf(789L)
      )
    )
  }

  @Test
  fun `handle offender deletion event throws if offender id display empty`() {
    assertThatThrownBy {
      handleMessage(
        """
          {"offenderIdDisplay":"",
          "referralId":123,
          "offenderIds":[456],
          "offenderBookIds":[789]}
        """.trimIndent(),
        mapOf("eventType" to "DATA_COMPLIANCE_OFFENDER-DELETION-GRANTED")
      )
    }
      .isInstanceOf(ConstraintViolationException::class.java)
      .hasMessageContaining("No offender ID specified in request")
    verifyNoInteractions(offenderDeletionService)
  }

  @Test
  fun `handle offender deletion event throws if referral id null`() {
    assertThatThrownBy {
      handleMessage(
        """
          {"offenderIdDisplay":"A1234AA",
          "offenderIds":[456],
          "offenderBookIds":[789]}
        """.trimIndent(),
        mapOf("eventType" to "DATA_COMPLIANCE_OFFENDER-DELETION-GRANTED")
      )
    }
      .isInstanceOf(ConstraintViolationException::class.java)
      .hasMessageContaining("No referral ID specified in request")
    verifyNoInteractions(offenderDeletionService)
  }

  @Test
  fun `handle deceased offender deletion`() {
    handleMessage(
      """
        {"batchId":987,
        "limit":10}
      """.trimIndent(),
      mapOf("eventType" to "DATA_COMPLIANCE_DECEASED-OFFENDER-DELETION-REQUEST")
    )
    verify(deceasedOffenderDeletionService).deleteDeceasedOffenders(987L, PageRequest.of(0, 10))
  }

  @Test
  fun `handle deceased offender deletion with no limit`() {
    handleMessage(
      """
        {"batchId":987}
      """.trimIndent(),
      mapOf("eventType" to "DATA_COMPLIANCE_DECEASED-OFFENDER-DELETION-REQUEST")
    )
    verify(deceasedOffenderDeletionService).deleteDeceasedOffenders(987L, Pageable.unpaged())
  }

  @Test
  fun `handle deceased offender deletion if batch id is null`() {
    assertThatThrownBy {
      handleMessage(
        """
          {"limit":10}
        """.trimIndent(),
        mapOf("eventType" to "DATA_COMPLIANCE_DECEASED-OFFENDER-DELETION-REQUEST")
      )
    }
      .isInstanceOf(ConstraintViolationException::class.java)
      .hasMessageContaining("No batch ID specified in the request")
    verifyNoInteractions(offenderDeletionService)
  }

  @Test
  fun `handle event throws if message attributes are not present`() {
    assertThatThrownBy {
      handleMessage(
        """
          {"offenderIdDisplay":"A1234AA"}
        """.trimIndent(),
        java.util.Map.of()
      )
    }
      .isInstanceOf(IllegalStateException::class.java)
      .hasMessage("Message event type not found")
    verifyNoInteractions(offenderDeletionService)
  }

  @Test
  fun `handle event throws if event type is unexpected`() {
    assertThatThrownBy {
      handleMessage(
        """
          {"offenderIdDisplay":"A1234AA"}
        """.trimIndent(),
        mapOf("eventType" to "UNEXPECTED!")
      )
    }
      .isInstanceOf(IllegalStateException::class.java)
      .hasMessageContaining("Unexpected message event type: 'UNEXPECTED!'")
    verifyNoInteractions(offenderDeletionService)
  }

  @Test
  fun `handle event throws if message is not present`() {
    assertThatThrownBy {
      handleMessage(
        " ",
        mapOf("eventType" to "DATA_COMPLIANCE_OFFENDER-DELETION-GRANTED")
      )
    }
      .isInstanceOf(IllegalStateException::class.java)
      .hasMessage("Failed to parse request:  ")
    verifyNoInteractions(offenderDeletionService)
  }

  @Test
  fun `handle event throws if message is un-parsable`() {
    assertThatThrownBy {
      handleMessage(
        "BAD MESSAGE!",
        mapOf("eventType" to "DATA_COMPLIANCE_OFFENDER-DELETION-GRANTED")
      )
    }
      .isInstanceOf(RuntimeException::class.java)
      .hasMessageContaining("Failed to parse request")
    verifyNoInteractions(offenderDeletionService)
  }

  private fun handleMessage(payload: String, headers: Map<String, Any>) {
    listener.handleEvent(mockMessage(payload, headers))
  }

  private fun mockMessage(payload: String, headers: Map<String, Any>): Message<String> {
    @Suppress("UNCHECKED_CAST")
    val message = Mockito.mock(Message::class.java) as Message<String>
    whenever(message.headers).thenReturn(MessageHeaders(headers))
    whenever(message.payload).thenReturn(payload)
    return message
  }
}
