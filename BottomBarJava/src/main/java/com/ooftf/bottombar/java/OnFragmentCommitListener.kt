package com.ooftf.bottombar.java

import androidx.fragment.app.Fragment

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/1/5 0005
 */
interface OnFragmentCommitListener<T> {
    /**
     * frament创建之后，提交之前调用
     *
     * @param tag
     * @param fragment
     */
    fun onFragmentCommit(tag: T, fragment: Fragment?)
}