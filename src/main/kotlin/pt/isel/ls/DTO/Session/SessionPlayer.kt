package pt.isel.ls.DTO.Session

import kotlinx.serialization.Serializable


/**
 * The [SessionPlayer] class is used to represent the request body of a session player addition
 */
@Serializable
data class SessionPlayer(
    val pid: Int
) {
    init {
        require(pid >= 0) { "Player identifier must be a positive number" }
    }
}
