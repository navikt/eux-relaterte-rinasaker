package no.nav.eux.relaterte.rinasaker.webapp.model

import java.util.*

class RelaterteRinasakerOppdatering(
    val relaterteRinasakerId: UUID,
    val beskrivelse: String = "En oppdatering",
    val rinasakIdList: List<String>? = null
)
