package com.android.phone;

import android.content.Context;
import android.os.Bundle;
import android.telephony.tcpka.IModemTcpKeepAliveIndCallback;
import com.android.phone.module.IModemModule;
import com.android.phone.statistics.ModemLogUploadToolsBase;
import com.android.phone.statistics.utils.ModemHydraStat;
import com.android.phone.statistics.utils.ModemHydraStatBase;
import com.android.phone.utils.ModemDFS;
import com.android.services.telephony.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class ModemDelegateImpl implements IModemModule {
    private static volatile ModemDelegateImpl sInstance;
    private Lazy mFastDormancy = new Lazy();
    private Lazy mTcpKa = new Lazy();
    private Lazy mModemDFS = new Lazy();

    public static ModemDelegateImpl init() {
        if (sInstance == null) {
            synchronized (ModemDelegateImpl.class) {
                try {
                    if (sInstance == null) {
                        ModemDelegateImpl modemDelegateImpl = new ModemDelegateImpl();
                        ModuleDelegate.register(IModemModule.class, modemDelegateImpl);
                        sInstance = modemDelegateImpl;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    private ModemDelegateImpl() {
    }

    public void setFastDormancy(FastDormancy fastDormancy) {
        this.mFastDormancy.set(fastDormancy);
    }

    public void setTcpKa(ModemTcpKaBase modemTcpKaBase) {
        this.mTcpKa.set(modemTcpKaBase);
    }

    public void setModemDFS(ModemDFS modemDFS) {
        this.mModemDFS.set(modemDFS);
    }

    @Override // com.android.phone.module.IModemModule
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (MiuiPhoneUtils.isODM4GPlatformSupportModemDump() && ModemDumpBase.getInstance() != null) {
            ModemDumpBase.getInstance().dump(fileDescriptor, printWriter, strArr);
        }
        if (ModemHydraStatBase.getInstance() != null) {
            ModemHydraStatBase.getInstance().dump(fileDescriptor, printWriter, strArr);
        }
    }

    @Override // com.android.phone.module.IModemModule
    public void reportModemAiStats() {
        ModemAiController.reportModemAiStats();
    }

    @Override // com.android.phone.module.IModemModule
    public void onModemTcpKaCloudChanged(boolean z) {
        ModemTcpKaBase modemTcpKaBase = (ModemTcpKaBase) this.mTcpKa.get();
        if (modemTcpKaBase != null) {
            modemTcpKaBase.onCloudChanged(z);
        }
    }

    @Override // com.android.phone.module.IModemModule
    public void reportModemTcpKaStatistics() {
        ModemTcpKaBase modemTcpKaBase = (ModemTcpKaBase) this.mTcpKa.get();
        if (modemTcpKaBase != null) {
            modemTcpKaBase.reportStatistics();
        }
    }

    @Override // com.android.phone.module.IModemModule
    public void dumpModemTcpKa(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        ModemTcpKaBase modemTcpKaBase = (ModemTcpKaBase) this.mTcpKa.get();
        if (modemTcpKaBase != null) {
            modemTcpKaBase.dump(fileDescriptor, printWriter, strArr);
        }
    }

    @Override // com.android.phone.module.IModemModule
    public int startTcpMonitorRegister(String str, int i, String str2, int i2, int i3) {
        ModemTcpKaBase modemTcpKaBase;
        if (ModemTcpKaBase.sCloudDisabled || (modemTcpKaBase = (ModemTcpKaBase) this.mTcpKa.get()) == null) {
            return 1;
        }
        return modemTcpKaBase.startTcpMonitorRegister(str, i, str2, i2, i3);
    }

    @Override // com.android.phone.module.IModemModule
    public int stopTcpMonitorRegister(int i) {
        ModemTcpKaBase modemTcpKaBase = (ModemTcpKaBase) this.mTcpKa.get();
        if (modemTcpKaBase != null) {
            return modemTcpKaBase.stopTcpMonitorRegister(i);
        }
        return 1;
    }

    @Override // com.android.phone.module.IModemModule
    public int startTcpKeepAlive(String str, int i, String str2, int i2, int i3, int i4) {
        ModemTcpKaBase modemTcpKaBase;
        if (ModemTcpKaBase.sCloudDisabled || (modemTcpKaBase = (ModemTcpKaBase) this.mTcpKa.get()) == null) {
            return 1;
        }
        return modemTcpKaBase.startTcpKeepAlive(str, i, str2, i2, i3, i4);
    }

    @Override // com.android.phone.module.IModemModule
    public int stopTcpKeepAlive(int i) {
        ModemTcpKaBase modemTcpKaBase = (ModemTcpKaBase) this.mTcpKa.get();
        if (modemTcpKaBase != null) {
            return modemTcpKaBase.stopTcpKeepAlive(i);
        }
        return 1;
    }

    @Override // com.android.phone.module.IModemModule
    public void registerModemTcpKaCallback(Object obj) {
        ModemTcpKaBase modemTcpKaBase = (ModemTcpKaBase) this.mTcpKa.get();
        if (modemTcpKaBase == null || !(obj instanceof IModemTcpKeepAliveIndCallback)) {
            return;
        }
        modemTcpKaBase.registerListener((IModemTcpKeepAliveIndCallback) obj);
    }

    @Override // com.android.phone.module.IModemModule
    public void unregisterModemTcpKaCallback(Object obj) {
        ModemTcpKaBase modemTcpKaBase = (ModemTcpKaBase) this.mTcpKa.get();
        if (modemTcpKaBase == null || !(obj instanceof IModemTcpKeepAliveIndCallback)) {
            return;
        }
        modemTcpKaBase.unregisterListener((IModemTcpKeepAliveIndCallback) obj);
    }

    @Override // com.android.phone.module.IModemModule
    public void updateScreenState(boolean z) {
        if (ModemHydraStatBase.getInstance() != null) {
            ModemHydraStatBase.getInstance().updateScreenState(z);
        }
    }

    @Override // com.android.phone.module.IModemModule
    public void updateModemHydraStat(String str, boolean z, int i) {
        if (ModemHydraStatBase.getInstance() != null) {
            ModemHydraStatBase.getInstance().updateModemHydraStat(str, z, i);
        }
    }

    @Override // com.android.phone.module.IModemModule
    public void updateAppScene(String str, boolean z) {
        ModemHydraStat.updateAppScene(str, z);
    }

    @Override // com.android.phone.module.IModemModule
    public void updatePingpongCellStat(int i) {
        if (ModemHydraStatBase.getInstance() != null) {
            ModemHydraStatBase.getInstance().updatePingpongCellStat(i);
        }
    }

    @Override // com.android.phone.module.IModemModule
    public void reportModemHydraStat() {
        if (ModemHydraStatBase.getInstance() != null) {
            ModemHydraStatBase.getInstance().reportModemHydraStat();
        }
    }

    @Override // com.android.phone.module.IModemModule
    public void modemHydraProcess(int i, Object obj) {
        if (ModemHydraStatBase.getInstance() != null) {
            ModemHydraStatBase.getInstance().modemHydraProcess(i, obj);
        }
    }

    @Override // com.android.phone.module.IModemModule
    public void modemHydraSendDelayMessage(int i, Object obj, int i2) {
        if (ModemHydraStatBase.getInstance() != null) {
            ModemHydraStatBase.getInstance().modemHydraSendDelayMessage(i, obj, i2);
        }
    }

    @Override // com.android.phone.module.IModemModule
    public long getCellPingPongCounts() {
        if (ModemHydraStatBase.getInstance() != null) {
            return ModemHydraStatBase.getInstance().getCellPingPongCounts();
        }
        return 0L;
    }

    @Override // com.android.phone.module.IModemModule
    public void processTopAppChanged(String str) {
        if (ModemHydraStatBase.getInstance() != null) {
            ModemHydraStatBase.getInstance().processTopAppChanged(str);
        }
    }

    @Override // com.android.phone.module.IModemModule
    public void getDataStallInfoFromModem(int i, HashMap map) {
        if (ModemHydraStatBase.getInstance() != null) {
            ModemHydraStatBase.getInstance().getDataStallInfoFromModem(i, map);
        }
    }

    @Override // com.android.phone.module.IModemModule
    public String processUeNoSaInfo(int i) {
        if (ModemHydraStatBase.getInstance() != null) {
            return ModemHydraStatBase.getInstance().processUeNoSaInfo(i);
        }
        return null;
    }

    @Override // com.android.phone.module.IModemModule
    public void getModemSimAbnormalInfo(int i, ArrayList arrayList) {
        if (ModemHydraStatBase.getInstance() != null) {
            ModemHydraStatBase.getInstance().getModemSimAbnormalInfo(i, arrayList);
        }
    }

    @Override // com.android.phone.module.IModemModule
    public ArrayList getRachFailRateInfo() {
        if (ModemHydraStatBase.getInstance() != null) {
            return ModemHydraStatBase.getInstance().getRachFailRateInfo();
        }
        return new ArrayList();
    }

    @Override // com.android.phone.module.IModemModule
    public JSONObject getAftersaleFromHydra() {
        if (ModemHydraStatBase.getInstance() != null) {
            return ModemHydraStatBase.getInstance().getAftersaleFromHydra();
        }
        return null;
    }

    @Override // com.android.phone.module.IModemModule
    public void startCaptureModemLog(String str, String str2) {
        if (ModemLogUploadToolsBase.getInstance() != null) {
            ModemLogUploadToolsBase.getInstance().StartCaptureModemLog(str, str2);
        }
    }

    @Override // com.android.phone.module.IModemModule
    public void performModemLogUploadDailyJob() {
        if (ModemLogUploadToolsBase.getInstance() != null) {
            ModemLogUploadToolsBase.getInstance().performDailyJob();
        }
    }

    @Override // com.android.phone.module.IModemModule
    public void dumpModemInfo(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (ModemDumpBase.getInstance() != null) {
            ModemDumpBase.getInstance().dump(fileDescriptor, printWriter, strArr);
        }
    }

    @Override // com.android.phone.module.IModemModule
    public boolean isModemDumpFeatureSupported(int i) {
        if (ModemDumpBase.getInstance() != null) {
            return ModemDumpBase.getInstance().isModemDumpFeatureSupported(i);
        }
        return false;
    }

    @Override // com.android.phone.module.IModemModule
    public StringBuilder getModemDumpAftersaleInfo() {
        if (ModemDumpBase.getInstance() != null) {
            return ModemDumpBase.getInstance().getAftersaleInfo();
        }
        return new StringBuilder();
    }

    @Override // com.android.phone.module.IModemModule
    public void setFastDormancyConfig(Context context, int i) {
        FastDormancy fastDormancy = (FastDormancy) this.mFastDormancy.get();
        if (fastDormancy != null) {
            fastDormancy.setFDConfigForPhoneId(context, i);
        }
    }

    @Override // com.android.phone.module.IModemModule
    public void getRfFatalDeviceStatus() {
        ModemDFS modemDFS = (ModemDFS) this.mModemDFS.get();
        if (modemDFS != null) {
            modemDFS.getRfFatalDeviceStatusForQcom();
        }
    }

    @Override // com.android.phone.module.IModemModule
    public void reportRfDeviceStatus() {
        ModemDFS modemDFS = (ModemDFS) this.mModemDFS.get();
        if (modemDFS != null) {
            modemDFS.reportRfDeviceStatusDbg();
        }
    }

    @Override // com.android.phone.module.IModemModule
    public void reportRfMainChipStatus() {
        ModemDFS modemDFS = (ModemDFS) this.mModemDFS.get();
        if (modemDFS != null) {
            modemDFS.reportRfMainChipStatusDbg();
        }
    }

    @Override // com.android.phone.module.IModemModule
    public void reportTxFbrxFault(Bundle bundle) {
        ModemDFS modemDFS = (ModemDFS) this.mModemDFS.get();
        if (modemDFS != null) {
            modemDFS.reportTxFbrxFault(bundle);
        }
    }

    @Override // com.android.phone.module.IModemModule
    public void imsErrorCausedByMBN(String str, int i) {
        ModemDFS modemDFS = (ModemDFS) this.mModemDFS.get();
        if (modemDFS != null) {
            modemDFS.imsErrorCausedByMBN(str, i);
        }
    }
}
