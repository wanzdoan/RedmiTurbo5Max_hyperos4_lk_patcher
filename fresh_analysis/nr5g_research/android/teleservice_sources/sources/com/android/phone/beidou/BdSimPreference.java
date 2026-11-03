package com.android.phone.beidou;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class BdSimPreference extends Preference {
    private static final int[] SIM_ICON = {R.drawable.ic_sim_1_big, R.drawable.ic_sim_2_big};
    private static final int SLOT_NUM = MiuiPhoneUtils.PHONE_COUNT;
    private List mSimInfoRecordList;
    private SparseArray mSimInfoSparse;

    public BdSimPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSimInfoRecordList = new ArrayList();
        this.mSimInfoSparse = new SparseArray();
        setLayoutResource(R.layout.bd_sim_preference);
        setVisible(false);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        View view = preferenceViewHolder.itemView;
        View viewFindViewById = view.findViewById(R.id.bd_sim_1);
        View viewFindViewById2 = view.findViewById(R.id.bd_sim_2);
        if (viewFindViewById == null || viewFindViewById2 == null) {
            return;
        }
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(this.mSimInfoSparse.get(0));
        initSimView(viewFindViewById, null);
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(this.mSimInfoSparse.get(1));
        initSimView(viewFindViewById2, null);
    }

    private void initSimView(View view, BdSimInfoBean bdSimInfoBean) {
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.bd_sim_info);
        TextView textView = (TextView) view.findViewById(R.id.title);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon);
        TextView textView2 = (TextView) view.findViewById(R.id.summary);
        TextView textView3 = (TextView) view.findViewById(R.id.bd_sim_state_text);
        if (linearLayout == null || textView == null || imageView == null || textView2 == null || textView3 == null) {
            Log.e("BdSimPreference", "initSimView: view is null");
            return;
        }
        throw null;
    }
}
