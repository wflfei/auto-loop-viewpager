package com.wfl.autolooppager;

/**
 * Created by wfl on 16/3/21.
 */
public interface PagerIndicator {
    void onPageScroll(int position, float positionOffset, int positionOffsetPixels);
    void setCount(int count, int current);
    void setCurrentPosition(int currentPosition);
    void setAnimated(boolean animated);
}
