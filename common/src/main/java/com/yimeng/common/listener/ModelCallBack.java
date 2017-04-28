package com.yimeng.common.listener;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public interface ModelCallBack<T> {
    public void success(T rootsData);

    public void fail(String error);
}
