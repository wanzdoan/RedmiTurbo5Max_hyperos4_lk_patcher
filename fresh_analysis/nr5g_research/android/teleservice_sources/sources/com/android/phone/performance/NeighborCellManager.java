package com.android.phone.performance;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class NeighborCellManager {
    private static NeighborCellManager sInstance;
    private ArrayList mNeighborCells = null;
    private int mServiceRat = 0;

    public enum Rat {
        Lte(1),
        Nr(2);

        public int value;

        Rat(int i) {
            this.value = i;
        }
    }

    public static synchronized NeighborCellManager getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new NeighborCellManager();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private NeighborCellManager() {
    }

    public class NeighborCellInfo implements Parcelable {
        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.android.phone.performance.NeighborCellManager.NeighborCellInfo.1
            @Override // android.os.Parcelable.Creator
            public NeighborCellInfo createFromParcel(Parcel parcel) {
                return new NeighborCellInfo(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public NeighborCellInfo[] newArray(int i) {
                return new NeighborCellInfo[i];
            }
        };
        public int dl_bw;
        public int nbrRsrp;
        public int nbrSnr;
        public int nbriratRsrp;
        public int nbriratSnr;
        public int nddsRrcState;
        public int pci;
        public int rrcState;
        public int rsrp;
        public int rsrq;
        public int timeStamp1;
        public int timeStamp2;
        public int timeStamp3;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public NeighborCellInfo() {
        }

        protected NeighborCellInfo(Parcel parcel) {
            this.timeStamp1 = parcel.readInt();
            this.pci = parcel.readInt();
            this.rsrp = parcel.readInt();
            this.rsrq = parcel.readInt();
            this.dl_bw = parcel.readInt();
            this.timeStamp2 = parcel.readInt();
            this.nbrRsrp = parcel.readInt();
            this.nbrSnr = parcel.readInt();
            this.timeStamp3 = parcel.readInt();
            this.nbriratRsrp = parcel.readInt();
            this.nbriratSnr = parcel.readInt();
            this.rrcState = parcel.readInt();
            this.nddsRrcState = parcel.readInt();
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.timeStamp1);
            parcel.writeInt(this.pci);
            parcel.writeInt(this.rsrp);
            parcel.writeInt(this.rsrq);
            parcel.writeInt(this.dl_bw);
            parcel.writeInt(this.timeStamp2);
            parcel.writeInt(this.nbrRsrp);
            parcel.writeInt(this.nbrSnr);
            parcel.writeInt(this.timeStamp3);
            parcel.writeInt(this.nbriratRsrp);
            parcel.writeInt(this.nbriratSnr);
            parcel.writeInt(this.rrcState);
            parcel.writeInt(this.nddsRrcState);
        }

        public String toString() {
            return "NeighborCellInfo{timeStamp1=" + this.timeStamp1 + ", pci=" + this.pci + ", rsrp=" + this.rsrp + ", rsrq=" + this.rsrq + ", dl_bw=" + this.dl_bw + ", timeStamp2=" + this.timeStamp2 + ", nbrRsrp=" + this.nbrRsrp + ", nbrSnr=" + this.nbrSnr + ", timeStamp3=" + this.timeStamp3 + ", nbriratRsrp=" + this.nbriratRsrp + ", nbriratSnr=" + this.nbriratSnr + ", rrcState=" + this.rrcState + ", nddsRrcState=" + this.nddsRrcState + "}";
        }
    }

    public void handleNeighborCellResult(ArrayList arrayList, int i) {
        this.mServiceRat = i;
        if (arrayList == null || arrayList.isEmpty()) {
            this.mNeighborCells = null;
            Log.d("NeighborCellManager", "No neighbor cells");
            return;
        }
        this.mNeighborCells = arrayList;
        Log.d("NeighborCellManager", "Neighbor cells count=" + arrayList.size() + ", serviceRat=" + i);
    }

    public static ArrayList parseNeighborCellInfo(int[] iArr) {
        if (iArr == null || iArr.length < 17) {
            StringBuilder sb = new StringBuilder("parseNeighborCellInfo: invalid data, length=");
            sb.append(iArr != null ? iArr.length : 0);
            Log.d("NeighborCellManager", sb.toString());
            return null;
        }
        ArrayList arrayList = new ArrayList();
        NeighborCellInfo neighborCellInfo = new NeighborCellInfo();
        neighborCellInfo.timeStamp1 = iArr[6];
        neighborCellInfo.pci = iArr[2];
        neighborCellInfo.rsrp = iArr[7];
        neighborCellInfo.rsrq = iArr[8];
        neighborCellInfo.dl_bw = iArr[5];
        neighborCellInfo.timeStamp2 = iArr[9];
        neighborCellInfo.nbrRsrp = iArr[10];
        neighborCellInfo.nbrSnr = iArr[11];
        neighborCellInfo.timeStamp3 = iArr[12];
        neighborCellInfo.nbriratRsrp = iArr[14];
        neighborCellInfo.nbriratSnr = iArr[13];
        neighborCellInfo.rrcState = iArr[15];
        neighborCellInfo.nddsRrcState = iArr[16];
        arrayList.add(neighborCellInfo);
        Log.d("NeighborCellManager", "parseNeighborCellInfo: length=" + iArr.length + ", timeStamp1=" + neighborCellInfo.timeStamp1 + ", pci=" + neighborCellInfo.pci + ", rsrp=" + neighborCellInfo.rsrp + ", snr=" + neighborCellInfo.rsrq);
        return arrayList;
    }

    public boolean shouldDegrade() {
        ArrayList arrayList = this.mNeighborCells;
        if (arrayList == null || arrayList.isEmpty() || this.mServiceRat != Rat.Nr.value) {
            return false;
        }
        ArrayList arrayList2 = this.mNeighborCells;
        int size = arrayList2.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList2.get(i);
            i++;
            NeighborCellInfo neighborCellInfo = (NeighborCellInfo) obj;
            if (neighborCellInfo != null && neighborCellInfo.timeStamp3 <= 30 && neighborCellInfo.nbriratRsrp >= -110 && neighborCellInfo.nbriratSnr >= -13) {
                Log.d("NeighborCellManager", "shouldDegrade: true, found LTE cell with rsrp=" + neighborCellInfo.nbriratRsrp + ", snr=" + neighborCellInfo.nbriratSnr);
                return true;
            }
        }
        return false;
    }

    public boolean shouldTriggerDataStall() {
        ArrayList arrayList = this.mNeighborCells;
        if (arrayList != null && !arrayList.isEmpty()) {
            ArrayList arrayList2 = this.mNeighborCells;
            int size = arrayList2.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList2.get(i);
                i++;
                NeighborCellInfo neighborCellInfo = (NeighborCellInfo) obj;
                if (neighborCellInfo != null && checkDataStallCondition(neighborCellInfo, this.mServiceRat)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDataStallCondition(NeighborCellInfo neighborCellInfo, int i) {
        boolean z = i == Rat.Nr.value;
        int i2 = neighborCellInfo.timeStamp1;
        int i3 = neighborCellInfo.rsrp;
        int i4 = neighborCellInfo.rsrq;
        if (!checkCellCondition(i2, i3, i4, z ? -110 : -108, z ? 0 : -15, z, "cell1", i3, i4)) {
            int i5 = neighborCellInfo.timeStamp2;
            int i6 = neighborCellInfo.nbrRsrp;
            int i7 = neighborCellInfo.nbrSnr;
            if (!checkCellCondition(i5, i6, i7, z ? -110 : -108, z ? 0 : -15, z, "cell2", i6, i7)) {
                int i8 = neighborCellInfo.timeStamp3;
                int i9 = neighborCellInfo.nbriratRsrp;
                int i10 = neighborCellInfo.nbriratSnr;
                if (!checkCellCondition(i8, i9, i10, z ? -108 : -110, z ? -15 : 0, !z, "cell3", i9, i10)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkCellCondition(int i, int i2, int i3, int i4, int i5, boolean z, String str, int i6, int i7) {
        if (i > 30 || i2 <= i4) {
            return false;
        }
        if (z) {
            if (i3 < i5) {
                return false;
            }
        } else if (i3 <= i5) {
            return false;
        }
        Log.d("NeighborCellManager", "shouldTriggerDataStall: found " + str);
        return true;
    }

    public void resetNeighborCellState() {
        this.mNeighborCells = null;
        this.mServiceRat = 0;
    }
}
