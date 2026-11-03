package com.android.internal.telephony.uicc.euicc.apdu;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.telephony.IccOpenLogicalChannelResponse;
import android.util.Base64;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Phone$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.euicc.EuiccSession;
import com.android.internal.telephony.uicc.IccIoResult;
import com.android.internal.telephony.uicc.euicc.async.AsyncResultCallback;
import com.android.internal.telephony.uicc.euicc.async.AsyncResultHelper;
import com.android.internal.telephony.util.DnsPacket;
import com.android.telephony.Rlog;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ApduSender {
    private final String mAid;
    private boolean mChannelInUse;
    private final Object mChannelInUseLock = new Object();
    private final String mChannelKey;
    private boolean mChannelOpened;
    private final String mChannelResponseKey;
    private final CloseLogicalChannelInvocation mCloseChannel;
    private final Context mContext;
    private final Handler mHandler;
    private final String mLogTag;
    private final OpenLogicalChannelInvocation mOpenChannel;
    private final boolean mSupportExtendedApdu;
    private final TransmitApduLogicalChannelInvocation mTransmitApdu;

    public ApduSender(Context context, int i, CommandsInterface commandsInterface, String str, boolean z) {
        if (!str.equals("A0000005591010FFFFFFFF8900000100") && !"user".equals(Build.TYPE)) {
            Phone$$ExternalSyntheticBUOutline0.m("Only ISD-R AID is supported.");
            throw null;
        }
        this.mLogTag = "ApduSender-" + i;
        this.mAid = str;
        this.mContext = context;
        this.mSupportExtendedApdu = z;
        this.mOpenChannel = new OpenLogicalChannelInvocation(commandsInterface);
        this.mCloseChannel = new CloseLogicalChannelInvocation(commandsInterface);
        this.mTransmitApdu = new TransmitApduLogicalChannelInvocation(commandsInterface);
        this.mChannelKey = "esim-channel_" + i;
        this.mChannelResponseKey = "esim-res-id_" + i;
        this.mHandler = new Handler();
        this.mChannelInUse = false;
    }

    public void send(RequestProvider requestProvider, ApduSenderResultCallback apduSenderResultCallback, Handler handler) {
        if (!acquireChannelLock()) {
            AsyncResultHelper.throwException(new ApduException("The logical channel is still in use."), apduSenderResultCallback, handler);
            return;
        }
        boolean zHasSession = EuiccSession.get(this.mContext).hasSession();
        if (this.mChannelOpened) {
            if (zHasSession) {
                EuiccSession.get(this.mContext).noteChannelOpen(this);
            }
            RequestBuilder requestBuilderWithOpenedChannel = getRequestBuilderWithOpenedChannel(requestProvider, !zHasSession, apduSenderResultCallback, handler);
            if (requestBuilderWithOpenedChannel == null) {
                return;
            }
            sendCommand(requestBuilderWithOpenedChannel.getCommands(), 0, !zHasSession, apduSenderResultCallback, handler);
            return;
        }
        if (zHasSession) {
            EuiccSession.get(this.mContext).noteChannelOpen(this);
        }
        openChannel(requestProvider, !zHasSession, apduSenderResultCallback, handler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public RequestBuilder getRequestBuilderWithOpenedChannel(RequestProvider requestProvider, boolean z, ApduSenderResultCallback apduSenderResultCallback, Handler handler) {
        Throwable th;
        int i = PreferenceManager.getDefaultSharedPreferences(this.mContext).getInt(this.mChannelKey, -1);
        byte[] bArrDecode = Base64.decode(PreferenceManager.getDefaultSharedPreferences(this.mContext).getString(this.mChannelResponseKey, ""), 0);
        RequestBuilder requestBuilder = new RequestBuilder(i, this.mSupportExtendedApdu);
        try {
            requestProvider.buildRequest(bArrDecode, requestBuilder);
            th = null;
        } catch (Throwable th2) {
            th = th2;
        }
        if (!requestBuilder.getCommands().isEmpty() && th == null) {
            return requestBuilder;
        }
        logd("Release as commands are empty or exception occurred");
        returnRespnseOrException(i, z, null, th, apduSenderResultCallback, handler);
        return null;
    }

    private void openChannel(final RequestProvider requestProvider, final boolean z, final ApduSenderResultCallback apduSenderResultCallback, final Handler handler) {
        this.mOpenChannel.invoke(this.mAid, new AsyncResultCallback<IccOpenLogicalChannelResponse>() { // from class: com.android.internal.telephony.uicc.euicc.apdu.ApduSender.1
            @Override // com.android.internal.telephony.uicc.euicc.async.AsyncResultCallback
            public void onResult(IccOpenLogicalChannelResponse iccOpenLogicalChannelResponse) {
                int i;
                int channel = iccOpenLogicalChannelResponse.getChannel();
                int status = iccOpenLogicalChannelResponse.getStatus();
                byte[] selectResponse = iccOpenLogicalChannelResponse.getSelectResponse();
                if (status != 3 || (channel = PreferenceManager.getDefaultSharedPreferences(ApduSender.this.mContext).getInt(ApduSender.this.mChannelKey, -1)) == -1) {
                    i = channel;
                } else {
                    ApduSender.this.logv("Try to use already opened channel: " + channel);
                    selectResponse = Base64.decode(PreferenceManager.getDefaultSharedPreferences(ApduSender.this.mContext).getString(ApduSender.this.mChannelResponseKey, ""), 0);
                    i = channel;
                    status = 1;
                }
                if (i == -1 || status != 1) {
                    ApduSender.this.mChannelOpened = false;
                    ApduSender.this.returnRespnseOrException(i, false, null, new ApduException("Failed to open logical channel for AID: " + ApduSender.this.mAid + ", with status: " + status), apduSenderResultCallback, handler);
                    return;
                }
                PreferenceManager.getDefaultSharedPreferences(ApduSender.this.mContext).edit().putInt(ApduSender.this.mChannelKey, i).putString(ApduSender.this.mChannelResponseKey, Base64.encodeToString(selectResponse, 0)).apply();
                ApduSender.this.mChannelOpened = true;
                RequestBuilder requestBuilderWithOpenedChannel = ApduSender.this.getRequestBuilderWithOpenedChannel(requestProvider, z, apduSenderResultCallback, handler);
                if (requestBuilderWithOpenedChannel == null) {
                    return;
                }
                ApduSender.this.sendCommand(requestBuilderWithOpenedChannel.getCommands(), 0, z, apduSenderResultCallback, handler);
            }
        }, handler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendCommand(final List list, final int i, final boolean z, final ApduSenderResultCallback apduSenderResultCallback, final Handler handler) {
        final ApduCommand apduCommand = (ApduCommand) list.get(i);
        this.mTransmitApdu.invoke(apduCommand, new AsyncResultCallback<IccIoResult>() { // from class: com.android.internal.telephony.uicc.euicc.apdu.ApduSender.2
            @Override // com.android.internal.telephony.uicc.euicc.async.AsyncResultCallback
            public void onResult(IccIoResult iccIoResult) {
                ApduSender.this.getCompleteResponse(apduCommand.channel, iccIoResult, null, new AsyncResultCallback<IccIoResult>() { // from class: com.android.internal.telephony.uicc.euicc.apdu.ApduSender.2.1
                    @Override // com.android.internal.telephony.uicc.euicc.async.AsyncResultCallback
                    public void onResult(IccIoResult iccIoResult2) {
                        ApduSender.this.logv("Full APDU response: " + iccIoResult2);
                        int i2 = iccIoResult2.sw1;
                        int i3 = (i2 << 8) | iccIoResult2.sw2;
                        if (i3 != 36864 && i2 != 145) {
                            AnonymousClass2 anonymousClass2 = AnonymousClass2.this;
                            ApduSender apduSender = ApduSender.this;
                            int i4 = apduCommand.channel;
                            ApduException apduException = new ApduException(i3);
                            AnonymousClass2 anonymousClass3 = AnonymousClass2.this;
                            apduSender.returnRespnseOrException(i4, true, null, apduException, apduSenderResultCallback, handler);
                            return;
                        }
                        AnonymousClass2 anonymousClass4 = AnonymousClass2.this;
                        if (i < list.size() - 1 && apduSenderResultCallback.shouldContinueOnIntermediateResult(iccIoResult2)) {
                            AnonymousClass2 anonymousClass5 = AnonymousClass2.this;
                            ApduSender.this.sendCommand(list, i + 1, z, apduSenderResultCallback, handler);
                        } else {
                            AnonymousClass2 anonymousClass6 = AnonymousClass2.this;
                            ApduSender.this.returnRespnseOrException(apduCommand.channel, z, iccIoResult2.payload, null, apduSenderResultCallback, handler);
                        }
                    }
                }, handler);
            }
        }, handler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getCompleteResponse(final int i, IccIoResult iccIoResult, ByteArrayOutputStream byteArrayOutputStream, final AsyncResultCallback asyncResultCallback, final Handler handler) {
        final ByteArrayOutputStream byteArrayOutputStream2 = byteArrayOutputStream == null ? new ByteArrayOutputStream() : byteArrayOutputStream;
        byte[] bArr = iccIoResult.payload;
        if (bArr != null) {
            try {
                byteArrayOutputStream2.write(bArr);
            } catch (IOException unused) {
            }
        }
        if (iccIoResult.sw1 != 97) {
            iccIoResult.payload = byteArrayOutputStream2.toByteArray();
            asyncResultCallback.onResult(iccIoResult);
        } else {
            this.mTransmitApdu.invoke(new ApduCommand(i, 0, DnsPacket.DnsRecord.NAME_COMPRESSION, 0, 0, iccIoResult.sw2, ""), new AsyncResultCallback<IccIoResult>() { // from class: com.android.internal.telephony.uicc.euicc.apdu.ApduSender.3
                @Override // com.android.internal.telephony.uicc.euicc.async.AsyncResultCallback
                public void onResult(IccIoResult iccIoResult2) {
                    ApduSender.this.getCompleteResponse(i, iccIoResult2, byteArrayOutputStream2, asyncResultCallback, handler);
                }
            }, handler);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tearDownPreferences() {
        PreferenceManager.getDefaultSharedPreferences(this.mContext).edit().remove(this.mChannelKey).remove(this.mChannelResponseKey).apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void returnRespnseOrException(int i, boolean z, byte[] bArr, Throwable th, ApduSenderResultCallback apduSenderResultCallback, Handler handler) {
        if (z) {
            closeAndReturn(i, bArr, th, apduSenderResultCallback, handler);
        } else {
            releaseChannelLockAndReturn(bArr, th, apduSenderResultCallback, handler);
        }
    }

    private void closeAndReturn(int i, final byte[] bArr, final Throwable th, final ApduSenderResultCallback apduSenderResultCallback, Handler handler) {
        this.mCloseChannel.invoke(Integer.valueOf(i), new AsyncResultCallback<Boolean>() { // from class: com.android.internal.telephony.uicc.euicc.apdu.ApduSender.4
            @Override // com.android.internal.telephony.uicc.euicc.async.AsyncResultCallback
            public void onResult(Boolean bool) {
                ApduSender.this.tearDownPreferences();
                ApduSender.this.mChannelOpened = false;
                ApduSender.this.releaseChannelLock();
                Throwable th2 = th;
                ApduSenderResultCallback apduSenderResultCallback2 = apduSenderResultCallback;
                if (th2 == null) {
                    apduSenderResultCallback2.onResult(bArr);
                } else {
                    apduSenderResultCallback2.onException(th2);
                }
            }
        }, handler);
    }

    public void closeAnyOpenChannel() {
        if (!acquireChannelLock()) {
            loge("[closeAnyOpenChannel] failed to acquire channel lock");
            return;
        }
        final int i = PreferenceManager.getDefaultSharedPreferences(this.mContext).getInt(this.mChannelKey, -1);
        if (i == -1) {
            releaseChannelLock();
            return;
        }
        logv("[closeAnyOpenChannel] closing the open channel : " + i);
        this.mCloseChannel.invoke(Integer.valueOf(i), new AsyncResultCallback<Boolean>() { // from class: com.android.internal.telephony.uicc.euicc.apdu.ApduSender.5
            @Override // com.android.internal.telephony.uicc.euicc.async.AsyncResultCallback
            public void onResult(Boolean bool) {
                if (bool.booleanValue()) {
                    ApduSender.this.logv("[closeAnyOpenChannel] Channel closed successfully: " + i);
                    ApduSender.this.tearDownPreferences();
                }
                ApduSender.this.mChannelOpened = false;
                ApduSender.this.releaseChannelLock();
            }
        }, this.mHandler);
    }

    private void releaseChannelLockAndReturn(final byte[] bArr, final Throwable th, final ApduSenderResultCallback apduSenderResultCallback, Handler handler) {
        handler.post(new Runnable() { // from class: com.android.internal.telephony.uicc.euicc.apdu.ApduSender$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$releaseChannelLockAndReturn$0(th, apduSenderResultCallback, bArr);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$releaseChannelLockAndReturn$0(Throwable th, ApduSenderResultCallback apduSenderResultCallback, byte[] bArr) {
        releaseChannelLock();
        if (th == null) {
            apduSenderResultCallback.onResult(bArr);
        } else {
            apduSenderResultCallback.onException(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseChannelLock() {
        synchronized (this.mChannelInUseLock) {
            logd("Channel lock released.");
            this.mChannelInUse = false;
            this.mChannelInUseLock.notify();
        }
    }

    private boolean acquireChannelLock() {
        synchronized (this.mChannelInUseLock) {
            if (this.mChannelInUse) {
                if (Looper.getMainLooper().equals(Looper.myLooper())) {
                    return false;
                }
                logd("Logical channel is in use. Wait.");
                try {
                    this.mChannelInUseLock.wait(2000L);
                } catch (InterruptedException unused) {
                }
                if (this.mChannelInUse) {
                    return false;
                }
            }
            this.mChannelInUse = true;
            logd("Channel lock acquired.");
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logv(String str) {
        Rlog.v(this.mLogTag, str);
    }

    private void logd(String str) {
        Rlog.d(this.mLogTag, str);
    }

    private void loge(String str) {
        Rlog.e(this.mLogTag, str);
    }
}
