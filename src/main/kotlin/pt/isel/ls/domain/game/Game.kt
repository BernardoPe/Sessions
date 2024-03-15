package pt.isel.ls.domain.game

/**
 *  Game
 *
 *  The [Game] Data Class is the representation of a Game in the system.
 *
 *  @param gid The game identifier
 *  @param developer The game developer
 *  @param genres The game list of genres
 */
data class Game (
        val gid: Int,
        val name: String,
        val developer: String,
        val genres: List<String>
) {
        init {
                require(gid >= 0) { "The game identifier must be a positive integer" }
                require(name.isNotBlank()) { "The game name must not be empty" }
                require(developer.isNotBlank()) { "The game developer must not be empty" }
                require(
                        genres.all { it.isNotBlank() }
                ) { "The game genres must not be empty" }
        }
}


