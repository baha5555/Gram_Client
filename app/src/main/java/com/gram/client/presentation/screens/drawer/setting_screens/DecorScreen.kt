package com.example.gramclient.presentation.screens.drawer.setting_screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R
import com.gram.client.presentation.screens.drawer.setting_screens.DecorScreenItem

class DecorScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomNavigator = LocalBottomSheetNavigator.current
        var selectedOption by remember { mutableStateOf(0) }
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier.fillMaxHeight(0.1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TopAppBar(
                        content = {
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                IconButton(modifier = Modifier, onClick = { navigator.pop() }) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_back_blue),
                                        contentDescription = ""
                                    )
                                }
                                Text(
                                    text = "Оформление",
                                    textAlign = TextAlign.Center,
                                    color = Color.Black,
                                    modifier = Modifier.align(Alignment.Center),
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        },
                        backgroundColor = Color.White,
                        contentColor = Color.Black,
                        elevation = 0.dp
                    )
                }
            }, backgroundColor = Color.White

        ) {
            Column {
                Text(
                    text = "Цвета",
                    Modifier.padding(start = 20.dp, top = 20.dp, bottom = 25.dp),
                    fontSize = 18.sp
                )
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    RadioButtonExample(selected = selectedOption == 0, color1 =Color(0xFF707070), color2 =  Color(0xFF1C1C1C)) {
                        selectedOption = 0
                    }
                    RadioButtonExample(selected = selectedOption == 1, color1 = Color(0xFF759BFF), color2 = Color(0xFF1152FD)) {
                        selectedOption = 1
                    }
                    RadioButtonExample(selected = selectedOption == 2,color1 = Color(0xFFFFF179), color2 = Color(0xFFF9DF00)) {
                        selectedOption = 2
                    }
                    RadioButtonExample(selected = selectedOption == 3, color1 =Color(0xFFFF7466) , color2 = Color(0xFFF93E2B)) {
                        selectedOption = 3
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    Text(text = "Стандарт")
                    Text(text = "Синий")
                    Text(text = "Желтый")
                    Text(text = "Красний")
                }
                Spacer(modifier = Modifier.height(30.dp))
                Divider(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .clickable {
                            bottomNavigator.show(DecorScreenItem())
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.theme_icon),
                            contentDescription = "theme_icon",
                            Modifier.padding(start = 20.dp, end = 12.dp)
                        )
                        Text(text = "Тема")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Системная")
                        IconButton(onClick = {}) {

                            Image(
                                modifier = Modifier.size(25.dp),
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_back_forward_blue),
                                contentDescription = "theme_icon",
                                )
                        }
                    }
                }

            }






        }
    }
}
@Composable
fun RadioButtonExample(
    selected: Boolean,
    color1: Color = Color.Black,
    color2: Color = Color.Black,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(45.dp)
            .background(brush = Brush.linearGradient(
                colors = listOf(color1, color2),
            ), shape = RoundedCornerShape(50))
            .clickable(onClick = onClick)
            .border(
                width = 2.dp,
                color = if (selected) Color(0xFF00D1FF) else Color.Transparent,
                shape = RoundedCornerShape(50)
            )
    ) {
        if (selected) {
            Icon(
                modifier = Modifier.align(Alignment.BottomEnd),
                imageVector = ImageVector.vectorResource(id = R.drawable.selectedicon),
                contentDescription = "",
                tint = Color.Unspecified
            )
        }
    }
}




@Composable
fun RadioButtonGroup(
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
) {
    Column {
        options.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) }
                    )
                    .padding(horizontal = 16.dp)
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = { onOptionSelected(text) },
                    colors = RadioButtonDefaults.colors(selectedColor = Color.Magenta),
                    modifier = Modifier.padding(start = 8.dp)
                )
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.selectedicon), contentDescription ="" , tint = Color.Unspecified)
                Text(
                    text = text,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
