package com.gram.client.presentation.components.voyager

import android.view.ViewTreeObserver
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
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
            when(withComment){
                Comments.CANCEL->mutableStateOf(TextFieldValue(text = Values.CommentCancelReasons.value, selection = TextRange(Values.CommentCancelReasons.value.length)))
                Comments.RATING -> mutableStateOf(TextFieldValue(text = Values.CommentRatingReasons.value, selection = TextRange(Values.CommentRatingReasons.value.length)))
                else -> mutableStateOf(TextFieldValue(text = Values.CommentDriver.value, selection = TextRange(Values.CommentDriver.value.length)))
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
                if (keyboard != null) {
                    keyboard.hide()
                }
            }
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.55f)
            .bringIntoViewRequester(bringIntoViewRequester),
        verticalArrangement = Arrangement.SpaceBetween) {
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
                    onValueChange = { text.value = it },
                    label = { Text(text = label)},
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
                )
            }
            Button(
                onClick = {
                    if (keyboard != null) {
                        keyboard.hide()
                    }
                    when(withComment){
                        Comments.CANCEL->Values.CommentCancelReasons.value =text.value.text
                        Comments.RATING -> Values.CommentRatingReasons.value =text.value.text
                        else -> {
                            Values.CommentDriver.value = text.value.text
                            bottomNavigator.hide()
                            return@Button
                        }
                    }
                    bottomNavigator.pop()
                },
                enabled = text.value.text != "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(57.dp)
                    .padding(horizontal = 15.dp)
                    .offset(0.dp, -15.dp)
                    ,
                shape = RoundedCornerShape(15.dp),
                elevation = ButtonDefaults.elevation(0.dp)
            ) {
                Text(text = "Готово", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}