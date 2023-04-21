package no.nav.eux.relaterte.rinasaker.webapp.model

import java.time.OffsetDateTime
import java.util.*

data class RelaterteRinasakerForespørsel(
    val relaterteRinasakerId: UUID = UUID.fromString("cdb8499f-8a6b-476a-91c8-4403f792cab0"),
    val beskrivelse: String = "En forespørsel",
    val opprettetDato: OffsetDateTime = OffsetDateTime.parse("2023-04-21T10:40:40.897718Z"),
    val rinasakIdList: List<String> = emptyList(),
)
