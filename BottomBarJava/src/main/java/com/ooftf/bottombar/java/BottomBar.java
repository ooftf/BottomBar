package com.ooftf.bottombar.java;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class BottomBar extends LinearLayout {
    MyDataSetObserver observer = new MyDataSetObserver();
    OnItemSelectInterceptor mOnItemSelectIInterceptor;
    OnItemSelectIChangedListener mOnItemSelectChangedListener;
    OnItemRepeatListener mOnItemRepeatListener;
    int selectIndex = -1;

    public BottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    {
        setOrientation(HORIZONTAL);
    }


    public void setOnItemSelectInterceptor(OnItemSelectInterceptor interceptor) {
        mOnItemSelectIInterceptor = interceptor;
    }


    public void setOnItemSelectChangedListener(OnItemSelectIChangedListener listener) {
        mOnItemSelectChangedListener = listener;
    }


    public void setOnItemRepeatListener(OnItemRepeatListener listener) {
        mOnItemRepeatListener = listener;
    }

    private Adapter mAdapter;

    public void setAdapter(Adapter adapter) {
        if (adapter == null) {
            return;
        }
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(observer);
        }

        mAdapter = adapter;
        mAdapter.registerAdapterDataObserver(observer);
        selectIndex = -1;
        createItems();
    }


    private void createItems() {
        removeAllViews();
        if (mAdapter == null) {
            return;
        }
        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            RecyclerView.ViewHolder viewHolder = mAdapter.createViewHolder(this, mAdapter.getItemViewType(i));
            viewHolder.itemView.setTag(R.id.bottom_bar_view_holder_tag_id, viewHolder);
            mAdapter.onBindViewHolder(viewHolder, i, selectIndex);
            addView(viewHolder.itemView);
            LinearLayout.LayoutParams layoutParams = (LayoutParams) viewHolder.itemView.getLayoutParams();
            layoutParams.weight = 1f;
            layoutParams.height = LayoutParams.MATCH_PARENT;
            layoutParams.width = 0;
            final int finalI = i;
            viewHolder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelectedIndex(finalI);
                }
            });
        }
    }

    /**
     * 设置新的选中index
     */
    public void setSelectedIndex(int index) {
        if (!isLegal(index)) {
            return;
        }
        //判断是否重复点击
        if (index == selectIndex) {
            if (mOnItemRepeatListener != null) {
                mOnItemRepeatListener.onItemRepeat(selectIndex);
            }
            return;
        }
        if (mOnItemSelectIInterceptor != null && mOnItemSelectIInterceptor.onItemSelect(selectIndex, index)) {
            return;
        }

        //没有被拦截
        if (mOnItemSelectChangedListener != null) {
            mOnItemSelectChangedListener.onItemSelect(selectIndex, index);
        }
        selectedAnimate(getChildAt(index));
        if (isLegal(selectIndex)) {
            unSelectedAnimate(getChildAt(selectIndex));
        }
        selectIndex = index;
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 判断Index是否合法
     */
    private boolean isLegal(int index) {
        if (index < 0) {
            return false;
        }
        if (index >= getChildCount()) {
            return false;
        }
        return true;
    }

    private void updateItems() {
        if (mAdapter == null) {
            return;
        }
        for (int i = 0; i < getChildCount(); i++) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) getChildAt(i).getTag(R.id.bottom_bar_view_holder_tag_id);
            mAdapter.onBindViewHolder(viewHolder, i, selectIndex);
        }
    }

    private void selectedAnimate(View view) {
        view.animate().translationY(-5f).scaleX(1.03f).scaleY(1.03f).setDuration(200).start();
    }

    private void unSelectedAnimate(View view) {
        view.animate().translationY(0f).scaleX(1.0f).scaleY(1.0f).setDuration(200).start();
    }


    class MyDataSetObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            updateItems();
        }

    }

    public abstract static class Adapter<VH extends RecyclerView.ViewHolder, B> extends RecyclerView.Adapter<VH> {
        private List<B> data = new ArrayList<>();

        @Override
        public final void onBindViewHolder(@NonNull VH holder, int position) {

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        /**
         * onBindViewHolder
         *
         * @param holder
         * @param position
         * @param selectedPosition
         */
        public abstract void onBindViewHolder(VH holder, int position, int selectedPosition);

        public void addAll(List<B> list) {
            data.addAll(list);
        }

        public void add(B item) {
            data.add(item);
        }

        public void setData(@NonNull List<B> data) {
            this.data = data;
        }

        public List<B> setData() {
            return data;
        }

        public B getItem(int position) {
            if (position < 0 || position >= data.size()) {
                return null;
            }
            return data.get(position);
        }

        public void clear() {
            data.clear();
        }
    }
}