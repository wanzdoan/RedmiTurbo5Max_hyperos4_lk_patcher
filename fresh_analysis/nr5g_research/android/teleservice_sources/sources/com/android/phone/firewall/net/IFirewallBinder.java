package com.android.phone.firewall.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.android.phone.firewall.model.FirewallRule;

/* JADX INFO: loaded from: classes.dex */
public interface IFirewallBinder extends IInterface {
    FirewallRule getSatelliteRule(String str);

    boolean isStarted();

    void setSatelliteRule(String str, FirewallRule firewallRule);

    public abstract class Stub extends Binder implements IFirewallBinder {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.android.phone.firewall.net.IFirewallBinder");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.android.phone.firewall.net.IFirewallBinder");
            }
            if (i == 1) {
                boolean zIsStarted = isStarted();
                parcel2.writeNoException();
                parcel2.writeBoolean(zIsStarted);
            } else if (i == 2) {
                String string = parcel.readString();
                FirewallRule firewallRule = (FirewallRule) parcel.readTypedObject(FirewallRule.CREATOR);
                parcel.enforceNoDataAvail();
                setSatelliteRule(string, firewallRule);
                parcel2.writeNoException();
            } else if (i == 3) {
                String string2 = parcel.readString();
                parcel.enforceNoDataAvail();
                FirewallRule satelliteRule = getSatelliteRule(string2);
                parcel2.writeNoException();
                parcel2.writeTypedObject(satelliteRule, 1);
            } else {
                return super.onTransact(i, parcel, parcel2, i2);
            }
            return true;
        }
    }
}
