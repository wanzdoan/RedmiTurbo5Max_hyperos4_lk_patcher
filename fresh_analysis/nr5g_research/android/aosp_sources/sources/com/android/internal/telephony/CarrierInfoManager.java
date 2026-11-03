package com.android.internal.telephony;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.provider.Telephony;
import android.telephony.CarrierConfigManager;
import android.telephony.ImsiEncryptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import java.security.PublicKey;
import java.util.Date;

/* JADX INFO: loaded from: classes.dex */
public class CarrierInfoManager {
    private long mLastAccessResetCarrierKey = 0;

    /* JADX WARN: Code duplicated, block: B:93:0x01ba A[PHI: r11
  0x01ba: PHI (r11v5 android.database.Cursor) = (r11v4 android.database.Cursor), (r11v6 android.database.Cursor) binds: [B:92:0x01b8, B:95:0x01d2] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:99:0x01d8  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r8v0 */
    /* JADX WARN: Type inference failed for: r8v1, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r8v2 */
    public static ImsiEncryptionInfo getCarrierInfoForImsiEncryption(int i, Context context, String str, int i2, boolean z, int i3) throws Throwable {
        Cursor cursorQuery;
        String string;
        String str2;
        ?? r8 = 0;
        if (TextUtils.isEmpty(str)) {
            Log.e("CarrierInfoManager", "Invalid networkOperator: " + str);
            return null;
        }
        String strSubstring = str.substring(0, 3);
        String strSubstring2 = str.substring(3);
        String str3 = "using values for mcc, mnc: " + strSubstring + "," + strSubstring2;
        Log.i("CarrierInfoManager", str3);
        try {
            try {
                cursorQuery = context.getContentResolver().query(Telephony.CarrierColumns.CONTENT_URI, new String[]{"public_key", "expiration_time", "key_identifier", "carrier_id"}, "mcc=? and mnc=? and key_type=?", new String[]{strSubstring, strSubstring2, String.valueOf(i)}, null);
                int i4 = -1;
                if (cursorQuery != null) {
                    try {
                        if (cursorQuery.moveToFirst()) {
                            if (cursorQuery.getCount() > 1) {
                                Log.e("CarrierInfoManager", "More than 1 row found for the keyType: " + i);
                                while (cursorQuery.moveToNext()) {
                                    String string2 = cursorQuery.getString(3);
                                    int i5 = TextUtils.isEmpty(string2) ? -1 : Integer.parseInt(string2);
                                    if (i5 != -1 && i5 == i2) {
                                        ImsiEncryptionInfo imsiEncryptionInfo = getImsiEncryptionInfo(cursorQuery, strSubstring, strSubstring2, i, i5);
                                        cursorQuery.close();
                                        return imsiEncryptionInfo;
                                    }
                                }
                                cursorQuery.moveToFirst();
                            }
                            String string3 = cursorQuery.getString(3);
                            if (!TextUtils.isEmpty(string3)) {
                                i4 = Integer.parseInt(string3);
                            }
                            ImsiEncryptionInfo imsiEncryptionInfo2 = getImsiEncryptionInfo(cursorQuery, strSubstring, strSubstring2, i, i4);
                            cursorQuery.close();
                            return imsiEncryptionInfo2;
                        }
                    } catch (IllegalArgumentException e) {
                        e = e;
                        Log.e("CarrierInfoManager", "Bad arguments:" + e);
                        if (cursorQuery != null) {
                            cursorQuery.close();
                        }
                        return null;
                    } catch (Exception e2) {
                        e = e2;
                        Log.e("CarrierInfoManager", "Query failed:" + e);
                        if (cursorQuery != null) {
                            cursorQuery.close();
                        }
                        return null;
                    }
                }
                Log.d("CarrierInfoManager", "No rows found for keyType: " + i);
                if (!z) {
                    Log.d("CarrierInfoManager", "Skipping fallback logic");
                    if (cursorQuery != null) {
                        cursorQuery.close();
                    }
                    return null;
                }
                CarrierConfigManager carrierConfigManager = (CarrierConfigManager) context.getSystemService("carrier_config");
                if (carrierConfigManager == null) {
                    Log.d("CarrierInfoManager", "Could not get CarrierConfigManager for backup key");
                    if (cursorQuery != null) {
                        cursorQuery.close();
                    }
                    return null;
                }
                if (i3 == -1) {
                    Log.d("CarrierInfoManager", "Could not get carrier config with invalid subId");
                    if (cursorQuery != null) {
                        cursorQuery.close();
                    }
                    return null;
                }
                PersistableBundle configForSubId = carrierConfigManager.getConfigForSubId(i3);
                if (configForSubId == null) {
                    Log.d("CarrierInfoManager", "Could not get carrier config bundle for backup key");
                    if (cursorQuery != null) {
                        cursorQuery.close();
                    }
                    return null;
                }
                int i6 = configForSubId.getInt("imsi_key_availability_int");
                if (!CarrierKeyDownloadManager.isKeyEnabled(i, i6)) {
                    Log.d("CarrierInfoManager", "Backup key does not have matching keyType. keyType=" + i + " keyAvailability=" + i6);
                    if (cursorQuery != null) {
                        cursorQuery.close();
                    }
                    return null;
                }
                if (i == 1) {
                    string = configForSubId.getString("imsi_carrier_public_key_epdg_string");
                    str2 = "backup_key_from_carrier_config_epdg";
                } else if (i == 2) {
                    string = configForSubId.getString("imsi_carrier_public_key_wlan_string");
                    str2 = "backup_key_from_carrier_config_wlan";
                } else {
                    string = null;
                    str2 = null;
                }
                if (!TextUtils.isEmpty(string)) {
                    Pair<PublicKey, Long> keyInformation = CarrierKeyDownloadManager.getKeyInformation(string.getBytes());
                    ImsiEncryptionInfo imsiEncryptionInfo3 = new ImsiEncryptionInfo(strSubstring, strSubstring2, i, str2, (PublicKey) keyInformation.first, new Date(((Long) keyInformation.second).longValue()), i2);
                    if (cursorQuery != null) {
                        cursorQuery.close();
                    }
                    return imsiEncryptionInfo3;
                }
                Log.d("CarrierInfoManager", "Could not get carrier config key string for backup key. keyType=" + i);
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
                return null;
            } catch (Throwable th) {
                th = th;
                r8 = str3;
                if (r8 != 0) {
                    r8.close();
                }
                throw th;
            }
        } catch (IllegalArgumentException e3) {
            e = e3;
            cursorQuery = null;
        } catch (Exception e4) {
            e = e4;
            cursorQuery = null;
        } catch (Throwable th2) {
            th = th2;
            if (r8 != 0) {
                r8.close();
            }
            throw th;
        }
    }

