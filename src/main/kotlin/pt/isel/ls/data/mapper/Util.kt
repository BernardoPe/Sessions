package pt.isel.ls.data.mapper

import pt.isel.ls.data.domain.primitives.Email
import pt.isel.ls.data.domain.primitives.Genre
import pt.isel.ls.data.domain.primitives.Name

fun String.toName(): Name = Name(this)

fun String.toGenre(): Genre = Genre(this)

fun String.toEmail(): Email = Email(this)
