package com.android.phone;

import android.os.Message;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class QmsClientBase {
    protected static volatile QmsClientBase mInstance;
    public boolean mQmsSwitchCloud;

    public interface IDppdInterfaceListener {
        void onDppdCapabilityChanged(boolean z);

        void onDppdRecommendationChanged(int i, int i2);

        void onDppdStatusCapabilityChanged(int i, boolean z);

        void onDppdStatusRecommendationChanged(int i, int i2, int i3);

        void onDppdStatusSubmodeInfoChanged(int i, int i2, int i3);

        void onDppdSubmodeInfoChanged(int i, int i2);

        void onUiOptionStatusChanged(int i, boolean z);
    }

    public interface IHstStateListener {
        void onGetHstStateChanged(int i, int i2);

        void onHstStateReportChanged(int i);
    }

    public interface ISubwayStateListener {
        void onSubwayStateReportChanged(int i);
    }

    public void enableCellBarringFeature(int i, boolean z, Message message) {
    }

    public abstract void getDppdCapability(Message message);

    public abstract void getDppdRecommendation(Message message);

    public abstract void getHstState(Message message);

    public abstract void getMultiSimSubmodeInfo(Message message);

    public abstract void getNfList(int i, Message message);

    public abstract void registerDppdInterfaceListener(IDppdInterfaceListener iDppdInterfaceListener);

    public abstract void registerHstStateListener(IHstStateListener iHstStateListener);

    public abstract void registerHstStateReport();

    public abstract void registerQmsClient(Message message);

    public void registerSubwayStateListener(ISubwayStateListener iSubwayStateListener) {
    }

    public void registerSubwayStateReport() {
    }

    public void setCellBarringInfo(int i, List list, Message message) {
    }

    public void setCellListInfo(List list, Message message) {
    }

    public void setCellListInfoV2(List list, Message message) {
    }

    public abstract void setCoverageState(int i, int i2, Message message);

    public abstract void setDataStallParam(int i, Object obj, Message message);

    public abstract void setHstState(int i, Message message);

    public abstract void setL2NrSelection(int i, Message message);

    public abstract void setLteDubiousCellArray(int i, ArrayList arrayList, Message message);

    public abstract void setNfList(byte b, ArrayList arrayList, Message message);

    public abstract void setNrDubiousCellArray(int i, ArrayList arrayList, Message message);

    public abstract void setOutOfServiceState(int i, int i2, Message message);

    public abstract void setPreferredCellList(int i, Object obj, Message message);

    public void setPreferredFrequencyList(int i, int i2, int i3, List list, List list2, Message message) {
    }

    public void setSubwayState(int i, Message message) {
    }

    public abstract void updateDppdUiOption(boolean z, Message message);

    public static QmsClientBase getInstance() {
        return mInstance;
    }
}
