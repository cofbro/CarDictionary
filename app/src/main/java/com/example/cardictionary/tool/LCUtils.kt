package com.example.cardictionary.tool

import android.content.Context
import android.util.Log
import android.widget.Toast
import cn.leancloud.LCObject
import cn.leancloud.LCQuery
import cn.leancloud.LCUser
import com.example.cardictionary.data.adapter.MessageListAdapter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class LCUtils {

    // 注册
    fun registerUserUsedLC(context: Context, username: String, password: String, email: String) {
        val user = LCUser()
        user.username = username
        user.password = password
        user.email = email
        user.signUpInBackground().subscribe(object : Observer<LCUser> {
            override fun onSubscribe(d: Disposable) {}

            override fun onNext(t: LCUser) {
                Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show()
            }

            override fun onError(e: Throwable) {
                Toast.makeText(context, "用户名已被占用，请重试", Toast.LENGTH_SHORT).show()
            }

            override fun onComplete() {}
        })
    }

    // 登录
    fun loginInUsedLC(context: Context, user: User, afterLogin: () -> Unit = {}) {
        LCUser.logIn(user.username, user.password).subscribe(object : Observer<LCUser> {
            override fun onSubscribe(d: Disposable) {}

            override fun onNext(t: LCUser) {
                afterLogin()
            }

            override fun onError(e: Throwable) {
                Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show()
            }

            override fun onComplete() {}
        })
    }

    // 第三方 QQ登录
    fun loginInUsedQQ(context: Context, expires_in: Int, openid: String, access_token: String) {
        val thirdPartyData: MutableMap<String, Any> = HashMap()
        thirdPartyData["expires_in"] = expires_in
        thirdPartyData["openid"] = openid
        thirdPartyData["access_token"] = access_token
        LCUser.loginWithAuthData(thirdPartyData, "qq").subscribe(object : Observer<LCUser> {
            override fun onSubscribe(d: Disposable) {}

            override fun onNext(t: LCUser) {
                Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show()
            }

            override fun onError(e: Throwable) {
                Toast.makeText(context, "登录失败" + e.message, Toast.LENGTH_SHORT).show()
            }

            override fun onComplete() {}
        })
    }

    // 储存评论
    fun sendComments(user: User, message: String, successCallback: () -> Unit) {
        val o = LCObject("Message")
        o.put("username", user.username)
        o.put("message", message)
        o.put("avatar", user.userAvatar)
        o.put("like", "0")
        saveCommentInBackground(o, successCallback)
    }

    // 点赞评论
    fun likeTheComment(id: String, currentLikes: String, successfully: () -> Unit) {
        val like = currentLikes.toInt() + 1
        updateCommentUsedId(id, like.toString()) {
            successfully()
        }
    }

    //取消点赞
    fun cancelLike(id: String, currentLikes: String, successfully: () -> Unit) {
        val like = currentLikes.toInt() - 1
        updateCommentUsedId(id, like.toString()) {
            successfully()
        }
    }

    // 加载评论
    fun getAllCommentsObject(successCallback: (ArrayList<MessageListAdapter.MessagePair>) -> Unit = {}) {
        val query = LCQuery<LCObject>("Message")
        query.selectKeys(listOf("message", "username", "avatar", "like"))
        query.findInBackground().subscribe(object : Observer<List<LCObject>> {
            override fun onSubscribe(d: Disposable) {}

            override fun onNext(t: List<LCObject>) {
                val messagePairList = arrayListOf<MessageListAdapter.MessagePair>()
                t.forEach {
                    val msg = it.get("message").toString()
                    val like = it.get("like").toString()
                    val username = it.get("username").toString()
                    val avatar = it.get("avatar").toString()
                    val user = UserWithNoPsd(username, avatar)
                    val id = it.objectId
                    messagePairList.add(MessageListAdapter.MessagePair(user, msg, like, id))
                }
                successCallback(messagePairList)
            }

            override fun onError(e: Throwable) {}

            override fun onComplete() {}
        })
    }

    // 储存评论
    private fun saveCommentInBackground(o: LCObject, successCallback: () -> Unit) {
        o.saveInBackground().subscribe(object : Observer<LCObject> {
            override fun onSubscribe(disposable: Disposable) {}
            override fun onNext(todo: LCObject) {
                successCallback()
            }

            override fun onError(throwable: Throwable) {}

            override fun onComplete() {}
        })
    }

    // 根据 ID 加载特定评论
    private fun updateCommentUsedId(id: String, like: String, successfully: () -> Unit = {}) {
        val o = LCObject.createWithoutData("Message", id)
        o.put("like", like)
        o.saveInBackground().subscribe(object : Observer<LCObject> {
            override fun onSubscribe(d: Disposable) {}

            override fun onNext(t: LCObject) {
                successfully()
            }

            override fun onError(e: Throwable) {}

            override fun onComplete() {}
        })
    }
}