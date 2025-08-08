package no.nav.eux.relaterte.rinasaker.webapp.model

import no.nav.eux.relaterte.rinasaker.webapp.common.offsetDateTime
import no.nav.eux.relaterte.rinasaker.webapp.common.uuid1
import java.time.OffsetDateTime
import java.util.*

data class RelaterteRinasakerForespørsel(
    val relaterteRinasakerId: UUID = uuid1,
    val beskrivelse: String = "En forespørsel",
    val opprettetDato: OffsetDateTime = offsetDateTime,
    val rinasakIdList: List<String> = emptyList(),
)
