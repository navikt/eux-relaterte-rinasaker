package no.nav.eux.relaterte.rinasaker.webapp.dataset

import no.nav.eux.relaterte.rinasaker.webapp.common.offsetDateTime
import no.nav.eux.relaterte.rinasaker.webapp.common.uuid1
import no.nav.eux.relaterte.rinasaker.webapp.common.uuid2
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasaker
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasakerForespørsel
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasakerGruppe

val relaterteRinasakerForespørsel1 = RelaterteRinasakerForespørsel(
    relaterteRinasakerId = uuid1,
    beskrivelse = "Første gruppe",
    rinasakIdList = listOf("a", "b"),
)

val relaterteRinasakerForespørsel2 = RelaterteRinasakerForespørsel(
    relaterteRinasakerId = uuid2,
    beskrivelse = "Andre gruppe",
    rinasakIdList = listOf("a", "c"),
)

val relaterteRinasakerForespørsler = listOf(
    relaterteRinasakerForespørsel1,
    relaterteRinasakerForespørsel2,
)

val forventetRelaterteRinasakerGruppe = RelaterteRinasakerGruppe(
    relaterteRinasaker = listOf(
        RelaterteRinasaker(
            relaterteRinasakerId = uuid1,
            beskrivelse = "Første gruppe",
            opprettetDato = offsetDateTime,
            rinasakIdList = listOf("a", "b"),
        ),
        RelaterteRinasaker(
            relaterteRinasakerId = uuid2,
            beskrivelse = "Andre gruppe",
            opprettetDato = offsetDateTime,
            rinasakIdList = listOf("a", "c"),
        ),
    )
)

val forventetRelaterteRinasakerGruppeKunB = RelaterteRinasakerGruppe(
    relaterteRinasaker = listOf(
        RelaterteRinasaker(
            relaterteRinasakerId = uuid1,
            beskrivelse = "Første gruppe",
            opprettetDato = offsetDateTime,
            rinasakIdList = listOf("a", "b"),
        ),
    )
)
