package pt.isel.ls.data.domain

data class Name(val name: String){
    init {
        require(name.isNotBlank()) { "The name must not be empty" }
        require(name.length in 3..40) { "The name must be between 3 and 40 characters" }
    }
}