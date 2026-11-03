package com.android.phone.settings.sim;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.R;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.settings.sim.bean.AdBean;
import com.android.phone.settings.sim.bean.PackageBean;
import com.android.phone.settings.sim.bean.SimInfoBean;
import com.android.phone.utils.FontUtil;
import com.android.phone.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import miui.os.Build;
import miui.telephony.DefaultSimManager;
import miui.telephony.PhoneNumberUtils;
import miui.telephony.SubscriptionInfo;
import miuix.animation.Folme;
import miuix.animation.ITouchStyle;
import miuix.animation.base.AnimConfig;
import miuix.core.util.MiuixUIUtils;
import miuix.preference.PreferenceStyle;

/* JADX INFO: loaded from: classes.dex */
public class MultiSimPreference extends Preference implements PreferenceStyle {
    private static final int[] SIM_ICON = {R.drawable.ic_sim_1_big, R.drawable.ic_sim_2_big};
    private static boolean mIsSupporteGoogleLPAeSim = MiuiPhoneUtils.isSupportGoogleLPAEsim();
    private boolean mIsEnabled;
    private List mSimInfoRecordList;
    private SparseArray mSimInfoSparse;

    @Override // miuix.preference.PreferenceStyle
    public boolean enabledCardStyle() {
        return false;
    }

    @Override // miuix.preference.FolmeAnimationController
    public boolean isTouchAnimationEnable() {
        return false;
    }

    public MultiSimPreference(Context context) {
        this(context, null);
    }

