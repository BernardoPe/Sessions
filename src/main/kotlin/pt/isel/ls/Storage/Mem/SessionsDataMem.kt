package pt.isel.ls.Storage.Mem

import pt.isel.ls.Storage.SessionsData

/**
 *  Data management class
 *
 *  Using the CRUD operations
 *
 *  @property create Create a new data entry
 *  @property get Read a data entry
 *  @property update Update a data entry
 *  @property delete Delete a data entry
 */
open class SessionsDataMem<E> (
    /**
     * Getter function
     *
     * This function is used as a comparator
     * It is used to compare the data entry with the given id in the [get] function
     *
     * @property E The data entry
     * @property Int The data entry identifier
     * @return Boolean. True if the data entry has the given id, false otherwise
     */
    private val getter: (E, Int) -> Boolean

) : SessionsData<E> {

    /**
     * Database Mock
     *
     * This is a mockup of the database, used for testing purposes.
     *
     * @property db The database.
     */
    var db: MutableList<E> = mutableListOf()

    /**
     * Create a new data entry
     *
     * This function adds a new data entry to the [db] list
     *
     * @param value The data entry to be created
     */
    override fun create(value: E) {
        db.add(value)
    }

    /**
     * Read a data entry
     *
     * This function reads a data entry from the [db] list
     *
     * @param id The data entry identifier
     * @return The data entry with the given id or null if it does not exist
     */
    override fun get(id: Int): E? {
        return db.firstOrNull { getter(it, id) }
    }

/**
     * Read all data entries
     *
     * This function reads all data entries from the [db] list
     *
     * @return A list with all the data entries in the [db] list
     */
    override fun getAll(): List<E> {
        return db
    }


    /**
     * Update a data entry
     *
     * This function updates a data entry in the [db] list
     *
     * @param id The data entry identifier
     * @param value The new data entry
     */
    override fun update(id: Int, value: E) {
        // Find the data entry with the given id
        val index = db.indexOfFirst { getter(it, id) }
        // check if the data entry exists
        if (index == -1) return
        // Update the data entry
        db[index] = value
    }

    /**
     * Delete a data entry
     *
     * This function deletes a data entry from the [db] list
     *
     * @param id The data entry identifier
     */
    override fun delete(id: Int) {
        db.removeAt(id)
    }
}