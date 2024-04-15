package pt.isel.ls.data.domain.util

data class Genre(val genre: String) {

    companion object {
        val genresList = listOf("RPG", "Adventure", "Shooter", "Turn-Based", "Action")
    }
    init {
        require(genre.isNotBlank()) { "Genres must not be blank" }
        require(genre.length in 3..40) { "Genres must be between 3 and 40 characters" }
        require(genre in genresList) { "Genre must be one of the following $genresList" }
    }

    override fun toString(): String {
        return genre
    }
}

