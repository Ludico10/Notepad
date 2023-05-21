package com.example.try2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet


class LinedEditText(context: Context?, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatEditText(context!!, attrs) {
    private val mRect: Rect = Rect()
    private val mPaint: Paint = Paint()

    init {
        mPaint.style = Paint.Style.FILL_AND_STROKE
    }

    override fun onDraw(canvas: Canvas) {
        val height = height
        val lineHeight = lineHeight
        var count = height / lineHeight
        if (lineCount > count) count = lineCount
        var baseline = getLineBounds(0, mRect)
        for (i in 0 until count) {
            canvas.drawLine(mRect.left.toFloat(), (baseline + 1).toFloat(), mRect.right.toFloat(), (baseline + 1).toFloat(), mPaint)
            baseline += lineHeight
        }
        super.onDraw(canvas)
    }
}