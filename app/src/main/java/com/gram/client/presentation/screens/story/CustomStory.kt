@file:Suppress("UNUSED_EXPRESSION")

package com.gram.client.presentation.screens.story

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CustomStory(
    count: Int = 4,
    slideDurationInSeconds: Long = 2,
    activedInx: MutableState<Int>,
    content: @Composable () -> Unit,
) {
    val navigator = LocalNavigator.currentOrThrow

    var active by remember { mutableStateOf(false) }
    var activeInx by remember { mutableStateOf(0) }
    val status = remember {
        mutableStateListOf<Float>().apply {
            repeat(count) {
                add(0.0f)
            }
        }
    }
    val progress = remember {
        mutableStateOf(0.0f)
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        scope.launch {
            repeat(count) {
                progress.value = 0.0f
                while (true) {
                    delay(slideDurationInSeconds*10)
                    progress.value += 0.01f
                    status[it] = progress.value
                    activedInx.value = it
                    if (progress.value >= 1) {
                        break
                    }
                }
            }
        }
    }
    LaunchedEffect(active) {
        while (active) {
            for (i in activeInx until count) {
                progress.value = 0.0f
                while (active) {
                    delay(slideDurationInSeconds*10)
                    progress.value += 0.01f
                    status[i] = progress.value
                    activedInx.value = i
                    if (progress.value >= 1) {
                        break
                    }
                }
                if (i == count - 1) active = false
            }
        }
    }

    Box(){
        content()
        Column {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
                .padding(vertical = 5.dp)) {
                repeat(count) { it ->
                    LinearProgressIndicator(
                        progress = status[it],
                        modifier = Modifier
                            .weight(1f)
                            .padding(5.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                if (active) {
                                    active = false
                                    GlobalScope.launch {
                                        delay(50)
                                        repeat(count) { inx -> status[inx] = 0.0f }
                                        repeat(it) { inx -> status[inx] = 1.0f }
                                        active = true
                                        activeInx = it
                                    }
                                } else {
                                    scope.cancel()
                                    repeat(count) { inx -> status[inx] = 0.0f }
                                    repeat(it) { inx -> status[inx] = 1.0f }
                                    active = true
                                    activeInx = it
                                }


                            },
                        backgroundColor = Color.White
                    )

                }
            }
            Box(modifier = Modifier
                .fillMaxSize()) {
                Box(Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() } // This is mandatory
                    ) {
                        if (activedInx.value in 1 until count) {
                            if (active) {
                                active = false
                                GlobalScope.launch {
                                    delay(50)
                                    repeat(count) { inx -> status[inx] = 0.0f }
                                    repeat(activedInx.value - 1) { inx -> status[inx] = 1.0f }
                                    active = true
                                    activeInx = activedInx.value - 1
                                }
                            } else {
                                scope.cancel()
                                repeat(count) { inx -> status[inx] = 0.0f }
                                repeat(activedInx.value - 1) { inx -> status[inx] = 1.0f }
                                active = true
                                activeInx = activedInx.value - 1
                            }
                        }


                    })
                Box(Modifier
                    .fillMaxWidth(0.5f)
                    .align(alignment = Alignment.CenterEnd)
                    .fillMaxHeight()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() } // This is mandatory
                    ) {
                        if (activedInx.value in 0 until count - 1) {
                            if (active) {
                                active = false
                                GlobalScope.launch {
                                    delay(50)
                                    repeat(count) { inx -> status[inx] = 0.0f }
                                    repeat(activedInx.value + 1) { inx -> status[inx] = 1.0f }
                                    active = true
                                    activeInx = activedInx.value + 1
                                }
                            } else {
                                scope.cancel()
                                repeat(count) { inx -> status[inx] = 0.0f }
                                repeat(activedInx.value + 1) { inx -> status[inx] = 1.0f }
                                active = true
                                activeInx = activedInx.value + 1
                            }
                        }
                    })
                IconButton(onClick = { navigator.pop() }, modifier = Modifier .align(alignment = Alignment.TopEnd)) {
                    Image(imageVector = Icons.Default.Close, contentDescription = "", Modifier.size(24.dp))
                }
            }
        }
    }
}