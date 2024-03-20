package pt.isel.ls.data.domain

data class Genre(val genre: String) {

    companion object {
        private val genresList = listOf("RPG", "Adventure", "Shooter", "Turn-Based", "Action")
    }
    init {
        require(genre.isNotBlank()) { "Genres must not be blank" }
        require(genre.length in 3..40) { "Genres must be between 1 and 40 characters" }
        require(genre in genresList) { "Genres must be one of the following: $genresList" }
    }

}

fun String.toGenre(): Genre = Genre(this)
