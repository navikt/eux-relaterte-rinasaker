package no.nav.eux.relaterte.rinasaker

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class TestConfig(
    val dataSourceProperties: DataSourceProperties
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostConstruct
    fun test() {
        logger.info("Hostname (scroll7): {}", dataSourceProperties.hostname)
    }
}