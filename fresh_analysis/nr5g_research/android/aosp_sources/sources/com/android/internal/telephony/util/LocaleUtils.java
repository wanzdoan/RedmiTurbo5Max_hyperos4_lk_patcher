package com.android.internal.telephony.util;

import android.content.Context;
import android.icu.util.ULocale;
import android.text.TextUtils;
import com.android.internal.telephony.MccTable;
import com.android.telephony.Rlog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class LocaleUtils {
    public static Locale getLocaleFromMccMnc(Context context, int i, String str, String str2) {
        boolean zIsEmpty = TextUtils.isEmpty(str2);
        if (zIsEmpty) {
            str2 = defaultLanguageForMccMnc(i, str);
        }
        TextUtils.isEmpty(str);
        String strCountryCodeForMcc = MccTable.countryCodeForMcc(String.valueOf(i));
        Rlog.d("LocaleUtils", "getLocaleFromMccMnc(" + str2 + ", " + strCountryCodeForMcc + ", " + i + ", " + str);
        Locale localeForLanguageCountry = getLocaleForLanguageCountry(context, str2, strCountryCodeForMcc);
        if (localeForLanguageCountry != null || zIsEmpty) {
            return localeForLanguageCountry;
        }
        String strDefaultLanguageForMccMnc = defaultLanguageForMccMnc(i, str);
        Rlog.d("LocaleUtils", "[retry ] getLocaleFromMccMnc(" + strDefaultLanguageForMccMnc + ", " + strCountryCodeForMcc + ", " + i + ", " + str);
        return getLocaleForLanguageCountry(context, strDefaultLanguageForMccMnc, strCountryCodeForMcc);
    }

    private static Locale getLocaleForLanguageCountry(Context context, String str, String str2) {
        if (str == null) {
            Rlog.d("LocaleUtils", "getLocaleForLanguageCountry: skipping no language");
            return null;
        }
        if (str2 == null) {
            str2 = "";
        }
        Locale locale = new Locale(str, str2);
        try {
            ArrayList arrayList = new ArrayList(Arrays.asList(context.getAssets().getLocales()));
            arrayList.remove("ar-XB");
            arrayList.remove("en-XA");
            ArrayList arrayList2 = new ArrayList();
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                Locale localeForLanguageTag = Locale.forLanguageTag(((String) obj).replace('_', '-'));
                if (localeForLanguageTag != null && !"und".equals(localeForLanguageTag.getLanguage()) && !localeForLanguageTag.getLanguage().isEmpty() && !localeForLanguageTag.getCountry().isEmpty() && localeForLanguageTag.getLanguage().equals(locale.getLanguage())) {
                    if (localeForLanguageTag.getCountry().equals(locale.getCountry())) {
                        Rlog.d("LocaleUtils", "getLocaleForLanguageCountry: got perfect match: " + localeForLanguageTag.toLanguageTag());
                        return localeForLanguageTag;
                    }
                    arrayList2.add(localeForLanguageTag);
                }
            }
            if (arrayList2.isEmpty()) {
                Rlog.d("LocaleUtils", "getLocaleForLanguageCountry: no locales for language " + str);
                return null;
            }
            Locale localeLookupFallback = lookupFallback(locale, arrayList2);
            if (localeLookupFallback != null) {
                Rlog.d("LocaleUtils", "getLocaleForLanguageCountry: got a fallback match: " + localeLookupFallback.toLanguageTag());
                return localeLookupFallback;
            }
            if (!TextUtils.isEmpty(locale.getCountry()) && isTranslated(context, locale)) {
                Rlog.d("LocaleUtils", "getLocaleForLanguageCountry: target locale is translated: " + locale);
                return locale;
            }
            Rlog.d("LocaleUtils", "getLocaleForLanguageCountry: got language-only match: " + str);
            return (Locale) arrayList2.get(0);
        } catch (Exception e) {
            Rlog.d("LocaleUtils", "getLocaleForLanguageCountry: exception", e);
            return null;
        }
    }

    public static String defaultLanguageForMccMnc(int i, String str) {
        TextUtils.isEmpty(str);
        String strCountryCodeForMcc = MccTable.countryCodeForMcc(String.valueOf(i));
        if (strCountryCodeForMcc.isEmpty()) {
            Rlog.d("LocaleUtils", "defaultLanguageForMccMnc(" + i + ", " + str + "): no country for mcc");
            return null;
        }
        if ("in".equals(strCountryCodeForMcc)) {
            return "en";
        }
        String language = ULocale.addLikelySubtags(new ULocale("und", strCountryCodeForMcc)).getLanguage();
        Rlog.d("LocaleUtils", "defaultLanguageForMccMnc(" + i + ", " + str + "): country " + strCountryCodeForMcc + " uses " + language);
        return language;
    }

    private static boolean isTranslated(Context context, Locale locale) {
        ULocale uLocaleAddLikelySubtags = ULocale.addLikelySubtags(ULocale.forLocale(locale));
        String language = uLocaleAddLikelySubtags.getLanguage();
        String script = uLocaleAddLikelySubtags.getScript();
        for (String str : context.getAssets().getLocales()) {
            ULocale uLocaleAddLikelySubtags2 = ULocale.addLikelySubtags(new ULocale(str));
            if (language.equals(uLocaleAddLikelySubtags2.getLanguage()) && script.equals(uLocaleAddLikelySubtags2.getScript())) {
                return true;
            }
        }
        return false;
    }

    private static Locale lookupFallback(Locale locale, List list) {
        do {
            locale = MccTable.FALLBACKS.get(locale);
            if (locale == null) {
                return null;
            }
        } while (!list.contains(locale));
        return locale;
    }
}
