package com.example.finfile.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FinanceDao {

    @Query("SELECT * FROM finance")
    fun getAll(): Flow<List<FinanceEntity>>

    @Query("SELECT * FROM finance WHERE category = :cat AND name = :name AND field = :field LIMIT 1")
    suspend fun find(cat: String, name: String, field: String): FinanceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: FinanceEntity): Long

    @Query("UPDATE finance SET value = :value WHERE category = :cat AND name = :name AND field = :field")
    suspend fun update(cat: String, name: String, field: String, value: Double)

    @Query("DELETE FROM finance")
    suspend fun clear()
}
