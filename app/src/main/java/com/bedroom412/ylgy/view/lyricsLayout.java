package com.bedroom412.ylgy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.bedroom412.ylgy.util.Lyric;

import java.util.Collections;
import java.util.List;

public class lyricsLayout extends LinearLayout {


    List<Lyric> lyrics = Collections.emptyList();

    public lyricsLayout(Context context) {
        this(context, null);
    }

    public lyricsLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public lyricsLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public lyricsLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int blank = getMeasuredHeight() / 2;
        Log.d("lyricsLayout", String.format("onLayout measuredHeight = %s", getMeasuredHeight()));
        Log.d("lyricsLayout", String.format("onLayout measuredW = %s", getMeasuredWidth()));
        super.onLayout(changed, l, t + blank, r, b + blank);

//        getMeasuredHeight()

    }
}
