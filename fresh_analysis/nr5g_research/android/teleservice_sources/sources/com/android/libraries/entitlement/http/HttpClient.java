package com.android.libraries.entitlement.http;

import android.net.Network;
import android.text.TextUtils;
import android.util.Log;
import com.android.libraries.entitlement.ServiceEntitlementException;
import com.android.libraries.entitlement.utils.DebugUtils;
import com.android.libraries.entitlement.utils.StreamUtils;
import com.android.libraries.entitlement.utils.StringUtils;
import com.xiaomi.settingsdk.backup.data.KeyJsonSettingItem;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public class HttpClient {
    private ArrayList mHistory = new ArrayList();
    private boolean mSaveHistory;

    public HttpClient(boolean z) {
        this.mSaveHistory = z;
    }

    public HttpResponse request(HttpRequest httpRequest) throws ServiceEntitlementException {
        if (this.mSaveHistory) {
            this.mHistory.add(httpRequest.toString());
        }
        DebugUtils.logPii("HttpClient.request url: " + httpRequest.url());
        HttpURLConnection httpURLConnectionCreateConnection = createConnection(httpRequest);
        DebugUtils.logPii("HttpClient.request headers (partial): " + httpURLConnectionCreateConnection.getRequestProperties());
        try {
            try {
                if ("POST".equals(httpRequest.requestMethod())) {
                    DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnectionCreateConnection.getOutputStream());
                    try {
                        String strReplace = httpRequest.postData().toString().replace("\\/", "/");
                        dataOutputStream.write(strReplace.getBytes(StandardCharsets.UTF_8));
                        DebugUtils.logPii("HttpClient.request post data: ".concat(strReplace));
                        dataOutputStream.close();
                    } catch (Throwable th) {
                        try {
                            dataOutputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                }
                httpURLConnectionCreateConnection.connect();
                HttpResponse httpResponse = getHttpResponse(httpURLConnectionCreateConnection);
                Log.d("ServiceEntitlement", "HttpClient.response : " + httpResponse.toShortDebugString());
                if (this.mSaveHistory) {
                    this.mHistory.add(httpResponse.toString());
                }
                httpURLConnectionCreateConnection.disconnect();
                return httpResponse;
            } catch (Throwable th3) {
                httpURLConnectionCreateConnection.disconnect();
                throw th3;
            }
        } catch (IOException e) {
            throw new ServiceEntitlementException(31, "Connection error stream: " + StreamUtils.inputStreamToStringSafe(httpURLConnectionCreateConnection.getErrorStream()) + " IOException: " + e.toString(), e);
        }
    }

    private HttpURLConnection createConnection(HttpRequest httpRequest) throws ServiceEntitlementException {
        HttpURLConnection httpURLConnection;
        try {
            URL url = new URL(httpRequest.url());
            httpRequest.urlConnectionFactory();
            Network network = httpRequest.network();
            if (network != null) {
                httpURLConnection = (HttpURLConnection) network.openConnection(url);
            } else {
                httpURLConnection = (HttpURLConnection) url.openConnection();
            }
            httpURLConnection.setInstanceFollowRedirects(false);
            for (Map.Entry entry : httpRequest.requestProperties().entrySet()) {
                Iterator it = ((List) entry.getValue()).iterator();
                while (it.hasNext()) {
                    httpURLConnection.addRequestProperty((String) entry.getKey(), (String) it.next());
                }
            }
            httpURLConnection.setRequestMethod(httpRequest.requestMethod());
            TimeUnit timeUnit = TimeUnit.SECONDS;
            httpURLConnection.setConnectTimeout((int) timeUnit.toMillis(httpRequest.timeoutInSec()));
            httpURLConnection.setReadTimeout((int) timeUnit.toMillis(httpRequest.timeoutInSec()));
            if ("POST".equals(httpRequest.requestMethod())) {
                httpURLConnection.setDoOutput(true);
            }
            return httpURLConnection;
        } catch (IOException e) {
            throw new ServiceEntitlementException(30, "Configure connection failed!", e);
        }
    }

    private static HttpResponse getHttpResponse(HttpURLConnection httpURLConnection) throws ServiceEntitlementException {
        String response;
        HttpResponse.Builder builder = HttpResponse.builder();
        builder.setContentType(getContentType(httpURLConnection));
        try {
            int responseCode = httpURLConnection.getResponseCode();
            DebugUtils.logPii("HttpClient.response headers: " + httpURLConnection.getHeaderFields());
            if (responseCode != 200 && responseCode != 302) {
                throw new ServiceEntitlementException(31, responseCode, httpURLConnection.getHeaderField("Retry-After"), "Invalid connection response: " + responseCode);
            }
            builder.setResponseCode(responseCode);
            builder.setResponseMessage(StringUtils.nullToEmpty(httpURLConnection.getResponseMessage()));
            builder.setLocation(StringUtils.nullToEmpty(httpURLConnection.getHeaderField("Location")));
            builder.setCookies(getCookies(httpURLConnection));
            try {
                if (httpURLConnection.getResponseCode() == 302) {
                    response = "";
                } else {
                    response = readResponse(httpURLConnection);
                }
                DebugUtils.logPii("HttpClient.response body: " + response);
                builder.setBody(response);
                return builder.build();
            } catch (IOException e) {
                throw new ServiceEntitlementException(32, "Read response body/message failed!", e);
            }
        } catch (IOException e2) {
            throw new ServiceEntitlementException(31, "Read response code failed!", e2);
        }
    }

    private static String readResponse(URLConnection uRLConnection) throws IOException {
        InputStream inputStream = uRLConnection.getInputStream();
        try {
            String strInputStreamToStringSafe = StreamUtils.inputStreamToStringSafe(inputStream);
            if (inputStream != null) {
                inputStream.close();
            }
            return strInputStreamToStringSafe;
        } catch (Throwable th) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private static int getContentType(URLConnection uRLConnection) {
        String headerField = uRLConnection.getHeaderField("Content-Type");
        if (TextUtils.isEmpty(headerField)) {
            return -1;
        }
        if (headerField.contains("xml") || "text/vnd.wap.connectivity".equals(headerField)) {
            return 1;
        }
        return headerField.contains(KeyJsonSettingItem.TYPE) ? 0 : -1;
    }

    private static List getCookies(URLConnection uRLConnection) {
        List<String> list = uRLConnection.getHeaderFields().get("Set-Cookie");
        return list == null ? Collections.EMPTY_LIST : list;
    }
}
