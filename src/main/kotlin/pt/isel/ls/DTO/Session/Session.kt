package pt.isel.ls.DTO.Session

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *  Session
 *
 *  The [Session] Data Class is the representation of a Game in the system.
 *
 *  @param ssid The session identifier
 *  @param capacity The session capacity
 *  @param gid The game identifier
 *  @param date The session date
 */
@Serializable
data class Session(
    @SerialName("ssid") val ssid: Int,
    @SerialName("capacity") val capacity: Int,
    @SerialName("gid")  val gid: Int,
    @SerialName("date") val date: String,
)
