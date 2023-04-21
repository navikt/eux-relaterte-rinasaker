package no.nav.eux.relaterte.rinasaker.webapp.model

import java.util.*
import java.util.UUID.randomUUID

class RelaterteRinasakerOppdatering(
    val relaterteRinasakerId: UUID = randomUUID(),
    val beskrivelse: String = "En oppdatering",
    val rinasakIdList: List<String> = emptyList()
)
