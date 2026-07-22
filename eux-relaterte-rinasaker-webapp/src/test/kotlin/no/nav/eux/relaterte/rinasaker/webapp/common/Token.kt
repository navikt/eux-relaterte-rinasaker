package no.nav.eux.relaterte.rinasaker.webapp.common

import com.nimbusds.jose.JOSEObjectType
import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.mock.oauth2.token.DefaultOAuth2TokenCallback

val MockOAuth2Server.token: String
    get() = token(
        audience = listOf("test-client-id"),
        tokenExpiry = 3600,
    )

fun MockOAuth2Server.token(
    audience: List<String>,
    tokenExpiry: Long,
    claims: Map<String, Any> = emptyMap(),
) = issueToken(
    "default",
    "test-client-id",
    DefaultOAuth2TokenCallback(
        "default",
        "subject1",
        JOSEObjectType.JWT.type,
        audience,
        claims,
        tokenExpiry,
    ),
).serialize()
