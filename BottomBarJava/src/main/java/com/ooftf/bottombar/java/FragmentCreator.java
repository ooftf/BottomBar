package com.ooftf.bottombar.java;

import android.support.v4.app.Fragment;

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/1/5 0005
 */
public interface FragmentCreator {
    /**
     * 创建fragment
     * @param tag
     * @return
     */
    Fragment create(String tag);
}