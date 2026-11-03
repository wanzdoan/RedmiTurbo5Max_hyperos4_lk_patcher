package com.android.services.telephony;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.os.Vibrator;

/* JADX INFO: loaded from: classes.dex */
class EmergencyTonePlayer {
    private static final long[] VIBRATE_PATTERN = {1000, 1000};
    private static final AudioAttributes VIBRATION_ATTRIBUTES = new AudioAttributes.Builder().setContentType(1).setUsage(2).build();
    private final AudioManager mAudioManager;
    private final Context mContext;
    private boolean mIsVibrating = false;
    private final Vibrator mVibrator;

    EmergencyTonePlayer(Context context) {
        this.mContext = context;
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
        this.mVibrator = (Vibrator) context.getSystemService("vibrator");
    }
}
