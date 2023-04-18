package no.nav.eux.relaterte.rinasaker.model

import java.time.LocalDateTime
import java.util.*

data class RelaterteRinasakerGruppeCreateRequest(
    val relaterteRinasakerId: UUID,
    val rinasakIdList: List<String>,
    val beskrivelse: String?,
    val opprettetDato: LocalDateTime?,
)
