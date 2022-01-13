package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql

import org.intellij.lang.annotations.Language

enum class MovementsRepositorySql(val sql: String) {

  @Language("SQL")
  GET_MOVEMENTS_BY_OFFENDERS_AND_MOVEMENT_TYPES(
    """
        SELECT OFFENDERS.OFFENDER_ID_DISPLAY  AS OFFENDER_NO,
        OEM.CREATE_DATETIME            AS CREATE_DATE_TIME,
        OEM.FROM_AGY_LOC_ID            AS FROM_AGENCY,
        OEM.TO_AGY_LOC_ID              AS TO_AGENCY,
        OEM.MOVEMENT_DATE,
        OEM.MOVEMENT_TIME,
        OEM.MOVEMENT_TYPE,
        OEM.DIRECTION_CODE,
        OEM.COMMENT_TEXT,
        AL1.DESCRIPTION               AS FROM_AGENCY_DESCRIPTION,
        AL2.DESCRIPTION               AS TO_AGENCY_DESCRIPTION,
        RC1.DESCRIPTION               AS MOVEMENT_TYPE_DESCRIPTION,
        RC2.DESCRIPTION               AS MOVEMENT_REASON,
        RC3.DESCRIPTION               AS FROM_CITY,
        RC4.DESCRIPTION               AS TO_CITY
        FROM OFFENDER_EXTERNAL_MOVEMENTS OEM
        INNER JOIN OFFENDER_BOOKINGS OB    ON OB.OFFENDER_BOOK_ID = OEM.OFFENDER_BOOK_ID %s
        INNER JOIN OFFENDERS               ON OFFENDERS.OFFENDER_ID = OB.OFFENDER_ID
        LEFT JOIN AGENCY_LOCATIONS AL1 ON OEM.FROM_AGY_LOC_ID = AL1.AGY_LOC_ID
                LEFT JOIN AGENCY_LOCATIONS AL2 ON OEM.TO_AGY_LOC_ID = AL2.AGY_LOC_ID
                LEFT JOIN REFERENCE_CODES RC1 ON RC1.CODE = OEM.MOVEMENT_TYPE AND RC1.DOMAIN = 'MOVE_TYPE'
        LEFT JOIN REFERENCE_CODES RC2 ON RC2.CODE = OEM.MOVEMENT_REASON_CODE AND RC2.DOMAIN = 'MOVE_RSN'
        LEFT JOIN REFERENCE_CODES RC3 ON RC3.CODE = OEM.FROM_CITY AND RC3.DOMAIN = 'CITY'
        LEFT JOIN REFERENCE_CODES RC4 ON RC4.CODE = OEM.TO_CITY AND RC4.DOMAIN = 'CITY'
        WHERE (:latestOnly = 0 OR
        OEM.MOVEMENT_SEQ = (SELECT MAX(OEM2.MOVEMENT_SEQ) FROM OFFENDER_EXTERNAL_MOVEMENTS OEM2
                WHERE OEM2.OFFENDER_BOOK_ID = OEM.OFFENDER_BOOK_ID
                AND OEM2.MOVEMENT_TYPE IN (:movementTypes)))
        AND OB.AGY_LOC_ID <> 'ZZGHI'
        AND OFFENDERS.OFFENDER_ID_DISPLAY in (:offenderNumbers)
        AND OEM.MOVEMENT_TYPE IN (:movementTypes)
    """
  ),

  @Language("SQL")
  GET_MOVEMENTS_BY_OFFENDERS(
    """
        SELECT OFFENDERS.OFFENDER_ID_DISPLAY  AS OFFENDER_NO,
        OEM.CREATE_DATETIME            AS CREATE_DATE_TIME,
        OEM.MOVEMENT_TYPE,
        OEM.MOVEMENT_DATE,
        OEM.MOVEMENT_TIME,
        OEM.FROM_AGY_LOC_ID            AS FROM_AGENCY,
        OEM.TO_AGY_LOC_ID              AS TO_AGENCY,
        OEM.DIRECTION_CODE,
        OEM.COMMENT_TEXT,
        AL1.DESCRIPTION               AS FROM_AGENCY_DESCRIPTION,
        AL2.DESCRIPTION               AS TO_AGENCY_DESCRIPTION,
        RC1.DESCRIPTION               AS MOVEMENT_TYPE_DESCRIPTION,
        RC2.DESCRIPTION               AS MOVEMENT_REASON,
        RC3.DESCRIPTION               AS FROM_CITY,
        RC4.DESCRIPTION               AS TO_CITY
        FROM OFFENDER_EXTERNAL_MOVEMENTS OEM
        INNER JOIN OFFENDER_BOOKINGS OB    ON OB.OFFENDER_BOOK_ID = OEM.OFFENDER_BOOK_ID %s
        INNER JOIN OFFENDERS               ON OFFENDERS.OFFENDER_ID = OB.OFFENDER_ID
        LEFT JOIN AGENCY_LOCATIONS AL1 ON OEM.FROM_AGY_LOC_ID = AL1.AGY_LOC_ID
                LEFT JOIN AGENCY_LOCATIONS AL2 ON OEM.TO_AGY_LOC_ID = AL2.AGY_LOC_ID
                LEFT JOIN REFERENCE_CODES RC1 ON RC1.CODE = OEM.MOVEMENT_TYPE AND RC1.DOMAIN = 'MOVE_TYPE'
        LEFT JOIN REFERENCE_CODES RC2 ON RC2.CODE = OEM.MOVEMENT_REASON_CODE AND RC2.DOMAIN = 'MOVE_RSN'
        LEFT JOIN REFERENCE_CODES RC3 ON RC3.CODE = OEM.FROM_CITY AND RC3.DOMAIN = 'CITY'
        LEFT JOIN REFERENCE_CODES RC4 ON RC4.CODE = OEM.TO_CITY AND RC4.DOMAIN = 'CITY'
        WHERE (:latestOnly = 0 OR
        OEM.MOVEMENT_SEQ = (SELECT MAX(OEM2.MOVEMENT_SEQ) FROM OFFENDER_EXTERNAL_MOVEMENTS OEM2
                WHERE OEM2.OFFENDER_BOOK_ID = OEM.OFFENDER_BOOK_ID))
        AND OB.AGY_LOC_ID <> 'ZZGHI'
        AND OFFENDERS.OFFENDER_ID_DISPLAY in (:offenderNumbers)
    """
  )
}
