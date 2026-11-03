package com.android.internal.telephony.ntndata;

import android.content.Intent;
import android.net.LinkProperties;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.telephony.DataFailCause;
import android.telephony.SubscriptionManager;
import android.telephony.data.DataCallResponse;
import android.telephony.data.DataProfile;
import android.telephony.data.DataService;
import android.telephony.data.DataServiceCallback;
import android.telephony.data.NetworkSliceInfo;
import android.telephony.data.TrafficDescriptor;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.ntnphone.NtnPhone;
import com.android.internal.telephony.ntnphone.NtnPhoneFactory;
import com.android.telephony.Rlog;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteDataService extends DataService {
    private static final int APN_UNTHROTTLED = 9;
    private static final int CANCEL_HANDOVER = 8;
    private static final int DATA_CALL_LIST_CHANGED = 6;
    private static final boolean DBG = true;
    private static final int DEACTIVATE_DATA_CALL_COMPLETE = 2;
    private static final int REGISTER_DATA_CALL_LIST_CHANGED = 10;
    private static final int REQUEST_DATA_CALL_LIST_COMPLETE = 5;
    private static final int SETUP_DATA_CALL_COMPLETE = 1;
    private static final int SET_DATA_PROFILE_COMPLETE = 4;
    private static final int SET_INITIAL_ATTACH_APN_COMPLETE = 3;
    private static final int START_HANDOVER = 7;
    private static final String TAG = SatelliteDataService.class.getSimpleName();
    private final String SERVICE_INTERFACE = MiuiDataServiceManagerImpl.SERVICE_INTERFACE;

    private class SatelliteDataServiceProvider extends DataService.DataServiceProvider {
        private final Map<Message, DataServiceCallback> mCallbackMap;
        private final Handler mHandler;
        private NtnPhone mNtnPhone;
        private final Phone mPhone;

        private SatelliteDataServiceProvider(int slotId) {
            super(SatelliteDataService.this, slotId);
            this.mCallbackMap = new HashMap();
            this.mPhone = PhoneFactory.getPhone(getSlotIndex());
            this.mHandler = new Handler(Looper.myLooper()) { // from class: com.android.internal.telephony.ntndata.SatelliteDataService.SatelliteDataServiceProvider.1
                @Override // android.os.Handler
                public void handleMessage(Message message) {
                    DataServiceCallback callback = (DataServiceCallback) SatelliteDataServiceProvider.this.mCallbackMap.remove(message);
                    AsyncResult ar = (AsyncResult) message.obj;
                    switch (message.what) {
                        case 1:
                            DataCallResponse response = (DataCallResponse) ar.result;
                            callback.onSetupDataCallComplete(ar.exception == null ? 0 : 4, response);
                            SatelliteDataServiceProvider.this.updateDataConnectionState(ar, SatelliteDataService.DBG);
                            break;
                        case 2:
                            callback.onDeactivateDataCallComplete(ar.exception == null ? 0 : 4);
                            SatelliteDataServiceProvider.this.updateDataConnectionState(ar, false);
                            break;
                        case 3:
                        case 4:
                        case 7:
                        case 8:
                        case 9:
                            break;
                        case 5:
                            callback.onRequestDataCallListComplete(ar.exception == null ? 0 : 4, ar.result != null ? (List) ar.result : Collections.EMPTY_LIST);
                            break;
                        case 6:
                            SatelliteDataServiceProvider.this.notifyDataCallListChanged((List) ar.result);
                            break;
                        case 10:
                            SatelliteDataServiceProvider.this.registerForDataCallListChanged();
                            break;
                        default:
                            SatelliteDataService.this.loge("Unexpected event: " + message.what);
                            break;
                    }
                }
            };
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(10), 1000L);
        }

        public void updateDataConnectionState(AsyncResult ar, boolean isActive) {
            SatelliteDataService.this.log("updateDataConnectionState ar.exception = " + ar.exception + ", ar.result = " + ar.result + ", isActive = " + isActive);
            if (ar.exception == null && ar.result != null && isActive) {
                DataCallResponse response = (DataCallResponse) ar.result;
                int failCause = DataFailCause.getFailCause(response.getCause());
                SatelliteDataService.this.log("updateDataConnectionState" + DataFailCause.getFailCause(response.getCause()));
                if (failCause == 0) {
                    updateDataConnectionState(isActive);
                    return;
                }
                return;
            }
            if (!isActive) {
                updateDataConnectionState(isActive);
            }
        }

        public void updateDataConnectionState(boolean isActive) {
            try {
                this.mNtnPhone = NtnPhoneFactory.getDefaultPhone();
                if (this.mNtnPhone != null) {
                    this.mNtnPhone.mCT.updateDataConnectionState(isActive);
                }
            } catch (Exception ex) {
                SatelliteDataService.this.log("updateDataConnectionState " + ex);
            }
        }

        public void registerForDataCallListChanged() {
            SatelliteDataService.this.log("Register for data call list changed.");
            try {
                this.mNtnPhone = NtnPhoneFactory.getDefaultPhone();
                if (this.mNtnPhone != null) {
                    this.mNtnPhone.mNtnRil.registerForDataCallListChanged(this.mHandler, 6, (Object) null);
                }
            } catch (Exception ex) {
                SatelliteDataService.this.log("registerForDataCallListChanged " + ex);
            }
        }

        public void setupDataCall(int accessNetworkType, DataProfile dataProfile, boolean isRoaming, boolean allowRoaming, int reason, LinkProperties linkProperties, int pduSessionId, NetworkSliceInfo sliceInfo, TrafficDescriptor trafficDescriptor, boolean matchAllRuleAllowed, DataServiceCallback callback) {
            Message message;
            SatelliteDataService.this.log("setupDataCall " + getSlotIndex());
            if (callback == null) {
                message = null;
            } else {
                Message message2 = Message.obtain(this.mHandler, 1);
                this.mCallbackMap.put(message2, callback);
                message = message2;
            }
            try {
                this.mNtnPhone = NtnPhoneFactory.getDefaultPhone();
                if (this.mNtnPhone != null) {
                    this.mNtnPhone.mNtnRil.setupDataCall(dataProfile, accessNetworkType, false, allowRoaming, isRoaming, reason, message);
                }
            } catch (Exception ex) {
                SatelliteDataService.this.log("setupDataCall " + ex);
            }
        }

        public void deactivateDataCall(int cid, int reason, DataServiceCallback callback) {
            SatelliteDataService.this.log("deactivateDataCall " + getSlotIndex());
            Message message = null;
            if (callback != null) {
                message = Message.obtain(this.mHandler, 2);
                this.mCallbackMap.put(message, callback);
            }
            try {
                this.mNtnPhone = NtnPhoneFactory.getDefaultPhone();
                if (this.mNtnPhone != null) {
                    this.mNtnPhone.mNtnRil.deactivateDataCall(cid, reason, message);
                }
            } catch (Exception ex) {
                SatelliteDataService.this.log("deactivateDataCall " + ex);
            }
        }

        public void setInitialAttachApn(DataProfile dataProfile, boolean isRoaming, DataServiceCallback callback) {
            SatelliteDataService.this.log("setInitialAttachApn " + getSlotIndex());
        }

        public void setDataProfile(List<DataProfile> dps, boolean isRoaming, DataServiceCallback callback) {
            SatelliteDataService.this.log("setDataProfile " + getSlotIndex());
        }

        public void requestDataCallList(DataServiceCallback callback) {
            SatelliteDataService.this.log("requestDataCallList " + getSlotIndex());
            Message message = null;
            if (callback != null) {
                message = Message.obtain(this.mHandler, 5);
                this.mCallbackMap.put(message, callback);
            }
            try {
                this.mNtnPhone = NtnPhoneFactory.getDefaultPhone();
                if (this.mNtnPhone != null) {
                    this.mNtnPhone.mNtnRil.getDataCallList(message);
                }
            } catch (Exception ex) {
                SatelliteDataService.this.log("setupDataCall " + ex);
            }
        }

        public void startHandover(int cid, DataServiceCallback callback) {
            SatelliteDataService.this.log("startHandover " + getSlotIndex());
        }

        public void cancelHandover(int cid, DataServiceCallback callback) {
        }

        public void close() {
            try {
                this.mNtnPhone = NtnPhoneFactory.getDefaultPhone();
                if (this.mNtnPhone != null) {
                    this.mNtnPhone.mNtnRil.unregisterForDataCallListChanged(this.mHandler);
                }
            } catch (Exception ex) {
                SatelliteDataService.this.log("unregisterForDataCallListChanged " + ex);
            }
        }
    }

    public IBinder onBind(Intent intent) {
        if (intent == null || !MiuiDataServiceManagerImpl.SERVICE_INTERFACE.equals(intent.getAction())) {
            loge("Unexpected intent " + intent);
            return super.onBind(intent);
        }
        return this.mBinder;
    }

    public DataService.DataServiceProvider onCreateDataServiceProvider(int slotIndex) {
        log("Cellular data service created for slot " + slotIndex);
        if (!SubscriptionManager.isValidSlotIndex(slotIndex)) {
            loge("Tried to cellular data service with invalid slotId " + slotIndex);
            return null;
        }
        return new SatelliteDataServiceProvider(slotIndex);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String s) {
        Rlog.d(TAG, s);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loge(String s) {
        Rlog.e(TAG, s);
    }
}
