package com.example.skin

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import java.lang.reflect.Constructor
import java.util.*

class SkinLayoutInflaterFactory : LayoutInflater.Factory2, Observer {
    companion object {
        private val mClassPrefixList = arrayOf(
            "android.widget.",
            "android.webkit.",
            "android.app.",
            "android.view."
        )

        //记录对应view的构造函数
        private val mConstructorSignature: Array<Class<*>> = arrayOf(
            Context::class.java, AttributeSet::class.java
        )

        private val mConstructorMap = HashMap<String, Constructor<out View>>()

        private val skinAttribute = SkinAttribute()
    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        //是sdk中的
        val view = if (name.indexOf(".") == -1) {
            createSDKView(context, name, attrs)
        } else {
            createView(context, name, attrs)
        }
        //收集每个view和它的属性
        if (view != null) {
            skinAttribute.look(view, attrs)
        }
        return view
    }

    private fun createView(context: Context, name: String, attrs: AttributeSet): View? {
        val constructor = findConstructor(context, name)
        return constructor?.newInstance(context, attrs)
    }

    private fun createSDKView(context: Context, name: String, attrs: AttributeSet): View? {
        mClassPrefixList.forEach {
            val view = createView(context, it + name, attrs)
            if (view != null)
                return view
        }
        return null
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return null
    }


    private fun findConstructor(context: Context, name: String): Constructor<out View>? {
        var constructor: Constructor<out View>? = mConstructorMap[name]
        if (constructor == null) {
            try {
                val clazz = context.classLoader.loadClass(name).asSubclass(View::class.java)
                constructor = clazz.getConstructor(*mConstructorSignature)
                mConstructorMap[name] = constructor
            } catch (e: Exception) {
            }
        }
        return constructor
    }

    override fun update(o: Observable?, arg: Any?) {
        Log.d("chy","update")
        skinAttribute.applySkin()
    }
}