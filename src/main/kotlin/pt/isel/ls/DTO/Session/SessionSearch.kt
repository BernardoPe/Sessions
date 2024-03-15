package pt.isel.ls.DTO.Session

import kotlinx.serialization.Serializable
import pt.isel.ls.utils.isValidTimeStamp

/**
 * The [SessionSearch] class is used to represent the request body of a session list search
 */
@Serializable
data class SessionSearch(
    val gid: Int,
    val date: String? = null,
    val state : String? = null,
    val pid : Int? = null
) {
    init {
        require(gid > 0) { "Game identifier must be a positive number" }
        require(date?.isNotBlank() ?: true && date?.isValidTimeStamp() ?: true) { "Session date must be a valid date" }
        require((state?.isNotBlank() ?: true) || state?.equals("open") ?: true || state?.equals("close") ?: true ) { "Session state must be a valid state" }
    }
}