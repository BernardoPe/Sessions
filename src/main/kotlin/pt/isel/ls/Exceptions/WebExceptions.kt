package pt.isel.ls.Exceptions

open class WebExceptions(status: Int, description: String, cause: String?) : Exception() {
    val status: Int = status
    val description: String = description
    val error_cause: String? = cause
}

// 400 Bad Request
class BadRequestException(cause: String?) : WebExceptions(400, "Bad Request", cause)

// 404 Not Found
class NotFoundException(item: String?) : WebExceptions(404, "Not Found", item)

// 500 Internal Server Error
class InternalErrorException() : WebExceptions(500, "Internal Server Error", null)

// 501 Not Implemented
class NotImplementedException() : WebExceptions(501, "Not Implemented", null)



