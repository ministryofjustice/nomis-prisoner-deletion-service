package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderAliasPendingDeletion

@Repository
interface OffenderAliasPendingDeletionRepository : CrudRepository<OffenderAliasPendingDeletion, Long> {

  fun findOffenderAliasPendingDeletionByOffenderNumber(@Param("offenderNumber") offenderNumber: String?): List<OffenderAliasPendingDeletion>
}
