package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderPendingDeletion
import java.time.LocalDate

@Repository
interface DeceasedOffenderPendingDeletionRepository : CrudRepository<OffenderPendingDeletion, String> {

  /**
   * The following query finds offenders that satisfy the following requirements.
   * <p>
   * -The offender is deceased in custody
   * -It has been 22 years (264 months) since the offender was recorded as deceased
   * <p>
   *
   */
  @Query(
    value = """
      SELECT o.OFFENDER_ID_DISPLAY 
      FROM OFFENDERS o 
      INNER JOIN OFFENDER_BOOKINGS ob 
      ON o.OFFENDER_ID = ob.OFFENDER_ID 
      INNER JOIN OFFENDER_EXTERNAL_MOVEMENTS oem 
      ON oem.OFFENDER_BOOK_ID = ob.OFFENDER_BOOK_ID 
      WHERE oem.MOVEMENT_REASON_CODE = 'DEC' 
      AND oem.MOVEMENT_DATE < add_months(:today, -264) 
      ORDER BY oem.MOVEMENT_DATE DESC""",
    nativeQuery = true
  )
  fun findDeceasedOffendersDueForDeletion(
    @Param("today") today: LocalDate,
    pageable: Pageable
  ): List<OffenderPendingDeletion>
}
