package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock
import java.time.ZoneOffset.UTC
import java.util.TimeZone
import javax.annotation.PostConstruct

@Configuration
class LocaleConfig {
  @PostConstruct
  fun init() {
    TimeZone.setDefault(TimeZone.getTimeZone(UTC))
  }

  @Bean
  fun clock(): Clock = Clock.systemUTC()
}
