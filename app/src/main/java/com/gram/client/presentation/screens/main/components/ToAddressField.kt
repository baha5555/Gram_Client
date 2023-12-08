package com.gram.client.presentation.screens.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R
import com.gram.client.domain.mainScreen.Address
import com.gram.client.presentation.components.voyager.MapPointScreen
import com.gram.client.presentation.components.voyager.SearchAddresses
import com.gram.client.presentation.screens.main.MainScreen
import com.gram.client.presentation.screens.main.MainViewModel
import com.gram.client.ui.theme.PrimaryColor
import com.gram.client.utils.Constants
import com.gram.client.utils.Values
import com.gram.client.utils.getAddressText


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ToAddressField(
    toAddress: List<Address>
) {
    val navigator = LocalNavigator.currentOrThrow
    val mainViewModel: MainViewModel = hiltViewModel()
    val bottomNavigator = LocalBottomSheetNavigator.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                Values.WhichAddress.value = Constants.TO_ADDRESS
                bottomNavigator.show(SearchAddresses() {
                    navigator.push(MapPointScreen())
                })
            }
            .background(PrimaryColor)
            .padding(horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.car_kuda_edem),
            contentDescription = "car_eco",
            modifier = Modifier.offset(x = -20.dp).size(60.dp)
        )
        Text(
            text = if (toAddress.isEmpty()) "Куда едем?" else getAddressText(toAddress[0]),
            textAlign = TextAlign.Start,
            color = if (toAddress.isEmpty()) Color.White else Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1, overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f).padding(end = 12.dp)
        )
        Row(
            modifier = Modifier
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
                        if (mainViewModel.fromAddress.value.address != "") {
                            navigator.push(MainScreen())
                        } else {
                            Values.WhichAddress.value = Constants.ADD_FROM_ADDRESS_FOR_NAVIGATE
                            bottomNavigator.show(SearchAddresses(
                                {
                                    navigator.push(MainScreen())
                                }
                            ) {
                                navigator.push(MapPointScreen())
                            })
                        }
                    },
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "car_eco",
                tint = Color.White
            )
        }

    }
}
