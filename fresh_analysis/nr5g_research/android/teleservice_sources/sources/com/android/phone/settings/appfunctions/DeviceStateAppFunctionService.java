package com.android.phone.settings.appfunctions;

import ai.onnxruntime.OrtProvider$$ExternalSyntheticBUOutline0;
import android.app.KeyguardManager;
import android.app.appfunctions.AppFunctionException;
import android.app.appfunctions.AppFunctionService;
import android.app.appfunctions.ExecuteAppFunctionRequest;
import android.app.appfunctions.ExecuteAppFunctionResponse;
import android.app.appsearch.GenericDocument;
import android.content.Context;
import android.content.pm.SigningInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.CancellationSignal;
import android.os.OutcomeReceiver;
import android.os.Trace;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.module.ITelephonyModule;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateItem;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateItemMetadata;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateMetadataResponse;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateResponse;
import com.google.android.appfunctions.schema.common.v1.devicestate.ItemizationDetail;
import com.google.android.appfunctions.schema.common.v1.devicestate.ItemizationType;
import com.google.android.appfunctions.schema.common.v1.devicestate.LocalizedString;
import com.google.android.appfunctions.schema.common.v1.devicestate.PerScreenDeviceStates;
import com.google.android.appfunctions.schema.common.v1.devicestate.PerScreenMetadata;
import com.google.android.appfunctions.schema.common.v1.devicestate.SetDeviceStateItemParams;
import com.google.android.appfunctions.schema.common.v1.devicestate.SetDeviceStateItemResponse;
import com.ot.pubsub.a.a;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import miui.os.Build;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManagerEx;

/* JADX INFO: loaded from: classes.dex */
public class DeviceStateAppFunctionService extends AppFunctionService {
    private Context applicationContext;
    private final Executor mParallelExecutor = ForkJoinPool.commonPool();

    public void onCreate() {
        super.onCreate();
        if (this.applicationContext == null) {
            this.applicationContext = getApplicationContext();
        }
        Log.d("DeviceStateAppFunctionService", "Device State Service created");
    }

    public void onExecuteFunction(ExecuteAppFunctionRequest executeAppFunctionRequest, String str, SigningInfo signingInfo, CancellationSignal cancellationSignal, OutcomeReceiver outcomeReceiver) {
        GenericDocument platformGenericDocument;
        Log.d("DeviceStateAppFunctionService", "onExecuteFunction created");
        if (!Build.IS_INTERNATIONAL_BUILD) {
            Log.d("DeviceStateAppFunctionService", "DeviceStateAppFunction is only supported on international builds");
            outcomeReceiver.onError(new AppFunctionException(1000, "DeviceStateAppFunction is not supported on this device"));
            return;
        }
        DeviceStateAppFunctionType deviceStateAppFunctionTypeFromId = DeviceStateAppFunctionType.fromId(executeAppFunctionRequest.getFunctionIdentifier());
        if (deviceStateAppFunctionTypeFromId == null) {
            outcomeReceiver.onError(new AppFunctionException(1003, executeAppFunctionRequest.getFunctionIdentifier() + " not supported."));
            return;
        }
        if (shouldCheckForDeviceLock(executeAppFunctionRequest.getParameters(), deviceStateAppFunctionTypeFromId) && isDeviceLocked(this.applicationContext)) {
            outcomeReceiver.onError(new AppFunctionException(1000, "Attempting to execute a device state app function while the device is locked"));
            return;
        }
        Trace.beginSection("DeviceStateAppFunction:" + executeAppFunctionRequest.getFunctionIdentifier());
        Log.d("DeviceStateAppFunctionService", "device state app function " + executeAppFunctionRequest.getFunctionIdentifier() + " called.");
        try {
            if (deviceStateAppFunctionTypeFromId == DeviceStateAppFunctionType.GET_UNCATEGORIZED) {
                platformGenericDocument = GenericDocumentToPlatformConverter.toPlatformGenericDocument(androidx.appsearch.app.GenericDocument.fromDocumentClass(buildResponseFromCatalyst(deviceStateAppFunctionTypeFromId)));
            } else if (deviceStateAppFunctionTypeFromId == DeviceStateAppFunctionType.SET_DEVICE_STATE) {
                platformGenericDocument = GenericDocumentToPlatformConverter.toPlatformGenericDocument(androidx.appsearch.app.GenericDocument.fromDocumentClass(buildResponseSetter(deviceStateAppFunctionTypeFromId, executeAppFunctionRequest.getParameters())));
            } else {
                platformGenericDocument = deviceStateAppFunctionTypeFromId == DeviceStateAppFunctionType.GET_METADATA ? GenericDocumentToPlatformConverter.toPlatformGenericDocument(androidx.appsearch.app.GenericDocument.fromDocumentClass(buildResponseMetadata(deviceStateAppFunctionTypeFromId))) : null;
            }
            outcomeReceiver.onResult(new ExecuteAppFunctionResponse(new GenericDocument.Builder("", "", "").setPropertyDocument("androidAppfunctionsReturnValue", platformGenericDocument).build()));
            Log.d("DeviceStateAppFunctionService", "app function " + executeAppFunctionRequest.getFunctionIdentifier() + " fulfilled.");
            Trace.endSection();
        } catch (Exception e) {
            Log.e("DeviceStateAppFunctionService", "Failed to convert DeviceStateResponse to Jetpack GenericDocument", e);
            throw new RuntimeException("AppSearch conversion failed: " + e.getMessage(), e);
        }
    }

