package com.android.phone.settings.sim;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.ModuleDelegate;
import com.android.phone.R;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.settings.sim.bean.DefaultSimBean;
import com.android.phone.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;
import miuix.animation.Folme;
import miuix.animation.ITouchStyle;
import miuix.animation.base.AnimConfig;
import miuix.popupwidget.widget.GuidePopupWindow;
import miuix.view.HapticCompat;
import miuix.view.HapticFeedbackConstants;

/* JADX INFO: loaded from: classes.dex */
public class DefaultSimPreference extends Preference {
    private static final int SLOT_NUM;
    private boolean mDataEnabled;
    private SparseArray mDataSparse;
    private int mDefaultDataSlotId;
    private View mDefaultSms;
    private int mDefaultSmsSlotId;
    private int mDefaultVoiceSlotId;
    private List mSimInfoRecordList;
    private boolean mSmsEnabled;
    private SparseArray mSmsSparse;
    private boolean mVoiceEnabled;
    private SparseArray mVoiceSparse;
    private static final int[] SIM1 = {R.drawable.ic_sim_1, R.drawable.ic_sim_1_checked};
    private static final int[] SIM2 = {R.drawable.ic_sim_2, R.drawable.ic_sim_2_checked};
    private static final int[] SIM2_ESIM = {R.drawable.ic_esim_global, R.drawable.ic_esim_global_checked};
    private static final int[] NO_DEFAULT = {R.drawable.ic_sim_no_default, R.drawable.ic_sim_no_default_checked};
    private static final int[] VIRTUAL_SIM = {R.drawable.ic_mi_sim, R.drawable.ic_mi_sim_checked};

    static {
        SLOT_NUM = (!MiuiPhoneUtils.isMultiSimEnabled() || TelephonyManager.isCustSingleSimDevice()) ? 1 : 2;
    }

    public DefaultSimPreference(Context context) {
        this(context, null);
    }

