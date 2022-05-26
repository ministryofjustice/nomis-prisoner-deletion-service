package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository

import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.Movement
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.sql.MovementsRepositorySql

@Repository
class MovementsRepository(val jdbcTemplate: NamedParameterJdbcOperations) {

  val rowMapper: DataClassRowMapper<Movement> = DataClassRowMapper.newInstance(Movement::class.java)

  fun getMovementsByOffenders(
    offenderNumbers: List<String>,
    movementTypes: List<String>?,
    latestOnly: Boolean,
    allBookings: Boolean
  ): List<Movement> {
    val firstSeqOnly = if (allBookings) "" else " AND OB.BOOKING_SEQ = 1"

    return if (movementTypes.isNullOrEmpty()) {
      jdbcTemplate.query(
        String.format(MovementsRepositorySql.GET_MOVEMENTS_BY_OFFENDERS.sql, firstSeqOnly),
        mapOf(
          "offenderNumbers" to offenderNumbers,
          "latestOnly" to latestOnly
        ),
        rowMapper
      )
    } else jdbcTemplate.query(
      String.format(MovementsRepositorySql.GET_MOVEMENTS_BY_OFFENDERS_AND_MOVEMENT_TYPES.sql, firstSeqOnly),
      mapOf(
        "offenderNumbers" to offenderNumbers,
        "movementTypes" to movementTypes,
        "latestOnly" to latestOnly
      ),
      rowMapper
    )
  }

  fun getDeceasedMovementByOffenders(offenderNumbers: List<String>,): Movement? {
    return jdbcTemplate.query(
      String.format(MovementsRepositorySql.GET_DECEASED_MOVEMENT_BY_OFFENDERS.sql),
      mapOf(
        "offenderNumbers" to offenderNumbers,
      ),
      rowMapper
    ).getOrNull(0)
  }
}
