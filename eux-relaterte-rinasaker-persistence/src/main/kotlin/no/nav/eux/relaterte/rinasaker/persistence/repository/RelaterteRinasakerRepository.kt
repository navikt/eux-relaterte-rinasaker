@file:Suppress("SpringDataRepositoryMethodParametersInspection")

package no.nav.eux.relaterte.rinasaker.persistence.repository

import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasakerGruppe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RelaterteRinasakerRepository : JpaRepository<RelaterteRinasakerGruppe, UUID> {

    fun findAllByRinasakIdListIn(rinasakIdList: List<String>): List<RelaterteRinasakerGruppe>
}
