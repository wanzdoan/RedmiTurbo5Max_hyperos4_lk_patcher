package com.android.phone.settings.sim;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.android.phone.settings.MultiSimInfoEditorActivity;
import java.util.Locale;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.app.AppCompatActivity;
import miuix.appcompat.app.floatingactivity.multiapp.MultiAppFloatingActivitySwitcher;

/* JADX INFO: loaded from: classes.dex */
public abstract class Utils$PhoneUtil {
    public static boolean isChinese() {
        return Locale.SIMPLIFIED_CHINESE.equals(Locale.getDefault());
    }

    public static void enterUri(Context context, Intent intent, String str) {
        try {
            Intent intent2 = new Intent(Intent.parseUri(str, 0));
            intent2.setMiuiFlags(intent.getMiuiFlags());
            intent2.addFlags(268435456);
            if (context instanceof AppCompatActivity) {
                MultiAppFloatingActivitySwitcher.configureFloatingService(intent2, (AppCompatActivity) context);
            }
            context.startActivity(intent2);
            Log.i("PhoneUtil", "Enter uri, " + str);
        } catch (Exception e) {
            Log.e("PhoneUtil", "Enter uri failed.", e);
        }
    }

    public static void enterSimEditor(Context context, Intent intent, int i) {
        try {
            Bundle bundle = new Bundle();
            SubscriptionManager.putSlotId(bundle, i);
            Intent intent2 = new Intent(context, (Class<?>) MultiSimInfoEditorActivity.class);
            intent2.setMiuiFlags(intent.getMiuiFlags());
            intent2.putExtras(bundle);
            if (context instanceof AppCompatActivity) {
                MultiAppFloatingActivitySwitcher.configureFloatingService(intent2, (AppCompatActivity) context);
            }
            context.startActivity(intent2);
            Log.i("PhoneUtil", "Enter sim editor.");
        } catch (Exception e) {
            Log.d("PhoneUtil", "Enter sim editor failed." + e);
        }
    }

    public static void enterESimManager(Context context, Intent intent, int i) {
        try {
            Intent intent2 = new Intent();
            intent2.setAction("android.telephony.euicc.action.MANAGE_EMBEDDED_SUBSCRIPTIONS");
            context.startActivity(intent2);
        } catch (Exception e) {
            Log.e("PhoneUtil", "Enter euicc manager failed.", e);
        }
    }
}
