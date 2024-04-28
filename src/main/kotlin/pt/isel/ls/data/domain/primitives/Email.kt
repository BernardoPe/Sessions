package pt.isel.ls.data.domain.primitives

/**
 * Represents an e-mail address
 *
 * Used to ensure that the e-mail is in a valid format and follows the proper data integrity rules
 */
data class Email(val email: String) {
    init {
        require(email.isNotBlank()) { "Emails must not be blank" }
        require(email.matches(Regex("^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})\$"))) { "Emails must be a valid e-mail format" }
    }

    override fun toString(): String {
        return email
    }
}

