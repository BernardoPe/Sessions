package pt.isel.ls.storage

interface SessionsData {
    val storagePlayer: SessionsDataPlayer

    val storageGame: SessionsDataGame

    val storageSession: SessionsDataSession
}