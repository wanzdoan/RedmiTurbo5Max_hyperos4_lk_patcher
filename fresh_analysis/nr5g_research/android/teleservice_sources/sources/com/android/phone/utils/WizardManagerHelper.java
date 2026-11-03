package com.android.phone.utils;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public abstract class WizardManagerHelper {
    public static void goToNextPage(Activity activity, Intent intent, int i) {
        if (activity == null || intent == null) {
            Folme$$ExternalSyntheticBUOutline0.m("fragment or originIntent cannot be null when go to next page in SetupWizard");
            return;
        }
        try {
            activity.startActivityForResult(getNextIntent(intent, i), 33);
        } catch (Exception e) {
            Log.e("WizardManagerHelper", "goToNextPage: error " + e.getMessage(), e);
        }
    }

    public static Intent getNextIntent(Intent intent, int i) {
        return getNextIntent(intent, i, null);
    }

    public static Intent getNextIntent(Intent intent, int i, Intent intent2) {
        Intent intent3 = new Intent("com.android.wizard.NEXT");
        copyWizardManagerExtras(intent, intent3);
        intent3.putExtra("com.android.setupwizard.ResultCode", i);
        if (intent2 != null && intent2.getExtras() != null) {
            intent3.putExtras(intent2.getExtras());
        }
        intent3.putExtra("theme", intent.getStringExtra("theme"));
        return intent3;
    }

    public static void copyWizardManagerExtras(Intent intent, Intent intent2) {
        intent2.putExtra("wizardBundle", intent.getBundleExtra("wizardBundle"));
        intent2.putExtra("theme", intent.getStringExtra("theme"));
        intent2.putExtra("firstRun", intent.getBooleanExtra("firstRun", false));
        intent2.putExtra("scriptUri", intent.getStringExtra("scriptUri"));
        intent2.putExtra("actionId", intent.getStringExtra("actionId"));
    }
}
