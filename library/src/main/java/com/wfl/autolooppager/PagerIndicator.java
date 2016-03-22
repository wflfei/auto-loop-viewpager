package com.wfl.autolooppager;

/**
 * Created by wfl on 16/3/21.
 */
public interface PagerIndicator {
    public void onPageScroll(int position, float positionOffset, int positionOffsetPixels);
    public void setCount(int count, int current);
    public void setCurrentPosition(int currentPosition);
}