    public MultiSimPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MultiSimPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mSimInfoRecordList = new ArrayList();
        this.mSimInfoSparse = new SparseArray();
        if (MiuixUIUtils.getFontLevel(context) == 2) {
            setLayoutResource(R.layout.sim_preference_large_font);
        } else {
            setLayoutResource(R.layout.sim_preference);
        }
    }

    public void setSimInfoRecordList(List list) {
        this.mSimInfoRecordList = list;
        generateSimInfo();
        notifyChanged();
    }

    private void generateSimInfo() {
        SimInfoBean simInfoBean;
        this.mSimInfoSparse.clear();
        int virtualSimSlot = MiuiPhoneUtils.getVirtualSimSlot();
        for (int i = 0; i < 2; i++) {
            SubscriptionInfo subInfo = getSubInfo(i);
            if (subInfo != null) {
                boolean z = virtualSimSlot == subInfo.getSlotId();
                simInfoBean = new SimInfoBean();
                if (z) {
                    simInfoBean.mSimType = 101;
                } else {
                    simInfoBean.mSimType = 100;
                }
                simInfoBean.mSlotId = subInfo.getSlotId();
                simInfoBean.mDisplayName = getSimDisplayName(subInfo, z);
                simInfoBean.mDisplayNumber = getSimDisplayNumber(subInfo, subInfo.getSlotId());
                simInfoBean.mIsVirtualSim = z;
                simInfoBean.mSimIcon = z ? R.drawable.ic_mi_sim_big : SIM_ICON[subInfo.getSlotId()];
            } else {
                simInfoBean = new SimInfoBean();
                List list = this.mSimInfoRecordList;
                if (list != null && list.size() == 1) {
                    simInfoBean.mSimType = 103;
                    simInfoBean.mSlotId = i;
                } else {
                    simInfoBean.mSimType = 102;
                    simInfoBean.mSlotId = i;
                }
                simInfoBean.mDisplayName = getContext().getResources().getString(R.string.msim_setting_no_sim_indicator);
                simInfoBean.mDisplayNumber = getContext().getResources().getString(R.string.msim_setting_no_sim_number);
                simInfoBean.mIsVirtualSim = false;
                simInfoBean.mSimIcon = SIM_ICON[i];
            }
            if (i == 1 && MiuiPhoneUtils.isEsimCapable() && ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isSupportForRegion()) {
                int esimSecureValue = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getEsimSecureValue();
                boolean zIsEsimActive = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isEsimActive();
                if (esimSecureValue == 1) {
                    if (!zIsEsimActive) {
                        simInfoBean.mDisplayName = getContext().getResources().getString(R.string.no_active_esim_text);
                        simInfoBean.mDisplayNumber = "";
                    } else if (!MiuiPhoneUtils.isSupportMep()) {
                        simInfoBean.mSimIcon = R.drawable.ic_esim_global_big;
                    }
                }
            }
            this.mSimInfoSparse.put(simInfoBean.mSlotId, simInfoBean);
        }
    }

    private SubscriptionInfo getSubInfo(int i) {
        List list = this.mSimInfoRecordList;
        if (list == null || list.size() <= 0) {
            return null;
        }
        for (SubscriptionInfo subscriptionInfo : this.mSimInfoRecordList) {
            if (subscriptionInfo.getSlotId() == i) {
                return subscriptionInfo;
            }
        }
        return null;
    }

    public void setIsEnabled(boolean z) {
        this.mIsEnabled = z;
        notifyChanged();
    }

    private String getSimDisplayName(SubscriptionInfo subscriptionInfo, boolean z) {
        String string;
        if (z) {
            string = MiuiPhoneUtils.getVirtualSimCarrierName();
        } else {
            string = subscriptionInfo.getDisplayName().toString();
        }
        return TextUtils.isEmpty(string) ? getContext().getResources().getString(R.string.msim_setting_no_sim_number) : string;
    }

    private String getSimDisplayNumber(SubscriptionInfo subscriptionInfo, int i) {
        String displayNumber;
        if (!MiuiPhoneUtils.isIccCardActivated(i)) {
            return getContext().getResources().getString(R.string.msim_sim_radio_off);
        }
        if (TextUtils.isEmpty(subscriptionInfo.getDisplayNumber())) {
            displayNumber = getContext().getResources().getString(R.string.msim_setting_no_sim_number);
        } else {
            displayNumber = subscriptionInfo.getDisplayNumber();
        }
        if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isShowCdmaUnavailable() && subscriptionInfo.getSlotId() != DefaultSimManager.getDefaultDataSlotId() && 2 == ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getPhoneType(subscriptionInfo.getSlotId())) {
            displayNumber = displayNumber + getContext().getResources().getString(R.string.can_not_use_for_not_data_sim);
        }
        return Utils.localizeNumber(displayNumber);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) throws Throwable {
        View view = preferenceViewHolder.itemView;
        View viewFindViewById = view.findViewById(R.id.sim_1);
        View viewFindViewById2 = view.findViewById(R.id.sim_2);
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), this.mSimInfoRecordList.size() < 2 ? getContext().getResources().getDimensionPixelSize(R.dimen.preference_category_padding_top) : 0);
        if (viewFindViewById == null || viewFindViewById2 == null) {
            return;
        }
        initSimView(viewFindViewById, (SimInfoBean) this.mSimInfoSparse.get(0));
        initSimView(viewFindViewById2, (SimInfoBean) this.mSimInfoSparse.get(1));
        view.setEnabled(false);
        viewFindViewById.setEnabled(false);
        viewFindViewById2.setEnabled(false);
    }

    private void initSimView(View view, SimInfoBean simInfoBean) throws Throwable {
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.sim_info);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon);
        ImageView imageView2 = (ImageView) view.findViewById(R.id.eicon);
        MarqueeTextView marqueeTextView = (MarqueeTextView) view.findViewById(R.id.title);
        TextView textView = (TextView) view.findViewById(R.id.summary);
        View viewFindViewById = view.findViewById(R.id.divider);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.extra_info);
        if (linearLayout == null || imageView == null || marqueeTextView == null || textView == null || viewFindViewById == null || relativeLayout == null) {
            return;
        }
        marqueeTextView.setMarqueeEnable(true);
        marqueeTextView.setFocusable(false);
        int i = simInfoBean.mSimType;
        if (i == 100 || i == 101) {
            linearLayout.setOnClickListener(new SimClickListener(1, simInfoBean));
            linearLayout.setClickable(true);
            ITouchStyle iTouchStyle = Folme.useAt(linearLayout).touch();
            ITouchStyle.TouchType touchType = ITouchStyle.TouchType.DOWN;
            ITouchStyle scale = iTouchStyle.setScale(1.0f, touchType);
            ITouchStyle.TouchType touchType2 = ITouchStyle.TouchType.UP;
            scale.setScale(1.0f, touchType2).setAlpha(0.6f, touchType).setAlpha(1.0f, touchType2).handleTouchOf(linearLayout, new AnimConfig[0]);
        } else if (i == 104) {
            linearLayout.setOnClickListener(new SimClickListener(4, simInfoBean));
            linearLayout.setClickable(true);
            ITouchStyle iTouchStyle2 = Folme.useAt(linearLayout).touch();
            ITouchStyle.TouchType touchType3 = ITouchStyle.TouchType.DOWN;
            ITouchStyle scale2 = iTouchStyle2.setScale(1.0f, touchType3);
            ITouchStyle.TouchType touchType4 = ITouchStyle.TouchType.UP;
            scale2.setScale(1.0f, touchType4).setAlpha(0.6f, touchType3).setAlpha(1.0f, touchType4).handleTouchOf(linearLayout, new AnimConfig[0]);
        } else {
            linearLayout.setOnClickListener(null);
            linearLayout.setClickable(false);
            Folme.clean(linearLayout);
        }
        imageView.setImageResource(simInfoBean.mSimIcon);
        marqueeTextView.setText(simInfoBean.mDisplayName);
        textView.setText(simInfoBean.mDisplayNumber);
        if (PhoneNumberUtils.isGlobalPhoneNumber(simInfoBean.mDisplayNumber) || PhoneNumberUtils.isChinaMobileNumber(simInfoBean.mDisplayNumber)) {
            textView.setContentDescription(PhoneNumberUtils.createTtsSpannable(simInfoBean.mDisplayNumber));
        }
        int i2 = simInfoBean.mSimType;
        if (i2 == 102 || i2 == 103) {
            textView.setCompoundDrawablesRelative(null, null, null, null);
        } else {
            Drawable drawable = getContext().getResources().getDrawable(R.drawable.ic_right_arrow, null);
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                textView.setCompoundDrawablePadding(getContext().getResources().getDimensionPixelSize(R.dimen.sim_drawable_padding));
                textView.setCompoundDrawablesRelative(null, null, drawable, null);
            }
        }
        if (mIsSupporteGoogleLPAeSim && simInfoBean.mSlotId == 1) {
            imageView2.setVisibility(0);
            imageView2.setImageResource(simInfoBean.mEsimIcon);
        }
        if (this.mIsEnabled) {
            imageView.setAlpha(1.0f);
            marqueeTextView.setAlpha(1.0f);
            textView.setAlpha(1.0f);
            linearLayout.setEnabled(true);
        } else {
            imageView.setAlpha(0.3f);
            marqueeTextView.setAlpha(0.3f);
            textView.setAlpha(0.3f);
            linearLayout.setEnabled(false);
        }
        if (Build.IS_INTERNATIONAL_BUILD) {
            viewFindViewById.setVisibility(8);
            relativeLayout.setVisibility(8);
            linearLayout.setBackgroundResource(R.drawable.sim_bg);
            return;
        }
        initExtraInfo(view, simInfoBean);
    }

    private void initExtraInfo(View view, SimInfoBean simInfoBean) throws Throwable {
        AdBean adInfo;
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.extra_info);
        TextView textView = (TextView) view.findViewById(R.id.extra_title);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.extra_summary_layout);
        TextView textView2 = (TextView) view.findViewById(R.id.extra_summary_value);
        TextView textView3 = (TextView) view.findViewById(R.id.extra_summary_unit);
        ImageView imageView = (ImageView) view.findViewById(R.id.red_dot);
        if (relativeLayout == null || textView == null || linearLayout == null || textView2 == null || textView3 == null || imageView == null) {
            return;
        }
        imageView.setVisibility(8);
        relativeLayout.setOnClickListener(null);
        Folme.clean(relativeLayout);
        int i = 0;
        if (MiuiPhoneUtils.isIccCardActivated(simInfoBean.mSlotId)) {
            textView.setAlpha(1.0f);
            textView2.setAlpha(1.0f);
            textView3.setAlpha(1.0f);
            relativeLayout.setEnabled(true);
        } else {
            textView.setAlpha(0.3f);
            textView2.setAlpha(0.3f);
            textView3.setAlpha(0.3f);
            relativeLayout.setEnabled(false);
        }
        textView.setMaxEms(MiuiPhoneUtils.isPad() ? 50 : textView.getMaxEms());
        int i2 = simInfoBean.mSimType;
        Log.i("MultiSimPreference", "initExtraInfo: type=" + i2);
        int i3 = 2;
        switch (i2) {
            case 100:
                PackageBean packageBean = getPackage(simInfoBean.mSlotId);
                if (packageBean != null) {
                    if (packageBean.notSetPackage()) {
                        selectPlan(textView);
                        linearLayout.setVisibility(8);
                        relativeLayout.setOnClickListener(new SimClickListener(3, simInfoBean));
                        initDefaultSimAnim(relativeLayout, simInfoBean);
                    } else if (packageBean.noSim()) {
                        noBalanceInfo(textView);
                        linearLayout.setVisibility(8);
                    } else if (!TextUtils.isEmpty(packageBean.mBillName) && !TextUtils.isEmpty(packageBean.mBillValue)) {
                        setExtraInfo(textView, linearLayout, textView2, textView3, packageBean.mBillName, packageBean.mBillValue, packageBean.mBillUnit);
                        relativeLayout.setOnClickListener(new SimClickListener(i, simInfoBean));
                        initDefaultSimAnim(relativeLayout, simInfoBean);
                    } else {
                        noBalanceInfo(textView);
                        linearLayout.setVisibility(8);
                        relativeLayout.setOnClickListener(new SimClickListener(i, simInfoBean));
                        initDefaultSimAnim(relativeLayout, simInfoBean);
                    }
                } else {
                    noBalanceInfo(textView);
                    linearLayout.setVisibility(8);
                }
                break;
            case 101:
                PackageBean dataUsageInfo = SimCacheHelper.getInstance().getDataUsageInfo(getContext());
                if (dataUsageInfo != null) {
                    if (!TextUtils.isEmpty(dataUsageInfo.mTrafficName) && !TextUtils.isEmpty(dataUsageInfo.mTrafficValue)) {
                        setExtraInfo(textView, linearLayout, textView2, textView3, dataUsageInfo.mTrafficName, dataUsageInfo.mTrafficValue, dataUsageInfo.mTrafficUnit);
                        if (!TextUtils.isEmpty(dataUsageInfo.mClickAction)) {
                            relativeLayout.setOnClickListener(new SimClickListener(i3, dataUsageInfo.mClickAction));
                            initDefaultSimAnim(relativeLayout, simInfoBean);
                        }
                    } else {
                        noBalanceInfo(textView);
                        linearLayout.setVisibility(8);
                    }
                } else {
                    noBalanceInfo(textView);
                    linearLayout.setVisibility(8);
                }
                break;
            case 102:
                noBalanceInfo(textView);
                textView.setAlpha(1.0f);
                relativeLayout.setEnabled(true);
                relativeLayout.setOnClickListener(new SimClickListener(i, simInfoBean));
                initDefaultSimAnim(relativeLayout, simInfoBean);
                linearLayout.setVisibility(8);
                break;
            case 103:
                if (Utils$PhoneUtil.isChinese() && (adInfo = SimCacheHelper.getInstance().getAdInfo(getContext())) != null && !TextUtils.isEmpty(adInfo.mMessage) && !TextUtils.isEmpty(adInfo.mIntentUri)) {
                    textView.setText(adInfo.mMessage);
                    textView.setTextColor(getContext().getResources().getColor(R.color.sim_extra_accent_color));
                    textView.setTypeface(FontUtil.createTypeface(false, "medium"));
                    imageView.setVisibility(0);
                    relativeLayout.setOnClickListener(new SimClickListener(i3, adInfo.mIntentUri));
                    initDefaultSimAnim(relativeLayout, simInfoBean);
                } else {
                    noBalanceInfo(textView);
                }
                linearLayout.setVisibility(8);
                break;
        }
    }

    private void selectPlan(TextView textView) {
        textView.setText(R.string.msim_setting_set_package);
        textView.setTextColor(getContext().getResources().getColor(R.color.sim_extra_accent_color));
        textView.setTypeface(FontUtil.createTypeface(false, "medium"));
    }

    private void noBalanceInfo(TextView textView) {
        textView.setText(R.string.msim_setting_no_sim_package_info);
        textView.setTextColor(getContext().getResources().getColor(R.color.sim_extra_title_color));
        textView.setTypeface(FontUtil.createTypeface(false, "medium"));
    }

    private void setExtraInfo(TextView textView, LinearLayout linearLayout, TextView textView2, TextView textView3, String str, String str2, String str3) {
        int i;
        textView.setText(str);
        textView.setTextColor(getContext().getResources().getColor(R.color.sim_extra_title_color));
        textView.setTypeface(FontUtil.createTypeface(false, "regular"));
        linearLayout.setVisibility(0);
        if (str2.startsWith("-")) {
            i = R.color.sim_extra_accent_color_warning;
        } else {
            i = R.color.sim_extra_accent_color;
        }
        textView2.setTextColor(getContext().getResources().getColor(i));
        textView3.setTextColor(getContext().getResources().getColor(i));
        textView2.setText(str2);
        textView3.setText(str3);
    }

    private void initDefaultSimAnim(View view, SimInfoBean simInfoBean) {
        ITouchStyle iTouchStyle = Folme.useAt(view).touch();
        if (MiuiPhoneUtils.isIccCardActivated(simInfoBean.mSlotId)) {
            ITouchStyle.TouchType touchType = ITouchStyle.TouchType.DOWN;
            ITouchStyle scale = iTouchStyle.setScale(1.0f, touchType);
            ITouchStyle.TouchType touchType2 = ITouchStyle.TouchType.UP;
            scale.setScale(1.0f, touchType2).setAlpha(0.6f, touchType).setAlpha(1.0f, touchType2);
        } else {
            ITouchStyle.TouchType touchType3 = ITouchStyle.TouchType.DOWN;
            ITouchStyle scale2 = iTouchStyle.setScale(1.0f, touchType3);
            ITouchStyle.TouchType touchType4 = ITouchStyle.TouchType.UP;
            scale2.setScale(1.0f, touchType4).setAlpha(0.3f, touchType3).setAlpha(0.3f, touchType4);
        }
        iTouchStyle.handleTouchOf(view, new AnimConfig[0]);
    }

    private PackageBean getPackage(int i) {
        List simInfo = SimCacheHelper.getInstance().getSimInfo(getContext());
        if (simInfo == null || simInfo.size() <= 0) {
            return null;
        }
        for (int i2 = 0; i2 < simInfo.size(); i2++) {
            PackageBean packageBean = (PackageBean) simInfo.get(i2);
            if (packageBean.mSlotId == i) {
                return packageBean;
            }
        }
        return null;
    }

    class SimClickListener implements View.OnClickListener {
        private int mNavCode;
        private SimInfoBean mSimInfo;
        private String mUri;

        private SimClickListener(int i, SimInfoBean simInfoBean) {
            this.mNavCode = i;
            this.mSimInfo = simInfoBean;
        }

        private SimClickListener(int i, String str) {
            this.mNavCode = i;
            this.mUri = str;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Intent intent = MultiSimPreference.this.getIntent();
            int i = this.mNavCode;
            if (i == 0) {
                Utils$VirtualSim.enterBusinessHall(MultiSimPreference.this.getContext(), intent, this.mSimInfo.mSlotId);
                return;
            }
            if (i == 1) {
                int simState = TelephonyManager.getDefault().getSimState(this.mSimInfo.mSlotId);
                MultiSimPreference multiSimPreference = MultiSimPreference.this;
                if (simState == 4) {
                    Toast.makeText(multiSimPreference.getContext(), MultiSimPreference.this.getContext().getResources().getString(R.string.sim_can_not_read, Integer.valueOf(this.mSimInfo.mSlotId + 1)), 1).show();
                    return;
                } else {
                    Utils$PhoneUtil.enterSimEditor(multiSimPreference.getContext(), intent, this.mSimInfo.mSlotId);
                    return;
                }
            }
            if (i == 2) {
                Utils$PhoneUtil.enterUri(MultiSimPreference.this.getContext(), intent, this.mUri);
            } else if (i == 3) {
                Utils$VirtualSim.enterPackageSetting(MultiSimPreference.this.getContext(), intent, this.mSimInfo.mSlotId);
            } else {
                if (i != 4) {
                    return;
                }
                Utils$PhoneUtil.enterESimManager(MultiSimPreference.this.getContext(), intent, this.mSimInfo.mSlotId);
            }
        }
    }
}
