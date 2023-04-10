package no.nav.eux.relaterte.rinasaker.service

import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasaker
import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerCreateRequest
import no.nav.eux.relaterte.rinasaker.persistence.repository.RelaterteRinasakerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RelaterteRinasakerService(
    val repository: RelaterteRinasakerRepository
) {

    @Transactional
    fun create(
        relaterteRinasaker: List<RelaterteRinasakerCreateRequest>
    ) {
        relaterteRinasaker.forEach {
            repository.save(it.toRelaterteRinasaker())
        }
    }

    fun search(): List<RelaterteRinasaker> =
        repository.findAll()
}
