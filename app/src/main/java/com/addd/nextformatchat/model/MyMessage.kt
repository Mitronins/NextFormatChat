package com.addd.nextformatchat.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by addd on 14.02.2018.
 */
class MyMessage(
        @SerializedName("id")
        @Expose
        val id: Int,
        @SerializedName("auto_date")
        @Expose
        val date: String,
        @SerializedName("text")
        @Expose
        val text: String,
        @SerializedName("user")
        @Expose
        val user: User,
        @SerializedName("chat")
        @Expose
        val idChat: Int
)