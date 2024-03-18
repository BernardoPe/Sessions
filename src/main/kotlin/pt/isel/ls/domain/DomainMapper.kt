package pt.isel.ls.domain

interface DomainMapper<T> {
    fun toDTO(): T

}