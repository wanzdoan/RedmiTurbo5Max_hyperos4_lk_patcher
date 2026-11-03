package com.android.phone.settings;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import com.android.phone.utils.MiuiKoreaOperatorCustimizedUtils;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionManager;
import miui.telephony.phonenumber.CountryCode;

/* JADX INFO: loaded from: classes.dex */
public class CdmaCallForwardEditPreference extends EditPhoneNumberPreference {
    int mReason;
    private int mSlotId;
    private static final Uri URI_DISABLE_CFU = Uri.parse("tel:*720");
    private static final Uri URI_DISABLE_CFB = Uri.parse("tel:*900");
    private static final Uri URI_DISABLE_CFNRY = Uri.parse("tel:*920");
    private static final Uri URI_DISABLE_CFNRC = Uri.parse("tel:*680");
    private static final Uri URI_DISABLE_CFU_KT = Uri.parse("tel:*710");
    private static final Uri URI_DISABLE_CFU_SKT = Uri.parse("tel:*73");

    public CdmaCallForwardEditPreference(Context context) {
        super(context);
        this.mSlotId = DefaultSimManager.getDefaultDataSlotId();
    }

    public CdmaCallForwardEditPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSlotId = DefaultSimManager.getDefaultDataSlotId();
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.CallForwardEditPreference, 0, R.style.EditPhoneNumberPreference);
        this.mReason = typedArrayObtainStyledAttributes.getInt(R.styleable.CallForwardEditPreference_reason, 0);
        typedArrayObtainStyledAttributes.recycle();
        setToggled(true);
        setDialogOnClosedListener(new EditPhoneNumberPreference.OnDialogClosedListener() { // from class: com.android.phone.settings.CdmaCallForwardEditPreference.1
            @Override // com.android.phone.settings.EditPhoneNumberPreference.OnDialogClosedListener
            public void onDialogClosed(EditPhoneNumberPreference editPhoneNumberPreference, int i) {
                CdmaCallForwardEditPreference.this.setToggled(true);
                if (i != -2) {
                    CdmaCallForwardEditPreference cdmaCallForwardEditPreference = CdmaCallForwardEditPreference.this;
                    Uri uri = null;
                    if (i == -1) {
                        String phoneNumber = cdmaCallForwardEditPreference.getPhoneNumber();
                        CdmaCallForwardEditPreference cdmaCallForwardEditPreference2 = CdmaCallForwardEditPreference.this;
                        int i2 = cdmaCallForwardEditPreference2.mReason;
                        if (i2 != 0) {
                            if (i2 == 1) {
                                uri = Uri.parse("tel:*90" + phoneNumber);
                            } else if (i2 == 2) {
                                uri = Uri.parse("tel:*92" + phoneNumber);
                            } else if (i2 == 3) {
                                uri = Uri.parse("tel:*68" + phoneNumber);
                            }
                        } else if (MiuiKoreaOperatorCustimizedUtils.isKrSim(MiuiPhoneUtils.getPhone(cdmaCallForwardEditPreference2.mSlotId))) {
                            uri = Uri.parse("tel:*71" + phoneNumber);
                        } else {
                            uri = Uri.parse("tel:*72" + phoneNumber);
                        }
                    } else {
                        int i3 = cdmaCallForwardEditPreference.mReason;
                        if (i3 != 0) {
                            if (i3 == 1) {
                                uri = CdmaCallForwardEditPreference.URI_DISABLE_CFB;
                            } else if (i3 == 2) {
                                uri = CdmaCallForwardEditPreference.URI_DISABLE_CFNRY;
                            } else if (i3 == 3) {
                                uri = CdmaCallForwardEditPreference.URI_DISABLE_CFNRC;
                            }
                        } else if (MiuiKoreaOperatorCustimizedUtils.isKrKtSim(MiuiPhoneUtils.getPhone(cdmaCallForwardEditPreference.mSlotId))) {
                            uri = CdmaCallForwardEditPreference.URI_DISABLE_CFU_KT;
                        } else if (MiuiKoreaOperatorCustimizedUtils.isKrSktSim(MiuiPhoneUtils.getPhone(CdmaCallForwardEditPreference.this.mSlotId))) {
                            uri = CdmaCallForwardEditPreference.URI_DISABLE_CFU_SKT;
                        } else {
                            uri = CdmaCallForwardEditPreference.URI_DISABLE_CFU;
                        }
                    }
                    if (uri != null) {
                        Intent intent = new Intent("android.intent.action.CALL");
                        intent.setData(uri);
                        SubscriptionManager.putSlotIdExtra(intent, CdmaCallForwardEditPreference.this.mSlotId);
                        CdmaCallForwardEditPreference.this.getContext().startActivity(intent);
                    }
                }
            }
        });
    }

    public void setSlotId(int i) {
        this.mSlotId = i;
    }

    @Override // com.android.phone.settings.EditPhoneNumberPreference
    public String getPhoneNumber() {
        String phoneNumber = super.getPhoneNumber();
        if (!MiuiPhoneUtils.shouldAddCountryCodeForCdmaCallForward(this.mSlotId)) {
            return phoneNumber;
        }
        return CountryCode.getIddCode() + CountryCode.getNetworkCountryCode() + phoneNumber;
    }
}
