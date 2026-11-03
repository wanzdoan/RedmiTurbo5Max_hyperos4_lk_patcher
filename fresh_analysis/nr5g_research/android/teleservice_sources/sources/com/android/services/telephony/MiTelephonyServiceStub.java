package com.android.services.telephony;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telecom.Conference;
import android.telecom.Connection;
import android.telecom.ConnectionRequest;
import android.telecom.ConnectionService;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.emergency.RadioOnHelper;
import com.xiaomi.services.telephony.TelephonyFactory;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiTelephonyServiceStub {
    private static volatile IMiTelephonyService sInstance;

    public static IMiTelephonyService get() {
        if (sInstance == null) {
            synchronized (MiTelephonyServiceStub.class) {
                try {
                    if (sInstance == null) {
                        sInstance = TelephonyFactory.getHyperInterface().makeTelephonyServiceProxy();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    public interface IMiTelephonyService {
        default void ShowSuppToastIfNeed(TelephonyConnection telephonyConnection, int i, int i2) {
        }

        default void configNetworkSelectionNotification(Context context, Notification.Builder builder, int i) {
        }

        default boolean disableManageConference(Phone phone) {
            return true;
        }

        default boolean disableNetworkIdentifiedEmergencyCall(TelephonyConnection telephonyConnection) {
            return false;
        }

        default void disconnectConferenceParticipants(Conference conference) {
        }

        default boolean dismissCountryISOAfterConference(Phone phone) {
            return false;
        }

        default void factoryReset() {
        }

        default int getMiuiResId(int i) {
            return i;
        }

        default Connection getTelephonyConnection(ConnectionRequest connectionRequest, Context context, String str, boolean z, Uri uri, Phone phone, TelephonyConnectionService.DisconnectCauseFactory disconnectCauseFactory, DeviceState deviceState, TelephonyConnectionService.PhoneFactoryProxy phoneFactoryProxy) {
            return null;
        }

        default Bitmap getVoiceMailStatusIcon(Context context) {
            return null;
        }

        default void handleMessage(TelephonyConnection telephonyConnection, Handler handler, Message message) {
        }

        default void init(Context context) {
        }

        default boolean isAdhocConferenceCall(Conference conference, Phone phone, boolean z) {
            return z;
        }

        default boolean isAfterSrvccConnection(TelephonyConnection telephonyConnection) {
            return false;
        }

        default boolean isCallTransferSupported(TelephonyConnection telephonyConnection, boolean z, boolean z2) {
            return false;
        }

        default boolean isDomesticRoamingEnable(Context context) {
            return false;
        }

        default boolean isHoldAllowed(Conference conference, boolean z) {
            return z;
        }

        default boolean isNoNotificationOnDomesticRoaming() {
            return false;
        }

        default boolean isSupportDomesticRoaming() {
            return false;
        }

        default void newTelephonyConnection(TelephonyConnection telephonyConnection) {
        }

        default void notifySSNotificationToast(TelephonyConnection telephonyConnection, int i, int i2, int i3) {
        }

        default void onAnswer(TelephonyConnection telephonyConnection, int i) {
        }

        default void onConnectionRemoved(Connection connection) {
        }

        default void onCreateConnectionComplete(ConnectionService connectionService, Connection connection) {
        }

        default boolean onExtrasChanged(Connection connection, Bundle bundle) {
            return false;
        }

        default void onMerge(Connection connection, Phone phone) {
        }

        default void onParticipantConnectionDisconnect(Connection connection, Uri uri) {
        }

        default void performConference(TelephonyConnection telephonyConnection, Connection connection) {
        }

        default boolean putSubscriptionIdExtra(Intent intent, int i) {
            return false;
        }

        default void registerForCallEvents(TelephonyConnection telephonyConnection, Phone phone, Handler handler) {
        }

        default boolean retryOutgoingOriginalConnection(TelephonyConnection telephonyConnection, TelephonyConnectionService telephonyConnectionService, RadioOnHelper radioOnHelper) {
            return false;
        }

        default void setAfterSrvccConnection(TelephonyConnection telephonyConnection, boolean z) {
        }

        default boolean setHoldable(Conference conference, boolean z) {
            return z;
        }

        default void setLocalDisconnecting(TelephonyConnection telephonyConnection, boolean z) {
        }

        default void setSrvccConnectionTimeReset(TelephonyConnection telephonyConnection, boolean z) {
        }

        default boolean shouldPlayDisconnectedToneForRingCall(int i) {
            return false;
        }

        default int toTelecomDisconnectCauseCode(int i) {
            return 0;
        }

        default Integer toTelecomDisconnectCauseDescription(Context context, int i, int i2, Integer num) {
            return num;
        }

        default CharSequence toTelecomDisconnectCauseLabelOrDescription(CharSequence charSequence, CharSequence charSequence2) {
            return charSequence;
        }

        default void unregisterForCallEvents(TelephonyConnection telephonyConnection, Phone phone, Handler handler) {
        }

        default void updateAddress(TelephonyConnection telephonyConnection, Uri uri) {
        }

        default void updateConnectionAddress(ConnectionRequest connectionRequest, Connection connection, Uri uri, Phone phone, String str, boolean z) {
        }

        default boolean updateHighDefAudioValue(TelephonyConnection telephonyConnection, boolean z) {
            return z;
        }

        default void updateMultiparty(TelephonyConnection telephonyConnection) {
        }

        default void updateStateForRelayCall(TelephonyConnection telephonyConnection) {
        }

        default boolean updateStatusHints(Conference conference) {
            return false;
        }

        default boolean updateStatusHints(TelephonyConnection telephonyConnection) {
            return false;
        }

        default String getSimCardDesp(Context context, int i) {
            return "";
        }

        default String getCallForwardNotiContentText(Context context, int i) {
            return "";
        }
    }
}
