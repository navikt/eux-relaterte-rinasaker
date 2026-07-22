package no.nav.eux.relaterte.rinasaker.webapp

import no.nav.eux.relaterte.rinasaker.Application
import no.nav.eux.relaterte.rinasaker.webapp.common.relaterteRinasakerSokUrl
import no.nav.eux.relaterte.rinasaker.webapp.common.relaterteRinasakerSøkUrl
import no.nav.eux.relaterte.rinasaker.webapp.common.relaterteRinasakerUrl
import no.nav.eux.relaterte.rinasaker.webapp.common.token
import no.nav.eux.relaterte.rinasaker.webapp.common.uuid1
import no.nav.eux.relaterte.rinasaker.webapp.common.uuid3
import no.nav.eux.relaterte.rinasaker.webapp.dataset.forventetRelaterteRinasakerGruppe
import no.nav.eux.relaterte.rinasaker.webapp.dataset.forventetRelaterteRinasakerGruppeKunB
import no.nav.eux.relaterte.rinasaker.webapp.dataset.relaterteRinasakerForespørsel1
import no.nav.eux.relaterte.rinasaker.webapp.dataset.relaterteRinasakerForespørsler
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasaker
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasakerForespørsel
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasakerGruppe
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasakerOppdatering
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasakerSøk
import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.token.support.spring.test.EnableMockOAuth2Server
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.jdbc.JdbcTestUtils.deleteFromTables
import org.springframework.test.web.servlet.client.RestTestClient
import java.time.LocalDateTime
import java.util.UUID

@SpringBootTest(
    classes = [Application::class],
    webEnvironment = RANDOM_PORT
)
@ActiveProfiles("test")
@EnableMockOAuth2Server
@AutoConfigureRestTestClient
class RelaterteRinasakerApiImplTest {

    @Autowired
    lateinit var mockOAuth2Server: MockOAuth2Server

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    lateinit var client: RestTestClient

    @BeforeEach
    fun setUp() {
        deleteFromTables(
            jdbcTemplate,
            "relaterte_rinasaker",
            "relaterte_rinasaker_gruppe"
        )
    }

    @Test
    fun `POST søk - tom database returnerer tom gruppe - 200 OK`() {
        assertThat(søk()).isEqualTo(RelaterteRinasakerGruppe())
    }

    @Test
    fun `POST opprett - lagrer alle felt slik at gruppen kan søkes opp - 201 Created`() {
        opprett(listOf(relaterteRinasakerForespørsel1))

        assertGruppe(
            actual = søk(),
            expected = RelaterteRinasakerGruppe(
                relaterteRinasaker = listOf(
                    forventetRelaterteRinasakerGruppe.relaterteRinasaker.first()
                )
            ),
        )
    }

    @Test
    fun `POST søk - returnerer alle grupper sortert på id - 200 OK`() {
        opprett(relaterteRinasakerForespørsler.reversed())

        assertGruppe(søk(), forventetRelaterteRinasakerGruppe)
    }

    @Test
    fun `POST opprett - bruker standardverdier når valgfrie felt mangler - 201 Created`() {
        val førOpprettelse = LocalDateTime.now()
        val forespørsel = RelaterteRinasakerForespørsel(
            relaterteRinasakerId = uuid3,
            beskrivelse = null,
            opprettetDato = null,
            rinasakIdList = null,
        )

        opprett(listOf(forespørsel))

        val opprettet = søk().relaterteRinasaker.single()
        assertThat(opprettet.relaterteRinasakerId).isEqualTo(uuid3)
        assertThat(opprettet.beskrivelse).isNull()
        assertThat(opprettet.rinasakIdList).isEmpty()

        val lagretOpprettetDato = jdbcTemplate.queryForObject(
            """
            SELECT opprettet_dato
            FROM relaterte_rinasaker_gruppe
            WHERE relaterte_rinasaker_gruppe_id = ?
            """.trimIndent(),
            LocalDateTime::class.java,
            uuid3,
        )
        assertThat(lagretOpprettetDato)
            .isBetween(førOpprettelse.minusSeconds(1), LocalDateTime.now().plusSeconds(1))
    }

    @Test
    fun `POST søk og sok - finner samme gruppe på rinasakId - 200 OK`() {
        opprett(relaterteRinasakerForespørsler)

        listOf(relaterteRinasakerSøkUrl, relaterteRinasakerSokUrl)
            .map { søk(RelaterteRinasakerSøk(rinasakId = "b"), it) }
            .forEach { assertGruppe(it, forventetRelaterteRinasakerGruppeKunB) }
    }

