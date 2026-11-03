package miuix.preference.utils;

import androidx.preference.Preference;
import miuix.preference.PreferencedynamicGroupController;

/* JADX INFO: loaded from: classes2.dex */
public abstract class PreferenceLayoutUtils {
    /* JADX WARN: Multi-variable type inference failed */
    public static boolean isDynamicGroupItem(Preference preference) {
        int groupItemType;
        return (preference instanceof PreferencedynamicGroupController) && (groupItemType = ((PreferencedynamicGroupController) preference).getGroupItemType()) > 0 && groupItemType < 5;
    }
}
