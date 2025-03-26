package com.iqoid.pairgoth.client.network

import android.content.Context
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.iqoid.pairgoth.TournamentActivity.Companion.BASE_URL_KEY
import com.iqoid.pairgoth.TournamentActivity.Companion.PREFS_NAME
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    private var baseUrl: String = "http://dummy/api/"

    @Volatile
    private var retrofit: Retrofit? = null

    @Volatile
    private var pairGothApiServiceInstance: PairGothApiService? = null

    private val lock = Any()

    val pairGothApiService: PairGothApiService
        get() {
            return pairGothApiServiceInstance ?: synchronized(lock) {
                pairGothApiServiceInstance ?: createRetrofitClient().also {
                    pairGothApiServiceInstance = it
                }
            }
        }

    private fun createRetrofitClient(): PairGothApiService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val acceptHeaderInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .build()
            chain.proceed(request)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(acceptHeaderInterceptor)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit!!.create(PairGothApiService::class.java)
    }

    fun updateBaseUrl(newBaseUrl: String) {
        synchronized(lock) {
            baseUrl = newBaseUrl
            pairGothApiServiceInstance = createRetrofitClient()
        }
    }

    fun initializeBaseUrl(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val savedBaseUrl = prefs.getString(BASE_URL_KEY, null)
        if (savedBaseUrl != null) {
            baseUrl = savedBaseUrl
        }
    }
}