package com.android.phone.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.aios.apptoolsdk.AppResult;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public abstract class CallRecordingTools {
    public static AppResult recordCall(Context context, String str) {
        if ("start".equals(str)) {
            return sendRecordCommand(context, true);
        }
        if ("stop".equals(str)) {
            return sendRecordCommand(context, false);
        }
        return AppResult.error("无效的 action 参数，支持：start（开始录音）/ stop（停止录音）");
    }

    private static AppResult sendRecordCommand(Context context, boolean z) {
        AppResult appResultError;
        final String string = UUID.randomUUID().toString();
        boolean z2 = true;
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final String[] strArr = {null};
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.tools.CallRecordingTools.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (string.equals(intent.getStringExtra("sid"))) {
                    strArr[0] = intent.getStringExtra("record_path");
                    countDownLatch.countDown();
                }
            }
        };
        context.registerReceiver(broadcastReceiver, new IntentFilter("com.android.action.RECORD_PATH"), 2);
        try {
            Intent intent = new Intent("com.android.incallui.action.RECORDER");
            intent.setPackage("com.android.incallui");
            intent.putExtra("record", z);
            intent.putExtra("sid", string);
            context.sendBroadcast(intent);
            Log.i("CallRecordingTools", "已发送录音控制广播，start=" + z + ", sid=" + string);
            if (!countDownLatch.await(4000L, TimeUnit.MILLISECONDS)) {
                Log.w("CallRecordingTools", "等待 InCallUI 回复超时");
                return AppResult.error(z ? "开始录音超时，请确认通话正在进行中" : "停止录音超时，请确认通话正在进行中");
            }
            String str = strArr[0];
            if (str == null || str.isEmpty()) {
                z2 = false;
            }
            Log.i("CallRecordingTools", "收到 InCallUI 回复，path=" + str);
            if (z) {
                return z2 ? AppResult.success("{\"status\":\"recording\"}") : AppResult.error("录音启动失败，当前可能没有通话或已在录音中");
            }
            if (z2) {
                appResultError = AppResult.success("{\"status\":\"stopped\",\"path\":\"" + ToolUtils.escapeJson(str) + "\"}");
            } else {
                appResultError = AppResult.error("停止录音失败，当前可能没有正在进行的录音");
            }
            return appResultError;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Log.e("CallRecordingTools", "录音操作被中断", e);
            return AppResult.error("录音操作被中断");
        } finally {
            context.unregisterReceiver(broadcastReceiver);
        }
    }
}