    private static ImsiEncryptionInfo getImsiEncryptionInfo(Cursor cursor, String str, String str2, int i, int i2) {
        try {
            return new ImsiEncryptionInfo(str, str2, i, cursor.getString(2), cursor.getBlob(0), new Date(cursor.getLong(1)), i2);
        } catch (Exception e) {
            Log.e("CarrierInfoManager", "Exception = " + e.getMessage());
            return null;
        }
    }

    public static void updateOrInsertCarrierKey(ImsiEncryptionInfo imsiEncryptionInfo, Context context, int i) {
        byte[] encoded = imsiEncryptionInfo.getPublicKey().getEncoded();
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mcc", imsiEncryptionInfo.getMcc());
        contentValues.put("mnc", imsiEncryptionInfo.getMnc());
        contentValues.put("carrier_id", Integer.valueOf(imsiEncryptionInfo.getCarrierId()));
        contentValues.put("key_type", Integer.valueOf(imsiEncryptionInfo.getKeyType()));
        contentValues.put("key_identifier", imsiEncryptionInfo.getKeyIdentifier());
        contentValues.put("public_key", encoded);
        contentValues.put("expiration_time", Long.valueOf(imsiEncryptionInfo.getExpirationTime().getTime()));
        try {
            Log.i("CarrierInfoManager", "Inserting imsiEncryptionInfo into db");
            contentResolver.insert(Telephony.CarrierColumns.CONTENT_URI, contentValues);
        } catch (SQLiteConstraintException unused) {
            Log.i("CarrierInfoManager", "Insert failed, updating imsiEncryptionInfo into db");
            ContentValues contentValues2 = new ContentValues();
            contentValues2.put("public_key", encoded);
            contentValues2.put("expiration_time", Long.valueOf(imsiEncryptionInfo.getExpirationTime().getTime()));
            contentValues2.put("key_identifier", imsiEncryptionInfo.getKeyIdentifier());
            try {
                if (contentResolver.update(Telephony.CarrierColumns.CONTENT_URI, contentValues2, "mcc=? and mnc=? and key_type=? and carrier_id=?", new String[]{imsiEncryptionInfo.getMcc(), imsiEncryptionInfo.getMnc(), String.valueOf(imsiEncryptionInfo.getKeyType()), String.valueOf(imsiEncryptionInfo.getCarrierId())}) == 0) {
                    Log.d("CarrierInfoManager", "Error updating values:" + imsiEncryptionInfo);
                }
            } catch (Exception e) {
                Log.d("CarrierInfoManager", "Error updating values:" + imsiEncryptionInfo + e);
            }
        } catch (Exception e2) {
            Log.d("CarrierInfoManager", "Error inserting/updating values:" + imsiEncryptionInfo + e2);
        }
    }

