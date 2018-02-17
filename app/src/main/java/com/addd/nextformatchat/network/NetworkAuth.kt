package com.addd.nextformatchat.network

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.addd.nextformatchat.MyApp
import com.addd.nextformatchat.model.Authorization
import com.addd.nextformatchat.model.UserReg
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by addd on 14.02.2018.
 */
object NetworkAuth {
    private val serviceAPI = AuthorizationAPI.Factory.create()
    private val APP_TOKEN = "token"
    var callback: MyCallback? = null
    var callbackRegistration: RegistrationCallback? = null

    fun authorization(login : String, pass : String) {
        val call = serviceAPI.authorization(login, pass)

        call.enqueue(object : Callback<Authorization> {
            override fun onResponse(call: Call<Authorization>?, response: Response<Authorization>?) {
                response?.let {
                    when {
                        response.code() != 200 -> callback?.resultAuth(400)
                        response.code() == 200 -> {
                            val mSettings: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApp.instance)
                            val editor: SharedPreferences.Editor = mSettings.edit()
                            editor.putString(APP_TOKEN, response.body()?.token)
                            editor.putInt(com.addd.nextformatchat.ID_USER, response.body()?.id ?: 0)
                            editor.apply()
                            callback?.resultAuth(200)

                        }
                        else -> {
                        }
                    }
                }
            }

            override fun onFailure(call: Call<Authorization>?, t: Throwable?) {
                callback?.resultAuth(500)
            }
        })
    }

    fun registration(userReg: UserReg) {
        val call = serviceAPI.registration(userReg)
        call.enqueue(object : retrofit2.Callback<Authorization> {
            override fun onResponse(call: Call<Authorization>?, response: Response<Authorization>?) {
                response?.body()?.let {
                    if (response.isSuccessful) {
                        val mSettings: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApp.instance)
                        val editor: SharedPreferences.Editor = mSettings.edit()
                        editor.putString(com.addd.nextformatchat.APP_TOKEN, response.body()?.token)
                        editor.putInt(com.addd.nextformatchat.ID_USER, response.body()?.id ?: 0)
                        editor.apply()
                        callbackRegistration?.resultReg(true)

                    } else {
                        callbackRegistration?.resultReg(false)
                    }
                }
            }

            override fun onFailure(call: Call<Authorization>?, t: Throwable?) {
                        callbackRegistration?.resultReg(false)
            }

        })
    }

    interface MyCallback {
        fun resultAuth(result: Int)
    }

    fun registerCallback(callback: MyCallback?) {
        this.callback = callback
    }

    interface RegistrationCallback {
        fun resultReg(result: Boolean)
    }

    fun registerCallbackReg(callback: RegistrationCallback?) {
        this.callbackRegistration = callback
    }
}