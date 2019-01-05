package com.ooftf.bottombar.java;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.HashSet;
import java.util.Set;


/**
 * Created by master on 2017/9/27 0027.
 */
class FragmentSwitchManager {
    private FragmentManager manager;
    private int containerViewId;
    private Set<String> tags = new HashSet<>();
    private com.ooftf.bottombar.java.OnFragmentCommitListener OnFragmentCommitListener;
    private FragmentCreator createFragment;

    public FragmentSwitchManager(FragmentManager manager, int containerViewId, FragmentCreator creator) {
        this.manager = manager;
        this.createFragment = creator;
        this.containerViewId = containerViewId;
    }

    public void setOnFragmentCommitListener(com.ooftf.bottombar.java.OnFragmentCommitListener onFragmentCommitListener) {
        this.OnFragmentCommitListener = onFragmentCommitListener;
    }

    void switchFragment(String tagId) {
        tags.add(tagId);
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        hideOther(fragmentTransaction, tagId);
        Fragment fragment = getFragment(fragmentTransaction, tagId);
        if (OnFragmentCommitListener != null) {
            OnFragmentCommitListener.onFragmentCommit(tagId, fragment);
        }
        fragmentTransaction.show(fragment).commitAllowingStateLoss();
    }

    private void hideOther(FragmentTransaction fragmentTransaction, String tagId) {
        for (String tag : tags) {
            if (tag == tagId) {
                continue;
            }
            Fragment fragment = manager.findFragmentByTag(tag);
            if (fragment != null) {
                fragmentTransaction.hide(fragment);
            }

        }
    }

    private Fragment getFragment(FragmentTransaction fragmentTransaction, String tagId) {
        Fragment fragment = manager.findFragmentByTag(tagId);
        if (null == fragment) {
            fragment = createFragment.create(tagId);
            fragmentTransaction.add(containerViewId, fragment, tagId);
        }
        return fragment;
    }
}
