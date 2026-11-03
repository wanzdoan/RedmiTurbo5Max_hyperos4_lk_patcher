package vendor.xiaomi.modem.qms;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes2.dex */
public class SetPreferredFrequencyListS implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.modem.qms.SetPreferredFrequencyListS.1
        @Override // android.os.Parcelable.Creator
        public SetPreferredFrequencyListS createFromParcel(Parcel parcel) {
            SetPreferredFrequencyListS setPreferredFrequencyListS = new SetPreferredFrequencyListS();
            setPreferredFrequencyListS.readFromParcel(parcel);
            return setPreferredFrequencyListS;
        }

        @Override // android.os.Parcelable.Creator
        public SetPreferredFrequencyListS[] newArray(int i) {
            return new SetPreferredFrequencyListS[i];
        }
    };
    public int action;
    public PreferredFrequencyInfoS[] lte_freq_list;
    public PreferredFrequencyInfoS[] nr_freq_list;
    public int slot_id;
    public int usage_type;
    public byte lte_freq_list_len = 0;
    public byte nr_freq_list_len = 0;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iDataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeInt(this.slot_id);
        parcel.writeInt(this.usage_type);
        parcel.writeInt(this.action);
        parcel.writeByte(this.lte_freq_list_len);
        parcel.writeTypedArray(this.lte_freq_list, i);
        parcel.writeByte(this.nr_freq_list_len);
        parcel.writeTypedArray(this.nr_freq_list, i);
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
                this.slot_id = parcel.readInt();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.usage_type = parcel.readInt();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.action = parcel.readInt();
                        if (parcel.dataPosition() - iDataPosition < i) {
                            this.lte_freq_list_len = parcel.readByte();
                            if (parcel.dataPosition() - iDataPosition < i) {
                                Parcelable.Creator creator = PreferredFrequencyInfoS.CREATOR;
                                this.lte_freq_list = (PreferredFrequencyInfoS[]) parcel.createTypedArray(creator);
                                if (parcel.dataPosition() - iDataPosition < i) {
                                    this.nr_freq_list_len = parcel.readByte();
                                    if (parcel.dataPosition() - iDataPosition < i) {
                                        this.nr_freq_list = (PreferredFrequencyInfoS[]) parcel.createTypedArray(creator);
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
        return describeContents(this.nr_freq_list) | describeContents(this.lte_freq_list);
    }

    private int describeContents(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Object[]) {
            int iDescribeContents = 0;
            for (Object obj2 : (Object[]) obj) {
                iDescribeContents |= describeContents(obj2);
            }
            return iDescribeContents;
        }
        if (obj instanceof Parcelable) {
            return ((Parcelable) obj).describeContents();
        }
        return 0;
    }
}
