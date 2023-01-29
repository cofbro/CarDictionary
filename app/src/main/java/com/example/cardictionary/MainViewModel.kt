package com.example.cardictionary

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.cardictionary.tool.User
import com.tencent.tauth.Tencent

class MainViewModel : ViewModel() {
    private var themeID = -1
    var mTencent: Tencent? = null
    val isLogin = MutableLiveData(false)
    private var user: User? = null

    companion object {
        private var INSTANCE: MainViewModel? = null
        fun getInstance(): MainViewModel {
            if (INSTANCE != null) {
                return INSTANCE!!
            } else {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = MainViewModel()
                    }
                }
            }
            return INSTANCE!!
        }
    }


    fun setCurrentUser(user: User?) {
        isLogin.postValue(true)
        this.user = user
    }

    fun getCurrentUser(): User? {
        return user
    }

    fun clearCurrentUser() {
        isLogin.postValue(false)
        setCurrentUser(null)
    }

    fun bindUserInfo(context: Context, headerLayout: View) {
        val nameView = headerLayout.findViewById<TextView>(R.id.mainName)
        val avatarView = headerLayout.findViewById<ImageView>(R.id.mainAvatar)
        if (user != null) {
            nameView.text = user!!.username
            Glide.with(context)
                .load(user!!.userAvatar)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(avatarView)
        } else {
            nameView.text = "未登录"
            avatarView.setImageResource(R.mipmap.ic_launcher)
        }
    }

    fun setThemeId(id: Int) {
        themeID = id
    }

    fun getThemeId(): Int {
        return themeID
    }
}