package pt.isel.ls.data.domain.primitives

data class Password(val password: String) {
    init {
        require(password.isNotBlank()) { "An password is needed to create a player" }
        require(password.length < 8) {
            "The password must at least have 8 characters"
        }
        require(password.any { it.isLowerCase() }) {
            "The password must contain at least one lowercase letter"
        }
        require(password.any { it.isUpperCase() }) {
            "The password must contain at least one uppercase letter"
        }
        require(password.any { it.isDigit() }) {
            "The password must contain at least one digit"
        }
        require(password.any { it.isWhitespace() }) {
            "The password must not contain any whitespace"
        }
        require(password.any { !it.isLetterOrDigit() }) {
            "The password must contain at least one special character"
        }
    }

    override fun toString(): String {
        return password
    }
}