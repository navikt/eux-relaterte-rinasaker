package no.nav.eux.relaterte.rinasaker.webapp.model

import java.time.OffsetDateTime
import java.time.OffsetDateTime.now
import java.util.*
import java.util.UUID.randomUUID

data class RelaterteRinasaker(
    val relaterteRinasakerId: UUID = randomUUID(),
    val beskrivelse: String = "Relaterte rinasaker",
    val opprettetDato: OffsetDateTime = now(),
    val rinasakIdList: List<String> = emptyList(),
)
