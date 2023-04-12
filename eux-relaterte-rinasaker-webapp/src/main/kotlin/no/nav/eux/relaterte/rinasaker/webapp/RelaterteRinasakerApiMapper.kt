package no.nav.eux.relaterte.rinasaker.webapp

import no.nav.eux.relaterte.rinasaker.model.*
import java.time.OffsetDateTime.from
import java.time.ZoneOffset.UTC

fun List<RelaterteRinasakerCreateType>.toRelaterteRinasakerGruppeCreateRequestList() =
    map { it.toRelaterteRinasakerGruppeCreateRequest() }

fun RelaterteRinasakerCreateType.toRelaterteRinasakerGruppeCreateRequest() =
    RelaterteRinasakerGruppeCreateRequest(
        relaterteRinasakerId = relaterteRinasakerId,
        beskrivelse = beskrivelse,
        rinasakIdList = rinasakIdList ?: emptyList(),
        opprettetDato = opprettetDato?.toLocalDateTime(),
    )

fun List<RelaterteRinasakerType>.toRelaterteRinasakerSearchResponseType() =
    RelaterteRinasakerSearchResponseType(this)

fun List<RelaterteRinasakerGruppe>.toRelaterteRinasakerType() =
    this.map { it.toRelaterteRinasakerType() }

fun RelaterteRinasakerGruppe.toRelaterteRinasakerType() =
    RelaterteRinasakerType(
        relaterteRinasakerId = relaterteRinasakerGruppeId,
        opprettetDato = from(opprettetDato.atOffset(UTC)),
        rinasakIdList = rinasakIdList,
        beskrivelse = beskrivelse,
    )