package com.nhat910.videocalldemo.ui.base;

public class BasePresenter<V extends MvpView> implements IPresenter<V> {
    private V view;

    @Override
    public void onAttach(V mvpView) {
        this.view = mvpView;
    }

    @Override
    public void onDetach() {
        this.view = null;
    }

    @Override
    public boolean isViewAttach() {
        return view != null;
    }

    public V getView() {
        return view;
    }
}
