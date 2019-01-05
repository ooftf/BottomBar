package com.ooftf.bottombar.java;

import android.support.v4.app.Fragment;

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/1/5 0005
 */
public interface OnFragmentCommitListener {
    /**
     * frament创建之后，提交之前调用
     *
     * @param tag
     * @param fragment
     */
    void onFragmentCommit(String tag, Fragment fragment);
}