package com.android.libraries.entitlement.http;

import com.android.libraries.entitlement.utils.DebugUtils;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class HttpCookieJar {
    private final List mCookies;

    private HttpCookieJar(List list) {
        this.mCookies = list;
    }

    public static HttpCookieJar parseSetCookieHeaders(List list) {
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.addAll(parseCookiesSafely((String) it.next()));
        }
        return new HttpCookieJar(Collections.unmodifiableList(arrayList));
    }

    public List toCookieHeaders() {
        ArrayList arrayList = new ArrayList();
        Iterator it = this.mCookies.iterator();
        while (it.hasNext()) {
            arrayList.add(removeObsoleteCookieAttributes((HttpCookie) it.next()).toString());
        }
        return Collections.unmodifiableList(arrayList);
    }

    private static List parseCookiesSafely(String str) {
        try {
            return HttpCookie.parse(str);
        } catch (IllegalArgumentException unused) {
            DebugUtils.logPii("Failed to parse cookie: " + str);
            return Collections.EMPTY_LIST;
        }
    }

    private static HttpCookie removeObsoleteCookieAttributes(HttpCookie httpCookie) {
        httpCookie.setDomain(null);
        httpCookie.setPath(null);
        httpCookie.setPortlist(null);
        return httpCookie;
    }
}
