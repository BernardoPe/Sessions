package pt.isel.ls.data.domain.primitives

/**
 * Represents a name
 *
 * Used to ensure that the name is in a valid format and follows the proper data integrity rules
 *
 * @property name the name
 *
 * @throws IllegalArgumentException if the name is empty or has less than 3 characters or more than 60 characters
 */
data class Name(val name: String) {
    init {
        require(name.isNotBlank()) { "The name must not be empty" }
        require(name.length in 3..60) { "The name must be between 3 and 60 characters" }
    }

    /**
     * Converts the name to a string
     * @return [String] the name
     */
    override fun toString(): String {
        return name
    }
}

