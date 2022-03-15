package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated

@Validated
@Configuration
class DataComplianceProperties(
  @Value("\${data.compliance.deletion.enabled:false}") val deletionEnabled: Boolean,
  @Value("\${data.compliance.deceased.deletion.enabled:false}") val deceasedDeletionEnabled: Boolean,
  @Value("\${data.compliance.offender.no.booking.deletion.enabled:false}") val offenderNoBookingDeletionEnabled: Boolean
) {

  init {
    log.info("Data compliance deletion enabled: {}", deletionEnabled)
    log.info("Data compliance deceased deletion enabled: {}", deceasedDeletionEnabled)
    log.info("Data compliance offender no booking deletion enabled: {}", offenderNoBookingDeletionEnabled)
  }

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}
