package no.nav.eux.relaterte.rinasaker

import com.zaxxer.hikari.HikariConfig
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "datasource")
data class DataSourceProperties(
    var hikari: HikariConfig
)
