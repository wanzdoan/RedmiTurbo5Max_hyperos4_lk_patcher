package com.android.internal.telephony.protobuf.nano.android;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

/* JADX INFO: loaded from: classes.dex */
public final class ParcelableMessageNanoCreator<T extends MessageNano> implements Parcelable.Creator<T> {
    private final Class mClazz;

    public ParcelableMessageNanoCreator(Class<T> cls) {
        this.mClazz = cls;
    }

    @Override // android.os.Parcelable.Creator
    public T createFromParcel(Parcel parcel) {
        String string = parcel.readString();
        byte[] bArrCreateByteArray = parcel.createByteArray();
        T t = null;
        try {
            Class[] clsArr = new Class[0];
            T t2 = (T) Class.forName(string, false, ParcelableMessageNanoCreator.class.getClassLoader()).asSubclass(MessageNano.class).getConstructor(null).newInstance(null);
            try {
                MessageNano.mergeFrom(t2, bArrCreateByteArray);
                return t2;
            } catch (InvalidProtocolBufferNanoException e) {
                e = e;
                t = t2;
                Log.e("PMNCreator", "Exception trying to create proto from parcel", e);
                return t;
            } catch (ClassNotFoundException e2) {
                e = e2;
                t = t2;
                Log.e("PMNCreator", "Exception trying to create proto from parcel", e);
                return t;
            } catch (IllegalAccessException e3) {
                e = e3;
                t = t2;
                Log.e("PMNCreator", "Exception trying to create proto from parcel", e);
                return t;
            } catch (InstantiationException e4) {
                e = e4;
                t = t2;
                Log.e("PMNCreator", "Exception trying to create proto from parcel", e);
                return t;
            } catch (NoSuchMethodException e5) {
                e = e5;
                t = t2;
                Log.e("PMNCreator", "Exception trying to create proto from parcel", e);
                return t;
            } catch (InvocationTargetException e6) {
                e = e6;
                t = t2;
                Log.e("PMNCreator", "Exception trying to create proto from parcel", e);
                return t;
            }
        } catch (InvalidProtocolBufferNanoException e7) {
            e = e7;
        } catch (ClassNotFoundException e8) {
            e = e8;
        } catch (IllegalAccessException e9) {
            e = e9;
        } catch (InstantiationException e10) {
            e = e10;
        } catch (NoSuchMethodException e11) {
            e = e11;
        } catch (InvocationTargetException e12) {
            e = e12;
        }
    }

    @Override // android.os.Parcelable.Creator
    public T[] newArray(int i) {
        return (T[]) ((MessageNano[]) Array.newInstance((Class<?>) this.mClazz, i));
    }

    static void writeToParcel(Class cls, MessageNano messageNano, Parcel parcel) {
        parcel.writeString(cls.getName());
        parcel.writeByteArray(MessageNano.toByteArray(messageNano));
    }
}