    public DeviceStateResponse buildResponseFromCatalyst(final DeviceStateAppFunctionType deviceStateAppFunctionType) {
        List<PerScreenConfig> list = (List) DeviceStateConfigProvider.getScreenConfigs().stream().filter(new Predicate() { // from class: com.android.phone.settings.appfunctions.DeviceStateAppFunctionService$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DeviceStateAppFunctionService.$r8$lambda$FLEo_WYCAJZrLJb9CtnLQapIeKo(deviceStateAppFunctionType, (PerScreenConfig) obj);
            }
        }).collect(Collectors.toList());
        ArrayList arrayList = new ArrayList();
        for (final PerScreenConfig perScreenConfig : list) {
            arrayList.add(CompletableFuture.supplyAsync(new Supplier() { // from class: com.android.phone.settings.appfunctions.DeviceStateAppFunctionService$$ExternalSyntheticLambda1
                @Override // java.util.function.Supplier
                public final Object get() {
                    return this.f$0.lambda$buildResponseFromCatalyst$1(perScreenConfig, deviceStateAppFunctionType);
                }
            }, this.mParallelExecutor).exceptionally(new Function() { // from class: com.android.phone.settings.appfunctions.DeviceStateAppFunctionService$$ExternalSyntheticLambda2
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return DeviceStateAppFunctionService.$r8$lambda$w3F2XTrJO14YyJuRMXKB29__ThY(perScreenConfig, (Throwable) obj);
                }
            }));
        }
        CompletableFuture.allOf((CompletableFuture[]) arrayList.toArray(new CompletableFuture[0])).join();
        return new DeviceStateResponse("", "", (List) arrayList.stream().map(new Function() { // from class: com.android.phone.settings.appfunctions.DeviceStateAppFunctionService$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return DeviceStateAppFunctionService.$r8$lambda$LEP5Qyt7ER4T77KaS2hhipf3LRQ((CompletableFuture) obj);
            }
        }).filter(new Predicate() { // from class: com.android.phone.settings.appfunctions.DeviceStateAppFunctionService$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return Objects.nonNull((PerScreenDeviceStates) obj);
            }
        }).collect(Collectors.toList()), getDeviceLocale(), "Device state fetched successfully");
    }

    public static /* synthetic */ boolean $r8$lambda$FLEo_WYCAJZrLJb9CtnLQapIeKo(DeviceStateAppFunctionType deviceStateAppFunctionType, PerScreenConfig perScreenConfig) {
        return perScreenConfig.isEnabled() && perScreenConfig.getCategory().contains(deviceStateAppFunctionType);
    }

    public static /* synthetic */ PerScreenDeviceStates $r8$lambda$w3F2XTrJO14YyJuRMXKB29__ThY(PerScreenConfig perScreenConfig, Throwable th) {
        Log.e("DeviceStateAppFunctionService", "Failed to build state for screen: " + perScreenConfig.getScreenKey(), th);
        return null;
    }

    public static /* synthetic */ PerScreenDeviceStates $r8$lambda$LEP5Qyt7ER4T77KaS2hhipf3LRQ(CompletableFuture completableFuture) {
        try {
            return (PerScreenDeviceStates) completableFuture.get();
        } catch (Exception unused) {
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public DeviceStateMetadataResponse buildResponseMetadata(DeviceStateAppFunctionType deviceStateAppFunctionType) {
        String str;
        String str2;
        String str3;
        int i;
        String str4;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        List<DeviceStateItemConfig> deviceStateItemList = DeviceStateConfigProvider.getDeviceStateItemList();
        HashMap map = new HashMap();
        for (DeviceStateItemConfig deviceStateItemConfig : deviceStateItemList) {
            map.put(deviceStateItemConfig.getSettingKey(), deviceStateItemConfig);
        }
        DeviceStateItemConfig deviceStateItemConfig2 = (DeviceStateItemConfig) map.get("sim_card_name");
        String englishString = getEnglishString(this.applicationContext, deviceStateItemConfig2.getEnglishName());
        String localizedString = getLocalizedString(this.applicationContext, deviceStateItemConfig2.getLocalizedName());
        DeviceStateItemConfig deviceStateItemConfig3 = (DeviceStateItemConfig) map.get("default_for_calls");
        String englishString2 = getEnglishString(this.applicationContext, deviceStateItemConfig3.getEnglishName());
        String localizedString2 = getLocalizedString(this.applicationContext, deviceStateItemConfig3.getLocalizedName());
        DeviceStateItemConfig deviceStateItemConfig4 = (DeviceStateItemConfig) map.get("default_data");
        String englishString3 = getEnglishString(this.applicationContext, deviceStateItemConfig4.getEnglishName());
        String localizedString3 = getLocalizedString(this.applicationContext, deviceStateItemConfig4.getLocalizedName());
        ArrayList arrayList3 = new ArrayList();
        for (int i2 = 0; i2 < 2; i2++) {
            SubscriptionInfo availableSubscriptionInfoBySlot = MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(i2);
            if (availableSubscriptionInfoBySlot != null) {
                arrayList3.add(new ItemizationDetail("", "", String.valueOf(i2), availableSubscriptionInfoBySlot.getDisplayName() == null ? "SIM " + (i2 + 1) : availableSubscriptionInfoBySlot.getDisplayName().toString()));
            }
        }
        if (!arrayList3.isEmpty()) {
            str2 = "";
            str3 = ", ";
            str = "SIM ";
            i = 0;
            arrayList2.add(new ItemizationType("", "", "sim", arrayList3, ""));
            ArrayList arrayList4 = new ArrayList();
            arrayList4.add(new DeviceStateItemMetadata("", "", "sim_card_name", "Custom name for a SIM card", new LocalizedString(str2, str2, englishString, localizedString), null, Boolean.TRUE, "{\"nonPredeterminedFinite\": {\"valueType\": \"STRING\"}}", "Max 20 characters. Use itemizationKeys: 0 for SIM 1, 1 for SIM 2.", ""));
            StringBuilder sb = new StringBuilder("SIM card settings. Available SIM cards: ");
            int i3 = 0;
            while (i3 < arrayList3.size()) {
                if (i3 > 0) {
                    sb.append(str3);
                }
                StringBuilder sb2 = new StringBuilder(str);
                i3++;
                sb2.append(i3);
                sb.append(sb2.toString());
            }
            sb.append(". You can view and edit SIM card name, check SIM status. Use itemizationKeys to select which SIM card to manage. To open SIM 1 page, set slot_id to 0. To open SIM 2 page, set slot_id to 1.");
            arrayList.add(new PerScreenMetadata("", "", sb.toString(), "intent:#Intent;launchFlags=0x10000000;component=com.android.phone/com.android.phone.settings.MultiSimInfoEditorActivity;i.slot_id=[slot_id];end", Collections.singletonList("sim"), arrayList4));
        } else {
            str = "SIM ";
            str2 = "";
            str3 = ", ";
            i = 0;
        }
        int i4 = i;
        int i5 = i4;
        while (i4 < 2) {
            if (MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(i4) != null) {
                i5++;
            }
            i4++;
        }
        int i6 = 1;
        boolean z = i5 >= 2 ? 1 : i;
        String str5 = "No SIM card or single SIM, set not supported.";
        if (z == 0) {
            str4 = "No SIM card or single SIM, set not supported.";
        } else {
            str4 = "Current value indicates the active default SIM.";
        }
        if (z != 0) {
            str5 = "Select default sim for mobile data.";
        }
        String str6 = str5;
        ArrayList arrayList5 = new ArrayList();
        arrayList5.add(new DeviceStateItemMetadata("", "", "default_for_calls", "Default for calls", new LocalizedString(str2, str2, englishString2, localizedString2), null, Boolean.valueOf(z), "{\"predeterminedFinite\": {\"valueType\": \"STRING\", \"values\": [\"sim1\", \"sim2\", \"not_set\"]}}", str4, ""));
        arrayList5.add(new DeviceStateItemMetadata("", "", "default_data", "Data SIM", new LocalizedString(str2, str2, englishString3, localizedString3), null, Boolean.valueOf(z), "{\"predeterminedFinite\": {\"valueType\": \"STRING\", \"values\": [\"sim1\", \"sim2\"]}}", str6, ""));
        StringBuilder sb3 = new StringBuilder("Device status settings, including default dialing card, data card, mobile data");
        ITelephonyModule iTelephonyModule = (ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class);
        if ((iTelephonyModule != null ? iTelephonyModule.getEsimSecureValue() : -1) >= 0) {
            sb3.append(", eSIM status");
        }
        sb3.append(".");
        String string = sb3.toString();
        String strBuildScreenIntentUri = buildScreenIntentUri("mobile_network_screen");
        List list = Collections.EMPTY_LIST;
        arrayList.add(new PerScreenMetadata("", "", string, strBuildScreenIntentUri, list, arrayList5));
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        if (defaultDataSlotId >= 0 && MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(defaultDataSlotId) != null) {
            arrayList.add(new PerScreenMetadata("", "", "Access Point Names (APN) settings for the default data SIM card (SIM " + (defaultDataSlotId + 1) + ").", "intent:#Intent;action=android.settings.APN_SETTINGS;launchFlags=0x10000000;package=com.android.settings;i.slot_id=" + defaultDataSlotId + ";end", list, list));
        }
        if (MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(i) != null || MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(1) != null) {
            StringBuilder sb4 = new StringBuilder("Preferred Network Type settings. Available SIM cards: ");
            int i7 = 1;
            for (int i8 = i; i8 < 2; i8++) {
                if (MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(i8) != null) {
                    if (i7 == 0) {
                        sb4.append(str3);
                    }
                    sb4.append(str);
                    sb4.append(i8 + 1);
                    i7 = i;
                }
            }
            sb4.append(". To open SIM 1 Preferred Network Type page, set slot_id to 0. To open SIM 2 Preferred Network Type page, set slot_id to 1.");
            String string2 = sb4.toString();
            List list2 = Collections.EMPTY_LIST;
            arrayList.add(new PerScreenMetadata("", "", string2, "intent:#Intent;launchFlags=0x10000000;component=com.android.phone/com.android.phone.settings.PreferredNetworkTypeListPreference;i.slot_id=[slot_id];end", list2, list2));
        }
        if (MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(i) != null || MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(1) != null) {
            StringBuilder sb5 = new StringBuilder("Network carrier selection settings. Available SIM cards: ");
            for (int i9 = i; i9 < 2; i9++) {
                if (MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(i9) != null) {
                    if (i6 == 0) {
                        sb5.append(str3);
                    }
                    sb5.append(str);
                    sb5.append(i9 + 1);
                    i6 = i;
                }
            }
            sb5.append(". To open SIM 1 carrier selection page, set slot_id to 0. To open SIM 2 carrier selection page, set slot_id to 1.");
            String string3 = sb5.toString();
            List list3 = Collections.EMPTY_LIST;
            arrayList.add(new PerScreenMetadata("", "", string3, "intent:#Intent;launchFlags=0x10000000;component=com.android.phone/com.android.phone.settings.NetworkSetting;i.slot_id=[slot_id];end", list3, list3));
        }
        return new DeviceStateMetadataResponse("", "", arrayList, getDeviceLocale(), "This metadata describes the settings available on this device. Use the 'key' field of each item to identify a setting for modification.", arrayList2);
    }

    public SetDeviceStateItemResponse buildResponseSetter(DeviceStateAppFunctionType deviceStateAppFunctionType, GenericDocument genericDocument) {
        try {
            if (DeviceStateAppFunctionType.SET_DEVICE_STATE.equals(deviceStateAppFunctionType)) {
                return setDeviceState(parseSetDeviceStateItemParams(genericDocument));
            }
            Log.i("DeviceStateAppFunctionService", "Unrecognised appFunctionType: " + deviceStateAppFunctionType);
            return null;
        } catch (Exception e) {
            Log.e("DeviceStateAppFunctionService", "Failed to build set response", e);
            return new SetDeviceStateItemResponse("", "", false, "", "Error: " + e.getMessage());
        }
    }

    private SetDeviceStateItemParams parseSetDeviceStateItemParams(GenericDocument genericDocument) {
        String strValueOf = null;
        GenericDocument propertyDocument = genericDocument != null ? genericDocument.getPropertyDocument("setDeviceStateItemParams") : null;
        if (propertyDocument == null) {
            OrtProvider$$ExternalSyntheticBUOutline0.m("Missing setDeviceStateItemParams in the request: ", genericDocument);
            return null;
        }
        String propertyString = propertyDocument.getPropertyString("key");
        String str = propertyString != null ? propertyString : "";
        String propertyString2 = propertyDocument.getPropertyString(a.p);
        if (propertyString2 == null) {
            propertyString2 = "";
        }
        if (propertyString2.length() > 20) {
            propertyString2 = "Name is too long";
        }
        Object property = propertyDocument.getProperty(a.p);
        if (property != null) {
            Log.i("DeviceStateAppFunctionService", "parseSetDeviceStateItemParams valueObj simple type: ".concat(property.getClass().getSimpleName()));
            if (property instanceof boolean[]) {
                boolean[] zArr = (boolean[]) property;
                if (zArr.length > 0) {
                    strValueOf = zArr[0] ? "true" : "false";
                }
                Log.i("DeviceStateAppFunctionService", "parseSetDeviceStateItemParams boolean value2:" + strValueOf);
            }
            if (property instanceof long[]) {
                long[] jArr = (long[]) property;
                if (jArr.length > 0) {
                    strValueOf = String.valueOf(jArr[0]);
                }
            } else if (property instanceof double[]) {
                double[] dArr = (double[]) property;
                if (dArr.length > 0) {
                    strValueOf = String.valueOf(dArr[0]);
                    Log.i("DeviceStateAppFunctionService", "parseSetDeviceStateItemParams value2:" + strValueOf + ", value2 length:" + strValueOf.length());
                    if (strValueOf.length() > 20) {
                        strValueOf = "Name is too long";
                    }
                }
            }
        }
        if (strValueOf == null) {
            strValueOf = "";
        }
        Log.i("DeviceStateAppFunctionService", "parseSetDeviceStateItemParams value:" + propertyString2 + ", value2:" + strValueOf);
        if ("".equals(propertyString2)) {
            if ("".equals(strValueOf)) {
                strValueOf = "null";
            }
            propertyString2 = strValueOf;
        }
        Object property2 = propertyDocument.getProperty("itemizationKeys");
        ArrayList arrayList = new ArrayList();
        if (property2 instanceof String[]) {
            for (String str2 : (String[]) property2) {
                arrayList.add(str2);
            }
        }
        Log.i("DeviceStateAppFunctionService", "parseSetDeviceStateItemParams itemizationKeys: " + arrayList);
        return new SetDeviceStateItemParams("", "", str, arrayList, propertyString2, Boolean.TRUE);
    }

    private SetDeviceStateItemResponse setDeviceState(SetDeviceStateItemParams setDeviceStateItemParams) {
        String key = setDeviceStateItemParams.getKey();
        String value = setDeviceStateItemParams.getValue();
        OperationResult operationResult = new OperationResult("Unknown state", "");
        key.hashCode();
        byte b = 2;
        boolean zEquals = false;
        switch (key) {
            case "default_for_calls":
                operationResult = setDefaultForCalls(value);
                break;
            case "default_data":
                operationResult = setDefaultData(value);
                break;
            case "sim_card_name":
                List itemizationKeys = setDeviceStateItemParams.getItemizationKeys();
                if (itemizationKeys != null && !itemizationKeys.isEmpty()) {
                    try {
                        operationResult = setSimName(value, Integer.parseInt((String) itemizationKeys.get(0)));
                    } catch (NumberFormatException unused) {
                        operationResult = new OperationResult("Failure: invalid slot in itemizationKeys: " + ((String) itemizationKeys.get(0)), "");
                    }
                    break;
                } else {
                    operationResult = new OperationResult("Failure: itemizationKeys required, use '0' for SIM 1 or '1' for SIM 2", "");
                    break;
                }
                break;
        }
        String resultDesc = operationResult.getResultDesc();
        String actualValue = operationResult.getActualValue();
        key.hashCode();
        switch (key.hashCode()) {
            case -1757611423:
                b = !key.equals("default_for_calls") ? (byte) -1 : (byte) 0;
                break;
            case -650734872:
                b = !key.equals("default_data") ? (byte) -1 : (byte) 1;
                break;
            case 1253832946:
                if (!key.equals("sim_card_name")) {
                    b = -1;
                }
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
            case 1:
                zEquals = resultDesc.equals(value);
                break;
            case 2:
                zEquals = resultDesc.equals(value);
                break;
        }
        boolean z = zEquals;
        if (z) {
            resultDesc = null;
        }
        return new SetDeviceStateItemResponse("", "", z, actualValue, resultDesc);
    }

    private OperationResult setSimName(String str, int i) {
        SubscriptionInfo availableSubscriptionInfoBySlot = MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(i);
        if (availableSubscriptionInfoBySlot == null) {
            return new OperationResult("Failure: SIM slot " + i + " not found", "");
        }
        String string = availableSubscriptionInfoBySlot.getDisplayName().toString();
        if ("Name is too long".equals(str)) {
            return new OperationResult("Failure: Name length exceeds 20 characters", string);
        }
        if (str.equals(string)) {
            return new OperationResult("Same as original name", string);
        }
        if (SubscriptionManagerEx.getDefault().setDisplayNameForSubscription(str, availableSubscriptionInfoBySlot.getSubscriptionId()) > 0) {
            return new OperationResult(str, str);
        }
        return new OperationResult("Modification failed", string);
    }

    private OperationResult setDefaultForCalls(String str) {
        String defaultVoiceSlotId = getDefaultVoiceSlotId();
        if (!"sim1".equals(str) && !"sim2".equals(str) && !"not_set".equals(str)) {
            return new OperationResult("Failure: Invalid parameter (only 'sim1', 'sim2', or 'not_set')", defaultVoiceSlotId);
        }
        DefaultSimManager defaultSimManager = DefaultSimManager.getInstance();
        if (defaultSimManager == null) {
            return new OperationResult("Failure: DefaultSimManager not available", defaultVoiceSlotId);
        }
        try {
            if ("not_set".equals(str)) {
                defaultSimManager.setDefaultVoiceSlotId(-1, "com.android.phone.settings.appfunctions");
            } else {
                int i = !"sim1".equals(str) ? 1 : 0;
                if (MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(i) == null) {
                    return new OperationResult("Failure: SIM " + (i + 1) + " not available", defaultVoiceSlotId);
                }
                defaultSimManager.setDefaultVoiceSlotId(i, "com.android.phone.settings.appfunctions");
            }
            return new OperationResult(str, str);
        } catch (Exception e) {
            Log.e("DeviceStateAppFunctionService", "Failed to set the default call card", e);
            return new OperationResult("Failure: " + e.getMessage(), defaultVoiceSlotId);
        }
    }

    private OperationResult setDefaultData(String str) {
        String defaultDataSlotId = getDefaultDataSlotId();
        if (!"sim1".equals(str) && !"sim2".equals(str)) {
            return new OperationResult("Failure: Invalid parameter (only 'sim1' or 'sim2')", defaultDataSlotId);
        }
        int i = !"sim1".equals(str) ? 1 : 0;
        if (MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(i) == null) {
            return new OperationResult("Failure: SIM " + (i + 1) + " not available", defaultDataSlotId);
        }
        DefaultSimManager defaultSimManager = DefaultSimManager.getInstance();
        if (defaultSimManager == null) {
            return new OperationResult("Failure: DefaultSimManager not available", defaultDataSlotId);
        }
        try {
            defaultSimManager.setDefaultDataSlotId(i, "com.android.phone.settings.appfunctions");
            return new OperationResult(str, str);
        } catch (Exception e) {
            Log.e("DeviceStateAppFunctionService", "Failed to set the default data card", e);
            return new OperationResult("Failure: " + e.getMessage(), defaultDataSlotId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: buildSingleScreenState, reason: merged with bridge method [inline-methods] */
    public PerScreenDeviceStates lambda$buildResponseFromCatalyst$1(final PerScreenConfig perScreenConfig, DeviceStateAppFunctionType deviceStateAppFunctionType) {
        List<DeviceStateItemConfig> list = (List) DeviceStateConfigProvider.getDeviceStateItemList().stream().filter(new Predicate() { // from class: com.android.phone.settings.appfunctions.DeviceStateAppFunctionService$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DeviceStateAppFunctionService.m3505$r8$lambda$xu4bIafBZqZy5ujkQjy5W1Z9tg(perScreenConfig, (DeviceStateItemConfig) obj);
            }
        }).collect(Collectors.toList());
        ArrayList arrayList = new ArrayList();
        for (DeviceStateItemConfig deviceStateItemConfig : list) {
            String settingKey = deviceStateItemConfig.getSettingKey();
            arrayList.add(new DeviceStateItem("", "", settingKey, settingKey, new LocalizedString("", "", getEnglishString(this.applicationContext, deviceStateItemConfig.getEnglishName()), getLocalizedString(this.applicationContext, deviceStateItemConfig.getLocalizedName())), getValueBySettingKey(settingKey), 0L, deviceStateItemConfig.getHintText()));
        }
        return new PerScreenDeviceStates("", "", perScreenConfig.getDescription(), buildScreenIntentUri(perScreenConfig.getScreenKey()), arrayList);
    }

    /* JADX INFO: renamed from: $r8$lambda$xu4bIa-fBZqZy5ujkQjy5W1Z9tg, reason: not valid java name */
    public static /* synthetic */ boolean m3505$r8$lambda$xu4bIafBZqZy5ujkQjy5W1Z9tg(PerScreenConfig perScreenConfig, DeviceStateItemConfig deviceStateItemConfig) {
        return deviceStateItemConfig.isEnabled() && deviceStateItemConfig.getSettingScreenKey().equals(perScreenConfig.getScreenKey());
    }

    private String getValueBySettingKey(String str) {
        str.hashCode();
        switch (str) {
            case "default_for_calls":
                return getDefaultVoiceSlotId();
            case "mobile_data":
                return getMobileDataEnabled();
            case "automatically_select_network":
                return "SIM1:" + (MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(0) != null ? getAutomaticallySelectNetwork(0) : "N/A") + ", SIM2:" + (MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(1) != null ? getAutomaticallySelectNetwork(1) : "N/A");
            case "default_data":
                return getDefaultDataSlotId();
            case "turn_on_esim":
                return getEsimEnabled();
            case "sim_card_name":
                return getSimCardNameValue();
            default:
                return "Unknown state";
        }
    }

    private String buildScreenIntentUri(String str) {
        if ("mobile_network_screen".equals(str)) {
            return "intent:#Intent;component=com.android.phone/com.android.phone.settings.MobileNetworkSettings;end";
        }
        if ("access_point_names".equals(str)) {
            return "intent:#Intent;action=android.settings.APN_SETTINGS;launchFlags=0x10000000;package=com.android.settings;i.slot_id=[slot_id];end";
        }
        if ("preferred_network_type".equals(str)) {
            return "intent:#Intent;launchFlags=0x10000000;component=com.android.phone/com.android.phone.settings.PreferredNetworkTypeListPreference;i.slot_id=[slot_id];end";
        }
        if ("select_carrier".equals(str)) {
            return "intent:#Intent;launchFlags=0x10000000;component=com.android.phone/com.android.phone.settings.NetworkSetting;i.slot_id=[slot_id];end";
        }
        if ("sim_card_screen".equals(str)) {
            return "intent:#Intent;launchFlags=0x10000000;component=com.android.phone/com.android.phone.settings.MultiSimInfoEditorActivity;i.slot_id=[slot_id];end";
        }
        return "";
    }

    private String getSimCardNameValue() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            SubscriptionInfo availableSubscriptionInfoBySlot = MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(i);
            if (availableSubscriptionInfoBySlot != null) {
                String string = availableSubscriptionInfoBySlot.getDisplayName() != null ? availableSubscriptionInfoBySlot.getDisplayName().toString() : "SIM " + (i + 1);
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append("SIM Card ");
                sb.append(i + 1);
                sb.append(": ");
                sb.append(string);
            }
        }
        return sb.length() > 0 ? sb.toString() : "No SIM card";
    }

    private String getAutomaticallySelectNetwork(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone == null) {
            return "No corresponding Phone service found for slot" + (i + 1);
        }
        ServiceState serviceState = phone.getServiceState();
        if (serviceState == null) {
            return "false";
        }
        return String.valueOf(!serviceState.getIsManualSelection());
    }

    private String getDefaultDataSlotId() {
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        if (defaultDataSlotId == 0 && MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(0) == null) {
            return "not_set";
        }
        if (defaultDataSlotId == 1 && MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(1) == null) {
            return "not_set";
        }
        if (defaultDataSlotId == 0) {
            return "sim1";
        }
        if (defaultDataSlotId != 1) {
            return "not_set";
        }
        return "sim2";
    }

    private String getDefaultVoiceSlotId() {
        int defaultVoiceSlotId = DefaultSimManager.getDefaultVoiceSlotId();
        if (defaultVoiceSlotId == 0) {
            return "sim1";
        }
        if (defaultVoiceSlotId == 1) {
            return "sim2";
        }
        return "not_set";
    }

    /* JADX WARN: Multi-variable type inference failed */
    private String getMobileDataEnabled() {
        boolean zIsDataEnabled;
        TelephonyManager telephonyManager = getTelephonyManager(this);
        if (telephonyManager != null) {
            zIsDataEnabled = telephonyManager.isDataEnabled();
        } else {
            Log.d("DeviceStateAppFunctionService", "TelephonyManager is null");
            zIsDataEnabled = false;
        }
        return String.valueOf(zIsDataEnabled);
    }

    private static String getEsimEnabled() {
        ITelephonyModule iTelephonyModule = (ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class);
        if (iTelephonyModule == null || iTelephonyModule.getEsimSecureValue() != 1) {
            return "false";
        }
        return "true";
    }

    private static TelephonyManager getTelephonyManager(Context context) {
        if (context != null) {
            return (TelephonyManager) context.getSystemService(TelephonyManager.class);
        }
        return null;
    }

    private String getDeviceLocale() {
        Locale locale = getResources().getConfiguration().getLocales().get(0);
        return locale.getLanguage() + "_" + locale.getCountry();
    }

    private boolean shouldCheckForDeviceLock(GenericDocument genericDocument, DeviceStateAppFunctionType deviceStateAppFunctionType) {
        GenericDocument propertyDocument = genericDocument.getPropertyDocument(deviceStateAppFunctionType.getFunctionId() + "Params");
        if (propertyDocument == null) {
            return true;
        }
        return !Boolean.TRUE.equals(Boolean.valueOf(propertyDocument.getPropertyBoolean("requestInitiatedWhileUnlocked")));
    }

    private boolean isDeviceLocked(Context context) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService("keyguard");
        return keyguardManager != null && keyguardManager.isDeviceLocked();
    }

    private String getEnglishString(Context context, int i) {
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(Locale.ENGLISH);
        try {
            return context.createConfigurationContext(configuration).getResources().getString(i);
        } catch (Resources.NotFoundException unused) {
            return context.getString(i);
        }
    }

    private String getLocalizedString(Context context, int i) {
        return context.getString(i);
    }
}
