package pt.isel.ls.storage

interface SessionsDataManager {
    fun <T> apply(block: (SessionsData) -> T): T
}