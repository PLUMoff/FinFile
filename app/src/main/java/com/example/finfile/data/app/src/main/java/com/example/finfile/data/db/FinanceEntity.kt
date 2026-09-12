package com.example.finfile.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "finance")
data class FinanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: String,
    val name: String,
    val field: String,
    val value: Double
)
