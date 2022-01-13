package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.utils

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.text.WordUtils
import java.util.regex.Matcher
import java.util.regex.Pattern

fun String.formatLocation(): String {
  val description = WordUtils.capitalizeFully(this)
  val matcher: Matcher = pattern.matcher(description)
  val stringBuilder = StringBuilder()
  while (matcher.find()) {
    val matched = matcher.group(1)
    matcher.appendReplacement(stringBuilder, matched.uppercase())
  }
  matcher.appendTail(stringBuilder)
  return stringBuilder.toString()
}

fun String.capitalize(): String {
  return WordUtils.capitalizeFully(this)
}

fun Any.toJson(objectMapper: ObjectMapper): String? {
  return try {
    objectMapper.writeValueAsString(this)
  } catch (e: Exception) {
    throw RuntimeException(e)
  }
}

private val pattern by lazy {
  Pattern.compile(
    "\\b(" + java.lang.String.join("|", ABBREVIATIONS) + ")\\b",
    Pattern.CASE_INSENSITIVE
  )
}

val ABBREVIATIONS = listOf(
  "AAA",
  "ADTP",
  "AIC",
  "AM",
  "ATB",
  "BBV",
  "BHU",
  "BICS",
  "CAD",
  "CASU",
  "CES",
  "CGL",
  "CIT",
  "CSC",
  "CSU",
  "CTTLS",
  "CV",
  "DART",
  "DDU",
  "DHL",
  "DRU",
  "ETS",
  "ESOL",
  "FT",
  "GP",
  "GFSL",
  "HCC",
  "HDC",
  "HMP",
  "HMPYOI",
  "HR",
  "IAG",
  "ICT",
  "IDTS",
  "IMB",
  "IPD",
  "IPSO",
  "ISMS",
  "IT",
  "ITQ",
  "JAC",
  "LRC",
  "L&S",
  "MBU",
  "MCASU",
  "MDT",
  "MOD",
  "MPU",
  "NVQ",
  "OBP",
  "OMU",
  "OU",
  "PACT",
  "PASRO",
  "PCVL",
  "PE",
  "PICTA",
  "PIPE",
  "PM",
  "PT",
  "PTTLS",
  "RAM",
  "RAPT",
  "ROTL",
  "RSU",
  "SDP",
  "SIU",
  "SMS",
  "SOTP",
  "SPU",
  "STC",
  "TLC",
  "TSP",
  "UK",
  "VCC",
  "VDT",
  "VP",
  "VTC",
  "WFC",
  "YOI"
)
