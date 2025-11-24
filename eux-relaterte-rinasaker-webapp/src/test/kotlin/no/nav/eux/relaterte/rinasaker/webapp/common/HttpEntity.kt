package no.nav.eux.relaterte.rinasaker.webapp.common

import com.nimbusds.jose.JOSEObjectType
import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.mock.oauth2.token.DefaultOAuth2TokenCallback
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

fun <T: Any> T.httpEntity(mockOAuth2Server: MockOAuth2Server): HttpEntity<T> {
    val headers = HttpHeaders()
    headers.contentType = MediaType.APPLICATION_JSON
    headers.set("Authorization", "Bearer ${mockOAuth2Server.token}")
    return HttpEntity<T>(this, headers)
}

val MockOAuth2Server.token: String
    get() = this
        .issueToken("default", "test-client-id", defaultOAuth2TokenCallback)
        .serialize()

var defaultOAuth2TokenCallback =
    DefaultOAuth2TokenCallback(
        "default",
        "subject1",
        JOSEObjectType.JWT.type,
        listOf("test-client-id"),
        emptyMap(),
        3600
    )
