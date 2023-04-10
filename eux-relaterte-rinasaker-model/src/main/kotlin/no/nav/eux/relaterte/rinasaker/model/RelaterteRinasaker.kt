package no.nav.eux.relaterte.rinasaker.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.*

@Entity
class RelaterteRinasaker(
    @Id
    val relaterteRinasakerId: UUID,
    val rinasakIdA: String,
    val rinasakIdB: String,
    val beskrivelse: String?,
    val opprettetDato: LocalDateTime,
)
