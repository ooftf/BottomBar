package com.ooftf.bottombar

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.ooftf.bottombar.java.R

class BottomBar(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    var onItemSelectIInterceptor: ((oldIndex: Int, newIndex: Int) -> Boolean)? = null
    var onItemSelectChangedListener: ((oldIndex: Int, newIndex: Int) -> Unit)? = null
    var onItemRepeatListener: ((position: Int) -> Unit)? = null
    var currentIndex = -1
        private set

    private var mAdapter: Adapter<out ViewHolder>? = null
    fun setAdapter(adapter: Adapter<out ViewHolder>) {
        mAdapter = adapter
        currentIndex = -1
        updateItems()
    }

    /**
     * 设置新的选中index
     */
    fun setSelectedIndex(newIndex: Int) {
        if (!isLegal(newIndex)) {
            return
        }
        val oldIndex = currentIndex
        //判断是否重复点击
        if (newIndex == oldIndex) {
            onItemRepeatListener?.invoke(oldIndex)
            return
        }
        if (onItemSelectIInterceptor?.invoke(oldIndex, newIndex) == true) {
            return
        }

        //没有被拦截
        onItemSelectChangedListener?.invoke(oldIndex, newIndex)
        selectedAnimate(getChildAt(newIndex))
        if (isLegal(oldIndex)) {
            unSelectedAnimate(getChildAt(oldIndex))
        }

        currentIndex = newIndex

        if (isLegal(oldIndex)) {
            updateItem(oldIndex)
        }
        if (isLegal(newIndex)) {
            updateItem(newIndex)
        }
    }

    /**
     * 判断Index是否合法
     */
    private fun isLegal(index: Int): Boolean {
        if (index < 0) {
            return false
        }
        return index < childCount
    }

    private fun updateItem(i: Int) {
        val adapter = mAdapter ?: return
        var holder = findViewHolderByPosition(i)
        if (holder == null) {
            holder = adapter.onCreateViewHolder(this)
            holder.itemView.setTag(R.id.bottom_bar_view_holder_tag_id, holder)
            val layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
            holder.itemView.layoutParams = layoutParams
            holder.itemView.setOnClickListener { v: View? -> setSelectedIndex(i) }
            addView(holder.itemView)
        }
        (adapter as? Adapter<in ViewHolder>)?.apply {
            onBindViewHolder(holder, i, currentIndex)
        }
    }

    private fun updateItems() {
        val adapter = mAdapter ?: return
        for (i in 0 until adapter.getItemCount()) {
            updateItem(i)
        }
        for (i in adapter.getItemCount() until childCount) {
            removeView(getChildAt(i))
        }
    }

    private fun findViewHolderByPosition(i: Int): ViewHolder? {
        return if (!isLegal(i)) {
            null
        } else getChildAt(i).getTag(R.id.bottom_bar_view_holder_tag_id) as? ViewHolder
    }

    private fun selectedAnimate(view: View) {
        view.animate().translationY(-5f).scaleX(1.03f).scaleY(1.03f).setDuration(200).start()
    }

    private fun unSelectedAnimate(view: View) {
        view.animate().translationY(0f).scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
    }


    init {
        orientation = HORIZONTAL
    }

    abstract class Adapter<VH : ViewHolder> {
        abstract fun onBindViewHolder(holder: VH, position: Int, selectedPosition: Int)
        abstract fun onCreateViewHolder(parent: ViewGroup): VH
        abstract fun getItemCount(): Int
    }

    abstract class ViewHolder(val itemView: View) {
    }
}