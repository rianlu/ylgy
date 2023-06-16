package com.bedroom412.ylgy.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View

/*

//class CircleIndicator(context: Context) : View(context) , ViewPager2.OnPageChangeCallback
class LyricsView(
    context: Context,
    attrs: AttributeSet
) : View(context, attrs) {
    val TAG = "LyricsView"

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : this(context, attrs)

    var textPaint: Paint = Paint(ANTI_ALIAS_FLAG)
    var selectPaint: Paint = Paint(ANTI_ALIAS_FLAG)
    var midLinePaint: Paint = Paint(ANTI_ALIAS_FLAG)

    //    var textSize = 0;
    var currentMillis: Long = 0
        set(value) {
            field = value
            updateIndex()
        }
    var totalMillis: Long = 0


    private fun updateIndex() {

        if (index < lyrics.size - 1) {
            if (currentMillis > lyrics[index + 1].time) {
                index = index + 1

            }
        }
        invalidate()
    }

    var lyrics = listOf<Lyric>()
    var index = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height)

        textPaint.textSize = 32f
        selectPaint.setColor(Color.GREEN)
        selectPaint.textSize = 32f
//        selectPaint.strokeWidth = textPaint.strokeWidth *
        midLinePaint.setColor(Color.RED)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    val rect = Rect()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (totalMillis == 0L) {
            return
        }

        var selectIndex = index;
        var currMillis = currentMillis


        var lyric = lyrics[selectIndex]

        var adjuestHeight = height / 2f
        canvas!!.translate(0f, adjuestHeight)

        // 中心线
        canvas.drawLine(0f,0f, width *1f, 0f, midLinePaint)

        var fm = selectPaint.getFontMetrics()
        val lineHeight: Float = fm.descent - fm.ascent

        val bounds = rect
        textPaint.getTextBounds(lyric.text, 0, lyric.text.length, bounds)
        val lineWidth = bounds.right - bounds.left

        val currentLinePercent = getCurrentLinePercent(currMillis, selectIndex)
//        val currentLinePercent = 0.3f
        var passedLineWidth: Float = lineWidth * currentLinePercent
        canvas.drawText(lyric.text, 0f,  0f, textPaint)


        var path = Path()
        path.addRect(
            0f , bounds.top.toFloat(),
            passedLineWidth, bounds.bottom.toFloat(), Path.Direction.CCW
        )
        canvas.save()
        canvas.clipPath(path)
        canvas.drawText(lyric.text, 0f,  0f, selectPaint)

//     画剩下的

        canvas.restore()
        var space = lineHeight + 32f

        var leftCount = (adjuestHeight / space).toInt() + 1


        for (iLoop in 0 until leftCount) {


            var pre = index - (iLoop + 1)
            var next  = index + iLoop + 1
            if (pre  >= 0) {
                canvas.drawText(lyrics[pre].text, 0f,  - (iLoop + 1) * space, textPaint)
            }

            if (index + iLoop + 1 < lyrics.size) {
                canvas.drawText(lyrics[next].text, 0f,  (iLoop + 1) * space, textPaint)
            }
        }

    }

    fun getCurrentLinePercent(currentMillis: Long, index: Int): Float {

        var nextMillis = -1L

        var lineStartTime = lyrics[index].time;

        if (index == lyrics.size - 1) {
            nextMillis = totalMillis
        } else {
            nextMillis = lyrics[index + 1].time
        }

        var fl = (currentMillis - lineStartTime * 1f) / (nextMillis - lineStartTime)
        Log.d(TAG,"lineStartTime = ${lineStartTime}, currentMillis = ${currentMillis}, index = ${index}, nextMillis = ${nextMillis}, percent = ${fl}")
        return fl
    }

}*/
