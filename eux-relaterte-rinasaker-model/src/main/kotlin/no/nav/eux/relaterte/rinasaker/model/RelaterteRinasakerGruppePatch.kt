package no.nav.eux.relaterte.rinasaker.model

import java.util.*

data class RelaterteRinasakerGruppePatch(
    val relaterteRinasakerGruppeId: UUID,
    val rinasakIdList: List<String>?,
    val beskrivelse: String?,
)
