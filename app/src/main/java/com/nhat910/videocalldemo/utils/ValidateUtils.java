package com.nhat910.videocalldemo.utils;

import android.content.Context;
import android.text.TextUtils;

import com.nhat910.videocalldemo.R;

public class ValidateUtils {
    public static boolean validateLogin(Context context, String userName, String passWord) {
        if (TextUtils.isEmpty(userName)) {
            AppUtils.showDialogMessage(context, context.getString(R.string.login), context.getString(R.string.empty_username_content), null);
            return false;
        }
        if (TextUtils.isEmpty(passWord)) {
            AppUtils.showDialogMessage(context, context.getString(R.string.login), context.getString(R.string.empty_password_content), null);
            return false;
        }
        return true;
    }

    public static boolean validateSignUp(Context context, String fullName, String userName, String passWord, String confirmPassWord, String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (TextUtils.isEmpty(fullName)) {
            AppUtils.showDialogMessage(context, context.getString(R.string.signup), context.getString(R.string.empty_fullname_content), null);
            return false;
        }
        if (TextUtils.isEmpty(userName)) {
            AppUtils.showDialogMessage(context, context.getString(R.string.signup), context.getString(R.string.empty_username_content), null);
            return false;
        }
        if (TextUtils.isEmpty(passWord)) {
            AppUtils.showDialogMessage(context, context.getString(R.string.signup), context.getString(R.string.empty_password_content), null);
            return false;
        }
        if (passWord.length() < 8) {
            AppUtils.showDialogMessage(context, context.getString(R.string.signup), context.getString(R.string.more_8_length_content), null);
            return false;
        }
        if (!passWord.equalsIgnoreCase(confirmPassWord)) {
            AppUtils.showDialogMessage(context, context.getString(R.string.signup), context.getString(R.string.uncorrect_password), null);
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            AppUtils.showDialogMessage(context, context.getString(R.string.signup), context.getString(R.string.empty_email_content), null);
            return false;
        }
        if (!email.matches(emailPattern)) {
            AppUtils.showDialogMessage(context, context.getString(R.string.signup), context.getString(R.string.invaild_email), null);
            return false;
        }
        return true;
    }
}
