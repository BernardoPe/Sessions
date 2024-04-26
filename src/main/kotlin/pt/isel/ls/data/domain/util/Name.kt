package pt.isel.ls.data.domain.util

data class Name(val name: String) {
    init {
        require(name.isNotBlank()) { "The name must not be empty" }
        require(name.length in 3..60) { "The name must be between 3 and 60   characters" }
    }

    override fun toString(): String {
        return name
    }
}

