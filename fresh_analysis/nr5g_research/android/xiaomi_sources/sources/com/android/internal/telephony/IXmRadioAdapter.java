package com.android.internal.telephony;

import android.os.ResultReceiver;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface IXmRadioAdapter {
    boolean enableEndc(boolean z, ResultReceiver resultReceiver);

    boolean getDdsAssistantInfo(ResultReceiver resultReceiver);

    int getHalVersion();

    boolean getModemReason(ResultReceiver resultReceiver);

    boolean getModemScene(ResultReceiver resultReceiver);

    boolean getSarDsi(ResultReceiver resultReceiver);

    boolean getSarDsiGroup(ResultReceiver resultReceiver);

    boolean promoteBSR(int i, ResultReceiver resultReceiver);

    boolean restartRILD(ResultReceiver resultReceiver);

    boolean sendCommMsg(int i, byte[] bArr, ResultReceiver resultReceiver);

    boolean setDataStallMode(int i, int i2, int i3, int i4, ResultReceiver resultReceiver);

    boolean setDeprioritizeSA(boolean z, ResultReceiver resultReceiver);

    boolean setFiveTupleBlackList(String str, List<String> list, int i, ResultReceiver resultReceiver);

    boolean setMobilityOptParams(boolean z, int i, int i2, ResultReceiver resultReceiver);

    boolean setModemCallbackSwitch(boolean z, ResultReceiver resultReceiver);

    boolean setModemEventIndSwitch(int i, boolean z, ResultReceiver resultReceiver);

    boolean setModemModelCfg(int[] iArr, ResultReceiver resultReceiver);

    boolean setNrMode(int i, ResultReceiver resultReceiver);

    boolean setPreferBandParams(int i, int[] iArr, int[] iArr2, int[] iArr3, int i2, ResultReceiver resultReceiver);

    boolean setSarDsi(int i, ResultReceiver resultReceiver);

    boolean setSarDsiGroup(int[] iArr, ResultReceiver resultReceiver);

    boolean setSensorState(int i, int i2, ResultReceiver resultReceiver);
}
