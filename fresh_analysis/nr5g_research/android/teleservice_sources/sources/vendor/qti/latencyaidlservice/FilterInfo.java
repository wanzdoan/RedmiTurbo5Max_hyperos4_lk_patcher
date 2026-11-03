package vendor.qti.latencyaidlservice;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.phone.settings.CommonOptions$$ExternalSyntheticBUOutline0;
import com.nxp.id.tp.common.HexString$$ExternalSyntheticBUOutline1;
import com.ot.pubsub.a.a;
import vendor.xiaomi.modem.qms.PlmnIdS$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes2.dex */
public class FilterInfo implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: vendor.qti.latencyaidlservice.FilterInfo.1
        @Override // android.os.Parcelable.Creator
        public FilterInfo createFromParcel(Parcel parcel) {
            FilterInfo filterInfo = new FilterInfo();
            filterInfo.readFromParcel(parcel);
            return filterInfo;
        }

        @Override // android.os.Parcelable.Creator
        public FilterInfo[] newArray(int i) {
            return new FilterInfo[i];
        }
    };
    public DownlinkLatencyLevel downlink_level;
    public int filterId = 0;
    public OOD ood;
    public PDCPDiscarTimer pdcp_timer;
    public long status;
    public UplinkLatencyLevel uplink_level;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iDataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeInt(this.filterId);
        parcel.writeLong(this.status);
        parcel.writeTypedObject(this.uplink_level, i);
        parcel.writeTypedObject(this.downlink_level, i);
        parcel.writeTypedObject(this.pdcp_timer, i);
        parcel.writeTypedObject(this.ood, i);
        int iDataPosition2 = parcel.dataPosition();
        parcel.setDataPosition(iDataPosition);
        parcel.writeInt(iDataPosition2 - iDataPosition);
        parcel.setDataPosition(iDataPosition2);
    }

    public final void readFromParcel(Parcel parcel) {
        int iDataPosition = parcel.dataPosition();
        int i = parcel.readInt();
        try {
            if (i < 4) {
                throw new BadParcelableException("Parcelable too small");
            }
            if (parcel.dataPosition() - iDataPosition < i) {
                this.filterId = parcel.readInt();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.status = parcel.readLong();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.uplink_level = (UplinkLatencyLevel) parcel.readTypedObject(UplinkLatencyLevel.CREATOR);
                        if (parcel.dataPosition() - iDataPosition < i) {
                            this.downlink_level = (DownlinkLatencyLevel) parcel.readTypedObject(DownlinkLatencyLevel.CREATOR);
                            if (parcel.dataPosition() - iDataPosition < i) {
                                this.pdcp_timer = (PDCPDiscarTimer) parcel.readTypedObject(PDCPDiscarTimer.CREATOR);
                                if (parcel.dataPosition() - iDataPosition < i) {
                                    this.ood = (OOD) parcel.readTypedObject(OOD.CREATOR);
                                    if (iDataPosition > Integer.MAX_VALUE - i) {
                                        PlmnIdS$$ExternalSyntheticBUOutline0.m();
                                        return;
                                    }
                                } else if (iDataPosition > Integer.MAX_VALUE - i) {
                                    PlmnIdS$$ExternalSyntheticBUOutline0.m();
                                    return;
                                }
                            } else if (iDataPosition > Integer.MAX_VALUE - i) {
                                PlmnIdS$$ExternalSyntheticBUOutline0.m();
                                return;
                            }
                        } else if (iDataPosition > Integer.MAX_VALUE - i) {
                            PlmnIdS$$ExternalSyntheticBUOutline0.m();
                            return;
                        }
                    } else if (iDataPosition > Integer.MAX_VALUE - i) {
                        PlmnIdS$$ExternalSyntheticBUOutline0.m();
                        return;
                    }
                } else if (iDataPosition > Integer.MAX_VALUE - i) {
                    PlmnIdS$$ExternalSyntheticBUOutline0.m();
                    return;
                }
            } else if (iDataPosition > Integer.MAX_VALUE - i) {
                PlmnIdS$$ExternalSyntheticBUOutline0.m();
                return;
            }
            parcel.setDataPosition(iDataPosition + i);
        } catch (Throwable th) {
            if (iDataPosition > Integer.MAX_VALUE - i) {
                PlmnIdS$$ExternalSyntheticBUOutline0.m();
            } else {
                parcel.setDataPosition(iDataPosition + i);
                throw th;
            }
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return describeContents(this.ood) | describeContents(this.uplink_level) | describeContents(this.downlink_level) | describeContents(this.pdcp_timer);
    }

    private int describeContents(Object obj) {
        if (obj != null && (obj instanceof Parcelable)) {
            return ((Parcelable) obj).describeContents();
        }
        return 0;
    }

    public final class UplinkLatencyLevel implements Parcelable {
        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: vendor.qti.latencyaidlservice.FilterInfo.UplinkLatencyLevel.1
            @Override // android.os.Parcelable.Creator
            public UplinkLatencyLevel createFromParcel(Parcel parcel) {
                return new UplinkLatencyLevel(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public UplinkLatencyLevel[] newArray(int i) {
                return new UplinkLatencyLevel[i];
            }
        };
        private int _tag;
        private Object _value;

        public final int getStability() {
            return 1;
        }

        private UplinkLatencyLevel(Parcel parcel) {
            readFromParcel(parcel);
        }

        public int getTag() {
            return this._tag;
        }

        public boolean getNoinit() {
            _assertTag(0);
            return ((Boolean) this._value).booleanValue();
        }

        public long getValue() {
            _assertTag(1);
            return ((Long) this._value).longValue();
        }

        @Override // android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this._tag);
            int i2 = this._tag;
            if (i2 == 0) {
                parcel.writeBoolean(getNoinit());
            } else {
                if (i2 != 1) {
                    return;
                }
                parcel.writeLong(getValue());
            }
        }

        public void readFromParcel(Parcel parcel) {
            int i = parcel.readInt();
            if (i == 0) {
                _set(i, Boolean.valueOf(parcel.readBoolean()));
            } else if (i == 1) {
                _set(i, Long.valueOf(parcel.readLong()));
            } else {
                HexString$$ExternalSyntheticBUOutline1.m("union: unknown tag: ", i);
            }
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            getTag();
            return 0;
        }

        private void _assertTag(int i) {
            if (getTag() == i) {
                return;
            }
            FilterInfo$OOD$$ExternalSyntheticBUOutline0.m("bad access: ", _tagString(i), ", ", _tagString(getTag()), " is available.");
        }

        private String _tagString(int i) {
            if (i == 0) {
                return "noinit";
            }
            if (i == 1) {
                return a.p;
            }
            CommonOptions$$ExternalSyntheticBUOutline0.m("unknown field: ", i);
            return null;
        }

        private void _set(int i, Object obj) {
            this._tag = i;
            this._value = obj;
        }
    }

    public final class DownlinkLatencyLevel implements Parcelable {
        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: vendor.qti.latencyaidlservice.FilterInfo.DownlinkLatencyLevel.1
            @Override // android.os.Parcelable.Creator
            public DownlinkLatencyLevel createFromParcel(Parcel parcel) {
                return new DownlinkLatencyLevel(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public DownlinkLatencyLevel[] newArray(int i) {
                return new DownlinkLatencyLevel[i];
            }
        };
        private int _tag;
        private Object _value;

        public final int getStability() {
            return 1;
        }

        private DownlinkLatencyLevel(Parcel parcel) {
            readFromParcel(parcel);
        }

        public int getTag() {
            return this._tag;
        }

        public boolean getNoinit() {
            _assertTag(0);
            return ((Boolean) this._value).booleanValue();
        }

        public long getValue() {
            _assertTag(1);
            return ((Long) this._value).longValue();
        }

        @Override // android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this._tag);
            int i2 = this._tag;
            if (i2 == 0) {
                parcel.writeBoolean(getNoinit());
            } else {
                if (i2 != 1) {
                    return;
                }
                parcel.writeLong(getValue());
            }
        }

        public void readFromParcel(Parcel parcel) {
            int i = parcel.readInt();
            if (i == 0) {
                _set(i, Boolean.valueOf(parcel.readBoolean()));
            } else if (i == 1) {
                _set(i, Long.valueOf(parcel.readLong()));
            } else {
                HexString$$ExternalSyntheticBUOutline1.m("union: unknown tag: ", i);
            }
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            getTag();
            return 0;
        }

        private void _assertTag(int i) {
            if (getTag() == i) {
                return;
            }
            FilterInfo$OOD$$ExternalSyntheticBUOutline0.m("bad access: ", _tagString(i), ", ", _tagString(getTag()), " is available.");
        }

        private String _tagString(int i) {
            if (i == 0) {
                return "noinit";
            }
            if (i == 1) {
                return a.p;
            }
            CommonOptions$$ExternalSyntheticBUOutline0.m("unknown field: ", i);
            return null;
        }

        private void _set(int i, Object obj) {
            this._tag = i;
            this._value = obj;
        }
    }

    public final class PDCPDiscarTimer implements Parcelable {
        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: vendor.qti.latencyaidlservice.FilterInfo.PDCPDiscarTimer.1
            @Override // android.os.Parcelable.Creator
            public PDCPDiscarTimer createFromParcel(Parcel parcel) {
                return new PDCPDiscarTimer(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public PDCPDiscarTimer[] newArray(int i) {
                return new PDCPDiscarTimer[i];
            }
        };
        private int _tag;
        private Object _value;

        public final int getStability() {
            return 1;
        }

        private PDCPDiscarTimer(Parcel parcel) {
            readFromParcel(parcel);
        }

        public int getTag() {
            return this._tag;
        }

        public boolean getNoinit() {
            _assertTag(0);
            return ((Boolean) this._value).booleanValue();
        }

        public int getValue() {
            _assertTag(1);
            return ((Integer) this._value).intValue();
        }

        @Override // android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this._tag);
            int i2 = this._tag;
            if (i2 == 0) {
                parcel.writeBoolean(getNoinit());
            } else {
                if (i2 != 1) {
                    return;
                }
                parcel.writeInt(getValue());
            }
        }

        public void readFromParcel(Parcel parcel) {
            int i = parcel.readInt();
            if (i == 0) {
                _set(i, Boolean.valueOf(parcel.readBoolean()));
            } else if (i == 1) {
                _set(i, Integer.valueOf(parcel.readInt()));
            } else {
                HexString$$ExternalSyntheticBUOutline1.m("union: unknown tag: ", i);
            }
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            getTag();
            return 0;
        }

        private void _assertTag(int i) {
            if (getTag() == i) {
                return;
            }
            FilterInfo$OOD$$ExternalSyntheticBUOutline0.m("bad access: ", _tagString(i), ", ", _tagString(getTag()), " is available.");
        }

        private String _tagString(int i) {
            if (i == 0) {
                return "noinit";
            }
            if (i == 1) {
                return a.p;
            }
            CommonOptions$$ExternalSyntheticBUOutline0.m("unknown field: ", i);
            return null;
        }

        private void _set(int i, Object obj) {
            this._tag = i;
            this._value = obj;
        }
    }

    public final class OOD implements Parcelable {
        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: vendor.qti.latencyaidlservice.FilterInfo.OOD.1
            @Override // android.os.Parcelable.Creator
            public OOD createFromParcel(Parcel parcel) {
                return new OOD(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public OOD[] newArray(int i) {
                return new OOD[i];
            }
        };
        private int _tag;
        private Object _value;

        public final int getStability() {
            return 1;
        }

        private OOD(Parcel parcel) {
            readFromParcel(parcel);
        }

        public int getTag() {
            return this._tag;
        }

        public boolean getNoinit() {
            _assertTag(0);
            return ((Boolean) this._value).booleanValue();
        }

        public long getValue() {
            _assertTag(1);
            return ((Long) this._value).longValue();
        }

        @Override // android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this._tag);
            int i2 = this._tag;
            if (i2 == 0) {
                parcel.writeBoolean(getNoinit());
            } else {
                if (i2 != 1) {
                    return;
                }
                parcel.writeLong(getValue());
            }
        }

        public void readFromParcel(Parcel parcel) {
            int i = parcel.readInt();
            if (i == 0) {
                _set(i, Boolean.valueOf(parcel.readBoolean()));
            } else if (i == 1) {
                _set(i, Long.valueOf(parcel.readLong()));
            } else {
                HexString$$ExternalSyntheticBUOutline1.m("union: unknown tag: ", i);
            }
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            getTag();
            return 0;
        }

        private void _assertTag(int i) {
            if (getTag() == i) {
                return;
            }
            FilterInfo$OOD$$ExternalSyntheticBUOutline0.m("bad access: ", _tagString(i), ", ", _tagString(getTag()), " is available.");
        }

        private String _tagString(int i) {
            if (i == 0) {
                return "noinit";
            }
            if (i == 1) {
                return a.p;
            }
            CommonOptions$$ExternalSyntheticBUOutline0.m("unknown field: ", i);
            return null;
        }

        private void _set(int i, Object obj) {
            this._tag = i;
            this._value = obj;
        }
    }
}
