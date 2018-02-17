package com.addd.nextformatchat.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by addd on 16.02.2018.
 */
class EventAuth {
    @SerializedName("event")
    @Expose
    val event: String? = null
    @SerializedName("data")
    @Expose
    val data: Any? = null
}