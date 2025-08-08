package no.nav.eux.relaterte.rinasaker.webapp.dataset

import no.nav.eux.relaterte.rinasaker.webapp.common.offsetDateTime
import no.nav.eux.relaterte.rinasaker.webapp.common.uuid1
import no.nav.eux.relaterte.rinasaker.webapp.common.uuid2
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasaker
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasakerForespørsel
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasakerGruppe
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasakerOppdatering


val expectedRelaterteRinasakerGruppe = RelaterteRinasakerGruppe(
    listOf(
        RelaterteRinasaker(
            relaterteRinasakerId = uuid1,
            beskrivelse = "En forespørsel",
            opprettetDato = offsetDateTime
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

val expectedRelaterteRinasakerGruppeKunB = RelaterteRinasakerGruppe(
    listOf(
        RelaterteRinasaker(
            relaterteRinasakerId = uuid1,
            beskrivelse = "En forespørsel",
            rinasakIdList = listOf("a", "b")

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

val expectedRelaterteRinasakerGruppeEnTilMangeOppdatertNyBeskrivelse = RelaterteRinasakerGruppe(
    listOf(
        RelaterteRinasaker(
            relaterteRinasakerId = uuid1,
            beskrivelse = "Ny beskrivelse",
            rinasakIdList = listOf("a", "b")

        ),
        RelaterteRinasaker(
            relaterteRinasakerId = uuid2,
            beskrivelse = "En forespørsel",
            rinasakIdList = listOf("a", "c")
        )
    )
)

val relaterteRinasakerForespørsel = listOf(
    RelaterteRinasakerForespørsel(
        relaterteRinasakerId = uuid1,
        rinasakIdList = listOf("a", "b")
    ),
    RelaterteRinasakerForespørsel(
        relaterteRinasakerId = uuid2,
        rinasakIdList = listOf("a", "c")
    ),
)

val relaterteRinasakerOppdatering = RelaterteRinasakerOppdatering(
    relaterteRinasakerId = uuid1,
    rinasakIdList = listOf("a", "b2"),
)

val relaterteRinasakerOppdateringNyBeskrivelse = RelaterteRinasakerOppdatering(
    relaterteRinasakerId = uuid1,
    beskrivelse = "Ny beskrivelse"
)
