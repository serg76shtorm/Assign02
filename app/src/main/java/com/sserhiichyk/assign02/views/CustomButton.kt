package com.sserhiichyk.assign02.views

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.res.ResourcesCompat
import com.sserhiichyk.assign02.R
import com.sserhiichyk.assign02.com.sserhiichyk.assign02.data.Constants.durationG
import com.sserhiichyk.assign02.com.sserhiichyk.assign02.data.Constants.margin
import com.sserhiichyk.assign02.com.sserhiichyk.assign02.data.Constants.timeG
import com.sserhiichyk.assign02.com.sserhiichyk.assign02.data.Constants.timeText
import kotlin.math.sin


class CustomButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet, defStyleAttr: Int = 0  //@AttrRes
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        style = Paint.Style.FILL
        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        textSize = resources.getDimension(R.dimen.global_middle)
    }

    private var text = resources.getText(R.string.create_user).toString()
    private var textBounds: Rect = Rect(0, 0, 0, 0)
    private var moveGX = 0
    private var moveGY = 0
    private var moveG = true
    private var moveGMirror = false
    private var widthViewMove = 0
    private val vectorDrawable: Drawable =
        ResourcesCompat.getDrawable(resources, R.drawable.ic_g, null)!!
    private var pictureImage = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    private var animator = false

    data class TextData(
        val position: Int,
        val charText: Char,
        var posX: Float,
        var posY: Float
    ) {
        var copyPosY = 0f

        init {
            copyPosY = posY
        }
    }

    private val textAnimator = ArrayList<TextData>()

    init {
        val count = attrs.attributeCount - 1
        //TODO: for i<count ?, for(i, j in ..)?
        for (i in 0..count) {
            val attrValue: String = attrs.getAttributeValue(i)
            if (attrs.getAttributeName(i).equals("text")) {
                if (attrValue.startsWith("@")) {
                    text = resources.getString(attrs.getAttributeResourceValue(i, 0))
                } else text = attrValue
            } else if (attrs.getAttributeName(i).equals("textColor")) {
                if (attrValue.startsWith("@")) {
                    paint.color =
                        Color.parseColor(resources.getString(attrs.getAttributeResourceValue(i, 0)))
                } else paint.color = Color.parseColor(attrValue)
            }

        }

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomButton)
        try {
            animator = typedArray.getBoolean(R.styleable.CustomButton_valueAnimator, true)
        } finally {
            typedArray.recycle()
        }

        vectorDrawable.bounds = Rect(0, 0, pictureImage.width, pictureImage.height)

        val canvasImage = Canvas(pictureImage)
        vectorDrawable.draw(canvasImage)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        widthViewMove = w / 2
        moveGY = h / 2 - pictureImage.height / 2
        moveGX = w / 2 - pictureImage.width - margin
        moveG = true

        textInit(w, h)

        paint.getTextBounds(text, 0, text.length, textBounds) //TODO: minSdk 29 :(

        if (animator) animation()

    }

    private fun textInit(w: Int, h: Int) {
        textAnimator.clear()
        paint.getTextBounds(text, 0, text.length, textBounds)

        var charPositionX: Float = (w / 2 + margin).toFloat()
        val charPositionY: Float = (h / 2 + (textBounds.height() / 2 - textBounds.bottom)).toFloat()

        text.forEachIndexed { i, it ->
            paint.getTextBounds(text, i, i + 1, textBounds)
            charPositionX += textBounds.left

            textAnimator.add(
                TextData(
                    i,
                    it,
                    charPositionX,
                    charPositionY
                )
            )

            charPositionX += textBounds.right
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas != null) {

            if (moveGMirror) {
                val matrixMirror = Matrix()
                matrixMirror.preScale(-1f, 1f)
                val newBitmap = Bitmap.createBitmap(
                    pictureImage,
                    0,
                    0,
                    pictureImage.width,
                    pictureImage.height,
                    matrixMirror,
                    false
                )

                canvas.drawBitmap(newBitmap, moveGX.toFloat(), moveGY.toFloat(), paint)
            } else {
                canvas.drawBitmap(pictureImage, moveGX.toFloat(), moveGY.toFloat(), paint)
            }

            textAnimator.forEach {
                canvas.drawText(it.charText.toString(), it.posX, it.posY, paint)
            }

        }
    }

    @SuppressLint("Recycle")
    fun animation() {

        val imageAnimator1 = ValueAnimator.ofFloat(0f, timeG.toFloat())
        val widthMove = widthViewMove - pictureImage.width - margin
        val animatorSteps = (widthMove) / timeG.toFloat()
        var zero = false
        imageAnimator1.interpolator = LinearInterpolator()
        imageAnimator1.duration = durationG

        moveGX = widthMove
        imageAnimator1.doOnEnd {
            moveGX = widthMove
        }

        imageAnimator1.addUpdateListener {
            val value = it.animatedValue as Float

            if (moveG) moveGX += animatorSteps.toInt()
            else if (zero) moveGX -= animatorSteps.toInt()

            if ((value.toInt() == 0) && (!zero)) {
                moveGMirror = true
                moveG = false
                zero = true
            } else if (value.toInt() == timeG / 2) {
                moveGMirror = false
                moveG = true
            } else if (value.toInt() == timeG) {
                moveGMirror = false
            }

            invalidate()
        }

        val imageAnimator2 = ValueAnimator.ofFloat(0f, timeText.toFloat())
        imageAnimator2.interpolator = LinearInterpolator()
        imageAnimator2.duration = durationG
        val amplitude = textBounds.height() / 2

        imageAnimator2.doOnStart {
            zero = false
        }

        imageAnimator2.addUpdateListener {
            val value = (it.animatedValue as Float).toDouble()
            val valueInt = value.toInt()
            var step = 10

            if (valueInt >= step) {
                textAnimator.forEach {
                    it.posY = (it.copyPosY + sin(it.posX * value) * amplitude).toFloat()
                }
                //TODO: не работает ?????
                step = valueInt + 10
/*
                Log.i(
                    "MainActivity", "animation2 ".plus(value.toInt().toString().plus(" step=").plus(step))
                )
*/
            }

            if (valueInt == timeText) {
                textAnimator.forEach {
                    it.posY = it.copyPosY
                }
            }

            invalidate()
        }

        val imageAnimatorSet = AnimatorSet()
        imageAnimatorSet.play(imageAnimator1).after(durationG).after(imageAnimator2)
        imageAnimatorSet.duration = durationG
        imageAnimatorSet.startDelay = 2000L
        imageAnimatorSet.doOnEnd {
            it.start()
        }

        imageAnimatorSet.start()
    }

}