package com.addd.nextformatchat.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by addd on 17.02.2018.
 */
class MyResultUsers {
    @SerializedName("count")
    @Expose
    val count: Int? = null
    @SerializedName("results")
    @Expose
    val users: ArrayList<User>? = null
    @SerializedName("next")
    @Expose
    val next: String? = null

}