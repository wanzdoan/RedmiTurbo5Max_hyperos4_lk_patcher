package com.miui.audiomonitor.distaudio;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.miui.audiomonitor.distaudio.data.DistAudioDeviceInfo;
import com.miui.audiomonitor.distaudio.data.DistAudioRoute;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface IDistAudioService extends IInterface {
    void connectDistAudioDevice(DistAudioRoute distAudioRoute);

    void disconnectDistAudioDevice(DistAudioRoute distAudioRoute);

    DistAudioRoute getCurDistAudioRoute();

    List getDistAudioDevices();

    void registerDistAudioCallback(String str, IDistAudioCallback iDistAudioCallback);

    void unregisterDistAudioCallback(String str);

    public abstract class Stub extends Binder implements IDistAudioService {
        public static IDistAudioService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.miui.audiomonitor.distaudio.IDistAudioService");
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof IDistAudioService)) {
                return (IDistAudioService) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        class Proxy implements IDistAudioService {
            public static IDistAudioService sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.miui.audiomonitor.distaudio.IDistAudioService
            public void connectDistAudioDevice(DistAudioRoute distAudioRoute) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.miui.audiomonitor.distaudio.IDistAudioService");
                    if (distAudioRoute != null) {
                        parcelObtain.writeInt(1);
                        distAudioRoute.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    if (!this.mRemote.transact(1, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().connectDistAudioDevice(distAudioRoute);
                    } else {
                        parcelObtain2.readException();
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.miui.audiomonitor.distaudio.IDistAudioService
            public void disconnectDistAudioDevice(DistAudioRoute distAudioRoute) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.miui.audiomonitor.distaudio.IDistAudioService");
                    if (distAudioRoute != null) {
                        parcelObtain.writeInt(1);
                        distAudioRoute.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    if (!this.mRemote.transact(2, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().disconnectDistAudioDevice(distAudioRoute);
                    } else {
                        parcelObtain2.readException();
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.miui.audiomonitor.distaudio.IDistAudioService
            public List getDistAudioDevices() {
                List listCreateTypedArrayList;
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.miui.audiomonitor.distaudio.IDistAudioService");
                    if (!this.mRemote.transact(3, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        listCreateTypedArrayList = Stub.getDefaultImpl().getDistAudioDevices();
                    } else {
                        parcelObtain2.readException();
                        listCreateTypedArrayList = parcelObtain2.createTypedArrayList(DistAudioDeviceInfo.CREATOR);
                    }
                    return listCreateTypedArrayList;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.miui.audiomonitor.distaudio.IDistAudioService
            public DistAudioRoute getCurDistAudioRoute() {
                DistAudioRoute curDistAudioRoute;
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.miui.audiomonitor.distaudio.IDistAudioService");
                    if (!this.mRemote.transact(4, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        curDistAudioRoute = Stub.getDefaultImpl().getCurDistAudioRoute();
                    } else {
                        parcelObtain2.readException();
                        curDistAudioRoute = parcelObtain2.readInt() != 0 ? (DistAudioRoute) DistAudioRoute.CREATOR.createFromParcel(parcelObtain2) : null;
                    }
                    return curDistAudioRoute;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.miui.audiomonitor.distaudio.IDistAudioService
            public void registerDistAudioCallback(String str, IDistAudioCallback iDistAudioCallback) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.miui.audiomonitor.distaudio.IDistAudioService");
                    parcelObtain.writeString(str);
                    parcelObtain.writeStrongBinder(iDistAudioCallback != null ? iDistAudioCallback.asBinder() : null);
                    if (!this.mRemote.transact(7, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerDistAudioCallback(str, iDistAudioCallback);
                    } else {
                        parcelObtain2.readException();
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.miui.audiomonitor.distaudio.IDistAudioService
            public void unregisterDistAudioCallback(String str) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.miui.audiomonitor.distaudio.IDistAudioService");
                    parcelObtain.writeString(str);
                    if (!this.mRemote.transact(8, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterDistAudioCallback(str);
                    } else {
                        parcelObtain2.readException();
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }

        public static IDistAudioService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
