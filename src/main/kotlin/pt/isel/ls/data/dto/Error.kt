package pt.isel.ls.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorOutputModel(val status: Int, val description: String, val errorCause: String?)