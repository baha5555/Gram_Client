@file:Suppress("CAST_NEVER_SUCCEEDS")

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.gram.client.app.preference.CustomPreference
import com.gram.client.domain.socket.EditOrderSocketResponse
import com.gram.client.presentation.screens.order.OrderExecutionViewModel
import com.gram.client.utils.Constants.URL
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException
import java.util.Collections.singletonMap

object SocketHandler {

    lateinit var mSocket: Socket
    private var gson = Gson()


    @Synchronized
    fun setSocket(accessToken: String, orderExecutionViewModel: OrderExecutionViewModel) {
        try {
            val options = IO.Options.builder()
                .setAuth(singletonMap("token", accessToken))
                .setTransports(arrayOf("websocket"))
                .build()

            mSocket = IO.socket(URL+"client_gram", options)
            mSocket.connect()

            mSocket.on(Socket.EVENT_CONNECT) { args ->
                Log.d("SocketIOCONNECT", "Socket connected")
                Log.d("SocketIOCONNECT", "Socket connected: ")
            }


            mSocket.on("client-mob-app:orders"){args->
                try {
                    Log.e("SocketIODATA","SocketIODATA ${args[0]}")
                    var model: EditOrderSocketResponse = gson.fromJson(args[0].toString(), EditOrderSocketResponse::class.java)
                    //orderExecutionViewModel.addActiveOrder(model.data)
                    orderExecutionViewModel.getActiveOrders()
                }catch (_ : Exception){

                }
            }
            mSocket.on("client-mob-app:cancel-orders"){args->
                Log.e("SocketIODATA","cancelSocketIODATA ${args[0]}")
                orderExecutionViewModel.getActiveOrders()

            }
            mSocket.on("client-mob-app:filing-time"){args->
                Log.e("SocketIODATA","cancelSocketIODATA ${args[0]}")
                orderExecutionViewModel.getActiveOrders()

            }
            mSocket.on("client-mob-app:complete-orders"){args->
                Log.e("SocketIODATA","completeSocketIODATA ${args[0]}")
                orderExecutionViewModel.getActiveOrders()
            }


            mSocket.on(Socket.EVENT_CONNECT_ERROR) { args ->
                mSocket = IO.socket("https://testapi.client.gram.tj/client_gram", options)
                Log.d("SocketIO", "Socket connection error: ${args[0]}")
            }
            mSocket.on(Socket.EVENT_DISCONNECT) { args ->
                Log.d("SocketIO", "Socket disconnected")
            }

        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun establishConnection(accessToken: String) {
        //setSocket(ACCESS_TOKEN)
        mSocket.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }
}

/*
val socket = IO.socket("https://driverapi.gram.tj/traffic_gram", options)
*/
fun Connect(orderExecutionViewModel: OrderExecutionViewModel, context: Context) {
    val customPreference = CustomPreference(context).getSocketAccessToken()
    Log.i("tokenSocket", customPreference)
    SocketHandler.setSocket(
        customPreference,
        orderExecutionViewModel
    )
}