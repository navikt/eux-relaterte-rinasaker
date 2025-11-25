package no.nav.eux.relaterte.rinasaker.webapp

import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity

fun <T> T.toOkResponseEntity() = ResponseEntity(this, OK)

fun <T> T.toCreatedEmptyResponseEntity() = ResponseEntity(this, CREATED)
