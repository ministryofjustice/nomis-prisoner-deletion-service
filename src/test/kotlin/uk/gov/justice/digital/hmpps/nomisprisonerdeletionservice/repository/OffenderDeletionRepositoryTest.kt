package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Condition
import org.assertj.core.api.ListAssert
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.integration.IntegrationTestBase
import javax.transaction.Transactional

class OffenderDeletionRepositoryTest : IntegrationTestBase() {

  @Autowired
  lateinit var repository: OffenderDeletionRepository

  @Autowired
  lateinit var jdbcTemplate: JdbcTemplate

  @Test
  @Transactional
  fun `cleanse offender data to base record`() {
    assertOffenderDataExists()
    assertThat(repository.cleanseOffenderDataExcludingBaseRecord("A1234AA")).containsExactly(-1001L)
    assertBaseRecordExists()
    assertNonBaseRecordOffenderDataDeleted()
    assertGlTransactionsAnonymised()
  }

  @Test
  @Transactional
  fun `cleanse offender data using unknown offender throws`() {
    Assertions.assertThatThrownBy { repository.cleanseOffenderDataExcludingBaseRecord("unknown") }
      .isInstanceOf(OffenderDeletionRepository.OffenderNotFoundException::class.java)
      .hasMessage("Offender with id [unknown] not found.")
  }

  @Test
  @Transactional
  fun `delete all offender data including base record`() {
    assertOffenderDataExists()
    assertThat(repository.deleteAllOffenderDataIncludingBaseRecord("A1234AA"))
      .containsExactly(-1001L)
    assertAllOffenderDataDeleted()
    assertGlTransactionsAnonymised()
  }

  @Test
  @Transactional
  fun `delete all offender data using unknown offender throws`() {
    Assertions.assertThatThrownBy {
      repository.deleteAllOffenderDataIncludingBaseRecord(
        "unknown"
      )
    }
      .isInstanceOf(OffenderDeletionRepository.OffenderNotFoundException::class.java)
      .hasMessage("Offender with id [unknown] not found.")
  }

  fun assertOffenderDataExists() {
    checkAllTables(Condition({ list: List<String?> -> !list.isEmpty() }, "Entry Found"))
  }

  fun assertAllOffenderDataDeleted() {
    checkAllTables(Condition({ obj: List<String?> -> obj.isEmpty() }, "Entry Not Found"))
  }

  fun assertNonBaseRecordOffenderDataDeleted() {
    checkNonBaseRecordTables(Condition({ obj: List<String?> -> obj.isEmpty() }, "Entry Not Found"))
  }

  fun assertBaseRecordExists() {
    checkBaseRecord(Condition({ list: List<String?> -> !list.isEmpty() }, "Entry is Found"))
  }

  fun checkAllTables(condition: Condition<in List<String?>>) {
    checkBaseRecord(condition)
    checkNonBaseRecordTables(condition)
  }

  fun checkNonBaseRecordTables(condition: Condition<in List<String?>>) {
    queryForCourtEventCharges().`is`(condition)
    queryByHealthProblemId("OFFENDER_MEDICAL_TREATMENTS").`is`(condition)
    queryByHealthProblemId("OFFENDER_HEALTH_PROBLEMS").`is`(condition)
    queryByProgramId("OFFENDER_COURSE_ATTENDANCES").`is`(condition)
    queryByProgramId("OFFENDER_PRG_PRF_PAY_BANDS").`is`(condition)
    queryByProgramId("OFFENDER_PROGRAM_PROFILES").`is`(condition)
    queryByAgencyIncidentId("AGENCY_INCIDENT_REPAIRS").`is`(condition)
    queryByAgencyIncidentId("AGENCY_INCIDENT_CHARGES").`is`(condition)
    queryByAgencyIncidentId("AGENCY_INCIDENT_PARTIES").`is`(condition)
    queryByAgencyIncidentId("AGENCY_INCIDENTS").`is`(condition)
    queryByIncidentCaseId("INCIDENT_CASES").`is`(condition)
    queryByIncidentCaseId("INCIDENT_CASE_QUESTIONS").`is`(condition)
    queryByIncidentCaseId("INCIDENT_CASE_RESPONSES").`is`(condition)
    queryByIncidentCaseId("INCIDENT_CASE_REQUIREMENTS").`is`(condition)
    queryByOffenderBookId("INCIDENT_CASE_PARTIES").`is`(condition)
    queryByOffenderBookId("BED_ASSIGNMENT_HISTORIES").`is`(condition)
    queryByOffenderBookId("COURT_EVENTS").`is`(condition)
    queryByOffenderBookId("OFFENDER_CASE_NOTES").`is`(condition)
    queryByOffenderBookId("OFFENDER_CASES").`is`(condition)
    queryByOffenderBookId("OFFENDER_CONTACT_PERSONS").`is`(condition)
    queryByOffenderBookId("OFFENDER_CURFEWS").`is`(condition)
    queryByOffenderBookId("OFFENDER_IEP_LEVELS").`is`(condition)
    queryByOffenderBookId("OFFENDER_IMPRISON_STATUSES").`is`(condition)
    queryByOffenderBookId("OFFENDER_IND_SCHEDULES").`is`(condition)
    queryByOffenderBookId("OFFENDER_KEY_DATE_ADJUSTS").`is`(condition)
    queryByOffenderBookId("OFFENDER_KEY_WORKERS").`is`(condition)
    queryByOffenderBookId("OFFENDER_LANGUAGES").`is`(condition)
    queryByOffenderBookId("OFFENDER_OIC_SANCTIONS").`is`(condition)
    queryByOffenderBookId("OFFENDER_PRG_OBLIGATIONS").`is`(condition)
    queryByOffenderBookId("OFFENDER_RELEASE_DETAILS").`is`(condition)
    queryByOffenderBookId("OFFENDER_VISIT_VISITORS").`is`(condition)
    queryByOffenderBookId("OFFENDER_VISITS").`is`(condition)
    queryByOffenderBookId("OFFENDER_VISIT_BALANCES").`is`(condition)
    queryByOffenderBookId("OFFENDER_CHARGES").`is`(condition)
    queryByOffenderBookId("OFFENDER_SENTENCE_TERMS").`is`(condition)
    queryByOffenderBookId("OFFENDER_SENTENCES").`is`(condition)
    queryByOffenderBookId("ORDERS").`is`(condition)
    queryByOffenderBookId("OFFENDER_BELIEFS").`is`(condition)
    queryByRootOffenderId("OFFENDER_IMMIGRATION_APPEALS").`is`(condition)
    queryByOffenderId("GL_TRANSACTIONS").`is`(condition)
    queryByOffenderId("OFFENDER_SUB_ACCOUNTS").`is`(condition)
    queryByOffenderId("OFFENDER_TRANSACTIONS").`is`(condition)
    queryByOffenderId("OFFENDER_TRUST_ACCOUNTS").`is`(condition)
  }

