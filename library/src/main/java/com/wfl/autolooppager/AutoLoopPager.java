package com.wfl.autolooppager;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


/**
 * Created by wfl on 16/3/18.
 *
 * <p>自动可循环滚动的ViewPager，带有页面指示器</p>
 *
 * @author Wang Fulin
 */
public class AutoLoopPager extends RelativeLayout {
    private static final String TAG = "AutoPager";
    private static final int DEFAULT_DURATION = 3000;
    private ViewPager mViewPager;
    private PagerIndicator mIndicator;
    private float mAspectRatio = 2.3000f;

    private int mCount;
    private int mCurrent;
    private boolean mAutoPlay = true;
    private int mDuration = DEFAULT_DURATION;

    private MyPagerAdapter mAdapter;
    private ViewPager.OnPageChangeListener mOnPageChangedListener;

    public AutoLoopPager(Context context) {
        super(context);
        init(context, null);
    }

    public AutoLoopPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public AutoLoopPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AutoLoopPager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * 设置该Pager的宽高比，在{@link #onMeasure(int, int)}时用到
     * @param aspectRatio
     */
    public void setAspectRatio(float aspectRatio) {
        this.mAspectRatio = aspectRatio;
        requestLayout();
    }

    /**
     * 设置是否可以自动滚动切换
     * @param auto
     */
    public void setAutoPlay(boolean auto) {
        this.mAutoPlay = auto;
        if (!mAutoPlay) {
            removeCallbacks(autoRunnable);
        } else {
            postDelayed(autoRunnable, mDuration);
        }
    }

    public void setIndicatorAnimed(boolean animed) {
        mIndicator.setAnimated(animed);
    }

    public void setAdapter(RecycleAdapter adapter) {
        mAdapter = new MyPagerAdapter(adapter);
        mViewPager.setAdapter(mAdapter);
        mCount = adapter.getCount();
        if (mCount > 0) {
            mViewPager.setCurrentItem(1);
        }
        mIndicator.setCount(mCount, 0);
    }

    /**
     * 获取当前显示的Item位置，是真实数据的位置
     * @return 当前的位置
     */
    public int getCurrentItem() {
        return mCurrent;
    }

    /**
     * 设置自动滚动的间隔时间，以毫秒为单位
     * @param autoDuration 自动滚动的间隔时间，以毫秒为单位。
     */
    public void setAutoDuration(int autoDuration) {
        this.mDuration = autoDuration;
    }

    /**
     * 设置Pager的OnPageChangedListener监听对象
     * @param listener
     */
    public void setOnPageChangedListener(ViewPager.OnPageChangeListener listener) {
        this.mOnPageChangedListener = listener;
    }

    private void init(Context context, AttributeSet attrs) {
        mViewPager = new LoopViewPager(context);
        RelativeLayout.LayoutParams pagerLp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mViewPager.setLayoutParams(pagerLp);
        addView(mViewPager);

        mIndicator = new AnimCirclePagerIndicator(context);
        RelativeLayout.LayoutParams indicatorLp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        indicatorLp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        indicatorLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        indicatorLp.bottomMargin = Utils.dp2px(context, 5);
        addView((View) mIndicator, indicatorLp);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int mPrePosition = -1;
            private float mPreviousOffset = -1;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int realPosition = mAdapter.toRealPosition(position);
                float realOffset = positionOffset;
                int realOffsetPixels = positionOffsetPixels;
                if (mAdapter != null) {
                    realPosition = mAdapter.toRealPosition(position);
                    if (positionOffset == 0
                            && mPreviousOffset == 0
                            && (position == 0 || position == mAdapter.getCount() - 1)) {
                        mViewPager.setCurrentItem(mAdapter.toPagerPosition(realPosition), false);
                    }
                }

                mPreviousOffset = positionOffset;
                if (realPosition != mAdapter.getRealCount() - 1) {

                } else {
                    if (positionOffset > .5) {
                        realPosition = 0;
                        realOffset = realOffsetPixels = 0;
                    } else {
                        realOffset = realOffsetPixels = 0;
                    }
                }
                if (mOnPageChangedListener != null) {
                    mOnPageChangedListener.onPageScrolled(realPosition,
                            realOffset, realOffsetPixels);
                }
                mIndicator.onPageScroll(realPosition, realOffset, realOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
                mCurrent = mAdapter.toRealPosition(position);
                if (mPrePosition != mCurrent) {
                    mPrePosition = mCurrent;
                    mIndicator.setCurrentPosition(mCurrent);
                    if (null != mOnPageChangedListener) {
                        mOnPageChangedListener.onPageSelected(mCurrent);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mAdapter != null) {
                    int position = mViewPager.getCurrentItem();
                    int realPosition = mAdapter.toRealPosition(position);
                    if (state == ViewPager.SCROLL_STATE_IDLE
                            && (position == 0 || position == mAdapter.getCount() - 1)) {
                        mViewPager.setCurrentItem(realPosition, false);
                    }
                }
                if (null != mOnPageChangedListener) {
                    mOnPageChangedListener.onPageScrollStateChanged(state);
                }
            }
        });

        // 当有触摸动作时暂停自动切换，手指离开屏幕后重新开始计时切换
        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mAutoPlay) {
                    if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        removeCallbacks(autoRunnable);
                    } else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                        postDelayed(autoRunnable, mDuration);
                    }
                }
                return false;
            }
        });

    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (mAutoPlay) {
            if (visibility == View.VISIBLE) {
                postDelayed(autoRunnable, mDuration);
            } else {
                removeCallbacks(autoRunnable);
            }
        }
    }

    private void smoothScrollToPosition(int position) {
        if (position < 0 || position >= mCount) {
            return;
        }
        mViewPager.setCurrentItem(mCurrent, true);
        mIndicator.setCurrentPosition(position);
    }

    Runnable autoRunnable = new Runnable() {
        @Override
        public void run() {
            if (mCurrent >= mCount - 1) {
                mCurrent = 0;
            } else {
                mCurrent++;
            }
            smoothScrollToPosition(mCurrent);
            postDelayed(this, mDuration);
        }
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int measuredWidth = widthSize;
        if (heightMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(measuredWidth, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            // 根据宽高比计算出高度
            int height = (int) (widthSize / mAspectRatio);
            setMeasuredDimension(measuredWidth, height);
        }

        int heightSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightSpec);
    }

    /**
     * 重写的ViewPager，主要是为了在设置真实的item时正确的对应到ViewPager的位置上
     */
    public class LoopViewPager extends ViewPager {

        public LoopViewPager(Context context) {
            super(context);
        }

        @Override
        public void setCurrentItem(int item, boolean smoothScroll) {
            super.setCurrentItem(mAdapter.toPagerPosition(item), smoothScroll);
        }
    }

    public class MyPagerAdapter extends PagerAdapter {
        /**
         * 真实数据来源,所有位置使用真实的位置
         */
        private RecycleAdapter recycleAdapter;

        public MyPagerAdapter(RecycleAdapter recycleAdapter) {
            this.recycleAdapter = recycleAdapter;
            recycleAdapter.setPagerSource(this);
        }

        public void setBannerAdapter(RecycleAdapter recycleAdapter) {
            this.recycleAdapter = recycleAdapter;
            recycleAdapter.setPagerSource(this);
        }

        public int getRealCount() {
            if (null != recycleAdapter) {
                return recycleAdapter.getCount();
            } else {
                return 0;
            }
        }

        /**
         * 将Pager中的位置转换为数据中真实的位置
         * @param position VIewPager中的位置
         * @return 在数据源中的位置
         */
        public int toRealPosition(int position) {
            int realCount = getRealCount();
            if (realCount == 0) {
                return 0;
            } else {
                int realPosition = (position - 1) % realCount;
                if (realPosition < 0) {
                    realPosition += realCount;
                }
                return realPosition;
            }
        }

        public int toPagerPosition(int realPosition) {
            return realPosition + 1;
        }

        @Override
        public int getCount() {
            int realCount = getRealCount();
            return realCount == 0 ? 0 : realCount + 2;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (null != recycleAdapter) {
                return recycleAdapter.instantiateItem(container, toRealPosition(position));
            }
            return super.instantiateItem(container, position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            if (null != recycleAdapter) {
                return recycleAdapter.isViewFromObject(view, object);
            } else {
                return false;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (null != recycleAdapter) {
                recycleAdapter.destoryItem(container, toRealPosition(position), object);
            }
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            mCount = getRealCount();
            mIndicator.setCount(mCount, 0);
            if (mCount > 0) {
                mViewPager.setCurrentItem(1);
            }
        }
    }

}
