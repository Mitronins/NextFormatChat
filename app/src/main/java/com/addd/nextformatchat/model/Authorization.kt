package com.addd.nextformatchat.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by addd on 14.02.2018.
 */
class Authorization {
    @SerializedName("token")
    @Expose
    val token: String? = null
    @SerializedName("id")
    @Expose
    val id: Int? = null
}