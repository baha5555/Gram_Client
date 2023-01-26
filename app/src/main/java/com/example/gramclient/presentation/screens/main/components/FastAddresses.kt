package com.example.gramclient.presentation.screens.main.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun FastAddresses() {
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        repeat(5) {
            item {
                FastAddressCard(title = "Панчшанбе Базар")
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}