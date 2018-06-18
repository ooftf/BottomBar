package com.ooftf.bottombar

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout

class BottomBar(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    init {
        orientation = HORIZONTAL
    }
    var observer = MyDataSetObserver()
    var onItemSelectIInterceptor: OnItemSelectIInterceptor? = null
    var onItemSelectChangedListener: OnItemSelectChangedListener? = null
    var onItemRepeatListener: OnItemRepeatListener? = null
    var adapter: Adapter<out RecyclerView.ViewHolder>? = null
        set(value) {
            adapter?.unregisterAdapterDataObserver(observer)
            field = value
            adapter?.registerAdapterDataObserver(observer)
            createItems()
            setSelectedIndex(0)
        }
    var selectIndex: Int = -1
    private fun createItems() {
        removeAllViews()
        adapter?.let { adapterUnNull ->
            (0 until adapterUnNull.itemCount).forEach { index ->
                val viewHoler = adapterUnNull.createViewHolder(this, adapterUnNull.getItemViewType(index))
                viewHoler.itemView.tag = viewHoler
                (adapterUnNull as Adapter<RecyclerView.ViewHolder>).onBindViewHolder(viewHoler, index, selectIndex)
                addView(viewHoler.itemView)
                (viewHoler.itemView.layoutParams as LayoutParams).weight = 1f
                (viewHoler.itemView.layoutParams as LayoutParams).height = LayoutParams.MATCH_PARENT
                (viewHoler.itemView.layoutParams as LayoutParams).width = 0
                viewHoler.itemView.setOnClickListener {
                    setSelectedIndex(index)
                }
            }
        }
    }

    /**
     * 设置新的选中index
     */
    fun setSelectedIndex(index: Int) {
        if (!isLegal(index)) return
        if (index == selectIndex) {//判断是否重复点击
            onItemRepeatListener?.onItemRepeat(selectIndex)
            return
        }
        if (onItemSelectIInterceptor == null || !onItemSelectIInterceptor!!.onIntercept(selectIndex, index)) {//判断是否拦截
            //没有被拦截
            onItemSelectChangedListener?.onItemSelectChanged(selectIndex, index)
            selectedAnimate(getChildAt(index))
            if (isLegal(selectIndex)) {
                unSelectedAnimate(getChildAt(selectIndex))
            }
            selectIndex = index
            adapter?.notifyDataSetChanged()
        }
    }

    /**
     * 判断Index是否合法
     */
    private fun isLegal(index: Int): Boolean {
        if (index < 0) return false
        if (index >= childCount) return false
        return true
    }

    fun updateItems() {
        (0 until childCount).forEach {
            (adapter as Adapter<RecyclerView.ViewHolder>).onBindViewHolder(getChildAt(it).tag as RecyclerView.ViewHolder, it, selectIndex)
        }
    }

    private fun selectedAnimate(view: View) {
        view.animate().translationY(-5f).scaleX(1.03f).scaleY(1.03f).setDuration(200).start()
    }

    private fun unSelectedAnimate(view: View) {
        view.animate().translationY(0f).scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
    }

    inner class MyDataSetObserver : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            updateItems()
        }

    }

    abstract class Adapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
        final override fun onBindViewHolder(holder: VH, position: Int) {

        }

        abstract fun onBindViewHolder(holder: VH, position: Int, selectedPosition: Int)
    }
}