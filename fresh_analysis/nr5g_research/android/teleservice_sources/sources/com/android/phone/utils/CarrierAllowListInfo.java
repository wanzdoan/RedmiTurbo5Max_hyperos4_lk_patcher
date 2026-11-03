package com.android.phone.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Binder;
import android.telephony.Rlog;
import android.text.TextUtils;
import com.android.internal.telephony.uicc.IccUtils;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class CarrierAllowListInfo {
    private static CarrierAllowListInfo mInstance;
    private Context mContext;
    private JSONObject mDataJSON;

    private CarrierAllowListInfo(Context context) {
        this.mContext = context;
        loadJsonFile(context);
    }

    public static CarrierAllowListInfo loadInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CarrierAllowListInfo(context);
        }
        return mInstance;
    }

    public Set validateCallerAndGetCarrierIds(String str) {
        CarrierInfo jsonForCallerInfo = parseJsonForCallerInfo(str);
        return (jsonForCallerInfo == null || !validateCallerSignature(this.mContext, str, jsonForCallerInfo.getSHAIdList())) ? Collections.singleton(-1) : jsonForCallerInfo.getCallerCarrierIdList();
    }

    private void loadJsonFile(Context context) {
        try {
            String jsonFromAssets = getJsonFromAssets(context, "CarrierRestrictionOperatorDetails.json", "UTF-8");
            if (TextUtils.isEmpty(jsonFromAssets)) {
                return;
            }
            this.mDataJSON = new JSONObject(jsonFromAssets);
        } catch (Exception e) {
            Rlog.e("CarrierAllowListInfo", "CarrierAllowListInfo: JSON file reading exception = " + e);
        }
    }

    private CarrierInfo parseJsonForCallerInfo(String str) {
        try {
            JSONObject jSONObject = this.mDataJSON;
            if (jSONObject == null || str == null) {
                return null;
            }
            JSONObject jSONObject2 = jSONObject.getJSONObject(str.trim());
            JSONArray jSONArray = jSONObject2.getJSONArray("callerSHA256Ids");
            JSONArray jSONArray2 = jSONObject2.getJSONArray("carrierIds");
            HashSet hashSet = new HashSet();
            for (int i = 0; i < jSONArray2.length(); i++) {
                hashSet.add(Integer.valueOf(jSONArray2.getInt(i)));
            }
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                arrayList.add((String) jSONArray.get(i2));
            }
            return new CarrierInfo(hashSet, arrayList);
        } catch (JSONException e) {
            Rlog.e("CarrierAllowListInfo", "getCallerSignatureInfo: JSONException = " + e);
            return null;
        }
    }

    private static String getJsonFromAssets(Context context, String str, String str2) {
        try {
            InputStream inputStreamOpen = context.getAssets().open(str);
            byte[] bArr = new byte[inputStreamOpen.available()];
            inputStreamOpen.read(bArr);
            inputStreamOpen.close();
            return new String(bArr, str2);
        } catch (IOException e) {
            Rlog.e("CarrierAllowListInfo", "getJsonFromAssets: Exception = " + e);
            return null;
        }
    }

    public static boolean validateCallerSignature(Context context, String str, List list) {
        if (!TextUtils.isEmpty(str) && list.size() != 0) {
            context.getPackageManager();
            PackageManager packageManager = context.createContextAsUser(Binder.getCallingUserHandle(), 0).getPackageManager();
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                for (Signature signature : packageManager.getPackageInfo(str, 64).signatures) {
                    if (!list.contains(IccUtils.bytesToHexString(messageDigest.digest(signature.toByteArray())))) {
                        return false;
                    }
                }
                return true;
            } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
                Rlog.e("CarrierAllowListInfo", "validateCallerSignature: Exception = " + e);
            }
        }
        return false;
    }

    public int updateJsonForTest(String str) {
        try {
            if (str == null) {
                loadJsonFile(this.mContext);
                return 0;
            }
            this.mDataJSON = new JSONObject(str);
            return 0;
        } catch (JSONException e) {
            Rlog.e("CarrierAllowListInfo", "updateJsonForTest: Exception = " + e);
            return -1;
        }
    }

    class CarrierInfo {
        private final Set mCallerCarrierIdList;
        private final List mSHAIdList;

        public CarrierInfo(Set set, List list) {
            this.mCallerCarrierIdList = set;
            this.mSHAIdList = list;
        }

        public Set getCallerCarrierIdList() {
            return this.mCallerCarrierIdList;
        }

        public List getSHAIdList() {
            return this.mSHAIdList;
        }
    }

    public List getShaIdList(String str, int i) {
        CarrierInfo jsonForCallerInfo = parseJsonForCallerInfo(str);
        if (jsonForCallerInfo != null && jsonForCallerInfo.getCallerCarrierIdList().contains(Integer.valueOf(i))) {
            return jsonForCallerInfo.getSHAIdList();
        }
        Rlog.e("CarrierAllowListInfo", "getShaIdList: carrierId or shaIdList is empty");
        return Collections.EMPTY_LIST;
    }
}
