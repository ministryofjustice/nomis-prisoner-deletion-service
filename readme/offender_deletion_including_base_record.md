```


        DELETE FROM INTERNET_ADDRESSES
        WHERE OWNER_CLASS IN ('OFF_EMP', 'OFF_EDU')
        AND OWNER_ID IN (?)


        DELETE FROM PHONES P
                WHERE (P.OWNER_CLASS IN ('OFF_EMP', 'OFF_EDU') AND P.OWNER_ID IN (?))
        OR (P.OWNER_CLASS = 'ADDR' AND EXISTS (SELECT 1 FROM ADDRESSES A
                WHERE P.OWNER_ID = A.ADDRESS_ID
                AND A.OWNER_CLASS IN ('OFF_EMP', 'OFF_EDU')
                AND A.OWNER_ID IN (?)))


        DELETE FROM ADDRESS_USAGES AU
                WHERE EXISTS (SELECT 1
        FROM ADDRESSES A
        WHERE AU.ADDRESS_ID = A.ADDRESS_ID
                AND A.OWNER_CLASS IN ('OFF_EMP', 'OFF_EDU')
        AND A.OWNER_ID IN (?))


        DELETE FROM ADDRESSES
        WHERE OWNER_CLASS IN ('OFF_EMP', 'OFF_EDU')
        AND OWNER_ID IN (?)


        DELETE FROM WORK_FLOW_LOGS WFL
                WHERE EXISTS (SELECT 1
        FROM WORK_FLOWS WF
        WHERE WF.WORK_FLOW_ID = WFL.WORK_FLOW_ID
                AND WF.OBJECT_ID IN (?))


        DELETE FROM WORK_FLOWS WHERE OBJECT_ID IN (?)


        DELETE FROM OFFENDER_OIC_SANCTIONS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OIC_HEARING_RESULTS OHR
                WHERE EXISTS (
                SELECT 1
        FROM OIC_HEARINGS OH
        INNER JOIN AGENCY_INCIDENT_PARTIES AIP
                ON AIP.OIC_INCIDENT_ID = OH.OIC_INCIDENT_ID
                WHERE OH.OIC_HEARING_ID = OHR.OIC_HEARING_ID
                AND AIP.OFFENDER_BOOK_ID IN (?))


        DELETE FROM OIC_HEARING_COMMENTS OHC
                WHERE EXISTS (
                SELECT 1
        FROM OIC_HEARINGS OH
        INNER JOIN AGENCY_INCIDENT_PARTIES AIP
                ON AIP.OIC_INCIDENT_ID = OH.OIC_INCIDENT_ID
                WHERE OH.OIC_HEARING_ID = OHC.OIC_HEARING_ID
                AND AIP.OFFENDER_BOOK_ID IN (?))


        DELETE FROM OIC_HEARING_NOTICES OHN
                WHERE EXISTS (
                SELECT 1
        FROM OIC_HEARINGS OH
        INNER JOIN AGENCY_INCIDENT_PARTIES AIP
                ON AIP.OIC_INCIDENT_ID = OH.OIC_INCIDENT_ID
                WHERE OH.OIC_HEARING_ID = OHN.OIC_HEARING_ID
                AND AIP.OFFENDER_BOOK_ID IN (?))


        DELETE FROM OIC_HEARINGS OH
                WHERE EXISTS (SELECT 1
        FROM AGENCY_INCIDENT_PARTIES AIP
        WHERE OH.OIC_INCIDENT_ID = AIP.OIC_INCIDENT_ID
                AND AIP.OFFENDER_BOOK_ID IN (?))


        SELECT AGENCY_INCIDENT_ID FROM AGENCY_INCIDENT_PARTIES WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM AGY_INC_INV_STATEMENTS AIIS
                WHERE EXISTS(SELECT 1
        FROM AGY_INC_INVESTIGATIONS AII
        WHERE AII.AGENCY_INCIDENT_ID IN (?)
        AND AIIS.AGY_INC_INVESTIGATION_ID = AII.AGY_INC_INVESTIGATION_ID)


        DELETE FROM AGY_INC_INVESTIGATIONS WHERE AGENCY_INCIDENT_ID IN (?)


        DELETE FROM AGENCY_INCIDENT_REPAIRS WHERE AGENCY_INCIDENT_ID IN (?)


        DELETE FROM AGENCY_INCIDENT_CHARGES WHERE AGENCY_INCIDENT_ID IN (?)


        DELETE FROM AGENCY_INCIDENT_PARTIES WHERE AGENCY_INCIDENT_ID IN (?)


        DELETE FROM AGENCY_INCIDENTS WHERE AGENCY_INCIDENT_ID IN (?)


        DELETE FROM OFFENDER_CASE_NOTE_SENTS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFF_CASE_NOTE_RECIPIENTS OCNR
                WHERE EXISTS (SELECT 1
        FROM OFFENDER_CASE_NOTES OCN
        WHERE OCN.CASE_NOTE_ID = OCNR.CASE_NOTE_ID
                AND OCN.OFFENDER_BOOK_ID IN (?))


        DELETE FROM OFFENDER_CASE_NOTES WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_IND_SCH_SENTS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_IND_SCHEDULES WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_MOVEMENT_APPS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_COURSE_ATTENDANCES OCA
                WHERE EXISTS (SELECT 1
        FROM OFFENDER_PROGRAM_PROFILES OPP
        WHERE OCA.OFF_PRGREF_ID = OPP.OFF_PRGREF_ID
                AND OPP.OFFENDER_BOOK_ID IN (?))


        DELETE FROM OFFENDER_EXCLUDE_ACTS_SCHDS OEAS
                WHERE EXISTS (SELECT 1
        FROM OFFENDER_PROGRAM_PROFILES OPP
        WHERE OEAS.OFF_PRGREF_ID = OPP.OFF_PRGREF_ID
                AND OPP.OFFENDER_BOOK_ID IN (?))


        DELETE FROM OFFENDER_PRG_PRF_PAY_BANDS OPPPB
                WHERE EXISTS (SELECT 1
        FROM OFFENDER_PROGRAM_PROFILES OPP
        WHERE OPPPB.OFF_PRGREF_ID = OPP.OFF_PRGREF_ID
                AND OPP.OFFENDER_BOOK_ID IN (?))


        DELETE FROM OFFENDER_PROGRAM_PROFILES WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_PRG_OBLIGATION_HTY OPOH
                WHERE EXISTS (SELECT 1
        FROM OFFENDER_PRG_OBLIGATIONS OPO
        WHERE OPO.OFFENDER_PRG_OBLIGATION_ID = OPOH.OFFENDER_PRG_OBLIGATION_ID
                AND OPO.OFFENDER_BOOK_ID IN (?))


        DELETE FROM OFFENDER_PRG_OBLIGATIONS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_SENT_COND_STATUSES OSCS
                WHERE EXISTS (SELECT 1
        FROM OFFENDER_SENT_CONDITIONS OSC
        WHERE OSC.OFFENDER_SENT_CONDITION_ID = OSCS.OFFENDER_SENT_CONDITION_ID
                AND OSC.OFFENDER_BOOK_ID IN (?))


        DELETE FROM OFFENDER_SENT_CONDITIONS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_SENTENCE_ADJUSTS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_KEY_DATE_ADJUSTS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_LICENCE_CONDITIONS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_LICENCE_RECALLS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_LICENCE_SENTENCES WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_SENTENCE_CHARGES WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_SENTENCE_STATUSES WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_SENTENCE_TERMS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_SENTENCE_UA_EVENTS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_SENTENCES WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_REORDER_SENTENCES WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM ORDER_PURPOSES OP
                WHERE EXISTS (SELECT 1
        FROM ORDERS O
        WHERE O.ORDER_ID = OP.ORDER_ID
                AND O.OFFENDER_BOOK_ID IN (?))


        DELETE FROM ORDERS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM LINK_CASE_TXNS LCT
                WHERE EXISTS (SELECT 1
        FROM OFFENDER_CASES OC
        WHERE OC.CASE_ID = LCT.CASE_ID
                AND OC.OFFENDER_BOOK_ID IN (?))


        DELETE FROM COURT_EVENT_CHARGES WHERE (EVENT_ID, OFFENDER_CHARGE_ID) IN (
        SELECT DISTINCT CEC.EVENT_ID, CEC.OFFENDER_CHARGE_ID
        FROM COURT_EVENTS CE
        FULL OUTER JOIN OFFENDER_CHARGES OC
        ON CE.OFFENDER_BOOK_ID = OC.OFFENDER_BOOK_ID
                INNER JOIN COURT_EVENT_CHARGES CEC
                ON (CE.EVENT_ID = CEC.EVENT_ID OR OC.OFFENDER_CHARGE_ID = CEC.OFFENDER_CHARGE_ID)
        WHERE CE.OFFENDER_BOOK_ID IN (?)
        AND OC.OFFENDER_BOOK_ID IN (?)
        )


        DELETE FROM COURT_EVENTS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_CHARGES WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_CASE_IDENTIFIERS OCI
                WHERE EXISTS (SELECT 1
        FROM OFFENDER_CASES OC
        WHERE OC.CASE_ID = OCI.CASE_ID
                AND OC.OFFENDER_BOOK_ID IN (?))


        DELETE FROM OFFENDER_CASE_STATUSES OCS
                WHERE EXISTS (SELECT 1
        FROM OFFENDER_CASES OC
        WHERE OC.CASE_ID = OCS.CASE_ID
                AND OC.OFFENDER_BOOK_ID IN (?))


        DELETE FROM OFFENDER_CASES WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_PERSON_RESTRICTS OPR
                WHERE EXISTS (SELECT 1
        FROM OFFENDER_CONTACT_PERSONS OCP
        WHERE OPR.OFFENDER_CONTACT_PERSON_ID = OCP.OFFENDER_CONTACT_PERSON_ID
                AND OCP.OFFENDER_BOOK_ID IN (?))


        DELETE FROM OFFENDER_CONTACT_PERSONS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_CSIP_FACTORS OCF
                WHERE EXISTS (SELECT 1
        FROM OFFENDER_CSIP_REPORTS OCR
        WHERE OCF.CSIP_ID = OCR.CSIP_ID
                AND OCR.OFFENDER_BOOK_ID IN (?))


        DELETE FROM OFFENDER_CSIP_INTVW OCI
                WHERE EXISTS (SELECT 1
        FROM OFFENDER_CSIP_REPORTS OCR
        WHERE OCI.CSIP_ID = OCR.CSIP_ID
                AND OCR.OFFENDER_BOOK_ID IN (?))


        DELETE FROM OFFENDER_CSIP_PLANS OCP
                WHERE EXISTS (SELECT 1
        FROM OFFENDER_CSIP_REPORTS OCR
        WHERE OCP.CSIP_ID = OCR.CSIP_ID
                AND OCR.OFFENDER_BOOK_ID IN (?))


        DELETE FROM OFFENDER_CSIP_ATTENDEES OCA
                WHERE EXISTS (
                SELECT 1
        FROM OFFENDER_CSIP_REVIEWS OCREV
        INNER JOIN OFFENDER_CSIP_REPORTS OCREP
                ON OCREV.CSIP_ID = OCREP.CSIP_ID
                WHERE OCA.REVIEW_ID = OCREV.REVIEW_ID
                AND OCREP.OFFENDER_BOOK_ID IN (?))


        DELETE FROM OFFENDER_CSIP_REVIEWS OCREV
                WHERE EXISTS (SELECT 1
        FROM OFFENDER_CSIP_REPORTS OCREP
        WHERE OCREV.CSIP_ID = OCREP.CSIP_ID
                AND OCREP.OFFENDER_BOOK_ID IN (?))


        DELETE FROM OFFENDER_CSIP_REPORTS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM CURFEW_ADDRESS_OCCUPANTS CAO
                WHERE EXISTS (SELECT 1
        FROM CURFEW_ADDRESSES CA
        WHERE CAO.CURFEW_ADDRESS_ID = CA.CURFEW_ADDRESS_ID
                AND CA.OFFENDER_BOOK_ID IN (?))


        DELETE FROM CURFEW_ADDRESSES WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM HDC_PROB_STAFF_RESPONSES HPSR
                WHERE EXISTS (SELECT 1
        FROM HDC_REQUEST_REFERRALS HRR
        WHERE HPSR.HDC_REQUEST_REFERRAL_ID = HRR.HDC_REQUEST_REFERRAL_ID
                AND HRR.OFFENDER_BOOK_ID IN (?))


        DELETE FROM HDC_PROB_STAFF_COMMENTS HPSC
                WHERE EXISTS (SELECT 1
        FROM HDC_REQUEST_REFERRALS HRR
        WHERE HPSC.HDC_REQUEST_REFERRAL_ID = HRR.HDC_REQUEST_REFERRAL_ID
                AND HRR.OFFENDER_BOOK_ID IN (?))


        DELETE FROM HDC_BOARD_DECISIONS HBD
                WHERE EXISTS (SELECT 1
        FROM HDC_REQUEST_REFERRALS HRR
        WHERE HBD.HDC_REQUEST_REFERRAL_ID = HRR.HDC_REQUEST_REFERRAL_ID
                AND HRR.OFFENDER_BOOK_ID IN (?))


        DELETE FROM HDC_GOVERNOR_DECISIONS HGD
                WHERE EXISTS (SELECT 1
        FROM HDC_REQUEST_REFERRALS HRR
        WHERE HGD.HDC_REQUEST_REFERRAL_ID = HRR.HDC_REQUEST_REFERRAL_ID
                AND HRR.OFFENDER_BOOK_ID IN (?))


        DELETE FROM HDC_REQUEST_REFERRALS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM HDC_STATUS_REASONS HSR
                WHERE EXISTS (
                SELECT 1
        FROM HDC_STATUS_TRACKINGS HST
        INNER JOIN OFFENDER_CURFEWS OC
                ON OC.OFFENDER_CURFEW_ID = HST.OFFENDER_CURFEW_ID
                WHERE HST.HDC_STATUS_TRACKING_ID = HSR.HDC_STATUS_TRACKING_ID
                AND OC.OFFENDER_BOOK_ID IN (?))


        DELETE FROM HDC_STATUS_TRACKINGS HST
                WHERE EXISTS (SELECT 1
        FROM OFFENDER_CURFEWS OC
        WHERE HST.OFFENDER_CURFEW_ID = OC.OFFENDER_CURFEW_ID
                AND OC.OFFENDER_BOOK_ID IN (?))


        DELETE FROM HDC_PRISON_STAFF_COMMENTS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_CURFEWS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_MEDICAL_TREATMENTS OMT
                WHERE EXISTS (SELECT 1
        FROM OFFENDER_HEALTH_PROBLEMS OHP
        WHERE OMT.OFFENDER_HEALTH_PROBLEM_ID = OHP.OFFENDER_HEALTH_PROBLEM_ID
                AND OHP.OFFENDER_BOOK_ID IN (?))


        DELETE FROM OFFENDER_HEALTH_PROBLEMS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_LIDS_REMAND_DAYS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_LIDS_KEY_DATES WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_REHAB_PROVIDERS ORP
                WHERE EXISTS (SELECT 1
        FROM OFFENDER_REHAB_DECISIONS ORD
        WHERE ORD.OFFENDER_REHAB_DECISION_ID = ORP.OFFENDER_REHAB_DECISION_ID
                AND ORD.OFFENDER_BOOK_ID IN (?))


        DELETE FROM OFFENDER_REHAB_DECISIONS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_SUBSTANCE_DETAILS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_SUBSTANCE_TREATMENTS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_SUBSTANCE_USES WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_VISIT_VISITORS OVV
                WHERE EXISTS (SELECT 1
        FROM OFFENDER_VISITS OV
        WHERE OVV.OFFENDER_VISIT_ID = OV.OFFENDER_VISIT_ID
                AND OV.OFFENDER_BOOK_ID IN (?))


        DELETE FROM OFFENDER_VISITS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_VISIT_BALANCE_ADJS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_VISIT_BALANCES WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_VO_VISITORS OVV
                WHERE EXISTS (SELECT 1
        FROM OFFENDER_VISIT_ORDERS OVO
        WHERE OVV.OFFENDER_VISIT_ORDER_ID = OVO.OFFENDER_VISIT_ORDER_ID
                AND OVO.OFFENDER_BOOK_ID IN (?))


        DELETE FROM OFFENDER_VISIT_ORDERS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_VSC_SENTENCE_TERMS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_VSC_SENTENCES WHERE OFFENDER_BOOK_ID IN (?)


        SELECT INCIDENT_CASE_ID FROM INCIDENT_CASE_PARTIES WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM INCIDENT_CASE_PARTIES WHERE INCIDENT_CASE_ID IN (?, ?, ?)


        DELETE FROM INCIDENT_CASE_RESPONSES WHERE INCIDENT_CASE_ID IN (?, ?, ?)


        DELETE FROM INCIDENT_CASE_QUESTIONS WHERE INCIDENT_CASE_ID IN (?, ?, ?)


        DELETE FROM INCIDENT_QUE_RESPONSE_HTY IQRH
                WHERE EXISTS (SELECT 1
        FROM INCIDENT_QUESTIONNAIRE_HTY IQH
        WHERE IQRH.INCIDENT_QUESTIONNAIRE_ID = IQH.INCIDENT_QUESTIONNAIRE_ID
                AND IQH.INCIDENT_CASE_ID IN (?, ?, ?))


        DELETE FROM INCIDENT_QUE_QUESTION_HTY IQQH
                WHERE EXISTS (SELECT 1
        FROM INCIDENT_QUESTIONNAIRE_HTY IQH
        WHERE IQQH.INCIDENT_QUESTIONNAIRE_ID = IQH.INCIDENT_QUESTIONNAIRE_ID
                AND IQH.INCIDENT_CASE_ID IN (?, ?, ?))


        DELETE FROM INCIDENT_QUESTIONNAIRE_HTY WHERE INCIDENT_CASE_ID IN (?, ?, ?)


        DELETE FROM INCIDENT_CASE_REQUIREMENTS WHERE INCIDENT_CASE_ID IN (?, ?, ?)


        DELETE FROM INCIDENT_CASES WHERE INCIDENT_CASE_ID IN (?, ?, ?)


        DELETE FROM BED_ASSIGNMENT_HISTORIES WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM CASE_ASSOCIATED_PERSONS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM IWP_DOCUMENTS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_BOOKING_AGY_LOCS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_BOOKING_EVENTS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_CASE_ASSOCIATIONS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_CASE_OFFICERS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_DATA_CORRECTIONS_HTY WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_DISCHARGE_BALANCES WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_EDUCATIONS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_EMPLOYMENTS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_FINE_PAYMENTS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_FIXED_TERM_RECALLS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_IEP_LEVELS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_IMPRISON_STATUSES WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_INTER_MVMT_LOCATIONS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_KEY_WORKERS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_LANGUAGES WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_MILITARY_RECORDS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_NO_PAY_PERIODS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_OGRS3_RISK_PREDICTORS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_PAY_STATUSES WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_PPTY_CONTAINERS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_PROFILES WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_RELEASE_DETAILS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_RELEASE_DETAILS_HTY WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_RESTRICTIONS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_RISK_PREDICTORS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_SUPERVISING_COURTS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_TEAM_ASSIGNMENTS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_TEAM_ASSIGN_HTY WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_TEST_SELECTIONS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_TMP_REL_SCHEDULES WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_TRUST_ACCOUNTS_TEMP WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_VSC_ERROR_LOGS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_VSC_SENT_CALCULATIONS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM TASK_ASSIGNMENT_HTY WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM WORKFLOW_HISTORY WHERE ORIG_OFFENDER_BOOK_ID IN (?)


        UPDATE GL_TRANSACTIONS
                SET OFFENDER_ID = NULL, OFFENDER_BOOK_ID = NULL
        WHERE OFFENDER_BOOK_ID IN (?)
        OR OFFENDER_ID IN (?)


        DELETE FROM OFFENDER_BELIEFS
        WHERE OFFENDER_BOOK_ID IN (?)
        OR ROOT_OFFENDER_ID IN (?)


        DELETE FROM OFFENDER_PROFILE_DETAILS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_PHYSICAL_ATTRIBUTES WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_IMAGES WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_IDENTIFYING_MARKS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_EXTERNAL_MOVEMENTS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_BOOKING_DETAILS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_ALERTS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_GANG_INVESTS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_GANG_EVIDENCES WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_GANG_AFFILIATIONS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_NA_DETAILS WHERE OFFENDER_BOOK_ID IN (?) OR NS_OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_NON_ASSOCIATIONS WHERE OFFENDER_BOOK_ID IN (?) OR NS_OFFENDER_BOOK_ID IN (?)


        DELETE FROM HDC_CALC_EXCLUSION_REASONS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_SENT_CALCULATIONS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_ASSESSMENT_ITEMS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_ASSESSMENTS WHERE OFFENDER_BOOK_ID IN (?)


        DELETE FROM OFFENDER_BOOKINGS WHERE OFFENDER_ID IN (?)


        DELETE FROM INTERNET_ADDRESSES
        WHERE OWNER_CLASS = 'OFF'
        AND OWNER_ID IN (?)


        DELETE FROM PHONES P
                WHERE (P.OWNER_CLASS = 'OFF' AND P.OWNER_ID IN (?))
        OR (P.OWNER_CLASS = 'ADDR' AND EXISTS (SELECT 1 FROM ADDRESSES A
                WHERE P.OWNER_ID = A.ADDRESS_ID
                AND A.OWNER_CLASS = 'OFF'
                AND A.OWNER_ID IN (?)))


        DELETE FROM ADDRESS_USAGES AU
                WHERE EXISTS (SELECT 1
        FROM ADDRESSES A
        WHERE AU.ADDRESS_ID = A.ADDRESS_ID
                AND OWNER_CLASS = 'OFF'
        AND A.OWNER_ID IN (?))


        DELETE FROM ADDRESSES
        WHERE OWNER_CLASS = 'OFF'
        AND OWNER_ID IN (?)


        DELETE FROM OFFENDER_TRANSACTION_DETAILS OTD
                WHERE EXISTS (SELECT 1
        FROM OFFENDER_TRANSACTIONS OT
        WHERE OTD.TXN_ID = OT.TXN_ID
                AND OTD.TXN_ENTRY_SEQ = OT.TXN_ENTRY_SEQ
                AND OT.OFFENDER_ID IN (?))


        DELETE FROM OFFENDER_TRANSACTIONS WHERE OFFENDER_ID IN (?)


        DELETE FROM BENEFICIARY_TRANSACTIONS BT
                WHERE EXISTS (SELECT 1
        FROM OFFENDER_BENEFICIARIES OB
        WHERE OB.BENEFICIARY_ID = BT.BENEFICIARY_ID
                AND OB.OFFENDER_ID IN (?))


        DELETE FROM OFFENDER_BENEFICIARIES WHERE OFFENDER_ID IN (?)


        DELETE FROM OFFENDER_ADJUSTMENT_TXNS WHERE OFFENDER_ID IN (?)


        DELETE FROM OFFENDER_DEDUCTION_RECEIPTS ODR
                WHERE EXISTS (SELECT 1
        FROM OFFENDER_DEDUCTIONS OD
        WHERE OD.OFFENDER_DEDUCTION_ID = ODR.OFFENDER_DEDUCTION_ID
                AND OD.OFFENDER_ID IN (?))


        DELETE FROM OFFENDER_DEDUCTIONS WHERE OFFENDER_ID IN (?)


        DELETE FROM OFFENDER_SUB_ACCOUNTS WHERE OFFENDER_ID IN (?)


        DELETE FROM OFFENDER_TRUST_ACCOUNTS WHERE OFFENDER_ID IN (?)


        DELETE FROM OFFENDER_PAYMENT_PROFILES WHERE OFFENDER_ID IN (?)


        DELETE FROM BANK_CHEQUE_BENEFICIARIES WHERE OFFENDER_ID IN (?)


        DELETE FROM OFFENDER_DAMAGE_OBLIGATIONS WHERE OFFENDER_ID IN (?)


        DELETE FROM OFFENDER_FREEZE_DISBURSEMENTS WHERE OFFENDER_ID IN (?)


        DELETE FROM OFFENDER_MINIMUM_BALANCES WHERE OFFENDER_ID IN (?)


        DELETE FROM SYSTEM_REPORT_REQUESTS WHERE OFFENDER_ID IN (?)


        DELETE FROM MERGE_TRANSACTION_LOGS MTL
                WHERE EXISTS (SELECT 1
        FROM MERGE_TRANSACTIONS MT
        WHERE MT.MERGE_TRANSACTION_ID = MTL.MERGE_TRANSACTION_ID
                AND (MT.OFFENDER_ID_1 IN (?) OR MT.OFFENDER_ID_2 IN (?)))


        DELETE FROM MERGE_TRANSACTIONS WHERE OFFENDER_ID_1 IN (?) OR OFFENDER_ID_2 IN (?)


        DELETE FROM LOCKED_MODULES WHERE OFFENDER_ID IN (?)


        DELETE FROM OFFENDER_IMMIGRATION_APPEALS WHERE ROOT_OFFENDER_ID IN (?)


        DELETE FROM OFFENDER_IDENTIFIERS WHERE OFFENDER_ID IN (?)


        DELETE FROM OFFENDERS WHERE OFFENDER_ID IN (?) OR ALIAS_OFFENDER_ID IN (?)


```