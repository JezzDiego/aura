package com.example.aura.presentation.ui.feature_home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen() {
    Scaffold(
        modifier = Modifier
            .safeContentPadding()
    ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                Text("Hello from Home Screen", )
            }

    }
}