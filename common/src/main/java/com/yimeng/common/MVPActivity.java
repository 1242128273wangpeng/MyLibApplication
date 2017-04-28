package com.yimeng.common;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Administrator on 2017/4/21 0021.
 */

public abstract class MVPActivity<P extends BasePresenter> extends BaseActivity implements BaseView {
    protected P mvpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        createPresenter();
        super.onCreate(savedInstanceState);
        mvpPresenter.onPresenterStart();
    }

    protected abstract void createPresenter();

    @Override
    protected void onDestroy() {
        if (mvpPresenter != null) {
            mvpPresenter.onPresenterDestroy();
        }
        super.onDestroy();
    }

}
