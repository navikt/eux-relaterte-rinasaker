package no.nav.eux.relaterte.rinasaker.webapp

import no.nav.eux.relaterte.rinasaker.Application
import no.nav.security.token.support.spring.test.EnableMockOAuth2Server
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.jdbc.JdbcTestUtils.*

@SpringBootTest(
    classes = [Application::class],
    webEnvironment = RANDOM_PORT
)
@ActiveProfiles("test")
@EnableMockOAuth2Server
class RelaterteRinasakerApiImplTest {

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
    fun `POST relaterterinasaker søk - søk uten kriterier 2 - 200`() {

    }

}
