package com.nhat910.videocalldemo.ui.base;

public interface IPresenter <V extends MvpView> {
    void onAttach(V mvpView);

    void onDetach();

    boolean isViewAttach();
}

