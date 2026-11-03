package com.android.phone.settings.hiddenmenu;

import android.content.Context;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Process;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.ims.ImsManager;
import android.telephony.ims.ImsMmTelManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.R;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public class PhoneInformationV2FragmentIms extends Fragment {
    private static String[] sPhoneIndexLabels = new String[0];
    private PhoneInformationV2PhoneId listener;
    private Context mContext;
    private TextView mImsRegistration;
    private TelephonyManager mTelephonyManager;
    private TextView mUtInterface;
    private TextView mVideoCalling;
    private TextView mVoiceOverLte;
    private TextView mVoiceOverWiFi;
    private LinearLayout phoneButton0;
    private LinearLayout phoneButton1;
    private TextView phoneTitle0;
    private TextView phoneTitle1;
    private boolean mSystemUser = true;
    private Phone mPhone = null;
    private ImsManager mImsManager = null;
    private int mPhoneId = -1;
    private int mSubId = -1;
    private final String REGISTERED = "Registered";
    private final String NOT_REGISTERED = "Not Registered";
    private final String AVAILABLE = "Available";
    private final String UNAVAILABLE = "Unavailable";

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PhoneInformationV2PhoneId) {
            this.listener = (PhoneInformationV2PhoneId) context;
        } else {
            PhoneInformationV2FragmentIms$$ExternalSyntheticBUOutline0.m(context);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        log("onCreate");
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.phone_information_v2_tab_ims, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        log("on View Created");
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
        PhoneInformationV2PhoneId phoneInformationV2PhoneId = this.listener;
        if (phoneInformationV2PhoneId != null) {
            int phoneId = phoneInformationV2PhoneId.getPhoneId();
            this.mPhoneId = phoneId;
            this.mSubId = SubscriptionManager.getSubscriptionId(phoneId);
        }
        TelephonyManager telephonyManagerCreateForSubscriptionId = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(this.mSubId);
        this.mTelephonyManager = telephonyManagerCreateForSubscriptionId;
        sPhoneIndexLabels = PhoneInformationUtil.getPhoneIndexLabels(telephonyManagerCreateForSubscriptionId);
        this.phoneButton0 = (LinearLayout) view.findViewById(R.id.phone_button_0);
        this.phoneTitle0 = (TextView) view.findViewById(R.id.phone_button_0_title);
        this.phoneButton1 = (LinearLayout) view.findViewById(R.id.phone_button_1);
        TextView textView = (TextView) view.findViewById(R.id.phone_button_1_title);
        this.phoneTitle1 = textView;
        PhoneInformationUtil.configurePhoneSelectionUi(this.phoneButton0, this.phoneButton1, this.phoneTitle0, textView, sPhoneIndexLabels);
        this.mImsManager = new ImsManager(this.mContext);
        this.mImsRegistration = (TextView) view.findViewById(R.id.ims_registration);
        this.mVoiceOverLte = (TextView) view.findViewById(R.id.voice_over_lte);
        this.mVoiceOverWiFi = (TextView) view.findViewById(R.id.voice_over_wifi);
        this.mVideoCalling = (TextView) view.findViewById(R.id.video_calling);
        this.mUtInterface = (TextView) view.findViewById(R.id.ut_interface);
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentIms$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                this.f$0.lambda$onViewCreated$0(view2);
            }
        };
        this.phoneButton0.setOnClickListener(onClickListener);
        this.phoneButton1.setOnClickListener(onClickListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$0(View view) {
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
                    this.listener.setPhoneId(i);
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

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        log("Started onResume");
        updateAllFields();
    }

    private void updatePhoneIndex() {
        log("updatePhoneIndex");
        this.mTelephonyManager = this.mTelephonyManager.createForSubscriptionId(this.mSubId);
        if (this.mSystemUser) {
            this.mPhone = PhoneFactory.getPhone(this.mPhoneId);
        }
        this.mImsManager = new ImsManager(this.mContext);
        updateAllFields();
    }

    private void updateSelectionVisuals() {
        LinearLayout linearLayout;
        LinearLayout linearLayout2;
        if (this.mPhoneId == 0) {
            linearLayout = this.phoneButton0;
            linearLayout2 = this.phoneButton1;
        } else {
            linearLayout = this.phoneButton1;
            linearLayout2 = this.phoneButton0;
        }
        linearLayout.setBackgroundColor(ContextCompat.getColor(this.mContext, android.R.color.holo_green_dark));
        linearLayout2.setBackgroundColor(ContextCompat.getColor(this.mContext, android.R.color.darker_gray));
    }

    private void updateAllFields() {
        boolean zIsVideoServiceAvailable;
        boolean zIsWfcServiceAvailable;
        boolean zIsValidSubscriptionId = SubscriptionManager.isValidSubscriptionId(this.mSubId);
        boolean zIsVoiceServiceAvailable = false;
        boolean z = zIsValidSubscriptionId && this.mTelephonyManager.isImsRegistered();
        final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        if (zIsValidSubscriptionId) {
            ImsMmTelManager imsMmTelManager = this.mImsManager.getImsMmTelManager(this.mSubId);
            zIsVoiceServiceAvailable = PhoneInformationUtil.isVoiceServiceAvailable(imsMmTelManager);
            zIsVideoServiceAvailable = PhoneInformationUtil.isVideoServiceAvailable(imsMmTelManager);
            zIsWfcServiceAvailable = PhoneInformationUtil.isWfcServiceAvailable(imsMmTelManager);
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            try {
                HandlerThread handlerThread = new HandlerThread("PhoneInfoIms");
                handlerThread.start();
                imsMmTelManager.isSupported(4, 1, handlerThread.getThreadExecutor(), new Consumer() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2FragmentIms$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        PhoneInformationV2FragmentIms.m3576$r8$lambda$b16nV5eDeKijgaiIL6CY7GyorM(countDownLatch, atomicBoolean, (Boolean) obj);
                    }
                });
                countDownLatch.await(2L, TimeUnit.SECONDS);
                handlerThread.quit();
            } catch (Exception unused) {
                log("Failed to get UT state.");
            }
        } else {
            zIsVideoServiceAvailable = false;
            zIsWfcServiceAvailable = false;
        }
        updateSelectionVisuals();
        this.mImsRegistration.setText(z ? "Registered" : "Not Registered");
        this.mVoiceOverLte.setText(zIsVoiceServiceAvailable ? "Available" : "Unavailable");
        this.mVoiceOverWiFi.setText(zIsWfcServiceAvailable ? "Available" : "Unavailable");
        this.mVideoCalling.setText(zIsVideoServiceAvailable ? "Available" : "Unavailable");
        this.mUtInterface.setText(atomicBoolean.get() ? "Available" : "Unavailable");
    }

    /* JADX INFO: renamed from: $r8$lambda$b16nV5eDeKijgaiIL6C-Y7GyorM, reason: not valid java name */
    public static /* synthetic */ void m3576$r8$lambda$b16nV5eDeKijgaiIL6CY7GyorM(CountDownLatch countDownLatch, AtomicBoolean atomicBoolean, Boolean bool) {
        countDownLatch.countDown();
        atomicBoolean.set(bool.booleanValue());
    }

    private static void log(String str) {
        Log.d("PhoneInformationV2 Ims", str);
    }
}
