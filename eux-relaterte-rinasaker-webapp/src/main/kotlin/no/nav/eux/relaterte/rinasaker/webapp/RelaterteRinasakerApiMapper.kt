package no.nav.eux.relaterte.rinasaker.webapp

import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerCreateRequest
import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerCreateType
import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerGruppe
import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerType
import java.time.OffsetDateTime.from
import java.time.ZoneOffset.UTC

fun List<RelaterteRinasakerCreateType>.toRelaterteRinasakerCreateTypeList() =
    this.map { it.toRelaterteRinasaker() }

fun RelaterteRinasakerCreateType.toRelaterteRinasaker() =
    RelaterteRinasakerCreateRequest(
        relaterteRinasakerId = relaterteRinasakerId,
        rinasakIdA = rinasakIdA,
        rinasakIdB = rinasakIdB,
        beskrivelse = beskrivelse,
        opprettetDato = opprettetDato?.toLocalDateTime()
    )

fun RelaterteRinasakerGruppe.toRelaterteRinasakType() =
    RelaterteRinasakerType(
        relaterteRinasakerId = relaterteRinasakerGruppeId,
        rinasakIdA = rinasakIdList[0],
        rinasakIdB = rinasakIdList[1],
        opprettetDato = from(opprettetDato.atOffset(UTC)),
        beskrivelse = beskrivelse,
    )