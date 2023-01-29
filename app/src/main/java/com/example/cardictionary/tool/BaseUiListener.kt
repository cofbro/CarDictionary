package com.example.cardictionary.tool

import android.util.Log
import com.tencent.tauth.IUiListener
import com.tencent.tauth.UiError


open class BaseUiListener : IUiListener {
    private val TAG = "chy"
    override fun onComplete(p0: Any?) {
        Log.d(TAG, "onComplete: ${p0.toString()}")
    }

    override fun onError(p0: UiError?) {
        Log.d(TAG, "onError: ")
    }

    override fun onCancel() {
        Log.d(TAG, "onCancel: ")
    }

    override fun onWarning(p0: Int) {
        Log.d(TAG, "onWarning: ")
    }


}

class LoginInfoBean(
    val access_token: String,
    val openid: String,
    val expires_in: Int
)

class UserInfoBean(
    val nickname: String,
    val figureurl_qq_2: String
)