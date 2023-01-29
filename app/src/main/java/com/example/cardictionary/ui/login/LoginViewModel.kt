package com.example.cardictionary.ui.login

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.cardictionary.MainViewModel
import com.example.cardictionary.databinding.FragmentLoginBinding
import com.example.cardictionary.tool.*
import com.google.gson.Gson
import com.tencent.connect.UserInfo
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(app: Application) : AndroidViewModel(app) {
    private val lcUtils = LCUtils()
    private val mainViewModel = MainViewModel.getInstance()
    private lateinit var userInfoBean: UserInfoBean

    fun loginNormalAccount(context: Context, binding: FragmentLoginBinding) {
        val username = binding.userNameView.text.toString()
        val password = binding.passwordEditText.text.toString()
        viewModelScope.launch(Dispatchers.IO) {
            val user = makeUser(username, null, password)
            lcUtils.loginInUsedLC(context, user) {
                Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show()
                mainViewModel.setCurrentUser(user)
                binding.root.findNavController().navigateUp()
                saveUserIntoLocal(context, user)
            }
        }
    }

    fun loginWithQQAccount(activity: Activity, tencent: Tencent) {
        val iUiListener = object : BaseUiListener() {
            override fun onComplete(p0: Any?) {
                super.onComplete(p0)
                val json = p0.toString()
                val gson = Gson().fromJson(json, LoginInfoBean::class.java)
                loginWithQQAccountStep2(
                    activity,
                    gson.expires_in,
                    gson.openid,
                    gson.access_token,
                    tencent
                ) {
                    viewModelScope.launch(Dispatchers.Main) {
                        val user = makeUser(userInfoBean.nickname, userInfoBean.figureurl_qq_2, null)
                        mainViewModel.setCurrentUser(user)
                        mainViewModel.mTencent = tencent
                    }
                }
            }

            override fun onError(p0: UiError?) {
                Toast.makeText(activity, "登录失败" + p0?.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
        loginWithQQAccountStep1(activity, tencent, iUiListener)
    }

    private fun loginWithQQAccountStep1(
        activity: Activity,
        tencent: Tencent,
        iUiListener: BaseUiListener
    ): Int {
        val scope = "get_simple_userinfo"
        return tencent.login(activity, scope, iUiListener)
    }

    private fun loginWithQQAccountStep2(
        context: Context,
        expires_in: Int,
        openid: String,
        access_token: String,
        tencent: Tencent,
        updateUICallback: () -> Unit = {}
    ) {
        lcUtils.loginInUsedQQ(context, expires_in, openid, access_token)
        tencent.qqToken.apply {
            setAccessToken(access_token, expires_in.toString())
            openId = openid
        }
        val userInfo = UserInfo(context, tencent.qqToken)
        userInfo.getUserInfo(object : BaseUiListener(){
            override fun onComplete(p0: Any?) {
                userInfoBean = Gson().fromJson(p0.toString(), UserInfoBean::class.java)
                updateUICallback()
            }
        })
    }

    fun makeUser(username: String, avatar: String?, psd: String?): User {
        if (psd != null) {
            return User(
                username,
                psd,
                "http://lc-nMBBbObD.cn-n1.lcfile.com/URcHCFIwzNsSXEXyhpj9fvKtvMtewtM1/%E7%94%B7%E5%AD%A9.png"
            )
        }
        return User(username, "000000", avatar!!)
    }


}