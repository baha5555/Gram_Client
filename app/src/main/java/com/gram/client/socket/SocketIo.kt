@file:Suppress("CAST_NEVER_SUCCEEDS")

import android.util.Log
import com.google.gson.Gson
import com.gram.client.domain.orderExecutionScreen.active.AllActiveOrdersResult
import com.gram.client.domain.socket.EditOrderSocketResponse
import com.gram.client.presentation.screens.order.OrderExecutionViewModel
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException
import java.util.Collections.singletonMap

object SocketHandler {

    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket(accessToken: String, orderExecutionViewModel: OrderExecutionViewModel) {
        try {
            val options = IO.Options.builder()
                .setAuth(singletonMap("token", accessToken))
                .setTransports(arrayOf("websocket"))
                .build()

            mSocket = IO.socket("https://testapi.client.gram.tj/client_gram", options)
            mSocket.connect()




            mSocket.on("client-mob-app:orders"){args->
                try {
                    Log.e("SocketIODATA","${args[0]}")
                    var gson = Gson()
                    var model: EditOrderSocketResponse = gson.fromJson(args[0].toString(), EditOrderSocketResponse::class.java)
                    Log.e("SocketIODATA","${model.data.id}")
                    orderExecutionViewModel.getActiveOrders()
                }catch (_ : Exception){

                }
            }
            mSocket.on("client-mob-app:cancel-orders"){args->
                Log.e("SocketIODATA","${args[0]}")

            }
            mSocket.on("client-mob-app:complete-orders"){args->
                Log.e("SocketIODATA","${args[0]}")
            }

            mSocket.on(Socket.EVENT_CONNECT) { args ->
                Log.d("SocketIO", "Socket connected")
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
