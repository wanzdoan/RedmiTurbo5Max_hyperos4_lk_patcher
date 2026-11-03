package com.mediatek.internal.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.CellSignalStrengthTdscdma;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.SignalStrength;

/* JADX INFO: loaded from: classes.dex */
public class FemtoCellInfo implements Parcelable {
    public static final Parcelable.Creator<FemtoCellInfo> CREATOR = new Parcelable.Creator<FemtoCellInfo>() { // from class: com.mediatek.internal.telephony.FemtoCellInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FemtoCellInfo createFromParcel(Parcel in) {
            FemtoCellInfo femtoCellInfo = new FemtoCellInfo(in.readInt(), in.readInt(), in.readString(), in.readString(), in.readString(), in.readInt(), in.readInt() == 1, in.readInt());
            return femtoCellInfo;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FemtoCellInfo[] newArray(int size) {
            return new FemtoCellInfo[size];
        }
    };
    public static final int CSG_ICON_TYPE_ALLOWED = 1;
    public static final int CSG_ICON_TYPE_NOT_ALLOWED = 0;
    public static final int CSG_ICON_TYPE_OPERATOR = 2;
    public static final int CSG_ICON_TYPE_OPERATOR_UNAUTHORIZED = 3;
    public static final int SIGNAL_STRENGTH_GOOD = 3;
    public static final int SIGNAL_STRENGTH_GREAT = 4;
    public static final int SIGNAL_STRENGTH_MODERATE = 2;
    public static final int SIGNAL_STRENGTH_NONE_OR_UNKNOWN = 0;
    public static final int SIGNAL_STRENGTH_POOR = 1;
    public static final int STATE_CONNECTED = 0;
    public static final int STATE_DISCONNECTED = 1;
    private boolean con;
    private int csgIconType;
    private int csgId;
    private String homeNodeBName;
    private String operatorAlphaLong;
    private String operatorNumeric;
    private int rat;
    private int sig;

    public int getCsgId() {
        return this.csgId;
    }

    public int getCsgIconType() {
        return this.csgIconType;
    }

    public String getHomeNodeBName() {
        return this.homeNodeBName;
    }

    public int getCsgRat() {
        return this.rat;
    }

    public String getOperatorNumeric() {
        return this.operatorNumeric;
    }

    public String getOperatorAlphaLong() {
        return this.operatorAlphaLong;
    }

    public int getConnectionState() {
        return this.con ? 0 : 1;
    }

    public int getSignalLevel() {
        return updateLevel();
    }

    private int updateLevel() {
        if (this.rat == 14) {
            SignalStrength ss = new SignalStrength(new CellSignalStrengthCdma(), new CellSignalStrengthGsm(), new CellSignalStrengthWcdma(), new CellSignalStrengthTdscdma(), new CellSignalStrengthLte(99, this.sig, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE), new CellSignalStrengthNr());
            return ss.getLevel();
        }
        if (this.rat == 3) {
            SignalStrength ss2 = new SignalStrength(new CellSignalStrengthCdma(), new CellSignalStrengthGsm(), new CellSignalStrengthWcdma(this.sig, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE), new CellSignalStrengthTdscdma(), new CellSignalStrengthLte(), new CellSignalStrengthNr());
            return ss2.getLevel();
        }
        return 0;
    }

    public FemtoCellInfo(int csgId, int csgIconType, String homeNodeBName, String operatorNumeric, String operatorAlphaLong, int rat, boolean con, int sig) {
        this.rat = 0;
        this.con = false;
        this.csgId = csgId;
        this.csgIconType = csgIconType;
        this.homeNodeBName = homeNodeBName;
        this.operatorNumeric = operatorNumeric;
        this.operatorAlphaLong = operatorAlphaLong;
        this.rat = rat;
        this.con = con;
        this.sig = sig;
    }

    public String toString() {
        return "FemtoCellInfo " + this.csgId + "/" + this.csgIconType + "/" + this.homeNodeBName + "/" + this.operatorNumeric + "/" + this.operatorAlphaLong + "/" + this.rat + "/" + this.con + "/" + this.sig + "/" + getSignalLevel();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.csgId);
        parcel.writeInt(this.csgIconType);
        parcel.writeString(this.homeNodeBName);
        parcel.writeString(this.operatorNumeric);
        parcel.writeString(this.operatorAlphaLong);
        parcel.writeInt(this.rat);
        parcel.writeInt(this.con ? 1 : 0);
        parcel.writeInt(this.sig);
    }
}
