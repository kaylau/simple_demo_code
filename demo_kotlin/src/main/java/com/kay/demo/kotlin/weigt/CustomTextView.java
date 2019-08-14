package com.kay.demo.kotlin.weigt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.kay.demo.kotlin.R;
import com.kay.demo.kotlin.util.logutil.LogUtil;

/**
 * Date: 2019/8/14 上午11:14
 * Author: kay lau
 * Description:
 */
public class CustomTextView extends android.support.v7.widget.AppCompatTextView {

    private String mText;
    private Paint mPaint;

    public CustomTextView(Context context) {
        this(context, null);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("CustomViewStyleable")
    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AttrDeclareView);
        int color = a.getColor(R.styleable.AttrDeclareView_background_color, 0);
        mText = a.getString(R.styleable.AttrDeclareView_android_text);
        int textSize = a.getDimensionPixelSize(R.styleable.AttrDeclareView_android_textSize, 0);
        int textColor = a.getColor(R.styleable.AttrDeclareView_android_textColor, 0);
        a.recycle();

        if (color != 0) {
            setBackgroundColor(textColor);
        }

        if (mText != null) {
            mPaint = new Paint();
//            mPaint.setColor(color);
//            mPaint.setTextSize(228.0f);
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 24.0f);
            setTextColor(color);
        }
        LogUtil.INSTANCE.e("textSize: " + textSize);
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mText != null) {

            Rect rect = new Rect();
            mPaint.getTextBounds(mText, 0, mText.length(), rect);

            canvas.drawText(mText, 0 - rect.left + (getWidth() - rect.width()) / 2,
                    0 - rect.top + (getHeight() - rect.height()) / 2, mPaint);
        }
    }
}
