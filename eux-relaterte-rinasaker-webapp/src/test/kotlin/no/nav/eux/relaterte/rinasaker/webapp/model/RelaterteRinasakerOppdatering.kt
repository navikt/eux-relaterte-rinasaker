package no.nav.eux.relaterte.rinasaker.webapp.model

import java.util.UUID

data class RelaterteRinasakerOppdatering(
    val relaterteRinasakerId: UUID,
    val beskrivelse: String? = null,
    val rinasakIdList: List<String>? = null,
)
