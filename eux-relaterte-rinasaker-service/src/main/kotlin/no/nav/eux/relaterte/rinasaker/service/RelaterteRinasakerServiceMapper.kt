package no.nav.eux.relaterte.rinasaker.service

import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerCreateRequest
import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerGruppe
import java.time.LocalDateTime.now

fun RelaterteRinasakerCreateRequest.toRelaterteRinasaker() =
    RelaterteRinasakerGruppe(
        relaterteRinasakerGruppeId = relaterteRinasakerId,
        rinasakIdList = listOf(rinasakIdA, rinasakIdB),
        beskrivelse = beskrivelse,
        opprettetDato = opprettetDato ?: now()
    )
