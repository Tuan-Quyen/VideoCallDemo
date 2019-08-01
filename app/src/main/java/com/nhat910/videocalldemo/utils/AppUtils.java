package com.nhat910.videocalldemo.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;

import com.nhat910.videocalldemo.R;
import com.nhat910.videocalldemo.interfaces.ConfirmListener;
import com.nhat910.videocalldemo.others.Constant;
import com.nhat910.videocalldemo.others.MessageDialog;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AppUtils {
    public static boolean isVideoHangUp = false;
    public static QBRTCVideoTrack qbrtcVideoTrackLocal,qbrtcVideoTrackRemote;

    public static void showDialogMessage(Context context, String title, String content, @Nullable ConfirmListener listener) {
        MessageDialog messageDialog = new MessageDialog(context, title, content, listener);
        messageDialog.show();
    }

    @SuppressLint("SimpleDateFormat")
    public static String convertTime(Context context, long dataTime) {
        long now = System.currentTimeMillis() / 1000;
        long timetemp = now - dataTime;
        if (timetemp < 60) {
            return context.getString(R.string.few_second_ago);
        } else if (timetemp < 86400 && timetemp > 60) {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            return format.format(dataTime * 1000);
        } else {
             SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return format.format(dataTime * 1000);
        }
    }

    public static List<String> grenerateHomeMenu(){
        List<String> _list = new ArrayList<>();
        _list.add(Constant.LOG_OUT);
        return _list;
    }
}
