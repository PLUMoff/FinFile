package com.example.finfile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.finfile.ui.FinanceScreen
import com.example.finfile.ui.theme.FinFileTheme

class MainActivity : ComponentActivity() {

    private val vm: FinanceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinFileTheme {
                FinanceScreen(vm)
            }
        }
    }
}
