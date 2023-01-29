package com.example.skin

import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat

class SkinAttribute {
    companion object {
        val mAttributes = arrayListOf(
            "background",
            "src",
            "textColor",
            "drawableLeft",
            "drawableTop",
            "drawableRight",
            "drawableBottom"
        )
    }

    private val mSkinViews: ArrayList<SkinView> = ArrayList()


    fun look(view: View, attributeSet: AttributeSet) {

        val mSkinPars: ArrayList<SkinPair> = ArrayList()
        for (i in 0 until attributeSet.attributeCount) {
            var resId = 0
            val attributeName = attributeSet.getAttributeName(i)
            if (mAttributes.contains(attributeName)) {
                val attributeValue = attributeSet.getAttributeValue(i)
                if (attributeValue.startsWith("#"))
                    continue
                else if (attributeValue.startsWith("?")) {
                    continue
                } else if (attributeValue.startsWith("@")){
                    resId = attributeValue.substring(1).toInt()
                    Log.d("chy","resource id $resId")
                }
                val skinPair = SkinPair(attributeName, resId)
                mSkinPars.add(skinPair)
            }
        }

        if (mSkinPars.isNotEmpty()) {
            val skinView = SkinView(view, mSkinPars)
            mSkinViews.add(skinView)
        }
    }

    fun applySkin() {
        mSkinViews.forEach {
            it.applySkin()
        }
    }

    class SkinPair(
        //属性名
        var attributeName: String,
        //对应的资源id
        var resId: Int
    )

    class SkinView(
        private val view: View,
        private val skinPairs: List<SkinPair>
    ) {

        fun applySkin() {
            skinPairs.forEach {
                when(it.attributeName){
                    "src" -> {
                        val drawable = SkinResources.getInstance().getDrawable(it.resId)
                        if (view is ImageView) {
                            view.setImageDrawable(drawable)
                        }
                    }
                    "background" -> {
                        val background = SkinResources.getInstance().getBackground(it.resId)
                        if (background is Drawable) {
                            view.background = background
                        } else {
                            view.setBackgroundColor(background as Int)
                        }
                    }
                    "textColor" -> {
                        (view as TextView).setTextColor(SkinResources.getInstance().getColor(it.resId))
                    }
                    "else" -> {}
                }
            }
        }
    }
}