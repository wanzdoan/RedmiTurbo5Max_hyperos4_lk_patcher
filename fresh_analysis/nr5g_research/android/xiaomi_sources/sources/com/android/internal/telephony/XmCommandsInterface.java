package com.android.internal.telephony;

import android.os.Handler;
import android.os.Message;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface XmCommandsInterface {
    void enableEndc(boolean z, Message message);

    void getDdsAssistantInfo(Message message);

    void getModemReason(Message message);

    void getModemScene(Message message);

    void getSarDsi(Message message);

    void getSarDsiGroup(Message message);

    void promoteBSR(int i, Message message);

    void registerForUnsolCommMsg(Handler handler, int i, Object obj);

    void registerHstStateCallback(Handler handler, int i, Object obj);

    void registerModemExceptionCallback(Handler handler, int i, Object obj);

    void registerModemSceneCallback(Handler handler, int i, Object obj);

    void registerQosStateCallback(Handler handler, int i, Object obj);

    void sendCommMsg(int i, byte[] bArr, Message message);

    void setDataStallMode(int i, int i2, int i3, int i4, Message message);

    void setDeprioritizeSA(boolean z, Message message);

    void setFiveTupleBlackList(String str, List<String> list, int i, Message message);

    void setMobilityOptParams(boolean z, int i, int i2, Message message);

    void setModemCallbackSwitch(boolean z, Message message);

    void setModemEventIndSwitch(int i, boolean z, Message message);

    void setModemModelCfg(int[] iArr, Message message);

    void setNrMode(int i, Message message);

    void setPreferBandParams(int i, int[] iArr, int[] iArr2, int[] iArr3, int i2, Message message);

    void setSarDsi(int i, Message message);

    void setSarDsiGroup(int[] iArr, Message message);

    void unRegisterForUnsolCommMsg(Handler handler);

    void unRegisterHstStateCallback(Handler handler);

    void unRegisterQosStateCallback(Handler handler);

    void unregisterModemExceptionCallback(Handler handler);

    void unregisterModemSceneCallback(Handler handler);
}
