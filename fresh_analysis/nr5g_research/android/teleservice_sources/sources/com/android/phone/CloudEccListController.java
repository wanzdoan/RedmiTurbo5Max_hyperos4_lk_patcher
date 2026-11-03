package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import miui.telephony.DefaultSimManager;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class CloudEccListController extends CloudEccController {
    private Context mContext = null;
    private CloudEccListHandler mHandler = null;
    private final int INT_SIZE = 4;
    private final int QCRIL_HOOK_MSG_REQ_SET_ECC_NUMBER = 5001;
    private final int DEFAULT_PHONE = 0;
    private final int DEFAULT_QUERY_CODE = 1;
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.CloudEccListController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            CloudEccController.logd("CloudEccListController mIntentReceiver = " + action);
            if (action == null) {
                return;
            }
            if (action.equals("CloudEccListController.intent.debug")) {
                CloudEccListController.this.mHandler.obtainMessage(1, intent).sendToTarget();
            } else if (action.equals("CloudEccListController.intent.get")) {
                CloudEccListController.this.mHandler.obtainMessage(2, intent).sendToTarget();
            }
        }
    };

    @Override // com.android.phone.CloudEccController
    void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    public static CloudEccController make() {
        if (CloudEccController.mInstance instanceof CloudEccListController) {
            return CloudEccController.mInstance;
        }
        CloudEccController.mInstance = new CloudEccListController();
        return CloudEccController.mInstance;
    }

    @Override // com.android.phone.CloudEccController
    public void init(Context context, HandlerThread handlerThread) {
        this.mContext = context;
        this.mHandler = new CloudEccListHandler(handlerThread.getLooper());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("CloudEccListController.intent.debug");
        intentFilter.addAction("CloudEccListController.intent.get");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
        CloudEccController.logd("CloudEccListController init");
    }

    private ByteBuffer createBufferWithNativeByteOrder(byte[] bArr) {
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        byteBufferWrap.order(ByteOrder.nativeOrder());
        return byteBufferWrap;
    }

    class CloudEccListHandler extends Handler {
        public CloudEccListHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i != 1) {
                if (i != 2) {
                    return;
                }
                CloudEccController.logd("CloudEccListController EVENT_ECC_LIST_GET");
                CloudEccListController.this.updateCloudEccList();
                return;
            }
            CloudEccController.logd("CloudEccListController EVENT_ECC_LIST_DEBUG");
            try {
                final ArrayList arrayList = new ArrayList();
                JSONObject jSONObject = new JSONObject();
                Intent intent = (Intent) message.obj;
                jSONObject.put(CloudEccController.TYPE_FLAG, intent.getStringExtra("type_flag"));
                jSONObject.put(CloudEccController.ECC, intent.getStringExtra("ecc"));
                jSONObject.put(CloudEccController.CATEGORY, intent.getStringExtra("category"));
                jSONObject.put(CloudEccController.CONDITION, intent.getStringExtra("condition"));
                jSONObject.put(CloudEccController.PLMN, intent.getStringExtra("plmn"));
                jSONObject.put(CloudEccController.MODE, intent.getStringExtra("mode"));
                CloudEccController.logd("CloudEccListController debugEccList = " + jSONObject);
                arrayList.add(jSONObject);
                new Thread() { // from class: com.android.phone.CloudEccListController.CloudEccListHandler.1
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() throws Throwable {
                        CloudEccListController.this.updateEccList(arrayList);
                    }
                }.start();
            } catch (Exception e) {
                CloudEccController.loge("EVENT_ECC_LIST_DEBUG error!!!", e);
            }
        }
    }

    @Override // com.android.phone.CloudEccController
    public void updateCloudEccList() {
        final List cloudEccListInfos = CloudEccController.getCloudEccListInfos();
        if (cloudEccListInfos == null) {
            return;
        }
        CloudEccController.logd("updateCloudEccList: count=" + cloudEccListInfos.size());
        if (isUpdateCloudEccListSupported() && isCloudEccListChanged(cloudEccListInfos)) {
            CloudEccController.logd("updateCloudEccList: ecc list changed, updating modem");
            new Thread() { // from class: com.android.phone.CloudEccListController.2
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() throws Throwable {
                    CloudEccListController.this.updateEccList(cloudEccListInfos);
                }
            }.start();
        }
        if (isRemoveEccDataSupported(this.mContext)) {
            return;
        }
        updateLatestCloudEccdata(cloudEccListInfos);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateEccList(List list) throws Throwable {
        CloudEccListController cloudEccListController;
        Throwable th;
        CloudEccListController cloudEccListController2;
        try {
            CloudEccController.logd("CloudEccListController updateEccList = " + list);
            if (list == null || list.size() <= 0 || this.mContext == null) {
                cloudEccListController2 = this;
            } else {
                updateLastCloudEccListVersion(list);
                this.mContext.getContentResolver().notifyChange(Uri.parse("content://com.xiaomi.ecc.provider/cloud_data"), null);
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    JSONObject jSONObject = (JSONObject) it.next();
                    if (CloudEccController.isValidCloudEccList(jSONObject)) {
                        String strOptString = jSONObject.optString(CloudEccController.TYPE_FLAG);
                        String strOptString2 = jSONObject.optString(CloudEccController.ECC);
                        int i = Integer.parseInt(jSONObject.optString(CloudEccController.CATEGORY));
                        int i2 = Integer.parseInt(jSONObject.optString(CloudEccController.CONDITION));
                        String strOptString3 = jSONObject.optString(CloudEccController.PLMN);
                        int i3 = Integer.parseInt(jSONObject.optString(CloudEccController.MODE));
                        if (ModemProvider.IS_QCOM && i3 == 4) {
                            try {
                                CloudEccController.logd("QC platform skip DELETE mode entry");
                            } catch (Throwable th2) {
                                th = th2;
                                cloudEccListController = this;
                                throw th;
                            }
                        } else {
                            cloudEccListController = this;
                            try {
                                cloudEccListController.execUpdateEccList(strOptString, strOptString2, i, i2, strOptString3, i3);
                                if (ModemProvider.IS_MTK) {
                                    try {
                                        Thread.sleep(500L);
                                    } catch (InterruptedException unused) {
                                    }
                                }
                            } catch (Throwable th3) {
                                th = th3;
                                th = th;
                                throw th;
                            }
                        }
                    } else {
                        cloudEccListController = this;
                        CloudEccController.loge("updateEccList not valid ecc list = " + jSONObject);
                    }
                    this = cloudEccListController;
                }
                cloudEccListController2 = this;
                for (Phone phone : PhoneFactory.getPhones()) {
                    FiveGManagerBase.getInstance().sendAtCommand(phone, new String[]{"xiaomi_ecc_update_end"}, (Message) null);
                }
            }
        } catch (Throwable th4) {
            th = th4;
            cloudEccListController = this;
        }
    }

    private void execUpdateEccList(String str, String str2, int i, int i2, String str3, int i3) {
        CloudEccController.logd("execUpdateEccList updateEccList ecc_type_flag = " + str + ", ecc_number = " + str2 + ", ecc_category = " + i + ", ecc_condition = " + i2 + ", ecc_plmn = " + str3 + ", ecc_mode = " + i3);
        Phone phone = MiuiPhoneUtils.getPhone(DefaultSimManager.getDefaultDataSlotId());
        if (phone == null) {
            return;
        }
        if (ModemProvider.IS_MTK) {
            try {
                FiveGManagerBase.getInstance().sendAtCommand(phone, new String[]{"EccEntry," + i3 + "," + str2 + "," + i + "," + i2 + "," + str3}, (Message) null);
                CloudEccController.logd("execUpdateEccList Response is: Success");
                return;
            } catch (Exception e) {
                CloudEccController.loge("execUpdateEccList exception: " + e.getMessage());
                return;
            }
        }
        if (ModemProvider.IS_QCOM) {
            try {
                int length = str.getBytes().length + 24 + str2.getBytes().length + str3.getBytes().length;
                byte[] bArr = new byte[length];
                ByteBuffer byteBufferCreateBufferWithNativeByteOrder = createBufferWithNativeByteOrder(bArr);
                CloudEccController.logd("execUpdateEccList byte length = " + length);
                byteBufferCreateBufferWithNativeByteOrder.putInt(str.getBytes().length);
                byteBufferCreateBufferWithNativeByteOrder.put(str.getBytes());
                byteBufferCreateBufferWithNativeByteOrder.putInt(str2.getBytes().length);
                byteBufferCreateBufferWithNativeByteOrder.put(str2.getBytes());
                byteBufferCreateBufferWithNativeByteOrder.putInt(i);
                byteBufferCreateBufferWithNativeByteOrder.putInt(i2);
                byteBufferCreateBufferWithNativeByteOrder.putInt(str3.getBytes().length);
                byteBufferCreateBufferWithNativeByteOrder.put(str3.getBytes());
                byteBufferCreateBufferWithNativeByteOrder.putInt(i3);
                ByteBuffer byteBufferWrap = ByteBuffer.wrap(RilHook.get(this.mContext).qcRilUpdateEccList(5001, 1, 0, bArr));
                byteBufferWrap.order(ByteOrder.nativeOrder());
                CloudEccController.logd("execUpdateEccList Response is: " + byteBufferWrap.toString() + "mResBuf[0] = " + String.valueOf((int) ((byte) byteBufferWrap.getInt())));
            } catch (Exception e2) {
                CloudEccController.loge("execUpdateEccList exception: " + e2.getMessage());
            }
        }
    }

    public boolean isUpdateCloudEccListSupported() {
        if (ModemProvider.IS_MTK) {
            return this.mContext.getResources().getBoolean(285540504);
        }
        if (ModemProvider.IS_QCOM) {
            return SystemProperties.getBoolean("persist.vendor.radio.ecc_cloud", false);
        }
        return false;
    }
}
