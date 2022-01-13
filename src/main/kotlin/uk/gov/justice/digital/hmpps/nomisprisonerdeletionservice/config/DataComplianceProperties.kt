package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated

@Validated
@Configuration
class DataComplianceProperties(
  @Value("\${data.compliance.deletion.enabled:false}") deletionEnabled: Boolean,
  @Value("\${data.compliance.deceased.deletion.enabled:false}") deceasedDeletionEnabled: Boolean
) {
  val deletionEnabled: Boolean
  val deceasedDeletionEnabled: Boolean

  init {
    log.info("Data compliance deletion enabled: {}", deletionEnabled)
    log.info("Data compliance deceased deletion enabled: {}", deceasedDeletionEnabled)
    this.deletionEnabled = deletionEnabled
    this.deceasedDeletionEnabled = deceasedDeletionEnabled
  }

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}
