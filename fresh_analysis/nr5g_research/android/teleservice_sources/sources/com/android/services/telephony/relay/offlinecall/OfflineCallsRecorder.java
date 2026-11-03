package com.android.services.telephony.relay.offlinecall;

import android.media.AudioFormat;
import android.media.AudioRecord;
import com.android.telephony.Rlog;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public class OfflineCallsRecorder {
    private static final int AUDIO_DATA_LENGTH_16kHZ = 640;
    private static final int AUDIO_DATA_LENGTH_8KHZ = 320;
    private static final String LOG_TAG = "OfflineCallsRecorder";
    private static final int THREAD_JOIN_TIMEOUT = 1000;
    private static boolean mIsRecorderWorking = false;
    private static boolean mIsThreadRunning = false;
    private AudioRecord mRecorder;
    private RecorderThread mRecorderThread;
    private final Consumer mResultConsumer;
    private int sampleRate = OfflineUtils.Offline.SAMPLE_RATE_VALUE_8KHZ;
    private int audioDataLength = AUDIO_DATA_LENGTH_8KHZ;

    public OfflineCallsRecorder(Consumer consumer) {
        this.mResultConsumer = consumer;
        init();
    }

    private void init() {
        int sampleRate = OfflineUtils.Offline.getSampleRate();
        this.sampleRate = sampleRate;
        if (sampleRate == 16000) {
            this.audioDataLength = AUDIO_DATA_LENGTH_16kHZ;
        }
        Rlog.d(LOG_TAG, "init sampleRate = " + this.sampleRate + ",audioDataLength = " + this.audioDataLength);
        int minBufferSize = AudioRecord.getMinBufferSize(this.sampleRate, 16, 2);
        StringBuilder sb = new StringBuilder("minBufferSize = ");
        sb.append(minBufferSize);
        Rlog.d(LOG_TAG, sb.toString());
        if (this.mRecorder == null) {
            this.mRecorder = new AudioRecord.Builder().setAudioFormat(new AudioFormat.Builder().setSampleRate(this.sampleRate).setChannelMask(16).setEncoding(2).build()).setAudioSource(7).setBufferSizeInBytes(minBufferSize).build();
        }
        initRecorderHandler();
    }

    private void initRecorderHandler() {
        RecorderThread recorderThread = new RecorderThread();
        this.mRecorderThread = recorderThread;
        recorderThread.setName("OfflineCallsRecorderThread");
    }

    class RecorderThread extends Thread {
        private byte[] recordDataBytes;

        private RecorderThread() {
            this.recordDataBytes = new byte[OfflineCallsRecorder.this.audioDataLength];
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            super.run();
            while (OfflineCallsRecorder.mIsThreadRunning) {
                try {
                    if (OfflineCallsRecorder.mIsRecorderWorking) {
                        if (Thread.currentThread().isInterrupted()) {
                            Rlog.i(OfflineCallsRecorder.LOG_TAG, "RecorderThread Interrupted");
                            return;
                        }
                        AudioRecord audioRecord = OfflineCallsRecorder.this.mRecorder;
                        byte[] bArr = this.recordDataBytes;
                        int i = audioRecord.read(bArr, 0, bArr.length, 0);
                        if (i <= 0) {
                            Rlog.i(OfflineCallsRecorder.LOG_TAG, "RecorderThread readResult = " + i);
                            return;
                        }
                        if (OfflineCallsRecorder.this.mResultConsumer != null) {
                            OfflineCallsRecorder.this.mResultConsumer.accept(this.recordDataBytes);
                        }
                    }
                } catch (Exception e) {
                    Rlog.e(OfflineCallsRecorder.LOG_TAG, "RecorderThread error = " + e);
                    return;
                }
            }
        }
    }

    public void startRecorder() {
        RecorderThread recorderThread;
        if (mIsRecorderWorking) {
            Rlog.i(LOG_TAG, "startRecorder mIsRecorderWorking = " + mIsRecorderWorking);
            return;
        }
        try {
            if (!mIsThreadRunning && (recorderThread = this.mRecorderThread) != null && recorderThread.getState() == Thread.State.NEW) {
                mIsThreadRunning = true;
                this.mRecorderThread.start();
            }
            AudioRecord audioRecord = this.mRecorder;
            if (audioRecord != null) {
                audioRecord.startRecording();
                mIsRecorderWorking = true;
            }
        } catch (Exception e) {
            Rlog.e(LOG_TAG, "startRecorder error = " + e);
        }
    }

    public void stopRecorder() {
        if (!mIsRecorderWorking) {
            Rlog.i(LOG_TAG, "stopRecorder mIsRecorderWorking = " + mIsRecorderWorking);
            return;
        }
        try {
            AudioRecord audioRecord = this.mRecorder;
            if (audioRecord != null) {
                audioRecord.stop();
                mIsRecorderWorking = false;
            }
        } catch (Exception e) {
            Rlog.e(LOG_TAG, "stopRecorder error = " + e);
        }
    }

    public void release() {
        try {
            AudioRecord audioRecord = this.mRecorder;
            if (audioRecord != null) {
                audioRecord.stop();
                this.mRecorder.release();
                this.mRecorder = null;
                mIsThreadRunning = false;
                mIsRecorderWorking = false;
                this.mRecorderThread.interrupt();
                this.mRecorderThread.join(1000L);
            }
        } catch (Exception e) {
            Rlog.e(LOG_TAG, "release error = " + e);
        }
    }
}
