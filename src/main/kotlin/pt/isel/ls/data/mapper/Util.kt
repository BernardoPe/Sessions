package pt.isel.ls.data.mapper

import pt.isel.ls.data.domain.util.Email
import pt.isel.ls.data.domain.util.Genre
import pt.isel.ls.data.domain.util.Name

/**
 * Extension function that converts a [String] to a [Name] object.
 * @return [Name] object.
 */
fun String.toName(): Name = Name(this)

/**
 * Extension function that converts a [String] to a [Genre] object.
 * @return [Genre] object.
 */
fun String.toGenre(): Genre = Genre(this)

/**
 * Extension function that converts a [String] to a [Email] object.
 * @return [Email] object.
 */
fun String.toEmail(): Email = Email(this)
