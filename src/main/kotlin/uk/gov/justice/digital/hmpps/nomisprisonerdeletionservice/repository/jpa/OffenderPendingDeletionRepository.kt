package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.jpa

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.FIND_OFFENDER_DUE_FOR_DELETION
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.FROM_OFFENDERS_DUE_FOR_DELETION
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.GIVEN_CONDITIONS
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.IN_ORDER
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.SELECT_COUNT
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.SELECT_OFFENDER_NUMBER
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.SELECT_OFFENDER_NUMBERS
import java.time.LocalDate
import java.util.Optional

@Repository
interface OffenderPendingDeletionRepository : CrudRepository<OffenderPendingDeletion, String> {

  /**
   * The following query finds offenders that satisfy the following requirements.
   *
   * The offender's deletion due date (latest sentence expiry date plus seven years)
   * is within the requested time window AND:
   *
   * * Inactive, Out
   * * Not a lifer or IPP
   * * Never unlawfully at large
   * * No health problems
   * * Not deceased in custody
   * * Only has one booking across all aliases
   * * No incidents linking offender to another offender
   */
  @Query(
    value = FROM_OFFENDERS_DUE_FOR_DELETION + SELECT_OFFENDER_NUMBERS + GIVEN_CONDITIONS + IN_ORDER,
    countQuery = FROM_OFFENDERS_DUE_FOR_DELETION + SELECT_COUNT + GIVEN_CONDITIONS,
    nativeQuery = true
  )
  fun getOffendersDueForDeletionBetween(
    @Param("fromDate") fromDate: LocalDate,
    @Param("toDate") toDate: LocalDate,
    pageable: Pageable
  ): Page<OffenderPendingDeletion>

  @Query(value = FIND_OFFENDER_DUE_FOR_DELETION + SELECT_OFFENDER_NUMBER + GIVEN_CONDITIONS, nativeQuery = true)
  fun findOffenderPendingDeletion(
    @Param("offenderNo") offenderNo: String,
    @Param("today") today: LocalDate
  ): Optional<OffenderPendingDeletion>
}
