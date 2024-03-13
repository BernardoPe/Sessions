package pt.isel.ls.Storage

/**
 *  Sessions Data management interface
 *
 *  This interface is used to manage the data of the sessions
 *  It is used to create, read, update and delete items from the database
 *
 *  @property create Create a new item in the database
 *  @property get Read an item from the database
 *  @property update Update an item in the database
 *  @property delete Delete an item from the database
 */
interface SessionsData<E> {
    /**
     * Create a new item in the database
     *
     * @param value The item to be created
     */
    fun create(value: E)

    /**
     * Read an item from the database by its identifier
     *
     * @param id The item identifier
     * @return The item with the given id or null if it does not exist
     */
    fun get(id: Int): E?

    /**
     * Read all items from the database
     *
     * @return A list with all the items in the database
     */
    fun getAll(): List<E>

    /**
     * Update an item in the database
     *
     * @param id The item identifier
     * @param value The new item
     */
    fun update(id: Int, value: E)

    /**
     * Delete an item from the database
     *
     * @param id The item identifier
     */
    fun delete(id: Int)
}