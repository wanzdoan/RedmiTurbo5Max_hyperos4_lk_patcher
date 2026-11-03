package com.android.internal.telephony.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.TelephonyNetworkSpecifier;
import android.os.Handler;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConfigurationManager;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class CellularNetworkValidator {
    public static final long MAX_VALIDATION_CACHE_TTL = 86400000;
    private static CellularNetworkValidator sInstance;
    private final ConnectivityManager mConnectivityManager;
    private final Context mContext;
    private final FeatureFlags mFlags;
    public ConnectivityNetworkCallback mNetworkCallback;
    private boolean mRequireTestPass;
    private int mSubId;
    private ValidationCallback mValidationCallback;
    private int mState = 0;
    public Handler mHandler = new Handler();
    private final ValidatedNetworkCache mValidatedNetworkCache = new ValidatedNetworkCache();

    public interface ValidationCallback {
        void onNetworkAvailable(Network network, int i);

        void onValidationDone(boolean z, int i);
    }

    private class ValidatedNetworkCache {
        private final ArrayDeque mValidatedNetworkAQ;
        private final Map mValidatedNetworkMap;

        private ValidatedNetworkCache() {
            this.mValidatedNetworkAQ = new ArrayDeque();
            this.mValidatedNetworkMap = new HashMap();
        }

        private static final class ValidatedNetwork {
            final String mValidationIdentity;
            long mValidationTimeStamp;

            ValidatedNetwork(String str, long j) {
                this.mValidationIdentity = str;
                this.mValidationTimeStamp = j;
            }

            void update(long j) {
                this.mValidationTimeStamp = j;
            }
        }

        synchronized boolean isRecentlyValidated(int i) {
            long validationCacheTtl = getValidationCacheTtl(i);
            String validationNetworkIdentity = getValidationNetworkIdentity(i);
            if (validationNetworkIdentity != null && this.mValidatedNetworkMap.containsKey(validationNetworkIdentity)) {
                boolean z = System.currentTimeMillis() - ((ValidatedNetwork) this.mValidatedNetworkMap.get(validationNetworkIdentity)).mValidationTimeStamp < validationCacheTtl;
                CellularNetworkValidator.logd("isRecentlyValidated on subId " + i + " ? " + z);
                return z;
            }
            return false;
        }

        synchronized void storeLastValidationResult(int i, boolean z) {
            try {
                String validationNetworkIdentity = getValidationNetworkIdentity(i);
                StringBuilder sb = new StringBuilder("storeLastValidationResult for subId ");
                sb.append(i);
                sb.append(z ? " validated." : " not validated.");
                CellularNetworkValidator.logd(sb.toString());
                if (validationNetworkIdentity == null) {
                    return;
                }
                if (!z) {
                    this.mValidatedNetworkAQ.remove(this.mValidatedNetworkMap.get(validationNetworkIdentity));
                    this.mValidatedNetworkMap.remove(validationNetworkIdentity);
                    return;
                }
                long jCurrentTimeMillis = System.currentTimeMillis();
                ValidatedNetwork validatedNetwork = (ValidatedNetwork) this.mValidatedNetworkMap.get(validationNetworkIdentity);
                if (validatedNetwork != null) {
                    validatedNetwork.update(jCurrentTimeMillis);
                    this.mValidatedNetworkAQ.remove(validatedNetwork);
                    this.mValidatedNetworkAQ.add(validatedNetwork);
                } else {
                    ValidatedNetwork validatedNetwork2 = new ValidatedNetwork(validationNetworkIdentity, jCurrentTimeMillis);
                    this.mValidatedNetworkMap.put(validationNetworkIdentity, validatedNetwork2);
                    this.mValidatedNetworkAQ.add(validatedNetwork2);
                }
                if (this.mValidatedNetworkAQ.size() > 10) {
                    this.mValidatedNetworkMap.remove(((ValidatedNetwork) this.mValidatedNetworkAQ.poll()).mValidationIdentity);
                }
            } catch (Throwable th) {
                throw th;
            }
        }

        private String getValidationNetworkIdentity(int i) {
            Phone phone;
            NetworkRegistrationInfo networkRegistrationInfo;
            if (SubscriptionManager.isUsableSubscriptionId(i) && (phone = PhoneFactory.getPhone(SubscriptionManagerService.getInstance().getPhoneId(i))) != null && phone.getServiceState() != null && (networkRegistrationInfo = phone.getServiceState().getNetworkRegistrationInfo(2, 1)) != null && networkRegistrationInfo.getCellIdentity() != null) {
                CellIdentity cellIdentity = networkRegistrationInfo.getCellIdentity();
                if (cellIdentity.getType() == 3 && cellIdentity.getMccString() != null && cellIdentity.getMncString() != null) {
                    CellIdentityLte cellIdentityLte = (CellIdentityLte) cellIdentity;
                    if (cellIdentityLte.getTac() != Integer.MAX_VALUE) {
                        return cellIdentity.getMccString() + cellIdentity.getMncString() + "_" + cellIdentityLte.getTac() + "_" + i;
                    }
                }
            }
            return null;
        }

        private long getValidationCacheTtl(int i) {
            PersistableBundle configForSubId;
            CarrierConfigManager carrierConfigManager = (CarrierConfigManager) CellularNetworkValidator.this.mContext.getSystemService("carrier_config");
            return Math.min((carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(i)) == null) ? 0L : configForSubId.getLong("data_switch_validation_min_gap_long"), CellularNetworkValidator.MAX_VALIDATION_CACHE_TTL);
        }
    }

    public static CellularNetworkValidator make(Context context, FeatureFlags featureFlags) {
        if (sInstance != null) {
            logd("createCellularNetworkValidator failed. Instance already exists.");
        } else {
            sInstance = new CellularNetworkValidator(context, featureFlags);
        }
        return sInstance;
    }

    public static CellularNetworkValidator getInstance() {
        return sInstance;
    }

    public boolean isValidationFeatureSupported() {
        return PhoneConfigurationManager.getInstance().getCurrentPhoneCapability().isNetworkValidationBeforeSwitchSupported();
    }

    public CellularNetworkValidator(Context context, FeatureFlags featureFlags) {
        this.mContext = context;
        this.mFlags = featureFlags;
        this.mConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
    }

    public synchronized void validate(final int i, long j, boolean z, ValidationCallback validationCallback) {
        try {
            if (i == this.mSubId) {
                return;
            }
            SubscriptionInfoInternal subscriptionInfoInternal = SubscriptionManagerService.getInstance().getSubscriptionInfoInternal(i);
            if (subscriptionInfoInternal != null && subscriptionInfoInternal.isActive()) {
                if (isValidating()) {
                    stopValidation();
                }
                this.mState = 1;
                this.mSubId = i;
                this.mValidationCallback = validationCallback;
                this.mRequireTestPass = z;
                logd("Start validating subId " + this.mSubId + " timeoutInMs " + j + " mRequireTestPass " + this.mRequireTestPass);
                this.mNetworkCallback = new ConnectivityNetworkCallback(i);
                this.mConnectivityManager.requestNetwork(createNetworkRequest(i), this.mNetworkCallback, this.mHandler);
                this.mHandler.postDelayed(new Runnable() { // from class: com.android.internal.telephony.data.CellularNetworkValidator$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$validate$0(i);
                    }
                }, j);
                return;
            }
            logd("Failed to start validation. Inactive subId " + i);
            validationCallback.onValidationDone(false, i);
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onValidationTimeout, reason: merged with bridge method [inline-methods] */
    public synchronized void lambda$validate$0(int i) {
        logd("timeout on subId " + i + " validation.");
        this.mValidatedNetworkCache.storeLastValidationResult(i, false);
        reportValidationResult(false, i);
    }

    public synchronized void stopValidation() {
        if (!isValidating()) {
            logd("No need to stop validation.");
            return;
        }
        ConnectivityNetworkCallback connectivityNetworkCallback = this.mNetworkCallback;
        if (connectivityNetworkCallback != null) {
            this.mConnectivityManager.unregisterNetworkCallback(connectivityNetworkCallback);
        }
        this.mState = 0;
        this.mHandler.removeCallbacksAndMessages(null);
        this.mSubId = -1;
    }

    public synchronized int getSubIdInValidation() {
        return this.mSubId;
    }

    public synchronized boolean isValidating() {
        return this.mState != 0;
    }

    private NetworkRequest createNetworkRequest(int i) {
        NetworkRequest.Builder networkSpecifier = new NetworkRequest.Builder().addCapability(12).addTransportType(0).setNetworkSpecifier(new TelephonyNetworkSpecifier.Builder().setSubscriptionId(i).build());
        Phone phone = PhoneFactory.getPhone(SubscriptionManager.getPhoneId(i));
        if (phone != null && phone.getServiceState().isUsingNonTerrestrialNetwork()) {
            networkSpecifier.addTransportType(10).removeCapability(13);
        }
        return networkSpecifier.build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void reportValidationResult(boolean z, int i) {
        try {
            if (this.mSubId != i) {
                return;
            }
            this.mHandler.removeCallbacksAndMessages(null);
            int i2 = this.mState;
            if (i2 == 1 || i2 == 2) {
                if (i2 == 1) {
                    this.mValidationCallback.onValidationDone(z, this.mSubId);
                }
                this.mState = 2;
                if (z || !this.mRequireTestPass) {
                    this.mHandler.postDelayed(new Runnable() { // from class: com.android.internal.telephony.data.CellularNetworkValidator$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.stopValidation();
                        }
                    }, 5000L);
                } else {
                    stopValidation();
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void reportNetworkAvailable(Network network, int i) {
        if (this.mSubId != i) {
            return;
        }
        this.mValidationCallback.onNetworkAvailable(network, i);
    }

    public class ConnectivityNetworkCallback extends ConnectivityManager.NetworkCallback {
        private final int mSubId;

        ConnectivityNetworkCallback(int i) {
            this.mSubId = i;
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(Network network) {
            CellularNetworkValidator.logd("network onAvailable " + network);
            boolean zIsRecentlyValidated = CellularNetworkValidator.this.mValidatedNetworkCache.isRecentlyValidated(this.mSubId);
            CellularNetworkValidator cellularNetworkValidator = CellularNetworkValidator.this;
            if (zIsRecentlyValidated) {
                cellularNetworkValidator.reportValidationResult(true, this.mSubId);
            } else {
                cellularNetworkValidator.reportNetworkAvailable(network, this.mSubId);
            }
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLosing(Network network, int i) {
            CellularNetworkValidator.logd("network onLosing " + network + " maxMsToLive " + i);
            CellularNetworkValidator.this.mValidatedNetworkCache.storeLastValidationResult(this.mSubId, false);
            CellularNetworkValidator.this.reportValidationResult(false, this.mSubId);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            CellularNetworkValidator.logd("network onLost " + network);
            CellularNetworkValidator.this.mValidatedNetworkCache.storeLastValidationResult(this.mSubId, false);
            CellularNetworkValidator.this.reportValidationResult(false, this.mSubId);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onUnavailable() {
            CellularNetworkValidator.logd("onUnavailable");
            CellularNetworkValidator.this.mValidatedNetworkCache.storeLastValidationResult(this.mSubId, false);
            CellularNetworkValidator.this.reportValidationResult(false, this.mSubId);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            if (networkCapabilities.hasCapability(16)) {
                CellularNetworkValidator.logd("onValidated");
                CellularNetworkValidator.this.mValidatedNetworkCache.storeLastValidationResult(this.mSubId, true);
                CellularNetworkValidator.this.reportValidationResult(true, this.mSubId);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String str) {
        Log.d("NetworkValidator", str);
    }
}
