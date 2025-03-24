package com.iqoid.pairgoth.client.network

import android.content.Context
import android.preference.PreferenceManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    private var baseUrl: String = "http://192.168.0.138:8080/api/"

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

        val acceptHeaderInterceptor = object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request().newBuilder()
                    .addHeader("Accept", "application/json")
                    .build()
                return chain.proceed(request)
            }
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
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val savedBaseUrl = sharedPreferences.getString("api-base-url", null)
        if (savedBaseUrl != null) {
            baseUrl = savedBaseUrl
        }
    }
}
