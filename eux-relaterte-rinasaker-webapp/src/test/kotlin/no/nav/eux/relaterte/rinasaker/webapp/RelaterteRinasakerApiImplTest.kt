package no.nav.eux.relaterte.rinasaker.webapp

import com.nimbusds.jose.JOSEObjectType.JWT
import io.restassured.http.ContentType.JSON
import io.restassured.module.mockmvc.RestAssuredMockMvc
import io.restassured.module.mockmvc.RestAssuredMockMvc.webAppContextSetup
import jakarta.servlet.Filter
import no.nav.eux.relaterte.rinasaker.Application
import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.mock.oauth2.token.DefaultOAuth2TokenCallback
import no.nav.security.token.support.spring.test.EnableMockOAuth2Server
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcConfigurer
import org.springframework.web.context.WebApplicationContext
import java.net.URI

@SpringBootTest(
    classes = [Application::class],
    webEnvironment = RANDOM_PORT
)
@ActiveProfiles("test")
@EnableMockOAuth2Server
class RelaterteRinasakerApiImplTest {

    @Autowired
    lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    lateinit var server: MockOAuth2Server

    @BeforeEach
    fun initialiseRestAssuredMockMvcWebApplicationContext() {
        webAppContextSetup(webApplicationContext, mockMvcConfigurer())
    }

    @Test
    fun `POST relaterterinasaker søk - no token in request - 401`() {
        RestAssuredMockMvc.given()
            .contentType(JSON)
            .body("{}")
            .`when`()
            .post(URI("/api/v1/relaterterinasaker/søk"))
            .then()
            .log()
            .ifValidationFails()
            .statusCode(UNAUTHORIZED.value())
    }

    @Test
    fun `POST relaterterinasaker søk - søk uten kriterier - 200`() {
        val token1 = token()
        val uri = "/api/v1/relaterterinasaker/søk"
        RestAssuredMockMvc.given()
            .header("Authorization", "Bearer $token1")
            .contentType(JSON)
            .body("{}")
            .`when`()
            .post(URI(uri))
            .then()
            .log()
            .ifValidationFails()
            .statusCode(OK.value())
    }

    fun mockMvcConfigurer() = object : MockMvcConfigurer {
        override fun afterConfigurerAdded(builder: ConfigurableMockMvcBuilder<*>) {
            builder.addFilters(
                *webApplicationContext
                    .getBeansOfType(Filter::class.java)
                    .values
                    .toTypedArray()
            )
        }
    }

    fun token(): String =
        server
            .issueToken("issuer1", "theclientid", defaultOAuth2TokenCallback)
            .serialize()

    var defaultOAuth2TokenCallback =
        DefaultOAuth2TokenCallback("issuer1", "subject1", JWT.type, listOf("demoapplication"), emptyMap(), 3600)
}
