package com.wfl.autolooppager;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * Created by wfl on 16/3/21.
 */
public class AnimCirclePagerIndicator extends View implements PagerIndicator {
    private static final String TAG = "AnimPagerIndicator";
    private static final int CIRCLE_COLOR = 0xFFCCCCCC;
    private static final int BACK_COLOR = 0x55FFFFFF;
    private static final int SOLID_COLOR = Color.WHITE;
    private static final int CIRCLE_DIAMETER = 8;
    private static final int STROKE_WIDTH = 1;
    private static final int DEFAULT_MARGIN_EACH = 10;

    private Paint circlePaint;
    private Paint solidPaint;

    private int itemWidth;
    private int circleDiameter;
    private int strokeWidth;
    private int shadowSize = 5;
    private int margin = DEFAULT_MARGIN_EACH;

    private int mCount = 0;
    private int mCurrent = 0;
    private int mAnimPosition = 0;
    private float mCurrentOffset = 0;

    private boolean mAnimated = true;


    public AnimCirclePagerIndicator(Context context) {
        super(context);
        init(context);
    }

    public AnimCirclePagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AnimCirclePagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AnimCirclePagerIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        circleDiameter = Utils.dp2px(context, CIRCLE_DIAMETER);
        strokeWidth = STROKE_WIDTH;

        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(BACK_COLOR);
        circlePaint.setShadowLayer(5, 0, 0, CIRCLE_COLOR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, circlePaint);
        }

        solidPaint = new Paint();
        solidPaint.setStyle(Paint.Style.FILL);
        solidPaint.setColor(SOLID_COLOR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, solidPaint);
        }
    }

    @Override
    public void setAnimated(boolean animated) {
        this.mAnimated = animated;
    }

    @Override
    public void onPageScroll(int position, float positionOffset, int positionOffsetPixels) {
//        Log.d(TAG, "onPageScroll: " + String.format("position=%d, posstionOffset=%f, positionPixels=%d", position, positionOffset, positionOffsetPixels));
        if (mAnimated) {
            mAnimPosition = position;
            mCurrentOffset = positionOffset;
            invalidate();
        }
    }

    public void setCount(int count, int current) {
        if (count <= 1 || current < 0) {
            return;
        }
        this.mCount = count;
        if (current >= count) {
            this.mCurrent = count - 1;
        } else {
            this.mCurrent = current;
        }
        requestLayout();
    }

    public void setCurrentPosition(int currentPosition) {
        this.mCurrent = currentPosition;
        invalidate();
    }


    public void setCircleDiameter(int circleDiameter) {
        this.circleDiameter = circleDiameter;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int w = circleDiameter + strokeWidth + shadowSize;
        itemWidth = w + margin;
        int width = itemWidth * mCount;
        if (widthMode == MeasureSpec.AT_MOST && width > widthSize) {
            width = widthSize;
        }
        int height = itemWidth;
        if (heightMode == MeasureSpec.AT_MOST && height > heightSize) {
            height = heightSize;
        }
        setMeasuredDimension(width + getPaddingLeft() + getPaddingRight(), height + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i=0; i<mCount; i++) {
            drawItem(i, canvas);
        }
        if (mAnimated) {
            if (mAnimPosition < 0 || mAnimPosition >= mCount) {
                return;
            }
            int startY = itemWidth / 2;
            int startX = (int) ((mAnimPosition + 1 + mCurrentOffset) * itemWidth - itemWidth / 2);
            canvas.drawCircle(startX, startY, circleDiameter / 2, solidPaint);
        } else {
            int startY = itemWidth / 2;
            int startX = (mCurrent + 1) * itemWidth - itemWidth / 2;
            canvas.drawCircle(startX, startY, circleDiameter / 2, solidPaint);
        }
    }

    private void drawItem(int position, Canvas canvas) {
        if (position < 0 || position >= mCount) {
            return;
        }
        int startY = itemWidth / 2;
        int startX = (position + 1) * itemWidth - itemWidth / 2;
        canvas.drawCircle(startX, startY, circleDiameter / 2, circlePaint);
    }
}
