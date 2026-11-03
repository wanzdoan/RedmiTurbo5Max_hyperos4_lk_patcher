package mediatek.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class MtkSimSmsInsertStatus implements Parcelable {
    public static final Parcelable.Creator<MtkSimSmsInsertStatus> CREATOR = new Parcelable.Creator<MtkSimSmsInsertStatus>() { // from class: mediatek.telephony.MtkSimSmsInsertStatus.1
        @Override // android.os.Parcelable.Creator
        public MtkSimSmsInsertStatus createFromParcel(Parcel source) {
            int status = source.readInt();
            String index = source.readString();
            return new MtkSimSmsInsertStatus(status, index);
        }

        @Override // android.os.Parcelable.Creator
        public MtkSimSmsInsertStatus[] newArray(int size) {
            return new MtkSimSmsInsertStatus[size];
        }
    };
    private static final String TAG = "MtkSimSmsInsertStatus";
    public String indexInIcc;
    public int insertStatus;

    public MtkSimSmsInsertStatus(int status, String index) {
        this.insertStatus = 0;
        this.indexInIcc = null;
        this.insertStatus = status;
        this.indexInIcc = index;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.insertStatus);
        dest.writeString(this.indexInIcc);
    }

    public int[] getIndex() {
        if (this.indexInIcc == null) {
            return null;
        }
        String[] temp = this.indexInIcc.split(",");
        if (temp == null || temp.length <= 0) {
            Rlog.d(TAG, "should not arrive here");
            return null;
        }
        int[] ret = new int[temp.length];
        for (int i = 0; i < ret.length; i++) {
            try {
                ret[i] = Integer.parseInt(temp[i]);
                Rlog.d(TAG, "index is " + ret[i]);
            } catch (NumberFormatException e) {
                Rlog.d("TAG", "fail to parse index");
                ret[i] = -1;
            }
        }
        return ret;
    }
}