    public static void setCarrierInfoForImsiEncryption(ImsiEncryptionInfo imsiEncryptionInfo, Context context, int i) {
        Log.i("CarrierInfoManager", "inserting carrier key: " + imsiEncryptionInfo);
        updateOrInsertCarrierKey(imsiEncryptionInfo, context, i);
    }

    public void resetCarrierKeysForImsiEncryption(Context context, int i, boolean z) {
        Log.i("CarrierInfoManager", "resetting carrier key, forceResetAll = " + z);
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (!z) {
            if (jCurrentTimeMillis - this.mLastAccessResetCarrierKey < 43200000) {
                Log.i("CarrierInfoManager", "resetCarrierKeysForImsiEncryption: Access rate exceeded");
                return;
            }
            this.mLastAccessResetCarrierKey = jCurrentTimeMillis;
        }
        int subscriptionId = SubscriptionManager.getSubscriptionId(i);
        if (!SubscriptionManager.isValidSubscriptionId(subscriptionId)) {
            Log.e("CarrierInfoManager", "Could not reset carrier keys, subscription for mPhoneId=" + i);
        } else {
            TelephonyManager telephonyManagerCreateForSubscriptionId = ((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(subscriptionId);
            deleteCarrierInfoForImsiEncryption(context, subscriptionId, telephonyManagerCreateForSubscriptionId.getSimCarrierId(), telephonyManagerCreateForSubscriptionId.getSimOperator());
            Intent intent = new Intent("com.android.internal.telephony.ACTION_CARRIER_CERTIFICATE_DOWNLOAD");
            SubscriptionManager.putPhoneIdAndSubIdExtra(intent, i);
            context.sendBroadcastAsUser(intent, UserHandle.ALL);
        }
    }

    public static void deleteCarrierInfoForImsiEncryption(Context context, int i, int i2) {
        deleteCarrierInfoForImsiEncryption(context, i, i2, null);
    }

    public static void deleteCarrierInfoForImsiEncryption(Context context, int i, int i2, String str) {
        Log.i("CarrierInfoManager", "deleting carrier key from db for subId=" + i);
        if (TextUtils.isEmpty(str)) {
            str = ((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(i).getSimOperator();
        }
        if (!TextUtils.isEmpty(str)) {
            String strSubstring = str.substring(0, 3);
            String strSubstring2 = str.substring(3);
            String strValueOf = String.valueOf(i2);
            try {
                Log.i("CarrierInfoManager", "Deleting the number of entries = " + context.getContentResolver().delete(Telephony.CarrierColumns.CONTENT_URI, "mcc=? and mnc=? and carrier_id=?", new String[]{strSubstring, strSubstring2, strValueOf}) + "   for carrierId = " + strValueOf);
                return;
            } catch (Exception e) {
                Log.e("CarrierInfoManager", "Delete failed" + e);
                return;
            }
        }
        Log.e("CarrierInfoManager", "Invalid networkOperator: " + str);
    }

    public static void deleteAllCarrierKeysForImsiEncryption(Context context) {
        Log.i("CarrierInfoManager", "deleting ALL carrier keys from db");
        try {
            context.getContentResolver().delete(Telephony.CarrierColumns.CONTENT_URI, null, null);
        } catch (Exception e) {
            Log.e("CarrierInfoManager", "Delete failed" + e);
        }
    }
}
