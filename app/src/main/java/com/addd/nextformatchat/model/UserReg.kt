package com.addd.nextformatchat.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by addd on 14.02.2018.
 */
class UserReg(
    @SerializedName("username")
    @Expose
    val login: String,
    @SerializedName("password")
    @Expose
    val password: String,
    @SerializedName("first_name")
    @Expose
    val firstName: String,
    @SerializedName("last_name")
    @Expose
    val lastName: String
    )