package com.xiaomi.modem.modemipc;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.xiaomi.modem.ModemUtils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Iterator;
import miui.telephony.CommenUtils;

/* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
/* JADX INFO: loaded from: classes.dex */
public class ModemIpcCallbackAgent {
    private static final String ACTION_PDC_CONFIGS_VALIDATION = "qualcomm.intent.action.ACTION_PDC_CONFIGS_VALIDATION";
    private static final String ACTION_PDC_VALIDATE_DUMPED = "qualcomm.intent.action.ACTIN_PDC_VALIDATE_DUMPED";
    private static final String LOG_TAG = "ModemIpcCallbackAgent";
    private static final String QUALCOMM_HYDRA_DATA_IND_BC_NAME = "qualcomm.intent.action.ACTION_HYDRA_DATA_IND";
    protected static Context mContext = null;

    public static void onInitContext(Context cnt) {
        mContext = cnt;
        log("onInitContext, mContext = " + mContext);
    }

    private static void broadcastDataCust(String strBroadCastName, byte[] bcData, Intent intent) {
        try {
            ByteBuffer bcBuffer = ModemUtils.getBuffer(bcData);
            if (ACTION_PDC_VALIDATE_DUMPED.equals(strBroadCastName)) {
                broadcastDataCustPdcValidateDumped(bcData, intent);
            } else if (ACTION_PDC_CONFIGS_VALIDATION.equals(strBroadCastName)) {
                broadcastDataCustPdcConfigsValidation(bcBuffer, intent);
            } else {
                intent.putExtra(CommenUtils.BROADCAST_CONTEXT, bcData);
            }
        } catch (Exception e) {
            log("broadcastDataCust failed, Exception:" + e.toString());
        }
    }

    private static void broadcastDataCustPdcValidateDumped(byte[] bcData, Intent intent) {
        log("PDC Validate Dumped " + ModemUtils.bytesToString(bcData));
        intent.putExtra("dump_file", bcData);
    }

    private static void broadcastDataCustPdcConfigsValidation(ByteBuffer bcBuffer, Intent intent) {
        int result = bcBuffer.getInt();
        int index = bcBuffer.getInt();
        int nvItemLen = bcBuffer.getInt();
        int nvRefValLen = bcBuffer.getInt();
        int nvCurValLen = bcBuffer.getInt();
        intent.putExtra("result", result);
        intent.putExtra("index", index);
        log("result:" + result + " index:" + index);
        if (nvItemLen != 0 && result == 0) {
            byte[] bytes = new byte[nvItemLen];
            bcBuffer.get(bytes);
            String nvItemInfo = new String(bytes);
            log("nvItemInfo:" + nvItemInfo);
            intent.putExtra("nv_item", nvItemInfo);
            if (nvRefValLen == 0) {
                intent.putExtra("ref_value", "");
            } else {
                byte[] bytes2 = new byte[nvRefValLen];
                bcBuffer.get(bytes2);
                String nvRefVal = new String(bytes2);
                log("nvRefVal:" + nvRefVal);
                intent.putExtra("ref_value", nvRefVal);
            }
            if (nvCurValLen == 0) {
                intent.putExtra("cur_value", "");
                return;
            }
            byte[] bytes3 = new byte[nvCurValLen];
            bcBuffer.get(bytes3);
            String nvCurVal = new String(bytes3);
            log("nvCurVal:" + nvCurVal);
            intent.putExtra("cur_value", nvCurVal);
        }
    }

