package pt.isel.ls.data.domain

data class Email(val email: String) {
    init {
        require(email.isNotBlank()) { "Emails must not be blank" }
        require(email.matches(Regex("^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})\$"))) { "Emails must be a valid e-mail format" }
    }
}