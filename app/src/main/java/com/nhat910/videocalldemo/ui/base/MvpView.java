package com.nhat910.videocalldemo.ui.base;

public interface MvpView {
    void showLoading();

    void hideLoading();

    void addFragment(BaseFragment fragment, boolean isAddToBackStack);

    void replaceFragment(BaseFragment fragment, boolean isAddToBackStack);

}
