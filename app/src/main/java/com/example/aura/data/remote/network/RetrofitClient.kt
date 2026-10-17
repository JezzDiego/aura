package com.example.aura.data.remote.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Cliente HTTP preparado para a futura API do Entrada Livre.
 * Por enquanto o app funciona 100% offline e nenhum repositório faz chamadas remotas.
 */
object RetrofitClient {

    private const val BASE_URL = "https://api.entradalivre.app/v1/"

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}


