package pt.isel.ls.data.domain.primitives

data class PasswordHash(val hash: String) {
    init {
        require(hash.isNotBlank()) { "The password hash must not be empty" }
    }

    override fun toString(): String {
        return hash
    }
}