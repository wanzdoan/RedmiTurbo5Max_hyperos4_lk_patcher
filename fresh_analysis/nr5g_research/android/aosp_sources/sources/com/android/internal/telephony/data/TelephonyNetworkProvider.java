package com.android.internal.telephony.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.MatchAllNetworkSpecifier;
import android.net.NetworkCapabilities;
import android.net.NetworkProvider;
import android.net.NetworkRequest;
import android.net.NetworkScore;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SubscriptionManager;
import android.util.ArrayMap;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.CallFailCause;
import com.android.internal.telephony.IndentingPrintWriter;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.NetworkTypeController$$ExternalSyntheticLambda2;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.configupdate.ConfigParser;
import com.android.internal.telephony.configupdate.ConfigProviderAdaptor;
import com.android.internal.telephony.configupdate.TelephonyConfigUpdateInstallReceiver;
import com.android.internal.telephony.metrics.NetworkRequestsStats;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyNetworkProvider extends NetworkProvider implements NetworkProvider.NetworkOfferCallback {
    public String LOG_TAG;
    private final Context mContext;
    private final Set mDynamicNetworkCapabilities;
    private final FeatureFlags mFlags;
    private final Handler mHandler;
    private final LocalLog mLocalLog;
    private final Map mNetworkRequests;
    protected final PhoneSwitcher mPhoneSwitcher;

    public TelephonyNetworkProvider(Looper looper, Context context, FeatureFlags featureFlags) {
        DataConfig dataConfig;
        super(context, looper, TelephonyNetworkProvider.class.getSimpleName());
        this.LOG_TAG = "TNP";
        this.mNetworkRequests = new ArrayMap();
        HashSet hashSet = new HashSet();
        this.mDynamicNetworkCapabilities = hashSet;
        this.mLocalLog = new LocalLog(CallFailCause.RADIO_UPLINK_FAILURE);
        this.mFlags = featureFlags;
        this.mContext = context;
        Handler handler = new Handler(looper);
        this.mHandler = handler;
        PhoneSwitcher phoneSwitcher = PhoneSwitcher.getInstance();
        this.mPhoneSwitcher = phoneSwitcher;
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        Objects.requireNonNull(handler);
        subscriptionManager.addOnSubscriptionsChangedListener(new NetworkTypeController$$ExternalSyntheticLambda2(handler), new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.internal.telephony.data.TelephonyNetworkProvider.1
            @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                TelephonyNetworkProvider.this.logl("Subscription changed.");
                TelephonyNetworkProvider.this.reevaluateNetworkRequests("subscription changed");
            }
        });
        Objects.requireNonNull(handler);
        phoneSwitcher.registerCallback(new PhoneSwitcher.PhoneSwitcherCallback(new NetworkTypeController$$ExternalSyntheticLambda2(handler)) { // from class: com.android.internal.telephony.data.TelephonyNetworkProvider.2
            @Override // com.android.internal.telephony.data.PhoneSwitcher.PhoneSwitcherCallback
            public void onPreferredDataPhoneIdChanged(int i) {
                TelephonyNetworkProvider.this.logl("Preferred data sub phone id changed to " + i);
                TelephonyNetworkProvider.this.reevaluateNetworkRequests("Preferred data subscription changed");
            }
        });
        TelephonyConfigUpdateInstallReceiver telephonyConfigUpdateInstallReceiver = TelephonyConfigUpdateInstallReceiver.getInstance();
        ConfigParser configParser = telephonyConfigUpdateInstallReceiver.getConfigParser(ConfigProviderAdaptor.DOMAIN_DATA);
        if (configParser != null && (dataConfig = (DataConfig) configParser.getConfig()) != null) {
            hashSet.addAll(dataConfig.getAllNetworkCapabilities());
        }
        Objects.requireNonNull(handler);
        telephonyConfigUpdateInstallReceiver.registerCallback(new NetworkTypeController$$ExternalSyntheticLambda2(handler), new ConfigProviderAdaptor.Callback() { // from class: com.android.internal.telephony.data.TelephonyNetworkProvider.3
            @Override // com.android.internal.telephony.configupdate.ConfigProviderAdaptor.Callback
            public void onChanged(ConfigParser configParser2) {
                if (configParser2 instanceof DataConfigParser) {
                    TelephonyNetworkProvider.this.updateNetworkOffer((DataConfig) configParser2.getConfig());
                }
            }
        });
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(ConnectivityManager.class);
        if (connectivityManager != null) {
            connectivityManager.registerNetworkProvider(this);
            NetworkCapabilities networkCapabilitiesMakeNetworkFilter = makeNetworkFilter();
            NetworkScore networkScoreBuild = new NetworkScore.Builder().build();
            Objects.requireNonNull(handler);
            registerNetworkOffer(networkScoreBuild, networkCapabilitiesMakeNetworkFilter, new NetworkTypeController$$ExternalSyntheticLambda2(handler), this);
            logl("registerNetworkOffer: " + networkCapabilitiesMakeNetworkFilter);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNetworkOffer(DataConfig dataConfig) {
        final HashSet hashSet = new HashSet();
        if (dataConfig != null) {
            hashSet.addAll(dataConfig.getAllNetworkCapabilities());
        }
        TelephonyNetworkRequest.getAllSupportedNetworkCapabilities().forEach(new Consumer() { // from class: com.android.internal.telephony.data.TelephonyNetworkProvider$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                hashSet.remove((Integer) obj);
            }
        });
        synchronized (this.mDynamicNetworkCapabilities) {
            try {
                if (this.mDynamicNetworkCapabilities.equals(hashSet)) {
                    return;
                }
                this.mDynamicNetworkCapabilities.clear();
                this.mDynamicNetworkCapabilities.addAll(hashSet);
                logl("updateNetworkOffer: mDynamicNetworkCapabilities=" + ((String) this.mDynamicNetworkCapabilities.stream().map(new DataConfigManager$$ExternalSyntheticLambda3()).collect(Collectors.joining(","))));
                if (((ConnectivityManager) this.mContext.getSystemService(ConnectivityManager.class)) != null) {
                    unregisterNetworkOffer(this);
                    NetworkCapabilities networkCapabilitiesMakeNetworkFilter = makeNetworkFilter();
                    NetworkScore networkScoreBuild = new NetworkScore.Builder().build();
                    Handler handler = this.mHandler;
                    Objects.requireNonNull(handler);
                    registerNetworkOffer(networkScoreBuild, networkCapabilitiesMakeNetworkFilter, new NetworkTypeController$$ExternalSyntheticLambda2(handler), this);
                    logl("Updated registerNetworkOffer: " + networkCapabilitiesMakeNetworkFilter);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private int getPhoneIdForNetworkRequest(TelephonyNetworkRequest telephonyNetworkRequest) {
        for (Phone phone : PhoneFactory.getPhones()) {
            int phoneId = phone.getPhoneId();
            if (this.mPhoneSwitcher.shouldApplyNetworkRequest(telephonyNetworkRequest, phoneId)) {
                return phoneId;
            }
        }
        return -1;
    }

    public void onNetworkNeeded(NetworkRequest networkRequest) {
        TelephonyNetworkRequest telephonyNetworkRequest = new TelephonyNetworkRequest(networkRequest, this.mFlags);
        if (this.mNetworkRequests.containsKey(telephonyNetworkRequest)) {
            loge("Duplicate network request " + telephonyNetworkRequest);
            return;
        }
        if (MiuiPhoneSwitcherStub.get().onNetworkNeeded(telephonyNetworkRequest)) {
            logl("onNetworkNeeded: com.android.phone satellite return");
            return;
        }
        this.mPhoneSwitcher.onRequestNetwork(telephonyNetworkRequest);
        int phoneIdForNetworkRequest = getPhoneIdForNetworkRequest(telephonyNetworkRequest);
        if (phoneIdForNetworkRequest != -1) {
            NetworkRequestsStats.addNetworkRequest(networkRequest, SubscriptionManager.getSubscriptionId(phoneIdForNetworkRequest));
            logl("onNetworkNeeded: phoneId=" + phoneIdForNetworkRequest + ", " + telephonyNetworkRequest);
            PhoneFactory.getPhone(phoneIdForNetworkRequest).getDataNetworkController().addNetworkRequest(telephonyNetworkRequest);
        } else {
            logl("onNetworkNeeded: Not applied. " + telephonyNetworkRequest);
        }
        this.mNetworkRequests.put(telephonyNetworkRequest, Integer.valueOf(phoneIdForNetworkRequest));
    }

    public void onNetworkUnneeded(final NetworkRequest networkRequest) {
        TelephonyNetworkRequest telephonyNetworkRequest = (TelephonyNetworkRequest) this.mNetworkRequests.keySet().stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.TelephonyNetworkProvider$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((TelephonyNetworkRequest) obj).getNativeNetworkRequest().equals(networkRequest);
            }
        }).findFirst().orElse(null);
        if (telephonyNetworkRequest == null) {
            loge("onNetworkUnneeded: Cannot find " + networkRequest);
            return;
        }
        this.mPhoneSwitcher.onReleaseNetwork(telephonyNetworkRequest);
        int iIntValue = ((Integer) this.mNetworkRequests.remove(telephonyNetworkRequest)).intValue();
        Phone phone = PhoneFactory.getPhone(iIntValue);
        if (phone != null) {
            logl("onNetworkUnneeded: phoneId=" + iIntValue + ", " + telephonyNetworkRequest);
            phone.getDataNetworkController().removeNetworkRequest(telephonyNetworkRequest);
            return;
        }
        loge("onNetworkUnneeded: Unable to get phone. phoneId=" + iIntValue);
    }

    protected void reevaluateNetworkRequests(String str) {
        logl("reevaluateNetworkRequests: " + str + ".");
        this.mNetworkRequests.forEach(new BiConsumer() { // from class: com.android.internal.telephony.data.TelephonyNetworkProvider$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                this.f$0.lambda$reevaluateNetworkRequests$1((TelephonyNetworkRequest) obj, (Integer) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reevaluateNetworkRequests$1(TelephonyNetworkRequest telephonyNetworkRequest, Integer num) {
        int phoneIdForNetworkRequest = getPhoneIdForNetworkRequest(telephonyNetworkRequest);
        if (phoneIdForNetworkRequest != num.intValue()) {
            if (num.intValue() != -1) {
                PhoneFactory.getPhone(num.intValue()).getDataNetworkController().removeNetworkRequest(telephonyNetworkRequest);
            }
            if (phoneIdForNetworkRequest != -1) {
                PhoneFactory.getPhone(phoneIdForNetworkRequest).getDataNetworkController().addNetworkRequest(telephonyNetworkRequest);
            }
            logl("Request moved. phoneId " + num + " -> " + phoneIdForNetworkRequest + " " + telephonyNetworkRequest);
            this.mNetworkRequests.put(telephonyNetworkRequest, Integer.valueOf(phoneIdForNetworkRequest));
        }
    }

    public NetworkCapabilities makeNetworkFilter() {
        NetworkCapabilities.Builder networkSpecifier = new NetworkCapabilities.Builder().addTransportType(0).addTransportType(10).addCapability(7).addCapability(33).addCapability(13).addCapability(28).addCapability(55).addCapability(56).addCapability(57).addCapability(58).addEnterpriseId(1).addEnterpriseId(2).addEnterpriseId(3).addEnterpriseId(4).addEnterpriseId(5).setNetworkSpecifier(new MatchAllNetworkSpecifier());
        List<Integer> allSupportedNetworkCapabilities = TelephonyNetworkRequest.getAllSupportedNetworkCapabilities();
        Objects.requireNonNull(networkSpecifier);
        allSupportedNetworkCapabilities.forEach(new DataNetwork$$ExternalSyntheticLambda13(networkSpecifier));
        synchronized (this.mDynamicNetworkCapabilities) {
            this.mDynamicNetworkCapabilities.forEach(new DataNetwork$$ExternalSyntheticLambda13(networkSpecifier));
        }
        return networkSpecifier.build();
    }

    protected void log(String str) {
        Rlog.d(this.LOG_TAG, str);
    }

    protected void loge(String str) {
        Rlog.e(this.LOG_TAG, str);
    }

    protected void logl(String str) {
        log(str);
        this.mLocalLog.log(str);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        final IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("TelephonyNetworkProvider:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("mPreferredDataPhoneId=" + this.mPhoneSwitcher.getPreferredDataPhoneId());
        int defaultDataSubId = SubscriptionManagerService.getInstance().getDefaultDataSubId();
        indentingPrintWriter.println("DefaultDataSubId=" + defaultDataSubId);
        indentingPrintWriter.println("DefaultDataPhoneId=" + SubscriptionManagerService.getInstance().getPhoneId(defaultDataSubId));
        indentingPrintWriter.println("Registered capabilities: " + makeNetworkFilter());
        indentingPrintWriter.println("Network requests:");
        indentingPrintWriter.increaseIndent();
        for (final Phone phone : PhoneFactory.getPhones()) {
            indentingPrintWriter.println("Phone " + phone.getPhoneId() + ":");
            indentingPrintWriter.increaseIndent();
            this.mNetworkRequests.forEach(new BiConsumer() { // from class: com.android.internal.telephony.data.TelephonyNetworkProvider$$ExternalSyntheticLambda1
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    TelephonyNetworkProvider.m904$r8$lambda$vgCcHwlxJT6upFtT2xxYyXUtDc(phone, indentingPrintWriter, (TelephonyNetworkRequest) obj, (Integer) obj2);
                }
            });
            indentingPrintWriter.decreaseIndent();
        }
        indentingPrintWriter.println("Not applied requests:");
        indentingPrintWriter.increaseIndent();
        this.mNetworkRequests.forEach(new BiConsumer() { // from class: com.android.internal.telephony.data.TelephonyNetworkProvider$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                TelephonyNetworkProvider.$r8$lambda$xEb1gImou_v0MKTgjwZ4CbxNsGQ(indentingPrintWriter, (TelephonyNetworkRequest) obj, (Integer) obj2);
            }
        });
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println();
        indentingPrintWriter.println("Local logs:");
        indentingPrintWriter.increaseIndent();
        this.mLocalLog.dump(fileDescriptor, indentingPrintWriter, strArr);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.decreaseIndent();
    }

    /* JADX INFO: renamed from: $r8$lambda$vgCcHwlxJT6up-FtT2xxYyXUtDc, reason: not valid java name */
    public static /* synthetic */ void m904$r8$lambda$vgCcHwlxJT6upFtT2xxYyXUtDc(Phone phone, IndentingPrintWriter indentingPrintWriter, TelephonyNetworkRequest telephonyNetworkRequest, Integer num) {
        if (num.intValue() == phone.getPhoneId()) {
            indentingPrintWriter.println(telephonyNetworkRequest);
        }
    }

    public static /* synthetic */ void $r8$lambda$xEb1gImou_v0MKTgjwZ4CbxNsGQ(IndentingPrintWriter indentingPrintWriter, TelephonyNetworkRequest telephonyNetworkRequest, Integer num) {
        if (num.intValue() == -1) {
            indentingPrintWriter.println(telephonyNetworkRequest);
        }
    }
}
