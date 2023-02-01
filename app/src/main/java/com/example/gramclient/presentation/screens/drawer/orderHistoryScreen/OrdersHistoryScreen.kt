package com.example.gramclient.presentation.screens.drawer.orderHistoryScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import cafe.adriel.voyager.core.screen.Screen
import com.example.gramclient.R
import com.example.gramclient.domain.orderHistoryScreen.OrderHistoryResult
import com.example.gramclient.domain.orderHistoryScreen.ToAddresse
import com.example.gramclient.presentation.components.CustomTopBar
import com.example.gramclient.presentation.screens.authorization.LoadingIndicator
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

class OrdersHistoryScreen : Screen{
    @Composable
    override fun Content() {
        val viewModel: OrderHistoryViewModel = hiltViewModel()

        var notOrderVisible by remember { mutableStateOf(false) }
        val stateGetOrderHistory = viewModel.stateGetOrderHistory
        val ordersHistory: List<OrderHistoryResult>? =
            stateGetOrderHistory.value.response
        val state = rememberSwipeRefreshState(stateGetOrderHistory.value.isLoading)
        LaunchedEffect(key1 = true) {
            viewModel.getOrderHistory()
        }
        Scaffold(topBar = {
            CustomTopBar(title = "История заказов")
        }) {

            when {
                stateGetOrderHistory.value.isLoading -> {
                    LoadingIndicator(isLoading = true)
                }
            }
            ordersHistory?.let { taxiOrdersNonNull ->
                SwipeRefresh(
                    modifier = Modifier.fillMaxSize(),
                    state = state,
                    onRefresh = {
                        notOrderVisible = false
                        viewModel.getOrderHistory()
                    }
                ) {
                    LazyColumn {
                        items(taxiOrdersNonNull) { item: OrderHistoryResult ->
                            item.to_addresses?.let { toAddress ->
                                item.from_address?.name?.let { fromAddress ->
                                    ListHistoryItem(
                                        status = item.status,
                                        createdAt = item.created_at,
                                        fromAddress = fromAddress,
                                        toAddresse = toAddress
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun ListHistoryItem(
    status: String,
    createdAt:String,
    fromAddress: String,
    toAddresse: List<ToAddresse>,
) {
    val expanded = remember { mutableStateOf(false) }
    Card(
        Modifier
            .padding(top = 15.dp, start = 15.dp, end = 15.dp)
            .fillMaxWidth()
    ) {
        Box(contentAlignment = TopEnd) {
            Column(
                modifier = Modifier
                    .height(140.dp)
                    .fillMaxWidth()
                    .padding(end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 35.dp)
                ) {
                    Text(
                        text = "$createdAt / ",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 25.dp)
                    )
                    Text(
                        text = status,
                        fontSize = 15.sp,
                        color = if (status == "Выполнен") Color(0xFF46C258) else Color(
                            0xFFFF1100
                        )
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.fillMaxWidth(0.1f)) {
                        Image(
                            modifier = Modifier.size(25.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.from_marker),
                            contentDescription = "Logo"
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(start = 10.dp)
                    ) {
                        Text(
                            text = fromAddress, fontWeight = FontWeight.Bold
                        )
//                        Text(text = "Максудчони Танбури улица")
                    }
                }
                toAddresse.forEach {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.fillMaxWidth(0.1f)) {
                            Image(
                                modifier = Modifier.size(25.dp),
                                imageVector = ImageVector.vectorResource(R.drawable.to_marker),
                                contentDescription = "Logo"
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(10.dp)
                        ) {

                            Text(
                                text = it.name, fontWeight = FontWeight.Bold
                            )
//                        Text(text = "Максудчони Танбури улица")
                        }
                    }
                }
            }
            IconButton(
                onClick = {/* expanded.value = true */},
                modifier = Modifier
                    .fillMaxWidth(0.1f)
                    .padding(end = 5.dp)
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_menu_blue),
                    contentDescription = ""
                )
                DropdownMenu(expanded = expanded.value,
                    onDismissRequest = { expanded.value = false }) {
                    DropdownMenuItem(onClick = {}) {
                        Text("Удалить")
                    }
                    DropdownMenuItem(onClick = {}) {
                        Text("Повторить")
                    }
                    DropdownMenuItem(onClick = {}) {
                        Text("Обратный маршрут")
                    }
                }
            }

        }
    }
}