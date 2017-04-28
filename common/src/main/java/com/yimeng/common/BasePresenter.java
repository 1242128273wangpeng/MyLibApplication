package com.yimeng.common;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2017/4/21 0021.
 */

public class BasePresenter<M, V extends BaseView> implements Presenter {
    protected V mvpView;
    protected M mModel;

    public BasePresenter(V mvpView, M mModel) {
        this.mvpView = mvpView;
        this.mModel = mModel;
    }

    /**
     * 是否使用eventBus,默认为使用(true)，
     *
     * @return
     */
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void onPresenterStart() {
        //如果要使用eventbus请将此方法实现
        if (useEventBus())//如果要使用eventbus请将此方法返回true
            EventBus.getDefault().register(this);//注册eventbus

    }

    @Override
    public void onPresenterDestroy() {
        //如果要使用eventbus请将此方法实现
        if (useEventBus())//如果要使用eventbus请将此方法返回true
            EventBus.getDefault().unregister(this);//解除注册eventbus
        // 请求网络的时候会长时间持有Activity,Fragment导致内存泄露
        this.mModel = null;
        this.mvpView = null;
    }


}
