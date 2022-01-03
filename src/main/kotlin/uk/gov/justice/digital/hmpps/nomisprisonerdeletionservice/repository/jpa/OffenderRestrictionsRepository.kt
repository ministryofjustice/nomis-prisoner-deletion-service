package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderRestrictions

@Repository
interface OffenderRestrictionsRepository : CrudRepository<OffenderRestrictions, String> {

  @Query(
    value = """
      SELECT * FROM offender_restrictions 
      WHERE offender_book_id IN (:bookIds) 
      AND (RESTRICTION_TYPE IN (:offenderRestrictionCodes) 
      OR REGEXP_LIKE (comment_text, :regex, 'i'))""",
    nativeQuery = true
  )
  fun findOffenderRestrictions(
    bookIds: Set<Long>,
    offenderRestrictionCodes: Set<String>,
    regex: String
  ): List<OffenderRestrictions>
}
