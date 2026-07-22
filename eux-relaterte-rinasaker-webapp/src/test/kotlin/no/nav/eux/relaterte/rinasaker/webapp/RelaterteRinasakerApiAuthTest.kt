package no.nav.eux.relaterte.rinasaker.webapp

import no.nav.eux.relaterte.rinasaker.Application
import no.nav.eux.relaterte.rinasaker.webapp.common.relaterteRinasakerSøkUrl
import no.nav.eux.relaterte.rinasaker.webapp.common.token
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasakerSøk
import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.token.support.spring.test.EnableMockOAuth2Server
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.client.RestTestClient
import java.time.Instant
import java.util.Date

@SpringBootTest(
    classes = [Application::class],
    webEnvironment = RANDOM_PORT
)
@ActiveProfiles("test")
@EnableMockOAuth2Server
@AutoConfigureRestTestClient
class RelaterteRinasakerApiAuthTest {

    @Autowired
    lateinit var client: RestTestClient

    @Autowired
    lateinit var mockOAuth2Server: MockOAuth2Server

    @Test
    fun `POST søk - uten token avvises - 401 Unauthorized`() {
        client
            .post()
            .uri(relaterteRinasakerSøkUrl)
            .body(RelaterteRinasakerSøk())
            .exchange()
            .expectStatus().isUnauthorized
    }

    @Test
    fun `POST søk - ugyldig token avvises - 401 Unauthorized`() {
        søkMedToken("ugyldig-token")
            .expectStatus().isUnauthorized
    }

    @Test
    fun `POST søk - token med feil audience avvises - 401 Unauthorized`() {
        val token = mockOAuth2Server.token(
            audience = listOf("feil-client-id"),
            tokenExpiry = 3600,
        )

        søkMedToken(token)
            .expectStatus().isUnauthorized
    }

    @Test
    fun `POST søk - utløpt token avvises - 401 Unauthorized`() {
        val nå = Instant.now()
        val token = mockOAuth2Server.token(
            audience = listOf("test-client-id"),
            tokenExpiry = 3600,
            claims = mapOf(
                "iat" to Date.from(nå.minusSeconds(180)),
                "nbf" to Date.from(nå.minusSeconds(180)),
                "exp" to Date.from(nå.minusSeconds(120)),
            ),
        )

        søkMedToken(token)
            .expectStatus().isUnauthorized
    }

    @Test
    fun `GET health - uten token er tillatt - 200 OK`() {
        client
            .get()
            .uri("/actuator/health")
            .exchange()
            .expectStatus().isOk
    }

    private fun søkMedToken(token: String) = client
        .post()
        .uri(relaterteRinasakerSøkUrl)
        .header(AUTHORIZATION, "Bearer $token")
        .body(RelaterteRinasakerSøk())
        .exchange()
}
