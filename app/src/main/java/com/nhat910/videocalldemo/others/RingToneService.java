package com.nhat910.videocalldemo.others;

import android.app.Activity;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.nhat910.videocalldemo.R;

import java.io.IOException;

public class RingToneService {
    private Vibrator vibrator;
    private MediaPlayer mp;

    public RingToneService(Activity activity){
        vibrator = (Vibrator) activity.getSystemService(activity.getBaseContext().VIBRATOR_SERVICE);
        Uri ringToneUri = Uri.parse("android.resource://" + activity.getPackageName() + "/" + R.raw.ringtone);
        mp = new MediaPlayer();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes.Builder audioAttributesBuilder = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION_SIGNALLING);

            mp.setAudioAttributes(audioAttributesBuilder.build());
        } else {
            mp.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        }

        try {
            mp.setDataSource(activity.getApplicationContext(), ringToneUri);
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playService(){
        long[] pattern = {0, 1000, 500};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern,0));
        }else{
            vibrator.vibrate(pattern, 0);
        }
        mp.start();
        mp.setLooping(true);
    }

    public void stopService(){
        vibrator.cancel();
        mp.stop();
    }
}
