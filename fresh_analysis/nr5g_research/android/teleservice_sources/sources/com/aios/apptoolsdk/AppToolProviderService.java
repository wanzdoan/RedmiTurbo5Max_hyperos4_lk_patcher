package com.aios.apptoolsdk;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.aios.apptoolsdk.aidl.IAppToolProvider;
import com.android.services.telephony.relay.phonecall.message.response.ResponseObject;
import com.ot.pubsub.a.a;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class AppToolProviderService extends Service {
    private final Map toolMap = new HashMap();
    private final IAppToolProvider.Stub binder = new IAppToolProvider.Stub() { // from class: com.aios.apptoolsdk.AppToolProviderService.1
        @Override // com.aios.apptoolsdk.aidl.IAppToolProvider
        public String handleRequest(String str) {
            AppResult appResultError;
            try {
                if (!AppToolProviderService.this.verifyCallerOrDebug()) {
                    return AppToolProviderService.this.buildErrorResponse(0, -32600, "Caller not authorized: requires system signature");
                }
                JSONObject jSONObject = new JSONObject(str);
                int iOptInt = jSONObject.optInt("id", 0);
                String strOptString = jSONObject.optString("method", "");
                JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("params");
                if (strOptString.hashCode() == 498659858 && strOptString.equals("tools/call")) {
                    if (jSONObjectOptJSONObject == null) {
                        return AppToolProviderService.this.buildErrorResponse(iOptInt, -32602, "缺少 params");
                    }
                    String strOptString2 = jSONObjectOptJSONObject.optString("name", null);
                    if (strOptString2 == null) {
                        return AppToolProviderService.this.buildErrorResponse(iOptInt, -32602, "缺少 tool name");
                    }
                    JSONObject jSONObjectOptJSONObject2 = jSONObjectOptJSONObject.optJSONObject("arguments");
                    if (jSONObjectOptJSONObject2 == null) {
                        jSONObjectOptJSONObject2 = new JSONObject();
                    }
                    AppTool appTool = (AppTool) AppToolProviderService.this.toolMap.get(strOptString2);
                    if (appTool == null) {
                        return AppToolProviderService.this.buildErrorResponse(iOptInt, -32602, "未知工具: ".concat(strOptString2));
                    }
                    long jClearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        try {
                            appResultError = appTool.execute(new ToolArgs(jSONObjectOptJSONObject2));
                        } catch (Exception e) {
                            Log.e("AppToolProvider", "工具 " + strOptString2 + " 执行异常", e);
                            StringBuilder sb = new StringBuilder("执行异常: ");
                            sb.append(e.getMessage());
                            appResultError = AppResult.error(sb.toString());
                        }
                        AppToolProviderService appToolProviderService = AppToolProviderService.this;
                        return appToolProviderService.buildResponse(iOptInt, appToolProviderService.buildCallResult(appResultError));
                    } finally {
                        Binder.restoreCallingIdentity(jClearCallingIdentity);
                    }
                }
                return AppToolProviderService.this.buildErrorResponse(iOptInt, -32601, "Method not found: ".concat(strOptString));
            } catch (Exception e2) {
                Log.e("AppToolProvider", "处理请求异常", e2);
                return AppToolProviderService.this.buildErrorResponse(0, -32603, "Internal error: " + e2.getMessage());
            }
        }
    };

    protected abstract List onCreateTools();

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        List<AppTool> listOnCreateTools = onCreateTools();
        for (AppTool appTool : listOnCreateTools) {
            this.toolMap.put(appTool.getName(), appTool);
        }
        Log.i("AppToolProvider", "已注册 " + listOnCreateTools.size() + " 个工具");
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        if (!verifyCallerOrDebug()) {
            Log.w("AppToolProvider", "onBind 拒绝：调用方不具备系统签名");
            return null;
        }
        Log.i("AppToolProvider", "Service bound, action=" + intent.getAction());
        return this.binder;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean verifyCallerOrDebug() {
        if ((getApplicationInfo().flags & 2) != 0) {
            return true;
        }
        return isCallerSystemSigned();
    }

    private boolean isCallerSystemSigned() {
        int callingUid = Binder.getCallingUid();
        PackageManager packageManager = getPackageManager();
        String[] packagesForUid = packageManager.getPackagesForUid(callingUid);
        return (packagesForUid == null || packagesForUid.length == 0 || packageManager.checkSignatures("android", packagesForUid[0]) != 0) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public JSONObject buildCallResult(AppResult appResult) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("type", "text");
        jSONObject.put("text", appResult.getText());
        JSONArray jSONArray = new JSONArray();
        jSONArray.put(jSONObject);
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put("content", jSONArray);
        jSONObject2.put("isError", appResult.isError());
        return jSONObject2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String buildResponse(int i, JSONObject jSONObject) {
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("jsonrpc", "2.0");
            jSONObject2.put("id", i);
            jSONObject2.put(a.L, jSONObject);
            return jSONObject2.toString();
        } catch (Exception e) {
            return buildErrorResponse(i, -32603, "JSON build error: " + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String buildErrorResponse(int i, int i2, String str) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(com.ot.pubsub.i.a.a.d, i2);
            jSONObject.put(a.m, str);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("jsonrpc", "2.0");
            jSONObject2.put("id", i);
            jSONObject2.put(ResponseObject.DEFAULT_MESSAGE_FAILED, jSONObject);
            return jSONObject2.toString();
        } catch (Exception unused) {
            return "{\"jsonrpc\":\"2.0\",\"id\":" + i + ",\"error\":{\"code\":-32603,\"message\":\"Fatal error\"}}";
        }
    }
}
