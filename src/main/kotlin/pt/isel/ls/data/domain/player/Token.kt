package pt.isel.ls.data.domain.player

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import pt.isel.ls.utils.currentLocalTime
import pt.isel.ls.utils.plus
import java.util.*
import kotlin.time.Duration.Companion.days


/**
 *  Token
 *
 *  The [Token] Class is the domain of a Token in the system with its attributes:
 *
 * @param token The token's value (unique [UUID] object)
 * @param playerId The user's id (unique [UInt] number)
 * @param timeCreation The token's creation date ([Instant] object)
 * @param timeExpiration The token's expiration date ([Instant] object)
 */

data class Token(
    val token: UUID,
    val playerId: UInt,
    val timeCreation: LocalDateTime = currentLocalTime(), // This property may be redundant
    val timeExpiration: LocalDateTime = timeCreation + 1.days // Can be changed for any expiration time
) {
    fun isExpired(): Boolean = timeExpiration < currentLocalTime()
}