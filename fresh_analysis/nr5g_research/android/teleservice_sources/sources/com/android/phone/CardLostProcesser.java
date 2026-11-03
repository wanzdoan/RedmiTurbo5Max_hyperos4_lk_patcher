package com.android.phone;

import android.content.DialogInterface;
import android.graphics.Insets;
import android.telephony.Rlog;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.utils.MiStatInterfaceUtil;
import java.util.List;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.app.AlertDialog;

/* JADX INFO: loaded from: classes.dex */
public class CardLostProcesser {
    private static final int SAFE_INSET_TYPES = WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout();
    private AlertDialog mDialog = null;
    public int mNewDds;
    public int mOldDds;
    public String[] mOldSimIccIds;
    public boolean mUserDataEnabled;

    private void reset() {
        this.mDialog = null;
        this.mUserDataEnabled = false;
        int i = SubscriptionManager.INVALID_SLOT_ID;
        this.mOldDds = i;
        this.mNewDds = i;
        this.mOldSimIccIds = null;
    }

    public CardLostProcesser(int i, int i2, String[] strArr) {
        this.mUserDataEnabled = false;
        int i3 = SubscriptionManager.INVALID_SLOT_ID;
        this.mOldDds = i;
        this.mNewDds = i2;
        this.mOldSimIccIds = strArr;
        this.mUserDataEnabled = TelephonyManager.from(PhoneGlobals.getInstance()).getDataEnabled();
    }

    public void processForCardLost() {
        Rlog.d("CardLostProcesser", "processForCardLost");
        if (this.mUserDataEnabled) {
            TelephonyManager.from(PhoneGlobals.getInstance()).setDataEnabled(false);
            Rlog.d("CardLostProcesser", "processForCardLost : setDataEnabled false");
        }
        if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isEsimIsActiveChangeToNoActive()) {
            Rlog.d("CardLostProcesser", "processForCardLost : not showAutoSwitchAlertDialog");
            ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setEsimIsActiveChangeToNoActive(false);
        } else {
            showAutoSwitchAlertDialog();
            MiStatInterfaceUtil.recordCountEvent("event_card_lost", "dds_card_lost");
        }
    }

    public void showAutoSwitchAlertDialog() {
        Rlog.d("CardLostProcesser", "showAutoSwitchAlertDialog : mOldDds = " + this.mOldDds + " mNewDds = " + this.mNewDds);
        AlertDialog alertDialogCreate = new AlertDialog.Builder(PhoneGlobals.getInstance(), miuix.appcompat.R$style.AlertDialog_Theme_DayNight).setMessage(PhoneGlobals.getInstance().getString(this.mUserDataEnabled ? R.string.dds_switch_warning_message_data_on : R.string.dds_switch_warning_message_data_off, Integer.valueOf(this.mOldDds + 1), Integer.valueOf(this.mNewDds + 1))).setPositiveButton(android.R.string.ok, (DialogInterface.OnClickListener) null).setCancelable(false).create();
        this.mDialog = alertDialogCreate;
        setDialogWindowType(alertDialogCreate);
        this.mDialog.show();
        configureDialogWindow(this.mDialog);
        requestDialogInsets(this.mDialog);
    }

    private void setDialogWindowType(AlertDialog alertDialog) {
        Window window;
        if (alertDialog == null || (window = alertDialog.getWindow()) == null) {
            return;
        }
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.type = 2003;
        window.setAttributes(attributes);
    }

    private void configureDialogWindow(AlertDialog alertDialog) {
        Window window;
        if (alertDialog == null || (window = alertDialog.getWindow()) == null) {
            return;
        }
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.layoutInDisplayCutoutMode = 3;
        attributes.setFitInsetsTypes(0);
        window.setAttributes(attributes);
        Rlog.d("CardLostProcesser", "configureDialogWindow : cutoutMode=" + attributes.layoutInDisplayCutoutMode + " fitTypes=" + attributes.getFitInsetsTypes());
        window.getDecorView().setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: com.android.phone.CardLostProcesser$$ExternalSyntheticLambda0
            @Override // android.view.View.OnApplyWindowInsetsListener
            public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                return CardLostProcesser.m184$r8$lambda$oendF7zpBGhmfwM0ULcxSaqBWo(view, windowInsets);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$oendF7zpBGhmfwM0ULcxSaq-BWo, reason: not valid java name */
    public static /* synthetic */ WindowInsets m184$r8$lambda$oendF7zpBGhmfwM0ULcxSaqBWo(View view, WindowInsets windowInsets) {
        Insets insetsIgnoringVisibility = windowInsets.getInsetsIgnoringVisibility(SAFE_INSET_TYPES);
        Rlog.d("CardLostProcesser", "applySafeInsets : " + insetsIgnoringVisibility);
        View viewFindViewById = view.findViewById(android.R.id.content);
        if (viewFindViewById != null) {
            viewFindViewById.setPadding(insetsIgnoringVisibility.left, insetsIgnoringVisibility.top, insetsIgnoringVisibility.right, insetsIgnoringVisibility.bottom);
        }
        return windowInsets;
    }

    private void requestDialogInsets(AlertDialog alertDialog) {
        Window window;
        if (alertDialog == null || (window = alertDialog.getWindow()) == null) {
            return;
        }
        window.getDecorView().requestApplyInsets();
    }

    public void dissmissAutoSwitchAlertDialog() {
        if (this.mDialog != null) {
            Rlog.d("CardLostProcesser", "dissmissAutoSwitchAlertDialog");
            this.mDialog.dismiss();
            reset();
        }
    }

    public boolean shouldRestoreForCardLost() {
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog == null || !alertDialog.isShowing()) {
            return false;
        }
        List<SubscriptionInfo> subscriptionInfoList = SubscriptionManager.getDefault().getSubscriptionInfoList();
        if (subscriptionInfoList.size() != MiuiPhoneUtils.PHONE_COUNT) {
            return false;
        }
        for (SubscriptionInfo subscriptionInfo : subscriptionInfoList) {
            if (!this.mOldSimIccIds[subscriptionInfo.getSlotId()].equals(subscriptionInfo.getIccId())) {
                return false;
            }
        }
        Rlog.d("CardLostProcesser", "shouldRestoreForCardLost true");
        return true;
    }

    public int restoreForCardLost() {
        int i = this.mOldDds;
        if (this.mUserDataEnabled) {
            TelephonyManager.from(PhoneGlobals.getInstance()).setDataEnabled(true);
            Rlog.d("CardLostProcesser", "processForCardLost : setDataEnabled true");
        }
        dissmissAutoSwitchAlertDialog();
        MiStatInterfaceUtil.recordCountEvent("event_card_lost", "dds_card_restore");
        return i;
    }
}
