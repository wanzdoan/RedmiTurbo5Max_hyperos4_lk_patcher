package com.android.services.telephony.domainselection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.SystemProperties;
import android.telephony.PhoneNumberUtils;
import android.telephony.emergency.EmergencyNumber;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.telephony.LocaleTracker;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.phone.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/* JADX INFO: loaded from: classes.dex */
public class DynamicRoutingController {
    private static final boolean DBG;
    private static DynamicRoutingController sInstance;
    private List mCountriesEnabled;
    private List mDynamicNumbers;
    private final ArrayMap mEmergencyNumbers;
    private boolean mEnabled;
    private BroadcastReceiver mIntentReceiver;
    private String mLastCountryIso;
    private final ArrayMap mNetworkCountries;
    private final PhoneFactoryProxy mPhoneFactoryProxy;

    public interface PhoneFactoryProxy {
        Phone getPhone(int i);
    }

    static {
        DBG = SystemProperties.getInt("ro.debuggable", 0) == 1;
        sInstance = new DynamicRoutingController();
    }

    class PhoneFactoryProxyImpl implements PhoneFactoryProxy {
        private PhoneFactoryProxyImpl() {
        }

        @Override // com.android.services.telephony.domainselection.DynamicRoutingController.PhoneFactoryProxy
        public Phone getPhone(int i) {
            return PhoneFactory.getPhone(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getLastKnownCountryIso(int i) {
        ServiceStateTracker serviceStateTracker;
        LocaleTracker localeTracker;
        try {
            Phone phone = this.mPhoneFactoryProxy.getPhone(i);
            if (phone != null && (serviceStateTracker = phone.getServiceStateTracker()) != null && (localeTracker = serviceStateTracker.getLocaleTracker()) != null) {
                String lastKnownCountryIso = localeTracker.getLastKnownCountryIso();
                Log.e("DynamicRoutingController", "getLastKnownCountryIso iso=" + lastKnownCountryIso);
                return lastKnownCountryIso;
            }
        } catch (Exception e) {
            Log.e("DynamicRoutingController", "getLastKnownCountryIso e=" + e);
        }
        return "";
    }

    public static DynamicRoutingController getInstance() {
        return sInstance;
    }

    private DynamicRoutingController() {
        this(new PhoneFactoryProxyImpl());
    }

    public DynamicRoutingController(PhoneFactoryProxy phoneFactoryProxy) {
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.android.services.telephony.domainselection.DynamicRoutingController.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("android.telephony.action.NETWORK_COUNTRY_CHANGED")) {
                    int intExtra = intent.getIntExtra("phone", -1);
                    String stringExtra = intent.getStringExtra("android.telephony.extra.NETWORK_COUNTRY");
                    Log.i("DynamicRoutingController", "ACTION_NETWORK_COUNTRY_CHANGED phoneId: " + intExtra + " countryIso: " + stringExtra);
                    if (TextUtils.isEmpty(stringExtra)) {
                        stringExtra = DynamicRoutingController.this.getLastKnownCountryIso(intExtra);
                        if (TextUtils.isEmpty(stringExtra)) {
                            return;
                        }
                    }
                    if (!TextUtils.equals((String) DynamicRoutingController.this.mNetworkCountries.get(Integer.valueOf(intExtra)), stringExtra)) {
                        DynamicRoutingController.this.mNetworkCountries.put(Integer.valueOf(intExtra), stringExtra);
                        DynamicRoutingController.this.updateDynamicEmergencyNumbers(intExtra);
                    }
                    DynamicRoutingController.this.mLastCountryIso = stringExtra;
                }
            }
        };
        this.mNetworkCountries = new ArrayMap();
        this.mEmergencyNumbers = new ArrayMap();
        this.mCountriesEnabled = null;
        this.mDynamicNumbers = null;
        this.mPhoneFactoryProxy = phoneFactoryProxy;
    }

