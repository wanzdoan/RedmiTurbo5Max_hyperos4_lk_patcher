package com.android.phone.firewall.config;

import android.content.Context;
import android.util.ArrayMap;
import android.util.Log;
import com.android.phone.firewall.model.FirewallRule;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class FireWallConfig extends ConfigFile {
    private static HashMap sInstanceMap;
    private String mFileName;

    public static synchronized FireWallConfig getInstance(Context context, String str) {
        FireWallConfig fireWallConfig;
        try {
            if (sInstanceMap == null) {
                sInstanceMap = new HashMap();
            }
            fireWallConfig = (FireWallConfig) sInstanceMap.get(str);
            if (fireWallConfig == null) {
                fireWallConfig = new FireWallConfig(context, str);
                sInstanceMap.put(str, fireWallConfig);
            }
        } catch (Throwable th) {
            throw th;
        }
        return fireWallConfig;
    }

    private FireWallConfig(Context context, String str) {
        this.mFileName = str;
        init(context);
    }

    @Override // com.android.phone.firewall.config.ConfigFile
    protected String getFileName() {
        return this.mFileName;
    }

    public synchronized void set(String str, FirewallRule firewallRule) {
        set(str, firewallRule.toString());
    }

    public synchronized ArrayMap getPairMap() {
        ArrayMap arrayMap;
        int i;
        arrayMap = new ArrayMap();
        for (Map.Entry entry : this.mPairValues.entrySet()) {
            String str = (String) entry.getKey();
            try {
                i = Integer.parseInt((String) entry.getValue());
            } catch (NumberFormatException e) {
                Log.i("PhoneFireWallConfig", "parse rule exception", e);
                i = 1;
            }
            FirewallRule firewallRule = FirewallRule.parse(i);
            if (str != null && firewallRule != null) {
                arrayMap.put(str, firewallRule);
            }
        }
        return arrayMap;
    }
}
