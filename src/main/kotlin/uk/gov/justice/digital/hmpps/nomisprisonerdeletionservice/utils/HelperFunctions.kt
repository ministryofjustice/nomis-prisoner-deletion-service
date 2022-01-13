package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.utils

inline fun <T : Any> guardLet(vararg elements: T?, closure: () -> Nothing): List<T> {
  return if (elements.all { it != null }) {
    elements.filterNotNull()
  } else {
    closure()
  }
}

inline fun <T : Any> ifLet(vararg elements: T?, closure: (List<T>) -> Unit) {
  if (elements.all { it != null }) {
    closure(elements.filterNotNull())
  }
}

infix fun <T> Collection<T>.deepEqualToIgnoreOrder(other: Collection<T>): Boolean {
  if (this !== other) {
    if (this.size != other.size) return false
    val areNotEqual = this.asSequence()
      .map { it in other }
      .contains(false)
    if (areNotEqual) return false
  }
  return true
}
