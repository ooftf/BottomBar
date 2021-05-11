package com.ooftf.bottombar.java;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Set;


/**
 * Created by master on 2017/9/27 0027.
 * 要保证T的类型在内存回收之后再次创建其toString值不会改变
 */
public class FragmentSwitchManager<T> {
    private FragmentManager manager;
    private int containerViewId;
    private Set<T> tags;
    private OnFragmentCommitListener<T> OnFragmentCommitListener;
    private FragmentCreator<T> createFragment;

    public FragmentSwitchManager(@NonNull FragmentManager manager, @NonNull int containerViewId, @NonNull Set<T> tags, @NonNull FragmentCreator<T> creator) {
        this.manager = manager;
        this.createFragment = creator;
        this.containerViewId = containerViewId;
        this.tags = tags;
    }


    public void setOnFragmentCommitListener(OnFragmentCommitListener<T> onFragmentCommitListener) {
        this.OnFragmentCommitListener = onFragmentCommitListener;
    }

    public void switchFragment(T tagId) {
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        hideOther(fragmentTransaction, tagId);
        Fragment fragment = getFragment(fragmentTransaction, tagId);
        if (OnFragmentCommitListener != null) {
            OnFragmentCommitListener.onFragmentCommit(tagId, fragment);
        }
        fragmentTransaction.show(fragment).commitAllowingStateLoss();
        manager.executePendingTransactions();
    }

    private void hideOther(FragmentTransaction fragmentTransaction, T tagId) {
        for (T tag : tags) {
            if (tag.equals(tagId)) {
                continue;
            }
            Fragment fragment = manager.findFragmentByTag(tag.toString());
            if (fragment != null) {
                if (!fragment.isAdded()) {
                    fragmentTransaction.add(containerViewId, fragment, tag.toString());
                }
                fragmentTransaction.hide(fragment);
            }

        }
    }

    private Fragment getFragment(FragmentTransaction fragmentTransaction, T tagId) {
        Fragment fragment = manager.findFragmentByTag(tagId.toString());
        if (null == fragment) {
            fragment = createFragment.create(tagId);
        }
        if (fragment != null && !fragment.isAdded()) {
            fragmentTransaction.add(containerViewId, fragment, tagId.toString());
        }
        return fragment;
    }
}
