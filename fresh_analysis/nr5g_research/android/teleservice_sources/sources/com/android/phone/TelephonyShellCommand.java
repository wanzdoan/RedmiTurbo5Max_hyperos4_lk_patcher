package com.android.phone;

import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.os.Binder;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.ServiceSpecificException;
import android.os.UserHandle;
import android.provider.BlockedNumberContract;
import android.telephony.BarringInfo;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.TelephonyRegistryManager;
import android.telephony.emergency.EmergencyNumber;
import android.telephony.ims.ImsException;
import android.telephony.ims.RcsContactUceCapability;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.telephony.BasicShellCommandHandler;
import com.android.internal.telephony.IIntegerConsumer;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.TelephonyPermissions;
import com.android.internal.telephony.d2d.Communicator;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.internal.util.FunctionalUtils;
import com.android.phone.callcomposer.CallComposerPictureManager;
import com.android.phone.utils.CarrierAllowListInfo;
import com.xiaomi.settingsdk.backup.data.KeyStringSettingItem;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import miuix.reflect.Reflects$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyShellCommand extends BasicShellCommandHandler {
    private static final Map CC_TYPE_MAP;
    private static final int[] ROUTING_TYPES = {0, 1, 2};
    private static final Map TEST_FEATURE_TAG_MAP;
    private final String QUOTES = "\"";
    private CarrierConfigManager mCarrierConfigManager;
    private Context mContext;
    private FakeRil mFakeRil;
    private final ITelephony mInterface;
    private SubscriptionManager mSubscriptionManager;
    private TelephonyRegistryManager mTelephonyRegistryManager;

    enum CcType {
        BOOLEAN,
        BOOLEAN_ARRAY,
        DOUBLE,
        DOUBLE_ARRAY,
        INT,
        INT_ARRAY,
        LONG,
        LONG_ARRAY,
        STRING,
        STRING_ARRAY,
        PERSISTABLE_BUNDLE,
        UNKNOWN
    }

    static {
        CcType ccType = CcType.STRING;
        CcType ccType2 = CcType.STRING_ARRAY;
        CC_TYPE_MAP = Map.ofEntries(Map.entry("gps.a_glonass_pos_protocol_select", ccType), Map.entry("gps.es_extension_sec", ccType), Map.entry("gps.gps_lock", ccType), Map.entry("gps.lpp_profile", ccType), Map.entry("gps.nfw_proxy_apps", ccType), Map.entry("gps.supl_es", ccType), Map.entry("gps.supl_host", ccType), Map.entry("gps.supl_mode", ccType), Map.entry("gps.supl_port", ccType), Map.entry("gps.supl_ver", ccType), Map.entry("gps.use_emergency_pdn_for_emergency_supl", ccType), Map.entry("carrier_app_no_wake_signal_config", ccType2), Map.entry("carrier_app_wake_signal_config", ccType2), Map.entry("call_screening_app", ccType), Map.entry("emailGatewayNumber", ccType), Map.entry("httpParams", ccType), Map.entry("naiSuffix", ccType), Map.entry("uaProfTagName", ccType), Map.entry("uaProfUrl", ccType), Map.entry("userAgent", ccType), Map.entry("ratchet_rat_families", ccType2));
        ArrayMap arrayMap = new ArrayMap(18);
        arrayMap.put("chat_v1", Collections.singleton("+g.3gpp.iari-ref=\"urn%3Aurn-7%3A3gpp-application.ims.iari.rcse.im\""));
        arrayMap.put("chat_v2", Collections.singleton("+g.3gpp.icsi-ref=\"urn%3Aurn-7%3A3gpp-service.ims.icsi.oma.cpm.session\""));
        arrayMap.put("ft", Collections.singleton("+g.3gpp.iari-ref=\"urn%3Aurn-7%3A3gpp-application.ims.iari.rcs.fthttp\""));
        arrayMap.put("ft_sms", Collections.singleton("+g.3gpp.iari-ref=\"urn%3Aurn-7%3A3gpp-application.ims.iari.rcs.ftsms\""));
        arrayMap.put("mmtel", Collections.singleton("+g.3gpp.icsi-ref=\"urn%3Aurn-7%3A3gpp-service.ims.icsi.mmtel\""));
        arrayMap.put("mmtel_vt", new ArraySet(Arrays.asList("+g.3gpp.icsi-ref=\"urn%3Aurn-7%3A3gpp-service.ims.icsi.mmtel\"", "video")));
        arrayMap.put("geo_push", Collections.singleton("+g.3gpp.iari-ref=\"urn%3Aurn-7%3A3gpp-application.ims.iari.rcs.geopush\""));
        arrayMap.put("geo_push_sms", Collections.singleton("+g.3gpp.iari-ref=\"urn%3Aurn-7%3A3gpp-application.ims.iari.rcs.geosms\""));
        arrayMap.put("call_comp", Collections.singleton("+g.3gpp.icsi-ref=\"urn%3Aurn-7%3A3gpp-service.ims.icsi.gsma.callcomposer\""));
        arrayMap.put("call_comp_mmtel", Collections.singleton("+g.gsma.callcomposer"));
        arrayMap.put("call_post", Collections.singleton("+g.3gpp.icsi-ref=\"urn%3Aurn-7%3A3gpp-service.ims.icsi.gsma.callunanswered\""));
        arrayMap.put("map", Collections.singleton("+g.3gpp.icsi-ref=\"urn%3Aurn-7%3A3gpp-service.ims.icsi.gsma.sharedmap\""));
        arrayMap.put("sketch", Collections.singleton("+g.3gpp.icsi-ref=\"urn%3Aurn-7%3A3gpp-service.ims.icsi.gsma.sharedsketch\""));
        arrayMap.put("chatbot", new ArraySet(Arrays.asList("+g.3gpp.iari-ref=\"urn%3Aurn-7%3A3gpp-application.ims.iari.rcs.chatbot\"", "+g.gsma.rcs.botversion=\"#=1\"")));
        arrayMap.put("chatbot_v2", new ArraySet(Arrays.asList("+g.3gpp.iari-ref=\"urn%3Aurn-7%3A3gpp-application.ims.iari.rcs.chatbot\"", "+g.gsma.rcs.botversion=\"#=1,#=2\"")));
        arrayMap.put("chatbot_sa", new ArraySet(Arrays.asList("+g.3gpp.iari-ref=\"urn%3Aurn-7%3A3gpp-application.ims.iari.rcs.chatbot.sa\"", "+g.gsma.rcs.botversion=\"#=1\"")));
        arrayMap.put("chatbot_sa_v2", new ArraySet(Arrays.asList("+g.3gpp.iari-ref=\"urn%3Aurn-7%3A3gpp-application.ims.iari.rcs.chatbot.sa\"", "+g.gsma.rcs.botversion=\"#=1,#=2\"")));
        arrayMap.put("chatbot_role", Collections.singleton("+g.gsma.rcs.isbot"));
        TEST_FEATURE_TAG_MAP = Collections.unmodifiableMap(arrayMap);
    }

    class CcOptionParseResult {
        public boolean mPersistent;
        public int mSubId;

        private CcOptionParseResult(TelephonyShellCommand telephonyShellCommand) {
        }
    }

    public TelephonyShellCommand(ITelephony iTelephony, Context context) {
        this.mInterface = iTelephony;
        this.mCarrierConfigManager = (CarrierConfigManager) context.getSystemService("carrier_config");
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService("telephony_subscription_service");
        this.mTelephonyRegistryManager = (TelephonyRegistryManager) context.getSystemService("telephony_registry");
        this.mContext = context;
        this.mFakeRil = new FakeRil(context);
    }

    @Override // com.android.internal.telephony.BasicShellCommandHandler
    public int onCommand(String str) {
        if (str == null) {
            return handleDefaultCommands(null);
        }
        str.hashCode();
        switch (str) {
            case "numverify":
                return handleNumberVerificationCommand();
            case "set-satellite-subscriberid-list-changed-intent-component":
                return handleSetSatelliteSubscriberIdListChangedIntentComponent();
            case "restart-modem":
                return handleRestartModemCommand();
            case "override-satellite-entitlement-status-response-for-cts-test":
                return handleOverrideSatelliteEntitlementStatusResponseForCtsTest();
            case "set-satellite-controller-timeout-duration":
                return handleSetSatelliteControllerTimeoutDuration();
            case "set-max-allowed-satellite-data-mode-for-cts-test":
                return handleSetMaxAllowedSatelliteDataModeForCtsTest();
            case "get-sim-slots-mapping":
                return handleGetSimSlotsMapping();
            case "has-carrier-privileges":
                return handleHasCarrierPrivilegesCommand();
            case "set-sim-count":
                return handleSetSimCount();
            case "delete_imsi_key":
                return handleDeleteTestImsiKey();
            case "set-allowed-network-types-for-users":
            case "get-allowed-network-types-for-users":
                return handleAllowedNetworkTypesCommand(str);
            case "set-satellite-service-package-name":
                return handleSetSatelliteServicePackageNameCommand();
            case "set-satellite-listening-timeout-duration":
                return handleSetSatelliteListeningTimeoutDuration();
            case "unattended-reboot":
                return handleUnattendedReboot();
            case "callcomposer":
                return handleCallComposerCommand();
            case "set-oem-enabled-satellite-provision-status":
                return handleSetOemEnabledSatelliteProvisionStatus();
            case "carrier_restriction_status_test":
                return handleCarrierRestrictionStatusCommand();
            case "set-cts-mode":
                return handleSetCtsMode();
            case "end-block-suppression":
                return handleEndBlockSuppressionCommand();
            case "set-support-disable-satellite-while-enable-in-progress":
                return handleSetSupportDisableSatelliteWhileEnableInProgress();
            case "override-config-data-version":
                return handleOverrideConfigDataVersion();
            case "override-satellite-entitlement-entilement-query-conditions":
                return handleOverrideSatelliteEntilementQueryConditions();
            case "barring":
                return handleBarringCommand();
            case "set-is-satellite-communication-allowed-for-current-location-cache":
                return handleSetIsSatelliteCommunicationAllowedForCurrentLocationCache();
            case "enable-physical-subscription":
                return handleEnablePhysicalSubscription(true);
            case "uncap-max-allowed-satellite-data-mode":
                return handleUncapMaxAllowedSatelliteDataMode();
            case "set-carrier-service-package-override":
                return setCarrierServicePackageOverride();
            case "disable-physical-subscription":
                return handleEnablePhysicalSubscription(false);
            case "set-satellite-access-allowed-for-subscriptions":
                return handleSetSatelliteAccessAllowedForSubscriptions();
            case "cc":
                return handleCcCommand();
            case "d2d":
                return handleD2dCommand();
            case "gba":
                return handleGbaCommand();
            case "ims":
                return handleImsCommand();
            case "src":
                return handleSingleRegistrationConfigCommand();
            case "uce":
                return handleRcsUceCommand();
            case "data":
                return handleDataTestModeCommand();
            case "get-phone-number":
                return handleGetPhoneNumber();
            case "set-country-codes":
                return handleSetCountryCodes();
            case "radio":
                return handleRadioCommand();
            case "set-satellite-access-control-overlay-configs":
                return handleSetSatelliteAccessControlOverlayConfigs();
            case "set-satellite-pointing-ui-class-name":
                return handleSetSatellitePointingUiClassNameCommand();
            case "thermal-mitigation":
                return handleThermalMitigationCommand();
            case "domainselection":
                return handleDomainSelectionCommand();
            case "set-satellite-ignore-cellular-service-state":
                return handleSetSatelliteIgnoreCellularServiceState();
            case "emergency-number-test-mode":
                return handleEmergencyNumberTestModeCommand();
            case "set-satellite-gateway-service-package-name":
                return handleSetSatelliteGatewayServicePackageNameCommand();
            case "set-emergency-call-to-satellite-handover-type":
                return handleSetEmergencyCallToSatelliteHandoverType();
            case "send-ril-event":
                return handleRilEvent();
            case "set-datagram-controller-timeout-duration":
                return handleSetDatagramControllerTimeoutDuration();
            case "set-datagram-controller-boolean-config":
                return handleSetDatagramControllerBooleanConfig();
            case "set-satellite-tn-scanning-support":
                return handleSetSatelliteTnScanningSupport();
            case "add-attach-restriction-for-carrier":
                return handleAddAttachRestrictionForCarrier(str);
            case "emergency-callback-mode":
                return handleEmergencyCallbackModeCommand();
            case "set-satellite-access-restriction-checking-result":
                return handleOverrideCarrierRoamingNtnEligibilityChanged();
            case "set-should-send-datagram-to-modem-in-demo-mode":
                return handleSetShouldSendDatagramToModemInDemoMode();
            case "get-imei":
                return handleGetImei();
            case "clear-carrier-service-package-override":
                return clearCarrierServicePackageOverride();
            case "remove-attach-restriction-for-carrier":
                return handleRemoveAttachRestrictionForCarrier(str);
            case "set-satellite-ignore-plmn-list-from-storage":
                return handleSetSatelliteIgnorePlmnListFromStorage();
            default:
                Log.d("TelephonyShellCommand", "handleDefaultCommands: cmd=".concat(str));
                return handleDefaultCommands(str);
        }
    }

    @Override // com.android.internal.telephony.BasicShellCommandHandler
    public void onHelp() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("Telephony Commands:");
        outPrintWriter.println("  help");
        outPrintWriter.println("    Print this help text.");
        outPrintWriter.println("  ims");
        outPrintWriter.println("    IMS Commands.");
        outPrintWriter.println("  uce");
        outPrintWriter.println("    RCS User Capability Exchange Commands.");
        outPrintWriter.println("  emergency-number-test-mode");
        outPrintWriter.println("    Emergency Number Test Mode Commands.");
        outPrintWriter.println("  end-block-suppression");
        outPrintWriter.println("    End Block Suppression command.");
        outPrintWriter.println("  data");
        outPrintWriter.println("    Data Test Mode Commands.");
        outPrintWriter.println("  cc");
        outPrintWriter.println("    Carrier Config Commands.");
        outPrintWriter.println("  gba");
        outPrintWriter.println("    GBA Commands.");
        outPrintWriter.println("  src");
        outPrintWriter.println("    RCS VoLTE Single Registration Config Commands.");
        outPrintWriter.println("  restart-modem");
        outPrintWriter.println("    Restart modem command.");
        outPrintWriter.println("  unattended-reboot");
        outPrintWriter.println("    Prepare for unattended reboot.");
        outPrintWriter.println("  has-carrier-privileges [package]");
        outPrintWriter.println("    Query carrier privilege status for a package. Prints true or false.");
        outPrintWriter.println("  get-allowed-network-types-for-users");
        outPrintWriter.println("    Get the Allowed Network Types.");
        outPrintWriter.println("  set-allowed-network-types-for-users");
        outPrintWriter.println("    Set the Allowed Network Types.");
        outPrintWriter.println("  radio");
        outPrintWriter.println("    Radio Commands.");
        onHelpIms();
        onHelpUce();
        onHelpEmergencyNumber();
        onHelpEndBlockSupperssion();
        onHelpDataTestMode();
        onHelpCc();
        onHelpGba();
        onHelpSrc();
        onHelpD2D();
        onHelpDisableOrEnablePhysicalSubscription();
        onHelpAllowedNetworkTypes();
        onHelpRadio();
        onHelpImei();
        onHelpSatellite();
        onHelpDomainSelection();
        onHelpRilEvent();
        onHelpSimCommands();
        onHelpGetPhoneNumber();
    }

    private void onHelpGetPhoneNumber() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("  get-phone-number <subId> <source>");
        outPrintWriter.println("    Returns the phone number for the subId from the specific source.");
        outPrintWriter.println("    Source values: 1 (UICC), 2 (CARRIER), 3 (IMS), 4 (TS43).");
    }

    private void onHelpSimCommands() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("SIM Commands:");
        outPrintWriter.println("  set-sim-count [SIM count device supported");
    }

    private void onHelpD2D() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("D2D Comms Commands:");
        outPrintWriter.println("  d2d send TYPE VALUE");
        outPrintWriter.println("    Sends a D2D message of specified type and value.");
        outPrintWriter.println("    Type: 1 - " + Communicator.messageToString(1));
        outPrintWriter.println("    Type: 2 - " + Communicator.messageToString(2));
        outPrintWriter.println("    Type: 3 - " + Communicator.messageToString(3));
        outPrintWriter.println("    Type: 4 - " + Communicator.messageToString(4));
        outPrintWriter.println("  d2d transport TYPE");
        outPrintWriter.println("    Forces the specified D2D transport TYPE to be active.  Use the");
        outPrintWriter.println("    short class name of the transport; i.e. DtmfTransport or RtpTransport.");
        outPrintWriter.println("  d2d set-device-support true/default");
        outPrintWriter.println("    true - forces device support to be enabled for D2D.");
        outPrintWriter.println("    default - clear any previously set force-enable of D2D, reverting to ");
        outPrintWriter.println("    the current device's configuration.");
    }

    private void onHelpBarring() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("Barring Commands:");
        outPrintWriter.println("  barring send -s SLOT_ID -b BARRING_TYPE -c IS_CONDITIONALLY_BARRED -t CONDITIONAL_BARRING_TIME_SECS");
        outPrintWriter.println("    Notifies of a barring info change for the specified slot id.");
        outPrintWriter.println("    BARRING_TYPE: 0 for BARRING_TYPE_NONE");
        outPrintWriter.println("    BARRING_TYPE: 1 for BARRING_TYPE_UNCONDITIONAL");
        outPrintWriter.println("    BARRING_TYPE: 2 for BARRING_TYPE_CONDITIONAL");
        outPrintWriter.println("    BARRING_TYPE: -1 for BARRING_TYPE_UNKNOWN");
    }

    private void onHelpIms() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("IMS Commands:");
        outPrintWriter.println("  ims set-ims-service [-s SLOT_ID] [-u USER_ID] (-c | -d | -f) PACKAGE_NAME");
        outPrintWriter.println("    Sets the ImsService defined in PACKAGE_NAME to to be the bound");
        outPrintWriter.println("    ImsService. Options are:");
        outPrintWriter.println("      -s: the slot ID that the ImsService should be bound for. If no option");
        outPrintWriter.println("          is specified, it will choose the default voice SIM slot.");
        outPrintWriter.println("      -u: the user ID that the ImsService should be bound on. If no option");
        outPrintWriter.println("          is specified, the SYSTEM user ID will be preferred followed by the");
        outPrintWriter.println("          current user ID if they are different");
        outPrintWriter.println("      -c: Override the ImsService defined in the carrier configuration.");
        outPrintWriter.println("      -d: Override the ImsService defined in the device overlay.");
        outPrintWriter.println("      -f: Set the feature that this override if for, if no option is");
        outPrintWriter.println("          specified, the new package name will be used for all features.");
        outPrintWriter.println("  ims get-ims-service [-s SLOT_ID] [-c | -d]");
        outPrintWriter.println("    Gets the package name of the currently defined ImsService.");
        outPrintWriter.println("    Options are:");
        outPrintWriter.println("      -s: The SIM slot ID for the registered ImsService. If no option");
        outPrintWriter.println("          is specified, it will choose the default voice SIM slot.");
        outPrintWriter.println("      -c: The ImsService defined as the carrier configured ImsService.");
        outPrintWriter.println("      -d: The ImsService defined as the device default ImsService.");
        outPrintWriter.println("      -f: The feature type that the query will be requested for. If none is");
        outPrintWriter.println("          specified, the returned package name will correspond to MMTEL.");
        outPrintWriter.println("  ims clear-ims-service-override [-s SLOT_ID]");
        outPrintWriter.println("    Clear all carrier ImsService overrides. This does not work for device ");
        outPrintWriter.println("    configuration overrides. Options are:");
        outPrintWriter.println("      -s: The SIM slot ID for the registered ImsService. If no option");
        outPrintWriter.println("          is specified, it will choose the default voice SIM slot.");
        outPrintWriter.println("  ims enable [-s SLOT_ID]");
        outPrintWriter.println("    enables IMS for the SIM slot specified, or for the default voice SIM slot");
        outPrintWriter.println("    if none is specified.");
        outPrintWriter.println("  ims disable [-s SLOT_ID]");
        outPrintWriter.println("    disables IMS for the SIM slot specified, or for the default voice SIM");
        outPrintWriter.println("    slot if none is specified.");
        outPrintWriter.println("  ims conference-event-package [enable/disable]");
        outPrintWriter.println("    enables or disables handling or network conference event package data.");
    }

    private void onHelpUce() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("User Capability Exchange Commands:");
        outPrintWriter.println("  uce get-eab-contact [PHONE_NUMBER]");
        outPrintWriter.println("    Get the EAB contacts from the EAB database.");
        outPrintWriter.println("    Options are:");
        outPrintWriter.println("      PHONE_NUMBER: The phone numbers to be removed from the EAB databases");
        outPrintWriter.println("    Expected output format :");
        outPrintWriter.println("      [PHONE_NUMBER],[RAW_CONTACT_ID],[CONTACT_ID],[DATA_ID]");
        outPrintWriter.println("  uce remove-eab-contact [-s SLOT_ID] [PHONE_NUMBER]");
        outPrintWriter.println("    Remove the EAB contacts from the EAB database.");
        outPrintWriter.println("    Options are:");
        outPrintWriter.println("      -s: The SIM slot ID to read carrier config value for. If no option");
        outPrintWriter.println("          is specified, it will choose the default voice SIM slot.");
        outPrintWriter.println("      PHONE_NUMBER: The phone numbers to be removed from the EAB databases");
        outPrintWriter.println("  uce get-device-enabled");
        outPrintWriter.println("    Get the config to check whether the device supports RCS UCE or not.");
        outPrintWriter.println("  uce set-device-enabled true|false");
        outPrintWriter.println("    Set the device config for RCS User Capability Exchange to the value.");
        outPrintWriter.println("    The value could be true, false.");
        outPrintWriter.println("  uce override-published-caps [-s SLOT_ID] add|remove|clear [CAPABILITIES]");
        outPrintWriter.println("    Override the existing SIP PUBLISH with different capabilities.");
        outPrintWriter.println("    Options are:");
        outPrintWriter.println("      -s: The SIM slot ID to read carrier config value for. If no option");
        outPrintWriter.println("          is specified, it will choose the default voice SIM slot.");
        outPrintWriter.println("      add [CAPABILITY]: add a new capability");
        outPrintWriter.println("      remove [CAPABILITY]: remove a capability");
        outPrintWriter.println("      clear: clear all capability overrides");
        outPrintWriter.println("      CAPABILITY: \":\" separated list of capabilities.");
        outPrintWriter.println("          Valid options are: [mmtel(_vt), chat_v1, chat_v2, ft, ft_sms,");
        outPrintWriter.println("          geo_push, geo_push_sms, call_comp, call_post, map, sketch, chatbot,");
        outPrintWriter.println("          chatbot_sa, chatbot_role] as well as full length");
        outPrintWriter.println("          featureTag=\"featureValue\" feature tags that are not defined here.");
        outPrintWriter.println("  uce get-last-publish-pidf [-s SLOT_ID]");
        outPrintWriter.println("    Get the PIDF XML included in the last SIP PUBLISH, or \"none\" if no ");
        outPrintWriter.println("    PUBLISH is active");
        outPrintWriter.println("  uce remove-request-disallowed-status [-s SLOT_ID]");
        outPrintWriter.println("    Remove the UCE is disallowed to execute UCE requests status");
        outPrintWriter.println("  uce set-capabilities-request-timeout [-s SLOT_ID] [REQUEST_TIMEOUT_MS]");
        outPrintWriter.println("    Set the timeout for contact capabilities request.");
    }

    private void onHelpNumberVerification() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("Number verification commands");
        outPrintWriter.println("  numverify override-package PACKAGE_NAME;");
        outPrintWriter.println("    Set the authorized package for number verification.");
        outPrintWriter.println("    Leave the package name blank to reset.");
        outPrintWriter.println("  numverify fake-call NUMBER <NETWORK_COUNTRY_ISO>");
        outPrintWriter.println("    Fake an incoming call from NUMBER. This is for testing. Output will be");
        outPrintWriter.println("    1 if the call would have been intercepted, 0 otherwise.");
    }

    private void onHelpThermalMitigation() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("Thermal mitigation commands");
        outPrintWriter.println("  thermal-mitigation allow-package PACKAGE_NAME");
        outPrintWriter.println("    Set the package as one of authorized packages for thermal mitigation.");
        outPrintWriter.println("  thermal-mitigation disallow-package PACKAGE_NAME");
        outPrintWriter.println("    Remove the package from one of the authorized packages for thermal mitigation.");
    }

    private void onHelpDisableOrEnablePhysicalSubscription() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("Disable or enable a physical subscription");
        outPrintWriter.println("  disable-physical-subscription SUB_ID");
        outPrintWriter.println("    Disable the physical subscription with the provided subId, if allowed.");
        outPrintWriter.println("  enable-physical-subscription SUB_ID");
        outPrintWriter.println("    Enable the physical subscription with the provided subId, if allowed.");
    }

    private void onHelpDataTestMode() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("Mobile Data Test Mode Commands:");
        outPrintWriter.println("  data enable: enable mobile data connectivity");
        outPrintWriter.println("  data disable: disable mobile data connectivity");
    }

    private void onHelpEmergencyNumber() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("Emergency Number Test Mode Commands:");
        outPrintWriter.println("  emergency-number-test-mode ");
        outPrintWriter.println("    Add(-a), Clear(-c), Print (-p) or Remove(-r) the emergency number list in the test mode");
        outPrintWriter.println("      -a <emergency number address>: add an emergency number address for the test mode, only allows '0'-'9', '*', '#' or '+'.");
        outPrintWriter.println("      -c: clear the emergency number list in the test mode.");
        outPrintWriter.println("      -r <emergency number address>: remove an existing emergency number address added by the test mode, only allows '0'-'9', '*', '#' or '+'.");
        outPrintWriter.println("      -p: get the full emergency number list in the test mode.");
    }

    private void onHelpEndBlockSupperssion() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("End Block Suppression command:");
        outPrintWriter.println("  end-block-suppression: disable suppressing blocking by contact");
        outPrintWriter.println("                         with emergency services.");
    }

    private void onHelpCc() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("Carrier Config Commands:");
        outPrintWriter.println("  cc get-value [-s SLOT_ID] [KEY]");
        outPrintWriter.println("    Print carrier config values.");
        outPrintWriter.println("    Options are:");
        outPrintWriter.println("      -s: The SIM slot ID to read carrier config value for. If no option");
        outPrintWriter.println("          is specified, it will choose the default voice SIM slot.");
        outPrintWriter.println("    KEY: The key to the carrier config value to print. All values are printed");
        outPrintWriter.println("         if KEY is not specified.");
        outPrintWriter.println("  cc set-value [-s SLOT_ID] [-p] KEY [NEW_VALUE]");
        outPrintWriter.println("    Set carrier config KEY to NEW_VALUE.");
        outPrintWriter.println("    Options are:");
        outPrintWriter.println("      -s: The SIM slot ID to set carrier config value for. If no option");
        outPrintWriter.println("          is specified, it will choose the default voice SIM slot.");
        outPrintWriter.println("      -p: Value will be stored persistent");
        outPrintWriter.println("    NEW_VALUE specifies the new value for carrier config KEY. Null will be");
        outPrintWriter.println("      used if NEW_VALUE is not set. Strings should be encapsulated with");
        outPrintWriter.println("      quotation marks. Spaces needs to be escaped. Example: \"Hello\\ World\"");
        outPrintWriter.println("      Separate items in arrays with space . Example: \"item1\" \"item2\"");
        outPrintWriter.println("  cc set-values-from-xml [-s SLOT_ID] [-p] < XML_FILE_PATH");
        outPrintWriter.println("    Set carrier config based on the contents of the XML_FILE. File must be");
        outPrintWriter.println("    provided through standard input and follow CarrierConfig XML format.");
        outPrintWriter.println("    Example: packages/apps/CarrierConfig/assets/*.xml");
        outPrintWriter.println("    Options are:");
        outPrintWriter.println("      -s: The SIM slot ID to set carrier config value for. If no option");
        outPrintWriter.println("          is specified, it will choose the default voice SIM slot.");
        outPrintWriter.println("      -p: Value will be stored persistent");
        outPrintWriter.println("  cc clear-values [-s SLOT_ID]");
        outPrintWriter.println("    Clear all carrier override values that has previously been set");
        outPrintWriter.println("    with set-value or set-values-from-xml");
        outPrintWriter.println("    Options are:");
        outPrintWriter.println("      -s: The SIM slot ID to clear carrier config values for. If no option");
        outPrintWriter.println("          is specified, it will choose the default voice SIM slot.");
    }

    private void onHelpGba() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("Gba Commands:");
        outPrintWriter.println("  gba set-service [-s SLOT_ID] PACKAGE_NAME");
        outPrintWriter.println("    Sets the GbaService defined in PACKAGE_NAME to to be the bound.");
        outPrintWriter.println("    Options are:");
        outPrintWriter.println("      -s: The SIM slot ID to read carrier config value for. If no option");
        outPrintWriter.println("          is specified, it will choose the default voice SIM slot.");
        outPrintWriter.println("  gba get-service [-s SLOT_ID]");
        outPrintWriter.println("    Gets the package name of the currently defined GbaService.");
        outPrintWriter.println("    Options are:");
        outPrintWriter.println("      -s: The SIM slot ID to read carrier config value for. If no option");
        outPrintWriter.println("          is specified, it will choose the default voice SIM slot.");
        outPrintWriter.println("  gba set-release [-s SLOT_ID] n");
        outPrintWriter.println("    Sets the time to release/unbind GbaService in n milli-second.");
        outPrintWriter.println("    Do not release/unbind if n is -1.");
        outPrintWriter.println("    Options are:");
        outPrintWriter.println("      -s: The SIM slot ID to read carrier config value for. If no option");
        outPrintWriter.println("          is specified, it will choose the default voice SIM slot.");
        outPrintWriter.println("  gba get-release [-s SLOT_ID]");
        outPrintWriter.println("    Gets the time to release/unbind GbaService in n milli-sencond.");
        outPrintWriter.println("    Options are:");
        outPrintWriter.println("      -s: The SIM slot ID to read carrier config value for. If no option");
        outPrintWriter.println("          is specified, it will choose the default voice SIM slot.");
    }

    private void onHelpSrc() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("RCS VoLTE Single Registration Config Commands:");
        outPrintWriter.println("  src set-test-enabled true|false");
        outPrintWriter.println("    Sets the test mode enabled for RCS VoLTE single registration.");
        outPrintWriter.println("    The value could be true, false, or null(undefined).");
        outPrintWriter.println("  src get-test-enabled");
        outPrintWriter.println("    Gets the test mode for RCS VoLTE single registration.");
        outPrintWriter.println("  src set-device-enabled true|false|null");
        outPrintWriter.println("    Sets the device config for RCS VoLTE single registration to the value.");
        outPrintWriter.println("    The value could be true, false, or null(undefined).");
        outPrintWriter.println("  src get-device-enabled");
        outPrintWriter.println("    Gets the device config for RCS VoLTE single registration.");
        outPrintWriter.println("  src set-carrier-enabled [-s SLOT_ID] true|false|null");
        outPrintWriter.println("    Sets the carrier config for RCS VoLTE single registration to the value.");
        outPrintWriter.println("    The value could be true, false, or null(undefined).");
        outPrintWriter.println("    Options are:");
        outPrintWriter.println("      -s: The SIM slot ID to set the config value for. If no option");
        outPrintWriter.println("          is specified, it will choose the default voice SIM slot.");
        outPrintWriter.println("  src get-carrier-enabled [-s SLOT_ID]");
        outPrintWriter.println("    Gets the carrier config for RCS VoLTE single registration.");
        outPrintWriter.println("    Options are:");
        outPrintWriter.println("      -s: The SIM slot ID to read the config value for. If no option");
        outPrintWriter.println("          is specified, it will choose the default voice SIM slot.");
        outPrintWriter.println("  src set-feature-validation [-s SLOT_ID] true|false|null");
        outPrintWriter.println("    Sets ims feature validation result.");
        outPrintWriter.println("    The value could be true, false, or null(undefined).");
        outPrintWriter.println("    Options are:");
        outPrintWriter.println("      -s: The SIM slot ID to set the config value for. If no option");
        outPrintWriter.println("          is specified, it will choose the default voice SIM slot.");
        outPrintWriter.println("  src get-feature-validation [-s SLOT_ID]");
        outPrintWriter.println("    Gets ims feature validation override value.");
        outPrintWriter.println("    Options are:");
        outPrintWriter.println("      -s: The SIM slot ID to read the config value for. If no option");
        outPrintWriter.println("          is specified, it will choose the default voice SIM slot.");
    }

    private void onHelpAllowedNetworkTypes() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("Allowed Network Types Commands:");
        outPrintWriter.println("  get-allowed-network-types-for-users [-s SLOT_ID]");
        outPrintWriter.println("    Print allowed network types value.");
        outPrintWriter.println("    Options are:");
        outPrintWriter.println("      -s: The SIM slot ID to read allowed network types value for. If no");
        outPrintWriter.println("          option is specified, it will choose the default voice SIM slot.");
        outPrintWriter.println("  set-allowed-network-types-for-users [-s SLOT_ID] [NETWORK_TYPES_BITMASK]");
        outPrintWriter.println("    Sets allowed network types to NETWORK_TYPES_BITMASK.");
        outPrintWriter.println("    Options are:");
        outPrintWriter.println("      -s: The SIM slot ID to set allowed network types value for. If no");
        outPrintWriter.println("          option is specified, it will choose the default voice SIM slot.");
        outPrintWriter.println("    NETWORK_TYPES_BITMASK specifies the new network types value and this type");
        outPrintWriter.println("      is bitmask in binary format. Reference the NetworkTypeBitMask");
        outPrintWriter.println("      at TelephonyManager.java");
        outPrintWriter.println("      For example:");
        outPrintWriter.println("        NR only                    : 10000000000000000000");
        outPrintWriter.println("        NR|LTE                     : 11000001000000000000");
        outPrintWriter.println("        NR|LTE|CDMA|EVDO|GSM|WCDMA : 11001111101111111111");
        outPrintWriter.println("        LTE|CDMA|EVDO|GSM|WCDMA    : 01001111101111111111");
        outPrintWriter.println("        LTE only                   : 01000001000000000000");
    }

    private void onHelpRadio() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("Radio Commands:");
        outPrintWriter.println("  radio set-modem-service [-s SERVICE_NAME]");
        outPrintWriter.println("    Sets the class name of modem service defined in SERVICE_NAME");
        outPrintWriter.println("    to be the bound. Options are:");
        outPrintWriter.println("      -s: the service name that the modem service should be bound for.");
        outPrintWriter.println("          If no option is specified, it will bind to the default.");
        outPrintWriter.println("  radio get-modem-service");
        outPrintWriter.println("    Gets the service name of the currently defined modem service.");
        outPrintWriter.println("    If it is binding to default, 'default' returns.");
        outPrintWriter.println("    If it doesn't bind to any modem service for some reasons,");
        outPrintWriter.println("    the result would be 'unknown'.");
    }

    private void onHelpSatellite() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("Satellite Commands:");
        outPrintWriter.println("  set-satellite-service-package-name [-s SERVICE_PACKAGE_NAME]");
        outPrintWriter.println("    Sets the package name of satellite service defined in");
        outPrintWriter.println("    SERVICE_PACKAGE_NAME to be bound. Options are:");
        outPrintWriter.println("      -s: the satellite service package name that Telephony will bind to.");
        outPrintWriter.println("          If no option is specified, it will bind to the default.");
        outPrintWriter.println("  set-satellite-gateway-service-package-name [-s SERVICE_PACKAGE_NAME]");
        outPrintWriter.println("    Sets the package name of satellite gateway service defined in");
        outPrintWriter.println("    SERVICE_PACKAGE_NAME to be bound. Options are:");
        outPrintWriter.println("      -s: the satellite gateway service package name that Telephony will bind");
        outPrintWriter.println("           to. If no option is specified, it will bind to the default.");
        outPrintWriter.println("  set-satellite-listening-timeout-duration [-t TIMEOUT_MILLIS]");
        outPrintWriter.println("    Sets the timeout duration in millis that satellite will stay at listening");
        outPrintWriter.println("    mode. Options are:");
        outPrintWriter.println("      -t: the timeout duration in milliseconds.");
        outPrintWriter.println("          If no option is specified, it will use the default values.");
        outPrintWriter.println("  set-satellite-pointing-ui-class-name [-p PACKAGE_NAME -c CLASS_NAME]");
        outPrintWriter.println("    Sets the package and class name of satellite pointing UI app defined in");
        outPrintWriter.println("    PACKAGE_NAME and CLASS_NAME to be launched. Options are:");
        outPrintWriter.println("      -p: the satellite pointing UI app package name that Telephony will");
        outPrintWriter.println("           launch. If no option is specified, it will launch the default.");
        outPrintWriter.println("      -c: the satellite pointing UI app class name that Telephony will");
        outPrintWriter.println("           launch.");
        outPrintWriter.println("  set-emergency-call-to-satellite-handover-type [-t HANDOVER_TYPE ");
        outPrintWriter.println("    -d DELAY_SECONDS] Override connectivity status in monitoring emergency ");
        outPrintWriter.println("    call and sending EVENT_DISPLAY_EMERGENCY_MESSAGE to Dialer.");
        outPrintWriter.println("    Options are:");
        outPrintWriter.println("      -t: the emergency call to satellite handover type.");
        outPrintWriter.println("          If no option is specified, override is disabled.");
        outPrintWriter.println("      -d: the delay in seconds in sending EVENT_DISPLAY_EMERGENCY_MESSAGE.");
        outPrintWriter.println("          If no option is specified, there is no delay in sending the event.");
        outPrintWriter.println("  set-satellite-access-control-overlay-configs [-r -a -f SATELLITE_S2_FILE ");
        outPrintWriter.println("    -d LOCATION_FRESH_DURATION_NANOS -c COUNTRY_CODES] Override the overlay");
        outPrintWriter.println("    configs of satellite access controller.");
        outPrintWriter.println("    Options are:");
        outPrintWriter.println("      -r: clear the overriding. Absent means enable overriding.");
        outPrintWriter.println("      -a: the country codes is an allowed list. Absent means disallowed.");
        outPrintWriter.println("      -f: the satellite s2 file.");
        outPrintWriter.println("      -d: the location fresh duration nanos.");
        outPrintWriter.println("      -c: the list of satellite country codes separated by comma.");
        outPrintWriter.println("  set-country-codes [-r -n CURRENT_NETWORK_COUNTRY_CODES -c");
        outPrintWriter.println("    CACHED_NETWORK_COUNTRY_CODES -l LOCATION_COUNTRY_CODE -t");
        outPrintWriter.println("    LOCATION_COUNTRY_CODE_TIMESTAMP] ");
        outPrintWriter.println("    Override the cached location country code and its update timestamp. ");
        outPrintWriter.println("    Options are:");
        outPrintWriter.println("      -r: clear the overriding. Absent means enable overriding.");
        outPrintWriter.println("      -n: the current network country code ISOs.");
        outPrintWriter.println("      -c: the cached network country code ISOs.");
        outPrintWriter.println("      -l: the location country code ISO.");
        outPrintWriter.println("      -t: the update timestamp nanos of the location country code.");
        outPrintWriter.println("  set-oem-enabled-satellite-provision-status [-p true/false]");
        outPrintWriter.println("    Sets the OEM-enabled satellite provision status. Options are:");
        outPrintWriter.println("      -p: the overriding satellite provision status. If no option is ");
        outPrintWriter.println("          specified, reset the overridden provision status.");
        outPrintWriter.println("  add-attach-restriction-for-carrier [-s SLOT_ID ");
        outPrintWriter.println("    -r SATELLITE_COMMUNICATION_RESTRICTION_REASON] Add a restriction reason ");
        outPrintWriter.println("     for disallowing carrier supported satellite plmn scan ");
        outPrintWriter.println("     and attach by modem. ");
        outPrintWriter.println("    Options are:");
        outPrintWriter.println("      -s: The SIM slot ID to add a restriction reason. If no option ");
        outPrintWriter.println("          is specified, it will choose the default voice SIM slot.");
        outPrintWriter.println("      -r: restriction reason ");
        outPrintWriter.println("          If no option is specified, it will use ");
        outPrintWriter.println("          the default value SATELLITE_COMMUNICATION_RESTRICTION_REASON_USER.");
        outPrintWriter.println("  remove-attach-restriction-for-carrier [-s SLOT_ID ");
        outPrintWriter.println("    -r SATELLITE_COMMUNICATION_RESTRICTION_REASON] Add a restriction reason ");
        outPrintWriter.println("     for disallowing carrier supported satellite plmn scan ");
        outPrintWriter.println("     and attach by modem. ");
        outPrintWriter.println("    Options are:");
        outPrintWriter.println("      -s: The SIM slot ID to add a restriction reason. If no option ");
        outPrintWriter.println("          is specified, it will choose the default voice SIM slot.");
        outPrintWriter.println("      -r: restriction reason ");
        outPrintWriter.println("          If no option is specified, it will use ");
        outPrintWriter.println("          the default value SATELLITE_COMMUNICATION_RESTRICTION_REASON_USER.");
    }

    private void onHelpImei() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("IMEI Commands:");
        outPrintWriter.println("  get-imei [-s SLOT_ID]");
        outPrintWriter.println("    Gets the device IMEI. Options are:");
        outPrintWriter.println("      -s: the slot ID to get the IMEI. If no option");
        outPrintWriter.println("          is specified, it will choose the default voice SIM slot.");
    }

    private void onHelpDomainSelection() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("Domain Selection Commands:");
        outPrintWriter.println("  domainselection set-dss-override COMPONENT_NAME");
        outPrintWriter.println("    Sets the service defined in COMPONENT_NAME to be bound");
        outPrintWriter.println("  domainselection clear-dss-override");
        outPrintWriter.println("    Clears DomainSelectionService override.");
    }

    private void onHelpRilEvent() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("RIL Commands:");
        outPrintWriter.println("  send-ril-event stk-proactive-cmd [-s SLOT-ID] [-t TLV]");
        outPrintWriter.println("    Sends the RIL_UNSOL_STK_PROACTIVE_COMMAND to CatService. Options are:");
        outPrintWriter.println("  -s: the slot ID corresponding to CatService instance");
        outPrintWriter.println("  -t: TLV to send in the event");
    }

    private int handleImsCommand() {
        String nextArg = getNextArg();
        if (nextArg == null) {
            onHelpIms();
            return 0;
        }
        nextArg.hashCode();
        switch (nextArg) {
            case "conference-event-package":
                return handleCepChange();
            case "enable":
                return handleEnableIms();
            case "clear-ims-service-override":
                return handleImsClearCarrierServiceCommand();
            case "get-ims-service":
                return handleImsGetServiceCommand();
            case "set-ims-service":
                return handleImsSetServiceCommand();
            case "disable":
                return handleDisableIms();
            default:
                return -1;
        }
    }

    private int handleDataTestModeCommand() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        String nextArgRequired = getNextArgRequired();
        if (nextArgRequired == null) {
            onHelpDataTestMode();
            return 0;
        }
        nextArgRequired.hashCode();
        if (nextArgRequired.equals("enable")) {
            try {
                this.mInterface.enableDataConnectivity(this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                Log.w("TelephonyShellCommand", "data enable, error " + e.getMessage());
                errPrintWriter.println("Exception: " + e.getMessage());
                return -1;
            }
        } else if (nextArgRequired.equals("disable")) {
            try {
                this.mInterface.disableDataConnectivity(this.mContext.getOpPackageName());
            } catch (RemoteException e2) {
                Log.w("TelephonyShellCommand", "data disable, error " + e2.getMessage());
                errPrintWriter.println("Exception: " + e2.getMessage());
                return -1;
            }
        } else {
            onHelpDataTestMode();
        }
        return 0;
    }

    private int handleEmergencyCallbackModeCommand() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        try {
            this.mInterface.startEmergencyCallbackMode();
            Log.d("TelephonyShellCommand", "handleEmergencyCallbackModeCommand: triggered");
            return 0;
        } catch (RemoteException e) {
            Log.w("TelephonyShellCommand", "emergency-callback-mode error: " + e.getMessage());
            errPrintWriter.println("Exception: " + e.getMessage());
            return -1;
        }
    }

    private void removeEmergencyNumberTestMode(String str) {
        String str2;
        PrintWriter errPrintWriter = getErrPrintWriter();
        int[] iArr = ROUTING_TYPES;
        int length = iArr.length;
        int i = 0;
        while (i < length) {
            try {
                str2 = str;
                try {
                    this.mInterface.updateEmergencyNumberListTestMode(2, new EmergencyNumber(str2, "", "", 0, new ArrayList(), 32, iArr[i]));
                } catch (RemoteException e) {
                    e = e;
                    RemoteException remoteException = e;
                    Log.w("TelephonyShellCommand", "emergency-number-test-mode error " + remoteException.getMessage());
                    errPrintWriter.println("Exception: " + remoteException.getMessage());
                }
            } catch (RemoteException e2) {
                e = e2;
                str2 = str;
            }
            i++;
            str = str2;
        }
    }

    /* JADX WARN: Code duplicated, block: B:77:0x0159  */
    private int handleEmergencyNumberTestModeCommand() {
        int i;
        PrintWriter errPrintWriter = getErrPrintWriter();
        String nextOption = getNextOption();
        if (nextOption == null) {
            onHelpEmergencyNumber();
            return 0;
        }
        nextOption.hashCode();
        switch (nextOption) {
            case "-a":
                String nextArgRequired = getNextArgRequired();
                if (nextArgRequired == null) {
                    errPrintWriter.println("An emergency number (only allow '0'-'9', '*', '#' or '+') needs to be specified after -a in the command ");
                    return -1;
                }
                String[] strArrSplit = nextArgRequired.split(":");
                String str = strArrSplit[0];
                if (str == null || !EmergencyNumber.validateEmergencyNumberAddress(str)) {
                    errPrintWriter.println("An emergency number (only allow '0'-'9', '*', '#' or '+') needs to be specified after -a in the command ");
                    return -1;
                }
                String str2 = strArrSplit[0];
                removeEmergencyNumberTestMode(str2);
                if (strArrSplit.length > 1) {
                    String lowerCase = strArrSplit[1].toLowerCase(Locale.ROOT);
                    lowerCase.hashCode();
                    switch (lowerCase) {
                        case "normal":
                            i = 2;
                            break;
                        case "unknown":
                            i = 0;
                            break;
                        case "emergency":
                            i = 1;
                            break;
                        default:
                            errPrintWriter.println("\"" + strArrSplit[1] + "\" is not a valid specification for emergency call routing. Please enter either \"normal\", \"unknown\", or \"emergency\" for call routing. (-a 1234:normal)");
                            return -1;
                    }
                } else {
                    i = 0;
                }
                try {
                    this.mInterface.updateEmergencyNumberListTestMode(1, new EmergencyNumber(str2, "", "", 0, new ArrayList(), 32, i));
                    return 0;
                } catch (RemoteException e) {
                    Log.w("TelephonyShellCommand", "emergency-number-test-mode -a " + str2 + ", error " + e.getMessage());
                    StringBuilder sb = new StringBuilder("Exception: ");
                    sb.append(e.getMessage());
                    errPrintWriter.println(sb.toString());
                    return -1;
                }
            case "-c":
                try {
                    this.mInterface.updateEmergencyNumberListTestMode(3, (EmergencyNumber) null);
                    return 0;
                } catch (RemoteException e2) {
                    Log.w("TelephonyShellCommand", "emergency-number-test-mode -c error " + e2.getMessage());
                    errPrintWriter.println("Exception: " + e2.getMessage());
                    return -1;
                }
            case "-p":
                try {
                    getOutPrintWriter().println(this.mInterface.getEmergencyNumberListTestMode());
                    return 0;
                } catch (RemoteException e3) {
                    Log.w("TelephonyShellCommand", "emergency-number-test-mode -p error " + e3.getMessage());
                    errPrintWriter.println("Exception: " + e3.getMessage());
                    return -1;
                }
            case "-r":
                String nextArgRequired2 = getNextArgRequired();
                if (nextArgRequired2 == null || !EmergencyNumber.validateEmergencyNumberAddress(nextArgRequired2)) {
                    errPrintWriter.println("An emergency number (only allow '0'-'9', '*', '#' or '+') needs to be specified after -r in the command ");
                    return -1;
                }
                removeEmergencyNumberTestMode(nextArgRequired2);
                return 0;
            default:
                onHelpEmergencyNumber();
                return 0;
        }
    }

    private int handleNumberVerificationCommand() {
        String nextArg = getNextArg();
        if (nextArg == null) {
            onHelpNumberVerification();
            return 0;
        }
        if (!checkShellUid()) {
            return -1;
        }
        nextArg.hashCode();
        if (nextArg.equals("override-package")) {
            NumberVerificationManager.overrideAuthorizedPackage(getNextArg());
            return 0;
        }
        if (!nextArg.equals("fake-call")) {
            return -1;
        }
        String nextArg2 = getNextArg();
        String nextArg3 = getNextArg();
        if (nextArg3 == null) {
            nextArg3 = Locale.getDefault().getCountry();
        }
        Log.i("ShellCommand", "numberVerificationFakeCall: " + nextArg2 + " Locale: " + nextArg3);
        getOutPrintWriter().println(NumberVerificationManager.getInstance().checkIncomingCall(nextArg2, nextArg3) ? "1" : "0");
        return 0;
    }

    private boolean subIsEsim(int i) {
        SubscriptionInfo activeSubscriptionInfo = this.mSubscriptionManager.getActiveSubscriptionInfo(i);
        if (activeSubscriptionInfo != null) {
            return activeSubscriptionInfo.isEmbedded();
        }
        return false;
    }

    private int handleEnablePhysicalSubscription(boolean z) {
        PrintWriter errPrintWriter = getErrPrintWriter();
        try {
            int i = Integer.parseInt(getNextArgRequired());
            if (!UserHandle.isSameApp(Binder.getCallingUid(), 0) || TelephonyUtils.IS_USER) {
                errPrintWriter.println("cc: Permission denied.");
                return -1;
            }
            if (subIsEsim(i)) {
                errPrintWriter.println("SubId " + i + " is not for a physical subscription");
                return -1;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(z ? "Enabling" : "Disabling");
            sb.append(" physical subscription with subId=");
            sb.append(i);
            Log.d("TelephonyShellCommand", sb.toString());
            this.mSubscriptionManager.setUiccApplicationsEnabled(i, z);
            return 0;
        } catch (NumberFormatException unused) {
            errPrintWriter.println((z ? "enable" : "disable").concat("-physical-subscription requires an integer as a subId."));
            return -1;
        }
    }

    private int handleThermalMitigationCommand() {
        String nextArg = getNextArg();
        String nextArg2 = getNextArg();
        if (nextArg == null || nextArg2 == null) {
            onHelpThermalMitigation();
            return 0;
        }
        if (!checkShellUid()) {
            return -1;
        }
        nextArg.hashCode();
        if (nextArg.equals("disallow-package")) {
            PhoneInterfaceManager.removePackageFromThermalMitigationAllowlist(nextArg2, this.mContext);
            return 0;
        }
        if (nextArg.equals("allow-package")) {
            PhoneInterfaceManager.addPackageToThermalMitigationAllowlist(nextArg2, this.mContext);
            return 0;
        }
        onHelpThermalMitigation();
        return -1;
    }

    private int handleD2dCommand() {
        String nextArg = getNextArg();
        if (nextArg == null) {
            onHelpD2D();
            return 0;
        }
        nextArg.hashCode();
        switch (nextArg) {
            case "set-device-support":
                return handleD2dDeviceSupportedCommand();
            case "send":
                return handleD2dSendCommand();
            case "transport":
                return handleD2dTransportCommand();
            default:
                return -1;
        }
    }

    private int handleD2dSendCommand() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        String nextArg = getNextArg();
        if (nextArg == null) {
            onHelpD2D();
            return 0;
        }
        try {
            int i = Integer.parseInt(nextArg);
            String nextArg2 = getNextArg();
            if (nextArg2 == null) {
                onHelpD2D();
                return 0;
            }
            try {
                try {
                    this.mInterface.sendDeviceToDeviceMessage(i, Integer.parseInt(nextArg2));
                    return 0;
                } catch (RemoteException e) {
                    Log.w("TelephonyShellCommand", "d2d send error: " + e.getMessage());
                    errPrintWriter.println("Exception: " + e.getMessage());
                    return -1;
                }
            } catch (NumberFormatException unused) {
                errPrintWriter.println("message value must be a valid integer");
                return -1;
            }
        } catch (NumberFormatException unused2) {
            errPrintWriter.println("message type must be a valid integer");
            return -1;
        }
    }

    private int handleD2dTransportCommand() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        String nextArg = getNextArg();
        if (nextArg == null) {
            onHelpD2D();
            return 0;
        }
        try {
            this.mInterface.setActiveDeviceToDeviceTransport(nextArg);
            return 0;
        } catch (RemoteException e) {
            Log.w("TelephonyShellCommand", "d2d transport error: " + e.getMessage());
            errPrintWriter.println("Exception: " + e.getMessage());
            return -1;
        }
    }

    private int handleBarringCommand() {
        String nextArg = getNextArg();
        if (nextArg == null) {
            onHelpBarring();
            return 0;
        }
        nextArg.hashCode();
        if (nextArg.equals("send")) {
            return handleBarringSendCommand();
        }
        return -1;
    }

    private int handleBarringSendCommand() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        int defaultSlot = getDefaultSlot();
        int subscriptionId = SubscriptionManager.getSubscriptionId(defaultSlot);
        int i = 2;
        boolean z = false;
        int i2 = 0;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                nextOption.hashCode();
                switch (nextOption) {
                    case "-b":
                        try {
                            i = Integer.parseInt(getNextArgRequired());
                            if (i < -1 || i > 2) {
                                throw new NumberFormatException();
                            }
                            break;
                        } catch (NumberFormatException unused) {
                            errPrintWriter.println("barring send requires an integer in range [-1,2] as a BARRING_TYPE.");
                            return -1;
                        }
                        break;
                    case "-c":
                        try {
                            z = Boolean.parseBoolean(getNextArgRequired());
                            break;
                        } catch (Exception unused2) {
                            errPrintWriter.println("barring send requires a boolean after -c indicating conditional barring");
                            return -1;
                        }
                        break;
                    case "-s":
                        try {
                            defaultSlot = Integer.parseInt(getNextArgRequired());
                            subscriptionId = SubscriptionManager.getSubscriptionId(defaultSlot);
                            break;
                        } catch (NumberFormatException unused3) {
                            errPrintWriter.println("barring send requires an integer as a SLOT_ID.");
                            return -1;
                        }
                        break;
                    case "-t":
                        try {
                            i2 = Integer.parseInt(getNextArgRequired());
                            break;
                        } catch (NumberFormatException unused4) {
                            errPrintWriter.println("barring send requires an integer for time of barring in seconds after -t for conditional barring");
                            return -1;
                        }
                        break;
                }
            } else {
                SparseArray sparseArray = new SparseArray();
                sparseArray.append(0, new BarringInfo.BarringServiceInfo(i, z, 0, i2));
                try {
                    this.mTelephonyRegistryManager.notifyBarringInfoChanged(defaultSlot, subscriptionId, new BarringInfo(null, sparseArray));
                    return 0;
                } catch (Exception e) {
                    Log.w("TelephonyShellCommand", "barring send error: " + e.getMessage());
                    errPrintWriter.println("Exception: " + e.getMessage());
                    return -1;
                }
            }
        }
    }

    private int handleD2dDeviceSupportedCommand() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        String nextArg = getNextArg();
        if (nextArg == null) {
            onHelpD2D();
            return 0;
        }
        try {
            this.mInterface.setDeviceToDeviceForceEnabled("true".equals(nextArg.toLowerCase(Locale.ROOT)));
            return 0;
        } catch (RemoteException e) {
            Log.w("TelephonyShellCommand", "Error forcing D2D enabled: " + e.getMessage());
            errPrintWriter.println("Exception: " + e.getMessage());
            return -1;
        }
    }

    /* JADX WARN: Code duplicated, block: B:94:0x01c7  */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 5 */
    private int handleImsSetServiceCommand() {
        Boolean bool;
        String str;
        String str2;
        int i;
        String str3;
        Boolean bool2;
        PrintWriter errPrintWriter = getErrPrintWriter();
        int defaultSlot = getDefaultSlot();
        ArrayList arrayList = new ArrayList();
        Boolean bool3 = null;
        int i2 = defaultSlot;
        int i3 = -10000;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                nextOption.hashCode();
                switch (nextOption) {
                    case "-c":
                        bool2 = Boolean.TRUE;
                        break;
                    case "-d":
                        bool2 = Boolean.FALSE;
                        break;
                    case "-f":
                        String[] strArrSplit = getNextArgRequired().split(",");
                        for (int i4 = 0; i4 < strArrSplit.length; i4++) {
                            try {
                                int i5 = Integer.parseInt(strArrSplit[i4]);
                                Integer numValueOf = Integer.valueOf(i5);
                                if (i5 >= 0 && i5 < 3) {
                                    arrayList.add(numValueOf);
                                }
                                errPrintWriter.println("ims set-ims-service -f " + numValueOf + " is an invalid feature.");
                                return -1;
                            } catch (NumberFormatException unused) {
                                errPrintWriter.println("ims set-ims-service -f tried to parse " + strArrSplit[i4] + " as an integer.");
                                return -1;
                            }
                        }
                        continue;
                        break;
                    case "-s":
                        try {
                            i2 = Integer.parseInt(getNextArgRequired());
                            continue;
                            break;
                        } catch (NumberFormatException unused2) {
                            errPrintWriter.println("ims set-ims-service requires an integer as a SLOT_ID.");
                            return -1;
                        }
                        break;
                    case "-u":
                        try {
                            i3 = Integer.parseInt(getNextArgRequired());
                            continue;
                            break;
                        } catch (NumberFormatException unused3) {
                            errPrintWriter.println("ims set-ims-service requires an integer as a USER_ID");
                            return -1;
                        }
                        break;
                    default:
                        continue;
                        break;
                }
                bool3 = bool2;
            } else {
                if (bool3 == null) {
                    errPrintWriter.println("ims set-ims-service requires either \"-c\" or \"-d\" to be set.");
                    return -1;
                }
                String nextArg = getNextArg();
                if (nextArg == null) {
                    nextArg = "";
                }
                try {
                    int[] iArr = new int[arrayList.size()];
                    i = -1;
                    for (int i6 = 0; i6 < arrayList.size(); i6++) {
                        try {
                            try {
                                iArr[i6] = ((Integer) arrayList.get(i6)).intValue();
                            } catch (RemoteException e) {
                                e = e;
                                bool = bool3;
                                str3 = "ims set-ims-service -s ";
                                str = "TelephonyShellCommand";
                                str2 = " ";
                                StringBuilder sb = new StringBuilder(str3);
                                sb.append(i2);
                                sb.append(" -u ");
                                sb.append(i3);
                                sb.append(str2);
                                sb.append(bool.booleanValue() ? "-c " : "-d ");
                                sb.append("-f ");
                                sb.append(arrayList);
                                sb.append(str2);
                                sb.append(nextArg);
                                sb.append(", error");
                                sb.append(e.getMessage());
                                Log.w(str, sb.toString());
                                errPrintWriter.println("Exception: " + e.getMessage());
                                return i;
                            }
                        } catch (RemoteException e2) {
                            e = e2;
                            bool = bool3;
                            str3 = "ims set-ims-service -s ";
                            str = "TelephonyShellCommand";
                            str2 = " ";
                            StringBuilder sb2 = new StringBuilder(str3);
                            sb2.append(i2);
                            sb2.append(" -u ");
                            sb2.append(i3);
                            sb2.append(str2);
                            sb2.append(bool.booleanValue() ? "-c " : "-d ");
                            sb2.append("-f ");
                            sb2.append(arrayList);
                            sb2.append(str2);
                            sb2.append(nextArg);
                            sb2.append(", error");
                            sb2.append(e.getMessage());
                            Log.w(str, sb2.toString());
                            errPrintWriter.println("Exception: " + e.getMessage());
                            return i;
                        }
                    }
                    String str4 = nextArg;
                    try {
                        ITelephony iTelephony = this.mInterface;
                        try {
                            boolean zBooleanValue = bool3.booleanValue();
                            bool = bool3;
                            str2 = " ";
                            str3 = "ims set-ims-service -s ";
                            str = "TelephonyShellCommand";
                            try {
                                boolean boundImsServiceOverride = iTelephony.setBoundImsServiceOverride(i2, i3, zBooleanValue, iArr, str4);
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append(str3);
                                sb3.append(i2);
                                sb3.append(" -u ");
                                sb3.append(i3);
                                sb3.append(str2);
                                sb3.append(bool.booleanValue() ? "-c " : "-d ");
                                sb3.append("-f ");
                                sb3.append(arrayList);
                                sb3.append(str2);
                                sb3.append(str4);
                                sb3.append(", result=");
                                sb3.append(boundImsServiceOverride);
                                Log.v(str, sb3.toString());
                                getOutPrintWriter().println(boundImsServiceOverride);
                                return 0;
                            } catch (RemoteException e3) {
                                e = e3;
                                nextArg = str4;
                                StringBuilder sb4 = new StringBuilder(str3);
                                sb4.append(i2);
                                sb4.append(" -u ");
                                sb4.append(i3);
                                sb4.append(str2);
                                sb4.append(bool.booleanValue() ? "-c " : "-d ");
                                sb4.append("-f ");
                                sb4.append(arrayList);
                                sb4.append(str2);
                                sb4.append(nextArg);
                                sb4.append(", error");
                                sb4.append(e.getMessage());
                                Log.w(str, sb4.toString());
                                errPrintWriter.println("Exception: " + e.getMessage());
                                return i;
                            }
                        } catch (RemoteException e4) {
                            e = e4;
                            bool = bool3;
                            str2 = " ";
                            str3 = "ims set-ims-service -s ";
                            str = "TelephonyShellCommand";
                        }
                    } catch (RemoteException e5) {
                        e = e5;
                        bool = bool3;
                        str = "TelephonyShellCommand";
                        str2 = " ";
                        str3 = "ims set-ims-service -s ";
                    }
                } catch (RemoteException e6) {
                    e = e6;
                    bool = bool3;
                    str = "TelephonyShellCommand";
                    str2 = " ";
                    i = -1;
                    str3 = "ims set-ims-service -s ";
                    StringBuilder sb5 = new StringBuilder(str3);
                    sb5.append(i2);
                    sb5.append(" -u ");
                    sb5.append(i3);
                    sb5.append(str2);
                    sb5.append(bool.booleanValue() ? "-c " : "-d ");
                    sb5.append("-f ");
                    sb5.append(arrayList);
                    sb5.append(str2);
                    sb5.append(nextArg);
                    sb5.append(", error");
                    sb5.append(e.getMessage());
                    Log.w(str, sb5.toString());
                    errPrintWriter.println("Exception: " + e.getMessage());
                    return i;
                }
            }
        }
    }

    private int handleImsClearCarrierServiceCommand() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        int defaultSlot = getDefaultSlot();
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                nextOption.hashCode();
                if (nextOption.equals("-s")) {
                    try {
                        defaultSlot = Integer.parseInt(getNextArgRequired());
                    } catch (NumberFormatException unused) {
                        errPrintWriter.println("ims set-ims-service requires an integer as a SLOT_ID.");
                        return -1;
                    }
                }
            } else {
                try {
                    boolean zClearCarrierImsServiceOverride = this.mInterface.clearCarrierImsServiceOverride(defaultSlot);
                    Log.v("TelephonyShellCommand", "ims clear-ims-service-override -s " + defaultSlot + ", result=" + zClearCarrierImsServiceOverride);
                    getOutPrintWriter().println(zClearCarrierImsServiceOverride);
                    return 0;
                } catch (RemoteException e) {
                    Log.w("TelephonyShellCommand", "ims clear-ims-service-override -s " + defaultSlot + ", error" + e.getMessage());
                    StringBuilder sb = new StringBuilder("Exception: ");
                    sb.append(e.getMessage());
                    errPrintWriter.println(sb.toString());
                    return -1;
                }
            }
        }
    }

    private int handleImsGetServiceCommand() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        int defaultSlot = getDefaultSlot();
        Integer num = 1;
        Boolean bool = null;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption == null) {
                if (bool == null) {
                    errPrintWriter.println("ims get-ims-service requires either \"-c\" or \"-d\" to be set.");
                    return -1;
                }
                try {
                    String boundImsServicePackage = this.mInterface.getBoundImsServicePackage(defaultSlot, bool.booleanValue(), num.intValue());
                    StringBuilder sb = new StringBuilder("ims get-ims-service -s ");
                    sb.append(defaultSlot);
                    sb.append(" ");
                    sb.append(bool.booleanValue() ? "-c " : "-d ");
                    sb.append("-f " + num);
                    sb.append(" , returned: ");
                    sb.append(boundImsServicePackage);
                    Log.v("TelephonyShellCommand", sb.toString());
                    getOutPrintWriter().println(boundImsServicePackage);
                    return 0;
                } catch (RemoteException unused) {
                    return -1;
                }
            }
            nextOption.hashCode();
            switch (nextOption) {
                case "-c":
                    bool = Boolean.TRUE;
                    break;
                case "-d":
                    bool = Boolean.FALSE;
                    break;
                case "-f":
                    try {
                        int i = Integer.parseInt(getNextArg());
                        Integer numValueOf = Integer.valueOf(i);
                        if (i < 0 || i >= 3) {
                            errPrintWriter.println("ims get-ims-service -f invalid feature.");
                            return -1;
                        }
                        num = numValueOf;
                        break;
                    } catch (NumberFormatException unused2) {
                        errPrintWriter.println("ims get-ims-service -f requires valid integer as feature.");
                        return -1;
                    }
                    break;
                case "-s":
                    try {
                        defaultSlot = Integer.parseInt(getNextArgRequired());
                        break;
                    } catch (NumberFormatException unused3) {
                        errPrintWriter.println("ims set-ims-service requires an integer as a SLOT_ID.");
                        return -1;
                    }
                    break;
            }
        }
    }

    private int handleEnableIms() {
        int defaultSlot = getDefaultSlot();
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                nextOption.hashCode();
                if (nextOption.equals("-s")) {
                    try {
                        defaultSlot = Integer.parseInt(getNextArgRequired());
                    } catch (NumberFormatException unused) {
                        getErrPrintWriter().println("ims enable requires an integer as a SLOT_ID.");
                        return -1;
                    }
                }
            } else {
                try {
                    this.mInterface.enableIms(defaultSlot);
                    Log.v("TelephonyShellCommand", "ims enable -s " + defaultSlot);
                    return 0;
                } catch (RemoteException unused2) {
                    return -1;
                }
            }
        }
    }

    private int handleDisableIms() {
        int defaultSlot = getDefaultSlot();
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                nextOption.hashCode();
                if (nextOption.equals("-s")) {
                    try {
                        defaultSlot = Integer.parseInt(getNextArgRequired());
                    } catch (NumberFormatException unused) {
                        getErrPrintWriter().println("ims disable requires an integer as a SLOT_ID.");
                        return -1;
                    }
                }
            } else {
                try {
                    this.mInterface.disableIms(defaultSlot);
                    Log.v("TelephonyShellCommand", "ims disable -s " + defaultSlot);
                    return 0;
                } catch (RemoteException unused2) {
                    return -1;
                }
            }
        }
    }

    private int handleCepChange() {
        Log.i("TelephonyShellCommand", "handleCepChange");
        String nextArg = getNextArg();
        if (nextArg == null) {
            return -1;
        }
        try {
            this.mInterface.setCepEnabled(nextArg.equals("enable"));
            return 0;
        } catch (RemoteException unused) {
            return -1;
        }
    }

    private int getDefaultSlot() {
        int defaultVoicePhoneId = SubscriptionManager.getDefaultVoicePhoneId();
        if (defaultVoicePhoneId <= -1 || defaultVoicePhoneId == Integer.MAX_VALUE) {
            return 0;
        }
        return defaultVoicePhoneId;
    }

    private CcOptionParseResult parseCcOptions(String str, boolean z) {
        PrintWriter errPrintWriter = getErrPrintWriter();
        CcOptionParseResult ccOptionParseResult = new CcOptionParseResult();
        ccOptionParseResult.mSubId = SubscriptionManager.getDefaultSubscriptionId();
        ccOptionParseResult.mPersistent = false;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption == null) {
                return ccOptionParseResult;
            }
            nextOption.hashCode();
            if (nextOption.equals("-p")) {
                if (z) {
                    ccOptionParseResult.mPersistent = true;
                } else {
                    errPrintWriter.println(str + "Unexpected option " + nextOption);
                    return null;
                }
            } else if (nextOption.equals("-s")) {
                try {
                    int iSlotStringToSubId = slotStringToSubId(str, getNextArgRequired());
                    ccOptionParseResult.mSubId = iSlotStringToSubId;
                    if (!SubscriptionManager.isValidSubscriptionId(iSlotStringToSubId)) {
                        errPrintWriter.println(str + "No valid subscription found.");
                        return null;
                    }
                } catch (IllegalArgumentException unused) {
                    errPrintWriter.println(str + "SLOT_ID expected after -s.");
                    return null;
                }
            } else {
                errPrintWriter.println(str + "Unknown option " + nextOption);
                return null;
            }
        }
    }

    private int slotStringToSubId(String str, String str2) {
        try {
            int i = Integer.parseInt(str2);
            if (!SubscriptionManager.isValidPhoneId(i)) {
                getErrPrintWriter().println(str + str2 + " is not a valid SLOT_ID.");
                return -1;
            }
            Phone phone = PhoneFactory.getPhone(i);
            if (phone == null) {
                getErrPrintWriter().println(str + "No subscription found in slot " + i + ".");
                return -1;
            }
            return phone.getSubId();
        } catch (NumberFormatException unused) {
            getErrPrintWriter().println(str + str2 + " is not a valid number for SLOT_ID.");
            return -1;
        }
    }

    private boolean checkShellUid() {
        return TelephonyPermissions.isRootOrShell(Binder.getCallingUid());
    }

    private int handleCcCommand() {
        if (!UserHandle.isSameApp(Binder.getCallingUid(), 0) || TelephonyUtils.IS_USER) {
            getErrPrintWriter().println("cc: Permission denied.");
            return -1;
        }
        String nextArg = getNextArg();
        if (nextArg == null) {
            onHelpCc();
            return 0;
        }
        nextArg.hashCode();
        switch (nextArg) {
            case "set-values-from-xml":
                return handleCcSetValuesFromXml();
            case "clear-values":
                return handleCcClearValues();
            case "set-value":
                return handleCcSetValue();
            case "get-value":
                return handleCcGetValue();
            default:
                getErrPrintWriter().println("cc: Unknown argument: ".concat(nextArg));
                return -1;
        }
    }

    private int handleCcGetValue() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        CcOptionParseResult ccOptions = parseCcOptions("cc get-value: ", false);
        if (ccOptions == null) {
            return -1;
        }
        PersistableBundle configForSubId = this.mCarrierConfigManager.getConfigForSubId(ccOptions.mSubId);
        if (configForSubId == null) {
            errPrintWriter.println("cc get-value: No carrier config values found for subId " + ccOptions.mSubId + ".");
            return -1;
        }
        String nextArg = getNextArg();
        if (nextArg != null) {
            if (!configForSubId.containsKey(nextArg)) {
                errPrintWriter.println("cc get-value: " + nextArg + " is not a valid key.");
                return -1;
            }
            getOutPrintWriter().println(ccValueToString(nextArg, getType("cc get-value: ", nextArg, configForSubId), configForSubId));
        } else {
            for (String str : new TreeSet(configForSubId.keySet())) {
                getOutPrintWriter().println(ccValueToString(str, getType("cc get-value: ", str, configForSubId), configForSubId));
            }
        }
        return 0;
    }

    private int handleCcSetValue() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        CcOptionParseResult ccOptions = parseCcOptions("cc set-value: ", true);
        if (ccOptions == null) {
            return -1;
        }
        PersistableBundle configForSubId = this.mCarrierConfigManager.getConfigForSubId(ccOptions.mSubId);
        if (configForSubId == null) {
            errPrintWriter.println("cc set-value: No carrier config values found for subId " + ccOptions.mSubId + ".");
            return -1;
        }
        String nextArg = getNextArg();
        if (nextArg == null || nextArg.equals("")) {
            errPrintWriter.println("cc set-value: KEY is missing");
            return -1;
        }
        if (!configForSubId.containsKey(nextArg)) {
            errPrintWriter.println("cc set-value: " + nextArg + " is not a valid key.");
            return -1;
        }
        ArrayList arrayList = new ArrayList();
        while (peekNextArg() != null) {
            arrayList.add(getNextArg());
        }
        CcType type = getType("cc set-value: ", nextArg, configForSubId);
        if (type == CcType.UNKNOWN) {
            errPrintWriter.println("cc set-value: ERROR: Not possible to override key with unknown type.");
            return -1;
        }
        if (type == CcType.PERSISTABLE_BUNDLE) {
            errPrintWriter.println("cc set-value: ERROR: Overriding of persistable bundle type is not supported. Use set-values-from-xml instead.");
            return -1;
        }
        PersistableBundle overrideBundle = getOverrideBundle("cc set-value: ", type, nextArg, arrayList);
        if (overrideBundle == null) {
            return -1;
        }
        this.mCarrierConfigManager.overrideConfig(ccOptions.mSubId, overrideBundle, ccOptions.mPersistent);
        PersistableBundle configForSubId2 = this.mCarrierConfigManager.getConfigForSubId(ccOptions.mSubId);
        if (configForSubId2 == null) {
            errPrintWriter.println("cc set-value: No carrier config values found for subId " + ccOptions.mSubId + ".");
            return -1;
        }
        String strCcValueToString = ccValueToString(nextArg, type, configForSubId);
        String strCcValueToString2 = ccValueToString(nextArg, type, configForSubId2);
        getOutPrintWriter().println("Previous value: \n" + strCcValueToString);
        getOutPrintWriter().println("New value: \n" + strCcValueToString2);
        return 0;
    }

    private int handleCcSetValuesFromXml() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        final String str = "cc set-values-from-xml: ";
        CcOptionParseResult ccOptions = parseCcOptions("cc set-values-from-xml: ", true);
        if (ccOptions == null) {
            return -1;
        }
        final PersistableBundle configForSubId = this.mCarrierConfigManager.getConfigForSubId(ccOptions.mSubId);
        if (configForSubId == null) {
            errPrintWriter.println("cc set-values-from-xml: No carrier config values found for subId " + ccOptions.mSubId + ".");
            return -1;
        }
        PersistableBundle persistableBundleFromXml = readPersistableBundleFromXml("cc set-values-from-xml: ");
        if (persistableBundleFromXml == null) {
            return -1;
        }
        Iterator<String> it = persistableBundleFromXml.keySet().iterator();
        while (it.hasNext()) {
            if (getType("cc set-values-from-xml: ", it.next(), configForSubId) == CcType.UNKNOWN) {
                errPrintWriter.println("cc set-values-from-xml: ERROR: Not possible to override key with unknown type.");
                return -1;
            }
        }
        this.mCarrierConfigManager.overrideConfig(ccOptions.mSubId, persistableBundleFromXml, ccOptions.mPersistent);
        final PersistableBundle configForSubId2 = this.mCarrierConfigManager.getConfigForSubId(ccOptions.mSubId);
        if (configForSubId2 == null) {
            errPrintWriter.println("cc set-values-from-xml: No carrier config values found for subId " + ccOptions.mSubId + ".");
            return -1;
        }
        persistableBundleFromXml.keySet().forEach(new Consumer() { // from class: com.android.phone.TelephonyShellCommand$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$handleCcSetValuesFromXml$0(str, configForSubId, configForSubId2, (String) obj);
            }
        });
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleCcSetValuesFromXml$0(String str, PersistableBundle persistableBundle, PersistableBundle persistableBundle2, String str2) {
        CcType type = getType(str, str2, persistableBundle);
        String strCcValueToString = ccValueToString(str2, type, persistableBundle);
        String strCcValueToString2 = ccValueToString(str2, type, persistableBundle2);
        getOutPrintWriter().println("Previous value: \n" + strCcValueToString);
        getOutPrintWriter().println("New value: \n" + strCcValueToString2);
    }

    private PersistableBundle readPersistableBundleFromXml(String str) {
        try {
            return PersistableBundle.readFromStream(getRawInputStream());
        } catch (IOException | RuntimeException e) {
            getErrPrintWriter().println(str + e);
            return null;
        }
    }

    private int handleCcClearValues() {
        CcOptionParseResult ccOptions = parseCcOptions("cc clear-values: ", false);
        if (ccOptions == null) {
            return -1;
        }
        this.mCarrierConfigManager.overrideConfig(ccOptions.mSubId, null, true);
        getOutPrintWriter().println("All previously set carrier config override values has been cleared");
        return 0;
    }

    private CcType getType(String str, String str2, PersistableBundle persistableBundle) {
        Object obj = persistableBundle.get(str2);
        Map map = CC_TYPE_MAP;
        if (map.containsKey(str2)) {
            return (CcType) map.get(str2);
        }
        if (obj != null) {
            if (obj instanceof Boolean) {
                return CcType.BOOLEAN;
            }
            if (obj instanceof boolean[]) {
                return CcType.BOOLEAN_ARRAY;
            }
            if (obj instanceof Double) {
                return CcType.DOUBLE;
            }
            if (obj instanceof double[]) {
                return CcType.DOUBLE_ARRAY;
            }
            if (obj instanceof Integer) {
                return CcType.INT;
            }
            if (obj instanceof int[]) {
                return CcType.INT_ARRAY;
            }
            if (obj instanceof Long) {
                return CcType.LONG;
            }
            if (obj instanceof long[]) {
                return CcType.LONG_ARRAY;
            }
            if (obj instanceof String) {
                return CcType.STRING;
            }
            if (obj instanceof String[]) {
                return CcType.STRING_ARRAY;
            }
            if (obj instanceof PersistableBundle) {
                return CcType.PERSISTABLE_BUNDLE;
            }
        } else {
            if (str2.endsWith("bool_array")) {
                return CcType.BOOLEAN_ARRAY;
            }
            if (str2.endsWith("double_array")) {
                return CcType.DOUBLE_ARRAY;
            }
            if (str2.endsWith("int_array")) {
                return CcType.INT_ARRAY;
            }
            if (str2.endsWith("long_array")) {
                return CcType.LONG_ARRAY;
            }
            if (str2.endsWith(KeyStringSettingItem.TYPE)) {
                return CcType.STRING;
            }
            if (str2.endsWith("string_array") || str2.endsWith("strings")) {
                return CcType.STRING_ARRAY;
            }
            if (str2.endsWith("bundle")) {
                return CcType.PERSISTABLE_BUNDLE;
            }
        }
        getErrPrintWriter().println(str + "ERROR: " + str2 + " has unknown type.");
        return CcType.UNKNOWN;
    }

    private String ccValueToString(String str, CcType ccType, PersistableBundle persistableBundle) {
        StringBuilder sb = new StringBuilder();
        String string = ccType.toString();
        Object obj = persistableBundle.get(str);
        if (obj == null) {
            sb.append("null");
        } else {
            int iOrdinal = ccType.ordinal();
            int i = 0;
            if (iOrdinal == 1) {
                boolean[] zArr = (boolean[]) obj;
                while (i < zArr.length) {
                    if (i != 0) {
                        sb.append(" ");
                    }
                    sb.append(zArr[i]);
                    i++;
                }
            } else if (iOrdinal == 3) {
                double[] dArr = (double[]) obj;
                while (i < dArr.length) {
                    if (i != 0) {
                        sb.append(" ");
                    }
                    sb.append(dArr[i]);
                    i++;
                }
            } else if (iOrdinal == 5) {
                int[] iArr = (int[]) obj;
                while (i < iArr.length) {
                    if (i != 0) {
                        sb.append(" ");
                    }
                    sb.append(iArr[i]);
                    i++;
                }
            } else if (iOrdinal == 7) {
                long[] jArr = (long[]) obj;
                while (i < jArr.length) {
                    if (i != 0) {
                        sb.append(" ");
                    }
                    sb.append(jArr[i]);
                    i++;
                }
            } else if (iOrdinal == 8) {
                sb.append("\"" + obj.toString() + "\"");
            } else if (iOrdinal == 9) {
                String[] strArr = (String[]) obj;
                while (i < strArr.length) {
                    if (i != 0) {
                        sb.append(" ");
                    }
                    if (strArr[i] != null) {
                        sb.append("\"" + strArr[i] + "\"");
                    } else {
                        sb.append("null");
                    }
                    i++;
                }
            } else {
                sb.append(obj.toString());
            }
        }
        return String.format("%-70s %-15s %s", str, string, sb);
    }

    private PersistableBundle getOverrideBundle(String str, CcType ccType, String str2, ArrayList arrayList) {
        PrintWriter errPrintWriter = getErrPrintWriter();
        PersistableBundle persistableBundle = new PersistableBundle();
        int iOrdinal = ccType.ordinal();
        boolean[] zArr = null;
        String[] strArr = null;
        long[] jArr = null;
        int[] iArr = null;
        double[] dArr = null;
        if (iOrdinal == 0 || iOrdinal == 2 || iOrdinal == 4 || iOrdinal == 6) {
            if (arrayList.size() != 1) {
                errPrintWriter.println(str + "Expected 1 value for type " + ccType + ". Found: " + arrayList.size());
                return null;
            }
        } else if (iOrdinal == 8 && arrayList.size() > 1) {
            errPrintWriter.println(str + "Expected 0 or 1 values for type " + ccType + ". Found: " + arrayList.size());
            return null;
        }
        int i = 0;
        switch (ccType) {
            case BOOLEAN:
                if ("true".equalsIgnoreCase((String) arrayList.get(0))) {
                    persistableBundle.putBoolean(str2, true);
                    return persistableBundle;
                }
                if ("false".equalsIgnoreCase((String) arrayList.get(0))) {
                    persistableBundle.putBoolean(str2, false);
                    return persistableBundle;
                }
                errPrintWriter.println(str + "Unable to parse " + ((String) arrayList.get(0)) + " as a " + ccType);
                return null;
            case BOOLEAN_ARRAY:
                if (arrayList.size() > 0) {
                    boolean[] zArr2 = new boolean[arrayList.size()];
                    for (int i2 = 0; i2 < arrayList.size(); i2++) {
                        if ("true".equalsIgnoreCase((String) arrayList.get(i2))) {
                            zArr2[i2] = true;
                        } else {
                            if (!"false".equalsIgnoreCase((String) arrayList.get(i2))) {
                                errPrintWriter.println(str + "Unable to parse " + ((String) arrayList.get(i2)) + " as a boolean.");
                                return null;
                            }
                            zArr2[i2] = false;
                        }
                    }
                    zArr = zArr2;
                }
                persistableBundle.putBooleanArray(str2, zArr);
                return persistableBundle;
            case DOUBLE:
                try {
                    persistableBundle.putDouble(str2, Double.parseDouble((String) arrayList.get(0)));
                    return persistableBundle;
                } catch (NumberFormatException unused) {
                    errPrintWriter.println(str + "Unable to parse " + ((String) arrayList.get(0)) + " as a " + ccType);
                    return null;
                }
            case DOUBLE_ARRAY:
                if (arrayList.size() > 0) {
                    double[] dArr2 = new double[arrayList.size()];
                    while (i < arrayList.size()) {
                        try {
                            dArr2[i] = Double.parseDouble((String) arrayList.get(i));
                            i++;
                        } catch (NumberFormatException unused2) {
                            errPrintWriter.println(str + "Unable to parse " + ((String) arrayList.get(i)) + " as a double.");
                            return null;
                        }
                    }
                    dArr = dArr2;
                }
                persistableBundle.putDoubleArray(str2, dArr);
                return persistableBundle;
            case INT:
                try {
                    persistableBundle.putInt(str2, Integer.parseInt((String) arrayList.get(0)));
                    return persistableBundle;
                } catch (NumberFormatException unused3) {
                    errPrintWriter.println(str + "Unable to parse " + ((String) arrayList.get(0)) + " as an " + ccType);
                    return null;
                }
            case INT_ARRAY:
                if (arrayList.size() > 0) {
                    int[] iArr2 = new int[arrayList.size()];
                    while (i < arrayList.size()) {
                        try {
                            iArr2[i] = Integer.parseInt((String) arrayList.get(i));
                            i++;
                        } catch (NumberFormatException unused4) {
                            errPrintWriter.println(str + "Unable to parse " + ((String) arrayList.get(i)) + " as an integer.");
                            return null;
                        }
                    }
                    iArr = iArr2;
                }
                persistableBundle.putIntArray(str2, iArr);
                return persistableBundle;
            case LONG:
                try {
                    persistableBundle.putLong(str2, Long.parseLong((String) arrayList.get(0)));
                    return persistableBundle;
                } catch (NumberFormatException unused5) {
                    errPrintWriter.println(str + "Unable to parse " + ((String) arrayList.get(0)) + " as a " + ccType);
                    return null;
                }
            case LONG_ARRAY:
                if (arrayList.size() > 0) {
                    long[] jArr2 = new long[arrayList.size()];
                    while (i < arrayList.size()) {
                        try {
                            jArr2[i] = Long.parseLong((String) arrayList.get(i));
                            i++;
                        } catch (NumberFormatException unused6) {
                            errPrintWriter.println(str + "Unable to parse " + ((String) arrayList.get(i)) + " as a long");
                            return null;
                        }
                    }
                    jArr = jArr2;
                }
                persistableBundle.putLongArray(str2, jArr);
                return persistableBundle;
            case STRING:
                persistableBundle.putString(str2, arrayList.size() > 0 ? (String) arrayList.get(0) : null);
                return persistableBundle;
            case STRING_ARRAY:
                if (arrayList.size() > 0) {
                    strArr = new String[arrayList.size()];
                    arrayList.toArray(strArr);
                }
                persistableBundle.putStringArray(str2, strArr);
                return persistableBundle;
            default:
                return persistableBundle;
        }
    }

    private int handleEndBlockSuppressionCommand() {
        if (!checkShellUid()) {
            return -1;
        }
        if (!BlockedNumberContract.SystemContract.getBlockSuppressionStatus(this.mContext).isSuppressed) {
            return 0;
        }
        BlockedNumberContract.SystemContract.endBlockSuppression(this.mContext);
        return 0;
    }

    private int handleRestartModemCommand() {
        if (!UserHandle.isSameApp(Binder.getCallingUid(), 0) || TelephonyUtils.IS_USER) {
            getErrPrintWriter().println("RestartModem: Permission denied.");
            return -1;
        }
        boolean zRebootRadio = TelephonyManager.getDefault().rebootRadio();
        getOutPrintWriter().println(zRebootRadio);
        return zRebootRadio ? 0 : -1;
    }

    private int handleGetImei() {
        String imei;
        if (!UserHandle.isSameApp(Binder.getCallingUid(), 0) || TelephonyUtils.IS_USER) {
            getErrPrintWriter().println("Device IMEI: Permission denied.");
            return -1;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        String nextArg = getNextArg();
        if (nextArg != null) {
            try {
                imei = TelephonyManager.from(this.mContext).getImei(Integer.parseInt(nextArg));
            } catch (NumberFormatException unused) {
                getErrPrintWriter().println("-s requires an integer as slot index.");
                return -1;
            }
        } else {
            imei = TelephonyManager.from(this.mContext).getImei();
        }
        getOutPrintWriter().println("Device IMEI: " + imei);
        Binder.restoreCallingIdentity(jClearCallingIdentity);
        return 0;
    }

    private int handleGetPhoneNumber() {
        if (!checkShellUid()) {
            Log.v("TelephonyShellCommand", "handleGetPhoneNumber checkShellUid fails");
            return -1;
        }
        PrintWriter outPrintWriter = getOutPrintWriter();
        try {
            int i = Integer.parseInt(getNextArgRequired());
            int i2 = Integer.parseInt(getNextArgRequired());
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                String phoneNumber = this.mSubscriptionManager.getPhoneNumber(i, i2);
                if (phoneNumber == null) {
                    phoneNumber = "";
                }
                outPrintWriter.println(phoneNumber);
                return 0;
            } catch (Exception e) {
                outPrintWriter.println("Error: " + e.getMessage());
                return -1;
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        } catch (Exception unused) {
            outPrintWriter.println("Error: subId and source are required and must be integers.");
            onHelpGetPhoneNumber();
            return -1;
        }
    }

    private int handleUnattendedReboot() {
        if (!UserHandle.isSameApp(Binder.getCallingUid(), 0) || TelephonyUtils.IS_USER) {
            getErrPrintWriter().println("UnattendedReboot: Permission denied.");
            return -1;
        }
        int iPrepareForUnattendedReboot = TelephonyManager.getDefault().prepareForUnattendedReboot();
        getOutPrintWriter().println("result: " + iPrepareForUnattendedReboot);
        return iPrepareForUnattendedReboot != 2 ? 0 : -1;
    }

    private int handleGetSimSlotsMapping() {
        if (!UserHandle.isSameApp(Binder.getCallingUid(), 0) || TelephonyUtils.IS_USER) {
            getErrPrintWriter().println("GetSimSlotsMapping: Permission denied.");
            return -1;
        }
        String string = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).getSimSlotMapping().toString();
        getOutPrintWriter().println("simSlotsMapping: " + string);
        return 0;
    }

    private int handleGbaCommand() {
        String nextArg = getNextArg();
        if (nextArg == null) {
            onHelpGba();
            return 0;
        }
        nextArg.hashCode();
        switch (nextArg) {
            case "set-release":
                return handleGbaSetReleaseCommand();
            case "get-release":
                return handleGbaGetReleaseCommand();
            case "set-service":
                return handleGbaSetServiceCommand();
            case "get-service":
                return handleGbaGetServiceCommand();
            default:
                return -1;
        }
    }

    private int getSubId(String str) {
        int defaultSlot = getDefaultSlot();
        String nextOption = getNextOption();
        if (nextOption != null && nextOption.equals("-s")) {
            try {
                defaultSlot = Integer.parseInt(getNextArgRequired());
            } catch (NumberFormatException unused) {
                getErrPrintWriter().println(str + " requires an integer as a SLOT_ID.");
                return -1;
            }
        }
        return SubscriptionManager.getSubscriptionId(defaultSlot);
    }

    private int handleGbaSetServiceCommand() {
        int subId = getSubId("gba set-service");
        if (subId == -1) {
            return -1;
        }
        String nextArg = getNextArg();
        if (nextArg == null) {
            nextArg = "";
        }
        try {
            boolean boundGbaServiceOverride = this.mInterface.setBoundGbaServiceOverride(subId, nextArg);
            Log.v("TelephonyShellCommand", "gba set-service -s " + subId + " " + nextArg + ", result=" + boundGbaServiceOverride);
            getOutPrintWriter().println(boundGbaServiceOverride);
            return 0;
        } catch (RemoteException e) {
            Log.w("TelephonyShellCommand", "gba set-service " + subId + " " + nextArg + ", error" + e.getMessage());
            PrintWriter errPrintWriter = getErrPrintWriter();
            StringBuilder sb = new StringBuilder("Exception: ");
            sb.append(e.getMessage());
            errPrintWriter.println(sb.toString());
            return -1;
        }
    }

    private int handleGbaGetServiceCommand() {
        int subId = getSubId("gba get-service");
        if (subId == -1) {
            return -1;
        }
        try {
            String boundGbaService = this.mInterface.getBoundGbaService(subId);
            Log.v("TelephonyShellCommand", "gba get-service -s " + subId + ", returned: " + boundGbaService);
            getOutPrintWriter().println(boundGbaService);
            return 0;
        } catch (RemoteException unused) {
            return -1;
        }
    }

    private int handleGbaSetReleaseCommand() {
        String nextArg;
        int subId = getRemainingArgsCount() > 1 ? getSubId("gba set-release") : SubscriptionManager.getDefaultSubscriptionId();
        if (subId == -1 || (nextArg = getNextArg()) == null) {
            return -1;
        }
        try {
            boolean gbaReleaseTimeOverride = this.mInterface.setGbaReleaseTimeOverride(subId, Integer.parseInt(nextArg));
            Log.v("TelephonyShellCommand", "gba set-release -s " + subId + " " + nextArg + ", result=" + gbaReleaseTimeOverride);
            getOutPrintWriter().println(gbaReleaseTimeOverride);
            return 0;
        } catch (RemoteException | NumberFormatException e) {
            Log.w("TelephonyShellCommand", "gba set-release -s " + subId + " " + nextArg + ", error" + e.getMessage());
            PrintWriter errPrintWriter = getErrPrintWriter();
            StringBuilder sb = new StringBuilder("Exception: ");
            sb.append(e.getMessage());
            errPrintWriter.println(sb.toString());
            return -1;
        }
    }

    private int handleGbaGetReleaseCommand() {
        int subId = getSubId("gba get-release");
        if (subId == -1) {
            return -1;
        }
        try {
            int gbaReleaseTime = this.mInterface.getGbaReleaseTime(subId);
            Log.v("TelephonyShellCommand", "gba get-release -s " + subId + ", returned: " + gbaReleaseTime);
            getOutPrintWriter().println(gbaReleaseTime);
            return 0;
        } catch (RemoteException unused) {
            return -1;
        }
    }

    private int handleSingleRegistrationConfigCommand() {
        String nextArg = getNextArg();
        if (nextArg == null) {
            onHelpSrc();
            return 0;
        }
        nextArg.hashCode();
        switch (nextArg) {
            case "get-test-enabled":
                return handleSrcGetTestEnabledCommand();
            case "get-device-enabled":
                return handleSrcGetDeviceEnabledCommand();
            case "get-carrier-enabled":
                return handleSrcGetCarrierEnabledCommand();
            case "set-device-enabled":
                return handleSrcSetDeviceEnabledCommand();
            case "set-test-enabled":
                return handleSrcSetTestEnabledCommand();
            case "get-feature-validation":
                return handleSrcGetFeatureValidationCommand();
            case "set-carrier-enabled":
                return handleSrcSetCarrierEnabledCommand();
            case "set-feature-validation":
                return handleSrcSetFeatureValidationCommand();
            default:
                return -1;
        }
    }

    private int handleRcsUceCommand() {
        String nextArg = getNextArg();
        if (nextArg == null) {
            onHelpUce();
            return 0;
        }
        nextArg.hashCode();
        switch (nextArg) {
            case "get-device-enabled":
                return handleUceGetDeviceEnabledCommand();
            case "set-capabilities-request-timeout":
                return handleUceSetCapRequestTimeout();
            case "get-last-publish-pidf":
                return handleUceGetPidfXml();
            case "set-device-enabled":
                return handleUceSetDeviceEnabledCommand();
            case "override-published-caps":
                return handleUceOverridePublishCaps();
            case "get-eab-contact":
                return handleGettingEabContactCommand();
            case "get-eab-capability":
                return handleGettingEabCapabilityCommand();
            case "remove-request-disallowed-status":
                return handleUceRemoveRequestDisallowedStatus();
            case "remove-eab-contact":
                return handleRemovingEabContactCommand();
            default:
                return -1;
        }
    }

    private int handleRemovingEabContactCommand() {
        int subId = getSubId("uce remove-eab-contact");
        if (subId == -1) {
            return -1;
        }
        String nextArgRequired = getNextArgRequired();
        if (TextUtils.isEmpty(nextArgRequired)) {
            return -1;
        }
        try {
            Log.v("TelephonyShellCommand", "uce remove-eab-contact -s " + subId + ", result: " + this.mInterface.removeContactFromEab(subId, nextArgRequired));
            return 0;
        } catch (RemoteException e) {
            Log.w("TelephonyShellCommand", "uce remove-eab-contact -s " + subId + ", error " + e.getMessage());
            PrintWriter errPrintWriter = getErrPrintWriter();
            StringBuilder sb = new StringBuilder("Exception: ");
            sb.append(e.getMessage());
            errPrintWriter.println(sb.toString());
            return -1;
        }
    }

    private int handleGettingEabContactCommand() {
        String nextArgRequired = getNextArgRequired();
        if (TextUtils.isEmpty(nextArgRequired)) {
            return -1;
        }
        try {
            String contactFromEab = this.mInterface.getContactFromEab(nextArgRequired);
            Log.v("TelephonyShellCommand", "uce get-eab-contact, result: " + contactFromEab);
            getOutPrintWriter().println(contactFromEab);
            return 0;
        } catch (RemoteException e) {
            Log.w("TelephonyShellCommand", "uce get-eab-contact, error " + e.getMessage());
            getErrPrintWriter().println("Exception: " + e.getMessage());
            return -1;
        }
    }

    private int handleGettingEabCapabilityCommand() {
        String nextArgRequired = getNextArgRequired();
        if (TextUtils.isEmpty(nextArgRequired)) {
            return -1;
        }
        try {
            String capabilityFromEab = this.mInterface.getCapabilityFromEab(nextArgRequired);
            Log.v("TelephonyShellCommand", "uce get-eab-capability, result: " + capabilityFromEab);
            getOutPrintWriter().println(capabilityFromEab);
            return 0;
        } catch (RemoteException e) {
            Log.w("TelephonyShellCommand", "uce get-eab-capability, error " + e.getMessage());
            getErrPrintWriter().println("Exception: " + e.getMessage());
            return -1;
        }
    }

    private int handleUceGetDeviceEnabledCommand() {
        try {
            boolean deviceUceEnabled = this.mInterface.getDeviceUceEnabled();
            Log.v("TelephonyShellCommand", "uce get-device-enabled, returned: " + deviceUceEnabled);
            getOutPrintWriter().println(deviceUceEnabled);
            return 0;
        } catch (RemoteException e) {
            Log.w("TelephonyShellCommand", "uce get-device-enabled, error " + e.getMessage());
            return -1;
        }
    }

    private int handleUceSetDeviceEnabledCommand() {
        String nextArg = getNextArg();
        if (TextUtils.isEmpty(nextArg)) {
            return -1;
        }
        try {
            this.mInterface.setDeviceUceEnabled(Boolean.parseBoolean(nextArg));
            Log.v("TelephonyShellCommand", "uce set-device-enabled " + nextArg + ", done");
            return 0;
        } catch (RemoteException | NumberFormatException e) {
            Log.w("TelephonyShellCommand", "uce set-device-enabled " + nextArg + ", error " + e.getMessage());
            PrintWriter errPrintWriter = getErrPrintWriter();
            StringBuilder sb = new StringBuilder("Exception: ");
            sb.append(e.getMessage());
            errPrintWriter.println(sb.toString());
            return -1;
        }
    }

    private int handleUceRemoveRequestDisallowedStatus() {
        int subId = getSubId("uce remove-request-disallowed-status");
        if (subId == -1) {
            Log.w("TelephonyShellCommand", "uce remove-request-disallowed-status, Invalid subscription ID");
            return -1;
        }
        try {
            boolean zRemoveUceRequestDisallowedStatus = this.mInterface.removeUceRequestDisallowedStatus(subId);
            Log.v("TelephonyShellCommand", "uce remove-request-disallowed-status, returned: " + zRemoveUceRequestDisallowedStatus);
            getOutPrintWriter().println(zRemoveUceRequestDisallowedStatus);
            return 0;
        } catch (RemoteException e) {
            Log.w("TelephonyShellCommand", "uce remove-request-disallowed-status, error " + e.getMessage());
            return -1;
        }
    }

    private int handleUceSetCapRequestTimeout() {
        int subId = getSubId("uce set-capabilities-request-timeout");
        if (subId == -1) {
            Log.w("TelephonyShellCommand", "uce set-capabilities-request-timeout, Invalid subscription ID");
            return -1;
        }
        try {
            boolean capabilitiesRequestTimeout = this.mInterface.setCapabilitiesRequestTimeout(subId, Long.valueOf(getNextArg()).longValue());
            Log.v("TelephonyShellCommand", "uce set-capabilities-request-timeout, returned: " + capabilitiesRequestTimeout);
            getOutPrintWriter().println(capabilitiesRequestTimeout);
            return 0;
        } catch (RemoteException e) {
            Log.w("TelephonyShellCommand", "uce set-capabilities-request-timeout, error " + e.getMessage());
            return -1;
        }
    }

    private int handleSrcSetTestEnabledCommand() {
        String nextArg = getNextArg();
        if (nextArg == null) {
            return -1;
        }
        try {
            this.mInterface.setRcsSingleRegistrationTestModeEnabled(Boolean.parseBoolean(nextArg));
            Log.v("TelephonyShellCommand", "src set-test-enabled " + nextArg + ", done");
            getOutPrintWriter().println("Done");
            return 0;
        } catch (RemoteException | NumberFormatException e) {
            Log.w("TelephonyShellCommand", "src set-test-enabled " + nextArg + ", error" + e.getMessage());
            PrintWriter errPrintWriter = getErrPrintWriter();
            StringBuilder sb = new StringBuilder("Exception: ");
            sb.append(e.getMessage());
            errPrintWriter.println(sb.toString());
            return -1;
        }
    }

    private int handleSrcGetTestEnabledCommand() {
        try {
            boolean rcsSingleRegistrationTestModeEnabled = this.mInterface.getRcsSingleRegistrationTestModeEnabled();
            Log.v("TelephonyShellCommand", "src get-test-enabled, returned: " + rcsSingleRegistrationTestModeEnabled);
            getOutPrintWriter().println(rcsSingleRegistrationTestModeEnabled);
            return 0;
        } catch (RemoteException unused) {
            return -1;
        }
    }

    /* JADX WARN: Code duplicated, block: B:49:0x0102  */
    private int handleUceOverridePublishCaps() {
        RcsContactUceCapability rcsContactUceCapabilityRemoveUceRegistrationOverrideShell;
        int subId = getSubId("uce override-published-caps");
        int i = -1;
        if (subId == -1) {
            return -1;
        }
        String nextArgRequired = getNextArgRequired();
        String nextArg = getNextArg();
        if (!"add".equals(nextArgRequired) && !"remove".equals(nextArgRequired) && !"clear".equals(nextArgRequired) && !"list".equals(nextArgRequired)) {
            getErrPrintWriter().println("Invalid operation: " + nextArgRequired);
            return -1;
        }
        if (!"clear".equals(nextArgRequired) && !"list".equals(nextArgRequired) && TextUtils.isEmpty(nextArg)) {
            getErrPrintWriter().println("\"" + nextArgRequired + "\" requires capabilities to be specified");
            return -1;
        }
        ArraySet arraySet = new ArraySet();
        if (!TextUtils.isEmpty(nextArg)) {
            String[] strArrSplit = nextArg.split(":");
            int length = strArrSplit.length;
            int i2 = 0;
            while (i2 < length) {
                int i3 = i;
                String str = strArrSplit[i2];
                arraySet.addAll((Collection) TEST_FEATURE_TAG_MAP.getOrDefault(str, Collections.singleton(str)));
                i2++;
                i = i3;
                strArrSplit = strArrSplit;
            }
        }
        int i4 = i;
        try {
            switch (nextArgRequired.hashCode()) {
                case -934610812:
                    if (!nextArgRequired.equals("remove")) {
                        rcsContactUceCapabilityRemoveUceRegistrationOverrideShell = null;
                    } else {
                        rcsContactUceCapabilityRemoveUceRegistrationOverrideShell = this.mInterface.removeUceRegistrationOverrideShell(subId, new ArrayList(arraySet));
                    }
                    break;
                case 96417:
                    if (!nextArgRequired.equals("add")) {
                        rcsContactUceCapabilityRemoveUceRegistrationOverrideShell = null;
                    } else {
                        rcsContactUceCapabilityRemoveUceRegistrationOverrideShell = this.mInterface.addUceRegistrationOverrideShell(subId, new ArrayList(arraySet));
                    }
                    break;
                case 3322014:
                    if (!nextArgRequired.equals("list")) {
                        rcsContactUceCapabilityRemoveUceRegistrationOverrideShell = null;
                    } else {
                        rcsContactUceCapabilityRemoveUceRegistrationOverrideShell = this.mInterface.getLatestRcsContactUceCapabilityShell(subId);
                    }
                    break;
                case 94746189:
                    if (!nextArgRequired.equals("clear")) {
                        rcsContactUceCapabilityRemoveUceRegistrationOverrideShell = null;
                    } else {
                        rcsContactUceCapabilityRemoveUceRegistrationOverrideShell = this.mInterface.clearUceRegistrationOverrideShell(subId);
                    }
                    break;
                default:
                    rcsContactUceCapabilityRemoveUceRegistrationOverrideShell = null;
                    break;
            }
            if (rcsContactUceCapabilityRemoveUceRegistrationOverrideShell == null) {
                getErrPrintWriter().println("Service not available");
                return i4;
            }
            getOutPrintWriter().println(rcsContactUceCapabilityRemoveUceRegistrationOverrideShell);
            return 0;
        } catch (RemoteException e) {
            Log.w("TelephonyShellCommand", "uce override-published-caps, error " + e.getMessage());
            getErrPrintWriter().println("Exception: " + e.getMessage());
            return i4;
        } catch (ServiceSpecificException e2) {
            ImsException imsException = new ImsException(e2.getMessage(), e2.errorCode);
            Log.w("TelephonyShellCommand", "uce override-published-caps, error " + imsException);
            getErrPrintWriter().println("Exception: " + imsException);
            return i4;
        }
    }

    private int handleUceGetPidfXml() {
        int subId = getSubId("uce get-last-publish-pidf");
        if (subId == -1) {
            return -1;
        }
        try {
            String lastUcePidfXmlShell = this.mInterface.getLastUcePidfXmlShell(subId);
            if (lastUcePidfXmlShell == null) {
                getErrPrintWriter().println("Service not available");
                return -1;
            }
            getOutPrintWriter().println(lastUcePidfXmlShell);
            return 0;
        } catch (RemoteException e) {
            Log.w("TelephonyShellCommand", "uce get-last-publish-pidf, error " + e.getMessage());
            getErrPrintWriter().println("Exception: " + e.getMessage());
            return -1;
        } catch (ServiceSpecificException e2) {
            ImsException imsException = new ImsException(e2.getMessage(), e2.errorCode);
            Log.w("TelephonyShellCommand", "uce get-last-publish-pidf error " + imsException);
            getErrPrintWriter().println("Exception: " + imsException);
            return -1;
        }
    }

    private int handleSrcSetDeviceEnabledCommand() {
        String nextArg = getNextArg();
        if (nextArg == null) {
            return -1;
        }
        try {
            this.mInterface.setDeviceSingleRegistrationEnabledOverride(nextArg);
            Log.v("TelephonyShellCommand", "src set-device-enabled " + nextArg + ", done");
            getOutPrintWriter().println("Done");
            return 0;
        } catch (RemoteException | NumberFormatException e) {
            Log.w("TelephonyShellCommand", "src set-device-enabled " + nextArg + ", error" + e.getMessage());
            PrintWriter errPrintWriter = getErrPrintWriter();
            StringBuilder sb = new StringBuilder("Exception: ");
            sb.append(e.getMessage());
            errPrintWriter.println(sb.toString());
            return -1;
        }
    }

    private int handleSrcGetDeviceEnabledCommand() {
        try {
            boolean deviceSingleRegistrationEnabled = this.mInterface.getDeviceSingleRegistrationEnabled();
            Log.v("TelephonyShellCommand", "src get-device-enabled, returned: " + deviceSingleRegistrationEnabled);
            getOutPrintWriter().println(deviceSingleRegistrationEnabled);
            return 0;
        } catch (RemoteException unused) {
            return -1;
        }
    }

    private int handleSrcSetCarrierEnabledCommand() {
        String nextArg;
        int subId = getRemainingArgsCount() > 1 ? getSubId("src set-carrier-enabled") : SubscriptionManager.getDefaultSubscriptionId();
        if (subId == -1 || (nextArg = getNextArg()) == null) {
            return -1;
        }
        try {
            boolean carrierSingleRegistrationEnabledOverride = this.mInterface.setCarrierSingleRegistrationEnabledOverride(subId, nextArg);
            Log.v("TelephonyShellCommand", "src set-carrier-enabled -s " + subId + " " + nextArg + ", result=" + carrierSingleRegistrationEnabledOverride);
            getOutPrintWriter().println(carrierSingleRegistrationEnabledOverride);
            return 0;
        } catch (RemoteException | NumberFormatException e) {
            Log.w("TelephonyShellCommand", "src set-carrier-enabled -s " + subId + " " + nextArg + ", error" + e.getMessage());
            PrintWriter errPrintWriter = getErrPrintWriter();
            StringBuilder sb = new StringBuilder("Exception: ");
            sb.append(e.getMessage());
            errPrintWriter.println(sb.toString());
            return -1;
        }
    }

    private int handleSrcGetCarrierEnabledCommand() {
        int subId = getSubId("src get-carrier-enabled");
        if (subId == -1) {
            return -1;
        }
        try {
            boolean carrierSingleRegistrationEnabled = this.mInterface.getCarrierSingleRegistrationEnabled(subId);
            Log.v("TelephonyShellCommand", "src get-carrier-enabled -s " + subId + ", returned: " + carrierSingleRegistrationEnabled);
            getOutPrintWriter().println(carrierSingleRegistrationEnabled);
            return 0;
        } catch (RemoteException unused) {
            return -1;
        }
    }

    private int handleSrcSetFeatureValidationCommand() {
        String nextArg;
        int subId = getRemainingArgsCount() > 1 ? getSubId("src set-feature-validation") : SubscriptionManager.getDefaultSubscriptionId();
        if (subId == -1 || (nextArg = getNextArg()) == null) {
            return -1;
        }
        try {
            boolean imsFeatureValidationOverride = this.mInterface.setImsFeatureValidationOverride(subId, nextArg);
            Log.v("TelephonyShellCommand", "src set-feature-validation -s " + subId + " " + nextArg + ", result=" + imsFeatureValidationOverride);
            getOutPrintWriter().println(imsFeatureValidationOverride);
            return 0;
        } catch (RemoteException | NumberFormatException e) {
            Log.w("TelephonyShellCommand", "src set-feature-validation -s " + subId + " " + nextArg + ", error" + e.getMessage());
            PrintWriter errPrintWriter = getErrPrintWriter();
            StringBuilder sb = new StringBuilder("Exception: ");
            sb.append(e.getMessage());
            errPrintWriter.println(sb.toString());
            return -1;
        }
    }

    private int handleSrcGetFeatureValidationCommand() {
        int subId = getSubId("src get-feature-validation");
        if (subId == -1) {
            return -1;
        }
        try {
            Boolean boolValueOf = Boolean.valueOf(this.mInterface.getImsFeatureValidationOverride(subId));
            Log.v("TelephonyShellCommand", "src get-feature-validation -s " + subId + ", returned: " + boolValueOf);
            getOutPrintWriter().println(boolValueOf);
            return 0;
        } catch (RemoteException unused) {
            return -1;
        }
    }

    private void onHelpCallComposer() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("Call composer commands");
        outPrintWriter.println("  callcomposer test-mode enable|disable|query");
        outPrintWriter.println("    Enables or disables test mode for call composer. In test mode, picture");
        outPrintWriter.println("    upload/download from carrier servers is disabled, and operations are");
        outPrintWriter.println("    performed using emulated local files instead.");
        outPrintWriter.println("  callcomposer simulate-outgoing-call [subId] [UUID]");
        outPrintWriter.println("    Simulates an outgoing call being placed with the picture ID as");
        outPrintWriter.println("    the provided UUID. This triggers storage to the call log.");
        outPrintWriter.println("  callcomposer user-setting [subId] enable|disable|query");
        outPrintWriter.println("    Enables or disables the user setting for call composer, as set by");
        outPrintWriter.println("    TelephonyManager#setCallComposerStatus.");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private int handleCallComposerCommand() {
        String nextArg = getNextArg();
        if (nextArg == null) {
            onHelpCallComposer();
            return 0;
        }
        this.mContext.enforceCallingPermission("android.permission.MODIFY_PHONE_STATE", "MODIFY_PHONE_STATE required for call composer shell cmds");
        nextArg.hashCode();
        switch (nextArg) {
            case "user-setting":
                try {
                    int iIntValue = Integer.valueOf(getNextArg()).intValue();
                    String nextArg2 = getNextArg();
                    if ("enable".equals(nextArg2)) {
                        this.mInterface.setCallComposerStatus(iIntValue, 1);
                    } else if ("disable".equals(nextArg2)) {
                        this.mInterface.setCallComposerStatus(iIntValue, 0);
                    } else if ("query".equals(nextArg2)) {
                        getOutPrintWriter().println(this.mInterface.getCallComposerStatus(iIntValue) == 1);
                    } else {
                        onHelpCallComposer();
                        return 1;
                    }
                    return 0;
                } catch (RemoteException e) {
                    e.printStackTrace(getOutPrintWriter());
                    return 1;
                }
            case "test-mode":
                String nextArg3 = getNextArg();
                if ("enable".equals(nextArg3)) {
                    CallComposerPictureManager.sTestMode = true;
                } else if ("disable".equals(nextArg3)) {
                    CallComposerPictureManager.sTestMode = false;
                } else if ("query".equals(nextArg3)) {
                    getOutPrintWriter().println(CallComposerPictureManager.sTestMode);
                } else {
                    onHelpCallComposer();
                    return 1;
                }
                return 0;
            case "simulate-outgoing-call":
                final int iIntValue2 = Integer.valueOf(getNextArg()).intValue();
                final UUID uuidFromString = UUID.fromString(getNextArg());
                final CompletableFuture completableFuture = new CompletableFuture();
                Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.phone.TelephonyShellCommand$$ExternalSyntheticLambda1
                    public final void runOrThrow() {
                        this.f$0.lambda$handleCallComposerCommand$1(iIntValue2, uuidFromString, completableFuture);
                    }
                });
                try {
                    getOutPrintWriter().println(String.valueOf((Uri) completableFuture.get()));
                    return 0;
                } catch (Exception e2) {
                    Reflects$$ExternalSyntheticBUOutline0.m(e2);
                    return 0;
                }
            default:
                return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleCallComposerCommand$1(int i, UUID uuid, final CompletableFuture completableFuture) {
        CallComposerPictureManager callComposerPictureManager = CallComposerPictureManager.getInstance(this.mContext, i);
        Objects.requireNonNull(completableFuture);
        callComposerPictureManager.storeUploadedPictureToCallLog(uuid, new Consumer() { // from class: com.android.phone.TelephonyShellCommand$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                completableFuture.complete((Uri) obj);
            }
        });
    }

    private int handleHasCarrierPrivilegesCommand() {
        String nextArgRequired = getNextArgRequired();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                boolean z = this.mInterface.checkCarrierPrivilegesForPackageAnyPhone(nextArgRequired) == 1;
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                getOutPrintWriter().println(z);
                return 0;
            } catch (RemoteException e) {
                Log.w("TelephonyShellCommand", "has-carrier-privileges exception", e);
                getErrPrintWriter().println("Exception: " + e.getMessage());
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return -1;
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    private int handleAllowedNetworkTypesCommand(String str) {
        if (!checkShellUid()) {
            return -1;
        }
        PrintWriter errPrintWriter = getErrPrintWriter();
        String str2 = str + ": ";
        Log.v("TelephonyShellCommand", str + " start");
        int iSlotStringToSubId = -1;
        do {
            String nextOption = getNextOption();
            if (nextOption != null) {
                if (nextOption.equals("-s")) {
                    try {
                        iSlotStringToSubId = slotStringToSubId(str2, getNextArgRequired());
                    } catch (IllegalArgumentException unused) {
                        errPrintWriter.println(str2.concat("SLOT_ID expected after -s."));
                        return -1;
                    }
                } else {
                    errPrintWriter.println(str2 + "Unknown option " + nextOption);
                    return -1;
                }
            } else {
                if ("get-allowed-network-types-for-users".equals(str)) {
                    return handleGetAllowedNetworkTypesCommand(iSlotStringToSubId);
                }
                if ("set-allowed-network-types-for-users".equals(str)) {
                    return handleSetAllowedNetworkTypesCommand(iSlotStringToSubId);
                }
                return -1;
            }
        } while (SubscriptionManager.isValidSubscriptionId(iSlotStringToSubId));
        errPrintWriter.println(str2 + "No valid subscription found.");
        return -1;
    }

    private int handleGetAllowedNetworkTypesCommand(int i) {
        PrintWriter errPrintWriter = getErrPrintWriter();
        try {
            ITelephony iTelephony = this.mInterface;
            if (iTelephony != null) {
                getOutPrintWriter().println(TelephonyManager.convertNetworkTypeBitmaskToString(iTelephony.getAllowedNetworkTypesForReason(i, 0)));
                return 0;
            }
            throw new IllegalStateException("telephony service is null.");
        } catch (RemoteException e) {
            Log.e("ShellCommand", "getAllowedNetworkTypesForReason RemoteException" + e);
            errPrintWriter.println("get-allowed-network-types-for-usersRemoteException " + e);
            return -1;
        }
    }

    private int handleSetAllowedNetworkTypesCommand(int i) {
        String str;
        PrintWriter errPrintWriter = getErrPrintWriter();
        String nextArg = getNextArg();
        if (TextUtils.isEmpty(nextArg)) {
            errPrintWriter.println("set-allowed-network-types-for-users No NETWORK_TYPES_BITMASK");
            return -1;
        }
        long jConvertNetworkTypeBitmaskFromStringToLong = convertNetworkTypeBitmaskFromStringToLong(nextArg);
        if (jConvertNetworkTypeBitmaskFromStringToLong < 0) {
            errPrintWriter.println("set-allowed-network-types-for-users No valid NETWORK_TYPES_BITMASK");
            return -1;
        }
        try {
            ITelephony iTelephony = this.mInterface;
            if (iTelephony != null) {
                if (!iTelephony.setAllowedNetworkTypesForReason(i, 0, jConvertNetworkTypeBitmaskFromStringToLong, this.mContext.getOpPackageName())) {
                    str = "set-allowed-network-types-for-users failed";
                } else {
                    str = "set-allowed-network-types-for-users completed";
                }
                getOutPrintWriter().println(str);
                return 0;
            }
            throw new IllegalStateException("telephony service is null.");
        } catch (RemoteException e) {
            Log.e("ShellCommand", "setAllowedNetworkTypesForReason RemoteException" + e);
            errPrintWriter.println("set-allowed-network-types-for-users RemoteException " + e);
            return -1;
        }
    }

    private long convertNetworkTypeBitmaskFromStringToLong(String str) {
        if (TextUtils.isEmpty(str)) {
            return -1L;
        }
        Log.v("TelephonyShellCommand", "AllowedNetworkTypes:" + str + ", length: " + str.length());
        try {
            return Long.parseLong(str, 2);
        } catch (NumberFormatException e) {
            Log.e("TelephonyShellCommand", "AllowedNetworkTypes: " + e);
            return -1L;
        }
    }

    private int handleRadioSetModemServiceCommand() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        String nextArgRequired = null;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                nextOption.hashCode();
                if (nextOption.equals("-s")) {
                    nextArgRequired = getNextArgRequired();
                }
            } else {
                try {
                    boolean modemService = this.mInterface.setModemService(nextArgRequired);
                    Log.v("TelephonyShellCommand", "RadioSetModemService " + nextArgRequired + ", result = " + modemService);
                    getOutPrintWriter().println(modemService);
                    return 0;
                } catch (RemoteException e) {
                    Log.w("TelephonyShellCommand", "RadioSetModemService: " + nextArgRequired + ", error = " + e.getMessage());
                    StringBuilder sb = new StringBuilder("Exception: ");
                    sb.append(e.getMessage());
                    errPrintWriter.println(sb.toString());
                    return -1;
                }
            }
        }
    }

    private int handleRadioGetModemServiceCommand() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        try {
            String modemService = this.mInterface.getModemService();
            getOutPrintWriter().println(modemService);
            Log.v("TelephonyShellCommand", "RadioGetModemService, result = " + modemService);
            return 0;
        } catch (RemoteException e) {
            errPrintWriter.println("Exception: " + e.getMessage());
            return -1;
        }
    }

    private int handleRadioCommand() {
        String nextArg = getNextArg();
        if (nextArg == null) {
            onHelpRadio();
            return 0;
        }
        nextArg.hashCode();
        if (nextArg.equals("set-modem-service")) {
            return handleRadioSetModemServiceCommand();
        }
        if (nextArg.equals("get-modem-service")) {
            return handleRadioGetModemServiceCommand();
        }
        return -1;
    }

    private int handleSetSatelliteServicePackageNameCommand() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        String nextArgRequired = null;
        String nextArgRequired2 = null;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                nextOption.hashCode();
                if (nextOption.equals("-p")) {
                    nextArgRequired2 = getNextArgRequired();
                } else if (nextOption.equals("-s")) {
                    nextArgRequired = getNextArgRequired();
                }
            } else {
                Log.d("TelephonyShellCommand", "handleSetSatelliteServicePackageNameCommand: serviceName=" + nextArgRequired + ", provisioned=" + nextArgRequired2);
                try {
                    boolean satelliteServicePackageName = this.mInterface.setSatelliteServicePackageName(nextArgRequired, nextArgRequired2);
                    Log.v("TelephonyShellCommand", "SetSatelliteServicePackageName " + nextArgRequired + ", provisioned=" + nextArgRequired2 + ", result = " + satelliteServicePackageName);
                    getOutPrintWriter().println(satelliteServicePackageName);
                    return 0;
                } catch (RemoteException e) {
                    Log.w("TelephonyShellCommand", "SetSatelliteServicePackageName: " + nextArgRequired + ", provisioned=" + nextArgRequired2 + ", error = " + e.getMessage());
                    StringBuilder sb = new StringBuilder("Exception: ");
                    sb.append(e.getMessage());
                    errPrintWriter.println(sb.toString());
                    return -1;
                }
            }
        }
    }

    private int handleSetSatelliteAccessAllowedForSubscriptions() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        String nextArgRequired = null;
        boolean z = false;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                nextOption.hashCode();
                if (nextOption.equals("-r")) {
                    z = true;
                } else if (nextOption.equals("-s")) {
                    nextArgRequired = getNextArgRequired();
                }
            } else {
                Log.d("TelephonyShellCommand", "handleSetSatelliteAccessAllowedForSubscriptions: subIdListStr=" + nextArgRequired + ", reset=" + z);
                try {
                    boolean satelliteAccessAllowedForSubscriptions = this.mInterface.setSatelliteAccessAllowedForSubscriptions(z, nextArgRequired);
                    Log.v("TelephonyShellCommand", "SetSatelliteAccessAllowedForSubscriptions " + nextArgRequired + ", result = " + satelliteAccessAllowedForSubscriptions);
                    getOutPrintWriter().println(satelliteAccessAllowedForSubscriptions);
                    return 0;
                } catch (RemoteException e) {
                    Log.w("TelephonyShellCommand", "SetSatelliteAccessAllowedForSubscriptions: error = " + e.getMessage());
                    errPrintWriter.println("Exception: " + e.getMessage());
                    return -1;
                }
            }
        }
    }

    private int handleSetSatelliteGatewayServicePackageNameCommand() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        String nextArgRequired = null;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                nextOption.hashCode();
                if (nextOption.equals("-s")) {
                    nextArgRequired = getNextArgRequired();
                }
            } else {
                Log.d("TelephonyShellCommand", "handleSetSatelliteGatewayServicePackageNameCommand: serviceName=" + nextArgRequired);
                try {
                    boolean satelliteGatewayServicePackageName = this.mInterface.setSatelliteGatewayServicePackageName(nextArgRequired);
                    Log.v("TelephonyShellCommand", "setSatelliteGatewayServicePackageName " + nextArgRequired + ", result = " + satelliteGatewayServicePackageName);
                    getOutPrintWriter().println(satelliteGatewayServicePackageName);
                    return 0;
                } catch (RemoteException e) {
                    Log.w("TelephonyShellCommand", "setSatelliteGatewayServicePackageName: " + nextArgRequired + ", error = " + e.getMessage());
                    StringBuilder sb = new StringBuilder("Exception: ");
                    sb.append(e.getMessage());
                    errPrintWriter.println(sb.toString());
                    return -1;
                }
            }
        }
    }

    private int handleSetSatellitePointingUiClassNameCommand() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        String nextArgRequired = null;
        String nextArgRequired2 = null;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                nextOption.hashCode();
                if (nextOption.equals("-c")) {
                    nextArgRequired2 = getNextArgRequired();
                } else if (nextOption.equals("-p")) {
                    nextArgRequired = getNextArgRequired();
                }
            } else {
                Log.d("TelephonyShellCommand", "handleSetSatellitePointingUiClassNameCommand: packageName=" + nextArgRequired + ", className=" + nextArgRequired2);
                try {
                    boolean satellitePointingUiClassName = this.mInterface.setSatellitePointingUiClassName(nextArgRequired, nextArgRequired2);
                    Log.v("TelephonyShellCommand", "setSatellitePointingUiClassName result =" + satellitePointingUiClassName);
                    getOutPrintWriter().println(satellitePointingUiClassName);
                    return 0;
                } catch (RemoteException e) {
                    Log.e("TelephonyShellCommand", "setSatellitePointingUiClassName: " + nextArgRequired + ", error = " + e.getMessage());
                    StringBuilder sb = new StringBuilder("Exception: ");
                    sb.append(e.getMessage());
                    errPrintWriter.println(sb.toString());
                    return -1;
                }
            }
        }
    }

    private int handleSetEmergencyCallToSatelliteHandoverType() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        int i = -1;
        int i2 = 0;
        int i3 = 0;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                nextOption.hashCode();
                switch (nextOption) {
                    case "-d":
                        try {
                            i2 = Integer.parseInt(getNextArgRequired());
                            break;
                        } catch (NumberFormatException unused) {
                            errPrintWriter.println("SetEmergencyCallToSatelliteHandoverType: require an integer for delaySeconds");
                            return -1;
                        }
                        break;
                    case "-s":
                        try {
                            i3 = Integer.parseInt(getNextArgRequired());
                            break;
                        } catch (NumberFormatException unused2) {
                            errPrintWriter.println("SetEmergencyCallToSatelliteHandoverType: require an integer for simSlotIndex");
                            return -1;
                        }
                        break;
                    case "-t":
                        try {
                            i = Integer.parseInt(getNextArgRequired());
                            break;
                        } catch (NumberFormatException unused3) {
                            errPrintWriter.println("SetEmergencyCallToSatelliteHandoverType: require an integer for handoverType");
                            return -1;
                        }
                        break;
                }
            } else {
                Log.d("TelephonyShellCommand", "handleSetEmergencyCallToSatelliteHandoverType: handoverType=" + i + ", delaySeconds=" + i2 + ", simSlotIndex=" + i3);
                try {
                    boolean emergencyCallToSatelliteHandoverType = this.mInterface.setEmergencyCallToSatelliteHandoverType(i, i2, i3);
                    Log.v("TelephonyShellCommand", "setEmergencyCallToSatelliteHandoverType result =" + emergencyCallToSatelliteHandoverType);
                    getOutPrintWriter().println(emergencyCallToSatelliteHandoverType);
                    return 0;
                } catch (RemoteException e) {
                    Log.e("TelephonyShellCommand", "setEmergencyCallToSatelliteHandoverType: " + i + ", error = " + e.getMessage());
                    StringBuilder sb = new StringBuilder("Exception: ");
                    sb.append(e.getMessage());
                    errPrintWriter.println(sb.toString());
                    return -1;
                }
            }
        }
    }

    private int handleSetSatelliteListeningTimeoutDuration() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        long j = 0;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                nextOption.hashCode();
                if (nextOption.equals("-t")) {
                    j = Long.parseLong(getNextArgRequired());
                }
            } else {
                Log.d("TelephonyShellCommand", "handleSetSatelliteListeningTimeoutDuration: timeoutMillis=" + j);
                try {
                    boolean satelliteListeningTimeoutDuration = this.mInterface.setSatelliteListeningTimeoutDuration(j);
                    Log.v("TelephonyShellCommand", "setSatelliteListeningTimeoutDuration " + j + ", result = " + satelliteListeningTimeoutDuration);
                    getOutPrintWriter().println(satelliteListeningTimeoutDuration);
                    return 0;
                } catch (RemoteException e) {
                    Log.w("TelephonyShellCommand", "setSatelliteListeningTimeoutDuration: " + j + ", error = " + e.getMessage());
                    StringBuilder sb = new StringBuilder("Exception: ");
                    sb.append(e.getMessage());
                    errPrintWriter.println(sb.toString());
                    return -1;
                }
            }
        }
    }

    private int handleSetSatelliteIgnoreCellularServiceState() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        boolean z = false;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                nextOption.hashCode();
                if (nextOption.equals("-d")) {
                    z = Boolean.parseBoolean(getNextArgRequired());
                }
            } else {
                Log.d("TelephonyShellCommand", "handleSetSatelliteIgnoreCellularServiceState: enabled =" + z);
                try {
                    boolean satelliteIgnoreCellularServiceState = this.mInterface.setSatelliteIgnoreCellularServiceState(z);
                    Log.v("TelephonyShellCommand", "handleSetSatelliteIgnoreCellularServiceState " + z + ", result = " + satelliteIgnoreCellularServiceState);
                    getOutPrintWriter().println(satelliteIgnoreCellularServiceState);
                    return 0;
                } catch (RemoteException e) {
                    Log.w("TelephonyShellCommand", "handleSetSatelliteIgnoreCellularServiceState: " + z + ", error = " + e.getMessage());
                    StringBuilder sb = new StringBuilder("Exception: ");
                    sb.append(e.getMessage());
                    errPrintWriter.println(sb.toString());
                    return -1;
                }
            }
        }
    }

    private int handleSetSupportDisableSatelliteWhileEnableInProgress() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        boolean z = false;
        boolean z2 = false;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                nextOption.hashCode();
                if (nextOption.equals("-r")) {
                    z = true;
                } else if (nextOption.equals("-s")) {
                    z2 = Boolean.parseBoolean(getNextArgRequired());
                }
            } else {
                Log.d("TelephonyShellCommand", "handleSetSupportDisableSatelliteWhileEnableInProgress: reset=" + z + ", supported=" + z2);
                try {
                    boolean supportDisableSatelliteWhileEnableInProgress = this.mInterface.setSupportDisableSatelliteWhileEnableInProgress(z, z2);
                    Log.v("TelephonyShellCommand", "handleSetSupportDisableSatelliteWhileEnableInProgress: result = " + supportDisableSatelliteWhileEnableInProgress);
                    getOutPrintWriter().println(supportDisableSatelliteWhileEnableInProgress);
                    return 0;
                } catch (RemoteException e) {
                    Log.w("TelephonyShellCommand", "handleSetSupportDisableSatelliteWhileEnableInProgress: error = " + e.getMessage());
                    errPrintWriter.println("Exception: " + e.getMessage());
                    return -1;
                }
            }
        }
    }

    private int handleSetSatelliteTnScanningSupport() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                nextOption.hashCode();
                switch (nextOption) {
                    case "-a":
                        z3 = Boolean.parseBoolean(getNextArgRequired());
                        break;
                    case "-r":
                        z = true;
                        break;
                    case "-s":
                        z2 = Boolean.parseBoolean(getNextArgRequired());
                        break;
                }
            } else {
                Log.d("TelephonyShellCommand", "handleSetSatelliteTnScanningSupport: reset=" + z + ", concurrentTnScanningSupported =" + z2 + ", tnScanningDuringSatelliteSessionAllowed=" + z3);
                try {
                    boolean tnScanningSupport = this.mInterface.setTnScanningSupport(z, z2, z3);
                    Log.v("TelephonyShellCommand", "handleSetSatelliteTnScanningSupport: result = " + tnScanningSupport);
                    getOutPrintWriter().println(tnScanningSupport);
                    return 0;
                } catch (RemoteException e) {
                    Log.w("TelephonyShellCommand", "handleSetSatelliteTnScanningSupport: error = " + e.getMessage());
                    errPrintWriter.println("Exception: " + e.getMessage());
                    return -1;
                }
            }
        }
    }

    private int handleSetCtsMode() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        boolean z = false;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption == null) {
                break;
            }
            nextOption.hashCode();
            if (nextOption.equals("-e")) {
                z = true;
            }
        }
        Log.d("TelephonyShellCommand", "handleSetCtsMode: ctsMode=" + z);
        try {
            boolean ctsMode = this.mInterface.setCtsMode(z);
            Log.v("TelephonyShellCommand", "handleSetCtsMode: result = " + ctsMode);
            getOutPrintWriter().println(ctsMode);
            return ctsMode ? 0 : -1;
        } catch (RemoteException e) {
            Log.w("TelephonyShellCommand", "handleSetCtsMode: error = " + e.getMessage());
            errPrintWriter.println("Exception: " + e.getMessage());
            return -1;
        }
    }

    private int handleSetDatagramControllerTimeoutDuration() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        long j = 0;
        boolean z = false;
        int i = 0;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                nextOption.hashCode();
                switch (nextOption) {
                    case "-d":
                        j = Long.parseLong(getNextArgRequired());
                        break;
                    case "-r":
                        z = true;
                        break;
                    case "-t":
                        i = Integer.parseInt(getNextArgRequired());
                        break;
                }
            } else {
                Log.d("TelephonyShellCommand", "setDatagramControllerTimeoutDuration: timeoutMillis=" + j + ", reset=" + z + ", timeoutType=" + i);
                try {
                    boolean datagramControllerTimeoutDuration = this.mInterface.setDatagramControllerTimeoutDuration(z, i, j);
                    Log.v("TelephonyShellCommand", "setDatagramControllerTimeoutDuration " + j + ", result = " + datagramControllerTimeoutDuration);
                    getOutPrintWriter().println(datagramControllerTimeoutDuration);
                    return 0;
                } catch (RemoteException e) {
                    Log.w("TelephonyShellCommand", "setDatagramControllerTimeoutDuration: " + j + ", error = " + e.getMessage());
                    StringBuilder sb = new StringBuilder("Exception: ");
                    sb.append(e.getMessage());
                    errPrintWriter.println(sb.toString());
                    return -1;
                }
            }
        }
    }

    private int handleSetDatagramControllerBooleanConfig() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        boolean z = false;
        boolean z2 = false;
        int i = 0;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                nextOption.hashCode();
                switch (nextOption) {
                    case "-d":
                        z = Boolean.parseBoolean(getNextArgRequired());
                        break;
                    case "-r":
                        z2 = true;
                        break;
                    case "-t":
                        i = Integer.parseInt(getNextArgRequired());
                        break;
                }
            } else {
                Log.d("TelephonyShellCommand", "setDatagramControllerBooleanConfig: enable=" + z + ", reset=" + z2 + ", booleanType=" + i);
                try {
                    boolean datagramControllerBooleanConfig = this.mInterface.setDatagramControllerBooleanConfig(z2, i, z);
                    Log.v("TelephonyShellCommand", "setDatagramControllerBooleanConfig result = " + datagramControllerBooleanConfig);
                    getOutPrintWriter().println(datagramControllerBooleanConfig);
                    return 0;
                } catch (RemoteException e) {
                    Log.w("TelephonyShellCommand", "setDatagramControllerBooleanConfig: error = " + e.getMessage());
                    errPrintWriter.println("Exception: " + e.getMessage());
                    return -1;
                }
            }
        }
    }

    private int handleSetSatelliteControllerTimeoutDuration() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        long j = 0;
        boolean z = false;
        int i = 0;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                nextOption.hashCode();
                switch (nextOption) {
                    case "-d":
                        j = Long.parseLong(getNextArgRequired());
                        break;
                    case "-r":
                        z = true;
                        break;
                    case "-t":
                        i = Integer.parseInt(getNextArgRequired());
                        break;
                }
            } else {
                Log.d("TelephonyShellCommand", "setSatelliteControllerTimeoutDuration: timeoutMillis=" + j + ", reset=" + z + ", timeoutType=" + i);
                try {
                    boolean satelliteControllerTimeoutDuration = this.mInterface.setSatelliteControllerTimeoutDuration(z, i, j);
                    Log.v("TelephonyShellCommand", "setSatelliteControllerTimeoutDuration " + j + ", result = " + satelliteControllerTimeoutDuration);
                    getOutPrintWriter().println(satelliteControllerTimeoutDuration);
                    return 0;
                } catch (RemoteException e) {
                    Log.w("TelephonyShellCommand", "setSatelliteControllerTimeoutDuration: " + j + ", error = " + e.getMessage());
                    StringBuilder sb = new StringBuilder("Exception: ");
                    sb.append(e.getMessage());
                    errPrintWriter.println(sb.toString());
                    return -1;
                }
            }
        }
    }

    private int handleSetShouldSendDatagramToModemInDemoMode() {
        boolean z;
        PrintWriter errPrintWriter = getErrPrintWriter();
        String nextArg = getNextArg();
        if (nextArg == null) {
            errPrintWriter.println("adb shell cmd phone set-should-send-datagram-to-modem-in-demo-mode : Invalid Argument");
            return -1;
        }
        nextArg.hashCode();
        if (nextArg.equals("true")) {
            z = true;
        } else {
            if (!nextArg.equals("false")) {
                errPrintWriter.println("adb shell cmd phone set-should-send-datagram-to-modem-in-demo-mode : Invalid Argument");
                return -1;
            }
            z = false;
        }
        Log.d("TelephonyShellCommand", "handleSetShouldSendDatagramToModemInDemoMode(" + z + ")");
        try {
            boolean shouldSendDatagramToModemInDemoMode = this.mInterface.setShouldSendDatagramToModemInDemoMode(z);
            Log.v("TelephonyShellCommand", "handleSetShouldSendDatagramToModemInDemoMode returns: " + shouldSendDatagramToModemInDemoMode);
            getOutPrintWriter().println(shouldSendDatagramToModemInDemoMode);
            return 0;
        } catch (RemoteException e) {
            Log.w("TelephonyShellCommand", "setShouldSendDatagramToModemInDemoMode(" + z + "), error = " + e.getMessage());
            StringBuilder sb = new StringBuilder("Exception: ");
            sb.append(e.getMessage());
            errPrintWriter.println(sb.toString());
            return -1;
        }
    }

    private int handleSetSatelliteAccessControlOverlayConfigs() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        boolean z = false;
        boolean z2 = false;
        String nextArgRequired = null;
        List listAsList = null;
        String nextArgRequired2 = null;
        long j = 0;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                Log.d("TelephonyShellCommand", "handleSetSatelliteAccessControlOverlayConfigs: opt=".concat(nextOption));
                nextOption.hashCode();
                switch (nextOption) {
                    case "-a":
                        z2 = true;
                        break;
                    case "-c":
                        listAsList = Arrays.asList(getNextArgRequired().split(","));
                        break;
                    case "-d":
                        j = Long.parseLong(getNextArgRequired());
                        break;
                    case "-f":
                        nextArgRequired = getNextArgRequired();
                        break;
                    case "-g":
                        nextArgRequired2 = getNextArgRequired();
                        break;
                    case "-r":
                        z = true;
                        break;
                }
            } else {
                Log.d("TelephonyShellCommand", "handleSetSatelliteAccessControlOverlayConfigs: reset=" + z + ", isAllowed=" + z2 + ", s2CellFile=" + nextArgRequired + ", locationFreshDurationNanos=" + j + ", satelliteCountryCodes=" + listAsList + ", satelliteAccessConfigurationFile=" + nextArgRequired2);
                try {
                    boolean satelliteAccessControlOverlayConfigs = this.mInterface.setSatelliteAccessControlOverlayConfigs(z, z2, nextArgRequired, j, listAsList, nextArgRequired2);
                    Log.v("TelephonyShellCommand", "setSatelliteAccessControlOverlayConfigs result =" + satelliteAccessControlOverlayConfigs);
                    getOutPrintWriter().println(satelliteAccessControlOverlayConfigs);
                    return 0;
                } catch (RemoteException e) {
                    Log.e("TelephonyShellCommand", "setSatelliteAccessControlOverlayConfigs: ex=" + e.getMessage());
                    errPrintWriter.println("Exception: " + e.getMessage());
                    return -1;
                }
            }
        }
    }

    private int handleOverrideSatelliteEntitlementStatusResponseForCtsTest() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        String nextArgRequired = null;
        boolean z = false;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                Log.d("TelephonyShellCommand", "handleOverrideSatelliteEntitlementStatusResponseForCtsTest: opt=".concat(nextOption));
                nextOption.hashCode();
                if (nextOption.equals("-r")) {
                    nextArgRequired = getNextArgRequired();
                } else if (nextOption.equals("-t")) {
                    z = true;
                }
            } else {
                Log.d("TelephonyShellCommand", "handleOverrideSatelliteEntitlementStatusResponseForCtsTest(" + nextArgRequired + ", throwException=" + z + ")");
                try {
                    boolean zOverrideSatelliteEntilementStatusResponseForCtsTest = this.mInterface.overrideSatelliteEntilementStatusResponseForCtsTest(nextArgRequired, z);
                    Log.v("TelephonyShellCommand", "handleOverrideSatelliteEntitlementStatusResponseForCtsTest returns: " + zOverrideSatelliteEntilementStatusResponseForCtsTest);
                    getOutPrintWriter().println(zOverrideSatelliteEntilementStatusResponseForCtsTest);
                    return 0;
                } catch (RemoteException e) {
                    Log.w("TelephonyShellCommand", "handleOverrideSatelliteEntitlementStatusResponseForCtsTest(" + nextArgRequired + "), error = " + e.getMessage());
                    StringBuilder sb = new StringBuilder("Exception: ");
                    sb.append(e.getMessage());
                    errPrintWriter.println(sb.toString());
                    return -1;
                }
            }
        }
    }

    private int handleOverrideSatelliteEntilementQueryConditions() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        boolean z = false;
        boolean z2 = false;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                Log.d("TelephonyShellCommand", "handleOverrideSatelliteEntilementQueryConditions: opt=".concat(nextOption));
                nextOption.hashCode();
                if (nextOption.equals("-i")) {
                    z = true;
                } else if (nextOption.equals("-r")) {
                    z2 = true;
                }
            } else {
                Log.d("TelephonyShellCommand", "handleOverrideSatelliteEntilementQueryConditions   (" + z + ", entilementRefreshDays=" + z2 + ")");
                try {
                    boolean zOverrideSatelliteEntilementQueryConditions = this.mInterface.overrideSatelliteEntilementQueryConditions(z, z2);
                    Log.v("TelephonyShellCommand", "handleOverrideSatelliteEntilementQueryConditions returns: " + zOverrideSatelliteEntilementQueryConditions);
                    getOutPrintWriter().println(zOverrideSatelliteEntilementQueryConditions);
                    return 0;
                } catch (RemoteException e) {
                    Log.w("TelephonyShellCommand", "handleOverrideSatelliteEntilementQueryConditions(" + z + ", entilementRefreshDays=" + z2 + "), error = " + e.getMessage());
                    StringBuilder sb = new StringBuilder("Exception: ");
                    sb.append(e.getMessage());
                    errPrintWriter.println(sb.toString());
                    return -1;
                }
            }
        }
    }

    private int handleSetMaxAllowedSatelliteDataModeForCtsTest() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        int i = -1;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                Log.d("TelephonyShellCommand", "handleSetMaxAllowedSatelliteDataModeForCtsTest: opt=".concat(nextOption));
                nextOption.hashCode();
                if (nextOption.equals("-m")) {
                    i = Integer.parseInt(getNextArgRequired());
                }
            } else {
                Log.d("TelephonyShellCommand", "handleSetMaxAllowedSatelliteDataModeForCtsTest(" + i + ")");
                try {
                    boolean maxAllowedSatelliteDataModeForCtsTest = this.mInterface.setMaxAllowedSatelliteDataModeForCtsTest(i);
                    Log.v("TelephonyShellCommand", "handleSetMaxAllowedSatelliteDataModeForCtsTest returns: " + maxAllowedSatelliteDataModeForCtsTest);
                    getOutPrintWriter().println(maxAllowedSatelliteDataModeForCtsTest);
                    return 0;
                } catch (RemoteException e) {
                    Log.w("TelephonyShellCommand", "handleSetMaxAllowedSatelliteDataModeForCtsTest(" + i + "), error = " + e.getMessage());
                    StringBuilder sb = new StringBuilder("Exception: ");
                    sb.append(e.getMessage());
                    errPrintWriter.println(sb.toString());
                    return -1;
                }
            }
        }
    }

    private int handleUncapMaxAllowedSatelliteDataMode() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        Log.d("TelephonyShellCommand", "handleUncapMaxAllowedSatelliteDataMode");
        if (!UserHandle.isSameApp(Binder.getCallingUid(), 0) || TelephonyUtils.IS_USER) {
            getErrPrintWriter().println("Permission denied.");
            return -1;
        }
        try {
            boolean zUncapMaxAllowedSatelliteDataMode = this.mInterface.uncapMaxAllowedSatelliteDataMode();
            Log.v("TelephonyShellCommand", "handleUncapMaxAllowedSatelliteDataMode returns: " + zUncapMaxAllowedSatelliteDataMode);
            getOutPrintWriter().println("Result: " + zUncapMaxAllowedSatelliteDataMode);
            return 0;
        } catch (RemoteException e) {
            Log.w("TelephonyShellCommand", "handleUncapMaxAllowedSatelliteDataMode, error = " + e.getMessage());
            errPrintWriter.println("Exception: " + e.getMessage());
            return -1;
        }
    }

    private int handleSetCountryCodes() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        List arrayList = new ArrayList();
        Map map = new HashMap();
        String nextArgRequired = null;
        long j = 0;
        boolean z = false;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                nextOption.hashCode();
                switch (nextOption) {
                    case "-c":
                        map = parseStringLongMap(getNextArgRequired());
                        break;
                    case "-l":
                        nextArgRequired = getNextArgRequired();
                        break;
                    case "-n":
                        arrayList = Arrays.asList(getNextArgRequired().split(","));
                        break;
                    case "-r":
                        z = true;
                        break;
                    case "-t":
                        j = Long.parseLong(getNextArgRequired());
                        break;
                }
            } else {
                Log.d("TelephonyShellCommand", "setCountryCodes: locationCountryCode=" + nextArgRequired + ", locationCountryCodeTimestampNanos=" + j + ", currentNetworkCountryCodes=" + arrayList);
                try {
                    boolean countryCodes = this.mInterface.setCountryCodes(z, arrayList, map, nextArgRequired, j);
                    Log.v("TelephonyShellCommand", "setCountryCodes result =" + countryCodes);
                    getOutPrintWriter().println(countryCodes);
                    return 0;
                } catch (RemoteException e) {
                    Log.e("TelephonyShellCommand", "setCountryCodes: ex=" + e.getMessage());
                    errPrintWriter.println("Exception: " + e.getMessage());
                    return -1;
                }
            }
        }
    }

    private int handleOverrideConfigDataVersion() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        boolean z = false;
        int i = 0;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                nextOption.hashCode();
                if (nextOption.equals("-r")) {
                    z = true;
                } else if (nextOption.equals("-v")) {
                    i = Integer.parseInt(getNextArgRequired());
                }
            } else {
                Log.d("TelephonyShellCommand", "overrideConfigDataVersion: reset=" + z + ", version=" + i);
                try {
                    boolean zOverrideConfigDataVersion = this.mInterface.overrideConfigDataVersion(z, i);
                    Log.v("TelephonyShellCommand", "overrideConfigDataVersion result =" + zOverrideConfigDataVersion);
                    getOutPrintWriter().println(zOverrideConfigDataVersion);
                    return 0;
                } catch (RemoteException e) {
                    Log.e("TelephonyShellCommand", "overrideConfigDataVersion: ex=" + e.getMessage());
                    errPrintWriter.println("Exception: " + e.getMessage());
                    return -1;
                }
            }
        }
    }

    private int handleSetOemEnabledSatelliteProvisionStatus() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        boolean z = true;
        boolean z2 = false;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                nextOption.hashCode();
                if (nextOption.equals("-p")) {
                    try {
                        z2 = Boolean.parseBoolean(getNextArgRequired());
                        z = false;
                    } catch (Exception unused) {
                        errPrintWriter.println("setOemEnabledSatelliteProvisionStatus requires a boolean after -p indicating provision status");
                        return -1;
                    }
                }
            } else {
                Log.d("TelephonyShellCommand", "setOemEnabledSatelliteProvisionStatus: reset=" + z + ", isProvisioned=" + z2);
                try {
                    boolean oemEnabledSatelliteProvisionStatus = this.mInterface.setOemEnabledSatelliteProvisionStatus(z, z2);
                    Log.v("TelephonyShellCommand", "setOemEnabledSatelliteProvisionStatus result = " + oemEnabledSatelliteProvisionStatus);
                    getOutPrintWriter().println(oemEnabledSatelliteProvisionStatus);
                    return 0;
                } catch (RemoteException e) {
                    Log.w("TelephonyShellCommand", "setOemEnabledSatelliteProvisionStatus: error = " + e.getMessage());
                    errPrintWriter.println("Exception: " + e.getMessage());
                    return -1;
                }
            }
        }
    }

    private int handleSetIsSatelliteCommunicationAllowedForCurrentLocationCache() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        String nextArg = getNextArg();
        if (nextArg == null) {
            errPrintWriter.println("adb shell cmd phone set-is-satellite-communication-allowed-for-current-location-cache : Invalid Argument");
            return -1;
        }
        Log.d("TelephonyShellCommand", "handleSetIsSatelliteCommunicationAllowedForCurrentLocationCache(" + nextArg + ")");
        try {
            boolean isSatelliteCommunicationAllowedForCurrentLocationCache = this.mInterface.setIsSatelliteCommunicationAllowedForCurrentLocationCache(nextArg);
            Log.v("TelephonyShellCommand", "setIsSatelliteCommunicationAllowedForCurrentLocationCache returns: " + isSatelliteCommunicationAllowedForCurrentLocationCache);
            getOutPrintWriter().println(isSatelliteCommunicationAllowedForCurrentLocationCache);
            return 0;
        } catch (RemoteException e) {
            Log.w("TelephonyShellCommand", "setIsSatelliteCommunicationAllowedForCurrentLocationCache(" + nextArg + "), error = " + e.getMessage());
            StringBuilder sb = new StringBuilder("Exception: ");
            sb.append(e.getMessage());
            errPrintWriter.println(sb.toString());
            return -1;
        }
    }

    private int handleSetSatelliteSubscriberIdListChangedIntentComponent() {
        String str;
        PrintWriter errPrintWriter = getErrPrintWriter();
        String nextArg = getNextArg();
        if (nextArg == null) {
            errPrintWriter.println("adb shell cmd phone set-satellite-subscriberid-list-changed-intent-component: Invalid Argument");
            return -1;
        }
        nextArg.hashCode();
        switch (nextArg) {
            case "-c":
                String nextArg2 = getNextArg();
                if (nextArg2 == null) {
                    nextArg2 = "android.telephony.satellite.cts.SatelliteReceiver";
                }
                str = nextArg + "/" + nextArg2;
                break;
            case "-p":
                String nextArg3 = getNextArg();
                if (nextArg3 == null) {
                    nextArg3 = "android.telephony.satellite.cts";
                }
                str = nextArg + "/" + nextArg3;
                break;
            case "-r":
                str = "reset";
                break;
            default:
                errPrintWriter.println("adb shell cmd phone set-satellite-subscriberid-list-changed-intent-component: Invalid Argument");
                return -1;
        }
        Log.d("TelephonyShellCommand", "handleSetSatelliteSubscriberIdListChangedIntentComponent(" + str + ")");
        try {
            boolean satelliteSubscriberIdListChangedIntentComponent = this.mInterface.setSatelliteSubscriberIdListChangedIntentComponent(str);
            Log.v("TelephonyShellCommand", "handleSetSatelliteSubscriberIdListChangedIntentComponent returns: " + satelliteSubscriberIdListChangedIntentComponent);
            getOutPrintWriter().println(satelliteSubscriberIdListChangedIntentComponent);
            return 0;
        } catch (RemoteException e) {
            Log.w("TelephonyShellCommand", "handleSetSatelliteSubscriberIdListChangedIntentComponent(" + str + "), error = " + e.getMessage());
            StringBuilder sb = new StringBuilder("Exception: ");
            sb.append(e.getMessage());
            errPrintWriter.println(sb.toString());
            return -1;
        }
    }

    private int handleAddAttachRestrictionForCarrier(String str) {
        PrintWriter errPrintWriter = getErrPrintWriter();
        String str2 = str + ": ";
        int defaultSubscriptionId = SubscriptionManager.getDefaultSubscriptionId();
        int i = 0;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                nextOption.hashCode();
                if (nextOption.equals("-r")) {
                    try {
                        i = Integer.parseInt(getNextArgRequired());
                    } catch (NumberFormatException unused) {
                        errPrintWriter.println("handleAddAttachRestrictionForCarrier: require an integer for reason");
                        return -1;
                    }
                } else if (nextOption.equals("-s")) {
                    try {
                        defaultSubscriptionId = slotStringToSubId(str2, getNextArgRequired());
                    } catch (NumberFormatException unused2) {
                        errPrintWriter.println("handleAddAttachRestrictionForCarrier: require an integer for subId");
                        return -1;
                    }
                } else {
                    continue;
                }
            } else {
                Log.d("TelephonyShellCommand", "handleAddAttachRestrictionForCarrier: subId= " + defaultSubscriptionId + ", reason= " + i);
                try {
                    this.mInterface.addAttachRestrictionForCarrier(defaultSubscriptionId, i, new IIntegerConsumer.Stub() { // from class: com.android.phone.TelephonyShellCommand.1
                        public void accept(int i2) {
                            Log.v("TelephonyShellCommand", "addAttachRestrictionForCarrier result = " + i2);
                            TelephonyShellCommand.this.getOutPrintWriter().println(i2);
                        }
                    });
                    return 0;
                } catch (RemoteException e) {
                    Log.e("TelephonyShellCommand", "addAttachRestrictionForCarrier: error = " + e.getMessage());
                    errPrintWriter.println("Exception: " + e.getMessage());
                    return -1;
                }
            }
        }
    }

    private int handleRemoveAttachRestrictionForCarrier(String str) {
        PrintWriter errPrintWriter = getErrPrintWriter();
        String str2 = str + ": ";
        int defaultSubscriptionId = SubscriptionManager.getDefaultSubscriptionId();
        int i = 0;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                nextOption.hashCode();
                if (nextOption.equals("-r")) {
                    try {
                        i = Integer.parseInt(getNextArgRequired());
                    } catch (NumberFormatException unused) {
                        errPrintWriter.println("handleRemoveAttachRestrictionForCarrier: require an integer for reason");
                        return -1;
                    }
                } else if (nextOption.equals("-s")) {
                    try {
                        defaultSubscriptionId = slotStringToSubId(str2, getNextArgRequired());
                    } catch (NumberFormatException unused2) {
                        errPrintWriter.println("handleRemoveAttachRestrictionForCarrier: require an integer for subId");
                        return -1;
                    }
                } else {
                    continue;
                }
            } else {
                Log.d("TelephonyShellCommand", "handleRemoveAttachRestrictionForCarrier: subId= " + defaultSubscriptionId + ", reason= " + i);
                try {
                    this.mInterface.removeAttachRestrictionForCarrier(defaultSubscriptionId, i, new IIntegerConsumer.Stub() { // from class: com.android.phone.TelephonyShellCommand.2
                        public void accept(int i2) {
                            Log.v("TelephonyShellCommand", "removeAttachRestrictionForCarrier result = " + i2);
                            TelephonyShellCommand.this.getOutPrintWriter().println(i2);
                        }
                    });
                    return 0;
                } catch (RemoteException e) {
                    Log.e("TelephonyShellCommand", "removeAttachRestrictionForCarrier: error = " + e.getMessage());
                    errPrintWriter.println("Exception: " + e.getMessage());
                    return -1;
                }
            }
        }
    }

    private Map parseStringLongMap(String str) {
        HashMap map = new HashMap();
        if (!TextUtils.isEmpty(str)) {
            String[] strArrSplit = str.split(";");
            if (strArrSplit.length != 2) {
                Log.e("TelephonyShellCommand", "parseStringLongMap: invalid inputStr=".concat(str));
                return map;
            }
            String[] strArrSplit2 = strArrSplit[0].split(",");
            String[] strArrSplit3 = strArrSplit[1].split(",");
            if (strArrSplit2.length != strArrSplit3.length) {
                Log.e("TelephonyShellCommand", "parseStringLongMap: invalid inputStr=".concat(str));
                return map;
            }
            for (int i = 0; i < strArrSplit2.length; i++) {
                try {
                    map.put(strArrSplit2[i], Long.valueOf(Long.parseLong(strArrSplit3[i])));
                } catch (Exception e) {
                    Log.e("TelephonyShellCommand", "parseStringLongMap: invalid inputStr=" + str + ", ex=" + e);
                }
            }
        }
        return map;
    }

    private int handleCarrierRestrictionStatusCommand() {
        try {
            if (checkShellUid()) {
                if (!"android.telephony.mockmodem.MockModemService".equalsIgnoreCase(this.mInterface.getModemService())) {
                }
                String nextOption = getNextOption();
                CarrierAllowListInfo carrierAllowListInfoLoadInstance = CarrierAllowListInfo.loadInstance(this.mContext);
                if (TextUtils.isEmpty(nextOption)) {
                    carrierAllowListInfoLoadInstance.updateJsonForTest(null);
                    return 0;
                }
                if (nextOption.startsWith("--")) {
                    nextOption = nextOption.replace("--", "");
                }
                String[] strArrSplit = nextOption.split(",");
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < strArrSplit.length; i++) {
                    String strConvertToJsonString = convertToJsonString(i, strArrSplit[i]);
                    if (TextUtils.isEmpty(strConvertToJsonString)) {
                        Log.v("TelephonyShellCommand", "handleCarrierRestrictionStatusCommand, Shell command parsing error");
                        return -1;
                    }
                    stringBuffer.append(strConvertToJsonString);
                }
                return carrierAllowListInfoLoadInstance.updateJsonForTest(stringBuffer.toString());
            }
            Log.v("TelephonyShellCommand", "handleCarrierRestrictionStatusCommand, MockModem service check fails or  checkShellUid fails");
            return -1;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private int setCarrierServicePackageOverride() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        int defaultSubscriptionId = SubscriptionManager.getDefaultSubscriptionId();
        while (true) {
            String nextOption = getNextOption();
            if (nextOption == null) {
                break;
            }
            nextOption.hashCode();
            if (nextOption.equals("-s")) {
                try {
                    defaultSubscriptionId = Integer.parseInt(getNextArgRequired());
                } catch (NumberFormatException unused) {
                    errPrintWriter.println("set-carrier-service-package-override requires an integer as a subscription ID.");
                    return -1;
                }
            }
        }
        String nextArg = getNextArg();
        if (nextArg == null) {
            errPrintWriter.println("set-carrier-service-package-override requires a override package name.");
            return -1;
        }
        try {
            this.mInterface.setCarrierServicePackageOverride(defaultSubscriptionId, nextArg, this.mContext.getOpPackageName());
            Log.v("TelephonyShellCommand", "set-carrier-service-package-override -s " + defaultSubscriptionId + " " + nextArg);
            return 0;
        } catch (RemoteException | IllegalArgumentException | IllegalStateException e) {
            Log.w("TelephonyShellCommand", "set-carrier-service-package-override -s " + defaultSubscriptionId + " " + nextArg + ", error" + e.getMessage());
            StringBuilder sb = new StringBuilder("Exception: ");
            sb.append(e.getMessage());
            errPrintWriter.println(sb.toString());
            return -1;
        }
    }

    private int clearCarrierServicePackageOverride() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        int defaultSubscriptionId = SubscriptionManager.getDefaultSubscriptionId();
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                nextOption.hashCode();
                if (nextOption.equals("-s")) {
                    try {
                        defaultSubscriptionId = Integer.parseInt(getNextArgRequired());
                    } catch (NumberFormatException unused) {
                        errPrintWriter.println("clear-carrier-service-package-override requires an integer as a subscription ID.");
                        return -1;
                    }
                }
            } else {
                try {
                    this.mInterface.setCarrierServicePackageOverride(defaultSubscriptionId, (String) null, this.mContext.getOpPackageName());
                    Log.v("TelephonyShellCommand", "clear-carrier-service-package-override -s " + defaultSubscriptionId);
                    return 0;
                } catch (RemoteException | IllegalArgumentException | IllegalStateException e) {
                    Log.w("TelephonyShellCommand", "clear-carrier-service-package-override -s " + defaultSubscriptionId + ", error" + e.getMessage());
                    StringBuilder sb = new StringBuilder("Exception: ");
                    sb.append(e.getMessage());
                    errPrintWriter.println(sb.toString());
                    return -1;
                }
            }
        }
    }

    private int handleDomainSelectionCommand() {
        String nextArg = getNextArg();
        if (nextArg == null) {
            onHelpDomainSelection();
            return 0;
        }
        nextArg.hashCode();
        if (nextArg.equals("set-dss-override")) {
            return handleDomainSelectionSetServiceOverrideCommand();
        }
        if (nextArg.equals("clear-dss-override")) {
            return handleDomainSelectionClearServiceOverrideCommand();
        }
        return -1;
    }

    private int handleDomainSelectionSetServiceOverrideCommand() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        String nextArg = getNextArg();
        try {
            boolean domainSelectionServiceOverride = this.mInterface.setDomainSelectionServiceOverride(ComponentName.unflattenFromString(nextArg));
            Log.v("TelephonyShellCommand", "domainselection set-dss-override " + nextArg + ", result=" + domainSelectionServiceOverride);
            getOutPrintWriter().println(domainSelectionServiceOverride);
            return 0;
        } catch (Exception e) {
            Log.w("TelephonyShellCommand", "domainselection set-dss-override " + nextArg + ", error=" + e.getMessage());
            StringBuilder sb = new StringBuilder("Exception: ");
            sb.append(e.getMessage());
            errPrintWriter.println(sb.toString());
            return -1;
        }
    }

    private int handleDomainSelectionClearServiceOverrideCommand() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        try {
            boolean zClearDomainSelectionServiceOverride = this.mInterface.clearDomainSelectionServiceOverride();
            Log.v("TelephonyShellCommand", "domainselection clear-dss-override result=" + zClearDomainSelectionServiceOverride);
            getOutPrintWriter().println(zClearDomainSelectionServiceOverride);
            return 0;
        } catch (RemoteException e) {
            Log.w("TelephonyShellCommand", "domainselection clear-dss-override error=" + e.getMessage());
            errPrintWriter.println("Exception: " + e.getMessage());
            return -1;
        }
    }

    private String convertToJsonString(int i, String str) {
        String[] strArrSplit = str.split(":");
        if (i == 0) {
            return "{\"" + strArrSplit[1] + "\":";
        }
        if (i == 1) {
            return "{\"" + strArrSplit[0] + "\":[" + strArrSplit[1] + "],";
        }
        if (i != 2) {
            return null;
        }
        return "\"" + strArrSplit[0] + "\":[\"" + strArrSplit[1] + "\"]}}";
    }

    private int handleOverrideCarrierRoamingNtnEligibilityChanged() {
        boolean z;
        PrintWriter errPrintWriter = getErrPrintWriter();
        boolean z2 = false;
        try {
            String nextArg = getNextArg();
            boolean z3 = true;
            if (nextArg == null) {
                z = true;
                z3 = false;
            } else if ("true".equalsIgnoreCase(nextArg)) {
                z = false;
            } else {
                z = false;
                z3 = false;
            }
            try {
                boolean zOverrideCarrierRoamingNtnEligibilityChanged = this.mInterface.overrideCarrierRoamingNtnEligibilityChanged(z3, z);
                Log.v("TelephonyShellCommand", "handleSetSatelliteAccessRestrictionCheckingResult returns: " + zOverrideCarrierRoamingNtnEligibilityChanged);
                getOutPrintWriter().println(zOverrideCarrierRoamingNtnEligibilityChanged);
                Log.d("TelephonyShellCommand", "handleSetSatelliteAccessRestrictionCheckingResult(" + z3 + ")");
                return 0;
            } catch (RemoteException e) {
                e = e;
                z2 = z3;
                Log.w("TelephonyShellCommand", "handleSetSatelliteAccessRestrictionCheckingResult(" + z2 + "), error = " + e.getMessage());
                StringBuilder sb = new StringBuilder("Exception: ");
                sb.append(e.getMessage());
                errPrintWriter.println(sb.toString());
                return -1;
            }
        } catch (RemoteException e2) {
            e = e2;
        }
    }

    private int handleDeleteTestImsiKey() {
        if (!checkShellUid()) {
            Log.v("TelephonyShellCommand", "handleCarrierRestrictionStatusCommand, MockModem service check fails or  checkShellUid fails");
            return -1;
        }
        Phone defaultPhone = PhoneFactory.getDefaultPhone();
        if (defaultPhone == null) {
            Log.e("TelephonyShellCommand", "handleCarrierRestrictionStatusCommandNo default Phone available");
            return -1;
        }
        defaultPhone.resetCarrierKeysForImsiEncryption(true);
        return 1;
    }

    private int handleRilEvent() {
        String nextArg = getNextArg();
        if (nextArg == null) {
            onHelpRilEvent();
            return -1;
        }
        nextArg.hashCode();
        if (nextArg.equals("stk-proactive-cmd")) {
            return handleStkProactiveCommand();
        }
        return -1;
    }

    private int handleStkProactiveCommand() {
        int defaultSlot = getDefaultSlot();
        PrintWriter errPrintWriter = getErrPrintWriter();
        String nextArgRequired = null;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption == null) {
                break;
            }
            nextOption.hashCode();
            if (nextOption.equals("-s")) {
                try {
                    defaultSlot = Integer.parseInt(getNextArgRequired());
                } catch (NumberFormatException unused) {
                    errPrintWriter.println("send-ril-event stk-proactive-cmd requires an integer as a SLOT_ID.");
                    return -1;
                }
            } else if (nextOption.equals("-t")) {
                nextArgRequired = getNextArgRequired();
            }
        }
        if (nextArgRequired == null) {
            errPrintWriter.println("send-ril-event stk-proactive-cmd requires a TLV");
        }
        this.mFakeRil.sendProactiveCmdToCatService(defaultSlot, nextArgRequired);
        return 0;
    }

    private int handleSetSatelliteIgnorePlmnListFromStorage() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        boolean z = false;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                nextOption.hashCode();
                if (nextOption.equals("-d")) {
                    z = Boolean.parseBoolean(getNextArgRequired());
                }
            } else {
                Log.d("TelephonyShellCommand", "handleSetSatelliteIgnorePlmnListFromStorage: enabled =" + z);
                try {
                    boolean satelliteIgnorePlmnListFromStorage = this.mInterface.setSatelliteIgnorePlmnListFromStorage(z);
                    Log.v("TelephonyShellCommand", "handleSetAllPlmnListFromStorageEmpty " + z + ", result = " + satelliteIgnorePlmnListFromStorage);
                    getOutPrintWriter().println(satelliteIgnorePlmnListFromStorage);
                    return 0;
                } catch (RemoteException e) {
                    Log.w("TelephonyShellCommand", "handleSetAllPlmnListFromStorageEmpty: " + z + ", error = " + e.getMessage());
                    StringBuilder sb = new StringBuilder("Exception: ");
                    sb.append(e.getMessage());
                    errPrintWriter.println(sb.toString());
                    return -1;
                }
            }
        }
    }

    private int handleSetSimCount() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        try {
            int i = Integer.parseInt(getNextArgRequired());
            if (i < 1 || i > 2) {
                errPrintWriter.println("Need either 1 or 2 for sim count");
                return -1;
            }
            Log.d("TelephonyShellCommand", "handleSetSimCount: sim count=" + i + ", uid=" + Binder.getCallingUid());
            try {
                this.mInterface.switchMultiSimConfig(i);
                Log.v("TelephonyShellCommand", "switchMultiSimConfig " + i);
                getOutPrintWriter().println("Switched to " + i + " SIMs");
                return 0;
            } catch (RemoteException e) {
                Log.w("TelephonyShellCommand", "switchMultiSimConfig: " + i + ", error = " + e.getMessage());
                StringBuilder sb = new StringBuilder("Exception: ");
                sb.append(e.getMessage());
                errPrintWriter.println(sb.toString());
                return -1;
            }
        } catch (NumberFormatException unused) {
            errPrintWriter.println("Need either 1 or 2 for sim count");
            return -1;
        }
    }
}
