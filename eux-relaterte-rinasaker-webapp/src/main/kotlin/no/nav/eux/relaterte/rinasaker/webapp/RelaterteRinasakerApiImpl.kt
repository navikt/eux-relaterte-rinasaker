package no.nav.eux.relaterte.rinasaker.webapp

import no.nav.eux.relaterte.rinasaker.api.RelaterterinasakerApi
import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerCreateType
import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerSearchCriteriaType
import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerType
import no.nav.eux.relaterte.rinasaker.service.RelaterteRinasakerService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class RelaterteRinasakerApiImpl(
    val service: RelaterteRinasakerService
) : RelaterterinasakerApi {

    override fun relaterteRinasakerSearch(
        relaterteRinasakerSearchCriteriaType: RelaterteRinasakerSearchCriteriaType,
        userId: String?,
    ): ResponseEntity<RelaterteRinasakerType> = ResponseEntity<RelaterteRinasakerType>(
        service.search().first().toRelaterteRinasakType(), OK
    )

    override fun relaterteRinasakerCreate(
        relaterteRinasakerCreateType: List<RelaterteRinasakerCreateType>,
        userId: String?
    ): ResponseEntity<Unit> {
        service.create(relaterteRinasakerCreateType.toRelaterteRinasakerCreateTypeList())
        return ResponseEntity(CREATED)
    }
}
