package no.nav.eux.relaterte.rinasaker

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "datasource")
data class DataSourceProperties(
    val hostname: String,
)