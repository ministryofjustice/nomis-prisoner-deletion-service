package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository

import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.connection.AppModuleName
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_AGENCY_INCIDENT_IDS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_ANONYMISE_GL_TRANSACTIONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_ADDRESSES_BY_BOOK_IDS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_ADDRESSES_BY_OFFENDER_IDS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_ADDRESS_USAGES_BY_BOOK_IDS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_ADDRESS_USAGES_BY_OFFENDER_IDS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_AGENCY_INCIDENTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_AGENCY_INCIDENT_CHARGES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_AGENCY_INCIDENT_PARTIES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_AGENCY_INCIDENT_REPAIRS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_AGY_INC_INVESTIGATIONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_AGY_INC_INV_STATEMENTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_BANK_CHEQUE_BENEFICIARIES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_BED_ASSIGNMENT_HISTORIES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_BENEFICIARY_TRANSACTIONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_CASE_ASSOCIATED_PERSONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_COURT_EVENTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_COURT_EVENT_CHARGES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_CURFEW_ADDRESSES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_CURFEW_ADDRESS_OCCUPANTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_HDC_BOARD_DECISIONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_HDC_CALC_EXCLUSION_REASONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_HDC_GOVERNOR_DECISIONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_HDC_PRISON_STAFF_COMMENTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_HDC_PROB_STAFF_COMMENTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_HDC_PROB_STAFF_RESPONSES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_HDC_REQUEST_REFERRALS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_HDC_STATUS_REASONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_HDC_STATUS_TRACKINGS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_INCIDENT_CASES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_INCIDENT_CASE_PARTIES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_INCIDENT_CASE_QUESTIONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_INCIDENT_CASE_REQUIREMENTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_INCIDENT_CASE_RESPONSES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_INCIDENT_QUESTIONNAIRE_HTY
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_INCIDENT_QUE_QUESTION_HTY
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_INCIDENT_QUE_RESPONSE_HTY
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_INTERNET_ADDRESSES_BY_BOOK_IDS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_INTERNET_ADDRESSES_BY_OFFENDER_IDS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_IWP_DOCUMENTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_LINK_CASE_TXNS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_LOCKED_MODULES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_MERGE_TRANSACTIONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_MERGE_TRANSACTION_LOGS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_ADJUSTMENT_TXNS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_ALERTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_ASSESSMENTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_ASSESSMENT_ITEMS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_BELIEFS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_BENEFICIARIES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_BOOKINGS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_BOOKING_AGY_LOCS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_BOOKING_DETAILS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_BOOKING_EVENTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_CASES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_CASE_ASSOCIATIONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_CASE_IDENTIFIERS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_CASE_NOTES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_CASE_NOTE_SENTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_CASE_OFFICERS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_CASE_STATUSES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_CHARGES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_CONTACT_PERSONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_COURSE_ATTENDANCES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_CSIP_ATTENDEES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_CSIP_FACTORS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_CSIP_INTVW
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_CSIP_PLANS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_CSIP_REPORTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_CSIP_REVIEWS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_CURFEWS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_DAMAGE_OBLIGATIONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_DATA_CORRECTIONS_HTY
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_DEDUCTIONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_DEDUCTION_RECEIPTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_DISCHARGE_BALANCES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_EDUCATIONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_EMPLOYMENTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_EXCLUDE_ACTS_SCHDS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_EXTERNAL_MOVEMENTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_FINE_PAYMENTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_FIXED_TERM_RECALLS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_FREEZE_DISBURSEMENTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_GANG_AFFILIATIONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_GANG_EVIDENCES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_GANG_INVESTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_HEALTH_PROBLEMS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_IDENTIFIERS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_IDENTIFYING_MARKS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_IEP_LEVELS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_IMAGES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_IMMIGRATION_APPEALS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_IMPRISON_STATUSES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_IND_SCHEDULES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_IND_SCH_SENTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_INTER_MVMT_LOCATIONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_KEY_DATE_ADJUSTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_KEY_WORKERS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_LANGUAGES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_LICENCE_CONDITIONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_LICENCE_RECALLS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_LICENCE_SENTENCES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_LIDS_KEY_DATES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_LIDS_REMAND_DAYS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_MEDICAL_TREATMENTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_MILITARY_RECORDS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_MINIMUM_BALANCES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_MOVEMENT_APPS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_NA_DETAILS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_NON_ASSOCIATIONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_NO_PAY_PERIODS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_OGRS3_RISK_PREDICTORS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_OIC_SANCTIONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_PAYMENT_PROFILES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_PAY_STATUSES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_PERSON_RESTRICTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_PHYSICAL_ATTRIBUTES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_PPTY_CONTAINERS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_PRG_OBLIGATIONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_PRG_OBLIGATION_HTY
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_PRG_PRF_PAY_BANDS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_PROFILES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_PROFILE_DETAILS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_PROGRAM_PROFILES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_REHAB_DECISIONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_REHAB_PROVIDERS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_RELEASE_DETAILS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_RELEASE_DETAILS_HTY
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_REORDER_SENTENCES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_RESTRICTIONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_RISK_PREDICTORS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_SENTENCES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_SENTENCE_ADJUSTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_SENTENCE_CHARGES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_SENTENCE_STATUSES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_SENTENCE_TERMS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_SENTENCE_UA_EVENTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_SENT_CALCULATIONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_SENT_CONDITIONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_SENT_COND_STATUSES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_SUBSTANCE_DETAILS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_SUBSTANCE_TREATMENTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_SUBSTANCE_USES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_SUB_ACCOUNTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_SUPERVISING_COURTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_TEAM_ASSIGNMENTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_TEAM_ASSIGN_HTY
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_TEST_SELECTIONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_TMP_REL_SCHEDULES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_TRANSACTIONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_TRANSACTION_DETAILS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_TRUST_ACCOUNTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_TRUST_ACCOUNTS_TEMP
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_VISITS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_VISIT_BALANCES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_VISIT_BALANCE_ADJS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_VISIT_ORDERS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_VISIT_VISITORS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_VO_VISITORS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_VSC_ERROR_LOGS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_VSC_SENTENCES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_VSC_SENTENCE_TERMS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFFENDER_VSC_SENT_CALCULATIONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OFF_CASE_NOTE_RECIPIENTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OIC_HEARINGS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OIC_HEARING_COMMENTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OIC_HEARING_NOTICES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_OIC_HEARING_RESULTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_ORDERS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_ORDER_PURPOSES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_PHONES_BY_BOOK_IDS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_PHONES_BY_OFFENDER_IDS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_SYSTEM_REPORT_REQUESTS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_TASK_ASSIGNMENT_HTY
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_WORKFLOW_HISTORY
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_WORK_FLOWS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_DELETE_WORK_FLOW_LOGS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_INCIDENT_CASES
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_OFFENDER_BOOKING_IDS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.OffenderDeletionRepositorySql.OD_OFFENDER_IDS
import java.util.function.Supplier

