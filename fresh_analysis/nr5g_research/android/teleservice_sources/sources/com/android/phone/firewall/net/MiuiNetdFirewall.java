package com.android.phone.firewall.net;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import com.android.phone.firewall.model.FirewallRule;
import java.util.regex.Pattern;
import miui.os.HyperOSCustFeatureResolve;
import miui.security.SecurityManager;

/* JADX INFO: loaded from: classes.dex */
public class MiuiNetdFirewall implements IFirewall {
    private static final String PKG_NAME_DINGDING = HyperOSCustFeatureResolve.getString("config_dingding_packages", "");
    private Context mContext;
    private HandlerThread mHandlerThread;
    boolean mIsSatelliteEnabled;
    private volatile boolean mIsStarted;
    private IFirewallListener mListener;
    private SecurityManager mSecurityManager;
    private Object mLock = new Object();
    private Object mRuleMapLock = new Object();
    private ArrayMap mSatelliteRuleMap = new ArrayMap();
    private WorkHandler mWorkHandler;
    ContentObserver mObserver = new ContentObserver(this.mWorkHandler) { // from class: com.android.phone.firewall.net.MiuiNetdFirewall.1
        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            if (uri == null) {
                MiuiNetdFirewall.this.log("uri is null");
                return;
            }
            if (uri.equals(Settings.System.getUriFor("satellite_state"))) {
                MiuiNetdFirewall.this.mWorkHandler.sendEmptyMessage(13);
                return;
            }
            if (uri.equals(Settings.Global.getUriFor("dingding_satellite_ip"))) {
                MiuiNetdFirewall miuiNetdFirewall = MiuiNetdFirewall.this;
                miuiNetdFirewall.mDingDingSatelliteIp = miuiNetdFirewall.getDingdingSatelliteIp();
                MiuiNetdFirewall.this.log("mDingDingSatelliteIp=" + MiuiNetdFirewall.this.mDingDingSatelliteIp);
            }
        }
    };
    private String mDingDingSatelliteIp = getDingdingSatelliteIp();

    public MiuiNetdFirewall(Context context) {
        this.mIsSatelliteEnabled = false;
        this.mContext = context;
        this.mSecurityManager = (SecurityManager) context.getSystemService("security");
        this.mIsSatelliteEnabled = isSatelliteEnabled();
        if (this.mIsSatelliteEnabled) {
            return;
        }
        ruleManagerSetCurrentNetworkState(1);
    }

    @Override // com.android.phone.firewall.net.IFirewall
    public boolean start() {
        synchronized (this.mLock) {
            try {
                if (!this.mIsStarted) {
                    this.mIsStarted = true;
                    startWorkHandler();
                    registerSettingsObserver();
                    Log.i("PhoneNetdFirewall", "netd firewall start");
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return true;
    }

    private void startWorkHandler() {
        HandlerThread handlerThread = new HandlerThread("PhoneNetdFirewall");
        this.mHandlerThread = handlerThread;
        handlerThread.start();
        this.mWorkHandler = new WorkHandler(this.mHandlerThread.getLooper());
    }

    @Override // com.android.phone.firewall.net.IFirewall
    public boolean isStarted() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIsStarted;
        }
        return z;
    }

    @Override // com.android.phone.firewall.net.IFirewall
    public void setSatelliteRule(String str, FirewallRule firewallRule) {
        synchronized (this.mRuleMapLock) {
            try {
                FirewallRule firewallRule2 = FirewallRule.Restrict;
                ArrayMap arrayMap = this.mSatelliteRuleMap;
                if (firewallRule == firewallRule2) {
                    arrayMap.remove(str);
                } else {
                    arrayMap.put(str, firewallRule);
                }
                setMiuiFirewallRule(str, firewallRule.value(), 0);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.android.phone.firewall.net.IFirewall
    public FirewallRule getSatelliteRule(String str) {
        synchronized (this.mRuleMapLock) {
            try {
                FirewallRule firewallRule = (FirewallRule) this.mSatelliteRuleMap.get(str);
                if (firewallRule != null) {
                    return firewallRule;
                }
                return FirewallRule.Restrict;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getUid(String str) {
        return getUidByPackageName(this.mContext, str);
    }

    private int getUidByPackageName(Context context, String str) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(str, 0);
            if (applicationInfo != null) {
                return applicationInfo.uid;
            }
            return -1;
        } catch (PackageManager.NameNotFoundException unused) {
            log("not find packageName :" + str);
            return -1;
        }
    }

    @Override // com.android.phone.firewall.net.IFirewall
    public void loadSatelliteRules(ArrayMap arrayMap) {
        synchronized (this.mRuleMapLock) {
            for (int i = 0; i < arrayMap.size(); i++) {
                try {
                    String str = (String) arrayMap.keyAt(i);
                    FirewallRule firewallRule = (FirewallRule) arrayMap.valueAt(i);
                    this.mSatelliteRuleMap.put(str, firewallRule);
                    if (firewallRule == FirewallRule.Allow) {
                        setMiuiFirewallRule(str, firewallRule.value(), 0);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    @Override // com.android.phone.firewall.net.IFirewall
    public void setListener(IFirewallListener iFirewallListener) {
        this.mListener = iFirewallListener;
    }

    public void registerSettingsObserver() {
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("satellite_state"), false, this.mObserver);
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("dingding_satellite_ip"), false, this.mObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ruleManagerSetCurrentNetworkState(int i) {
        log("ruleManagerSetCurrentNetworkState: " + i);
        NetdExecutor.setSecFirewallRule("ntnfirewall>" + i);
        SecurityManager securityManager = this.mSecurityManager;
        if (securityManager != null) {
            securityManager.setNtnNetworkState(i);
        }
    }

    private void setMiuiFirewallRule(String str, int i, int i2) {
        if (this.mWorkHandler == null) {
            return;
        }
        Message messageObtain = Message.obtain();
        messageObtain.what = 12;
        Bundle bundle = new Bundle();
        bundle.putString("package_name", str);
        bundle.putInt("rule", i);
        bundle.putInt("type", i2);
        messageObtain.setData(bundle);
        this.mWorkHandler.sendMessage(messageObtain);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ruleManagerSetMiuiFirewallRule(Context context, String str, int i, int i2, int i3) {
        log("ruleManagerSetMiuiFirewallRule: " + str + ", uid=" + i + ", rule=" + i2 + ", type=" + i3);
        String str2 = PKG_NAME_DINGDING;
        if (!str2.equals(str)) {
            NetdExecutor.setSecFirewallRule("ntnfirewall>" + i + ">" + i2 + ">" + i3);
        }
        if (this.mSecurityManager != null) {
            boolean zEquals = str2.equals(str);
            SecurityManager securityManager = this.mSecurityManager;
            if (zEquals) {
                securityManager.setNtnMiuiFirewallRule(str, this.mDingDingSatelliteIp, i, i2, i3);
            } else {
                securityManager.setNtnMiuiFirewallRule(str, (String) null, i, i2, i3);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ruleManagerSetMiuiFirewallRuleForDingding() {
        SecurityManager securityManager;
        String str = PKG_NAME_DINGDING;
        int uid = getUid(str);
        log("ruleManagerSetMiuiFirewallRuleForDingding, uid=" + uid);
        if (uid == -1 || (securityManager = this.mSecurityManager) == null) {
            return;
        }
        securityManager.setNtnMiuiFirewallRule(str, this.mDingDingSatelliteIp, uid, FirewallRule.Allow.value(), 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSatelliteEnabled() {
        return Settings.System.getInt(this.mContext.getContentResolver(), "satellite_state", 0) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getDingdingSatelliteIp() {
        String string = Settings.Global.getString(this.mContext.getContentResolver(), "dingding_satellite_ip");
        return !isValidIPv4(string) ? "203.119.238.130" : string;
    }

    public boolean isValidIPv4(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return Pattern.compile("^((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])$").matcher(str).matches();
    }

    public class WorkHandler extends Handler {
        WorkHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            MiuiNetdFirewall.this.log("handleMessage: " + message.what);
            switch (message.what) {
                case 11:
                    MiuiNetdFirewall.this.ruleManagerSetCurrentNetworkState(((Integer) message.obj).intValue());
                    break;
                case 12:
                    doSetFirewallRule(message.getData());
                    break;
                case 13:
                    boolean zIsSatelliteEnabled = MiuiNetdFirewall.this.isSatelliteEnabled();
                    MiuiNetdFirewall.this.log("mIsSatelliteEnabled=" + MiuiNetdFirewall.this.mIsSatelliteEnabled + ", isSatelliteEnabled: " + zIsSatelliteEnabled);
                    MiuiNetdFirewall miuiNetdFirewall = MiuiNetdFirewall.this;
                    if (miuiNetdFirewall.mIsSatelliteEnabled != zIsSatelliteEnabled) {
                        miuiNetdFirewall.mIsSatelliteEnabled = zIsSatelliteEnabled;
                        if (zIsSatelliteEnabled) {
                            miuiNetdFirewall.ruleManagerSetCurrentNetworkState(0);
                            MiuiNetdFirewall.this.ruleManagerSetMiuiFirewallRuleForDingding();
                        } else {
                            miuiNetdFirewall.ruleManagerSetCurrentNetworkState(1);
                        }
                        break;
                    }
                    break;
            }
        }

        private void doSetFirewallRule(Bundle bundle) {
            if (bundle == null) {
                return;
            }
            String string = bundle.getString("package_name");
            int i = bundle.getInt("rule", -10);
            int i2 = bundle.getInt("type", -10);
            if (!TextUtils.isEmpty(string) && i != -10 && i2 != -10) {
                MiuiNetdFirewall miuiNetdFirewall = MiuiNetdFirewall.this;
                miuiNetdFirewall.ruleManagerSetMiuiFirewallRule(miuiNetdFirewall.mContext, string, MiuiNetdFirewall.this.getUid(string), i, i2);
            } else {
                Log.e("PhoneNetdFirewall", String.format("setMiuiFirewallRule failed！pkgName: %s , rule: %s , type: %s", string, Integer.valueOf(i), Integer.valueOf(i2)));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("PhoneNetdFirewall", str);
    }
}
