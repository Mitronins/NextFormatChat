package com.addd.nextformatchat.network

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.addd.nextformatchat.APP_TOKEN
import com.addd.nextformatchat.MyApp
import com.addd.nextformatchat.model.*
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by addd on 14.02.2018.
 */
object NetworkController {
    private val BASE_URL = "http://176.57.215.171/api/"
    private val api: ChatAPI by lazy { init(MyApp.instance) }
    private fun init(context: Context): ChatAPI {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val okHttpClient = OkHttpClient.Builder()
        val interceptor = Interceptor { chain ->
            val request = chain.request().newBuilder().addHeader("Authorization", "Token " + sp.getString("token", ""))?.build()
            chain.proceed(request)
        }

        okHttpClient.networkInterceptors().add(interceptor)
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(GsonBuilder().create())).client(okHttpClient.build()).build()
        return retrofit.create(ChatAPI::class.java)
    }

    var callbackChats: ChatsCallback? = null
    var callbackMessage: MessagesCallback? = null
    var sendCallback: SendMsg? = null
    var callbackUsers: CallbackUsers? = null

//------------------------------------------запросы---------------------------------------------------


    fun getChats(page: Int, pagination: Boolean) {
        val call = api.getChats(page)
        call.enqueue(object : retrofit2.Callback<MyResultChats> {
            override fun onResponse(call: Call<MyResultChats>?, response: Response<MyResultChats>?) {
                response?.body()?.let {
                    if (response.isSuccessful) {
                        callbackChats?.result(it.chats ?: ArrayList(), true, it.count
                                ?: 0, pagination)
                    } else {
                        callbackChats?.result(ArrayList(), false, 0, pagination)
                    }
                }
            }

            override fun onFailure(call: Call<MyResultChats>?, t: Throwable?) {
                callbackChats?.result(ArrayList(), false, 0, pagination)
            }

        })
    }


    fun getChatMeasseges(page: Int, pagination: Boolean, id: Int) {
        val call = api.getChatMeasseges(id, page)
        call.enqueue(object : retrofit2.Callback<MyResultMessages> {
            override fun onResponse(call: Call<MyResultMessages>?, response: Response<MyResultMessages>?) {
                response?.body()?.let {
                    if (response.isSuccessful) {
                        callbackMessage?.result(it.chats ?: ArrayList(), true, it.count
                                ?: 0, pagination)
                    } else {
                        callbackMessage?.result(ArrayList(), false, 0, pagination)
                    }
                }
            }

            override fun onFailure(call: Call<MyResultMessages>?, t: Throwable?) {
                callbackMessage?.result(ArrayList(), false, 0, pagination)
            }

        })
    }


    fun sendMsg(send: com.addd.nextformatchat.model.SendMsg, id: Int) {
        val call = api.sendMsg(id, send)
        call.enqueue(object : retrofit2.Callback<MyMessage> {
            override fun onResponse(call: Call<MyMessage>?, response: Response<MyMessage>?) {
                response?.body()?.let {
                    if (response.isSuccessful) {
                        sendCallback?.resultSend(it)
                    } else {
                        sendCallback?.resultSend(null)
                    }
                }
            }

            override fun onFailure(call: Call<MyMessage>?, t: Throwable?) {
                sendCallback?.resultSend(null)
            }

        })
    }

    fun getChatUsers(page: Int, pagination: Boolean) {
        val call = api.getUsers(page)
        call.enqueue(object : retrofit2.Callback<MyResultUsers> {
            override fun onResponse(call: Call<MyResultUsers>?, response: Response<MyResultUsers>?) {
                response?.body()?.let {
                    if (response.isSuccessful) {
                        callbackUsers?.resultUsers(it.users ?: ArrayList(), true,it.count ?: 0, pagination)
                    } else {
                        callbackUsers?.resultUsers(ArrayList(), true, 0,pagination)
                    }
                }
            }

            override fun onFailure(call: Call<MyResultUsers>?, t: Throwable?) {
                callbackUsers?.resultUsers(ArrayList(), true, 0, pagination)
            }

        })
    }
    //--------------------------------callbacks------------------------------------------------


    interface ChatsCallback {
        fun result(listChats: ArrayList<Chat>, result: Boolean, count: Int, pagination: Boolean)
    }

    fun registerChatCallback(callback: ChatsCallback?) {
        this.callbackChats = callback
    }

    interface MessagesCallback {
        fun result(listMessage: ArrayList<MyMessage>, result: Boolean, count: Int, pagination: Boolean)
    }

    fun registerMessagesCallback(callback: MessagesCallback?) {
        this.callbackMessage = callback
    }

    interface SendMsg {
        fun resultSend(result: MyMessage?)
    }

    fun registerSendMsgCallback(callback: SendMsg) {
        this.sendCallback = callback
    }

    interface CallbackUsers {
        fun resultUsers(listUsers: ArrayList<User>, result: Boolean, count: Int, pagination: Boolean)
    }

    fun registrationUsersCallback(callbak: CallbackUsers) {
        this.callbackUsers = callbak
    }
}