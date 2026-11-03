package com.android.internal.telephony.phonenumbers.prefixmapper;

import com.android.internal.telephony.phonenumbers.MetadataLoader;
import com.android.internal.telephony.phonenumbers.Phonenumber$PhoneNumber;
import com.android.internal.telephony.phonenumbers.metadata.DefaultMetadataDependenciesProvider;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/* JADX INFO: loaded from: classes.dex */
public class PrefixFileReader {
    private static final Logger logger = Logger.getLogger(PrefixFileReader.class.getName());
    private final String phonePrefixDataDirectory;
    private MappingFileProvider mappingFileProvider = new MappingFileProvider();
    private Map availablePhonePrefixMaps = new HashMap();
    private final MetadataLoader metadataLoader = DefaultMetadataDependenciesProvider.getInstance().getMetadataLoader();

    public PrefixFileReader(String str) throws Throwable {
        this.phonePrefixDataDirectory = str;
        loadMappingFileProvider();
    }

    private void loadMappingFileProvider() throws Throwable {
        ObjectInputStream objectInputStream = null;
        try {
            try {
                ObjectInputStream objectInputStream2 = new ObjectInputStream(this.metadataLoader.loadMetadata(this.phonePrefixDataDirectory + "config"));
                try {
                    this.mappingFileProvider.readExternal(objectInputStream2);
                    close(objectInputStream2);
                } catch (IOException e) {
                    e = e;
                    objectInputStream = objectInputStream2;
                    logger.log(Level.WARNING, e.toString());
                    close(objectInputStream);
                } catch (Throwable th) {
                    th = th;
                    objectInputStream = objectInputStream2;
                    close(objectInputStream);
                    throw th;
                }
            } catch (IOException e2) {
                e = e2;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private PhonePrefixMap getPhonePrefixDescriptions(int i, String str, String str2, String str3) {
        String fileName = this.mappingFileProvider.getFileName(i, str, str2, str3);
        synchronized (PrefixFileReader.class) {
            try {
                if (fileName.length() == 0) {
                    return null;
                }
                if (!this.availablePhonePrefixMaps.containsKey(fileName)) {
                    loadPhonePrefixMapFromFile(fileName);
                }
                return (PhonePrefixMap) this.availablePhonePrefixMaps.get(fileName);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void loadPhonePrefixMapFromFile(String str) throws Throwable {
        ObjectInputStream objectInputStream = null;
        try {
            try {
                ObjectInputStream objectInputStream2 = new ObjectInputStream(this.metadataLoader.loadMetadata(this.phonePrefixDataDirectory + str));
                try {
                    PhonePrefixMap phonePrefixMap = new PhonePrefixMap();
                    phonePrefixMap.readExternal(objectInputStream2);
                    this.availablePhonePrefixMaps.put(str, phonePrefixMap);
                    close(objectInputStream2);
                } catch (IOException e) {
                    e = e;
                    objectInputStream = objectInputStream2;
                    logger.log(Level.WARNING, e.toString());
                    close(objectInputStream);
                } catch (Throwable th) {
                    th = th;
                    objectInputStream = objectInputStream2;
                    close(objectInputStream);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (IOException e2) {
            e = e2;
        }
    }

    private static void close(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.log(Level.WARNING, e.toString());
            }
        }
    }

    public String getDescriptionForNumber(Phonenumber$PhoneNumber phonenumber$PhoneNumber, String str, String str2, String str3) {
        int countryCode = phonenumber$PhoneNumber.getCountryCode();
        if (countryCode == 1) {
            countryCode = ((int) (phonenumber$PhoneNumber.getNationalNumber() / 10000000)) + 1000;
        }
        PhonePrefixMap phonePrefixDescriptions = getPhonePrefixDescriptions(countryCode, str, str2, str3);
        String strLookup = phonePrefixDescriptions != null ? phonePrefixDescriptions.lookup(phonenumber$PhoneNumber) : null;
        if ((strLookup == null || strLookup.length() == 0) && mayFallBackToEnglish(str)) {
            PhonePrefixMap phonePrefixDescriptions2 = getPhonePrefixDescriptions(countryCode, "en", "", "");
            if (phonePrefixDescriptions2 == null) {
                return "";
            }
            strLookup = phonePrefixDescriptions2.lookup(phonenumber$PhoneNumber);
        }
        return strLookup != null ? strLookup : "";
    }

    private boolean mayFallBackToEnglish(String str) {
        return (str.equals("zh") || str.equals("ja") || str.equals("ko")) ? false : true;
    }
}
