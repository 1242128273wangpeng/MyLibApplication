package com.yimeng.common;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2017/4/21 0021.
 */

public abstract class MVPFragment<P extends BasePresenter> extends BaseFragment {
    protected P mvpPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected abstract P createPresenter();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mvpPresenter != null) {
            mvpPresenter.onPresenterDestroy();
        }
    }

}
