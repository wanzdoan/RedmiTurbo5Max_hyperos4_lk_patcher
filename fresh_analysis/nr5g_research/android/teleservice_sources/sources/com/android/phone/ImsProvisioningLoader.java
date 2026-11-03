package com.android.phone;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseArray;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public class ImsProvisioningLoader {
    private static final String LOG_TAG = "ImsProvisioningLoader";
    private Context mContext;
    private SharedPreferences mTelephonySharedPreferences;
    private SparseArray mSubIdBundleArray = new SparseArray();
    private final Object mLock = new Object();

    public ImsProvisioningLoader(Context context) {
        this.mContext = context;
        this.mTelephonySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public int getProvisioningStatus(int i, int i2, int i3, int i4) {
        initCache(i);
        return getImsProvisioningStatus(i, i2, i4, i3);
    }

    public boolean setProvisioningStatus(int i, int i2, int i3, int i4, boolean z) {
        initCache(i);
        return setImsFeatureProvisioning(i, i2, i4, i3, z);
    }

    private boolean isFileExist(int i) {
        return new File(this.mContext.getFilesDir(), getFileName(i)).exists();
    }

    private void initCache(int i) {
        ImsProvisioningLoader imsProvisioningLoader;
        int i2;
        PersistableBundle subIdBundleFromXml;
        ImsProvisioningLoader imsProvisioningLoader2;
        int i3;
        synchronized (this.mLock) {
            try {
                if (((PersistableBundle) this.mSubIdBundleArray.get(i, null)) != null) {
                    return;
                }
                if (isFileExist(i)) {
                    subIdBundleFromXml = readSubIdBundleFromXml(i);
                    imsProvisioningLoader = this;
                    i2 = i;
                } else {
                    int i4 = 0;
                    int[] iArr = {0, 1, 2, 3};
                    PersistableBundle persistableBundle = new PersistableBundle();
                    while (i4 < 4) {
                        int i5 = iArr[i4];
                        int uTProvisioningStatus = this.getUTProvisioningStatus(i, i5);
                        this.logd("check UT provisioning status " + uTProvisioningStatus);
                        if (1 == uTProvisioningStatus) {
                            imsProvisioningLoader2 = this;
                            i3 = i;
                            imsProvisioningLoader2.setProvisioningStatusToSubIdBundle(i3, 1, i5, 4, persistableBundle, uTProvisioningStatus);
                        } else {
                            imsProvisioningLoader2 = this;
                            i3 = i;
                        }
                        i4++;
                        this = imsProvisioningLoader2;
                        i = i3;
                    }
                    imsProvisioningLoader = this;
                    i2 = i;
                    imsProvisioningLoader.saveSubIdBundleToXml(i2, persistableBundle);
                    subIdBundleFromXml = persistableBundle;
                }
                imsProvisioningLoader.mSubIdBundleArray.put(i2, subIdBundleFromXml);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private int getImsProvisioningStatus(int i, int i2, int i3, int i4) {
        PersistableBundle persistableBundle;
        synchronized (this.mLock) {
            persistableBundle = (PersistableBundle) this.mSubIdBundleArray.get(i, null);
        }
        return getProvisioningStatusFromSubIdBundle(i, i2, i3, i4, persistableBundle);
    }

    private boolean setImsFeatureProvisioning(int i, int i2, int i3, int i4, boolean z) {
        synchronized (this.mLock) {
            try {
                if (getImsProvisioningStatus(i, i2, i3, i4) == z) {
                    logd("already stored provisioning status " + z + " ImsFeature " + i2 + " tech " + i3 + " capa " + i4);
                    return false;
                }
                PersistableBundle persistableBundle = (PersistableBundle) this.mSubIdBundleArray.get(i, null);
                setProvisioningStatusToSubIdBundle(i, i2, i3, i4, persistableBundle, z ? 1 : 0);
                saveSubIdBundleToXml(i, persistableBundle);
                return true;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private int getProvisioningStatusFromSubIdBundle(int i, int i2, int i3, int i4, PersistableBundle persistableBundle) {
        if (persistableBundle == null || persistableBundle.isEmpty()) {
            logd("getProvisioningStatusFromSubIdBundle", i, "xml is empty");
            return -1;
        }
        PersistableBundle persistableBundle2 = persistableBundle.getPersistableBundle(String.valueOf(i2));
        if (persistableBundle2 == null) {
            logd("getProvisioningStatusFromSubIdBundle", i, "ImsFeature " + i2 + " does not exist in xml");
            return -1;
        }
        PersistableBundle persistableBundle3 = persistableBundle2.getPersistableBundle(String.valueOf(i3));
        if (persistableBundle3 == null) {
            logd("getProvisioningStatusFromSubIdBundle", i, "RegistrationTech " + i3 + " does not exist in xml");
            return -1;
        }
        return getIntValueFromBundle(i, i3, String.valueOf(i4), persistableBundle3);
    }

    private void setProvisioningStatusToSubIdBundle(int i, int i2, int i3, int i4, PersistableBundle persistableBundle, int i5) {
        logd("setProvisioningStatusToSubIdBundle", i, "set provisioning status " + i5 + " ImsFeature " + i2 + " tech " + i3 + " capa " + i4);
        PersistableBundle persistableBundle2 = persistableBundle.getPersistableBundle(String.valueOf(i2));
        if (persistableBundle2 == null) {
            persistableBundle2 = new PersistableBundle();
            persistableBundle.putPersistableBundle(String.valueOf(i2), persistableBundle2);
        }
        PersistableBundle persistableBundle3 = persistableBundle2.getPersistableBundle(String.valueOf(i3));
        if (persistableBundle3 == null) {
            persistableBundle3 = new PersistableBundle();
            persistableBundle2.putPersistableBundle(String.valueOf(i3), persistableBundle3);
        }
        persistableBundle3.putInt(String.valueOf(i4), i5);
    }

    private int getIntValueFromBundle(int i, int i2, String str, PersistableBundle persistableBundle) {
        int i3 = persistableBundle.getInt(str, -1);
        logd("getIntValueFromBundle", i, "Cache hit, tech=" + i2 + " capability=" + str + ": returning " + i3);
        return i3;
    }

    private PersistableBundle readSubIdBundleFromXml(int i) {
        String fileName = getFileName(i);
        PersistableBundle persistableBundle = new PersistableBundle();
        synchronized (this.mLock) {
            try {
                try {
                    FileInputStream fileInputStream = new FileInputStream(new File(this.mContext.getFilesDir(), fileName));
                    persistableBundle = PersistableBundle.readFromStream(fileInputStream);
                    fileInputStream.close();
                } catch (FileNotFoundException e) {
                    logd(e.toString());
                }
            } catch (IOException e2) {
                loge(e2.toString());
            } catch (RuntimeException e3) {
                loge(e3.toString());
            }
        }
        return persistableBundle;
    }

    private void saveSubIdBundleToXml(int i, PersistableBundle persistableBundle) {
        String fileName = getFileName(i);
        if (persistableBundle == null || persistableBundle.isEmpty()) {
            logd("subIdBundle is empty");
            return;
        }
        synchronized (this.mLock) {
            try {
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(this.mContext.getFilesDir(), fileName));
                    persistableBundle.writeToStream(fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    loge(e.toString());
                }
            } catch (RuntimeException e2) {
                loge(e2.toString());
            }
        }
    }

    private int getUTProvisioningStatus(int i, int i2) {
        return getMmTelCapabilityProvisioningBitfield(i, i2) > 0 ? 1 : -1;
    }

    private int getMmTelCapabilityProvisioningBitfield(int i, int i2) {
        return this.mTelephonySharedPreferences.getInt(getMmTelProvisioningKey(i, i2), 0);
    }

    private String getMmTelProvisioningKey(int i, int i2) {
        return "provision_ims_mmtel_" + i + "_" + i2;
    }

    private String getFileName(int i) {
        return "imsprovisioningstatus_" + i + ".xml";
    }

    void clear() {
        synchronized (this.mLock) {
            this.mSubIdBundleArray.clear();
        }
    }

    void setProvisioningToXml(int i, PersistableBundle persistableBundle, String[] strArr) {
        for (String str : strArr) {
            String[] strArrSplit = str.split(",");
            setProvisioningStatusToSubIdBundle(i, Integer.valueOf(strArrSplit[0]).intValue(), Integer.valueOf(strArrSplit[1]).intValue(), Integer.valueOf(strArrSplit[2]).intValue(), persistableBundle, Integer.valueOf(strArrSplit[3]).intValue());
        }
        saveSubIdBundleToXml(i, persistableBundle);
    }

    private void loge(String str) {
        Log.e(LOG_TAG, str);
    }

    private void logd(String str, int i, String str2) {
        Log.d(LOG_TAG, str + "[" + i + "]: " + str2);
    }

    private void logd(String str) {
        Log.d(LOG_TAG, str);
    }
}