    @Test
    fun `POST søk - rinasakId i flere grupper returnerer alle treff - 200 OK`() {
        opprett(relaterteRinasakerForespørsler)

        val treff = søk(RelaterteRinasakerSøk(rinasakId = "a")).normalisert()

        assertThat(treff.relaterteRinasaker)
            .containsExactlyInAnyOrderElementsOf(
                forventetRelaterteRinasakerGruppe.normalisert().relaterteRinasaker
            )
    }

    @Test
    fun `POST søk - ukjent rinasakId returnerer tom gruppe - 200 OK`() {
        opprett(relaterteRinasakerForespørsler)

        assertThat(søk(RelaterteRinasakerSøk(rinasakId = "ukjent")))
            .isEqualTo(RelaterteRinasakerGruppe())
    }

    @Test
    fun `PATCH - oppdaterer rinasaker og beholder øvrige felt - 201 Created`() {
        opprett(relaterteRinasakerForespørsler)

        endre(
            RelaterteRinasakerOppdatering(
                relaterteRinasakerId = uuid1,
                rinasakIdList = listOf("a", "b2"),
            )
        )

        assertGruppe(
            actual = søk(),
            expected = forventetRelaterteRinasakerGruppe.oppdater(uuid1) {
                copy(rinasakIdList = listOf("a", "b2"))
            },
        )
    }

    @Test
    fun `PATCH - oppdaterer beskrivelse og beholder øvrige felt - 201 Created`() {
        opprett(relaterteRinasakerForespørsler)

        endre(
            RelaterteRinasakerOppdatering(
                relaterteRinasakerId = uuid1,
                beskrivelse = "Ny beskrivelse",
            )
        )

        assertGruppe(
            actual = søk(),
            expected = forventetRelaterteRinasakerGruppe.oppdater(uuid1) {
                copy(beskrivelse = "Ny beskrivelse")
            },
        )
    }

    @Test
    fun `PATCH - eksplisitte nullverdier beholder gruppen uendret - 201 Created`() {
        opprett(relaterteRinasakerForespørsler)

        endre(RelaterteRinasakerOppdatering(relaterteRinasakerId = uuid1))

        assertGruppe(søk(), forventetRelaterteRinasakerGruppe)
    }

    @Test
    fun `PATCH - utelatte felt beholder gruppen uendret - 201 Created`() {
        opprett(relaterteRinasakerForespørsler)

        patch(
            relaterteRinasakerUrl,
            mapOf("relaterteRinasakerId" to uuid1),
        ).expectStatus().isCreated

        assertGruppe(søk(), forventetRelaterteRinasakerGruppe)
    }

    @Test
    fun `PATCH - tom rinasakIdList fjerner alle knytninger - 201 Created`() {
        opprett(relaterteRinasakerForespørsler)

        endre(
            RelaterteRinasakerOppdatering(
                relaterteRinasakerId = uuid1,
                rinasakIdList = emptyList(),
            )
        )

        assertGruppe(
            actual = søk(),
            expected = forventetRelaterteRinasakerGruppe.oppdater(uuid1) {
                copy(rinasakIdList = emptyList())
            },
        )
    }

    @Test
    fun `PATCH - ukjent gruppe oppretter ikke ny gruppe - 201 Created`() {
        opprett(relaterteRinasakerForespørsler)

        endre(
            RelaterteRinasakerOppdatering(
                relaterteRinasakerId = uuid3,
                beskrivelse = "Finnes ikke",
            )
        )

        assertGruppe(søk(), forventetRelaterteRinasakerGruppe)
    }

    @Test
    fun `POST opprett - tom forespørselsliste avvises - 400 Bad Request`() {
        post(relaterteRinasakerUrl, emptyList<RelaterteRinasakerForespørsel>())
            .expectStatus().isBadRequest
    }

    @Test
    fun `POST opprett - mer enn 500 grupper avvises - 400 Bad Request`() {
        val forespørsler = List(501) {
            relaterteRinasakerForespørsel1.copy(
                relaterteRinasakerId = UUID.nameUUIDFromBytes(it.toString().toByteArray())
            )
        }

        post(relaterteRinasakerUrl, forespørsler)
            .expectStatus().isBadRequest
    }

