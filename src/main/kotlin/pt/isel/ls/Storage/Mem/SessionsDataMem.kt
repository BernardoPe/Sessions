package pt.isel.ls.Storage.Mem

import pt.isel.ls.Storage.SessionsData

/**
 *  Data management class
 *
 *  Using the CRUD operations
 *
 *  @property create - Create a new data entry
 *  @property read - Read a data entry
 *  @property update - Update a data entry
 *  @property delete - Delete a data entry
 */
open class SessionsDataMem<E> : SessionsData<E> {

    /**
     * Database Mock
     *
     * This is a mockup of the database, used for testing purposes.
     *
     * @property db The database.
     */
    var db: MutableList<E> = mutableListOf()

    open override fun create(value: E) {
        db.add(value)
    }

    open override fun read(id: Int): E? {
        return db[id]
    }

    open override fun update(id: Int, value: E) {
        db[id] = value
    }

    open override fun delete(id: Int) {
        db.removeAt(id)
    }
}