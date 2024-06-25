package pt.isel.ls.exceptions

import org.http4k.core.Status
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.NOT_FOUND

/**
 * The [SessionsExceptions] class is an exception that is thrown when an expected exception occurs.
 */
open class SessionsExceptions(val status: Int, val description: String, val errorCause: String?) : Exception()

/**
 * The [BadRequestException] class is an exception that is thrown when the request is invalid.
 * @param cause The cause of the exception
 */
class BadRequestException(cause: String?) : SessionsExceptions(BAD_REQUEST.code, "Bad Request", cause)

/**
 * The [NotFoundException] class is an exception that is thrown when the resource is not found.
 * @param item The item that was not found
 */
class NotFoundException(item: String?) : SessionsExceptions(NOT_FOUND.code, "Not Found", item)


/**
 * The [InternalServerErrorException] class is an exception that is thrown when the server has an internal error.
 */

class InternalServerErrorException :
    SessionsExceptions(Status.INTERNAL_SERVER_ERROR.code, "Internal Server Error", null)

/**
 * The [UnsupportedMediaTypeException] class is an exception that is thrown when the media type is not supported.
 */
class UnsupportedMediaTypeException : SessionsExceptions(Status.UNSUPPORTED_MEDIA_TYPE.code, "Unsupported Media Type", null)

/**
 * The [UnauthorizedException] class is an exception that is thrown when the user is not authorized.
 */
class UnauthorizedException : SessionsExceptions(Status.UNAUTHORIZED.code, "Unauthorized", "Invalid Auth")

