package com.example.ecolog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class LogActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LogScreen()
        }
    }
}