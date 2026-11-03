package com.android.phone.settings.hiddenmenu;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.os.PersistableBundle;
import android.os.Process;
import android.telephony.DataSpecificRegistrationInfo;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;
import android.telephony.ims.ImsException;
import android.telephony.ims.ImsManager;
import android.telephony.ims.ImsMmTelManager;
import android.telephony.ims.ProvisioningManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.R;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

/* JADX INFO: loaded from: classes.dex */
public class PhoneInformationV2FragmentDataNetwork extends Fragment {
    private TextView mCellInfo;
    private int mCellInfoRefreshRateIndex;
    private Spinner mCellInfoRefreshRateSpinner;
    private Context mContext;
    private TextView mDBm;
    private TextView mDataNetwork;
    private TextView mDataRawReg;
    private TelephonyDisplayInfo mDisplayInfo;
    private TextView mDownlinkKbps;
    private Switch mEabProvisionedSwitch;
    private CheckBox mFilterAll;
    private CheckBox mFilterGsm;
    private CheckBox mFilterLte;
    private CheckBox mFilterNr;
    private CheckBox mFilterWcdma;
    private TextView mGprsState;
    private TextView mGsmState;
    private Handler mHandler;
    private Switch mImsVolteProvisionedSwitch;
    private Switch mImsVtProvisionedSwitch;
    private Switch mImsWfcProvisionedSwitch;
    private PhoneInformationV2PhoneId mListener;
    private Spinner mMockDataNetworkType;
    private Spinner mMockSignalStrength;
    private TextView mNrAvailable;
    private TextView mNrFrequency;
    private TextView mNrState;
    private TextView mOperatorName;
    private TextView mOverrideNetwork;
    private LinearLayout mPhoneButton0;
    private LinearLayout mPhoneButton1;
    private TextView mPhoneTitle0;
    private TextView mPhoneTitle1;
    private Button mPreferredNetworkTypeApply;
    private ThreadPoolExecutor mQueuedWork;
    private Switch mRadioPowerOnSwitch;
    private TextView mRoamingState;
    private Switch mSimulateOutOfServiceSwitch;
    private TelephonyManager mTelephonyManager;
    private TextView mUplinkKbps;
    private View mView;
    private PhoneInfoSharedViewModel mViewModel;
    private TextView mVoiceNetwork;
    private TextView mVoiceRawReg;
    private static final boolean IS_USER_BUILD = "user".equals(Build.TYPE);
    private static String[] sPhoneIndexLabels = new String[0];
    private static final String[] CELL_INFO_REFRESH_RATE_LABELS = {"Disabled", "Immediate", "Min 5s", "Min 10s", "Min 60s"};
    private static final int[] CELL_INFO_REFRESH_RATES = {Integer.MAX_VALUE, 0, 5000, 10000, 60000};
    private boolean mIsUpdating = false;
    private ImsManager mImsManager = null;
    private ProvisioningManager mProvisioningManager = null;
    private Phone mPhone = null;
    private boolean mSystemUser = true;
    private int mPhoneId = -1;
    private int mSubId = -1;
    private final boolean[] mSimulateOos = new boolean[2];
    private int[] mSelectedSignalStrengthIndex = new int[2];
    private int[] mSelectedMockDataNetworkTypeIndex = new int[2];
    private List mCellInfoResult = null;
    CompoundButton.OnCheckedChangeListener mImsVolteCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDataNetwork.1
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            PhoneInformationV2FragmentDataNetwork.this.setImsVolteProvisionedState(z);
        }
    };
    CompoundButton.OnCheckedChangeListener mImsVtCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDataNetwork.2
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            PhoneInformationV2FragmentDataNetwork.this.setImsVtProvisionedState(z);
        }
    };
    CompoundButton.OnCheckedChangeListener mImsWfcCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDataNetwork.3
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            PhoneInformationV2FragmentDataNetwork.this.setImsWfcProvisionedState(z);
        }
    };
    CompoundButton.OnCheckedChangeListener mEabCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDataNetwork.4
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            PhoneInformationV2FragmentDataNetwork.this.setEabProvisionedState(z);
        }
    };
    private TelephonyCallback mTelephonyCallback = new RadioInfoTelephonyCallback();
    AdapterView.OnItemSelectedListener mCellInfoRefreshRateHandler = new AdapterView.OnItemSelectedListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDataNetwork.9
        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView adapterView) {
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView adapterView, View view, int i, long j) {
            PhoneInformationV2FragmentDataNetwork.this.mCellInfoRefreshRateIndex = i;
            PhoneInformationV2FragmentDataNetwork.this.mTelephonyManager.setCellInfoListRate(PhoneInformationV2FragmentDataNetwork.CELL_INFO_REFRESH_RATES[i], PhoneInformationV2FragmentDataNetwork.this.mPhoneId);
            PhoneInformationV2FragmentDataNetwork.this.updateAllCellInfo();
        }
    };
    AdapterView.OnItemSelectedListener mOnMockSignalStrengthSelectedListener = new AdapterView.OnItemSelectedListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDataNetwork.10
        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView adapterView) {
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView adapterView, View view, int i, long j) {
            PhoneInformationV2FragmentDataNetwork.log("mOnSignalStrengthSelectedListener: " + i);
            PhoneInformationV2FragmentDataNetwork.this.mSelectedSignalStrengthIndex[PhoneInformationV2FragmentDataNetwork.this.mPhoneId] = i;
            if (PhoneInformationV2FragmentDataNetwork.this.mSystemUser) {
                PhoneInformationV2FragmentDataNetwork.this.mPhone.getTelephonyTester().setSignalStrength(PhoneInformationUtil.SIGNAL_STRENGTH_LEVEL[i].intValue());
                PhoneInformationV2FragmentDataNetwork.this.mViewModel.setDataSignalStrength(i, PhoneInformationV2FragmentDataNetwork.this.mPhoneId);
            }
        }
    };
    AdapterView.OnItemSelectedListener mOnMockDataNetworkTypeSelectedListener = new AdapterView.OnItemSelectedListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDataNetwork.11
        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView adapterView) {
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView adapterView, View view, int i, long j) {
            PhoneInformationV2FragmentDataNetwork.log("mOnMockDataNetworkTypeSelectedListener: " + i);
            PhoneInformationV2FragmentDataNetwork.this.mSelectedMockDataNetworkTypeIndex[PhoneInformationV2FragmentDataNetwork.this.mPhoneId] = i;
            PhoneInformationV2FragmentDataNetwork.this.mViewModel.setDataNetworkTypeDisplay(i, PhoneInformationV2FragmentDataNetwork.this.mPhoneId);
            Intent intent = new Intent("com.android.internal.telephony.TestServiceState");
            if (i > 0) {
                StringBuilder sb = new StringBuilder("mOnMockDataNetworkTypeSelectedListener: Override RAT: ");
                Integer[] numArr = PhoneInformationUtil.MOCK_DATA_NETWORK_TYPE;
                sb.append(ServiceState.rilRadioTechnologyToString(numArr[i].intValue()));
                PhoneInformationV2FragmentDataNetwork.log(sb.toString());
                intent.putExtra("data_reg_state", 0);
                intent.putExtra("data_rat", numArr[i]);
            } else {
                PhoneInformationV2FragmentDataNetwork.log("mOnMockDataNetworkTypeSelectedListener: Remove RAT override.");
                intent.putExtra("action", "reset");
            }
            if (PhoneInformationV2FragmentDataNetwork.this.mSystemUser) {
                PhoneInformationV2FragmentDataNetwork.this.mPhone.getTelephonyTester().setServiceStateTestIntent(intent);
            }
        }
    };
    CompoundButton.OnCheckedChangeListener mRadioPowerOnChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDataNetwork.12
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            Phone[] phones = PhoneFactory.getPhones();
            if (phones == null) {
                return;
            }
            StringBuilder sb = new StringBuilder("toggle radio power: phone*");
            sb.append(phones.length);
            sb.append(" ");
            sb.append(PhoneInformationV2FragmentDataNetwork.this.isRadioOn() ? SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_ON : SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF);
            PhoneInformationV2FragmentDataNetwork.log(sb.toString());
            for (Phone phone : phones) {
                if (phone != null) {
                    phone.setRadioPower(z);
                }
            }
        }
    };
    private final CompoundButton.OnCheckedChangeListener mSimulateOosOnChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDataNetwork$$ExternalSyntheticLambda0
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            this.f$0.lambda$new$11(compoundButton, z);
        }
    };

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PhoneInformationV2PhoneId) {
            this.mListener = (PhoneInformationV2PhoneId) context;
        } else {
            PhoneInformationV2FragmentIms$$ExternalSyntheticBUOutline0.m(context);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mViewModel = (PhoneInfoSharedViewModel) new ViewModelProvider(requireActivity()).get(PhoneInfoSharedViewModel.class);
        log("onCreate");
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.phone_information_v2_tab_data_network, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mView = view;
        this.mContext = requireContext();
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mSystemUser = Process.myUserHandle().isSystem();
        log("onCreate: mSystemUser=" + this.mSystemUser);
        if (this.mSystemUser) {
            this.mPhone = PhoneInformationUtil.getPhone(SubscriptionManager.getDefaultSubscriptionId());
        }
        int defaultSubscriptionId = SubscriptionManager.getDefaultSubscriptionId();
        this.mSubId = defaultSubscriptionId;
        Phone phone = this.mPhone;
        if (phone != null) {
            this.mPhoneId = phone.getPhoneId();
        } else {
            this.mPhoneId = SubscriptionManager.getPhoneId(defaultSubscriptionId);
        }
        if (!SubscriptionManager.isValidPhoneId(this.mPhoneId)) {
            this.mPhoneId = 0;
        }
        PhoneInformationV2PhoneId phoneInformationV2PhoneId = this.mListener;
        if (phoneInformationV2PhoneId != null) {
            int phoneId = phoneInformationV2PhoneId.getPhoneId();
            this.mPhoneId = phoneId;
            this.mSubId = SubscriptionManager.getSubscriptionId(phoneId);
        }
        this.mImsManager = new ImsManager(this.mContext);
        try {
            this.mProvisioningManager = ProvisioningManager.createForSubscriptionId(this.mSubId);
        } catch (IllegalArgumentException e) {
            log("onCreate : IllegalArgumentException " + e.getMessage());
            this.mProvisioningManager = null;
        }
        this.mQueuedWork = new ThreadPoolExecutor(1, 1, 300000L, TimeUnit.MICROSECONDS, new LinkedBlockingDeque());
        TelephonyManager telephonyManagerCreateForSubscriptionId = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(this.mSubId);
        this.mTelephonyManager = telephonyManagerCreateForSubscriptionId;
        sPhoneIndexLabels = PhoneInformationUtil.getPhoneIndexLabels(telephonyManagerCreateForSubscriptionId);
        this.mOperatorName = (TextView) view.findViewById(R.id.operator);
        this.mRoamingState = (TextView) view.findViewById(R.id.roaming);
        this.mGprsState = (TextView) view.findViewById(R.id.gprs);
        this.mDataNetwork = (TextView) view.findViewById(R.id.data_network);
        this.mDataRawReg = (TextView) view.findViewById(R.id.data_raw_registration_state);
        this.mOverrideNetwork = (TextView) view.findViewById(R.id.override_network);
        this.mGsmState = (TextView) view.findViewById(R.id.gsm);
        this.mVoiceNetwork = (TextView) view.findViewById(R.id.voice_network);
        this.mVoiceRawReg = (TextView) view.findViewById(R.id.voice_raw_registration_state);
        this.mDBm = (TextView) view.findViewById(R.id.dbm);
        this.mNrAvailable = (TextView) view.findViewById(R.id.nr_available);
        this.mNrState = (TextView) view.findViewById(R.id.nr_state);
        this.mNrFrequency = (TextView) view.findViewById(R.id.nr_frequency);
        this.mDownlinkKbps = (TextView) view.findViewById(R.id.dl_kbps);
        this.mUplinkKbps = (TextView) view.findViewById(R.id.ul_kbps);
        updateBandwidths(0, 0);
        TextView textView = (TextView) view.findViewById(R.id.cellinfo);
        this.mCellInfo = textView;
        textView.setTypeface(Typeface.MONOSPACE);
        this.mFilterAll = (CheckBox) view.findViewById(R.id.filter_all);
        this.mFilterNr = (CheckBox) view.findViewById(R.id.filter_nr);
        this.mFilterLte = (CheckBox) view.findViewById(R.id.filter_lte);
        this.mFilterWcdma = (CheckBox) view.findViewById(R.id.filter_wcdma);
        this.mFilterGsm = (CheckBox) view.findViewById(R.id.filter_gsm);
        this.mPreferredNetworkTypeApply = (Button) view.findViewById(R.id.preferredNetworkTypeApply);
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDataNetwork$$ExternalSyntheticLambda1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                this.f$0.lambda$onViewCreated$0(compoundButton, z);
            }
        };
        this.mFilterNr.setOnCheckedChangeListener(onCheckedChangeListener);
        this.mFilterLte.setOnCheckedChangeListener(onCheckedChangeListener);
        this.mFilterWcdma.setOnCheckedChangeListener(onCheckedChangeListener);
        this.mFilterGsm.setOnCheckedChangeListener(onCheckedChangeListener);
        this.mFilterAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDataNetwork$$ExternalSyntheticLambda2
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                this.f$0.lambda$onViewCreated$1(compoundButton, z);
            }
        });
        this.mPreferredNetworkTypeApply.setOnClickListener(new View.OnClickListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDataNetwork$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                this.f$0.lambda$onViewCreated$3(view2);
            }
        });
        this.mMockSignalStrength = (Spinner) view.findViewById(R.id.signalStrength);
        if (!Build.isDebuggable() || !this.mSystemUser) {
            this.mMockSignalStrength.setVisibility(8);
            view.findViewById(R.id.signalStrength).setVisibility(8);
            view.findViewById(R.id.signal_strength_label).setVisibility(8);
        } else {
            ArrayAdapter arrayAdapter = new ArrayAdapter(this.mContext, android.R.layout.simple_spinner_item, PhoneInformationUtil.SIGNAL_STRENGTH_LEVEL);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.mMockSignalStrength.setAdapter((SpinnerAdapter) arrayAdapter);
        }
        this.mMockDataNetworkType = (Spinner) view.findViewById(R.id.dataNetworkType);
        if (!Build.isDebuggable() || !this.mSystemUser) {
            this.mMockDataNetworkType.setVisibility(8);
            view.findViewById(R.id.dataNetworkType).setVisibility(8);
            view.findViewById(R.id.data_network_type_label).setVisibility(8);
        } else {
            ArrayAdapter arrayAdapter2 = new ArrayAdapter(this.mContext, android.R.layout.simple_spinner_item, (String[]) Arrays.stream(PhoneInformationUtil.MOCK_DATA_NETWORK_TYPE).map(new Function() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDataNetwork$$ExternalSyntheticLambda4
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return ServiceState.rilRadioTechnologyToString(((Integer) obj).intValue());
                }
            }).toArray(new IntFunction() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDataNetwork$$ExternalSyntheticLambda5
                @Override // java.util.function.IntFunction
                public final Object apply(int i) {
                    return PhoneInformationV2FragmentDataNetwork.m3544$r8$lambda$C0A9JEHoIV2ddhWLSOT65jbVlg(i);
                }
            }));
            arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.mMockDataNetworkType.setAdapter((SpinnerAdapter) arrayAdapter2);
        }
        this.mRadioPowerOnSwitch = (Switch) view.findViewById(R.id.radio_power);
        this.mSimulateOutOfServiceSwitch = (Switch) view.findViewById(R.id.simulate_out_of_service);
        if (!Build.isDebuggable()) {
            this.mSimulateOutOfServiceSwitch.setVisibility(8);
        }
        this.mImsVolteProvisionedSwitch = (Switch) view.findViewById(R.id.volte_provisioned_switch);
        this.mImsVtProvisionedSwitch = (Switch) view.findViewById(R.id.vt_provisioned_switch);
        this.mImsWfcProvisionedSwitch = (Switch) view.findViewById(R.id.wfc_provisioned_switch);
        this.mEabProvisionedSwitch = (Switch) view.findViewById(R.id.eab_provisioned_switch);
        if (!isImsSupportedOnDevice()) {
            this.mImsVolteProvisionedSwitch.setVisibility(8);
            this.mImsVtProvisionedSwitch.setVisibility(8);
            this.mImsWfcProvisionedSwitch.setVisibility(8);
            this.mEabProvisionedSwitch.setVisibility(8);
        }
        if ((this.mTelephonyManager.getSupportedRadioAccessFamily() & 524288) == 0) {
            setNrStatsVisibility(8);
        }
        this.mCellInfoRefreshRateSpinner = (Spinner) view.findViewById(R.id.cell_info_rate_select);
        ArrayAdapter arrayAdapter3 = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, CELL_INFO_REFRESH_RATE_LABELS);
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mCellInfoRefreshRateSpinner.setAdapter((SpinnerAdapter) arrayAdapter3);
        this.mCellInfoRefreshRateIndex = 0;
        this.mPhoneButton0 = (LinearLayout) view.findViewById(R.id.phone_button_0);
        this.mPhoneTitle0 = (TextView) view.findViewById(R.id.phone_button_0_title);
        this.mPhoneButton1 = (LinearLayout) view.findViewById(R.id.phone_button_1);
        TextView textView2 = (TextView) view.findViewById(R.id.phone_button_1_title);
        this.mPhoneTitle1 = textView2;
        PhoneInformationUtil.configurePhoneSelectionUi(this.mPhoneButton0, this.mPhoneButton1, this.mPhoneTitle0, textView2, sPhoneIndexLabels);
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDataNetwork$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                this.f$0.lambda$onViewCreated$5(view2);
            }
        };
        this.mPhoneButton0.setOnClickListener(onClickListener);
        this.mPhoneButton1.setOnClickListener(onClickListener);
        restoreFromBundle(bundle);
        updateUI();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$0(CompoundButton compoundButton, boolean z) {
        if (this.mIsUpdating) {
            return;
        }
        this.mIsUpdating = true;
        if (!z) {
            this.mFilterAll.setChecked(false);
        } else if (this.mFilterNr.isChecked() && this.mFilterLte.isChecked() && this.mFilterWcdma.isChecked() && this.mFilterGsm.isChecked()) {
            this.mFilterAll.setChecked(true);
        }
        this.mIsUpdating = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$1(CompoundButton compoundButton, boolean z) {
        if (this.mIsUpdating) {
            return;
        }
        this.mIsUpdating = true;
        CheckBox checkBox = this.mFilterNr;
        if (z) {
            checkBox.setChecked(true);
            this.mFilterLte.setChecked(true);
            this.mFilterWcdma.setChecked(true);
            this.mFilterGsm.setChecked(true);
        } else {
            checkBox.setChecked(false);
            this.mFilterLte.setChecked(false);
            this.mFilterWcdma.setChecked(false);
            this.mFilterGsm.setChecked(false);
        }
        this.mIsUpdating = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$3(View view) {
        final long j = this.mFilterNr.isChecked() ? 524288L : 0L;
        if (this.mFilterLte.isChecked()) {
            j |= 266240;
        }
        if (this.mFilterWcdma.isChecked()) {
            j |= 82820;
        }
        if (this.mFilterGsm.isChecked()) {
            j |= 32771;
        }
        new Thread(new Runnable() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDataNetwork$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onViewCreated$2(j);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$2(long j) {
        this.mTelephonyManager.setAllowedNetworkTypesForReason(0, j);
    }

    /* JADX INFO: renamed from: $r8$lambda$C0A9JEHo-IV2ddhWLSOT65jbVlg, reason: not valid java name */
    public static /* synthetic */ String[] m3544$r8$lambda$C0A9JEHoIV2ddhWLSOT65jbVlg(int i) {
        return new String[i];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$5(View view) {
        int i;
        if (view.getId() == R.id.phone_button_0) {
            i = 0;
        } else {
            i = view.getId() == R.id.phone_button_1 ? 1 : -1;
        }
        log("targetPhoneId: " + i + ", mPhoneId: " + this.mPhoneId);
        if (i != -1) {
            if (this.mTelephonyManager.getActiveModemCount() > i) {
                if (this.mPhoneId != i) {
                    this.mPhoneId = i;
                    this.mListener.setPhoneId(i);
                    try {
                        this.mSubId = SubscriptionManager.getSubscriptionId(this.mPhoneId);
                        log("Updated phone id to " + this.mPhoneId + ", sub id to " + this.mSubId);
                        updatePhoneIndex();
                        return;
                    } catch (SecurityException unused) {
                        log("Permission error getting subId for phoneId " + this.mPhoneId);
                        return;
                    }
                }
                return;
            }
            log("Selected phone index " + i + " is not active/available.");
        }
    }

    private void updateUI() {
        int[] iArr = this.mSelectedSignalStrengthIndex;
        int i = this.mPhoneId;
        iArr[i] = ((Integer) this.mViewModel.getDataSignalStrength(i).getValue()).intValue();
        int[] iArr2 = this.mSelectedMockDataNetworkTypeIndex;
        int i2 = this.mPhoneId;
        iArr2[i2] = ((Integer) this.mViewModel.getDataNetworkTypeDisplay(i2).getValue()).intValue();
        boolean[] zArr = this.mSimulateOos;
        int i3 = this.mPhoneId;
        zArr[i3] = Boolean.TRUE.equals(this.mViewModel.getSimulateOutOfService(i3).getValue());
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        log("Started onResume");
        updateAllFields();
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        log("onPause: unregister phone & data intents");
        super.onPause();
        this.mTelephonyManager.unregisterTelephonyCallback(this.mTelephonyCallback);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        log("onDestroy");
        super.onDestroy();
        ThreadPoolExecutor threadPoolExecutor = this.mQueuedWork;
        if (threadPoolExecutor != null) {
            threadPoolExecutor.shutdown();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("mSelectedPhoneIndex", this.mPhoneId);
        bundle.putInt("mCellInfoRefreshRateIndex", this.mCellInfoRefreshRateIndex);
    }

    private void restoreFromBundle(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        int i = bundle.getInt("mSelectedPhoneIndex", 0);
        this.mPhoneId = i;
        this.mSubId = SubscriptionManager.getSubscriptionId(i);
        this.mCellInfoRefreshRateIndex = bundle.getInt("mCellInfoRefreshRateIndex", 0);
    }

    private boolean isImsConfigProvisioningRequired(int i, int i2) {
        ProvisioningManager provisioningManager = this.mProvisioningManager;
        if (provisioningManager == null) {
            return false;
        }
        try {
            return provisioningManager.isProvisioningRequiredForCapability(i, i2);
        } catch (RuntimeException e) {
            log("isImsConfigProvisioningRequired() exception: " + e.getMessage());
            return false;
        }
    }

    private boolean isRcsConfigProvisioningRequired(int i, int i2) {
        ProvisioningManager provisioningManager = this.mProvisioningManager;
        if (provisioningManager == null) {
            return false;
        }
        try {
            return provisioningManager.isRcsProvisioningRequiredForCapability(i, i2);
        } catch (RuntimeException e) {
            log("isRcsConfigProvisioningRequired() exception: " + e.getMessage());
            return false;
        }
    }

    private boolean getImsConfigProvisionedState(int i, int i2) {
        ProvisioningManager provisioningManager = this.mProvisioningManager;
        if (provisioningManager == null) {
            return false;
        }
        try {
            return provisioningManager.getProvisioningStatusForCapability(i, i2);
        } catch (RuntimeException e) {
            log("getImsConfigProvisionedState() exception: " + e.getMessage());
            return false;
        }
    }

    private boolean getRcsConfigProvisionedState(int i, int i2) {
        ProvisioningManager provisioningManager = this.mProvisioningManager;
        if (provisioningManager == null) {
            return false;
        }
        try {
            return provisioningManager.getRcsProvisioningStatusForCapability(i, i2);
        } catch (RuntimeException e) {
            log("getRcsConfigProvisionedState() exception: " + e.getMessage());
            return false;
        }
    }

    private boolean isImsVolteProvisioningRequired() {
        return isImsConfigProvisioningRequired(1, 0);
    }

    private boolean isImsVtProvisioningRequired() {
        return isImsConfigProvisioningRequired(2, 0);
    }

    private boolean isImsWfcProvisioningRequired() {
        return isImsConfigProvisioningRequired(1, 1);
    }

    private boolean isEabProvisioningRequired() {
        return isRcsConfigProvisioningRequired(2, 0);
    }

    private boolean isImsVolteProvisioned() {
        return getImsConfigProvisionedState(1, 0);
    }

    private boolean isImsSupportedOnDevice() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.telephony.ims");
    }

    private boolean isImsVtProvisioned() {
        return getImsConfigProvisionedState(2, 0);
    }

    private boolean isImsWfcProvisioned() {
        return getImsConfigProvisionedState(1, 1);
    }

    private boolean isEabProvisioned() {
        return getRcsConfigProvisionedState(2, 0);
    }

    private boolean isEabEnabledByPlatform() {
        PersistableBundle configForSubId;
        if (!SubscriptionManager.isValidPhoneId(this.mPhoneId) || (configForSubId = PhoneInformationUtil.getCarrierConfig(this.mContext).getConfigForSubId(this.mSubId)) == null) {
            return false;
        }
        return configForSubId.getBoolean("use_rcs_presence_bool", false) || configForSubId.getBoolean("ims.enable_presence_capability_exchange_bool", false);
    }

    private void updateServiceEnabledByPlatform() {
        if (!SubscriptionManager.isValidSubscriptionId(this.mSubId)) {
            log("updateServiceEnabledByPlatform subscription ID is invalid");
            return;
        }
        ImsMmTelManager imsMmTelManager = this.mImsManager.getImsMmTelManager(this.mSubId);
        try {
            imsMmTelManager.isSupported(1, 1, this.mContext.getMainExecutor(), new Consumer() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDataNetwork$$ExternalSyntheticLambda10
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$updateServiceEnabledByPlatform$6((Boolean) obj);
                }
            });
            imsMmTelManager.isSupported(2, 1, this.mContext.getMainExecutor(), new Consumer() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDataNetwork$$ExternalSyntheticLambda11
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$updateServiceEnabledByPlatform$7((Boolean) obj);
                }
            });
            imsMmTelManager.isSupported(1, 2, this.mContext.getMainExecutor(), new Consumer() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDataNetwork$$ExternalSyntheticLambda12
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$updateServiceEnabledByPlatform$8((Boolean) obj);
                }
            });
        } catch (ImsException e) {
            log(e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateServiceEnabledByPlatform$6(Boolean bool) {
        updateVolteProvisionedSwitch(bool.booleanValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateServiceEnabledByPlatform$7(Boolean bool) {
        updateVtProvisionedSwitch(bool.booleanValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateServiceEnabledByPlatform$8(Boolean bool) {
        updateWfcProvisionedSwitch(bool.booleanValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDataState() {
        if (isAdded()) {
            int dataState = this.mTelephonyManager.getDataState();
            Resources resources = getResources();
            String string = resources.getString(R.string.radioInfo_unknown);
            if (!SubscriptionManager.isValidSubscriptionId(this.mSubId) || dataState == 0) {
                string = resources.getString(R.string.radioInfo_data_disconnected);
            } else if (dataState == 1) {
                string = resources.getString(R.string.radioInfo_data_connecting);
            } else if (dataState == 2) {
                string = resources.getString(R.string.radioInfo_data_connected);
            } else if (dataState == 3) {
                string = resources.getString(R.string.radioInfo_data_suspended);
            }
            this.mGprsState.setText(string);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNetworkType() {
        SubscriptionManager subscriptionManager = (SubscriptionManager) this.mContext.getSystemService(SubscriptionManager.class);
        if (SubscriptionManager.isValidPhoneId(this.mPhoneId) && subscriptionManager.isActiveSubscriptionId(this.mSubId)) {
            this.mDataNetwork.setText(ServiceState.rilRadioTechnologyToString(this.mTelephonyManager.getServiceStateForSlot(this.mPhoneId).getRilDataRadioTechnology()));
            this.mVoiceNetwork.setText(ServiceState.rilRadioTechnologyToString(this.mTelephonyManager.getServiceStateForSlot(this.mPhoneId).getRilVoiceRadioTechnology()));
            this.mOverrideNetwork.setText(TelephonyDisplayInfo.overrideNetworkTypeToString((this.mDisplayInfo == null || !SubscriptionManager.isValidSubscriptionId(this.mSubId)) ? 0 : this.mDisplayInfo.getOverrideNetworkType()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRadioPowerState() {
        this.mRadioPowerOnSwitch.setOnCheckedChangeListener(null);
        this.mRadioPowerOnSwitch.setChecked(isRadioOn());
        this.mRadioPowerOnSwitch.setOnCheckedChangeListener(this.mRadioPowerOnChangeListener);
    }

    private void updateVolteProvisionedSwitch(boolean z) {
        boolean z2 = false;
        boolean z3 = z && isImsVolteProvisioned();
        log("updateVolteProvisionedSwitch isProvisioned" + z3);
        this.mImsVolteProvisionedSwitch.setOnCheckedChangeListener(null);
        this.mImsVolteProvisionedSwitch.setChecked(z3);
        this.mImsVolteProvisionedSwitch.setOnCheckedChangeListener(this.mImsVolteCheckedChangeListener);
        Switch r2 = this.mImsVolteProvisionedSwitch;
        if (!IS_USER_BUILD && z && isImsVolteProvisioningRequired()) {
            z2 = true;
        }
        r2.setEnabled(z2);
    }

    private void updateVtProvisionedSwitch(boolean z) {
        boolean z2 = false;
        boolean z3 = z && isImsVtProvisioned();
        log("updateVtProvisionedSwitch isProvisioned" + z3);
        this.mImsVtProvisionedSwitch.setOnCheckedChangeListener(null);
        this.mImsVtProvisionedSwitch.setChecked(z3);
        this.mImsVtProvisionedSwitch.setOnCheckedChangeListener(this.mImsVtCheckedChangeListener);
        Switch r2 = this.mImsVtProvisionedSwitch;
        if (!IS_USER_BUILD && z && isImsVtProvisioningRequired()) {
            z2 = true;
        }
        r2.setEnabled(z2);
    }

    private void updateWfcProvisionedSwitch(boolean z) {
        boolean z2 = false;
        boolean z3 = z && isImsWfcProvisioned();
        log("updateWfcProvisionedSwitch isProvisioned" + z3);
        this.mImsWfcProvisionedSwitch.setOnCheckedChangeListener(null);
        this.mImsWfcProvisionedSwitch.setChecked(z3);
        this.mImsWfcProvisionedSwitch.setOnCheckedChangeListener(this.mImsWfcCheckedChangeListener);
        Switch r2 = this.mImsWfcProvisionedSwitch;
        if (!IS_USER_BUILD && z && isImsWfcProvisioningRequired()) {
            z2 = true;
        }
        r2.setEnabled(z2);
    }

    private void updateEabProvisionedSwitch(boolean z) {
        log("updateEabProvisionedSwitch isEabWfcProvisioned()=" + isEabProvisioned());
        this.mEabProvisionedSwitch.setOnCheckedChangeListener(null);
        this.mEabProvisionedSwitch.setChecked(isEabProvisioned());
        this.mEabProvisionedSwitch.setOnCheckedChangeListener(this.mEabCheckedChangeListener);
        this.mEabProvisionedSwitch.setEnabled(!IS_USER_BUILD && z && isEabProvisioningRequired());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateImsProvisionedState() {
        if (isImsSupportedOnDevice()) {
            updateServiceEnabledByPlatform();
            updateEabProvisionedSwitch(isEabEnabledByPlatform());
        }
    }

    private String getRawRegistrationStateText(ServiceState serviceState, int i, int i2) {
        NetworkRegistrationInfo networkRegistrationInfo;
        if (serviceState == null || (networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(i, i2)) == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(NetworkRegistrationInfo.registrationStateToString(networkRegistrationInfo.getNetworkRegistrationState()));
        sb.append(networkRegistrationInfo.isEmergencyEnabled() ? "_EM" : "");
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRawRegistrationState(ServiceState serviceState) {
        ServiceState serviceStateForSlot = this.mTelephonyManager.getServiceStateForSlot(this.mPhoneId);
        this.mVoiceRawReg.setText(getRawRegistrationStateText(serviceStateForSlot, 1, 1));
        this.mDataRawReg.setText(getRawRegistrationStateText(serviceStateForSlot, 2, 1));
    }

    private void updateAllFields() {
        updateDataState();
        updateSelectionVisuals();
        updateRadioPowerState();
        updateImsProvisionedState();
        updateNetworkType();
        updateNrStats();
        new Thread(new Runnable() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDataNetwork$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateAllFields$10();
            }
        }).start();
        updateCellInfo(this.mCellInfoResult);
        this.mCellInfoRefreshRateSpinner.setOnItemSelectedListener(this.mCellInfoRefreshRateHandler);
        this.mCellInfoRefreshRateSpinner.setSelection(this.mCellInfoRefreshRateIndex);
        this.mTelephonyManager.setCellInfoListRate(CELL_INFO_REFRESH_RATES[this.mCellInfoRefreshRateIndex], this.mSubId);
        this.mMockSignalStrength.setSelection(this.mSelectedSignalStrengthIndex[this.mPhoneId]);
        this.mMockSignalStrength.setOnItemSelectedListener(this.mOnMockSignalStrengthSelectedListener);
        this.mMockDataNetworkType.setSelection(this.mSelectedMockDataNetworkTypeIndex[this.mPhoneId]);
        this.mMockDataNetworkType.setOnItemSelectedListener(this.mOnMockDataNetworkTypeSelectedListener);
        this.mRadioPowerOnSwitch.setOnCheckedChangeListener(this.mRadioPowerOnChangeListener);
        this.mSimulateOutOfServiceSwitch.setChecked(this.mSimulateOos[this.mPhoneId]);
        this.mSimulateOutOfServiceSwitch.setOnCheckedChangeListener(this.mSimulateOosOnChangeListener);
        this.mImsVolteProvisionedSwitch.setOnCheckedChangeListener(this.mImsVolteCheckedChangeListener);
        this.mImsVtProvisionedSwitch.setOnCheckedChangeListener(this.mImsVtCheckedChangeListener);
        this.mImsWfcProvisionedSwitch.setOnCheckedChangeListener(this.mImsWfcCheckedChangeListener);
        this.mEabProvisionedSwitch.setOnCheckedChangeListener(this.mEabCheckedChangeListener);
        unregisterPhoneStateListener();
        registerPhoneStateListener();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateAllFields$10() {
        long allowedNetworkTypesBitmask = this.mTelephonyManager.getAllowedNetworkTypesBitmask();
        final boolean z = (524288 & allowedNetworkTypesBitmask) != 0;
        final boolean z2 = (4096 & allowedNetworkTypesBitmask) != 0;
        final boolean z3 = (4 & allowedNetworkTypesBitmask) != 0;
        final boolean z4 = (allowedNetworkTypesBitmask & 32768) != 0;
        this.mHandler.post(new Runnable() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDataNetwork$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateAllFields$9(z, z2, z3, z4);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateAllFields$9(boolean z, boolean z2, boolean z3, boolean z4) {
        this.mIsUpdating = true;
        this.mFilterNr.setChecked(z);
        this.mFilterLte.setChecked(z2);
        this.mFilterWcdma.setChecked(z3);
        this.mFilterGsm.setChecked(z4);
        this.mFilterAll.setChecked(z && z2 && z3 && z4);
        this.mIsUpdating = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCellInfo(List list) {
        this.mCellInfo.setText(PhoneInformationUtil.buildCellInfoString(list));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAllCellInfo() {
        this.mCellInfo.setText("");
        final Runnable runnable = new Runnable() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDataNetwork.5
            @Override // java.lang.Runnable
            public void run() {
                PhoneInformationV2FragmentDataNetwork phoneInformationV2FragmentDataNetwork = PhoneInformationV2FragmentDataNetwork.this;
                phoneInformationV2FragmentDataNetwork.updateCellInfo(phoneInformationV2FragmentDataNetwork.mCellInfoResult);
            }
        };
        this.mQueuedWork.execute(new Runnable() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDataNetwork.6
            @Override // java.lang.Runnable
            public void run() {
                PhoneInformationV2FragmentDataNetwork phoneInformationV2FragmentDataNetwork = PhoneInformationV2FragmentDataNetwork.this;
                phoneInformationV2FragmentDataNetwork.mCellInfoResult = phoneInformationV2FragmentDataNetwork.mTelephonyManager.getAllCellInfo();
                PhoneInformationV2FragmentDataNetwork.this.mHandler.post(runnable);
            }
        });
    }

    private void updatePhoneIndex() {
        unregisterPhoneStateListener();
        this.mTelephonyManager = this.mTelephonyManager.createForSubscriptionId(this.mSubId);
        if (this.mSystemUser) {
            this.mPhone = PhoneFactory.getPhone(this.mPhoneId);
        }
        this.mImsManager = new ImsManager(this.mContext);
        try {
            this.mProvisioningManager = ProvisioningManager.createForSubscriptionId(this.mSubId);
        } catch (IllegalArgumentException e) {
            log("updatePhoneIndex : IllegalArgumentException " + e.getMessage());
            this.mProvisioningManager = null;
        }
        updateAllFields();
    }

    private void unregisterPhoneStateListener() {
        this.mTelephonyManager.unregisterTelephonyCallback(this.mTelephonyCallback);
        this.mOperatorName.setText("");
        this.mRoamingState.setText("");
        this.mGprsState.setText("");
        this.mDataNetwork.setText("");
        this.mDataRawReg.setText("");
        this.mOverrideNetwork.setText("");
        this.mVoiceNetwork.setText("");
        this.mGsmState.setText("");
        this.mVoiceRawReg.setText("");
        this.mDBm.setText("");
        this.mDownlinkKbps.setText("");
        this.mUplinkKbps.setText("");
        this.mCellInfo.setText("");
    }

    private void registerPhoneStateListener() {
        this.mTelephonyCallback = new RadioInfoTelephonyCallback();
        this.mTelephonyManager.registerTelephonyCallback(new HandlerExecutor(this.mHandler), this.mTelephonyCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateServiceState(ServiceState serviceState) {
        if (!SubscriptionManager.isValidSubscriptionId(this.mSubId)) {
            serviceState = this.mTelephonyManager.getServiceStateForSlot(this.mPhoneId);
        }
        log("Update service state " + serviceState);
        int state = serviceState.getState();
        Resources resources = getResources();
        String string = resources.getString(R.string.radioInfo_unknown);
        if (state == 0) {
            string = resources.getString(R.string.radioInfo_service_in);
        } else if (state == 1) {
            string = resources.getString(R.string.radioInfo_service_out);
        } else if (state == 2) {
            string = resources.getString(R.string.radioInfo_service_emergency);
        } else if (state == 3) {
            string = resources.getString(R.string.radioInfo_service_off);
        }
        this.mGsmState.setText(string);
        boolean roaming = serviceState.getRoaming();
        TextView textView = this.mRoamingState;
        if (roaming) {
            textView.setText(R.string.radioInfo_roaming_in);
        } else {
            textView.setText(R.string.radioInfo_roaming_not);
        }
        this.mOperatorName.setText(serviceState.getOperatorAlphaLong());
    }

    private void updateBandwidths(int i, int i2) {
        if (i < 0 || i == Integer.MAX_VALUE) {
            i = -1;
        }
        if (i2 < 0 || i2 == Integer.MAX_VALUE) {
            i2 = -1;
        }
        this.mDownlinkKbps.setText(String.format("%-5d", Integer.valueOf(i)));
        this.mUplinkKbps.setText(String.format("%-5d", Integer.valueOf(i2)));
    }

    private void setNrStatsVisibility(int i) {
        ((TextView) this.mView.findViewById(R.id.nr_available_label)).setVisibility(i);
        this.mNrAvailable.setVisibility(i);
        ((TextView) this.mView.findViewById(R.id.nr_state_label)).setVisibility(i);
        this.mNrState.setVisibility(i);
        ((TextView) this.mView.findViewById(R.id.nr_frequency_label)).setVisibility(i);
        this.mNrFrequency.setVisibility(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNrStats() {
        DataSpecificRegistrationInfo dataSpecificInfo;
        if ((this.mTelephonyManager.getSupportedRadioAccessFamily() & 524288) == 0) {
            return;
        }
        ServiceState serviceStateForSlot = this.mTelephonyManager.getServiceStateForSlot(this.mPhoneId);
        if (serviceStateForSlot != null) {
            NetworkRegistrationInfo networkRegistrationInfo = serviceStateForSlot.getNetworkRegistrationInfo(2, 1);
            if (networkRegistrationInfo != null && (dataSpecificInfo = networkRegistrationInfo.getDataSpecificInfo()) != null) {
                this.mNrAvailable.setText(String.valueOf(dataSpecificInfo.isNrAvailable));
            }
            this.mNrState.setText(NetworkRegistrationInfo.nrStateToString(serviceStateForSlot.getNrState()));
            this.mNrFrequency.setText(ServiceState.frequencyRangeToString(serviceStateForSlot.getNrFrequencyRange()));
            return;
        }
        log("Clear Nr stats by null service state");
        this.mNrAvailable.setText("");
        this.mNrState.setText("");
        this.mNrFrequency.setText("");
    }

    class RadioInfoTelephonyCallback extends TelephonyCallback implements TelephonyCallback.DataConnectionStateListener, TelephonyCallback.CallStateListener, TelephonyCallback.ServiceStateListener, TelephonyCallback.DisplayInfoListener, TelephonyCallback.CellInfoListener {
        private RadioInfoTelephonyCallback() {
        }

        @Override // android.telephony.TelephonyCallback.DataConnectionStateListener
        public void onDataConnectionStateChanged(int i, int i2) {
            PhoneInformationV2FragmentDataNetwork.this.updateDataState();
            PhoneInformationV2FragmentDataNetwork.this.updateNetworkType();
        }

        @Override // android.telephony.TelephonyCallback.CellInfoListener
        public void onCellInfoChanged(List list) {
            PhoneInformationV2FragmentDataNetwork.log("onCellInfoChanged: arrayCi=" + list);
            PhoneInformationV2FragmentDataNetwork.this.mCellInfoResult = list;
            PhoneInformationV2FragmentDataNetwork phoneInformationV2FragmentDataNetwork = PhoneInformationV2FragmentDataNetwork.this;
            phoneInformationV2FragmentDataNetwork.updateCellInfo(phoneInformationV2FragmentDataNetwork.mCellInfoResult);
        }

        @Override // android.telephony.TelephonyCallback.CallStateListener
        public void onCallStateChanged(int i) {
            PhoneInformationV2FragmentDataNetwork.this.updateNetworkType();
        }

        @Override // android.telephony.TelephonyCallback.ServiceStateListener
        public void onServiceStateChanged(ServiceState serviceState) {
            PhoneInformationV2FragmentDataNetwork.log("onServiceStateChanged: ServiceState=" + serviceState);
            PhoneInformationV2FragmentDataNetwork.this.updateServiceState(serviceState);
            PhoneInformationV2FragmentDataNetwork.this.updateRadioPowerState();
            PhoneInformationV2FragmentDataNetwork.this.updateNetworkType();
            PhoneInformationV2FragmentDataNetwork.this.updateRawRegistrationState(serviceState);
            PhoneInformationV2FragmentDataNetwork.this.updateImsProvisionedState();
            if (PhoneInformationV2FragmentDataNetwork.this.mHandler.hasMessages(1008)) {
                return;
            }
            PhoneInformationV2FragmentDataNetwork.this.mHandler.post(new Runnable() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDataNetwork$RadioInfoTelephonyCallback$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onServiceStateChanged$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onServiceStateChanged$0() {
            PhoneInformationV2FragmentDataNetwork.this.updateNrStats();
        }

        @Override // android.telephony.TelephonyCallback.DisplayInfoListener
        public void onDisplayInfoChanged(TelephonyDisplayInfo telephonyDisplayInfo) {
            PhoneInformationV2FragmentDataNetwork.this.mDisplayInfo = telephonyDisplayInfo;
            PhoneInformationV2FragmentDataNetwork.this.updateNetworkType();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isRadioOn() {
        return this.mTelephonyManager.getRadioPowerState() == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setImsVolteProvisionedState(boolean z) {
        log("setImsVolteProvisioned state: ".concat(z ? SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_ON : SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF));
        setImsConfigProvisionedState(1, 0, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setImsVtProvisionedState(boolean z) {
        log("setImsVtProvisioned() state: ".concat(z ? SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_ON : SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF));
        setImsConfigProvisionedState(2, 0, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setImsWfcProvisionedState(boolean z) {
        log("setImsWfcProvisioned() state: ".concat(z ? SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_ON : SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF));
        setImsConfigProvisionedState(1, 1, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEabProvisionedState(boolean z) {
        log("setEabProvisioned() state: ".concat(z ? SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_ON : SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF));
        setRcsConfigProvisionedState(2, 0, z);
    }

    private void setImsConfigProvisionedState(final int i, final int i2, final boolean z) {
        if (this.mProvisioningManager != null) {
            this.mQueuedWork.execute(new Runnable() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDataNetwork.7
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        PhoneInformationV2FragmentDataNetwork.this.mProvisioningManager.setProvisioningStatusForCapability(i, i2, z);
                    } catch (RuntimeException e) {
                        PhoneInformationV2FragmentDataNetwork.log("setImsConfigProvisioned() exception: " + e.getMessage());
                    }
                }
            });
        }
    }

    private void setRcsConfigProvisionedState(final int i, final int i2, final boolean z) {
        if (this.mProvisioningManager != null) {
            this.mQueuedWork.execute(new Runnable() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDataNetwork.8
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        PhoneInformationV2FragmentDataNetwork.this.mProvisioningManager.setRcsProvisioningStatusForCapability(i, i2, z);
                    } catch (RuntimeException e) {
                        PhoneInformationV2FragmentDataNetwork.log("setRcsConfigProvisioned() exception:" + e.getMessage());
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$11(CompoundButton compoundButton, boolean z) {
        Intent intent = new Intent("com.android.internal.telephony.TestServiceState");
        this.mViewModel.setSimulateOutOfService(z, this.mPhoneId);
        if (z) {
            log("Send OOS override broadcast intent.");
            intent.putExtra("data_reg_state", 1);
            this.mSimulateOos[this.mPhoneId] = true;
        } else {
            log("Remove OOS override.");
            intent.putExtra("action", "reset");
            this.mSimulateOos[this.mPhoneId] = false;
        }
        this.mPhone.getTelephonyTester().setServiceStateTestIntent(intent);
    }

    private void updateSelectionVisuals() {
        LinearLayout linearLayout;
        LinearLayout linearLayout2;
        if (this.mPhoneId == 0) {
            linearLayout = this.mPhoneButton0;
            linearLayout2 = this.mPhoneButton1;
        } else {
            linearLayout = this.mPhoneButton1;
            linearLayout2 = this.mPhoneButton0;
        }
        linearLayout.setBackgroundColor(ContextCompat.getColor(this.mContext, android.R.color.holo_green_dark));
        linearLayout2.setBackgroundColor(ContextCompat.getColor(this.mContext, android.R.color.darker_gray));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("PhoneInformationV2 DataNetwork", str);
    }
}
