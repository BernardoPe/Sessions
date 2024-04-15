package pt.isel.ls.data.mapper

import pt.isel.ls.data.domain.util.Email
import pt.isel.ls.data.domain.util.Genre
import pt.isel.ls.data.domain.util.Name

fun String.toName(): Name = Name(this)

fun String.toGenre(): Genre = Genre(this)

fun String.toEmail(): Email = Email(this)
