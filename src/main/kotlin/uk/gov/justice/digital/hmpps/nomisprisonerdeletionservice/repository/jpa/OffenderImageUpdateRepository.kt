package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderWithImage
import java.time.LocalDateTime

@Repository
interface OffenderImageUpdateRepository : PagingAndSortingRepository<OffenderWithImage, Long> {

  @Query(
    value = """
      SELECT o.offender_id_display 
      FROM offenders o 
      INNER JOIN offender_bookings ob ON ob.offender_id = o.offender_id 
      INNER JOIN offender_images oi ON oi.offender_book_id = ob.offender_book_id 
      WHERE oi.capture_datetime > :start""",
    nativeQuery = true
  )
  fun getOffendersWithImagesCapturedAfter(
    @Param("start") start: LocalDateTime,
    pageable: Pageable
  ): Page<OffenderWithImage>
}
