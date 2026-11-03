package com.android.internal.telephony.data;

import android.net.LinkProperties;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.SubscriptionManager;
import android.telephony.data.DataCallResponse;
import android.telephony.data.DataProfile;
import android.telephony.data.DataService;
import android.telephony.data.DataServiceCallback;
import android.telephony.data.NetworkSliceInfo;
import android.telephony.data.TrafficDescriptor;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.NetworkTypeController$ModemDisplayNetworkType$$ExternalSyntheticRecord0;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.telephony.Rlog;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public class CellularDataService extends DataService {
    private static final String TAG = CellularDataService.class.getSimpleName();

    /* JADX INFO: Access modifiers changed from: private */
    class CellularDataServiceProvider extends DataService.DataServiceProvider {
        private final Map mCallbackMap;
        private final Handler mHandler;
        private final Phone mPhone;
        private final Map mResultCodeCallbackMap;

        /* JADX INFO: Access modifiers changed from: private */
        static final class ResultCodeCallback extends Record {
            private final Consumer callback;
            private final Executor executor;

            private /* synthetic */ boolean $record$equals(Object obj) {
                if (!(obj instanceof ResultCodeCallback)) {
                    return false;
                }
                ResultCodeCallback resultCodeCallback = (ResultCodeCallback) obj;
                return Objects.equals(this.executor, resultCodeCallback.executor) && Objects.equals(this.callback, resultCodeCallback.callback);
            }

            private /* synthetic */ Object[] $record$getFieldsAsObjects() {
                return new Object[]{this.executor, this.callback};
            }

            private ResultCodeCallback(Executor executor, Consumer consumer) {
                this.executor = executor;
                this.callback = consumer;
            }

            public Consumer<Integer> callback() {
                return this.callback;
            }

            @Override // java.lang.Record
            public final boolean equals(Object obj) {
                return $record$equals(obj);
            }

            public Executor executor() {
                return this.executor;
            }

            @Override // java.lang.Record
            public final int hashCode() {
                return CellularDataService$CellularDataServiceProvider$ResultCodeCallback$$ExternalSyntheticRecord0.m(this.executor, this.callback);
            }

            @Override // java.lang.Record
            public final String toString() {
                return NetworkTypeController$ModemDisplayNetworkType$$ExternalSyntheticRecord0.m($record$getFieldsAsObjects(), ResultCodeCallback.class, "executor;callback");
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void accept(final int i) {
                this.executor.execute(new Runnable() { // from class: com.android.internal.telephony.data.CellularDataService$CellularDataServiceProvider$ResultCodeCallback$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$accept$0(i);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$accept$0(int i) {
                this.callback.accept(Integer.valueOf(i));
            }
        }

        private CellularDataServiceProvider(int i) {
            super(CellularDataService.this, i);
            this.mCallbackMap = new HashMap();
            this.mResultCodeCallbackMap = new HashMap();
            Phone phone = PhoneFactory.getPhone(getSlotIndex());
            this.mPhone = phone;
            Looper looperMyLooper = Looper.myLooper();
            Objects.requireNonNull(looperMyLooper);
            Handler handler = new Handler(looperMyLooper) { // from class: com.android.internal.telephony.data.CellularDataService.CellularDataServiceProvider.1
                @Override // android.os.Handler
                public void handleMessage(Message message) {
                    DataServiceCallback dataServiceCallback = (DataServiceCallback) CellularDataServiceProvider.this.mCallbackMap.remove(message);
                    ResultCodeCallback resultCodeCallback = (ResultCodeCallback) CellularDataServiceProvider.this.mResultCodeCallbackMap.remove(message);
                    AsyncResult asyncResult = (AsyncResult) message.obj;
                    switch (message.what) {
                        case 1:
                            dataServiceCallback.onSetupDataCallComplete(asyncResult.exception != null ? 4 : 0, (DataCallResponse) asyncResult.result);
                            break;
                        case 2:
                            dataServiceCallback.onDeactivateDataCallComplete(asyncResult.exception != null ? 4 : 0);
                            break;
                        case 3:
                            dataServiceCallback.onSetInitialAttachApnComplete(asyncResult.exception != null ? 4 : 0);
                            break;
                        case 4:
                            dataServiceCallback.onSetDataProfileComplete(asyncResult.exception != null ? 4 : 0);
                            break;
                        case 5:
                            int i2 = asyncResult.exception != null ? 4 : 0;
                            Object obj = asyncResult.result;
                            dataServiceCallback.onRequestDataCallListComplete(i2, obj != null ? (List) obj : Collections.EMPTY_LIST);
                            break;
                        case 6:
                            CellularDataServiceProvider.this.notifyDataCallListChanged((List) asyncResult.result);
                            break;
                        case 7:
                            dataServiceCallback.onHandoverStarted(CellularDataServiceProvider.this.toResultCode(asyncResult.exception));
                            break;
                        case 8:
                            dataServiceCallback.onHandoverCancelled(CellularDataServiceProvider.this.toResultCode(asyncResult.exception));
                            break;
                        case 9:
                            Object obj2 = asyncResult.result;
                            boolean z = obj2 instanceof DataProfile;
                            CellularDataServiceProvider cellularDataServiceProvider = CellularDataServiceProvider.this;
                            if (z) {
                                cellularDataServiceProvider.notifyDataProfileUnthrottled((DataProfile) obj2);
                            } else {
                                cellularDataServiceProvider.notifyApnUnthrottled((String) obj2);
                            }
                            break;
                        case 10:
                            resultCodeCallback.accept(CellularDataServiceProvider.this.toResultCode(asyncResult.exception));
                            break;
                        case 11:
                            resultCodeCallback.accept(CellularDataServiceProvider.this.toResultCode(asyncResult.exception));
                            break;
                        case 12:
                            resultCodeCallback.accept(CellularDataServiceProvider.this.toResultCode(asyncResult.exception));
                            break;
                        case 13:
                            CellularDataServiceProvider.this.notifyDataCallListUpdated((List) asyncResult.result);
                            break;
                        default:
                            CellularDataService.this.loge("Unexpected event: " + message.what);
                            break;
                    }
                }
            };
            this.mHandler = handler;
            phone.mCi.registerForDataCallListChanged(handler, 6, null);
            phone.mCi.registerForDataCallListUpdated(handler, 13, null);
            phone.mCi.registerForApnUnthrottled(handler, 9, null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int toResultCode(Throwable th) {
            if (th == null) {
                return 0;
            }
            if (th instanceof CommandException) {
                return ((CommandException) th).getCommandError() == CommandException.Error.REQUEST_NOT_SUPPORTED ? 1 : 4;
            }
            CellularDataService.this.loge("Throwable is of type " + th.getClass().getSimpleName() + " but should be CommandException");
            return 4;
        }

        public void setupDataCall(int i, DataProfile dataProfile, boolean z, boolean z2, int i2, LinkProperties linkProperties, int i3, NetworkSliceInfo networkSliceInfo, TrafficDescriptor trafficDescriptor, boolean z3, DataServiceCallback dataServiceCallback) {
            Message messageObtain;
            if (dataServiceCallback != null) {
                messageObtain = Message.obtain(this.mHandler, 1);
                this.mCallbackMap.put(messageObtain, dataServiceCallback);
            } else {
                messageObtain = null;
            }
            this.mPhone.mCi.setupDataCall(i, dataProfile, z2, i2, linkProperties, i3, networkSliceInfo, trafficDescriptor, z3, messageObtain);
        }

        public void deactivateDataCall(int i, int i2, DataServiceCallback dataServiceCallback) {
            Message messageObtain;
            if (dataServiceCallback != null) {
                messageObtain = Message.obtain(this.mHandler, 2);
                this.mCallbackMap.put(messageObtain, dataServiceCallback);
            } else {
                messageObtain = null;
            }
            this.mPhone.mCi.deactivateDataCall(i, i2, messageObtain);
        }

        public void setInitialAttachApn(DataProfile dataProfile, boolean z, DataServiceCallback dataServiceCallback) {
            Message messageObtain;
            if (dataServiceCallback != null) {
                messageObtain = Message.obtain(this.mHandler, 3);
                this.mCallbackMap.put(messageObtain, dataServiceCallback);
            } else {
                messageObtain = null;
            }
            this.mPhone.mCi.setInitialAttachApn(dataProfile, messageObtain);
        }

        public void setDataProfile(List<DataProfile> list, boolean z, DataServiceCallback dataServiceCallback) {
            Message messageObtain;
            if (dataServiceCallback != null) {
                messageObtain = Message.obtain(this.mHandler, 4);
                this.mCallbackMap.put(messageObtain, dataServiceCallback);
            } else {
                messageObtain = null;
            }
            this.mPhone.mCi.setDataProfile((DataProfile[]) list.toArray(new DataProfile[0]), messageObtain);
        }

        public void requestDataCallList(DataServiceCallback dataServiceCallback) {
            Message messageObtain;
            if (dataServiceCallback != null) {
                messageObtain = Message.obtain(this.mHandler, 5);
                this.mCallbackMap.put(messageObtain, dataServiceCallback);
            } else {
                messageObtain = null;
            }
            this.mPhone.mCi.getDataCallList(messageObtain);
        }

        public void startHandover(int i, DataServiceCallback dataServiceCallback) {
            Message messageObtain;
            if (dataServiceCallback != null) {
                messageObtain = Message.obtain(this.mHandler, 7);
                this.mCallbackMap.put(messageObtain, dataServiceCallback);
            } else {
                messageObtain = null;
            }
            this.mPhone.mCi.startHandover(messageObtain, i);
        }

        public void cancelHandover(int i, DataServiceCallback dataServiceCallback) {
            Message messageObtain;
            if (dataServiceCallback != null) {
                messageObtain = Message.obtain(this.mHandler, 8);
                this.mCallbackMap.put(messageObtain, dataServiceCallback);
            } else {
                messageObtain = null;
            }
            this.mPhone.mCi.cancelHandover(messageObtain, i);
        }

        public void notifyUserDataEnabled(boolean z, Executor executor, Consumer<Integer> consumer) {
            Objects.requireNonNull(executor, "executor cannot be null");
            Objects.requireNonNull(consumer, "resultCodeCallback cannot be null");
            Message messageObtain = Message.obtain(this.mHandler, 10);
            this.mResultCodeCallbackMap.put(messageObtain, new ResultCodeCallback(executor, consumer));
            this.mPhone.mCi.setUserDataEnabled(messageObtain, z);
        }

        public void notifyUserDataRoamingEnabled(boolean z, Executor executor, Consumer<Integer> consumer) {
            Objects.requireNonNull(executor, "executor cannot be null");
            Objects.requireNonNull(consumer, "resultCodeCallback cannot be null");
            Message messageObtain = Message.obtain(this.mHandler, 11);
            this.mResultCodeCallbackMap.put(messageObtain, new ResultCodeCallback(executor, consumer));
            this.mPhone.mCi.setUserDataRoamingEnabled(messageObtain, z);
        }

        public void notifyImsDataNetwork(int i, int i2, int i3, int i4, Executor executor, Consumer<Integer> consumer) {
            Objects.requireNonNull(executor, "executor cannot be null");
            Objects.requireNonNull(consumer, "resultCodeCallback cannot be null");
            Message messageObtain = Message.obtain(this.mHandler, 12);
            this.mResultCodeCallbackMap.put(messageObtain, new ResultCodeCallback(executor, consumer));
            this.mPhone.mCi.notifyImsDataNetwork(i, i2, i3, i4, messageObtain);
        }

        public void close() {
            this.mPhone.mCi.unregisterForDataCallListChanged(this.mHandler);
            this.mPhone.mCi.unregisterForDataCallListUpdated(this.mHandler);
        }
    }

    public DataService.DataServiceProvider onCreateDataServiceProvider(int i) {
        log("Cellular data service created for slot " + i);
        if (!SubscriptionManager.isValidSlotIndex(i)) {
            loge("Tried to cellular data service with invalid slotId " + i);
            return null;
        }
        return new CellularDataServiceProvider(i);
    }

    private void log(String str) {
        Rlog.d(TAG, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loge(String str) {
        Rlog.e(TAG, str);
    }
}
