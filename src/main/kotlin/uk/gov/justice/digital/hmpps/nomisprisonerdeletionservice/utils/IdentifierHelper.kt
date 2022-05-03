package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.utils

import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderAliasPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.repository.model.OffenderIdentifierPendingDeletion
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.validation.ChecksumComponents
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.validation.getValidCroComponents
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.validation.getValidPncComponents
import java.util.function.Predicate
import java.util.stream.Stream

fun getFormattedCroNumbersFrom(offenderAliases: List<OffenderAliasPendingDeletion>): Set<String> {
  return getIdentifiersFrom(offenderAliases, OffenderIdentifierPendingDeletion::isCro)
    .map(::getValidCroComponents)
    .map { formatChecksumComponentsWithNoLeadingZeros(it) }
    .toList()
    .toSet()
}

fun getFormattedPncNumbersFrom(offenderAliases: List<OffenderAliasPendingDeletion>): Set<String> {
  return getIdentifiersFrom(offenderAliases, OffenderIdentifierPendingDeletion::isPnc)
    .map(::getValidPncComponents)
    .map(::formatChecksumComponentsWithNoLeadingZeros)
    .toList()
    .toSet()
}

fun formatChecksumComponentsWithNoLeadingZeros(components: ChecksumComponents?): String {
  return components?.year.toString() + "/" + components?.serial?.toInt() + components?.checksum
}

fun getIdentifiersFrom(
  offenderAliasPendingDeletions: Collection<OffenderAliasPendingDeletion>,
  typeFilter: Predicate<OffenderIdentifierPendingDeletion>
): Stream<String> {
  return offenderAliasPendingDeletions.stream()
    .map(OffenderAliasPendingDeletion::offenderIdentifiers)
    .flatMap { it.stream() }
    .filter(typeFilter)
    .map(OffenderIdentifierPendingDeletion::identifier)
    .map { it?.trim() }
}
