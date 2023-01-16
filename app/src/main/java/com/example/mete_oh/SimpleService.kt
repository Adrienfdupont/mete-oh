package com.example.mete_oh

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface SimpleService {
    @GET("posts")
    suspend fun listAll(): List<Post>

}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl("http://example")
    .build()
object API {
    val retrofitService: SimpleService by lazy {
        retrofit.create(SimpleService::class.java)
    }
}