package com.android.phone.firewall.net;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import com.android.phone.firewall.config.FireWallConfig;
import com.android.phone.firewall.model.FirewallRule;

/* JADX INFO: loaded from: classes.dex */
public class FirewallService extends Service {
    private Context mContext;
    private IFirewall mFirewall;
    private FirewallBinder mFirewallBinder;
    private FireWallConfig mSatelliteFirewallConfig;
    private IFirewallListener mIFirewallListener = new IFirewallListener() { // from class: com.android.phone.firewall.net.FirewallService.1
    };
    private Handler mHandler = new Handler() { // from class: com.android.phone.firewall.net.FirewallService.2
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 2) {
                return;
            }
            FirewallService.this.saveRuleData();
        }
    };

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        Log.i("PhoneFireWallService", "onBind");
        return this.mFirewallBinder.asBinder();
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    class FirewallBinder extends IFirewallBinder.Stub {
        private FirewallBinder() {
        }

        @Override // com.android.phone.firewall.net.IFirewallBinder
        public boolean isStarted() {
            return FirewallService.this.mFirewall.isStarted();
        }

        @Override // com.android.phone.firewall.net.IFirewallBinder
        public void setSatelliteRule(String str, FirewallRule firewallRule) {
            FirewallService.this.log("setSatelliteRule");
            FirewallService.this.mSatelliteFirewallConfig.set(str, firewallRule);
            FirewallService.this.mFirewall.setSatelliteRule(str, firewallRule);
            FirewallService.this.saveRuleDataDelay();
            Log.i("PhoneFireWallService", String.format("set satellite rule for %s:%s", str, firewallRule.toString()));
        }

        @Override // com.android.phone.firewall.net.IFirewallBinder
        public FirewallRule getSatelliteRule(String str) {
            FirewallService.this.log("getSatelliteRule");
            return FirewallService.this.mFirewall.getSatelliteRule(str);
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        Context applicationContext = getApplicationContext();
        this.mContext = applicationContext;
        this.mSatelliteFirewallConfig = FireWallConfig.getInstance(applicationContext, "fire_wall_satellite_config.db");
        this.mFirewallBinder = new FirewallBinder();
        createFireWall();
        super.onCreate();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        return super.onStartCommand(intent, 1, i2);
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
    }

    private void createFireWall() {
        MiuiNetdFirewall miuiNetdFirewall = new MiuiNetdFirewall(getApplicationContext());
        this.mFirewall = miuiNetdFirewall;
        miuiNetdFirewall.start();
        this.mFirewall.setListener(this.mIFirewallListener);
        loadSatelliteFirewall();
    }

    private void loadSatelliteFirewall() {
        this.mFirewall.loadSatelliteRules(this.mSatelliteFirewallConfig.getPairMap());
        log("loadSatelliteFirewall over");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveRuleDataDelay() {
        if (this.mHandler.hasMessages(2)) {
            this.mHandler.removeMessages(2);
        }
        this.mHandler.sendEmptyMessageDelayed(2, 100L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveRuleData() {
        this.mSatelliteFirewallConfig.saveNow();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("PhoneFireWallService", str);
    }
}
