package pt.isel.ls.exceptions

open class StorageExceptions(val description: String, val errorCause: String?) : Exception()

// Game name already exists
class GameNameAlreadyExistsException(cause: String?) : StorageExceptions("Game name already exists", cause)

// Game not found
class GameNotFoundException(item: String?) : StorageExceptions("Game not found", item)

// Player email already exists
class PlayerEmailAlreadyExistsException(cause: String?) : StorageExceptions("Player email already exists", cause)

// Player not found
class PlayerNotFoundException(item: String?) : StorageExceptions("Player not found", item)

// Session not found
class SessionNotFoundException(item: String?) : StorageExceptions("Session not found", item)
