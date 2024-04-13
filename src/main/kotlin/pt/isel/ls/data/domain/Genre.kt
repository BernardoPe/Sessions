package pt.isel.ls.data.domain

data class Genre(val genre: String) {

    companion object {
        val genresList = listOf("RPG", "Adventure", "Shooter", "Turn-Based", "Action")
    }
    init {
        require(genre.isNotBlank()) { "Genres must not be blank" }
        require(genre.length in 3..40) { "Genres must be between 3 and 40 characters" }
    }

    override fun toString(): String {
        return genre
    }
}

fun String.toGenre(): Genre = Genre(this)
