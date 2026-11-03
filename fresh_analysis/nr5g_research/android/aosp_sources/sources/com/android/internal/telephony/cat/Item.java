package com.android.internal.telephony.cat;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class Item implements Parcelable {
    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() { // from class: com.android.internal.telephony.cat.Item.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Item createFromParcel(Parcel parcel) {
            return new Item(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Item[] newArray(int i) {
            return new Item[i];
        }
    };
    public Bitmap icon;
    public int id;
    public String text;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Item(int i, String str) {
        this(i, str, null);
    }

    public Item(int i, String str, Bitmap bitmap) {
        this.id = i;
        this.text = str;
        this.icon = bitmap;
    }

    public Item(Parcel parcel) {
        this.id = parcel.readInt();
        this.text = parcel.readString();
        this.icon = (Bitmap) parcel.readParcelable(Bitmap.class.getClassLoader());
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.text);
        parcel.writeParcelable(this.icon, i);
    }

    public String toString() {
        return this.text;
    }
}