    private static void broadcastRildDataInd(byte[] data, String packageName, int sub) {
        if (mContext == null) {
            log("broadcastRildDataInd, mContext is null");
            return;
        }
        if (data == null) {
            log("broadcastRildDataInd, data is null");
            return;
        }
        if (sub != 0 && 1 != sub) {
            log("sub is invalid, do nothing");
            return;
        }
        ByteBuffer byteBuf = ByteBuffer.wrap(data);
        byteBuf.order(ByteOrder.nativeOrder());
        int lenLeft = data.length;
        if (lenLeft < 4) {
            log("broadcastRildDataInd, (broadcastNameLen)lenLeft can not < 4, lenLeft = " + lenLeft);
            return;
        }
        int broadcastNameLen = byteBuf.getInt();
        int lenLeft2 = lenLeft - 4;
        if (lenLeft2 < broadcastNameLen || broadcastNameLen <= 0) {
            log("broadcastRildDataInd, lenLeft < broadcastNameLen || broadcastNameLen <= 0, broadcastNameLen = " + broadcastNameLen + ", lenLeft = " + lenLeft2);
            return;
        }
        byte[] byteBroadCastName = new byte[broadcastNameLen];
        for (int i = 0; i < broadcastNameLen; i++) {
            byteBroadCastName[i] = byteBuf.get();
        }
        int lenLeft3 = lenLeft2 - broadcastNameLen;
        ByteBuffer bufBroadCastName = ByteBuffer.wrap(byteBroadCastName);
        bufBroadCastName.order(ByteOrder.nativeOrder());
        String strBroadCastName = ModemUtils.byteBufferToString(bufBroadCastName);
        if (strBroadCastName == null) {
            log("broadcastRildDataInd, strBroadCastName is null, broadcastNameLen = " + broadcastNameLen + ", strBroadCastName = " + strBroadCastName + ", lenLeft = " + lenLeft3);
            return;
        }
        Intent intent = new Intent(strBroadCastName);
        intent.putExtra(CommenUtils.BROADCAST_SUB, sub);
        if (packageName != null) {
            intent.setPackage(packageName);
        }
        if (lenLeft3 > 0) {
            byte[] byteBroadCastContext = new byte[lenLeft3];
            byteBuf.get(byteBroadCastContext);
            broadcastDataCust(strBroadCastName, byteBroadCastContext, intent);
        }
        log("Broadcasting: broadcastNameLen = " + broadcastNameLen + ", strBroadCastName = " + strBroadCastName + ", sub = " + sub + ", bc len = " + lenLeft3 + ", packageName = " + packageName + ", mContext = " + mContext);
        mContext.sendBroadcast(intent);
    }

    public static byte[] broadcastRildDataIndIfneed(byte[] data, int sub) {
        int packageNameLen;
        byte[] dataNew = data;
        if (data == null) {
            log("rildIndHeadParse, data is null");
            return null;
        }
        ByteBuffer buffer = ModemUtils.getBuffer(dataNew);
        int lenLeft = dataNew.length;
        if (lenLeft < 4) {
            log("rildIndHeadParse, (verInd)lenLeft can not < 4, lenLeft = " + lenLeft + ", dataNew.length = " + dataNew.length + ", cnt = 0");
            return null;
        }
        int verInd = buffer.getInt();
        int cnt = 0 + 4;
        int lenLeft2 = lenLeft - 4;
        int bBcSend = 1;
        String packageName = null;
        if (100 != verInd) {
            if (verInd < 100) {
                log("verInd(" + verInd + "), old rildIndHeadParse, data.length = " + data.length);
            } else {
                log("verInd(" + verInd + "), not support, rildIndHeadParse do nothing, data.length = " + data.length);
                return null;
            }
        } else {
            if (lenLeft2 < 1) {
                log("rildIndHeadParse, (bBcSend)lenLeft can not < 1, lenLeft = " + lenLeft2 + ", dataNew.length = " + dataNew.length + ", cnt = " + cnt);
                return null;
            }
            bBcSend = buffer.get();
            int cnt2 = cnt + 1;
            int lenLeft3 = lenLeft2 - 1;
            if (lenLeft3 < 1) {
                log("rildIndHeadParse, (bPackageNameValid)lenLeft can not < 1, lenLeft = " + lenLeft3 + ", dataNew.length = " + dataNew.length + ", cnt = " + cnt2);
                return null;
            }
            int bPackageNameValid = buffer.get();
            int cnt3 = cnt2 + 1;
            int lenLeft4 = lenLeft3 - 1;
            if (1 != bPackageNameValid) {
                packageNameLen = 0;
            } else {
                if (lenLeft4 < 4) {
                    log("rildIndHeadParse, (packageNameLen)lenLeft can not < 4, lenLeft = " + lenLeft4 + ", dataNew.length = " + dataNew.length + ", cnt = " + cnt3);
                    return null;
                }
                packageNameLen = buffer.getInt();
                int cnt4 = cnt3 + 4;
                int lenLeft5 = lenLeft4 - 4;
                if (packageNameLen <= 0 || packageNameLen > 50) {
                    log("verInd(" + verInd + "), packageNameLen(" + packageNameLen + ") is invalid, should be in(0, 50], lenLeft = " + lenLeft5 + ", dataNew.length = " + dataNew.length + ", cnt = " + cnt4);
                    return null;
                }
                if (lenLeft5 < packageNameLen) {
                    log("rildIndHeadParse, (packageName)lenLeft can not < " + packageNameLen + ", lenLeft = " + lenLeft5 + ", dataNew.length = " + dataNew.length + ", cnt = " + cnt4);
                    return null;
                }
                byte[] bytes = new byte[packageNameLen];
                buffer.get(bytes);
                packageName = ModemUtils.bytesToString(bytes);
                cnt3 = cnt4 + packageNameLen;
                lenLeft4 = lenLeft5 - packageNameLen;
            }
            if (lenLeft4 <= 0) {
                log("rildIndHeadParse, (dataNew)lenLeft <= 0, lenLeft = " + lenLeft4 + ", dataNew.length = " + dataNew.length + ", cnt = " + cnt3);
                return null;
            }
            byte[] bytes2 = new byte[lenLeft4];
            buffer.get(bytes2);
            dataNew = bytes2;
            log("verInd(" + verInd + "), bPackageNameValid = " + bPackageNameValid + ", packageNameLen = " + packageNameLen + ", packageName = " + packageName + ", bBcSend = " + bBcSend + ", dataNew.length = " + dataNew.length + ", data.length = " + data.length + ", cnt = " + cnt3 + ", lenLeft = " + lenLeft4);
        }
        if (1 == bBcSend) {
            broadcastRildDataInd(dataNew, packageName, sub);
        }
        return dataNew;
    }

