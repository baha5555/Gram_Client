package com.example.gramclient.presentation.components

import android.annotation.SuppressLint
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CustomRequestError(onClick: () -> Unit) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    scope.launch {
        val result = snackbarHostState.showSnackbar(
            message = "Не удалось отправить запрос.\nПроверте соединение с интернетом.\n",
            actionLabel = "Повторить",
            duration = SnackbarDuration.Indefinite
        )
        when (result) {
            SnackbarResult.ActionPerformed -> {
                onClick.invoke()
            }
            SnackbarResult.Dismissed -> {
                /* dismissed, no action needed */
            }
        }
    }
    SnackbarHost(snackbarHostState)
}