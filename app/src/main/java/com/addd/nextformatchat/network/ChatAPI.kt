package com.addd.nextformatchat.network

import com.addd.nextformatchat.model.*
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by addd on 14.02.2018.
 */
interface ChatAPI {
    @GET("chats/")
    fun getChats(@Query("page") page: Int): Call<MyResultChats>

    @GET("chats/{id}/messages")
    fun getChatMeasseges(@Path("id") id : Int, @Query("page") page: Int): Call<MyResultMessages>

    @POST("chats/{id}/send_message/")
    fun sendMsg(@Path("id") id : Int, @Body send: SendMsg): Call<MyMessage>

    @GET("users/")
    fun getUsers(@Query("page") page: Int): Call<MyResultUsers>

}