package com.kutugondrong.cryptoonlinekg.preference

import android.content.Context
import android.content.SharedPreferences

class LocalPreferences(mContext: Context, name: String) {

    private val sharedPreference: SharedPreferences = mContext.getSharedPreferences(name, Context.MODE_PRIVATE)

    var isLogin: Boolean
        get() = sharedPreference.getBoolean(PrefKey.ISLOGIN, false)
        set(isLogin) = sharedPreference.edit().putBoolean(PrefKey.ISLOGIN, isLogin).apply()

}

