package com.darvis.data.db

import androidx.room.*

@androidx.room.Dao
interface Dao<T> {

    /**
     * Insert an object in the database.
     *
     * @param entity the object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: T)

    /**
     * Insert an array of objects in the database.
     *
     * @param entities the objects to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg entities: T)

    /**
     * Update an object from the database.
     *
     * @param entity the object to be updated
     */
    @Update
    fun update(entity: T)

    @Update
    fun update(entities: List<T>)

    @Delete
    fun delete(entity: T)

    @Delete
    fun delete(entities: List<T>)
}