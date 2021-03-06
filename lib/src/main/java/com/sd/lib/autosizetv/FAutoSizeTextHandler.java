package com.sd.lib.autosizetv;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;

public class FAutoSizeTextHandler {
    private final TextView mTextView;
    private AppCompatTextViewAutoSizeHelper mAutoSizeTextHelper;
    private boolean mIsAutoSizeEnable = true;

    public FAutoSizeTextHandler(@NonNull TextView textView) {
        mTextView = textView;
    }

    private AppCompatTextViewAutoSizeHelper getAutoSizeTextHelper() {
        if (mAutoSizeTextHelper == null) {
            mAutoSizeTextHelper = new AppCompatTextViewAutoSizeHelper(mTextView);
        }
        return mAutoSizeTextHelper;
    }

    /**
     * 初始化
     *
     * @param attrs
     * @param defStyleAttr
     */
    public void init(@Nullable AttributeSet attrs, int defStyleAttr) {
        final Context context = mTextView.getContext();

        boolean autoSizeEnable = true;
        float tempStep = 0;
        float tempMin = 0;
        float tempMax = mTextView.getTextSize();

        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, androidx.appcompat.R.styleable.AppCompatTextView,
                    defStyleAttr, 0);
            ViewCompat.saveAttributeDataForStyleable(mTextView, mTextView.getContext(),
                    androidx.appcompat.R.styleable.AppCompatTextView, attrs, a,
                    defStyleAttr, 0);

            if (a.hasValue(R.styleable.AppCompatTextView_autoSizeTextType)) {
                final int type = a.getInt(R.styleable.AppCompatTextView_autoSizeTextType, TextViewCompat.AUTO_SIZE_TEXT_TYPE_NONE);
                if (type == TextViewCompat.AUTO_SIZE_TEXT_TYPE_NONE) {
                    autoSizeEnable = false;
                }
            }

            if (a.hasValue(R.styleable.AppCompatTextView_autoSizeStepGranularity)) {
                tempStep = a.getDimension(R.styleable.AppCompatTextView_autoSizeStepGranularity, tempStep);
            }
            if (a.hasValue(R.styleable.AppCompatTextView_autoSizeMinTextSize)) {
                tempMin = a.getDimension(R.styleable.AppCompatTextView_autoSizeMinTextSize, tempMin);
            }
            if (a.hasValue(R.styleable.AppCompatTextView_autoSizeMaxTextSize)) {
                tempMax = a.getDimension(R.styleable.AppCompatTextView_autoSizeMaxTextSize, tempMax);
            }

            a.recycle();
        }


        final float finalStep = tempStep > 0 ? tempStep : dp2px(1, context);
        final float finalMin = tempMin > 0 ? tempMin : dp2px(6, context);
        final float finalMax = Math.max(tempMax, (finalMin + finalStep));

        setAutoSizeEnable(autoSizeEnable);
        setAutoSizeWithConfiguration(
                (int) finalMin,
                (int) finalMax,
                (int) finalStep,
                TypedValue.COMPLEX_UNIT_PX);
    }

    /**
     * 是否开启自适应大小
     */
    public void setAutoSizeEnable(boolean autoSizeEnable) {
        mIsAutoSizeEnable = autoSizeEnable;
    }

    /**
     * 设置自适应配置
     *
     * @param minSize 最小值
     * @param maxSize 最大值
     * @param step    步进
     * @param unit    {@link TypedValue}
     */
    public void setAutoSizeWithConfiguration(int minSize,
                                             int maxSize,
                                             int step,
                                             int unit) {
        Log.i(FAutoSizeTextHandler.class.getSimpleName(), "setAutoSizeWithConfiguration"
                + " minSize:" + minSize
                + " maxSize:" + maxSize
                + " step:" + step
                + " unit:" + unit
        );

        getAutoSizeTextHelper().setAutoSizeTextTypeUniformWithConfiguration(minSize, maxSize, step,
                unit);
    }

    /**
     * 自适应
     *
     * @param maxWidth  最大宽度
     * @param maxHeight 最小宽度
     */
    public void autoSize(int maxWidth, int maxHeight) {
        if (mIsAutoSizeEnable) {
            final long startTime = System.currentTimeMillis();
            mAutoSizeTextHelper.autoSizeText(maxWidth, maxHeight);
            Log.i(FAutoSizeTextHandler.class.getSimpleName(), "autoSize"
                    + " width:" + maxWidth
                    + " height:" + maxHeight
                    + " time:" + (System.currentTimeMillis() - startTime)
            );
        }
    }

    private static int dp2px(int dp, Context context) {
        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) (dp * displayMetrics.density + 0.5);
    }
}
