package com.miui.audiomonitor.distaudio;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.miui.audiomonitor.distaudio.data.DistAudioDeviceInfo;

/* JADX INFO: loaded from: classes.dex */
public interface IDistAudioCallback extends IInterface {
    void onConnectionStatus(DistAudioDeviceInfo distAudioDeviceInfo, int i, String str);

    void onDistAudioDeviceAdded(DistAudioDeviceInfo distAudioDeviceInfo);

    void onDistAudioDeviceRemoved(DistAudioDeviceInfo distAudioDeviceInfo);

    public abstract class Stub extends Binder implements IDistAudioCallback {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.miui.audiomonitor.distaudio.IDistAudioCallback");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            if (i == 1) {
                parcel.enforceInterface("com.miui.audiomonitor.distaudio.IDistAudioCallback");
                onDistAudioDeviceAdded(parcel.readInt() != 0 ? (DistAudioDeviceInfo) DistAudioDeviceInfo.CREATOR.createFromParcel(parcel) : null);
                return true;
            }
            if (i == 2) {
                parcel.enforceInterface("com.miui.audiomonitor.distaudio.IDistAudioCallback");
                onDistAudioDeviceRemoved(parcel.readInt() != 0 ? (DistAudioDeviceInfo) DistAudioDeviceInfo.CREATOR.createFromParcel(parcel) : null);
                return true;
            }
            if (i == 3) {
                parcel.enforceInterface("com.miui.audiomonitor.distaudio.IDistAudioCallback");
                onConnectionStatus(parcel.readInt() != 0 ? (DistAudioDeviceInfo) DistAudioDeviceInfo.CREATOR.createFromParcel(parcel) : null, parcel.readInt(), parcel.readString());
                return true;
            }
            if (i == 1598968902) {
                parcel2.writeString("com.miui.audiomonitor.distaudio.IDistAudioCallback");
                return true;
            }
            return super.onTransact(i, parcel, parcel2, i2);
        }
    }
}
