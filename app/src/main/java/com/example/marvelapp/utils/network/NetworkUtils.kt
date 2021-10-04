package com.example.marvelapp.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.marvelapp.utils.constants.Constants
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class NetworkUtils: INetworkUtils, KoinComponent {

    private val context: Context by inject()

    override fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val connection = connectivityManager.getNetworkCapabilities(network)
        return connection != null && (connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || connection.hasTransport(
            NetworkCapabilities.TRANSPORT_CELLULAR
        ))
    }
    override fun isInternetAvailable(): Boolean {
        return try {
            val sockTestAddress = InetSocketAddress(Constants.TEST_SOCKET_ADDRESS, Constants.TEST_SOCKET_PORT)
            with(Socket()) {
                this.connect(sockTestAddress, Constants.TEST_SOCKET_TIMEOUT)
                this.close()
                true
            }
        } catch (e: IOException) {
            false
        }
    }
}