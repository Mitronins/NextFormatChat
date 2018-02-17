package com.addd.nextformatchat.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by addd on 15.02.2018.
 */
class User (
    @SerializedName("username")
    @Expose
    val username: String,
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("first_name")
    @Expose
    val firstName: String,
    @SerializedName("last_name")
    @Expose
    val lastName: String
)