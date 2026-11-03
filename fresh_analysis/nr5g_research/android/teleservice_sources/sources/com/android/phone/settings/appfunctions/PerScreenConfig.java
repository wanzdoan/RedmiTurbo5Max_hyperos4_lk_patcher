package com.android.phone.settings.appfunctions;

import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class PerScreenConfig {
    private final Set category;
    private final String description;
    private final boolean enabled;
    private final String screenKey;

    public PerScreenConfig(boolean z, String str, String str2, Set set) {
        this.enabled = z;
        this.screenKey = str;
        this.description = str2;
        this.category = set != null ? new HashSet(set) : new HashSet();
    }

    public PerScreenConfig(boolean z, String str, String str2) {
        this(z, str, str2, new HashSet() { // from class: com.android.phone.settings.appfunctions.PerScreenConfig.1
            {
                add(DeviceStateAppFunctionType.GET_UNCATEGORIZED);
            }
        });
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public String getScreenKey() {
        return this.screenKey;
    }

    public Set getCategory() {
        return new HashSet(this.category);
    }

    public String getDescription() {
        return this.description;
    }
}
