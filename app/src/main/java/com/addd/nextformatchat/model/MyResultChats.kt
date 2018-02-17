package com.addd.nextformatchat.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by addd on 14.02.2018.
 */
class MyResultChats {
    @SerializedName("count")
    @Expose
    val count: Int? = null
    @SerializedName("results")
    @Expose
    val chats: ArrayList<Chat>? = null
}