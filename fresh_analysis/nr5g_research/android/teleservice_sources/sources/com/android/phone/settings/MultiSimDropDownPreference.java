package com.android.phone.settings;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import java.util.List;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.widget.Spinner;
import miuix.preference.DropDownPreference;
import miuix.preference.R$id;
import miuix.preference.R$layout;

/* JADX INFO: loaded from: classes.dex */
public class MultiSimDropDownPreference extends DropDownPreference {
    private static final int[] SMALL_SIM_SLOT_ICON = {R.drawable.small_sim_icon_slot_1, R.drawable.small_sim_icon_slot_2};
    private CustomDropDownLayoutAdapter mAdapter;
    private List mSimInfoRecordList;

    public MultiSimDropDownPreference(Context context) {
        super(context);
    }

    public MultiSimDropDownPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R.layout.msim_list_preference_layout);
        this.mSimInfoRecordList = SubscriptionManager.getDefault().getActiveSubscriptionInfoList();
        CustomDropDownLayoutAdapter customDropDownLayoutAdapter = new CustomDropDownLayoutAdapter(context, this.mSimInfoRecordList);
        this.mAdapter = customDropDownLayoutAdapter;
        setAdapter(customDropDownLayoutAdapter);
    }

    @Override // miuix.preference.DropDownPreference, miuix.preference.BasePreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ((Spinner) preferenceViewHolder.itemView.findViewById(R$id.spinner)).setLayoutParams(new LinearLayout.LayoutParams(-2, -1));
    }

    public void notifySimInfoDataChanged() {
        List activeSubscriptionInfoList = SubscriptionManager.getDefault().getActiveSubscriptionInfoList();
        this.mSimInfoRecordList = activeSubscriptionInfoList;
        this.mAdapter.setSimInfoRecordList(activeSubscriptionInfoList);
        setAdapter(this.mAdapter);
        this.mAdapter.notifyDataSetChanged();
    }

    class CustomDropDownLayoutAdapter extends ArrayAdapter {
        private static final int TAG_VIEW = R$id.tag_spinner_dropdown_view;
        private LayoutInflater mInflater;
        private List mSimInfoRecordList;

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public boolean hasStableIds() {
            return true;
        }

        public void setSimInfoRecordList(List list) {
            this.mSimInfoRecordList = list;
        }

        CustomDropDownLayoutAdapter(Context context, List list) {
            super(context, 0);
            this.mInflater = LayoutInflater.from(context);
            this.mSimInfoRecordList = list;
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public Object getItem(int i) {
            SubscriptionInfo subscriptionInfo = (SubscriptionInfo) this.mSimInfoRecordList.get(i);
            Object virtualSimCarrierName = MiuiPhoneUtils.isVirtualSim(subscriptionInfo.getSlotId()) ? MiuiPhoneUtils.getVirtualSimCarrierName() : subscriptionInfo.getDisplayName();
            if (subscriptionInfo.getSlotId() != 0) {
                virtualSimCarrierName = virtualSimCarrierName + " ";
            }
            return String.valueOf(virtualSimCarrierName);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public int getCount() {
            return this.mSimInfoRecordList.size();
        }

        @Override // android.widget.ArrayAdapter, android.widget.BaseAdapter, android.widget.SpinnerAdapter
        public View getDropDownView(int i, View view, ViewGroup viewGroup) {
            if (view == null || view.getTag() == null) {
                view = this.mInflater.inflate(R$layout.miuix_appcompat_spiner_dropdown_view_double_line, viewGroup, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.icon = (ImageView) view.findViewById(android.R.id.icon);
                viewHolder.title = (TextView) view.findViewById(android.R.id.title);
                viewHolder.summary = (TextView) view.findViewById(android.R.id.summary);
                view.setTag(TAG_VIEW, viewHolder);
            }
            SubscriptionInfo subscriptionInfo = (SubscriptionInfo) this.mSimInfoRecordList.get(i);
            String strValueOf = String.valueOf(MiuiPhoneUtils.isVirtualSim(subscriptionInfo.getSlotId()) ? MiuiPhoneUtils.getVirtualSimCarrierName() : subscriptionInfo.getDisplayName());
            String displayNumber = subscriptionInfo.getDisplayNumber();
            int i2 = i >= MultiSimDropDownPreference.SMALL_SIM_SLOT_ICON.length ? -1 : MultiSimDropDownPreference.SMALL_SIM_SLOT_ICON[i];
            if (MiuiPhoneUtils.isValidatePhoneId(subscriptionInfo.getSlotId())) {
                i2 = subscriptionInfo.getSlotId() < MultiSimDropDownPreference.SMALL_SIM_SLOT_ICON.length ? MultiSimDropDownPreference.SMALL_SIM_SLOT_ICON[subscriptionInfo.getSlotId()] : -1;
            }
            Object tag = view.getTag(TAG_VIEW);
            if (tag != null) {
                ViewHolder viewHolder2 = (ViewHolder) tag;
                boolean zIsEmpty = TextUtils.isEmpty(strValueOf);
                TextView textView = viewHolder2.title;
                if (!zIsEmpty) {
                    textView.setText(strValueOf);
                    viewHolder2.title.setEllipsize(TextUtils.TruncateAt.END);
                    viewHolder2.title.setMaxLines(1);
                    viewHolder2.title.setMaxEms(5);
                    viewHolder2.title.setVisibility(0);
                } else {
                    textView.setVisibility(8);
                }
                boolean zIsEmpty2 = TextUtils.isEmpty(displayNumber);
                TextView textView2 = viewHolder2.summary;
                if (!zIsEmpty2) {
                    textView2.setText(displayNumber);
                    viewHolder2.summary.setVisibility(0);
                } else {
                    textView2.setVisibility(8);
                }
                ImageView imageView = viewHolder2.icon;
                if (i2 > 0) {
                    imageView.setImageResource(i2);
                    viewHolder2.icon.setVisibility(0);
                    return view;
                }
                imageView.setVisibility(8);
            }
            return view;
        }

        class ViewHolder {
            ImageView icon;
            TextView summary;
            TextView title;

            private ViewHolder() {
            }
        }
    }
}
