package com.ooftf.bottombar.java

import androidx.fragment.app.Fragment

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/1/5 0005
 */
interface FragmentCreator<T> {
    /**
     * 创建fragment
     * @param tag
     * @return
     */
    fun create(tag: T): Fragment
}