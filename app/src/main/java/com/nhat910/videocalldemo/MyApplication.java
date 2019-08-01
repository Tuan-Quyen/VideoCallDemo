package com.nhat910.videocalldemo;

import android.app.Application;

import com.nhat910.videocalldemo.others.Constant;
import com.quickblox.auth.session.QBSettings;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initCredentials();

    }

    private void initCredentials() {
        QBSettings.getInstance().init(getApplicationContext(), Constant.APPLICATION_ID, Constant.AUTH_KEY, Constant.AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(Constant.ACCOUNT_KEY);
    }
}
