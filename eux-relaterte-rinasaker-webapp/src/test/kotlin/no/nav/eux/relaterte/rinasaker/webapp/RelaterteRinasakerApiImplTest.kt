package no.nav.eux.relaterte.rinasaker.webapp

import com.nimbusds.jose.JOSEObjectType.JWT
import no.nav.eux.relaterte.rinasaker.Application
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasaker
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasakerForespørsel
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasakerGruppe
import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.mock.oauth2.token.DefaultOAuth2TokenCallback
import no.nav.security.token.support.spring.test.EnableMockOAuth2Server
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.jdbc.JdbcTestUtils.*
import java.time.OffsetDateTime
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

    @Test
    fun `POST relaterterinasaker søk - søk uten kriterier - 200`() {
        val headers = HttpHeaders()
        headers.contentType = APPLICATION_JSON
        headers.set("Authorization", "Bearer ${token()}")
        val entity: HttpEntity<String> = HttpEntity<String>("{}", headers)
        val response = restTemplate
            .postForObject("/api/v1/relaterterinasaker/søk", entity, RelaterteRinasakerGruppe::class.java)
        assertThat(response)
            .isEqualTo(RelaterteRinasakerGruppe(emptyList()))
    }

    @Test
    fun `POST relaterterinasaker forespørsel - ny sak, tomt søk - 200`() {
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
        assertThat(searchResponse)
            .isEqualTo(
                RelaterteRinasakerGruppe(
                    listOf(
                        RelaterteRinasaker(
                            relaterteRinasakerId = UUID.fromString("cdb8499f-8a6b-476a-91c8-4403f792cab0"),
                            beskrivelse = "En forespørsel",
                            OffsetDateTime.parse("2023-04-21T10:40:40.897718Z")
                        )
                    )
                )
            )
    }

    fun <T> T.httpEntity(): HttpEntity<T> {
        val headers = HttpHeaders()
        headers.contentType = APPLICATION_JSON
        headers.set("Authorization", "Bearer ${token()}")
        return HttpEntity<T>(this, headers)
    }

    fun token(): String =
        mockOAuth2Server
            .issueToken("issuer1", "theclientid", defaultOAuth2TokenCallback)
            .serialize()

    var defaultOAuth2TokenCallback =
        DefaultOAuth2TokenCallback("issuer1", "subject1", JWT.type, listOf("demoapplication"), emptyMap(), 3600)

}
