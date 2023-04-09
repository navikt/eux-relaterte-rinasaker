package no.nav.eux.relaterte.rinasaker.service

import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasaker
import no.nav.eux.relaterte.rinasaker.persistence.repository.RelaterteRinasakerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RelaterteRinasakerService(
    val relaterteRinasakerRepository: RelaterteRinasakerRepository
) {

    @Transactional
    fun create(relaterteRinasaker: List<RelaterteRinasaker>) {
        relaterteRinasaker.forEach {
            relaterteRinasakerRepository.save(it)
        }
    }

    fun search(): List<RelaterteRinasaker> =
        relaterteRinasakerRepository.findAll()
}