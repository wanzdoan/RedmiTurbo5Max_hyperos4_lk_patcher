package com.android.phone.settings.hiddenmenu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.euicc.EuiccConnector;
import com.android.phone.R;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class PhoneInformationV2FragmentDeviceDetails extends Fragment {
    private static final boolean IS_USER_BUILD = "user".equals(Build.TYPE);
    private static String[] sPhoneIndexLabels = new String[0];
    private TextView mCarrierId;
    private Context mContext;
    private TextView mDds;
    private TextView mDeviceId;
    private Switch mDsdsSwitch;
    private TextView mGid1;
    private TextView mLine1Number;
    private PhoneInformationV2PhoneId mListener;
    private LinearLayout mPhoneButton0;
    private LinearLayout mPhoneButton1;
    private TextView mPhoneTitle0;
    private TextView mPhoneTitle1;
    private Switch mRemovableEsimSwitch;
    private TextView mSubscriberId;
    private TextView mSubscriptionId;
    private TelephonyManager mTelephonyManager;
    private Phone mPhone = null;
    private boolean mSystemUser = true;
    private int mPhoneId = -1;
    private int mSubId = -1;
    DialogInterface.OnClickListener mOnDsdsDialogConfirmedListener = new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDeviceDetails.1
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == -1) {
                PhoneInformationV2FragmentDeviceDetails.this.mDsdsSwitch.toggle();
                PhoneInformationV2FragmentDeviceDetails.this.performDsdsSwitch();
            }
        }
    };
    CompoundButton.OnCheckedChangeListener mRemovableEsimChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDeviceDetails$$ExternalSyntheticLambda0
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            this.f$0.lambda$new$2(compoundButton, z);
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
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.phone_information_v2_tab_device_details, viewGroup, false);
    }

    private static void log(String str) {
        Log.d("PhoneInformationV2 DeviceDetails", str);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        log("Started onResume");
        updateAllFields();
    }

    private void updateAllFields() {
        updateProperties();
        updateSelectionVisuals();
        updateSubscriptionIds();
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        log("onCreate");
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        log("onViewCreated");
        this.mContext = requireContext();
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
        TelephonyManager telephonyManagerCreateForSubscriptionId = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(this.mSubId);
        this.mTelephonyManager = telephonyManagerCreateForSubscriptionId;
        sPhoneIndexLabels = PhoneInformationUtil.getPhoneIndexLabels(telephonyManagerCreateForSubscriptionId);
        this.mPhoneButton0 = (LinearLayout) view.findViewById(R.id.phone_button_0);
        this.mPhoneTitle0 = (TextView) view.findViewById(R.id.phone_button_0_title);
        this.mPhoneButton1 = (LinearLayout) view.findViewById(R.id.phone_button_1);
        TextView textView = (TextView) view.findViewById(R.id.phone_button_1_title);
        this.mPhoneTitle1 = textView;
        PhoneInformationUtil.configurePhoneSelectionUi(this.mPhoneButton0, this.mPhoneButton1, this.mPhoneTitle0, textView, sPhoneIndexLabels);
        this.mDeviceId = (TextView) view.findViewById(R.id.imei);
        this.mLine1Number = (TextView) view.findViewById(R.id.number);
        this.mSubscriptionId = (TextView) view.findViewById(R.id.subid);
        this.mDds = (TextView) view.findViewById(R.id.dds);
        this.mSubscriberId = (TextView) view.findViewById(R.id.imsi);
        this.mGid1 = (TextView) view.findViewById(R.id.gid1);
        this.mCarrierId = (TextView) view.findViewById(R.id.carrier_id);
        Switch r7 = (Switch) view.findViewById(R.id.removable_esim_switch);
        this.mRemovableEsimSwitch = r7;
        if (!IS_USER_BUILD) {
            r7.setEnabled(true);
            this.mRemovableEsimSwitch.setChecked(this.mTelephonyManager.isRemovableEsimDefaultEuicc());
            this.mRemovableEsimSwitch.setOnCheckedChangeListener(this.mRemovableEsimChangeListener);
        }
        this.mDsdsSwitch = (Switch) view.findViewById(R.id.dsds_switch);
        if (PhoneInformationUtil.isDsdsSupported() && !PhoneInformationUtil.dsdsModeOnly()) {
            this.mDsdsSwitch.setVisibility(0);
            this.mDsdsSwitch.setOnClickListener(new View.OnClickListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDeviceDetails$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    this.f$0.lambda$onViewCreated$0(view2);
                }
            });
            this.mDsdsSwitch.setChecked(PhoneInformationUtil.isDsdsEnabled());
        } else {
            this.mDsdsSwitch.setVisibility(8);
        }
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentDeviceDetails$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                this.f$0.lambda$onViewCreated$1(view2);
            }
        };
        this.mPhoneButton0.setOnClickListener(onClickListener);
        this.mPhoneButton1.setOnClickListener(onClickListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$0(View view) {
        if (this.mTelephonyManager.doesSwitchMultiSimConfigTriggerReboot()) {
            this.mDsdsSwitch.toggle();
            showDsdsChangeDialog();
        } else {
            performDsdsSwitch();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$1(View view) {
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

    private void updatePhoneIndex() {
        log("updatePhoneIndex");
        this.mTelephonyManager = this.mTelephonyManager.createForSubscriptionId(this.mSubId);
        if (this.mSystemUser) {
            this.mPhone = PhoneFactory.getPhone(this.mPhoneId);
        }
        updateAllFields();
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

    private void showDsdsChangeDialog() {
        new AlertDialog.Builder(this.mContext).setTitle(R.string.dsds_dialog_title).setMessage(R.string.dsds_dialog_message).setPositiveButton(R.string.dsds_dialog_confirm, this.mOnDsdsDialogConfirmedListener).setNegativeButton(R.string.dsds_dialog_cancel, this.mOnDsdsDialogConfirmedListener).create().show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void performDsdsSwitch() {
        this.mTelephonyManager.switchMultiSimConfig(this.mDsdsSwitch.isChecked() ? 2 : 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(CompoundButton compoundButton, boolean z) {
        setRemovableEsimAsDefaultEuicc(z);
    }

    private void setRemovableEsimAsDefaultEuicc(boolean z) {
        Log.d("PhoneInformationV2 DeviceDetails", "setRemovableEsimAsDefaultEuicc isChecked: " + z);
        this.mTelephonyManager.setRemovableEsimAsDefaultEuicc(z);
        ComponentInfo componentInfoFindBestComponent = EuiccConnector.findBestComponent(this.mContext.getPackageManager());
        if (componentInfoFindBestComponent == null) {
            Log.d("PhoneInformationV2 DeviceDetails", "setRemovableEsimAsDefaultEuicc: unable to find suitable component info");
            return;
        }
        Intent intent = new Intent("android.telephony.euicc.action.REMOVABLE_ESIM_AS_DEFAULT");
        intent.setPackage(componentInfoFindBestComponent.packageName);
        intent.putExtra("isDefault", z);
        this.mContext.sendBroadcast(intent);
    }

    private void updateProperties() {
        Resources resources = getResources();
        TelephonyManager telephonyManagerCreateForSubscriptionId = this.mTelephonyManager.createForSubscriptionId(this.mSubId);
        this.mDeviceId.setText(this.mTelephonyManager.getImei(this.mPhoneId));
        this.mSubscriberId.setText(PhoneInformationUtil.getSubscriberId(telephonyManagerCreateForSubscriptionId, resources));
        this.mGid1.setText(PhoneInformationUtil.getGid1(telephonyManagerCreateForSubscriptionId, resources));
        this.mCarrierId.setText(PhoneInformationUtil.getCarrierIdString(telephonyManagerCreateForSubscriptionId, resources));
        SubscriptionManager subscriptionManager = (SubscriptionManager) this.mContext.getSystemService(SubscriptionManager.class);
        int i = this.mSubId;
        this.mLine1Number.setText(((subscriptionManager.getPhoneNumber(i) + " { CARRIER:" + subscriptionManager.getPhoneNumber(i, 2) + ", UICC:" + subscriptionManager.getPhoneNumber(i, 1) + ", IMS:" + subscriptionManager.getPhoneNumber(i, 3)) + ", TS43:" + subscriptionManager.getPhoneNumber(i, 4)).concat(" }"));
    }

    private void updateSubscriptionIds() {
        this.mSubscriptionId.setText(String.format(Locale.ROOT, "%d", Integer.valueOf(this.mSubId)));
        this.mDds.setText(Integer.toString(SubscriptionManager.getDefaultDataSubscriptionId()));
    }
}
