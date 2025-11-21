package no.nav.eux.relaterte.rinasaker.webapp

import no.nav.eux.relaterte.rinasaker.api.RelaterteRinasakerApi
import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerCreateType
import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerPatchType
import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerSearchCriteriaType
import no.nav.eux.relaterte.rinasaker.service.RelaterteRinasakerService
import org.springframework.web.bind.annotation.RestController

@RestController
class RelaterteRinasakerApiImpl(
    val service: RelaterteRinasakerService
) : RelaterteRinasakerApi {

    override fun relaterteRinasakerSearch(
        relaterteRinasakerSearchCriteriaType: RelaterteRinasakerSearchCriteriaType,
        userId: String?,
    ) = service
        .search(relaterteRinasakerSearchCriteriaType.relaterteRinasakerSearchCriteria)
        .toRelaterteRinasakerType()
        .toRelaterteRinasakerSearchResponseType()
        .toOkResponseEntity()

    override fun relaterteRinasakerSearchSok(
        relaterteRinasakerSearchCriteriaType: RelaterteRinasakerSearchCriteriaType,
        userId: String?,
    ) = service
        .search(relaterteRinasakerSearchCriteriaType.relaterteRinasakerSearchCriteria)
        .toRelaterteRinasakerType()
        .toRelaterteRinasakerSearchResponseType()
        .toOkResponseEntity()

    override fun opprettRelaterteRinasaker(
        relaterteRinasakerCreateType: List<RelaterteRinasakerCreateType>,
        userId: String?
    ) = service
        .create(relaterteRinasakerCreateType.relaterteRinasakerGruppeCreateRequestList)
        .toCreatedEmptyResponseEntity()

    override fun endreRelaterteRinasaker(
        relaterteRinasakerPatchType: RelaterteRinasakerPatchType,
        userId: String?
    ) = service
        .patch(relaterteRinasakerPatchType.relaterteRinasakerGruppePatch)
        .toCreatedEmptyResponseEntity()
}
