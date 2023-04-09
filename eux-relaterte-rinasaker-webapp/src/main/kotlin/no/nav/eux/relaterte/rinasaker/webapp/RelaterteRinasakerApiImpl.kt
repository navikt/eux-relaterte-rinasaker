package no.nav.eux.relaterte.rinasaker.webapp

import no.nav.eux.relaterte.rinasaker.api.RelaterterinasakerApi
import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakSearchCriteriaType
import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakType
import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasaker
import no.nav.eux.relaterte.rinasaker.service.RelaterteRinasakerService
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class RelaterteRinasakerApiImpl(
    val service: RelaterteRinasakerService
) : RelaterterinasakerApi {

    override fun relaterteRinasakerSearch(
        relaterteRinasakSearchCriteriaType: RelaterteRinasakSearchCriteriaType,
        userId: String?,
    ): ResponseEntity<RelaterteRinasakType> = ResponseEntity<RelaterteRinasakType>(
        service.search().first().toRelaterteRinasakType(), OK
    )

    override fun relaterteRinasakerCreate(
        relaterteRinasakType: List<RelaterteRinasakType>,
        userId: String?
    ): ResponseEntity<Unit>{
        service.create(relaterteRinasakType.toRelaterteRinasaker())
        return ResponseEntity(HttpStatus.CREATED)
    }
}

private fun List<RelaterteRinasakType>.toRelaterteRinasaker() =
    this.map { it.toRelaterteRinasaker() }

private fun RelaterteRinasakType.toRelaterteRinasaker() =
    RelaterteRinasaker(this.relaterteRinasakerId)

private fun RelaterteRinasaker.toRelaterteRinasakType() =
    RelaterteRinasakType(this.relaterteRinasakerId)
