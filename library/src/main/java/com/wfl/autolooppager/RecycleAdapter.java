package com.wfl.autolooppager;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by wfl on 16/3/21.
 * 使用ViewHolder的可回收的一个Pager Adapter，这里没有直接继承 PagerAdapter 而是直接声明了必要的方法，供真正的Adapter使用
 * @author Wang Fulin
 */
public abstract class RecycleAdapter<VH extends RecycleAdapter.ViewHolder> {
    private final Queue<VH> mCache = new LinkedList<>();
    private final SparseArray<VH> mHolders = new SparseArray<>();
    private PagerAdapter pagerAdapter;

    public void setPagerSource(PagerAdapter pagerSource) {
        this.pagerAdapter = pagerSource;
    }

    public Object instantiateItem(ViewGroup container, int position) {
        VH holder = mCache.poll();
        if (null == holder) {
            holder = onCreateViewHolder(container);
        }
        mHolders.put(position, holder);
        container.addView(holder.mView, null);
        onBindViewHolder(holder, position);
        return holder;
    }

    public void destoryItem(ViewGroup container, int position, Object object) {
        VH holder = (VH) object;
        mHolders.remove(position);
        container.removeView(holder.mView);
        mCache.offer(holder);
        onRecycleViewHolder(holder);
    }

    public boolean isViewFromObject(View view, Object object) {
        ViewHolder holder = (ViewHolder) object;
        return holder.mView == view;
    }

    public void notifyDataSetChanged() {
        if (pagerAdapter != null) {
            pagerAdapter.notifyDataSetChanged();
        }
    }

    protected abstract int getCount();
    protected abstract VH onCreateViewHolder(ViewGroup container);
    protected abstract void onBindViewHolder(VH holder, int position);
    protected abstract void onRecycleViewHolder(VH holder);

    public VH getViewHolder(int position) {
        return mHolders.get(position);
    }

    public static class ViewHolder {
        protected View mView;

        protected ViewHolder(@NonNull View view) {
            this.mView = view;
        }
    }
}
