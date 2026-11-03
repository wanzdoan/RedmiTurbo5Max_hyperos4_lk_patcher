package com.android.services.telephony.relay.offlinecall;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.HandlerThread;
import com.android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class OfflineCallsPlayer {
    private static final String LOG_TAG = "OfflineCallsPlayer";
    public static final int MAX_AUDIO_COUNT = 3;
    public static int mAudioCount;
    private AudioTrack mPlayer;
    private Handler mPlayerHandler;
    private int sampleRate = OfflineUtils.Offline.SAMPLE_RATE_VALUE_8KHZ;

    public OfflineCallsPlayer() {
        init();
    }

    private void init() {
        this.sampleRate = OfflineUtils.Offline.getSampleRate();
        Rlog.d(LOG_TAG, "init sampleRate = " + this.sampleRate);
        int minBufferSize = AudioTrack.getMinBufferSize(this.sampleRate, 4, 2);
        Rlog.d(LOG_TAG, "init minBufferSize = " + minBufferSize);
        if (this.mPlayer == null) {
            this.mPlayer = new AudioTrack.Builder().setAudioAttributes(new AudioAttributes.Builder().setFlags(2304).setUsage(2).setContentType(1).build()).setAudioFormat(new AudioFormat.Builder().setEncoding(2).setSampleRate(this.sampleRate).setChannelMask(4).build()).setBufferSizeInBytes(minBufferSize).setTransferMode(1).build();
        }
        this.mPlayerHandler = startHandler("OfflineCallsPlayerThread");
    }

    private Handler startHandler(String str) {
        HandlerThread handlerThread = new HandlerThread(str);
        handlerThread.start();
        return new Handler(handlerThread.getLooper());
    }

    public void startPlayer(final byte[] bArr) {
        Handler handler = this.mPlayerHandler;
        if (handler != null) {
            handler.post(new Runnable() { // from class: com.android.services.telephony.relay.offlinecall.OfflineCallsPlayer.1
                @Override // java.lang.Runnable
                public void run() {
                    OfflineCallsPlayer.this.play(bArr);
                    int i = OfflineCallsPlayer.mAudioCount;
                    if (i > 0) {
                        OfflineCallsPlayer.mAudioCount = i - 1;
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void play(byte[] bArr) {
        try {
            AudioTrack audioTrack = this.mPlayer;
            if (audioTrack == null || audioTrack.write(bArr, 0, bArr.length) < 0) {
                return;
            }
            this.mPlayer.play();
        } catch (Exception e) {
            Rlog.e(LOG_TAG, "play error = " + e);
        }
    }

    public void stopPlayer() {
        try {
            AudioTrack audioTrack = this.mPlayer;
            if (audioTrack != null) {
                audioTrack.stop();
                mAudioCount = 0;
            }
        } catch (Exception e) {
            Rlog.e(LOG_TAG, "stopPlayer error = " + e);
        }
    }

    public void release() {
        try {
            AudioTrack audioTrack = this.mPlayer;
            if (audioTrack != null) {
                audioTrack.stop();
                this.mPlayer.release();
                this.mPlayer = null;
                mAudioCount = 0;
            }
        } catch (Exception e) {
            Rlog.e(LOG_TAG, "stopPlayer error = " + e);
        }
    }
}
