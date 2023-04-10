package no.nav.eux.relaterte.rinasaker.webapp

import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasaker
import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerCreateRequest
import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerCreateType
import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerType
import java.time.OffsetDateTime

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

fun RelaterteRinasaker.toRelaterteRinasakType() =
    RelaterteRinasakerType(
        relaterteRinasakerId = relaterteRinasakerId,
        rinasakIdA = rinasakIdA,
        rinasakIdB = rinasakIdB,
        opprettetDato = OffsetDateTime.from(opprettetDato),
        beskrivelse = beskrivelse,
    )