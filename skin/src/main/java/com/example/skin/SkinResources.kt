package com.example.skin

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.Log

class SkinResources(context: Context) {

    companion object {
        private var instance: SkinResources? = null
        fun getInstance(): SkinResources {
            return instance!!
        }

        fun init(context: Context): SkinResources {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = SkinResources(context)
                    }
                }
            }
            return instance!!
        }
    }

    private lateinit var mSkinPkgName: String
    private var mSkinResources: Resources? = null
    private var mAppResources: Resources = context.resources
    private var isDefaultSkin = true

    fun resetSkin() {
        mSkinResources = null
        mSkinPkgName = ""
        isDefaultSkin = true
    }

    fun setSkin(resources: Resources?, pkgName: String) {
        Log.d("chy","coming")
        mSkinResources = resources
        mSkinPkgName = pkgName
        //是否使用默认皮肤
        isDefaultSkin = TextUtils.isEmpty(pkgName) || resources == null
    }

    private fun getIdentifier(resId: Int): Int {
        if (isDefaultSkin || mSkinResources == null) {
            return resId
        }
        val attributeEntryName = mAppResources.getResourceEntryName(resId)
        val attributeTypeName = mAppResources.getResourceTypeName(resId)
        Log.d("chy","attributeEntryName: $attributeEntryName , attributeTypeName is : $attributeTypeName")
        Log.d("chy","mSkinResources is : $mSkinResources")
        return mSkinResources!!.getIdentifier(attributeEntryName, attributeTypeName, mSkinPkgName)
    }


    fun getColor(resId: Int):Int {
        if (isDefaultSkin) {
            return mAppResources.getColor(resId, null)
        }
        val skinId = getIdentifier(resId)
        return if (skinId == 0) {
            mAppResources.getColor(resId, null)
        } else mSkinResources!!.getColor(skinId, null)
    }

    fun getResourceId(originResId: Int): Int {
        return getIdentifier(originResId)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getDrawable(resId: Int): Drawable {
        if (isDefaultSkin) {
            return mAppResources.getDrawable(resId)
        }
        val skinId = getIdentifier(resId)
        Log.d("chy","skinID is :$skinId")
        return if(skinId == 0) {
            mAppResources.getDrawable(resId)
        } else {
            mSkinResources!!.getDrawable(skinId)
        }
    }

    fun getBackground(resId: Int): Any {
        val attributeTypeName = mAppResources.getResourceTypeName(resId)
        return if (attributeTypeName.equals("color")) {
            getColor(resId)
        } else {
            getDrawable(resId)
        }
    }
}