package com.android.internal.telephony.subscription;

import android.R;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Telephony;
import android.telephony.SubscriptionManager;
import android.telephony.UiccAccessRule;
import android.text.TextUtils;
import android.util.Base64;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.AndroidUtilIndentingPrintWriter;
import com.android.internal.telephony.Call$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.IccProvider;
import com.android.internal.telephony.InboundSmsHandler;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.Phone$$ExternalSyntheticBUOutline3;
import com.android.internal.telephony.RILUtils$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.RILUtils$$ExternalSyntheticBUOutline1;
import com.android.internal.telephony.build.UnboundedSdkLevel$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.util.function.TriConsumer;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class SubscriptionDatabaseManager extends Handler {
    private final Map mAllSubscriptionInfoInternalCache;
    private final boolean mAsyncMode;
    private final SubscriptionDatabaseManagerCallback mCallback;
    private final Context mContext;
    private boolean mDatabaseInitialized;
    private final FeatureFlags mFeatureFlags;
    private final LocalLog mLocalLog;
    private final ReadWriteLock mReadWriteLock;
    private final UiccController mUiccController;
    private static final Map SUBSCRIPTION_GET_METHOD_MAP = Map.ofEntries(new AbstractMap.SimpleImmutableEntry("_id", new SubscriptionDatabaseManager$$ExternalSyntheticLambda64()), new AbstractMap.SimpleImmutableEntry("icc_id", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda130
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ((SubscriptionInfoInternal) obj).getIccId();
        }
    }), new AbstractMap.SimpleImmutableEntry("sim_id", new SubscriptionDatabaseManager$$ExternalSyntheticLambda141()), new AbstractMap.SimpleImmutableEntry("display_name", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda152
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ((SubscriptionInfoInternal) obj).getDisplayName();
        }
    }), new AbstractMap.SimpleImmutableEntry("carrier_name", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda163
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ((SubscriptionInfoInternal) obj).getCarrierName();
        }
    }), new AbstractMap.SimpleImmutableEntry("name_source", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda174
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getDisplayNameSource());
        }
    }), new AbstractMap.SimpleImmutableEntry("color", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda185
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getIconTint());
        }
    }), new AbstractMap.SimpleImmutableEntry(IccProvider.STR_NUMBER, new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda196
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ((SubscriptionInfoInternal) obj).getNumber();
        }
    }), new AbstractMap.SimpleImmutableEntry("data_roaming", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda207
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getDataRoaming());
        }
    }), new AbstractMap.SimpleImmutableEntry("mcc_string", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda218
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ((SubscriptionInfoInternal) obj).getMcc();
        }
    }), new AbstractMap.SimpleImmutableEntry("mnc_string", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda75
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ((SubscriptionInfoInternal) obj).getMnc();
        }
    }), new AbstractMap.SimpleImmutableEntry("ehplmns", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda86
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ((SubscriptionInfoInternal) obj).getEhplmns();
        }
    }), new AbstractMap.SimpleImmutableEntry("hplmns", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda97
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ((SubscriptionInfoInternal) obj).getHplmns();
        }
    }), new AbstractMap.SimpleImmutableEntry("is_embedded", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda108
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getEmbedded());
        }
    }), new AbstractMap.SimpleImmutableEntry("card_id", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda119
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ((SubscriptionInfoInternal) obj).getCardString();
        }
    }), new AbstractMap.SimpleImmutableEntry("access_rules", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda125
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ((SubscriptionInfoInternal) obj).getNativeAccessRules();
        }
    }), new AbstractMap.SimpleImmutableEntry("access_rules_from_carrier_configs", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda126
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ((SubscriptionInfoInternal) obj).getCarrierConfigAccessRules();
        }
    }), new AbstractMap.SimpleImmutableEntry("is_removable", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda127
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getRemovableEmbedded());
        }
    }), new AbstractMap.SimpleImmutableEntry("enable_cmas_extreme_threat_alerts", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda128
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getCellBroadcastExtremeThreatAlertEnabled());
        }
    }), new AbstractMap.SimpleImmutableEntry("enable_cmas_severe_threat_alerts", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda129
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getCellBroadcastSevereThreatAlertEnabled());
        }
    }), new AbstractMap.SimpleImmutableEntry("enable_cmas_amber_alerts", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda131
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getCellBroadcastAmberAlertEnabled());
        }
    }), new AbstractMap.SimpleImmutableEntry("enable_emergency_alerts", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda132
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getCellBroadcastEmergencyAlertEnabled());
        }
    }), new AbstractMap.SimpleImmutableEntry("alert_sound_duration", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda133
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getCellBroadcastAlertSoundDuration());
        }
    }), new AbstractMap.SimpleImmutableEntry("alert_reminder_interval", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda134
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getCellBroadcastAlertReminderInterval());
        }
    }), new AbstractMap.SimpleImmutableEntry("enable_alert_vibrate", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda135
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getCellBroadcastAlertVibrationEnabled());
        }
    }), new AbstractMap.SimpleImmutableEntry("enable_alert_speech", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda136
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getCellBroadcastAlertSpeechEnabled());
        }
    }), new AbstractMap.SimpleImmutableEntry("enable_etws_test_alerts", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda137
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getCellBroadcastEtwsTestAlertEnabled());
        }
    }), new AbstractMap.SimpleImmutableEntry("enable_channel_50_alerts", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda138
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getCellBroadcastAreaInfoMessageEnabled());
        }
    }), new AbstractMap.SimpleImmutableEntry("enable_cmas_test_alerts", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda139
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getCellBroadcastTestAlertEnabled());
        }
    }), new AbstractMap.SimpleImmutableEntry("show_cmas_opt_out_dialog", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda140
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getCellBroadcastOptOutDialogEnabled());
        }
    }), new AbstractMap.SimpleImmutableEntry("volte_vt_enabled", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda142
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getEnhanced4GModeEnabled());
        }
    }), new AbstractMap.SimpleImmutableEntry("vt_ims_enabled", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda143
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getVideoTelephonyEnabled());
        }
    }), new AbstractMap.SimpleImmutableEntry("wfc_ims_enabled", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda144
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getWifiCallingEnabled());
        }
    }), new AbstractMap.SimpleImmutableEntry("wfc_ims_mode", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda145
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getWifiCallingMode());
        }
    }), new AbstractMap.SimpleImmutableEntry("wfc_ims_roaming_mode", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda146
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getWifiCallingModeForRoaming());
        }
    }), new AbstractMap.SimpleImmutableEntry("wfc_ims_roaming_enabled", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda147
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getWifiCallingEnabledForRoaming());
        }
    }), new AbstractMap.SimpleImmutableEntry("is_opportunistic", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda148
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getOpportunistic());
        }
    }), new AbstractMap.SimpleImmutableEntry("group_uuid", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda149
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ((SubscriptionInfoInternal) obj).getGroupUuid();
        }
    }), new AbstractMap.SimpleImmutableEntry("iso_country_code", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda150
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ((SubscriptionInfoInternal) obj).getCountryIso();
        }
    }), new AbstractMap.SimpleImmutableEntry("carrier_id", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda151
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getCarrierId());
        }
    }), new AbstractMap.SimpleImmutableEntry("profile_class", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda153
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getProfileClass());
        }
    }), new AbstractMap.SimpleImmutableEntry("subscription_type", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda154
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getSubscriptionType());
        }
    }), new AbstractMap.SimpleImmutableEntry("group_owner", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda155
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ((SubscriptionInfoInternal) obj).getGroupOwner();
        }
    }), new AbstractMap.SimpleImmutableEntry("enabled_mobile_data_policies", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda156
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ((SubscriptionInfoInternal) obj).getEnabledMobileDataPolicies();
        }
    }), new AbstractMap.SimpleImmutableEntry("imsi", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda157
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ((SubscriptionInfoInternal) obj).getImsi();
        }
    }), new AbstractMap.SimpleImmutableEntry("uicc_applications_enabled", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda158
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getUiccApplicationsEnabled());
        }
    }), new AbstractMap.SimpleImmutableEntry("ims_rcs_uce_enabled", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda159
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getRcsUceEnabled());
        }
    }), new AbstractMap.SimpleImmutableEntry("cross_sim_calling_enabled", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda160
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getCrossSimCallingEnabled());
        }
    }), new AbstractMap.SimpleImmutableEntry("rcs_config", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda161
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ((SubscriptionInfoInternal) obj).getRcsConfig();
        }
    }), new AbstractMap.SimpleImmutableEntry("allowed_network_types_for_reasons", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda162
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ((SubscriptionInfoInternal) obj).getAllowedNetworkTypesForReasons();
        }
    }), new AbstractMap.SimpleImmutableEntry("d2d_sharing_status", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda164
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getDeviceToDeviceStatusSharingPreference());
        }
    }), new AbstractMap.SimpleImmutableEntry("voims_opt_in_status", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda165
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getVoImsOptInEnabled());
        }
    }), new AbstractMap.SimpleImmutableEntry("d2d_sharing_contacts", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda166
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ((SubscriptionInfoInternal) obj).getDeviceToDeviceStatusSharingContacts();
        }
    }), new AbstractMap.SimpleImmutableEntry("nr_advanced_calling_enabled", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda167
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getNrAdvancedCallingEnabled());
        }
    }), new AbstractMap.SimpleImmutableEntry("phone_number_source_carrier", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda168
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ((SubscriptionInfoInternal) obj).getNumberFromCarrier();
        }
    }), new AbstractMap.SimpleImmutableEntry("phone_number_source_ims", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda169
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ((SubscriptionInfoInternal) obj).getNumberFromIms();
        }
    }), new AbstractMap.SimpleImmutableEntry("phone_number_source_ts43", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda170
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ((SubscriptionInfoInternal) obj).getNumberFromTs43();
        }
    }), new AbstractMap.SimpleImmutableEntry("port_index", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda171
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getPortIndex());
        }
    }), new AbstractMap.SimpleImmutableEntry("usage_setting", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda172
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getUsageSetting());
        }
    }), new AbstractMap.SimpleImmutableEntry("tp_message_ref", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda173
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getLastUsedTPMessageReference());
        }
    }), new AbstractMap.SimpleImmutableEntry("user_handle", new SubscriptionDatabaseManager$$ExternalSyntheticLambda175()), new AbstractMap.SimpleImmutableEntry("satellite_enabled", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda176
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getSatelliteEnabled());
        }
    }), new AbstractMap.SimpleImmutableEntry("satellite_attach_enabled_for_carrier", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda177
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getSatelliteAttachEnabledForCarrier());
        }
    }), new AbstractMap.SimpleImmutableEntry("is_only_ntn", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda178
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getOnlyNonTerrestrialNetwork());
        }
    }), new AbstractMap.SimpleImmutableEntry("service_capabilities", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda179
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getServiceCapabilities());
        }
    }), new AbstractMap.SimpleImmutableEntry("transfer_status", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda180
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getTransferStatus());
        }
    }), new AbstractMap.SimpleImmutableEntry("satellite_entitlement_status", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda181
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getSatelliteEntitlementStatus());
        }
    }), new AbstractMap.SimpleImmutableEntry("satellite_entitlement_plmns", new SubscriptionDatabaseManager$$ExternalSyntheticLambda182()), new AbstractMap.SimpleImmutableEntry("satellite_esos_supported", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda183
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getSatelliteESOSSupported());
        }
    }), new AbstractMap.SimpleImmutableEntry("is_satellite_provisioned_for_non_ip_datagram", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda184
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getIsSatelliteProvisionedForNonIpDatagram());
        }
    }), new AbstractMap.SimpleImmutableEntry("is_private_network", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda186
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((SubscriptionInfoInternal) obj).getIsPrivateNetwork());
        }
    }), new AbstractMap.SimpleImmutableEntry("satellite_entitlement_barred_plmns", new SubscriptionDatabaseManager$$ExternalSyntheticLambda187()), new AbstractMap.SimpleImmutableEntry("satellite_entitlement_data_plan_plmns", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda188
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ((SubscriptionInfoInternal) obj).getSatelliteEntitlementDataPlanForPlmns();
        }
    }), new AbstractMap.SimpleImmutableEntry("satellite_entitlement_service_type_map", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda189
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ((SubscriptionInfoInternal) obj).getSatelliteEntitlementPlmnsServiceTypes();
        }
    }), new AbstractMap.SimpleImmutableEntry("satellite_entitlement_data_service_policy", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda190
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ((SubscriptionInfoInternal) obj).getSatellitePlmnsDataServicePolicy();
        }
    }), new AbstractMap.SimpleImmutableEntry("satellite_entitlement_voice_service_policy", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda191
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ((SubscriptionInfoInternal) obj).getSatellitePlmnsVoiceServicePolicy();
        }
    }), new AbstractMap.SimpleImmutableEntry("streaming_app_max_downlink_kbps", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda192
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Long.valueOf(((SubscriptionInfoInternal) obj).getStreamingAppMaxDownlinkKbps());
        }
    }), new AbstractMap.SimpleImmutableEntry("streaming_app_max_uplink_kbps", new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda193
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Long.valueOf(((SubscriptionInfoInternal) obj).getStreamingAppMaxUplinkKbps());
        }
    }));
    private static final Map SUBSCRIPTION_SET_INTEGER_METHOD_MAP = Map.ofEntries(new AbstractMap.SimpleImmutableEntry("sim_id", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda194
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((SubscriptionDatabaseManager) obj).setSimSlotIndex(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("name_source", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda195
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setDisplayNameSource(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("color", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda197
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setIconTint(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("data_roaming", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda198
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setDataRoaming(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("is_embedded", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda199
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setEmbedded(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("is_removable", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda200
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setRemovableEmbedded(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("enable_cmas_extreme_threat_alerts", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda201
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setCellBroadcastExtremeThreatAlertEnabled(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("enable_cmas_severe_threat_alerts", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda202
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setCellBroadcastSevereThreatAlertEnabled(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("enable_cmas_amber_alerts", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda203
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setCellBroadcastAmberAlertEnabled(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("enable_emergency_alerts", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda204
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setCellBroadcastEmergencyAlertEnabled(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("alert_sound_duration", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda205
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setCellBroadcastAlertSoundDuration(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("alert_reminder_interval", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda206
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setCellBroadcastAlertReminderInterval(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("enable_alert_vibrate", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda208
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setCellBroadcastAlertVibrationEnabled(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("enable_alert_speech", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda209
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setCellBroadcastAlertSpeechEnabled(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("enable_etws_test_alerts", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda210
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setCellBroadcastEtwsTestAlertEnabled(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("enable_channel_50_alerts", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda211
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setCellBroadcastAreaInfoMessageEnabled(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("enable_cmas_test_alerts", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda212
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setCellBroadcastTestAlertEnabled(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("show_cmas_opt_out_dialog", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda213
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setCellBroadcastOptOutDialogEnabled(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("volte_vt_enabled", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda214
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setEnhanced4GModeEnabled(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("vt_ims_enabled", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda215
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setVideoTelephonyEnabled(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("wfc_ims_enabled", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda216
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setWifiCallingEnabled(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("wfc_ims_mode", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda217
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setWifiCallingMode(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("wfc_ims_roaming_mode", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda65
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setWifiCallingModeForRoaming(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("wfc_ims_roaming_enabled", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda66
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setWifiCallingEnabledForRoaming(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("is_opportunistic", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda67
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setOpportunistic(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("carrier_id", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda68
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((SubscriptionDatabaseManager) obj).setCarrierId(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("profile_class", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda69
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setProfileClass(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("subscription_type", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda70
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setSubscriptionType(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("uicc_applications_enabled", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda71
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setUiccApplicationsEnabled(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("ims_rcs_uce_enabled", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda72
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setRcsUceEnabled(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("cross_sim_calling_enabled", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda73
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setCrossSimCallingEnabled(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("d2d_sharing_status", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda74
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setDeviceToDeviceStatusSharingPreference(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("voims_opt_in_status", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda76
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setVoImsOptInEnabled(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("nr_advanced_calling_enabled", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda77
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setNrAdvancedCallingEnabled(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("port_index", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda78
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((SubscriptionDatabaseManager) obj).setPortIndex(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("usage_setting", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda79
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((SubscriptionDatabaseManager) obj).setUsageSetting(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("tp_message_ref", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda80
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((SubscriptionDatabaseManager) obj).setLastUsedTPMessageReference(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("user_handle", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda81
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setUserId(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("satellite_enabled", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda82
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setSatelliteEnabled(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("satellite_attach_enabled_for_carrier", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda83
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setSatelliteAttachEnabledForCarrier(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("is_only_ntn", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda84
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((SubscriptionDatabaseManager) obj).setNtn(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("service_capabilities", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda85
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((SubscriptionDatabaseManager) obj).setServiceCapabilities(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("transfer_status", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda87
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setTransferStatus(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("satellite_entitlement_status", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda88
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setSatelliteEntitlementStatus(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("satellite_esos_supported", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda89
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((SubscriptionDatabaseManager) obj).setSatelliteESOSSupported(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("is_satellite_provisioned_for_non_ip_datagram", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda90
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((SubscriptionDatabaseManager) obj).setIsSatelliteProvisionedForNonIpDatagram(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("is_private_network", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda91
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((SubscriptionDatabaseManager) obj).setIsPrivateNetwork(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }));
    private static final Map SUBSCRIPTION_SET_LONG_METHOD_MAP = Map.ofEntries(new AbstractMap.SimpleImmutableEntry("streaming_app_max_downlink_kbps", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda92
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setStreamingAppMaxDownlinkKbps(((Integer) obj2).intValue(), ((Long) obj3).longValue());
        }
    }), new AbstractMap.SimpleImmutableEntry("streaming_app_max_uplink_kbps", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda93
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setStreamingAppMaxUplinkKbps(((Integer) obj2).intValue(), ((Long) obj3).longValue());
        }
    }));
    private static final Map SUBSCRIPTION_SET_STRING_METHOD_MAP = Map.ofEntries(new AbstractMap.SimpleImmutableEntry("icc_id", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda94
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setIccId(((Integer) obj2).intValue(), (String) obj3);
        }
    }), new AbstractMap.SimpleImmutableEntry("display_name", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda95
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((SubscriptionDatabaseManager) obj).setDisplayName(((Integer) obj2).intValue(), (String) obj3);
        }
    }), new AbstractMap.SimpleImmutableEntry("carrier_name", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda96
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((SubscriptionDatabaseManager) obj).setCarrierName(((Integer) obj2).intValue(), (String) obj3);
        }
    }), new AbstractMap.SimpleImmutableEntry(IccProvider.STR_NUMBER, new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda98
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setNumber(((Integer) obj2).intValue(), (String) obj3);
        }
    }), new AbstractMap.SimpleImmutableEntry("mcc_string", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda99
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((SubscriptionDatabaseManager) obj).setMcc(((Integer) obj2).intValue(), (String) obj3);
        }
    }), new AbstractMap.SimpleImmutableEntry("mnc_string", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda100
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((SubscriptionDatabaseManager) obj).setMnc(((Integer) obj2).intValue(), (String) obj3);
        }
    }), new AbstractMap.SimpleImmutableEntry("ehplmns", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda101
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setEhplmns(((Integer) obj2).intValue(), (String) obj3);
        }
    }), new AbstractMap.SimpleImmutableEntry("hplmns", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda102
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setHplmns(((Integer) obj2).intValue(), (String) obj3);
        }
    }), new AbstractMap.SimpleImmutableEntry("card_id", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda103
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((SubscriptionDatabaseManager) obj).setCardString(((Integer) obj2).intValue(), (String) obj3);
        }
    }), new AbstractMap.SimpleImmutableEntry("group_uuid", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda104
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((SubscriptionDatabaseManager) obj).setGroupUuid(((Integer) obj2).intValue(), (String) obj3);
        }
    }), new AbstractMap.SimpleImmutableEntry("iso_country_code", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda105
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((SubscriptionDatabaseManager) obj).setCountryIso(((Integer) obj2).intValue(), (String) obj3);
        }
    }), new AbstractMap.SimpleImmutableEntry("group_owner", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda106
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((SubscriptionDatabaseManager) obj).setGroupOwner(((Integer) obj2).intValue(), (String) obj3);
        }
    }), new AbstractMap.SimpleImmutableEntry("enabled_mobile_data_policies", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda107
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((SubscriptionDatabaseManager) obj).setEnabledMobileDataPolicies(((Integer) obj2).intValue(), (String) obj3);
        }
    }), new AbstractMap.SimpleImmutableEntry("imsi", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda109
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((SubscriptionDatabaseManager) obj).setImsi(((Integer) obj2).intValue(), (String) obj3);
        }
    }), new AbstractMap.SimpleImmutableEntry("allowed_network_types_for_reasons", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda110
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setAllowedNetworkTypesForReasons(((Integer) obj2).intValue(), (String) obj3);
        }
    }), new AbstractMap.SimpleImmutableEntry("d2d_sharing_contacts", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda111
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setDeviceToDeviceStatusSharingContacts(((Integer) obj2).intValue(), (String) obj3);
        }
    }), new AbstractMap.SimpleImmutableEntry("phone_number_source_carrier", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda112
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setNumberFromCarrier(((Integer) obj2).intValue(), (String) obj3);
        }
    }), new AbstractMap.SimpleImmutableEntry("phone_number_source_ims", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda113
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((SubscriptionDatabaseManager) obj).setNumberFromIms(((Integer) obj2).intValue(), (String) obj3);
        }
    }), new AbstractMap.SimpleImmutableEntry("phone_number_source_ts43", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda114
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setNumberFromTs43(((Integer) obj2).intValue(), (String) obj3);
        }
    }), new AbstractMap.SimpleImmutableEntry("satellite_entitlement_plmns", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda115
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setSatelliteEntitlementPlmns(((Integer) obj2).intValue(), (String) obj3);
        }
    }), new AbstractMap.SimpleImmutableEntry("satellite_entitlement_barred_plmns", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda116
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setSatelliteEntitlementBarredPlmns(((Integer) obj2).intValue(), (String) obj3);
        }
    }), new AbstractMap.SimpleImmutableEntry("satellite_entitlement_data_plan_plmns", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda117
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setSatelliteEntitlementDataPlanForPlmns(((Integer) obj2).intValue(), (String) obj3);
        }
    }), new AbstractMap.SimpleImmutableEntry("satellite_entitlement_service_type_map", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda118
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setSatelliteEntitlementPlmnServiceTypeMap(((Integer) obj2).intValue(), (String) obj3);
        }
    }), new AbstractMap.SimpleImmutableEntry("satellite_entitlement_data_service_policy", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda120
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setSatelliteEntitlementPlmnDataServicePolicy(((Integer) obj2).intValue(), (String) obj3);
        }
    }), new AbstractMap.SimpleImmutableEntry("satellite_entitlement_voice_service_policy", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda121
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setSatelliteEntitlementPlmnVoiceServicePolicy(((Integer) obj2).intValue(), (String) obj3);
        }
    }));
    private static final Map SUBSCRIPTION_SET_BYTE_ARRAY_METHOD_MAP = Map.ofEntries(new AbstractMap.SimpleImmutableEntry("access_rules", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda122
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setNativeAccessRules(((Integer) obj2).intValue(), (byte[]) obj3);
        }
    }), new AbstractMap.SimpleImmutableEntry("access_rules_from_carrier_configs", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda123
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setCarrierConfigAccessRules(((Integer) obj2).intValue(), (byte[]) obj3);
        }
    }), new AbstractMap.SimpleImmutableEntry("rcs_config", new TriConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda124
        public final void accept(Object obj, Object obj2, Object obj3) throws Throwable {
            ((SubscriptionDatabaseManager) obj).setRcsConfig(((Integer) obj2).intValue(), (byte[]) obj3);
        }
    }));
    private static final Set GROUP_SHARING_COLUMNS = Set.of((Object[]) new String[]{"display_name", "name_source", "color", "data_roaming", "volte_vt_enabled", "vt_ims_enabled", "wfc_ims_enabled", "wfc_ims_mode", "wfc_ims_roaming_mode", "wfc_ims_roaming_enabled", "enabled_mobile_data_policies", "uicc_applications_enabled", "ims_rcs_uce_enabled", "cross_sim_calling_enabled", "rcs_config", "d2d_sharing_status", "voims_opt_in_status", "d2d_sharing_contacts", "nr_advanced_calling_enabled", "user_handle", "satellite_enabled", "satellite_attach_enabled_for_carrier", "streaming_app_max_downlink_kbps", "streaming_app_max_uplink_kbps"});
    private static final Set DEPRECATED_DATABASE_COLUMNS = Set.of("display_number_format", "mcc", "mnc", "sim_provisioning_status", "is_metered", "data_enabled_override_rules", "allowed_network_types");

    private void logv(String str) {
    }

    public static abstract class SubscriptionDatabaseManagerCallback {
        private final Executor mExecutor;

        public abstract void onInitialized();

        public abstract void onSubscriptionChanged(int i);

        public SubscriptionDatabaseManagerCallback(Executor executor) {
            this.mExecutor = executor;
        }

        public Executor getExecutor() {
            return this.mExecutor;
        }

        public void invokeFromExecutor(Runnable runnable) {
            this.mExecutor.execute(runnable);
        }
    }

    public SubscriptionDatabaseManager(Context context, Looper looper, FeatureFlags featureFlags, SubscriptionDatabaseManagerCallback subscriptionDatabaseManagerCallback) {
        super(looper);
        this.mReadWriteLock = new ReentrantReadWriteLock();
        this.mLocalLog = new LocalLog(128);
        this.mAllSubscriptionInfoInternalCache = new HashMap(16);
        this.mDatabaseInitialized = false;
        log("Created SubscriptionDatabaseManager.");
        this.mContext = context;
        this.mCallback = subscriptionDatabaseManagerCallback;
        this.mUiccController = UiccController.getInstance();
        this.mAsyncMode = context.getResources().getBoolean(R.bool.config_viewBasedRotaryEncoderHapticsEnabled);
        this.mFeatureFlags = featureFlags;
        initializeDatabase();
    }

    protected Object getSubscriptionInfoFieldByColumnName(SubscriptionInfoInternal subscriptionInfoInternal, String str) {
        Map map = SUBSCRIPTION_GET_METHOD_MAP;
        if (map.containsKey(str)) {
            return ((Function) map.get(str)).apply(subscriptionInfoInternal);
        }
        RILUtils$$ExternalSyntheticBUOutline1.m("Invalid column name ", str);
        return null;
    }

    public Object getSubscriptionProperty(int i, String str) {
        SubscriptionInfoInternal subscriptionInfoInternal = getSubscriptionInfoInternal(i);
        if (subscriptionInfoInternal == null) {
            throw new IllegalArgumentException("getSubscriptionProperty: Invalid subId " + i + ", columnName=" + str);
        }
        return getSubscriptionInfoFieldByColumnName(subscriptionInfoInternal, str);
    }

    public void setSubscriptionProperty(int i, String str, Object obj) {
        byte[] bArrDecode;
        long jLongValue;
        int iIntValue;
        Map map = SUBSCRIPTION_SET_INTEGER_METHOD_MAP;
        if (map.containsKey(str)) {
            if (obj instanceof String) {
                iIntValue = Integer.parseInt((String) obj);
            } else if (obj instanceof Integer) {
                iIntValue = ((Integer) obj).intValue();
            } else {
                SubscriptionDatabaseManager$$ExternalSyntheticBUOutline0.m(str, obj.getClass(), " to integer.");
                return;
            }
            ((TriConsumer) map.get(str)).accept(this, Integer.valueOf(i), Integer.valueOf(iIntValue));
            return;
        }
        Map map2 = SUBSCRIPTION_SET_STRING_METHOD_MAP;
        if (map2.containsKey(str)) {
            ((TriConsumer) map2.get(str)).accept(this, Integer.valueOf(i), (String) obj);
            return;
        }
        Map map3 = SUBSCRIPTION_SET_LONG_METHOD_MAP;
        if (map3.containsKey(str)) {
            if (obj instanceof String) {
                jLongValue = Long.parseLong((String) obj);
            } else if (obj instanceof Number) {
                jLongValue = ((Number) obj).longValue();
            } else {
                SubscriptionDatabaseManager$$ExternalSyntheticBUOutline0.m(str, obj.getClass(), " to long.");
                return;
            }
            ((TriConsumer) map3.get(str)).accept(this, Integer.valueOf(i), Long.valueOf(jLongValue));
            return;
        }
        Map map4 = SUBSCRIPTION_SET_BYTE_ARRAY_METHOD_MAP;
        if (map4.containsKey(str)) {
            if (obj instanceof String) {
                bArrDecode = Base64.decode((String) obj, 0);
            } else if (obj instanceof byte[]) {
                bArrDecode = (byte[]) obj;
            } else {
                SubscriptionDatabaseManager$$ExternalSyntheticBUOutline0.m(str, obj.getClass(), " to byte[].");
                return;
            }
            ((TriConsumer) map4.get(str)).accept(this, Integer.valueOf(i), bArrDecode);
            return;
        }
        UnboundedSdkLevel$$ExternalSyntheticBUOutline0.m("Does not support set ", str, ".");
    }

    private ContentValues createDeltaContentValues(SubscriptionInfoInternal subscriptionInfoInternal, SubscriptionInfoInternal subscriptionInfoInternal2) {
        Object subscriptionInfoFieldByColumnName;
        ContentValues contentValues = new ContentValues();
        for (String str : Telephony.SimInfo.getAllColumns()) {
            if (!DEPRECATED_DATABASE_COLUMNS.contains(str) && !str.equals("_id") && (subscriptionInfoFieldByColumnName = getSubscriptionInfoFieldByColumnName(subscriptionInfoInternal2, str)) != null) {
                if (!Objects.equals(subscriptionInfoInternal != null ? getSubscriptionInfoFieldByColumnName(subscriptionInfoInternal, str) : null, subscriptionInfoFieldByColumnName)) {
                    contentValues.putObject(str, subscriptionInfoFieldByColumnName);
                }
            }
        }
        return contentValues;
    }

    private int insertNewRecordIntoDatabaseSync(ContentValues contentValues) {
        Objects.requireNonNull(contentValues);
        Uri uriInsert = this.mContext.getContentResolver().insert(Telephony.SimInfo.CONTENT_URI, contentValues);
        if (uriInsert != null && uriInsert.getLastPathSegment() != null) {
            int i = Integer.parseInt(uriInsert.getLastPathSegment());
            if (SubscriptionManager.isValidSubscriptionId(i)) {
                logv("insertNewRecordIntoDatabaseSync: contentValues=" + contentValues);
                logl("insertNewRecordIntoDatabaseSync: Successfully added subscription. subId=" + uriInsert.getLastPathSegment());
                return i;
            }
        }
        logel("insertNewRecordIntoDatabaseSync: Failed to insert subscription into database. contentValues=" + contentValues);
        return -1;
    }

    public int insertSubscriptionInfo(SubscriptionInfoInternal subscriptionInfoInternal) {
        Objects.requireNonNull(subscriptionInfoInternal);
        if (SubscriptionManager.isValidSubscriptionId(subscriptionInfoInternal.getSubscriptionId())) {
            Call$$ExternalSyntheticBUOutline0.m("insertSubscriptionInfo: Not a new subscription to insert. subInfo=", subscriptionInfoInternal);
            return 0;
        }
        synchronized (this) {
            if (!this.mDatabaseInitialized) {
                throw new IllegalStateException("Database has not been initialized. Can't insert new record at this point.");
            }
        }
        this.mReadWriteLock.writeLock().lock();
        try {
            final int iInsertNewRecordIntoDatabaseSync = insertNewRecordIntoDatabaseSync(createDeltaContentValues(null, subscriptionInfoInternal));
            if (iInsertNewRecordIntoDatabaseSync > 0) {
                this.mAllSubscriptionInfoInternalCache.put(Integer.valueOf(iInsertNewRecordIntoDatabaseSync), onCreateSubscriptionInfoInternalBuilder(subscriptionInfoInternal).setId(iInsertNewRecordIntoDatabaseSync).build());
            } else {
                logel("insertSubscriptionInfo: Failed to insert a new subscription. subInfo=" + subscriptionInfoInternal);
            }
            this.mReadWriteLock.writeLock().unlock();
            this.mCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda21
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$insertSubscriptionInfo$0(iInsertNewRecordIntoDatabaseSync);
                }
            });
            return iInsertNewRecordIntoDatabaseSync;
        } catch (Throwable th) {
            this.mReadWriteLock.writeLock().unlock();
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$insertSubscriptionInfo$0(int i) {
        this.mCallback.onSubscriptionChanged(i);
    }

    public void removeSubscriptionInfo(final int i) {
        if (!this.mAllSubscriptionInfoInternalCache.containsKey(Integer.valueOf(i))) {
            Phone$$ExternalSyntheticBUOutline3.m("subId ", i, " is invalid.");
            return;
        }
        this.mReadWriteLock.writeLock().lock();
        try {
            if (this.mContext.getContentResolver().delete(Telephony.SimInfo.CONTENT_URI, InboundSmsHandler.SELECT_BY_ID, new String[]{Integer.toString(i)}) > 0) {
                this.mAllSubscriptionInfoInternalCache.remove(Integer.valueOf(i));
            } else {
                logel("Failed to remove subscription with subId=" + i);
            }
            this.mReadWriteLock.writeLock().unlock();
            this.mCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda240
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$removeSubscriptionInfo$1(i);
                }
            });
        } catch (Throwable th) {
            this.mReadWriteLock.writeLock().unlock();
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeSubscriptionInfo$1(int i) {
        this.mCallback.onSubscriptionChanged(i);
    }

    private int updateDatabase(final int i, final ContentValues contentValues) {
        logv("updateDatabase: prepare to update sub " + i);
        synchronized (this) {
            try {
                if (!this.mDatabaseInitialized) {
                    logel("updateDatabase: Database has not been initialized. Can't update database at this point. contentValues=" + contentValues);
                    return 0;
                }
                if (this.mAsyncMode) {
                    post(new Runnable() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda246
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$updateDatabase$2(i, contentValues);
                        }
                    });
                    return 1;
                }
                logv("updateDatabase: sync updated subscription in the database. subId=" + i + ", contentValues= " + contentValues.getValues());
                return this.mContext.getContentResolver().update(Uri.withAppendedPath(Telephony.SimInfo.CONTENT_URI, String.valueOf(i)), contentValues, null, null);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDatabase$2(int i, ContentValues contentValues) {
        this.mContext.getContentResolver().update(Uri.withAppendedPath(Telephony.SimInfo.CONTENT_URI, String.valueOf(i)), contentValues, null, null);
        logv("updateDatabase: async updated subscription in the database. subId=" + i + ", contentValues= " + contentValues.getValues());
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v0 */
    /* JADX WARN: Type inference failed for: r3v2, types: [java.lang.Integer, java.lang.Object] */
    protected <T> void writeDatabaseAndCacheHelper(final int i, final String str, final T t, final BiFunction<SubscriptionInfoInternal.Builder, T, SubscriptionInfoInternal.Builder> biFunction) throws Throwable {
        SubscriptionDatabaseManager subscriptionDatabaseManagerValueOf;
        final ContentValues contentValues = new ContentValues();
        this.mReadWriteLock.writeLock().lock();
        try {
            Map map = this.mAllSubscriptionInfoInternalCache;
            subscriptionDatabaseManagerValueOf = Integer.valueOf(i);
            final SubscriptionInfoInternal subscriptionInfoInternal = (SubscriptionInfoInternal) map.get(subscriptionDatabaseManagerValueOf);
            try {
                if (subscriptionInfoInternal == null) {
                    logel("Subscription doesn't exist. subId=" + i + ", columnName=" + str);
                    throw new IllegalArgumentException("Subscription doesn't exist. subId=" + i + ", columnName=" + str);
                }
                final boolean zContains = GROUP_SHARING_COLUMNS.contains(str);
                this.mAllSubscriptionInfoInternalCache.forEach(new BiConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda35
                    @Override // java.util.function.BiConsumer
                    public final void accept(Object obj, Object obj2) {
                        this.f$0.lambda$writeDatabaseAndCacheHelper$4(i, zContains, subscriptionInfoInternal, str, t, biFunction, contentValues, (Integer) obj, (SubscriptionInfoInternal) obj2);
                    }
                });
                this.mReadWriteLock.writeLock().unlock();
                return;
            } catch (Throwable th) {
                th = th;
            }
        } catch (Throwable th2) {
            th = th2;
            subscriptionDatabaseManagerValueOf = this;
        }
        Throwable th3 = th;
        subscriptionDatabaseManagerValueOf.mReadWriteLock.writeLock().unlock();
        throw th3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$writeDatabaseAndCacheHelper$4(final int i, boolean z, SubscriptionInfoInternal subscriptionInfoInternal, String str, Object obj, BiFunction biFunction, ContentValues contentValues, Integer num, SubscriptionInfoInternal subscriptionInfoInternal2) {
        if ((num.intValue() == i || (z && !subscriptionInfoInternal.getGroupUuid().isEmpty() && subscriptionInfoInternal.getGroupUuid().equals(subscriptionInfoInternal2.getGroupUuid()))) && !Objects.equals(getSubscriptionInfoFieldByColumnName(subscriptionInfoInternal2, str), obj)) {
            logv("writeDatabaseAndCacheHelper: subId=" + i + ",columnName=" + str + ", newValue=" + obj);
            SubscriptionInfoInternal.Builder builder = (SubscriptionInfoInternal.Builder) biFunction.apply(onCreateSubscriptionInfoInternalBuilder(subscriptionInfoInternal2), obj);
            contentValues.putObject(str, obj);
            if (updateDatabase(num.intValue(), contentValues) > 0) {
                this.mAllSubscriptionInfoInternalCache.put(num, builder.build());
                this.mCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda245
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$writeDatabaseAndCacheHelper$3(i);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$writeDatabaseAndCacheHelper$3(int i) {
        this.mCallback.onSubscriptionChanged(i);
    }

    public void updateSubscription(SubscriptionInfoInternal subscriptionInfoInternal) {
        Objects.requireNonNull(subscriptionInfoInternal);
        this.mReadWriteLock.writeLock().lock();
        try {
            final int subscriptionId = subscriptionInfoInternal.getSubscriptionId();
            SubscriptionInfoInternal subscriptionInfoInternal2 = (SubscriptionInfoInternal) this.mAllSubscriptionInfoInternalCache.get(Integer.valueOf(subscriptionInfoInternal.getSubscriptionId()));
            if (subscriptionInfoInternal2 == null) {
                throw new IllegalArgumentException("updateSubscription: subscription does not exist. subId=" + subscriptionId);
            }
            if (!subscriptionInfoInternal2.equalsDbItemsOnly(subscriptionInfoInternal) && updateDatabase(subscriptionId, createDeltaContentValues(subscriptionInfoInternal2, subscriptionInfoInternal)) > 0) {
                this.mAllSubscriptionInfoInternalCache.put(Integer.valueOf(subscriptionId), subscriptionInfoInternal);
                this.mCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda13
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$updateSubscription$5(subscriptionId);
                    }
                });
            }
            this.mReadWriteLock.writeLock().unlock();
        } catch (Throwable th) {
            this.mReadWriteLock.writeLock().unlock();
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSubscription$5(int i) {
        this.mCallback.onSubscriptionChanged(i);
    }

    public void setIccId(int i, String str) throws Throwable {
        Objects.requireNonNull(str);
        writeDatabaseAndCacheHelper(i, "icc_id", str, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda242
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setIccId((String) obj2);
            }
        });
    }

    public void setSimSlotIndex(int i, int i2) {
        writeDatabaseAndCacheHelper(i, "sim_id", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda60
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setSimSlotIndex(((Integer) obj2).intValue());
            }
        });
    }

    public void setDisplayName(int i, String str) {
        Objects.requireNonNull(str);
        writeDatabaseAndCacheHelper(i, "display_name", str, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda29
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setDisplayName((String) obj2);
            }
        });
    }

    public void setCarrierName(int i, String str) {
        Objects.requireNonNull(str);
        writeDatabaseAndCacheHelper(i, "carrier_name", str, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda229
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setCarrierName((String) obj2);
            }
        });
    }

    public void setDisplayNameSource(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "name_source", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda19
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setDisplayNameSource(((Integer) obj2).intValue());
            }
        });
    }

    public void setIconTint(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "color", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda58
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setIconTint(((Integer) obj2).intValue());
            }
        });
    }

    public void setNumber(int i, String str) throws Throwable {
        Objects.requireNonNull(str);
        writeDatabaseAndCacheHelper(i, IccProvider.STR_NUMBER, str, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda37
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setNumber((String) obj2);
            }
        });
    }

    public void setDataRoaming(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "data_roaming", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda228
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setDataRoaming(((Integer) obj2).intValue());
            }
        });
    }

    public void setMcc(int i, String str) {
        Objects.requireNonNull(str);
        writeDatabaseAndCacheHelper(i, "mcc_string", str, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda51
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setMcc((String) obj2);
            }
        });
    }

    public void setMnc(int i, String str) {
        Objects.requireNonNull(str);
        writeDatabaseAndCacheHelper(i, "mnc_string", str, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda241
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setMnc((String) obj2);
            }
        });
    }

    public void setEhplmns(int i, String[] strArr) {
        Objects.requireNonNull(strArr);
        setEhplmns(i, (String) Arrays.stream(strArr).filter(Predicate.not(new SubscriptionDatabaseManager$$ExternalSyntheticLambda17())).collect(Collectors.joining(",")));
    }

    public void setEhplmns(int i, String str) throws Throwable {
        Objects.requireNonNull(str);
        writeDatabaseAndCacheHelper(i, "ehplmns", str, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda222
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setEhplmns((String) obj2);
            }
        });
    }

    public void setHplmns(int i, String[] strArr) {
        Objects.requireNonNull(strArr);
        setHplmns(i, (String) Arrays.stream(strArr).filter(Predicate.not(new SubscriptionDatabaseManager$$ExternalSyntheticLambda17())).collect(Collectors.joining(",")));
    }

    public void setHplmns(int i, String str) throws Throwable {
        Objects.requireNonNull(str);
        writeDatabaseAndCacheHelper(i, "hplmns", str, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda26
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setHplmns((String) obj2);
            }
        });
    }

    public void setEmbedded(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "is_embedded", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda40
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setEmbedded(((Integer) obj2).intValue());
            }
        });
    }

    public void setEmbedded(int i, boolean z) {
        setEmbedded(i, z ? 1 : 0);
    }

    public void setCardString(int i, String str) {
        Objects.requireNonNull(str);
        setCardId(i, this.mUiccController.convertToPublicCardId(str));
        writeDatabaseAndCacheHelper(i, "card_id", str, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda15
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setCardString((String) obj2);
            }
        });
    }

    public void setCardId(int i, int i2) {
        this.mReadWriteLock.writeLock().lock();
        try {
            SubscriptionInfoInternal subscriptionInfoInternal = (SubscriptionInfoInternal) this.mAllSubscriptionInfoInternalCache.get(Integer.valueOf(i));
            if (subscriptionInfoInternal == null) {
                throw new IllegalArgumentException("setCardId: Subscription doesn't exist. subId=" + i);
            }
            this.mAllSubscriptionInfoInternalCache.put(Integer.valueOf(i), onCreateSubscriptionInfoInternalBuilder(subscriptionInfoInternal).setCardId(i2).build());
            this.mReadWriteLock.writeLock().unlock();
        } catch (Throwable th) {
            this.mReadWriteLock.writeLock().unlock();
            throw th;
        }
    }

    public void setNativeAccessRules(int i, byte[] bArr) throws Throwable {
        Objects.requireNonNull(bArr);
        writeDatabaseAndCacheHelper(i, "access_rules", bArr, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda238
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setNativeAccessRules((byte[]) obj2);
            }
        });
    }

    public void setCarrierConfigAccessRules(int i, byte[] bArr) throws Throwable {
        Objects.requireNonNull(bArr);
        writeDatabaseAndCacheHelper(i, "access_rules_from_carrier_configs", bArr, new SubscriptionDatabaseManager$$ExternalSyntheticLambda224());
    }

    public void setCarrierConfigAccessRules(int i, UiccAccessRule[] uiccAccessRuleArr) {
        Objects.requireNonNull(uiccAccessRuleArr);
        writeDatabaseAndCacheHelper(i, "access_rules_from_carrier_configs", UiccAccessRule.encodeRules(uiccAccessRuleArr), new SubscriptionDatabaseManager$$ExternalSyntheticLambda224());
    }

    public void setRemovableEmbedded(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "is_removable", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda221
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setRemovableEmbedded(((Integer) obj2).intValue());
            }
        });
    }

    public void setCellBroadcastExtremeThreatAlertEnabled(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "enable_cmas_extreme_threat_alerts", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda3
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setCellBroadcastExtremeThreatAlertEnabled(((Integer) obj2).intValue());
            }
        });
    }

    public void setCellBroadcastSevereThreatAlertEnabled(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "enable_cmas_severe_threat_alerts", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda20
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setCellBroadcastSevereThreatAlertEnabled(((Integer) obj2).intValue());
            }
        });
    }

    public void setCellBroadcastAmberAlertEnabled(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "enable_cmas_amber_alerts", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda38
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setCellBroadcastAmberAlertEnabled(((Integer) obj2).intValue());
            }
        });
    }

    public void setCellBroadcastEmergencyAlertEnabled(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "enable_emergency_alerts", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda39
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setCellBroadcastEmergencyAlertEnabled(((Integer) obj2).intValue());
            }
        });
    }

    public void setCellBroadcastAlertSoundDuration(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "alert_sound_duration", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda18
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setCellBroadcastAlertSoundDuration(((Integer) obj2).intValue());
            }
        });
    }

    public void setCellBroadcastAlertReminderInterval(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "alert_reminder_interval", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda25
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setCellBroadcastAlertReminderInterval(((Integer) obj2).intValue());
            }
        });
    }

    public void setCellBroadcastAlertVibrationEnabled(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "enable_alert_vibrate", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda227
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setCellBroadcastAlertVibrationEnabled(((Integer) obj2).intValue());
            }
        });
    }

    public void setCellBroadcastAlertSpeechEnabled(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "enable_alert_speech", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda225
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setCellBroadcastAlertSpeechEnabled(((Integer) obj2).intValue());
            }
        });
    }

    public void setCellBroadcastEtwsTestAlertEnabled(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "enable_etws_test_alerts", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda61
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setCellBroadcastEtwsTestAlertEnabled(((Integer) obj2).intValue());
            }
        });
    }

    public void setCellBroadcastAreaInfoMessageEnabled(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "enable_channel_50_alerts", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda41
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setCellBroadcastAreaInfoMessageEnabled(((Integer) obj2).intValue());
            }
        });
    }

    public void setCellBroadcastTestAlertEnabled(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "enable_cmas_test_alerts", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda47
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setCellBroadcastTestAlertEnabled(((Integer) obj2).intValue());
            }
        });
    }

    public void setCellBroadcastOptOutDialogEnabled(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "show_cmas_opt_out_dialog", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda62
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setCellBroadcastOptOutDialogEnabled(((Integer) obj2).intValue());
            }
        });
    }

    public void setEnhanced4GModeEnabled(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "volte_vt_enabled", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda32
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setEnhanced4GModeEnabled(((Integer) obj2).intValue());
            }
        });
    }

    public void setVideoTelephonyEnabled(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "vt_ims_enabled", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda11
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setVideoTelephonyEnabled(((Integer) obj2).intValue());
            }
        });
    }

    public void setWifiCallingEnabled(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "wfc_ims_enabled", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda5
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setWifiCallingEnabled(((Integer) obj2).intValue());
            }
        });
    }

    public void setWifiCallingMode(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "wfc_ims_mode", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda226
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setWifiCallingMode(((Integer) obj2).intValue());
            }
        });
    }

    public void setWifiCallingModeForRoaming(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "wfc_ims_roaming_mode", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda232
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setWifiCallingModeForRoaming(((Integer) obj2).intValue());
            }
        });
    }

    public void setWifiCallingEnabledForRoaming(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "wfc_ims_roaming_enabled", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda28
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setWifiCallingEnabledForRoaming(((Integer) obj2).intValue());
            }
        });
    }

    public void setOpportunistic(int i, boolean z) {
        setOpportunistic(i, z ? 1 : 0);
    }

    public void setOpportunistic(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "is_opportunistic", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda16
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setOpportunistic(((Integer) obj2).intValue());
            }
        });
    }

    public void setGroupUuid(int i, String str) {
        Objects.requireNonNull(str);
        writeDatabaseAndCacheHelper(i, "group_uuid", str, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda56
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setGroupUuid((String) obj2);
            }
        });
    }

    public void setCountryIso(int i, String str) {
        Objects.requireNonNull(str);
        writeDatabaseAndCacheHelper(i, "iso_country_code", str, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda44
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setCountryIso((String) obj2);
            }
        });
    }

    public void setCarrierId(int i, int i2) {
        writeDatabaseAndCacheHelper(i, "carrier_id", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda52
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setCarrierId(((Integer) obj2).intValue());
            }
        });
    }

    public void setProfileClass(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "profile_class", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda234
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setProfileClass(((Integer) obj2).intValue());
            }
        });
    }

    public void setSubscriptionType(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "subscription_type", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setType(((Integer) obj2).intValue());
            }
        });
    }

    public void setGroupOwner(int i, String str) {
        Objects.requireNonNull(str);
        writeDatabaseAndCacheHelper(i, "group_owner", str, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda235
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setGroupOwner((String) obj2);
            }
        });
    }

    public void setEnabledMobileDataPolicies(int i, String str) {
        Objects.requireNonNull(str);
        writeDatabaseAndCacheHelper(i, "enabled_mobile_data_policies", str, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda33
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setEnabledMobileDataPolicies((String) obj2);
            }
        });
    }

    public void setImsi(int i, String str) {
        Objects.requireNonNull(str);
        writeDatabaseAndCacheHelper(i, "imsi", str, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda239
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setImsi((String) obj2);
            }
        });
    }

    public void setUiccApplicationsEnabled(int i, boolean z) {
        setUiccApplicationsEnabled(i, z ? 1 : 0);
    }

    public void setUiccApplicationsEnabled(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "uicc_applications_enabled", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda23
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setUiccApplicationsEnabled(((Integer) obj2).intValue());
            }
        });
    }

    public void setRcsUceEnabled(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "ims_rcs_uce_enabled", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda14
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setRcsUceEnabled(((Integer) obj2).intValue());
            }
        });
    }

    public void setCrossSimCallingEnabled(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "cross_sim_calling_enabled", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda55
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setCrossSimCallingEnabled(((Integer) obj2).intValue());
            }
        });
    }

    public void setRcsConfig(int i, byte[] bArr) throws Throwable {
        Objects.requireNonNull(bArr);
        writeDatabaseAndCacheHelper(i, "rcs_config", bArr, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda236
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setRcsConfig((byte[]) obj2);
            }
        });
    }

    public void setAllowedNetworkTypesForReasons(int i, String str) throws Throwable {
        Objects.requireNonNull(str);
        writeDatabaseAndCacheHelper(i, "allowed_network_types_for_reasons", str, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda57
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setAllowedNetworkTypesForReasons((String) obj2);
            }
        });
    }

    public void setDeviceToDeviceStatusSharingPreference(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "d2d_sharing_status", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda12
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setDeviceToDeviceStatusSharingPreference(((Integer) obj2).intValue());
            }
        });
    }

    public void setVoImsOptInEnabled(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "voims_opt_in_status", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda43
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setVoImsOptInEnabled(((Integer) obj2).intValue());
            }
        });
    }

    public void setDeviceToDeviceStatusSharingContacts(int i, String str) throws Throwable {
        Objects.requireNonNull(str);
        writeDatabaseAndCacheHelper(i, "d2d_sharing_contacts", str, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda54
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setDeviceToDeviceStatusSharingContacts((String) obj2);
            }
        });
    }

    public void setNrAdvancedCallingEnabled(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "nr_advanced_calling_enabled", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda9
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setNrAdvancedCallingEnabled(((Integer) obj2).intValue());
            }
        });
    }

    public void setNumberFromCarrier(int i, String str) throws Throwable {
        Objects.requireNonNull(str);
        writeDatabaseAndCacheHelper(i, "phone_number_source_carrier", str, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda220
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setNumberFromCarrier((String) obj2);
            }
        });
    }

    public void setNumberFromIms(int i, String str) {
        Objects.requireNonNull(str);
        writeDatabaseAndCacheHelper(i, "phone_number_source_ims", str, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda30
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setNumberFromIms((String) obj2);
            }
        });
    }

    public void setNumberFromTs43(int i, String str) throws Throwable {
        Objects.requireNonNull(str);
        writeDatabaseAndCacheHelper(i, "phone_number_source_ts43", str, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda31
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setNumberFromTs43((String) obj2);
            }
        });
    }

    public void setPortIndex(int i, int i2) {
        writeDatabaseAndCacheHelper(i, "port_index", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda243
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setPortIndex(((Integer) obj2).intValue());
            }
        });
    }

    public void setUsageSetting(int i, int i2) {
        writeDatabaseAndCacheHelper(i, "usage_setting", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda27
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setUsageSetting(((Integer) obj2).intValue());
            }
        });
    }

    public void setLastUsedTPMessageReference(int i, int i2) {
        writeDatabaseAndCacheHelper(i, "tp_message_ref", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda53
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setLastUsedTPMessageReference(((Integer) obj2).intValue());
            }
        });
    }

    public void setUserId(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "user_handle", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda42
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setUserId(((Integer) obj2).intValue());
            }
        });
    }

    public void setSatelliteEnabled(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "satellite_enabled", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda22
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setSatelliteEnabled(((Integer) obj2).intValue());
            }
        });
    }

    public void setSatelliteAttachEnabledForCarrier(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "satellite_attach_enabled_for_carrier", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda233
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setSatelliteAttachEnabledForCarrier(((Integer) obj2).intValue());
            }
        });
    }

    public void setNtn(int i, int i2) {
        writeDatabaseAndCacheHelper(i, "is_only_ntn", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda50
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setOnlyNonTerrestrialNetwork(((Integer) obj2).intValue());
            }
        });
    }

    public void setGroupDisabled(final int i, boolean z) {
        this.mReadWriteLock.writeLock().lock();
        try {
            SubscriptionInfoInternal subscriptionInfoInternal = (SubscriptionInfoInternal) this.mAllSubscriptionInfoInternalCache.get(Integer.valueOf(i));
            if (subscriptionInfoInternal == null) {
                throw new IllegalArgumentException("setGroupDisabled: Subscription doesn't exist. subId=" + i);
            }
            boolean z2 = subscriptionInfoInternal.isGroupDisabled() != z;
            this.mAllSubscriptionInfoInternalCache.put(Integer.valueOf(i), onCreateSubscriptionInfoInternalBuilder(subscriptionInfoInternal).setGroupDisabled(z).build());
            this.mReadWriteLock.writeLock().unlock();
            if (z2) {
                log("setGroupDisabled value changed, firing the callback");
                this.mCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda59
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$setGroupDisabled$6(i);
                    }
                });
            }
        } catch (Throwable th) {
            this.mReadWriteLock.writeLock().unlock();
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setGroupDisabled$6(int i) {
        this.mCallback.onSubscriptionChanged(i);
    }

    public void setServiceCapabilities(int i, int i2) {
        writeDatabaseAndCacheHelper(i, "service_capabilities", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda63
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setServiceCapabilities(((Integer) obj2).intValue());
            }
        });
    }

    public void setSatelliteEntitlementStatus(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "satellite_entitlement_status", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda8
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setSatelliteEntitlementStatus(((Integer) obj2).intValue());
            }
        });
    }

    public void setSatelliteEntitlementPlmns(int i, String str) throws Throwable {
        writeDatabaseAndCacheHelper(i, "satellite_entitlement_plmns", str, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda45
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setSatelliteEntitlementPlmns((String) obj2);
            }
        });
    }

    public void setSatelliteEntitlementInfo(int i, List<String> list, List<String> list2, Map<String, Integer> map, Map<String, List<Integer>> map2, Map<String, Integer> map3, Map<String, Integer> map4) {
        setSatelliteEntitlementPlmns(i, (String) list.stream().collect(Collectors.joining(",")));
        setSatelliteEntitlementBarredPlmns(i, (String) list2.stream().collect(Collectors.joining(",")));
        setSatelliteEntitlementDataPlanForPlmns(i, serializeMapToJsonString(map));
        setSatelliteEntitlementPlmnServiceTypeMap(i, serializeMapListToJsonString(map2));
        setSatelliteEntitlementPlmnDataServicePolicy(i, serializeMapToJsonString(map3));
        setSatelliteEntitlementPlmnVoiceServicePolicy(i, serializeMapToJsonString(map4));
    }

    public void setSatelliteESOSSupported(int i, int i2) {
        writeDatabaseAndCacheHelper(i, "satellite_esos_supported", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda4
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setSatelliteESOSSupported(((Integer) obj2).intValue());
            }
        });
    }

    public void setIsSatelliteProvisionedForNonIpDatagram(int i, int i2) {
        writeDatabaseAndCacheHelper(i, "is_satellite_provisioned_for_non_ip_datagram", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda2
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setIsSatelliteProvisionedForNonIpDatagram(((Integer) obj2).intValue());
            }
        });
    }

    public void setIsPrivateNetwork(int i, int i2) {
        writeDatabaseAndCacheHelper(i, "is_private_network", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda10
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setIsPrivateNetwork(((Integer) obj2).intValue());
            }
        });
    }

    public void setSatelliteEntitlementBarredPlmns(int i, String str) throws Throwable {
        writeDatabaseAndCacheHelper(i, "satellite_entitlement_barred_plmns", str, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda223
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setSatelliteEntitlementBarredPlmnsList((String) obj2);
            }
        });
    }

    public void setSatelliteEntitlementDataPlanForPlmns(int i, String str) throws Throwable {
        writeDatabaseAndCacheHelper(i, "satellite_entitlement_data_plan_plmns", str, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda36
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setSatelliteEntitlementDataPlanForPlmns((String) obj2);
            }
        });
    }

    public void setSatelliteEntitlementPlmnServiceTypeMap(int i, String str) throws Throwable {
        writeDatabaseAndCacheHelper(i, "satellite_entitlement_service_type_map", str, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda244
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setSatelliteEntitlementPlmnServiceTypes((String) obj2);
            }
        });
    }

    public void setSatelliteEntitlementPlmnDataServicePolicy(int i, String str) throws Throwable {
        writeDatabaseAndCacheHelper(i, "satellite_entitlement_data_service_policy", str, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda231
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setSatellitePlmnsDataServicePolicy((String) obj2);
            }
        });
    }

    public void setSatelliteEntitlementPlmnVoiceServicePolicy(int i, String str) throws Throwable {
        writeDatabaseAndCacheHelper(i, "satellite_entitlement_voice_service_policy", str, new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda237
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setSatellitePlmnsVoiceServicePolicy((String) obj2);
            }
        });
    }

    public void setStreamingAppMaxDownlinkKbps(int i, long j) throws Throwable {
        writeDatabaseAndCacheHelper(i, "streaming_app_max_downlink_kbps", Long.valueOf(j), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda34
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setStreamingAppMaxDownlinkKbps(((Long) obj2).longValue());
            }
        });
    }

    public void setStreamingAppMaxUplinkKbps(int i, long j) throws Throwable {
        writeDatabaseAndCacheHelper(i, "streaming_app_max_uplink_kbps", Long.valueOf(j), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda230
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setStreamingAppMaxUplinkKbps(((Long) obj2).longValue());
            }
        });
    }

    public void reloadDatabaseSync() {
        logl("reloadDatabaseSync");
        loadDatabaseInternal();
    }

    private void loadDatabaseInternal() {
        logl("loadDatabaseInternal");
        Cursor cursorQuery = this.mContext.getContentResolver().query(Telephony.SimInfo.CONTENT_URI, null, null, null, null);
        try {
            this.mReadWriteLock.writeLock().lock();
            try {
                HashMap map = new HashMap();
                boolean z = false;
                while (cursorQuery != null && cursorQuery.moveToNext()) {
                    final SubscriptionInfoInternal subscriptionInfoInternalCreateSubscriptionInfoFromCursor = createSubscriptionInfoFromCursor(cursorQuery);
                    map.put(Integer.valueOf(subscriptionInfoInternalCreateSubscriptionInfoFromCursor.getSubscriptionId()), subscriptionInfoInternalCreateSubscriptionInfoFromCursor);
                    if (!Objects.equals(this.mAllSubscriptionInfoInternalCache.get(Integer.valueOf(subscriptionInfoInternalCreateSubscriptionInfoFromCursor.getSubscriptionId())), subscriptionInfoInternalCreateSubscriptionInfoFromCursor)) {
                        if (SubscriptionManager.isValidSlotIndex(subscriptionInfoInternalCreateSubscriptionInfoFromCursor.getSimSlotIndex())) {
                            logl("loadDatabaseInternal will notify change for slot=" + subscriptionInfoInternalCreateSubscriptionInfoFromCursor.getSimSlotIndex());
                            this.mCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda48
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$loadDatabaseInternal$7(subscriptionInfoInternalCreateSubscriptionInfoFromCursor);
                                }
                            });
                        }
                        z = true;
                    }
                }
                if (z) {
                    this.mAllSubscriptionInfoInternalCache.clear();
                    this.mAllSubscriptionInfoInternalCache.putAll(map);
                    logl("Loaded " + this.mAllSubscriptionInfoInternalCache.size() + " records from the subscription database.");
                    this.mAllSubscriptionInfoInternalCache.forEach(new BiConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda49
                        @Override // java.util.function.BiConsumer
                        public final void accept(Object obj, Object obj2) {
                            this.f$0.lambda$loadDatabaseInternal$8((Integer) obj, (SubscriptionInfoInternal) obj2);
                        }
                    });
                }
                this.mReadWriteLock.writeLock().unlock();
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
            } catch (Throwable th) {
                this.mReadWriteLock.writeLock().unlock();
                throw th;
            }
        } catch (Throwable th2) {
            if (cursorQuery == null) {
                throw th2;
            }
            try {
                cursorQuery.close();
                throw th2;
            } catch (Throwable th3) {
                th2.addSuppressed(th3);
                throw th2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadDatabaseInternal$7(SubscriptionInfoInternal subscriptionInfoInternal) {
        this.mCallback.onSubscriptionChanged(subscriptionInfoInternal.getSubscriptionId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadDatabaseInternal$8(Integer num, SubscriptionInfoInternal subscriptionInfoInternal) {
        log("  " + subscriptionInfoInternal.toString());
    }

    private void initializeDatabase() {
        if (this.mAsyncMode) {
            post(new Runnable() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$initializeDatabase$9();
                }
            });
            return;
        }
        synchronized (this) {
            loadDatabaseInternal();
            this.mDatabaseInitialized = true;
            SubscriptionDatabaseManagerCallback subscriptionDatabaseManagerCallback = this.mCallback;
            Objects.requireNonNull(subscriptionDatabaseManagerCallback);
            subscriptionDatabaseManagerCallback.invokeFromExecutor(new SubscriptionDatabaseManager$$ExternalSyntheticLambda7(subscriptionDatabaseManagerCallback));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initializeDatabase$9() {
        synchronized (this) {
            loadDatabaseInternal();
            this.mDatabaseInitialized = true;
            SubscriptionDatabaseManagerCallback subscriptionDatabaseManagerCallback = this.mCallback;
            Objects.requireNonNull(subscriptionDatabaseManagerCallback);
            subscriptionDatabaseManagerCallback.invokeFromExecutor(new SubscriptionDatabaseManager$$ExternalSyntheticLambda7(subscriptionDatabaseManagerCallback));
        }
    }

    protected SubscriptionInfoInternal createSubscriptionInfoFromCursor(Cursor cursor) {
        SubscriptionInfoInternal.Builder builder = new SubscriptionInfoInternal.Builder();
        builder.setId(cursor.getInt(cursor.getColumnIndexOrThrow("_id"))).setIccId(TextUtils.emptyIfNull(cursor.getString(cursor.getColumnIndexOrThrow("icc_id")))).setSimSlotIndex(cursor.getInt(cursor.getColumnIndexOrThrow("sim_id"))).setDisplayName(TextUtils.emptyIfNull(cursor.getString(cursor.getColumnIndexOrThrow("display_name")))).setCarrierName(TextUtils.emptyIfNull(cursor.getString(cursor.getColumnIndexOrThrow("carrier_name")))).setDisplayNameSource(cursor.getInt(cursor.getColumnIndexOrThrow("name_source"))).setIconTint(cursor.getInt(cursor.getColumnIndexOrThrow("color"))).setNumber(TextUtils.emptyIfNull(cursor.getString(cursor.getColumnIndexOrThrow(IccProvider.STR_NUMBER)))).setDataRoaming(cursor.getInt(cursor.getColumnIndexOrThrow("data_roaming"))).setMcc(TextUtils.emptyIfNull(cursor.getString(cursor.getColumnIndexOrThrow("mcc_string")))).setMnc(TextUtils.emptyIfNull(cursor.getString(cursor.getColumnIndexOrThrow("mnc_string")))).setEhplmns(TextUtils.emptyIfNull(cursor.getString(cursor.getColumnIndexOrThrow("ehplmns")))).setHplmns(TextUtils.emptyIfNull(cursor.getString(cursor.getColumnIndexOrThrow("hplmns")))).setEmbedded(cursor.getInt(cursor.getColumnIndexOrThrow("is_embedded")));
        String strEmptyIfNull = TextUtils.emptyIfNull(cursor.getString(cursor.getColumnIndexOrThrow("card_id")));
        builder.setCardString(strEmptyIfNull);
        int iConvertToPublicCardId = this.mUiccController.convertToPublicCardId(strEmptyIfNull);
        byte[] blob = cursor.getBlob(cursor.getColumnIndexOrThrow("access_rules"));
        if (blob != null) {
            builder.setNativeAccessRules(blob);
        }
        byte[] blob2 = cursor.getBlob(cursor.getColumnIndexOrThrow("access_rules_from_carrier_configs"));
        if (blob2 != null) {
            builder.setCarrierConfigAccessRules(blob2);
        }
        byte[] blob3 = cursor.getBlob(cursor.getColumnIndexOrThrow("rcs_config"));
        if (blob3 != null) {
            builder.setRcsConfig(blob3);
        }
        builder.setCardId(iConvertToPublicCardId).setRemovableEmbedded(cursor.getInt(cursor.getColumnIndexOrThrow("is_removable"))).setCellBroadcastExtremeThreatAlertEnabled(cursor.getInt(cursor.getColumnIndexOrThrow("enable_cmas_extreme_threat_alerts"))).setCellBroadcastSevereThreatAlertEnabled(cursor.getInt(cursor.getColumnIndexOrThrow("enable_cmas_severe_threat_alerts"))).setCellBroadcastAmberAlertEnabled(cursor.getInt(cursor.getColumnIndexOrThrow("enable_cmas_amber_alerts"))).setCellBroadcastEmergencyAlertEnabled(cursor.getInt(cursor.getColumnIndexOrThrow("enable_emergency_alerts"))).setCellBroadcastAlertSoundDuration(cursor.getInt(cursor.getColumnIndexOrThrow("alert_sound_duration"))).setCellBroadcastAlertReminderInterval(cursor.getInt(cursor.getColumnIndexOrThrow("alert_reminder_interval"))).setCellBroadcastAlertVibrationEnabled(cursor.getInt(cursor.getColumnIndexOrThrow("enable_alert_vibrate"))).setCellBroadcastAlertSpeechEnabled(cursor.getInt(cursor.getColumnIndexOrThrow("enable_alert_speech"))).setCellBroadcastEtwsTestAlertEnabled(cursor.getInt(cursor.getColumnIndexOrThrow("enable_etws_test_alerts"))).setCellBroadcastAreaInfoMessageEnabled(cursor.getInt(cursor.getColumnIndexOrThrow("enable_channel_50_alerts"))).setCellBroadcastTestAlertEnabled(cursor.getInt(cursor.getColumnIndexOrThrow("enable_cmas_test_alerts"))).setCellBroadcastOptOutDialogEnabled(cursor.getInt(cursor.getColumnIndexOrThrow("show_cmas_opt_out_dialog"))).setEnhanced4GModeEnabled(cursor.getInt(cursor.getColumnIndexOrThrow("volte_vt_enabled"))).setVideoTelephonyEnabled(cursor.getInt(cursor.getColumnIndexOrThrow("vt_ims_enabled"))).setWifiCallingEnabled(cursor.getInt(cursor.getColumnIndexOrThrow("wfc_ims_enabled"))).setWifiCallingMode(cursor.getInt(cursor.getColumnIndexOrThrow("wfc_ims_mode"))).setWifiCallingModeForRoaming(cursor.getInt(cursor.getColumnIndexOrThrow("wfc_ims_roaming_mode"))).setWifiCallingEnabledForRoaming(cursor.getInt(cursor.getColumnIndexOrThrow("wfc_ims_roaming_enabled"))).setOpportunistic(cursor.getInt(cursor.getColumnIndexOrThrow("is_opportunistic"))).setGroupUuid(TextUtils.emptyIfNull(cursor.getString(cursor.getColumnIndexOrThrow("group_uuid")))).setCountryIso(TextUtils.emptyIfNull(cursor.getString(cursor.getColumnIndexOrThrow("iso_country_code")))).setCarrierId(cursor.getInt(cursor.getColumnIndexOrThrow("carrier_id"))).setProfileClass(cursor.getInt(cursor.getColumnIndexOrThrow("profile_class"))).setType(cursor.getInt(cursor.getColumnIndexOrThrow("subscription_type"))).setGroupOwner(TextUtils.emptyIfNull(cursor.getString(cursor.getColumnIndexOrThrow("group_owner")))).setEnabledMobileDataPolicies(TextUtils.emptyIfNull(cursor.getString(cursor.getColumnIndexOrThrow("enabled_mobile_data_policies")))).setImsi(TextUtils.emptyIfNull(cursor.getString(cursor.getColumnIndexOrThrow("imsi")))).setUiccApplicationsEnabled(cursor.getInt(cursor.getColumnIndexOrThrow("uicc_applications_enabled"))).setRcsUceEnabled(cursor.getInt(cursor.getColumnIndexOrThrow("ims_rcs_uce_enabled"))).setCrossSimCallingEnabled(cursor.getInt(cursor.getColumnIndexOrThrow("cross_sim_calling_enabled"))).setAllowedNetworkTypesForReasons(TextUtils.emptyIfNull(cursor.getString(cursor.getColumnIndexOrThrow("allowed_network_types_for_reasons")))).setDeviceToDeviceStatusSharingPreference(cursor.getInt(cursor.getColumnIndexOrThrow("d2d_sharing_status"))).setVoImsOptInEnabled(cursor.getInt(cursor.getColumnIndexOrThrow("voims_opt_in_status"))).setDeviceToDeviceStatusSharingContacts(TextUtils.emptyIfNull(cursor.getString(cursor.getColumnIndexOrThrow("d2d_sharing_contacts")))).setNrAdvancedCallingEnabled(cursor.getInt(cursor.getColumnIndexOrThrow("nr_advanced_calling_enabled"))).setNumberFromCarrier(TextUtils.emptyIfNull(cursor.getString(cursor.getColumnIndexOrThrow("phone_number_source_carrier")))).setNumberFromIms(TextUtils.emptyIfNull(cursor.getString(cursor.getColumnIndexOrThrow("phone_number_source_ims")))).setPortIndex(cursor.getInt(cursor.getColumnIndexOrThrow("port_index"))).setUsageSetting(cursor.getInt(cursor.getColumnIndexOrThrow("usage_setting"))).setLastUsedTPMessageReference(cursor.getInt(cursor.getColumnIndexOrThrow("tp_message_ref"))).setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("user_handle"))).setSatelliteEnabled(cursor.getInt(cursor.getColumnIndexOrThrow("satellite_enabled"))).setSatelliteAttachEnabledForCarrier(cursor.getInt(cursor.getColumnIndexOrThrow("satellite_attach_enabled_for_carrier"))).setServiceCapabilities(cursor.getInt(cursor.getColumnIndexOrThrow("service_capabilities"))).setSatelliteEntitlementStatus(cursor.getInt(cursor.getColumnIndexOrThrow("satellite_entitlement_status"))).setSatelliteEntitlementPlmns(cursor.getString(cursor.getColumnIndexOrThrow("satellite_entitlement_plmns"))).setIsSatelliteProvisionedForNonIpDatagram(cursor.getInt(cursor.getColumnIndexOrThrow("is_satellite_provisioned_for_non_ip_datagram"))).setSatelliteEntitlementBarredPlmnsList(cursor.getString(cursor.getColumnIndexOrThrow("satellite_entitlement_barred_plmns"))).setSatelliteEntitlementDataPlanForPlmns(cursor.getString(cursor.getColumnIndexOrThrow("satellite_entitlement_data_plan_plmns"))).setSatelliteEntitlementPlmnServiceTypes(cursor.getString(cursor.getColumnIndexOrThrow("satellite_entitlement_service_type_map"))).setSatellitePlmnsDataServicePolicy(cursor.getString(cursor.getColumnIndexOrThrow("satellite_entitlement_data_service_policy"))).setSatellitePlmnsVoiceServicePolicy(cursor.getString(cursor.getColumnIndexOrThrow("satellite_entitlement_voice_service_policy")));
        builder.setOnlyNonTerrestrialNetwork(cursor.getInt(cursor.getColumnIndexOrThrow("is_only_ntn")));
        builder.setTransferStatus(cursor.getInt(cursor.getColumnIndexOrThrow("transfer_status")));
        builder.setNumberFromTs43(TextUtils.emptyIfNull(cursor.getString(cursor.getColumnIndexOrThrow("phone_number_source_ts43"))));
        builder.setSatelliteESOSSupported(cursor.getInt(cursor.getColumnIndexOrThrow("satellite_esos_supported")));
        builder.setIsPrivateNetwork(cursor.getInt(cursor.getColumnIndexOrThrow("is_private_network")));
        builder.setStreamingAppMaxDownlinkKbps(cursor.getLong(cursor.getColumnIndexOrThrow("streaming_app_max_downlink_kbps"))).setStreamingAppMaxUplinkKbps(cursor.getLong(cursor.getColumnIndexOrThrow("streaming_app_max_uplink_kbps")));
        return builder.build();
    }

    public void syncToGroup(int i) {
        if (!this.mAllSubscriptionInfoInternalCache.containsKey(Integer.valueOf(i))) {
            RILUtils$$ExternalSyntheticBUOutline0.m("Invalid subId ", i);
            return;
        }
        for (String str : GROUP_SHARING_COLUMNS) {
            setSubscriptionProperty(i, str, getSubscriptionProperty(i, str));
        }
    }

    public SubscriptionInfoInternal getSubscriptionInfoInternal(int i) {
        this.mReadWriteLock.readLock().lock();
        try {
            return (SubscriptionInfoInternal) this.mAllSubscriptionInfoInternalCache.get(Integer.valueOf(i));
        } finally {
            this.mReadWriteLock.readLock().unlock();
        }
    }

    public List<SubscriptionInfoInternal> getAllSubscriptions() {
        this.mReadWriteLock.readLock().lock();
        try {
            return new ArrayList(this.mAllSubscriptionInfoInternalCache.values());
        } finally {
            this.mReadWriteLock.readLock().unlock();
        }
    }

    public SubscriptionInfoInternal getSubscriptionInfoInternalByIccId(final String str) {
        this.mReadWriteLock.readLock().lock();
        try {
            return (SubscriptionInfoInternal) this.mAllSubscriptionInfoInternalCache.values().stream().filter(new Predicate() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda24
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return ((SubscriptionInfoInternal) obj).getIccId().equals(str);
                }
            }).findFirst().orElse(null);
        } finally {
            this.mReadWriteLock.readLock().unlock();
        }
    }

    public void setTransferStatus(int i, int i2) throws Throwable {
        writeDatabaseAndCacheHelper(i, "transfer_status", Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda46
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((SubscriptionInfoInternal.Builder) obj).setTransferStatus(((Integer) obj2).intValue());
            }
        });
    }

    public String serializeMapToJsonString(Map<String, Integer> map) {
        JSONObject jSONObject = new JSONObject();
        if (map == null || map.isEmpty()) {
            return jSONObject.toString();
        }
        try {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                jSONObject.put(entry.getKey(), entry.getValue());
            }
        } catch (JSONException e) {
            loge("serializeMapToCV JSON error: " + e.getMessage());
        }
        return jSONObject.toString();
    }

    public String serializeMapListToJsonString(Map<String, List<Integer>> map) {
        JSONObject jSONObject = new JSONObject();
        if (map == null || map.isEmpty()) {
            return jSONObject.toString();
        }
        try {
            for (Map.Entry<String, List<Integer>> entry : map.entrySet()) {
                JSONArray jSONArray = new JSONArray();
                Iterator<Integer> it = entry.getValue().iterator();
                while (it.hasNext()) {
                    jSONArray.put(it.next().intValue());
                }
                jSONObject.put(entry.getKey(), jSONArray);
            }
        } catch (JSONException e) {
            loge("serializeMapListToCV JSON error: " + e.getMessage());
        }
        return jSONObject.toString();
    }

    private void log(String str) {
        Rlog.d("SDMGR", str);
    }

    private void loge(String str) {
        Rlog.e("SDMGR", str);
    }

    private void logel(String str) {
        loge(str);
        this.mLocalLog.log(str);
    }

    private void logl(String str) {
        log(str);
        this.mLocalLog.log(str);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        final AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
        androidUtilIndentingPrintWriter.println(SubscriptionDatabaseManager.class.getSimpleName().concat(":"));
        androidUtilIndentingPrintWriter.increaseIndent();
        androidUtilIndentingPrintWriter.println("All subscriptions:");
        androidUtilIndentingPrintWriter.increaseIndent();
        this.mReadWriteLock.readLock().lock();
        try {
            this.mAllSubscriptionInfoInternalCache.forEach(new BiConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionDatabaseManager$$ExternalSyntheticLambda219
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    androidUtilIndentingPrintWriter.println((SubscriptionInfoInternal) obj2);
                }
            });
            this.mReadWriteLock.readLock().unlock();
            androidUtilIndentingPrintWriter.decreaseIndent();
            androidUtilIndentingPrintWriter.println();
            androidUtilIndentingPrintWriter.println("mAsyncMode=" + this.mAsyncMode);
            synchronized (this) {
                androidUtilIndentingPrintWriter.println("mDatabaseInitialized=" + this.mDatabaseInitialized);
            }
            androidUtilIndentingPrintWriter.println("mReadWriteLock=" + this.mReadWriteLock);
            androidUtilIndentingPrintWriter.println();
            androidUtilIndentingPrintWriter.println("Local log:");
            androidUtilIndentingPrintWriter.increaseIndent();
            this.mLocalLog.dump(fileDescriptor, printWriter, strArr);
            androidUtilIndentingPrintWriter.decreaseIndent();
            androidUtilIndentingPrintWriter.decreaseIndent();
        } catch (Throwable th) {
            this.mReadWriteLock.readLock().unlock();
            throw th;
        }
    }

    protected SubscriptionInfoInternal.Builder onCreateSubscriptionInfoInternalBuilder(SubscriptionInfoInternal subscriptionInfoInternal) {
        return new SubscriptionInfoInternal.Builder(subscriptionInfoInternal);
    }
}
