package no.nav.eux.relaterte.rinasaker.webapp

import no.nav.eux.relaterte.rinasaker.api.RelaterterinasakerApi
import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerCreateType
import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerSearchCriteriaType
import no.nav.eux.relaterte.rinasaker.service.RelaterteRinasakerService
import org.springframework.web.bind.annotation.RestController

@RestController
class RelaterteRinasakerApiImpl(
    val service: RelaterteRinasakerService
) : RelaterterinasakerApi {

    override fun relaterteRinasakerSearch(
        relaterteRinasakerSearchCriteriaType: RelaterteRinasakerSearchCriteriaType,
        userId: String?,
    ) = service
        .search()
        .toRelaterteRinasakerType()
        .toRelaterteRinasakerSearchResponseType()
        .toOkResponseEntity()

    override fun relaterteRinasakerCreate(
        relaterteRinasakerCreateType: List<RelaterteRinasakerCreateType>,
        userId: String?
    ) = service
        .create(relaterteRinasakerCreateType.toRelaterteRinasakerGruppeCreateRequestList())
        .toCreatedEmptyResponseEntity()
}
