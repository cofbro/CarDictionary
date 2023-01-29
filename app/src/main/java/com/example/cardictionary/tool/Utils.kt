package com.example.cardictionary.tool

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Vibrator
import android.view.View
import android.view.animation.LinearInterpolator
import org.jsoup.Jsoup

fun String.toNormalString(): String {
    val doc = Jsoup.parse(this)
    return doc.body().text()
}

fun vSimple(context: Context, millisecode: Int) {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    vibrator.vibrate(millisecode.toLong())
}

fun blinkTextAnimator(view: View) {
    ObjectAnimator.ofFloat(view, "alpha", 1f, 0f).apply {
        interpolator = LinearInterpolator()
        duration = 1200
        repeatMode = ObjectAnimator.REVERSE
        repeatCount = ObjectAnimator.INFINITE
        start()
    }
}

