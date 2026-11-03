package com.android.phone.firewall.net;

import android.util.ArrayMap;
import com.android.phone.firewall.model.FirewallRule;

/* JADX INFO: loaded from: classes.dex */
public interface IFirewall {
    FirewallRule getSatelliteRule(String str);

    boolean isStarted();

    void loadSatelliteRules(ArrayMap arrayMap);

    void setListener(IFirewallListener iFirewallListener);

    void setSatelliteRule(String str, FirewallRule firewallRule);

    boolean start();
}