    public void initialize(Context context) {
        try {
            this.mEnabled = context.getResources().getBoolean(R.bool.dynamic_routing_emergency_enabled);
        } catch (Resources.NotFoundException e) {
            Log.e("DynamicRoutingController", "init exception=" + e);
        } catch (NullPointerException e2) {
            Log.e("DynamicRoutingController", "init exception=" + e2);
        }
        this.mCountriesEnabled = readResourceConfiguration(context, R.array.config_countries_dynamic_routing_emergency_enabled);
        this.mDynamicNumbers = readResourceConfiguration(context, R.array.config_dynamic_routing_emergency_numbers);
        Log.i("DynamicRoutingController", "init enabled=" + this.mEnabled + ", countriesEnabled=" + this.mCountriesEnabled);
        if (this.mEnabled) {
            context.registerReceiver(this.mIntentReceiver, new IntentFilter("android.telephony.action.NETWORK_COUNTRY_CHANGED"));
        }
    }

    private List readResourceConfiguration(Context context, int i) {
        Log.i("DynamicRoutingController", "readResourceConfiguration id=" + i);
        try {
            try {
                List listAsList = Arrays.asList(context.getResources().getStringArray(i));
                return listAsList == null ? new ArrayList() : listAsList;
            } catch (Resources.NotFoundException e) {
                Log.e("DynamicRoutingController", "readResourceConfiguration exception=" + e);
                return new ArrayList();
            } catch (NullPointerException e2) {
                Log.e("DynamicRoutingController", "readResourceConfiguration exception=" + e2);
                return new ArrayList();
            }
        } catch (Throwable th) {
            new ArrayList();
            throw th;
        }
    }

    public boolean isDynamicRoutingEnabled() {
        Log.i("DynamicRoutingController", "isDynamicRoutingEnabled " + this.mEnabled);
        return this.mEnabled;
    }

    public boolean isDynamicRoutingEnabled(Phone phone) {
        Log.i("DynamicRoutingController", "isDynamicRoutingEnabled");
        boolean z = false;
        if (phone == null) {
            return false;
        }
        String str = (String) this.mNetworkCountries.get(Integer.valueOf(phone.getPhoneId()));
        Log.i("DynamicRoutingController", "isDynamicRoutingEnabled phoneId=" + phone.getPhoneId() + ", iso=" + str + ", lastIso=" + this.mLastCountryIso);
        if (TextUtils.isEmpty(str)) {
            str = this.mLastCountryIso;
        }
        if (this.mEnabled && this.mCountriesEnabled.contains(str)) {
            z = true;
        }
        Log.i("DynamicRoutingController", "isDynamicRoutingEnabled returns " + z);
        return z;
    }

    public int getEmergencyCallRouting(Phone phone, String str, boolean z, boolean z2, boolean z3) {
        Log.i("DynamicRoutingController", "getEmergencyCallRouting isNormal=" + z + ", isAllowed=" + z2 + ", needToTurnOnRadio=" + z3);
        String strStripSeparators = PhoneNumberUtils.stripSeparators(str);
        boolean zIsDynamicNumber = isDynamicNumber(phone, strStripSeparators);
        if ((z || zIsDynamicNumber || !z2) && !isFromNetworkOrSim(phone, strStripSeparators)) {
            return (!isDynamicRoutingEnabled(phone) || phone.getServiceState().getState() == 0 || z3) ? 2 : 1;
        }
        return 0;
    }

    private boolean isFromNetworkOrSim(Phone phone, String str) {
        if (phone == null) {
            return false;
        }
        Log.i("DynamicRoutingController", "isFromNetworkOrSim phoneId=" + phone.getPhoneId());
        if (phone.getEmergencyNumberTracker() == null) {
            return false;
        }
        for (EmergencyNumber emergencyNumber : phone.getEmergencyNumberTracker().getEmergencyNumbers(str)) {
            if (emergencyNumber.getNumber().equals(str) && (emergencyNumber.isFromSources(1) || emergencyNumber.isFromSources(2))) {
                Log.i("DynamicRoutingController", "isFromNetworkOrSim SIM or NETWORK emergency number");
                return true;
            }
        }
        return false;
    }

    private String getNetworkCountryIso(int i) {
        String str = (String) this.mNetworkCountries.get(Integer.valueOf(i));
        return TextUtils.isEmpty(str) ? this.mLastCountryIso : str;
    }

