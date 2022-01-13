package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.data.OffenderNumber
import java.time.LocalDateTime
import javax.transaction.Transactional

@Service
@Transactional
class OffenderImageUpdateService() {
  fun getOffendersWithImagesCapturedAfter(start: LocalDateTime, pageable: Pageable): Page<OffenderNumber> {

    return Page.empty()
  }
}
