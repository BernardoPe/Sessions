package pt.isel.ls.domain

interface DomainMapper<T> {
    fun toInfoDTO(): T

}