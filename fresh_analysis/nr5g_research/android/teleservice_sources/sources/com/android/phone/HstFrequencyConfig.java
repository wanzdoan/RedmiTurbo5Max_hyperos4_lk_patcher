package com.android.phone;

import android.database.Cursor;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class HstFrequencyConfig {
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0 */
    /* JADX WARN: Type inference failed for: r1v1 */
    /* JADX WARN: Type inference failed for: r1v2, types: [android.database.Cursor] */
    public static List getFrequencyData(String str, String str2, boolean z) {
        Cursor hstFrequencyData;
        ?? r1 = 0;
        if (str != null) {
            try {
                if (str2 != null) {
                    try {
                        CloudCellDbHelper dbHelper = getDbHelper();
                        if (dbHelper == null) {
                            return null;
                        }
                        hstFrequencyData = dbHelper.getHstFrequencyData(str, str2, !z ? 1 : 0);
                        if (hstFrequencyData != null) {
                            try {
                                if (hstFrequencyData.getCount() != 0) {
                                    ArrayList arrayList = new ArrayList();
                                    int columnIndex = hstFrequencyData.getColumnIndex("mcc");
                                    int columnIndex2 = hstFrequencyData.getColumnIndex("mnc");
                                    int columnIndex3 = hstFrequencyData.getColumnIndex("band");
                                    int columnIndex4 = hstFrequencyData.getColumnIndex("arfcn");
                                    if (columnIndex >= 0 && columnIndex2 >= 0 && columnIndex3 >= 0 && columnIndex4 >= 0) {
                                        while (hstFrequencyData.moveToNext()) {
                                            String string = hstFrequencyData.getString(columnIndex);
                                            String string2 = hstFrequencyData.getString(columnIndex2);
                                            String string3 = hstFrequencyData.getString(columnIndex3);
                                            String string4 = hstFrequencyData.getString(columnIndex4);
                                            if (string != null && string2 != null && string3 != null && string4 != null) {
                                                arrayList.add(List.of(string, string2, string3, string4));
                                            }
                                        }
                                        ArrayList arrayList2 = arrayList.isEmpty() ? null : arrayList;
                                        try {
                                            hstFrequencyData.close();
                                        } catch (Exception unused) {
                                        }
                                        return arrayList2;
                                    }
                                    Log.e("HstFrequencyConfig", "getFrequencyData column index invalid");
                                    try {
                                        hstFrequencyData.close();
                                    } catch (Exception unused2) {
                                    }
                                    return null;
                                }
                            } catch (Exception e) {
                                e = e;
                            }
                        }
                        if (hstFrequencyData != null) {
                            try {
                                hstFrequencyData.close();
                            } catch (Exception unused3) {
                            }
                        }
                        return null;
                    } catch (Exception e2) {
                        e = e2;
                        hstFrequencyData = null;
                    } catch (Throwable th) {
                        th = th;
                        if (r1 != 0) {
                            try {
                                r1.close();
                            } catch (Exception unused4) {
                            }
                        }
                        throw th;
                    }
                    Log.e("HstFrequencyConfig", "getFrequencyData error", e);
                    if (hstFrequencyData != null) {
                        try {
                            hstFrequencyData.close();
                        } catch (Exception unused5) {
                        }
                    }
                    return null;
                }
            } catch (Throwable th2) {
                th = th2;
                r1 = str;
            }
        }
        return null;
    }

    private static CloudCellDbHelper getDbHelper() {
        try {
            return CloudCellDbHelper.getInstance(PhoneGlobals.getInstance());
        } catch (Exception e) {
            Log.e("HstFrequencyConfig", "getDbHelper error", e);
            return null;
        }
    }
}
