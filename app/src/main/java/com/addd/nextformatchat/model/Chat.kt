package com.addd.nextformatchat.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by addd on 14.02.2018.
 */
class Chat {
    @SerializedName("name")
    @Expose
    val name: String? = null
    @SerializedName("id")
    @Expose
    val id: Int? = null
    @SerializedName("users")
    @Expose
    val users: ArrayList<User>? = null
    @SerializedName("last_date")
    @Expose
    val last_date: String? = null
    @SerializedName("last_message")
    @Expose
    val lastMessage: MyMessage? = null
}