package com.xiaomi.onetrack.g;

import android.text.TextUtils;
import com.xiaomi.onetrack.OneTrack;
import com.xiaomi.onetrack.d.f;
import com.xiaomi.onetrack.f.a;
import com.xiaomi.onetrack.util.ab;
import com.xiaomi.onetrack.util.o;
import com.xiaomi.onetrack.util.r;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public abstract class b {
    public static final String j = a();
    private static String k = "HttpUtil";

    private static String a() {
        try {
            return new String("miui_sdkconfig_jafej!@#)(*e@!#".getBytes(), "UTF-8");
        } catch (Exception e) {
            r.b(k, e.getMessage());
            return "";
        }
    }

    public static String a(String str, byte[] bArr) {
        HttpURLConnection httpURLConnection;
        OutputStream outputStream;
        InputStream inputStream;
        r.a(k, "doPost url=" + str + ", len=" + bArr.length);
        InputStream inputStream2 = null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            try {
                httpURLConnection.setConnectTimeout(30000);
                httpURLConnection.setReadTimeout(30000);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                a(httpURLConnection);
                httpURLConnection.setRequestProperty("Content-Type", "application/octet-stream");
                httpURLConnection.setRequestProperty("OT_SID", f.a().b()[1]);
                httpURLConnection.setRequestProperty("OT_ts", Long.toString(System.currentTimeMillis()));
                httpURLConnection.setRequestProperty("OT_net", c.a(a.b()).toString());
                httpURLConnection.setRequestProperty("OT_sender", a.e());
                httpURLConnection.setRequestProperty("OT_protocol", "3.0");
                outputStream = httpURLConnection.getOutputStream();
                try {
                    outputStream.write(bArr, 0, bArr.length);
                    outputStream.flush();
                    int responseCode = httpURLConnection.getResponseCode();
                    inputStream = httpURLConnection.getInputStream();
                    try {
                        try {
                            byte[] bArrB = o.b(inputStream);
                            r.a(k, String.format("HttpUtils POST 上传成功 url: %s, code: %s", str, Integer.valueOf(responseCode)));
                            String str2 = new String(bArrB, "UTF-8");
                            o.a(inputStream);
                            o.a(outputStream);
                            o.a(httpURLConnection);
                            return str2;
                        } catch (IOException e) {
                            e = e;
                            r.b(k, String.format("HttpUtils POST 上传失败, url: %s, error: %s", str, e.getMessage()));
                            o.a(inputStream);
                            o.a(outputStream);
                            o.a(httpURLConnection);
                            return null;
                        }
                    } catch (Throwable th) {
                        th = th;
                        inputStream2 = inputStream;
                        o.a(inputStream2);
                        o.a(outputStream);
                        o.a(httpURLConnection);
                        throw th;
                    }
                } catch (IOException e2) {
                    e = e2;
                    inputStream = null;
                } catch (Throwable th2) {
                    th = th2;
                    o.a(inputStream2);
                    o.a(outputStream);
                    o.a(httpURLConnection);
                    throw th;
                }
            } catch (IOException e3) {
                e = e3;
                outputStream = null;
                inputStream = outputStream;
                r.b(k, String.format("HttpUtils POST 上传失败, url: %s, error: %s", str, e.getMessage()));
                o.a(inputStream);
                o.a(outputStream);
                o.a(httpURLConnection);
                return null;
            } catch (Throwable th3) {
                th = th3;
                outputStream = null;
            }
        } catch (IOException e4) {
            e = e4;
            httpURLConnection = null;
            outputStream = null;
        } catch (Throwable th4) {
            th = th4;
            httpURLConnection = null;
            outputStream = null;
        }
    }

    private static void a(HttpURLConnection httpURLConnection) {
        OneTrack.HttpRequestProperty httpReqPropConnection;
        if (httpURLConnection == null || (httpReqPropConnection = OneTrack.getHttpReqPropConnection()) == null) {
            return;
        }
        try {
            r.a(k, String.format("setCustomRequestProperty->connection: %s", httpReqPropConnection.getType()));
            httpURLConnection.setRequestProperty("Connection", httpReqPropConnection.getType());
        } catch (Exception e) {
            r.b(k, "setCustomRequestProperty error: " + e.getMessage());
        }
    }

    public static String a(String str, Map map, boolean z) {
        return a("GET", str, map, z);
    }

    public static String b(String str, Map map, boolean z) {
        return a("POST", str, map, z);
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
            } catch (Exception e) {
                e = e;
                outputStream = null;
                httpURLConnection = null;
                inputStream = null;
                r.b(k, "HttpUtils callRequest failed, url: " + str2 + ", e: ", e);
                o.a(inputStream);
                o.a(outputStream);
                o.a(httpURLConnection);
                return null;
            } catch (Throwable th) {
                th = th;
                outputStream = null;
                httpURLConnection = null;
                o.a(inputStream2);
                o.a(outputStream);
                o.a(httpURLConnection);
                throw th;
            }
        }
        if (!"GET".equals(str) || strA == null) {
            str3 = str2;
        } else {
            str3 = str2 + "?" + strA;
        }
        httpURLConnection = (HttpURLConnection) new URL(str3).openConnection();
        try {
            httpURLConnection.setConnectTimeout(30000);
            httpURLConnection.setReadTimeout(30000);
            a(httpURLConnection);
            try {
                try {
                    if ("GET".equals(str)) {
                        httpURLConnection.setRequestMethod("GET");
                    } else {
                        if ("POST".equals(str) && strA != null) {
                            httpURLConnection.setRequestMethod("POST");
                            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            httpURLConnection.setDoOutput(true);
                            byte[] bytes = strA.getBytes("UTF-8");
                            outputStream = httpURLConnection.getOutputStream();
                            try {
                                outputStream.write(bytes, 0, bytes.length);
                                outputStream.flush();
                            } catch (Exception e2) {
                                e = e2;
                                inputStream = null;
                                r.b(k, "HttpUtils callRequest failed, url: " + str2 + ", e: ", e);
                                o.a(inputStream);
                                o.a(outputStream);
                                o.a(httpURLConnection);
                                return null;
                            } catch (Throwable th2) {
                                th = th2;
                                o.a(inputStream2);
                                o.a(outputStream);
                                o.a(httpURLConnection);
                                throw th;
                            }
                        }
                        int responseCode = httpURLConnection.getResponseCode();
                        inputStream = httpURLConnection.getInputStream();
                        byte[] bArrB = o.b(inputStream);
                        r.a(k, String.format("HttpUtils POST 上传成功 url: %s, code: %s", str2, Integer.valueOf(responseCode)));
                        String str4 = new String(bArrB, "UTF-8");
                        o.a(inputStream);
                        o.a(outputStream);
                        o.a(httpURLConnection);
                        return str4;
                    }
                    byte[] bArrB2 = o.b(inputStream);
                    r.a(k, String.format("HttpUtils POST 上传成功 url: %s, code: %s", str2, Integer.valueOf(responseCode)));
                    String str5 = new String(bArrB2, "UTF-8");
                    o.a(inputStream);
                    o.a(outputStream);
                    o.a(httpURLConnection);
                    return str5;
                } catch (Exception e3) {
                    e = e3;
                    r.b(k, "HttpUtils callRequest failed, url: " + str2 + ", e: ", e);
                    o.a(inputStream);
                    o.a(outputStream);
                    o.a(httpURLConnection);
                    return null;
                }
            } catch (Throwable th3) {
                th = th3;
                inputStream2 = inputStream;
                o.a(inputStream2);
                o.a(outputStream);
                o.a(httpURLConnection);
                throw th;
            }
            outputStream = null;
            int responseCode2 = httpURLConnection.getResponseCode();
            inputStream = httpURLConnection.getInputStream();
        } catch (Exception e4) {
            e = e4;
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
                        sb.append("&");
                    }
                    sb.append(URLEncoder.encode((String) entry.getKey(), "UTF-8"));
                    sb.append("=");
                    sb.append(URLEncoder.encode(entry.getValue() == null ? "null" : (String) entry.getValue(), "UTF-8"));
                }
            } catch (UnsupportedEncodingException unused) {
                r.b(k, "format params failed");
            }
        }
        if (z) {
            String strA = a(map);
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(URLEncoder.encode("sign", "UTF-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(strA, "UTF-8"));
        }
        return sb.toString();
    }

    public static String a(Map map) {
        StringBuilder sb = new StringBuilder();
        if (map != null) {
            ArrayList arrayList = new ArrayList(map.keySet());
            Collections.sort(arrayList);
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                String str = (String) obj;
                if (!TextUtils.isEmpty(str)) {
                    sb.append(str);
                    sb.append((String) map.get(str));
                }
            }
        }
        sb.append(j);
        return com.xiaomi.onetrack.d.d.c(sb.toString());
    }

    public static boolean a(com.xiaomi.onetrack.a.b.a aVar) throws Throwable {
        InputStream inputStream;
        if (aVar == null || TextUtils.isEmpty(aVar.c())) {
            r.a(k, "doGetAdMonitor response true ,monitor or url is null");
            return true;
        }
        String strC = aVar.c();
        String str = "";
        int i = 0;
        int responseCode = 0;
        HttpURLConnection httpURLConnection = null;
        while (i < 3) {
            try {
                try {
                    if (responseCode / 100 == 3) {
                        i++;
                        strC = httpURLConnection.getHeaderField("Location");
                        r.a(k, "redirect url is:" + strC);
                    }
                    HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(strC).openConnection();
                    try {
                        httpURLConnection2.setInstanceFollowRedirects(false);
                        httpURLConnection2.setRequestMethod("GET");
                        httpURLConnection2.setConnectTimeout(30000);
                        httpURLConnection2.setReadTimeout(30000);
                        responseCode = httpURLConnection2.getResponseCode();
                        r.a(k, "AdMonitor get 请求url:" + strC + "_ResponseCode：" + responseCode);
                        if (responseCode / 100 != 5 && responseCode / 100 != 4 && responseCode / 100 != 3) {
                            o.a(httpURLConnection2);
                            return true;
                        }
                        try {
                            InputStream errorStream = httpURLConnection2.getErrorStream();
                            if (errorStream != null) {
                                str = new String(o.b(errorStream), "UTF-8");
                            }
                        } catch (Throwable th) {
                            r.a(k, "doGetAdMonitor getErrorStream Throwable：" + th.getMessage());
                        }
                        try {
                            if (!ab.b(str) && (inputStream = httpURLConnection2.getInputStream()) != null) {
                                str = new String(o.b(inputStream), "UTF-8");
                            }
                        } catch (Throwable th2) {
                            r.a(k, "doGetAdMonitor error getInputStream Throwable：" + th2.getMessage());
                        }
                        r.a(k, "doGetAdMonitor get 请求 errBody：" + str);
                        com.xiaomi.onetrack.a.a.a().a(aVar, responseCode, str, (Throwable) null);
                        if (responseCode / 100 != 3) {
                            o.a(httpURLConnection2);
                            return false;
                        }
                        httpURLConnection = httpURLConnection2;
                    } catch (ProtocolException e) {
                        e = e;
                        httpURLConnection = httpURLConnection2;
                        com.xiaomi.onetrack.a.a.a().a(aVar, responseCode, str, e);
                        if (TextUtils.isEmpty(e.getMessage()) && e.getMessage().contains("200 OK")) {
                            r.a(k, "response code is 200, bug status line is invalid.");
                            o.a(httpURLConnection);
                            return true;
                        }
                        o.a(httpURLConnection);
                    } catch (Exception e2) {
                        e = e2;
                        httpURLConnection = httpURLConnection2;
                        r.b(k, "HttpUtils doGetAdMonitor 上传异常:" + e.getMessage());
                        com.xiaomi.onetrack.a.a.a().a(aVar, responseCode, str, e);
                        o.a(httpURLConnection);
                    } catch (Throwable th3) {
                        th = th3;
                        httpURLConnection = httpURLConnection2;
                        o.a(httpURLConnection);
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                }
            } catch (ProtocolException e3) {
                e = e3;
            } catch (Exception e4) {
                e = e4;
            }
        }
        r.a(k, "redirectCount >= 3, return true");
        o.a(httpURLConnection);
        return true;
    }

    public static d a(String str, String str2, String str3) throws Throwable {
        OutputStream outputStream;
        HttpURLConnection httpURLConnection;
        InputStream inputStream;
        OutputStream outputStream2;
        InputStream inputStream2;
        InputStream inputStream3 = null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            try {
                httpURLConnection.setConnectTimeout(60000);
                httpURLConnection.setReadTimeout(60000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("accept", "*/*");
                httpURLConnection.setRequestProperty("connection", "Keep-Alive");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Authorization", str3);
                httpURLConnection.setDoOutput(true);
                byte[] bytes = str2.getBytes("UTF-8");
                outputStream = httpURLConnection.getOutputStream();
                try {
                    outputStream.write(bytes, 0, bytes.length);
                    outputStream.flush();
                    int responseCode = httpURLConnection.getResponseCode();
                    d dVar = new d();
                    dVar.d = responseCode;
                    if (responseCode != 200) {
                        inputStream2 = httpURLConnection.getErrorStream();
                    } else {
                        inputStream2 = httpURLConnection.getInputStream();
                    }
                    try {
                        dVar.a(new String(o.b(inputStream2), "UTF-8"));
                        if (dVar.d == 200) {
                            r.a(k, "POST 成功 publishResponse:" + dVar.toString());
                        } else {
                            r.b(k, "POST 失败 publishResponse:" + dVar.toString());
                        }
                        o.a(inputStream2);
                        o.a(outputStream);
                        o.a(httpURLConnection);
                        return dVar;
                    } catch (Exception e) {
                        inputStream = inputStream2;
                        e = e;
                        outputStream2 = outputStream;
                        try {
                            r.b(k, "HttpUtils post url:" + str + " ," + e.getMessage());
                            o.a(inputStream);
                            o.a(outputStream2);
                            o.a(httpURLConnection);
                            return null;
                        } catch (Throwable th) {
                            th = th;
                            inputStream3 = inputStream;
                            outputStream = outputStream2;
                            o.a(inputStream3);
                            o.a(outputStream);
                            o.a(httpURLConnection);
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        inputStream3 = inputStream2;
                        o.a(inputStream3);
                        o.a(outputStream);
                        o.a(httpURLConnection);
                        throw th;
                    }
                } catch (Exception e2) {
                    e = e2;
                    outputStream2 = outputStream;
                    inputStream = null;
                } catch (Throwable th3) {
                    th = th3;
                }
            } catch (Exception e3) {
                e = e3;
                inputStream = null;
                outputStream2 = null;
            } catch (Throwable th4) {
                th = th4;
                outputStream = null;
            }
        } catch (Exception e4) {
            e = e4;
            inputStream = null;
            outputStream2 = null;
            httpURLConnection = null;
        } catch (Throwable th5) {
            th = th5;
            outputStream = null;
            httpURLConnection = null;
        }
    }

    public static String a(String str, String str2, Map map) throws Throwable {
        String strA;
        OutputStream outputStream;
        HttpURLConnection httpURLConnection;
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
            } catch (Exception e) {
                e = e;
                inputStream = null;
                outputStream = null;
                httpURLConnection = null;
                r.b(k, "HttpUtils post token:" + str2 + " ," + e.getMessage());
                o.a(inputStream);
                o.a(outputStream);
                o.a(httpURLConnection);
                return null;
            } catch (Throwable th) {
                th = th;
                outputStream = null;
                httpURLConnection = null;
                o.a(inputStream2);
                o.a(outputStream);
                o.a(httpURLConnection);
                throw th;
            }
        }
        httpURLConnection = (HttpURLConnection) new URL(str2).openConnection();
        try {
            httpURLConnection.setConnectTimeout(60000);
            httpURLConnection.setReadTimeout(60000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + str);
            httpURLConnection.setDoOutput(true);
            byte[] bytes = strA.getBytes("UTF-8");
            outputStream = httpURLConnection.getOutputStream();
            try {
                outputStream.write(bytes, 0, bytes.length);
                outputStream.flush();
                int responseCode = httpURLConnection.getResponseCode();
                r.a(k, "postGetToken responseData statusCode:" + responseCode);
                inputStream = httpURLConnection.getInputStream();
                try {
                    try {
                        String str3 = new String(o.b(inputStream), "UTF-8");
                        o.a(inputStream);
                        o.a(outputStream);
                        o.a(httpURLConnection);
                        return str3;
                    } catch (Exception e2) {
                        e = e2;
                        r.b(k, "HttpUtils post token:" + str2 + " ," + e.getMessage());
                        o.a(inputStream);
                        o.a(outputStream);
                        o.a(httpURLConnection);
                        return null;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    inputStream2 = inputStream;
                    o.a(inputStream2);
                    o.a(outputStream);
                    o.a(httpURLConnection);
                    throw th;
                }
            } catch (Exception e3) {
                e = e3;
                inputStream = null;
            } catch (Throwable th3) {
                th = th3;
                o.a(inputStream2);
                o.a(outputStream);
                o.a(httpURLConnection);
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            inputStream = null;
            outputStream = null;
        } catch (Throwable th4) {
            th = th4;
            outputStream = null;
        }
    }
}