    @Test
    fun `POST opprett - mer enn 500 rinasaker i en gruppe avvises - 400 Bad Request`() {
        val forespørsel = relaterteRinasakerForespørsel1.copy(
            rinasakIdList = List(501) { it.toString() }
        )

        post(relaterteRinasakerUrl, listOf(forespørsel))
            .expectStatus().isBadRequest
    }

    @Test
    fun `PATCH - mer enn 500 rinasaker avvises - 400 Bad Request`() {
        val oppdatering = RelaterteRinasakerOppdatering(
            relaterteRinasakerId = uuid1,
            rinasakIdList = List(501) { it.toString() },
        )

        patch(relaterteRinasakerUrl, oppdatering)
            .expectStatus().isBadRequest
    }

    @Test
    fun `POST opprett - manglende gruppe-id avvises - 400 Bad Request`() {
        post(
            relaterteRinasakerUrl,
            listOf(mapOf("beskrivelse" to "Mangler id"))
        ).expectStatus().isBadRequest
    }

    @Test
    fun `POST søk - tom rinasakId returnerer valideringsfeil - 400 Bad Request`() {
        post(relaterteRinasakerSøkUrl, RelaterteRinasakerSøk(rinasakId = ""))
            .expectStatus().isBadRequest
            .expectBody()
            .jsonPath("$.timestamp").exists()
            .jsonPath("$.errors[0].field").isEqualTo("rinasakId")
            .jsonPath("$.errors[0].defaultMessage").exists()
            .jsonPath("$.errors[0].rejectedValue").isEqualTo("")
    }

    @Test
    fun `POST søk - rinasakId over 35 tegn avvises - 400 Bad Request`() {
        post(relaterteRinasakerSøkUrl, RelaterteRinasakerSøk(rinasakId = "a".repeat(36)))
            .expectStatus().isBadRequest
    }

    @Test
    fun `POST søk - rinasakId på 35 tegn godtas - 200 OK`() {
        assertThat(søk(RelaterteRinasakerSøk(rinasakId = "a".repeat(35))))
            .isEqualTo(RelaterteRinasakerGruppe())
    }

    @Test
    fun `PATCH - manglende gruppe-id avvises - 400 Bad Request`() {
        patch(
            relaterteRinasakerUrl,
            mapOf("beskrivelse" to "Mangler id"),
        ).expectStatus().isBadRequest
    }

    private fun opprett(forespørsler: List<RelaterteRinasakerForespørsel>) {
        post(relaterteRinasakerUrl, forespørsler)
            .expectStatus().isCreated
    }

    private fun endre(oppdatering: RelaterteRinasakerOppdatering) {
        patch(relaterteRinasakerUrl, oppdatering)
            .expectStatus().isCreated
    }

    private fun søk(
        søk: RelaterteRinasakerSøk = RelaterteRinasakerSøk(),
        url: String = relaterteRinasakerSøkUrl,
    ) = post(url, søk)
        .expectStatus().isOk
        .expectBody(RelaterteRinasakerGruppe::class.java)
        .returnResult()
        .responseBody
        .let(::requireNotNull)

    private fun post(
        url: String,
        body: Any,
    ) = client
        .post()
        .uri(url)
        .header(AUTHORIZATION, "Bearer ${mockOAuth2Server.token}")
        .body(body)
        .exchange()

    private fun patch(
        url: String,
        body: Any,
    ) = client
        .patch()
        .uri(url)
        .header(AUTHORIZATION, "Bearer ${mockOAuth2Server.token}")
        .body(body)
        .exchange()

    private fun RelaterteRinasakerGruppe.oppdater(
        relaterteRinasakerId: UUID,
        transform: RelaterteRinasaker.() -> RelaterteRinasaker,
    ) = copy(
        relaterteRinasaker = relaterteRinasaker.map {
            if (it.relaterteRinasakerId == relaterteRinasakerId) it.transform() else it
        }
    )

    private fun RelaterteRinasakerGruppe.normalisert() = copy(
        relaterteRinasaker = relaterteRinasaker.map {
            it.copy(rinasakIdList = it.rinasakIdList.sorted())
        }
    )

    private fun assertGruppe(
        actual: RelaterteRinasakerGruppe,
        expected: RelaterteRinasakerGruppe,
    ) {
        assertThat(actual.normalisert()).isEqualTo(expected.normalisert())
    }
}
