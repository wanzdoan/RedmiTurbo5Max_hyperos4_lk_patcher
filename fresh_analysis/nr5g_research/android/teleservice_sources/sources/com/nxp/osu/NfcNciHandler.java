package com.nxp.osu;

import android.content.Context;
import android.nfc.NfcAdapter;
import android.util.Log;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public class NfcNciHandler {
    private static Context sAppContext;
    private int RESPONSE_WAIT_TIMEOUT = 2000;
    NfcAdapter.NfcVendorNciCallback mNfcVendorNciCallback = new NfcAdapter.NfcVendorNciCallback() { // from class: com.nxp.osu.NfcNciHandler.1
        public void onVendorNciResponse(int i, int i2, byte[] bArr) {
            Log.d("OSU-NfcHandler", String.format("onVendorNciResponse GID :%02X, OID: %02X", Integer.valueOf(i), Integer.valueOf(i2)));
            NfcNciHandler.this.mResponse = bArr;
            if (NfcNciHandler.this.mResponse != null) {
                for (int i3 = 0; i3 < NfcNciHandler.this.mResponse.length; i3++) {
                    Log.d("OSU-NfcHandler", String.format("%02X", Byte.valueOf(NfcNciHandler.this.mResponse[i3])));
                }
            }
            if (NfcNciHandler.this.mResponseCountDownLatch != null) {
                NfcNciHandler.this.mResponseCountDownLatch.countDown();
            }
        }

        public void onVendorNciNotification(int i, int i2, byte[] bArr) {
            Log.d("OSU-NfcHandler", String.format("onVendorNciNotification GID :%02X, OID: %02X", Integer.valueOf(i), Integer.valueOf(i2)));
        }
    };
    private byte[] mResponse;
    private volatile CountDownLatch mResponseCountDownLatch;
    private static NfcNciHandler sInstance = new NfcNciHandler();
    private static final byte[] NCI_RESET_PAYLOAD = new byte[0];

    private NfcNciHandler() {
    }

    public static NfcNciHandler getInstance() {
        return sInstance;
    }

    public static void setContext(Context context) {
        Log.d("OSU-NfcHandler", "Application Context Set");
        sAppContext = context;
    }

    public void performNciColdReset() {
        Context context = sAppContext;
        if (context == null) {
            Log.d("OSU-NfcHandler", "Reset Failed :  Application Context NULL");
            return;
        }
        NfcAdapter defaultAdapter = NfcAdapter.getDefaultAdapter(context);
        if (defaultAdapter == null) {
            Log.e("OSU-NfcHandler", "Reset Failed :  NFC Adapter NULL");
            return;
        }
        if (!defaultAdapter.isEnabled()) {
            Log.e("OSU-NfcHandler", "NFC Disabled can not send vendor command");
            return;
        }
        defaultAdapter.registerNfcVendorNciCallback(Executors.newSingleThreadExecutor(), this.mNfcVendorNciCallback);
        Log.d("OSU-NfcHandler", "sendVendorNci Start...");
        if (!sendVendorNciMessage(15, 30, NCI_RESET_PAYLOAD, defaultAdapter)) {
            Log.e("OSU-NfcHandler", "NCI COLD Reset Failed");
        }
        defaultAdapter.unregisterNfcVendorNciCallback(this.mNfcVendorNciCallback);
    }

    private synchronized boolean sendVendorNciMessage(int i, int i2, byte[] bArr, NfcAdapter nfcAdapter) {
        if (nfcAdapter == null) {
            return false;
        }
        try {
            this.mResponse = null;
            this.mResponseCountDownLatch = new CountDownLatch(1);
            if (nfcAdapter.sendVendorNciMessage(1, i, i2, bArr) != 0 || this.mResponseCountDownLatch.await(this.RESPONSE_WAIT_TIMEOUT, TimeUnit.MILLISECONDS)) {
                return true;
            }
            Log.e("OSU-NfcHandler", " Send Vendor NCI : response Timeout ");
            return false;
        } catch (Exception unused) {
            Log.e("OSU-NfcHandler", "Error while calling sendVendorNciMessage()");
            return false;
        }
    }
}
