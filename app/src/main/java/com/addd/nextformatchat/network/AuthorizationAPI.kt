package com.addd.nextformatchat.network

import com.addd.nextformatchat.model.Authorization
import com.addd.nextformatchat.model.UserReg
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by addd on 14.02.2018.
 */
interface AuthorizationAPI {
    @FormUrlEncoded
    @POST("login/")
    fun authorization(@Field("username") login: String, @Field("password") password: String): Call<Authorization>

    @POST("register/")
    fun registration(@Body text: UserReg): Call<Authorization>

    companion object Factory {

        fun create(): AuthorizationAPI {
            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://176.57.215.171/api/")
                    .build()

            return retrofit.create(AuthorizationAPI::class.java)
        }
    }
}