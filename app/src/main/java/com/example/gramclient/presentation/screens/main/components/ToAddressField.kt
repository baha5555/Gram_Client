package com.example.gramclient.presentation.screens.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.gramclient.R
import com.example.gramclient.domain.mainScreen.Address
import com.example.gramclient.presentation.screens.main.MainScreen
import com.example.gramclient.ui.theme.PrimaryColor
import com.example.gramclient.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ToAddressField(
    WHICH_ADDRESS: MutableState<String>,
    isSearchState: MutableState<Boolean>,
    bottomSheetState: BottomSheetScaffoldState,
    scope: CoroutineScope,
    toAddress: List<Address>
) {
    val navigator = LocalNavigator.currentOrThrow
    toAddress.forEach { address ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(20.dp))
                .clickable {
                    scope.launch {
                        bottomSheetState.bottomSheetState.expand()
                    }
                    isSearchState.value = true
                    WHICH_ADDRESS.value = Constants.TO_ADDRESS
                }
                .background(PrimaryColor)
                .padding(horizontal = 5.dp)
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_car),
                contentDescription = "car_eco",
                modifier = Modifier.align(Alignment.CenterStart)
            )
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    color = Color.White,
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight(0.5f)
                        .offset((-10).dp, 0.dp)
                )
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            navigator.push(MainScreen())
                        },
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "car_eco",
                    tint = Color.White
                )
            }
            if (address.address == "") {
                Text(
                    text = "Куда едем?",
                    textAlign = TextAlign.Start,
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1, overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .align(Alignment.Center)
                )
            } else {
                Text(
                    text = address.address,
                    textAlign = TextAlign.Start,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1, overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .align(Alignment.Center)
                )
            }
        }
    }
}
