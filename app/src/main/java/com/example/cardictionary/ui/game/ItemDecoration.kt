package com.example.cardictionary.ui.game

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.example.cardictionary.R
import com.example.cardictionary.data.adapter.SlideCardAdapter

class ItemDecoration(
    private val context: Context,
    private val gameQuestionViewModel: GameQuestionViewModel
) : RecyclerView.ItemDecoration() {
    private var hasAlreadyAdd = false
    private var rotate = 0f
    private lateinit var rectF: RectF
    private val bgPaint = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = Color.parseColor("#F4F4F4")
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.FILL_AND_STROKE
        setShadowLayer(40f, 0f, 0f, Color.parseColor("#C2C1C1"))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val matrix = Matrix()
        val top = parent.height - 200f
        val left = parent.width / 2f
        matrix.setRotate(rotate, left, top)
        c.setMatrix(matrix)
        c.drawCircle(left, top, 80f, bgPaint)
        val bitmap = createBitmap(context.resources.getDrawable(R.drawable.refresh, null))
        c.drawBitmap(bitmap!!, left - 38, top - 38, null)
        rectF = RectF(left - 80, top - 80, left + 80, top + 80)
        if (!hasAlreadyAdd) {
            parent.addOnItemTouchListener(ItemTouchListener(rectF, this))
            hasAlreadyAdd= true
        }
    }

    fun click(parent: RecyclerView) {
        gameQuestionViewModel.setJokeContent { data ->
            (parent.adapter as SlideCardAdapter).setDataJokePair(data)
        }
        ValueAnimator.ofFloat(0f,1080f).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = 1500
            addUpdateListener {
                rotate = it.animatedValue as Float
                parent.invalidateItemDecorations()
            }
            start()
        }
    }

    private fun createBitmap(drawable: Drawable?): Bitmap? {
        if (drawable != null) {
            val b = androidx.core.graphics.createBitmap(
                75,
                75,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(b)
            // 重点 这个坑一定要记住
            /**
             * 坑！！！！！！！！！
             * 坑！！！！！！！！！
             * 坑！！！！！！！！！
             */
            drawable.bounds = Rect(0, 0, 75, 75)
            drawable.draw(canvas)
            return b
        }
        return null
    }
}