package pt.isel.ls.exceptions

import kotlinx.serialization.Serializable
import org.http4k.core.Status
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.NOT_IMPLEMENTED

@Serializable

open class WebExceptions(val status: Int, val description: String, val errorCause: String?) : Exception()

// 400 Bad Request

class BadRequestException(cause: String?) : WebExceptions(BAD_REQUEST.code, "Bad Request", cause)

// 404 Not Found
class NotFoundException(item: String?) : WebExceptions(NOT_FOUND.code, "Not Found", item)

// 501 Not Implemented
@Serializable
class NotImplementedException() : WebExceptions(NOT_IMPLEMENTED.code, "Not Implemented", null)

@Serializable
class InternalServerErrorException() : WebExceptions(Status.INTERNAL_SERVER_ERROR.code, "Internal Server Error", null)

@Serializable
class UnsupportedMediaTypeException() : WebExceptions(Status.UNSUPPORTED_MEDIA_TYPE.code, "Unsupported Media Type", null)

@Serializable
class UnauthorizedException() : WebExceptions(Status.UNAUTHORIZED.code, "Unauthorized", "Invalid Auth")