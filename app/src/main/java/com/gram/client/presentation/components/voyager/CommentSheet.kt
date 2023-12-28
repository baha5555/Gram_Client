package com.gram.client.presentation.components.voyager

import android.view.ViewTreeObserver
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.gram.client.utils.Comments
import com.gram.client.utils.Values
import kotlinx.coroutines.launch

class CommentSheet(val label: String, val withComment: String) : Screen {

    @OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {
        val bottomNavigator = LocalBottomSheetNavigator.current
        val bringIntoViewRequester = remember { BringIntoViewRequester() }
        val scope = rememberCoroutineScope()
        val focusRequester = remember { FocusRequester() }
        val keyboard = LocalSoftwareKeyboardController.current
        val text = remember {
            when (withComment) {
                Comments.CANCEL -> mutableStateOf(
                    TextFieldValue(
                        text = Values.CommentCancelReasons.value,
                        selection = TextRange(Values.CommentCancelReasons.value.length)
                    )
                )
                Comments.RATING -> mutableStateOf(
                    TextFieldValue(
                        text = Values.CommentRatingReasons.value,
                        selection = TextRange(Values.CommentRatingReasons.value.length)
                    )
                )
                Comments.DRIVER -> mutableStateOf(
                    TextFieldValue(
                        text = Values.CommentDriver.value,
                        selection = TextRange(Values.CommentDriver.value.length)
                    )
                )
                else -> mutableStateOf(
                    TextFieldValue(
                        text = Values.CommentToAnotherHuman.value,
                        selection = TextRange(Values.CommentToAnotherHuman.value.length)
                    )
                )
            }
        }
        val view = LocalView.current
        DisposableEffect(view) {
            focusRequester.requestFocus()
            val listener = ViewTreeObserver.OnGlobalLayoutListener {
                scope.launch { bringIntoViewRequester.bringIntoView() }
            }
            view.viewTreeObserver.addOnGlobalLayoutListener(listener)
            onDispose {
                view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
                keyboard?.hide()
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.55f)
                .bringIntoViewRequester(bringIntoViewRequester),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column() {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .background(Color(0xFFF5F4F2)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(label, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                }
                TextField(
                    value = text.value,
                    onValueChange = {
                        if (withComment == Comments.TO_ANOTHER_HUMAN) {
                            text.value = it
                        } else {
                            text.value = it
                        }
                    },
                    label = { Text(text = label) },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        disabledTextColor = Color.Transparent,
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(fontSize = 18.sp),
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            if (it.isFocused) {
                                keyboard?.show()
                            }
                        },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = if (withComment==Comments.TO_ANOTHER_HUMAN) KeyboardType.Number else KeyboardType.Text),                 )
            }
            Button(
                onClick = {
                    keyboard?.hide()
                    when (withComment) {
                        Comments.CANCEL -> Values.CommentCancelReasons.value = text.value.text
                        Comments.RATING -> Values.CommentRatingReasons.value = text.value.text
                        Comments.DRIVER -> {
                            Values.CommentDriver.value = text.value.text
                            bottomNavigator.hide()
                            return@Button
                        }
                        else -> {
                            Values.CommentToAnotherHuman.value = text.value.text
                            bottomNavigator.hide()
                            return@Button
                        }
                    }
                    bottomNavigator.pop()
                },
                enabled = if(withComment!=Comments.TO_ANOTHER_HUMAN) true else text.value.text.length==12,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(57.dp)
                    .padding(horizontal = 15.dp)
                    .offset(0.dp, (-15).dp),
                shape = RoundedCornerShape(15.dp),
                elevation = ButtonDefaults.elevation(0.dp)
            ) {
                Text(text = "Готово", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}