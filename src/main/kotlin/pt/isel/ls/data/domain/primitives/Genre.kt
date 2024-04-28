package pt.isel.ls.data.domain.primitives


/**
 * Represents a genre
 *
 * Used to ensure that the genre is in a valid format and follows the proper data integrity rules
 *
 * @property genre the genre
 *
 * @throws IllegalArgumentException if the genre is blank, not in a valid format or not in the allowed genres list
 */
data class Genre(val genre: String) {

    companion object {
        // The list of genres that are allowed
        val genresList = listOf("RPG", "Adventure", "Shooter", "Turn-Based", "Action")
    }
    init {
        require(genre.isNotBlank()) { "Genres must not be blank" }
        require(genre.length in 3..40) { "Genres must be between 3 and 40 characters" }
        require(genre in genresList) { "Genre must be one of the following $genresList" }
    }

    /**
     * Converts the genre to a string
     * @return [String] the genre as a string
     */
    override fun toString(): String {
        return genre
    }
}

