package pt.isel.ls.data.domain.primitives

/**
 * Represents a name
 *
 * Used to ensure that the name is in a valid format and follows the proper data integrity rules
 */
data class Name(val name: String) {
    init {
        require(name.isNotBlank()) { "The name must not be empty" }
        require(name.length in 3..60) { "The name must be between 3 and 60   characters" }
    }

    override fun toString(): String {
        return name
    }
}