    public DefaultSimPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DefaultSimPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mSimInfoRecordList = new ArrayList();
        this.mVoiceSparse = new SparseArray();
        this.mDataSparse = new SparseArray();
        this.mSmsSparse = new SparseArray();
        this.mDefaultVoiceSlotId = -1;
        this.mDefaultDataSlotId = 0;
        this.mDefaultSmsSlotId = -1;
        setLayoutResource(R.layout.default_sim_preference);
    }

    public void setIsEnabled(boolean z) {
        this.mVoiceEnabled = z;
        this.mDataEnabled = z;
        this.mSmsEnabled = z;
        notifySimDataChanged();
    }

    public void setVoiceEnabled(boolean z) {
        this.mVoiceEnabled = z;
        notifySimDataChanged();
    }

    public void setDataEnabled(boolean z) {
        this.mDataEnabled = z;
        notifySimDataChanged();
    }

    public void setSmsEnabled(boolean z) {
        this.mSmsEnabled = z;
        notifySimDataChanged();
    }

    public void setSimInfoRecordList(List list) {
        this.mSimInfoRecordList = list;
        notifySimDataChanged();
    }

    public void updateSmsLayoutUI() {
        Log.i("DefaultSimPreference", "updateSmsLayoutUI mSmsEnabled = " + this.mSmsEnabled);
        boolean z = this.mSmsEnabled;
        View view = this.mDefaultSms;
        if (z) {
            view.setVisibility(0);
        } else {
            view.setVisibility(8);
        }
    }

    public void notifySimDataChanged() {
        this.mDefaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        this.mDefaultVoiceSlotId = ModemProvider.getInstance().getDefaultVoiceSlotId();
        this.mDefaultSmsSlotId = SubscriptionManager.getDefault().getDefaultSmsSlotId();
        generateDefaultSim();
        notifyChanged();
    }

    private void generateDefaultSim() {
        this.mVoiceSparse.clear();
        this.mDataSparse.clear();
        this.mSmsSparse.clear();
        int virtualSimSlot = MiuiPhoneUtils.getVirtualSimSlot();
        int i = 0;
        while (true) {
            if (i >= SLOT_NUM) {
                break;
            }
            SubscriptionInfo subInfo = getSubInfo(i);
            if (subInfo != null) {
                DefaultSimBean defaultSimBean = new DefaultSimBean();
                defaultSimBean.mSlotId = subInfo.getSlotId();
                defaultSimBean.mIsVirtualSim = subInfo.getSlotId() == virtualSimSlot;
                defaultSimBean.isChecked = subInfo.getSlotId() == this.mDefaultVoiceSlotId;
                this.mVoiceSparse.put(defaultSimBean.mSlotId, defaultSimBean);
                DefaultSimBean defaultSimBean2 = new DefaultSimBean();
                defaultSimBean2.mSlotId = subInfo.getSlotId();
                defaultSimBean2.mIsVirtualSim = subInfo.getSlotId() == virtualSimSlot;
                defaultSimBean2.isChecked = subInfo.getSlotId() == this.mDefaultDataSlotId;
                this.mDataSparse.put(defaultSimBean2.mSlotId, defaultSimBean2);
                DefaultSimBean defaultSimBean3 = new DefaultSimBean();
                defaultSimBean3.mSlotId = subInfo.getSlotId();
                defaultSimBean3.mIsVirtualSim = subInfo.getSlotId() == virtualSimSlot;
                defaultSimBean3.isChecked = subInfo.getSlotId() == this.mDefaultSmsSlotId;
                this.mSmsSparse.put(defaultSimBean3.mSlotId, defaultSimBean3);
                if (!MiuiPhoneUtils.isIccCardActivated(subInfo.getSlotId())) {
                    this.mVoiceEnabled = false;
                    this.mDataEnabled = false;
                    this.mSmsEnabled = false;
                }
            } else {
                DefaultSimBean defaultSimBean4 = new DefaultSimBean();
                defaultSimBean4.mSlotId = i;
                defaultSimBean4.mIsVirtualSim = false;
                defaultSimBean4.isChecked = i == this.mDefaultVoiceSlotId;
                this.mVoiceSparse.put(i, defaultSimBean4);
                DefaultSimBean defaultSimBean5 = new DefaultSimBean();
                defaultSimBean5.mSlotId = i;
                defaultSimBean5.mIsVirtualSim = false;
                defaultSimBean5.isChecked = i == this.mDefaultDataSlotId;
                this.mDataSparse.put(i, defaultSimBean5);
                DefaultSimBean defaultSimBean6 = new DefaultSimBean();
                defaultSimBean6.mSlotId = i;
                defaultSimBean6.mIsVirtualSim = false;
                defaultSimBean6.isChecked = i == this.mDefaultSmsSlotId;
                this.mSmsSparse.put(i, defaultSimBean6);
            }
            i++;
        }
        DefaultSimBean defaultSimBean7 = new DefaultSimBean();
        defaultSimBean7.mSlotId = -1;
        defaultSimBean7.mIsVirtualSim = false;
        defaultSimBean7.isChecked = this.mDefaultVoiceSlotId < 0;
        this.mVoiceSparse.put(-1, defaultSimBean7);
        DefaultSimBean defaultSimBean8 = new DefaultSimBean();
        defaultSimBean8.mSlotId = -1;
        defaultSimBean8.mIsVirtualSim = false;
        defaultSimBean8.isChecked = this.mDefaultSmsSlotId < 0;
        this.mSmsSparse.put(-1, defaultSimBean8);
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

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        View view = preferenceViewHolder.itemView;
        View viewFindViewById = view.findViewById(R.id.default_voice);
        View viewFindViewById2 = view.findViewById(R.id.default_data);
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), getContext().getResources().getDimensionPixelSize(R.dimen.preference_category_default_sim_padding_bottom));
        this.mDefaultSms = view.findViewById(R.id.default_sms);
        if (viewFindViewById != null) {
            if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isEsimActive() && MiuiPhoneUtils.isSupportEuiccBinding()) {
                viewFindViewById.setVisibility(8);
            } else {
                viewFindViewById.setVisibility(0);
                initDefaultVoice(viewFindViewById);
            }
        }
        if (viewFindViewById2 != null) {
            initDefaultData(viewFindViewById2);
        }
        View view2 = this.mDefaultSms;
        if (view2 != null) {
            initDefaultSms(view2);
            updateSmsLayoutUI();
        }
        view.setEnabled(false);
    }

    private void initDefaultVoice(View view) {
        TextView textView = (TextView) view.findViewById(R.id.title);
        ImageView imageView = (ImageView) view.findViewById(R.id.sim1);
        ImageView imageView2 = (ImageView) view.findViewById(R.id.sim2);
        ImageView imageView3 = (ImageView) view.findViewById(R.id.no_default);
        if (textView != null) {
            if (this.mVoiceEnabled) {
                textView.setAlpha(1.0f);
            } else {
                textView.setAlpha(0.3f);
            }
        }
        if (imageView != null) {
            initVoiceView(imageView, (DefaultSimBean) this.mVoiceSparse.get(0));
        }
        if (imageView2 != null) {
            initVoiceView(imageView2, (DefaultSimBean) this.mVoiceSparse.get(1));
        }
        if (imageView3 != null) {
            initVoiceView(imageView3, (DefaultSimBean) this.mVoiceSparse.get(-1));
        }
    }

    private void initVoiceView(ImageView imageView, DefaultSimBean defaultSimBean) {
        String string;
        if (defaultSimBean != null) {
            if (defaultSimBean.isChecked) {
                imageView.setBackground(getContext().getResources().getDrawable(R.drawable.default_voice_bg, null));
            } else {
                imageView.setBackground(null);
            }
            if (defaultSimBean.mIsVirtualSim) {
                imageView.setImageResource(VIRTUAL_SIM[defaultSimBean.isChecked ? 1 : 0]);
            } else {
                int i = defaultSimBean.mSlotId;
                if (i == 0) {
                    imageView.setImageResource(SIM1[defaultSimBean.isChecked ? 1 : 0]);
                } else if (i == 1) {
                    boolean z = defaultSimBean.isChecked;
                    imageView.setImageResource(SIM2[z ? 1 : 0]);
                    if (MiuiPhoneUtils.IS_ESIM_CAPABLE && !MiuiPhoneUtils.isSupportMep() && ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isEsimActive()) {
                        imageView.setImageResource(SIM2_ESIM[z ? 1 : 0]);
                    }
                } else if (i == -1) {
                    imageView.setImageResource(NO_DEFAULT[defaultSimBean.isChecked ? 1 : 0]);
                }
            }
            imageView.setOnClickListener(new DefaultSimClickListener(0, defaultSimBean));
            if (this.mVoiceEnabled) {
                imageView.setAlpha(1.0f);
            } else {
                imageView.setAlpha(0.3f);
            }
            initAnim(imageView);
            if (defaultSimBean.mSlotId == -1) {
                string = getContext().getResources().getString(R.string.msim_setting_pop_text);
            } else {
                string = String.format(getContext().getResources().getString(R.string.msim_setting_default_voice_description), Integer.valueOf(defaultSimBean.mSlotId + 1));
            }
            initAccessibilityNodeInfo(imageView, this.mVoiceEnabled, defaultSimBean.isChecked, string);
        }
    }

    private void initDefaultData(View view) {
        TextView textView = (TextView) view.findViewById(R.id.title);
        ImageView imageView = (ImageView) view.findViewById(R.id.sim1);
        ImageView imageView2 = (ImageView) view.findViewById(R.id.sim2);
        if (textView != null) {
            if (this.mDataEnabled) {
                textView.setAlpha(1.0f);
            } else {
                textView.setAlpha(0.3f);
            }
        }
        if (imageView != null) {
            initDataView(imageView, (DefaultSimBean) this.mDataSparse.get(0));
        }
        if (imageView2 != null) {
            initDataView(imageView2, (DefaultSimBean) this.mDataSparse.get(1));
        }
    }

    private void initDataView(ImageView imageView, DefaultSimBean defaultSimBean) {
        if (defaultSimBean != null) {
            if (defaultSimBean.isChecked) {
                imageView.setBackground(getContext().getResources().getDrawable(R.drawable.default_data_bg, null));
            } else {
                imageView.setBackground(null);
            }
            int i = 1;
            if (defaultSimBean.mIsVirtualSim) {
                imageView.setImageResource(VIRTUAL_SIM[defaultSimBean.isChecked ? 1 : 0]);
            } else {
                int i2 = defaultSimBean.mSlotId;
                if (i2 == 0) {
                    imageView.setImageResource(SIM1[defaultSimBean.isChecked ? 1 : 0]);
                } else if (i2 == 1) {
                    boolean z = defaultSimBean.isChecked;
                    imageView.setImageResource(SIM2[z ? 1 : 0]);
                    if (MiuiPhoneUtils.IS_ESIM_CAPABLE && !MiuiPhoneUtils.isSupportMep() && ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isEsimActive()) {
                        imageView.setImageResource(SIM2_ESIM[z ? 1 : 0]);
                    }
                }
            }
            imageView.setOnClickListener(new DefaultSimClickListener(i, defaultSimBean));
            if (this.mDataEnabled) {
                imageView.setAlpha(1.0f);
            } else {
                imageView.setAlpha(0.3f);
            }
            initAnim(imageView);
            initAccessibilityNodeInfo(imageView, this.mDataEnabled, defaultSimBean.isChecked, String.format(getContext().getResources().getString(R.string.msim_setting_default_data_description), Integer.valueOf(defaultSimBean.mSlotId + 1)));
        }
    }

    private void initDefaultSms(View view) {
        TextView textView = (TextView) view.findViewById(R.id.title);
        ImageView imageView = (ImageView) view.findViewById(R.id.sim1);
        ImageView imageView2 = (ImageView) view.findViewById(R.id.sim2);
        ImageView imageView3 = (ImageView) view.findViewById(R.id.no_default);
        if (textView != null) {
            if (this.mSmsEnabled) {
                textView.setAlpha(1.0f);
            } else {
                textView.setAlpha(0.3f);
            }
            textView.setEnabled(false);
        }
        if (imageView != null) {
            initSmsView(imageView, (DefaultSimBean) this.mSmsSparse.get(0));
        }
        if (imageView2 != null) {
            initSmsView(imageView2, (DefaultSimBean) this.mSmsSparse.get(1));
        }
        if (imageView3 != null) {
            initSmsView(imageView3, (DefaultSimBean) this.mSmsSparse.get(-1));
        }
    }

    private void initSmsView(ImageView imageView, DefaultSimBean defaultSimBean) {
        String string;
        if (defaultSimBean != null) {
            if (defaultSimBean.isChecked) {
                imageView.setBackground(getContext().getResources().getDrawable(R.drawable.default_sms_bg, null));
            } else {
                imageView.setBackground(null);
            }
            if (defaultSimBean.mIsVirtualSim) {
                imageView.setImageResource(VIRTUAL_SIM[defaultSimBean.isChecked ? 1 : 0]);
            } else {
                int i = defaultSimBean.mSlotId;
                if (i == 0) {
                    imageView.setImageResource(SIM1[defaultSimBean.isChecked ? 1 : 0]);
                } else if (i == 1) {
                    boolean z = defaultSimBean.isChecked;
                    imageView.setImageResource(SIM2[z ? 1 : 0]);
                    if (MiuiPhoneUtils.IS_ESIM_CAPABLE && !MiuiPhoneUtils.isSupportMep() && ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isEsimActive()) {
                        imageView.setImageResource(SIM2_ESIM[z ? 1 : 0]);
                    }
                } else if (i == -1) {
                    imageView.setImageResource(NO_DEFAULT[defaultSimBean.isChecked ? 1 : 0]);
                }
            }
            imageView.setOnClickListener(new DefaultSimClickListener(2, defaultSimBean));
            if (this.mSmsEnabled) {
                imageView.setAlpha(1.0f);
            } else {
                imageView.setAlpha(0.3f);
            }
            initAnim(imageView);
            if (defaultSimBean.mSlotId == -1) {
                string = getContext().getResources().getString(R.string.msim_setting_sms_pop_text);
            } else {
                string = String.format(getContext().getResources().getString(R.string.msim_setting_default_sms_description), Integer.valueOf(defaultSimBean.mSlotId + 1));
            }
            initAccessibilityNodeInfo(imageView, this.mSmsEnabled, defaultSimBean.isChecked, string);
        }
    }

    private void initAnim(View view) {
        ITouchStyle iTouchStyle = Folme.useAt(view).touch();
        iTouchStyle.setAlpha(view.getAlpha(), ITouchStyle.TouchType.DOWN).setAlpha(view.getAlpha(), ITouchStyle.TouchType.UP).setTint(0.0f, 0.0f, 0.0f, 0.0f);
        iTouchStyle.handleTouchOf(view, new AnimConfig[0]);
    }

    private void initAccessibilityNodeInfo(ImageView imageView, final boolean z, final boolean z2, final String str) {
        imageView.setAccessibilityDelegate(new View.AccessibilityDelegate(this) { // from class: com.android.phone.settings.sim.DefaultSimPreference.1
            @Override // android.view.View.AccessibilityDelegate
            public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
                super.onInitializeAccessibilityEvent(view, accessibilityEvent);
                accessibilityEvent.setChecked(z2);
            }

            @Override // android.view.View.AccessibilityDelegate
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.setEnabled(z);
                accessibilityNodeInfo.setCheckable(true);
                accessibilityNodeInfo.setChecked(z2);
                accessibilityNodeInfo.setContentDescription(str);
            }
        });
    }

    public boolean isVoice(String str) {
        return !TextUtils.isEmpty(str) && str.startsWith("voice-");
    }

    public boolean isData(String str) {
        return !TextUtils.isEmpty(str) && str.startsWith("data-");
    }

    public boolean isSms(String str) {
        return !TextUtils.isEmpty(str) && str.startsWith("sms-");
    }

    public int getSlotId(String str) {
        return Integer.valueOf(str.replace("voice-", "").replace("data-", "").replace("sms-", "")).intValue();
    }

    class DefaultSimClickListener implements View.OnClickListener {
        private DefaultSimBean mSimBean;
        private int mType;

        private DefaultSimClickListener(int i, DefaultSimBean defaultSimBean) {
            this.mType = i;
            this.mSimBean = defaultSimBean;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Log.i("DefaultSimPreference", "onClick mSimBean = " + this.mSimBean);
            if (this.mSimBean.isChecked) {
                return;
            }
            int i = this.mType;
            if (i == 0) {
                if (DefaultSimPreference.this.mVoiceEnabled) {
                    if (this.mSimBean.mSlotId == -1 && !Utils.isTalkBackEnabled(DefaultSimPreference.this.getContext())) {
                        if (HapticCompat.doesSupportHaptic("2.0")) {
                            new HapticCompat();
                            HapticCompat.performHapticFeedback(view, HapticFeedbackConstants.MIUI_BUTTON_MIDDLE);
                        }
                        GuidePopupWindow guidePopupWindow = new GuidePopupWindow(DefaultSimPreference.this.getContext());
                        guidePopupWindow.setArrowMode(16);
                        guidePopupWindow.setGuideText(R.string.msim_setting_pop_text);
                        guidePopupWindow.show(view, 0, 0, true);
                    }
                    DefaultSimPreference.this.callChangeListener("voice-" + this.mSimBean.mSlotId);
                    return;
                }
                return;
            }
            if (i == 1) {
                if (DefaultSimPreference.this.mDataEnabled) {
                    DefaultSimPreference.this.callChangeListener("data-" + this.mSimBean.mSlotId);
                    return;
                }
                return;
            }
            if (i == 2 && DefaultSimPreference.this.mSmsEnabled) {
                if (this.mSimBean.mSlotId == -1) {
                    if (HapticCompat.doesSupportHaptic("2.0")) {
                        new HapticCompat();
                        HapticCompat.performHapticFeedback(view, HapticFeedbackConstants.MIUI_BUTTON_MIDDLE);
                    }
                    GuidePopupWindow guidePopupWindow2 = new GuidePopupWindow(DefaultSimPreference.this.getContext());
                    guidePopupWindow2.setArrowMode(16);
                    guidePopupWindow2.setGuideText(R.string.msim_setting_sms_pop_text);
                    guidePopupWindow2.show(view, 0, 0, true);
                }
                DefaultSimPreference.this.callChangeListener("sms-" + this.mSimBean.mSlotId);
            }
        }
    }
}
