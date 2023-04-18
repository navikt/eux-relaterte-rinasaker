package no.nav.eux.relaterte.rinasaker.model

import java.util.*

class RelaterteRinasakerGruppePatch(
    val relaterteRinasakerGruppeId: UUID,
    val rinasakIdList: List<String>?,
    val beskrivelse: String?,
)
