package no.nav.eux.relaterte.rinasaker.service

import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerGruppe
import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerGruppeCreateRequest
import java.time.LocalDateTime.now

fun RelaterteRinasakerGruppeCreateRequest.toRelaterteRinasaker() =
    RelaterteRinasakerGruppe(
        relaterteRinasakerGruppeId = relaterteRinasakerId,
        rinasakIdList = rinasakIdList,
        beskrivelse = beskrivelse,
        opprettetDato = opprettetDato ?: now()
    )
