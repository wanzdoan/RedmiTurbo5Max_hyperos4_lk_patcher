package com.android.services.telephony;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class HoldTracker {
    private final Set mHoldables = new HashSet();

    public void addHoldable(Holdable holdable) {
        if (this.mHoldables.contains(holdable)) {
            return;
        }
        this.mHoldables.add(holdable);
        updateHoldCapability();
    }

    public void removeHoldable(Holdable holdable) {
        if (this.mHoldables.remove(holdable)) {
            updateHoldCapability();
        }
    }

    public void updateHoldCapability() {
        Iterator it = this.mHoldables.iterator();
        int i = 0;
        while (it.hasNext()) {
            if (!((Holdable) it.next()).isChildHoldable()) {
                i++;
            }
        }
        Log.d(this, "updateHoldCapability(): topHoldableCount = " + i, new Object[0]);
        boolean z = i < 2;
        for (Holdable holdable : this.mHoldables) {
            holdable.setHoldable(holdable.isChildHoldable() ? false : z);
        }
    }
}