    public boolean isDynamicNumber(Phone phone, String str) {
        List list;
        if (phone != null && phone.getEmergencyNumberTracker() != null && !TextUtils.isEmpty(str) && (list = this.mDynamicNumbers) != null && !list.isEmpty()) {
            List list2 = (List) this.mEmergencyNumbers.get(Integer.valueOf(phone.getPhoneId()));
            if (list2 == null) {
                updateDynamicEmergencyNumbers(phone.getPhoneId());
                list2 = (List) this.mEmergencyNumbers.get(Integer.valueOf(phone.getPhoneId()));
            }
            if (!TextUtils.isEmpty(getNetworkCountryIso(phone.getPhoneId())) && list2 != null && !list2.isEmpty()) {
                List<EmergencyNumber> dynamicEmergencyNumbers = getDynamicEmergencyNumbers(list2, str);
                List emergencyNumbers = phone.getEmergencyNumberTracker().getEmergencyNumbers(str);
                if (dynamicEmergencyNumbers != null && emergencyNumbers != null && !dynamicEmergencyNumbers.isEmpty() && !emergencyNumbers.isEmpty()) {
                    if (DBG) {
                        Log.i("DynamicRoutingController", "isDynamicNumber " + emergencyNumbers);
                    }
                    for (final EmergencyNumber emergencyNumber : dynamicEmergencyNumbers) {
                        if (emergencyNumbers.stream().anyMatch(new Predicate() { // from class: com.android.services.telephony.domainselection.DynamicRoutingController$$ExternalSyntheticLambda0
                            @Override // java.util.function.Predicate
                            public final boolean test(Object obj) {
                                return DynamicRoutingController.$r8$lambda$ulgTkLXxC1Q1V7JjL598BkMmndQ(emergencyNumber, (EmergencyNumber) obj);
                            }
                        })) {
                            Log.i("DynamicRoutingController", "isDynamicNumber found");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static /* synthetic */ boolean $r8$lambda$ulgTkLXxC1Q1V7JjL598BkMmndQ(EmergencyNumber emergencyNumber, EmergencyNumber emergencyNumber2) {
        if (TextUtils.equals(emergencyNumber2.getCountryIso(), emergencyNumber.getCountryIso())) {
            return TextUtils.equals(emergencyNumber2.getMnc(), emergencyNumber.getMnc()) || TextUtils.isEmpty(emergencyNumber.getMnc());
        }
        return false;
    }

    private static List getDynamicEmergencyNumbers(List list, final String str) {
        List list2 = list.stream().filter(new Predicate() { // from class: com.android.services.telephony.domainselection.DynamicRoutingController$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((EmergencyNumber) obj).getNumber().equals(str);
            }
        }).toList();
        if (DBG) {
            Log.i("DynamicRoutingController", "getDynamicEmergencyNumbers " + list2);
        }
        return list2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDynamicEmergencyNumbers(int i) {
        List list = this.mDynamicNumbers;
        if (list == null || list.isEmpty()) {
            this.mEmergencyNumbers.put(Integer.valueOf(i), new ArrayList());
            return;
        }
        String networkCountryIso = getNetworkCountryIso(i);
        if (TextUtils.isEmpty(networkCountryIso)) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (String str : this.mDynamicNumbers) {
            if (!TextUtils.isEmpty(str) && str.startsWith(networkCountryIso)) {
                arrayList.addAll(getEmergencyNumbers(str));
            }
        }
        this.mEmergencyNumbers.put(Integer.valueOf(i), arrayList);
    }

    private List getEmergencyNumbers(String str) {
        String[] strArrSplit;
        ArrayList arrayList = new ArrayList();
        if (!TextUtils.isEmpty(str) && (strArrSplit = str.split(",")) != null && strArrSplit.length >= 3 && !TextUtils.isEmpty(strArrSplit[0])) {
            if (!TextUtils.isEmpty(strArrSplit[2])) {
                for (int i = 2; i < strArrSplit.length; i++) {
                    if (!TextUtils.isEmpty(strArrSplit[i])) {
                        arrayList.add(new EmergencyNumber(strArrSplit[i], strArrSplit[0], strArrSplit[1], 0, new ArrayList(), 16, 0));
                    }
                }
            }
        }
        return arrayList;
    }
}
