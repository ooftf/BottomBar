package com.ooftf.bottombar.java;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.Set;


/**
 * Created by master on 2017/9/27 0027.
 */
class FragmentSwitchManager {
    private FragmentManager manager;
    private int containerViewId;
    private Set<String> tags;
    private OnFragmentCommitListener OnFragmentCommitListener;
    private FragmentCreator createFragment;

    public FragmentSwitchManager(@NonNull FragmentManager manager, @NonNull int containerViewId, @NonNull Set<String> tags, @NonNull FragmentCreator creator) {
        this.manager = manager;
        this.createFragment = creator;
        this.containerViewId = containerViewId;
        this.tags = tags;
    }

    public void setOnFragmentCommitListener(OnFragmentCommitListener onFragmentCommitListener) {
        this.OnFragmentCommitListener = onFragmentCommitListener;
    }

    void switchFragment(String tagId) {
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
