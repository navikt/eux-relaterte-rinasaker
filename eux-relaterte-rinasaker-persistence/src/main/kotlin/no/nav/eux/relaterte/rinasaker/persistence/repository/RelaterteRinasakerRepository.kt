package no.nav.eux.relaterte.rinasaker.persistence.repository

import no.nav.eux.relaterte.rinasaker.model.RelaterteRinasaker
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RelaterteRinasakerRepository : JpaRepository<RelaterteRinasaker, UUID>