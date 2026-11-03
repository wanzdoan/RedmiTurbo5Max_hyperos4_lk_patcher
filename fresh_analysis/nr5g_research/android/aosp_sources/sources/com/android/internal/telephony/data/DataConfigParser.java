package com.android.internal.telephony.data;

import android.util.Log;
import com.android.internal.telephony.TelephonyConfigData;
import com.android.internal.telephony.configupdate.ConfigParser;
import com.android.internal.telephony.configupdate.ConfigProviderAdaptor;
import com.android.internal.telephony.protobuf.InvalidProtocolBufferException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public class DataConfigParser extends ConfigParser<DataConfig> {
    public DataConfigParser(byte[] bArr) {
        super(bArr);
    }

    public DataConfigParser(InputStream inputStream) throws IOException {
        super(inputStream);
    }

    public DataConfigParser(File file) throws IOException {
        super(new FileInputStream(file));
    }

    /* JADX WARN: Type inference failed for: r0v6, types: [T, com.android.internal.telephony.data.DataConfig] */
    @Override // com.android.internal.telephony.configupdate.ConfigParser
    public void parseData(byte[] bArr) {
        Log.d("DataConfigParser", "parseData");
        try {
            TelephonyConfigData.TelephonyConfigProto from = TelephonyConfigData.TelephonyConfigProto.parseFrom(bArr);
            if (from.hasData()) {
                this.mVersion = from.getData().getVersion();
                this.mConfig = new DataConfig(from.getData());
                Log.d("DataConfigParser", "DataConfig is created");
            }
        } catch (InvalidProtocolBufferException e) {
            Log.e("DataConfigParser", "InvalidProtocolBufferException : " + e);
        } catch (Exception e2) {
            Log.e("DataConfigParser", "Exception : " + e2);
        }
    }

    @Override // com.android.internal.telephony.configupdate.ConfigParser
    public String getDomain() {
        return ConfigProviderAdaptor.DOMAIN_DATA;
    }
}
