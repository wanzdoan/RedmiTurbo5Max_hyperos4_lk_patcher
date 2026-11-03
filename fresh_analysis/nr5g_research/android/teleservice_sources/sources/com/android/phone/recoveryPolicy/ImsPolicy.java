package com.android.phone.recoveryPolicy;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class ImsPolicy extends Policy {
    public Policy.Action getAction(String str, String str2, String str3, String str4, String str5) {
        boolean z;
        for (Policy.Action action : this.actionList) {
            boolean z2 = true;
            if (!action.conditions.containsKey("pdnEndType")) {
                z = false;
            } else if (!TextUtils.isEmpty(str4) && str4.equals(action.conditions.get("pdnEndType"))) {
                z = true;
            }
            if (action.conditions.containsKey("errorCode")) {
                ArrayList arrayList = new ArrayList(Arrays.asList(((String) action.conditions.get("errorCode")).split(",")));
                if (!TextUtils.isEmpty(str) && arrayList.contains(str)) {
                    z = true;
                }
            }
            if (action.conditions.containsKey("roam")) {
                if (!TextUtils.isEmpty(str2) && str2.equals(action.conditions.get("roam"))) {
                    z = true;
                }
            }
            if (action.conditions.containsKey("rat")) {
                if (!TextUtils.isEmpty(str3) && str3.equals(action.conditions.get("rat"))) {
                    z = true;
                }
            }
            if (!action.conditions.containsKey("pdpStatus")) {
                z2 = z;
            } else if (TextUtils.isEmpty(str5) || !str5.equals(action.conditions.get("pdpStatus"))) {
            }
            if (z2) {
                return action;
            }
        }
        return null;
    }
}
