package com.addd.nextformatchat.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by addd on 14.02.2018.
 */
class SendMsg(
        @SerializedName("text")
        @Expose
        val text: String? = null
)