@Repository
class OffenderDeletionRepository(
  val jdbcTemplate: NamedParameterJdbcTemplate
) {

  /**
   * Deletes all data associated with an offender with the given offender number,
   * with the exception of the the 'Base Record' & GL_TRANSACTIONS (General Ledger) table
   * associated rows are anonymised by setting offender_id and offender_book_id to null.
   *
   * @return Set of offender_ids of the deleted offender aliases.
   */
  fun cleanseOffenderDataExcludingBaseRecord(offenderNumber: String): Set<Long> {
    log.info("Cleaning data for offender: '{}'", offenderNumber)
    val offenderIds = offenderIdsFor(offenderNumber)
    if (offenderIds.isEmpty()) {
      throw OffenderNotFoundException(
        offenderNumber
      )
    }
    val bookIds = getBookIds(offenderIds)
    deleteNonBaseRecordOffenderBookingData(offenderIds, bookIds)
    deleteNonBaseRecordOffenderData(offenderIds)
    log.info(
      "Deleted {} offender records (excluding non-base-records) for offenderNumber: '{}'",
      offenderIds.size,
      offenderNumber
    )
    return offenderIds
  }

  /**
   * Deletes all data associated with an offender with the given offender number,
   * with the exception of the GL_TRANSACTIONS (General Ledger) table - associated rows
   * are anonymised by setting offender_id and offender_book_id to null.
   *
   * @return Set of offender_ids of the deleted offender aliases.
   */
  fun deleteAllOffenderDataIncludingBaseRecord(offenderNumber: String): Set<Long> {
    log.info("Deleting all data for offender: '{}'", offenderNumber)
    val offenderIds = offenderIdsFor(offenderNumber)
    if (offenderIds.isEmpty()) {
      throw OffenderNotFoundException(
        offenderNumber
      )
    }
    val bookIds = getBookIds(offenderIds)
    deleteNonBaseRecordOffenderBookingData(offenderIds, bookIds)
    deleteBaseRecordOffenderBookingData(offenderIds, bookIds)
    deleteNonBaseRecordOffenderData(offenderIds)
    deleteBaseRecordOffenderData(offenderIds)
    log.info("Deleted {} offender records with offenderNumber: '{}'", offenderIds.size, offenderNumber)
    return offenderIds
  }

  /**
   * Deletes all data associated with an offender with the given offender number with the exception of the following tables:
   *
   * GL_TRANSACTIONS (General Ledger) table - associated rows are anonymised by setting offender_id and offender_book_id to null.
   * OFFENDERS
   * OFFENDER_PROFILE_DETAILS
   * OFFENDER_PHYSICAL_ATTRIBUTES
   * OFFENDER_IMAGES
   * OFFENDER_IDENTIFYING_MARKS
   * OFFENDER_EXTERNAL_MOVEMENTS
   * OFFENDER_BOOKING_DETAILS
   * OFFENDER_ALERTS
   * OFFENDER_GANG_INVESTS
   * OFFENDER_GANG_EVIDENCES
   * OFFENDER_GANG_AFFILIATIONS
   * OFFENDER_IDENTIFIERS
   * OFFENDER_PROFILE_DETAILS
   * OFFENDER_ASSESSMENTS_ITEMS
   * OFFENDER_ASSESSMENTS
   * OFFENDER_NON_ASSOCIATIONS
   * OFFENDER_NA_DETAILS
   * OFFENDER_BOOKINGS
   * OFFENDER_EXTERNAL_MOVEMENTS
   * OFFENDER_SENT_CALCULATIONS
   * OFFENDER_ASSESSMENT_ITEMS
   * HDC_CALC_EXCLUSION_REASONS

   * @return Set of offender_ids of the deleted offender aliases.
   */
  fun deleteNonBaseRecordOffenderBookingData(offenderIds: Set<Long>, bookIds: Set<Long>) {
    log.debug("Deleting all non-base record offender booking data for offender ID: '{}'", offenderIds)
    if (bookIds.isNotEmpty()) {
      deleteNonBaseRecordOffenderBookingRelatedData(bookIds)
      executeNamedSqlWithOffenderIdsAndBookingIds(OD_ANONYMISE_GL_TRANSACTIONS, offenderIds, bookIds)
      executeNamedSqlWithOffenderIdsAndBookingIds(OD_DELETE_OFFENDER_BELIEFS, offenderIds, bookIds)
    }
  }

  fun deleteAllOffenderDataExcludingBookings(offenderNumber: String): Set<Long> {
    log.info("Deleting all offender data for offender: '{}'", offenderNumber)
    val offenderIds = offenderIdsFor(offenderNumber)
    if (offenderIds.isEmpty()) { throw OffenderNotFoundException(offenderNumber) }
    deleteNonBaseRecordOffenderData(offenderIds)
    deleteBaseRecordOffenderData(offenderIds)
    log.info("Deleted {} offender records with offenderNumber: '{}'", offenderIds.size, offenderNumber)
    return offenderIds
  }

  fun setContext(context: AppModuleName?) {
    val sql =
      """
          BEGIN 
          nomis_context.set_context('AUDIT_MODULE_NAME','$context'); 
          END;"""

    getJdbcTemplateBase()?.execute(sql)
  }

  fun deleteNonBaseRecordOffenderData(offenderIds: Set<Long>) {
    log.debug("Cleaned all (non-booking, non-base-record) offender data for offender ID: '{}'", offenderIds)
    deleteContactDetailsByOffenderIds(offenderIds)
    deleteOffenderFinances(offenderIds)
    executeNamedSqlWithOffenderIds(OD_DELETE_BANK_CHEQUE_BENEFICIARIES, offenderIds)
    executeNamedSqlWithOffenderIds(OD_DELETE_OFFENDER_DAMAGE_OBLIGATIONS, offenderIds)
    executeNamedSqlWithOffenderIds(OD_DELETE_OFFENDER_FREEZE_DISBURSEMENTS, offenderIds)
    executeNamedSqlWithOffenderIds(OD_DELETE_OFFENDER_MINIMUM_BALANCES, offenderIds)
    executeNamedSqlWithOffenderIds(OD_DELETE_SYSTEM_REPORT_REQUESTS, offenderIds)
    executeNamedSqlWithOffenderIds(OD_DELETE_MERGE_TRANSACTION_LOGS, offenderIds)
    executeNamedSqlWithOffenderIds(OD_DELETE_MERGE_TRANSACTIONS, offenderIds)
    executeNamedSqlWithOffenderIds(OD_DELETE_LOCKED_MODULES, offenderIds)
    executeNamedSqlWithOffenderIds(OD_DELETE_OFFENDER_IMMIGRATION_APPEALS, offenderIds)
  }

  fun deleteBaseRecordOffenderBookingData(offenderIds: Set<Long>, bookIds: Set<Long>) {
    log.info("Deleting 'Base Record' Booking data for booking ID's: {} for offender: {}", bookIds, offenderIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_PROFILE_DETAILS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_PHYSICAL_ATTRIBUTES, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_IMAGES, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_IDENTIFYING_MARKS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_EXTERNAL_MOVEMENTS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_BOOKING_DETAILS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_ALERTS, bookIds)
    deleteOffenderGangAffiliations(bookIds)
    deleteOffenderNonAssociations(bookIds)
    deleteOffenderSentCalculations(bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_ASSESSMENT_ITEMS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_ASSESSMENTS, bookIds)
    val bookingRowsDeleted =
      executeNamedSqlWithOffenderIds(OD_DELETE_OFFENDER_BOOKINGS, offenderIds)
    log.info("Deleted {} bookings for offender {}", bookingRowsDeleted, offenderIds)
  }

  fun getBookIds(offenderIds: Set<Long>): Set<Long> {
    return jdbcTemplate.queryForList(
      OD_OFFENDER_BOOKING_IDS.sql,
      mapOf("offenderIds" to offenderIds), Long::class.java
    ).toSet()
  }

  fun deleteNonBaseRecordOffenderBookingRelatedData(bookIds: Set<Long>) {
    log.debug("Deleting all offender booking data for book ID: '{}'", bookIds)
    deleteContactDetailsByBookIds(bookIds)
    deleteWorkFlows(bookIds)
    deleteAgencyIncidents(bookIds)
    deleteOffenderCases(bookIds)
    deleteOffenderContactPersons(bookIds)
    deleteOffenderCSIPReports(bookIds)
    deleteOffenderCurfews(bookIds)
    deleteOffenderHealthProblems(bookIds)
    deleteOffenderLIDSKeyDates(bookIds)
    deleteOffenderRehabDecisions(bookIds)
    deleteOffenderSubstanceUses(bookIds)
    deleteOffenderVisits(bookIds)
    deleteOffenderVisitBalances(bookIds)
    deleteOffenderVisitOrders(bookIds)
    deleteOffenderVSCSentences(bookIds)
    deleteIncidentCases(bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_BED_ASSIGNMENT_HISTORIES, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_CASE_ASSOCIATED_PERSONS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_IWP_DOCUMENTS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_BOOKING_AGY_LOCS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_BOOKING_EVENTS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_CASE_ASSOCIATIONS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_CASE_OFFICERS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_DATA_CORRECTIONS_HTY, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_DISCHARGE_BALANCES, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_EDUCATIONS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_EMPLOYMENTS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_FINE_PAYMENTS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_FIXED_TERM_RECALLS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_IEP_LEVELS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_IMPRISON_STATUSES, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_INTER_MVMT_LOCATIONS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_KEY_WORKERS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_LANGUAGES, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_MILITARY_RECORDS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_NO_PAY_PERIODS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_OGRS3_RISK_PREDICTORS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_PAY_STATUSES, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_PPTY_CONTAINERS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_PROFILES, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_RELEASE_DETAILS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_RELEASE_DETAILS_HTY, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_RESTRICTIONS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_RISK_PREDICTORS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_SUPERVISING_COURTS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_TEAM_ASSIGNMENTS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_TEAM_ASSIGN_HTY, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_TEST_SELECTIONS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_TMP_REL_SCHEDULES, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_TRUST_ACCOUNTS_TEMP, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_VSC_ERROR_LOGS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_VSC_SENT_CALCULATIONS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_TASK_ASSIGNMENT_HTY, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_WORKFLOW_HISTORY, bookIds)
  }

  fun deleteBaseRecordOffenderData(offenderIds: Set<Long>) {
    executeNamedSqlWithOffenderIds(OD_DELETE_OFFENDER_IDENTIFIERS, offenderIds)
    executeNamedSqlWithOffenderIds(OD_DELETE_OFFENDER, offenderIds)
  }

  fun deleteContactDetailsByBookIds(bookIds: Set<Long>) {
    executeNamedSqlWithBookingIds(OD_DELETE_INTERNET_ADDRESSES_BY_BOOK_IDS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_PHONES_BY_BOOK_IDS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_ADDRESS_USAGES_BY_BOOK_IDS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_ADDRESSES_BY_BOOK_IDS, bookIds)
  }

  fun deleteWorkFlows(bookIds: Set<Long>) {
    executeNamedSqlWithBookingIds(OD_DELETE_WORK_FLOW_LOGS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_WORK_FLOWS, bookIds)
  }

  fun deleteOffenderCases(bookIds: Set<Long>) {
    deleteOrders(bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_CASE_IDENTIFIERS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_CASE_STATUSES, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_CASES, bookIds)
  }

  fun deleteOrders(bookIds: Set<Long>) {
    deleteOffenderSentences(bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_REORDER_SENTENCES, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_ORDER_PURPOSES, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_ORDERS, bookIds)
    deleteCourtEventsAndOffenderCharges(bookIds)
  }

  fun deleteOffenderSentences(bookIds: Set<Long>) {
    deleteOffenderCaseNotes(bookIds)
    deleteOffenderSentConditions(bookIds)
    deleteOffenderSentenceAdjusts(bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_LICENCE_CONDITIONS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_LICENCE_RECALLS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_LICENCE_SENTENCES, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_SENTENCE_CHARGES, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_SENTENCE_STATUSES, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_SENTENCE_TERMS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_SENTENCE_UA_EVENTS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_SENTENCES, bookIds)
  }

  fun deleteOffenderCaseNotes(bookIds: Set<Long>) {
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_CASE_NOTE_SENTS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFF_CASE_NOTE_RECIPIENTS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_CASE_NOTES, bookIds)
  }

  fun deleteOffenderSentConditions(bookIds: Set<Long>) {
    deleteOffenderPrgObligations(bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_SENT_COND_STATUSES, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_SENT_CONDITIONS, bookIds)
  }

  fun deleteOffenderPrgObligations(bookIds: Set<Long>) {
    deleteOffenderMovementApps(bookIds)
    deleteOffenderProgramProfiles(bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_PRG_OBLIGATION_HTY, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_PRG_OBLIGATIONS, bookIds)
  }

  fun deleteOffenderMovementApps(bookIds: Set<Long>) {
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_IND_SCH_SENTS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_IND_SCHEDULES, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_MOVEMENT_APPS, bookIds)
  }

  fun deleteOffenderProgramProfiles(bookIds: Set<Long>) {
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_COURSE_ATTENDANCES, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_EXCLUDE_ACTS_SCHDS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_PRG_PRF_PAY_BANDS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_PROGRAM_PROFILES, bookIds)
  }

  fun deleteOffenderSentenceAdjusts(bookIds: Set<Long>) {
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_SENTENCE_ADJUSTS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_KEY_DATE_ADJUSTS, bookIds)
  }

  fun deleteCourtEventsAndOffenderCharges(bookIds: Set<Long>) {
    executeNamedSqlWithBookingIds(OD_DELETE_LINK_CASE_TXNS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_COURT_EVENT_CHARGES, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_COURT_EVENTS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_CHARGES, bookIds)
  }

  fun agencyIncidentIdsFor(bookIds: Set<Long>): Set<Long> {
    return jdbcTemplate.queryForList(OD_AGENCY_INCIDENT_IDS.sql, mapOf("bookIds" to bookIds), Long::class.java).toSet()
  }

  fun deleteAgencyIncidents(bookIds: Set<Long>) {
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_OIC_SANCTIONS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OIC_HEARING_RESULTS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OIC_HEARING_COMMENTS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OIC_HEARING_NOTICES, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OIC_HEARINGS, bookIds)
    val agencyIncidentIds = agencyIncidentIdsFor(bookIds)
    if (!agencyIncidentIds.isEmpty()) {
      executeNamedSqlWithAgencyIncidentIds(
        OD_DELETE_AGY_INC_INV_STATEMENTS,
        agencyIncidentIds
      )
      executeNamedSqlWithAgencyIncidentIds(
        OD_DELETE_AGY_INC_INVESTIGATIONS,
        agencyIncidentIds
      )
      executeNamedSqlWithAgencyIncidentIds(
        OD_DELETE_AGENCY_INCIDENT_REPAIRS,
        agencyIncidentIds
      )
      executeNamedSqlWithAgencyIncidentIds(
        OD_DELETE_AGENCY_INCIDENT_CHARGES,
        agencyIncidentIds
      )
      executeNamedSqlWithAgencyIncidentIds(
        OD_DELETE_AGENCY_INCIDENT_PARTIES,
        agencyIncidentIds
      )
      executeNamedSqlWithAgencyIncidentIds(OD_DELETE_AGENCY_INCIDENTS, agencyIncidentIds)
    }
  }

  fun deleteOffenderContactPersons(bookIds: Set<Long>) {
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_PERSON_RESTRICTS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_CONTACT_PERSONS, bookIds)
  }

  fun deleteOffenderCSIPReports(bookIds: Set<Long>) {
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_CSIP_FACTORS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_CSIP_INTVW, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_CSIP_PLANS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_CSIP_ATTENDEES, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_CSIP_REVIEWS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_CSIP_REPORTS, bookIds)
  }

  fun deleteOffenderCurfews(bookIds: Set<Long>) {
    deleteCurfewAddresses(bookIds)
    deleteHDCRequestReferrals(bookIds)
    deleteHDCStatusTrackings(bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_HDC_PRISON_STAFF_COMMENTS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_CURFEWS, bookIds)
  }

  fun deleteCurfewAddresses(bookIds: Set<Long>) {
    executeNamedSqlWithBookingIds(OD_DELETE_CURFEW_ADDRESS_OCCUPANTS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_CURFEW_ADDRESSES, bookIds)
  }

  fun deleteHDCRequestReferrals(bookIds: Set<Long>) {
    executeNamedSqlWithBookingIds(OD_DELETE_HDC_PROB_STAFF_RESPONSES, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_HDC_PROB_STAFF_COMMENTS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_HDC_BOARD_DECISIONS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_HDC_GOVERNOR_DECISIONS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_HDC_REQUEST_REFERRALS, bookIds)
  }

  fun deleteHDCStatusTrackings(bookIds: Set<Long>) {
    executeNamedSqlWithBookingIds(OD_DELETE_HDC_STATUS_REASONS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_HDC_STATUS_TRACKINGS, bookIds)
  }

  fun deleteOffenderGangAffiliations(bookIds: Set<Long>) {
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_GANG_INVESTS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_GANG_EVIDENCES, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_GANG_AFFILIATIONS, bookIds)
  }

  fun deleteOffenderHealthProblems(bookIds: Set<Long>) {
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_MEDICAL_TREATMENTS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_HEALTH_PROBLEMS, bookIds)
  }

  fun deleteOffenderLIDSKeyDates(bookIds: Set<Long>) {
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_LIDS_REMAND_DAYS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_LIDS_KEY_DATES, bookIds)
  }

  fun deleteOffenderNonAssociations(bookIds: Set<Long>) {
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_NA_DETAILS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_NON_ASSOCIATIONS, bookIds)
  }

  fun deleteOffenderRehabDecisions(bookIds: Set<Long>) {
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_REHAB_PROVIDERS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_REHAB_DECISIONS, bookIds)
  }

  fun deleteOffenderSentCalculations(bookIds: Set<Long>) {
    executeNamedSqlWithBookingIds(OD_DELETE_HDC_CALC_EXCLUSION_REASONS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_SENT_CALCULATIONS, bookIds)
  }

  fun deleteOffenderSubstanceUses(bookIds: Set<Long>) {
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_SUBSTANCE_DETAILS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_SUBSTANCE_TREATMENTS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_SUBSTANCE_USES, bookIds)
  }

  fun deleteOffenderVisits(bookIds: Set<Long>) {
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_VISIT_VISITORS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_VISITS, bookIds)
  }

  fun deleteOffenderVisitBalances(bookIds: Set<Long>) {
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_VISIT_BALANCE_ADJS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_VISIT_BALANCES, bookIds)
  }

  fun deleteOffenderVisitOrders(bookIds: Set<Long>) {
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_VO_VISITORS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_VISIT_ORDERS, bookIds)
  }

  fun deleteOffenderVSCSentences(bookIds: Set<Long>) {
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_VSC_SENTENCE_TERMS, bookIds)
    executeNamedSqlWithBookingIds(OD_DELETE_OFFENDER_VSC_SENTENCES, bookIds)
  }

  fun incidentCaseIdsFor(bookIds: Set<Long>): Set<Long> {
    return jdbcTemplate.queryForList(
      OD_INCIDENT_CASES.sql,
      mapOf("bookIds" to bookIds), Long::class.java
    ).toSet()
  }

  fun deleteIncidentCases(bookIds: Set<Long>) {
    val incidentCaseIds = incidentCaseIdsFor(bookIds)
    if (incidentCaseIds.isNotEmpty()) {
      executeNamedSqlWithIncidentCaseIds(OD_DELETE_INCIDENT_CASE_PARTIES, incidentCaseIds)
      executeNamedSqlWithIncidentCaseIds(
        OD_DELETE_INCIDENT_CASE_RESPONSES,
        incidentCaseIds
      )
      executeNamedSqlWithIncidentCaseIds(
        OD_DELETE_INCIDENT_CASE_QUESTIONS,
        incidentCaseIds
      )
      executeNamedSqlWithIncidentCaseIds(
        OD_DELETE_INCIDENT_QUE_RESPONSE_HTY,
        incidentCaseIds
      )
      executeNamedSqlWithIncidentCaseIds(
        OD_DELETE_INCIDENT_QUE_QUESTION_HTY,
        incidentCaseIds
      )
      executeNamedSqlWithIncidentCaseIds(
        OD_DELETE_INCIDENT_QUESTIONNAIRE_HTY,
        incidentCaseIds
      )
      executeNamedSqlWithIncidentCaseIds(
        OD_DELETE_INCIDENT_CASE_REQUIREMENTS,
        incidentCaseIds
      )
      executeNamedSqlWithIncidentCaseIds(OD_DELETE_INCIDENT_CASES, incidentCaseIds)
    }
  }

  fun deleteContactDetailsByOffenderIds(offenderIds: Set<Long>) {
    executeNamedSqlWithOffenderIds(OD_DELETE_INTERNET_ADDRESSES_BY_OFFENDER_IDS, offenderIds)
    executeNamedSqlWithOffenderIds(OD_DELETE_PHONES_BY_OFFENDER_IDS, offenderIds)
    executeNamedSqlWithOffenderIds(OD_DELETE_ADDRESS_USAGES_BY_OFFENDER_IDS, offenderIds)
    executeNamedSqlWithOffenderIds(OD_DELETE_ADDRESSES_BY_OFFENDER_IDS, offenderIds)
  }

  fun deleteOffenderFinances(offenderIds: Set<Long>) {
    deleteOffenderTransactions(offenderIds)
    deleteOffenderDeductions(offenderIds)
    executeNamedSqlWithOffenderIds(OD_DELETE_OFFENDER_SUB_ACCOUNTS, offenderIds)
    executeNamedSqlWithOffenderIds(OD_DELETE_OFFENDER_TRUST_ACCOUNTS, offenderIds)
    executeNamedSqlWithOffenderIds(OD_DELETE_OFFENDER_PAYMENT_PROFILES, offenderIds)
  }

  fun deleteOffenderTransactions(offenderIds: Set<Long>) {
    executeNamedSqlWithOffenderIds(OD_DELETE_OFFENDER_TRANSACTION_DETAILS, offenderIds)
    executeNamedSqlWithOffenderIds(OD_DELETE_OFFENDER_TRANSACTIONS, offenderIds)
  }

  fun deleteOffenderDeductions(offenderIds: Set<Long>) {
    deleteOffenderBeneficiaries(offenderIds)
    executeNamedSqlWithOffenderIds(OD_DELETE_OFFENDER_ADJUSTMENT_TXNS, offenderIds)
    executeNamedSqlWithOffenderIds(OD_DELETE_OFFENDER_DEDUCTION_RECEIPTS, offenderIds)
    executeNamedSqlWithOffenderIds(OD_DELETE_OFFENDER_DEDUCTIONS, offenderIds)
  }

  fun deleteOffenderBeneficiaries(offenderIds: Set<Long>) {
    executeNamedSqlWithOffenderIds(OD_DELETE_BENEFICIARY_TRANSACTIONS, offenderIds)
    executeNamedSqlWithOffenderIds(OD_DELETE_OFFENDER_BENEFICIARIES, offenderIds)
  }

  fun executeNamedSqlWithOffenderIds(query: OffenderDeletionRepositorySql, ids: Set<Long>): Int {
    return jdbcTemplate.update(query.sql, mapOf("offenderIds" to ids))
  }

  fun executeNamedSqlWithBookingIds(query: OffenderDeletionRepositorySql, ids: Set<Long>) {
    jdbcTemplate.update(query.sql, mapOf("bookIds" to ids))
  }

  fun executeNamedSqlWithOffenderIdsAndBookingIds(
    query: OffenderDeletionRepositorySql,
    offenderIds: Set<Long>,
    bookIds: Set<Long>
  ) {
    jdbcTemplate.update(query.sql, mapOf("offenderIds" to offenderIds, "bookIds" to bookIds.ifEmpty { null }))
  }

  fun executeNamedSqlWithIncidentCaseIds(query: OffenderDeletionRepositorySql, ids: Set<Long>) {
    jdbcTemplate.update(query.sql, mapOf("incidentCaseIds" to ids))
  }

  fun executeNamedSqlWithAgencyIncidentIds(query: OffenderDeletionRepositorySql, ids: Set<Long>) {
    jdbcTemplate.update(query.sql, mapOf("agencyIncidentIds" to ids))
  }

  fun offenderIdsFor(offenderNumber: String): Set<Long> {
    return jdbcTemplate.queryForList(OD_OFFENDER_IDS.sql, mapOf("offenderNo" to offenderNumber), Long::class.java)
      .toSet()
  }

  fun getJdbcTemplateBase(): JdbcTemplate? {
    return jdbcTemplate.jdbcOperations as JdbcTemplate
  }

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }

  class OffenderNotFoundException(id: String?) :
    RuntimeException("Offender with id [$id] not found."),
    Supplier<OffenderNotFoundException> {
    override fun get(): OffenderNotFoundException {
      return OffenderNotFoundException(message)
    }
  }
}
