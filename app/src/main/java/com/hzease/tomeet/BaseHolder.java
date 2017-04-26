package com.hzease.tomeet;

import android.view.View;

/**
 * 其实可以看做MVP模式中的Presenter
 */
public abstract class BaseHolder<T> {
    public View holderView;//注意：一开始就将convertView转移到holder中用一个变量表示了
    public BaseHolder(){
        holderView = initHolderView();//需要一个holderview
        //1. 设置tag
        holderView.setTag(this);
    }

    public abstract View initHolderView();
    public abstract void bindData(T t);


    public View getHolderView(){
        return holderView;
    }
}
