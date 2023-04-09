package no.nav.eux.relaterte.rinasaker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(value = [org.openapitools.SpringDocConfiguration::class])
@EnableConfigurationProperties(DataSourceProperties::class)
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
