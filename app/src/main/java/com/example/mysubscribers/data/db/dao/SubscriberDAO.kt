package com.example.mysubscribers.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mysubscribers.data.db.entity.SubscriberEntity

@Dao
interface SubscriberDAO {

    @Insert
    suspend fun insert(subscriber: SubscriberEntity): Long

    @Update
    suspend fun update(subscriber: SubscriberEntity)

    @Query("DELETE FROM subscriber WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM subscriber")
    suspend fun deleteAll()

    @Query("SELECT * FROM subscriber")
    suspend fun getAll(): List<SubscriberEntity>
}