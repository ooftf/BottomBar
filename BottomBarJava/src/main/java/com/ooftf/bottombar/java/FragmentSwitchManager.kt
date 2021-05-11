package com.ooftf.bottombar.java

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * Created by master on 2017/9/27 0027.
 * 要保证T的类型在内存回收之后再次创建其toString值不会改变
 */
class FragmentSwitchManager<T>(
    private val manager: FragmentManager,
    private val containerViewId: Int,
    private val tags: Set<T>,
    private val createFragment: FragmentCreator<T>
) {
    private val fragmentTemp = HashMap<T, Fragment>()
    private val handler by lazy {
        Handler(Looper.getMainLooper())
    }


    fun switchFragment(tagId: T) {
        val fragmentTransaction = manager.beginTransaction()
        hideOther(fragmentTransaction, tagId)
        var fragment = findAddFragment(tagId)
        if (null == fragment) {
            fragment = createFragment.create(tagId)
            fragmentTemp[tagId] = fragment
            fragmentTransaction.add(containerViewId, fragment, tagId.toString())
        }
        fragmentTransaction.show(fragment).commitAllowingStateLoss()
        handler.post {
            fragmentTemp.remove(tagId)
        }
    }

    private fun hideOther(fragmentTransaction: FragmentTransaction, tagId: T) {
        for (tag in tags) {
            if (tag == tagId) {
                continue
            }
            findAddFragment(tag)?.let {
                fragmentTransaction.hide(it)
            }
        }
    }

    private fun findAddFragment(tagId: T): Fragment? {
        var fragment = manager.findFragmentByTag(tagId.toString())
        if (null == fragment) {
            fragment = fragmentTemp[tagId]
        }
        return fragment
    }
}