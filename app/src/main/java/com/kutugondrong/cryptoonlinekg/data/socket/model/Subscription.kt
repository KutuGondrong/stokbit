package com.kutugondrong.cryptoonlinekg.data.socket.model

import com.google.gson.annotations.SerializedName

data class Subscription(
    @SerializedName("action")
    var action: String = "SubAdd",
    @SerializedName("subs")
    var subs: List<String>
)
