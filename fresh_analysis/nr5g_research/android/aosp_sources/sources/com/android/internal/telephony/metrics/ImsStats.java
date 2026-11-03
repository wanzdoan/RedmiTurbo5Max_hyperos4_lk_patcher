package com.android.internal.telephony.metrics;

import android.os.SystemClock;
import android.telephony.ServiceState;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsRegistrationAttributes;
import android.telephony.ims.feature.MmTelFeature;
import android.util.Patterns;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.nano.PersistAtomsProto$ImsRegistrationStats;
import com.android.internal.telephony.nano.PersistAtomsProto$ImsRegistrationTermination;
import com.android.telephony.Rlog;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public class ImsStats {
    private static final String TAG = "ImsStats";
    private long mLastTimestamp;
    private final ImsPhone mPhone;
    private static final Pattern PATTERN_UUID = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
    private static final Pattern PATTERN_URI = Pattern.compile("([a-zA-Z]{2,}:)" + Patterns.EMAIL_ADDRESS.pattern());
    private static final Pattern PATTERN_IPV4 = Pattern.compile("((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9]))");
    private static final Pattern PATTERN_IPV6 = Pattern.compile("([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,6}(:[0-9a-fA-F]{1,4}){1,6}|([0-9a-fA-F]{1,4}:){1,7}:|:(:[0-9a-fA-F]{1,4}){1,7}");
    private static final Pattern PATTERN_IMEI = Pattern.compile("(^|[^0-9])(?:[0-9]{8}-[0-9]{6}-[0-9][0-9]?|[0-9]{2}-[0-9]{6}-[0-9]{6}-[0-9][0-9]?|[0-9]{16})($|[^0-9])");
    private static final Pattern PATTERN_UNSEGMENTED_IMEI_IMSI = Pattern.compile("(^|[^0-9])[0-9]{15}($|[^0-9])");
    private static final Pattern PATTERN_HOSTNAME = Pattern.compile("([0-9a-zA-Z][0-9a-zA-Z_\\-]{0,61}[0-9a-zA-Z]\\.){2,}[a-zA-Z]{2,}");
    private static final Pattern PATTERN_UNKNOWN_ID = Pattern.compile("(^|[^0-9a-fA-F])(([-\\.]?0)*[1-9a-fA-F]([-\\.]?[0-9a-fA-F]){5,}|0*[1-9a-fA-F]([0-9a-fA-F]){4,})");
    private int mLastRegistrationState = 0;
    int mLastTransportType = -1;
    private MmTelFeature.MmTelCapabilities mLastAvailableFeatures = new MmTelFeature.MmTelCapabilities();
    private final MmTelFeature.MmTelCapabilities mLastWwanCapableFeatures = new MmTelFeature.MmTelCapabilities();
    private final MmTelFeature.MmTelCapabilities mLastWlanCapableFeatures = new MmTelFeature.MmTelCapabilities();
    private final PersistAtomsStorage mStorage = PhoneFactory.getMetricsCollector().getAtomsStorage();
    private PersistAtomsProto$ImsRegistrationStats mLastRegistrationStats = getDefaultImsRegistrationStats();

    public ImsStats(ImsPhone imsPhone) {
        this.mPhone = imsPhone;
        updateImsRegistrationStats();
        this.mLastTimestamp = getTimeMillis();
    }

    public synchronized void conclude() {
        MmTelFeature.MmTelCapabilities mmTelCapabilities;
        try {
            long timeMillis = getTimeMillis();
            long j = timeMillis - this.mLastTimestamp;
            if (j < 1000) {
                logw("conclude: discarding transient stats, duration=%d", Long.valueOf(j));
            } else {
                PersistAtomsProto$ImsRegistrationStats persistAtomsProto$ImsRegistrationStatsCopyOfDimensionsOnly = copyOfDimensionsOnly(this.mLastRegistrationStats);
                if (persistAtomsProto$ImsRegistrationStatsCopyOfDimensionsOnly.rat == 0) {
                    logw("conclude: discarding UNKNOWN RAT, duration=%d", Long.valueOf(j));
                    this.mLastTimestamp = timeMillis;
                    return;
                }
                PersistAtomsProto$ImsRegistrationStats persistAtomsProto$ImsRegistrationStats = this.mLastRegistrationStats;
                persistAtomsProto$ImsRegistrationStatsCopyOfDimensionsOnly.registeredTimes = persistAtomsProto$ImsRegistrationStats.registeredTimes;
                persistAtomsProto$ImsRegistrationStats.registeredTimes = 0;
                int i = this.mLastRegistrationState;
                if (i == 0) {
                    persistAtomsProto$ImsRegistrationStatsCopyOfDimensionsOnly.unregisteredMillis = j;
                } else if (i == 1) {
                    persistAtomsProto$ImsRegistrationStatsCopyOfDimensionsOnly.registeringMillis = j;
                } else if (i == 2) {
                    persistAtomsProto$ImsRegistrationStatsCopyOfDimensionsOnly.registeredMillis = j;
                    persistAtomsProto$ImsRegistrationStatsCopyOfDimensionsOnly.voiceAvailableMillis = this.mLastAvailableFeatures.isCapable(1) ? j : 0L;
                    persistAtomsProto$ImsRegistrationStatsCopyOfDimensionsOnly.videoAvailableMillis = this.mLastAvailableFeatures.isCapable(2) ? j : 0L;
                    persistAtomsProto$ImsRegistrationStatsCopyOfDimensionsOnly.utAvailableMillis = this.mLastAvailableFeatures.isCapable(4) ? j : 0L;
                    persistAtomsProto$ImsRegistrationStatsCopyOfDimensionsOnly.smsAvailableMillis = this.mLastAvailableFeatures.isCapable(8) ? j : 0L;
                    if (persistAtomsProto$ImsRegistrationStatsCopyOfDimensionsOnly.rat == 18) {
                        mmTelCapabilities = this.mLastWlanCapableFeatures;
                    } else {
                        mmTelCapabilities = this.mLastWwanCapableFeatures;
                    }
                    persistAtomsProto$ImsRegistrationStatsCopyOfDimensionsOnly.voiceCapableMillis = mmTelCapabilities.isCapable(1) ? j : 0L;
                    persistAtomsProto$ImsRegistrationStatsCopyOfDimensionsOnly.videoCapableMillis = mmTelCapabilities.isCapable(2) ? j : 0L;
                    persistAtomsProto$ImsRegistrationStatsCopyOfDimensionsOnly.utCapableMillis = mmTelCapabilities.isCapable(4) ? j : 0L;
                    if (!mmTelCapabilities.isCapable(8)) {
                        j = 0;
                    }
                    persistAtomsProto$ImsRegistrationStatsCopyOfDimensionsOnly.smsCapableMillis = j;
                }
                this.mStorage.addImsRegistrationStats(persistAtomsProto$ImsRegistrationStatsCopyOfDimensionsOnly);
            }
            this.mLastTimestamp = timeMillis;
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void onImsCapabilitiesChanged(int i, MmTelFeature.MmTelCapabilities mmTelCapabilities) {
        boolean z;
        try {
            conclude();
            int iConvertRegistrationTechToNetworkType = convertRegistrationTechToNetworkType(i);
            boolean z2 = true;
            this.mLastTransportType = iConvertRegistrationTechToNetworkType == 18 ? 2 : 1;
            PersistAtomsProto$ImsRegistrationStats persistAtomsProto$ImsRegistrationStats = this.mLastRegistrationStats;
            if (persistAtomsProto$ImsRegistrationStats.rat != iConvertRegistrationTechToNetworkType) {
                persistAtomsProto$ImsRegistrationStats.rat = iConvertRegistrationTechToNetworkType;
                z = true;
            } else {
                z = false;
            }
            persistAtomsProto$ImsRegistrationStats.isIwlanCrossSim = i == 2;
            boolean zIsCapable = mmTelCapabilities.isCapable(1);
            if (this.mLastAvailableFeatures.isCapable(1) == zIsCapable) {
                z2 = false;
            }
            this.mLastAvailableFeatures = mmTelCapabilities;
            if ((z && zIsCapable) || z2) {
                this.mPhone.getDefaultPhone().getServiceStateTracker().getServiceStateStats().onImsVoiceRegistrationChanged();
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void onSetFeatureResponse(int i, int i2, int i3) {
        try {
            MmTelFeature.MmTelCapabilities lastCapableFeaturesForTech = getLastCapableFeaturesForTech(i2);
            if (lastCapableFeaturesForTech != null) {
                conclude();
                if (i3 == 1) {
                    lastCapableFeaturesForTech.addCapabilities(i);
                } else {
                    lastCapableFeaturesForTech.removeCapabilities(i);
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void onImsRegistering(int i) {
        conclude();
        this.mLastTransportType = i;
        updateImsRegistrationStats();
        this.mLastRegistrationStats.rat = convertTransportTypeToNetworkType(i);
        this.mLastRegistrationState = 1;
    }

    public synchronized void onImsRegistered(ImsRegistrationAttributes imsRegistrationAttributes) {
        try {
            boolean z = true;
            if (this.mLastRegistrationState != 2) {
                this.mLastRegistrationStats.registeredTimes = 1;
            }
            conclude();
            this.mLastTransportType = imsRegistrationAttributes.getTransportType();
            if (this.mLastRegistrationState == 0) {
                updateImsRegistrationStats();
            }
            this.mLastRegistrationStats.rat = convertTransportTypeToNetworkType(imsRegistrationAttributes.getTransportType());
            PersistAtomsProto$ImsRegistrationStats persistAtomsProto$ImsRegistrationStats = this.mLastRegistrationStats;
            if (imsRegistrationAttributes.getRegistrationTechnology() != 2) {
                z = false;
            }
            persistAtomsProto$ImsRegistrationStats.isIwlanCrossSim = z;
            this.mLastRegistrationState = 2;
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void onImsUnregistered(ImsReasonInfo imsReasonInfo) {
        try {
            conclude();
            PersistAtomsProto$ImsRegistrationTermination persistAtomsProto$ImsRegistrationTermination = new PersistAtomsProto$ImsRegistrationTermination();
            if (this.mLastRegistrationState != 0) {
                PersistAtomsProto$ImsRegistrationStats persistAtomsProto$ImsRegistrationStats = this.mLastRegistrationStats;
                persistAtomsProto$ImsRegistrationTermination.carrierId = persistAtomsProto$ImsRegistrationStats.carrierId;
                persistAtomsProto$ImsRegistrationTermination.ratAtEnd = getRatAtEnd(persistAtomsProto$ImsRegistrationStats.rat);
                persistAtomsProto$ImsRegistrationTermination.isIwlanCrossSim = this.mLastRegistrationStats.isIwlanCrossSim;
            } else {
                persistAtomsProto$ImsRegistrationTermination.carrierId = this.mPhone.getDefaultPhone().getCarrierId();
            }
            persistAtomsProto$ImsRegistrationTermination.ratAtEnd = getRatAtEnd(this.mLastRegistrationStats.rat);
            persistAtomsProto$ImsRegistrationTermination.isMultiSim = SimSlotState.isMultiSim();
            persistAtomsProto$ImsRegistrationTermination.setupFailed = this.mLastRegistrationState != 2;
            persistAtomsProto$ImsRegistrationTermination.reasonCode = imsReasonInfo.getCode();
            persistAtomsProto$ImsRegistrationTermination.extraCode = imsReasonInfo.getExtraCode();
            persistAtomsProto$ImsRegistrationTermination.extraMessage = filterExtraMessage(imsReasonInfo.getExtraMessage());
            persistAtomsProto$ImsRegistrationTermination.count = 1;
            this.mStorage.addImsRegistrationTermination(persistAtomsProto$ImsRegistrationTermination);
            this.mLastRegistrationState = 0;
            this.mLastAvailableFeatures = new MmTelFeature.MmTelCapabilities();
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void onServiceStateChanged(ServiceState serviceState) {
        try {
            conclude();
            int dataNetworkType = serviceState.getDataNetworkType();
            MmTelFeature.MmTelCapabilities lastCapableFeaturesForNetworkType = getLastCapableFeaturesForNetworkType(dataNetworkType);
            PersistAtomsProto$ImsRegistrationStats persistAtomsProto$ImsRegistrationStats = this.mLastRegistrationStats;
            if (lastCapableFeaturesForNetworkType != null) {
                persistAtomsProto$ImsRegistrationStats.rat = dataNetworkType;
            } else {
                persistAtomsProto$ImsRegistrationStats.rat = 0;
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized int getImsVoiceRadioTech() {
        if (this.mLastRegistrationState != 0 && this.mLastAvailableFeatures.isCapable(1)) {
            return this.mLastRegistrationStats.rat;
        }
        return 0;
    }

    private int getRatAtEnd(int i) {
        return i == 18 ? i : getWwanPsRat();
    }

    private int convertTransportTypeToNetworkType(int i) {
        if (i != 1) {
            return i != 2 ? 0 : 18;
        }
        return getWwanPsRat();
    }

    private int getWwanPsRat() {
        return ServiceStateStats.getRat(this.mPhone.getServiceStateTracker().getServiceState(), 2);
    }

    private PersistAtomsProto$ImsRegistrationStats getDefaultImsRegistrationStats() {
        this.mPhone.getDefaultPhone();
        PersistAtomsProto$ImsRegistrationStats persistAtomsProto$ImsRegistrationStats = new PersistAtomsProto$ImsRegistrationStats();
        persistAtomsProto$ImsRegistrationStats.rat = 0;
        return persistAtomsProto$ImsRegistrationStats;
    }

    private void updateImsRegistrationStats() {
        Phone defaultPhone = this.mPhone.getDefaultPhone();
        this.mLastRegistrationStats.carrierId = defaultPhone.getCarrierId();
        this.mLastRegistrationStats.simSlotIndex = defaultPhone.getPhoneId();
    }

    private MmTelFeature.MmTelCapabilities getLastCapableFeaturesForTech(int i) {
        if (i == -1) {
            return null;
        }
        if (i == 1 || i == 2) {
            return this.mLastWlanCapableFeatures;
        }
        return this.mLastWwanCapableFeatures;
    }

    private MmTelFeature.MmTelCapabilities getLastCapableFeaturesForNetworkType(int i) {
        if (i == 0) {
            return null;
        }
        if (i == 18) {
            return this.mLastWlanCapableFeatures;
        }
        return this.mLastWwanCapableFeatures;
    }

    private int convertRegistrationTechToNetworkType(int i) {
        if (i == -1) {
            return 0;
        }
        if (i == 0) {
            return 13;
        }
        if (i == 1 || i == 2) {
            return 18;
        }
        if (i == 3) {
            return 20;
        }
        loge("convertRegistrationTechToNetworkType: unknown radio tech %d", Integer.valueOf(i));
        return getWwanPsRat();
    }

    private static PersistAtomsProto$ImsRegistrationStats copyOfDimensionsOnly(PersistAtomsProto$ImsRegistrationStats persistAtomsProto$ImsRegistrationStats) {
        PersistAtomsProto$ImsRegistrationStats persistAtomsProto$ImsRegistrationStats2 = new PersistAtomsProto$ImsRegistrationStats();
        persistAtomsProto$ImsRegistrationStats2.carrierId = persistAtomsProto$ImsRegistrationStats.carrierId;
        persistAtomsProto$ImsRegistrationStats2.simSlotIndex = persistAtomsProto$ImsRegistrationStats.simSlotIndex;
        persistAtomsProto$ImsRegistrationStats2.rat = persistAtomsProto$ImsRegistrationStats.rat;
        persistAtomsProto$ImsRegistrationStats2.isIwlanCrossSim = persistAtomsProto$ImsRegistrationStats.isIwlanCrossSim;
        return persistAtomsProto$ImsRegistrationStats2;
    }

    protected long getTimeMillis() {
        return SystemClock.elapsedRealtime();
    }

    public static String filterExtraMessage(String str) {
        if (str == null) {
            return "";
        }
        String strReplaceAll = PATTERN_UNKNOWN_ID.matcher(PATTERN_UNSEGMENTED_IMEI_IMSI.matcher(PATTERN_IMEI.matcher(PATTERN_IPV6.matcher(PATTERN_IPV4.matcher(PATTERN_HOSTNAME.matcher(PATTERN_URI.matcher(PATTERN_UUID.matcher(str).replaceAll("<UUID_REDACTED>")).replaceAll("$1<REDACTED>")).replaceAll("<HOSTNAME_REDACTED>")).replaceAll("<IPV4_REDACTED>")).replaceAll("<IPV6_REDACTED>")).replaceAll("$1<IMEI_REDACTED>$2")).replaceAll("$1<IMEI_IMSI_REDACTED>$2")).replaceAll("$1<ID_REDACTED>");
        return strReplaceAll.length() > 128 ? strReplaceAll.substring(0, 128) : strReplaceAll;
    }

    private void logw(String str, Object... objArr) {
        Rlog.w(TAG, "[" + this.mPhone.getPhoneId() + "] " + String.format(str, objArr));
    }

    private void loge(String str, Object... objArr) {
        Rlog.e(TAG, "[" + this.mPhone.getPhoneId() + "] " + String.format(str, objArr));
    }
}
