package com.android.phone.settings.sim;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.android.phone.MiuiPhoneUtils;
import miui.os.Build;
import miuix.appcompat.app.AppCompatActivity;
import miuix.appcompat.app.floatingactivity.multiapp.MultiAppFloatingActivitySwitcher;

/* JADX INFO: loaded from: classes.dex */
public abstract class Utils$VirtualSim {
    public static void enterContactBusinessHall(Context context, Intent intent, int i) {
        try {
            Intent intent2 = new Intent();
            intent2.setPackage("com.android.contacts");
            intent2.setAction("com.mobile.businesshall.ACTION_ROUTER");
            intent2.putExtra("slotId", i);
            intent2.putExtra("launchfrom", "multisimsetting");
            intent2.addFlags(268435456);
            context.startActivity(intent2);
            Log.i("VirtualSimUtil", "Enter Contact BusinessHall hall.");
        } catch (Exception e) {
            Log.e("VirtualSimUtil", "Enter Contact BusinessHall failed.", e);
        }
    }

    public static void enterMiuiBusinessHall(Context context, Intent intent, int i) {
        try {
            Intent intent2 = new Intent();
            intent2.setPackage("com.miui.virtualsim");
            intent2.setAction("com.miui.businesshall.ACTION_ROUTER");
            intent2.putExtra("slotId", i);
            intent2.putExtra("launchfrom", "multisimsetting");
            intent2.addFlags(268435456);
            context.startActivity(intent2);
            Log.i("VirtualSimUtil", "Enter Miui BusinessHall hall.");
        } catch (Exception e) {
            Log.e("VirtualSimUtil", "Enter Miui BusinessHall failed.", e);
        }
    }

    public static void enterBusinessHall(Context context, Intent intent, int i) {
        if (isSupportMiuiMiMall(context)) {
            enterMiuiBusinessHall(context, intent, i);
        } else if (isSupportContactMiMall(context)) {
            enterContactBusinessHall(context, intent, i);
        } else {
            enterNetworkAssistant(context, i);
        }
    }

    public static void enterPackageSetting(Context context, Intent intent, int i) {
        try {
            Intent intent2 = new Intent();
            intent2.setAction("miui.intent.action.NETWORKASSISTANT_AUTO_TRAFFIC_CORRECTION_SETTING");
            Bundle bundle = new Bundle();
            bundle.putInt("sim_slot_num_tag", i);
            bundle.putBoolean("traffic_guide", true);
            bundle.putBoolean("key_back", true);
            intent2.putExtras(bundle);
            intent2.setMiuiFlags(intent.getMiuiFlags());
            if (!MiuiPhoneUtils.isDeviceSupportFold() && !Build.IS_TABLET) {
                intent2.addFlags(268435456);
            }
            if (context instanceof AppCompatActivity) {
                MultiAppFloatingActivitySwitcher.configureFloatingService(intent2, (AppCompatActivity) context);
            }
            context.startActivity(intent2);
            Log.i("VirtualSimUtil", "Enter package setting");
        } catch (Exception e) {
            Log.e("VirtualSimUtil", "Enter package setting failed.", e);
        }
    }

    public static void enterNetworkAssistant(Context context, int i) {
        try {
            Intent intent = new Intent();
            intent.setPackage("com.miui.securitycenter");
            intent.setAction("miui.intent.action.NETWORKASSISTANT_ENTRANCE");
            intent.putExtra("sim_slot_num_tag", i);
            intent.addFlags(268435456);
            context.startActivity(intent);
            Log.i("VirtualSimUtil", "Enter network assistant");
        } catch (Exception e) {
            Log.e("VirtualSimUtil", "Enter network assistant failed.", e);
        }
    }

    public static boolean isSupportContactMiMall(Context context) {
        try {
            Intent intent = new Intent();
            intent.setPackage("com.android.contacts");
            intent.setAction("com.mobile.businesshall.ACTION_ROUTER");
            return context.getPackageManager().resolveActivity(intent, 0) != null;
        } catch (Exception e) {
            Log.e("VirtualSimUtil", "Not suport contact mimall.", e);
            return false;
        }
    }

    public static boolean isSupportMiuiMiMall(Context context) {
        try {
            Intent intent = new Intent();
            intent.setPackage("com.miui.virtualsim");
            intent.setAction("com.miui.businesshall.ACTION_ROUTER");
            return context.getPackageManager().resolveActivity(intent, 0) != null;
        } catch (Exception e) {
            Log.e("VirtualSimUtil", "Not suport contact mimall.", e);
            return false;
        }
    }
}
