package com.android.phone;

/* JADX INFO: loaded from: classes.dex */
public abstract class ModemTcpKaFactory {
    public static ModemTcpKaBase make() {
        ModemTcpKaBase modemTcpKaBase;
        if (ModemTcpKaBase.getInstance() != null) {
            return ModemTcpKaBase.getInstance();
        }
        synchronized (ModemTcpKaBase.class) {
            try {
                if (ModemTcpKaBase.getInstance() != null) {
                    return ModemTcpKaBase.getInstance();
                }
                if (ModemProvider.IS_QCOM) {
                    modemTcpKaBase = new QcModemTcpKeepAliveController();
                } else if (ModemProvider.IS_MTK) {
                    modemTcpKaBase = new MtkModemTcpKeepAliveController();
                } else if (ModemProvider.IS_XRING) {
                    modemTcpKaBase = new ModemTcpKeepAliveController();
                } else {
                    modemTcpKaBase = new ModemTcpKaBase();
                }
                ModemTcpKaBase.setInstance(modemTcpKaBase);
                return modemTcpKaBase;
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
