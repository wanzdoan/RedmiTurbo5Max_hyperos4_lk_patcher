package com.ot.pubsub.i.a;

import android.text.TextUtils;
import com.ot.pubsub.c.d;
import com.ot.pubsub.util.i;
import com.ot.pubsub.util.j;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class b {
    public static final int a = 30000;
    public static final int b = 60000;
    public static final int c = 30000;
    public static final int d = 60000;
    private static String e = "HttpUtil";
    private static final String f = "UTF-8";
    private static final String g = "&";
    private static final String h = "=";
    private static final String i = "miui_sdkconfig_jafej!@#)(*e@!#";
    private static final String j = "POST";
    private static final String k = "GET";

    private b() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1 */
    /* JADX WARN: Type inference failed for: r0v2, types: [java.io.OutputStream] */
    /* JADX WARN: Type inference failed for: r0v3 */
    /* JADX WARN: Type inference failed for: r0v6 */
    /* JADX WARN: Type inference failed for: r11v1 */
    /* JADX WARN: Type inference failed for: r11v13 */
    /* JADX WARN: Type inference failed for: r11v14 */
    /* JADX WARN: Type inference failed for: r11v4, types: [java.io.OutputStream] */
    /* JADX WARN: Type inference failed for: r11v6 */
    /* JADX WARN: Type inference failed for: r11v7 */
    public static com.ot.pubsub.i.a a(String str, String str2, String str3) throws Throwable {
        HttpURLConnection httpURLConnection;
        ?? r11;
        InputStream inputStream;
        ?? r0;
        InputStream inputStream2;
        InputStream inputStream3 = null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            try {
                httpURLConnection.setConnectTimeout(60000);
                httpURLConnection.setReadTimeout(60000);
                httpURLConnection.setRequestMethod(j);
                httpURLConnection.setRequestProperty("accept", "*/*");
                httpURLConnection.setRequestProperty("connection", "Keep-Alive");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Authorization", str3);
                httpURLConnection.setDoOutput(true);
                byte[] bytes = str2.getBytes(f);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                try {
                    outputStream.write(bytes, 0, bytes.length);
                    outputStream.flush();
                    int responseCode = httpURLConnection.getResponseCode();
                    com.ot.pubsub.i.a aVar = new com.ot.pubsub.i.a();
                    aVar.d = responseCode;
                    if (responseCode != 200) {
                        inputStream2 = httpURLConnection.getErrorStream();
                    } else {
                        inputStream2 = httpURLConnection.getInputStream();
                    }
                    try {
                        aVar.a(new String(i.c(inputStream2), f));
                        if (aVar.d == 200) {
                            j.a(e, "POST 成功 publishResponse:" + aVar.toString());
                        } else {
                            j.b(e, "POST 失败 publishResponse:" + aVar.toString());
                        }
                        i.a(inputStream2);
                        i.a(outputStream);
                        i.a(httpURLConnection);
                        return aVar;
                    } catch (Exception e2) {
                        inputStream = inputStream2;
                        e = e2;
                        r0 = outputStream;
                        try {
                            j.b(e, "HttpUtils POST 上传异常", e.getMessage());
                            i.a(inputStream);
                            i.a((OutputStream) r0);
                            i.a(httpURLConnection);
                            return null;
                        } catch (Throwable th) {
                            th = th;
                            inputStream3 = inputStream;
                            r11 = r0;
                            i.a(inputStream3);
                            i.a((OutputStream) r11);
                            i.a(httpURLConnection);
                            throw th;
                        }
                    } catch (Throwable th2) {
                        inputStream3 = inputStream2;
                        th = th2;
                        r11 = outputStream;
                        i.a(inputStream3);
                        i.a((OutputStream) r11);
                        i.a(httpURLConnection);
                        throw th;
                    }
                } catch (Exception e3) {
                    e = e3;
                    r0 = outputStream;
                    inputStream = null;
                } catch (Throwable th3) {
                    th = th3;
                    r11 = outputStream;
                }
            } catch (Exception e4) {
                e = e4;
                inputStream = null;
                r0 = inputStream;
                j.b(e, "HttpUtils POST 上传异常", e.getMessage());
                i.a(inputStream);
                i.a((OutputStream) r0);
                i.a(httpURLConnection);
                return null;
            } catch (Throwable th4) {
                th = th4;
                r11 = 0;
            }
        } catch (Exception e5) {
            e = e5;
            httpURLConnection = null;
            inputStream = null;
        } catch (Throwable th5) {
            th = th5;
            httpURLConnection = null;
            r11 = 0;
        }
    }

    public static String a(String str, String str2, Map map) throws Throwable {
        String strA;
        HttpURLConnection httpURLConnection;
        OutputStream outputStream;
        InputStream inputStream;
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        InputStream inputStream2 = null;
        if (map == null) {
            strA = null;
        } else {
            try {
                strA = a(map, true);
            } catch (Exception e2) {
                e = e2;
                inputStream = null;
                httpURLConnection = null;
                outputStream = null;
                j.b(e, "HttpUtils POST 上传异常", e);
                i.a(inputStream);
                i.a(outputStream);
                i.a(httpURLConnection);
                return null;
            } catch (Throwable th) {
                th = th;
                httpURLConnection = null;
                outputStream = null;
                i.a(inputStream2);
                i.a(outputStream);
                i.a(httpURLConnection);
                throw th;
            }
        }
        httpURLConnection = (HttpURLConnection) new URL(str2).openConnection();
        try {
            httpURLConnection.setConnectTimeout(30000);
            httpURLConnection.setReadTimeout(30000);
            httpURLConnection.setRequestMethod(j);
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + str);
            httpURLConnection.setDoOutput(true);
            byte[] bytes = strA.getBytes(f);
            outputStream = httpURLConnection.getOutputStream();
            try {
                outputStream.write(bytes, 0, bytes.length);
                outputStream.flush();
                int responseCode = httpURLConnection.getResponseCode();
                j.a(e, "postGetToken responseData statusCode:" + responseCode);
                inputStream = httpURLConnection.getInputStream();
                try {
                    try {
                        String str3 = new String(i.c(inputStream), f);
                        i.a(inputStream);
                        i.a(outputStream);
                        i.a(httpURLConnection);
                        return str3;
                    } catch (Exception e3) {
                        e = e3;
                        j.b(e, "HttpUtils POST 上传异常", e);
                        i.a(inputStream);
                        i.a(outputStream);
                        i.a(httpURLConnection);
                        return null;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    inputStream2 = inputStream;
                    i.a(inputStream2);
                    i.a(outputStream);
                    i.a(httpURLConnection);
                    throw th;
                }
            } catch (Exception e4) {
                e = e4;
                inputStream = null;
            } catch (Throwable th3) {
                th = th3;
                i.a(inputStream2);
                i.a(outputStream);
                i.a(httpURLConnection);
                throw th;
            }
        } catch (Exception e5) {
            e = e5;
            inputStream = null;
            outputStream = null;
        } catch (Throwable th4) {
            th = th4;
            outputStream = null;
        }
    }

    public static String a(String str, Map map, boolean z) throws IOException {
        return a(j, str, map, z);
    }

    private static String a(String str, String str2, Map map, boolean z) throws Throwable {
        String strA;
        OutputStream outputStream;
        HttpURLConnection httpURLConnection;
        InputStream inputStream;
        String str3;
        InputStream inputStream2 = null;
        if (map == null) {
            strA = null;
        } else {
            try {
                strA = a(map, z);
            } catch (Exception e2) {
                e = e2;
                outputStream = null;
                httpURLConnection = null;
                inputStream = null;
                j.b(e, "HttpUtils POST 上传异常", e);
                i.a(inputStream);
                i.a(outputStream);
                i.a(httpURLConnection);
                return null;
            } catch (Throwable th) {
                th = th;
                outputStream = null;
                httpURLConnection = null;
                i.a(inputStream2);
                i.a(outputStream);
                i.a(httpURLConnection);
                throw th;
            }
        }
        if (!k.equals(str) || strA == null) {
            str3 = str2;
        } else {
            str3 = str2 + "? " + strA;
        }
        httpURLConnection = (HttpURLConnection) new URL(str3).openConnection();
        try {
            httpURLConnection.setConnectTimeout(30000);
            httpURLConnection.setReadTimeout(30000);
            try {
                try {
                    if (k.equals(str)) {
                        httpURLConnection.setRequestMethod(k);
                    } else {
                        if (j.equals(str) && strA != null) {
                            httpURLConnection.setRequestMethod(j);
                            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            httpURLConnection.setDoOutput(true);
                            byte[] bytes = strA.getBytes(f);
                            outputStream = httpURLConnection.getOutputStream();
                            try {
                                outputStream.write(bytes, 0, bytes.length);
                                outputStream.flush();
                            } catch (Exception e3) {
                                e = e3;
                                inputStream = null;
                                j.b(e, "HttpUtils POST 上传异常", e);
                                i.a(inputStream);
                                i.a(outputStream);
                                i.a(httpURLConnection);
                                return null;
                            } catch (Throwable th2) {
                                th = th2;
                                i.a(inputStream2);
                                i.a(outputStream);
                                i.a(httpURLConnection);
                                throw th;
                            }
                        }
                        int responseCode = httpURLConnection.getResponseCode();
                        inputStream = httpURLConnection.getInputStream();
                        byte[] bArrC = i.c(inputStream);
                        j.a(e, String.format("HttpUtils POST 上传成功 url: %s, code: %s", str2, Integer.valueOf(responseCode)));
                        String str4 = new String(bArrC, f);
                        i.a(inputStream);
                        i.a(outputStream);
                        i.a(httpURLConnection);
                        return str4;
                    }
                    byte[] bArrC2 = i.c(inputStream);
                    j.a(e, String.format("HttpUtils POST 上传成功 url: %s, code: %s", str2, Integer.valueOf(responseCode)));
                    String str5 = new String(bArrC2, f);
                    i.a(inputStream);
                    i.a(outputStream);
                    i.a(httpURLConnection);
                    return str5;
                } catch (Exception e4) {
                    e = e4;
                    j.b(e, "HttpUtils POST 上传异常", e);
                    i.a(inputStream);
                    i.a(outputStream);
                    i.a(httpURLConnection);
                    return null;
                }
            } catch (Throwable th3) {
                th = th3;
                inputStream2 = inputStream;
                i.a(inputStream2);
                i.a(outputStream);
                i.a(httpURLConnection);
                throw th;
            }
            outputStream = null;
            int responseCode2 = httpURLConnection.getResponseCode();
            inputStream = httpURLConnection.getInputStream();
        } catch (Exception e5) {
            e = e5;
            outputStream = null;
            inputStream = null;
        } catch (Throwable th4) {
            th = th4;
            outputStream = null;
        }
    }

    private static String a(Map map, boolean z) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry entry : map.entrySet()) {
            try {
                if (!TextUtils.isEmpty((CharSequence) entry.getKey())) {
                    if (sb.length() > 0) {
                        sb.append(g);
                    }
                    sb.append(URLEncoder.encode((String) entry.getKey(), f));
                    sb.append(h);
                    sb.append(URLEncoder.encode(entry.getValue() == null ? "null" : (String) entry.getValue(), f));
                }
            } catch (UnsupportedEncodingException unused) {
                j.b(e, "format params failed");
            }
        }
        if (z) {
            String strA = a(map);
            if (sb.length() > 0) {
                sb.append(g);
            }
            sb.append(URLEncoder.encode("sign", f));
            sb.append(h);
            sb.append(URLEncoder.encode(strA, f));
        }
        return sb.toString();
    }

    public static String a(Map map) {
        StringBuilder sb = new StringBuilder();
        if (map != null) {
            ArrayList arrayList = new ArrayList(map.keySet());
            Collections.sort(arrayList);
            int size = arrayList.size();
            int i2 = 0;
            while (i2 < size) {
                Object obj = arrayList.get(i2);
                i2++;
                String str = (String) obj;
                if (!TextUtils.isEmpty(str)) {
                    sb.append(str);
                    sb.append((String) map.get(str));
                }
            }
        }
        sb.append(i);
        return d.c(sb.toString());
    }
}
