package com.ooftf.bottombar

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import javax.security.auth.login.LoginException

/**
 * Created by master on 2017/9/27 0027.
 * 要保证T的类型在内存回收之后再次创建其toString值不会改变
 */
class FragmentSwitchManager<T>(
    private val manager: FragmentManager,
    private val containerViewId: Int,
    private val createFragment: (T) -> Fragment
) {
    private val fragmentTemp = HashMap<T, Fragment>()
    private val handler by lazy {
        Handler(Looper.getMainLooper())
    }


    fun switchFragment(tagId: T) {
        var isCreateFragment = false
        val fragmentTransaction = manager.beginTransaction()
        var fragment = findAddFragment(tagId)
        if (null == fragment) {
            isCreateFragment = true
            fragment = createFragment(tagId)
            fragmentTemp[tagId] = fragment
            fragmentTransaction.add(containerViewId, fragment, tagId.toString())
        }
        hideOther(fragmentTransaction, fragment)
        fragmentTransaction.show(fragment).commitAllowingStateLoss()
        if (isCreateFragment) {
            handler.post {
                fragmentTemp.remove(tagId)
            }
        }
    }

    private fun hideOther(fragmentTransaction: FragmentTransaction, exclude: Fragment) {
        manager.fragments.forEach {
            if (getFragmentContainerId(it) == containerViewId && it != exclude) {
                fragmentTransaction.hide(it)
            }
        }
        fragmentTemp.values.forEach {
            if (getFragmentContainerId(it) == containerViewId && it != exclude) {
                fragmentTransaction.hide(it)
            }
        }
    }

    val mContainerIdField = Fragment::class.java.getDeclaredField("mContainerId").apply {
        isAccessible = true
    }

    private fun getFragmentContainerId(fragment: Fragment): Int {
        return mContainerIdField.getInt(fragment)
    }

    private fun findAddFragment(tagId: T): Fragment? {
        var fragment = manager.findFragmentByTag(tagId.toString())
        if (null == fragment) {
            fragment = fragmentTemp[tagId]
        }
        return fragment
    }
}