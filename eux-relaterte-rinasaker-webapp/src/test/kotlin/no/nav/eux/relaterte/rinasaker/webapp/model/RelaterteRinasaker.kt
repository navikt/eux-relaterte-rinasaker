package no.nav.eux.relaterte.rinasaker.webapp.model

import no.nav.eux.relaterte.rinasaker.webapp.common.offsetDateTime
import java.time.OffsetDateTime
import java.util.*

data class RelaterteRinasaker(
    val relaterteRinasakerId: UUID,
    val beskrivelse: String = "Relaterte rinasaker",
    val opprettetDato: OffsetDateTime = offsetDateTime,
    val rinasakIdList: List<String> = emptyList(),
)
