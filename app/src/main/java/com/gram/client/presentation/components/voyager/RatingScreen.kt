package com.gram.client.presentation.components.voyager

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.gram.client.R
import com.gram.client.presentation.components.CustomCheckBox
import com.gram.client.presentation.screens.order.OrderExecutionViewModel
import com.gram.client.utils.Comments
import com.gram.client.utils.Values

class RatingScreen(
    private val orderId: Int,
    private val price: Int?,
    private val function: () -> Unit
) : Screen {
    private var reasonsCheck: MutableState<String> = mutableStateOf("")
    private val ratingState = mutableStateOf(0)

    @Composable
    override fun Content() {
        val bottomNavigator = LocalBottomSheetNavigator.current
        val orderExecutionViewModel: OrderExecutionViewModel = hiltViewModel()
        val stateGetRatingReasons = orderExecutionViewModel.stateGetRatingReasons.value
        val text = remember { mutableStateOf("") }

        when (ratingState.value) {
            0 -> {
                text.value = "Оцените водителя"
            }
            5 -> {
                text.value = "Отлично"
            }
            else -> {
                text.value = "Спасибо за ваш отзыв"
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "$price cомон",
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                modifier = Modifier.padding(vertical = 10.dp)
            )
            Text(text = text.value, fontSize = 18.sp)
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(vertical = 20.dp), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(5) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = if (it < ratingState.value) R.drawable.ic_star else R.drawable.ic_star_border),
                        contentDescription = "",
                        modifier = Modifier.clickable(indication = null,
                            interactionSource = remember { MutableInteractionSource() }) {
                            ratingState.value = it + 1
                        }
                    )
                }
            }
            AnimatedVisibility(
                visible = text.value == "Спасибо за ваш отзыв"
            ) {
                Column() {
                    stateGetRatingReasons.response?.result?.forEach {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { reasonsCheck.value = it.name }
                                .padding(vertical = 10.dp)) {
                            Text("" + it.name, fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            CustomCheckBox(
                                isChecked = reasonsCheck.value == it.name,
                                onChecked = { reasonsCheck.value = it.name })
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(57.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color(0xFFF7F7F7))
                    .clickable {
                        bottomNavigator.push(CommentSheet("Ваш коментарий...", Comments.RATING))
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = if (Values.CommentRatingReasons.value == "") "Ваш коментарий..." else Values.CommentRatingReasons.value,
                    fontSize = 18.sp,
                    color = if (Values.CommentRatingReasons.value == "") Color(0xFFBEBEB5) else Color.Black,
                    modifier = Modifier.padding(15.dp)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    bottomNavigator.hide()

                    orderExecutionViewModel.sendRating2(
                        orderId,
                        ratingState.value * 10,
                        "" + if (reasonsCheck.value != "") reasonsCheck.value else "" + if (Values.CommentRatingReasons.value != "") "\n" + Values.CommentRatingReasons.value else ""
                    )
                    Values.CommentRatingReasons.value = ""
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(57.dp),
                shape = RoundedCornerShape(15.dp),
                elevation = ButtonDefaults.elevation(0.dp)
            ) {
                Text(text = "Готово", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }

        }
    }
}