package no.nav.eux.relaterte.rinasaker.webapp.dataset

import no.nav.eux.relaterte.rinasaker.webapp.common.offsetDateTime1
import no.nav.eux.relaterte.rinasaker.webapp.common.uuid1
import no.nav.eux.relaterte.rinasaker.webapp.common.uuid2
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasaker
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasakerGruppe


val expectedRelaterteRinasakerGruppe = RelaterteRinasakerGruppe(
    listOf(
        RelaterteRinasaker(
            relaterteRinasakerId = uuid1,
            beskrivelse = "En forespørsel",
            offsetDateTime1
        )
    )
)

val expectedRelaterteRinasakerGruppeEnTilMange = RelaterteRinasakerGruppe(
    listOf(
        RelaterteRinasaker(
            relaterteRinasakerId = uuid1,
            beskrivelse = "En forespørsel",
            rinasakIdList = listOf("a", "b")

        ),
        RelaterteRinasaker(
            relaterteRinasakerId = uuid2,
            beskrivelse = "En forespørsel",
            rinasakIdList = listOf("a", "c")
        )
    )
)

val expectedRelaterteRinasakerGruppeEnTilMangeOppdatert = RelaterteRinasakerGruppe(
    listOf(
        RelaterteRinasaker(
            relaterteRinasakerId = uuid1,
            beskrivelse = "En oppdatering",
            rinasakIdList = listOf("a", "b2")

        ),
        RelaterteRinasaker(
            relaterteRinasakerId = uuid2,
            beskrivelse = "En forespørsel",
            rinasakIdList = listOf("a", "c")
        )
    )
)