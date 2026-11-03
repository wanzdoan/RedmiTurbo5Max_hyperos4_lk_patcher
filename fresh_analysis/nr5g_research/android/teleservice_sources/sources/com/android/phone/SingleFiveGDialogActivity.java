package com.android.phone;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.phone.utils.FiveGUtils;
import java.util.ArrayList;
import java.util.List;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.AppCompatActivity;

/* JADX INFO: loaded from: classes.dex */
public class SingleFiveGDialogActivity extends AppCompatActivity implements SubscriptionManager.OnSubscriptionsChangedListener {
    private static String LOG_TAG = "SingleFiveGDialogActivity";
    private static final int[] SMALL_SIM_SLOT_ICON = {R.drawable.small_sim_icon_slot_1, R.drawable.small_sim_icon_slot_2};
    private MultiSimAdapter mMultiSimAdapter;
    private AlertDialog mDialog = null;
    private List mSimInfoRecordList = new ArrayList();
    private int mSlotIdToBeDeactivate = -1;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getIntent() == null) {
            return;
        }
        int intExtra = getIntent().getIntExtra("dialog_type", 0);
        log("dialogType=" + intExtra);
        if (intExtra != 0) {
            showDialog(intExtra);
        } else {
            finish();
        }
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int i) {
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mDialog = null;
        }
        this.mSlotIdToBeDeactivate = FiveGUtils.getViceSlotId();
        if (i == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight);
            builder.setTitle(R.string.single_fiveg_deactivate_sim_dialog_title_select_deactivate_sim);
            this.mMultiSimAdapter = new MultiSimAdapter(this);
            SubscriptionManager.getDefault().addOnSubscriptionsChangedListener(this);
            notifySimInfoDataChanged();
            builder.setSingleChoiceItems(this.mMultiSimAdapter, this.mSlotIdToBeDeactivate, new DialogInterface.OnClickListener() { // from class: com.android.phone.SingleFiveGDialogActivity.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    SingleFiveGDialogActivity.this.mSlotIdToBeDeactivate = i2;
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.SingleFiveGDialogActivity.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    SingleFiveGController singleFiveGController = SingleFiveGController.getInstance();
                    if (singleFiveGController != null) {
                        singleFiveGController.showSingle5GPromptDialog();
                    }
                    SingleFiveGDialogActivity.this.finish();
                }
            });
            builder.setPositiveButton(R.string.single_fiveg_deactivate_sim_dialog_btn_disable_sim, new DialogInterface.OnClickListener() { // from class: com.android.phone.SingleFiveGDialogActivity.3
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    SingleFiveGController singleFiveGController = SingleFiveGController.getInstance();
                    if (singleFiveGController != null) {
                        singleFiveGController.deactivateSimAndEnable5G(SingleFiveGDialogActivity.this.mSlotIdToBeDeactivate);
                    }
                    SingleFiveGDialogActivity.this.finish();
                }
            });
            builder.setCancelable(false);
            AlertDialog alertDialogCreate = builder.create();
            this.mDialog = alertDialogCreate;
            alertDialogCreate.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.android.phone.SingleFiveGDialogActivity.4
                @Override // android.content.DialogInterface.OnDismissListener
                public void onDismiss(DialogInterface dialogInterface) {
                    SingleFiveGDialogActivity.this.removeOnSubscriptionsChangedListener();
                }
            });
            return this.mDialog;
        }
        return super.onCreateDialog(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeOnSubscriptionsChangedListener() {
        SubscriptionManager.getDefault().removeOnSubscriptionsChangedListener(this);
    }

    public void onSubscriptionsChanged() {
        notifySimInfoDataChanged();
    }

    public void notifySimInfoDataChanged() {
        List subscriptionInfoList = SubscriptionManager.getDefault().getSubscriptionInfoList();
        this.mSimInfoRecordList = subscriptionInfoList;
        if ((subscriptionInfoList != null ? subscriptionInfoList.size() : 0) < MiuiPhoneUtils.PHONE_COUNT) {
            finish();
        } else {
            this.mMultiSimAdapter.notifyDataSetChanged();
        }
    }

    class MultiSimAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        MultiSimAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override // android.widget.Adapter
        public SubscriptionInfo getItem(int i) {
            return (SubscriptionInfo) SingleFiveGDialogActivity.this.mSimInfoRecordList.get(i);
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return SingleFiveGDialogActivity.this.mSimInfoRecordList.size();
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = this.mInflater.inflate(R.layout.msim_list_item, (ViewGroup) null);
                viewHolder = new ViewHolder();
                viewHolder.simDisplayName = (CheckedTextView) view.findViewById(R.id.txt_sim_displayname);
                viewHolder.simIcon = (ImageView) view.findViewById(R.id.img_sim_icon);
                viewHolder.simNumber = (TextView) view.findViewById(R.id.txt_sim_number);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            SubscriptionInfo item = getItem(i);
            boolean zIsVirtualSim = MiuiPhoneUtils.isVirtualSim(item.getSlotId());
            viewHolder.simDisplayName.setText(zIsVirtualSim ? MiuiPhoneUtils.getVirtualSimCarrierName() : item.getDisplayName());
            int i2 = zIsVirtualSim ? R.drawable.mi_small_sim_icon : SingleFiveGDialogActivity.SMALL_SIM_SLOT_ICON[item.getSlotId()];
            ImageView imageView = viewHolder.simIcon;
            if (i2 > 0) {
                imageView.setImageResource(i2);
            } else {
                imageView.setVisibility(8);
            }
            String displayNumber = item.getDisplayNumber();
            viewHolder.simNumber.setText(displayNumber);
            viewHolder.simIcon.setVisibility(0);
            viewHolder.simNumber.setSelected(true);
            viewHolder.simNumber.setVisibility(TextUtils.isEmpty(displayNumber) ? 8 : 0);
            return view;
        }

        class ViewHolder {
            CheckedTextView simDisplayName;
            ImageView simIcon;
            TextView simNumber;

            private ViewHolder(MultiSimAdapter multiSimAdapter) {
            }
        }
    }

    @Override // miuix.appcompat.app.AppCompatActivity, android.app.Activity
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mDialog = null;
        }
        if (this.mMultiSimAdapter != null) {
            this.mMultiSimAdapter = null;
        }
    }

    private static void log(String str) {
        Log.d(LOG_TAG, str);
    }
}
