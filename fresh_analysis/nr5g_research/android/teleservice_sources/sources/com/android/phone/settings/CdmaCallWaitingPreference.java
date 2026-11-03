package com.android.phone.settings;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline1;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import com.android.phone.custompreferences.CustomListPreference;
import com.android.phone.utils.MiuiKoreaOperatorCustimizedUtils;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionManager;

/* JADX INFO: loaded from: classes.dex */
public class CdmaCallWaitingPreference extends CustomListPreference {
    private int mSlotId;
    private static final Uri URI_ENABLE_CW = Uri.parse("tel:*74");
    private static final Uri URI_DISABLE_CW = Uri.parse("tel:*740");
    private static final Uri URI_ENABLE_CW_KT = Uri.parse("tel:*40");
    private static final Uri URI_DISABLE_CW_KT = Uri.parse("tel:*400");
    private static final Uri URI_ENABLE_CW_SKT = Uri.parse("tel:*68");
    private static final Uri URI_DISABLE_CW_SKT = Uri.parse("tel:*680");

    public CdmaCallWaitingPreference(Context context) {
        super(context);
        this.mSlotId = DefaultSimManager.getDefaultDataSlotId();
    }

    public CdmaCallWaitingPreference(Context context, AttributeSet attributeSet) {
        String string;
        super(context, attributeSet);
        this.mSlotId = DefaultSimManager.getDefaultDataSlotId();
        setDefaultValue(null);
        setEntries(R.array.cdma_call_waiting_entries);
        setEntryValues(R.array.cdma_call_waiting_entry_values);
        if (TextUtils.isEmpty(getTitle())) {
            setTitle(R.string.labelCW);
        }
        if (MiuiKoreaOperatorCustimizedUtils.isCustForKrOps()) {
            if (MiuiKoreaOperatorCustimizedUtils.isKrKtSim(MiuiPhoneUtils.getPhone(this.mSlotId))) {
                string = getContext().getString(R.string.messageCW_kr, "*40", "*400");
            } else if (!MiuiKoreaOperatorCustimizedUtils.isKrSktSim(MiuiPhoneUtils.getPhone(this.mSlotId))) {
                string = "";
            } else {
                string = getContext().getString(R.string.messageCW_kr, "*68", "*680");
            }
            if (TextUtils.isEmpty(string)) {
                return;
            }
            setSummary(string);
        }
    }

    @Override // com.android.phone.custompreferences.CustomListPreference
    protected void onDialogClosed(boolean z) {
        Uri uri;
        super.onDialogClosed(z);
        if (z) {
            int i = Integer.parseInt(getValue());
            if (i != 0) {
                if (i == 1) {
                    if (MiuiKoreaOperatorCustimizedUtils.isKrKtSim(MiuiPhoneUtils.getPhone(this.mSlotId))) {
                        uri = URI_ENABLE_CW_KT;
                    } else if (MiuiKoreaOperatorCustimizedUtils.isKrSktSim(MiuiPhoneUtils.getPhone(this.mSlotId))) {
                        uri = URI_ENABLE_CW_SKT;
                    } else {
                        uri = URI_ENABLE_CW;
                    }
                } else {
                    OrtUtil$$ExternalSyntheticBUOutline1.m("Unexpexted call waiting value ", getValue());
                    return;
                }
            } else if (MiuiKoreaOperatorCustimizedUtils.isKrKtSim(MiuiPhoneUtils.getPhone(this.mSlotId))) {
                uri = URI_DISABLE_CW_KT;
            } else if (MiuiKoreaOperatorCustimizedUtils.isKrSktSim(MiuiPhoneUtils.getPhone(this.mSlotId))) {
                uri = URI_DISABLE_CW_SKT;
            } else {
                uri = URI_DISABLE_CW;
            }
            Intent intent = new Intent("android.intent.action.CALL");
            intent.setData(uri);
            SubscriptionManager.putSlotIdExtra(intent, this.mSlotId);
            ((TelecomManager) getContext().getSystemService("telecom")).placeCall(intent.getData(), intent.getExtras());
        }
    }

    public void setSlotId(int i) {
        this.mSlotId = i;
    }
}
