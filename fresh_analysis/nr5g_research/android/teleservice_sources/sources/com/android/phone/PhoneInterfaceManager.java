package com.android.phone;

import ai.onnxruntime.OrtProvider$$ExternalSyntheticBUOutline0;
import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.app.PropertyInvalidatedCache;
import android.app.compat.CompatChanges;
import android.app.role.RoleManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.internal.telephony.sysprop.TelephonyProperties;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.LocaleList;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.ParcelFileDescriptor;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceSpecificException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.WorkSource;
import android.preference.PreferenceManager;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.provider.Telephony;
import android.service.carrier.CarrierIdentifier;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.ActivityStatsTechSpecificInfo;
import android.telephony.AnomalyReporter;
import android.telephony.CallForwardingInfo;
import android.telephony.CarrierConfigManager;
import android.telephony.CarrierRestrictionRules;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityGsm;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoWcdma;
import android.telephony.DataThrottlingRequest;
import android.telephony.IBootstrapAuthenticationCallback;
import android.telephony.ICellInfoCallback;
import android.telephony.IccOpenLogicalChannelResponse;
import android.telephony.LocationAccessPolicy;
import android.telephony.MiuiTelephonyManagerStub;
import android.telephony.ModemActivityInfo;
import android.telephony.NeighboringCellInfo;
import android.telephony.NetworkScanRequest;
import android.telephony.PhoneCapability;
import android.telephony.PhoneNumberRange;
import android.telephony.RadioAccessFamily;
import android.telephony.RadioAccessSpecifier;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SignalStrengthUpdateRequest;
import android.telephony.SignalThresholdInfo;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyBaseUtilsStub;
import android.telephony.TelephonyFrameworkInitializer;
import android.telephony.TelephonyManager;
import android.telephony.ThermalMitigationRequest;
import android.telephony.UiccCardInfo;
import android.telephony.UiccPortInfo;
import android.telephony.UiccSlotInfo;
import android.telephony.UiccSlotMapping;
import android.telephony.UssdResponse;
import android.telephony.VisualVoicemailSmsFilterSettings;
import android.telephony.data.NetworkSlicingConfig;
import android.telephony.emergency.EmergencyNumber;
import android.telephony.gba.GbaAuthRequest;
import android.telephony.gba.UaSecurityProtocolIdentifier;
import android.telephony.ims.ImsException;
import android.telephony.ims.RcsClientConfiguration;
import android.telephony.ims.RcsContactUceCapability;
import android.telephony.ims.RegistrationManager;
import android.telephony.ims.aidl.IFeatureProvisioningCallback;
import android.telephony.ims.aidl.IImsCapabilityCallback;
import android.telephony.ims.aidl.IImsConfig;
import android.telephony.ims.aidl.IImsConfigCallback;
import android.telephony.ims.aidl.IImsRegistration;
import android.telephony.ims.aidl.IImsRegistrationCallback;
import android.telephony.ims.aidl.IRcsConfigCallback;
import android.telephony.satellite.EnableRequestAttributes;
import android.telephony.satellite.EnableResponse;
import android.telephony.satellite.INtnSignalStrengthCallback;
import android.telephony.satellite.ISatelliteCapabilitiesCallback;
import android.telephony.satellite.ISatelliteCommunicationAccessStateCallback;
import android.telephony.satellite.ISatelliteDatagramCallback;
import android.telephony.satellite.ISatelliteDisallowedReasonsCallback;
import android.telephony.satellite.ISatelliteModemStateCallback;
import android.telephony.satellite.ISatelliteProvisionStateCallback;
import android.telephony.satellite.ISatelliteTransmissionUpdateCallback;
import android.telephony.satellite.ISelectedNbIotSatelliteSubscriptionCallback;
import android.telephony.satellite.PlmnSatelliteConfig;
import android.telephony.satellite.PointingUiAppLaunchIntentAttributes;
import android.telephony.satellite.SatelliteDatagram;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.EventLog;
import android.util.Log;
import android.util.Pair;
import androidx.fragment.app.BackStackRecord$$ExternalSyntheticBUOutline0;
import com.android.ims.ImsManager;
import com.android.ims.internal.IImsServiceFeatureCallback;
import com.android.ims.rcs.uce.eab.EabUtil;
import com.android.internal.app.LocalePicker;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.CallManager;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.CarrierPrivilegesTracker;
import com.android.internal.telephony.CarrierResolver;
import com.android.internal.telephony.CellNetworkScanResult;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.GbaManager;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.HalVersion;
import com.android.internal.telephony.IBooleanConsumer;
import com.android.internal.telephony.ICallForwardingInfoCallback;
import com.android.internal.telephony.IImsStateCallback;
import com.android.internal.telephony.IIntegerConsumer;
import com.android.internal.telephony.INumberVerificationCallback;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.IccLogicalChannelRequest;
import com.android.internal.telephony.LocaleTracker;
import com.android.internal.telephony.NetworkScanRequestTracker;
import com.android.internal.telephony.OperatorInfo;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConfigurationManager;
import com.android.internal.telephony.PhoneConstantConversions;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.ProxyController;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.RadioInterfaceCapabilityController;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.SmsApplication;
import com.android.internal.telephony.SmsPermissions;
import com.android.internal.telephony.TelephonyCommonUtilsStub;
import com.android.internal.telephony.TelephonyCountryDetector;
import com.android.internal.telephony.TelephonyPermissions;
import com.android.internal.telephony.configupdate.TelephonyConfigUpdateInstallReceiver;
import com.android.internal.telephony.data.DataNetworkController;
import com.android.internal.telephony.data.DataProfileManager;
import com.android.internal.telephony.data.DataUtils;
import com.android.internal.telephony.domainselection.DomainSelectionResolver;
import com.android.internal.telephony.emergency.EmergencyNumberTracker;
import com.android.internal.telephony.euicc.EuiccConnector;
import com.android.internal.telephony.ims.ImsResolver;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneCallTracker;
import com.android.internal.telephony.metrics.RcsStats;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.internal.telephony.satellite.SatelliteServiceUtils;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccIoResult;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.PinStorage;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccPort;
import com.android.internal.telephony.uicc.UiccProfile;
import com.android.internal.telephony.uicc.UiccSlot;
import com.android.internal.telephony.util.LocaleUtils;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.internal.telephony.util.VoicemailNotificationSettingsUtil;
import com.android.internal.util.FunctionalUtils;
import com.android.phone.callcomposer.CallComposerPictureManager;
import com.android.phone.callcomposer.CallComposerPictureTransfer;
import com.android.phone.callcomposer.ImageData;
import com.android.phone.satellite.accesscontrol.SatelliteAccessController;
import com.android.phone.satellite.entitlement.SatelliteEntitlementController;
import com.android.phone.settings.PickSmsSubscriptionActivity;
import com.android.phone.slice.SlicePurchaseController;
import com.android.phone.utils.CarrierAllowListInfo;
import com.android.phone.vvm.PhoneAccountHandleConverter;
import com.android.phone.vvm.RemoteVvmTaskManager;
import com.android.phone.vvm.VisualVoicemailSettingsUtil;
import com.android.phone.vvm.VisualVoicemailSmsFilterConfig;
import com.android.services.telephony.MiTelephonyServiceStub;
import com.android.services.telephony.TelecomAccountRegistry;
import com.android.services.telephony.TelephonyConnectionService;
import com.android.services.telephony.domainselection.TelephonyDomainSelectionService;
import com.android.services.telephony.relay.network.SmartNetworkRelayScore;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.android.storage.s2.S2Support$$ExternalSyntheticBUOutline0;
import com.android.telephony.Rlog;
import com.mediatek.internal.telephony.RadioManager;
import com.mediatek.phone.MtkPhoneInterfaceManagerEx;
import com.nxp.id.tp.common.HexString$$ExternalSyntheticBUOutline1;
import com.ot.pubsub.c.d$$ExternalSyntheticBUOutline0;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.internal.telephony.ModemServiceMoniter;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.mirilhook.MiRilHookBase;
import com.xiaomi.modem.sar.DeviceStateListenerBase;
import gov.nist.core.GenericObject$$ExternalSyntheticBUOutline0;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;
import miuix.animation.motion.MergeMotion$$ExternalSyntheticBUOutline0;
import miuix.preference.flexible.AbstractBaseTemplate;

/* JADX INFO: loaded from: classes.dex */
public class PhoneInterfaceManager extends ITelephony.Stub {
    private static PhoneInterfaceManager sInstance;
    private static List sThermalMitigationAllowlistedPackages = new ArrayList();
    private final PhoneGlobals mApp;
    private AppOpsManager mAppOps;
    private FeatureFlags mFeatureFlags;
    private PackageManager mPackageManager;
    private final SatelliteAccessController mSatelliteAccessController;
    private final SatelliteEntitlementController mSatelliteEntitlementController;
    private final SharedPreferences mTelephonySharedPreferences;
    private ComponentName mTestEuiccUiComponent;
    private final UserManager mUserManager;
    private final Set mCarrierPrivilegeTestOverrideSubIds = new ArraySet();
    private final String KEY_PACKAGE_NAME = "package_name";
    private final String KEY_UID = "uid";
    private boolean mIsInCtsMode = false;
    private AtomicBoolean mCurrentlyModifyingSimAutoPinManagementState = new AtomicBoolean(false);
    private ActivityStatsTechSpecificInfo[] mLastModemActivitySpecificInfo = null;
    private ModemActivityInfo mLastModemActivityInfo = null;
    private final CallManager mCM = PhoneGlobals.getInstance().mCM;
    private final ImsResolver mImsResolver = ImsResolver.getInstance();
    private final SatelliteController mSatelliteController = SatelliteController.getInstance();
    private final MainThreadHandler mMainThreadHandler = new MainThreadHandler();
    private NetworkScanRequestTracker mNetworkScanRequestTracker = new NetworkScanRequestTracker();
    private final PhoneConfigurationManager mPhoneConfigurationManager = PhoneConfigurationManager.getInstance();
    private final RadioInterfaceCapabilityController mRadioInterfaceCapabilities = RadioInterfaceCapabilityController.getInstance();
    private final AtomicBoolean mNotifyUserActivity = new AtomicBoolean(false);
    private final int mVendorApiLevel = SystemProperties.getInt("ro.vendor.api_level", Build.VERSION.DEVICE_INITIAL_SDK_INT);
    private final TelephonyShellCommand mTelephonyShellCommand = new TelephonyShellCommand(this, getDefaultPhone().getContext());

    interface Success {
    }

    final class IccAPDUArgument {
        public int channel;
        public int cla;
        public int command;
        public String data;
        public int p1;
        public int p2;
        public int p3;

        public IccAPDUArgument(int i, int i2, int i3, int i4, int i5, int i6, String str) {
            this.channel = i;
            this.cla = i2;
            this.command = i3;
            this.p1 = i4;
            this.p2 = i5;
            this.p3 = i6;
            this.data = str;
        }
    }

    final class ManualNetworkSelectionArgument {
        public OperatorInfo operatorInfo;
        public boolean persistSelection;

        public ManualNetworkSelectionArgument(OperatorInfo operatorInfo, boolean z) {
            this.operatorInfo = operatorInfo;
            this.persistSelection = z;
        }
    }

    final class PurchasePremiumCapabilityArgument {
        public IIntegerConsumer callback;
        public int capability;

        PurchasePremiumCapabilityArgument(int i, IIntegerConsumer iIntegerConsumer) {
            this.capability = i;
            this.callback = iIntegerConsumer;
        }
    }

    final class MainThreadRequest {
        public Object argument;
        public Phone phone;
        public Object result;
        public Integer subId;
        public WorkSource workSource;

        MainThreadRequest(Object obj, Phone phone, WorkSource workSource) {
            this.subId = -1;
            this.argument = obj;
            if (phone != null) {
                this.phone = phone;
            }
            this.workSource = workSource;
        }

        MainThreadRequest(Object obj, Integer num, WorkSource workSource) {
            this.subId = -1;
            this.argument = obj;
            if (num != null) {
                this.subId = num;
            }
            this.workSource = workSource;
        }
    }

    final class MainThreadHandler extends Handler {
        private MainThreadHandler() {
        }

        /* JADX WARN: Code duplicated, block: B:130:0x037d  */
        /* JADX WARN: Code duplicated, block: B:280:0x067c  */
        /* JADX WARN: Code duplicated, block: B:295:0x06d9  */
        /* JADX WARN: Code duplicated, block: B:339:0x0798  */
        /* JADX WARN: Code duplicated, block: B:579:0x0e64  */
        /* JADX WARN: Code duplicated, block: B:655:0x1081  */
        /* JADX WARN: Code duplicated, block: B:657:0x1088  */
        /* JADX WARN: Instruction removed from duplicated block: B:579:0x0e64, please report this as an issue */
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Object obj;
            Object obj2;
            Object obj3;
            IccOpenLogicalChannelResponse iccOpenLogicalChannelResponse;
            Object obj4;
            Object obj5;
            Object obj6;
            int i;
            Bundle bundle;
            Object obj7;
            CellNetworkScanResult cellNetworkScanResult;
            Object obj8;
            int i2;
            int carrierIdFromIdentifier;
            Object obj9;
            boolean zHandleUssdRequest;
            Object obj10;
            Object arrayList;
            Object unsupportedOperationException;
            Object obj11;
            Object obj12;
            int i3;
            Object obj13;
            CallForwardingInfo callForwardingInfo;
            int i4;
            int i5;
            Object obj14;
            Object obj15;
            Object obj16;
            int i6;
            Object obj17;
            Phone defaultPhone = PhoneInterfaceManager.this.getDefaultPhone();
            int i7 = message.what;
            if (i7 == 1) {
                MainThreadRequest mainThreadRequest = (MainThreadRequest) message.obj;
                mainThreadRequest.result = Boolean.valueOf(PhoneInterfaceManager.this.getPhoneFromRequest(mainThreadRequest) != null ? PhoneInterfaceManager.this.getPhoneFromRequest(mainThreadRequest).handlePinMmi((String) mainThreadRequest.argument) : false);
                notifyRequester(mainThreadRequest);
                return;
            }
            if (i7 == 21) {
                MainThreadRequest mainThreadRequest2 = (MainThreadRequest) message.obj;
                PhoneInterfaceManager.this.getPhoneFromRequest(mainThreadRequest2).getAllowedNetworkTypesBitmask(obtainMessage(22, mainThreadRequest2));
                return;
            }
            if (i7 == 22) {
                AsyncResult asyncResult = (AsyncResult) message.obj;
                MainThreadRequest mainThreadRequest3 = (MainThreadRequest) asyncResult.userObj;
                Throwable th = asyncResult.exception;
                if (th == null && (obj = asyncResult.result) != null) {
                    mainThreadRequest3.result = obj;
                } else {
                    mainThreadRequest3.result = new int[]{-1};
                    if (asyncResult.result == null) {
                        PhoneInterfaceManager.loge("getAllowedNetworkTypesBitmask: Empty response");
                    } else if (th instanceof CommandException) {
                        PhoneInterfaceManager.loge("getAllowedNetworkTypesBitmask: CommandException: " + asyncResult.exception);
                    } else {
                        PhoneInterfaceManager.loge("getAllowedNetworkTypesBitmask: Unknown exception");
                    }
                }
                notifyRequester(mainThreadRequest3);
                return;
            }
            byte[] bArr = null;
            networkSlicingConfig = null;
            Parcelable networkSlicingConfig = null;
            modemActivityInfo = null;
            modemActivityInfo = null;
            Parcelable modemActivityInfo = null;
            illegalStateException = null;
            Object illegalStateException = null;
            if (i7 == 25) {
                MainThreadRequest mainThreadRequest4 = (MainThreadRequest) message.obj;
                UiccPort uiccPortFromRequest = PhoneInterfaceManager.this.getUiccPortFromRequest(mainThreadRequest4);
                if (uiccPortFromRequest == null) {
                    PhoneInterfaceManager.loge("sendEnvelopeWithStatus: No UICC");
                    mainThreadRequest4.result = new IccIoResult(111, 0, (byte[]) null);
                    notifyRequester(mainThreadRequest4);
                    return;
                }
                uiccPortFromRequest.sendEnvelopeWithStatus((String) mainThreadRequest4.argument, obtainMessage(26, mainThreadRequest4));
                return;
            }
            if (i7 != 26) {
                int i8 = 4;
                switch (i7) {
                    case 7:
                        MainThreadRequest mainThreadRequest5 = (MainThreadRequest) message.obj;
                        IccAPDUArgument iccAPDUArgument = (IccAPDUArgument) mainThreadRequest5.argument;
                        UiccPort uiccPortFromRequest2 = PhoneInterfaceManager.this.getUiccPortFromRequest(mainThreadRequest5);
                        if (uiccPortFromRequest2 == null) {
                            PhoneInterfaceManager.loge("iccTransmitApduLogicalChannel: No UICC");
                            mainThreadRequest5.result = new IccIoResult(111, 0, (byte[]) null);
                            notifyRequester(mainThreadRequest5);
                            return;
                        }
                        uiccPortFromRequest2.iccTransmitApduLogicalChannel(iccAPDUArgument.channel, iccAPDUArgument.cla, iccAPDUArgument.command, iccAPDUArgument.p1, iccAPDUArgument.p2, iccAPDUArgument.p3, iccAPDUArgument.data, obtainMessage(8, mainThreadRequest5));
                        return;
                    case 8:
                        AsyncResult asyncResult2 = (AsyncResult) message.obj;
                        MainThreadRequest mainThreadRequest6 = (MainThreadRequest) asyncResult2.userObj;
                        if (asyncResult2.exception == null && (obj3 = asyncResult2.result) != null) {
                            mainThreadRequest6.result = obj3;
                        } else {
                            mainThreadRequest6.result = new IccIoResult(111, 0, (byte[]) null);
                            if (asyncResult2.result == null) {
                                PhoneInterfaceManager.loge("iccTransmitApduLogicalChannel: Empty response");
                            } else if (asyncResult2.exception instanceof CommandException) {
                                PhoneInterfaceManager.loge("iccTransmitApduLogicalChannel: CommandException: " + asyncResult2.exception);
                            } else {
                                PhoneInterfaceManager.loge("iccTransmitApduLogicalChannel: Unknown exception");
                            }
                        }
                        notifyRequester(mainThreadRequest6);
                        return;
                    case 9:
                        MainThreadRequest mainThreadRequest7 = (MainThreadRequest) message.obj;
                        UiccPort uiccPortFromRequest3 = PhoneInterfaceManager.this.getUiccPortFromRequest(mainThreadRequest7);
                        IccLogicalChannelRequest iccLogicalChannelRequest = (IccLogicalChannelRequest) mainThreadRequest7.argument;
                        if (uiccPortFromRequest3 == null) {
                            PhoneInterfaceManager.loge("iccOpenLogicalChannel: No UICC");
                            mainThreadRequest7.result = new IccOpenLogicalChannelResponse(-1, 2, null);
                            notifyRequester(mainThreadRequest7);
                            return;
                        }
                        uiccPortFromRequest3.iccOpenLogicalChannel(iccLogicalChannelRequest.aid, iccLogicalChannelRequest.p2, obtainMessage(10, mainThreadRequest7));
                        return;
                    case 10:
                        AsyncResult asyncResult3 = (AsyncResult) message.obj;
                        MainThreadRequest mainThreadRequest8 = (MainThreadRequest) asyncResult3.userObj;
                        if (asyncResult3.exception == null && (obj4 = asyncResult3.result) != null) {
                            int[] iArr = (int[]) obj4;
                            int i9 = iArr[0];
                            if (iArr.length > 1) {
                                bArr = new byte[iArr.length - 1];
                                for (int i10 = 1; i10 < iArr.length; i10++) {
                                    bArr[i10 - 1] = (byte) iArr[i10];
                                }
                            }
                            iccOpenLogicalChannelResponse = new IccOpenLogicalChannelResponse(i9, 1, bArr);
                            UiccPort uiccPortFromRequest4 = PhoneInterfaceManager.this.getUiccPortFromRequest(mainThreadRequest8);
                            if (uiccPortFromRequest4 == null) {
                                PhoneInterfaceManager.loge("EVENT_OPEN_CHANNEL_DONE: UiccPort is null");
                            } else {
                                IccLogicalChannelRequest iccLogicalChannelRequest2 = (IccLogicalChannelRequest) mainThreadRequest8.argument;
                                iccLogicalChannelRequest2.channel = i9;
                                uiccPortFromRequest4.onLogicalChannelOpened(iccLogicalChannelRequest2);
                            }
                        } else {
                            if (asyncResult3.result == null) {
                                PhoneInterfaceManager.loge("iccOpenLogicalChannel: Empty response");
                            }
                            if (asyncResult3.exception != null) {
                                PhoneInterfaceManager.loge("iccOpenLogicalChannel: Exception: " + asyncResult3.exception);
                            }
                            CommandException commandException = asyncResult3.exception;
                            if (commandException instanceof CommandException) {
                                CommandException.Error commandError = commandException.getCommandError();
                                if (commandError == CommandException.Error.MISSING_RESOURCE) {
                                    i8 = 2;
                                } else if (commandError == CommandException.Error.NO_SUCH_ELEMENT) {
                                    i8 = 3;
                                }
                            }
                            iccOpenLogicalChannelResponse = new IccOpenLogicalChannelResponse(-1, i8, null);
                        }
                        mainThreadRequest8.result = iccOpenLogicalChannelResponse;
                        notifyRequester(mainThreadRequest8);
                        return;
                    case 11:
                        MainThreadRequest mainThreadRequest9 = (MainThreadRequest) message.obj;
                        UiccPort uiccPortFromRequest5 = PhoneInterfaceManager.this.getUiccPortFromRequest(mainThreadRequest9);
                        if (uiccPortFromRequest5 == null) {
                            PhoneInterfaceManager.loge("iccCloseLogicalChannel: No UICC");
                            mainThreadRequest9.result = new IllegalArgumentException("iccCloseLogicalChannel: No UICC");
                            notifyRequester(mainThreadRequest9);
                            return;
                        }
                        uiccPortFromRequest5.iccCloseLogicalChannel(((Integer) mainThreadRequest9.argument).intValue(), obtainMessage(12, mainThreadRequest9));
                        return;
                    case 12:
                        AsyncResult asyncResult4 = (AsyncResult) message.obj;
                        MainThreadRequest mainThreadRequest10 = (MainThreadRequest) asyncResult4.userObj;
                        Throwable th2 = asyncResult4.exception;
                        if (th2 == null) {
                            mainThreadRequest10.result = Boolean.TRUE;
                            UiccPort uiccPortFromRequest6 = PhoneInterfaceManager.this.getUiccPortFromRequest(mainThreadRequest10);
                            if (uiccPortFromRequest6 == null) {
                                PhoneInterfaceManager.loge("EVENT_CLOSE_CHANNEL_DONE: UiccPort is null");
                            } else {
                                uiccPortFromRequest6.onLogicalChannelClosed(((Integer) mainThreadRequest10.argument).intValue());
                            }
                        } else {
                            mainThreadRequest10.result = Boolean.FALSE;
                            if (th2 instanceof CommandException) {
                                PhoneInterfaceManager.loge("iccCloseLogicalChannel: CommandException: " + asyncResult4.exception);
                                if (asyncResult4.exception.getCommandError() == CommandException.Error.INVALID_ARGUMENTS) {
                                    illegalStateException = new IllegalArgumentException("iccCloseLogicalChannel: invalid argument ");
                                }
                            } else {
                                PhoneInterfaceManager.loge("iccCloseLogicalChannel: Unknown exception");
                            }
                            if (illegalStateException == null) {
                                illegalStateException = new IllegalStateException("exception from modem to close iccLogical Channel");
                            }
                            mainThreadRequest10.result = illegalStateException;
                        }
                        notifyRequester(mainThreadRequest10);
                        return;
                    default:
                        switch (i7) {
                            case 29:
                                MainThreadRequest mainThreadRequest11 = (MainThreadRequest) message.obj;
                                IccAPDUArgument iccAPDUArgument2 = (IccAPDUArgument) mainThreadRequest11.argument;
                                UiccPort uiccPortFromRequest7 = PhoneInterfaceManager.this.getUiccPortFromRequest(mainThreadRequest11);
                                if (uiccPortFromRequest7 == null) {
                                    PhoneInterfaceManager.loge("iccTransmitApduBasicChannel: No UICC");
                                    mainThreadRequest11.result = new IccIoResult(111, 0, (byte[]) null);
                                    notifyRequester(mainThreadRequest11);
                                    return;
                                }
                                uiccPortFromRequest7.iccTransmitApduBasicChannel(iccAPDUArgument2.cla, iccAPDUArgument2.command, iccAPDUArgument2.p1, iccAPDUArgument2.p2, iccAPDUArgument2.p3, iccAPDUArgument2.data, obtainMessage(30, mainThreadRequest11));
                                return;
                            case DeviceStateListenerBase.TYPE_HST_ENHANCEMENT_STATE /* 30 */:
                                AsyncResult asyncResult5 = (AsyncResult) message.obj;
                                MainThreadRequest mainThreadRequest12 = (MainThreadRequest) asyncResult5.userObj;
                                if (asyncResult5.exception == null && (obj5 = asyncResult5.result) != null) {
                                    mainThreadRequest12.result = obj5;
                                } else {
                                    mainThreadRequest12.result = new IccIoResult(111, 0, (byte[]) null);
                                    if (asyncResult5.result == null) {
                                        PhoneInterfaceManager.loge("iccTransmitApduBasicChannel: Empty response");
                                    } else if (asyncResult5.exception instanceof CommandException) {
                                        PhoneInterfaceManager.loge("iccTransmitApduBasicChannel: CommandException: " + asyncResult5.exception);
                                    } else {
                                        PhoneInterfaceManager.loge("iccTransmitApduBasicChannel: Unknown exception");
                                    }
                                }
                                notifyRequester(mainThreadRequest12);
                                return;
                            case 31:
                                MainThreadRequest mainThreadRequest13 = (MainThreadRequest) message.obj;
                                IccAPDUArgument iccAPDUArgument3 = (IccAPDUArgument) mainThreadRequest13.argument;
                                UiccPort uiccPortFromRequest8 = PhoneInterfaceManager.this.getUiccPortFromRequest(mainThreadRequest13);
                                if (uiccPortFromRequest8 == null) {
                                    PhoneInterfaceManager.loge("iccExchangeSimIO: No UICC");
                                    mainThreadRequest13.result = new IccIoResult(111, 0, (byte[]) null);
                                    notifyRequester(mainThreadRequest13);
                                    return;
                                }
                                uiccPortFromRequest8.iccExchangeSimIO(iccAPDUArgument3.cla, iccAPDUArgument3.command, iccAPDUArgument3.p1, iccAPDUArgument3.p2, iccAPDUArgument3.p3, iccAPDUArgument3.data, obtainMessage(32, mainThreadRequest13));
                                return;
                            case DynamicSarService.WIFI_DATA_STATE_TCP_STATE /* 32 */:
                                AsyncResult asyncResult6 = (AsyncResult) message.obj;
                                MainThreadRequest mainThreadRequest14 = (MainThreadRequest) asyncResult6.userObj;
                                if (asyncResult6.exception == null && (obj6 = asyncResult6.result) != null) {
                                    mainThreadRequest14.result = obj6;
                                } else {
                                    mainThreadRequest14.result = new IccIoResult(111, 0, (byte[]) null);
                                }
                                notifyRequester(mainThreadRequest14);
                                return;
                            case 33:
                                MainThreadRequest mainThreadRequest15 = (MainThreadRequest) message.obj;
                                Message messageObtainMessage = obtainMessage(34, mainThreadRequest15);
                                Pair pair = (Pair) mainThreadRequest15.argument;
                                PhoneInterfaceManager.this.getPhoneFromRequest(mainThreadRequest15).setVoiceMailNumber((String) pair.first, (String) pair.second, messageObtainMessage);
                                return;
                            case 34:
                                handleNullReturnEvent(message, "setVoicemailNumber");
                                return;
                            case MiRilHookBase.EVENT_OEMHOOK_NV_EFS_SYNC /* 35 */:
                                MainThreadRequest mainThreadRequest16 = (MainThreadRequest) message.obj;
                                PhoneInterfaceManager.this.getPhoneFromRequest(mainThreadRequest16).setNetworkSelectionModeAutomatic(obtainMessage(36, mainThreadRequest16));
                                return;
                            case 36:
                                handleNullReturnEvent(message, "setNetworkSelectionModeAutomatic");
                                return;
                            case 37:
                                MainThreadRequest mainThreadRequest17 = (MainThreadRequest) message.obj;
                                Message messageObtainMessage2 = obtainMessage(38, mainThreadRequest17);
                                messageObtainMessage2.setData(message.getData());
                                boolean zIsActivityInfoOptimizeSupported = MiPhoneAppStub.get().isActivityInfoOptimizeSupported();
                                if (defaultPhone != null && !zIsActivityInfoOptimizeSupported) {
                                    defaultPhone.getModemActivityInfo(messageObtainMessage2, mainThreadRequest17.workSource);
                                    return;
                                }
                                ResultReceiver resultReceiver = (ResultReceiver) mainThreadRequest17.argument;
                                Bundle bundle2 = new Bundle();
                                if (zIsActivityInfoOptimizeSupported && PhoneInterfaceManager.this.mLastModemActivityInfo != null) {
                                    bundle2.putParcelable("controller_activity", PhoneInterfaceManager.this.mLastModemActivityInfo);
                                    PhoneInterfaceManager.log("getModemActivityInfo opt");
                                } else {
                                    bundle2.putParcelable("controller_activity", new ModemActivityInfo(0L, 0, 0, new int[ModemActivityInfo.getNumTxPowerLevels()], 0));
                                }
                                resultReceiver.send(0, bundle2);
                                return;
                            case 38:
                                AsyncResult asyncResult7 = (AsyncResult) message.obj;
                                MainThreadRequest mainThreadRequest18 = (MainThreadRequest) asyncResult7.userObj;
                                ResultReceiver resultReceiver2 = (ResultReceiver) mainThreadRequest18.argument;
                                if (PhoneInterfaceManager.this.mLastModemActivityInfo == null) {
                                    PhoneInterfaceManager.this.mLastModemActivitySpecificInfo = new ActivityStatsTechSpecificInfo[1];
                                    PhoneInterfaceManager.this.mLastModemActivitySpecificInfo[0] = new ActivityStatsTechSpecificInfo(0, 0, new int[ModemActivityInfo.getNumTxPowerLevels()], 0);
                                    PhoneInterfaceManager.this.mLastModemActivityInfo = new ModemActivityInfo(0L, 0, 0, PhoneInterfaceManager.this.mLastModemActivitySpecificInfo);
                                }
                                Throwable th3 = asyncResult7.exception;
                                if (th3 == null && (obj7 = asyncResult7.result) != null) {
                                    ModemActivityInfo modemActivityInfo2 = (ModemActivityInfo) obj7;
                                    if (PhoneInterfaceManager.this.isModemActivityInfoValid(modemActivityInfo2)) {
                                        PhoneInterfaceManager.this.mergeModemActivityInfo(modemActivityInfo2);
                                    } else {
                                        PhoneInterfaceManager.loge("queryModemActivityInfo: invalid response");
                                    }
                                    long timestampMillis = PhoneInterfaceManager.this.mLastModemActivityInfo.getTimestampMillis();
                                    long sleepTimeMillis = PhoneInterfaceManager.this.mLastModemActivityInfo.getSleepTimeMillis();
                                    long idleTimeMillis = PhoneInterfaceManager.this.mLastModemActivityInfo.getIdleTimeMillis();
                                    PhoneInterfaceManager phoneInterfaceManager = PhoneInterfaceManager.this;
                                    modemActivityInfo = new ModemActivityInfo(timestampMillis, sleepTimeMillis, idleTimeMillis, phoneInterfaceManager.deepCopyModemActivitySpecificInfo(phoneInterfaceManager.mLastModemActivitySpecificInfo));
                                } else {
                                    if (asyncResult7.result == null) {
                                        PhoneInterfaceManager.loge("queryModemActivityInfo: Empty response");
                                        i = 2;
                                    } else if (th3 instanceof CommandException) {
                                        PhoneInterfaceManager.loge("queryModemActivityInfo: CommandException: " + asyncResult7.exception);
                                        i = 3;
                                    } else {
                                        PhoneInterfaceManager.loge("queryModemActivityInfo: Unknown exception");
                                    }
                                    bundle = new Bundle();
                                    if (modemActivityInfo != null) {
                                        bundle.putParcelable("controller_activity", modemActivityInfo);
                                    } else {
                                        bundle.putInt("exception", i);
                                    }
                                    resultReceiver2.send(0, bundle);
                                    notifyRequester(mainThreadRequest18);
                                    return;
                                }
                                i = 0;
                                bundle = new Bundle();
                                if (modemActivityInfo != null) {
                                    bundle.putParcelable("controller_activity", modemActivityInfo);
                                } else {
                                    bundle.putInt("exception", i);
                                }
                                resultReceiver2.send(0, bundle);
                                notifyRequester(mainThreadRequest18);
                                return;
                            case MiRilHookBase.EVENT_OEMHOOK_CARD_SLOT_STATUS_QUERY /* 39 */:
                                MainThreadRequest mainThreadRequest19 = (MainThreadRequest) message.obj;
                                PhoneInterfaceManager.this.getPhoneFromRequest(mainThreadRequest19).getAvailableNetworks(obtainMessage(40, mainThreadRequest19));
                                return;
                            case 40:
                                AsyncResult asyncResult8 = (AsyncResult) message.obj;
                                MainThreadRequest mainThreadRequest20 = (MainThreadRequest) asyncResult8.userObj;
                                if (asyncResult8.exception == null && asyncResult8.result != null) {
                                    cellNetworkScanResult = new CellNetworkScanResult(1, (List) asyncResult8.result);
                                } else {
                                    if (asyncResult8.result == null) {
                                        PhoneInterfaceManager.loge("getCellNetworkScanResults: Empty response");
                                    }
                                    if (asyncResult8.exception != null) {
                                        PhoneInterfaceManager.loge("getCellNetworkScanResults: Exception: " + asyncResult8.exception);
                                    }
                                    CommandException commandException2 = asyncResult8.exception;
                                    if (commandException2 instanceof CommandException) {
                                        CommandException.Error commandError2 = commandException2.getCommandError();
                                        if (commandError2 == CommandException.Error.RADIO_NOT_AVAILABLE) {
                                            i8 = 2;
                                        } else if (commandError2 == CommandException.Error.GENERIC_FAILURE) {
                                            i8 = 3;
                                        }
                                    }
                                    cellNetworkScanResult = new CellNetworkScanResult(i8, (List) null);
                                }
                                mainThreadRequest20.result = cellNetworkScanResult;
                                notifyRequester(mainThreadRequest20);
                                return;
                            case 41:
                                MainThreadRequest mainThreadRequest21 = (MainThreadRequest) message.obj;
                                ManualNetworkSelectionArgument manualNetworkSelectionArgument = (ManualNetworkSelectionArgument) mainThreadRequest21.argument;
                                PhoneInterfaceManager.this.getPhoneFromRequest(mainThreadRequest21).selectNetworkManually(manualNetworkSelectionArgument.operatorInfo, manualNetworkSelectionArgument.persistSelection, obtainMessage(42, mainThreadRequest21));
                                return;
                            case 42:
                                AsyncResult asyncResult9 = (AsyncResult) message.obj;
                                MainThreadRequest mainThreadRequest22 = (MainThreadRequest) asyncResult9.userObj;
                                if (asyncResult9.exception == null) {
                                    mainThreadRequest22.result = Boolean.TRUE;
                                } else {
                                    mainThreadRequest22.result = Boolean.FALSE;
                                    PhoneInterfaceManager.loge("setNetworkSelectionModeManual " + asyncResult9.exception);
                                }
                                notifyRequester(mainThreadRequest22);
                                PhoneInterfaceManager.this.mApp.onNetworkSelectionChanged(mainThreadRequest22.subId.intValue());
                                return;
                            case 43:
                                MainThreadRequest mainThreadRequest23 = (MainThreadRequest) message.obj;
                                defaultPhone.setAllowedCarriers((CarrierRestrictionRules) mainThreadRequest23.argument, obtainMessage(44, mainThreadRequest23), mainThreadRequest23.workSource);
                                return;
                            case MiRilHookBase.EVENT_OEMHOOK_HVOLTE_MBN_CFG /* 44 */:
                                AsyncResult asyncResult10 = (AsyncResult) message.obj;
                                MainThreadRequest mainThreadRequest24 = (MainThreadRequest) asyncResult10.userObj;
                                if (asyncResult10.exception == null && (obj8 = asyncResult10.result) != null) {
                                    mainThreadRequest24.result = obj8;
                                } else {
                                    mainThreadRequest24.result = 2;
                                    if (asyncResult10.exception instanceof CommandException) {
                                        PhoneInterfaceManager.loge("setAllowedCarriers: CommandException: " + asyncResult10.exception);
                                        if (asyncResult10.exception.getCommandError() == CommandException.Error.REQUEST_NOT_SUPPORTED) {
                                            mainThreadRequest24.result = 1;
                                        }
                                    } else {
                                        PhoneInterfaceManager.loge("setAllowedCarriers: Unknown exception");
                                    }
                                }
                                notifyRequester(mainThreadRequest24);
                                return;
                            case AbstractBaseTemplate.BIG_ICON_WIDTH_THRESHOLD_DP /* 45 */:
                                MainThreadRequest mainThreadRequest25 = (MainThreadRequest) message.obj;
                                defaultPhone.getAllowedCarriers(obtainMessage(46, mainThreadRequest25), mainThreadRequest25.workSource);
                                return;
                            case 46:
                                AsyncResult asyncResult11 = (AsyncResult) message.obj;
                                MainThreadRequest mainThreadRequest26 = (MainThreadRequest) asyncResult11.userObj;
                                if (asyncResult11.exception == null && (obj9 = asyncResult11.result) != null) {
                                    mainThreadRequest26.result = obj9;
                                } else {
                                    mainThreadRequest26.result = new IllegalStateException("Failed to get carrier restrictions");
                                    if (asyncResult11.result == null) {
                                        PhoneInterfaceManager.loge("getAllowedCarriers: Empty response");
                                    } else if (asyncResult11.exception instanceof CommandException) {
                                        PhoneInterfaceManager.loge("getAllowedCarriers: CommandException: " + asyncResult11.exception);
                                    } else {
                                        PhoneInterfaceManager.loge("getAllowedCarriers: Unknown exception");
                                    }
                                }
                                Object obj18 = mainThreadRequest26.argument;
                                if (obj18 != null) {
                                    CallerCallbackInfo callerCallbackInfo = (CallerCallbackInfo) obj18;
                                    Consumer consumer = callerCallbackInfo.getConsumer();
                                    Set carrierIds = callerCallbackInfo.getCarrierIds();
                                    if (asyncResult11.exception == null) {
                                        Object obj19 = asyncResult11.result;
                                        if (obj19 instanceof CarrierRestrictionRules) {
                                            CarrierRestrictionRules carrierRestrictionRules = (CarrierRestrictionRules) obj19;
                                            try {
                                                carrierIdFromIdentifier = CarrierResolver.getCarrierIdFromIdentifier(PhoneInterfaceManager.this.mApp, (CarrierIdentifier) carrierRestrictionRules.getAllowedCarriers().get(0));
                                            } catch (IndexOutOfBoundsException | NullPointerException e) {
                                                Rlog.e("PhoneInterfaceManager", "CarrierIdentifier exception = " + e);
                                                carrierIdFromIdentifier = -1;
                                            }
                                            int carrierRestrictionStatus = carrierRestrictionRules.getCarrierRestrictionStatus();
                                            i2 = (carrierIdFromIdentifier != -1 && carrierIds.contains(Integer.valueOf(carrierIdFromIdentifier)) && carrierRestrictionStatus == 2) ? 3 : carrierRestrictionStatus;
                                        } else {
                                            Rlog.e("PhoneInterfaceManager", "getCarrierRestrictionStatus: exception ex = " + asyncResult11.exception);
                                            i2 = 0;
                                        }
                                        break;
                                    } else {
                                        Rlog.e("PhoneInterfaceManager", "getCarrierRestrictionStatus: exception ex = " + asyncResult11.exception);
                                        i2 = 0;
                                    }
                                    consumer.accept(Integer.valueOf(i2));
                                    return;
                                }
                                notifyRequester(mainThreadRequest26);
                                return;
                            case ModemServiceMoniter.EVENT_OEMHOOK_CALL_STATUS_NOTIFY /* 47 */:
                                MainThreadRequest mainThreadRequest27 = (MainThreadRequest) message.obj;
                                Phone phoneFromRequest = PhoneInterfaceManager.this.getPhoneFromRequest(mainThreadRequest27);
                                Pair pair2 = (Pair) mainThreadRequest27.argument;
                                String str = (String) pair2.first;
                                ResultReceiver resultReceiver3 = (ResultReceiver) pair2.second;
                                if (!PhoneInterfaceManager.this.isUssdApiAllowed(mainThreadRequest27.subId.intValue())) {
                                    Rlog.w("PhoneInterfaceManager", "handleUssdRequest: carrier does not support USSD apis.");
                                    UssdResponse ussdResponse = new UssdResponse(str, (CharSequence) null);
                                    Bundle bundle3 = new Bundle();
                                    bundle3.putParcelable("USSD_RESPONSE", ussdResponse);
                                    resultReceiver3.send(-1, bundle3);
                                    mainThreadRequest27.result = Boolean.TRUE;
                                    notifyRequester(mainThreadRequest27);
                                    return;
                                }
                                if (phoneFromRequest != null) {
                                    try {
                                        zHandleUssdRequest = phoneFromRequest.handleUssdRequest(str, resultReceiver3);
                                    } catch (CallStateException unused) {
                                        mainThreadRequest27.result = Boolean.FALSE;
                                    }
                                } else {
                                    zHandleUssdRequest = false;
                                }
                                mainThreadRequest27.result = Boolean.valueOf(zHandleUssdRequest);
                                notifyRequester(mainThreadRequest27);
                                return;
                            case ModemServiceMoniter.EVENT_OEMHOOK_DDS_CHANGE_NOTIFY /* 48 */:
                                MainThreadRequest mainThreadRequest28 = (MainThreadRequest) message.obj;
                                UiccPort uiccPortFromRequest9 = PhoneInterfaceManager.this.getUiccPortFromRequest(mainThreadRequest28);
                                if (uiccPortFromRequest9 == null) {
                                    PhoneInterfaceManager.loge("getForbiddenPlmns() UiccPort is null");
                                    mainThreadRequest28.result = new IllegalArgumentException("getForbiddenPlmns() UiccPort is null");
                                    notifyRequester(mainThreadRequest28);
                                    return;
                                }
                                Integer num = (Integer) mainThreadRequest28.argument;
                                UiccCardApplication applicationByType = uiccPortFromRequest9.getApplicationByType(num.intValue());
                                if (applicationByType == null) {
                                    PhoneInterfaceManager.loge("getForbiddenPlmns() no app with specified type -- " + num);
                                    mainThreadRequest28.result = new IllegalArgumentException("Failed to get UICC App");
                                    notifyRequester(mainThreadRequest28);
                                    return;
                                }
                                applicationByType.getIccRecords().getForbiddenPlmns(obtainMessage(49, mainThreadRequest28));
                                return;
                            case 49:
                                AsyncResult asyncResult12 = (AsyncResult) message.obj;
                                MainThreadRequest mainThreadRequest29 = (MainThreadRequest) asyncResult12.userObj;
                                if (asyncResult12.exception == null && (obj10 = asyncResult12.result) != null) {
                                    mainThreadRequest29.result = obj10;
                                } else {
                                    mainThreadRequest29.result = new IllegalArgumentException("Failed to retrieve Forbidden Plmns");
                                    if (asyncResult12.result == null) {
                                        PhoneInterfaceManager.loge("getForbiddenPlmns: Empty response");
                                    } else {
                                        PhoneInterfaceManager.loge("getForbiddenPlmns: Unknown exception");
                                    }
                                }
                                notifyRequester(mainThreadRequest29);
                                return;
                            case 50:
                                MainThreadRequest mainThreadRequest30 = (MainThreadRequest) message.obj;
                                UiccController.getInstance().switchSlots((List) mainThreadRequest30.argument, obtainMessage(51, mainThreadRequest30));
                                return;
                            case 51:
                                AsyncResult asyncResult13 = (AsyncResult) message.obj;
                                MainThreadRequest mainThreadRequest31 = (MainThreadRequest) asyncResult13.userObj;
                                mainThreadRequest31.result = Boolean.valueOf(asyncResult13.exception == null);
                                notifyRequester(mainThreadRequest31);
                                return;
                            case 52:
                                MainThreadRequest mainThreadRequest32 = (MainThreadRequest) message.obj;
                                PhoneInterfaceManager.this.getPhoneFromRequest(mainThreadRequest32).getNetworkSelectionMode(obtainMessage(53, mainThreadRequest32));
                                return;
                            case 53:
                                AsyncResult asyncResult14 = (AsyncResult) message.obj;
                                MainThreadRequest mainThreadRequest33 = (MainThreadRequest) asyncResult14.userObj;
                                if (asyncResult14.exception != null) {
                                    mainThreadRequest33.result = 0;
                                } else if (((int[]) asyncResult14.result)[0] == 0) {
                                    mainThreadRequest33.result = 1;
                                } else {
                                    mainThreadRequest33.result = 2;
                                }
                                notifyRequester(mainThreadRequest33);
                                return;
                            default:
                                switch (i7) {
                                    case 60:
                                        MainThreadRequest mainThreadRequest34 = (MainThreadRequest) message.obj;
                                        mainThreadRequest34.phone.requestCellInfoUpdate(mainThreadRequest34.workSource, obtainMessage(61, mainThreadRequest34));
                                        return;
                                    case 61:
                                        AsyncResult asyncResult15 = (AsyncResult) message.obj;
                                        MainThreadRequest mainThreadRequest35 = (MainThreadRequest) asyncResult15.userObj;
                                        if (asyncResult15.exception != null || (arrayList = asyncResult15.result) == null) {
                                            arrayList = new ArrayList();
                                        }
                                        mainThreadRequest35.result = arrayList;
                                        synchronized (mainThreadRequest35) {
                                            mainThreadRequest35.notifyAll();
                                            break;
                                        }
                                        return;
                                    case MiRilHookBase.EVENT_OEMHOOK_MODEM_STATS_DATA_GET /* 62 */:
                                        MainThreadRequest mainThreadRequest36 = (MainThreadRequest) message.obj;
                                        PhoneInterfaceManager.this.getPhoneFromRequest(mainThreadRequest36).getCellIdentity((WorkSource) mainThreadRequest36.argument, obtainMessage(63, mainThreadRequest36));
                                        return;
                                    case 63:
                                        AsyncResult asyncResult16 = (AsyncResult) message.obj;
                                        MainThreadRequest mainThreadRequest37 = (MainThreadRequest) asyncResult16.userObj;
                                        if (asyncResult16.exception == null) {
                                            mainThreadRequest37.result = asyncResult16.result;
                                        } else {
                                            PhoneInterfaceManager.this.getPhoneFromRequest(mainThreadRequest37);
                                            FeatureFlags unused2 = PhoneInterfaceManager.this.mFeatureFlags;
                                            mainThreadRequest37.result = new CellIdentityGsm();
                                        }
                                        synchronized (mainThreadRequest37) {
                                            mainThreadRequest37.notifyAll();
                                            break;
                                        }
                                        return;
                                    case DynamicSarService.WIFI_DATA_STATE_EXTRA_APP_STATE /* 64 */:
                                        defaultPhone.rebootModem(obtainMessage(65, (MainThreadRequest) message.obj));
                                        return;
                                    case 65:
                                        AsyncResult asyncResult17 = (AsyncResult) message.obj;
                                        MainThreadRequest mainThreadRequest38 = (MainThreadRequest) asyncResult17.userObj;
                                        CommandException commandException3 = asyncResult17.exception;
                                        int iSwitchDispatch = PhoneInterfaceManager$MainThreadHandler$$ExternalSyntheticTypeSwitchClass1.switchDispatch(commandException3, 0);
                                        if (iSwitchDispatch == -1) {
                                            unsupportedOperationException = new Success(this) { // from class: com.android.phone.PhoneInterfaceManager.MainThreadHandler.1
                                            };
                                        } else if (iSwitchDispatch == 0) {
                                            CommandException commandException4 = commandException3;
                                            int i11 = AnonymousClass5.$SwitchMap$com$android$internal$telephony$CommandException$Error[commandException4.getCommandError().ordinal()];
                                            if (i11 == 1) {
                                                unsupportedOperationException = new UnsupportedOperationException("Reboot Radio not Supported");
                                            } else if (i11 == 2) {
                                                unsupportedOperationException = new IllegalStateException("Modem currently unavailable");
                                            } else {
                                                unsupportedOperationException = new RuntimeException((Throwable) commandException4);
                                            }
                                        } else {
                                            unsupportedOperationException = new RuntimeException(asyncResult17.exception);
                                        }
                                        mainThreadRequest38.result = unsupportedOperationException;
                                        notifyRequester(mainThreadRequest38);
                                        return;
                                    case 66:
                                        MainThreadRequest mainThreadRequest39 = (MainThreadRequest) message.obj;
                                        Message messageObtainMessage3 = obtainMessage(67, mainThreadRequest39);
                                        messageObtainMessage3.setData(message.getData());
                                        mainThreadRequest39.phone.requestCellInfoUpdate(mainThreadRequest39.workSource, messageObtainMessage3);
                                        return;
                                    case 67:
                                        AsyncResult asyncResult18 = (AsyncResult) message.obj;
                                        ICellInfoCallback iCellInfoCallback = (ICellInfoCallback) ((MainThreadRequest) asyncResult18.userObj).argument;
                                        try {
                                            if (asyncResult18.exception != null) {
                                                Log.e("PhoneInterfaceManager", "Exception retrieving CellInfo=" + asyncResult18.exception);
                                                iCellInfoCallback.onError(2, asyncResult18.exception.getClass().getName(), asyncResult18.exception.toString());
                                                return;
                                            }
                                            if (asyncResult18.result == null) {
                                                Log.w("PhoneInterfaceManager", "Timeout Waiting for CellInfo!");
                                                iCellInfoCallback.onError(1, (String) null, (String) null);
                                                return;
                                            } else {
                                                if (message.getData() != null && message.getData().get("package_name") != null && message.getData().get("uid") != null) {
                                                    iCellInfoCallback.onCellInfo(MiPhoneAppStub.get().getBlurCellInfo(PhoneInterfaceManager.this.mApp, PhoneInterfaceManager.this.mAppOps, (List) asyncResult18.result, (String) message.getData().get("package_name"), ((Integer) message.getData().get("uid")).intValue()));
                                                    if (message.getData().get("extra_state") != null) {
                                                        return;
                                                    }
                                                    return;
                                                }
                                                iCellInfoCallback.onCellInfo((List) asyncResult18.result);
                                                return;
                                            }
                                        } catch (RemoteException unused3) {
                                            Log.w("PhoneInterfaceManager", "Discarded CellInfo due to Callback RemoteException");
                                            return;
                                        }
                                    case 68:
                                        MainThreadRequest mainThreadRequest40 = (MainThreadRequest) message.obj;
                                        boolean zBooleanValue = ((Boolean) mainThreadRequest40.argument).booleanValue();
                                        Message messageObtainMessage4 = obtainMessage(69, mainThreadRequest40);
                                        messageObtainMessage4.arg1 = zBooleanValue ? 1 : 0;
                                        PhoneConfigurationManager.getInstance().enablePhone(mainThreadRequest40.phone, zBooleanValue, messageObtainMessage4);
                                        return;
                                    case 69:
                                        AsyncResult asyncResult19 = (AsyncResult) message.obj;
                                        MainThreadRequest mainThreadRequest41 = (MainThreadRequest) asyncResult19.userObj;
                                        mainThreadRequest41.result = Boolean.valueOf(asyncResult19.exception == null);
                                        int phoneId = mainThreadRequest41.phone.getPhoneId();
                                        if (((Boolean) mainThreadRequest41.result).booleanValue()) {
                                            PhoneInterfaceManager.this.mPhoneConfigurationManager.addToPhoneStatusCache(phoneId, message.arg1 == 1);
                                        } else {
                                            Log.e("PhoneInterfaceManager", message.what + " failure. Not updating modem status." + asyncResult19.exception);
                                        }
                                        notifyRequester(mainThreadRequest41);
                                        return;
                                    case 70:
                                        MainThreadRequest mainThreadRequest42 = (MainThreadRequest) message.obj;
                                        PhoneConfigurationManager.getInstance().getPhoneStatusFromModem(mainThreadRequest42.phone, obtainMessage(71, mainThreadRequest42));
                                        return;
                                    case 71:
                                        AsyncResult asyncResult20 = (AsyncResult) message.obj;
                                        MainThreadRequest mainThreadRequest43 = (MainThreadRequest) asyncResult20.userObj;
                                        int phoneId2 = mainThreadRequest43.phone.getPhoneId();
                                        if (asyncResult20.exception == null && (obj11 = asyncResult20.result) != null) {
                                            mainThreadRequest43.result = obj11;
                                            PhoneInterfaceManager.this.mPhoneConfigurationManager.addToPhoneStatusCache(phoneId2, ((Boolean) mainThreadRequest43.result).booleanValue());
                                        } else {
                                            mainThreadRequest43.result = Boolean.TRUE;
                                            Log.e("PhoneInterfaceManager", message.what + " failure. Not updating modem status." + asyncResult20.exception);
                                        }
                                        notifyRequester(mainThreadRequest43);
                                        return;
                                    case 72:
                                        MainThreadRequest mainThreadRequest44 = (MainThreadRequest) message.obj;
                                        UiccPort uiccPortFromRequest10 = PhoneInterfaceManager.this.getUiccPortFromRequest(mainThreadRequest44);
                                        if (uiccPortFromRequest10 == null) {
                                            PhoneInterfaceManager.loge("setForbiddenPlmns: UiccPort is null");
                                            mainThreadRequest44.result = -1;
                                            notifyRequester(mainThreadRequest44);
                                            return;
                                        }
                                        Pair pair3 = (Pair) mainThreadRequest44.argument;
                                        Integer num2 = (Integer) pair3.first;
                                        List list = (List) pair3.second;
                                        UiccCardApplication applicationByType2 = uiccPortFromRequest10.getApplicationByType(num2.intValue());
                                        if (applicationByType2 == null) {
                                            PhoneInterfaceManager.loge("setForbiddenPlmns: no app with specified type -- " + num2);
                                            mainThreadRequest44.result = -1;
                                            PhoneInterfaceManager.loge("Failed to get UICC App");
                                            notifyRequester(mainThreadRequest44);
                                            return;
                                        }
                                        applicationByType2.getIccRecords().setForbiddenPlmns(obtainMessage(73, mainThreadRequest44), list);
                                        return;
                                    case 73:
                                        AsyncResult asyncResult21 = (AsyncResult) message.obj;
                                        MainThreadRequest mainThreadRequest45 = (MainThreadRequest) asyncResult21.userObj;
                                        if (asyncResult21.exception == null && (obj12 = asyncResult21.result) != null) {
                                            mainThreadRequest45.result = obj12;
                                        } else {
                                            mainThreadRequest45.result = -1;
                                            PhoneInterfaceManager.loge("Failed to set Forbidden Plmns");
                                            if (asyncResult21.result == null) {
                                                PhoneInterfaceManager.loge("setForbidenPlmns: Empty response");
                                            } else if (asyncResult21.exception != null) {
                                                PhoneInterfaceManager.loge("setForbiddenPlmns: Exception: " + asyncResult21.exception);
                                                mainThreadRequest45.result = -1;
                                            } else {
                                                PhoneInterfaceManager.loge("setForbiddenPlmns: Unknown exception");
                                            }
                                        }
                                        notifyRequester(mainThreadRequest45);
                                        return;
                                    default:
                                        switch (i7) {
                                            case 76:
                                                MainThreadRequest mainThreadRequest46 = (MainThreadRequest) message.obj;
                                                Message messageObtainMessage5 = obtainMessage(77, mainThreadRequest46);
                                                Pair pair4 = (Pair) mainThreadRequest46.argument;
                                                PhoneInterfaceManager.this.getPhoneFromRequest(mainThreadRequest46).getIccCard().changeIccLockPassword((String) pair4.first, (String) pair4.second, messageObtainMessage5);
                                                return;
                                            case 77:
                                                AsyncResult asyncResult22 = (AsyncResult) message.obj;
                                                MainThreadRequest mainThreadRequest47 = (MainThreadRequest) asyncResult22.userObj;
                                                if (asyncResult22.exception == null) {
                                                    Log.d("PhoneInterfaceManager", "In EVENT_CHANGE_ICC_LOCK_PASSWORD_DONE, during modification? " + PhoneInterfaceManager.this.mCurrentlyModifyingSimAutoPinManagementState.get());
                                                    mainThreadRequest47.result = Integer.MAX_VALUE;
                                                    Pair pair5 = (Pair) mainThreadRequest47.argument;
                                                    int phoneId3 = PhoneInterfaceManager.this.getPhoneFromRequest(mainThreadRequest47).getPhoneId();
                                                    if (!PhoneInterfaceManager.this.mCurrentlyModifyingSimAutoPinManagementState.get()) {
                                                        UiccController.getInstance().getPinStorage().storePin((String) pair5.second, phoneId3);
                                                    }
                                                } else {
                                                    mainThreadRequest47.result = Integer.valueOf(message.arg1);
                                                }
                                                notifyRequester(mainThreadRequest47);
                                                return;
                                            case 78:
                                                MainThreadRequest mainThreadRequest48 = (MainThreadRequest) message.obj;
                                                Message messageObtainMessage6 = obtainMessage(79, mainThreadRequest48);
                                                Pair pair6 = (Pair) mainThreadRequest48.argument;
                                                PhoneInterfaceManager.this.getPhoneFromRequest(mainThreadRequest48).getIccCard().setIccLockEnabled(((Boolean) pair6.first).booleanValue(), (String) pair6.second, messageObtainMessage6);
                                                return;
                                            case 79:
                                                AsyncResult asyncResult23 = (AsyncResult) message.obj;
                                                MainThreadRequest mainThreadRequest49 = (MainThreadRequest) asyncResult23.userObj;
                                                if (asyncResult23.exception == null) {
                                                    Log.d("PhoneInterfaceManager", "In EVENT_SET_ICC_LOCK_ENABLED_DONE, during modification? " + PhoneInterfaceManager.this.mCurrentlyModifyingSimAutoPinManagementState.get());
                                                    mainThreadRequest49.result = Integer.MAX_VALUE;
                                                    Pair pair7 = (Pair) mainThreadRequest49.argument;
                                                    int phoneId4 = PhoneInterfaceManager.this.getPhoneFromRequest(mainThreadRequest49).getPhoneId();
                                                    if (!PhoneInterfaceManager.this.mCurrentlyModifyingSimAutoPinManagementState.get()) {
                                                        if (((Boolean) pair7.first).booleanValue()) {
                                                            UiccController.getInstance().getPinStorage().storePin((String) pair7.second, phoneId4);
                                                        } else {
                                                            UiccController.getInstance().getPinStorage().clearPin(phoneId4);
                                                        }
                                                    }
                                                } else {
                                                    mainThreadRequest49.result = Integer.valueOf(message.arg1);
                                                }
                                                notifyRequester(mainThreadRequest49);
                                                return;
                                            case 80:
                                                MainThreadRequest mainThreadRequest50 = (MainThreadRequest) message.obj;
                                                mainThreadRequest50.phone.setSystemSelectionChannels((List) ((Pair) mainThreadRequest50.argument).first, obtainMessage(81, mainThreadRequest50));
                                                return;
                                            case MiRilHookBase.EVENT_OEMHOOK_MTB_CLASS_MASK_GET /* 81 */:
                                                AsyncResult asyncResult24 = (AsyncResult) message.obj;
                                                MainThreadRequest mainThreadRequest51 = (MainThreadRequest) asyncResult24.userObj;
                                                ((Consumer) ((Pair) mainThreadRequest51.argument).second).accept(Boolean.valueOf(asyncResult24.exception == null));
                                                notifyRequester(mainThreadRequest51);
                                                return;
                                            case MiRilHookBase.EVENT_OEMHOOK_XIAOMI_MODEM_HAL_CHIP_ID_GET /* 82 */:
                                                removeMessages(82);
                                                Intent intent = new Intent("android.intent.action.USER_ACTIVITY_NOTIFICATION");
                                                intent.addFlags(1073741824);
                                                PhoneInterfaceManager.this.getDefaultPhone().getContext().sendBroadcastAsUser(intent, UserHandle.ALL, "android.permission.USER_ACTIVITY");
                                                return;
                                            case 83:
                                                MainThreadRequest mainThreadRequest52 = (MainThreadRequest) message.obj;
                                                mainThreadRequest52.phone.getCallForwardingOption(((Integer) ((Pair) mainThreadRequest52.argument).first).intValue(), obtainMessage(84, mainThreadRequest52));
                                                return;
                                            case SmartNetworkRelayScore.SCORE_PASS /* 84 */:
                                                AsyncResult asyncResult25 = (AsyncResult) message.obj;
                                                TelephonyManager.CallForwardingInfoCallback callForwardingInfoCallback = (TelephonyManager.CallForwardingInfoCallback) ((Pair) ((MainThreadRequest) asyncResult25.userObj).argument).second;
                                                if (asyncResult25.exception == null && (obj13 = asyncResult25.result) != null) {
                                                    CallForwardInfo[] callForwardInfoArr = (CallForwardInfo[]) obj13;
                                                    int length = callForwardInfoArr.length;
                                                    int i12 = 0;
                                                    while (true) {
                                                        if (i12 < length) {
                                                            CallForwardInfo callForwardInfo = callForwardInfoArr[i12];
                                                            if ((callForwardInfo.serviceClass & 1) > 0) {
                                                                callForwardingInfo = new CallForwardingInfo(callForwardInfo.status == 1, callForwardInfo.reason, callForwardInfo.number, callForwardInfo.timeSeconds);
                                                            } else {
                                                                i12++;
                                                            }
                                                        } else {
                                                            callForwardingInfo = null;
                                                        }
                                                    }
                                                    if (callForwardingInfo == null) {
                                                        callForwardingInfo = new CallForwardingInfo(false, 0, (String) null, 0);
                                                    }
                                                    callForwardingInfoCallback.onCallForwardingInfoAvailable(callForwardingInfo);
                                                    return;
                                                }
                                                if (asyncResult25.result == null) {
                                                    PhoneInterfaceManager.loge("EVENT_GET_CALL_FORWARDING_DONE: Empty response");
                                                }
                                                if (asyncResult25.exception != null) {
                                                    PhoneInterfaceManager.loge("EVENT_GET_CALL_FORWARDING_DONE: Exception: " + asyncResult25.exception);
                                                }
                                                CommandException commandException5 = asyncResult25.exception;
                                                if (commandException5 instanceof CommandException) {
                                                    CommandException.Error commandError3 = commandException5.getCommandError();
                                                    if (commandError3 == CommandException.Error.FDN_CHECK_FAILURE) {
                                                        i3 = 2;
                                                    } else if (commandError3 == CommandException.Error.REQUEST_NOT_SUPPORTED) {
                                                        i3 = 3;
                                                    } else {
                                                        i3 = 1;
                                                    }
                                                } else {
                                                    i3 = 1;
                                                }
                                                callForwardingInfoCallback.onError(i3);
                                                return;
                                            case 85:
                                                Message messageObtainMessage7 = obtainMessage(86, (MainThreadRequest) message.obj);
                                                MainThreadRequest mainThreadRequest53 = (MainThreadRequest) message.obj;
                                                CallForwardingInfo callForwardingInfo2 = (CallForwardingInfo) ((Pair) mainThreadRequest53.argument).first;
                                                mainThreadRequest53.phone.setCallForwardingOption(callForwardingInfo2.isEnabled() ? 3 : 0, callForwardingInfo2.getReason(), callForwardingInfo2.getNumber(), callForwardingInfo2.getTimeoutSeconds(), messageObtainMessage7);
                                                return;
                                            case 86:
                                                AsyncResult asyncResult26 = (AsyncResult) message.obj;
                                                Consumer consumer2 = (Consumer) ((Pair) ((MainThreadRequest) asyncResult26.userObj).argument).second;
                                                if (asyncResult26.exception != null) {
                                                    PhoneInterfaceManager.loge("setCallForwarding exception: " + asyncResult26.exception);
                                                    CommandException commandException6 = asyncResult26.exception;
                                                    if (commandException6 instanceof CommandException) {
                                                        CommandException.Error commandError4 = commandException6.getCommandError();
                                                        if (commandError4 == CommandException.Error.FDN_CHECK_FAILURE) {
                                                            i4 = 2;
                                                        } else if (commandError4 == CommandException.Error.REQUEST_NOT_SUPPORTED) {
                                                            i4 = 3;
                                                        } else {
                                                            i4 = 1;
                                                        }
                                                    } else {
                                                        i4 = 1;
                                                    }
                                                    consumer2.accept(Integer.valueOf(i4));
                                                    return;
                                                }
                                                consumer2.accept(0);
                                                return;
                                            case 87:
                                                MainThreadRequest mainThreadRequest54 = (MainThreadRequest) message.obj;
                                                PhoneInterfaceManager.this.getPhoneFromRequest(mainThreadRequest54).getCallWaiting(obtainMessage(88, mainThreadRequest54));
                                                return;
                                            case 88:
                                                AsyncResult asyncResult27 = (AsyncResult) message.obj;
                                                Consumer consumer3 = (Consumer) ((MainThreadRequest) asyncResult27.userObj).argument;
                                                if (asyncResult27.exception == null && (obj14 = asyncResult27.result) != null) {
                                                    int[] iArr2 = (int[]) obj14;
                                                    if (iArr2.length <= 1 || (iArr2[1] & 1) <= 0) {
                                                        i5 = 2;
                                                    } else {
                                                        i5 = iArr2[0] == 0 ? 2 : 1;
                                                    }
                                                } else {
                                                    if (asyncResult27.result == null) {
                                                        PhoneInterfaceManager.loge("EVENT_GET_CALL_WAITING_DONE: Empty response");
                                                    }
                                                    if (asyncResult27.exception != null) {
                                                        PhoneInterfaceManager.loge("EVENT_GET_CALL_WAITING_DONE: Exception: " + asyncResult27.exception);
                                                    }
                                                    CommandException commandException7 = asyncResult27.exception;
                                                    if (commandException7 instanceof CommandException) {
                                                        CommandException.Error commandError5 = commandException7.getCommandError();
                                                        if (commandError5 == CommandException.Error.REQUEST_NOT_SUPPORTED) {
                                                            i5 = 4;
                                                        } else if (commandError5 == CommandException.Error.FDN_CHECK_FAILURE) {
                                                            i5 = 5;
                                                        } else {
                                                            i5 = 3;
                                                        }
                                                    } else {
                                                        i5 = 3;
                                                    }
                                                }
                                                consumer3.accept(Integer.valueOf(i5));
                                                return;
                                            case 89:
                                                MainThreadRequest mainThreadRequest55 = (MainThreadRequest) message.obj;
                                                PhoneInterfaceManager.this.getPhoneFromRequest(mainThreadRequest55).setCallWaiting(((Boolean) ((Pair) mainThreadRequest55.argument).first).booleanValue(), obtainMessage(90, mainThreadRequest55));
                                                return;
                                            case 90:
                                                AsyncResult asyncResult28 = (AsyncResult) message.obj;
                                                MainThreadRequest mainThreadRequest56 = (MainThreadRequest) asyncResult28.userObj;
                                                boolean zBooleanValue2 = ((Boolean) ((Pair) mainThreadRequest56.argument).first).booleanValue();
                                                Consumer consumer4 = (Consumer) ((Pair) mainThreadRequest56.argument).second;
                                                if (asyncResult28.exception != null) {
                                                    PhoneInterfaceManager.loge("setCallWaiting exception: " + asyncResult28.exception);
                                                    CommandException commandException8 = asyncResult28.exception;
                                                    if (commandException8 instanceof CommandException) {
                                                        CommandException.Error commandError6 = commandException8.getCommandError();
                                                        if (commandError6 == CommandException.Error.REQUEST_NOT_SUPPORTED) {
                                                            consumer4.accept(4);
                                                            return;
                                                        } else if (commandError6 == CommandException.Error.FDN_CHECK_FAILURE) {
                                                            consumer4.accept(5);
                                                            return;
                                                        } else {
                                                            consumer4.accept(3);
                                                            return;
                                                        }
                                                    }
                                                    consumer4.accept(3);
                                                    return;
                                                }
                                                consumer4.accept(Integer.valueOf(zBooleanValue2 ? 1 : 2));
                                                return;
                                            case miuix.core.util.SystemProperties.PROP_VALUE_MAX /* 91 */:
                                                MainThreadRequest mainThreadRequest57 = (MainThreadRequest) message.obj;
                                                Message messageObtainMessage8 = obtainMessage(92, mainThreadRequest57);
                                                Phone phoneFromRequest2 = PhoneInterfaceManager.this.getPhoneFromRequest(mainThreadRequest57);
                                                if (phoneFromRequest2 != null) {
                                                    phoneFromRequest2.setNrDualConnectivityState(((Integer) mainThreadRequest57.argument).intValue(), messageObtainMessage8, mainThreadRequest57.workSource);
                                                    return;
                                                }
                                                PhoneInterfaceManager.loge("enableNrDualConnectivity: No phone object");
                                                mainThreadRequest57.result = 2;
                                                notifyRequester(mainThreadRequest57);
                                                return;
                                            case 92:
                                                AsyncResult asyncResult29 = (AsyncResult) message.obj;
                                                MainThreadRequest mainThreadRequest58 = (MainThreadRequest) asyncResult29.userObj;
                                                if (asyncResult29.exception == null) {
                                                    mainThreadRequest58.result = 0;
                                                } else {
                                                    mainThreadRequest58.result = 3;
                                                    CommandException commandException9 = asyncResult29.exception;
                                                    if (commandException9 instanceof CommandException) {
                                                        CommandException.Error commandError7 = commandException9.getCommandError();
                                                        if (commandError7 == CommandException.Error.RADIO_NOT_AVAILABLE) {
                                                            mainThreadRequest58.result = 2;
                                                        } else if (commandError7 == CommandException.Error.REQUEST_NOT_SUPPORTED) {
                                                            mainThreadRequest58.result = 1;
                                                        }
                                                        PhoneInterfaceManager.loge("enableNrDualConnectivity: CommandException: " + asyncResult29.exception);
                                                    } else {
                                                        PhoneInterfaceManager.loge("enableNrDualConnectivity: Unknown exception");
                                                    }
                                                }
                                                notifyRequester(mainThreadRequest58);
                                                return;
                                            case 93:
                                                MainThreadRequest mainThreadRequest59 = (MainThreadRequest) message.obj;
                                                Message messageObtainMessage9 = obtainMessage(94, mainThreadRequest59);
                                                Phone phoneFromRequest3 = PhoneInterfaceManager.this.getPhoneFromRequest(mainThreadRequest59);
                                                if (phoneFromRequest3 != null) {
                                                    phoneFromRequest3.isNrDualConnectivityEnabled(messageObtainMessage9, mainThreadRequest59.workSource);
                                                    return;
                                                }
                                                PhoneInterfaceManager.loge("isNRDualConnectivityEnabled: No phone object");
                                                mainThreadRequest59.result = Boolean.FALSE;
                                                notifyRequester(mainThreadRequest59);
                                                return;
                                            case 94:
                                                AsyncResult asyncResult30 = (AsyncResult) message.obj;
                                                MainThreadRequest mainThreadRequest60 = (MainThreadRequest) asyncResult30.userObj;
                                                Throwable th4 = asyncResult30.exception;
                                                if (th4 == null && (obj15 = asyncResult30.result) != null) {
                                                    mainThreadRequest60.result = obj15;
                                                } else {
                                                    Object obj20 = asyncResult30.result;
                                                    if (obj20 != null) {
                                                        mainThreadRequest60.result = obj20;
                                                    } else {
                                                        mainThreadRequest60.result = Boolean.FALSE;
                                                    }
                                                    if (obj20 == null) {
                                                        PhoneInterfaceManager.loge("isNRDualConnectivityEnabled: Empty response");
                                                    } else if (th4 instanceof CommandException) {
                                                        PhoneInterfaceManager.loge("isNRDualConnectivityEnabled: CommandException: " + asyncResult30.exception);
                                                    } else {
                                                        PhoneInterfaceManager.loge("isNRDualConnectivityEnabled: Unknown exception");
                                                    }
                                                }
                                                notifyRequester(mainThreadRequest60);
                                                return;
                                            default:
                                                switch (i7) {
                                                    case 97:
                                                        MainThreadRequest mainThreadRequest61 = (MainThreadRequest) message.obj;
                                                        Message messageObtainMessage10 = obtainMessage(98, mainThreadRequest61);
                                                        Phone phoneFromRequest4 = PhoneInterfaceManager.this.getPhoneFromRequest(mainThreadRequest61);
                                                        if (phoneFromRequest4 != null) {
                                                            phoneFromRequest4.getSystemSelectionChannels(messageObtainMessage10);
                                                            return;
                                                        }
                                                        PhoneInterfaceManager.loge("getSystemSelectionChannels: No phone object");
                                                        mainThreadRequest61.result = new ArrayList();
                                                        notifyRequester(mainThreadRequest61);
                                                        return;
                                                    case 98:
                                                        AsyncResult asyncResult31 = (AsyncResult) message.obj;
                                                        MainThreadRequest mainThreadRequest62 = (MainThreadRequest) asyncResult31.userObj;
                                                        if (asyncResult31.exception == null && (obj16 = asyncResult31.result) != null) {
                                                            mainThreadRequest62.result = obj16;
                                                        } else {
                                                            mainThreadRequest62.result = new IllegalStateException("Failed to retrieve system selecton channels");
                                                            if (asyncResult31.result == null) {
                                                                PhoneInterfaceManager.loge("getSystemSelectionChannels: Empty response");
                                                            } else {
                                                                PhoneInterfaceManager.loge("getSystemSelectionChannels: Unknown exception");
                                                            }
                                                        }
                                                        notifyRequester(mainThreadRequest62);
                                                        return;
                                                    case 99:
                                                        MainThreadRequest mainThreadRequest63 = (MainThreadRequest) message.obj;
                                                        Message messageObtainMessage11 = obtainMessage(100, mainThreadRequest63);
                                                        DataThrottlingRequest dataThrottlingRequest = (DataThrottlingRequest) mainThreadRequest63.argument;
                                                        Phone phoneFromRequest5 = PhoneInterfaceManager.this.getPhoneFromRequest(mainThreadRequest63);
                                                        if (phoneFromRequest5 != null) {
                                                            phoneFromRequest5.setDataThrottling(messageObtainMessage11, mainThreadRequest63.workSource, dataThrottlingRequest.getDataThrottlingAction(), dataThrottlingRequest.getCompletionDurationMillis());
                                                            return;
                                                        }
                                                        PhoneInterfaceManager.loge("setDataThrottling: No phone object");
                                                        mainThreadRequest63.result = 2;
                                                        notifyRequester(mainThreadRequest63);
                                                        return;
                                                    case 100:
                                                        AsyncResult asyncResult32 = (AsyncResult) message.obj;
                                                        MainThreadRequest mainThreadRequest64 = (MainThreadRequest) asyncResult32.userObj;
                                                        Throwable th5 = asyncResult32.exception;
                                                        if (th5 == null) {
                                                            mainThreadRequest64.result = 0;
                                                        } else if (th5 instanceof CommandException) {
                                                            PhoneInterfaceManager.loge("setDataThrottling: CommandException: " + asyncResult32.exception);
                                                            CommandException.Error commandError8 = asyncResult32.exception.getCommandError();
                                                            if (commandError8 == CommandException.Error.RADIO_NOT_AVAILABLE) {
                                                                mainThreadRequest64.result = 2;
                                                            } else if (commandError8 == CommandException.Error.INVALID_ARGUMENTS) {
                                                                mainThreadRequest64.result = -1;
                                                            } else if (commandError8 == CommandException.Error.REQUEST_NOT_SUPPORTED) {
                                                                mainThreadRequest64.result = -2;
                                                            } else {
                                                                mainThreadRequest64.result = 1;
                                                            }
                                                        } else {
                                                            mainThreadRequest64.result = 1;
                                                        }
                                                        Log.w("PhoneInterfaceManager", "DataThrottlingResult = " + mainThreadRequest64.result);
                                                        notifyRequester(mainThreadRequest64);
                                                        return;
                                                    case 101:
                                                        Message messageObtainMessage12 = obtainMessage(102, (MainThreadRequest) message.obj);
                                                        MainThreadRequest mainThreadRequest65 = (MainThreadRequest) message.obj;
                                                        mainThreadRequest65.phone.setSimPowerState(((Integer) ((Pair) mainThreadRequest65.argument).first).intValue(), messageObtainMessage12, mainThreadRequest65.workSource);
                                                        return;
                                                    case 102:
                                                        AsyncResult asyncResult33 = (AsyncResult) message.obj;
                                                        IIntegerConsumer iIntegerConsumer = (IIntegerConsumer) ((Pair) ((MainThreadRequest) asyncResult33.userObj).argument).second;
                                                        if (asyncResult33.exception != null) {
                                                            PhoneInterfaceManager.loge("setSimPower exception: " + asyncResult33.exception);
                                                            CommandException commandException10 = asyncResult33.exception;
                                                            if (commandException10 instanceof CommandException) {
                                                                CommandException.Error commandError9 = commandException10.getCommandError();
                                                                if (commandError9 == CommandException.Error.SIM_ERR) {
                                                                    i8 = 3;
                                                                } else if (commandError9 == CommandException.Error.INVALID_ARGUMENTS) {
                                                                    i8 = 1;
                                                                } else if (commandError9 != CommandException.Error.REQUEST_NOT_SUPPORTED) {
                                                                    i8 = 2;
                                                                }
                                                            } else {
                                                                i8 = 1;
                                                            }
                                                            try {
                                                                iIntegerConsumer.accept(i8);
                                                                return;
                                                            } catch (RemoteException unused4) {
                                                                Log.w("PhoneInterfaceManager", "setSimPower: callback not available.");
                                                                return;
                                                            }
                                                        }
                                                        try {
                                                            iIntegerConsumer.accept(0);
                                                            return;
                                                        } catch (RemoteException unused5) {
                                                            Log.w("PhoneInterfaceManager", "setSimPower: callback not available.");
                                                            return;
                                                        }
                                                    case 103:
                                                        MainThreadRequest mainThreadRequest66 = (MainThreadRequest) message.obj;
                                                        Phone phoneFromRequest6 = PhoneInterfaceManager.this.getPhoneFromRequest(mainThreadRequest66);
                                                        if (phoneFromRequest6 == null || phoneFromRequest6.getServiceStateTracker() == null) {
                                                            mainThreadRequest66.result = new IllegalStateException("Phone or SST is null");
                                                            notifyRequester(mainThreadRequest66);
                                                            return;
                                                        } else {
                                                            Pair pair8 = (Pair) mainThreadRequest66.argument;
                                                            phoneFromRequest6.getSignalStrengthController().setSignalStrengthUpdateRequest(mainThreadRequest66.subId.intValue(), ((Integer) pair8.first).intValue(), (SignalStrengthUpdateRequest) pair8.second, obtainMessage(MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT, mainThreadRequest66));
                                                            return;
                                                        }
                                                    case MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT /* 104 */:
                                                        AsyncResult asyncResult34 = (AsyncResult) message.obj;
                                                        MainThreadRequest mainThreadRequest67 = (MainThreadRequest) asyncResult34.userObj;
                                                        Object obj21 = asyncResult34.exception;
                                                        if (obj21 == null) {
                                                            obj21 = Boolean.TRUE;
                                                        }
                                                        mainThreadRequest67.result = obj21;
                                                        notifyRequester(mainThreadRequest67);
                                                        return;
                                                    case MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT /* 105 */:
                                                        MainThreadRequest mainThreadRequest68 = (MainThreadRequest) message.obj;
                                                        Phone phoneFromRequest7 = PhoneInterfaceManager.this.getPhoneFromRequest(mainThreadRequest68);
                                                        if (phoneFromRequest7 == null || phoneFromRequest7.getServiceStateTracker() == null) {
                                                            mainThreadRequest68.result = new IllegalStateException("Phone or SST is null");
                                                            notifyRequester(mainThreadRequest68);
                                                            return;
                                                        } else {
                                                            Pair pair9 = (Pair) mainThreadRequest68.argument;
                                                            phoneFromRequest7.getSignalStrengthController().clearSignalStrengthUpdateRequest(mainThreadRequest68.subId.intValue(), ((Integer) pair9.first).intValue(), (SignalStrengthUpdateRequest) pair9.second, obtainMessage(106, mainThreadRequest68));
                                                            return;
                                                        }
                                                    case 106:
                                                        AsyncResult asyncResult35 = (AsyncResult) message.obj;
                                                        MainThreadRequest mainThreadRequest69 = (MainThreadRequest) asyncResult35.userObj;
                                                        Object obj22 = asyncResult35.exception;
                                                        if (obj22 == null) {
                                                            obj22 = Boolean.TRUE;
                                                        }
                                                        mainThreadRequest69.result = obj22;
                                                        notifyRequester(mainThreadRequest69);
                                                        return;
                                                    case 107:
                                                        MainThreadRequest mainThreadRequest70 = (MainThreadRequest) message.obj;
                                                        Message messageObtainMessage13 = obtainMessage(108, mainThreadRequest70);
                                                        Pair pair10 = (Pair) mainThreadRequest70.argument;
                                                        PhoneInterfaceManager.this.getPhoneFromRequest(mainThreadRequest70).setAllowedNetworkTypes(((Integer) pair10.first).intValue(), ((Long) pair10.second).longValue(), messageObtainMessage13);
                                                        return;
                                                    case 108:
                                                        handleNullReturnEvent(message, "setAllowedNetworkTypesForReason");
                                                        return;
                                                    case 109:
                                                        final MainThreadRequest mainThreadRequest71 = (MainThreadRequest) message.obj;
                                                        final PinStorage pinStorage = UiccController.getInstance().getPinStorage();
                                                        pinStorage.post(new Runnable() { // from class: com.android.phone.PhoneInterfaceManager$MainThreadHandler$$ExternalSyntheticLambda0
                                                            @Override // java.lang.Runnable
                                                            public final void run() {
                                                                this.f$0.lambda$handleMessage$0(mainThreadRequest71, pinStorage);
                                                            }
                                                        });
                                                        return;
                                                    case 110:
                                                        MainThreadRequest mainThreadRequest72 = (MainThreadRequest) message.obj;
                                                        mainThreadRequest72.phone.getSlicingConfig(obtainMessage(111, mainThreadRequest72));
                                                        return;
                                                    case 111:
                                                        AsyncResult asyncResult36 = (AsyncResult) message.obj;
                                                        MainThreadRequest mainThreadRequest73 = (MainThreadRequest) asyncResult36.userObj;
                                                        ResultReceiver resultReceiver4 = (ResultReceiver) mainThreadRequest73.argument;
                                                        Bundle bundle4 = new Bundle();
                                                        if (asyncResult36.exception != null) {
                                                            Log.e("PhoneInterfaceManager", "Exception retrieving slicing configuration=" + asyncResult36.exception);
                                                            i6 = 2;
                                                        } else {
                                                            Object obj23 = asyncResult36.result;
                                                            if (obj23 == null) {
                                                                Log.w("PhoneInterfaceManager", "Timeout Waiting for slicing configuration!");
                                                                i6 = 1;
                                                            } else {
                                                                networkSlicingConfig = (NetworkSlicingConfig) obj23;
                                                                i6 = 0;
                                                            }
                                                        }
                                                        if (networkSlicingConfig == null) {
                                                            networkSlicingConfig = new NetworkSlicingConfig();
                                                        }
                                                        bundle4.putParcelable("slicing_config_handle", networkSlicingConfig);
                                                        resultReceiver4.send(i6, bundle4);
                                                        notifyRequester(mainThreadRequest73);
                                                        return;
                                                    case 112:
                                                        MainThreadRequest mainThreadRequest74 = (MainThreadRequest) message.obj;
                                                        mainThreadRequest74.result = Boolean.valueOf(defaultPhone.eraseDataInSharedPreferences());
                                                        notifyRequester(mainThreadRequest74);
                                                        return;
                                                    case ModemServiceMoniter.EVENT_OEMHOOK_NOTIFY_MODEM_SYNC_DONE /* 113 */:
                                                        MainThreadRequest mainThreadRequest75 = (MainThreadRequest) message.obj;
                                                        Message messageObtainMessage14 = obtainMessage(ModemServiceMoniter.EVENT_OEMHOOK_SCREEN_STATUS_NOTIFY_EX, mainThreadRequest75);
                                                        Phone phoneFromRequest8 = PhoneInterfaceManager.this.getPhoneFromRequest(mainThreadRequest75);
                                                        if (phoneFromRequest8 != null) {
                                                            phoneFromRequest8.setVoNrEnabled(((Boolean) mainThreadRequest75.argument).booleanValue(), messageObtainMessage14, mainThreadRequest75.workSource);
                                                            return;
                                                        }
                                                        PhoneInterfaceManager.loge("setVoNrEnabled: No phone object");
                                                        mainThreadRequest75.result = 2;
                                                        notifyRequester(mainThreadRequest75);
                                                        return;
                                                    case ModemServiceMoniter.EVENT_OEMHOOK_SCREEN_STATUS_NOTIFY_EX /* 114 */:
                                                        AsyncResult asyncResult37 = (AsyncResult) message.obj;
                                                        MainThreadRequest mainThreadRequest76 = (MainThreadRequest) asyncResult37.userObj;
                                                        if (asyncResult37.exception == null) {
                                                            mainThreadRequest76.result = 0;
                                                        } else {
                                                            mainThreadRequest76.result = 3;
                                                            CommandException commandException11 = asyncResult37.exception;
                                                            if (commandException11 instanceof CommandException) {
                                                                CommandException.Error commandError10 = commandException11.getCommandError();
                                                                if (commandError10 == CommandException.Error.RADIO_NOT_AVAILABLE) {
                                                                    mainThreadRequest76.result = 2;
                                                                } else if (commandError10 == CommandException.Error.REQUEST_NOT_SUPPORTED) {
                                                                    mainThreadRequest76.result = 5;
                                                                } else {
                                                                    mainThreadRequest76.result = 3;
                                                                }
                                                                PhoneInterfaceManager.loge("setVoNrEnabled: CommandException: " + asyncResult37.exception);
                                                            } else {
                                                                PhoneInterfaceManager.loge("setVoNrEnabled: Unknown exception");
                                                            }
                                                        }
                                                        notifyRequester(mainThreadRequest76);
                                                        return;
                                                    case MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_GET /* 115 */:
                                                        MainThreadRequest mainThreadRequest77 = (MainThreadRequest) message.obj;
                                                        Message messageObtainMessage15 = obtainMessage(MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_SET, mainThreadRequest77);
                                                        Phone phoneFromRequest9 = PhoneInterfaceManager.this.getPhoneFromRequest(mainThreadRequest77);
                                                        if (phoneFromRequest9 != null) {
                                                            phoneFromRequest9.isVoNrEnabled(messageObtainMessage15, mainThreadRequest77.workSource);
                                                            return;
                                                        }
                                                        PhoneInterfaceManager.loge("isVoNrEnabled: No phone object");
                                                        mainThreadRequest77.result = Boolean.FALSE;
                                                        notifyRequester(mainThreadRequest77);
                                                        return;
                                                    case MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_SET /* 116 */:
                                                        AsyncResult asyncResult38 = (AsyncResult) message.obj;
                                                        MainThreadRequest mainThreadRequest78 = (MainThreadRequest) asyncResult38.userObj;
                                                        Throwable th6 = asyncResult38.exception;
                                                        if (th6 == null && (obj17 = asyncResult38.result) != null) {
                                                            mainThreadRequest78.result = obj17;
                                                        } else {
                                                            Object obj24 = asyncResult38.result;
                                                            if (obj24 != null) {
                                                                mainThreadRequest78.result = obj24;
                                                            } else {
                                                                mainThreadRequest78.result = Boolean.FALSE;
                                                            }
                                                            if (obj24 == null) {
                                                                PhoneInterfaceManager.loge("isVoNrEnabled: Empty response");
                                                            } else if (th6 instanceof CommandException) {
                                                                PhoneInterfaceManager.loge("isVoNrEnabled: CommandException: " + asyncResult38.exception);
                                                            } else {
                                                                PhoneInterfaceManager.loge("isVoNrEnabled: Unknown exception");
                                                            }
                                                        }
                                                        notifyRequester(mainThreadRequest78);
                                                        return;
                                                    case 117:
                                                        MainThreadRequest mainThreadRequest79 = (MainThreadRequest) message.obj;
                                                        SlicePurchaseController.getInstance(mainThreadRequest79.phone, PhoneInterfaceManager.this.mFeatureFlags).purchasePremiumCapability(((PurchasePremiumCapabilityArgument) mainThreadRequest79.argument).capability, obtainMessage(118, mainThreadRequest79));
                                                        return;
                                                    case 118:
                                                        AsyncResult asyncResult39 = (AsyncResult) message.obj;
                                                        PurchasePremiumCapabilityArgument purchasePremiumCapabilityArgument = (PurchasePremiumCapabilityArgument) ((MainThreadRequest) asyncResult39.userObj).argument;
                                                        try {
                                                            int iIntValue = ((Integer) asyncResult39.result).intValue();
                                                            purchasePremiumCapabilityArgument.callback.accept(iIntValue);
                                                            PhoneInterfaceManager.log("purchasePremiumCapability: capability=" + TelephonyManager.convertPremiumCapabilityToString(purchasePremiumCapabilityArgument.capability) + ", result=" + TelephonyManager.convertPurchaseResultToString(iIntValue));
                                                            return;
                                                        } catch (RemoteException e2) {
                                                            AnomalyReporter.reportAnomaly(UUID.fromString("24bf97a6-e8a6-44d8-a6a4-255d7548733c"), "Purchase premium capability " + TelephonyManager.convertPremiumCapabilityToString(purchasePremiumCapabilityArgument.capability) + " failed: " + e2);
                                                            return;
                                                        }
                                                    default:
                                                        Log.w("PhoneInterfaceManager", "MainThreadHandler: unexpected message code: " + message.what);
                                                        return;
                                                }
                                        }
                                }
                        }
                }
            }
            AsyncResult asyncResult40 = (AsyncResult) message.obj;
            MainThreadRequest mainThreadRequest80 = (MainThreadRequest) asyncResult40.userObj;
            if (asyncResult40.exception == null && (obj2 = asyncResult40.result) != null) {
                mainThreadRequest80.result = obj2;
            } else {
                mainThreadRequest80.result = new IccIoResult(111, 0, (byte[]) null);
                if (asyncResult40.result == null) {
                    PhoneInterfaceManager.loge("sendEnvelopeWithStatus: Empty response");
                } else {
                    boolean z = asyncResult40.exception instanceof CommandException;
                    Throwable th7 = asyncResult40.exception;
                    if (z) {
                        PhoneInterfaceManager.loge("sendEnvelopeWithStatus: CommandException: " + th7);
                    } else {
                        PhoneInterfaceManager.loge("sendEnvelopeWithStatus: exception:" + th7);
                    }
                }
            }
            notifyRequester(mainThreadRequest80);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$handleMessage$0(MainThreadRequest mainThreadRequest, PinStorage pinStorage) {
            mainThreadRequest.result = Integer.valueOf(pinStorage.prepareUnattendedReboot(mainThreadRequest.workSource));
            notifyRequester(mainThreadRequest);
        }

        private void notifyRequester(MainThreadRequest mainThreadRequest) {
            synchronized (mainThreadRequest) {
                mainThreadRequest.notifyAll();
            }
        }

        private void handleNullReturnEvent(Message message, String str) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            MainThreadRequest mainThreadRequest = (MainThreadRequest) asyncResult.userObj;
            Throwable th = asyncResult.exception;
            if (th == null) {
                mainThreadRequest.result = Boolean.TRUE;
            } else {
                mainThreadRequest.result = Boolean.FALSE;
                if (th instanceof CommandException) {
                    PhoneInterfaceManager.loge(str + ": CommandException: " + asyncResult.exception);
                } else {
                    PhoneInterfaceManager.loge(str + ": Unknown exception");
                }
            }
            notifyRequester(mainThreadRequest);
        }
    }

    private Object sendRequest(int i, Object obj) {
        return sendRequest(i, obj, -1, null, null, -1L);
    }

    private Object sendRequest(int i, Object obj, WorkSource workSource) {
        return sendRequest(i, obj, -1, null, workSource, -1L);
    }

    private Object sendRequest(int i, Object obj, Integer num) {
        return sendRequest(i, obj, num, null, null, -1L);
    }

    private Object sendRequest(int i, Object obj, Integer num, long j) {
        return sendRequest(i, obj, num, null, null, j);
    }

    private Object sendRequest(int i, Object obj, int i2, WorkSource workSource) {
        return sendRequest(i, obj, Integer.valueOf(i2), null, workSource, -1L);
    }

    private Object sendRequest(int i, Object obj, Phone phone, WorkSource workSource) {
        return sendRequest(i, obj, -1, phone, workSource, -1L);
    }

    private Object sendRequest(int i, Object obj, Integer num, Phone phone, WorkSource workSource, long j) {
        MainThreadRequest mainThreadRequest;
        if (Looper.myLooper() == this.mMainThreadHandler.getLooper()) {
            d$$ExternalSyntheticBUOutline0.m("This method will deadlock if called from the main thread.");
            return null;
        }
        if (num.intValue() != -1 && phone != null) {
            Folme$$ExternalSyntheticBUOutline0.m("subId and phone cannot both be specified!");
            return null;
        }
        if (phone != null) {
            mainThreadRequest = new MainThreadRequest(obj, phone, workSource);
        } else {
            mainThreadRequest = new MainThreadRequest(obj, num, workSource);
        }
        this.mMainThreadHandler.obtainMessage(i, mainThreadRequest).sendToTarget();
        synchronized (mainThreadRequest) {
            try {
                if (j >= 0) {
                    long jElapsedRealtime = SystemClock.elapsedRealtime();
                    long j2 = j + jElapsedRealtime;
                    while (mainThreadRequest.result == null && jElapsedRealtime < j2) {
                        try {
                            mainThreadRequest.wait(j2 - jElapsedRealtime);
                        } catch (InterruptedException unused) {
                        } finally {
                            SystemClock.elapsedRealtime();
                        }
                    }
                } else {
                    while (mainThreadRequest.result == null) {
                        try {
                            mainThreadRequest.wait();
                        } catch (InterruptedException unused2) {
                        }
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (mainThreadRequest.result == null) {
            Log.wtf("PhoneInterfaceManager", "sendRequest: Blocking command timed out. Something has gone terribly wrong.");
        }
        return mainThreadRequest.result;
    }

    private void sendRequestAsync(int i, Object obj) {
        sendRequestAsync(i, obj, null, null);
    }

    private void sendRequestAsync(int i, Object obj, Phone phone, WorkSource workSource) {
        this.mMainThreadHandler.obtainMessage(i, new MainThreadRequest(obj, phone, workSource)).sendToTarget();
    }

    private void sendRequestAsync(int i, Object obj, Phone phone, WorkSource workSource, int i2, String str) {
        Message messageObtainMessage = this.mMainThreadHandler.obtainMessage(i, new MainThreadRequest(obj, phone, workSource));
        Bundle bundle = new Bundle();
        bundle.putString("package_name", str);
        bundle.putInt("uid", i2);
        messageObtainMessage.setData(bundle);
        messageObtainMessage.sendToTarget();
    }

    static PhoneInterfaceManager init(PhoneGlobals phoneGlobals, FeatureFlags featureFlags) {
        PhoneInterfaceManager phoneInterfaceManager;
        synchronized (PhoneInterfaceManager.class) {
            try {
                if (sInstance != null) {
                    Log.wtf("PhoneInterfaceManager", "init() called multiple times!  sInstance = " + sInstance);
                } else {
                    sInstance = new PhoneInterfaceManager(phoneGlobals, featureFlags);
                    TelephonyFrameworkInitializer.getTelephonyServiceManager().getTelephonyServiceRegisterer().register(sInstance);
                    MtkPhoneInterfaceManagerEx.init(phoneGlobals);
                }
                phoneInterfaceManager = sInstance;
            } catch (Throwable th) {
                throw th;
            }
        }
        return phoneInterfaceManager;
    }

    private PhoneInterfaceManager(PhoneGlobals phoneGlobals, FeatureFlags featureFlags) {
        this.mApp = phoneGlobals;
        this.mFeatureFlags = featureFlags;
        this.mUserManager = (UserManager) phoneGlobals.getSystemService("user");
        this.mAppOps = (AppOpsManager) phoneGlobals.getSystemService("appops");
        this.mTelephonySharedPreferences = PreferenceManager.getDefaultSharedPreferences(phoneGlobals);
        this.mPackageManager = phoneGlobals.getPackageManager();
        this.mSatelliteAccessController = SatelliteAccessController.getOrCreateInstance(getDefaultPhone().getContext(), featureFlags);
        PropertyInvalidatedCache.invalidateCache("cache_key.telephony.phone_account_to_subid");
        CarrierAllowListInfo.loadInstance(phoneGlobals);
        this.mSatelliteEntitlementController = SatelliteEntitlementController.make(phoneGlobals, this.mFeatureFlags);
        MiPhoneAppStub.get().initCellInfoLogger();
    }

    public SharedPreferences getSharedPreferences() {
        return this.mTelephonySharedPreferences;
    }

    public Phone getDefaultPhone() {
        Phone phone = getPhone(getDefaultSubscription());
        return phone != null ? phone : PhoneFactory.getDefaultPhone();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Phone getPhoneFromRequest(MainThreadRequest mainThreadRequest) {
        Phone phone = mainThreadRequest.phone;
        return phone != null ? phone : getPhoneFromSubId(mainThreadRequest.subId.intValue());
    }

    private Phone getPhoneFromSubId(int i) {
        return i == -1 ? getDefaultPhone() : getPhone(i);
    }

    private Phone getPhoneFromSubIdOrDefault(int i) {
        Phone phoneFromSubId = getPhoneFromSubId(i);
        if (phoneFromSubId != null) {
            return phoneFromSubId;
        }
        loge("Called with invalid subId: " + i + ". Retrying with default phone.");
        return getDefaultPhone();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public UiccPort getUiccPortFromRequest(MainThreadRequest mainThreadRequest) {
        Phone phoneFromRequest = getPhoneFromRequest(mainThreadRequest);
        if (phoneFromRequest == null) {
            return null;
        }
        return UiccController.getInstance().getUiccPort(phoneFromRequest.getPhoneId());
    }

    private Phone getPhone(int i) {
        return PhoneFactory.getPhone(SubscriptionManager.getPhoneId(i));
    }

    private void sendEraseModemConfig() {
    }

    private void sendEraseDataInSharedPreferences() {
    }

    private boolean isImsAvailableOnDevice() {
        PackageManager packageManager = getDefaultPhone().getContext().getPackageManager();
        if (packageManager == null) {
            return true;
        }
        return packageManager.hasSystemFeature("android.hardware.telephony.ims", 0);
    }

    public void dial(String str) {
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.calling", "dial");
        dialForSubscriber(getPreferredVoiceSubscription(), str);
    }

    public void dialForSubscriber(int i, String str) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            String strCreateTelUrl = createTelUrl(str);
            if (strCreateTelUrl == null) {
                return;
            }
            PhoneConstants.State state = this.mCM.getState(i);
            if (state != PhoneConstants.State.OFFHOOK && state != PhoneConstants.State.RINGING) {
                Intent intent = new Intent("android.intent.action.DIAL", Uri.parse(strCreateTelUrl));
                intent.addFlags(268435456);
                this.mApp.startActivityAsUser(intent, UserHandle.CURRENT);
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void call(String str, String str2) {
        callForSubscriber(getPreferredVoiceSubscription(), str, str2);
    }

    public void callForSubscriber(int i, String str, String str2) {
        enforceCallPermission();
        if (this.mAppOps.noteOp("android:call_phone", Binder.getCallingUid(), str) != 0) {
            return;
        }
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.calling", "call");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            String strCreateTelUrl = createTelUrl(str2);
            if (strCreateTelUrl == null) {
                return;
            }
            List activeSubscriptionInfoListPrivileged = getActiveSubscriptionInfoListPrivileged();
            if (activeSubscriptionInfoListPrivileged != null) {
                Iterator it = activeSubscriptionInfoListPrivileged.iterator();
                while (it.hasNext()) {
                    if (((SubscriptionInfo) it.next()).getSubscriptionId() == i) {
                        Intent intent = new Intent("android.intent.action.CALL", Uri.parse(strCreateTelUrl));
                        intent.putExtra("subscription", i);
                        intent.addFlags(268435456);
                        this.mApp.startActivityAsUser(intent, UserHandle.CURRENT);
                        return;
                    }
                }
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean supplyPinForSubscriber(int i, String str) {
        return supplyPinReportResultForSubscriber(i, str)[0] == 0;
    }

    public boolean supplyPukForSubscriber(int i, String str, String str2) {
        return supplyPukReportResultForSubscriber(i, str, str2)[0] == 0;
    }

    public int[] supplyPinReportResultForSubscriber(int i, String str) {
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "supplyPinReportResultForSubscriber");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            UnlockSim unlockSim = new UnlockSim(phone.getPhoneId(), phone.getIccCard());
            unlockSim.start();
            return unlockSim.unlockSim(null, str);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int[] supplyPukReportResultForSubscriber(int i, String str, String str2) {
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "supplyPukForSubscriber");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            UnlockSim unlockSim = new UnlockSim(phone.getPhoneId(), phone.getIccCard());
            unlockSim.start();
            return unlockSim.unlockSim(str, str2);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    class UnlockSim extends Thread {
        private static final UUID SUPPLY_PIN_UUID = UUID.fromString("d3768135-4323-491d-a6c8-bda01fc89040");
        private Handler mHandler;
        private final int mPhoneId;
        private final IccCard mSimCard;
        private boolean mDone = false;
        private int mResult = 2;
        private int mRetryCount = -1;

        UnlockSim(int i, IccCard iccCard) {
            this.mPhoneId = i;
            this.mSimCard = iccCard;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Looper.prepare();
            synchronized (this) {
                this.mHandler = new Handler() { // from class: com.android.phone.PhoneInterfaceManager.UnlockSim.1
                    @Override // android.os.Handler
                    public void handleMessage(Message message) {
                        AsyncResult asyncResult = (AsyncResult) message.obj;
                        int i = message.what;
                        if (i == 100) {
                            Log.d("PhoneInterfaceManager", "SUPPLY_PIN_COMPLETE");
                            synchronized (UnlockSim.this) {
                                try {
                                    UnlockSim.this.mRetryCount = message.arg1;
                                    CommandException commandException = asyncResult.exception;
                                    if (commandException != null) {
                                        CommandException.Error commandError = commandException instanceof CommandException ? commandException.getCommandError() : null;
                                        if (commandError == CommandException.Error.PASSWORD_INCORRECT) {
                                            UnlockSim.this.mResult = 1;
                                        } else {
                                            CommandException.Error error = CommandException.Error.ABORTED;
                                            UnlockSim unlockSim = UnlockSim.this;
                                            if (commandError == error) {
                                                unlockSim.mResult = 3;
                                            } else {
                                                unlockSim.mResult = 2;
                                            }
                                        }
                                    } else {
                                        UnlockSim.this.mResult = 0;
                                    }
                                    UnlockSim.this.mDone = true;
                                    removeMessages(101);
                                    UnlockSim.this.notifyAll();
                                } catch (Throwable th) {
                                    throw th;
                                }
                            }
                            return;
                        }
                        if (i == 101 && !UnlockSim.this.mDone) {
                            AnomalyReporter.reportAnomaly(UnlockSim.SUPPLY_PIN_UUID, "Delay in receiving SIM PIN response ");
                        }
                    }
                };
                notifyAll();
            }
            Looper.loop();
        }

        synchronized int[] unlockSim(String str, String str2) {
            Handler handler;
            int[] iArr;
            while (true) {
                handler = this.mHandler;
                if (handler != null) {
                    break;
                }
                try {
                    wait();
                } catch (InterruptedException unused) {
                    Thread.currentThread().interrupt();
                }
                throw th;
            }
            Message messageObtain = Message.obtain(handler, 100);
            IccCard iccCard = this.mSimCard;
            if (str == null) {
                iccCard.supplyPin(str2, messageObtain);
            } else {
                iccCard.supplyPuk(str, str2, messageObtain);
            }
            while (!this.mDone) {
                try {
                    Log.d("PhoneInterfaceManager", "wait for done");
                    this.mHandler.sendEmptyMessageDelayed(101, 10000L);
                    wait();
                } catch (InterruptedException unused2) {
                    Thread.currentThread().interrupt();
                }
            }
            Log.d("PhoneInterfaceManager", "unlockSim done");
            int i = this.mResult;
            iArr = new int[]{i, this.mRetryCount};
            if (i == 0 && str2.length() > 0) {
                UiccController.getInstance().getPinStorage().storePin(str2, this.mPhoneId);
            }
            this.mHandler.getLooper().quitSafely();
            return iArr;
        }
    }

    public void updateServiceLocation() {
        Log.e("PhoneInterfaceManager", "Call to unsupported method updateServiceLocation()");
    }

    public void updateServiceLocationWithPackageName(String str) {
        ((AppOpsManager) this.mApp.getSystemService(AppOpsManager.class)).checkPackage(Binder.getCallingUid(), str);
        if (TelephonyPermissions.getTargetSdk(this.mApp, str) > 30) {
            return;
        }
        int i = AnonymousClass5.$SwitchMap$android$telephony$LocationAccessPolicy$LocationPermissionResult[LocationAccessPolicy.checkLocationPermission(this.mApp, new LocationAccessPolicy.LocationPermissionQuery.Builder().setCallingPackage(str).setCallingFeatureId((String) null).setCallingPid(Binder.getCallingPid()).setCallingUid(Binder.getCallingUid()).setMethod("updateServiceLocation").setMinSdkVersionForCoarse(1).setMinSdkVersionForFine(29).build()).ordinal()];
        if (i == 1 || i == 2) {
            return;
        }
        WorkSource workSource = getWorkSource(Binder.getCallingUid());
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            getPhoneFromSubIdOrDefault(getDefaultSubscription()).updateServiceLocation(workSource);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    /* JADX WARN: Code duplicated, block: B:13:0x004b  */
    public int getCurrentTtyMode() {
        enforceReadPrivilegedPermission("Needs READ_PRIVILEGED_PHONE_STATE for getCurrentTtyMode");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.calling", "getCurrentTtyMode");
        Phone defaultPhone = getDefaultPhone();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        int i = 0;
        try {
            try {
                int intForUser = Settings.Secure.getIntForUser(defaultPhone.getContext().getContentResolver(), "preferred_tty_mode", defaultPhone.getContext().getUserId());
                if (intForUser != 0) {
                    try {
                        AudioManager audioManager = (AudioManager) defaultPhone.getContext().getSystemService(AudioManager.class);
                        if (audioManager != null && audioManager.isWiredHeadsetOn()) {
                            i = intForUser;
                        }
                    } catch (Settings.SettingNotFoundException unused) {
                        i = intForUser;
                        return i;
                    }
                } else {
                    i = intForUser;
                }
                return i;
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        } catch (Settings.SettingNotFoundException unused2) {
        }
    }

    public boolean isRadioOn(String str) {
        return isRadioOnWithFeature(str, null);
    }

    public boolean isRadioOnWithFeature(String str, String str2) {
        return isRadioOnForSubscriberWithFeature(getDefaultSubscription(), str, str2);
    }

    public boolean isRadioOnForSubscriber(int i, String str) {
        return isRadioOnForSubscriberWithFeature(i, str, null);
    }

    public boolean isRadioOnForSubscriberWithFeature(int i, String str, String str2) {
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mApp, i, str, str2, "isRadioOnForSubscriber")) {
            return false;
        }
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.radio.access", "isRadioOnWithFeature");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return isRadioOnForSubscriber(i);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private boolean isRadioOnForSubscriber(int i) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        log("SubscriptionManager.getPhoneId(" + i + ")=" + SubscriptionManager.getPhoneId(i));
        try {
            Phone phone = getPhone(i);
            if (phone != null) {
                return phone.isRadioOn();
            }
            log("getPhone is null");
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void toggleRadioOnOff() {
        toggleRadioOnOffForSubscriber(getDefaultSubscription());
    }

    public void toggleRadioOnOffForSubscriber(int i) {
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.radio.access", "toggleRadioOnOffForSubscriber");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            log("toggleRadioOnOffForSubscriber handled by RadioManager");
            if (phone != null) {
                RadioManager.getInstance().notifySimModeChange(!isRadioOnForSubscriber(i), phone.getPhoneId());
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setRadio(boolean z) {
        return setRadioForSubscriber(getDefaultSubscription(), z);
    }

    public boolean setRadioForSubscriber(int i, boolean z) {
        enforceModifyPermission();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone == null) {
                log("getPhone is null");
                return false;
            }
            if (!phone.isRadioAvailable()) {
                log("setRadioForSubscriber, radio unavailable.");
                return false;
            }
            log("setRadioForSubscriber, Radio=" + phone.isRadioOn() + "Request=" + z);
            if (phone.isRadioOn() != z) {
                toggleRadioOnOffForSubscriber(i);
            }
            return true;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean needMobileRadioShutdown() {
        enforceReadPrivilegedPermission("needMobileRadioShutdown");
        if (!this.mApp.getResources().getBoolean(android.R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.radio.access", "needMobileRadioShutdown");
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        for (int i = 0; i < TelephonyManager.getDefault().getPhoneCount(); i++) {
            try {
                Phone phone = PhoneFactory.getPhone(i);
                RadioManager radioManager = RadioManager.getInstance();
                if (phone != null && phone.isRadioAvailable() && radioManager != null && !RadioManager.isModemPowerOff(i)) {
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                    return true;
                }
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                throw th;
            }
        }
        logv(TelephonyManager.getDefault().getPhoneCount() + " Phones are shutdown.");
        Binder.restoreCallingIdentity(jClearCallingIdentity);
        return false;
    }

    public void shutdownMobileRadios() {
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.radio.access", "shutdownMobileRadios");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        for (int i = 0; i < TelephonyManager.getDefault().getPhoneCount(); i++) {
            try {
                logv("Shutting down Phone " + i);
                shutdownRadioUsingPhoneId(i);
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                throw th;
            }
        }
        Binder.restoreCallingIdentity(jClearCallingIdentity);
    }

    private void shutdownRadioUsingPhoneId(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null || !phone.isRadioAvailable()) {
            return;
        }
        phone.shutdownRadio();
    }

    public boolean setRadioPower(boolean z) {
        enforceModifyPermission();
        if (!z) {
            log("setRadioPower off: callingPackage=" + getCurrentPackageName());
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone defaultPhone = PhoneFactory.getDefaultPhone();
            if (defaultPhone != null) {
                defaultPhone.setRadioPower(z);
                return true;
            }
            loge("There's no default phone.");
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean requestRadioPowerOffForReason(int i, int i2) {
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.radio.access", "requestRadioPowerOffForReason");
        log("requestRadioPowerOffForReason: subId=" + i + ",reason=" + i2 + ",callingPackage=" + getCurrentPackageName());
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone[] phones = PhoneFactory.getPhones();
            int length = phones.length;
            int i3 = 0;
            boolean z = false;
            while (i3 < length) {
                phones[i3].setRadioPowerForReason(false, i2);
                i3++;
                z = true;
            }
            if (!z) {
                loge("requestRadioPowerOffForReason: no phone exists");
            }
            return z;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean clearRadioPowerOffForReason(int i, int i2) {
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.radio.access", "clearRadioPowerOffForReason");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone[] phones = PhoneFactory.getPhones();
            int length = phones.length;
            int i3 = 0;
            boolean z = false;
            while (i3 < length) {
                phones[i3].setRadioPowerForReason(true, i2);
                i3++;
                z = true;
            }
            if (!z) {
                loge("clearRadioPowerOffForReason: no phone exists");
            }
            return z;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public List getRadioPowerOffReasons(int i, String str, String str2) {
        enforceReadPrivilegedPermission("getRadioPowerOffReasons");
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.radio.access", "getRadioPowerOffReasons");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        ArrayList arrayList = new ArrayList();
        try {
            if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mApp, i, str, str2, "getRadioPowerOffReasons")) {
                return arrayList;
            }
            Phone phoneFromSubIdOrDefault = getPhoneFromSubIdOrDefault(i);
            if (phoneFromSubIdOrDefault != null) {
                arrayList.addAll(phoneFromSubIdOrDefault.getRadioPowerOffReasons());
            } else {
                loge("getRadioPowerOffReasons: phone is null");
            }
            return arrayList;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean enableDataConnectivity(String str) {
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.data", "enableDataConnectivity");
        if (MiPhoneAppStub.get().setAllPhonesDataEnabled(this.mApp, true, str)) {
            return true;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(SubscriptionManager.getDefaultDataSubscriptionId());
            if (phone == null || phone.getDataSettingsManager() == null) {
                return false;
            }
            phone.getDataSettingsManager().setDataEnabled(0, true, str);
            return true;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean disableDataConnectivity(String str) {
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.data", "disableDataConnectivity");
        if (MiPhoneAppStub.get().setAllPhonesDataEnabled(this.mApp, false, str)) {
            return true;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(SubscriptionManager.getDefaultDataSubscriptionId());
            if (phone == null || phone.getDataSettingsManager() == null) {
                return false;
            }
            phone.getDataSettingsManager().setDataEnabled(0, false, str);
            return true;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isDataConnectivityPossible(int i) {
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.data", "isDataConnectivityPossible");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone != null) {
                return phone.isDataAllowed();
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean handlePinMmi(String str) {
        return handlePinMmiForSubscriber(getDefaultSubscription(), str);
    }

    /* JADX INFO: renamed from: handleUssdRequest, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$setCallWaitingStatus$5(int i, String str, ResultReceiver resultReceiver) {
        enforceCallPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.radio.access", "handleUssdRequest");
        if (!TelephonyPermissions.checkSubscriptionAssociatedWithUser(this.mApp, i, Binder.getCallingUserHandle())) {
            throw new SecurityException("The subscription " + i + " is not associated with the calling user");
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (SubscriptionManager.isValidSubscriptionId(i)) {
                sendRequest(47, new Pair(str, resultReceiver), Integer.valueOf(i));
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean handlePinMmiForSubscriber(int i, String str) {
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.calling", "handlePinMmiForSubscriber");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (SubscriptionManager.isValidSubscriptionId(i)) {
                return ((Boolean) sendRequest(1, str, Integer.valueOf(i))).booleanValue();
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getCallState() {
        if (CompatChanges.isChangeEnabled(157233955L, Binder.getCallingUid())) {
            ImsRcsController$$ExternalSyntheticBUOutline0.m("This method can only be used for applications targeting API version 30 or less.");
            return 0;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return PhoneConstantConversions.convertCallState(getPhoneFromSubIdOrDefault(getDefaultSubscription()).getState());
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getCallStateForSubscription(int i, String str, String str2) {
        int iConvertCallState = 0;
        if (CompatChanges.isChangeEnabled(157233955L, Binder.getCallingUid()) && !TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mApp, i, str, str2, "getCallStateForSubscription")) {
            ImsRcsController$$ExternalSyntheticBUOutline0.m("getCallState requires READ_PHONE_STATE for apps targeting API level 31+.");
            return 0;
        }
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.calling", "getCallStateForSubscription");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone == null) {
                return 0;
            }
            PhoneConstants.State state = phone.getState();
            if (state != null) {
                iConvertCallState = PhoneConstantConversions.convertCallState(state);
            }
            return iConvertCallState;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getDataState() {
        return getDataStateForSubId(SubscriptionManager.getDefaultDataSubscriptionId());
    }

    public int getDataStateForSubId(int i) {
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.data", "getDataStateForSubId");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone != null) {
                return phone.getDataNetworkController().getInternetDataNetworkState();
            }
            return PhoneConstantConversions.convertDataState(PhoneConstants.DataState.DISCONNECTED);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getDataActivity() {
        return getDataActivityForSubId(SubscriptionManager.getDefaultDataSubscriptionId());
    }

    public int getDataActivityForSubId(int i) {
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.data", "getDataActivityForSubId");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone != null) {
                return phone.getDataActivityState();
            }
            return 0;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public CellIdentity getCellLocation(String str, String str2) {
        MiPhoneAppStub.get().recordCallerInfo(1, str, str2);
        int callingUid = Binder.getCallingUid();
        ((AppOpsManager) this.mApp.getSystemService(AppOpsManager.class)).checkPackage(Binder.getCallingUid(), str);
        int i = AnonymousClass5.$SwitchMap$android$telephony$LocationAccessPolicy$LocationPermissionResult[LocationAccessPolicy.checkLocationPermission(this.mApp, new LocationAccessPolicy.LocationPermissionQuery.Builder().setCallingPackage(str).setCallingFeatureId(str2).setCallingPid(Binder.getCallingPid()).setCallingUid(Binder.getCallingUid()).setMethod("getCellLocation").setMinSdkVersionForCoarse(1).setMinSdkVersionForFine(29).build()).ordinal()];
        if (i == 1) {
            ImsRcsController$$ExternalSyntheticBUOutline0.m("Not allowed to access cell location");
            return null;
        }
        if (i != 2) {
            enforceTelephonyFeatureWithException(str, "android.hardware.telephony.radio.access", "getCellLocation");
            WorkSource workSource = getWorkSource(Binder.getCallingUid());
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return MiPhoneAppStub.get().getBlurCellLocation(this.mApp, this.mAppOps, (CellIdentity) sendRequest(62, workSource, Integer.valueOf(SubscriptionManager.getDefaultDataSubscriptionId()), 10000L), str, callingUid);
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }
        return new CellIdentityGsm();
    }

    public String getNetworkCountryIsoForPhone(int i) {
        if (!this.mApp.getResources().getBoolean(android.R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.radio.access", "getNetworkCountryIsoForPhone");
        }
        if (MiPhoneAppStub.get().isNotReadyForApplication(this.mApp)) {
            Rlog.w("PhoneInterfaceManager", "Application is not ready, return empty string");
            return "";
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        if (i == -1) {
            i = Integer.MAX_VALUE;
        }
        try {
            Phone phone = PhoneFactory.getPhone(i);
            if (phone == null) {
                return "";
            }
            ServiceStateTracker serviceStateTracker = phone.getServiceStateTracker();
            if (serviceStateTracker == null) {
                return "";
            }
            LocaleTracker localeTracker = serviceStateTracker.getLocaleTracker();
            return localeTracker == null ? "" : localeTracker.getCurrentCountry();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void enableLocationUpdates() {
        this.mApp.enforceCallingOrSelfPermission("android.permission.CONTROL_LOCATION_UPDATES", null);
    }

    public void disableLocationUpdates() {
        this.mApp.enforceCallingOrSelfPermission("android.permission.CONTROL_LOCATION_UPDATES", null);
    }

    public List getNeighboringCellInfo(String str, String str2) {
        MiPhoneAppStub.get().recordCallerInfo(2, str, str2);
        try {
            ((AppOpsManager) this.mApp.getSystemService(AppOpsManager.class)).checkPackage(Binder.getCallingUid(), str);
            if (TelephonyPermissions.getTargetSdk(this.mApp, str) >= 29) {
                ImsRcsController$$ExternalSyntheticBUOutline0.m("getNeighboringCellInfo() is unavailable to callers targeting Q+ SDK levels.");
                return null;
            }
            if (this.mAppOps.noteOp("android:neighboring_cells", Binder.getCallingUid(), str) != 0) {
                return null;
            }
            enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.radio.access", "getNeighboringCellInfo");
            List<CellInfo> allCellInfo = getAllCellInfo(str, str2);
            if (allCellInfo == null) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            for (CellInfo cellInfo : allCellInfo) {
                if (cellInfo instanceof CellInfoGsm) {
                    arrayList.add(new NeighboringCellInfo((CellInfoGsm) cellInfo));
                } else if (cellInfo instanceof CellInfoWcdma) {
                    arrayList.add(new NeighboringCellInfo((CellInfoWcdma) cellInfo));
                }
            }
            if (arrayList.size() > 0) {
                return arrayList;
            }
            return null;
        } catch (SecurityException e) {
            EventLog.writeEvent(1397638484, "190619791", Integer.valueOf(Binder.getCallingUid()));
            throw e;
        }
    }

    private List getCachedCellInfo() {
        ArrayList arrayList = new ArrayList();
        for (Phone phone : PhoneFactory.getPhones()) {
            List allCellInfo = phone.getAllCellInfo();
            if (allCellInfo != null) {
                arrayList.addAll(allCellInfo);
            }
        }
        return arrayList;
    }

    public List getAllCellInfo(String str, String str2) {
        int callingUid = Binder.getCallingUid();
        log("getAllCellInfo: package=" + str + ", uid=" + Binder.getCallingUid());
        ((AppOpsManager) this.mApp.getSystemService(AppOpsManager.class)).checkPackage(Binder.getCallingUid(), str);
        int i = AnonymousClass5.$SwitchMap$android$telephony$LocationAccessPolicy$LocationPermissionResult[LocationAccessPolicy.checkLocationPermission(this.mApp, new LocationAccessPolicy.LocationPermissionQuery.Builder().setCallingPackage(str).setCallingFeatureId(str2).setCallingPid(Binder.getCallingPid()).setCallingUid(Binder.getCallingUid()).setMethod("getAllCellInfo").setMinSdkVersionForCoarse(1).setMinSdkVersionForFine(29).build()).ordinal()];
        if (i == 1) {
            ImsRcsController$$ExternalSyntheticBUOutline0.m("Not allowed to access cell info");
            return null;
        }
        if (i != 2) {
            enforceTelephonyFeatureWithException(str, "android.hardware.telephony.radio.access", "getAllCellInfo");
            AppOpsManager appOpsManager = (AppOpsManager) getDefaultPhone().getContext().getSystemService(AppOpsManager.class);
            Objects.requireNonNull(appOpsManager);
            this.mAppOps = appOpsManager;
            appOpsManager.noteOpNoThrow(166, Binder.getCallingUid(), str, str2, "getAllCellInfo reporting cell info");
            if (TelephonyPermissions.getTargetSdk(this.mApp, str) >= 29) {
                return MiPhoneAppStub.get().getBlurCellInfo(this.mApp, this.mAppOps, getCachedCellInfo(), str, callingUid);
            }
            WorkSource workSource = getWorkSource(Binder.getCallingUid());
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                ArrayList arrayList = new ArrayList();
                for (Phone phone : PhoneFactory.getPhones()) {
                    List list = (List) sendRequest(60, (Object) null, phone, workSource);
                    if (list != null) {
                        arrayList.addAll(list);
                    }
                }
                return MiPhoneAppStub.get().getBlurCellInfo(this.mApp, this.mAppOps, arrayList, str, callingUid);
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }
        return new ArrayList();
    }

    public void requestCellInfoUpdate(int i, ICellInfoCallback iCellInfoCallback, String str, String str2) {
        requestCellInfoUpdateInternal(i, iCellInfoCallback, str, str2, getWorkSource(Binder.getCallingUid()));
    }

    public void requestCellInfoUpdateWithWorkSource(int i, ICellInfoCallback iCellInfoCallback, String str, String str2, WorkSource workSource) {
        enforceModifyPermission();
        requestCellInfoUpdateInternal(i, iCellInfoCallback, str, str2, workSource);
    }

    private void requestCellInfoUpdateInternal(int i, ICellInfoCallback iCellInfoCallback, String str, String str2, WorkSource workSource) {
        ((AppOpsManager) this.mApp.getSystemService(AppOpsManager.class)).checkPackage(Binder.getCallingUid(), str);
        int i2 = AnonymousClass5.$SwitchMap$android$telephony$LocationAccessPolicy$LocationPermissionResult[LocationAccessPolicy.checkLocationPermission(this.mApp, new LocationAccessPolicy.LocationPermissionQuery.Builder().setCallingPackage(str).setCallingFeatureId(str2).setCallingPid(Binder.getCallingPid()).setCallingUid(Binder.getCallingUid()).setMethod("requestCellInfoUpdate").setMinSdkVersionForCoarse(1).setMinSdkVersionForFine(1).build()).ordinal()];
        if (i2 == 1) {
            if (TelephonyPermissions.getTargetSdk(this.mApp, str) < 29) {
                EventLog.writeEvent(1397638484, "154934934", Integer.valueOf(Binder.getCallingUid()));
            }
            ImsRcsController$$ExternalSyntheticBUOutline0.m("Not allowed to access cell info");
            return;
        }
        if (i2 == 2) {
            if (TelephonyPermissions.getTargetSdk(this.mApp, str) < 29) {
                EventLog.writeEvent(1397638484, "154934934", Integer.valueOf(Binder.getCallingUid()));
            }
            try {
                iCellInfoCallback.onCellInfo(new ArrayList());
                return;
            } catch (RemoteException unused) {
                return;
            }
        }
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.radio.access", "requestCellInfoUpdateInternal");
        AppOpsManager appOpsManager = (AppOpsManager) getDefaultPhone().getContext().getSystemService(AppOpsManager.class);
        Objects.requireNonNull(appOpsManager);
        this.mAppOps = appOpsManager;
        appOpsManager.noteOpNoThrow(166, Binder.getCallingUid(), str, str2, "requestCellInfoUpdate reporting cell info");
        Phone phoneFromSubId = getPhoneFromSubId(i);
        if (phoneFromSubId == null) {
            HexString$$ExternalSyntheticBUOutline1.m("Invalid Subscription Id: ", i);
        } else {
            sendRequestAsync(66, iCellInfoCallback, phoneFromSubId, workSource, Binder.getCallingUid(), str);
        }
    }

    public void setCellInfoListRate(int i, int i2) {
        enforceModifyPermission();
        WorkSource workSource = getWorkSource(Binder.getCallingUid());
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i2);
            if (phone == null) {
                getDefaultPhone().setCellInfoListRate(i, workSource);
            } else {
                phone.setCellInfoListRate(i, workSource);
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String getImeiForSlot(int i, String str, String str2) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            return null;
        }
        int subId = phone.getSubId();
        enforceCallingPackage(str, Binder.getCallingUid(), "getImeiForSlot");
        if (!TelephonyPermissions.checkCallingOrSelfReadDeviceIdentifiers(this.mApp, subId, str, str2, "getImeiForSlot")) {
            return null;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return phone.getImei();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String getMeidForSlot(int i, String str, String str2) {
        try {
            this.mAppOps.checkPackage(Binder.getCallingUid(), str);
            Phone phone = PhoneFactory.getPhone(i);
            if (phone == null) {
                Log.w("PhoneInterfaceManager", "getMeidForSlot: no phone for slot " + i);
                return null;
            }
            if (!TelephonyPermissions.checkCallingOrSelfReadDeviceIdentifiers(this.mApp, phone.getSubId(), str, str2, "getMeidForSlot")) {
                Log.w("PhoneInterfaceManager", "getMeidForSlot: identifier access denied for " + str);
                return null;
            }
            enforceTelephonyFeatureWithException(str, "android.hardware.telephony.cdma", "getMeidForSlot");
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                String meidForSlot = MiuiTelephonyManagerStub.get().getMeidForSlot(i);
                if (TextUtils.isEmpty(meidForSlot)) {
                    Log.w("PhoneInterfaceManager", "getMeidForSlot: MEID unavailable for slot " + i);
                    return null;
                }
                Log.d("PhoneInterfaceManager", "getMeidForSlot: MEID available for slot " + i);
                return meidForSlot;
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        } catch (SecurityException unused) {
            EventLog.writeEvent(1397638484, "186530496", Integer.valueOf(Binder.getCallingUid()));
            PhoneInterfaceManager$$ExternalSyntheticBUOutline0.m(str, Binder.getCallingUid());
            return null;
        }
    }

    public String getPrimaryImei(String str, String str2) {
        enforceCallingPackage(str, Binder.getCallingUid(), "getPrimaryImei");
        if (!checkCallingOrSelfReadDeviceIdentifiersForAnySub(this.mApp, str, str2, "getPrimaryImei")) {
            ImsRcsController$$ExternalSyntheticBUOutline0.m("Caller does not have permission");
            return null;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            for (Phone phone : PhoneFactory.getPhones()) {
                if (phone.getImeiType() == 1) {
                    String imei = phone.getImei();
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                    return imei;
                }
            }
            throw new UnsupportedOperationException("Operation not supported");
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public String getTypeAllocationCodeForSlot(int i) {
        String imei;
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null || (imei = phone.getImei()) == null) {
            return null;
        }
        try {
            return imei.substring(0, 8);
        } catch (IndexOutOfBoundsException unused) {
            Log.e("PhoneInterfaceManager", "IMEI length shorter than upper index.");
            return null;
        }
    }

    public String getDeviceSoftwareVersionForSlot(int i, String str, String str2) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            return null;
        }
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mApp, phone.getSubId(), str, str2, "getDeviceSoftwareVersionForSlot")) {
            return null;
        }
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony", "getDeviceSoftwareVersionForSlot");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return phone.getDeviceSvn();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getSubscriptionCarrierId(int i) {
        if (!this.mApp.getResources().getBoolean(android.R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "getSubscriptionCarrierId");
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            return phone == null ? -1 : phone.getCarrierId();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String getSubscriptionCarrierName(int i) {
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "getSubscriptionCarrierName");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            return phone == null ? null : phone.getCarrierName();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getSubscriptionSpecificCarrierId(int i) {
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "getSubscriptionSpecificCarrierId");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            return phone == null ? -1 : phone.getSpecificCarrierId();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String getSubscriptionSpecificCarrierName(int i) {
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "getSubscriptionSpecificCarrierName");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            return phone == null ? null : phone.getSpecificCarrierName();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getCarrierIdFromMccMnc(int i, String str, boolean z) {
        if (!z) {
            enforceReadPrivilegedPermission("getCarrierIdFromMccMnc");
        }
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            return -1;
        }
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "getCarrierIdFromMccMnc");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return CarrierResolver.getCarrierIdFromMccMnc(phone.getContext(), str);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private void enforceCallingPackage(String str, int i, String str2) {
        int packageUid;
        PackageManager packageManager = this.mApp.getBaseContext().createContextAsUser(UserHandle.getUserHandleForUid(i), 0).getPackageManager();
        try {
            packageUid = packageManager.getPackageUid(str, 0);
        } catch (PackageManager.NameNotFoundException unused) {
            packageUid = -1;
        }
        if (PccAwareUidComparator.isSameApp(packageManager, packageUid, i)) {
            return;
        }
        throw new SecurityException(str2 + ": Package " + str + " does not belong to " + i);
    }

    public void enforceModifyPermission() {
        this.mApp.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", null);
    }

    public void enforceReadPermission() {
        enforceReadPermission(null);
    }

    public void enforceReadPermission(String str) {
        this.mApp.enforceCallingOrSelfPermission("android.permission.READ_PHONE_STATE", str);
    }

    private void enforceActiveEmergencySessionPermission() {
        this.mApp.enforceCallingOrSelfPermission("android.permission.READ_ACTIVE_EMERGENCY_SESSION", null);
    }

    private void enforceCallPermission() {
        if (MiPhoneAppStub.get().enforceCallPermission()) {
            return;
        }
        this.mApp.enforceCallingOrSelfPermission("android.permission.CALL_PHONE", null);
    }

    private void enforceSettingsPermission() {
        this.mApp.enforceCallingOrSelfPermission("android.permission.NETWORK_SETTINGS", null);
    }

    private void enforceRebootPermission() {
        this.mApp.enforceCallingOrSelfPermission("android.permission.REBOOT", null);
    }

    private void enforceSatelliteCommunicationPermission(String str) {
        this.mApp.enforceCallingOrSelfPermission("android.permission.SATELLITE_COMMUNICATION", str);
    }

    private void enforcePackageUsageStatsPermission(String str) {
        this.mApp.enforceCallingOrSelfPermission("android.permission.PACKAGE_USAGE_STATS", str);
    }

    private String createTelUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return "tel:" + str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("PhoneInterfaceManager", str);
    }

    private static void logv(String str) {
        Log.v("PhoneInterfaceManager", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loge(String str) {
        Log.e("PhoneInterfaceManager", str);
    }

    public void requestNumberVerification(PhoneNumberRange phoneNumberRange, long j, INumberVerificationCallback iNumberVerificationCallback, String str) {
        if (this.mApp.checkCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE") != 0) {
            ImsRcsController$$ExternalSyntheticBUOutline0.m("Caller must hold the MODIFY_PHONE_STATE permission");
            return;
        }
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        String authorizedPackage = NumberVerificationManager.getAuthorizedPackage(this.mApp);
        if (!TextUtils.equals(str, authorizedPackage)) {
            throw new SecurityException("Calling package must be configured in the device config: calling package: " + str + ", configured package: " + authorizedPackage);
        }
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.calling", "requestNumberVerification");
        if (phoneNumberRange == null) {
            GenericObject$$ExternalSyntheticBUOutline0.m("Range must be non-null");
        } else {
            NumberVerificationManager.getInstance().requestVerification(phoneNumberRange, iNumberVerificationCallback, Math.min(j, TelephonyManager.getMaxNumberVerificationTimeoutMillis()));
        }
    }

    public boolean setVoiceMailNumber(int i, String str, String str2) {
        TelephonyPermissions.enforceCallingOrSelfCarrierPrivilege(this.mApp, i, "setVoiceMailNumber");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.calling", "setVoiceMailNumber");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Boolean bool = (Boolean) sendRequest(33, new Pair(str, str2), new Integer(i), 2000L);
            if (bool != null) {
                return bool.booleanValue();
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public Bundle getVisualVoicemailSettings(String str, int i) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        if (!TextUtils.equals(str, ((TelecomManager) this.mApp.getSystemService(TelecomManager.class)).getSystemDialerPackage())) {
            ImsRcsController$$ExternalSyntheticBUOutline0.m("caller must be system dialer");
            return null;
        }
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.calling", "getVisualVoicemailSettings");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            PhoneAccountHandle phoneAccountHandleFromSubId = PhoneAccountHandleConverter.fromSubId(i);
            if (phoneAccountHandleFromSubId == null) {
                return null;
            }
            return VisualVoicemailSettingsUtil.dump(this.mApp, phoneAccountHandleFromSubId);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String getVisualVoicemailPackageName(String str, String str2, int i) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mApp, i, str, str2, "getVisualVoicemailPackageName")) {
            return null;
        }
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.calling", "getVisualVoicemailPackageName");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ComponentName remotePackage = RemoteVvmTaskManager.getRemotePackage(this.mApp, i);
            return remotePackage != null ? remotePackage.getPackageName() : null;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void enableVisualVoicemailSmsFilter(String str, int i, VisualVoicemailSmsFilterSettings visualVoicemailSmsFilterSettings) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.calling", "enableVisualVoicemailSmsFilter");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            VisualVoicemailSmsFilterConfig.enableVisualVoicemailSmsFilter(this.mApp, str, i, visualVoicemailSmsFilterSettings);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void disableVisualVoicemailSmsFilter(String str, int i) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.calling", "disableVisualVoicemailSmsFilter");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            VisualVoicemailSmsFilterConfig.disableVisualVoicemailSmsFilter(this.mApp, str, i);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public VisualVoicemailSmsFilterSettings getVisualVoicemailSmsFilterSettings(String str, int i) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return VisualVoicemailSmsFilterConfig.getVisualVoicemailSmsFilterSettings(this.mApp, str, i);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public VisualVoicemailSmsFilterSettings getActiveVisualVoicemailSmsFilterSettings(int i) {
        enforceReadPrivilegedPermission("getActiveVisualVoicemailSmsFilterSettings");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return VisualVoicemailSmsFilterConfig.getActiveVisualVoicemailSmsFilterSettings(this.mApp, i);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void sendVisualVoicemailSmsForSubscriber(String str, String str2, int i, String str3, int i2, String str4, PendingIntent pendingIntent) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        enforceVisualVoicemailPackage(str, i);
        enforceSendSmsPermission();
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.calling", "sendVisualVoicemailSmsForSubscriber");
        PhoneFactory.getSmsController().sendVisualVoicemailSmsForSubscriber(str, Binder.getCallingUserHandle().getIdentifier(), str2, i, str3, i2, str4, pendingIntent);
    }

    public void setVoiceActivationState(int i, int i2) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "setVoiceActivationState");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.calling", "setVoiceActivationState");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone != null) {
                phone.setVoiceActivationState(i2);
            } else {
                loge("setVoiceActivationState fails with invalid subId: " + i);
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setDataActivationState(int i, int i2) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "setDataActivationState");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.data", "setDataActivationState");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone != null) {
                phone.setDataActivationState(i2);
            } else {
                loge("setDataActivationState fails with invalid subId: " + i);
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getVoiceActivationState(int i, String str) {
        enforceReadPrivilegedPermission("getVoiceActivationState");
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.calling", "getVoiceActivationState");
        Phone phone = getPhone(i);
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        if (phone != null) {
            try {
                return phone.getVoiceActivationState();
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }
        Binder.restoreCallingIdentity(jClearCallingIdentity);
        return 0;
    }

    public int getDataActivationState(int i, String str) {
        enforceReadPrivilegedPermission("getDataActivationState");
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.data", "getDataActivationState");
        Phone phone = getPhone(i);
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        if (phone != null) {
            try {
                return phone.getDataActivationState();
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }
        Binder.restoreCallingIdentity(jClearCallingIdentity);
        return 0;
    }

    public int getVoiceMessageCountForSubscriber(int i, String str, String str2) {
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mApp, i, str, str2, "getVoiceMessageCountForSubscriber")) {
            return 0;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone != null) {
                return phone.getVoiceMessageCount();
            }
            return 0;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isConcurrentVoiceAndDataAllowed(int i) {
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.data", "isConcurrentVoiceAndDataAllowed");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return getPhoneFromSubIdOrDefault(i).isConcurrentVoiceAndDataAllowed();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void sendDialerSpecialCode(String str, String str2) {
        Phone defaultPhone = getDefaultPhone();
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        if (!TextUtils.equals(str, ((TelecomManager) defaultPhone.getContext().getSystemService(TelecomManager.class)).getDefaultDialerPackage())) {
            TelephonyPermissions.enforceCallingOrSelfCarrierPrivilege(this.mApp, getDefaultSubscription(), "sendDialerSpecialCode");
        }
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.calling", "sendDialerSpecialCode");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            defaultPhone.sendDialerSpecialCode(str2);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getNetworkSelectionMode(int i) {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "getNetworkSelectionMode");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.radio.access", "getNetworkSelectionMode");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (isActiveSubscription(i)) {
                return ((Integer) sendRequest(52, (Object) null, Integer.valueOf(i))).intValue();
            }
            return 0;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isInEmergencySmsMode() {
        enforceReadPrivilegedPermission("isInEmergencySmsMode");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.messaging", "isInEmergencySmsMode");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            for (Phone phone : PhoneFactory.getPhones()) {
                if (phone.isInEmergencySmsMode()) {
                    return true;
                }
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public void registerImsRegistrationCallback(int i, IImsRegistrationCallback iImsRegistrationCallback) throws ServiceSpecificException {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "registerImsRegistrationCallback");
        if (!ImsManager.isImsSupportedOnDevice(this.mApp)) {
            throw new ServiceSpecificException(2, "IMS not available on device.");
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                verifyImsMmTelConfiguredOrThrow(getSlotIndexOrException(i));
                ImsStateCallbackController imsStateCallbackController = ImsStateCallbackController.getInstance();
                if (imsStateCallbackController != null) {
                    ImsManager imsManager = imsStateCallbackController.getImsManager(i);
                    if (imsManager != null) {
                        imsManager.addRegistrationCallbackForSubscription(iImsRegistrationCallback, i);
                        Binder.restoreCallingIdentity(jClearCallingIdentity);
                        return;
                    }
                    throw new ServiceSpecificException(1);
                }
                throw new ServiceSpecificException(2);
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void unregisterImsRegistrationCallback(int i, IImsRegistrationCallback iImsRegistrationCallback) {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "unregisterImsRegistrationCallback");
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            HexString$$ExternalSyntheticBUOutline1.m("Invalid Subscription ID: ", i);
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ImsStateCallbackController imsStateCallbackController = ImsStateCallbackController.getInstance();
            if (imsStateCallbackController != null) {
                ImsManager imsManager = imsStateCallbackController.getImsManager(i);
                if (imsManager == null) {
                    Log.i("PhoneInterfaceManager", "unregisterImsRegistrationCallback: " + i + "is inactive, ignoring unregister.");
                } else {
                    imsManager.removeRegistrationCallbackForSubscription(iImsRegistrationCallback, i);
                }
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public void registerImsEmergencyRegistrationCallback(int i, IImsRegistrationCallback iImsRegistrationCallback) throws ServiceSpecificException {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "registerImsEmergencyRegistrationCallback");
        if (!ImsManager.isImsSupportedOnDevice(this.mApp)) {
            throw new ServiceSpecificException(2, "IMS not available on device.");
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                verifyImsMmTelConfiguredOrThrow(getSlotIndexOrException(i));
                ImsStateCallbackController imsStateCallbackController = ImsStateCallbackController.getInstance();
                if (imsStateCallbackController != null) {
                    ImsManager imsManager = imsStateCallbackController.getImsManager(i);
                    if (imsManager != null) {
                        imsManager.addEmergencyRegistrationCallbackForSubscription(iImsRegistrationCallback, i);
                        Binder.restoreCallingIdentity(jClearCallingIdentity);
                        return;
                    }
                    throw new ServiceSpecificException(1);
                }
                throw new ServiceSpecificException(2);
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void unregisterImsEmergencyRegistrationCallback(int i, IImsRegistrationCallback iImsRegistrationCallback) {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "unregisterImsEmergencyRegistrationCallback");
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            HexString$$ExternalSyntheticBUOutline1.m("Invalid Subscription ID: ", i);
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ImsStateCallbackController imsStateCallbackController = ImsStateCallbackController.getInstance();
            if (imsStateCallbackController != null) {
                ImsManager imsManager = imsStateCallbackController.getImsManager(i);
                if (imsManager == null) {
                    Log.i("PhoneInterfaceManager", "unregisterImsEmergencyRegistrationCallback: " + i + "is inactive, ignoring unregister.");
                } else {
                    imsManager.removeEmergencyRegistrationCallbackForSubscription(iImsRegistrationCallback, i);
                }
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public void getImsMmTelRegistrationState(int i, final IIntegerConsumer iIntegerConsumer) throws ServiceSpecificException {
        enforceReadPrivilegedPermission("getImsMmTelRegistrationState");
        if (!ImsManager.isImsSupportedOnDevice(this.mApp)) {
            throw new ServiceSpecificException(2, "IMS not available on device.");
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone == null) {
                Log.w("PhoneInterfaceManager", "getImsMmTelRegistrationState: called with an invalid subscription '" + i + "'");
                throw new ServiceSpecificException(3);
            }
            phone.getImsRegistrationState(new Consumer() { // from class: com.android.phone.PhoneInterfaceManager$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    PhoneInterfaceManager.$r8$lambda$80PBl3p0xCgvpdPDeGOKl2yTfLo(iIntegerConsumer, (Integer) obj);
                }
            });
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public static /* synthetic */ void $r8$lambda$80PBl3p0xCgvpdPDeGOKl2yTfLo(IIntegerConsumer iIntegerConsumer, Integer num) {
        int iIntValue;
        if (num == null) {
            iIntValue = 0;
        } else {
            try {
                iIntValue = num.intValue();
            } catch (RemoteException unused) {
                Log.w("PhoneInterfaceManager", "getImsMmTelRegistrationState: callback not available.");
                return;
            }
        }
        iIntegerConsumer.accept(iIntValue);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public void getImsMmTelRegistrationTransportType(int i, final IIntegerConsumer iIntegerConsumer) throws ServiceSpecificException {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "getImsMmTelRegistrationTransportType");
        if (!ImsManager.isImsSupportedOnDevice(this.mApp)) {
            throw new ServiceSpecificException(2, "IMS not available on device.");
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone == null) {
                Log.w("PhoneInterfaceManager", "getImsMmTelRegistrationState: called with an invalid subscription '" + i + "'");
                throw new ServiceSpecificException(3);
            }
            phone.getImsRegistrationTech(new Consumer() { // from class: com.android.phone.PhoneInterfaceManager$$ExternalSyntheticLambda9
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    PhoneInterfaceManager.$r8$lambda$u4h9ExzL6fazceNx46W42nyk9qw(iIntegerConsumer, (Integer) obj);
                }
            });
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public static /* synthetic */ void $r8$lambda$u4h9ExzL6fazceNx46W42nyk9qw(IIntegerConsumer iIntegerConsumer, Integer num) {
        try {
            iIntegerConsumer.accept(((Integer) RegistrationManager.IMS_REG_TO_ACCESS_TYPE_MAP.get(Integer.valueOf(num == null ? -1 : num.intValue()))).intValue());
        } catch (RemoteException unused) {
            Log.w("PhoneInterfaceManager", "getImsMmTelRegistrationState: callback not available.");
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public void registerMmTelCapabilityCallback(int i, IImsCapabilityCallback iImsCapabilityCallback) throws ServiceSpecificException {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "registerMmTelCapabilityCallback");
        if (!ImsManager.isImsSupportedOnDevice(this.mApp)) {
            throw new ServiceSpecificException(2, "IMS not available on device.");
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                verifyImsMmTelConfiguredOrThrow(getSlotIndexOrException(i));
                ImsStateCallbackController imsStateCallbackController = ImsStateCallbackController.getInstance();
                if (imsStateCallbackController != null) {
                    ImsManager imsManager = imsStateCallbackController.getImsManager(i);
                    if (imsManager != null) {
                        imsManager.addCapabilitiesCallbackForSubscription(iImsCapabilityCallback, i);
                        Binder.restoreCallingIdentity(jClearCallingIdentity);
                        return;
                    }
                    throw new ServiceSpecificException(1);
                }
                throw new ServiceSpecificException(2);
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void unregisterMmTelCapabilityCallback(int i, IImsCapabilityCallback iImsCapabilityCallback) {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "unregisterMmTelCapabilityCallback");
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            HexString$$ExternalSyntheticBUOutline1.m("Invalid Subscription ID: ", i);
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ImsStateCallbackController imsStateCallbackController = ImsStateCallbackController.getInstance();
            if (imsStateCallbackController != null) {
                ImsManager imsManager = imsStateCallbackController.getImsManager(i);
                if (imsManager == null) {
                    Log.i("PhoneInterfaceManager", "unregisterMmTelCapabilityCallback: " + i + " is inactive, ignoring unregister.");
                } else {
                    imsManager.removeCapabilitiesCallbackForSubscription(iImsCapabilityCallback, i);
                }
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isCapable(int i, int i2, int i3) {
        enforceReadPrivilegedPermission("isCapable");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "isCapable");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            int slotIndexOrException = getSlotIndexOrException(i);
            verifyImsMmTelConfiguredOrThrow(slotIndexOrException);
            return ImsManager.getInstance(this.mApp, slotIndexOrException).queryMmTelCapability(i2, i3);
        } catch (ImsException unused) {
            Log.i("PhoneInterfaceManager", "isCapable: " + i + " is inactive, returning false.");
            return false;
        } catch (com.android.ims.ImsException e) {
            Log.w("PhoneInterfaceManager", "IMS isCapable - service unavailable: " + e.getMessage());
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isAvailable(int i, int i2, int i3) {
        enforceReadPrivilegedPermission("isAvailable");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "isAvailable");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone == null) {
                return false;
            }
            return phone.isImsCapabilityAvailable(i2, i3);
        } catch (com.android.ims.ImsException e) {
            Log.w("PhoneInterfaceManager", "IMS isAvailable - service unavailable: " + e.getMessage());
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
        Binder.restoreCallingIdentity(jClearCallingIdentity);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public void isMmTelCapabilitySupported(int i, final IIntegerConsumer iIntegerConsumer, int i2, int i3) throws ServiceSpecificException {
        enforceReadPrivilegedPermission("isMmTelCapabilitySupported");
        if (!ImsManager.isImsSupportedOnDevice(this.mApp)) {
            throw new ServiceSpecificException(2, "IMS not available on device.");
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                int slotIndex = getSlotIndex(i);
                if (slotIndex <= -1) {
                    Log.w("PhoneInterfaceManager", "isMmTelCapabilitySupported: called with an inactive subscription '" + i + "'");
                    throw new ServiceSpecificException(3);
                }
                verifyImsMmTelConfiguredOrThrow(slotIndex);
                ImsManager.getInstance(this.mApp, slotIndex).isSupported(i2, i3, new Consumer() { // from class: com.android.phone.PhoneInterfaceManager$$ExternalSyntheticLambda7
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        PhoneInterfaceManager.$r8$lambda$ez6xQeppCi4_zgwVwOS0YtEXjYM(iIntegerConsumer, (Boolean) obj);
                    }
                });
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public static /* synthetic */ void $r8$lambda$ez6xQeppCi4_zgwVwOS0YtEXjYM(IIntegerConsumer iIntegerConsumer, Boolean bool) {
        int i = 0;
        if (bool != null) {
            try {
                if (bool.booleanValue()) {
                    i = 1;
                }
            } catch (RemoteException unused) {
                Log.w("PhoneInterfaceManager", "isMmTelCapabilitySupported: remote caller is not running. Ignore");
                return;
            }
        }
        iIntegerConsumer.accept(i);
    }

    public boolean isAdvancedCallingSettingEnabled(int i) {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "isAdvancedCallingSettingEnabled");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "isAdvancedCallingSettingEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                boolean zIsEnhanced4gLteModeSettingEnabledByUser = ImsManager.getInstance(this.mApp, getSlotIndexOrException(i)).isEnhanced4gLteModeSettingEnabledByUser();
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return zIsEnhanced4gLteModeSettingEnabledByUser;
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void setAdvancedCallingSettingEnabled(int i, boolean z) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "setAdvancedCallingSettingEnabled");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "setAdvancedCallingSettingEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                ImsManager.getInstance(this.mApp, getSlotIndexOrException(i)).setEnhanced4gLteModeSetting(z);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public boolean isVtSettingEnabled(int i) {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "isVtSettingEnabled");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "isVtSettingEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                boolean zIsVtEnabledByUser = ImsManager.getInstance(this.mApp, getSlotIndexOrException(i)).isVtEnabledByUser();
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return zIsVtEnabledByUser;
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void setVtSettingEnabled(int i, boolean z) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "setVtSettingEnabled");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "setVtSettingEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                ImsManager.getInstance(this.mApp, getSlotIndexOrException(i)).setVtSetting(z);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public boolean isVoWiFiSettingEnabled(int i) {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "isVoWiFiSettingEnabled");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "isVoWiFiSettingEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                boolean zIsWfcEnabledByUser = ImsManager.getInstance(this.mApp, getSlotIndexOrException(i)).isWfcEnabledByUser();
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return zIsWfcEnabledByUser;
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void setVoWiFiSettingEnabled(int i, boolean z) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "setVoWiFiSettingEnabled");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "setVoWiFiSettingEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                ImsManager.getInstance(this.mApp, getSlotIndexOrException(i)).setWfcSetting(z);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public boolean isCrossSimCallingEnabledByUser(int i) {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "isCrossSimCallingEnabledByUser");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "isCrossSimCallingEnabledByUser");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                boolean zIsCrossSimCallingEnabledByUser = ImsManager.getInstance(this.mApp, getSlotIndexOrException(i)).isCrossSimCallingEnabledByUser();
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return zIsCrossSimCallingEnabledByUser;
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void setCrossSimCallingEnabled(int i, boolean z) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "setCrossSimCallingEnabled");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "setCrossSimCallingEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                ImsManager.getInstance(this.mApp, getSlotIndexOrException(i)).setCrossSimCallingEnabled(z);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public boolean isVoWiFiRoamingSettingEnabled(int i) {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "isVoWiFiRoamingSettingEnabled");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "isVoWiFiRoamingSettingEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                boolean zIsWfcRoamingEnabledByUser = ImsManager.getInstance(this.mApp, getSlotIndexOrException(i)).isWfcRoamingEnabledByUser();
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return zIsWfcRoamingEnabledByUser;
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void setVoWiFiRoamingSettingEnabled(int i, boolean z) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "setVoWiFiRoamingSettingEnabled");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "setVoWiFiRoamingSettingEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                ImsManager.getInstance(this.mApp, getSlotIndexOrException(i)).setWfcRoamingSetting(z);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void setVoWiFiNonPersistent(int i, boolean z, int i2) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "setVoWiFiNonPersistent");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "setVoWiFiNonPersistent");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                ImsManager.getInstance(this.mApp, getSlotIndexOrException(i)).setWfcNonPersistent(z, i2);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public int getVoWiFiModeSetting(int i) {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "getVoWiFiModeSetting");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "getVoWiFiModeSetting");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                int wfcMode = ImsManager.getInstance(this.mApp, getSlotIndexOrException(i)).getWfcMode(false);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return wfcMode;
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void setVoWiFiModeSetting(int i, int i2) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "setVoWiFiModeSetting");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "setVoWiFiModeSetting");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                ImsManager.getInstance(this.mApp, getSlotIndexOrException(i)).setWfcMode(i2, false);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public int getVoWiFiRoamingModeSetting(int i) {
        enforceReadPrivilegedPermission("getVoWiFiRoamingModeSetting");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "getVoWiFiRoamingModeSetting");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                int wfcMode = ImsManager.getInstance(this.mApp, getSlotIndexOrException(i)).getWfcMode(true);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return wfcMode;
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void setVoWiFiRoamingModeSetting(int i, int i2) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "setVoWiFiRoamingModeSetting");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "setVoWiFiRoamingModeSetting");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                ImsManager.getInstance(this.mApp, getSlotIndexOrException(i)).setWfcMode(i2, true);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void setRttCapabilitySetting(int i, boolean z) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "setRttCapabilityEnabled");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "setRttCapabilitySetting");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                ImsManager.getInstance(this.mApp, getSlotIndexOrException(i)).setRttEnabled(z);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public boolean isTtyOverVolteEnabled(int i) {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "isTtyOverVolteEnabled");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "isTtyOverVolteEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                boolean zIsTtyOnVoLteCapable = ImsManager.getInstance(this.mApp, getSlotIndexOrException(i)).isTtyOnVoLteCapable();
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return zIsTtyOnVoLteCapable;
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void registerImsProvisioningChangedCallback(int i, IImsConfigCallback iImsConfigCallback) {
        enforceReadPrivilegedPermission("registerImsProvisioningChangedCallback");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                if (!isImsAvailableOnDevice()) {
                    throw new ServiceSpecificException(2, "IMS not available on device.");
                }
                verifyImsMmTelConfiguredOrThrow(getSlotIndexOrException(i));
                ImsStateCallbackController imsStateCallbackController = ImsStateCallbackController.getInstance();
                if (imsStateCallbackController != null) {
                    ImsManager imsManager = imsStateCallbackController.getImsManager(i);
                    if (imsManager != null) {
                        imsManager.addProvisioningCallbackForSubscription(iImsConfigCallback, i);
                        Binder.restoreCallingIdentity(jClearCallingIdentity);
                        return;
                    }
                    throw new ServiceSpecificException(1);
                }
                throw new ServiceSpecificException(2);
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void unregisterImsProvisioningChangedCallback(int i, IImsConfigCallback iImsConfigCallback) {
        enforceReadPrivilegedPermission("unregisterImsProvisioningChangedCallback");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            HexString$$ExternalSyntheticBUOutline1.m("Invalid Subscription ID: ", i);
            return;
        }
        try {
            ImsStateCallbackController imsStateCallbackController = ImsStateCallbackController.getInstance();
            if (imsStateCallbackController != null) {
                ImsManager imsManager = imsStateCallbackController.getImsManager(i);
                if (imsManager == null) {
                    Log.i("PhoneInterfaceManager", "unregisterImsProvisioningChangedCallback: " + i + " is inactive, ignoring unregister.");
                } else {
                    imsManager.removeProvisioningCallbackForSubscription(iImsConfigCallback, i);
                }
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void registerFeatureProvisioningChangedCallback(int i, IFeatureProvisioningCallback iFeatureProvisioningCallback) {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "registerFeatureProvisioningChangedCallback");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            HexString$$ExternalSyntheticBUOutline1.m("Invalid Subscription ID: ", i);
            return;
        }
        try {
            ImsProvisioningController imsProvisioningController = ImsProvisioningController.getInstance();
            if (imsProvisioningController == null) {
                throw new ServiceSpecificException(2, "Device does not support IMS");
            }
            imsProvisioningController.addFeatureProvisioningChangedCallback(i, iFeatureProvisioningCallback);
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void unregisterFeatureProvisioningChangedCallback(int i, IFeatureProvisioningCallback iFeatureProvisioningCallback) {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "unregisterFeatureProvisioningChangedCallback");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            HexString$$ExternalSyntheticBUOutline1.m("Invalid Subscription ID: ", i);
            return;
        }
        try {
            ImsProvisioningController imsProvisioningController = ImsProvisioningController.getInstance();
            if (imsProvisioningController == null) {
                loge("unregisterFeatureProvisioningChangedCallback: Device does not support IMS");
            } else {
                imsProvisioningController.removeFeatureProvisioningChangedCallback(i, iFeatureProvisioningCallback);
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private void checkModifyPhoneStatePermission(int i, String str) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, str);
    }

    public void setRcsProvisioningStatusForCapability(int i, int i2, int i3, boolean z) {
        checkModifyPhoneStatePermission(i, "setRcsProvisioningStatusForCapability");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "setRcsProvisioningStatusForCapability");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ImsProvisioningController imsProvisioningController = ImsProvisioningController.getInstance();
            if (imsProvisioningController == null) {
                loge("setRcsProvisioningStatusForCapability: Device does not support IMS");
            } else {
                imsProvisioningController.setRcsProvisioningStatusForCapability(i, i2, i3, z);
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean getRcsProvisioningStatusForCapability(int i, int i2, int i3) {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "getRcsProvisioningStatusForCapability");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "getRcsProvisioningStatusForCapability");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ImsProvisioningController imsProvisioningController = ImsProvisioningController.getInstance();
            if (imsProvisioningController == null) {
                loge("getRcsProvisioningStatusForCapability: Device does not support IMS");
                return true;
            }
            return imsProvisioningController.getRcsProvisioningStatusForCapability(i, i2, i3);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setImsProvisioningStatusForCapability(int i, int i2, int i3, boolean z) {
        checkModifyPhoneStatePermission(i, "setImsProvisioningStatusForCapability");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "setImsProvisioningStatusForCapability");
        String currentPackageNameOrPhone = getCurrentPackageNameOrPhone();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ImsProvisioningController imsProvisioningController = ImsProvisioningController.getInstance();
            if (imsProvisioningController == null) {
                loge("setImsProvisioningStatusForCapability: Device does not support IMS");
            } else {
                imsProvisioningController.setImsProvisioningStatusForCapability(currentPackageNameOrPhone, i, i2, i3, z);
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean getImsProvisioningStatusForCapability(int i, int i2, int i3) {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "getProvisioningStatusForCapability");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "getImsProvisioningStatusForCapability");
        String currentPackageNameOrPhone = getCurrentPackageNameOrPhone();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ImsProvisioningController imsProvisioningController = ImsProvisioningController.getInstance();
            if (imsProvisioningController == null) {
                loge("getImsProvisioningStatusForCapability: Device does not support IMS");
                return true;
            }
            return imsProvisioningController.getImsProvisioningStatusForCapability(currentPackageNameOrPhone, i, i2, i3);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isProvisioningRequiredForCapability(int i, int i2, int i3) {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "isProvisioningRequiredForCapability");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "isProvisioningRequiredForCapability");
        if (!isImsAvailableOnDevice()) {
            Rlog.d("PhoneInterfaceManager", "isProvisioningRequiredForCapability, ims not available ");
            return false;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ImsProvisioningController imsProvisioningController = ImsProvisioningController.getInstance();
            if (imsProvisioningController == null) {
                loge("isProvisioningRequiredForCapability: Device does not support IMS");
                return false;
            }
            return imsProvisioningController.isImsProvisioningRequiredForCapability(i, i2, i3);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isRcsProvisioningRequiredForCapability(int i, int i2, int i3) {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "isProvisioningRequiredForCapability");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "isRcsProvisioningRequiredForCapability");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ImsProvisioningController imsProvisioningController = ImsProvisioningController.getInstance();
            if (imsProvisioningController == null) {
                loge("isRcsProvisioningRequiredForCapability: Device does not support IMS");
                return false;
            }
            return imsProvisioningController.isRcsProvisioningRequiredForCapability(i, i2, i3);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getImsProvisioningInt(int i, int i2) {
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            S2Support$$ExternalSyntheticBUOutline0.m("Invalid Subscription id '", i, "'");
            return 0;
        }
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "getImsProvisioningInt");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "getImsProvisioningInt");
        String currentPackageNameOrPhone = getCurrentPackageNameOrPhone();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            int slotIndex = getSlotIndex(i);
            if (slotIndex <= -1) {
                Log.w("PhoneInterfaceManager", "getImsProvisioningInt: called with an inactive subscription '" + i + "' for key:" + i2);
                return -1;
            }
            ImsProvisioningController imsProvisioningController = ImsProvisioningController.getInstance();
            if (imsProvisioningController == null) {
                loge("getImsProvisioningInt: Device does not support IMS");
                return -1;
            }
            int provisioningValue = imsProvisioningController.getProvisioningValue(currentPackageNameOrPhone, i, i2);
            return provisioningValue != -1 ? provisioningValue : ImsManager.getInstance(this.mApp, slotIndex).getConfigInt(i2);
        } catch (com.android.ims.ImsException unused) {
            Log.w("PhoneInterfaceManager", "getImsProvisioningInt: ImsService is not available for subscription '" + i + "' for key:" + i2);
            return -1;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String getImsProvisioningString(int i, int i2) {
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            S2Support$$ExternalSyntheticBUOutline0.m("Invalid Subscription id '", i, "'");
            return null;
        }
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "getImsProvisioningString");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "getImsProvisioningString");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            int slotIndex = getSlotIndex(i);
            if (slotIndex <= -1) {
                Log.w("PhoneInterfaceManager", "getImsProvisioningString: called for an inactive subscription id '" + i + "' for key:" + i2);
                return "STRING_QUERY_RESULT_ERROR_GENERIC";
            }
            return ImsManager.getInstance(this.mApp, slotIndex).getConfigString(i2);
        } catch (com.android.ims.ImsException unused) {
            Log.w("PhoneInterfaceManager", "getImsProvisioningString: ImsService is not available for sub '" + i + "' for key:" + i2);
            return "STRING_QUERY_RESULT_ERROR_NOT_READY";
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int setImsProvisioningInt(int i, int i2, int i3) {
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            S2Support$$ExternalSyntheticBUOutline0.m("Invalid Subscription id '", i, "'");
            return 0;
        }
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "setImsProvisioningInt");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "setImsProvisioningInt");
        String currentPackageNameOrPhone = getCurrentPackageNameOrPhone();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            int slotIndex = getSlotIndex(i);
            if (slotIndex <= -1) {
                Log.w("PhoneInterfaceManager", "setImsProvisioningInt: called with an inactive subscription id '" + i + "' for key:" + i2);
                return 1;
            }
            ImsProvisioningController imsProvisioningController = ImsProvisioningController.getInstance();
            if (imsProvisioningController == null) {
                loge("setImsProvisioningInt: Device does not support IMS");
                return 1;
            }
            int provisioningValue = imsProvisioningController.setProvisioningValue(currentPackageNameOrPhone, i, i2, i3);
            return provisioningValue != -1 ? provisioningValue : ImsManager.getInstance(this.mApp, slotIndex).setConfig(i2, i3);
        } catch (com.android.ims.ImsException | RemoteException e) {
            Log.w("PhoneInterfaceManager", "setImsProvisioningInt: ImsService unavailable for sub '" + i + "' for key:" + i2, e);
            return 1;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int setImsProvisioningString(int i, int i2, String str) {
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            S2Support$$ExternalSyntheticBUOutline0.m("Invalid Subscription id '", i, "'");
            return 0;
        }
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "setImsProvisioningString");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "setImsProvisioningString");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            int slotIndex = getSlotIndex(i);
            if (slotIndex <= -1) {
                Log.w("PhoneInterfaceManager", "setImsProvisioningString: called with an inactive subscription id '" + i + "' for key:" + i2);
                return 1;
            }
            return ImsManager.getInstance(this.mApp, slotIndex).setConfig(i2, str);
        } catch (com.android.ims.ImsException | RemoteException e) {
            Log.w("PhoneInterfaceManager", "setImsProvisioningString: ImsService unavailable for sub '" + i + "' for key:" + i2, e);
            return 1;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private void verifyImsMmTelConfiguredOrThrow(int i) throws ImsException {
        ImsResolver imsResolver = this.mImsResolver;
        if (imsResolver == null || !imsResolver.isImsServiceConfiguredForFeature(i, 1)) {
            throw new ImsException("This subscription does not support MMTEL over IMS", 2);
        }
    }

    private int getSlotIndexOrException(int i) throws ImsException {
        int slotIndex = SubscriptionManager.getSlotIndex(i);
        if (SubscriptionManager.isValidSlotIndex(slotIndex)) {
            return slotIndex;
        }
        throw new ImsException("Invalid Subscription Id, subId=" + i, 3);
    }

    private int getSlotIndex(int i) {
        int slotIndex = SubscriptionManager.getSlotIndex(i);
        if (SubscriptionManager.isValidSlotIndex(slotIndex)) {
            return slotIndex;
        }
        return -1;
    }

    public int getNetworkTypeForSubscriber(int i, String str, String str2) {
        try {
            this.mAppOps.checkPackage(Binder.getCallingUid(), str);
            int targetSdk = TelephonyPermissions.getTargetSdk(this.mApp, str);
            if (targetSdk > 29) {
                return getDataNetworkTypeForSubscriber(i, str, str2);
            }
            if (targetSdk == 29 && !TelephonyPermissions.checkCallingOrSelfReadPhoneStateNoThrow(this.mApp, i, str, str2, "getNetworkTypeForSubscriber")) {
                return 0;
            }
            enforceTelephonyFeatureWithException(str, "android.hardware.telephony.radio.access", "getNetworkTypeForSubscriber");
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                Phone phone = getPhone(i);
                if (phone != null) {
                    return phone.getServiceState().getDataNetworkType();
                }
                return 0;
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        } catch (SecurityException unused) {
            EventLog.writeEvent(1397638484, "186776740", Integer.valueOf(Binder.getCallingUid()));
            PhoneInterfaceManager$$ExternalSyntheticBUOutline0.m(str, Binder.getCallingUid());
            return 0;
        }
    }

    public int getDataNetworkType(String str, String str2) {
        return getDataNetworkTypeForSubscriber(SubscriptionManager.getDefaultDataSubscriptionId(), str, str2);
    }

    public int getDataNetworkTypeForSubscriber(int i, String str, String str2) {
        if (!TelephonyPermissions.checkCallingOrSelfReadNonDangerousPhoneStateNoThrow(this.mApp, "getDataNetworkTypeForSubscriber") && !TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mApp, i, str, str2, "getDataNetworkTypeForSubscriber")) {
            loge("getDataNetworkTypeForSubscriber: missing permission " + str);
            return 0;
        }
        if (!this.mApp.getResources().getBoolean(android.R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(str, "android.hardware.telephony.radio.access", "getDataNetworkTypeForSubscriber");
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone != null) {
                return phone.getServiceState().getDataNetworkType();
            }
            loge("getDataNetworkTypeForSubscriber: phone is null for sub " + i);
            return 0;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getVoiceNetworkTypeForSubscriber(int i, String str, String str2) {
        if (!TelephonyPermissions.checkCallingOrSelfReadNonDangerousPhoneStateNoThrow(this.mApp, "getVoiceNetworkTypeForSubscriber") && !TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mApp, i, str, str2, "getVoiceNetworkTypeForSubscriber")) {
            return 0;
        }
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.calling", "getVoiceNetworkTypeForSubscriber");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone != null) {
                return phone.getServiceState().getVoiceNetworkType();
            }
            return 0;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean hasIccCard() {
        return hasIccCardUsingSlotIndex(SubscriptionManager.getSlotIndex(getDefaultSubscription()));
    }

    public boolean hasIccCardUsingSlotIndex(int i) {
        if (!this.mApp.getResources().getBoolean(android.R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "hasIccCardUsingSlotIndex");
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = PhoneFactory.getPhone(i);
            if (phone != null) {
                return phone.getIccCard().hasIccCard();
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private int getDefaultSubscription() {
        return SubscriptionManager.getDefaultSubscriptionId();
    }

    private int getPreferredVoiceSubscription() {
        if (TelephonyCommonUtilsStub.get().isMiuiRom()) {
            return MiPhoneAppStub.get().getPreferredVoiceSubscription();
        }
        return SubscriptionManager.getDefaultVoiceSubscriptionId();
    }

    private boolean isActiveSubscription(int i) {
        return getSubscriptionManagerService().isActiveSubId(i, this.mApp.getOpPackageName(), this.mApp.getFeatureId());
    }

    private Phone getPhoneFromSlotPortIndexOrThrowException(int i, int i2) {
        int phoneIdFromSlotPortIndex = UiccController.getInstance().getPhoneIdFromSlotPortIndex(i, i2);
        if (phoneIdFromSlotPortIndex == -1) {
            throw new IllegalArgumentException("Given slot index: " + i + " port index: " + i2 + " does not correspond to an active phone");
        }
        return PhoneFactory.getPhone(phoneIdFromSlotPortIndex);
    }

    public IccOpenLogicalChannelResponse iccOpenLogicalChannel(IccLogicalChannelRequest iccLogicalChannelRequest) {
        Phone phoneFromValidIccLogicalChannelRequest = getPhoneFromValidIccLogicalChannelRequest(iccLogicalChannelRequest, "iccOpenLogicalChannel");
        this.mAppOps.checkPackage(Binder.getCallingUid(), iccLogicalChannelRequest.callingPackage);
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "iccOpenLogicalChannel");
        return iccOpenLogicalChannelWithPermission(phoneFromValidIccLogicalChannelRequest, iccLogicalChannelRequest);
    }

    private Phone getPhoneFromValidIccLogicalChannelRequest(IccLogicalChannelRequest iccLogicalChannelRequest, String str) {
        int i = iccLogicalChannelRequest.subId;
        if (i != -1) {
            TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, str);
            return getPhoneFromSubId(iccLogicalChannelRequest.subId);
        }
        if (iccLogicalChannelRequest.slotIndex != -1) {
            enforceModifyPermission();
            return getPhoneFromSlotPortIndexOrThrowException(iccLogicalChannelRequest.slotIndex, iccLogicalChannelRequest.portIndex);
        }
        Folme$$ExternalSyntheticBUOutline0.m("Both subId and slotIndex in request are invalid.");
        return null;
    }

    private IccOpenLogicalChannelResponse iccOpenLogicalChannelWithPermission(Phone phone, IccLogicalChannelRequest iccLogicalChannelRequest) {
        ComponentInfo componentInfoFindBestComponent;
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (TextUtils.equals("A0000005591010FFFFFFFF8900000100", iccLogicalChannelRequest.aid) && ((componentInfoFindBestComponent = EuiccConnector.findBestComponent(getDefaultPhone().getContext().getPackageManager())) == null || !TextUtils.equals(iccLogicalChannelRequest.callingPackage, componentInfoFindBestComponent.packageName))) {
                loge("The calling package is not allowed to access ISD-R.");
                throw new SecurityException("The calling package is not allowed to access ISD-R.");
            }
            IccOpenLogicalChannelResponse iccOpenLogicalChannelResponse = (IccOpenLogicalChannelResponse) sendRequest(9, iccLogicalChannelRequest, phone, (WorkSource) null);
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            return iccOpenLogicalChannelResponse;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public boolean iccCloseLogicalChannel(IccLogicalChannelRequest iccLogicalChannelRequest) {
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "iccCloseLogicalChannel");
        return iccCloseLogicalChannelWithPermission(getPhoneFromValidIccLogicalChannelRequest(iccLogicalChannelRequest, "iccCloseLogicalChannel"), iccLogicalChannelRequest);
    }

    private boolean iccCloseLogicalChannelWithPermission(Phone phone, IccLogicalChannelRequest iccLogicalChannelRequest) {
        boolean zIsChangeEnabled = CompatChanges.isChangeEnabled(208739934L, Binder.getCallingUid());
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            int i = iccLogicalChannelRequest.channel;
            if (i < 0) {
                throw new IllegalArgumentException("request.channel is less than 0");
            }
            Object objSendRequest = sendRequest(11, Integer.valueOf(i), phone, (WorkSource) null);
            Boolean bool = Boolean.FALSE;
            if (objSendRequest instanceof RuntimeException) {
                if (zIsChangeEnabled) {
                    throw ((RuntimeException) objSendRequest);
                }
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return false;
            }
            if (objSendRequest instanceof Boolean) {
                bool = (Boolean) objSendRequest;
            } else {
                loge("iccCloseLogicalChannelWithPermission: supported return type " + objSendRequest);
            }
            boolean zBooleanValue = bool.booleanValue();
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            return zBooleanValue;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public String iccTransmitApduLogicalChannel(int i, int i2, int i3, int i4, int i5, int i6, int i7, String str) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "iccTransmitApduLogicalChannel");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "iccTransmitApduLogicalChannel");
        return iccTransmitApduLogicalChannelWithPermission(getPhoneFromSubId(i), i2, i3, i4, i5, i6, i7, str);
    }

    public String iccTransmitApduLogicalChannelByPort(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, String str) {
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "iccTransmitApduLogicalChannelBySlot");
        return iccTransmitApduLogicalChannelWithPermission(getPhoneFromSlotPortIndexOrThrowException(i, i2), i3, i4, i5, i6, i7, i8, str);
    }

    private String iccTransmitApduLogicalChannelWithPermission(Phone phone, int i, int i2, int i3, int i4, int i5, int i6, String str) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        if (i > 0 && i < 256) {
            try {
                IccIoResult iccIoResult = (IccIoResult) sendRequest(7, new IccAPDUArgument(i, i2, i3, i4, i5, i6, str), phone, (WorkSource) null);
                String strSubstring = Integer.toHexString((iccIoResult.sw1 << 8) + iccIoResult.sw2 + 65536).substring(1);
                if (iccIoResult.payload != null) {
                    strSubstring = IccUtils.bytesToHexString(iccIoResult.payload) + strSubstring;
                }
                return strSubstring;
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }
        Binder.restoreCallingIdentity(jClearCallingIdentity);
        return "6881";
    }

    public String iccTransmitApduBasicChannel(int i, String str, int i2, int i3, int i4, int i5, int i6, String str2) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "iccTransmitApduBasicChannel");
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "iccTransmitApduBasicChannel");
        return iccTransmitApduBasicChannelWithPermission(getPhoneFromSubId(i), str, i2, i3, i4, i5, i6, str2);
    }

    public String iccTransmitApduBasicChannelByPort(int i, int i2, String str, int i3, int i4, int i5, int i6, int i7, String str2) {
        enforceModifyPermission();
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "iccTransmitApduBasicChannelBySlot");
        return iccTransmitApduBasicChannelWithPermission(getPhoneFromSlotPortIndexOrThrowException(i, i2), str, i3, i4, i5, i6, i7, str2);
    }

    /* JADX WARN: Code duplicated, block: B:25:0x007c A[Catch: all -> 0x003e, TRY_LEAVE, TryCatch #0 {all -> 0x003e, blocks: (B:9:0x0019, B:11:0x0023, B:13:0x0035, B:18:0x0041, B:19:0x0049, B:23:0x0053, B:25:0x007c), top: B:30:0x0019 }] */
    /* JADX WARN: Instruction removed from duplicated block: B:25:0x007c, please report this as an issue */
    private String iccTransmitApduBasicChannelWithPermission(Phone phone, String str, int i, int i2, int i3, int i4, int i5, String str2) {
        int i6;
        int i7;
        String str3;
        IccIoResult iccIoResult;
        String strSubstring;
        ComponentInfo componentInfoFindBestComponent;
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        if (i2 == 164) {
            i6 = i3;
            if (i6 == 4 && i4 == 0) {
                i7 = i5;
                if (i7 == 16) {
                    try {
                        str3 = str2;
                        if (TextUtils.equals("A0000005591010FFFFFFFF8900000100", str3) && ((componentInfoFindBestComponent = EuiccConnector.findBestComponent(getDefaultPhone().getContext().getPackageManager())) == null || !TextUtils.equals(str, componentInfoFindBestComponent.packageName))) {
                            loge("The calling package is not allowed to select ISD-R.");
                            throw new SecurityException("The calling package is not allowed to select ISD-R.");
                        }
                    } catch (Throwable th) {
                        Binder.restoreCallingIdentity(jClearCallingIdentity);
                        throw th;
                    }
                }
                iccIoResult = (IccIoResult) sendRequest(29, new IccAPDUArgument(0, i, i2, i6, i4, i7, str3), phone, (WorkSource) null);
                strSubstring = Integer.toHexString((iccIoResult.sw1 << 8) + iccIoResult.sw2 + 65536).substring(1);
                if (iccIoResult.payload != null) {
                    strSubstring = IccUtils.bytesToHexString(iccIoResult.payload) + strSubstring;
                }
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return strSubstring;
            }
            str3 = str2;
            iccIoResult = (IccIoResult) sendRequest(29, new IccAPDUArgument(0, i, i2, i6, i4, i7, str3), phone, (WorkSource) null);
            strSubstring = Integer.toHexString((iccIoResult.sw1 << 8) + iccIoResult.sw2 + 65536).substring(1);
            if (iccIoResult.payload != null) {
                strSubstring = IccUtils.bytesToHexString(iccIoResult.payload) + strSubstring;
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            return strSubstring;
        }
        i6 = i3;
        i7 = i5;
        str3 = str2;
        iccIoResult = (IccIoResult) sendRequest(29, new IccAPDUArgument(0, i, i2, i6, i4, i7, str3), phone, (WorkSource) null);
        strSubstring = Integer.toHexString((iccIoResult.sw1 << 8) + iccIoResult.sw2 + 65536).substring(1);
        if (iccIoResult.payload != null) {
            strSubstring = IccUtils.bytesToHexString(iccIoResult.payload) + strSubstring;
        }
        Binder.restoreCallingIdentity(jClearCallingIdentity);
        return strSubstring;
    }

    public byte[] iccExchangeSimIO(int i, int i2, int i3, int i4, int i5, int i6, String str) {
        byte[] bArr;
        int length;
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "iccExchangeSimIO");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "iccExchangeSimIO");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            IccIoResult iccIoResult = (IccIoResult) sendRequest(31, new IccAPDUArgument(-1, i2, i3, i4, i5, i6, str), Integer.valueOf(i));
            byte[] bArr2 = iccIoResult.payload;
            if (bArr2 != null) {
                length = bArr2.length + 2;
                bArr = new byte[length];
                System.arraycopy(bArr2, 0, bArr, 0, bArr2.length);
            } else {
                bArr = new byte[2];
                length = 2;
            }
            bArr[length - 1] = (byte) iccIoResult.sw2;
            bArr[length - 2] = (byte) iccIoResult.sw1;
            return bArr;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String[] getForbiddenPlmns(int i, int i2, String str, String str2) {
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mApp, i, str, str2, "getForbiddenPlmns")) {
            return null;
        }
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.subscription", "getForbiddenPlmns");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (i2 != 2 && i2 != 1) {
                loge("getForbiddenPlmnList(): App Type must be USIM or SIM");
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return null;
            }
            Object objSendRequest = sendRequest(48, new Integer(i2), Integer.valueOf(i));
            if (!(objSendRequest instanceof String[])) {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return null;
            }
            String[] strArr = (String[]) objSendRequest;
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            return strArr;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public int setForbiddenPlmns(int i, int i2, List list, String str, String str2) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "setForbiddenPlmns");
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.subscription", "setForbiddenPlmns");
        if (i2 != 2 && i2 != 1) {
            loge("setForbiddenPlmnList(): App Type must be USIM or SIM");
            Folme$$ExternalSyntheticBUOutline0.m("Invalid appType: App Type must be USIM or SIM");
            return 0;
        }
        if (list == null) {
            Folme$$ExternalSyntheticBUOutline0.m("Fplmn List provided is null");
            return 0;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            String str3 = (String) it.next();
            if (!CellIdentity.isValidPlmn(str3)) {
                OrtProvider$$ExternalSyntheticBUOutline0.m("Invalid fplmn provided: ", str3);
                return 0;
            }
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return ((Integer) sendRequest(72, new Pair(new Integer(i2), list), Integer.valueOf(i))).intValue();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String sendEnvelopeWithStatus(int i, String str) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "sendEnvelopeWithStatus");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "sendEnvelopeWithStatus");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            IccIoResult iccIoResult = (IccIoResult) sendRequest(25, str, Integer.valueOf(i));
            if (iccIoResult.payload == null) {
                return "";
            }
            return IccUtils.bytesToHexString(iccIoResult.payload) + Integer.toHexString((iccIoResult.sw1 << 8) + iccIoResult.sw2 + 65536).substring(1);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean resetModemConfig(int i) {
        return rebootModem(i);
    }

    public boolean rebootModem(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        boolean z = false;
        if (phone == null) {
            HexString$$ExternalSyntheticBUOutline1.m("No modem at slotIndex=", i);
            return false;
        }
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, phone.getSubId(), "rebootModem");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.radio.access", "rebootModem");
        boolean zIsChangeEnabled = CompatChanges.isChangeEnabled(476225321L, Binder.getCallingUid());
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Object objSendRequest = sendRequest(64, null);
            Objects.requireNonNull(objSendRequest);
            int iSwitchDispatch = PhoneInterfaceManager$$ExternalSyntheticTypeSwitchClass14.switchDispatch(objSendRequest, 0);
            if (iSwitchDispatch == 0) {
                z = true;
            } else if (iSwitchDispatch != 1) {
                Rlog.e("PhoneInterfaceManager", "Unsupported return from sendRequest()" + objSendRequest);
                if (zIsChangeEnabled) {
                    throw new RuntimeException(objSendRequest.toString());
                }
            } else {
                RuntimeException runtimeException = (RuntimeException) objSendRequest;
                if (zIsChangeEnabled) {
                    throw runtimeException;
                }
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            return z;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void resetIms(int i) {
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "resetIms");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ImsResolver imsResolver = this.mImsResolver;
            if (imsResolver == null) {
                return;
            }
            imsResolver.resetIms(i);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void enableIms(int i) {
        enforceModifyPermission();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ImsResolver imsResolver = this.mImsResolver;
            if (imsResolver == null) {
                return;
            }
            imsResolver.enableIms(i);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void disableIms(int i) {
        enforceModifyPermission();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ImsResolver imsResolver = this.mImsResolver;
            if (imsResolver == null) {
                return;
            }
            imsResolver.disableIms(i);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void registerMmTelFeatureCallback(int i, IImsServiceFeatureCallback iImsServiceFeatureCallback) {
        enforceModifyPermission();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ImsResolver imsResolver = this.mImsResolver;
            if (imsResolver == null) {
                throw new ServiceSpecificException(2, "Device does not support IMS");
            }
            imsResolver.listenForFeature(i, 1, iImsServiceFeatureCallback);
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void unregisterImsFeatureCallback(IImsServiceFeatureCallback iImsServiceFeatureCallback) {
        enforceModifyPermission();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ImsResolver imsResolver = this.mImsResolver;
            if (imsResolver == null) {
                return;
            }
            imsResolver.unregisterImsFeatureCallback(iImsServiceFeatureCallback);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public IImsRegistration getImsRegistration(int i, int i2) {
        enforceModifyPermission();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ImsResolver imsResolver = this.mImsResolver;
            if (imsResolver != null) {
                return imsResolver.getImsRegistration(i, i2);
            }
            return null;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public IImsConfig getImsConfig(int i, int i2) {
        enforceModifyPermission();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ImsResolver imsResolver = this.mImsResolver;
            if (imsResolver != null) {
                return imsResolver.getImsConfig(i, i2);
            }
            return null;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setBoundImsServiceOverride(int i, int i2, boolean z, int[] iArr, String str) {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setBoundImsServiceOverride");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, SubscriptionManager.getSubscriptionId(i), "setBoundImsServiceOverride");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ImsResolver imsResolver = this.mImsResolver;
            if (imsResolver != null) {
                return imsResolver.overrideImsServiceConfiguration(str, i, i2, z, iArr);
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean clearCarrierImsServiceOverride(int i) {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "clearCarrierImsServiceOverride");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, SubscriptionManager.getSubscriptionId(i), "clearCarrierImsServiceOverride");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ImsResolver imsResolver = this.mImsResolver;
            if (imsResolver != null) {
                return imsResolver.clearCarrierImsServiceConfiguration(i);
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String getBoundImsServicePackage(int i, boolean z, int i2) {
        TelephonyPermissions.enforceCallingOrSelfReadPrivilegedPhoneStatePermissionOrCarrierPrivilege(this.mApp, SubscriptionManager.getSubscriptionId(i), "getBoundImsServicePackage");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ImsResolver imsResolver = this.mImsResolver;
            if (imsResolver == null) {
                return "";
            }
            return imsResolver.getImsServiceConfiguration(i, z, i2);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public void getImsMmTelFeatureState(int i, final IIntegerConsumer iIntegerConsumer) throws ServiceSpecificException {
        enforceReadPrivilegedPermission("getImsMmTelFeatureState");
        if (!ImsManager.isImsSupportedOnDevice(this.mApp)) {
            throw new ServiceSpecificException(2, "IMS not available on device.");
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                int slotIndex = getSlotIndex(i);
                if (slotIndex <= -1) {
                    Log.w("PhoneInterfaceManager", "getImsMmTelFeatureState: called with an inactive subscription '" + i + "'");
                    throw new ServiceSpecificException(3);
                }
                verifyImsMmTelConfiguredOrThrow(slotIndex);
                ImsManager.getInstance(this.mApp, slotIndex).getImsServiceState(new Consumer() { // from class: com.android.phone.PhoneInterfaceManager$$ExternalSyntheticLambda11
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        PhoneInterfaceManager.m1846$r8$lambda$iodq1rewOSlLt0AQDs6qlPvVhA(iIntegerConsumer, (Integer) obj);
                    }
                });
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$iodq1rewOSlLt-0AQDs6qlPvVhA, reason: not valid java name */
    public static /* synthetic */ void m1846$r8$lambda$iodq1rewOSlLt0AQDs6qlPvVhA(IIntegerConsumer iIntegerConsumer, Integer num) {
        int iIntValue;
        if (num == null) {
            iIntValue = 0;
        } else {
            try {
                iIntValue = num.intValue();
            } catch (RemoteException unused) {
                Log.w("PhoneInterfaceManager", "getImsMmTelFeatureState: remote caller is no longer running. Ignore");
                return;
            }
        }
        iIntegerConsumer.accept(iIntValue);
    }

    public void setImsRegistrationState(boolean z) {
        enforceModifyPermission();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            for (Phone phone : PhoneFactory.getPhones()) {
                if (SubscriptionManager.isValidSubscriptionId(phone.getSubId())) {
                    phone.setImsRegistrationState(z);
                }
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void setNetworkSelectionModeAutomatic(int i) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "setNetworkSelectionModeAutomatic");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.radio.access", "setNetworkSelectionModeAutomatic");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (isActiveSubscription(i)) {
                sendRequest(35, (Object) null, Integer.valueOf(i), 2000L);
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setNetworkSelectionModeManual(int i, OperatorInfo operatorInfo, boolean z) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "setNetworkSelectionModeManual");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.radio.access", "setNetworkSelectionModeManual");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        if (!isActiveSubscription(i)) {
            return false;
        }
        try {
            return ((Boolean) sendRequest(41, new ManualNetworkSelectionArgument(operatorInfo, z), Integer.valueOf(i))).booleanValue();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String getManualNetworkSelectionPlmn(int i) {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "getManualNetworkSelectionPlmn");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.radio.access", "getManualNetworkSelectionPlmn");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (!isActiveSubscription(i)) {
                throw new IllegalArgumentException("Invalid Subscription Id: " + i);
            }
            Phone phone = getPhone(i);
            if (phone == null) {
                throw new IllegalArgumentException("Invalid Subscription Id: " + i);
            }
            OperatorInfo savedNetworkSelection = phone.getSavedNetworkSelection();
            String manualNetworkSelectionPlmn = TextUtils.isEmpty(savedNetworkSelection.getOperatorNumeric()) ? phone.getManualNetworkSelectionPlmn() : savedNetworkSelection.getOperatorNumeric();
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            return manualNetworkSelectionPlmn;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public CellNetworkScanResult getCellNetworkScanResults(int i, String str, String str2) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "getCellNetworkScanResults");
        int i2 = AnonymousClass5.$SwitchMap$android$telephony$LocationAccessPolicy$LocationPermissionResult[LocationAccessPolicy.checkLocationPermission(this.mApp, new LocationAccessPolicy.LocationPermissionQuery.Builder().setCallingPackage(str).setCallingFeatureId(str2).setCallingPid(Binder.getCallingPid()).setCallingUid(Binder.getCallingUid()).setMethod("getCellNetworkScanResults").setMinSdkVersionForFine(29).setMinSdkVersionForCoarse(29).setMinSdkVersionForEnforcement(29).build()).ordinal()];
        if (i2 == 1) {
            ImsRcsController$$ExternalSyntheticBUOutline0.m("Not allowed to access scan results -- location");
            return null;
        }
        if (i2 == 2) {
            return null;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return (CellNetworkScanResult) sendRequest(39, (Object) null, Integer.valueOf(i));
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void getCallForwarding(int i, int i2, final ICallForwardingInfoCallback iCallForwardingInfoCallback) {
        enforceReadPrivilegedPermission("getCallForwarding");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.calling", "getCallForwarding");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone == null) {
                try {
                    iCallForwardingInfoCallback.onError(1);
                } catch (RemoteException unused) {
                }
            } else {
                sendRequestAsync(83, Pair.create(Integer.valueOf(i2), new TelephonyManager.CallForwardingInfoCallback(this) { // from class: com.android.phone.PhoneInterfaceManager.1
                    public void onCallForwardingInfoAvailable(CallForwardingInfo callForwardingInfo) {
                        try {
                            iCallForwardingInfoCallback.onCallForwardingInfoAvailable(callForwardingInfo);
                        } catch (RemoteException unused2) {
                        }
                    }

                    public void onError(int i3) {
                        try {
                            iCallForwardingInfoCallback.onError(i3);
                        } catch (RemoteException unused2) {
                        }
                    }
                }), phone, null);
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setCallForwarding(int i, CallForwardingInfo callForwardingInfo, IIntegerConsumer iIntegerConsumer) {
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.calling", "setCallForwarding");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone == null) {
                try {
                    iIntegerConsumer.accept(1);
                } catch (RemoteException unused) {
                }
            } else {
                Objects.requireNonNull(iIntegerConsumer);
                sendRequestAsync(85, Pair.create(callForwardingInfo, FunctionalUtils.ignoreRemoteException(new PhoneInterfaceManager$$ExternalSyntheticLambda5(iIntegerConsumer))), phone, null);
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void getCallWaitingStatus(final int i, IIntegerConsumer iIntegerConsumer) {
        enforceReadPrivilegedPermission("getCallWaitingStatus");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.calling", "getCallWaitingStatus");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone == null) {
                try {
                    iIntegerConsumer.accept(3);
                } catch (RemoteException unused) {
                }
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return;
            }
            if (new CarrierConfigManager(phone.getContext()).getConfigForSubId(i).getBoolean("use_call_waiting_ussd_bool", false)) {
                CarrierXmlParser carrierXmlParser = new CarrierXmlParser(phone.getContext(), getSubscriptionCarrierId(i));
                final String strMakeCommand = "";
                try {
                    strMakeCommand = carrierXmlParser.getFeature("callwaiting").makeCommand(CarrierXmlParser.SsEntry.SSAction.QUERY, null);
                } catch (NullPointerException e) {
                    loge("Failed to generate USSD number" + e);
                }
                final CallWaitingUssdResultReceiver callWaitingUssdResultReceiver = new CallWaitingUssdResultReceiver(this.mMainThreadHandler, iIntegerConsumer, carrierXmlParser, CarrierXmlParser.SsEntry.SSAction.QUERY);
                Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.android.phone.PhoneInterfaceManager$$ExternalSyntheticLambda8
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$getCallWaitingStatus$4(i, strMakeCommand, callWaitingUssdResultReceiver);
                    }
                });
            } else {
                Objects.requireNonNull(iIntegerConsumer);
                sendRequestAsync(87, FunctionalUtils.ignoreRemoteException(new PhoneInterfaceManager$$ExternalSyntheticLambda5(iIntegerConsumer)), phone, null);
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void setCallWaitingStatus(final int i, boolean z, IIntegerConsumer iIntegerConsumer) {
        CarrierXmlParser.SsEntry.SSAction sSAction;
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.calling", "setCallWaitingStatus");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone == null) {
                try {
                    iIntegerConsumer.accept(3);
                } catch (RemoteException unused) {
                }
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return;
            }
            if (new CarrierConfigManager(phone.getContext()).getConfigForSubId(i).getBoolean("use_call_waiting_ussd_bool", false)) {
                CarrierXmlParser carrierXmlParser = new CarrierXmlParser(phone.getContext(), getSubscriptionCarrierId(i));
                if (z) {
                    sSAction = CarrierXmlParser.SsEntry.SSAction.UPDATE_ACTIVATE;
                } else {
                    sSAction = CarrierXmlParser.SsEntry.SSAction.UPDATE_DEACTIVATE;
                }
                final String strMakeCommand = "";
                try {
                    strMakeCommand = carrierXmlParser.getFeature("callwaiting").makeCommand(sSAction, null);
                } catch (NullPointerException e) {
                    loge("Failed to generate USSD number" + e);
                }
                final CallWaitingUssdResultReceiver callWaitingUssdResultReceiver = new CallWaitingUssdResultReceiver(this.mMainThreadHandler, iIntegerConsumer, carrierXmlParser, sSAction);
                Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.android.phone.PhoneInterfaceManager$$ExternalSyntheticLambda12
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$setCallWaitingStatus$5(i, strMakeCommand, callWaitingUssdResultReceiver);
                    }
                });
            } else {
                Boolean boolValueOf = Boolean.valueOf(z);
                Objects.requireNonNull(iIntegerConsumer);
                sendRequestAsync(89, Pair.create(boolValueOf, FunctionalUtils.ignoreRemoteException(new PhoneInterfaceManager$$ExternalSyntheticLambda5(iIntegerConsumer))), phone, null);
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public int requestNetworkScan(int i, boolean z, NetworkScanRequest networkScanRequest, Messenger messenger, IBinder iBinder, String str, String str2) {
        SecurityException securityExceptionCheckNetworkRequestForSanitizedLocationAccess;
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "requestNetworkScan");
        LocationAccessPolicy.LocationPermissionResult locationPermissionResult = LocationAccessPolicy.LocationPermissionResult.DENIED_HARD;
        LocationAccessPolicy.LocationPermissionResult locationPermissionResultCheckLocationPermission = !z ? LocationAccessPolicy.checkLocationPermission(this.mApp, new LocationAccessPolicy.LocationPermissionQuery.Builder().setCallingPackage(str).setCallingFeatureId(str2).setCallingPid(Binder.getCallingPid()).setCallingUid(Binder.getCallingUid()).setMethod("requestNetworkScan").setMinSdkVersionForFine(29).setMinSdkVersionForCoarse(29).setMinSdkVersionForEnforcement(29).build()) : locationPermissionResult;
        if (locationPermissionResultCheckLocationPermission != LocationAccessPolicy.LocationPermissionResult.ALLOWED && (securityExceptionCheckNetworkRequestForSanitizedLocationAccess = checkNetworkRequestForSanitizedLocationAccess(networkScanRequest, i, str)) != null) {
            if (locationPermissionResultCheckLocationPermission == locationPermissionResult) {
                throw securityExceptionCheckNetworkRequestForSanitizedLocationAccess;
            }
            loge(securityExceptionCheckNetworkRequestForSanitizedLocationAccess.getMessage());
            return -1;
        }
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.radio.access", "requestNetworkScan");
        int callingUid = Binder.getCallingUid();
        int callingPid = Binder.getCallingPid();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mNetworkScanRequestTracker.startNetworkScan(z, networkScanRequest, messenger, iBinder, getPhoneFromSubIdOrDefault(i), callingUid, callingPid, str);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private SecurityException checkNetworkRequestForSanitizedLocationAccess(NetworkScanRequest networkScanRequest, int i, String str) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            int iCheckCarrierPrivilegesForPackage = checkCarrierPrivilegesForPackage(i, str);
            boolean z = iCheckCarrierPrivilegesForPackage == 1;
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            boolean z2 = this.mApp.checkCallingOrSelfPermission("android.permission.NETWORK_SCAN") == 0;
            if (!z && !z2) {
                return new SecurityException("permission.NETWORK_SCAN or carrier privileges is needed for network scans without location access.");
            }
            if (networkScanRequest.getSpecifiers() == null || networkScanRequest.getSpecifiers().length <= 0) {
                return null;
            }
            for (RadioAccessSpecifier radioAccessSpecifier : networkScanRequest.getSpecifiers()) {
                if (radioAccessSpecifier.getChannels() != null && radioAccessSpecifier.getChannels().length > 0) {
                    return new SecurityException("Specific channels must not be scanned without location access.");
                }
            }
            return null;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void stopNetworkScan(int i, int i2) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "stopNetworkScan");
        int callingUid = Binder.getCallingUid();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mNetworkScanRequestTracker.stopNetworkScan(i2, callingUid);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getAllowedNetworkTypesBitmask(int i) {
        TelephonyPermissions.enforceCallingOrSelfReadPrivilegedPhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "getAllowedNetworkTypesBitmask");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.radio.access", "getAllowedNetworkTypesBitmask");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            int[] iArr = (int[]) sendRequest(21, (Object) null, Integer.valueOf(i));
            return iArr != null ? iArr[0] : -1;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public long getAllowedNetworkTypesForReason(int i, int i2) {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "getAllowedNetworkTypesForReason");
        if (!this.mApp.getResources().getBoolean(android.R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.radio.access", "getAllowedNetworkTypesForReason");
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return getPhoneFromSubIdOrDefault(i).getAllowedNetworkTypes(i2);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int setNrDualConnectivityState(int i, int i2) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "enableNRDualConnectivity");
        if (!isRadioInterfaceCapabilitySupported("CAPABILITY_NR_DUAL_CONNECTIVITY_CONFIGURATION_AVAILABLE")) {
            return 1;
        }
        WorkSource workSource = getWorkSource(Binder.getCallingUid());
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return ((Integer) sendRequest(91, Integer.valueOf(i2), i, workSource)).intValue();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isNrDualConnectivityEnabled(int i) {
        TelephonyPermissions.enforceCallingOrSelfReadPrivilegedPhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "isNRDualConnectivityEnabled");
        if (!isRadioInterfaceCapabilitySupported("CAPABILITY_NR_DUAL_CONNECTIVITY_CONFIGURATION_AVAILABLE")) {
            return false;
        }
        WorkSource workSource = getWorkSource(Binder.getCallingUid());
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return ((Boolean) sendRequest(93, (Object) null, i, workSource)).booleanValue();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setAllowedNetworkTypesForReason(int i, int i2, long j, String str) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "setAllowedNetworkTypesForReason");
        String strConvertAllowedNetworkTypeMapIndexToDbName = Phone.convertAllowedNetworkTypeMapIndexToDbName(i2);
        if (this.mApp.checkCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE") != 0 && i2 == 3) {
            throw new SecurityException("setAllowedNetworkTypesForReason cannot be called with carrier privileges for reason " + strConvertAllowedNetworkTypeMapIndexToDbName);
        }
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.radio.access", "setAllowedNetworkTypesForReason");
        if (!TelephonyManager.isValidAllowedNetworkTypesReason(i2)) {
            loge("setAllowedNetworkTypesForReason: Invalid allowed network type reason: " + i2);
            return false;
        }
        if (!SubscriptionManager.isUsableSubscriptionId(i)) {
            loge("setAllowedNetworkTypesForReason: Invalid subscriptionId:" + i);
            return false;
        }
        log("setAllowedNetworkTypesForReason: subId=" + i + ", reason=" + strConvertAllowedNetworkTypeMapIndexToDbName + ", value=" + TelephonyManager.convertNetworkTypeBitmaskToString(j) + ", callingPackage=" + str);
        Phone phone = getPhone(i);
        if (phone == null) {
            return false;
        }
        if (j == phone.getAllowedNetworkTypes(i2)) {
            log("setAllowedNetworkTypesForReason: " + strConvertAllowedNetworkTypeMapIndexToDbName + ". Already has this value.");
            return true;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        MiPhoneAppStub.get().setAllowedNetworkTypesForReason(i, i2, j);
        try {
            return ((Boolean) sendRequest(107, new Pair(Integer.valueOf(i2), Long.valueOf(j)), Integer.valueOf(i))).booleanValue();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isTetheringApnRequiredForSubscriber(int i) {
        enforceModifyPermission();
        if (!this.mApp.getResources().getBoolean(android.R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.data", "isTetheringApnRequiredForSubscriber");
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        Phone phone = getPhone(i);
        try {
            if (phone != null) {
                try {
                    boolean zHasMatchedTetherApnSetting = phone.hasMatchedTetherApnSetting();
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                    return zHasMatchedTetherApnSetting;
                } catch (NullPointerException e) {
                    Log.e("PhoneInterfaceManager", "NullPointerException in isTetheringApnRequiredForSubscriber, subId=" + i + ", phone=" + phone, e);
                    throw e;
                }
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            return false;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public boolean getDataEnabled(int i) {
        return isUserDataEnabled(i);
    }

    public boolean isUserDataEnabled(int i) {
        if (!TelephonyCommonUtilsStub.get().isMiuiRom()) {
            try {
                try {
                    this.mApp.enforceCallingOrSelfPermission("android.permission.READ_BASIC_PHONE_STATE", "isUserDataEnabled");
                } catch (SecurityException unused) {
                    TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "isUserDataEnabled");
                }
            } catch (SecurityException unused2) {
                this.mApp.enforceCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE", "isUserDataEnabled");
            }
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                Phone phone = PhoneFactory.getPhone(SubscriptionManager.getPhoneId(i));
                if (phone != null) {
                    return phone.isUserDataEnabled();
                }
                return false;
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }
        return MiPhoneAppStub.get().isUserDataEnabled(this.mApp, i);
    }

    public boolean isDataEnabled(int i) {
        try {
            try {
                try {
                    this.mApp.enforceCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE", "isDataEnabled");
                } catch (SecurityException unused) {
                    this.mApp.enforceCallingOrSelfPermission("android.permission.READ_BASIC_PHONE_STATE", "isDataEnabled");
                }
            } catch (SecurityException unused2) {
                enforceReadPrivilegedPermission("isDataEnabled");
            }
        } catch (SecurityException unused3) {
            this.mApp.enforceCallingOrSelfPermission("android.permission.READ_PHONE_STATE", "isDataEnabled");
        }
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.data", "isDataEnabled");
        if (TelephonyCommonUtilsStub.get().isMiuiRom()) {
            return MiPhoneAppStub.get().isDataEnabled(this.mApp, i);
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = PhoneFactory.getPhone(SubscriptionManager.getPhoneId(i));
            if (phone == null || phone.getDataSettingsManager() == null) {
                return false;
            }
            return phone.getDataSettingsManager().isDataEnabled();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isDataEnabledForReason(int i, int i2) {
        try {
            try {
                try {
                    this.mApp.enforceCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE", "isDataEnabledForReason");
                } catch (SecurityException unused) {
                    this.mApp.enforceCallingOrSelfPermission("android.permission.READ_PHONE_STATE", "isDataEnabledForReason");
                }
            } catch (SecurityException unused2) {
                TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "isDataEnabledForReason");
            }
        } catch (SecurityException unused3) {
            this.mApp.enforceCallingOrSelfPermission("android.permission.READ_BASIC_PHONE_STATE", "isDataEnabledForReason");
        }
        if (!this.mApp.getResources().getBoolean(android.R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.data", "isDataEnabledForReason");
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = PhoneFactory.getPhone(SubscriptionManager.getPhoneId(i));
            if (phone == null || phone.getDataSettingsManager() == null) {
                return false;
            }
            return phone.getDataSettingsManager().isDataEnabledForReason(i2);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getCarrierPrivilegeStatus(int i) {
        if (!this.mApp.getResources().getBoolean(android.R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "getCarrierPrivilegeStatus");
        }
        return getCarrierPrivilegeStatusForUidWithPermission(i, Binder.getCallingUid());
    }

    public int getCarrierPrivilegeStatusForUid(int i, int i2) {
        enforceReadPrivilegedPermission("getCarrierPrivilegeStatusForUid");
        if (!this.mApp.getResources().getBoolean(android.R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "getCarrierPrivilegeStatusForUid");
        }
        return getCarrierPrivilegeStatusForUidWithPermission(i, i2);
    }

    private int getCarrierPrivilegeStatusForUidWithPermission(int i, int i2) {
        Phone phone = getPhone(i);
        if (phone == null) {
            loge("getCarrierPrivilegeStatusForUid: Invalid subId");
            return 0;
        }
        CarrierPrivilegesTracker carrierPrivilegesTracker = phone.getCarrierPrivilegesTracker();
        if (carrierPrivilegesTracker == null) {
            loge("getCarrierPrivilegeStatusForUid: No CarrierPrivilegesTracker");
            return -1;
        }
        return carrierPrivilegesTracker.getCarrierPrivilegeStatusForUid(i2);
    }

    public int checkCarrierPrivilegesForPackage(int i, String str) {
        enforceReadPrivilegedPermission("checkCarrierPrivilegesForPackage");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "checkCarrierPrivilegesForPackage");
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        Phone phone = getPhone(i);
        if (phone == null) {
            loge("checkCarrierPrivilegesForPackage: Invalid subId");
            return 0;
        }
        CarrierPrivilegesTracker carrierPrivilegesTracker = phone.getCarrierPrivilegesTracker();
        if (carrierPrivilegesTracker == null) {
            loge("checkCarrierPrivilegesForPackage: No CarrierPrivilegesTracker");
            return -1;
        }
        return carrierPrivilegesTracker.getCarrierPrivilegeStatusForPackage(str);
    }

    public int checkCarrierPrivilegesForPackageAnyPhone(String str) {
        enforceReadPrivilegedPermission("checkCarrierPrivilegesForPackageAnyPhone");
        if (!this.mApp.getResources().getBoolean(android.R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "checkCarrierPrivilegesForPackageAnyPhone");
        }
        return checkCarrierPrivilegesForPackageAnyPhoneWithPermission(str);
    }

    private int checkCarrierPrivilegesForPackageAnyPhoneWithPermission(String str) {
        CarrierPrivilegesTracker carrierPrivilegesTracker;
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        int carrierPrivilegeStatusForPackage = -1;
        for (int i = 0; i < TelephonyManager.getDefault().getPhoneCount(); i++) {
            Phone phone = PhoneFactory.getPhone(i);
            if (phone != null && (carrierPrivilegesTracker = phone.getCarrierPrivilegesTracker()) != null && (carrierPrivilegeStatusForPackage = carrierPrivilegesTracker.getCarrierPrivilegeStatusForPackage(str)) == 1) {
                return carrierPrivilegeStatusForPackage;
            }
        }
        return carrierPrivilegeStatusForPackage;
    }

    public List getCarrierPackageNamesForIntentAndPhone(Intent intent, int i) {
        enforceReadPrivilegedPermission("getCarrierPackageNamesForIntentAndPhone");
        if (!this.mApp.getResources().getBoolean(android.R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "getCarrierPackageNamesForIntentAndPhone");
        }
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            return Collections.EMPTY_LIST;
        }
        CarrierPrivilegesTracker carrierPrivilegesTracker = phone.getCarrierPrivilegesTracker();
        if (carrierPrivilegesTracker == null) {
            return Collections.EMPTY_LIST;
        }
        return carrierPrivilegesTracker.getCarrierPackageNamesForIntent(intent);
    }

    public List getPackagesWithCarrierPrivileges(int i) {
        enforceReadPrivilegedPermission("getPackagesWithCarrierPrivileges");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "getPackagesWithCarrierPrivileges");
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            return Collections.EMPTY_LIST;
        }
        CarrierPrivilegesTracker carrierPrivilegesTracker = phone.getCarrierPrivilegesTracker();
        if (carrierPrivilegesTracker == null) {
            return Collections.EMPTY_LIST;
        }
        return new ArrayList(carrierPrivilegesTracker.getPackagesWithCarrierPrivileges());
    }

    public List getPackagesWithCarrierPrivilegesForAllPhones() {
        enforceReadPrivilegedPermission("getPackagesWithCarrierPrivilegesForAllPhones");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "getPackagesWithCarrierPrivilegesForAllPhones");
        ArraySet arraySet = new ArraySet();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        for (int i = 0; i < TelephonyManager.getDefault().getPhoneCount(); i++) {
            try {
                arraySet.addAll(getPackagesWithCarrierPrivileges(i));
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                throw th;
            }
        }
        Binder.restoreCallingIdentity(jClearCallingIdentity);
        return new ArrayList(arraySet);
    }

    public String getCarrierServicePackageNameForLogicalSlot(int i) {
        CarrierPrivilegesTracker carrierPrivilegesTracker;
        enforceReadPrivilegedPermission("getCarrierServicePackageNameForLogicalSlot");
        if (!this.mApp.getResources().getBoolean(android.R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "getCarrierServicePackageNameForLogicalSlot");
        }
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null || (carrierPrivilegesTracker = phone.getCarrierPrivilegesTracker()) == null) {
            return null;
        }
        return carrierPrivilegesTracker.getCarrierServicePackageName();
    }

    private String getIccId(int i) {
        Phone phone = getPhone(i);
        UiccPort uiccPort = phone == null ? null : phone.getUiccPort();
        if (uiccPort == null) {
            return null;
        }
        String iccId = uiccPort.getIccId();
        if (TextUtils.isEmpty(iccId)) {
            return null;
        }
        return iccId;
    }

    public void setCallComposerStatus(int i, int i2) {
        ImsPhone imsPhone;
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.calling", "setCallComposerStatus");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                Phone phone = getPhone(i);
                if (phone != null && (imsPhone = phone.getImsPhone()) != null && imsPhone.getPhoneType() == 5) {
                    imsPhone.setCallComposerStatus(i2);
                    ImsManager.getInstance(this.mApp, getSlotIndexOrException(i)).updateImsServiceConfig();
                }
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public int getCallComposerStatus(int i) {
        ImsPhone imsPhone;
        enforceReadPrivilegedPermission("getCallComposerStatus");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.calling", "getCallComposerStatus");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone == null || (imsPhone = phone.getImsPhone()) == null || imsPhone.getPhoneType() != 5) {
                return 0;
            }
            return imsPhone.getCallComposerStatus();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setLine1NumberForDisplayForSubscriber(int i, String str, String str2) {
        TelephonyPermissions.enforceCallingOrSelfCarrierPrivilege(this.mApp, i, "setLine1NumberForDisplayForSubscriber");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "setLine1NumberForDisplayForSubscriber");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            String iccId = getIccId(i);
            Phone phone = getPhone(i);
            if (phone == null) {
                return false;
            }
            if (!TextUtils.isEmpty(str2) && str2.length() > 50) {
                Rlog.e("PhoneInterfaceManager", "Number is too long");
                return false;
            }
            String subscriberId = phone.getSubscriberId();
            if (TextUtils.isEmpty(iccId)) {
                return false;
            }
            SharedPreferences.Editor editorEdit = this.mTelephonySharedPreferences.edit();
            String str3 = "carrier_alphtag_" + iccId;
            if (str == null) {
                editorEdit.remove(str3);
            } else {
                editorEdit.putString(str3, str);
            }
            String str4 = "carrier_number_" + iccId;
            String str5 = "carrier_subscriber_" + iccId;
            if (str2 == null) {
                editorEdit.remove(str4);
                editorEdit.remove(str5);
            } else {
                editorEdit.putString(str4, str2);
                editorEdit.putString(str5, subscriberId);
            }
            editorEdit.commit();
            return true;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String getLine1NumberForDisplay(int i, String str, String str2) {
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneNumber(this.mApp, i, str, str2, "getLine1NumberForDisplay")) {
            return null;
        }
        if (!this.mApp.getResources().getBoolean(android.R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(str, "android.hardware.telephony.subscription", "getLine1NumberForDisplay");
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            String iccId = getIccId(i);
            if (iccId == null) {
                return null;
            }
            return this.mTelephonySharedPreferences.getString("carrier_number_".concat(iccId), null);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String getLine1AlphaTagForDisplay(int i, String str, String str2) {
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mApp, i, str, str2, "getLine1AlphaTagForDisplay")) {
            return null;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            String iccId = getIccId(i);
            if (iccId == null) {
                return null;
            }
            return this.mTelephonySharedPreferences.getString("carrier_alphtag_".concat(iccId), null);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String[] getMergedSubscriberIds(int i, String str, String str2) {
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mApp, -1, str, str2, "getMergedSubscriberIds")) {
            return null;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            TelephonyManager telephonyManagerFrom = TelephonyManager.from(this.mApp);
            ArraySet arraySet = new ArraySet();
            if (isActiveSubscription(i)) {
                arraySet.add(telephonyManagerFrom.getSubscriberId(i));
            }
            Map<String, ?> all = this.mTelephonySharedPreferences.getAll();
            String str3 = null;
            for (String str4 : all.keySet()) {
                if (str4.startsWith("carrier_subscriber_") && arraySet.contains((String) all.get(str4))) {
                    str3 = (String) all.get("carrier_number_" + str4.substring(19));
                    if (!TextUtils.isEmpty(str3)) {
                        break;
                    }
                }
            }
            if (TextUtils.isEmpty(str3)) {
                return null;
            }
            ArraySet arraySet2 = new ArraySet();
            for (String str5 : all.keySet()) {
                if (str5.startsWith("carrier_number_") && str3.equals((String) all.get(str5))) {
                    String str6 = (String) all.get("carrier_subscriber_" + str5.substring(15));
                    if (!TextUtils.isEmpty(str6)) {
                        arraySet2.add(str6);
                    }
                }
            }
            String[] strArr = (String[]) arraySet2.toArray(new String[arraySet2.size()]);
            Arrays.sort(strArr);
            return strArr;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String[] getMergedImsisFromGroup(int i, String str) {
        enforceReadPrivilegedPermission("getMergedImsisFromGroup");
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.subscription", "getMergedImsisFromGroup");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            TelephonyManager telephonyManager = (TelephonyManager) this.mApp.getSystemService(TelephonyManager.class);
            String subscriberId = telephonyManager.getSubscriberId(i);
            if (subscriberId != null) {
                ParcelUuid groupUuid = getSubscriptionManagerService().getSubscriptionInfo(i).getGroupUuid();
                if (groupUuid == null) {
                    String[] strArr = {subscriberId};
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                    return strArr;
                }
                ArrayList arrayList = new ArrayList();
                Iterator it = getSubscriptionManagerService().getSubscriptionsInGroup(groupUuid, this.mApp.getOpPackageName(), this.mApp.getAttributionTag()).iterator();
                while (it.hasNext()) {
                    String subscriberId2 = telephonyManager.getSubscriberId(((SubscriptionInfo) it.next()).getSubscriptionId());
                    if (subscriberId2 != null) {
                        arrayList.add(subscriberId2);
                    }
                }
                String[] strArr2 = (String[]) arrayList.toArray(new String[arrayList.size()]);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return strArr2;
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            return null;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public boolean setOperatorBrandOverride(int i, String str) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "setOperatorBrandOverride");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "setOperatorBrandOverride");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            return phone == null ? false : phone.setOperatorBrandOverride(str);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setRoamingOverride(int i, List list, List list2, List list3, List list4) {
        TelephonyPermissions.enforceCallingOrSelfCarrierPrivilege(this.mApp, i, "setRoamingOverride");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone != null) {
                return phone.setRoamingOverride(list, list2, list3, list4);
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getRadioAccessFamily(int i, String str) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            return 0;
        }
        try {
            TelephonyPermissions.enforceCallingOrSelfReadPrivilegedPhoneStatePermissionOrCarrierPrivilege(this.mApp, phone.getSubId(), "getRadioAccessFamily");
            if (!this.mApp.getResources().getBoolean(android.R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
                enforceTelephonyFeatureWithException(str, "android.hardware.telephony.radio.access", "getRadioAccessFamily");
            }
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return ProxyController.getInstance().getRadioAccessFamily(i);
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        } catch (SecurityException e) {
            EventLog.writeEvent(1397638484, "150857259", -1, "Missing Permission");
            throw e;
        }
    }

    public void uploadCallComposerPicture(final int i, String str, final String str2, final ParcelFileDescriptor parcelFileDescriptor, final ResultReceiver resultReceiver) {
        enforceCallingPackage(str, Binder.getCallingUid(), "Invalid package:" + str);
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.calling", "uploadCallComposerPicture");
        if (!((RoleManager) this.mApp.getSystemService(RoleManager.class)).getRoleHoldersAsUser("android.app.role.DIALER", UserHandle.of(ActivityManager.getCurrentUser())).contains(str)) {
            ImsRcsController$$ExternalSyntheticBUOutline0.m("App must be the dialer role holder to upload a call composer pic");
        } else {
            Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.android.phone.PhoneInterfaceManager$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$uploadCallComposerPicture$7(parcelFileDescriptor, resultReceiver, str2, i);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$uploadCallComposerPicture$7(ParcelFileDescriptor parcelFileDescriptor, final ResultReceiver resultReceiver, String str, int i) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream((int) TelephonyManager.getMaximumCallComposerPictureSize());
        ParcelFileDescriptor.AutoCloseInputStream autoCloseInputStream = new ParcelFileDescriptor.AutoCloseInputStream(parcelFileDescriptor);
        byte[] bArr = new byte[16384];
        boolean z = false;
        int i2 = 0;
        while (true) {
            try {
                try {
                    int i3 = autoCloseInputStream.read(bArr);
                    if (i3 == -1) {
                        z = true;
                        break;
                    }
                    i2 += i3;
                    if (i2 > TelephonyManager.getMaximumCallComposerPictureSize()) {
                        loge("Too many bytes read for call composer picture: " + i2);
                        try {
                            autoCloseInputStream.close();
                            break;
                        } catch (IOException unused) {
                            break;
                        }
                    }
                    byteArrayOutputStream.write(bArr, 0, i3);
                } catch (IOException e) {
                    loge("Remote end of call composer picture pipe closed: " + e);
                }
            } catch (IOException unused2) {
                parcelFileDescriptor.checkError();
                resultReceiver.send(4, null);
            }
        }
        try {
            parcelFileDescriptor.checkError();
            if (!z) {
                loge("Did not finish reading entire image; aborting");
            } else {
                CallComposerPictureManager.getInstance(this.mApp, i).handleUploadToServer(new CallComposerPictureTransfer.Factory(this) { // from class: com.android.phone.PhoneInterfaceManager.2
                }, new ImageData(byteArrayOutputStream.toByteArray(), str, null), new Consumer() { // from class: com.android.phone.PhoneInterfaceManager$$ExternalSyntheticLambda13
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        PhoneInterfaceManager.$r8$lambda$reoFTkjzpqIrdXn_NqXoOQJfQt8(resultReceiver, (Pair) obj);
                    }
                });
            }
        } catch (IOException e2) {
            loge("Remote end for call composer closed with an error: " + e2);
        }
    }

    public static /* synthetic */ void $r8$lambda$reoFTkjzpqIrdXn_NqXoOQJfQt8(ResultReceiver resultReceiver, Pair pair) {
        if (pair.first != null) {
            ParcelUuid parcelUuid = new ParcelUuid((UUID) pair.first);
            Bundle bundle = new Bundle();
            bundle.putParcelable("call_composer_picture_handle", parcelUuid);
            resultReceiver.send(-1, bundle);
            return;
        }
        resultReceiver.send(((Integer) pair.second).intValue(), null);
    }

    public void enableVideoCalling(boolean z) {
        Phone defaultPhone = getDefaultPhone();
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "enableVideoCalling");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ImsManager.getInstance(defaultPhone.getContext(), defaultPhone.getPhoneId()).setVtSetting(z);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isVideoCallingEnabled(String str, String str2) {
        Phone defaultPhone = getDefaultPhone();
        boolean z = false;
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mApp, defaultPhone.getSubId(), str, str2, "isVideoCallingEnabled")) {
            return false;
        }
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.ims", "isVideoCallingEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ImsManager imsManager = ImsManager.getInstance(defaultPhone.getContext(), defaultPhone.getPhoneId());
            if (imsManager.isVtEnabledByPlatform() && imsManager.isEnhanced4gLteModeSettingEnabledByUser() && imsManager.isVtEnabledByUser()) {
                z = true;
            }
            return z;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean canChangeDtmfToneLength(int i, String str, String str2) {
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mApp, i, str, str2, "isVideoCallingEnabled")) {
            return false;
        }
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.calling", "canChangeDtmfToneLength");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return ((CarrierConfigManager) this.mApp.getSystemService("carrier_config")).getConfigForSubId(i).getBoolean("dtmf_type_enabled_bool");
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isWorldPhone(int i, String str, String str2) {
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mApp, i, str, str2, "isVideoCallingEnabled")) {
            return false;
        }
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony", "isWorldPhone");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return ((CarrierConfigManager) this.mApp.getSystemService("carrier_config")).getConfigForSubId(i).getBoolean("world_phone_bool");
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isTtyModeSupported() {
        return ((TelecomManager) this.mApp.getSystemService(TelecomManager.class)).isTtySupported();
    }

    public boolean isHearingAidCompatibilitySupported() {
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.calling", "isHearingAidCompatibilitySupported");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mApp.getResources().getBoolean(R.bool.hac_enabled);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isRttSupported(int i) {
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "isRttSupported");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        Phone phone = getPhone(i);
        try {
            PhoneGlobals phoneGlobals = this.mApp;
            boolean z = false;
            boolean z2 = phoneGlobals.getResources() != null ? phoneGlobals.getResources().getBoolean(R.bool.config_support_rtt) : false;
            if (TelephonyManager.from(phoneGlobals).getSimCardState() != 11) {
                log("isRttSupported: sim invalid, subId: " + i + ", isDeviceSupported:" + z2);
                return z2;
            }
            if (phone == null) {
                log("isRttSupported: no Phone found. Invalid subId:" + i);
                return false;
            }
            boolean z3 = this.mApp.getCarrierConfigForSubId(i).getBoolean("rtt_supported_bool");
            Log.d("PhoneInterfaceManager", "isRttSupported: carrier: " + z3 + ", device: " + z2);
            if (z3 && z2) {
                z = true;
            }
            return z;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isRttEnabled(int i) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            boolean z = false;
            if (!this.mPackageManager.hasSystemFeature("android.hardware.telephony.ims")) {
                return false;
            }
            boolean zIsRttSupported = isRttSupported(i);
            boolean z2 = Settings.Secure.getInt(this.mApp.getContentResolver(), "rtt_calling_mode", 0) != 0;
            boolean z3 = this.mApp.getCarrierConfigForSubId(i).getBoolean("ignore_rtt_mode_setting_bool");
            if (zIsRttSupported && (z2 || z3)) {
                z = true;
            }
            return z;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String getDeviceId(String str) {
        return getDeviceIdWithFeature(str, null);
    }

    public String getDeviceIdWithFeature(String str, String str2) {
        try {
            this.mAppOps.checkPackage(Binder.getCallingUid(), str);
            Phone phone = PhoneFactory.getPhone(0);
            if (phone == null) {
                return null;
            }
            if (!TelephonyPermissions.checkCallingOrSelfReadDeviceIdentifiers(this.mApp, phone.getSubId(), str, str2, "getDeviceId")) {
                return null;
            }
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return phone.getDeviceId();
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        } catch (SecurityException unused) {
            EventLog.writeEvent(1397638484, "186530889", Integer.valueOf(Binder.getCallingUid()));
            PhoneInterfaceManager$$ExternalSyntheticBUOutline0.m(str, Binder.getCallingUid());
            return null;
        }
    }

    public boolean isImsRegistered(int i) {
        Phone phone = getPhone(i);
        if (phone != null) {
            return phone.isImsRegistered();
        }
        return false;
    }

    public int getSubIdForPhoneAccountHandle(PhoneAccountHandle phoneAccountHandle, String str, String str2) {
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mApp, getDefaultSubscription(), str, str2, "getSubIdForPhoneAccountHandle")) {
            ImsRcsController$$ExternalSyntheticBUOutline0.m("Requires READ_PHONE_STATE permission.");
            return 0;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return PhoneUtils.getSubIdForPhoneAccountHandle(phoneAccountHandle);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public PhoneAccountHandle getPhoneAccountHandleForSubscriptionId(int i) {
        TelephonyPermissions.enforceCallingOrSelfReadPrivilegedPhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "getPhoneAccountHandleForSubscriptionId, subscriptionId: " + i);
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.calling", "getPhoneAccountHandleForSubscriptionId");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone != null) {
                return PhoneUtils.makePstnPhoneAccountHandle(phone);
            }
            return null;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isWifiCallingAvailable(int i) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone != null) {
                return phone.isWifiCallingEnabled();
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isVideoTelephonyAvailable(int i) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone != null) {
                return phone.isVideoEnabled();
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getImsRegTechnologyForMmTel(int i) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone != null) {
                return phone.getImsRegistrationTech();
            }
            return -1;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void factoryReset(int i, String str) {
        enforceSettingsPermission();
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony", "factoryReset");
        if (this.mUserManager.hasUserRestriction("no_network_reset")) {
            return;
        }
        Phone defaultPhone = getDefaultPhone();
        if (defaultPhone != null) {
            TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, getDefaultPhone().getSubId(), "factoryReset");
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (SubscriptionManager.isUsableSubIdValue(i) && !this.mUserManager.hasUserRestriction("no_config_mobile_networks")) {
                boolean zIsDefaultDataRoamingEnabled = false;
                setDataEnabledForReason(i, 0, getDefaultDataEnabled(), str);
                setNetworkSelectionModeAutomatic(i);
                Phone phone = getPhone(i);
                cleanUpAllowedNetworkTypes(phone, i);
                clearLastInternetDataProfiles(phone, i);
                if (phone != null) {
                    zIsDefaultDataRoamingEnabled = phone.getDataSettingsManager().isDefaultDataRoamingEnabled();
                }
                setDataRoamingEnabled(i, zIsDefaultDataRoamingEnabled);
                getPhone(i).resetCarrierKeysForImsiEncryption(true);
            }
            cleanUpSmsRawTable(getDefaultPhone().getContext());
            int slotIndex = getSlotIndex(i);
            if (isImsAvailableOnDevice() && slotIndex > -1) {
                ImsManager.getInstance(this.mApp, slotIndex).factoryReset();
            }
            if (defaultPhone == null) {
                return;
            }
            String property = DeviceConfig.getProperty("telephony", "reset_network_erase_modem_config_enabled");
            if (property != null && Boolean.parseBoolean(property)) {
                sendEraseModemConfig();
            }
            sendEraseDataInSharedPreferences();
            if (TelephonyBaseUtilsStub.isMiuiRom()) {
                MiTelephonyServiceStub.get().factoryReset();
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private void clearLastInternetDataProfiles(Phone phone, int i) {
        DataNetworkController dataNetworkController;
        DataProfileManager dataProfileManager;
        if (phone == null || (dataNetworkController = phone.getDataNetworkController()) == null || (dataProfileManager = dataNetworkController.getDataProfileManager()) == null) {
            return;
        }
        dataProfileManager.clearLastInternetDataProfiles(i);
    }

    void cleanUpAllowedNetworkTypes(Phone phone, int i) {
        if (phone == null || !SubscriptionManager.isUsableSubscriptionId(i) || MiPhoneAppStub.get().factoryResetByFiveGCapable(phone)) {
            return;
        }
        MiPhoneAppStub.get().cleanUpAllowedNetworkTypes(phone, i);
        long rafFromNetworkType = RadioAccessFamily.getRafFromNetworkType(RILConstants.PREFERRED_NETWORK_MODE);
        SubscriptionManager.setSubscriptionProperty(i, "allowed_network_types_for_reasons", "user=" + rafFromNetworkType);
        phone.loadAllowedNetworksFromSubscriptionDatabase();
        phone.setAllowedNetworkTypes(0, rafFromNetworkType, (Message) null);
    }

    private void cleanUpSmsRawTable(Context context) {
        context.getContentResolver().delete(Uri.withAppendedPath(Telephony.Sms.CONTENT_URI, "raw/permanentDelete"), null, null);
    }

    public String getSimLocaleForSubscriber(int i) {
        String language;
        enforceReadPrivilegedPermission("getSimLocaleForSubscriber, subId: " + i);
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "getSimLocaleForSubscriber");
        Phone phone = getPhone(i);
        if (phone == null) {
            log("getSimLocaleForSubscriber, invalid subId");
            return null;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            SubscriptionInfo activeSubscriptionInfo = getSubscriptionManagerService().getActiveSubscriptionInfo(i, phone.getContext().getOpPackageName(), phone.getContext().getAttributionTag());
            if (activeSubscriptionInfo == null) {
                log("getSimLocaleForSubscriber, inactive subId: " + i);
                return null;
            }
            int mcc = activeSubscriptionInfo.getMcc();
            Locale localeFromSimAndCarrierPrefs = phone.getLocaleFromSimAndCarrierPrefs();
            if (localeFromSimAndCarrierPrefs == null) {
                language = null;
            } else {
                if (!localeFromSimAndCarrierPrefs.getCountry().isEmpty()) {
                    return matchLocaleFromSupportedLocaleList(phone, localeFromSimAndCarrierPrefs);
                }
                language = localeFromSimAndCarrierPrefs.getLanguage();
            }
            Locale localeFromMccMnc = LocaleUtils.getLocaleFromMccMnc(this.mApp, mcc, activeSubscriptionInfo.getMncString(), language);
            if (localeFromMccMnc != null) {
                return matchLocaleFromSupportedLocaleList(phone, localeFromMccMnc);
            }
            return null;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    String matchLocaleFromSupportedLocaleList(Phone phone, Locale locale) {
        for (String str : LocalePicker.getSupportedLocales(phone.getContext())) {
            if (LocaleList.matchesLanguageAndScript(locale, Locale.forLanguageTag(str)) && TextUtils.equals(locale.getCountry(), Locale.forLanguageTag(str).getCountry())) {
                return str;
            }
        }
        return locale.toLanguageTag();
    }

    private List getActiveSubscriptionInfoListPrivileged() {
        return getSubscriptionManagerService().getActiveSubscriptionInfoList(this.mApp.getOpPackageName(), this.mApp.getAttributionTag(), true);
    }

    public void requestModemActivityInfo(ResultReceiver resultReceiver) {
        enforceModifyPermission();
        if (!this.mApp.getResources().getBoolean(android.R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony", "requestModemActivityInfo");
        }
        WorkSource workSource = getWorkSource(Binder.getCallingUid());
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            sendRequestAsync(37, resultReceiver, null, workSource, Binder.getCallingUid(), workSource.getPackageName(0));
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isModemActivityInfoValid(ModemActivityInfo modemActivityInfo) {
        if (modemActivityInfo == null) {
            return false;
        }
        int timestampMillis = ((int) (modemActivityInfo.getTimestampMillis() - this.mLastModemActivityInfo.getTimestampMillis())) + 50;
        if (modemActivityInfo.isValid()) {
            long j = timestampMillis;
            if (modemActivityInfo.getSleepTimeMillis() <= j && modemActivityInfo.getIdleTimeMillis() <= j) {
                return true;
            }
        }
        return false;
    }

    private void updateLastModemActivityInfo(ModemActivityInfo modemActivityInfo, int i, int i2) {
        int numTxPowerLevels = ModemActivityInfo.getNumTxPowerLevels();
        int[] iArr = new int[numTxPowerLevels];
        int[] transmitTimeMillis = modemActivityInfo.getTransmitTimeMillis(i, i2);
        int[] transmitTimeMillis2 = this.mLastModemActivityInfo.getTransmitTimeMillis(i, i2);
        for (int i3 = 0; i3 < numTxPowerLevels; i3++) {
            iArr[i3] = transmitTimeMillis[i3] + transmitTimeMillis2[i3];
        }
        this.mLastModemActivityInfo.setTransmitTimeMillis(i, i2, iArr);
        this.mLastModemActivityInfo.setReceiveTimeMillis(i, i2, modemActivityInfo.getReceiveTimeMillis(i, i2) + this.mLastModemActivityInfo.getReceiveTimeMillis(i, i2));
    }

    private void updateLastModemActivityInfo(ModemActivityInfo modemActivityInfo, int i) {
        int numTxPowerLevels = ModemActivityInfo.getNumTxPowerLevels();
        int[] iArr = new int[numTxPowerLevels];
        int[] transmitTimeMillis = modemActivityInfo.getTransmitTimeMillis(i);
        int[] transmitTimeMillis2 = this.mLastModemActivityInfo.getTransmitTimeMillis(i);
        for (int i2 = 0; i2 < numTxPowerLevels; i2++) {
            iArr[i2] = transmitTimeMillis[i2] + transmitTimeMillis2[i2];
        }
        this.mLastModemActivityInfo.setTransmitTimeMillis(i, iArr);
        this.mLastModemActivityInfo.setReceiveTimeMillis(i, modemActivityInfo.getReceiveTimeMillis(i) + this.mLastModemActivityInfo.getReceiveTimeMillis(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void mergeModemActivityInfo(ModemActivityInfo modemActivityInfo) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < modemActivityInfo.getSpecificInfoLength(); i++) {
            int specificInfoRat = modemActivityInfo.getSpecificInfoRat(i);
            int specificInfoFrequencyRange = modemActivityInfo.getSpecificInfoFrequencyRange(i);
            boolean z = false;
            for (int i2 = 0; i2 < this.mLastModemActivitySpecificInfo.length; i2++) {
                if (specificInfoRat == this.mLastModemActivityInfo.getSpecificInfoRat(i2) && !z) {
                    if (specificInfoRat == 6) {
                        if (specificInfoFrequencyRange == this.mLastModemActivityInfo.getSpecificInfoFrequencyRange(i2)) {
                            updateLastModemActivityInfo(modemActivityInfo, specificInfoRat, specificInfoFrequencyRange);
                        }
                    } else {
                        updateLastModemActivityInfo(modemActivityInfo, specificInfoRat);
                    }
                    z = true;
                }
            }
            if (!z) {
                arrayList.addAll(Arrays.asList(new ActivityStatsTechSpecificInfo(specificInfoRat, specificInfoFrequencyRange, modemActivityInfo.getTransmitTimeMillis(specificInfoRat, specificInfoFrequencyRange), (int) modemActivityInfo.getReceiveTimeMillis(specificInfoRat, specificInfoFrequencyRange))));
            }
        }
        arrayList.addAll(Arrays.asList(this.mLastModemActivitySpecificInfo));
        ActivityStatsTechSpecificInfo[] activityStatsTechSpecificInfoArr = new ActivityStatsTechSpecificInfo[arrayList.size()];
        this.mLastModemActivitySpecificInfo = activityStatsTechSpecificInfoArr;
        arrayList.toArray(activityStatsTechSpecificInfoArr);
        this.mLastModemActivityInfo.setTimestamp(modemActivityInfo.getTimestampMillis());
        this.mLastModemActivityInfo.setSleepTimeMillis(modemActivityInfo.getSleepTimeMillis() + this.mLastModemActivityInfo.getSleepTimeMillis());
        this.mLastModemActivityInfo.setIdleTimeMillis(modemActivityInfo.getIdleTimeMillis() + this.mLastModemActivityInfo.getIdleTimeMillis());
        this.mLastModemActivityInfo = new ModemActivityInfo(this.mLastModemActivityInfo.getTimestampMillis(), this.mLastModemActivityInfo.getSleepTimeMillis(), this.mLastModemActivityInfo.getIdleTimeMillis(), this.mLastModemActivitySpecificInfo);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ActivityStatsTechSpecificInfo[] deepCopyModemActivitySpecificInfo(ActivityStatsTechSpecificInfo[] activityStatsTechSpecificInfoArr) {
        int length = activityStatsTechSpecificInfoArr.length;
        ActivityStatsTechSpecificInfo[] activityStatsTechSpecificInfoArr2 = new ActivityStatsTechSpecificInfo[length];
        for (int i = 0; i < length; i++) {
            activityStatsTechSpecificInfoArr2[i] = new ActivityStatsTechSpecificInfo(activityStatsTechSpecificInfoArr[i].getRat(), activityStatsTechSpecificInfoArr[i].getFrequencyRange(), activityStatsTechSpecificInfoArr[i].getTransmitTimeMillis(), (int) activityStatsTechSpecificInfoArr[i].getReceiveTimeMillis());
        }
        return activityStatsTechSpecificInfoArr2;
    }

    public ServiceState getServiceStateForSlot(int i, boolean z, boolean z2, String str, String str2) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            loge("getServiceStateForSlot retuning null for invalid slotIndex=" + i);
            return null;
        }
        int subId = phone.getSubId();
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mApp, subId, str, str2, "getServiceStateForSubscriber")) {
            return null;
        }
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.radio.access", "getServiceStateForSubscriber");
        boolean z3 = !z && LocationAccessPolicy.checkLocationPermission(this.mApp, new LocationAccessPolicy.LocationPermissionQuery.Builder().setCallingPackage(str).setCallingFeatureId(str2).setCallingPid(Binder.getCallingPid()).setCallingUid(Binder.getCallingUid()).setMethod("getServiceStateForSlot").setLogAsInfo(true).setMinSdkVersionForFine(29).setMinSdkVersionForCoarse(29).setMinSdkVersionForEnforcement(29).build()) == LocationAccessPolicy.LocationPermissionResult.ALLOWED;
        boolean z4 = !z2 && LocationAccessPolicy.checkLocationPermission(this.mApp, new LocationAccessPolicy.LocationPermissionQuery.Builder().setCallingPackage(str).setCallingFeatureId(str2).setCallingPid(Binder.getCallingPid()).setCallingUid(Binder.getCallingUid()).setMethod("getServiceStateForSlot").setLogAsInfo(true).setMinSdkVersionForCoarse(29).setMinSdkVersionForFine(Integer.MAX_VALUE).setMinSdkVersionForEnforcement(29).build()) == LocationAccessPolicy.LocationPermissionResult.ALLOWED;
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            SubscriptionInfoInternal subscriptionInfoInternal = getSubscriptionManagerService().getSubscriptionInfoInternal(subId);
            if (subscriptionInfoInternal != null && !subscriptionInfoInternal.isActive()) {
                log("getServiceStateForSlot returning null for inactive subId=" + subId);
                return null;
            }
            ServiceState serviceState = phone.getServiceState();
            boolean zContains = phone.getDataServicePackages().contains(str);
            if (!z3 && !zContains) {
                if (z4) {
                    return serviceState.createLocationInfoSanitizedCopy(false);
                }
                return serviceState.createLocationInfoSanitizedCopy(true);
            }
            return serviceState;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public Uri getVoicemailRingtoneUri(PhoneAccountHandle phoneAccountHandle) {
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.calling", "getVoicemailRingtoneUri");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phoneForPhoneAccountHandle = PhoneUtils.getPhoneForPhoneAccountHandle(phoneAccountHandle);
            if (phoneForPhoneAccountHandle == null) {
                phoneForPhoneAccountHandle = getDefaultPhone();
            }
            return VoicemailNotificationSettingsUtil.getRingtoneUri(phoneForPhoneAccountHandle.getContext());
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setVoicemailRingtoneUri(String str, PhoneAccountHandle phoneAccountHandle, Uri uri) {
        Phone defaultPhone = getDefaultPhone();
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        if (!TextUtils.equals(str, ((TelecomManager) defaultPhone.getContext().getSystemService(TelecomManager.class)).getDefaultDialerPackage())) {
            TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, PhoneUtils.getSubIdForPhoneAccountHandle(phoneAccountHandle), "setVoicemailRingtoneUri");
        }
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.calling", "setVoicemailRingtoneUri");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phoneForPhoneAccountHandle = PhoneUtils.getPhoneForPhoneAccountHandle(phoneAccountHandle);
            if (phoneForPhoneAccountHandle != null) {
                defaultPhone = phoneForPhoneAccountHandle;
            }
            VoicemailNotificationSettingsUtil.setRingtoneUri(defaultPhone.getContext(), uri);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isVoicemailVibrationEnabled(PhoneAccountHandle phoneAccountHandle) {
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.calling", "isVoicemailVibrationEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phoneForPhoneAccountHandle = PhoneUtils.getPhoneForPhoneAccountHandle(phoneAccountHandle);
            if (phoneForPhoneAccountHandle == null) {
                phoneForPhoneAccountHandle = getDefaultPhone();
            }
            return VoicemailNotificationSettingsUtil.isVibrationEnabled(phoneForPhoneAccountHandle.getContext());
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setVoicemailVibrationEnabled(String str, PhoneAccountHandle phoneAccountHandle, boolean z) {
        Phone defaultPhone = getDefaultPhone();
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        if (!TextUtils.equals(str, ((TelecomManager) defaultPhone.getContext().getSystemService(TelecomManager.class)).getDefaultDialerPackage())) {
            TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, PhoneUtils.getSubIdForPhoneAccountHandle(phoneAccountHandle), "setVoicemailVibrationEnabled");
        }
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.calling", "setVoicemailVibrationEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phoneForPhoneAccountHandle = PhoneUtils.getPhoneForPhoneAccountHandle(phoneAccountHandle);
            if (phoneForPhoneAccountHandle != null) {
                defaultPhone = phoneForPhoneAccountHandle;
            }
            VoicemailNotificationSettingsUtil.setVibrationEnabled(defaultPhone.getContext(), z);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void enforceReadPrivilegedPermission(String str) {
        this.mApp.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", str);
    }

    private void enforceSendSmsPermission() {
        this.mApp.enforceCallingOrSelfPermission("android.permission.SEND_SMS", null);
    }

    private void enforceInteractAcrossUsersPermission(String str) {
        this.mApp.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS", str);
    }

    private void enforceVisualVoicemailPackage(String str, int i) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ComponentName remotePackage = RemoteVvmTaskManager.getRemotePackage(this.mApp, i);
            if (remotePackage == null) {
                throw new SecurityException("Caller not current active visual voicemail package[null]");
            }
            if (!str.equals(remotePackage.getPackageName())) {
                throw new SecurityException("Caller not current active visual voicemail package");
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public String getAidForAppType(int i, int i2) {
        enforceReadPrivilegedPermission("getAidForAppType");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "getAidForAppType");
        Phone phone = getPhone(i);
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        String aid = null;
        if (phone != null) {
            try {
                try {
                    UiccCardApplication applicationByType = UiccController.getInstance().getUiccPort(phone.getPhoneId()).getApplicationByType(i2);
                    if (applicationByType == null) {
                        return null;
                    }
                    aid = applicationByType.getAid();
                } catch (Exception e) {
                    Log.e("PhoneInterfaceManager", "Not getting aid", e);
                }
                return aid;
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }
        Binder.restoreCallingIdentity(jClearCallingIdentity);
        return null;
    }

    public List getTelephonyHistograms() {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, getDefaultSubscription(), "getTelephonyHistograms");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return RIL.getTelephonyRILTimingHistograms();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int setAllowedCarriers(CarrierRestrictionRules carrierRestrictionRules) {
        if (TelephonyPermissions.isShell(Binder.getCallingUid())) {
            ImsRcsController$$ExternalSyntheticBUOutline0.m("setAllowedCarriers cannot be invoked by shell");
            return 0;
        }
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.carrierlock", "setAllowedCarriers");
        WorkSource workSource = getWorkSource(Binder.getCallingUid());
        if (carrierRestrictionRules == null) {
            GenericObject$$ExternalSyntheticBUOutline0.m("carrier restriction cannot be null");
            return 0;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return ((Integer) sendRequest(43, carrierRestrictionRules, workSource)).intValue();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public CarrierRestrictionRules getAllowedCarriers() {
        enforceReadPrivilegedPermission("getAllowedCarriers");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.carrierlock", "getAllowedCarriers");
        WorkSource workSource = getWorkSource(Binder.getCallingUid());
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Object objSendRequest = sendRequest(45, (Object) null, workSource);
            if (objSendRequest instanceof CarrierRestrictionRules) {
                return (CarrierRestrictionRules) objSendRequest;
            }
            return null;
        } catch (Exception e) {
            Log.e("PhoneInterfaceManager", "getAllowedCarriers. Exception ex=" + e);
            return null;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
        Binder.restoreCallingIdentity(jClearCallingIdentity);
    }

    public void getCarrierRestrictionStatus(IIntegerConsumer iIntegerConsumer, String str) {
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.subscription", "getCarrierRestrictionStatus");
        try {
            this.mApp.enforceCallingOrSelfPermission("android.permission.READ_BASIC_PHONE_STATE", "getCarrierRestrictionStatus");
        } catch (SecurityException unused) {
            this.mApp.enforceCallingOrSelfPermission("android.permission.READ_PHONE_STATE", "getCarrierRestrictionStatus");
        }
        Set setValidateCallerAndGetCarrierIds = validateCallerAndGetCarrierIds(str);
        if (setValidateCallerAndGetCarrierIds.contains(-1)) {
            Rlog.e("PhoneInterfaceManager", "getCarrierRestrictionStatus: caller is not registered");
            ImsRcsController$$ExternalSyntheticBUOutline0.m("Not an authorized caller");
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Objects.requireNonNull(iIntegerConsumer);
            sendRequestAsync(45, new CallerCallbackInfo(FunctionalUtils.ignoreRemoteException(new PhoneInterfaceManager$$ExternalSyntheticLambda5(iIntegerConsumer)), setValidateCallerAndGetCarrierIds));
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public List getShaIdFromAllowList(String str, int i) {
        enforceReadPrivilegedPermission("checkCarrierRestrictionFileForNoChange");
        return CarrierAllowListInfo.loadInstance(this.mApp).getShaIdList(str, i);
    }

    public Set validateCallerAndGetCarrierIds(String str) {
        return CarrierAllowListInfo.loadInstance(this.mApp).validateCallerAndGetCarrierIds(str);
    }

    public void carrierActionSetRadioEnabled(int i, boolean z) {
        enforceModifyPermission();
        Phone phone = getPhone(i);
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        if (phone == null) {
            loge("carrierAction: SetRadioEnabled fails with invalid sibId: " + i);
            return;
        }
        try {
            phone.carrierActionSetRadioEnabled(z);
        } catch (Exception e) {
            Log.e("PhoneInterfaceManager", "carrierAction: SetRadioEnabled fails. Exception ex=" + e);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int setVoNrEnabled(int i, boolean z) {
        enforceModifyPermission();
        Phone phone = getPhone(i);
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        if (phone == null) {
            loge("setVoNrEnabled fails with no phone object for subId: " + i);
            return 2;
        }
        try {
            try {
                int slotIndexOrException = getSlotIndexOrException(i);
                log("setVoNrEnabled: enable VoNR: " + z);
                if (ImsManager.getInstance(this.mApp, slotIndexOrException).setVonrSetting(z)) {
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                    return 0;
                }
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return 5;
            } catch (ImsException e) {
                loge("setVoNrEnabled, couldn't enable VoNR: " + e);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return 5;
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public boolean isVoNrEnabled(int i) {
        enforceReadPrivilegedPermission("isVoNrEnabled");
        int integerSubscriptionProperty = SubscriptionManager.getIntegerSubscriptionProperty(i, "nr_advanced_calling_enabled", -1, this.mApp);
        boolean z = this.mApp.getCarrierConfigForSubId(i).getBoolean("vonr_enabled_bool");
        log("isVoNrEnabled, onByDefault: " + z + ", setting: " + integerSubscriptionProperty);
        if (integerSubscriptionProperty == -1) {
            return z;
        }
        return integerSubscriptionProperty == 1;
    }

    public void carrierActionReportDefaultNetworkStatus(int i, boolean z) {
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.radio.access", "carrierActionReportDefaultNetworkStatus");
        Phone phone = getPhone(i);
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (phone == null) {
                loge("carrierAction: ReportDefaultNetworkStatus fails with invalid sibId: " + i);
                return;
            }
            phone.carrierActionReportDefaultNetworkStatus(z);
        } catch (Exception e) {
            Log.e("PhoneInterfaceManager", "carrierAction: ReportDefaultNetworkStatus fails. Exception ex=" + e);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void carrierActionResetAll(int i) {
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "carrierActionResetAll");
        Phone phone = getPhone(i);
        if (phone == null) {
            loge("carrierAction: ResetAll fails with invalid sibId: " + i);
        } else {
            try {
                phone.carrierActionResetAll();
            } catch (Exception e) {
                Log.e("PhoneInterfaceManager", "carrierAction: ResetAll fails. Exception ex=" + e);
            }
        }
    }

    protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (this.mApp.checkCallingOrSelfPermission("android.permission.DUMP") != 0) {
            printWriter.println("Permission Denial: can't dump Phone from pid=" + Binder.getCallingPid() + ", uid=" + Binder.getCallingUid() + "without permission android.permission.DUMP");
            return;
        }
        MiPhoneAppStub.get().dump(this.mApp, fileDescriptor, printWriter, strArr);
        try {
            DumpsysHandler.dump(this.mApp, fileDescriptor, printWriter, strArr);
        } catch (Exception e) {
            printWriter.println("Failed to dump phone information: " + e);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int handleShellCommand(ParcelFileDescriptor parcelFileDescriptor, ParcelFileDescriptor parcelFileDescriptor2, ParcelFileDescriptor parcelFileDescriptor3, String[] strArr) {
        Log.d("PhoneInterfaceManager", "handleShellCommand");
        return this.mTelephonyShellCommand.exec(this, parcelFileDescriptor.getFileDescriptor(), parcelFileDescriptor2.getFileDescriptor(), parcelFileDescriptor3.getFileDescriptor(), strArr);
    }

    public void setDataEnabledForReason(int i, int i2, boolean z, String str) {
        String str2;
        if (i2 == 0 || i2 == 2) {
            try {
                TelephonyPermissions.enforceCallingOrSelfCarrierPrivilege(this.mApp, i, "setDataEnabledForReason");
            } catch (SecurityException unused) {
                enforceModifyPermission();
            }
        } else {
            enforceModifyPermission();
        }
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.data", "setDataEnabledForReason");
        int callingUid = Binder.getCallingUid();
        if (MiPhoneAppStub.get().setDataEnabledForReason(this.mApp, i2, z, str)) {
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        if (i2 == 0 && z && str != null) {
            try {
                str2 = str;
                this.mAppOps.noteOpNoThrow("android:enable_mobile_data_by_user", callingUid, str2, null, null);
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        } else {
            str2 = str;
        }
        Phone phone = getPhone(i);
        if (phone != null) {
            if (i2 == 2) {
                phone.carrierActionSetMeteredApnsEnabled(z);
            } else if (phone.getDataSettingsManager() != null) {
                phone.getDataSettingsManager().setDataEnabled(i2, z, str2);
            }
        }
    }

    public List getClientRequestStats(String str, String str2, int i) {
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mApp, i, str, str2, "getClientRequestStats")) {
            return null;
        }
        Phone phone = getPhone(i);
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        if (phone != null) {
            try {
                return phone.getClientRequestStats();
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }
        Binder.restoreCallingIdentity(jClearCallingIdentity);
        return null;
    }

    private WorkSource getWorkSource(int i) {
        String nameForUid = this.mApp.getBaseContext().createContextAsUser(UserHandle.getUserHandleForUid(i), 0).getPackageManager().getNameForUid(i);
        if (UserHandle.isSameApp(i, 0) && nameForUid == null) {
            nameForUid = "root";
        }
        return new WorkSource(i, nameForUid);
    }

    public void setSimPowerStateForSlot(int i, int i2) {
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "setSimPowerStateForSlot");
        Phone phone = PhoneFactory.getPhone(i);
        WorkSource workSource = getWorkSource(Binder.getCallingUid());
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        if (phone != null) {
            try {
                phone.setSimPowerState(i2, (Message) null, workSource);
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }
    }

    public void setSimPowerStateForSlotWithCallback(int i, int i2, IIntegerConsumer iIntegerConsumer) {
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "setSimPowerStateForSlotWithCallback");
        Phone phone = PhoneFactory.getPhone(i);
        WorkSource workSource = getWorkSource(Binder.getCallingUid());
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        if (phone != null) {
            try {
                sendRequestAsync(101, Pair.create(Integer.valueOf(i2), iIntegerConsumer), phone, workSource);
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isUssdApiAllowed(int i) {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mApp.getSystemService("carrier_config");
        if (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(i)) == null) {
            return false;
        }
        return configForSubId.getBoolean("allow_ussd_requests_via_telephony_manager_bool");
    }

    public boolean getEmergencyCallbackMode(int i) {
        enforceReadPrivilegedPermission("getEmergencyCallbackMode");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.calling", "getEmergencyCallbackMode");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return getPhoneFromSubIdOrDefault(i).isInEcm();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public SignalStrength getSignalStrength(int i) {
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.radio.access", "getSignalStrength");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone != null) {
                return phone.getSignalStrength();
            }
            return null;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getRadioPowerState(int i, String str, String str2) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null || !TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mApp, phone.getSubId(), str, str2, "getRadioPowerState")) {
            return 2;
        }
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.radio.access", "getRadioPowerState");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return phone.getRadioPowerState();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isDataRoamingEnabled(int i) {
        try {
            try {
                this.mApp.enforceCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE", "isDataRoamingEnabled");
            } catch (SecurityException unused) {
                TelephonyPermissions.enforceCallingOrSelfReadPhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "isDataRoamingEnabled");
            }
        } catch (SecurityException unused2) {
            this.mApp.enforceCallingOrSelfPermission("android.permission.READ_BASIC_PHONE_STATE", "isDataRoamingEnabled");
        }
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.data", "isDataRoamingEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            return phone != null ? phone.getDataRoamingEnabled() : false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setDataRoamingEnabled(int i, boolean z) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "setDataRoamingEnabled");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.data", "setDataRoamingEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone != null) {
                phone.setDataRoamingEnabled(z);
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isManualNetworkSelectionAllowed(int i) {
        TelephonyPermissions.enforceCallingOrSelfReadPrivilegedPhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "isManualNetworkSelectionAllowed");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.radio.access", "isManualNetworkSelectionAllowed");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            return phone != null ? phone.isCspPlmnEnabled() : true;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private boolean haveCarrierPrivilegeAccess(UiccPort uiccPort, String str) {
        Phone phone;
        CarrierPrivilegesTracker carrierPrivilegesTracker;
        UiccProfile uiccProfile = uiccPort.getUiccProfile();
        return (uiccProfile == null || (phone = PhoneFactory.getPhone(uiccProfile.getPhoneId())) == null || (carrierPrivilegesTracker = phone.getCarrierPrivilegesTracker()) == null || carrierPrivilegesTracker.getCarrierPrivilegeStatusForPackage(str) != 1) ? false : true;
    }

    public List getUiccCardsInfo(String str) {
        boolean z;
        boolean z2;
        ((AppOpsManager) this.mApp.getSystemService(AppOpsManager.class)).checkPackage(Binder.getCallingUid(), str);
        boolean z3 = true;
        boolean z4 = false;
        try {
            enforceReadPrivilegedPermission("getUiccCardsInfo");
            z = true;
            z2 = false;
        } catch (SecurityException unused) {
            if (checkCarrierPrivilegesForPackageAnyPhoneWithPermission(str) == 1) {
                z = false;
                z2 = false;
            } else {
                if (!TelephonyPermissions.checkCallingOrSelfReadNonDangerousPhoneStateNoThrow(this.mApp, "getUiccCardsInfo")) {
                    ImsRcsController$$ExternalSyntheticBUOutline0.m("Caller does not have permission.");
                    return null;
                }
                z2 = true;
                z = false;
            }
        }
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.subscription", "getUiccCardsInfo");
        boolean zIsChangeEnabled = CompatChanges.isChangeEnabled(202110963L, Binder.getCallingUid());
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            UiccController uiccController = UiccController.getInstance();
            ArrayList allUiccCardInfos = uiccController.getAllUiccCardInfos();
            if (z) {
                return allUiccCardInfos;
            }
            ArrayList arrayList = new ArrayList();
            int size = allUiccCardInfos.size();
            int i = 0;
            while (i < size) {
                Object obj = allUiccCardInfos.get(i);
                i++;
                UiccCardInfo uiccCardInfo = (UiccCardInfo) obj;
                uiccCardInfo.setIccIdAccessRestricted(zIsChangeEnabled);
                if (z2) {
                    arrayList.add(uiccCardInfo.createSensitiveInfoSanitizedCopy(z4));
                } else if (uiccController.getUiccCardForSlot(uiccCardInfo.getPhysicalSlotIndex()) == null) {
                    arrayList.add(uiccCardInfo.createSensitiveInfoSanitizedCopy(z3));
                } else {
                    Collection<UiccPortInfo> ports = uiccCardInfo.getPorts();
                    if (ports.isEmpty()) {
                        arrayList.add(uiccCardInfo.createSensitiveInfoSanitizedCopy(z3));
                    } else {
                        ArrayList arrayList2 = new ArrayList();
                        for (UiccPortInfo uiccPortInfo : ports) {
                            boolean z5 = zIsChangeEnabled;
                            UiccPort uiccPortForSlot = uiccController.getUiccPortForSlot(uiccCardInfo.getPhysicalSlotIndex(), uiccPortInfo.getPortIndex());
                            if (uiccPortForSlot == null) {
                                arrayList2.add(uiccPortInfo.createSensitiveInfoSanitizedCopy());
                            } else if (haveCarrierPrivilegeAccess(uiccPortForSlot, str)) {
                                arrayList2.add(uiccPortInfo);
                            } else {
                                arrayList2.add(uiccPortInfo.createSensitiveInfoSanitizedCopy());
                            }
                            zIsChangeEnabled = z5;
                        }
                        boolean z6 = zIsChangeEnabled;
                        arrayList.add(new UiccCardInfo(uiccCardInfo.isEuicc(), uiccCardInfo.getCardId(), null, uiccCardInfo.getPhysicalSlotIndex(), uiccCardInfo.isRemovable(), uiccCardInfo.isMultipleEnabledProfilesSupported(), arrayList2));
                        zIsChangeEnabled = z6;
                        z3 = true;
                        z4 = false;
                    }
                }
            }
            return arrayList;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public UiccSlotInfo[] getUiccSlotsInfo(String str) throws Throwable {
        long j;
        String eid;
        int i;
        long j2;
        PhoneInterfaceManager phoneInterfaceManager = this;
        String str2 = str;
        ((AppOpsManager) phoneInterfaceManager.mApp.getSystemService(AppOpsManager.class)).checkPackage(Binder.getCallingUid(), str2);
        phoneInterfaceManager.enforceReadPrivilegedPermission("getUiccSlotsInfo");
        if (!phoneInterfaceManager.mApp.getResources().getBoolean(android.R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            phoneInterfaceManager.enforceTelephonyFeatureWithException(str2, "android.hardware.telephony.subscription", "getUiccSlotsInfo");
        }
        boolean zIsChangeEnabled = CompatChanges.isChangeEnabled(202110963L, Binder.getCallingUid());
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            UiccSlot[] uiccSlots = UiccController.getInstance().getUiccSlots();
            try {
                if (uiccSlots == null || uiccSlots.length == 0) {
                    Rlog.i("PhoneInterfaceManager", "slots is null or empty.");
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                    return null;
                }
                UiccSlotInfo[] uiccSlotInfoArr = new UiccSlotInfo[uiccSlots.length];
                int i2 = 0;
                int i3 = 0;
                while (i3 < uiccSlots.length) {
                    UiccSlot uiccSlot = uiccSlots[i3];
                    if (uiccSlot == null) {
                        j2 = jClearCallingIdentity;
                    } else {
                        UiccCard uiccCard = uiccSlot.getUiccCard();
                        if (uiccCard != null) {
                            eid = uiccCard.getCardId();
                        } else {
                            eid = uiccSlot.getEid();
                            if (TextUtils.isEmpty(eid)) {
                                eid = uiccSlot.getIccId(i2);
                            }
                        }
                        if (eid != null) {
                            eid = IccUtils.stripTrailingFs(eid);
                        }
                        String str3 = eid;
                        int i4 = AnonymousClass5.$SwitchMap$com$android$internal$telephony$uicc$IccCardStatus$CardState[uiccSlot.getCardState().ordinal()];
                        boolean z = true;
                        if (i4 != 1) {
                            i = 2;
                            if (i4 != 2) {
                                i = 3;
                                if (i4 != 3) {
                                    i = 4;
                                    if (i4 != 4) {
                                        i = i2;
                                    }
                                }
                            }
                        } else {
                            i = 1;
                        }
                        ArrayList arrayList = new ArrayList();
                        int[] portList = uiccSlot.getPortList();
                        int length = portList.length;
                        while (i2 < length) {
                            long j3 = jClearCallingIdentity;
                            int i5 = portList[i2];
                            arrayList.add(new UiccPortInfo(IccUtils.stripTrailingFs(phoneInterfaceManager.getIccId(uiccSlot, i5, str2, z)), i5, uiccSlot.getPhoneIdFromPortIndex(i5), uiccSlot.isPortActive(i5)));
                            i2++;
                            phoneInterfaceManager = this;
                            str2 = str;
                            jClearCallingIdentity = j3;
                            z = true;
                        }
                        j2 = jClearCallingIdentity;
                        UiccSlotInfo uiccSlotInfo = new UiccSlotInfo(uiccSlot.isEuicc(), str3, i, uiccSlot.isExtendedApduSupported(), uiccSlot.isRemovable(), arrayList, uiccSlot.getSimType(), uiccSlot.getSupportedSimTypes());
                        uiccSlotInfoArr[i3] = uiccSlotInfo;
                        uiccSlotInfo.setLogicalSlotAccessRestricted(zIsChangeEnabled);
                    }
                    i3++;
                    phoneInterfaceManager = this;
                    str2 = str;
                    jClearCallingIdentity = j2;
                    i2 = 0;
                }
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return uiccSlotInfoArr;
            } catch (Throwable th) {
                th = th;
            }
        } catch (Throwable th2) {
            th = th2;
            j = jClearCallingIdentity;
        }
        Binder.restoreCallingIdentity(j);
        throw th;
    }

    /* JADX INFO: renamed from: com.android.phone.PhoneInterfaceManager$5, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$android$telephony$LocationAccessPolicy$LocationPermissionResult;
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$CommandException$Error;
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$uicc$IccCardStatus$CardState;

        static {
            int[] iArr = new int[IccCardStatus.CardState.values().length];
            $SwitchMap$com$android$internal$telephony$uicc$IccCardStatus$CardState = iArr;
            try {
                iArr[IccCardStatus.CardState.CARDSTATE_ABSENT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardStatus$CardState[IccCardStatus.CardState.CARDSTATE_PRESENT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardStatus$CardState[IccCardStatus.CardState.CARDSTATE_ERROR.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardStatus$CardState[IccCardStatus.CardState.CARDSTATE_RESTRICTED.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            int[] iArr2 = new int[LocationAccessPolicy.LocationPermissionResult.values().length];
            $SwitchMap$android$telephony$LocationAccessPolicy$LocationPermissionResult = iArr2;
            try {
                iArr2[LocationAccessPolicy.LocationPermissionResult.DENIED_HARD.ordinal()] = 1;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$android$telephony$LocationAccessPolicy$LocationPermissionResult[LocationAccessPolicy.LocationPermissionResult.DENIED_SOFT.ordinal()] = 2;
            } catch (NoSuchFieldError unused6) {
            }
            int[] iArr3 = new int[CommandException.Error.values().length];
            $SwitchMap$com$android$internal$telephony$CommandException$Error = iArr3;
            try {
                iArr3[CommandException.Error.REQUEST_NOT_SUPPORTED.ordinal()] = 1;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.RADIO_NOT_AVAILABLE.ordinal()] = 2;
            } catch (NoSuchFieldError unused8) {
            }
        }
    }

    private String getIccId(UiccSlot uiccSlot, int i, String str, boolean z) {
        return (!z && (uiccSlot.getUiccCard() == null || uiccSlot.getUiccCard().getUiccPort(i) == null || !haveCarrierPrivilegeAccess(uiccSlot.getUiccCard().getUiccPort(i), str))) ? "FFFFFFFFFFFFFFFFFFFF" : uiccSlot.getIccId(i);
    }

    public boolean switchSlots(int[] iArr) {
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "switchSlots");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < iArr.length; i++) {
                arrayList.add(new UiccSlotMapping(0, iArr[i], i));
            }
            return ((Boolean) sendRequest(50, arrayList)).booleanValue();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setSimSlotMapping(List list) {
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "setSimSlotMapping");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return ((Boolean) sendRequest(50, list)).booleanValue();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getCardIdForDefaultEuicc(int i, String str) {
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.euicc", "getCardIdForDefaultEuicc");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return UiccController.getInstance().getCardIdForDefaultEuicc();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void refreshUiccProfile(int i) {
        enforceModifyPermission();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone == null) {
                return;
            }
            UiccPort uiccPort = phone.getUiccPort();
            if (uiccPort == null) {
                return;
            }
            UiccProfile uiccProfile = uiccPort.getUiccProfile();
            if (uiccProfile == null) {
                return;
            }
            uiccProfile.refresh();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private boolean getDefaultDataEnabled() {
        return ((Boolean) TelephonyProperties.mobile_data().orElse(Boolean.TRUE)).booleanValue();
    }

    public void setCarrierTestOverride(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9) {
        enforceModifyPermission();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone == null) {
                loge("setCarrierTestOverride fails with invalid subId: " + i);
                return;
            }
            CarrierPrivilegesTracker carrierPrivilegesTracker = phone.getCarrierPrivilegesTracker();
            if (carrierPrivilegesTracker != null) {
                carrierPrivilegesTracker.setTestOverrideCarrierPrivilegeRules(str8);
            }
            phone.setCarrierTestOverride(str, str2, str3, str4, str5, str6, str7, str8, str9);
            Set set = this.mCarrierPrivilegeTestOverrideSubIds;
            if (str8 == null) {
                set.remove(Integer.valueOf(i));
            } else {
                set.add(Integer.valueOf(i));
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setCarrierServicePackageOverride(int i, String str, String str2) {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setCarrierServicePackageOverride");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone == null || phone.getSubId() != i) {
                loge("setCarrierServicePackageOverride fails with invalid subId: " + i);
                throw new IllegalArgumentException("No phone for subid");
            }
            CarrierPrivilegesTracker carrierPrivilegesTracker = phone.getCarrierPrivilegesTracker();
            if (carrierPrivilegesTracker == null) {
                loge("setCarrierServicePackageOverride failed with no CPT for phone");
                throw new IllegalStateException("No CPT for phone");
            }
            carrierPrivilegesTracker.setTestOverrideCarrierServicePackage(str);
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public int getCarrierIdListVersion(int i) {
        enforceReadPrivilegedPermission("getCarrierIdListVersion");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone == null) {
                loge("getCarrierIdListVersion fails with invalid subId: " + i);
                return -1;
            }
            return phone.getCarrierIdListVersion();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getNumberOfModemsWithSimultaneousDataConnections(int i, String str, String str2) {
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mApp, i, str, str2, "getNumberOfModemsWithSimultaneousDataConnections")) {
            return -1;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mPhoneConfigurationManager.getNumberOfModemsWithSimultaneousDataConnections();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public Map getEmergencyNumberList(String str, String str2) {
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mApp, getDefaultSubscription(), str, str2, "getEmergencyNumberList")) {
            ImsRcsController$$ExternalSyntheticBUOutline0.m("Requires READ_PHONE_STATE permission.");
            return null;
        }
        enforceTelephonyFeatureWithException(str, Arrays.asList("android.hardware.telephony.calling", "android.hardware.telephony.messaging"), "getEmergencyNumberList");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            HashMap map = new HashMap();
            for (Phone phone : PhoneFactory.getPhones()) {
                if (phone.getEmergencyNumberTracker() != null && phone.getEmergencyNumberTracker().getEmergencyNumberList() != null) {
                    map.put(Integer.valueOf(phone.getSubId()), phone.getEmergencyNumberTracker().getEmergencyNumberList());
                }
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            return map;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public boolean isEmergencyNumber(String str, boolean z) {
        Phone defaultPhone = getDefaultPhone();
        if (!z) {
            TelephonyPermissions.enforceCallingOrSelfReadPrivilegedPhoneStatePermissionOrCarrierPrivilege(this.mApp, defaultPhone.getSubId(), "isEmergencyNumber(Potential)");
        }
        if (!this.mApp.getResources().getBoolean(android.R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(getCurrentPackageName(), Arrays.asList("android.hardware.telephony.calling", "android.hardware.telephony.messaging"), "isEmergencyNumber");
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            for (Phone phone : PhoneFactory.getPhones()) {
                if (!MiPhoneAppStub.get().isIgnoreEmergencyNumber(phone, str) && phone.getEmergencyNumberTracker() != null && phone.getEmergencyNumberTracker().isEmergencyNumber(str)) {
                    return true;
                }
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void startEmergencyCallbackMode() {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "startEmergencyCallbackMode");
        enforceModifyPermission();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            for (GsmCdmaPhone gsmCdmaPhone : PhoneFactory.getPhones()) {
                Rlog.d("PhoneInterfaceManager", "startEmergencyCallbackMode phone type: " + gsmCdmaPhone.getPhoneType());
                gsmCdmaPhone.obtainMessage(25).sendToTarget();
                Rlog.d("PhoneInterfaceManager", "startEmergencyCallbackMode: triggered");
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void updateEmergencyNumberListTestMode(int i, EmergencyNumber emergencyNumber) {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "updateEmergencyNumberListTestMode");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            for (Phone phone : PhoneFactory.getPhones()) {
                EmergencyNumberTracker emergencyNumberTracker = phone.getEmergencyNumberTracker();
                if (emergencyNumberTracker != null) {
                    emergencyNumberTracker.executeEmergencyNumberTestModeCommand(i, emergencyNumber);
                }
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public List getEmergencyNumberListTestMode() {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "getEmergencyNumberListTestMode");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            HashSet hashSet = new HashSet();
            for (Phone phone : PhoneFactory.getPhones()) {
                EmergencyNumberTracker emergencyNumberTracker = phone.getEmergencyNumberTracker();
                if (emergencyNumberTracker != null) {
                    Iterator it = emergencyNumberTracker.getEmergencyNumberList().iterator();
                    while (it.hasNext()) {
                        hashSet.add(((EmergencyNumber) it.next()).getNumber());
                    }
                }
            }
            return new ArrayList(hashSet);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getEmergencyNumberDbVersion(int i) {
        enforceReadPrivilegedPermission("getEmergencyNumberDbVersion");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), Arrays.asList("android.hardware.telephony.calling", "android.hardware.telephony.messaging"), "getEmergencyNumberDbVersion");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone == null) {
                loge("getEmergencyNumberDbVersion fails with invalid subId: " + i);
                return -1;
            }
            return phone.getEmergencyNumberDbVersion();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void notifyOtaEmergencyNumberDbInstalled() {
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), Arrays.asList("android.hardware.telephony.calling", "android.hardware.telephony.messaging"), "notifyOtaEmergencyNumberDbInstalled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            for (Phone phone : PhoneFactory.getPhones()) {
                EmergencyNumberTracker emergencyNumberTracker = phone.getEmergencyNumberTracker();
                if (emergencyNumberTracker != null) {
                    emergencyNumberTracker.updateOtaEmergencyNumberDatabase();
                }
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void updateOtaEmergencyNumberDbFilePath(ParcelFileDescriptor parcelFileDescriptor) {
        enforceActiveEmergencySessionPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), Arrays.asList("android.hardware.telephony.calling", "android.hardware.telephony.messaging"), "updateOtaEmergencyNumberDbFilePath");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            for (Phone phone : PhoneFactory.getPhones()) {
                EmergencyNumberTracker emergencyNumberTracker = phone.getEmergencyNumberTracker();
                if (emergencyNumberTracker != null) {
                    emergencyNumberTracker.updateOtaEmergencyNumberDbFilePath(parcelFileDescriptor);
                }
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void resetOtaEmergencyNumberDbFilePath() {
        enforceActiveEmergencySessionPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), Arrays.asList("android.hardware.telephony.calling", "android.hardware.telephony.messaging"), "resetOtaEmergencyNumberDbFilePath");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            for (Phone phone : PhoneFactory.getPhones()) {
                EmergencyNumberTracker emergencyNumberTracker = phone.getEmergencyNumberTracker();
                if (emergencyNumberTracker != null) {
                    emergencyNumberTracker.resetOtaEmergencyNumberDbFilePath();
                }
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public List getCertsFromCarrierPrivilegeAccessRules(int i) {
        enforceReadPrivilegedPermission("getCertsFromCarrierPrivilegeAccessRules");
        Phone phone = getPhone(i);
        if (phone == null) {
            return null;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            UiccProfile uiccProfileForPhone = UiccController.getInstance().getUiccProfileForPhone(phone.getPhoneId());
            if (uiccProfileForPhone != null) {
                return uiccProfileForPhone.getCertsFromCarrierPrivilegeAccessRules();
            }
            return null;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean enableModemForSlot(int i, boolean z) {
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony", "enableModemForSlot");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = PhoneFactory.getPhone(i);
            if (phone != null) {
                return ((Boolean) sendRequest(68, Boolean.valueOf(z), phone, (WorkSource) null)).booleanValue();
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isModemEnabledForSlot(int i, String str, String str2) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            return false;
        }
        if (TelephonyPermissions.checkCallingOrSelfReadNonDangerousPhoneStateNoThrow(this.mApp, "isModemEnabledForSlot") || TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mApp, phone.getSubId(), str, str2, "isModemEnabledForSlot")) {
            enforceTelephonyFeatureWithException(str, "android.hardware.telephony", "isModemEnabledForSlot");
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return this.mPhoneConfigurationManager.getPhoneStatusFromCache(phone.getPhoneId());
            } catch (NoSuchElementException unused) {
                return ((Boolean) sendRequest(70, (Object) null, phone, (WorkSource) null)).booleanValue();
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }
        ImsRcsController$$ExternalSyntheticBUOutline0.m("Caller has no permission.");
        return false;
    }

    public void setMultiSimCarrierRestriction(boolean z) {
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.carrierlock", "setMultiSimCarrierRestriction");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mTelephonySharedPreferences.edit().putBoolean("multisim_restricted", z).commit();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int isMultiSimSupported(String str, String str2) {
        if (!TelephonyPermissions.checkCallingOrSelfReadNonDangerousPhoneStateNoThrow(this.mApp, "isMultiSimSupported") && !TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mApp, getDefaultPhone().getSubId(), str, str2, "isMultiSimSupported")) {
            ImsRcsController$$ExternalSyntheticBUOutline0.m("Caller does not have permission.");
            return 0;
        }
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.subscription", "isMultiSimSupported");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return isMultiSimSupportedInternal();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private int isMultiSimSupportedInternal() {
        boolean z;
        UiccSlot[] uiccSlots = UiccController.getInstance().getUiccSlots();
        if (uiccSlots == null) {
            loge("isMultiSimSupportedInternal: slots is null");
            return 1;
        }
        int length = uiccSlots.length;
        int i = 0;
        while (true) {
            if (i < length) {
                UiccSlot uiccSlot = uiccSlots[i];
                if (uiccSlot != null && uiccSlot.isMultipleEnabledProfileSupported()) {
                    z = true;
                    break;
                }
                i++;
            } else {
                z = false;
                break;
            }
        }
        if (uiccSlots.length < 2 && !z) {
            loge("isMultiSimSupportedInternal: requires at least 2 cards or MEP support");
            return 1;
        }
        PhoneCapability staticPhoneCapability = this.mPhoneConfigurationManager.getStaticPhoneCapability();
        if (staticPhoneCapability == null) {
            loge("isMultiSimSupportedInternal: no static configuration available");
            return 1;
        }
        if (staticPhoneCapability.getLogicalModemList().size() >= 2) {
            return this.mTelephonySharedPreferences.getBoolean("multisim_restricted", false) ? 2 : 0;
        }
        loge("isMultiSimSupportedInternal: maximum number of modem is < 2");
        return 1;
    }

    public void switchMultiSimConfig(int i) {
        if (i != 1) {
            TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, Integer.MAX_VALUE, "switchMultiSimConfig");
        } else {
            enforceModifyPermission();
        }
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "switchMultiSimConfig");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (isMultiSimSupportedInternal() != 0) {
                loge("switchMultiSimConfig not possible. It is restricted or not supported.");
            } else {
                this.mPhoneConfigurationManager.switchMultiSimConfig(i);
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isApplicationOnUicc(int i, int i2) {
        enforceReadPrivilegedPermission("isApplicationOnUicc");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "isApplicationOnUicc");
        Phone phone = getPhone(i);
        if (phone == null) {
            return false;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            UiccPort uiccPort = phone.getUiccPort();
            if (uiccPort == null) {
                return false;
            }
            UiccProfile uiccProfile = uiccPort.getUiccProfile();
            if (uiccProfile == null) {
                return false;
            }
            if (1 > i2 || i2 > 5) {
                return false;
            }
            return uiccProfile.isApplicationOnIcc(IccCardApplicationStatus.AppType.values()[i2]);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean doesSwitchMultiSimConfigTriggerReboot(int i, String str, String str2) {
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mApp, i, str, str2, "doesSwitchMultiSimConfigTriggerReboot")) {
            return false;
        }
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.subscription", "doesSwitchMultiSimConfigTriggerReboot");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mPhoneConfigurationManager.isRebootRequiredForModemConfigChange();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public List getSlotsMapping(String str) {
        enforceReadPrivilegedPermission("getSlotsMapping");
        ((AppOpsManager) this.mApp.getSystemService(AppOpsManager.class)).checkPackage(Binder.getCallingUid(), str);
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.subscription", "getSlotsMapping");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        ArrayList arrayList = new ArrayList();
        try {
            UiccSlotInfo[] uiccSlotsInfo = getUiccSlotsInfo(this.mApp.getOpPackageName());
            if (uiccSlotsInfo != null) {
                for (int i = 0; i < uiccSlotsInfo.length; i++) {
                    UiccSlotInfo uiccSlotInfo = uiccSlotsInfo[i];
                    if (uiccSlotInfo != null) {
                        for (UiccPortInfo uiccPortInfo : uiccSlotInfo.getPorts()) {
                            if (SubscriptionManager.isValidPhoneId(uiccPortInfo.getLogicalSlotIndex())) {
                                arrayList.add(new UiccSlotMapping(uiccPortInfo.getPortIndex(), i, uiccPortInfo.getLogicalSlotIndex(), uiccSlotInfo.getSimType()));
                            }
                        }
                    } else {
                        loge("getSlotsMapping, SlotInfo[ " + i + " ] is null, which is not expected");
                    }
                }
            }
            return arrayList;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getRadioHalVersion() {
        return getHalVersion(0);
    }

    public int getHalVersion(int i) {
        Phone defaultPhone = getDefaultPhone();
        if (defaultPhone == null) {
            return -1;
        }
        HalVersion halVersion = defaultPhone.getHalVersion(i);
        if (halVersion.equals(HalVersion.UNKNOWN)) {
            return -1;
        }
        return (halVersion.major * 100) + halVersion.minor;
    }

    public String getCurrentPackageName() {
        String[] packagesForUid;
        try {
            PackageManager packageManager = this.mApp.getBaseContext().createContextAsUser(Binder.getCallingUserHandle(), 0).getPackageManager();
            if (packageManager == null || (packagesForUid = packageManager.getPackagesForUid(Binder.getCallingUid())) == null) {
                return null;
            }
            return packagesForUid[0];
        } catch (IllegalStateException unused) {
            return null;
        }
    }

    private String getCurrentPackageNameOrPhone() {
        PackageManager packageManager = this.mApp.getBaseContext().createContextAsUser(Binder.getCallingUserHandle(), 0).getPackageManager();
        String nameForUid = packageManager == null ? null : packageManager.getNameForUid(Binder.getCallingUid());
        return (nameForUid == null || nameForUid.isEmpty()) ? getCurrentPackageName() : nameForUid;
    }

    public boolean isDataEnabledForApn(int i, int i2, String str) {
        enforceReadPrivilegedPermission("Needs READ_PRIVILEGED_PHONE_STATE for isDataEnabledForApn");
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.data", "isDataEnabledForApn");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i2);
            if (phone == null) {
                return false;
            }
            return !phone.getDataNetworkController().getDataConfigManager().isMeteredCapability(DataUtils.apnTypeToNetworkCapability(i), phone.getServiceState().getDataRoaming()) || (phone.getDataSettingsManager() != null ? phone.getDataSettingsManager().isDataEnabled(i) : false);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isApnMetered(int i, int i2) {
        enforceReadPrivilegedPermission("isApnMetered");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.data", "isApnMetered");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i2);
            if (phone != null) {
                return phone.getDataNetworkController().getDataConfigManager().isMeteredCapability(DataUtils.apnTypeToNetworkCapability(i), phone.getServiceState().getDataRoaming());
            }
            return true;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setSystemSelectionChannels(List list, int i, final IBooleanConsumer iBooleanConsumer) {
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.radio.access", "setSystemSelectionChannels");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone != null) {
                sendRequestAsync(80, Pair.create(list, new Consumer() { // from class: com.android.phone.PhoneInterfaceManager$$ExternalSyntheticLambda6
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        PhoneInterfaceManager.$r8$lambda$orKbsyUtS1_vuvkDvnkv4EO4q3A(iBooleanConsumer, (Boolean) obj);
                    }
                }), phone, null);
            } else {
                if (iBooleanConsumer != null) {
                    try {
                        iBooleanConsumer.accept(false);
                    } catch (RemoteException unused) {
                    }
                }
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public static /* synthetic */ void $r8$lambda$orKbsyUtS1_vuvkDvnkv4EO4q3A(IBooleanConsumer iBooleanConsumer, Boolean bool) {
        if (iBooleanConsumer != null) {
            try {
                iBooleanConsumer.accept(bool.booleanValue());
            } catch (RemoteException unused) {
            }
        }
    }

    public List getSystemSelectionChannels(int i) {
        TelephonyPermissions.enforceCallingOrSelfReadPrivilegedPhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "getSystemSelectionChannels");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.radio.access", "getSystemSelectionChannels");
        WorkSource workSource = getWorkSource(Binder.getCallingUid());
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Object objSendRequest = sendRequest(97, (Object) null, i, workSource);
            if (objSendRequest instanceof IllegalStateException) {
                throw ((IllegalStateException) objSendRequest);
            }
            List list = (List) objSendRequest;
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            return list;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public boolean isMvnoMatched(int i, int i2, String str) {
        enforceReadPrivilegedPermission("isMvnoMatched");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "isMvnoMatched");
        return UiccController.getInstance().mvnoMatches(i, i2, str);
    }

    public void enqueueSmsPickResult(String str, String str2, IIntegerConsumer iIntegerConsumer) {
        if (str == null) {
            str = getCurrentPackageName();
        }
        Phone defaultPhone = getDefaultPhone();
        PhoneGlobals phoneGlobals = this.mApp;
        if (!new SmsPermissions(defaultPhone, phoneGlobals, (AppOpsManager) phoneGlobals.getSystemService("appops")).checkCallingCanSendSms(str, str2, "Sending message")) {
            ImsRcsController$$ExternalSyntheticBUOutline0.m("Requires SEND_SMS permission to perform this operation");
            return;
        }
        PickSmsSubscriptionActivity.addPendingResult(iIntegerConsumer);
        Intent intent = new Intent();
        intent.setClass(this.mApp, PickSmsSubscriptionActivity.class);
        intent.addFlags(268435456);
        intent.putExtra("dialog_type", 4);
        this.mApp.startActivityAsUser(intent, UserHandle.CURRENT);
    }

    public void showSwitchToManagedProfileDialog() {
        enforceModifyPermission();
        try {
            Intent intent = new Intent("android.intent.action.SENDTO");
            intent.setData(Uri.parse("smsto:"));
            intent.addFlags(268435456);
            this.mApp.startActivityAsUser(intent, UserHandle.CURRENT);
        } catch (ActivityNotFoundException unused) {
            Log.w("PhoneInterfaceManager", "Unable to show intent forwarder, try showing error dialog instead");
            Intent intent2 = new Intent();
            intent2.setClass(this.mApp, ErrorDialogActivity.class);
            intent2.addFlags(268435456);
            this.mApp.startActivityAsUser(intent2, UserHandle.CURRENT);
        }
    }

    public String getMmsUAProfUrl(int i) {
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.messaging", "getMmsUAProfUrl");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            String string = this.mApp.getCarrierConfigForSubId(i).getString("uaProfUrl");
            if (!TextUtils.isEmpty(string)) {
                return string.replace("%s", Build.MODEL);
            }
            return SubscriptionManager.getResourcesForSubId(getDefaultPhone().getContext(), i).getString(android.R.string.days).replace("%s", Build.MODEL);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String getMmsUserAgent(int i) {
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.messaging", "getMmsUserAgent");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            String string = this.mApp.getCarrierConfigForSubId(i).getString("userAgent");
            if (!TextUtils.isEmpty(string)) {
                return string.replace("%s", Build.MODEL);
            }
            return MiPhoneAppStub.get().getMmsUserAgent(getDefaultPhone().getContext(), i, SubscriptionManager.getResourcesForSubId(getDefaultPhone().getContext(), i).getString(android.R.string.day).replace("%s", Build.MODEL));
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isMobileDataPolicyEnabled(int i, int i2) {
        enforceReadPrivilegedPermission("isMobileDataPolicyEnabled");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.data", "isMobileDataPolicyEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone != null && phone.getDataSettingsManager() != null) {
                return phone.getDataSettingsManager().isMobileDataPolicyEnabled(i2);
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setMobileDataPolicyEnabled(int i, int i2, boolean z) {
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.data", "setMobileDataPolicyEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone != null && phone.getDataSettingsManager() != null) {
                phone.getDataSettingsManager().setMobileDataPolicy(i2, z);
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setCepEnabled(boolean z) {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setCepEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Rlog.i("PhoneInterfaceManager", "setCepEnabled isCepEnabled=" + z);
            for (Phone phone : PhoneFactory.getPhones()) {
                ImsPhone imsPhone = phone.getImsPhone();
                if (imsPhone != null && imsPhone.getPhoneType() == 5) {
                    ImsPhone imsPhone2 = imsPhone;
                    imsPhone2.getCallTracker().setConferenceEventPackageEnabled(z);
                    Rlog.i("PhoneInterfaceManager", "setCepEnabled isCepEnabled=" + z + ", for imsPhone " + imsPhone2.getMsisdn());
                }
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void notifyRcsAutoConfigurationReceived(int i, byte[] bArr, boolean z) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "notifyRcsAutoConfigurationReceived");
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            HexString$$ExternalSyntheticBUOutline1.m("Invalid Subscription ID: ", i);
            return;
        }
        if (!CompatChanges.isChangeEnabled(297989574L, getCurrentPackageName(), Binder.getCallingUserHandle())) {
            if (!isImsAvailableOnDevice()) {
                OrtUtil$$ExternalSyntheticBUOutline0.m("IMS not available on device.");
                return;
            }
        } else {
            enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims.singlereg", "notifyRcsAutoConfigurationReceived");
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            RcsProvisioningMonitor.getInstance().updateConfig(i, bArr, z);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isIccLockEnabled(int i) {
        enforceReadPrivilegedPermission("isIccLockEnabled");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "isIccLockEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone == null || phone.getIccCard() == null) {
                return false;
            }
            return phone.getIccCard().getIccLockEnabled();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int setIccLockEnabled(int i, boolean z, String str) {
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "setIccLockEnabled");
        Phone phone = getPhone(i);
        if (phone == null) {
            return 0;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return ((Integer) sendRequest(78, new Pair(Boolean.valueOf(z), str), phone, (WorkSource) null)).intValue();
        } catch (Exception e) {
            Log.e("PhoneInterfaceManager", "setIccLockEnabled. Exception e =" + e);
            return 0;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int changeIccLockPassword(int i, String str, String str2) {
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "changeIccLockPassword");
        Phone phone = getPhone(i);
        if (phone == null) {
            return 0;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return ((Integer) sendRequest(76, new Pair(str, str2), phone, (WorkSource) null)).intValue();
        } catch (Exception e) {
            Log.e("PhoneInterfaceManager", "changeIccLockPassword. Exception e =" + e);
            return 0;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void requestUserActivityNotification() {
        if (this.mNotifyUserActivity.get() || this.mMainThreadHandler.hasMessages(82)) {
            return;
        }
        this.mNotifyUserActivity.set(true);
    }

    public void userActivity() {
        if (!UserHandle.isSameApp(Binder.getCallingUid(), 1000)) {
            ImsRcsController$$ExternalSyntheticBUOutline0.m("Only the OS may call notifyUserActivity()");
        } else if (this.mNotifyUserActivity.getAndSet(false)) {
            this.mMainThreadHandler.sendEmptyMessageDelayed(82, 200L);
        }
    }

    public boolean canConnectTo5GInDsdsMode() {
        return this.mApp.getResources().getBoolean(R.bool.config_5g_connection_in_dsds_mode);
    }

    public List getEquivalentHomePlmns(int i, String str, String str2) {
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mApp, i, str, str2, "getEquivalentHomePlmns")) {
            ImsRcsController$$ExternalSyntheticBUOutline0.m("Requires READ_PHONE_STATE permission.");
            return null;
        }
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.subscription", "getEquivalentHomePlmns");
        Phone phone = getPhone(i);
        if (phone == null) {
            d$$ExternalSyntheticBUOutline0.m("phone is not available");
            return null;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return phone.getEquivalentHomePlmns();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isRadioInterfaceCapabilitySupported(String str) {
        if (!this.mApp.getResources().getBoolean(android.R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.radio.access", "isRadioInterfaceCapabilitySupported");
        }
        Set capabilities = this.mRadioInterfaceCapabilities.getCapabilities();
        if (capabilities == null) {
            d$$ExternalSyntheticBUOutline0.m("radio interface capabilities are not available");
            return false;
        }
        return capabilities.contains(str);
    }

    public void bootstrapAuthenticationRequest(int i, int i2, Uri uri, UaSecurityProtocolIdentifier uaSecurityProtocolIdentifier, boolean z, IBootstrapAuthenticationCallback iBootstrapAuthenticationCallback) {
        TelephonyPermissions.enforceAnyPermissionGrantedOrCarrierPrivileges(this.mApp, i, Binder.getCallingUid(), "bootstrapAuthenticationRequest", new String[]{"android.permission.PERFORM_IMS_SINGLE_REGISTRATION", "android.permission.MODIFY_PHONE_STATE"});
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "bootstrapAuthenticationRequest");
        if (!SubscriptionManager.isValidSubscriptionId(i) || i2 < 0 || i2 > 5 || uri == null || uaSecurityProtocolIdentifier == null || iBootstrapAuthenticationCallback == null) {
            Log.d("PhoneInterfaceManager", "bootstrapAuthenticationRequest failed due to invalid parameters");
            if (iBootstrapAuthenticationCallback != null) {
                try {
                    iBootstrapAuthenticationCallback.onAuthenticationFailure(0, 1);
                    return;
                } catch (RemoteException e) {
                    log("Fail to notify onAuthenticationFailure due to " + e);
                    return;
                }
            }
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            getGbaManager(i).bootstrapAuthenticationRequest(new GbaAuthRequest(i, i2, uri, uaSecurityProtocolIdentifier.toByteArray(), z, iBootstrapAuthenticationCallback));
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private boolean setRadioPowerForThermal(boolean z) {
        boolean z2 = false;
        for (int i = 0; i < TelephonyManager.getDefault().getActiveModemCount(); i++) {
            Phone phone = PhoneFactory.getPhone(i);
            if (phone != null) {
                z2 = true;
                phone.setRadioPowerForReason(z, 1);
            }
        }
        return z2;
    }

    private int handleDataThrottlingRequest(int i, DataThrottlingRequest dataThrottlingRequest, String str) {
        boolean zIsRadioInterfaceCapabilitySupported = isRadioInterfaceCapabilitySupported("CAPABILITY_THERMAL_MITIGATION_DATA_THROTTLING");
        if (!zIsRadioInterfaceCapabilitySupported && dataThrottlingRequest.getDataThrottlingAction() != 0) {
            Folme$$ExternalSyntheticBUOutline0.m("modem does not support data throttling");
            return 0;
        }
        if (!setRadioPowerForThermal(true)) {
            return 2;
        }
        setDataEnabledForReason(i, 3, true, str);
        if (zIsRadioInterfaceCapabilitySupported) {
            int iIntValue = ((Integer) sendRequest(99, dataThrottlingRequest, Integer.valueOf(i))).intValue();
            if (iIntValue != -1) {
                if (iIntValue != -2) {
                    return iIntValue;
                }
                log("Modem likely does not support data throttling on secondary carrier. Data throttling action = " + dataThrottlingRequest.getDataThrottlingAction());
                return 1;
            }
            Folme$$ExternalSyntheticBUOutline0.m("modem returned INVALID_ARGUMENTS");
        }
        return 0;
    }

    private static List getThermalMitigationAllowlist(Context context) {
        if (sThermalMitigationAllowlistedPackages.isEmpty()) {
            for (String str : context.getResources().getStringArray(R.array.thermal_mitigation_allowlisted_packages)) {
                sThermalMitigationAllowlistedPackages.add(str);
            }
        }
        return sThermalMitigationAllowlistedPackages;
    }

    private boolean isAnyPhoneInEmergencyState() {
        if (((TelecomManager) this.mApp.getSystemService(TelecomManager.class)).isInEmergencyCall()) {
            Log.e("PhoneInterfaceManager", "Phone state is not valid. One of the phones is in an emergency call");
            return true;
        }
        for (Phone phone : PhoneFactory.getPhones()) {
            if (phone.isInEmergencySmsMode() || phone.isInEcm()) {
                Log.e("PhoneInterfaceManager", "Phone state is not valid. isInEmergencySmsMode = " + phone.isInEmergencySmsMode() + " isInEmergencyCallbackMode = " + phone.isInEcm());
                return true;
            }
        }
        return false;
    }

    static void addPackageToThermalMitigationAllowlist(String str, Context context) {
        List thermalMitigationAllowlist = getThermalMitigationAllowlist(context);
        sThermalMitigationAllowlistedPackages = thermalMitigationAllowlist;
        thermalMitigationAllowlist.add(str);
    }

    static void removePackageFromThermalMitigationAllowlist(String str, Context context) {
        List thermalMitigationAllowlist = getThermalMitigationAllowlist(context);
        sThermalMitigationAllowlistedPackages = thermalMitigationAllowlist;
        thermalMitigationAllowlist.remove(str);
    }

    public int sendThermalMitigationRequest(int i, ThermalMitigationRequest thermalMitigationRequest, String str) {
        int iHandleDataThrottlingRequest;
        enforceModifyPermission();
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.radio.access", "sendThermalMitigationRequest");
        if (!getThermalMitigationAllowlist(getDefaultPhone().getContext()).contains(str)) {
            throw new SecurityException("Calling package must be configured in the device config. calling package: " + str);
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                int thermalMitigationAction = thermalMitigationRequest.getThermalMitigationAction();
                if (thermalMitigationAction != 0) {
                    iHandleDataThrottlingRequest = 3;
                    if (thermalMitigationAction != 1) {
                        if (thermalMitigationAction == 2) {
                            if (thermalMitigationRequest.getDataThrottlingRequest() != null) {
                                throw new IllegalArgumentException("dataThrottlingRequest  must be null for ThermalMitigationRequest.THERMAL_MITIGATION_ACTION_RADIO_OFF");
                            }
                            TelecomAccountRegistry telecomAccountRegistry = TelecomAccountRegistry.getInstance(null);
                            if (telecomAccountRegistry != null && getPhone(i) != null) {
                                TelephonyConnectionService telephonyConnectionService = telecomAccountRegistry.getTelephonyConnectionService();
                                if (telephonyConnectionService != null && telephonyConnectionService.isEmergencyCallPending()) {
                                    Log.e("PhoneInterfaceManager", "An emergency call is pending");
                                } else if (!isAnyPhoneInEmergencyState()) {
                                    iHandleDataThrottlingRequest = !setRadioPowerForThermal(false) ? 2 : 0;
                                }
                            }
                        } else {
                            throw new IllegalArgumentException("the requested thermalMitigationAction does not exist. Requested action: " + thermalMitigationAction);
                        }
                    } else {
                        if (thermalMitigationRequest.getDataThrottlingRequest() != null) {
                            throw new IllegalArgumentException("dataThrottlingRequest must be null for ThermalMitigationRequest.THERMAL_MITIGATION_ACTION_VOICE_ONLY");
                        }
                        if (setRadioPowerForThermal(true)) {
                            setDataEnabledForReason(i, 3, false, str);
                        }
                    }
                } else {
                    iHandleDataThrottlingRequest = handleDataThrottlingRequest(i, thermalMitigationRequest.getDataThrottlingRequest(), str);
                }
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return iHandleDataThrottlingRequest;
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (Exception e2) {
                Log.e("PhoneInterfaceManager", "thermalMitigationRequest. Exception e =" + e2);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return 1;
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public boolean setBoundGbaServiceOverride(int i, String str) {
        enforceModifyPermission();
        int currentUser = ActivityManager.getCurrentUser();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return getGbaManager(i).overrideServicePackage(str, currentUser);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String getBoundGbaService(int i) {
        enforceReadPrivilegedPermission("getBoundGbaServicePackage");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return getGbaManager(i).getServicePackage();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setGbaReleaseTimeOverride(int i, int i2) {
        enforceModifyPermission();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return getGbaManager(i).overrideReleaseTime(i2);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getGbaReleaseTime(int i) {
        enforceReadPrivilegedPermission("getGbaReleaseTime");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return getGbaManager(i).getReleaseTime();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private GbaManager getGbaManager(int i) {
        GbaManager gbaManager = GbaManager.getInstance(i);
        if (gbaManager != null) {
            return gbaManager;
        }
        return GbaManager.make(this.mApp, i, this.mApp.getResources().getString(R.string.config_gba_package), this.mApp.getResources().getInteger(R.integer.config_gba_release_time), this.mFeatureFlags);
    }

    public boolean isRcsVolteSingleRegistrationCapable(int i) {
        Boolean boolIsRcsVolteSingleRegistrationEnabled;
        TelephonyPermissions.enforceAnyPermissionGrantedOrCarrierPrivileges(this.mApp, i, Binder.getCallingUid(), "isRcsVolteSingleRegistrationCapable", new String[]{"android.permission.PERFORM_IMS_SINGLE_REGISTRATION", "android.permission.READ_PRIVILEGED_PHONE_STATE"});
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            HexString$$ExternalSyntheticBUOutline1.m("Invalid Subscription ID: ", i);
            return false;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            RcsProvisioningMonitor rcsProvisioningMonitor = RcsProvisioningMonitor.getInstance();
            if (rcsProvisioningMonitor != null && (boolIsRcsVolteSingleRegistrationEnabled = rcsProvisioningMonitor.isRcsVolteSingleRegistrationEnabled(i)) != null) {
                boolean zBooleanValue = boolIsRcsVolteSingleRegistrationEnabled.booleanValue();
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return zBooleanValue;
            }
            throw new ServiceSpecificException(1, "service is temporarily unavailable.");
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public void registerRcsProvisioningCallback(int i, IRcsConfigCallback iRcsConfigCallback) throws ServiceSpecificException {
        TelephonyPermissions.enforceAnyPermissionGrantedOrCarrierPrivileges(this.mApp, i, Binder.getCallingUid(), "registerRcsProvisioningCallback", new String[]{"android.permission.PERFORM_IMS_SINGLE_REGISTRATION", "android.permission.READ_PRIVILEGED_PHONE_STATE"});
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            HexString$$ExternalSyntheticBUOutline1.m("Invalid Subscription ID: ", i);
            return;
        }
        if (CompatChanges.isChangeEnabled(297989574L, getCurrentPackageName(), Binder.getCallingUserHandle())) {
            enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims.singlereg", "registerRcsProvisioningCallback");
        } else if (!isImsAvailableOnDevice()) {
            throw new ServiceSpecificException(2, "IMS not available on device.");
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (!RcsProvisioningMonitor.getInstance().registerRcsProvisioningCallback(i, iRcsConfigCallback)) {
                throw new ServiceSpecificException(3, "Active subscription not found.");
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void unregisterRcsProvisioningCallback(int i, IRcsConfigCallback iRcsConfigCallback) {
        TelephonyPermissions.enforceAnyPermissionGrantedOrCarrierPrivileges(this.mApp, i, Binder.getCallingUid(), "unregisterRcsProvisioningCallback", new String[]{"android.permission.PERFORM_IMS_SINGLE_REGISTRATION", "android.permission.READ_PRIVILEGED_PHONE_STATE"});
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            HexString$$ExternalSyntheticBUOutline1.m("Invalid Subscription ID: ", i);
            return;
        }
        if (CompatChanges.isChangeEnabled(297989574L, getCurrentPackageName(), Binder.getCallingUserHandle())) {
            enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims.singlereg", "unregisterRcsProvisioningCallback");
        } else if (!isImsAvailableOnDevice()) {
            Rlog.w("PhoneInterfaceManager", "IMS not available on device.");
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            RcsProvisioningMonitor.getInstance().unregisterRcsProvisioningCallback(i, iRcsConfigCallback);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void triggerRcsReconfiguration(int i) {
        TelephonyPermissions.enforceAnyPermissionGranted(this.mApp, Binder.getCallingUid(), "triggerRcsReconfiguration", new String[]{"android.permission.PERFORM_IMS_SINGLE_REGISTRATION"});
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            HexString$$ExternalSyntheticBUOutline1.m("Invalid Subscription ID: ", i);
            return;
        }
        if (!CompatChanges.isChangeEnabled(297989574L, getCurrentPackageName(), Binder.getCallingUserHandle())) {
            if (!isImsAvailableOnDevice()) {
                OrtUtil$$ExternalSyntheticBUOutline0.m("IMS not available on device.");
                return;
            }
        } else {
            enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims.singlereg", "triggerRcsReconfiguration");
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            RcsProvisioningMonitor.getInstance().requestReconfig(i);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public void setRcsClientConfiguration(int i, RcsClientConfiguration rcsClientConfiguration) throws ServiceSpecificException {
        TelephonyPermissions.enforceAnyPermissionGranted(this.mApp, Binder.getCallingUid(), "setRcsClientConfiguration", new String[]{"android.permission.PERFORM_IMS_SINGLE_REGISTRATION"});
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            HexString$$ExternalSyntheticBUOutline1.m("Invalid Subscription ID: ", i);
            return;
        }
        if (!CompatChanges.isChangeEnabled(297989574L, getCurrentPackageName(), Binder.getCallingUserHandle())) {
            if (!isImsAvailableOnDevice()) {
                throw new ServiceSpecificException(2, "IMS not available on device.");
            }
        } else {
            enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims.singlereg", "setRcsClientConfiguration");
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                IImsConfig imsConfig = getImsConfig(getSlotIndex(i), 2);
                if (imsConfig == null) {
                    Rlog.e("PhoneInterfaceManager", "null result for setRcsClientConfiguration");
                    throw new ServiceSpecificException(3, "could not find the requested subscription");
                }
                imsConfig.setRcsClientConfiguration(rcsClientConfiguration);
                RcsStats.getInstance().onRcsClientProvisioningStats(i, 1);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            } catch (RemoteException e) {
                Rlog.e("PhoneInterfaceManager", "fail to setRcsClientConfiguration " + e.getMessage());
                throw new ServiceSpecificException(1, "service is temporarily unavailable.");
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void setRcsSingleRegistrationTestModeEnabled(boolean z) {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setRcsSingleRegistrationTestModeEnabled");
        RcsProvisioningMonitor.getInstance().setTestModeEnabled(z);
    }

    public boolean getRcsSingleRegistrationTestModeEnabled() {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "getRcsSingleRegistrationTestModeEnabled");
        return RcsProvisioningMonitor.getInstance().getTestModeEnabled();
    }

    public void setDeviceSingleRegistrationEnabledOverride(String str) {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setDeviceSingleRegistrationEnabledOverride");
        enforceModifyPermission();
        Boolean boolValueOf = "NULL".equalsIgnoreCase(str) ? null : Boolean.valueOf(Boolean.parseBoolean(str));
        RcsProvisioningMonitor.getInstance().overrideDeviceSingleRegistrationEnabled(boolValueOf);
        this.mApp.imsRcsController.setDeviceSingleRegistrationSupportOverride(boolValueOf);
    }

    public void sendDeviceToDeviceMessage(int i, int i2) {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "sendDeviceToDeviceMessage");
        enforceModifyPermission();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            TelephonyConnectionService telephonyConnectionService = TelecomAccountRegistry.getInstance(null).getTelephonyConnectionService();
            if (telephonyConnectionService == null) {
                Rlog.e("PhoneInterfaceManager", "sendDeviceToDeviceMessage: not in a call.");
            } else {
                telephonyConnectionService.sendTestDeviceToDeviceMessage(i, i2);
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setActiveDeviceToDeviceTransport(String str) {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setActiveDeviceToDeviceTransport");
        enforceModifyPermission();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            TelephonyConnectionService telephonyConnectionService = TelecomAccountRegistry.getInstance(null).getTelephonyConnectionService();
            if (telephonyConnectionService == null) {
                Rlog.e("PhoneInterfaceManager", "setActiveDeviceToDeviceTransport: not in a call.");
            } else {
                telephonyConnectionService.setActiveDeviceToDeviceTransport(str);
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setDeviceToDeviceForceEnabled(final boolean z) {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setDeviceToDeviceForceEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Arrays.stream(PhoneFactory.getPhones()).forEach(new Consumer() { // from class: com.android.phone.PhoneInterfaceManager$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    PhoneInterfaceManager.$r8$lambda$E_W8odTGTfX000plpFUKGZOfeCo(z, (Phone) obj);
                }
            });
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public static /* synthetic */ void $r8$lambda$E_W8odTGTfX000plpFUKGZOfeCo(boolean z, Phone phone) {
        ImsPhoneCallTracker callTracker;
        ImsPhone imsPhone = phone.getImsPhone();
        if (imsPhone == null || !(imsPhone instanceof ImsPhone) || (callTracker = imsPhone.getCallTracker()) == null || !(callTracker instanceof ImsPhoneCallTracker)) {
            return;
        }
        callTracker.setDeviceToDeviceForceEnabled(z);
    }

    public boolean getDeviceSingleRegistrationEnabled() {
        enforceReadPrivilegedPermission("getDeviceSingleRegistrationEnabled");
        return RcsProvisioningMonitor.getInstance().getDeviceSingleRegistrationEnabled();
    }

    public boolean setCarrierSingleRegistrationEnabledOverride(int i, String str) {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setCarrierSingleRegistrationEnabledOverride");
        enforceModifyPermission();
        return RcsProvisioningMonitor.getInstance().overrideCarrierSingleRegistrationEnabled(i, "NULL".equalsIgnoreCase(str) ? null : Boolean.valueOf(Boolean.parseBoolean(str)));
    }

    public boolean getCarrierSingleRegistrationEnabled(int i) {
        enforceReadPrivilegedPermission("getCarrierSingleRegistrationEnabled");
        return RcsProvisioningMonitor.getInstance().getCarrierSingleRegistrationEnabled(i);
    }

    public boolean setImsFeatureValidationOverride(int i, String str) {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setImsFeatureValidationOverride");
        return RcsProvisioningMonitor.getInstance().overrideImsFeatureValidation(i, "NULL".equalsIgnoreCase(str) ? null : Boolean.valueOf(Boolean.parseBoolean(str)));
    }

    public boolean getImsFeatureValidationOverride(int i) {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "getImsFeatureValidationOverride");
        return RcsProvisioningMonitor.getInstance().getImsFeatureValidationOverride(i).booleanValue();
    }

    public String getMobileProvisioningUrl() {
        enforceReadPrivilegedPermission("getMobileProvisioningUrl");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return getDefaultPhone().getMobileProvisioningUrl();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String getContactFromEab(String str) {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "getContactFromEab");
        enforceModifyPermission();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return EabUtil.getContactFromEab(getDefaultPhone().getContext(), str);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String getCapabilityFromEab(String str) {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "getCapabilityFromEab");
        enforceModifyPermission();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return EabUtil.getCapabilityFromEab(getDefaultPhone().getContext(), str);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int removeContactFromEab(int i, String str) {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "removeCapabilitiesFromEab");
        enforceModifyPermission();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return EabUtil.removeContactFromEab(i, str, getDefaultPhone().getContext());
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean getDeviceUceEnabled() {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "getDeviceUceEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mApp.getDeviceUceEnabled();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setDeviceUceEnabled(boolean z) {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setDeviceUceEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mApp.setDeviceUceEnabled(z);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public RcsContactUceCapability addUceRegistrationOverrideShell(int i, List list) {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "addUceRegistrationOverrideShell");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                RcsContactUceCapability rcsContactUceCapabilityAddUceRegistrationOverrideShell = this.mApp.imsRcsController.addUceRegistrationOverrideShell(i, new ArraySet(list));
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return rcsContactUceCapabilityAddUceRegistrationOverrideShell;
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode(), e.getMessage());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public RcsContactUceCapability removeUceRegistrationOverrideShell(int i, List list) {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "removeUceRegistrationOverrideShell");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                RcsContactUceCapability rcsContactUceCapabilityRemoveUceRegistrationOverrideShell = this.mApp.imsRcsController.removeUceRegistrationOverrideShell(i, new ArraySet(list));
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return rcsContactUceCapabilityRemoveUceRegistrationOverrideShell;
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode(), e.getMessage());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public RcsContactUceCapability clearUceRegistrationOverrideShell(int i) {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "clearUceRegistrationOverrideShell");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                RcsContactUceCapability rcsContactUceCapabilityClearUceRegistrationOverrideShell = this.mApp.imsRcsController.clearUceRegistrationOverrideShell(i);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return rcsContactUceCapabilityClearUceRegistrationOverrideShell;
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode(), e.getMessage());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public RcsContactUceCapability getLatestRcsContactUceCapabilityShell(int i) {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "getLatestRcsContactUceCapabilityShell");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                RcsContactUceCapability latestRcsContactUceCapabilityShell = this.mApp.imsRcsController.getLatestRcsContactUceCapabilityShell(i);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return latestRcsContactUceCapabilityShell;
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode(), e.getMessage());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public String getLastUcePidfXmlShell(int i) {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "uceGetLastPidfXml");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                String lastUcePidfXmlShell = this.mApp.imsRcsController.getLastUcePidfXmlShell(i);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return lastUcePidfXmlShell;
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode(), e.getMessage());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public boolean removeUceRequestDisallowedStatus(int i) {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "uceRemoveDisallowedStatus");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                boolean zRemoveUceRequestDisallowedStatus = this.mApp.imsRcsController.removeUceRequestDisallowedStatus(i);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return zRemoveUceRequestDisallowedStatus;
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode(), e.getMessage());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public boolean setCapabilitiesRequestTimeout(int i, long j) {
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setCapRequestTimeout");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                boolean capabilitiesRequestTimeout = this.mApp.imsRcsController.setCapabilitiesRequestTimeout(i, j);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return capabilitiesRequestTimeout;
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode(), e.getMessage());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void setSignalStrengthUpdateRequest(int i, SignalStrengthUpdateRequest signalStrengthUpdateRequest, String str) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "setSignalStrengthUpdateRequest");
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.radio.access", "setSignalStrengthUpdateRequest");
        int callingUid = Binder.getCallingUid();
        ((AppOpsManager) this.mApp.getSystemService(AppOpsManager.class)).checkPackage(callingUid, str);
        validateSignalStrengthUpdateRequest(this.mApp, signalStrengthUpdateRequest, callingUid);
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Object objSendRequest = sendRequest(103, new Pair(Integer.valueOf(callingUid), signalStrengthUpdateRequest), Integer.valueOf(i));
            if (objSendRequest instanceof IllegalStateException) {
                throw ((IllegalStateException) objSendRequest);
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void clearSignalStrengthUpdateRequest(int i, SignalStrengthUpdateRequest signalStrengthUpdateRequest, String str) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, i, "clearSignalStrengthUpdateRequest");
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.radio.access", "clearSignalStrengthUpdateRequest");
        int callingUid = Binder.getCallingUid();
        ((AppOpsManager) this.mApp.getSystemService(AppOpsManager.class)).checkPackage(callingUid, str);
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Object objSendRequest = sendRequest(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT, new Pair(Integer.valueOf(callingUid), signalStrengthUpdateRequest), Integer.valueOf(i));
            if (objSendRequest instanceof IllegalStateException) {
                throw ((IllegalStateException) objSendRequest);
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    private static void validateSignalStrengthUpdateRequest(Context context, SignalStrengthUpdateRequest signalStrengthUpdateRequest, int i) {
        if (TelephonyPermissions.isSystemOrPhone(i)) {
            return;
        }
        if (signalStrengthUpdateRequest.isSystemThresholdReportingRequestedWhileIdle()) {
            context.enforceCallingOrSelfPermission("android.permission.LISTEN_ALWAYS_REPORTED_SIGNAL_STRENGTH", "validateSignalStrengthUpdateRequest");
        }
        for (SignalThresholdInfo signalThresholdInfo : signalStrengthUpdateRequest.getSignalThresholdInfos()) {
            if (signalThresholdInfo.getHysteresisMs() != 0 || signalThresholdInfo.isEnabled()) {
                Folme$$ExternalSyntheticBUOutline0.m("Only system can set hide fields in SignalThresholdInfo");
                return;
            }
            int[] thresholds = signalThresholdInfo.getThresholds();
            Objects.requireNonNull(thresholds);
            if (thresholds.length < SignalThresholdInfo.getMinimumNumberOfThresholdsAllowed() || thresholds.length > SignalThresholdInfo.getMaximumNumberOfThresholdsAllowed()) {
                BackStackRecord$$ExternalSyntheticBUOutline0.m("thresholds length is out of range: ", thresholds.length);
                return;
            }
        }
    }

    public PhoneCapability getPhoneCapability() {
        enforceReadPrivilegedPermission("getPhoneCapability");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony", "getPhoneCapability");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mPhoneConfigurationManager.getCurrentPhoneCapability();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int prepareForUnattendedReboot() {
        WorkSource workSource = getWorkSource(Binder.getCallingUid());
        enforceRebootPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "prepareForUnattendedReboot");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return ((Integer) sendRequest(109, (Object) null, workSource)).intValue();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void getSlicingConfig(ResultReceiver resultReceiver) {
        TelephonyPermissions.enforceCallingOrSelfReadPrivilegedPhoneStatePermissionOrCarrierPrivilege(this.mApp, -1, "getSlicingConfig");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.radio.access", "getSlicingConfig");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            sendRequestAsync(110, resultReceiver, getDefaultPhone(), null);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isPremiumCapabilityAvailableForPurchase(int i, int i2) {
        if (!TelephonyPermissions.checkCallingOrSelfReadNonDangerousPhoneStateNoThrow(this.mApp, "isPremiumCapabilityAvailableForPurchase")) {
            log("Premium capability " + TelephonyManager.convertPremiumCapabilityToString(i) + " is not available for purchase due to missing permissions.");
            ImsRcsController$$ExternalSyntheticBUOutline0.m("isPremiumCapabilityAvailableForPurchase requires permission READ_BASIC_PHONE_STATE.");
            return false;
        }
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.data", "isPremiumCapabilityAvailableForPurchase");
        Phone phone = getPhone(i2);
        if (phone == null) {
            loge("isPremiumCapabilityAvailableForPurchase: phone is null, subId=" + i2);
            return false;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return SlicePurchaseController.getInstance(phone, this.mFeatureFlags).isPremiumCapabilityAvailableForPurchase(i);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void purchasePremiumCapability(int i, IIntegerConsumer iIntegerConsumer, int i2) {
        String currentPackageName;
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses;
        log("purchasePremiumCapability: capability=" + TelephonyManager.convertPremiumCapabilityToString(i) + ", caller=" + getCurrentPackageName());
        if (!TelephonyPermissions.checkCallingOrSelfReadNonDangerousPhoneStateNoThrow(this.mApp, "purchasePremiumCapability")) {
            log("purchasePremiumCapability " + TelephonyManager.convertPremiumCapabilityToString(i) + " failed due to missing permissions.");
            ImsRcsController$$ExternalSyntheticBUOutline0.m("purchasePremiumCapability requires permission READ_BASIC_PHONE_STATE.");
            return;
        }
        if (!TelephonyPermissions.checkInternetPermissionNoThrow(this.mApp, "purchasePremiumCapability")) {
            log("purchasePremiumCapability " + TelephonyManager.convertPremiumCapabilityToString(i) + " failed due to missing permissions.");
            ImsRcsController$$ExternalSyntheticBUOutline0.m("purchasePremiumCapability requires permission INTERNET.");
            return;
        }
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.data", "purchasePremiumCapability");
        Phone phone = getPhone(i2);
        if (phone == null) {
            try {
                iIntegerConsumer.accept(11);
                loge("purchasePremiumCapability: phone is null, subId=" + i2);
                return;
            } catch (RemoteException e) {
                AnomalyReporter.reportAnomaly(UUID.fromString("24bf97a6-e8a6-44d8-a6a4-255d7548733c"), "Purchase premium capability " + TelephonyManager.convertPremiumCapabilityToString(i) + " failed due to RemoteException handling null phone: " + e);
                return;
            }
        }
        try {
            currentPackageName = this.mApp.getPackageManager().getApplicationInfoAsUser(getCurrentPackageName(), 0, Binder.getCallingUserHandle()).processName;
        } catch (PackageManager.NameNotFoundException unused) {
            currentPackageName = getCurrentPackageName();
        }
        ActivityManager activityManager = (ActivityManager) this.mApp.getSystemService(ActivityManager.class);
        if (activityManager != null && (runningAppProcesses = activityManager.getRunningAppProcesses()) != null) {
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                log("purchasePremiumCapability: process " + runningAppProcessInfo.processName + " has importance " + runningAppProcessInfo.importance);
                if (runningAppProcessInfo.processName.equals(currentPackageName) && runningAppProcessInfo.importance <= 200) {
                    sendRequestAsync(117, new PurchasePremiumCapabilityArgument(i, iIntegerConsumer), phone, null);
                    return;
                }
            }
        }
        try {
            iIntegerConsumer.accept(5);
            loge("purchasePremiumCapability: " + currentPackageName + " is not in the foreground.");
        } catch (RemoteException e2) {
            AnomalyReporter.reportAnomaly(UUID.fromString("24bf97a6-e8a6-44d8-a6a4-255d7548733c"), "Purchase premium capability " + TelephonyManager.convertPremiumCapabilityToString(i) + " failed due to RemoteException handling background application: " + e2);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public void registerImsStateCallback(int i, int i2, IImsStateCallback iImsStateCallback, String str) throws ServiceSpecificException {
        if (i2 == 1) {
            TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "registerImsStateCallback");
        } else if (i2 == 2) {
            TelephonyPermissions.enforceAnyPermissionGrantedOrCarrierPrivileges(this.mApp, i, Binder.getCallingUid(), "registerImsStateCallback", new String[]{"android.permission.READ_PRIVILEGED_PHONE_STATE", "android.permission.READ_PRECISE_PHONE_STATE", "android.permission.ACCESS_RCS_USER_CAPABILITY_EXCHANGE", "android.permission.PERFORM_IMS_SINGLE_REGISTRATION"});
        }
        if (!ImsManager.isImsSupportedOnDevice(this.mApp)) {
            throw new ServiceSpecificException(2, "IMS not available on device.");
        }
        if (i == Integer.MAX_VALUE) {
            throw new ServiceSpecificException(3);
        }
        ImsStateCallbackController imsStateCallbackController = ImsStateCallbackController.getInstance();
        if (imsStateCallbackController == null) {
            throw new ServiceSpecificException(2, "IMS not available on device.");
        }
        if (str == null) {
            str = getCurrentPackageName();
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                getSlotIndexOrException(i);
                imsStateCallbackController.registerImsStateCallback(i, i2, iImsStateCallback, str);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void unregisterImsStateCallback(IImsStateCallback iImsStateCallback) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        ImsStateCallbackController imsStateCallbackController = ImsStateCallbackController.getInstance();
        if (imsStateCallbackController == null) {
            return;
        }
        try {
            imsStateCallbackController.unregisterImsStateCallback(iImsStateCallback);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public CellIdentity getLastKnownCellIdentity(int i, String str, String str2) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        if (LocationAccessPolicy.checkLocationPermission(this.mApp, new LocationAccessPolicy.LocationPermissionQuery.Builder().setCallingPackage(str).setCallingFeatureId(str2).setCallingPid(Binder.getCallingPid()).setCallingUid(Binder.getCallingUid()).setMethod("getLastKnownCellIdentity").setLogAsInfo(true).setMinSdkVersionForFine(29).setMinSdkVersionForCoarse(29).setMinSdkVersionForEnforcement(29).build()) != LocationAccessPolicy.LocationPermissionResult.ALLOWED || !TelephonyPermissions.checkLastKnownCellIdAccessPermission(this.mApp)) {
            ImsRcsController$$ExternalSyntheticBUOutline0.m("getLastKnownCellIdentity need ACCESS_FINE_LOCATION and ACCESS_LAST_KNOWN_CELL_ID permission.");
            return null;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ServiceStateTracker serviceStateTracker = getPhoneFromSubIdOrDefault(i).getServiceStateTracker();
            if (serviceStateTracker == null) {
                return null;
            }
            return serviceStateTracker.getLastKnownCellIdentity();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setModemService(String str) {
        Log.d("PhoneInterfaceManager", "setModemService - " + str);
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setModemService");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, -1, "setModemService");
        return this.mPhoneConfigurationManager.setModemService(str);
    }

    public String getModemService() {
        Log.d("PhoneInterfaceManager", "getModemService");
        TelephonyPermissions.enforceCallingOrSelfReadPrivilegedPhoneStatePermissionOrCarrierPrivilege(this.mApp, -1, "getModemService");
        String modemService = this.mPhoneConfigurationManager.getModemService();
        Log.d("PhoneInterfaceManager", "result = " + modemService);
        return modemService;
    }

    public List getSatellitePlmnsForCarrier(int i) {
        enforceSatelliteCommunicationPermission("getSatellitePlmnsForCarrier");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.getSatellitePlmnsForCarrier(i);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setVoiceServiceStateOverride(int i, boolean z, String str) {
        this.mApp.enforceCallingOrSelfPermission("android.permission.BIND_TELECOM_CONNECTION_SERVICE", "setVoiceServiceStateOverride");
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone == null) {
                return;
            }
            Log.i("PhoneInterfaceManager", "setVoiceServiceStateOverride: subId=" + i + ", phone=" + phone + ", hasService=" + z + ", callingPackage=" + str);
            phone.setVoiceServiceStateOverride(z);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setRemovableEsimAsDefaultEuicc(boolean z, String str) {
        enforceModifyPermission();
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            UiccController.getInstance().setRemovableEsimAsDefaultEuicc(z);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isRemovableEsimDefaultEuicc(String str) {
        enforceReadPrivilegedPermission("isRemovableEsimDefaultEuicc");
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return UiccController.getInstance().isRemovableEsimDefaultEuicc();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public ComponentName getDefaultRespondViaMessageApplication(int i, boolean z) {
        enforceInteractAcrossUsersPermission("getDefaultRespondViaMessageApplication");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.messaging", "getDefaultRespondViaMessageApplication");
        Context context = getPhoneFromSubIdOrDefault(i).getContext();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            UserHandle mainUser = this.mUserManager.getMainUser();
            if (mainUser != null) {
                context = context.createContextAsUser(mainUser, 0);
            } else {
                mainUser = TelephonyUtils.getSubscriptionUserHandle(context, i);
            }
            Log.d("PhoneInterfaceManager", "getDefaultRespondViaMessageApplication: mainUser = " + mainUser);
            return SmsApplication.getDefaultRespondViaMessageApplicationAsUser(context, z, mainUser);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setNullCipherAndIntegrityEnabled(boolean z) {
        enforceModifyPermission();
        checkForNullCipherAndIntegritySupport();
        SharedPreferences.Editor editorEdit = this.mTelephonySharedPreferences.edit();
        editorEdit.putBoolean("pref_null_cipher_and_integrity_enabled", z);
        editorEdit.apply();
        for (Phone phone : PhoneFactory.getPhones()) {
            phone.handleNullCipherEnabledChange();
        }
    }

    public boolean isNullCipherAndIntegrityPreferenceEnabled() {
        enforceReadPermission();
        checkForNullCipherAndIntegritySupport();
        return getDefaultPhone().getNullCipherAndIntegrityEnabledPreference();
    }

    private void checkForNullCipherAndIntegritySupport() {
        if (getHalVersion(4) < 201) {
            MergeMotion$$ExternalSyntheticBUOutline0.m("Null cipher and integrity operations require HAL 2.1 or above");
        } else {
            if (getDefaultPhone().isNullCipherAndIntegritySupported()) {
                return;
            }
            MergeMotion$$ExternalSyntheticBUOutline0.m("Null cipher and integrity operations unsupported by modem");
        }
    }

    private void checkForIdentifierDisclosureNotificationSupport() {
        if (getHalVersion(4) < 202) {
            MergeMotion$$ExternalSyntheticBUOutline0.m("Cellular identifier disclosure transparency operations require HAL 2.2 or above");
        } else {
            if (getDefaultPhone().isIdentifierDisclosureTransparencySupported()) {
                return;
            }
            MergeMotion$$ExternalSyntheticBUOutline0.m("Cellular identifier disclosure transparency operations unsupported by modem");
        }
    }

    private void checkForNullCipherNotificationSupport() {
        if (getHalVersion(4) < 202) {
            MergeMotion$$ExternalSyntheticBUOutline0.m("Null cipher notification operations require HAL 2.2 or above");
        } else {
            if (getDefaultPhone().isNullCipherNotificationSupported()) {
                return;
            }
            MergeMotion$$ExternalSyntheticBUOutline0.m("Null cipher notification operations unsupported by modem");
        }
    }

    public int getSimStateForSlotIndex(int i) {
        Phone phone;
        IccCard iccCard;
        IccCardConstants.State state;
        if (!this.mApp.getResources().getBoolean(android.R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "getSimStateForSlotIndex");
        }
        if (i < 0) {
            state = IccCardConstants.State.UNKNOWN;
        } else {
            try {
                phone = PhoneFactory.getPhone(i);
            } catch (IllegalStateException unused) {
                phone = null;
            }
            if (phone == null || (iccCard = phone.getIccCard()) == null) {
                state = IccCardConstants.State.UNKNOWN;
            } else {
                state = iccCard.getState();
            }
        }
        return state.ordinal();
    }

    private void persistEmergencyCallDiagnosticDataInternal(String str, boolean z, long j, boolean z2, boolean z3) {
        DropBoxManager dropBoxManager = (DropBoxManager) this.mApp.getSystemService(DropBoxManager.class);
        TelephonyManager.EmergencyCallDiagnosticData.Builder builder = new TelephonyManager.EmergencyCallDiagnosticData.Builder();
        builder.setTelecomDumpsysCollectionEnabled(z2).setTelephonyDumpsysCollectionEnabled(z3);
        if (z) {
            builder.setLogcatCollectionStartTimeMillis(j);
        }
        TelephonyManager.EmergencyCallDiagnosticData emergencyCallDiagnosticDataBuild = builder.build();
        Log.d("PhoneInterfaceManager", "persisting with Params " + emergencyCallDiagnosticDataBuild.toString());
        new DiagnosticDataCollector(Runtime.getRuntime(), Executors.newCachedThreadPool(), dropBoxManager, ((ActivityManager) this.mApp.getSystemService(ActivityManager.class)).isLowRamDevice()).persistEmergencyDianosticData(new DataCollectorConfig.Adapter(), emergencyCallDiagnosticDataBuild, str);
    }

    public void persistEmergencyCallDiagnosticData(String str, boolean z, long j, boolean z2, boolean z3) {
        this.mApp.enforceCallingPermission("android.permission.READ_DROPBOX_DATA", "persistEmergencyCallDiagnosticData");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            persistEmergencyCallDiagnosticDataInternal(str, z, j, z2, z3);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public List getCellBroadcastIdRanges(int i) {
        this.mApp.enforceCallingPermission("android.permission.MODIFY_CELL_BROADCASTS", "getCellBroadcastIdRanges");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.messaging", "getCellBroadcastIdRanges");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return getPhone(i).getCellBroadcastIdRanges();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setCellBroadcastIdRanges(int i, List list, final IIntegerConsumer iIntegerConsumer) {
        this.mApp.enforceCallingPermission("android.permission.MODIFY_CELL_BROADCASTS", "setCellBroadcastIdRanges");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.messaging", "setCellBroadcastIdRanges");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            getPhoneFromSubId(i).setCellBroadcastIdRanges(list, new Consumer() { // from class: com.android.phone.PhoneInterfaceManager$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    PhoneInterfaceManager.m1845$r8$lambda$KhNB6Urev1lNrFi3qxiM4oVooc(iIntegerConsumer, (Integer) obj);
                }
            });
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$KhNB6Urev1lNrFi3q-xiM4oVooc, reason: not valid java name */
    public static /* synthetic */ void m1845$r8$lambda$KhNB6Urev1lNrFi3qxiM4oVooc(IIntegerConsumer iIntegerConsumer, Integer num) {
        if (iIntegerConsumer != null) {
            try {
                iIntegerConsumer.accept(num.intValue());
            } catch (RemoteException unused) {
                Log.w("PhoneInterfaceManager", "setCellBroadcastIdRanges: callback not available.");
            }
        }
    }

    public boolean isDomainSelectionSupported() {
        this.mApp.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", "isDomainSelectionSupported");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return DomainSelectionResolver.getInstance().isDomainSelectionSupported();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isAospDomainSelectionService() {
        this.mApp.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", "isAospDomainSelectionService");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (!DomainSelectionResolver.getInstance().isDomainSelectionSupported()) {
                return false;
            }
            String string = this.mApp.getResources().getString(R.string.config_domain_selection_service_component_name);
            ComponentName componentNameCreateRelative = ComponentName.createRelative(this.mApp.getPackageName(), TelephonyDomainSelectionService.class.getName());
            Log.i("PhoneInterfaceManager", "isAospDomainSelectionService dss=" + string + ", aosp=" + componentNameCreateRelative.flattenToString());
            return TextUtils.equals(componentNameCreateRelative.flattenToString(), string);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void requestSatelliteEnabled(boolean z, boolean z2, boolean z3, IIntegerConsumer iIntegerConsumer) {
        enforceSatelliteCommunicationPermission("requestSatelliteEnabled");
        if (shouldIgnoreSatelliteRequestInCtsMode("requestSatelliteEnabled", iIntegerConsumer)) {
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (z) {
                this.mSatelliteAccessController.requestIsCommunicationAllowedForCurrentLocation(new AnonymousClass3(this.mMainThreadHandler, "PIM:requestSatelliteEnabled", iIntegerConsumer, z, z2, z3), true);
                this.mSatelliteController.incrementResultReceiverCount("PIM:requestSatelliteEnabled");
            } else {
                this.mSatelliteController.requestSatelliteEnabled(z, z2, z3, iIntegerConsumer);
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    /* JADX INFO: renamed from: com.android.phone.PhoneInterfaceManager$3, reason: invalid class name */
    class AnonymousClass3 extends ResultReceiver {
        final /* synthetic */ IIntegerConsumer val$callback;
        final /* synthetic */ String val$caller;
        final /* synthetic */ boolean val$enableDemoMode;
        final /* synthetic */ boolean val$enableSatellite;
        final /* synthetic */ boolean val$isEmergency;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Handler handler, String str, IIntegerConsumer iIntegerConsumer, boolean z, boolean z2, boolean z3) {
            super(handler);
            this.val$caller = str;
            this.val$callback = iIntegerConsumer;
            this.val$enableSatellite = z;
            this.val$enableDemoMode = z2;
            this.val$isEmergency = z3;
        }

        public static /* synthetic */ boolean $r8$lambda$t8bhmOnjoWO88QavNUtn9TUElFk(Integer num) {
            return num.intValue() == 3 || num.intValue() == 1 || num.intValue() == 0;
        }

        @Override // android.os.ResultReceiver
        protected void onReceiveResult(int i, Bundle bundle) {
            boolean z;
            Log.d("PhoneInterfaceManager", "Satellite access restriction resultCode=" + i + ", resultData=" + bundle);
            PhoneInterfaceManager.this.mSatelliteController.decrementResultReceiverCount(this.val$caller);
            IIntegerConsumer iIntegerConsumer = this.val$callback;
            Objects.requireNonNull(iIntegerConsumer);
            Consumer consumerIgnoreRemoteException = FunctionalUtils.ignoreRemoteException(new PhoneInterfaceManager$$ExternalSyntheticLambda5(iIntegerConsumer));
            if (i == 0) {
                if (bundle != null && bundle.containsKey("satellite_communication_allowed")) {
                    z = bundle.getBoolean("satellite_communication_allowed");
                } else {
                    PhoneInterfaceManager.loge("KEY_SATELLITE_COMMUNICATION_ALLOWED does not exist.");
                    z = false;
                }
                if (PhoneInterfaceManager.this.mSatelliteAccessController.getSatelliteDisallowedReasons().stream().anyMatch(new Predicate() { // from class: com.android.phone.PhoneInterfaceManager$3$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return PhoneInterfaceManager.AnonymousClass3.$r8$lambda$t8bhmOnjoWO88QavNUtn9TUElFk((Integer) obj);
                    }
                })) {
                    Log.d("PhoneInterfaceManager", "Satellite access is disallowed for current location.");
                    consumerIgnoreRemoteException.accept(16);
                    return;
                } else if (z) {
                    PhoneInterfaceManager.this.mSatelliteAccessController.updateSystemSelectionChannels(new ResultReceiver(PhoneInterfaceManager.this.mMainThreadHandler) { // from class: com.android.phone.PhoneInterfaceManager.3.1
                        @Override // android.os.ResultReceiver
                        protected void onReceiveResult(int i2, Bundle bundle2) {
                            Log.d("PhoneInterfaceManager", "updateSystemSelectionChannels resultCode=" + i2);
                            SatelliteController satelliteController = PhoneInterfaceManager.this.mSatelliteController;
                            AnonymousClass3 anonymousClass3 = AnonymousClass3.this;
                            satelliteController.requestSatelliteEnabled(anonymousClass3.val$enableSatellite, anonymousClass3.val$enableDemoMode, anonymousClass3.val$isEmergency, anonymousClass3.val$callback);
                        }
                    });
                    return;
                } else {
                    consumerIgnoreRemoteException.accept(16);
                    return;
                }
            }
            consumerIgnoreRemoteException.accept(Integer.valueOf(i));
        }
    }

    public void requestEnableSatellite(int i, EnableRequestAttributes enableRequestAttributes, IIntegerConsumer iIntegerConsumer) {
        enforceSatelliteCommunicationPermission("requestSatelliteEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Log.d("PhoneInterfaceManager", "requestEnableSatellite: subId=" + i + ", attributes=" + enableRequestAttributes);
            if (enableRequestAttributes.getConnectType() != 0) {
                Log.d("PhoneInterfaceManager", "requestEnableSatellite: falling back to requestSatelliteEnabled");
                this.mSatelliteController.requestSatelliteEnabled(enableRequestAttributes.isEnabled(), enableRequestAttributes.isDemoMode(), enableRequestAttributes.isEmergencyMode(), iIntegerConsumer);
            } else if (enableRequestAttributes.getSatelliteEnablementRequestReason() == 2) {
                Log.d("PhoneInterfaceManager", "requestEnableSatellite: isAutomaticAndUser, calling requestEnableSatelliteForCarrier with enable=" + enableRequestAttributes.isEnabled());
                this.mSatelliteController.requestEnableSatelliteForCarrier(i, enableRequestAttributes.isEnabled(), 0, iIntegerConsumer);
            } else {
                Log.d("PhoneInterfaceManager", "requestEnableSatellite: isAutomatic but reason is " + enableRequestAttributes.getSatelliteEnablementRequestReason() + ", returning SATELLITE_RESULT_REQUEST_NOT_SUPPORTED");
                try {
                    iIntegerConsumer.accept(11);
                } catch (RemoteException e) {
                    Log.e("PhoneInterfaceManager", "requestEnableSatellite: " + e);
                }
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void requestIsSatelliteEnabled(ResultReceiver resultReceiver) {
        enforceSatelliteCommunicationPermission("requestIsSatelliteEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.requestIsSatelliteEnabled(resultReceiver);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void requestEnableSatelliteStatus(int i, int i2, ResultReceiver resultReceiver) {
        enforceSatelliteCommunicationPermission("requestEnableSatelliteStatus");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Log.d("PhoneInterfaceManager", "requestEnableSatelliteStatus: subId=" + i + ", connectType=" + i2);
            SatelliteController satelliteController = this.mSatelliteController;
            if (i2 == 0) {
                Set attachRestrictionReasonsForCarrier = satelliteController.getAttachRestrictionReasonsForCarrier(i);
                Log.d("PhoneInterfaceManager", "requestEnableSatelliteStatus: restrictions=" + attachRestrictionReasonsForCarrier);
                Bundle bundle = new Bundle();
                bundle.putParcelable("enable_response", new EnableResponse(attachRestrictionReasonsForCarrier.isEmpty(), false, false, new int[0]));
                resultReceiver.send(0, bundle);
            } else {
                satelliteController.requestIsSatelliteEnabled(resultReceiver);
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void requestIsDemoModeEnabled(ResultReceiver resultReceiver) {
        enforceSatelliteCommunicationPermission("requestIsDemoModeEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.requestIsDemoModeEnabled(resultReceiver);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void requestIsEmergencyModeEnabled(ResultReceiver resultReceiver) {
        enforceSatelliteCommunicationPermission("requestIsEmergencyModeEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.requestIsEmergencyModeEnabled(resultReceiver);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void requestIsSatelliteSupported(ResultReceiver resultReceiver) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.requestIsSatelliteSupported(resultReceiver);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void requestSatelliteCapabilities(ResultReceiver resultReceiver) {
        enforceSatelliteCommunicationPermission("requestSatelliteCapabilities");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.requestSatelliteCapabilities(resultReceiver);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void startSatelliteTransmissionUpdates(IIntegerConsumer iIntegerConsumer, ISatelliteTransmissionUpdateCallback iSatelliteTransmissionUpdateCallback) {
        enforceSatelliteCommunicationPermission("startSatelliteTransmissionUpdates");
        if (shouldIgnoreSatelliteRequestInCtsMode("startSatelliteTransmissionUpdates", iIntegerConsumer)) {
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.startSatelliteTransmissionUpdates(iIntegerConsumer, iSatelliteTransmissionUpdateCallback);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void stopSatelliteTransmissionUpdates(IIntegerConsumer iIntegerConsumer, ISatelliteTransmissionUpdateCallback iSatelliteTransmissionUpdateCallback) {
        enforceSatelliteCommunicationPermission("stopSatelliteTransmissionUpdates");
        if (shouldIgnoreSatelliteRequestInCtsMode("stopSatelliteTransmissionUpdates", iIntegerConsumer)) {
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.stopSatelliteTransmissionUpdates(iIntegerConsumer, iSatelliteTransmissionUpdateCallback);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public ICancellationSignal provisionSatelliteService(String str, byte[] bArr, IIntegerConsumer iIntegerConsumer) {
        enforceSatelliteCommunicationPermission("provisionSatelliteService");
        if (shouldIgnoreSatelliteRequestInCtsMode("provisionSatelliteService", iIntegerConsumer)) {
            return null;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.provisionSatelliteService(str, bArr, iIntegerConsumer);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void deprovisionSatelliteService(String str, IIntegerConsumer iIntegerConsumer) {
        enforceSatelliteCommunicationPermission("deprovisionSatelliteService");
        if (shouldIgnoreSatelliteRequestInCtsMode("deprovisionSatelliteService", iIntegerConsumer)) {
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.deprovisionSatelliteService(str, iIntegerConsumer);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int registerForSatelliteProvisionStateChanged(ISatelliteProvisionStateCallback iSatelliteProvisionStateCallback) {
        enforceSatelliteCommunicationPermission("registerForSatelliteProvisionStateChanged");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.registerForSatelliteProvisionStateChanged(iSatelliteProvisionStateCallback);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void unregisterForSatelliteProvisionStateChanged(ISatelliteProvisionStateCallback iSatelliteProvisionStateCallback) {
        enforceSatelliteCommunicationPermission("unregisterForSatelliteProvisionStateChanged");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.unregisterForSatelliteProvisionStateChanged(iSatelliteProvisionStateCallback);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void requestIsSatelliteProvisioned(ResultReceiver resultReceiver) {
        enforceSatelliteCommunicationPermission("requestIsSatelliteProvisioned");
        if (shouldIgnoreSatelliteRequestInCtsMode("requestIsSatelliteProvisioned", resultReceiver)) {
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.requestIsSatelliteProvisioned(resultReceiver);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int registerForSatelliteModemStateChanged(ISatelliteModemStateCallback iSatelliteModemStateCallback) {
        enforceSatelliteCommunicationPermission("registerForSatelliteModemStateChanged");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.registerForSatelliteModemStateChanged(iSatelliteModemStateCallback);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void unregisterForModemStateChanged(ISatelliteModemStateCallback iSatelliteModemStateCallback) {
        enforceSatelliteCommunicationPermission("unregisterForModemStateChanged");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.unregisterForModemStateChanged(iSatelliteModemStateCallback);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int registerForIncomingDatagram(ISatelliteDatagramCallback iSatelliteDatagramCallback) {
        enforceSatelliteCommunicationPermission("registerForIncomingDatagram");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.registerForIncomingDatagram(iSatelliteDatagramCallback);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void unregisterForIncomingDatagram(ISatelliteDatagramCallback iSatelliteDatagramCallback) {
        enforceSatelliteCommunicationPermission("unregisterForIncomingDatagram");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.unregisterForIncomingDatagram(iSatelliteDatagramCallback);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void pollPendingDatagrams(IIntegerConsumer iIntegerConsumer) {
        enforceSatelliteCommunicationPermission("pollPendingDatagrams");
        if (shouldIgnoreSatelliteRequestInCtsMode("pollPendingDatagrams", iIntegerConsumer)) {
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.pollPendingDatagrams(iIntegerConsumer);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void sendDatagram(int i, SatelliteDatagram satelliteDatagram, boolean z, IIntegerConsumer iIntegerConsumer) {
        enforceSatelliteCommunicationPermission("sendDatagram");
        if (shouldIgnoreSatelliteRequestInCtsMode("sendDatagram", iIntegerConsumer)) {
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.sendDatagram(i, satelliteDatagram, z, iIntegerConsumer);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int[] getSatelliteDisallowedReasons() {
        enforceSatelliteCommunicationPermission("getSatelliteDisallowedReasons");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteAccessController.getSatelliteDisallowedReasons().stream().mapToInt(new CarrierConfigLoader$$ExternalSyntheticLambda0()).toArray();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void registerForSatelliteDisallowedReasonsChanged(ISatelliteDisallowedReasonsCallback iSatelliteDisallowedReasonsCallback) {
        enforceSatelliteCommunicationPermission("registerForSatelliteDisallowedReasonsChanged");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteAccessController.registerForSatelliteDisallowedReasonsChanged(iSatelliteDisallowedReasonsCallback);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void unregisterForSatelliteDisallowedReasonsChanged(ISatelliteDisallowedReasonsCallback iSatelliteDisallowedReasonsCallback) {
        enforceSatelliteCommunicationPermission("unregisterForSatelliteDisallowedReasonsChanged");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteAccessController.unregisterForSatelliteDisallowedReasonsChanged(iSatelliteDisallowedReasonsCallback);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void requestEntitlementRefresh(int i, IIntegerConsumer iIntegerConsumer) {
        enforceSatelliteCommunicationPermission("requestEntitlementRefresh");
        Log.d("PhoneInterfaceManager", "requestEntitlementRefresh: subId = " + i);
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteEntitlementController.requestEntitlementRefresh(i, iIntegerConsumer);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void requestIsCommunicationAllowedForCurrentLocation(int i, ResultReceiver resultReceiver) {
        enforceSatelliteCommunicationPermission("requestIsCommunicationAllowedForCurrentLocation");
        if (shouldIgnoreSatelliteRequestInCtsMode("requestIsCommunicationAllowedForCurrentLocation", resultReceiver)) {
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteAccessController.requestIsCommunicationAllowedForCurrentLocation(resultReceiver, false);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void requestSatelliteAccessConfigurationForCurrentLocation(ResultReceiver resultReceiver) {
        enforceSatelliteCommunicationPermission("requestSatelliteAccessConfigurationForCurrentLocation");
        if (shouldIgnoreSatelliteRequestInCtsMode("requestSatelliteAccessConfigurationForCurrentLocation", resultReceiver)) {
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteAccessController.requestSatelliteAccessConfigurationForCurrentLocation(resultReceiver);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void requestTimeForNextSatelliteVisibility(ResultReceiver resultReceiver) {
        enforceSatelliteCommunicationPermission("requestTimeForNextSatelliteVisibility");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.requestTimeForNextSatelliteVisibility(resultReceiver);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void requestSatelliteDisplayName(ResultReceiver resultReceiver) {
        enforceSatelliteCommunicationPermission("requestSatelliteDisplayName");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.requestSatelliteDisplayName(resultReceiver);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void requestSelectedNbIotSatelliteSubscriptionId(ResultReceiver resultReceiver) {
        enforceSatelliteCommunicationPermission("requestSelectedNbIotSatelliteSubscriptionId");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.requestSelectedNbIotSatelliteSubscriptionId(resultReceiver);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int registerForSelectedNbIotSatelliteSubscriptionChanged(ISelectedNbIotSatelliteSubscriptionCallback iSelectedNbIotSatelliteSubscriptionCallback) {
        enforceSatelliteCommunicationPermission("registerForSelectedNbIotSatelliteSubscriptionChanged");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.registerForSelectedNbIotSatelliteSubscriptionChanged(iSelectedNbIotSatelliteSubscriptionCallback);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void unregisterForSelectedNbIotSatelliteSubscriptionChanged(ISelectedNbIotSatelliteSubscriptionCallback iSelectedNbIotSatelliteSubscriptionCallback) {
        enforceSatelliteCommunicationPermission("unregisterForSelectedNbIotSatelliteSubscriptionChanged");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.unregisterForSelectedNbIotSatelliteSubscriptionChanged(iSelectedNbIotSatelliteSubscriptionCallback);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setDeviceAlignedWithSatellite(boolean z) {
        enforceSatelliteCommunicationPermission("setDeviceAlignedWithSatellite");
        if (shouldIgnoreSatelliteRequestInCtsMode("setDeviceAlignedWithSatellite", null)) {
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.setDeviceAlignedWithSatellite(z);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void addAttachRestrictionForCarrier(int i, int i2, IIntegerConsumer iIntegerConsumer) {
        enforceSatelliteCommunicationPermission("addAttachRestrictionForCarrier");
        if (shouldIgnoreSatelliteRequestInCtsMode("addAttachRestrictionForCarrier", iIntegerConsumer)) {
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.addAttachRestrictionForCarrier(i, i2, iIntegerConsumer);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void removeAttachRestrictionForCarrier(int i, int i2, IIntegerConsumer iIntegerConsumer) {
        enforceSatelliteCommunicationPermission("removeAttachRestrictionForCarrier");
        if (shouldIgnoreSatelliteRequestInCtsMode("removeAttachRestrictionForCarrier", iIntegerConsumer)) {
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.removeAttachRestrictionForCarrier(i, i2, iIntegerConsumer);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int[] getAttachRestrictionReasonsForCarrier(int i) {
        enforceSatelliteCommunicationPermission("getAttachRestrictionReasonsForCarrier");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.getAttachRestrictionReasonsForCarrier(i).stream().mapToInt(new ToIntFunction() { // from class: com.android.phone.PhoneInterfaceManager$$ExternalSyntheticLambda2
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    return ((Integer) obj).intValue();
                }
            }).toArray();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void requestNtnSignalStrength(ResultReceiver resultReceiver) {
        enforceSatelliteCommunicationPermission("requestNtnSignalStrength");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.requestNtnSignalStrength(resultReceiver);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void registerForNtnSignalStrengthChanged(INtnSignalStrengthCallback iNtnSignalStrengthCallback) {
        enforceSatelliteCommunicationPermission("registerForNtnSignalStrengthChanged");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.registerForNtnSignalStrengthChanged(iNtnSignalStrengthCallback);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void unregisterForNtnSignalStrengthChanged(INtnSignalStrengthCallback iNtnSignalStrengthCallback) {
        enforceSatelliteCommunicationPermission("unregisterForNtnSignalStrengthChanged");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.unregisterForNtnSignalStrengthChanged(iNtnSignalStrengthCallback);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int registerForCapabilitiesChanged(ISatelliteCapabilitiesCallback iSatelliteCapabilitiesCallback) {
        enforceSatelliteCommunicationPermission("registerForCapabilitiesChanged");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.registerForCapabilitiesChanged(iSatelliteCapabilitiesCallback);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void unregisterForCapabilitiesChanged(ISatelliteCapabilitiesCallback iSatelliteCapabilitiesCallback) {
        enforceSatelliteCommunicationPermission("unregisterForCapabilitiesChanged");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.unregisterForCapabilitiesChanged(iSatelliteCapabilitiesCallback);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int registerForSatelliteSupportedStateChanged(IBooleanConsumer iBooleanConsumer) {
        enforceSatelliteCommunicationPermission("registerForSatelliteSupportedStateChanged");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.registerForSatelliteSupportedStateChanged(iBooleanConsumer);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void unregisterForSatelliteSupportedStateChanged(IBooleanConsumer iBooleanConsumer) {
        enforceSatelliteCommunicationPermission("unregisterForSatelliteSupportedStateChanged");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.unregisterForSatelliteSupportedStateChanged(iBooleanConsumer);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setSatelliteServicePackageName(String str, String str2) {
        Log.d("PhoneInterfaceManager", "setSatelliteServicePackageName - " + str + ", provisioned=" + str2);
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setSatelliteServicePackageName");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, -1, "setSatelliteServicePackageName");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.setSatelliteServicePackageName(str, str2);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setSatelliteAccessAllowedForSubscriptions(boolean z, String str) {
        Log.d("PhoneInterfaceManager", "setSatelliteAccessAllowedForSubscriptions - " + str + ", reset=" + z);
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setSatelliteAccessAllowedForSubscriptions");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, -1, "setSatelliteAccessAllowedForSubscriptions");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.setSatelliteAccessAllowedForSubscriptions(z, str);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setSatelliteGatewayServicePackageName(String str) {
        Log.d("PhoneInterfaceManager", "setSatelliteGatewayServicePackageName - " + str);
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setSatelliteGatewayServicePackageName");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, -1, "setSatelliteGatewayServicePackageName");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.setSatelliteGatewayServicePackageName(str);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setSatellitePointingUiClassName(String str, String str2) {
        Log.d("PhoneInterfaceManager", "setSatellitePointingUiClassName: packageName=" + str + ", className=" + str2);
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setSatellitePointingUiClassName");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, -1, "setSatellitePointingUiClassName");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.setSatellitePointingUiClassName(str, str2);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setSatelliteListeningTimeoutDuration(long j) {
        Log.d("PhoneInterfaceManager", "setSatelliteListeningTimeoutDuration - " + j);
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setSatelliteListeningTimeoutDuration");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, -1, "setSatelliteListeningTimeoutDuration");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.setSatelliteListeningTimeoutDuration(j);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setTnScanningSupport(boolean z, boolean z2, boolean z3) {
        Log.d("PhoneInterfaceManager", "setTnScanningSupport: reset= " + z + ", concurrentTnScanningSupported=" + z2 + ", tnScanningDuringSatelliteSessionAllowed=" + z3);
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setTnScanningSupport");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, -1, "setTnScanningSupport");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.setTnScanningSupport(z, z2, z3);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setSatelliteIgnoreCellularServiceState(boolean z) {
        Log.d("PhoneInterfaceManager", "setSatelliteIgnoreCellularServiceState - " + z);
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setSatelliteIgnoreCellularServiceState");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, -1, "setSatelliteIgnoreCellularServiceState");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.setSatelliteIgnoreCellularServiceState(z);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setSupportDisableSatelliteWhileEnableInProgress(boolean z, boolean z2) {
        Log.d("PhoneInterfaceManager", "setSupportDisableSatelliteWhileEnableInProgress - reset=" + z + ", supported=" + z2);
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setSupportDisableSatelliteWhileEnableInProgress");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, -1, "setSupportDisableSatelliteWhileEnableInProgress");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.setSupportDisableSatelliteWhileEnableInProgress(z, z2);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setDatagramControllerTimeoutDuration(boolean z, int i, long j) {
        Log.d("PhoneInterfaceManager", "setDatagramControllerTimeoutDuration - " + j + ", reset=" + z + ", timeoutMillis=" + j);
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setDatagramControllerTimeoutDuration");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, -1, "setDatagramControllerTimeoutDuration");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.setDatagramControllerTimeoutDuration(z, i, j);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setDatagramControllerBooleanConfig(boolean z, int i, boolean z2) {
        Log.d("PhoneInterfaceManager", "setDatagramControllerBooleanConfig: booleanType=" + i + ", reset=" + z + ", enable=" + z2);
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setDatagramControllerBooleanConfig");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, -1, "ssetDatagramControllerBooleanConfig");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.setDatagramControllerBooleanConfig(z, i, z2);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setSatelliteControllerTimeoutDuration(boolean z, int i, long j) {
        Log.d("PhoneInterfaceManager", "setSatelliteControllerTimeoutDuration - " + j + ", reset=" + z + ", timeoutMillis=" + j);
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setSatelliteControllerTimeoutDuration");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, -1, "setSatelliteControllerTimeoutDuration");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.setSatelliteControllerTimeoutDuration(z, i, j);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setEmergencyCallToSatelliteHandoverType(int i, int i2, int i3) {
        Log.d("PhoneInterfaceManager", "setEmergencyCallToSatelliteHandoverType - " + i + ", delaySeconds=" + i2 + ", simSlotId=" + i3);
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setEmergencyCallToSatelliteHandoverType");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, -1, "setEmergencyCallToSatelliteHandoverType");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.setEmergencyCallToSatelliteHandoverType(i, i2, i3);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setOemEnabledSatelliteProvisionStatus(boolean z, boolean z2) {
        Log.d("PhoneInterfaceManager", "setOemEnabledSatelliteProvisionStatus - reset=" + z + ", isProvisioned=" + z2);
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setOemEnabledSatelliteProvisionStatus");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, -1, "setOemEnabledSatelliteProvisionStatus");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.setOemEnabledSatelliteProvisionStatus(z, z2);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setCountryCodes(boolean z, List list, Map map, String str, long j) {
        Log.d("PhoneInterfaceManager", "setCountryCodes: currentNetworkCountryCodes=" + String.join(", ", list) + ", locationCountryCode=" + str + ", locationCountryCodeTimestampNanos" + j + ", reset=" + z + ", cachedNetworkCountryCodes=" + String.join(", ", map.keySet()));
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setCountryCodes");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, -1, "setCountryCodes");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return TelephonyCountryDetector.getInstance(getDefaultPhone().getContext(), this.mFeatureFlags).setCountryCodes(z, list, map, str, j);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean overrideConfigDataVersion(boolean z, int i) {
        Log.d("PhoneInterfaceManager", "overrideVersion - reset=" + z + ", version=" + i);
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "overrideConfigDataVersion");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, -1, "overrideVersion");
        return TelephonyConfigUpdateInstallReceiver.getInstance().overrideVersion(z, i);
    }

    public boolean setSatelliteAccessControlOverlayConfigs(boolean z, boolean z2, String str, long j, List list, String str2) {
        StringBuilder sb = new StringBuilder("setSatelliteAccessControlOverlayConfigs: reset=");
        sb.append(z);
        sb.append(", isAllowed");
        sb.append(z2);
        sb.append(", s2CellFile=");
        sb.append(str);
        sb.append(", locationFreshDurationNanos=");
        sb.append(j);
        sb.append(", satelliteCountryCodes=");
        sb.append(list != null ? String.join(", ", list) : null);
        sb.append(", satelliteAccessConfigurationFile=");
        sb.append(str2);
        Log.d("PhoneInterfaceManager", sb.toString());
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setSatelliteAccessControlOverlayConfigs");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, -1, "setSatelliteAccessControlOverlayConfigs");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteAccessController.setSatelliteAccessControlOverlayConfigs(z, z2, str, j, list, str2);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setShouldSendDatagramToModemInDemoMode(boolean z) {
        Log.d("PhoneInterfaceManager", "setShouldSendDatagramToModemInDemoMode");
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setShouldSendDatagramToModemInDemoMode");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, -1, "setShouldSendDatagramToModemInDemoMode");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.setShouldSendDatagramToModemInDemoMode(z);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setIsSatelliteCommunicationAllowedForCurrentLocationCache(String str) {
        Log.d("PhoneInterfaceManager", "setIsSatelliteCommunicationAllowedForCurrentLocationCache: state=" + str);
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setIsSatelliteCommunicationAllowedForCurrentLocationCache");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, -1, "setIsSatelliteCommunicationAllowedForCurrentLocationCache");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteAccessController.setIsSatelliteCommunicationAllowedForCurrentLocationCache(str);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean overrideSatelliteEntilementStatusResponseForCtsTest(String str, boolean z) {
        Log.d("PhoneInterfaceManager", "overrideSatelliteEntilementStatusResponseForCtsTest: overriddenEntilementStatusResponse=" + str);
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "overrideSatelliteEntilementStatusResponseForCtsTest");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, -1, "overrideSatelliteEntilementStatusResponseForCtsTest");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteEntitlementController.overrideEntilementStatusResponseForCtsTest(str, z);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean overrideSatelliteEntilementQueryConditions(boolean z, boolean z2) {
        Log.d("PhoneInterfaceManager", "overrideSatelliteEntilementQueryConditions: ignoreInternetConnection=" + z + ", ignoreRefreshCondition=" + z2);
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "overrideSatelliteEntilementQueryConditions");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, -1, "overrideEntilementQueryConditions");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteEntitlementController.overrideEntilementQueryConditions(z, z2);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setMaxAllowedSatelliteDataModeForCtsTest(int i) {
        Log.d("PhoneInterfaceManager", "setMaxAllowedSatelliteDataModeForCtsTest: maxAllowedDataMode=" + i);
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setMaxAllowedSatelliteDataModeForCtsTest");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, -1, "setMaxAllowedSatelliteDataModeForCtsTest");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.setMaxAllowedDataModeForCtsTest(i);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean uncapMaxAllowedSatelliteDataMode() {
        Log.d("PhoneInterfaceManager", "uncapMaxAllowedSatelliteDataMode");
        TelephonyPermissions.enforceDebugBuildsOnly("uncapMaxAllowedSatelliteDataMode");
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "uncapMaxAllowedSatelliteDataMode");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, -1, "uncapMaxAllowedSatelliteDataMode");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.uncapMaxAllowedDataMode();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setDomainSelectionServiceOverride(ComponentName componentName) {
        Log.i("PhoneInterfaceManager", "setDomainSelectionServiceOverride component=" + componentName);
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setDomainSelectionServiceOverride");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, getDefaultSubscription(), "setDomainSelectionServiceOverride");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (DomainSelectionResolver.getInstance().isDomainSelectionSupported()) {
                return DomainSelectionResolver.getInstance().setDomainSelectionServiceOverride(componentName);
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean clearDomainSelectionServiceOverride() {
        Log.i("PhoneInterfaceManager", "clearDomainSelectionServiceOverride");
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "clearDomainSelectionServiceOverride");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, getDefaultSubscription(), "clearDomainSelectionServiceOverride");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (DomainSelectionResolver.getInstance().isDomainSelectionSupported()) {
                return DomainSelectionResolver.getInstance().clearDomainSelectionServiceOverride();
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setEnableCellularIdentifierDisclosureNotifications(boolean z) {
        enforceModifyPermission();
        checkForIdentifierDisclosureNotificationSupport();
        SharedPreferences.Editor editorEdit = this.mTelephonySharedPreferences.edit();
        editorEdit.putBoolean("pref_identifier_disclosure_notifications_enabled", z);
        editorEdit.apply();
        for (Phone phone : PhoneFactory.getPhones()) {
            phone.handleIdentifierDisclosureNotificationPreferenceChange();
        }
    }

    public boolean isCellularIdentifierDisclosureNotificationsEnabled() {
        enforceReadPrivilegedPermission("isCellularIdentifierDisclosureNotificationEnabled");
        checkForIdentifierDisclosureNotificationSupport();
        return getDefaultPhone().getIdentifierDisclosureNotificationsPreferenceEnabled();
    }

    public int[] getSupportedNetworkAlertCategories() {
        enforceReadPrivilegedPermission("getSupportedNetworkAlertCategories");
        if (getHalVersion(4) < 204) {
            MergeMotion$$ExternalSyntheticBUOutline0.m("Network alert operations require HAL 2.4 or above");
            return null;
        }
        try {
            return getDefaultPhone().getSupportedNetworkAlertCategories();
        } catch (UnsupportedOperationException e) {
            Log.e("PhoneInterfaceManager", "getSupportedNetworkAlertCategories: UnsupportedOperationException", e);
            return new int[0];
        }
    }

    public void setNullCipherNotificationsEnabled(boolean z) {
        enforceModifyPermission();
        checkForNullCipherNotificationSupport();
        SharedPreferences.Editor editorEdit = this.mTelephonySharedPreferences.edit();
        editorEdit.putBoolean("pref_null_cipher_notifications_enabled", z);
        editorEdit.apply();
        for (Phone phone : PhoneFactory.getPhones()) {
            phone.handleNullCipherNotificationPreferenceChanged();
        }
    }

    public boolean isNullCipherNotificationsEnabled() {
        enforceReadPrivilegedPermission("isNullCipherNotificationsEnabled");
        checkForNullCipherNotificationSupport();
        return getDefaultPhone().getNullCipherNotificationsPreferenceEnabled();
    }

    private static boolean checkCallingOrSelfReadDeviceIdentifiersForAnySub(Context context, String str, String str2, String str3) {
        for (Phone phone : PhoneFactory.getPhones()) {
            if (TelephonyPermissions.checkCallingOrSelfReadDeviceIdentifiers(context, phone.getSubId(), str, str2, str3)) {
                return true;
            }
        }
        return false;
    }

    public SubscriptionManagerService getSubscriptionManagerService() {
        return SubscriptionManagerService.getInstance();
    }

    class CallerCallbackInfo {
        private final Set mCarrierIds;
        private final Consumer mConsumer;

        public CallerCallbackInfo(Consumer consumer, Set set) {
            this.mConsumer = consumer;
            this.mCarrierIds = set;
        }

        public Consumer getConsumer() {
            return this.mConsumer;
        }

        public Set getCarrierIds() {
            return this.mCarrierIds;
        }
    }

    public void setPackageManager(PackageManager packageManager) {
        this.mPackageManager = packageManager;
    }

    public void setAppOpsManager(AppOpsManager appOpsManager) {
        this.mAppOps = appOpsManager;
    }

    public void setFeatureFlags(FeatureFlags featureFlags) {
        this.mFeatureFlags = featureFlags;
    }

    private void enforceTelephonyFeatureWithException(String str, String str2, String str3) {
        TelephonyUtils.enforceTelephonyFeatureWithException(str, this.mPackageManager, this.mVendorApiLevel, str2, str3);
    }

    private void enforceTelephonyFeatureWithException(String str, List list, String str2) {
        TelephonyUtils.enforceTelephonyFeatureWithException(str, this.mPackageManager, this.mVendorApiLevel, list, str2);
    }

    public int registerForCommunicationAccessStateChanged(int i, ISatelliteCommunicationAccessStateCallback iSatelliteCommunicationAccessStateCallback) {
        enforceSatelliteCommunicationPermission("registerForCommunicationAccessStateChanged");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteAccessController.registerForCommunicationAccessStateChanged(i, iSatelliteCommunicationAccessStateCallback);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void unregisterForCommunicationAccessStateChanged(int i, ISatelliteCommunicationAccessStateCallback iSatelliteCommunicationAccessStateCallback) {
        enforceSatelliteCommunicationPermission("unregisterForCommunicationAccessStateChanged");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteAccessController.unregisterForCommunicationAccessStateChanged(i, iSatelliteCommunicationAccessStateCallback);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void requestSatelliteSessionStats(int i, ResultReceiver resultReceiver) {
        enforceModifyPermission();
        enforcePackageUsageStatsPermission("requestSatelliteSessionStats");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.requestSatelliteSessionStats(i, resultReceiver);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void requestSatelliteSubscriberProvisionStatus(ResultReceiver resultReceiver) {
        enforceSatelliteCommunicationPermission("requestSatelliteSubscriberProvisionStatus");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.requestSatelliteSubscriberProvisionStatus(resultReceiver);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void provisionSatellite(List list, ResultReceiver resultReceiver) {
        enforceSatelliteCommunicationPermission("provisionSatellite");
        if (shouldIgnoreSatelliteRequestInCtsMode("provisionSatellite", resultReceiver)) {
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.provisionSatellite(list, resultReceiver);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void deprovisionSatellite(List list, ResultReceiver resultReceiver) {
        enforceSatelliteCommunicationPermission("deprovisionSatellite");
        if (shouldIgnoreSatelliteRequestInCtsMode("deprovisionSatellite", resultReceiver)) {
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.deprovisionSatellite(list, resultReceiver);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setNtnSmsSupported(boolean z) {
        enforceSatelliteCommunicationPermission("setNtnSmsSupported");
        enforceSendSmsPermission();
        if (shouldIgnoreSatelliteRequestInCtsMode("setNtnSmsSupported", null)) {
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.setNtnSmsSupportedByMessagesApp(z);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setSatelliteSubscriberIdListChangedIntentComponent(String str) {
        Log.d("PhoneInterfaceManager", "setSatelliteSubscriberIdListChangedIntentComponent");
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setSatelliteSubscriberIdListChangedIntentComponent");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, -1, "setSatelliteSubscriberIdListChangedIntentComponent");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.setSatelliteSubscriberIdListChangedIntentComponent(str);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setTestEuiccUiComponent(ComponentName componentName) {
        enforceModifyPermission();
        log("setTestEuiccUiComponent: " + componentName);
        this.mTestEuiccUiComponent = componentName;
    }

    public ComponentName getTestEuiccUiComponent() {
        enforceReadPrivilegedPermission("getTestEuiccUiComponent");
        return this.mTestEuiccUiComponent;
    }

    public boolean overrideCarrierRoamingNtnEligibilityChanged(boolean z, boolean z2) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteAccessController.overrideCarrierRoamingNtnEligibilityChanged(z, z2);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getCarrierIdFromIdentifier(CarrierIdentifier carrierIdentifier) {
        enforceReadPrivilegedPermission("getCarrierIdFromIdentifier");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "getCarrierIdFromIdentifier");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return CarrierResolver.getCarrierIdFromIdentifier(this.mApp, carrierIdentifier, true);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public List getSatelliteDataOptimizedApps() {
        enforceSatelliteCommunicationPermission("getSatelliteDataOptimizedApps");
        new ArrayList();
        int identifier = Binder.getCallingUserHandle().getIdentifier();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.getSatelliteDataOptimizedApps(identifier);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getSatelliteDataSupportMode(int i) {
        enforceSatelliteCommunicationPermission("getSatelliteDataSupportMode");
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            HexString$$ExternalSyntheticBUOutline1.m("Invalid Subscription ID: ", i);
            return 0;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.getSatelliteDataSupportMode(i);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public PlmnSatelliteConfig getPlmnSatelliteConfig(int i, String str) {
        enforceSatelliteCommunicationPermission("getPlmnSatelliteConfig");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.getPlmnSatelliteConfig(i, str);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void notifyEntitlementStatusChanged(int i, List list, long j) {
        enforceModifyPermission();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (list.contains("ap2016")) {
                this.mSatelliteEntitlementController.requestEntitlementRefresh(i, new IIntegerConsumer.Stub(this) { // from class: com.android.phone.PhoneInterfaceManager.4
                    public void accept(int i2) {
                        Log.d("PhoneInterfaceManager", "Entitlement refresh result: " + i2);
                    }
                });
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isInCarrierRoamingNtnMode(int i) {
        enforceSatelliteCommunicationPermission("isInCarrierRoamingNtnMode");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.isInSatelliteModeForCarrierRoaming(SatelliteServiceUtils.getPhone(i));
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int[] getCarrierRoamingNtnAvailableServices(int i) {
        enforceSatelliteCommunicationPermission("getCarrierRoamingNtnAvailableServices");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.getSupportedServicesOnCarrierRoamingNtn(i);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void requestPointingUiAppLaunchIntent(PointingUiAppLaunchIntentAttributes pointingUiAppLaunchIntentAttributes, ResultReceiver resultReceiver) {
        enforceSatelliteCommunicationPermission("requestPointingUiAppLaunchIntent");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSatelliteController.requestPointingUiAppLaunchIntent(pointingUiAppLaunchIntentAttributes, resultReceiver);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setCtsMode(boolean z) {
        Rlog.d("PhoneInterfaceManager", "setCtsMode - " + z);
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setCtsMode");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, -1, "setCtsMode");
        this.mIsInCtsMode = z;
        return true;
    }

    public boolean setSatelliteIgnorePlmnListFromStorage(boolean z) {
        Log.d("PhoneInterfaceManager", "setSatelliteIgnorePlmnListFromStorage - " + z);
        TelephonyPermissions.enforceShellOnly(Binder.getCallingUid(), "setSatelliteIgnorePlmnListFromStorage");
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, -1, "setSatelliteIgnorePlmnListFromStorage");
        return this.mSatelliteController.setSatelliteIgnorePlmnListFromStorage(z);
    }

    private boolean shouldIgnoreSatelliteRequestInCtsMode(String str, Object obj) {
        if (!this.mIsInCtsMode || isCallingPackageAllowedInCtsMode(str)) {
            return false;
        }
        if (obj == null) {
            return true;
        }
        if (obj instanceof ResultReceiver) {
            ((ResultReceiver) obj).send(11, null);
        } else if (obj instanceof IIntegerConsumer) {
            FunctionalUtils.ignoreRemoteException(new PhoneInterfaceManager$$ExternalSyntheticLambda5((IIntegerConsumer) obj)).accept(11);
        }
        return true;
    }

    private boolean isCallingPackageAllowedInCtsMode(String str) {
        int callingUid = Binder.getCallingUid();
        PackageManager packageManager = this.mApp.getBaseContext().createContextAsUser(Binder.getCallingUserHandle(), 0).getPackageManager();
        if (packageManager == null) {
            Rlog.e("PhoneInterfaceManager", "isCallingPackageAllowedInCtsMode: pm is null");
            return false;
        }
        String[] packagesForUid = packageManager.getPackagesForUid(callingUid);
        if (packagesForUid == null) {
            Log.e("PhoneInterfaceManager", "isCallingPackageAllowedInCtsMode: callingPackages is null for uid=" + callingUid);
            return false;
        }
        for (String str2 : packagesForUid) {
            Rlog.w("PhoneInterfaceManager", "isCallingPackageAllowedInCtsMode: checking " + str2);
            if (str2 != null && (str2.contains(".preconditions") || TextUtils.equals(str2, "android.telephony.cts") || TextUtils.equals(str2, "android.telephony.satellite.cts") || TextUtils.equals(str2, RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME))) {
                return true;
            }
        }
        Log.e("PhoneInterfaceManager", "isCallingPackageAllowedInCtsMode: callingUid=" + callingUid + ", callingPackages=" + String.join(",", packagesForUid) + " is not allowed in CTS mode for method: " + str);
        return false;
    }

    public int getSimAutoPinManagementEnrollmentStatus(int i) {
        SubscriptionInfo subscriptionInfo = getSubscriptionManagerService().getSubscriptionInfo(i);
        if (subscriptionInfo != null) {
            return UiccController.getInstance().getPinStorage().isPinPlatformManaged(subscriptionInfo.getCardString()) ? 1 : 0;
        }
        HexString$$ExternalSyntheticBUOutline1.m("Invalid subscription ID: ", i);
        return 0;
    }

    private Bundle putNumberOfRemainingPinAttemptsInBundle(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("managed_sim_pin_enrollment_attempts", i);
        return bundle;
    }

    private Bundle putManagedPinResultInBundle(String str) {
        Bundle bundle = new Bundle();
        bundle.putString("managed_sim_pin_enrollment_generated_pin", str);
        return bundle;
    }

    public void enrollSimInAutoPinManagement(int i, String str, ResultReceiver resultReceiver) {
        Bundle bundle;
        TelephonyPermissions.enforceControlSimAutoPinManagementPermission(this.mApp, "Missing permission to enroll SIM in automatic PIN management.");
        SubscriptionInfo subscriptionInfo = getSubscriptionManagerService().getSubscriptionInfo(i);
        if (subscriptionInfo == null) {
            resultReceiver.send(3, new Bundle());
            return;
        }
        Log.d("PhoneInterfaceManager", "Enrollment request for subId " + i);
        if (isIccLockEnabled(i)) {
            Log.d("PhoneInterfaceManager", "SIM currently locked, returning.");
            resultReceiver.send(4, new Bundle());
            return;
        }
        try {
            int i2 = 1;
            this.mCurrentlyModifyingSimAutoPinManagementState.set(true);
            int iccLockEnabled = setIccLockEnabled(i, true, str);
            if (iccLockEnabled != Integer.MAX_VALUE) {
                Log.d("PhoneInterfaceManager", "Failed to turn on SIM lock, error: " + iccLockEnabled);
                resultReceiver.send(1, putNumberOfRemainingPinAttemptsInBundle(iccLockEnabled));
                return;
            }
            String str2 = String.format("%04d", Integer.valueOf(new SecureRandom().nextInt(10000)));
            int iChangeIccLockPassword = changeIccLockPassword(i, str, str2);
            if (iChangeIccLockPassword == Integer.MAX_VALUE) {
                UiccController.getInstance().getPinStorage().storePlatformManagedPin(subscriptionInfo.getSimSlotIndex(), str2, str);
                resultReceiver.send(0, putManagedPinResultInBundle(str2));
                return;
            }
            Log.d("PhoneInterfaceManager", "Failed to change SIM PIN, error: " + iChangeIccLockPassword);
            if (iChangeIccLockPassword > 0) {
                bundle = putNumberOfRemainingPinAttemptsInBundle(iChangeIccLockPassword);
            } else {
                bundle = new Bundle();
                i2 = 5;
            }
            resultReceiver.send(i2, bundle);
        } finally {
            this.mCurrentlyModifyingSimAutoPinManagementState.set(false);
        }
    }

    public void unenrollSimFromAutoPinManagement(int i, ResultReceiver resultReceiver) {
        TelephonyPermissions.enforceControlSimAutoPinManagementPermission(this.mApp, "Missing permission to unenroll SIM from automatic PIN management.");
        SubscriptionInfo subscriptionInfo = getSubscriptionManagerService().getSubscriptionInfo(i);
        if (subscriptionInfo == null) {
            Log.d("PhoneInterfaceManager", "No subscription info for subscription ID " + i);
            resultReceiver.send(2, new Bundle());
            return;
        }
        int simSlotIndex = subscriptionInfo.getSimSlotIndex();
        String cardString = subscriptionInfo.getCardString();
        PinStorage pinStorage = UiccController.getInstance().getPinStorage();
        if (!pinStorage.isPinPlatformManaged(cardString)) {
            Log.d("PhoneInterfaceManager", "ICC not enrolled " + cardString + " on slot " + simSlotIndex);
            resultReceiver.send(1, new Bundle());
            return;
        }
        Log.d("PhoneInterfaceManager", "Proceeding to unenroll SIM " + cardString + " in slot " + simSlotIndex + " from automatic PIN management.");
        String pin = pinStorage.getPin(simSlotIndex, cardString);
        if (pin.equals("")) {
            Log.d("PhoneInterfaceManager", "Failed getting current pin.");
            resultReceiver.send(3, new Bundle());
            return;
        }
        String oldPin = pinStorage.getOldPin(cardString);
        if (oldPin.equals("")) {
            Log.d("PhoneInterfaceManager", "Failed getting old pin.");
            resultReceiver.send(3, new Bundle());
            return;
        }
        try {
            this.mCurrentlyModifyingSimAutoPinManagementState.set(true);
            if (changeIccLockPassword(i, pin, oldPin) != Integer.MAX_VALUE) {
                Log.d("PhoneInterfaceManager", "Failed changing pin for " + cardString);
                resultReceiver.send(4, new Bundle());
                return;
            }
            pinStorage.clearPlatformManagedPin(simSlotIndex);
            int iccLockEnabled = setIccLockEnabled(i, false, oldPin);
            if (iccLockEnabled != Integer.MAX_VALUE) {
                resultReceiver.send(5, putNumberOfRemainingPinAttemptsInBundle(iccLockEnabled));
            } else {
                resultReceiver.send(0, new Bundle());
            }
        } finally {
            this.mCurrentlyModifyingSimAutoPinManagementState.set(false);
        }
    }

    public void getAutoManagedPinForSim(int i, ResultReceiver resultReceiver) {
        TelephonyPermissions.enforceControlSimAutoPinManagementPermission(this.mApp, "Missing permission to read platform-managed PIN for SIM.");
        SubscriptionInfo subscriptionInfo = getSubscriptionManagerService().getSubscriptionInfo(i);
        if (subscriptionInfo == null) {
            resultReceiver.send(1, putManagedPinResultInBundle(""));
            return;
        }
        int simSlotIndex = subscriptionInfo.getSimSlotIndex();
        String cardString = subscriptionInfo.getCardString();
        PinStorage pinStorage = UiccController.getInstance().getPinStorage();
        if (!pinStorage.isPinPlatformManaged(cardString)) {
            resultReceiver.send(1, putManagedPinResultInBundle(""));
            return;
        }
        String pin = pinStorage.getPin(simSlotIndex, cardString);
        resultReceiver.send(pin.equals("") ? 2 : 0, putManagedPinResultInBundle(pin));
    }

    public boolean isSatelliteAttachSupported(int i) {
        enforceSatelliteCommunicationPermission("isSatelliteAttachSupported");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            HexString$$ExternalSyntheticBUOutline1.m("Invalid Subscription ID: ", i);
            return false;
        }
        try {
            return this.mSatelliteController.isSatelliteSupportedViaCarrier(i);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isSatelliteEntitlementSupported(int i) {
        enforceSatelliteCommunicationPermission("isSatelliteEntitlementSupported");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            HexString$$ExternalSyntheticBUOutline1.m("Invalid Subscription ID: ", i);
            return false;
        }
        try {
            return this.mSatelliteController.isSatelliteEntitlementSupported(i);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String getSatelliteEntitlementServerUrl(int i) {
        enforceSatelliteCommunicationPermission("getSatelliteEntitlementServerUrl");
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            HexString$$ExternalSyntheticBUOutline1.m("Invalid Subscription ID: ", i);
            return null;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.getSatelliteEntitlementServerUrl(i);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getSatelliteNtnConnectType(int i) {
        enforceSatelliteCommunicationPermission("getSatelliteNtnConnectType");
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            HexString$$ExternalSyntheticBUOutline1.m("Invalid Subscription ID: ", i);
            return 0;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSatelliteController.getCarrierRoamingNtnConnectType(i);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }
}
