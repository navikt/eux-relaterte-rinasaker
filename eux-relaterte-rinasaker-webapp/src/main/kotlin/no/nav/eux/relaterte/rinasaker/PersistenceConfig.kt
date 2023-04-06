package no.nav.eux.relaterte.rinasaker

import com.zaxxer.hikari.HikariDataSource
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PersistenceConfig(
    val dataSourceProperties: DataSourceProperties
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun hikariDataSource() = HikariDataSource(dataSourceProperties.hikari)

    @PostConstruct
    fun test() {
        logger.info("JDBC URL (test22): ${dataSourceProperties.hikari.jdbcUrl}")
        logger.info("Username (test22): ${dataSourceProperties.hikari.username}")
    }
}
