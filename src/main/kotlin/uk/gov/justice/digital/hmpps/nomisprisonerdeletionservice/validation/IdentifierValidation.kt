package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.validation

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Following the definitions provided here:
 * https://assets.publishing.service.gov.uk/government/uploads/system/uploads/attachment_data/file/862971/cjs-data-standards-catalogue-6.pdf
 */

fun getValidPncComponents(pncNumber: String): ChecksumComponents? {
  return matching(pncNumber, validPncFormat)?.let {
    ChecksumComponents(
      year = it.group(1),
      serial = it.group(2),
      serialFormat = "%07d",
      checksum = it.group(3)
    ).takeIf(::isChecksumAMatch)
  }
}

fun getValidCroComponents(croNumber: String): ChecksumComponents? {
  return getValidStandardCroComponents(croNumber) ?: getValidCroSfComponents(croNumber)
}

private fun getValidStandardCroComponents(croNumber: String): ChecksumComponents? {
  return matching(croNumber, validCroFormat)
    ?.let {
      ChecksumComponents(
        year = it.group(2),
        serial = it.group(1),
        serialFormat = "%06d",
        checksum = it.group(3)
      )
    }
    .takeIf(::isChecksumAMatch)
}

private fun getValidCroSfComponents(croNumber: String): ChecksumComponents? {
  return matching(croNumber, validCroSfFormat)
    ?.let {
      ChecksumComponents(
        year = it.group(1),
        serial = it.group(2),
        serialFormat = "%05d",
        checksum = it.group(3)
      )
    }
    .takeIf(::isChecksumAMatch)
}

private fun matching(identity: String, pattern: Pattern): Matcher? {
  return pattern.matcher(identity).takeIf { it.matches() }
}

private fun isChecksumAMatch(components: ChecksumComponents?): Boolean = if (components != null) {
  generateChecksumChar(components.derivedValue) == components.checksumChar
} else false

private fun generateChecksumChar(digits: Int): Char {
  return validChecksumCharacters[digits % validChecksumCharacters.size]
}

val validCroFormat: Pattern = Pattern.compile("^([0-9]{1,6})/([0-9]{2})([A-Z])$")
val validPncFormat: Pattern = Pattern.compile("^[0-9]{0,2}([0-9]{2})/([0-9]{1,7})([A-Z])$")
val validCroSfFormat: Pattern = Pattern.compile("^SF([0-9]{2})/([0-9]{1,5})([A-Z])$")

// All letters from the alphabet excluding 'I', 'O' and 'S', with 'Z' at the 0th index:
val validChecksumCharacters = charArrayOf(
  'Z',
  'A',
  'B',
  'C',
  'D',
  'E',
  'F',
  'G',
  'H',
  'J',
  'K',
  'L',
  'M',
  'N',
  'P',
  'Q',
  'R',
  'T',
  'U',
  'V',
  'W',
  'X',
  'Y'
)

data class ChecksumComponents(
  val year: String? = null,
  val serial: String? = null,
  val serialFormat: String? = null,
  val checksum: String? = null,
  val derivedValue: Int = (year + String.format(serialFormat!!, serial!!.toInt())).toInt(),
  val checksumChar: Char = checksum!![0],
)
