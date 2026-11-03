package com.android.phone.settings.appfunctions;

import com.android.phone.R;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class DeviceStateConfigProvider {
    public static List getScreenConfigs() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new PerScreenConfig(true, "mobile_network_screen", "Mobile Network Page: Obtain default call SIM card, default data SIM card, mobile data switch, eSIM status."));
        arrayList.add(new PerScreenConfig(true, "access_point_names", "APNs Page: Access the APN (Access Point Name) settings page for SIM card 1 or SIM card 2. To open SIM 1 APN page, set slot_id to 0. To open SIM 2 APN page, set slot_id to 1."));
        arrayList.add(new PerScreenConfig(true, "preferred_network_type", "Preferred Network Type Page: Access the preferred network type settings page for SIM card 1 or SIM card 2. To open SIM 1 page, set slot_id to 0. To open SIM 2 page, set slot_id to 1."));
        arrayList.add(new PerScreenConfig(true, "select_carrier", "Select Carrier Page: Get the status of the automatically selected network for SIM card 1 or SIM card 2. To open SIM 1 page, set slot_id to 0. To open SIM 2 page, set slot_id to 1."));
        arrayList.add(new PerScreenConfig(true, "sim_card_screen", "SIM Card Page: View and edit SIM card name. To open SIM 1 page, set slot_id to 0. To open SIM 2 page, set slot_id to 1."));
        return arrayList;
    }

    public static List getDeviceStateItemList() {
        ArrayList arrayList = new ArrayList();
        int i = R.string.select_automatically;
        arrayList.add(new DeviceStateItemConfig(true, "automatically_select_network", "select_carrier", "", i, i));
        int i2 = R.string.msim_setting_default_voice;
        arrayList.add(new DeviceStateItemConfig(true, "default_for_calls", "mobile_network_screen", "", i2, i2));
        int i3 = R.string.msim_setting_default_data;
        arrayList.add(new DeviceStateItemConfig(true, "default_data", "mobile_network_screen", "", i3, i3));
        int i4 = R.string.data_enabled;
        arrayList.add(new DeviceStateItemConfig(true, "mobile_data", "mobile_network_screen", "", i4, i4));
        int i5 = R.string.enable_esim_title;
        arrayList.add(new DeviceStateItemConfig(true, "turn_on_esim", "mobile_network_screen", "", i5, i5));
        int i6 = R.string.msim_info_editor_name;
        arrayList.add(new DeviceStateItemConfig(true, "sim_card_name", "sim_card_screen", "", i6, i6));
        return arrayList;
    }
}
