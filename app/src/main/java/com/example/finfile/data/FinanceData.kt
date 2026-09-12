package com.example.finfile.data

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class AccountRow(
    val key: String,
    name: String,
    invested: Double,
    minus: Double,
    plus: Double
) {
    var name by mutableStateOf(name)
    var invested by mutableStateOf(invested)
    var minus by mutableStateOf(minus)
    var plus by mutableStateOf(plus)
    val total: Double get() = invested - minus + plus
}

object Defaults {
    val stocks = listOf(
        Triple("Тинькофф", 278_500.0, 85_253.0),
        Triple("Сбербанк", 215_000.0, 65_674.0),
        Triple("БКС",       70_000.0, 32_167.0),
    )
    val bonds = listOf(
        Triple("Тинькофф", 0.0,     0.0),
        Triple("Сбербанк", 92_350.0, 2_960.0),
        Triple("БКС",       0.0,     0.0),
    )
    val deposits = listOf(500_000.0, 300_000.0, 600_000.0, 300_000.0)
    const val cashSimple = 500_000.0
    const val cashGifts  = 200_000.0
    const val cashWine   =  50_000.0
    const val cardVTB    =  24_150.0
    const val cardTink   =  29_000.0
    const val cardAlfa   =  33_600.0
}
