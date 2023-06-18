package com.bedroom412.ylgy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class LyricsBlankView extends View{

    public LyricsBlankView(Context context) {
        super(context);
    }

    public LyricsBlankView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LyricsBlankView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LyricsBlankView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int hsize = MeasureSpec.getSize(heightMeasureSpec);
//        int wsize = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),  MeasureSpec.getSize(heightMeasureSpec) / 2);
//        Log.d("ylhy", String.format("blankonMeasure = %s  %s", wsize, hsize));
//        setMeasuredDimension(100, 100);
    }
}
