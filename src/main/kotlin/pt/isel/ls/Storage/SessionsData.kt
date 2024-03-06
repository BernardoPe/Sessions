package pt.isel.ls.Storage

interface SessionsData<E> {
    fun create(value: E)

    fun read(id: Int): E?

    fun update(id: Int, value: E)

    fun delete(id: Int)
}