package com.example.cardictionary.ui.choice

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import com.example.cardictionary.R
import kotlin.math.abs


class ChoiceItem : View {
    private var iconDrawable: Drawable? = null

    // xml 中设置
    private var itemNum = -1

    // adapter 中设置
    private var answerNum = -2
    var answerCallback: ((Boolean) -> Unit)? = null
    private var answer = false
    private var perText = ""
    private var lineWidth = 0f
    private val p: ArrayList<String> = arrayListOf()
    private var currentScale = 1f
        set(value) {
            field = value
            invalidate()
        }
    private var flag = false
    private var question: String? = ""
    private var clickQRectF = RectF()
    private var isMultiply = false
    private lateinit var bgPaint: Paint
    private lateinit var textPaint: Paint
    private val textBounds = Rect()
    private var textBaseline = 0f
    private val DEFAULT_PADDING = 30f
    private var DEFAULT_OFFSET_X = 10f
    private var DEFAULT_OFFSET_Y = 0f
    private lateinit var icon: Bitmap

    constructor(context: Context?) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun init(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChoiceItem)
        question = typedArray.getString(R.styleable.ChoiceItem_firstQ)
        isMultiply = typedArray.getBoolean(R.styleable.ChoiceItem_multiply, false)
        itemNum = typedArray.getInt(R.styleable.ChoiceItem_itemNumber, -1)
        iconDrawable = typedArray.getDrawable(R.styleable.ChoiceItem_nameIcon)
            ?: throw RuntimeException("the attribution is missing")
        changeSVGColor("#ff000000")
        typedArray.recycle()

        bgPaint = getPaint(Color.parseColor("#ddf5f5f5"))
        textPaint = getPaint(Color.BLACK).apply {
            textSize = 40f
        }
    }

    private fun computeItemNum() {
        answer = answerNum == itemNum
    }

    private fun getPaint(mColor: Int): Paint {
        return Paint().apply {
            color = mColor
            isDither = true
            isAntiAlias = true
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }
    }

    fun setQuestion(QuestionStr: String) {
        question = QuestionStr
        invalidate()
    }

    fun setAnswer(num: Int) {
        answerNum = num
        computeItemNum()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        DEFAULT_OFFSET_Y = height / 2f
        if (question != null) {
            //drawTextInRoundRect(null, question!!, icon)
            question!!.forEach {
                val measuredTextWidth = textPaint.measureText(it.toString())
                if (measuredTextWidth + lineWidth >= width - 3 * DEFAULT_OFFSET_X - icon.width) {
                    lineWidth = 0f
                    p.add(perText)
                    perText = ""
                }
                lineWidth += measuredTextWidth
                perText += it
            }
            p.add(perText)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            canvas.scale(currentScale, currentScale, width / 2f, height / 2f)
            if (question != null) {
                drawTextInRoundRect(canvas, question!!, icon)
            }

        }
    }


    private fun drawTextInRoundRect(canvas: Canvas?, text: String, icon: Bitmap) {

        textPaint.getTextBounds(text, 0, text.length, textBounds)
        textBaseline = getTextBaseline(textPaint, textBounds.height().toFloat())

        // 画背景
        var right = DEFAULT_OFFSET_X + textBounds.width().toFloat() + DEFAULT_PADDING + icon.width
        if (right > width - 10f) {
            right = width - 10f
        }
        canvas?.drawRoundRect(
            DEFAULT_OFFSET_X,
            0f,
            right + 2 * DEFAULT_OFFSET_X,
            textBounds.height().toFloat() * p.size + 2 * DEFAULT_PADDING,
            20f,
            20f,
            bgPaint
        )
        // 保存当前的 RectF 区域
        clickQRectF.left = 2 * DEFAULT_OFFSET_X
        clickQRectF.top = 0f
        clickQRectF.right = right + 2 * DEFAULT_OFFSET_X
        clickQRectF.bottom =
            textBounds.height().toFloat() * p.size + 2 * DEFAULT_PADDING

        // 画 A B C D 选项字母
        canvas?.drawBitmap(
            icon,
            2 * DEFAULT_OFFSET_X,
            (clickQRectF.bottom - clickQRectF.top - icon.height) / 2f,
            null
        )

        // 画文字
        var lineHeight = 0f
        for (i in 0 until p.size) {
            canvas?.drawText(
                p[i],
                0,
                p[i].length,
                3 * DEFAULT_OFFSET_X + icon.width,
                textBaseline + lineHeight,
                textPaint
            )
            lineHeight += textBounds.height()
        }

    }

    private fun getTextBaseline(paint: Paint, containerHeight: Float): Float {
        val fontMetrics = paint.fontMetrics
        val bottom = fontMetrics.bottom
        val top = fontMetrics.top
        return containerHeight / 2f + (bottom + abs(top)) / 2f - bottom + DEFAULT_PADDING
    }

    private fun createBitmap(drawable: Drawable): Bitmap {

        val b = androidx.core.graphics.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(b)
        drawable.bounds = Rect(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return b

    }

    private fun setScaleAnimator(): ObjectAnimator {
        val objectAnimator = ObjectAnimator.ofFloat(this, "currentScale", 0f).apply {
            duration = 300
            if (isRunning) {
                setFloatValues(1f, currentScale)
            } else {
                setFloatValues(1f, 0.9f)
            }

            interpolator = LinearInterpolator()
        }
        return objectAnimator
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (clickQRectF.contains(event.x, event.y)) {
                    flag = true
                    setScaleAnimator().start()
                }
            }
            MotionEvent.ACTION_UP -> {
//                if (clickQRectF.contains(event.x, event.y) || (!clickQRectF.contains(
//                        event.x,
//                        event.y
//                    ) && flag)
//                ) {
//                    setScaleAnimator().reverse()
//
//                }
                if (clickQRectF.contains(event.x, event.y) && flag) {
                    checkTheAnswerIsRight()
                }
                setScaleAnimator().reverse()
                flag = false
            }
            MotionEvent.ACTION_MOVE -> {
                if (flag && !clickQRectF.contains(event.x, event.y)) {
                    flag = false
                }
            }
        }
        return true
    }


    private fun checkTheAnswerIsRight() {
        if (answer) {
            dispatchIfAnswerIsRight()
            answerCallback?.let { it(true) }
        } else {
            dispatchIfAnswerIsFalse()
            answerCallback?.let { it(false) }
        }

    }

    private fun dispatchIfAnswerIsRight() {
        textPaint.color = Color.parseColor("#00ff00")
        bgPaint.color = Color.parseColor("#4D3ddc84")
        changeSVGColor("#1afa29")

    }

    private fun dispatchIfAnswerIsFalse() {
        textPaint.color = Color.parseColor("#dd3535")
        bgPaint.color = Color.parseColor("#4DEF4545")
        changeSVGColor("#dd3535")
    }

    private fun changeSVGColor(colorString: String) {
        val porterDuffColorFilter = PorterDuffColorFilter(
            Color.parseColor(colorString),
            PorterDuff.Mode.SRC_ATOP
        )
        if (iconDrawable != null) {
            iconDrawable?.colorFilter = porterDuffColorFilter
            icon = createBitmap(iconDrawable!!)
            invalidate()
        }

    }
}