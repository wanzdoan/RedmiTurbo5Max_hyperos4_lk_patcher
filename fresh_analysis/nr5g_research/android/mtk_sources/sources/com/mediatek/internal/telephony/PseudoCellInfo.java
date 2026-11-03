package com.mediatek.internal.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class PseudoCellInfo implements Parcelable {
    public static final Parcelable.Creator<PseudoCellInfo> CREATOR = new Parcelable.Creator<PseudoCellInfo>() { // from class: com.mediatek.internal.telephony.PseudoCellInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PseudoCellInfo createFromParcel(Parcel in) {
            return new PseudoCellInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PseudoCellInfo[] newArray(int size) {
            return new PseudoCellInfo[size];
        }
    };
    public int mApcMode;
    public boolean mApcReport;
    public int mCellCount;
    public ArrayList<CellInfo> mCellInfos;
    public int mReportInterval;

    class CellInfo {
        public int arfcn;
        public int bsic;
        public int cid;
        public int lac;
        public int plmn;
        public int type;

        CellInfo(PseudoCellInfo this$0) {
        }
    }

    protected PseudoCellInfo() {
        this.mApcMode = 0;
        this.mApcReport = false;
        this.mReportInterval = 0;
        this.mCellCount = 0;
        this.mCellInfos = null;
    }

    protected PseudoCellInfo(int[] msgs) {
        this.mApcMode = msgs[0];
        this.mApcReport = msgs[1] == 1;
        this.mReportInterval = msgs[2];
        this.mCellCount = msgs[3];
        this.mCellInfos = new ArrayList<>();
        for (int i = 0; i < this.mCellCount; i++) {
            CellInfo cell = new CellInfo(this);
            cell.type = msgs[(i * 6) + 4];
            cell.plmn = msgs[(i * 6) + 5];
            cell.lac = msgs[(i * 6) + 6];
            cell.cid = msgs[(i * 6) + 7];
            cell.arfcn = msgs[(i * 6) + 8];
            cell.bsic = msgs[(i * 6) + 9];
            this.mCellInfos.add(cell);
        }
    }

    protected PseudoCellInfo(int apcMode, boolean reportEnable, int interVal, int[] cellinfo) {
        this.mApcMode = apcMode;
        this.mApcReport = reportEnable;
        this.mReportInterval = interVal;
        setCellInfo(cellinfo);
    }

    public void updateApcSetting(int apcMode, boolean reportEnable, int interVal) {
        this.mApcMode = apcMode;
        this.mApcReport = reportEnable;
        this.mReportInterval = interVal;
    }

    public void setCellInfo(int[] cellInfo) {
        this.mCellCount = cellInfo[0];
        this.mCellInfos = new ArrayList<>();
        for (int i = 0; i < this.mCellCount; i++) {
            CellInfo cell = new CellInfo(this);
            cell.type = cellInfo[(i * 6) + 1];
            cell.plmn = cellInfo[(i * 6) + 2];
            cell.lac = cellInfo[(i * 6) + 3];
            cell.cid = cellInfo[(i * 6) + 4];
            cell.arfcn = cellInfo[(i * 6) + 5];
            cell.bsic = cellInfo[(i * 6) + 6];
            this.mCellInfos.add(cell);
        }
    }

    public int getApcMode() {
        return this.mApcMode;
    }

    public boolean getReportEnable() {
        return this.mApcReport;
    }

    public int getReportInterval() {
        return this.mReportInterval;
    }

    public int getCellCount() {
        return this.mCellCount;
    }

    public int getType(int index) {
        if (index < 0 || index >= this.mCellCount || this.mCellInfos == null || this.mCellInfos.get(index) == null) {
            return 0;
        }
        return this.mCellInfos.get(index).type;
    }

    public int getPlmn(int index) {
        if (index < 0 || index >= this.mCellCount || this.mCellInfos == null || this.mCellInfos.get(index) == null) {
            return 0;
        }
        return this.mCellInfos.get(index).plmn;
    }

    public int getLac(int index) {
        if (index < 0 || index >= this.mCellCount || this.mCellInfos == null || this.mCellInfos.get(index) == null) {
            return 0;
        }
        return this.mCellInfos.get(index).lac;
    }

    public int getCid(int index) {
        if (index < 0 || index >= this.mCellCount || this.mCellInfos == null || this.mCellInfos.get(index) == null) {
            return 0;
        }
        return this.mCellInfos.get(index).cid;
    }

    public int getArfcn(int index) {
        if (index < 0 || index >= this.mCellCount || this.mCellInfos == null || this.mCellInfos.get(index) == null) {
            return 0;
        }
        return this.mCellInfos.get(index).arfcn;
    }

    public int getBsic(int index) {
        if (index < 0 || index >= this.mCellCount || this.mCellInfos == null || this.mCellInfos.get(index) == null) {
            return 0;
        }
        return this.mCellInfos.get(index).bsic;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append(this.mApcMode);
        sb.append(", ");
        sb.append(this.mApcReport);
        sb.append(", ");
        sb.append(this.mReportInterval);
        sb.append(", ");
        sb.append(this.mCellCount);
        sb.append("]");
        for (int i = 0; i < this.mCellCount && this.mCellInfos != null && this.mCellInfos.get(i) != null; i++) {
            sb.append("[");
            sb.append(this.mCellInfos.get(i).type);
            sb.append(", ");
            sb.append(this.mCellInfos.get(i).plmn);
            sb.append(", ");
            sb.append(this.mCellInfos.get(i).lac);
            sb.append(", ");
            sb.append(this.mCellInfos.get(i).cid);
            sb.append(", ");
            sb.append(this.mCellInfos.get(i).arfcn);
            sb.append(", ");
            sb.append(this.mCellInfos.get(i).bsic);
            sb.append("]");
        }
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mApcMode);
        parcel.writeInt(this.mApcReport ? 1 : 0);
        parcel.writeInt(this.mReportInterval);
        parcel.writeInt(this.mCellCount);
        for (int i2 = 0; i2 < this.mCellCount && this.mCellInfos != null && this.mCellInfos.get(i2) != null; i2++) {
            parcel.writeInt(this.mCellInfos.get(i2).type);
            parcel.writeInt(this.mCellInfos.get(i2).plmn);
            parcel.writeInt(this.mCellInfos.get(i2).lac);
            parcel.writeInt(this.mCellInfos.get(i2).cid);
            parcel.writeInt(this.mCellInfos.get(i2).arfcn);
            parcel.writeInt(this.mCellInfos.get(i2).bsic);
        }
    }

    protected PseudoCellInfo(Parcel in) {
        this.mApcMode = in.readInt();
        this.mApcReport = in.readInt() == 1;
        this.mReportInterval = in.readInt();
        this.mCellCount = in.readInt();
        this.mCellInfos = new ArrayList<>();
        for (int i = 0; i < this.mCellCount; i++) {
            CellInfo cell = new CellInfo(this);
            cell.type = in.readInt();
            cell.plmn = in.readInt();
            cell.lac = in.readInt();
            cell.cid = in.readInt();
            cell.arfcn = in.readInt();
            cell.bsic = in.readInt();
            this.mCellInfos.add(cell);
        }
    }
}
