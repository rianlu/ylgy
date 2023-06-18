package com.bedroom412.ylgy.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

import androidx.annotation.Nullable;
import androidx.core.view.ViewConfigurationCompat;

import com.bedroom412.ylgy.R;
import com.bedroom412.ylgy.util.Lyric;

import java.util.Collections;
import java.util.List;


/**
 * lyrics layout
 *
 * @see #setLyrics
 * @since 2023/06/18
 */
public class LyricsLayout extends LinearLayout implements DurationListener {

    /**
     * lyric margin
     */
    private float lyricMarginLeft;
    /**
     * lyric margin
     */
    private float lyricMarginTop;
    /**
     * lyric margin
     */
    private float lyricMarginRight;

    /**
     * lyric margin
     */
    private float lyricMarginBottom;

    private Scroller mScroller;

    /**
     * the view to scroll mid of layout
     */
    private View lastSelectView = null;


    /**
     * layout is dragging
     */
    private boolean mIsBeingDragged = false;


    /**
     * last move  y aix pixels
     */
    private float mLastMotionY;

    /**
     * last action down y aix pixels
     */
    private float mLastScrollYDown;

    /**
     * the min pixels threshold of touch
     */
    private int mTouchSlop;

    /**
     * lyrics
     */
    List<Lyric> lyrics = Collections.emptyList();

    public LyricsLayout(Context context) {
        this(context, null);
    }

    public LyricsLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LyricsLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public LyricsLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        Resources r = getResources();
        float default_lyric_margin = r.getDimension(R.dimen.default_lyric_margin_bottom);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LyricsLayout, defStyleAttr, defStyleRes);

        lyricMarginLeft = a.getDimension(R.styleable.LyricsLayout_lyric_margin_left, default_lyric_margin);
        lyricMarginTop = a.getDimension(R.styleable.LyricsLayout_lyric_margin_top, default_lyric_margin);
        lyricMarginRight = a.getDimension(R.styleable.LyricsLayout_lyric_margin_right, default_lyric_margin);
        lyricMarginBottom = a.getDimension(R.styleable.LyricsLayout_lyric_margin_bottom, default_lyric_margin);

        a.recycle();

        mScroller = new Scroller(context);

        ViewConfiguration configuration = ViewConfiguration.get(context);

        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
    }


    public void setLyrics(List<Lyric> list) {
        this.lyrics = list;
        initLyricsLayout();
    }

    private void initLyricsLayout() {
        Context context = getContext();
        removeAllViews();

        View blankView = new LyricsBlankView(context);
        blankView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(blankView);

        for (int i = 0; i < lyrics.size(); i++) {
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins((int) lyricMarginLeft, (int) lyricMarginTop, (int) lyricMarginRight, (int) lyricMarginBottom);
            LyricsView lyricsView = new LyricsView(context);
            lyricsView.setmLyric(lyrics.get(i));
            lyricsView.setmIndex(i);
            addView(lyricsView, lp);
        }

        View blankView2 = new LyricsBlankView(context);
        blankView2.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(blankView2);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
            return true;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = (int) ev.getY();
                mScroller.computeScrollOffset();
                mIsBeingDragged = !mScroller.isFinished();
                break;
            case MotionEvent.ACTION_MOVE:
                mLastMotionY = ev.getY();
                float yDiff = Math.abs(mLastMotionY - mLastScrollYDown);
                // 当手指拖动值大于TouchSlop值时，认为应该进行滚动，拦截子控件的事件
                if (yDiff > mTouchSlop) {
                    mIsBeingDragged = true;
                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsBeingDragged = false;

        }

        return mIsBeingDragged;
    }

    @Override
    protected int computeVerticalScrollRange() {
        int childCount = getChildCount();
        return getChildAt(childCount - 1).getBottom() + getMeasuredHeight() / 2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mIsBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE:
                mIsBeingDragged = true;
                float mLastScrollY = event.getY();
                int scrolledY = (int) (this.mLastMotionY - mLastScrollY);
                boolean canScrollVertically = canScrollVertically(scrolledY);
                if (canScrollVertically) {
                    this.mLastMotionY = mLastScrollY;
                    scrollBy(0, scrolledY);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                mIsBeingDragged = false;
        }

        return true;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);


        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof LyricsView) {

                LyricsView child = (LyricsView) childAt;
                int bottom = childAt.getBottom();
                int top = childAt.getTop();

                int i1 = getMeasuredHeight() / 2 + t;
                if (i1 >= top && i1 <= bottom) {
                    child.onFocus(true);
                } else {
                    child.onFocus(false);
                }
            }
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
        super.computeScroll();
    }

    @Override
    public void onProcessing(long duration) {
        int index = -1;

        View selectView = null;
        for (int i = 0; i < lyrics.size(); i++) {
            Lyric lyric = lyrics.get(i);
            if (lyric.getTimeBegin() <= duration && (duration <= lyric.getTimeEnd())) {
                index = i;
            }
        }

        if (index == -1) {
            return;
        }

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof LyricsView) {
                LyricsView child = (LyricsView) childAt;
                child.onProcessing(index, duration);
                if (child.getmIndex() == index) {
                    selectView = child;
                }
            }
        }

        if (selectView != null) {
            if (selectView != lastSelectView) {
                lastSelectView = selectView;
                int height = selectView.getHeight();
                int bottom = selectView.getBottom();
                beginScrollTo( bottom - getMeasuredHeight() / 2 - height / 2);
            }
        }
    }

    private void beginScrollTo(int dy) {
        if (mIsBeingDragged) {
            return;
        }
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
        mIsBeingDragged = false;
        int scrollY = getScrollY();
        int dy1 = dy - scrollY;
        mScroller.startScroll(0, scrollY, 0, dy1);
        invalidate();
    }
}
