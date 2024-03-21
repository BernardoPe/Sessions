package pt.isel.ls.exceptions

import kotlinx.serialization.Serializable
import org.http4k.core.Status
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.NOT_IMPLEMENTED

/**
 * The [SessionsExceptions] class is an exception that is thrown when an expected exception occurs.
 */
@Serializable
open class SessionsExceptions(val status: Int, val description: String, val errorCause: String?) : Exception()


/**
 * The [BadRequestException] class is an exception that is thrown when the request is invalid.
 * @property cause The cause of the exception
 */
class BadRequestException(cause: String?) : SessionsExceptions(BAD_REQUEST.code, "Bad Request", cause)

/**
 * The [NotFoundException] class is an exception that is thrown when the resource is not found.
 * @property item The item that was not found
 */
class NotFoundException(item: String?) : SessionsExceptions(NOT_FOUND.code, "Not Found", item)

/**
 * The [NotImplementedException] class is an exception that is thrown when the request is not implemented.
 */
@Serializable
class NotImplementedException : SessionsExceptions(NOT_IMPLEMENTED.code, "Not Implemented", null)


/**
 * The [InternalServerErrorException] class is an exception that is thrown when the server has an internal error.
 */
@Serializable
class InternalServerErrorException : SessionsExceptions(Status.INTERNAL_SERVER_ERROR.code, "Internal Server Error", null)

/**
 * The [UnsupportedMediaTypeException] class is an exception that is thrown when the media type is not supported.
 */
@Serializable
class UnsupportedMediaTypeException : SessionsExceptions(Status.UNSUPPORTED_MEDIA_TYPE.code, "Unsupported Media Type", null)

/**
 * The [UnauthorizedException] class is an exception that is thrown when the user is not authorized.
 */
@Serializable
class UnauthorizedException : SessionsExceptions(Status.UNAUTHORIZED.code, "Unauthorized", "Invalid Auth")