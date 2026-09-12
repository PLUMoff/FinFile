package com.example.finfile.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finfile.FinanceViewModel
import com.example.finfile.data.AccountRow
import java.text.NumberFormat
import java.util.Locale

private val nf: NumberFormat = NumberFormat.getNumberInstance(Locale("ru", "RU"))

fun formatMoney(v: Double): String = nf.format(v.toLong()) + " ₽"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceScreen(vm: FinanceViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ФинФайл") },
                actions = {
                    TextButton(onClick = { vm.resetAll() }) {
                        Text("Сброс")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SectionCard(
                title = "Акции",
                rows = vm.stocks,
                onChange = { row, field, v -> vm.setStock(row, field, v) },
                total = { vm.stocksTotal }
            )
            SectionCard(
                title = "Облигации",
                rows = vm.bonds,
                onChange = { row, field, v -> vm.setBond(row, field, v) },
                total = { vm.bondsTotal }
            )

            Card {
                Column(Modifier.padding(12.dp)) {
                    Text("Вклады (ВТБ)", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(Modifier.height(8.dp))
                    vm.deposits.forEachIndexed { i, v ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("${i + 1}.", Modifier.width(24.dp))
                            var text by remember(v) { mutableStateOf(v.toLong().toString()) }
                            OutlinedTextField(
                                value = text,
                                onValueChange = {
                                    text = it.filter { c -> c.isDigit() }
                                    vm.setDeposit(i, text.toDoubleOrNull() ?: 0.0)
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                    }
                    TotalRow("Итого", vm.depositsTotal)
                }
            }

            Card {
                Column(Modifier.padding(12.dp)) {
                    Text("Наличные", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(Modifier.height(8.dp))
                    MoneyField("Просто",  vm.cashSimple) { vm.setCashSimple(it) }
                    MoneyField("Подарки", vm.cashGifts)  { vm.setCashGifts(it)  }
                    MoneyField("Вино",    vm.cashWine)   { vm.setCashWine(it)   }
                    TotalRow("Итого", vm.cashTotal)
                }
            }

            Card {
                Column(Modifier.padding(12.dp)) {
                    Text("Карты", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(Modifier.height(8.dp))
                    MoneyField("ВТБ",    vm.cardVTB)  { vm.setCardVTB(it)  }
                    MoneyField("Тинёк",  vm.cardTink) { vm.setCardTink(it) }
                    MoneyField("Альфа",  vm.cardAlfa) { vm.setCardAlfa(it) }
                    TotalRow("Итого", vm.cardsTotal)
                }
            }

            Card(colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )) {
                Row(
                    Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("КАК-ТО ТАК", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(formatMoney(vm.grandTotal), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    rows: List<AccountRow>,
    onChange: (AccountRow, String, Double) -> Unit,
    total: () -> Double
) {
    Card {
        Column(Modifier.padding(12.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            rows.forEach { row ->
                Text(row.name, fontWeight = FontWeight.SemiBold)
                MoneyField("Вложено", row.invested) { onChange(row, "invested", it) }
                MoneyField("Минус",   row.minus)    { onChange(row, "minus",    it) }
                MoneyField("Плюс",    row.plus)     { onChange(row, "plus",     it) }
                TotalRow("Итого по ${row.name}", row.total)
                Spacer(Modifier.height(6.dp))
            }
            TotalRow("Всего", total())
        }
    }
}

@Composable
private fun MoneyField(label: String, value: Double, onChange: (Double) -> Unit) {
    var text by remember(value) { mutableStateOf(value.toLong().toString()) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, Modifier.width(96.dp))
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it.filter { c -> c.isDigit() }
                onChange(text.toDoubleOrNull() ?: 0.0)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
    Spacer(Modifier.height(4.dp))
}

@Composable
private fun TotalRow(label: String, value: Double) {
    Row(
        Modifier.fillMaxWidth().padding(top = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = FontWeight.Medium)
        Text(formatMoney(value), fontWeight = FontWeight.Bold)
    }
}
