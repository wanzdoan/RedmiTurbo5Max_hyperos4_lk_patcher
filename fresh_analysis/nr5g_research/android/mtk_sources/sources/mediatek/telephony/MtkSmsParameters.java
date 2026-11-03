package mediatek.telephony;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class MtkSmsParameters implements Parcelable {
    public static final Parcelable.Creator<MtkSmsParameters> CREATOR = new Parcelable.Creator<MtkSmsParameters>() { // from class: mediatek.telephony.MtkSmsParameters.1
        @Override // android.os.Parcelable.Creator
        public MtkSmsParameters createFromParcel(Parcel source) {
            int format = source.readInt();
            int vp = source.readInt();
            int pid = source.readInt();
            int dcs = source.readInt();
            return new MtkSmsParameters(format, vp, pid, dcs);
        }

        @Override // android.os.Parcelable.Creator
        public MtkSmsParameters[] newArray(int size) {
            return new MtkSmsParameters[size];
        }
    };
    public int dcs;
    public int format;
    public int pid;
    public int vp;

    public MtkSmsParameters(int format, int vp, int pid, int dcs) {
        this.format = format;
        this.vp = vp;
        this.pid = pid;
        this.dcs = dcs;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.format);
        dest.writeInt(this.vp);
        dest.writeInt(this.pid);
        dest.writeInt(this.dcs);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(50);
        sb.append("[");
        sb.append(this.format);
        sb.append(", ");
        sb.append(this.vp);
        sb.append(", ");
        sb.append(this.pid);
        sb.append(", ");
        sb.append(this.dcs);
        sb.append("]");
        return sb.toString();
    }
}
