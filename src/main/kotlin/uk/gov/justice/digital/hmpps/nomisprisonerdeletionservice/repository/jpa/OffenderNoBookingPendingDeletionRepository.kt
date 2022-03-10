package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderPendingDeletion

@Repository
interface OffenderNoBookingPendingDeletionRepository : JpaRepository<OffenderPendingDeletion, String> {

  /**
   * The following query finds offenders that satisfy the following requirements.
   * <p>
   * -Offender has no booking
   * -Offender is not tagged bail (OFFENDER_IDENTIFIER:TBRI) ->  (Tagged Bail)
   * <p>
   *
   */
  @Query(
    value = """
    WITH OFFENDERS_NO_BOOKING_IDS AS (
    SELECT DISTINCT o.OFFENDER_ID_DISPLAY
    FROM OFFENDERS o
    MINUS
    SELECT DISTINCT o.OFFENDER_ID_DISPLAY
    FROM OFFENDERS o
    INNER JOIN OFFENDER_BOOKINGS ob
    ON ob.OFFENDER_ID = o.OFFENDER_ID
    MINUS 
    SELECT DISTINCT o.OFFENDER_ID_DISPLAY
    FROM OFFENDERS o
    INNER JOIN OFFENDER_IDENTIFIERS oi 
    ON oi.OFFENDER_ID = o.OFFENDER_ID 
    WHERE oi.IDENTIFIER_TYPE = 'TBRI')
    SELECT o.OFFENDER_ID_DISPLAY
    FROM OFFENDERS o 
    INNER JOIN OFFENDERS_NO_BOOKING_IDS onb 
    ON onb.OFFENDER_ID_DISPLAY = o.OFFENDER_ID_DISPLAY
    GROUP BY o.OFFENDER_ID_DISPLAY 
    ORDER BY MAX(o.CREATE_DATE) ASC""",
    nativeQuery = true
  )
  fun findOffendersWithNoBookingsDueForDeletion(pageable: Pageable): List<OffenderPendingDeletion>
}
