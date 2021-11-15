package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.executeScripts
import javax.sql.DataSource

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(PER_CLASS)
class FreeTextRepositoryTest {

  @Autowired
  lateinit var repository: uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa.FreeTextRepository

  @BeforeAll
  fun setup(@Autowired dataSource: DataSource) {
    executeScripts(dataSource, "define_regexp_like.sql")
  }

  @AfterAll
  fun cleanup(@Autowired dataSource: DataSource) {
    executeScripts(dataSource, "drop_regexp_like.sql")
  }

  @Test
  fun `find match using BookIds`() {

    assertThat(repository.findMatchUsingBookIds(setOf(-1L), ".*Text.*"))
      .extracting<String> { it.tableName.trim() }
      .containsExactlyInAnyOrder(
        "ADDRESSES",
        "AGENCY_INCIDENTS",
        "AGENCY_INCIDENT_CHARGES",
        "AGENCY_INCIDENT_PARTIES",
        "AGY_INC_INVESTIGATIONS",
        "AGY_INC_INV_STATEMENTS",
        "COURT_EVENTS",
        "CURFEW_ADDRESS_OCCUPANTS",
        "HDC_BOARD_DECISIONS",
        "HDC_GOVERNOR_DECISIONS",
        "HDC_PRISON_STAFF_COMMENTS",
        "HDC_PROB_STAFF_COMMENTS",
        "HDC_REQUEST_REFERRALS",
        "INCIDENT_CASES",
        "INCIDENT_CASE_PARTIES",
        "INCIDENT_CASE_REQUIREMENTS",
        "INCIDENT_CASE_RESPONSES",
        "INCIDENT_QUE_RESPONSE_HTY",
        "IWP_DOCUMENTS",
        "OFFENDER_ALERTS",
        "OFFENDER_ASSESSMENTS",
        "OFFENDER_ASSESSMENT_ITEMS",
        "OFFENDER_BELIEFS",
        "OFFENDER_CASES",
        "OFFENDER_CASE_NOTES",
        "OFFENDER_CASE_STATUSES",
        "OFFENDER_CONTACT_PERSONS",
        "OFFENDER_COURSE_ATTENDANCES",
        "OFFENDER_CSIP_ATTENDEES",
        "OFFENDER_CSIP_FACTORS",
        "OFFENDER_CSIP_INTVW",
        "OFFENDER_CSIP_PLANS",
        "OFFENDER_CSIP_REPORTS",
        "OFFENDER_CSIP_REVIEWS",
        "OFFENDER_CURFEWS",
        "OFFENDER_DATA_CORRECTIONS_HTY",
        "OFFENDER_EDUCATIONS",
        "OFFENDER_EMPLOYMENTS",
        "OFFENDER_EXTERNAL_MOVEMENTS",
        "OFFENDER_FINE_PAYMENTS",
        "OFFENDER_FIXED_TERM_RECALLS",
        "OFFENDER_GANG_AFFILIATIONS",
        "OFFENDER_GANG_INVESTS",
        "OFFENDER_HEALTH_PROBLEMS",
        "OFFENDER_IDENTIFYING_MARKS",
        "OFFENDER_IEP_LEVELS",
        "OFFENDER_IMPRISON_STATUSES",
        "OFFENDER_IND_SCHEDULES",
        "OFFENDER_KEY_DATE_ADJUSTS",
        "OFFENDER_LANGUAGES",
        "OFFENDER_LICENCE_RECALLS",
        "OFFENDER_MEDICAL_TREATMENTS",
        "OFFENDER_MILITARY_RECORDS",
        "OFFENDER_MOVEMENT_APPS",
        "OFFENDER_NA_DETAILS",
        "OFFENDER_NO_PAY_PERIODS",
        "OFFENDER_OIC_SANCTIONS",
        "OFFENDER_PAY_STATUSES",
        "OFFENDER_PERSON_RESTRICTS",
        "OFFENDER_PPTY_CONTAINERS",
        "OFFENDER_PROGRAM_PROFILES",
        "OFFENDER_REHAB_DECISIONS",
        "OFFENDER_REHAB_PROVIDERS",
        "OFFENDER_RELEASE_DETAILS",
        "OFFENDER_RELEASE_DETAILS_HTY",
        "OFFENDER_RESTRICTIONS",
        "OFFENDER_SENTENCES",
        "OFFENDER_SENTENCE_ADJUSTS",
        "OFFENDER_SENTENCE_STATUSES",
        "OFFENDER_SENT_CALCULATIONS",
        "OFFENDER_SENT_CONDITIONS",
        "OFFENDER_SENT_COND_STATUSES",
        "OFFENDER_SUBSTANCE_DETAILS",
        "OFFENDER_SUBSTANCE_TREATMENTS",
        "OFFENDER_TEST_SELECTIONS",
        "OFFENDER_VISITS",
        "OFFENDER_VISIT_BALANCE_ADJS",
        "OFFENDER_VISIT_ORDERS",
        "OFFENDER_VSC_SENTENCES",
        "OFFENDER_VSC_SENT_CALCULATIONS",
        "OFFENDER_VISIT_VISITORS",
        "OFF_CASE_NOTE_RECIPIENTS",
        "OIC_HEARINGS",
        "OIC_HEARING_NOTICES",
        "ORDERS",
        "TASK_ASSIGNMENT_HTY"
      )
  }

  @Test
  fun findMatchUsingBookIdsReturnsEmpty() {
    assertThat(repository.findMatchUsingBookIds(setOf(-1L), ".*NO MATCH.*"))
      .isEmpty()
  }

  @Test
  fun findMatchUsingOffenderIds() {
    assertThat(repository.findMatchUsingOffenderIds(setOf(-1001L), ".*Text.*"))
      .extracting<String> { it.tableName.trim() }
      .containsExactlyInAnyOrder(
        "OFFENDER_DAMAGE_OBLIGATIONS",
        "OFFENDER_FREEZE_DISBURSEMENTS",
        "OFFENDER_IDENTIFIERS",
        "OFFENDER_PAYMENT_PROFILES"
      )
  }

  @Test
  fun findMatchUsingOffenderIdsReturnsEmpty() {
    assertThat(repository.findMatchUsingOffenderIds(setOf(-1001L), ".*NO MATCH.*")).isEmpty()
  }
}
