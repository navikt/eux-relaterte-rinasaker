package no.nav.eux.relaterte.rinasaker.webapp

import no.nav.eux.relaterte.rinasaker.Application
import no.nav.eux.relaterte.rinasaker.webapp.common.relaterteRinasakerSokUrl
import no.nav.eux.relaterte.rinasaker.webapp.common.relaterteRinasakerSøkUrl
import no.nav.eux.relaterte.rinasaker.webapp.common.relaterteRinasakerUrl
import no.nav.eux.relaterte.rinasaker.webapp.common.token
import no.nav.eux.relaterte.rinasaker.webapp.dataset.*
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasakerForespørsel
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasakerGruppe
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
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.jdbc.JdbcTestUtils.deleteFromTables
import org.springframework.test.web.servlet.client.RestTestClient

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
    fun `POST relaterterinasaker søk - søk uten kriterier - 200`() {
        val response = client
            .post()
            .uri(relaterteRinasakerSøkUrl)
            .body(RelaterteRinasakerSøk())
            .header("Authorization", "Bearer ${mockOAuth2Server.token}")
            .exchange()
            .expectStatus().isOk
            .expectBody(RelaterteRinasakerGruppe::class.java)
            .returnResult()
        assertThat(response.responseBody).isEqualTo(RelaterteRinasakerGruppe(emptyList()))
    }

    @Test
    fun `POST relaterterinasaker - forespørsel, ny sak, tomt søk - 201`() {
        client
            .post()
            .uri(relaterteRinasakerUrl)
            .body(listOf(RelaterteRinasakerForespørsel()))
            .header("Authorization", "Bearer ${mockOAuth2Server.token}")
            .exchange()
            .expectStatus().isCreated

        val searchResponse = client
            .post()
            .uri(relaterteRinasakerSøkUrl)
            .body(RelaterteRinasakerSøk())
            .header("Authorization", "Bearer ${mockOAuth2Server.token}")
            .exchange()
            .expectStatus().isOk
            .expectBody(RelaterteRinasakerGruppe::class.java)
            .returnResult()
        assertThat(searchResponse.responseBody).isEqualTo(expectedRelaterteRinasakerGruppe)
    }

    @Test
    fun `POST relaterterinasaker - forespørsel, en til mange knytning mellom saker - 201`() {
        client
            .post()
            .uri(relaterteRinasakerUrl)
            .body(relaterteRinasakerForespørsel)
            .header("Authorization", "Bearer ${mockOAuth2Server.token}")
            .exchange()
            .expectStatus().isCreated

        val searchResponse = client
            .post()
            .uri(relaterteRinasakerSøkUrl)
            .body(RelaterteRinasakerSøk())
            .header("Authorization", "Bearer ${mockOAuth2Server.token}")
            .exchange()
            .expectStatus().isOk
            .expectBody(RelaterteRinasakerGruppe::class.java)
            .returnResult()
        assertThat(searchResponse.responseBody).isEqualTo(expectedRelaterteRinasakerGruppeEnTilMange)
    }

    @Test
    fun `POST relaterterinasaker søk - søk på rinasakId - 200`() {
        client
            .post()
            .uri(relaterteRinasakerUrl)
            .body(relaterteRinasakerForespørsel)
            .header("Authorization", "Bearer ${mockOAuth2Server.token}")
            .exchange()
            .expectStatus().isCreated

        val searchResponse = client
            .post()
            .uri(relaterteRinasakerSøkUrl)
            .body(RelaterteRinasakerSøk(rinasakId = "b"))
            .header("Authorization", "Bearer ${mockOAuth2Server.token}")
            .exchange()
            .expectStatus().isOk
            .expectBody(RelaterteRinasakerGruppe::class.java)
            .returnResult()
        assertThat(searchResponse.responseBody).isEqualTo(expectedRelaterteRinasakerGruppeKunB)
    }

    @Test
    fun `POST relaterterinasaker sok - søk på rinasakId - 200`() {
        client
            .post()
            .uri(relaterteRinasakerUrl)
            .body(relaterteRinasakerForespørsel)
            .header("Authorization", "Bearer ${mockOAuth2Server.token}")
            .exchange()
            .expectStatus().isCreated

        val searchResponse = client
            .post()
            .uri(relaterteRinasakerSokUrl)
            .body(RelaterteRinasakerSøk(rinasakId = "b"))
            .header("Authorization", "Bearer ${mockOAuth2Server.token}")
            .exchange()
            .expectStatus().isOk
            .expectBody(RelaterteRinasakerGruppe::class.java)
            .returnResult()
        assertThat(searchResponse.responseBody).isEqualTo(expectedRelaterteRinasakerGruppeKunB)
    }

    @Test
    fun `PATCH relaterterinasaker - oppdater knytning - 200`() {
        client
            .post()
            .uri(relaterteRinasakerUrl)
            .body(relaterteRinasakerForespørsel)
            .header("Authorization", "Bearer ${mockOAuth2Server.token}")
            .exchange()
            .expectStatus().isCreated

        client
            .patch()
            .uri(relaterteRinasakerUrl)
            .body(relaterteRinasakerOppdatering)
            .header("Authorization", "Bearer ${mockOAuth2Server.token}")
            .exchange()
            .expectStatus().isCreated

        val searchResponse = client
            .post()
            .uri(relaterteRinasakerSøkUrl)
            .body(RelaterteRinasakerSøk())
            .header("Authorization", "Bearer ${mockOAuth2Server.token}")
            .exchange()
            .expectStatus().isOk
            .expectBody(RelaterteRinasakerGruppe::class.java)
            .returnResult()
        assertThat(searchResponse.responseBody).isEqualTo(expectedRelaterteRinasakerGruppeEnTilMangeOppdatert)
    }

    @Test
    fun `PATCH relaterterinasaker - oppdater beskrivelse - 200`() {
        client
            .post()
            .uri(relaterteRinasakerUrl)
            .body(relaterteRinasakerForespørsel)
            .header("Authorization", "Bearer ${mockOAuth2Server.token}")
            .exchange()
            .expectStatus().isCreated

        client
            .patch()
            .uri(relaterteRinasakerUrl)
            .body(relaterteRinasakerOppdateringNyBeskrivelse)
            .header("Authorization", "Bearer ${mockOAuth2Server.token}")
            .exchange()
            .expectStatus().isCreated

        val searchResponse = client
            .post()
            .uri(relaterteRinasakerSøkUrl)
            .body(RelaterteRinasakerSøk())
            .header("Authorization", "Bearer ${mockOAuth2Server.token}")
            .exchange()
            .expectStatus().isOk
            .expectBody(RelaterteRinasakerGruppe::class.java)
            .returnResult()
        assertThat(searchResponse.responseBody)
            .isEqualTo(expectedRelaterteRinasakerGruppeEnTilMangeOppdatertNyBeskrivelse)
    }

}
