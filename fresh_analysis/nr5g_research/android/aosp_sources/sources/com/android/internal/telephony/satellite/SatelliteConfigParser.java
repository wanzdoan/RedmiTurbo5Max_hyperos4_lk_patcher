package com.android.internal.telephony.satellite;

import android.util.Log;
import com.android.internal.telephony.TelephonyConfigData;
import com.android.internal.telephony.configupdate.ConfigParser;
import com.android.internal.telephony.configupdate.ConfigProviderAdaptor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteConfigParser extends ConfigParser<SatelliteConfig> {
    public SatelliteConfigParser(byte[] bArr) {
        super(bArr);
    }

    public SatelliteConfigParser(InputStream inputStream) throws IOException {
        super(inputStream);
    }

    public SatelliteConfigParser(File file) throws IOException {
        super(file);
    }

    /* JADX WARN: Type inference failed for: r4v4, types: [T, com.android.internal.telephony.satellite.SatelliteConfig] */
    @Override // com.android.internal.telephony.configupdate.ConfigParser
    protected void parseData(byte[] bArr) {
        try {
            try {
                if (bArr == null) {
                    Log.d("SatelliteConfigParser", "config data is null");
                    return;
                }
                TelephonyConfigData.TelephonyConfigProto from = TelephonyConfigData.TelephonyConfigProto.parseFrom(bArr);
                if (!from.hasSatellite()) {
                    Log.e("SatelliteConfigParser", "telephonyConfigData does not have satellite config");
                    return;
                }
                this.mVersion = from.getSatellite().getVersion();
                this.mConfig = new SatelliteConfig(from.getSatellite());
                Log.d("SatelliteConfigParser", "SatelliteConfig is created");
            } catch (Exception e) {
                Log.e("SatelliteConfigParser", "Parse Error", e);
                this.mVersion = -1;
                this.mConfig = null;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                this.mVersion = -1;
                this.mConfig = null;
            }
            throw th;
        }
    }

    @Override // com.android.internal.telephony.configupdate.ConfigParser
    public String getDomain() {
        return ConfigProviderAdaptor.DOMAIN_SATELLITE;
    }
}
