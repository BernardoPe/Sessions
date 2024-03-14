package pt.isel.ls.DTO.Session

import kotlinx.serialization.Serializable

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
        require(date?.isNotBlank() ?: true ) { "Session date must be a valid date in the future" }
        require((state?.isNotBlank() ?: true) || state?.equals("open") ?: true || state?.equals("close") ?: true ) { "Session state must be a valid state" }
    }
}