package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NomisPrisonerDeletionService

fun main(args: Array<String>) {
  runApplication<NomisPrisonerDeletionService>(*args)
}
