package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.validation

import com.google.common.base.Preconditions.checkState
import org.apache.commons.lang3.ObjectUtils.isNotEmpty
import org.springframework.stereotype.Component
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException
import javax.validation.ConstraintViolationException
import javax.validation.Validator

@Component
class Validator(val validator: Validator?) {

  fun validate(any: Any?) {
    val violations = validator?.validate(any)

    if (!violations.isNullOrEmpty()) {
      throw ConstraintViolationException(violations)
    }
  }
}

fun validateRegex(regex: String, payload: String) {
  checkState(isNotEmpty(regex), "Empty regex provided in request")
  try {
    Pattern.compile(regex)
  } catch (ex: PatternSyntaxException) {
    throw IllegalStateException("Invalid regex provided in request: $payload", ex)
  }
}
