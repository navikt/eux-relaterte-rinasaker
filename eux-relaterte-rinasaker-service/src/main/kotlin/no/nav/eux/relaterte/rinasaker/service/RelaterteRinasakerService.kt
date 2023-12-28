package no.nav.eux.relaterte.rinasaker.service

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerGruppe
import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerGruppeCreateRequest
import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerGruppePatch
import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerSearchCriteria
import no.nav.eux.relaterte.rinasaker.persistence.repository.RelaterteRinasakerRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RelaterteRinasakerService(
    val repository: RelaterteRinasakerRepository
) {

    val log = logger {}

    @Transactional
    fun create(
        relaterteRinasaker: List<RelaterteRinasakerGruppeCreateRequest>
    ) {
        relaterteRinasaker.forEach {
            repository.save(it.toRelaterteRinasaker())
        }
    }

    @Transactional
    fun patch(
        patch: RelaterteRinasakerGruppePatch
    ) {
        repository
            .findByIdOrNull(patch.relaterteRinasakerGruppeId)
            ?.let { repository.save(it.patch(patch)) }
    }

    fun search(
        searchCriteria: RelaterteRinasakerSearchCriteria
    ): List<RelaterteRinasakerGruppe> =
        searchCriteria
            .rinasakId
            ?.also { log.info { "søk på ${searchCriteria.rinasakId}" } }
            ?.let { repository.findAllByRinasakIdListIn(listOf(it)) }
            ?: repository.findAll().sortedBy { it.relaterteRinasakerGruppeId }
}
