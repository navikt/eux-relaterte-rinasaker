package no.nav.eux.relaterte.rinasaker.service

import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerGruppe
import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerGruppeCreateRequest
import no.nav.eux.relaterte.rinasaker.persistence.repository.RelaterteRinasakerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RelaterteRinasakerService(
    val repository: RelaterteRinasakerRepository
) {

    @Transactional
    fun create(
        relaterteRinasaker: List<RelaterteRinasakerGruppeCreateRequest>
    ) {
        relaterteRinasaker.forEach {
            repository.save(it.toRelaterteRinasaker())
        }
    }

    fun search(): List<RelaterteRinasakerGruppe> =
        repository.findAll()
}
