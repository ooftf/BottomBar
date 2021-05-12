package com.ooftf.bottombar.holder

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ooftf.arch.frame.mvvm.activity.BaseActivity
import com.ooftf.arch.frame.mvvm.activity.BaseBindingActivity
import com.ooftf.bottombar.BottomBar
import com.ooftf.bottombar.FragmentSwitchManager
import com.ooftf.bottombar.holder.databinding.ActivityMainBinding

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2021/5/12
 */
class MainActivity2 : BaseBindingActivity<ActivityMainBinding>() {
    var isIntercept = false
    val fsm by lazy {
        FragmentSwitchManager<Int>(supportFragmentManager, R.id.text) {
            MyFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.bottomBar.onItemSelectChangedListener = { oldIndex: Int?, newIndex: Int ->
            fsm.switchFragment(newIndex)
        }
        val adapter: BottomBar.Adapter<ViewHolder> = object : BottomBar.Adapter<ViewHolder>() {
            override fun onBindViewHolder(
                holder: ViewHolder,
                position: Int,
                selectedPosition: Int
            ) {
                if (position == selectedPosition) {
                    holder.title.setTextColor(Color.parseColor("#2196F3"))
                    when (position) {
                        0 -> {
                            holder.title.text = "First"
                            holder.icon.setImageResource(R.drawable.ic_app_selected_24dp)
                        }
                        1 -> {
                            holder.title.text = "Second"
                            holder.icon.setImageResource(R.drawable.ic_debug_selected_24dp)
                        }
                    }
                } else {
                    holder.title.setTextColor(Color.parseColor("#000000"))
                    when (position) {
                        0 -> {
                            holder.title.text = "First"
                            holder.icon.setImageResource(R.drawable.ic_app_24dp)
                        }
                        1 -> {
                            holder.title.text = "Second"
                            holder.icon.setImageResource(R.drawable.ic_debug_24dp)
                        }
                    }
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
                return ViewHolder(layoutInflater.inflate(R.layout.item_bottombar, parent, false))
            }

            override fun getItemCount(): Int {
                return 2
            }

        }
        binding.bottomBar.setAdapter(adapter)
        binding.bottomBar.setSelectedIndex(0)
    }

    internal inner class ViewHolder(itemView: View) : BottomBar.ViewHolder(itemView) {
        var title: TextView
        var icon: ImageView

        init {
            title = itemView.findViewById(R.id.title)
            icon = itemView.findViewById(R.id.icon)
        }
    }
}