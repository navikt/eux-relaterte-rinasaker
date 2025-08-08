package no.nav.eux.relaterte.rinasaker.webapp

import io.restassured.http.ContentType.JSON
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import io.restassured.module.mockmvc.RestAssuredMockMvc.webAppContextSetup
import jakarta.servlet.Filter
import no.nav.eux.relaterte.rinasaker.Application
import no.nav.eux.relaterte.rinasaker.webapp.common.relaterteRinasakerSøkUrl
import no.nav.eux.relaterte.rinasaker.webapp.common.token
import no.nav.eux.relaterte.rinasaker.webapp.model.RelaterteRinasakerSøk
import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.token.support.spring.test.EnableMockOAuth2Server
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.OK
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
class RelaterteRinasakerApiAuthImplTest {

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
    fun `POST relaterterinasaker søk - no token in request - 403`() {
        given()
            .contentType(JSON)
            .body(RelaterteRinasakerSøk())
            .`when`()
            .post(URI(relaterteRinasakerSøkUrl))
            .then()
            .log()
            .ifValidationFails()
            .statusCode(FORBIDDEN.value())
    }

    @Test
    fun `POST relaterterinasaker søk - søk uten kriterier - 200`() {
        given()
            .header("Authorization", "Bearer ${server.token}")
            .contentType(JSON)
            .body(RelaterteRinasakerSøk())
            .`when`()
            .post(URI(relaterteRinasakerSøkUrl))
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
}
