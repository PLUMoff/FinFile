package com.example.finfile.data

import com.example.finfile.data.db.FinanceDao
import com.example.finfile.data.db.FinanceEntity
import kotlinx.coroutines.flow.Flow

class FinanceRepository(private val dao: FinanceDao) {

    val all: Flow<List<FinanceEntity>> = dao.getAll()

    suspend fun getOrCreate(cat: String, name: String, field: String, def: Double): Double {
        val existing = dao.find(cat, name, field)
        return if (existing != null) existing.value
        else {
            dao.insert(FinanceEntity(category = cat, name = name, field = field, value = def))
            def
        }
    }

    suspend fun set(cat: String, name: String, field: String, value: Double) {
        val existing = dao.find(cat, name, field)
        if (existing == null) {
            dao.insert(FinanceEntity(category = cat, name = name, field = field, value = value))
        } else {
            dao.update(cat, name, field, value)
        }
    }

    suspend fun clear() = dao.clear()
}
