package com.example.skin

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.collection.ArrayMap
import androidx.core.view.LayoutInflaterCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.lang.reflect.Field
import java.util.*

class ApplicationActivityLifecycle(private val mObservable: Observable) :
    Application.ActivityLifecycleCallbacks {
    private val skinPackageName = "/skinpkg-debug.apk"

    private val mLayoutInflaterFactories = ArrayMap<Activity, SkinLayoutInflaterFactory>()

    @SuppressLint("DiscouragedPrivateApi")
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {


        /**
         * 更新布局视图
         */
        // 获得Activity的布局加载器
        val layoutInflater = activity.layoutInflater
        val mFactory2: Field = LayoutInflater::class.java.getDeclaredField("mFactory2")
        mFactory2.isAccessible = true

        // 使用factory2 设置布局加载工程
        val skinLayoutInflaterFactory = SkinLayoutInflaterFactory()
        // LayoutInflaterCompat.setFactory2(layoutInflater, skinLayoutInflaterFactory)
        mFactory2.set(layoutInflater, skinLayoutInflaterFactory)
        mLayoutInflaterFactories[activity] = skinLayoutInflaterFactory
        mObservable.addObserver(skinLayoutInflaterFactory)
    }



    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {
        val observer: SkinLayoutInflaterFactory = mLayoutInflaterFactories.remove(activity)!!
        SkinManager.getInstance().deleteObserver(observer)
    }


}