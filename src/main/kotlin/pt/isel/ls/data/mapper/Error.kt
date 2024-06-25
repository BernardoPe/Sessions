package pt.isel.ls.data.mapper

import pt.isel.ls.data.dto.ErrorOutputModel
import pt.isel.ls.exceptions.SessionsExceptions

fun SessionsExceptions.toErrorDTO() = ErrorOutputModel(status, description, errorCause)