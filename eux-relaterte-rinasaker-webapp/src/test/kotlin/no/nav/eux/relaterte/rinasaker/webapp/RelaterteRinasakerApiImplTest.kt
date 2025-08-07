package no.nav.eux.relaterte.rinasaker.webapp

import no.nav.eux.relaterte.rinasaker.Application
import no.nav.eux.relaterte.rinasaker.webapp.common.*
import no.nav.eux.relaterte.rinasaker.webapp.dataset.*
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasakerForespørsel
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasakerGruppe
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasakerSøk
import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.token.support.spring.test.EnableMockOAuth2Server
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
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
import org.springframework.test.jdbc.JdbcTestUtils.deleteFromTables

@Disabled
@SpringBootTest(
    classes = [Application::class],
    webEnvironment = RANDOM_PORT
)
@ActiveProfiles("test")
@EnableMockOAuth2Server
class RelaterteRinasakerApiImplTest {

    @Autowired
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

    val <T> T.httpEntity: HttpEntity<T>
        get() = httpEntity(mockOAuth2Server)

    @Test
    fun `POST relaterterinasaker søk - søk uten kriterier - 200`() {
        val headers = HttpHeaders()
        headers.contentType = APPLICATION_JSON
        headers.set("Authorization", "Bearer ${mockOAuth2Server.token}")
        val entity: HttpEntity<String> = HttpEntity<String>("{}", headers)
        val response: RelaterteRinasakerGruppe? = restTemplate
            .postForObject(url = relaterteRinasakerSøkUrl, request = entity)
        assertThat(response).isEqualTo(RelaterteRinasakerGruppe(emptyList()))
    }

    @Test
    fun `POST relaterterinasaker - forespørsel, ny sak, tomt søk - 201`() {
        val createResponse = restTemplate.postForEntity<Void>(
            relaterteRinasakerUrl,
            listOf(RelaterteRinasakerForespørsel()).httpEntity
        )
        assertThat(createResponse.statusCode.value()).isEqualTo(201)
        val searchResponse: RelaterteRinasakerGruppe? = restTemplate.postForObject(
            url = relaterteRinasakerSøkUrl,
            request = RelaterteRinasakerSøk().httpEntity
        )
        assertThat(searchResponse).isEqualTo(expectedRelaterteRinasakerGruppe)
    }

    @Test
    fun `POST relaterterinasaker - forespørsel, en til mange knytning mellom saker - 201`() {
        val createResponse = restTemplate.postForEntity<Void>(
            url = relaterteRinasakerUrl,
            request = relaterteRinasakerForespørsel.httpEntity
        )
        assertThat(createResponse.statusCode.value()).isEqualTo(201)
        val searchResponse: RelaterteRinasakerGruppe? = restTemplate.postForObject(
            url = relaterteRinasakerSøkUrl,
            request = RelaterteRinasakerSøk().httpEntity
        )
        assertThat(searchResponse).isEqualTo(expectedRelaterteRinasakerGruppeEnTilMange)
    }

    @Test
    fun `POST relaterterinasaker søk - søk på rinasakId - 200`() {
        restTemplate.postForEntity<Void>(
            url = relaterteRinasakerUrl,
            request = relaterteRinasakerForespørsel.httpEntity
        )
        val searchResponse = restTemplate.postForEntity<RelaterteRinasakerGruppe>(
            url = relaterteRinasakerSøkUrl,
            request = RelaterteRinasakerSøk(rinasakId = "b").httpEntity
        )
        assertThat(searchResponse.statusCode.value()).isEqualTo(200)
        assertThat(searchResponse.body).isEqualTo(expectedRelaterteRinasakerGruppeKunB)
    }

    @Test
    fun `POST relaterterinasaker sok - søk på rinasakId - 200`() {
        restTemplate.postForEntity<Void>(
            url = relaterteRinasakerUrl,
            request = relaterteRinasakerForespørsel.httpEntity
        )
        val searchResponse = restTemplate.postForEntity<RelaterteRinasakerGruppe>(
            url = relaterteRinasakerSokUrl,
            request = RelaterteRinasakerSøk(rinasakId = "b").httpEntity
        )
        assertThat(searchResponse.statusCode.value()).isEqualTo(200)
        assertThat(searchResponse.body).isEqualTo(expectedRelaterteRinasakerGruppeKunB)
    }

    @Test
    fun `PATCH relaterterinasaker - oppdater knytning - 200`() {
        restTemplate.postForEntity<Void>(
            url = relaterteRinasakerUrl,
            request = relaterteRinasakerForespørsel.httpEntity
        )
        restTemplate.patchForObject<Unit>(
            url = relaterteRinasakerUrl,
            request = relaterteRinasakerOppdatering.httpEntity
        )
        val søkResponse: RelaterteRinasakerGruppe? = restTemplate.postForObject(
            relaterteRinasakerSøkUrl,
            RelaterteRinasakerSøk().httpEntity
        )
        assertThat(søkResponse).isEqualTo(expectedRelaterteRinasakerGruppeEnTilMangeOppdatert)
    }

    @Test
    fun `PATCH relaterterinasaker - oppdater beskrivelse - 200`() {
        restTemplate.postForEntity<Void>(
            url = relaterteRinasakerUrl,
            request = relaterteRinasakerForespørsel.httpEntity
        )
        restTemplate.patchForObject<Unit>(
            url = relaterteRinasakerUrl,
            request = relaterteRinasakerOppdateringNyBeskrivelse.httpEntity
        )
        val søkResponse: RelaterteRinasakerGruppe? = restTemplate.postForObject(
            relaterteRinasakerSøkUrl,
            RelaterteRinasakerSøk().httpEntity
        )
        assertThat(søkResponse).isEqualTo(expectedRelaterteRinasakerGruppeEnTilMangeOppdatertNyBeskrivelse)
    }

}
