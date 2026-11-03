package com.android.phone.settings.hiddenmenu;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.telephony.RadioAccessFamily;
import android.telephony.RadioAccessSpecifier;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.phone.R;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.IntPredicate;

/* JADX INFO: loaded from: classes.dex */
public class PhoneInformationV2FragmentSatellite extends Fragment {
    private static String[] sPhoneIndexLabels = new String[0];
    private String mActionEsos;
    private String mActionEsosDemo;
    private Context mContext;
    private Switch mEnforceSatelliteChannel;
    private Button mEsosButton;
    private Button mEsosDemoButton;
    private Button mForceCampSatelliteButton;
    private RadioGroup mForceCampSatelliteSelectionRadioGroup;
    private PhoneInformationV2PhoneId mListener;
    private Spinner mManualOverrideBand;
    private Switch mMockSatellite;
    private RadioGroup mMockSatelliteData;
    private Switch mMockSatelliteDataSwitch;
    private Button mNbIotConfigViewerButton;
    private LinearLayout mPhoneButton0;
    private LinearLayout mPhoneButton1;
    private TextView mPhoneTitle0;
    private TextView mPhoneTitle1;
    private EditText mSatelliteChannels;
    private Button mSatelliteEnableNonEmergencyModeButton;
    private TelephonyManager mTelephonyManager;
    private PhoneInfoSharedViewModel mViewModel;
    private final List mOriginalApnSettings = new ArrayList();
    private int mSubId = -1;
    private int mPhoneId = -1;
    private final PersistableBundle[] mCarrierSatelliteOriginalBundle = new PersistableBundle[2];
    private final PersistableBundle[] mSatelliteDataOriginalBundle = new PersistableBundle[2];
    private Phone mPhone = null;
    private boolean mSystemUser = true;
    private final CompoundButton.OnCheckedChangeListener mForceSatelliteChannelOnChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentSatellite$$ExternalSyntheticLambda0
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            this.f$0.lambda$new$5(compoundButton, z);
        }
    };
    private final RadioGroup.OnCheckedChangeListener mForceCampSatelliteSelectionRadioGroupListener = new RadioGroup.OnCheckedChangeListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentSatellite$$ExternalSyntheticLambda1
        @Override // android.widget.RadioGroup.OnCheckedChangeListener
        public final void onCheckedChanged(RadioGroup radioGroup, int i) {
            this.f$0.lambda$new$6(radioGroup, i);
        }
    };
    private final AdapterView.OnItemSelectedListener mManualOverrideBandSelectedListener = new AdapterView.OnItemSelectedListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentSatellite.1
        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView adapterView) {
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView adapterView, View view, int i, long j) {
            PhoneInformationV2FragmentSatellite.log("mManualOverrideBandSelectedListener: " + i);
            PhoneInformationV2FragmentSatellite.this.mViewModel.setSelectedManualOverrideBandIndex(i, PhoneInformationV2FragmentSatellite.this.mPhoneId);
            if (i == 0) {
                PhoneInformationV2FragmentSatellite.log("mManualOverrideBandSelectedListener: No band selected");
            }
        }
    };
    private final View.OnClickListener mForceCampSatelliteConnectHandler = new View.OnClickListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentSatellite$$ExternalSyntheticLambda2
        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            this.f$0.lambda$new$7(view);
        }
    };
    private final RadioGroup.OnCheckedChangeListener mMockSatelliteDataListener = new RadioGroup.OnCheckedChangeListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentSatellite$$ExternalSyntheticLambda3
        @Override // android.widget.RadioGroup.OnCheckedChangeListener
        public final void onCheckedChanged(RadioGroup radioGroup, int i) {
            this.f$0.lambda$new$17(radioGroup, i);
        }
    };
    private final CompoundButton.OnCheckedChangeListener mMockSatelliteDataSwitchListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentSatellite$$ExternalSyntheticLambda4
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            this.f$0.lambda$new$18(compoundButton, z);
        }
    };
    private final CompoundButton.OnCheckedChangeListener mMockSatelliteListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentSatellite$$ExternalSyntheticLambda5
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            this.f$0.lambda$new$19(compoundButton, z);
        }
    };
    private final Handler mHandler = new Handler(this, Looper.getMainLooper()) { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentSatellite.2
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
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
        log("onCreateView");
        return layoutInflater.inflate(R.layout.phone_information_v2_tab_satellite, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        log("onViewCreated");
        this.mContext = requireContext();
        this.mSubId = SubscriptionManager.getDefaultSubscriptionId();
        if (this.mSystemUser) {
            this.mPhone = PhoneInformationUtil.getPhone(SubscriptionManager.getDefaultSubscriptionId());
        }
        Phone phone = this.mPhone;
        if (phone != null) {
            this.mPhoneId = phone.getPhoneId();
        } else {
            this.mPhoneId = SubscriptionManager.getPhoneId(this.mSubId);
        }
        PhoneInformationV2PhoneId phoneInformationV2PhoneId = this.mListener;
        if (phoneInformationV2PhoneId != null) {
            int phoneId = phoneInformationV2PhoneId.getPhoneId();
            this.mPhoneId = phoneId;
            this.mSubId = SubscriptionManager.getSubscriptionId(phoneId);
        }
        this.mTelephonyManager = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(this.mSubId);
        Resources resources = getResources();
        sPhoneIndexLabels = PhoneInformationUtil.getPhoneIndexLabels(this.mTelephonyManager);
        this.mActionEsos = resources.getString(android.R.string.dump_heap_system_text);
        this.mActionEsosDemo = resources.getString(android.R.string.done_label);
        this.mPhoneButton0 = (LinearLayout) view.findViewById(R.id.phone_button_0);
        this.mPhoneTitle0 = (TextView) view.findViewById(R.id.phone_button_0_title);
        this.mPhoneButton1 = (LinearLayout) view.findViewById(R.id.phone_button_1);
        TextView textView = (TextView) view.findViewById(R.id.phone_button_1_title);
        this.mPhoneTitle1 = textView;
        PhoneInformationUtil.configurePhoneSelectionUi(this.mPhoneButton0, this.mPhoneButton1, this.mPhoneTitle0, textView, sPhoneIndexLabels);
        this.mMockSatellite = (Switch) view.findViewById(R.id.mock_carrier_roaming_satellite);
        this.mMockSatelliteDataSwitch = (Switch) view.findViewById(R.id.satellite_data_controller_switch);
        this.mMockSatelliteData = (RadioGroup) view.findViewById(R.id.satellite_data_controller);
        this.mEnforceSatelliteChannel = (Switch) view.findViewById(R.id.enforce_satellite_channel);
        this.mForceCampSatelliteSelectionRadioGroup = (RadioGroup) view.findViewById(R.id.force_camp_satellite_selection_radio_group);
        this.mManualOverrideBand = (Spinner) view.findViewById(R.id.manual_band_selector);
        this.mSatelliteChannels = (EditText) view.findViewById(R.id.satellite_channel_number);
        this.mForceCampSatelliteButton = (Button) view.findViewById(R.id.force_camp_satellite_button);
        if (!Build.isDebuggable()) {
            this.mMockSatellite.setVisibility(8);
            this.mMockSatelliteDataSwitch.setVisibility(8);
            this.mMockSatelliteData.setVisibility(8);
            this.mEnforceSatelliteChannel.setVisibility(8);
            this.mForceCampSatelliteSelectionRadioGroup.setVisibility(8);
            this.mManualOverrideBand.setVisibility(8);
            this.mSatelliteChannels.setVisibility(8);
            view.findViewById(R.id.manual_override_fields).setVisibility(8);
            this.mForceCampSatelliteButton.setVisibility(8);
        } else {
            ArrayAdapter arrayAdapter = new ArrayAdapter(this.mContext, android.R.layout.simple_spinner_item, PhoneInformationUtil.BAND_LABELS);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.mManualOverrideBand.setAdapter((SpinnerAdapter) arrayAdapter);
        }
        this.mForceCampSatelliteSelectionRadioGroup.setVisibility(8);
        view.findViewById(R.id.manual_override_fields).setVisibility(8);
        this.mForceCampSatelliteButton.setVisibility(8);
        this.mSatelliteEnableNonEmergencyModeButton = (Button) view.findViewById(R.id.satellite_enable_non_emergency_mode);
        this.mNbIotConfigViewerButton = (Button) view.findViewById(R.id.nb_iot_config_viewer);
        this.mEsosButton = (Button) view.findViewById(R.id.esos_questionnaire);
        this.mEsosDemoButton = (Button) view.findViewById(R.id.demo_esos_questionnaire);
        boolean zShouldHideButton = shouldHideButton(this.mActionEsos);
        Button button = this.mEsosButton;
        if (zShouldHideButton) {
            button.setVisibility(8);
            log("mActionEsos is not visible");
        } else {
            button.setOnClickListener(new View.OnClickListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentSatellite$$ExternalSyntheticLambda6
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    this.f$0.lambda$onViewCreated$0(view2);
                }
            });
            log("mActionEsos is visible");
        }
        boolean zShouldHideButton2 = shouldHideButton(this.mActionEsosDemo);
        Button button2 = this.mEsosDemoButton;
        if (zShouldHideButton2) {
            button2.setVisibility(8);
            log("mActionEsosDemo is gone");
        } else {
            button2.setOnClickListener(new View.OnClickListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentSatellite$$ExternalSyntheticLambda7
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    this.f$0.lambda$onViewCreated$1(view2);
                }
            });
            log("mActionEsosDemo is visible");
        }
        boolean zShouldHideNonEmergencyMode = PhoneInformationUtil.shouldHideNonEmergencyMode(this.mContext, this.mSubId);
        Button button3 = this.mSatelliteEnableNonEmergencyModeButton;
        if (zShouldHideNonEmergencyMode) {
            button3.setVisibility(8);
        } else {
            button3.setOnClickListener(new View.OnClickListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentSatellite$$ExternalSyntheticLambda8
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    this.f$0.lambda$onViewCreated$2(view2);
                }
            });
        }
        this.mNbIotConfigViewerButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentSatellite$$ExternalSyntheticLambda9
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                this.f$0.lambda$onViewCreated$3(view2);
            }
        });
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentSatellite$$ExternalSyntheticLambda10
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                this.f$0.lambda$onViewCreated$4(view2);
            }
        };
        this.mPhoneButton0.setOnClickListener(onClickListener);
        this.mPhoneButton1.setOnClickListener(onClickListener);
        updateUI();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$0(View view) {
        this.mContext.startActivityAsUser(new Intent(this.mActionEsos).addFlags(268468224), UserHandle.CURRENT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$1(View view) {
        this.mContext.startActivityAsUser(new Intent(this.mActionEsosDemo).addFlags(268468224), UserHandle.CURRENT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$2(View view) {
        Intent intent = PhoneInformationUtil.mNonEsosIntent;
        if (intent != null) {
            this.mContext.sendBroadcast(intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$3(View view) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.putExtra("mSubId", this.mSubId);
        intent.setClassName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.settings.SatelliteConfigViewer");
        this.mContext.startActivityAsUser(intent, UserHandle.CURRENT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$4(View view) {
        int i;
        if (view.getId() == R.id.phone_button_0) {
            i = 0;
        } else {
            i = view.getId() == R.id.phone_button_1 ? 1 : -1;
        }
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
        Integer num;
        this.mCarrierSatelliteOriginalBundle[0] = this.mViewModel.getSatelliteEnabledBundle(0);
        this.mCarrierSatelliteOriginalBundle[1] = this.mViewModel.getSatelliteEnabledBundle(1);
        this.mSatelliteDataOriginalBundle[0] = this.mViewModel.getSatelliteDataModeBundle(0);
        this.mSatelliteDataOriginalBundle[1] = this.mViewModel.getSatelliteDataModeBundle(1);
        Switch r0 = this.mMockSatellite;
        Boolean bool = Boolean.TRUE;
        r0.setChecked(bool.equals(this.mViewModel.getSatelliteEnabled(this.mPhoneId).getValue()));
        boolean zEquals = bool.equals(this.mViewModel.getSatelliteDataEnabled(this.mPhoneId).getValue());
        this.mMockSatelliteDataSwitch.setChecked(zEquals);
        this.mMockSatelliteData.setEnabled(zEquals);
        setDataModeChangeVisibility(zEquals);
        if (zEquals && (num = (Integer) this.mViewModel.getSatelliteDataMode(this.mPhoneId).getValue()) != null && num.intValue() != 0) {
            this.mMockSatelliteData.check(num.intValue());
        }
        boolean zEquals2 = bool.equals(this.mViewModel.getSatelliteForceCamped(this.mPhoneId).getValue());
        this.mEnforceSatelliteChannel.setChecked(zEquals2);
        updateForceCampUI(zEquals2);
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

    boolean shouldHideButton(String str) {
        return !Build.isDebuggable() || TextUtils.isEmpty(str) || this.mContext.getPackageManager().resolveActivity(new Intent(str), 0) == null;
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        updateAllFields();
        log("Started onResume");
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        log("onDestroy");
        super.onDestroy();
    }

    private void updateAllFields() {
        if (this.mPhoneId == -1) {
            this.mPhoneId = 0;
        }
        this.mSubId = SubscriptionManager.getSubscriptionId(this.mPhoneId);
        this.mMockSatellite.setChecked(this.mCarrierSatelliteOriginalBundle[this.mPhoneId] != null);
        this.mMockSatellite.setOnCheckedChangeListener(this.mMockSatelliteListener);
        this.mMockSatelliteDataSwitch.setChecked(this.mSatelliteDataOriginalBundle[this.mPhoneId] != null);
        this.mMockSatelliteDataSwitch.setOnCheckedChangeListener(this.mMockSatelliteDataSwitchListener);
        this.mMockSatelliteData.setOnCheckedChangeListener(this.mMockSatelliteDataListener);
        updateSatelliteChannelDisplay(this.mPhoneId);
        updateSelectionVisuals();
        this.mEnforceSatelliteChannel.setChecked(this.mViewModel.getOriginalSystemChannels(this.mPhoneId) != null);
        this.mEnforceSatelliteChannel.setOnCheckedChangeListener(this.mForceSatelliteChannelOnChangeListener);
        this.mForceCampSatelliteSelectionRadioGroup.setOnCheckedChangeListener(this.mForceCampSatelliteSelectionRadioGroupListener);
        updateForceCampUI(this.mViewModel.getOriginalSystemChannels(this.mPhoneId) != null);
        enableSatelliteBandControls(this.mViewModel.getOriginalSystemChannels(this.mPhoneId) == null);
        this.mManualOverrideBand.setOnItemSelectedListener(this.mManualOverrideBandSelectedListener);
        this.mForceCampSatelliteButton.setOnClickListener(this.mForceCampSatelliteConnectHandler);
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        log("onPause: unregister phone & data intents");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5(CompoundButton compoundButton, boolean z) {
        if (!isValidSubscription(this.mSubId)) {
            loge("Force satellite channel invalid subId " + this.mSubId);
        } else {
            if (PhoneInformationUtil.getCarrierConfig(this.mContext) == null) {
                loge("Force satellite channel cm == null");
                return;
            }
            int i = this.mPhoneId;
            if (z) {
                log("Satellite Force Camp Switch: enabled for phoneId " + i);
                this.mForceCampSatelliteSelectionRadioGroup.clearCheck();
            } else {
                log("Satellite Force Camp Switch: disabled for phoneId " + i);
                clearForcedSatelliteChannel();
            }
            updateForceCampUI(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$6(RadioGroup radioGroup, int i) {
        if (i == R.id.starlink_band) {
            log("Force satellite channel set to STARLINK_BAND");
            setManualOverrideBandVisibility(false);
        } else if (i == R.id.ast_band) {
            log("Force satellite channel set to AST_BAND");
            setManualOverrideBandVisibility(false);
        } else if (i == R.id.manual_override_band) {
            setManualOverrideBandVisibility(true);
            this.mManualOverrideBand.setSelection(this.mViewModel.getSelectedManualOverrideBandIndex(this.mPhoneId));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$7(View view) {
        int[] iArr;
        int[] iArr2 = PhoneInformationUtil.STARLINK_BAND;
        int[] iArr3 = PhoneInformationUtil.STARLINK_CHANNELS;
        int checkedRadioButtonId = this.mForceCampSatelliteSelectionRadioGroup.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.starlink_band) {
            log("Connect start with starlink");
        } else if (checkedRadioButtonId == R.id.ast_band) {
            iArr2 = PhoneInformationUtil.AST_BAND;
            iArr3 = PhoneInformationUtil.AST_CHANNELS;
            log("Connect start with ast");
        } else if (checkedRadioButtonId == R.id.manual_override_band) {
            int iIntValue = PhoneInformationUtil.BAND_VALUES[this.mViewModel.getSelectedManualOverrideBandIndex(this.mPhoneId)].intValue();
            if (iIntValue == -1) {
                loge("Manual override band not selected");
                return;
            }
            int[] iArr4 = {iIntValue};
            try {
                iArr = new int[]{Integer.parseInt(this.mSatelliteChannels.getText().toString())};
            } catch (NumberFormatException unused) {
                log("Could not convert to satellite channel, connecting without channel.");
                iArr = new int[0];
            }
            log("Connect start with manual override band" + Arrays.toString(iArr4) + "and channel " + Arrays.toString(iArr));
            iArr3 = iArr;
            iArr2 = iArr4;
        }
        forceSatelliteChannel(iArr2, checkedRadioButtonId, iArr3);
    }

    private void forceSatelliteChannel(final int[] iArr, final int i, final int[] iArr2) {
        final int i2 = this.mSubId;
        final int i3 = this.mPhoneId;
        final TelephonyManager telephonyManagerCreateForSubscriptionId = this.mTelephonyManager.createForSubscriptionId(i2);
        new Thread(new Runnable() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentSatellite$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$forceSatelliteChannel$9(i2, i3, telephonyManagerCreateForSubscriptionId, iArr, iArr2, i);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$forceSatelliteChannel$9(int i, final int i2, TelephonyManager telephonyManager, int[] iArr, int[] iArr2, int i3) {
        SatelliteController.getInstance().setSatelliteIgnorePlmnListFromStorage(true);
        this.mViewModel.setOriginalSystemChannels(PhoneInformationUtil.getCarrierConfig(this.mContext).getConfigForSubId(i, "satellite_attach_supported_bool", "satellite_entitlement_supported_bool", "emergency_messaging_supported_bool"), i2);
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putBoolean("satellite_attach_supported_bool", true);
        persistableBundle.putBoolean("emergency_messaging_supported_bool", true);
        PhoneInformationUtil.getCarrierConfig(this.mContext).overrideConfig(i, persistableBundle, false);
        try {
            telephonyManager.setAllowedNetworkTypesForReason(4, RadioAccessFamily.getRafFromNetworkType(11));
            log("Force satellite channel set to LTE only");
            List listOf = List.of(new RadioAccessSpecifier(3, iArr, iArr2));
            try {
                log("Force satellite channel new channels " + listOf);
                telephonyManager.setSystemSelectionChannels(listOf);
                PersistableBundle persistableBundle2 = new PersistableBundle();
                persistableBundle2.putIntArray("force_camp_satellite_bands", iArr);
                persistableBundle2.putInt("force_camp_satellite_band_selected", i3);
                persistableBundle2.putIntArray("force_camp_satellite_channels", iArr2);
                this.mViewModel.setPreviousSatelliteBand(persistableBundle2, i2);
                this.mHandler.post(new Runnable() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentSatellite$$ExternalSyntheticLambda19
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$forceSatelliteChannel$8(i2);
                    }
                });
            } catch (Exception e) {
                loge("Force satellite channel failed to set channels " + e);
            }
        } catch (Exception e2) {
            loge("Force satellite channel failed to set network type to LTE " + e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$forceSatelliteChannel$8(int i) {
        if (i != this.mPhoneId) {
            return;
        }
        enableSatelliteBandControls(false);
        this.mViewModel.setSatelliteForceCamped(true, this.mPhoneId);
    }

    private void clearForcedSatelliteChannel() {
        log("clearForcedSatelliteChannel: phone " + this.mPhoneId);
        final TelephonyManager telephonyManagerCreateForSubscriptionId = this.mTelephonyManager.createForSubscriptionId(this.mSubId);
        final int i = this.mSubId;
        final int i2 = this.mPhoneId;
        new Thread(new Runnable() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentSatellite$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$clearForcedSatelliteChannel$11(telephonyManagerCreateForSubscriptionId, i2, i);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearForcedSatelliteChannel$11(TelephonyManager telephonyManager, final int i, int i2) {
        try {
            SatelliteController.getInstance().setSatelliteIgnorePlmnListFromStorage(false);
            telephonyManager.setSystemSelectionChannels(Collections.EMPTY_LIST);
            log("Force satellite channel successfully cleared channels");
            telephonyManager.setAllowedNetworkTypesForReason(4, TelephonyManager.getAllNetworkTypesBitmask());
            log("Force satellite channel successfully reset network type to " + TelephonyManager.getAllNetworkTypesBitmask());
            PersistableBundle originalSystemChannels = this.mViewModel.getOriginalSystemChannels(i);
            if (originalSystemChannels != null) {
                PhoneInformationUtil.getCarrierConfig(this.mContext).overrideConfig(i2, originalSystemChannels, false);
                log("Force satellite channel successfully restored config to " + originalSystemChannels);
                this.mViewModel.setOriginalSystemChannels(null, i);
            }
            this.mViewModel.setPreviousSatelliteBand(null, i);
        } catch (Exception e) {
            loge("Force satellite channel: Can't clear mock " + e);
        }
        this.mHandler.post(new Runnable() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentSatellite$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$clearForcedSatelliteChannel$10(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearForcedSatelliteChannel$10(int i) {
        if (i != this.mPhoneId) {
            return;
        }
        enableSatelliteBandControls(true);
        this.mViewModel.setSelectedManualOverrideBandIndex(0, this.mPhoneId);
        this.mViewModel.setPreviousSatelliteBand(null, i);
        this.mViewModel.setSatelliteForceCamped(false, this.mPhoneId);
        this.mSatelliteChannels.setText("");
    }

    private void updateForceCampUI(boolean z) {
        int i;
        log("updateForceCampUI: showSatelliteBandOptions " + z);
        int i2 = R.id.starlink_band;
        PersistableBundle previousSatelliteBand = this.mViewModel.getPreviousSatelliteBand(this.mPhoneId);
        if (previousSatelliteBand != null) {
            i2 = previousSatelliteBand.getInt("force_camp_satellite_band_selected");
            int[] intArray = previousSatelliteBand.getIntArray("force_camp_satellite_channels");
            if (i2 == R.id.manual_override_band) {
                setManualOverrideBandVisibility(true);
                this.mManualOverrideBand.setSelection(this.mViewModel.getSelectedManualOverrideBandIndex(this.mPhoneId));
                if (intArray != null && intArray.length > 0 && (i = intArray[0]) != -1) {
                    this.mSatelliteChannels.setText(String.valueOf(i));
                }
            }
        }
        setForceCampSatelliteSelectionRadioGroupVisibility(z);
        this.mForceCampSatelliteSelectionRadioGroup.check(i2);
    }

    private void setForceCampSatelliteSelectionRadioGroupVisibility(boolean z) {
        log("setForceCampSatelliteSelectionRadioGroupVisibility: isChecked " + z);
        RadioGroup radioGroup = this.mForceCampSatelliteSelectionRadioGroup;
        if (z) {
            radioGroup.setVisibility(0);
            this.mForceCampSatelliteButton.setVisibility(0);
        } else {
            radioGroup.setVisibility(8);
            this.mForceCampSatelliteButton.setVisibility(8);
            setManualOverrideBandVisibility(false);
        }
    }

    private void setManualOverrideBandVisibility(boolean z) {
        log("setManualOverrideBandVisibility: visible " + z);
        View view = getView();
        if (view != null) {
            view.findViewById(R.id.manual_override_fields).setVisibility(z ? 0 : 8);
        }
    }

    private void enableSatelliteBandControls(boolean z) {
        log("enableSatelliteBandControls: enable " + z);
        this.mForceCampSatelliteButton.setEnabled(z);
        this.mSatelliteChannels.setEnabled(z);
        this.mManualOverrideBand.setEnabled(z);
        this.mForceCampSatelliteSelectionRadioGroup.setEnabled(z);
        for (int i = 0; i < this.mForceCampSatelliteSelectionRadioGroup.getChildCount(); i++) {
            this.mForceCampSatelliteSelectionRadioGroup.getChildAt(i).setEnabled(z);
        }
        Button button = this.mForceCampSatelliteButton;
        if (!z) {
            button.setText(R.string.force_camp_satellite_button_connected_string);
        } else {
            button.setText(R.string.force_camp_satellite_button_string);
        }
    }

    private boolean isValidSubscription(int i) {
        boolean zIsActiveSubscriptionId = SubscriptionManager.isValidSubscriptionId(i) ? ((SubscriptionManager) this.mContext.getSystemService(SubscriptionManager.class)).isActiveSubscriptionId(i) : false;
        log("isValidSubscription, subId [ " + i + " ] = " + zIsActiveSubscriptionId);
        return zIsActiveSubscriptionId;
    }

    private void updateSatelliteChannelDisplay(final int i) {
        log("updateSatelliteChannelDisplay: phoneId " + i);
        if (this.mEnforceSatelliteChannel.isChecked()) {
            return;
        }
        final int subscriptionId = SubscriptionManager.getSubscriptionId(i);
        if (!isValidSubscription(subscriptionId)) {
            log("updateSatelliteChannelDisplay: invalid subId for phoneId " + i);
            return;
        }
        new Thread(new Runnable() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentSatellite$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateSatelliteChannelDisplay$16(subscriptionId, i);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSatelliteChannelDisplay$16(int i, final int i2) {
        int i3;
        TelephonyManager telephonyManagerCreateForSubscriptionId = this.mTelephonyManager.createForSubscriptionId(i);
        try {
            final List systemSelectionChannels = telephonyManagerCreateForSubscriptionId.getSystemSelectionChannels();
            final long allowedNetworkTypesForReason = telephonyManagerCreateForSubscriptionId.getAllowedNetworkTypesForReason(4);
            final long rafFromNetworkType = RadioAccessFamily.getRafFromNetworkType(11);
            if (systemSelectionChannels.size() != 1) {
                return;
            }
            RadioAccessSpecifier radioAccessSpecifier = (RadioAccessSpecifier) systemSelectionChannels.getFirst();
            if (radioAccessSpecifier.getRadioAccessNetwork() != 3 && (allowedNetworkTypesForReason & rafFromNetworkType) != allowedNetworkTypesForReason) {
                this.mHandler.post(new Runnable() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentSatellite$$ExternalSyntheticLambda14
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$updateSatelliteChannelDisplay$12(i2, systemSelectionChannels, allowedNetworkTypesForReason, rafFromNetworkType);
                    }
                });
                return;
            }
            int[] bands = radioAccessSpecifier.getBands();
            int[] channels = radioAccessSpecifier.getChannels();
            boolean zAnyMatch = Arrays.stream(channels).anyMatch(new IntPredicate() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentSatellite$$ExternalSyntheticLambda15
                @Override // java.util.function.IntPredicate
                public final boolean test(int i4) {
                    return PhoneInformationV2FragmentSatellite.$r8$lambda$SsKm4uru4hiq8857O6qDnZcjMhs(i4);
                }
            });
            boolean zAnyMatch2 = Arrays.stream(channels).anyMatch(new IntPredicate() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentSatellite$$ExternalSyntheticLambda16
                @Override // java.util.function.IntPredicate
                public final boolean test(int i4) {
                    return PhoneInformationV2FragmentSatellite.m3578$r8$lambda$7qSw2W8M7qZPT70ppFRA57DSOo(i4);
                }
            });
            if (!zAnyMatch) {
                if (zAnyMatch2) {
                    i3 = R.id.ast_band;
                } else {
                    i3 = R.id.manual_override_band;
                    this.mViewModel.setSelectedManualOverrideBandIndex(0, i2);
                    if (bands.length > 0) {
                        int i4 = bands[0];
                        int i5 = 0;
                        while (true) {
                            Integer[] numArr = PhoneInformationUtil.BAND_VALUES;
                            if (i5 >= numArr.length) {
                                break;
                            }
                            if (i4 == numArr[i5].intValue()) {
                                this.mViewModel.setSelectedManualOverrideBandIndex(0, i2);
                            }
                            i5++;
                        }
                    }
                }
            } else {
                i3 = R.id.starlink_band;
            }
            PersistableBundle persistableBundle = new PersistableBundle();
            persistableBundle.putIntArray("force_camp_satellite_bands", bands);
            persistableBundle.putInt("force_camp_satellite_band_selected", i3);
            persistableBundle.putIntArray("force_camp_satellite_channels", channels);
            this.mViewModel.setPreviousSatelliteBand(persistableBundle, i2);
            this.mHandler.post(new Runnable() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentSatellite$$ExternalSyntheticLambda17
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$updateSatelliteChannelDisplay$15(i2, systemSelectionChannels, allowedNetworkTypesForReason, rafFromNetworkType);
                }
            });
        } catch (Exception e) {
            loge("updateSatelliteChannelDisplay " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSatelliteChannelDisplay$12(int i, List list, long j, long j2) {
        if (i != this.mPhoneId) {
            return;
        }
        log("Force satellite get channel " + list + " get networkTypeBitMask " + j + " lte " + j2);
        this.mEnforceSatelliteChannel.setChecked(false);
    }

    public static /* synthetic */ boolean $r8$lambda$SsKm4uru4hiq8857O6qDnZcjMhs(int i) {
        for (int i2 : PhoneInformationUtil.STARLINK_CHANNELS) {
            if (i == i2) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: renamed from: $r8$lambda$7qSw2W8M7qZPT-70ppFRA57DSOo, reason: not valid java name */
    public static /* synthetic */ boolean m3578$r8$lambda$7qSw2W8M7qZPT70ppFRA57DSOo(int i) {
        for (int i2 : PhoneInformationUtil.AST_CHANNELS) {
            if (i == i2) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSatelliteChannelDisplay$15(int i, List list, long j, long j2) {
        if (i != this.mPhoneId) {
            return;
        }
        log("Force satellite get channel " + list + " get networkTypeBitMask " + j + " lte " + j2);
        this.mEnforceSatelliteChannel.setChecked(true);
        enableSatelliteBandControls(false);
    }

    private void updateSatelliteDataButton() {
        int i;
        if (this.mSatelliteDataOriginalBundle[this.mPhoneId] == null) {
            PersistableBundle configForSubId = PhoneInformationUtil.getCarrierConfig(this.mContext).getConfigForSubId(this.mSubId, "satellite_data_support_mode_int", "satellite_entitlement_supported_bool", "carrier_roaming_satellite_default_services_int_array", "carrier_supported_satellite_services_per_provider_bundle");
            PersistableBundle[] persistableBundleArr = this.mSatelliteDataOriginalBundle;
            int i2 = this.mPhoneId;
            persistableBundleArr[i2] = configForSubId;
            this.mViewModel.setSatelliteDataModeBundle(configForSubId, i2);
            log("satData: OriginalConfig = " + configForSubId);
        }
        PersistableBundle configForSubId2 = PhoneInformationUtil.getCarrierConfig(this.mContext).getConfigForSubId(this.mSubId, "satellite_data_support_mode_int", "carrier_roaming_satellite_default_services_int_array", "carrier_supported_satellite_services_per_provider_bundle");
        int i3 = configForSubId2.getInt("satellite_data_support_mode_int", -1);
        log("satData: present dataMode = " + i3);
        if (i3 != -1) {
            if (i3 == 0) {
                i = R.id.satellite_data_restricted;
            } else if (i3 == 1) {
                i = R.id.satellite_data_constrained;
            } else {
                i = i3 != 2 ? 0 : R.id.satellite_data_unConstrained;
            }
            this.mMockSatelliteData.check(i);
        }
        PhoneInformationUtil.updateCarrierConfigToSupportData(PhoneInformationUtil.getCarrierConfig(this.mContext), this.mSubId, configForSubId2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:4:0x000c  */
    public /* synthetic */ void lambda$new$17(RadioGroup radioGroup, int i) {
        int i2;
        this.mViewModel.setSatelliteDataMode(i, this.mPhoneId);
        if (i == R.id.satellite_data_restricted) {
            i2 = 0;
        } else if (i == R.id.satellite_data_constrained) {
            i2 = 1;
        } else if (i == R.id.satellite_data_unConstrained) {
            i2 = 2;
        } else {
            i2 = 0;
        }
        log("satData: OnCheckedChangeListener setting dataMode = " + i2);
        if (PhoneInformationUtil.getCarrierConfig(this.mContext) == null) {
            return;
        }
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putInt("satellite_data_support_mode_int", i2);
        persistableBundle.putBoolean("satellite_entitlement_supported_bool", false);
        if (isValidSubscription(this.mSubId)) {
            PhoneInformationUtil.getCarrierConfig(this.mContext).overrideConfig(this.mSubId, persistableBundle, false);
            log("satData: mMockSatelliteDataListener: Updated new config" + persistableBundle);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$18(CompoundButton compoundButton, boolean z) {
        log("satData: ServiceData enabling = " + z);
        this.mViewModel.setSatelliteDataModeEnabled(z, this.mPhoneId);
        if (z) {
            if (isValidOperator(this.mSubId)) {
                log("satData: Uncapping maxAllowedDataMode");
                PhoneInformationUtil.uncapMaxAllowedDataMode();
                updateSatelliteDataButton();
            } else {
                log("satData: Not a valid Operator");
                this.mMockSatelliteDataSwitch.setChecked(false);
                return;
            }
        } else {
            log("satData: restoring maxAllowedDataMode");
            PhoneInformationUtil.restoreMaxAllowedDataMode();
            reloadCarrierConfigDefaults();
        }
        setDataModeChangeVisibility(z);
    }

    private void setDataModeChangeVisibility(boolean z) {
        RadioGroup radioGroup = this.mMockSatelliteData;
        if (z) {
            radioGroup.setVisibility(0);
        } else {
            radioGroup.setVisibility(8);
        }
    }

    private void reloadCarrierConfigDefaults() {
        if (this.mSatelliteDataOriginalBundle[this.mPhoneId] != null) {
            log("satData: Setting originalCarrierConfig = " + this.mSatelliteDataOriginalBundle[this.mPhoneId]);
            PhoneInformationUtil.getCarrierConfig(this.mContext).overrideConfig(this.mSubId, this.mSatelliteDataOriginalBundle[this.mPhoneId], false);
            PersistableBundle[] persistableBundleArr = this.mSatelliteDataOriginalBundle;
            int i = this.mPhoneId;
            persistableBundleArr[i] = null;
            this.mViewModel.setSatelliteDataModeBundle(null, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$19(CompoundButton compoundButton, boolean z) {
        int i = this.mSubId;
        int i2 = this.mPhoneId;
        if (SubscriptionManager.isValidPhoneId(i2) && isValidSubscription(i)) {
            if (PhoneInformationUtil.getCarrierConfig(this.mContext) == null) {
                log("mMockSatelliteListener: Carrier config is null");
                return;
            }
            this.mViewModel.setSatelliteEnabled(z, i2);
            if (!z) {
                try {
                    PhoneInformationUtil.restoreOriginalApns(this.mContext, this.mOriginalApnSettings, "PhoneInformationV2 Satellite");
                    this.mOriginalApnSettings.clear();
                    PhoneInformationUtil.getCarrierConfig(this.mContext).overrideConfig(i, this.mCarrierSatelliteOriginalBundle[i2], false);
                    this.mCarrierSatelliteOriginalBundle[i2] = null;
                    this.mViewModel.setSatelliteEnabledBundle(null, i2);
                    SatelliteController.getInstance().setSatelliteIgnorePlmnListFromStorage(false);
                    log("mMockSatelliteListener: Successfully cleared mock for phone " + i2);
                    SatelliteController.getInstance().setSatelliteIgnorePlmnListFromStorage(false);
                    return;
                } catch (Exception unused) {
                    loge("mMockSatelliteListener: Can't clear mock because invalid sub Id " + i + ", insert SIM and use adb shell cmd phone cc clear-values");
                    this.mMockSatellite.setChecked(true);
                    return;
                }
            }
            if (!isValidOperator(i)) {
                this.mMockSatellite.setChecked(false);
                loge("mMockSatelliteListener: Can't mock because no operator for phone " + i2);
                return;
            }
            PersistableBundle configForSubId = PhoneInformationUtil.getCarrierConfig(this.mContext).getConfigForSubId(i, "satellite_attach_supported_bool", "satellite_entitlement_supported_bool", "carrier_supported_satellite_services_per_provider_bundle");
            this.mCarrierSatelliteOriginalBundle[i2] = configForSubId;
            this.mViewModel.setSatelliteEnabledBundle(configForSubId, i2);
            this.mOriginalApnSettings.clear();
            this.mOriginalApnSettings.addAll(PhoneInformationUtil.updateApnInfrastructureBitmaskForSatellite(this.mContext, i, "PhoneInformationV2 Satellite"));
            PersistableBundle persistableBundle = new PersistableBundle();
            persistableBundle.putBoolean("satellite_attach_supported_bool", true);
            persistableBundle.putBoolean("satellite_entitlement_supported_bool", false);
            persistableBundle.putPersistableBundle("carrier_supported_satellite_services_per_provider_bundle", PhoneInformationUtil.getSatelliteServicesBundleForOperatorPlmn(this.mTelephonyManager, this.mPhoneId, this.mSubId, configForSubId));
            SatelliteController.getInstance().setSatelliteIgnorePlmnListFromStorage(true);
            log("mMockSatelliteListener: old " + configForSubId);
            log("mMockSatelliteListener: new " + persistableBundle);
            SatelliteController.getInstance().setSatelliteIgnorePlmnListFromStorage(true);
            PhoneInformationUtil.getCarrierConfig(this.mContext).overrideConfig(i, persistableBundle, false);
        }
    }

    private boolean isValidOperator(int i) {
        String networkOperatorForPhone;
        TelephonyManager telephonyManager;
        if (isValidSubscription(i)) {
            networkOperatorForPhone = this.mTelephonyManager.getNetworkOperatorForPhone(this.mPhoneId);
            if (TextUtils.isEmpty(networkOperatorForPhone) && (telephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)) != null) {
                networkOperatorForPhone = telephonyManager.getSimOperatorNumericForPhone(this.mPhoneId);
            }
        } else {
            networkOperatorForPhone = null;
        }
        return !TextUtils.isEmpty(networkOperatorForPhone);
    }

    private void updatePhoneIndex() {
        log("updatePhoneIndex");
        this.mTelephonyManager = this.mTelephonyManager.createForSubscriptionId(this.mSubId);
        if (this.mSystemUser) {
            this.mPhone = PhoneFactory.getPhone(this.mPhoneId);
        }
        updateAllFields();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("PhoneInformationV2 Satellite", str);
    }

    private static void loge(String str) {
        Log.e("PhoneInformationV2 Satellite", str);
    }
}
