package com.sd.lib.autosizetv;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class FAutoSizeTextView extends AppCompatTextView {
    private final FAutoSizeTextHandler mAutoSizeTextHandler;
    private Integer mLastWidthMeasureSpec, mLastHeightMeasureSpec;

    public FAutoSizeTextView(@NonNull Context context) {
        this(context, null);
    }

    public FAutoSizeTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public FAutoSizeTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mAutoSizeTextHandler = new FAutoSizeTextHandler(this);
        mAutoSizeTextHandler.init(attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        saveMeasureSpec(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void saveMeasureSpec(int widthMeasureSpec, int heightMeasureSpec) {
        boolean shouldAutoSize = false;
        if (mLastWidthMeasureSpec == null ||
                mLastHeightMeasureSpec == null) {
            shouldAutoSize = true;
        } else if (mLastWidthMeasureSpec != widthMeasureSpec ||
                mLastHeightMeasureSpec != heightMeasureSpec) {
            shouldAutoSize = true;
        }

        if (shouldAutoSize) {
            mLastWidthMeasureSpec = widthMeasureSpec;
            mLastHeightMeasureSpec = heightMeasureSpec;
            startAutoSize();
        }
    }

    private void startAutoSize() {
        removeCallbacks(mAutoSizeRunnable);
        post(mAutoSizeRunnable);
    }

    private final Runnable mAutoSizeRunnable = new Runnable() {
        @Override
        public void run() {
            if (mLastWidthMeasureSpec == null || mLastHeightMeasureSpec == null) {
                return;
            }

            if (mAutoSizeTextHandler != null) {
                final int width = MeasureSpec.getSize(mLastWidthMeasureSpec);
                final int height = MeasureSpec.getSize(mLastHeightMeasureSpec);
                mAutoSizeTextHandler.autoSize(width, height);
            }
        }
    };
}
