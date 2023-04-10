package no.nav.eux.relaterte.rinasaker.model

import java.time.LocalDateTime
import java.util.*

class RelaterteRinasakerCreateRequest(
    val relaterteRinasakerId: UUID,
    val rinasakIdA: String,
    val rinasakIdB: String,
    val beskrivelse: String?,
    val opprettetDato: LocalDateTime?,
)
