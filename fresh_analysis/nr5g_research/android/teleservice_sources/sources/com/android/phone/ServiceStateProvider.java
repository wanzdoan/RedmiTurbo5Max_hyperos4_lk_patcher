package com.android.phone;

import ai.onnxruntime.OrtProvider$$ExternalSyntheticBUOutline0;
import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline4;
import android.app.compat.CompatChanges;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Parcel;
import android.provider.Telephony;
import android.telephony.LocationAccessPolicy;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.util.Log;
import com.android.internal.telephony.TelephonyPermissions;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class ServiceStateProvider extends ContentProvider {
    static final long ENFORCE_LOCATION_PERMISSION_CHECK = 191911306;
    private final HashMap mServiceStates = new HashMap();
    public static final Uri AUTHORITY_URI = Uri.parse("content://service-state");
    static final String[] ALL_COLUMNS = {"voice_reg_state", "data_reg_state", "voice_roaming_type", "data_roaming_type", "voice_operator_alpha_long", "voice_operator_alpha_short", "voice_operator_numeric", "data_operator_alpha_long", "data_operator_alpha_short", "data_operator_numeric", "is_manual_network_selection", "ril_voice_radio_technology", "ril_data_radio_technology", "css_indicator", "network_id", "system_id", "cdma_roaming_indicator", "cdma_default_roaming_indicator", "cdma_eri_icon_index", "cdma_eri_icon_mode", "is_emergency_only", "is_using_carrier_aggregation", "operator_alpha_long_raw", "operator_alpha_short_raw", "data_network_type", "duplex_mode"};
    static final String[] PUBLIC_COLUMNS = {"voice_reg_state", "data_reg_state", "voice_operator_numeric", "is_manual_network_selection", "data_network_type", "duplex_mode"};
    private static final Set LOCATION_PROTECTED_COLUMNS_SET = Set.of("network_id", "system_id");

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        return true;
    }

    public ServiceState getServiceState(int i) {
        return (ServiceState) this.mServiceStates.get(Integer.valueOf(i));
    }

    public int getDefaultSubId() {
        return SubscriptionManager.getDefaultSubscriptionId();
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        if (!isPathPrefixMatch(uri, Telephony.ServiceStateTable.CONTENT_URI)) {
            return null;
        }
        try {
            int defaultSubId = Integer.parseInt(uri.getLastPathSegment());
            Log.d("ServiceStateProvider", "subId=" + defaultSubId);
            if (defaultSubId == Integer.MAX_VALUE) {
                defaultSubId = getDefaultSubId();
            }
            Parcel parcelObtain = Parcel.obtain();
            byte[] asByteArray = contentValues.getAsByteArray("service_state");
            parcelObtain.unmarshall(asByteArray, 0, asByteArray.length);
            parcelObtain.setDataPosition(0);
            ServiceState serviceState = (ServiceState) ServiceState.CREATOR.createFromParcel(parcelObtain);
            ServiceState serviceState2 = getServiceState(defaultSubId);
            notifyChangeForSubIdAndField(getContext(), serviceState2, serviceState, defaultSubId);
            notifyChangeForSubId(getContext(), serviceState2, serviceState, defaultSubId);
            this.mServiceStates.put(Integer.valueOf(defaultSubId), serviceState);
            return uri;
        } catch (NumberFormatException e) {
            Log.e("ServiceStateProvider", "insert: no subId provided in uri");
            throw e;
        }
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        throw new RuntimeException("Not supported");
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        throw new RuntimeException("Not supported");
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        throw new RuntimeException("Not supported");
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        int defaultSubId;
        String[] strArr3;
        boolean z;
        Object[] objArr;
        if (!isPathPrefixMatch(uri, Telephony.ServiceStateTable.CONTENT_URI)) {
            OrtProvider$$ExternalSyntheticBUOutline0.m("Invalid URI: ", uri);
            return null;
        }
        try {
            defaultSubId = Integer.parseInt(uri.getLastPathSegment());
        } catch (NumberFormatException unused) {
            Log.d("ServiceStateProvider", "query: no subId provided in uri, using default.");
            defaultSubId = getDefaultSubId();
        }
        Log.d("ServiceStateProvider", "subId=" + defaultSubId);
        if (defaultSubId == Integer.MAX_VALUE) {
            defaultSubId = getDefaultSubId();
        }
        ServiceState serviceState = getServiceState(defaultSubId);
        if (serviceState == null) {
            Log.d("ServiceStateProvider", "returning null");
            return null;
        }
        boolean zIsChangeEnabled = CompatChanges.isChangeEnabled(ENFORCE_LOCATION_PERMISSION_CHECK);
        boolean z2 = TelephonyPermissions.getTargetSdk(getContext(), getCallingPackage()) >= 31;
        boolean z3 = getContext().checkCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE") == 0;
        if (zIsChangeEnabled && z2 && !z3) {
            strArr3 = PUBLIC_COLUMNS;
        } else {
            String[] strArr4 = ALL_COLUMNS;
            if (zIsChangeEnabled) {
                boolean z4 = strArr == null;
                if (strArr == null) {
                    z = false;
                    break;
                }
                int length = strArr.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        z = false;
                        break;
                    }
                    if (LOCATION_PROTECTED_COLUMNS_SET.contains(strArr[i])) {
                        z = true;
                        break;
                    }
                    i++;
                }
                if (z4 || z) {
                    if (!hasLocationPermission()) {
                        if (z2) {
                            ImsRcsController$$ExternalSyntheticBUOutline0.m("Querying location sensitive info requires location permissions");
                            return null;
                        }
                        serviceState = getLocationRedactedServiceState(serviceState);
                    }
                } else {
                    serviceState = getLocationRedactedServiceState(serviceState);
                }
            }
            strArr3 = strArr4;
        }
        int state = serviceState.getState();
        int dataRegistrationState = serviceState.getDataRegistrationState();
        int voiceRoamingType = serviceState.getVoiceRoamingType();
        int dataRoamingType = serviceState.getDataRoamingType();
        String operatorAlphaLong = serviceState.getOperatorAlphaLong();
        String operatorAlphaShort = serviceState.getOperatorAlphaShort();
        String operatorNumeric = serviceState.getOperatorNumeric();
        String operatorAlphaLong2 = serviceState.getOperatorAlphaLong();
        String operatorAlphaShort2 = serviceState.getOperatorAlphaShort();
        String operatorNumeric2 = serviceState.getOperatorNumeric();
        boolean isManualSelection = serviceState.getIsManualSelection();
        int rilVoiceRadioTechnology = serviceState.getRilVoiceRadioTechnology();
        int rilDataRadioTechnology = serviceState.getRilDataRadioTechnology();
        int cssIndicator = serviceState.getCssIndicator();
        int cdmaNetworkId = serviceState.getCdmaNetworkId();
        int cdmaSystemId = serviceState.getCdmaSystemId();
        boolean zIsEmergencyOnly = serviceState.isEmergencyOnly();
        boolean zIsUsingCarrierAggregation = serviceState.isUsingCarrierAggregation();
        String operatorAlphaLongRaw = serviceState.getOperatorAlphaLongRaw();
        String operatorAlphaShortRaw = serviceState.getOperatorAlphaShortRaw();
        int dataNetworkType = serviceState.getDataNetworkType();
        int duplexMode = serviceState.getDuplexMode();
        if (strArr3 == ALL_COLUMNS) {
            objArr = new Object[]{Integer.valueOf(state), Integer.valueOf(dataRegistrationState), Integer.valueOf(voiceRoamingType), Integer.valueOf(dataRoamingType), operatorAlphaLong, operatorAlphaShort, operatorNumeric, operatorAlphaLong2, operatorAlphaShort2, operatorNumeric2, Integer.valueOf(isManualSelection ? 1 : 0), Integer.valueOf(rilVoiceRadioTechnology), Integer.valueOf(rilDataRadioTechnology), Integer.valueOf(cssIndicator), Integer.valueOf(cdmaNetworkId), Integer.valueOf(cdmaSystemId), -1, -1, -1, -1, Integer.valueOf(zIsEmergencyOnly ? 1 : 0), Integer.valueOf(zIsUsingCarrierAggregation ? 1 : 0), operatorAlphaLongRaw, operatorAlphaShortRaw, Integer.valueOf(dataNetworkType), Integer.valueOf(duplexMode)};
        } else {
            objArr = new Object[]{Integer.valueOf(state), Integer.valueOf(dataRegistrationState), operatorNumeric, Integer.valueOf(isManualSelection ? 1 : 0), Integer.valueOf(dataNetworkType), Integer.valueOf(duplexMode)};
        }
        return buildSingleRowResult(strArr, strArr3, objArr);
    }

    private static Cursor buildSingleRowResult(String[] strArr, String[] strArr2, Object[] objArr) {
        if (strArr == null) {
            strArr = strArr2;
        }
        MatrixCursor matrixCursor = new MatrixCursor(strArr, 1);
        MatrixCursor.RowBuilder rowBuilderNewRow = matrixCursor.newRow();
        for (int i = 0; i < matrixCursor.getColumnCount(); i++) {
            String columnName = matrixCursor.getColumnName(i);
            int i2 = 0;
            while (true) {
                if (i2 < strArr2.length) {
                    if (strArr2[i2].equals(columnName)) {
                        break;
                    }
                    i2++;
                } else {
                    OrtUtil$$ExternalSyntheticBUOutline4.m("Invalid column ", strArr[i]);
                    return null;
                }
            }
            rowBuilderNewRow.add(objArr[i2]);
        }
        return matrixCursor;
    }

    public static void notifyChangeForSubIdAndField(Context context, ServiceState serviceState, ServiceState serviceState2, int i) {
        boolean z = serviceState == null;
        if (z || voiceRegStateChanged(serviceState, serviceState2)) {
            context.getContentResolver().notifyChange(Telephony.ServiceStateTable.getUriForSubscriptionIdAndField(i, "voice_reg_state"), null, false, -1);
        }
        if (z || dataRegStateChanged(serviceState, serviceState2)) {
            context.getContentResolver().notifyChange(Telephony.ServiceStateTable.getUriForSubscriptionIdAndField(i, "data_reg_state"), null, false, -1);
        }
        if (z || voiceRoamingTypeChanged(serviceState, serviceState2)) {
            context.getContentResolver().notifyChange(Telephony.ServiceStateTable.getUriForSubscriptionIdAndField(i, "voice_roaming_type"), null, false, -1);
        }
        if (z || dataRoamingTypeChanged(serviceState, serviceState2)) {
            context.getContentResolver().notifyChange(Telephony.ServiceStateTable.getUriForSubscriptionIdAndField(i, "data_roaming_type"), null, false, -1);
        }
        if (z || dataNetworkTypeChanged(serviceState, serviceState2)) {
            context.getContentResolver().notifyChange(Telephony.ServiceStateTable.getUriForSubscriptionIdAndField(i, "data_network_type"), null, false, -1);
        }
    }

    private static boolean voiceRegStateChanged(ServiceState serviceState, ServiceState serviceState2) {
        return serviceState.getState() != serviceState2.getState();
    }

    private static boolean dataRegStateChanged(ServiceState serviceState, ServiceState serviceState2) {
        return serviceState.getDataRegistrationState() != serviceState2.getDataRegistrationState();
    }

    private static boolean voiceRoamingTypeChanged(ServiceState serviceState, ServiceState serviceState2) {
        return serviceState.getVoiceRoamingType() != serviceState2.getVoiceRoamingType();
    }

    private static boolean dataRoamingTypeChanged(ServiceState serviceState, ServiceState serviceState2) {
        return serviceState.getDataRoamingType() != serviceState2.getDataRoamingType();
    }

    private static boolean dataNetworkTypeChanged(ServiceState serviceState, ServiceState serviceState2) {
        return serviceState.getDataNetworkType() != serviceState2.getDataNetworkType();
    }

    public static void notifyChangeForSubId(Context context, ServiceState serviceState, ServiceState serviceState2, int i) {
        if (serviceState == null || voiceRegStateChanged(serviceState, serviceState2) || dataRegStateChanged(serviceState, serviceState2) || voiceRoamingTypeChanged(serviceState, serviceState2) || dataRoamingTypeChanged(serviceState, serviceState2) || dataNetworkTypeChanged(serviceState, serviceState2)) {
            context.getContentResolver().notifyChange(Telephony.ServiceStateTable.getUriForSubscriptionId(i), null, false, -1);
        }
    }

    private boolean isPathPrefixMatch(Uri uri, Uri uri2) {
        if (!Objects.equals(uri.getScheme(), uri2.getScheme()) || !Objects.equals(uri.getAuthority(), uri2.getAuthority())) {
            return false;
        }
        List<String> pathSegments = uri.getPathSegments();
        List<String> pathSegments2 = uri2.getPathSegments();
        int size = pathSegments2.size();
        if (pathSegments.size() < size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!Objects.equals(pathSegments.get(i), pathSegments2.get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean hasLocationPermission() {
        return LocationAccessPolicy.checkLocationPermission(getContext(), new LocationAccessPolicy.LocationPermissionQuery.Builder().setCallingPackage(getCallingPackage()).setCallingFeatureId(getCallingAttributionTag()).setCallingPid(Binder.getCallingPid()).setCallingUid(Binder.getCallingUid()).setMethod("ServiceStateProvider#query").setLogAsInfo(true).setMinSdkVersionForFine(29).setMinSdkVersionForCoarse(29).setMinSdkVersionForEnforcement(29).build()) == LocationAccessPolicy.LocationPermissionResult.ALLOWED;
    }

    static ServiceState getLocationRedactedServiceState(ServiceState serviceState) {
        return serviceState.createLocationInfoSanitizedCopy(true);
    }
}
