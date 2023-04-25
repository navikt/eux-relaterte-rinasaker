package no.nav.eux.relaterte.rinasaker.webapp

import no.nav.eux.relaterte.rinasaker.Application
import no.nav.eux.relaterte.rinasaker.webapp.common.httpEntity
import no.nav.eux.relaterte.rinasaker.webapp.common.token
import no.nav.eux.relaterte.rinasaker.webapp.common.uuid1
import no.nav.eux.relaterte.rinasaker.webapp.common.uuid2
import no.nav.eux.relaterte.rinasaker.webapp.dataset.expectedRelaterteRinasakerGruppe
import no.nav.eux.relaterte.rinasaker.webapp.dataset.expectedRelaterteRinasakerGruppeEnTilMange
import no.nav.eux.relaterte.rinasaker.webapp.dataset.expectedRelaterteRinasakerGruppeEnTilMangeOppdatert
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasakerForespørsel
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasakerGruppe
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasakerOppdatering
import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.token.support.spring.test.EnableMockOAuth2Server
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.patchForObject
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.boot.test.web.client.postForObject
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.jdbc.JdbcTestUtils.*
import java.util.*

@SpringBootTest(
    classes = [Application::class],
    webEnvironment = RANDOM_PORT
)
@ActiveProfiles("test")
@EnableMockOAuth2Server
class RelaterteRinasakerApiImplTest {

    @Autowired
    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    lateinit var mockOAuth2Server: MockOAuth2Server

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun initialiseRestAssuredMockMvcWebApplicationContext() {
        deleteFromTables(
            jdbcTemplate,
            "relaterte_rinasaker",
            "relaterte_rinasaker_gruppe"
        )
    }

    fun <T> T.httpEntity() = httpEntity(mockOAuth2Server)

    @Test
    fun `POST relaterterinasaker søk - søk uten kriterier - 200`() {
        val headers = HttpHeaders()
        headers.contentType = APPLICATION_JSON
        headers.set("Authorization", "Bearer ${mockOAuth2Server.token}")
        val entity: HttpEntity<String> = HttpEntity<String>("{}", headers)
        val response: RelaterteRinasakerGruppe? = restTemplate
            .postForObject(url = "/api/v1/relaterterinasaker/søk", request = entity)
        assertThat(response).isEqualTo(RelaterteRinasakerGruppe(emptyList()))
    }

    @Test
    fun `POST relaterterinasaker - forespørsel, ny sak, tomt søk - 201`() {
        val createResponse = restTemplate.postForEntity<Void>(
            "/api/v1/relaterterinasaker",
            listOf(RelaterteRinasakerForespørsel()).httpEntity()
        )
        assertThat(createResponse.statusCode.value()).isEqualTo(201)
        val searchResponse = restTemplate.postForObject(
            "/api/v1/relaterterinasaker/søk",
            "{}".httpEntity(),
            RelaterteRinasakerGruppe::class.java
        )
        assertThat(searchResponse).isEqualTo(expectedRelaterteRinasakerGruppe)
    }

    @Test
    fun `POST relaterterinasaker, forespørsel, en til mange knytning mellom saker - 201`() {
        val createResponse = restTemplate.postForEntity<Void>(
            "/api/v1/relaterterinasaker",
            listOf(
                RelaterteRinasakerForespørsel(relaterteRinasakerId = uuid1, rinasakIdList = listOf("a", "b")),
                RelaterteRinasakerForespørsel(relaterteRinasakerId = uuid2, rinasakIdList = listOf("a", "c")),
            )
                .httpEntity()
        )
        assertThat(createResponse.statusCode.value()).isEqualTo(201)
        val searchResponse = restTemplate.postForObject(
            "/api/v1/relaterterinasaker/søk",
            "{}".httpEntity(),
            RelaterteRinasakerGruppe::class.java
        )
        assertThat(searchResponse).isEqualTo(expectedRelaterteRinasakerGruppeEnTilMange)
    }

    @Test
    fun `PATCH relaterterinasaker - oppdater knytning - 200`() {
        restTemplate.postForEntity<Void>(
            "/api/v1/relaterterinasaker",
            listOf(
                RelaterteRinasakerForespørsel(relaterteRinasakerId = uuid1, rinasakIdList = listOf("a", "b")),
                RelaterteRinasakerForespørsel(relaterteRinasakerId = uuid2, rinasakIdList = listOf("a", "c")),
            )
                .httpEntity()
        )
        restTemplate.patchForObject<Void>(
            "/api/v1/relaterterinasaker",
            RelaterteRinasakerOppdatering(
                relaterteRinasakerId = uuid1,
                rinasakIdList = listOf("a", "b2"),
            )
                .httpEntity()
        )
        val søkResponse = restTemplate.postForObject(
            "/api/v1/relaterterinasaker/søk",
            "{}".httpEntity(),
            RelaterteRinasakerGruppe::class.java
        )
        assertThat(søkResponse).isEqualTo(expectedRelaterteRinasakerGruppeEnTilMangeOppdatert)
    }
}
