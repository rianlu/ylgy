package com.bedroom412.ylgy.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.bedroom412.ylgy.R;
import com.bedroom412.ylgy.util.Lyric;

public class LyricsView extends View implements DurationListener ,LyricActionListener{

    private DynamicLayout mLayout;
    //    private float lyricLineMarginDimension = 15;
    private int mIndex;

    private Lyric mLyric;

    private boolean mIsSelect = false;

    private boolean mIsFocus = false;

    private long mDuration;

//    private Paint mUnselectedPaint;

    private float selectedTextSizeScale;
    private Paint paint;

    private int mUnselectedColor;

    private int mHighLightSelectedColor;

    private Path path = new Path();

    public int getmIndex() {
        return mIndex;
    }

    public LyricsView(Context context) {
        this(context, null);
    }

    public LyricsView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.lyric_view);
    }

    public LyricsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public LyricsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Resources r = getResources();
        Resources.Theme theme = context.getTheme();
        float default_unselected_text_size = 1.0f;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            default_unselected_text_size = r.getFloat(R.dimen.default_selected_text_scale);
        }
        int default_unselected_text_color = r.getColor(R.color.default_unselected_text_color, theme);
        float default_selected_text_size = r.getDimension(R.dimen.default_selected_text_size);
        int default_selected_text_color = r.getColor(R.color.default_selected_text_color, theme);
        int default_selected_high_light_text_color = r.getColor(R.color.default_selected_high_light_text_color, theme);


        TypedArray a = theme.obtainStyledAttributes(attrs, R.styleable.LyricsView, defStyleAttr, defStyleRes);

        float selected_text_size_scale = a.getFloat(R.styleable.LyricsView_selected_text_size_scale, default_unselected_text_size);
        int unselected_text_color = a.getColor(R.styleable.LyricsView_unselected_text_color, default_unselected_text_color);
        float selected_text_size = a.getDimension(R.styleable.LyricsView_selected_text_size, default_selected_text_size);
        int selected_text_color = a.getColor(R.styleable.LyricsView_selected_text_color, default_selected_text_color);
        int selected_high_light_text_color = a.getColor(R.styleable.LyricsView_selected_high_light_text_color, default_selected_high_light_text_color);


        a.recycle();

        paint = new Paint();
        paint.setTextSize(selected_text_size);
        paint.setColor(selected_text_color);

        mUnselectedColor = unselected_text_color;

        mHighLightSelectedColor =selected_high_light_text_color;

        selectedTextSizeScale = selected_text_size_scale;
        setBackgroundColor(Color.GRAY);
    }

    public void setmLyric(Lyric mLyric) {
        this.mLyric = mLyric;
    }

    public void setmIndex(int mIndex) {
        this.mIndex = mIndex;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);
        mLayout = new DynamicLayout(mLyric.getText(), new TextPaint(paint),
                width, Layout.Alignment.ALIGN_CENTER, 1f, 0f, false);

        setMeasuredDimension(width + getPaddingLeft() + getPaddingRight(), mLayout.getHeight() + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mIsSelect) {
            drawSelectedPaint(canvas);
        } else if (mIsFocus) {
            drawPaint(canvas,mHighLightSelectedColor);
        } else {
            drawPaint(canvas,mUnselectedColor);
        }
    }

    private void drawSelectedPaint(Canvas canvas) {

        long timeBegin = mLyric.getTimeBegin();
        long timeEnd = mLyric.getTimeEnd();

        float percent = (mDuration - timeBegin) * 1f / (timeEnd - timeBegin);
        int wordPassIndex = (int) Math.max(0, (percent * mLyric.getText().length()) - 1);
        int lineCount = mLayout.getLineCount();

        for (int i = 0; i < lineCount; i++) {

            int lineStartIndex = mLayout.getLineStart(i);
            int lineEndIndex = mLayout.getLineEnd(i);

            String lineText = mLyric.getText().substring(lineStartIndex, lineEndIndex);

            float x = (getMeasuredWidth() - mLayout.getLineWidth(i)) / 2f;

            float y = getPaddingTop() + mLayout.getLineBaseline(i);

            paint.setColor(mUnselectedColor);
            canvas.drawText(lineText, x, y, paint);

            if (wordPassIndex >= lineStartIndex && wordPassIndex <= lineEndIndex) {
                float currentDurationLineWidth = getCurrentDurationLineWidth(percent, i);
                float stopX = x + currentDurationLineWidth;
                path.reset();
                path.addRect(
                        x, mLayout.getLineTop(i), stopX, mLayout.getLineBottom(i), Path.Direction.CCW
                );
                canvas.save();
                canvas.clipPath(path);
                paint.setColor(mHighLightSelectedColor);
                canvas.drawText(lineText, x, y, paint);
                paint.setColor(mUnselectedColor);
                canvas.restore();
            } else if (wordPassIndex > lineEndIndex) {
                paint.setColor(mHighLightSelectedColor);
                canvas.drawText(lineText, x, y, paint);
                paint.setColor(mUnselectedColor);
            }else {
                paint.setColor(mUnselectedColor);
                canvas.drawText(lineText, x, y, paint);
            }
        }
    }

    private void drawPaint(Canvas canvas , int color) {
        int lineCount = mLayout.getLineCount();
        for (int i = 0; i < lineCount; i++) {

            int lineStartIndex = mLayout.getLineStart(i);
            int lineEndIndex = mLayout.getLineEnd(i);
            String lineText = mLyric.getText().substring(lineStartIndex, lineEndIndex);
            float x = (getMeasuredWidth() - mLayout.getLineWidth(i)) / 2f;
            float y = getPaddingTop() + mLayout.getLineBaseline(i);
            paint.setColor(color);
            canvas.drawText(lineText, x, y, paint);
        }

    }

    private float getCurrentDurationLineWidth(float percent, int currPos) {

        int lineCount = mLayout.getLineCount();

        if (lineCount == 1) {
            return mLayout.getLineWidth(0) * percent;
        }


        float lineWidthBeforePos = 0;
//        float currWidthBeforePos = 0;
        float lineWidthAllPos = 0;


        for (int i = 0; i < lineCount; i++) {
            if (i < currPos) {
                lineWidthBeforePos += mLayout.getLineWidth(i);
            }
            lineWidthAllPos += mLayout.getLineWidth(i);
        }

        return (lineWidthAllPos * percent) - lineWidthBeforePos;
    }

    @Override
    public void onProcessing(int index, long duration) {

        if (mLyric == null) {
            return;
        }

        boolean selected = this.mIndex == index;
        if (mIsSelect != selected || selected) {
            mIsSelect = selected;
            this.mDuration = duration;
            if (mIsSelect) {
                setScaleX(selectedTextSizeScale);
                setScaleY(selectedTextSizeScale);
            } else {
                setScaleX(1.0f);
                setScaleY(1.0f);
            }
            invalidate();
        }
    }

    @Override
    public void onFocus(boolean isFocus) {
        if (this.mIsFocus != isFocus) {
            this.mIsFocus = isFocus;
            invalidate();
        }
    }
}
