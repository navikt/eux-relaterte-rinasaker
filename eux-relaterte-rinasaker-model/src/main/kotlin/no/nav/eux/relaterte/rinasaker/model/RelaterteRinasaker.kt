package no.nav.eux.relaterte.rinasaker.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@Entity
class RelaterteRinasaker(
    @Id
    val relaterteRinasakerId: UUID
)
