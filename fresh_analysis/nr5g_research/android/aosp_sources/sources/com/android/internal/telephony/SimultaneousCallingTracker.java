package com.android.internal.telephony;

import android.R;
import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Message;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyRegistryManager;
import android.util.Log;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class SimultaneousCallingTracker {
    protected static final int EVENT_DEVICE_CONFIG_CHANGED = 104;
    protected static final int EVENT_IMS_REGISTRATION_CHANGED = 105;
    protected static final int EVENT_MULTI_SIM_CONFIG_CHANGED = 103;
    protected static final int EVENT_PHONE_CAPABILITY_CHANGED = 102;
    protected static final int EVENT_SUBSCRIPTION_CHANGED = 101;
    private static SimultaneousCallingTracker sInstance;
    private final Context mContext;
    private final Handler mHandler;
    private final PhoneConfigurationManager mPhoneConfigurationManager;
    private final PhoneConfigurationManager.Listener mPhoneConfigurationManagerListener;
    private final SubscriptionManager.OnSubscriptionsChangedListener mSubscriptionsChangedListener;
    private Map mVoiceCapablePhoneMap = new HashMap();
    public boolean isDeviceSimultaneousCallingCapable = false;
    public Set<Listener> mListeners = new CopyOnWriteArraySet();
    private Map mSimultaneousCallPhoneSupportMap = new HashMap();

    public interface Listener {
        void onSimultaneousCallingSupportChanged(Map<Integer, Set<Integer>> map);
    }

    public static abstract class ListenerBase implements Listener {
        @Override // com.android.internal.telephony.SimultaneousCallingTracker.Listener
        public void onSimultaneousCallingSupportChanged(Map<Integer, Set<Integer>> map) {
        }
    }

    public static SimultaneousCallingTracker init(Context context) {
        if (sInstance == null) {
            sInstance = new SimultaneousCallingTracker(context);
        } else {
            Log.wtf("SimultaneousCallingTracker", "init() called multiple times!  sInstance = " + sInstance);
        }
        return sInstance;
    }

    private SimultaneousCallingTracker(Context context) {
        SubscriptionManager.OnSubscriptionsChangedListener onSubscriptionsChangedListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.internal.telephony.SimultaneousCallingTracker.1
            @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                if (SimultaneousCallingTracker.this.mHandler.hasMessages(101)) {
                    return;
                }
                SimultaneousCallingTracker.this.mHandler.sendEmptyMessage(101);
            }
        };
        this.mSubscriptionsChangedListener = onSubscriptionsChangedListener;
        PhoneConfigurationManager.Listener listener = new PhoneConfigurationManager.Listener() { // from class: com.android.internal.telephony.SimultaneousCallingTracker.2
            @Override // com.android.internal.telephony.PhoneConfigurationManager.Listener
            public void onPhoneCapabilityChanged() {
                if (SimultaneousCallingTracker.this.mHandler.hasMessages(102)) {
                    return;
                }
                SimultaneousCallingTracker.this.mHandler.sendEmptyMessage(102);
            }

            @Override // com.android.internal.telephony.PhoneConfigurationManager.Listener
            public void onDeviceConfigChanged() {
                if (SimultaneousCallingTracker.this.mHandler.hasMessages(104)) {
                    return;
                }
                SimultaneousCallingTracker.this.mHandler.sendEmptyMessage(104);
            }
        };
        this.mPhoneConfigurationManagerListener = listener;
        this.mContext = context;
        ConfigManagerHandler configManagerHandler = new ConfigManagerHandler();
        this.mHandler = configManagerHandler;
        PhoneConfigurationManager phoneConfigurationManager = PhoneConfigurationManager.getInstance();
        this.mPhoneConfigurationManager = phoneConfigurationManager;
        phoneConfigurationManager.addListener(listener);
        PhoneConfigurationManager.registerForMultiSimConfigChange(configManagerHandler, 103, null);
        ((TelephonyRegistryManager) context.getSystemService("telephony_registry")).addOnSubscriptionsChangedListener(onSubscriptionsChangedListener, new HandlerExecutor(configManagerHandler));
    }

    public static SimultaneousCallingTracker getInstance() {
        if (sInstance == null) {
            Log.wtf("SimultaneousCallingTracker", "getInstance null");
        }
        return sInstance;
    }

    private final class ConfigManagerHandler extends Handler {
        private ConfigManagerHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Log.v("SimultaneousCallingTracker", "Received EVENT " + message.what);
            switch (message.what) {
                case 101:
                    SimultaneousCallingTracker.this.updatePhoneMapAndSimultaneousCallSupportMap();
                    break;
                case 102:
                    SimultaneousCallingTracker.this.checkSimultaneousCallingDeviceCapability();
                    break;
                case 103:
                    int iIntValue = ((Integer) ((AsyncResult) message.obj).result).intValue();
                    SimultaneousCallingTracker simultaneousCallingTracker = SimultaneousCallingTracker.this;
                    if (iIntValue > 1) {
                        simultaneousCallingTracker.updatePhoneMapAndSimultaneousCallSupportMap();
                    } else {
                        simultaneousCallingTracker.disableSimultaneousCallingSupport();
                        SimultaneousCallingTracker.this.handleSimultaneousCallingSupportChanged();
                    }
                    break;
                case 104:
                case 105:
                    SimultaneousCallingTracker.this.updateSimultaneousCallSupportMap();
                    break;
                default:
                    Log.i("SimultaneousCallingTracker", "Received unknown event: " + message.what);
                    break;
            }
        }
    }

    public void addListener(Listener listener) {
        this.mListeners.add(listener);
    }

    public final void removeListener(Listener listener) {
        this.mListeners.remove(listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkSimultaneousCallingDeviceCapability() {
        if (this.mPhoneConfigurationManager.getNumberOfModemsWithSimultaneousVoiceConnections() > 1) {
            this.isDeviceSimultaneousCallingCapable = true;
            this.mPhoneConfigurationManager.registerForSimultaneousCellularCallingSlotsChanged(new Consumer() { // from class: com.android.internal.telephony.SimultaneousCallingTracker$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.onSimultaneousCellularCallingSlotsChanged((Set) obj);
                }
            });
        }
    }

    public Set<Integer> getSubIdsSupportingSimultaneousCalling(int i) {
        if (!this.isDeviceSimultaneousCallingCapable) {
            Log.v("SimultaneousCallingTracker", "Device is not simultaneous calling capable");
            return Collections.EMPTY_SET;
        }
        for (Integer num : this.mSimultaneousCallPhoneSupportMap.keySet()) {
            if (PhoneFactory.getPhone(num.intValue()).getSubId() == i) {
                HashSet hashSet = new HashSet();
                Iterator it = ((Set) this.mSimultaneousCallPhoneSupportMap.get(num)).iterator();
                while (it.hasNext()) {
                    hashSet.add(Integer.valueOf(((Phone) it.next()).getSubId()));
                }
                Log.d("SimultaneousCallingTracker", "getSlotsSupportingSimultaneousCalling for subId=" + i + "; subIdsSupportingSimultaneousCalling=[" + getStringFromSet(hashSet) + "].");
                return hashSet;
            }
        }
        Log.e("SimultaneousCallingTracker", "getSlotsSupportingSimultaneousCalling: Subscription ID not found in the map of voice capable phones.");
        return Collections.EMPTY_SET;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePhoneMapAndSimultaneousCallSupportMap() {
        if (!this.isDeviceSimultaneousCallingCapable) {
            Log.d("SimultaneousCallingTracker", "Ignoring updatePhoneMapAndSimultaneousCallSupportMap since device is not DSDA capable.");
            return;
        }
        unregisterForImsRegistrationChanges(this.mVoiceCapablePhoneMap);
        this.mVoiceCapablePhoneMap = generateVoiceCapablePhoneMapBasedOnUserAssociation();
        Log.i("SimultaneousCallingTracker", "updatePhoneMapAndSimultaneousCallSupportMap: mVoiceCapablePhoneMap.size = " + this.mVoiceCapablePhoneMap.size());
        registerForImsRegistrationChanges(this.mVoiceCapablePhoneMap);
        updateSimultaneousCallSupportMap();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSimultaneousCallSupportMap() {
        if (!this.isDeviceSimultaneousCallingCapable) {
            Log.d("SimultaneousCallingTracker", "Ignoring updateSimultaneousCallSupportMap since device is not DSDAcapable.");
        } else {
            this.mSimultaneousCallPhoneSupportMap = generateSimultaneousCallSupportMap(this.mVoiceCapablePhoneMap);
            handleSimultaneousCallingSupportChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSimultaneousCellularCallingSlotsChanged(Set set) {
        updateSimultaneousCallSupportMap();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disableSimultaneousCallingSupport() {
        if (!this.isDeviceSimultaneousCallingCapable) {
            Log.d("SimultaneousCallingTracker", "Ignoring updateSimultaneousCallSupportMap since device is not DSDAcapable.");
            return;
        }
        unregisterForImsRegistrationChanges(this.mVoiceCapablePhoneMap);
        this.mSimultaneousCallPhoneSupportMap.clear();
        this.mVoiceCapablePhoneMap.clear();
    }

    private void registerForImsRegistrationChanges(Map map) {
        for (Phone phone : map.keySet()) {
            ImsPhone imsPhone = (ImsPhone) phone.getImsPhone();
            if (imsPhone != null) {
                Log.v("SimultaneousCallingTracker", "registerForImsRegistrationChanges: registering phoneId = " + phone.getPhoneId());
                imsPhone.registerForImsRegistrationChanges(this.mHandler, 105, null);
            } else {
                Log.v("SimultaneousCallingTracker", "registerForImsRegistrationChanges: phone not recognized as ImsPhone: phoneId = " + phone.getPhoneId());
            }
        }
    }

    private void unregisterForImsRegistrationChanges(Map map) {
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            ImsPhone imsPhone = (ImsPhone) ((Phone) it.next()).getImsPhone();
            if (imsPhone != null) {
                imsPhone.unregisterForImsRegistrationChanges(this.mHandler);
            }
        }
    }

    private Map generateVoiceCapablePhoneMapBasedOnUserAssociation() {
        HashMap map = new HashMap(3);
        for (Phone phone : PhoneFactory.getPhones()) {
            int subId = phone.getSubId();
            SubscriptionInfo subscriptionInfo = SubscriptionManagerService.getInstance().getSubscriptionInfo(subId);
            if (subId > -1 && subscriptionInfo != null && subscriptionInfo.getServiceCapabilities().contains(1)) {
                Log.v("SimultaneousCallingTracker", "generateVoiceCapablePhoneMapBasedOnUserAssociation: adding phoneId = " + phone.getPhoneId());
                map.put(phone, new HashSet(3));
            }
        }
        HashMap map2 = new HashMap(3);
        for (Phone phone2 : map.keySet()) {
            HashSet hashSet = new HashSet(3);
            for (Phone phone3 : map.keySet()) {
                if (phone2.getPhoneId() != phone3.getPhoneId() && phonesHaveSameUserAssociation(phone2, phone3)) {
                    hashSet.add(phone3);
                }
            }
            map2.put(phone2, hashSet);
        }
        return map2;
    }

    private Map generateSimultaneousCallSupportMap(Map map) {
        HashMap map2 = new HashMap(3);
        for (Phone phone : map.keySet()) {
            map2.put(Integer.valueOf(phone.getPhoneId()), new HashSet((Collection) map.get(phone)));
        }
        for (Phone phone2 : map.keySet()) {
            if (phone2.isImsRegistered()) {
                if (this.mPhoneConfigurationManager.isVirtualDsdaEnabled() || phone2.isImsServiceSimultaneousCallingSupportCapable(this.mContext)) {
                    int imsTransportType = getImsTransportType(phone2);
                    if (!this.mContext.getResources().getBoolean(R.bool.config_hideDisplayCutoutWithDisplayArea) || imsTransportType != 2) {
                        for (Phone phone3 : map.keySet()) {
                            if (phone2.getPhoneId() != phone3.getPhoneId() && !phonesSupportSimultaneousCallingViaCellularOrWlan(phone2, phone3)) {
                                ((Set) map2.get(Integer.valueOf(phone2.getPhoneId()))).remove(phone3);
                            }
                        }
                    }
                } else {
                    ((Set) map2.get(Integer.valueOf(phone2.getPhoneId()))).clear();
                }
            } else {
                for (Phone phone4 : map.keySet()) {
                    if (phone2.getPhoneId() != phone4.getPhoneId() && !phonesSupportSimultaneousCallingViaCellularOrWlan(phone2, phone4)) {
                        ((Set) map2.get(Integer.valueOf(phone2.getPhoneId()))).remove(phone4);
                    }
                }
            }
        }
        Log.v("SimultaneousCallingTracker", "generateSimultaneousCallSupportMap: returning simultaneousCallSubSupportMap = " + getStringFromMap(map2));
        return map2;
    }

    private boolean phonesHaveSameUserAssociation(Phone phone, Phone phone2) {
        return Objects.equals(phone.getUserHandle(), phone2.getUserHandle());
    }

    private boolean phonesSupportCellularSimultaneousCalling(Phone phone, Phone phone2) {
        Set<Integer> slotsSupportingSimultaneousCellularCalls = this.mPhoneConfigurationManager.getSlotsSupportingSimultaneousCellularCalls();
        Log.v("SimultaneousCallingTracker", "phonesSupportCellularSimultaneousCalling: modem returned slots = " + getStringFromSet(slotsSupportingSimultaneousCellularCalls));
        return slotsSupportingSimultaneousCellularCalls.contains(Integer.valueOf(phone.getPhoneId())) && slotsSupportingSimultaneousCellularCalls.contains(Integer.valueOf(phone2.getPhoneId()));
    }

    private boolean phonesSupportSimultaneousCallingViaCellularOrWlan(Phone phone, Phone phone2) {
        return (this.mContext.getResources().getBoolean(R.bool.config_hideDisplayCutoutWithDisplayArea) && getImsTransportType(phone2) == 2) || phonesSupportCellularSimultaneousCalling(phone, phone2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSimultaneousCallingSupportChanged() {
        try {
            Log.v("SimultaneousCallingTracker", "handleSimultaneousCallingSupportChanged");
            HashMap map = new HashMap();
            for (Integer num : this.mSimultaneousCallPhoneSupportMap.keySet()) {
                Phone phone = PhoneFactory.getPhone(num.intValue());
                if (phone == null) {
                    Log.wtf("SimultaneousCallingTracker", "handleSimultaneousCallingSupportChanged: phoneId=" + num + " not found.");
                    return;
                }
                int subId = phone.getSubId();
                HashSet hashSet = new HashSet(3);
                Iterator it = ((Set) this.mSimultaneousCallPhoneSupportMap.get(num)).iterator();
                while (it.hasNext()) {
                    hashSet.add(Integer.valueOf(((Phone) it.next()).getSubId()));
                }
                map.put(Integer.valueOf(subId), hashSet);
            }
            Iterator<Listener> it2 = this.mListeners.iterator();
            while (it2.hasNext()) {
                it2.next().onSimultaneousCallingSupportChanged(map);
            }
        } catch (Exception e) {
            Log.w("SimultaneousCallingTracker", "handleSimultaneousCallingSupportChanged: Exception = " + e);
        }
    }

    private int getImsTransportType(Phone phone) {
        ImsPhone imsPhone = (ImsPhone) phone.getImsPhone();
        if (imsPhone != null) {
            return imsPhone.getTransportType();
        }
        Log.d("SimultaneousCallingTracker", "getImsTransportType: IMS not supported for phone = " + phone);
        return -1;
    }

    private String getStringFromMap(Map map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry entry : map.entrySet()) {
            sb.append("Phone ID=");
            sb.append(entry.getKey());
            sb.append(" - Simultaneous calling compatible phone IDs=[");
            sb.append((String) ((Set) entry.getValue()).stream().map(new Function() { // from class: com.android.internal.telephony.SimultaneousCallingTracker$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return Integer.valueOf(((Phone) obj).getPhoneId());
                }
            }).map(new SimultaneousCallingTracker$$ExternalSyntheticLambda0()).collect(Collectors.joining(", ")));
            sb.append("]; ");
        }
        return sb.toString();
    }

    private String getStringFromSet(Set set) {
        return (String) set.stream().map(new SimultaneousCallingTracker$$ExternalSyntheticLambda0()).collect(Collectors.joining(","));
    }
}