  fun checkBaseRecord(condition: Condition<in List<String?>>) {
    // Identity
    queryByOffenderBookId("OFFENDER_IMAGES").`is`(condition)
    queryByOffenderBookId("OFFENDER_PHYSICAL_ATTRIBUTES").`is`(condition)
    queryByOffenderBookId("OFFENDER_PROFILE_DETAILS").`is`(condition)
    queryByOffenderBookId("OFFENDER_IDENTIFYING_MARKS").`is`(condition)

    // Security
    queryByOffenderBookId("OFFENDER_ALERTS").`is`(condition)
    queryByOffenderBookId("OFFENDER_BOOKING_DETAILS").`is`(condition)
    queryByOffenderBookId("OFFENDER_ASSESSMENTS").`is`(condition)
    queryByOffenderBookId("OFFENDER_ASSESSMENT_ITEMS").`is`(condition)
    queryByOffenderBookId("OFFENDER_GANG_AFFILIATIONS").`is`(condition)
    queryByOffenderBookId("OFFENDER_GANG_EVIDENCES").`is`(condition)
    queryByOffenderBookId("OFFENDER_GANG_INVESTS").`is`(condition)
    queryByOffenderBookId("OFFENDER_NON_ASSOCIATIONS").`is`(condition)
    queryByOffenderBookId("OFFENDER_NA_DETAILS").`is`(condition)

    // Movements
    queryByOffenderBookId("OFFENDER_EXTERNAL_MOVEMENTS").`is`(condition)
    queryByOffenderBookId("OFFENDER_SENT_CALCULATIONS").`is`(condition)
    queryByOffenderBookId("HDC_CALC_EXCLUSION_REASONS").`is`(condition)
    queryByOffenderId("OFFENDER_IDENTIFIERS").`is`(condition)
    queryByOffenderId("OFFENDER_BOOKINGS").`is`(condition)
    queryByOffenderId("OFFENDERS").`is`(condition)
  }

  fun assertGlTransactionsAnonymised() {
    assertThat(
      jdbcTemplate.queryForList(
        "SELECT txn_id FROM gl_transactions WHERE txn_id = 301826802 and gl_entry_seq = 1",
        String::class.java
      )
    )
      .isNotEmpty
  }

  fun queryByAgencyIncidentId(tableName: String): ListAssert<String> {
    return Assertions.assertThat(
      jdbcTemplate.queryForList(
        "SELECT agency_incident_id FROM $tableName WHERE agency_incident_id IN (-6)",
        String::class.java
      )
    )
  }

  fun queryByHealthProblemId(tableName: String): ListAssert<String> {
    return Assertions.assertThat(
      jdbcTemplate.queryForList(
        "SELECT offender_health_problem_id FROM $tableName WHERE offender_health_problem_id IN (-201, -205, -206)",
        String::class.java
      )
    )
  }

  fun queryByProgramId(tableName: String): ListAssert<String> {
    return Assertions.assertThat(
      jdbcTemplate.queryForList(
        "SELECT off_prgref_id FROM $tableName WHERE off_prgref_id IN (-1, -2, -3, -4)",
        String::class.java
      )
    )
  }

  fun queryForCourtEventCharges(): ListAssert<String> {
    return Assertions.assertThat(
      jdbcTemplate.queryForList(
        "SELECT event_id FROM court_event_charges WHERE event_id = -201 AND offender_charge_id = -1",
        String::class.java
      )
    )
  }

  fun queryByIncidentCaseId(tableName: String): ListAssert<String> {
    return Assertions.assertThat(
      jdbcTemplate.queryForList(
        "SELECT incident_case_id FROM $tableName WHERE incident_case_id IN (-1, -2, -3)",
        String::class.java
      )
    )
  }

  fun queryByOffenderBookId(tableName: String): ListAssert<String> {
    return Assertions.assertThat(
      jdbcTemplate.queryForList(
        "SELECT offender_book_id FROM $tableName WHERE offender_book_id = -1",
        String::class.java
      )
    )
  }

  fun queryByOffenderId(tableName: String): ListAssert<String> {
    return Assertions.assertThat(
      jdbcTemplate.queryForList(
        "SELECT offender_id FROM $tableName WHERE offender_id = -1001",
        String::class.java
      )
    )
  }

  fun queryByRootOffenderId(tableName: String): ListAssert<String> {
    return Assertions.assertThat(
      jdbcTemplate.queryForList(
        "SELECT root_offender_id FROM $tableName WHERE root_offender_id = -1001",
        String::class.java
      )
    )
  }
}
