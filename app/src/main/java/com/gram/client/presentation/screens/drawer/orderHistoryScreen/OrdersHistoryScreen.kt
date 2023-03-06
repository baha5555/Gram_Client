package com.gram.client.presentation.screens.drawer.orderHistoryScreen


import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R
import com.gram.client.domain.orderHistory.Data
import com.gram.client.domain.orderHistory.ToAddresse
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*


class OrdersHistoryScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: OrderHistoryViewModel = hiltViewModel()
        val pagingData: Flow<PagingData<Data>> = viewModel.paging(10)
        val lazyPagingItems = pagingData.collectAsLazyPagingItems()
        val response = viewModel.response().collectAsLazyPagingItems()
        val navigator = LocalNavigator.currentOrThrow
        var lastDate by remember { mutableStateOf("") }
        var currentDate by remember { mutableStateOf("") }

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
                                    text = "Мои заказы",
                                    textAlign = TextAlign.Center,
                                    color = Color.Black,
                                    modifier = Modifier.align(Alignment.Center),
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        },
                        backgroundColor = Color(0xFFF4F4F2),
                        contentColor = Color.Black,
                        elevation = 0.dp
                    )
                }
            }, backgroundColor = Color(0xFFF4F4F2)

        )
        {
            LazyColumn {
                items(lazyPagingItems) {
                    it?.let { res ->
                        val dateTimeString = res.created_at
                        val dateTime = remember {
                            LocalDateTime.parse(
                                dateTimeString,
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                            )
                        }
                        val formattedDate =
                            remember { dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) }
                        val formatter =
                            DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH)
                        val date = LocalDate.parse(formattedDate, formatter)
                        val outputFormat =
                            DateTimeFormatter.ofPattern("dd MMMM, EEEE", Locale("ru"))

                        res.from_address?.name?.let { fromAddress ->
                            res.to_addresses?.let { toAddress ->
                                currentDate = date.format(outputFormat)
                                if (currentDate != lastDate) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        modifier = Modifier.padding(start = 24.dp),
                                        text = date.format(outputFormat),
                                        fontSize = 20.sp,
                                        color = Color(0xFF999997),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                res.tariff_id?.let { it1 ->
                                    res.created_at?.let { it2 ->
                                        res.status?.let { it3 ->
                                            ListHistoryItem(
                                                price = it.price.toString(),
                                                status = it3,
                                                createdAt = it2,
                                                fromAddress = fromAddress,
                                                toAddresse = toAddress,
                                                tariff_id = it1,
                                            ){
                                                viewModel.updateSelectedOrder(it)
                                                Log.e("created_at","${res.created_at}")
                                                navigator.push(CardOrderHistory())
                                            }
                                        }
                                    }
                                }

                                lastDate = currentDate
                            }
                        }
                    }
                }
                when (val state = lazyPagingItems.loadState.refresh) { //FIRST LOAD
                    is LoadState.Loading -> { // Loading UI
                        item {
                            Column(
                                modifier = Modifier
                                    .fillParentMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(8.dp),
                                    text = "идёт загрузка истории",
                                    color = Color.Black
                                )
                                CircularProgressIndicator(color = Color.Black)
                            }
                        }
                    }
                    else -> {}
                }

                when (val state = lazyPagingItems.loadState.append) { // Pagination
                    is LoadState.Loading -> { // Pagination Loading UI
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Text(text = "идёт загрузка истории", color = Color.Black)
                                CircularProgressIndicator(color = Color.Black)
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListHistoryItem(
    price: String,
    status: String,
    createdAt: String,
    fromAddress: String,
    toAddresse: List<ToAddresse>,
    tariff_id: Int,
    onClick:()->Unit
    ) {
    val navigator = LocalNavigator.currentOrThrow
    val dateTimeString = createdAt
    val dateTime = remember {
        LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }
    val formattedHour = remember {
        dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
    }
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .padding(horizontal = 3.dp),
        shape = RoundedCornerShape(22),
        elevation = 0.dp,
        onClick = {
            onClick()
        }
    )
    {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.padding(10.dp)) {

                Row(modifier = Modifier.fillMaxWidth(0.8f)) {
                    Text(
                        text = "$price с, ",
                        fontSize = 20.sp,
                        maxLines = 1,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = " $fromAddress,",
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold
                    )
                    toAddresse.forEach {
                        Text(
                            text = " ${it.name}",
                            fontSize = 20.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (status == "Отменен") "Отменено " else "поездка в $formattedHour",
                    color = if (status == "Отменен") Color.Red else Color.Black, fontSize = 18.sp
                )
            }
            if (tariff_id == 4) {
                Icon(
                    modifier = Modifier.size( width = 80.dp, height = 50.dp),
                    painter = painterResource(id = R.drawable.car_business_icon ),
                    contentDescription = "car_econom_icon",
                    tint = Color.Unspecified
                )
            } else {
                Icon(
                    modifier = Modifier.size( 75.dp),
                    painter = painterResource(id = R.drawable.car_econom_icon),
                    contentDescription = "car_econom_icon",
                    tint = Color.Unspecified
                )
            }

        }
    }
}