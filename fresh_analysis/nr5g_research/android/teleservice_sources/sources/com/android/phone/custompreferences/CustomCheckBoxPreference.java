package com.android.phone.custompreferences;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.preference.CheckBoxPreference;
import androidx.preference.PreferenceViewHolder;
import miuix.view.HapticCompat;
import miuix.view.HapticFeedbackConstants;

/* JADX INFO: loaded from: classes.dex */
public class CustomCheckBoxPreference extends CheckBoxPreference {
    public CustomCheckBoxPreference(Context context) {
        super(context);
    }

    public CustomCheckBoxPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CustomCheckBoxPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public CustomCheckBoxPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    @Override // androidx.preference.CheckBoxPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        final View view = preferenceViewHolder.itemView;
        view.post(new Runnable() { // from class: com.android.phone.custompreferences.CustomCheckBoxPreference$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onBindViewHolder$0(view);
            }
        });
        preferenceViewHolder.findViewById(R.id.checkbox).setOnTouchListener(new View.OnTouchListener() { // from class: com.android.phone.custompreferences.CustomCheckBoxPreference$$ExternalSyntheticLambda1
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view2, MotionEvent motionEvent) {
                return this.f$0.lambda$onBindViewHolder$1(view2, motionEvent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(View view) {
        final View.AccessibilityDelegate accessibilityDelegate = view.getAccessibilityDelegate();
        view.setAccessibilityDelegate(new View.AccessibilityDelegate(this) { // from class: com.android.phone.custompreferences.CustomCheckBoxPreference.1
            @Override // android.view.View.AccessibilityDelegate
            public void onInitializeAccessibilityNodeInfo(View view2, AccessibilityNodeInfo accessibilityNodeInfo) {
                View.AccessibilityDelegate accessibilityDelegate2 = accessibilityDelegate;
                if (accessibilityDelegate2 != null) {
                    accessibilityDelegate2.onInitializeAccessibilityNodeInfo(view2, accessibilityNodeInfo);
                } else {
                    super.onInitializeAccessibilityNodeInfo(view2, accessibilityNodeInfo);
                }
                accessibilityNodeInfo.setCheckable(false);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onBindViewHolder$1(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1) {
            int i = HapticFeedbackConstants.MIUI_MESH_NORMAL;
            if (!HapticCompat.supportLinearMotor(i)) {
                i = HapticFeedbackConstants.MIUI_MESH_LIGHT;
            }
            new HapticCompat();
            HapticCompat.performHapticFeedback(view, HapticFeedbackConstants.MIUI_ZAXIS_SWITCH, i);
            performClick();
        }
        return true;
    }
}
