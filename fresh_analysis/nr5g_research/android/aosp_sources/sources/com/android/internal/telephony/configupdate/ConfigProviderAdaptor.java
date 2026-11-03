package com.android.internal.telephony.configupdate;

import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public interface ConfigProviderAdaptor {
    public static final String DOMAIN_DATA = "data";
    public static final String DOMAIN_SATELLITE = "satellite";

    public static class Callback {
        public void onChanged(ConfigParser configParser) {
        }
    }

    ConfigParser getConfigParser(String str);

    void registerCallback(Executor executor, Callback callback);

    void unregisterCallback(Callback callback);
}
