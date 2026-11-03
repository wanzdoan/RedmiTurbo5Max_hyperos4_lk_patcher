package com.android.internal.telephony.security;

import android.R;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.safetycenter.SafetyCenterManager;
import android.safetycenter.SafetyEvent;
import android.safetycenter.SafetySourceData;
import android.safetycenter.SafetySourceIssue;
import android.safetycenter.SafetySourceStatus;
import android.telephony.CellularIdentifierDisclosure;
import android.text.TextUtils;
import android.text.format.DateFormat;
import com.android.internal.telephony.CallWaitingController;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import java.time.Instant;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Stream;

/* JADX INFO: loaded from: classes.dex */
public class CellularNetworkSecuritySafetySource {
    private static final Intent CELLULAR_NETWORK_SECURITY_SETTINGS_INTENT = new Intent("android.settings.CELLULAR_NETWORK_SECURITY");
    private static CellularNetworkSecuritySafetySource sInstance;
    private boolean mIdentifierDisclosureIssuesEnabled;
    private boolean mNullCipherStateIssuesEnabled;
    private final SafetyCenterManagerWrapper mSafetyCenterManagerWrapper;
    private HashMap mNullCipherStates = new HashMap();
    private HashMap mIdentifierDisclosures = new HashMap();
    private final BroadcastReceiver mReceiver = new CellularNetworkSecurityBroadcastReceiver();
    private final SubscriptionManagerService mSubscriptionManagerService = SubscriptionManagerService.getInstance();

