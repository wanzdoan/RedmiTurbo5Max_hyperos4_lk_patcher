package com.miui.miwallpaper;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;

/* JADX INFO: loaded from: classes.dex */
public interface IMiuiWallpaperManagerService extends IInterface {
    ParcelFileDescriptor getMiuiWallpaperPreview(int i);

    public abstract class Stub extends Binder implements IMiuiWallpaperManagerService {
        public static IMiuiWallpaperManagerService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.miui.miwallpaper.IMiuiWallpaperManagerService");
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof IMiuiWallpaperManagerService)) {
                return (IMiuiWallpaperManagerService) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        class Proxy implements IMiuiWallpaperManagerService {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.miui.miwallpaper.IMiuiWallpaperManagerService
            public ParcelFileDescriptor getMiuiWallpaperPreview(int i) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.miui.miwallpaper.IMiuiWallpaperManagerService");
                    parcelObtain.writeInt(i);
                    this.mRemote.transact(8, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0 ? (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcelObtain2) : null;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }
    }
}