    public static void callModemIpcCallbackForRild(HashMap<ModemIpcCallbackAgent, ModemIpcCallbackInfo> mapForOemHookCallback, byte[] bytes, int sub) {
        int pid;
        int broadcastNameLen;
        byte[] byteBroadCastName;
        ByteBuffer bufBroadCastName;
        mapForOemHookCallback = mapForOemHookCallback;
        try {
            if (bytes == null) {
                log("callModemIpcCallbackForRild, bytes is null, do nothing");
                return;
            }
            if (mapForOemHookCallback == null) {
                log("callModemIpcCallbackForRild, mapForOemHookCallback is null, do nothing");
                return;
            }
            byte[] dataInd = bytes;
            int lenLeft = dataInd.length;
            ByteBuffer byteBuf = ByteBuffer.wrap(dataInd);
            byteBuf.order(ByteOrder.nativeOrder());
            if (lenLeft < 4) {
                log("callModemIpcCallbackForRild, lenLeft < 4, lenLeft = " + lenLeft);
                return;
            }
            int broadcastNameLen2 = byteBuf.getInt();
            int lenLeft2 = lenLeft - 4;
            if (lenLeft2 < broadcastNameLen2 || broadcastNameLen2 <= 0) {
                log("callModemIpcCallbackForRild, lenLeft < broadcastNameLen || broadcastNameLen <= 0, broadcastNameLen = " + broadcastNameLen2 + ", lenLeft = " + lenLeft2);
                return;
            }
            byte[] byteBroadCastName2 = new byte[broadcastNameLen2];
            for (int i = 0; i < broadcastNameLen2; i++) {
                byteBroadCastName2[i] = byteBuf.get();
            }
            int lenLeft3 = lenLeft2 - broadcastNameLen2;
            ByteBuffer bufBroadCastName2 = ByteBuffer.wrap(byteBroadCastName2);
            bufBroadCastName2.order(ByteOrder.nativeOrder());
            String bcName = ModemUtils.byteBufferToString(bufBroadCastName2);
            if (bcName == null) {
                log("callModemIpcCallbackForRild, bcName is null, broadcastNameLen = " + broadcastNameLen2 + ", bcName = " + bcName + ", lenLeft = " + lenLeft3);
                return;
            }
            byte[] byteBroadCastContext = new byte[1];
            if (lenLeft3 > 0) {
                byteBroadCastContext = new byte[lenLeft3];
                byteBuf.get(byteBroadCastContext);
            }
            log("callModemIpcCallbackForRild, bcName = " + bcName + ", mapForOemHookCallback = " + mapForOemHookCallback + ", dataInd.length = " + dataInd.length + ", sub = " + sub + ", mapForOemHookCallback.size = " + mapForOemHookCallback.size() + ", phone count = " + ModemUtils.getPhoneCount() + ", lenLeft = " + lenLeft3);
            Iterator<ModemIpcCallbackAgent> it = mapForOemHookCallback.keySet().iterator();
            while (it.hasNext()) {
                ModemIpcCallbackAgent callback = it.next();
                ModemIpcCallbackInfo cbInfo = mapForOemHookCallback.get(callback);
                if (cbInfo == null) {
                    log("request is null, continue, callback = " + callback);
                } else {
                    String bcNameNeed = cbInfo.mBcName;
                    if (!bcName.equals(bcNameNeed)) {
                        log("bcName is not needed, continue, bcName = " + bcName + ", bcNameNeed = " + bcNameNeed);
                    } else {
                        int subNeed = cbInfo.mSub;
                        if (sub == subNeed || subNeed >= ModemUtils.getPhoneCount()) {
                            byte[] dataInd2 = dataInd;
                            ByteBuffer buf = null;
                            int processFlagCb = -1;
                            if (cbInfo.mRequest == null) {
                                pid = -1;
                            } else {
                                buf = ModemUtils.getBuffer(cbInfo.mRequest);
                                processFlagCb = buf.getInt();
                                pid = buf.getInt();
                            }
                            ByteBuffer byteBuf2 = ByteBuffer.wrap(byteBroadCastContext);
                            ByteBuffer buf2 = buf;
                            byteBuf2.order(ByteOrder.nativeOrder());
                            int lenLeft4 = lenLeft3;
                            Iterator<ModemIpcCallbackAgent> it2 = it;
                            if (!QUALCOMM_HYDRA_DATA_IND_BC_NAME.equals(bcName)) {
                                broadcastNameLen = broadcastNameLen2;
                                byteBroadCastName = byteBroadCastName2;
                                bufBroadCastName = bufBroadCastName2;
                            } else {
                                int stats_client_id = byteBuf2.getInt();
                                byteBroadCastName = byteBroadCastName2;
                                if (buf2 == null) {
                                    log("QUALCOMM_HYDRA_DATA_IND_BC_NAME, buf is null, continue, callback = " + callback + ", stats_client_id = " + stats_client_id);
                                    broadcastNameLen = broadcastNameLen2;
                                    bufBroadCastName = bufBroadCastName2;
                                } else {
                                    bufBroadCastName = bufBroadCastName2;
                                    int stats_client_id_need = buf2.getInt();
                                    broadcastNameLen = broadcastNameLen2;
                                    if (stats_client_id == stats_client_id_need) {
                                        byteBroadCastContext = byteBroadCastContext;
                                        log("callModemIpcCallbackForRild, callback = " + callback + ", byteBroadCastContext = " + byteBroadCastContext + ", stats_client_id = " + stats_client_id + ", stats_client_id_need = " + stats_client_id_need + ", sub = " + sub);
                                    } else {
                                        String bcName2 = bcName;
                                        log("QUALCOMM_HYDRA_DATA_IND_BC_NAME, stats_client_id is not need, continue, callback = " + callback + ", stats_client_id = " + stats_client_id + ", stats_client_id_need = " + stats_client_id_need + ", sub = " + sub + ", cbInfo.mBcName = " + cbInfo.mBcName + ", cbInfo.mSub = " + cbInfo.mSub + ", cbInfo.mRequest = " + cbInfo.mRequest + ", processFlagCb = " + processFlagCb + ", pid = " + pid);
                                        bcName = bcName2;
                                        byteBroadCastContext = byteBroadCastContext;
                                    }
                                }
                                dataInd = dataInd2;
                                byteBroadCastName2 = byteBroadCastName;
                                lenLeft3 = lenLeft4;
                                it = it2;
                                bufBroadCastName2 = bufBroadCastName;
                                broadcastNameLen2 = broadcastNameLen;
                            }
                            bcName = bcName;
                            log("bcName is needed, continue, bcName = " + bcName + ", will call onModemIpcCallback, cbInfo.mBcName = " + cbInfo.mBcName + ", cbInfo.mSub = " + cbInfo.mSub + ", cbInfo.mRequest = " + cbInfo.mRequest + ", processFlagCb = " + processFlagCb + ", pid = " + pid);
                            callback.onModemIpcCallback(bcName, byteBroadCastContext, sub);
                            dataInd = dataInd2;
                            byteBroadCastName2 = byteBroadCastName;
                            lenLeft3 = lenLeft4;
                            it = it2;
                            bufBroadCastName2 = bufBroadCastName;
                            broadcastNameLen2 = broadcastNameLen;
                        } else {
                            log("sub is not needed, continue, sub = " + sub + ", subNeed = " + subNeed + ", getPhoneCount() = " + ModemUtils.getPhoneCount());
                        }
                    }
                }
                dataInd = dataInd;
            }
        } catch (Exception e) {
            log("callModemIpcCallbackForRild Exception happened: " + e);
            e.printStackTrace();
        }
    }

    public void onModemIpcCallback(String bcName, byte[] request, int sub) {
        Log.d(LOG_TAG, "onModemIpcCallback, bcName = " + bcName + ", request = " + request + ", sub = " + sub + ", mContext = " + mContext);
    }

    private static final void log(String msg) {
        ModemIpcLog.getInstance(mContext).log(LOG_TAG, msg);
    }
}
