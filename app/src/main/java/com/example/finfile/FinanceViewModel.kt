package com.example.finfile

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finfile.data.AccountRow
import com.example.finfile.data.Defaults
import com.example.finfile.data.FinanceRepository
import com.example.finfile.data.db.AppDatabase
import kotlinx.coroutines.launch

class FinanceViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = FinanceRepository(AppDatabase.get(app).financeDao())

    val stocks = mutableStateListOf<AccountRow>()
    val bonds  = mutableStateListOf<AccountRow>()
    val deposits = mutableStateListOf<Double>()

    var cashSimple by mutableStateOf(0.0)
    var cashGifts  by mutableStateOf(0.0)
    var cashWine   by mutableStateOf(0.0)

    var cardVTB  by mutableStateOf(0.0)
    var cardTink by mutableStateOf(0.0)
    var cardAlfa by mutableStateOf(0.0)

    init {
        viewModelScope.launch { loadAll() }
    }

    private suspend fun loadAll() {
        Defaults.stocks.forEach { (name, inv, minus) ->
            stocks += AccountRow(
                key = name, name = name,
                invested = repo.getOrCreate("stock", name, "invested", inv),
                minus    = repo.getOrCreate("stock", name, "minus",    minus),
                plus     = repo.getOrCreate("stock", name, "plus",     0.0),
            )
        }
        Defaults.bonds.forEach { (name, inv, minus) ->
            bonds += AccountRow(
                key = name, name = name,
                invested = repo.getOrCreate("bond", name, "invested", inv),
                minus    = repo.getOrCreate("bond", name, "minus",    minus),
                plus     = repo.getOrCreate("bond", name, "plus",     0.0),
            )
        }
        Defaults.deposits.forEachIndexed { i, def ->
            deposits += repo.getOrCreate("deposit", "ВТБ$i", "value", def)
        }
        cashSimple = repo.getOrCreate("cash", "Просто",  "value", Defaults.cashSimple)
        cashGifts  = repo.getOrCreate("cash", "Подарки", "value", Defaults.cashGifts)
        cashWine   = repo.getOrCreate("cash", "Вино",    "value", Defaults.cashWine)
        cardVTB    = repo.getOrCreate("card", "ВТБ",   "value", Defaults.cardVTB)
        cardTink   = repo.getOrCreate("card", "Тинёк", "value", Defaults.cardTink)
        cardAlfa   = repo.getOrCreate("card", "Альфа", "value", Defaults.cardAlfa)
    }

    fun setStock(row: AccountRow, field: String, v: Double) {
        when (field) {
            "invested" -> { row.invested = v; save("stock", row.key, "invested", v) }
            "minus"    -> { row.minus    = v; save("stock", row.key, "minus",    v) }
            "plus"     -> { row.plus     = v; save("stock", row.key, "plus",     v) }
        }
    }
    fun setBond(row: AccountRow, field: String, v: Double) {
        when (field) {
            "invested" -> { row.invested = v; save("bond", row.key, "invested", v) }
            "minus"    -> { row.minus    = v; save("bond", row.key, "minus",    v) }
            "plus"     -> { row.plus     = v; save("bond", row.key, "plus",     v) }
        }
    }
    fun setDeposit(i: Int, v: Double) {
        deposits[i] = v
        save("deposit", "ВТБ$i", "value", v)
    }
    fun setCashSimple(v: Double) { cashSimple = v; save("cash", "Просто",  "value", v) }
    fun setCashGifts(v: Double)  { cashGifts  = v; save("cash", "Подарки", "value", v) }
    fun setCashWine(v: Double)   { cashWine   = v; save("cash", "Вино",    "value", v) }
    fun setCardVTB(v: Double)    { cardVTB    = v; save("card", "ВТБ",   "value", v) }
    fun setCardTink(v: Double)   { cardTink   = v; save("card", "Тинёк", "value", v) }
    fun setCardAlfa(v: Double)   { cardAlfa   = v; save("card", "Альфа", "value", v) }

    private fun save(cat: String, name: String, field: String, value: Double) {
        viewModelScope.launch { repo.set(cat, name, field, value) }
    }

    val stocksTotal: Double get() = stocks.sumOf { it.total }
    val bondsTotal:  Double get() = bonds.sumOf  { it.total }
    val depositsTotal: Double get() = deposits.sum()
    val cashTotal: Double get() = cashSimple + cashGifts + cashWine
    val cardsTotal: Double get() = cardVTB + cardTink + cardAlfa
    val grandTotal: Double get() = stocksTotal + bondsTotal + depositsTotal + cashTotal + cardsTotal

    fun resetAll() = viewModelScope.launch {
        repo.clear()
        stocks.clear(); bonds.clear(); deposits.clear()
        loadAll()
    }
}