    public static synchronized CellularNetworkSecuritySafetySource getInstance(Context context) {
        try {
            if (sInstance == null) {
                sInstance = new CellularNetworkSecuritySafetySource(new SafetyCenterManagerWrapper(context));
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    public CellularNetworkSecuritySafetySource(SafetyCenterManagerWrapper safetyCenterManagerWrapper) {
        this.mSafetyCenterManagerWrapper = safetyCenterManagerWrapper;
    }

    public synchronized void setNullCipherIssueEnabled(Context context, boolean z) {
        try {
            if (z != this.mNullCipherStateIssuesEnabled) {
                this.mNullCipherStateIssuesEnabled = z;
                this.mNullCipherStates.clear();
                updateSafetyCenter(context);
                if (z) {
                    context.registerReceiver(this.mReceiver, new IntentFilter("android.intent.action.AIRPLANE_MODE"));
                } else {
                    context.unregisterReceiver(this.mReceiver);
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void setNullCipherState(Context context, int i, int i2) {
        this.mNullCipherStates.put(Integer.valueOf(i), Integer.valueOf(i2));
        updateSafetyCenter(context);
    }

    public synchronized void clearNullCipherState(Context context, int i) {
        this.mNullCipherStates.remove(Integer.valueOf(i));
        updateSafetyCenter(context);
    }

    public synchronized void setIdentifierDisclosureIssueEnabled(Context context, boolean z) {
        if (z != this.mIdentifierDisclosureIssuesEnabled) {
            this.mIdentifierDisclosureIssuesEnabled = z;
            this.mIdentifierDisclosures.clear();
            updateSafetyCenter(context);
        }
    }

    public synchronized void setIdentifierDisclosure(Context context, int i, CellularIdentifierDisclosure cellularIdentifierDisclosure, int i2, Instant instant, Instant instant2) {
        this.mIdentifierDisclosures.put(Integer.valueOf(i), new IdentifierDisclosure(i2, instant, instant2, cellularIdentifierDisclosure));
        updateSafetyCenter(context);
    }

    public synchronized void clearIdentifierDisclosure(Context context, int i) {
        this.mIdentifierDisclosures.remove(Integer.valueOf(i));
        updateSafetyCenter(context);
    }

    public synchronized void refresh(Context context, String str) {
        this.mSafetyCenterManagerWrapper.setRefreshedSafetySourceData(str, getSafetySourceData(context));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSafetyCenter(Context context) {
        this.mSafetyCenterManagerWrapper.setSafetySourceData(getSafetySourceData(context));
    }

    private boolean isSafetySourceHidden() {
        return (this.mNullCipherStateIssuesEnabled || this.mIdentifierDisclosureIssuesEnabled) ? false : true;
    }

    private SafetySourceData getSafetySourceData(final Context context) {
        if (isSafetySourceHidden()) {
            return null;
        }
        SafetySourceIssue[] safetySourceIssueArr = (SafetySourceIssue[]) Stream.concat(this.mNullCipherStates.entrySet().stream().map(new Function() { // from class: com.android.internal.telephony.security.CellularNetworkSecuritySafetySource$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$getSafetySourceData$0(context, (Map.Entry) obj);
            }
        }), this.mIdentifierDisclosures.entrySet().stream().map(new Function() { // from class: com.android.internal.telephony.security.CellularNetworkSecuritySafetySource$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$getSafetySourceData$1(context, (Map.Entry) obj);
            }
        })).flatMap(new Function() { // from class: com.android.internal.telephony.security.CellularNetworkSecuritySafetySource$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((Optional) obj).stream();
            }
        }).toArray(new IntFunction() { // from class: com.android.internal.telephony.security.CellularNetworkSecuritySafetySource$$ExternalSyntheticLambda3
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return CellularNetworkSecuritySafetySource.$r8$lambda$87rWVyIbTjwLAVRxiUjgJKsHlhk(i);
            }
        });
        SafetySourceData.Builder builder = new SafetySourceData.Builder();
        int iMax = 200;
        for (SafetySourceIssue safetySourceIssue : safetySourceIssueArr) {
            builder.addIssue(safetySourceIssue);
            iMax = Math.max(iMax, safetySourceIssue.getSeverityLevel());
        }
        builder.setStatus(new SafetySourceStatus.Builder(context.getString(R.string.work_mode_turn_on), context.getString(R.string.work_mode_off_title), iMax).setPendingIntent(this.mSafetyCenterManagerWrapper.getActivityPendingIntent(context, CELLULAR_NETWORK_SECURITY_SETTINGS_INTENT)).build());
        return builder.build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Optional lambda$getSafetySourceData$0(Context context, Map.Entry entry) {
        return getNullCipherIssue(context, ((Integer) entry.getKey()).intValue(), ((Integer) entry.getValue()).intValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Optional lambda$getSafetySourceData$1(Context context, Map.Entry entry) {
        return getIdentifierDisclosureIssue(context, ((Integer) entry.getKey()).intValue(), (IdentifierDisclosure) entry.getValue());
    }

    public static /* synthetic */ SafetySourceIssue[] $r8$lambda$87rWVyIbTjwLAVRxiUjgJKsHlhk(int i) {
        return new SafetySourceIssue[i];
    }

    private Optional getNullCipherIssue(Context context, int i, int i2) {
        SafetySourceIssue.Notification notificationBuild;
        SafetySourceIssue.Builder builder;
        if (!this.mNullCipherStateIssuesEnabled) {
            return Optional.empty();
        }
        SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionManagerService.getSubscriptionInfoInternal(i);
        if (i2 == 0) {
            return Optional.empty();
        }
        if (i2 == 1) {
            SafetySourceIssue.Builder builder2 = new SafetySourceIssue.Builder("null_cipher_encrypted_" + i, context.getString(R.string.wrong_hsum_configuration_notification_message, subscriptionInfoInternal.getDisplayName()), context.getString(R.string.write_fail_reason_cannot_write, subscriptionInfoInternal.getDisplayName()), 200, "null_cipher_encrypted");
            notificationBuild = new SafetySourceIssue.Notification.Builder(context.getString(R.string.wrong_hsum_configuration_notification_message, subscriptionInfoInternal.getDisplayName()), context.getString(R.string.write_fail_reason_cannot_write, subscriptionInfoInternal.getDisplayName())).build();
            builder = builder2;
        } else if (i2 == 2) {
            builder = new SafetySourceIssue.Builder("null_cipher_non_encrypted_" + i, context.getString(R.string.years), context.getString(R.string.wrong_hsum_configuration_notification_title, subscriptionInfoInternal.getDisplayName()), 300, "null_cipher_non_encrypted");
            notificationBuild = new SafetySourceIssue.Notification.Builder(context.getString(R.string.years), context.getString(R.string.year, subscriptionInfoInternal.getDisplayName())).build();
        } else {
            throw new AssertionError();
        }
        builder.setNotificationBehavior(300).setIssueCategory(400).setCustomNotification(notificationBuild).addAction(new SafetySourceIssue.Action.Builder("cellular_security_settings", context.getString(R.string.write_fail_reason_cancelled), this.mSafetyCenterManagerWrapper.getActivityPendingIntent(context, CELLULAR_NETWORK_SECURITY_SETTINGS_INTENT)).build());
        Intent learnMoreIntent = getLearnMoreIntent(context);
        if (learnMoreIntent != null) {
            builder.addAction(new SafetySourceIssue.Action.Builder("learn_more", context.getString(R.string.work_profile_telephony_paused_turn_on_button), this.mSafetyCenterManagerWrapper.getActivityPendingIntent(context, learnMoreIntent)).build());
        }
        return Optional.of(builder.build());
    }

    private Optional getIdentifierDisclosureIssue(Context context, int i, IdentifierDisclosure identifierDisclosure) {
        String string;
        String string2;
        if (!this.mIdentifierDisclosureIssuesEnabled || identifierDisclosure.getDisclosureCount() == 0) {
            return Optional.empty();
        }
        SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionManagerService.getSubscriptionInfoInternal(i);
        String cellularIdentifier = getCellularIdentifier(context, identifierDisclosure.mDisclosure.getCellularIdentifier());
        if (TextUtils.isEmpty(cellularIdentifier)) {
            string2 = context.getString(R.string.work_profile_deleted_reason_maximum_password_failure, getCurrentTime(context), subscriptionInfoInternal.getDisplayName());
            string = context.getString(R.string.work_profile_deleted_details, getCurrentTime(context), subscriptionInfoInternal.getDisplayName());
        } else {
            String string3 = context.getString(R.string.work_profile_deleted, getCurrentTime(context), cellularIdentifier, subscriptionInfoInternal.getDisplayName());
            string = context.getString(R.string.work_profile_deleted_description_dpm_wipe, getCurrentTime(context), cellularIdentifier, subscriptionInfoInternal.getDisplayName());
            string2 = string3;
        }
        String str = string;
        SafetySourceIssue.Builder builderAddAction = new SafetySourceIssue.Builder("identifier_disclosure_" + i, context.getString(R.string.work_profile_telephony_paused_text), str, 300, "identifier_disclosure").setNotificationBehavior(300).setIssueCategory(400).setCustomNotification(new SafetySourceIssue.Notification.Builder(context.getString(R.string.work_profile_telephony_paused_text), string2).build()).addAction(new SafetySourceIssue.Action.Builder("cellular_security_settings", context.getString(R.string.write_fail_reason_cancelled), this.mSafetyCenterManagerWrapper.getActivityPendingIntent(context, CELLULAR_NETWORK_SECURITY_SETTINGS_INTENT)).build());
        Intent learnMoreIntent = getLearnMoreIntent(context);
        if (learnMoreIntent != null) {
            builderAddAction.addAction(new SafetySourceIssue.Action.Builder("learn_more", context.getString(R.string.work_profile_telephony_paused_turn_on_button), this.mSafetyCenterManagerWrapper.getActivityPendingIntent(context, learnMoreIntent)).build());
        }
        return Optional.of(builderAddAction.build());
    }

    private String getCurrentTime(Context context) {
        return DateFormat.getTimeFormat(context).format(Calendar.getInstance().getTime());
    }

    private String getCellularIdentifier(Context context, int i) {
        if (i == 1) {
            return context.getString(R.string.config_emergency_call_number);
        }
        if (i == 2) {
            return context.getString(R.string.config_dreamsDefaultComponent);
        }
        if (i == 3) {
            return context.getString(R.string.config_emergency_dialer_package);
        }
        return "";
    }

    private Intent getLearnMoreIntent(Context context) {
        try {
            String string = context.getString(R.string.work_mode_emergency_call_button);
            if (string.isEmpty()) {
                return null;
            }
            return new Intent("android.intent.action.VIEW", Uri.parse(string));
        } catch (Resources.NotFoundException unused) {
            return null;
        }
    }

    public static class SafetyCenterManagerWrapper {
        private final SafetyCenterManager mSafetyCenterManager;

        public SafetyCenterManagerWrapper(Context context) {
            this.mSafetyCenterManager = (SafetyCenterManager) context.getSystemService(SafetyCenterManager.class);
        }

        public PendingIntent getActivityPendingIntent(Context context, Intent intent) {
            return PendingIntent.getActivity(context, 0, intent, 67108864);
        }

        public void setSafetySourceData(SafetySourceData safetySourceData) {
            this.mSafetyCenterManager.setSafetySourceData("AndroidCellularNetworkSecurity", safetySourceData, new SafetyEvent.Builder(100).build());
        }

        public void setRefreshedSafetySourceData(String str, SafetySourceData safetySourceData) {
            this.mSafetyCenterManager.setSafetySourceData("AndroidCellularNetworkSecurity", safetySourceData, new SafetyEvent.Builder(200).setRefreshBroadcastId(str).build());
        }
    }

    private static class IdentifierDisclosure {
        private final CellularIdentifierDisclosure mDisclosure;
        private final int mDisclosureCount;
        private final Instant mWindowEnd;
        private final Instant mWindowStart;

        private IdentifierDisclosure(int i, Instant instant, Instant instant2, CellularIdentifierDisclosure cellularIdentifierDisclosure) {
            this.mDisclosureCount = i;
            this.mWindowStart = instant;
            this.mWindowEnd = instant2;
            this.mDisclosure = cellularIdentifierDisclosure;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getDisclosureCount() {
            return this.mDisclosureCount;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof IdentifierDisclosure)) {
                return false;
            }
            IdentifierDisclosure identifierDisclosure = (IdentifierDisclosure) obj;
            return this.mDisclosureCount == identifierDisclosure.mDisclosureCount && Objects.equals(this.mWindowStart, identifierDisclosure.mWindowStart) && Objects.equals(this.mWindowEnd, identifierDisclosure.mWindowEnd) && Objects.equals(this.mDisclosure, identifierDisclosure.mDisclosure);
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.mDisclosureCount), this.mWindowStart, this.mWindowEnd, this.mDisclosure);
        }
    }

    private class CellularNetworkSecurityBroadcastReceiver extends BroadcastReceiver {
        private CellularNetworkSecurityBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.AIRPLANE_MODE") && intent.getBooleanExtra(CallWaitingController.KEY_STATE, false)) {
                CellularNetworkSecuritySafetySource.this.mNullCipherStates.clear();
                CellularNetworkSecuritySafetySource.this.updateSafetyCenter(context);
            }
        }
    }
}
