package no.nav.eux.relaterte.rinasaker.model

import jakarta.persistence.*
import jakarta.persistence.FetchType.EAGER
import java.time.LocalDateTime
import java.util.*

@Entity
class RelaterteRinasakerGruppe(
    @Id
    val relaterteRinasakerGruppeId: UUID,
    @ElementCollection(targetClass = String::class, fetch = EAGER)
    @CollectionTable(
        name = "relaterte_rinasaker",
        joinColumns = [JoinColumn(name = "relaterte_rinasaker_gruppe_id")]
    )
    @Column(name = "rinasak_id")
    val rinasakIdList: List<String>,
    val beskrivelse: String?,
    val opprettetDato: LocalDateTime,
)
