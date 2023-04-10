package no.nav.eux.relaterte.rinasaker.service

import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasaker
import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerCreateRequest
import java.time.LocalDateTime.now

fun RelaterteRinasakerCreateRequest.toRelaterteRinasaker() =
    RelaterteRinasaker(
        relaterteRinasakerId = relaterteRinasakerId,
        rinasakIdA = rinasakIdA,
        rinasakIdB = rinasakIdB,
        beskrivelse = beskrivelse,
        opprettetDato = opprettetDato ?: now()
    )
