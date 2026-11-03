package com.android.phone.settings.hiddenmenu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.R;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class PhoneInformationV2 extends AppCompatActivity implements View.OnClickListener, PhoneInformationV2PhoneId {
    private LinearLayout itemFourContainer;
    private LinearLayout itemOneContainer;
    private LinearLayout itemThreeContainer;
    private LinearLayout itemTwoContainer;
    private BroadcastReceiver mCarrierConfigReceiver;
    private PhoneInfoSharedViewModel mViewModel;
    private int phoneId;
    private SharedPreferences prefs;
    private List navItemContainers = new ArrayList();
    private int currentlySelectedItem = -1;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (PhoneInformationUtil.isRadioInfoAccessRestricted(this)) {
            finish();
            return;
        }
        setContentView(R.layout.phone_information_v2);
        this.mViewModel = (PhoneInfoSharedViewModel) new ViewModelProvider(this).get(PhoneInfoSharedViewModel.class);
        this.phoneId = 0;
        this.itemOneContainer = (LinearLayout) findViewById(R.id.nav_item_one_container);
        this.itemTwoContainer = (LinearLayout) findViewById(R.id.nav_item_two_container);
        this.itemThreeContainer = (LinearLayout) findViewById(R.id.nav_item_three_container);
        this.itemFourContainer = (LinearLayout) findViewById(R.id.nav_item_four_container);
        this.navItemContainers.add(this.itemOneContainer);
        this.navItemContainers.add(this.itemTwoContainer);
        this.navItemContainers.add(this.itemThreeContainer);
        this.navItemContainers.add(this.itemFourContainer);
        this.itemOneContainer.setOnClickListener(this);
        this.itemTwoContainer.setOnClickListener(this);
        this.itemThreeContainer.setOnClickListener(this);
        this.itemFourContainer.setOnClickListener(this);
        SharedPreferences sharedPreferences = getSharedPreferences("PhoneInfoV2Prefs", 0);
        this.prefs = sharedPreferences;
        selectNavItem(sharedPreferences.getInt("last_selected_nav_item_id", R.id.nav_item_one_container), false);
        if (PhoneInformationUtil.isUserBuild()) {
            this.mCarrierConfigReceiver = new BroadcastReceiver() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationV2.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    if ("android.telephony.action.CARRIER_CONFIG_CHANGED".equals(intent.getAction()) && PhoneInformationUtil.isRadioInfoAccessRestricted(context)) {
                        PhoneInformationV2.this.finish();
                    }
                }
            };
            registerReceiver(this.mCarrierConfigReceiver, new IntentFilter("android.telephony.action.CARRIER_CONFIG_CHANGED"), 2);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        selectNavItem(view.getId(), true);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        if (this.mViewModel != null) {
            CarrierConfigManager carrierConfigManager = (CarrierConfigManager) getSystemService(CarrierConfigManager.class);
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TelephonyManager.class);
            for (int i = 0; i < telephonyManager.getActiveModemCount(); i++) {
                if (PhoneFactory.getPhone(i) != null) {
                    int subscriptionId = SubscriptionManager.getSubscriptionId(i);
                    Log.d("PhoneInformationV2", "onDestroy: subId for phone (" + i + "): " + subscriptionId);
                    if (SubscriptionManager.isValidSubscriptionId(subscriptionId)) {
                        carrierConfigManager.overrideConfig(subscriptionId, null, false);
                        if (Boolean.TRUE.equals(this.mViewModel.getSatelliteDataEnabled(i).getValue())) {
                            PhoneInformationUtil.restoreMaxAllowedDataMode();
                        }
                    }
                }
            }
            this.mViewModel.resetToDefaults();
        }
        super.onDestroy();
        BroadcastReceiver broadcastReceiver = this.mCarrierConfigReceiver;
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
            this.mCarrierConfigReceiver = null;
        }
    }

    private void selectNavItem(int i, boolean z) {
        Fragment phoneInformationV2FragmentIms;
        int i2 = this.currentlySelectedItem;
        if (i2 != i || i2 == -1) {
            if (i == R.id.nav_item_one_container) {
                phoneInformationV2FragmentIms = new PhoneInformationV2FragmentDeviceDetails();
            } else if (i == R.id.nav_item_two_container) {
                phoneInformationV2FragmentIms = new PhoneInformationV2FragmentDataNetwork();
            } else if (i == R.id.nav_item_three_container) {
                phoneInformationV2FragmentIms = new PhoneInformationV2FragmentSatellite();
            } else {
                phoneInformationV2FragmentIms = i == R.id.nav_item_four_container ? new PhoneInformationV2FragmentIms() : null;
            }
            if (phoneInformationV2FragmentIms != null) {
                loadFragment(phoneInformationV2FragmentIms);
                this.currentlySelectedItem = i;
                updateSelectionVisuals(i);
                if (z) {
                    this.prefs.edit().putInt("last_selected_nav_item_id", i).apply();
                    return;
                }
                return;
            }
            this.currentlySelectedItem = i;
            updateSelectionVisuals(i);
            if (z) {
                this.prefs.edit().putInt("last_selected_nav_item_id", i).apply();
            }
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_container, fragment).setTransition(4099).commit();
    }

    private void updateSelectionVisuals(int i) {
        TextView textView;
        ImageView imageView;
        int color = ContextCompat.getColor(this, android.R.color.holo_green_dark);
        int color2 = ContextCompat.getColor(this, android.R.color.darker_gray);
        for (LinearLayout linearLayout : this.navItemContainers) {
            if (linearLayout.getId() == R.id.nav_item_one_container) {
                imageView = (ImageView) linearLayout.findViewById(R.id.nav_item_one_icon);
                textView = (TextView) linearLayout.findViewById(R.id.nav_item_one_label);
            } else if (linearLayout.getId() == R.id.nav_item_two_container) {
                imageView = (ImageView) linearLayout.findViewById(R.id.nav_item_two_icon);
                textView = (TextView) linearLayout.findViewById(R.id.nav_item_two_label);
            } else if (linearLayout.getId() == R.id.nav_item_three_container) {
                imageView = (ImageView) linearLayout.findViewById(R.id.nav_item_three_icon);
                textView = (TextView) linearLayout.findViewById(R.id.nav_item_three_label);
            } else {
                textView = (TextView) linearLayout.findViewById(R.id.nav_item_four_label);
                imageView = null;
            }
            if (linearLayout.getId() == i) {
                if (imageView != null) {
                    imageView.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                }
                if (textView != null) {
                    textView.setTextColor(color);
                }
            } else {
                if (imageView != null) {
                    imageView.setColorFilter(color2, PorterDuff.Mode.SRC_IN);
                }
                if (textView != null) {
                    textView.setTextColor(color2);
                }
            }
        }
    }

    private void notifyOtherFragments(int i) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
        }
    }

    @Override // com.android.phone.settings.hiddenmenu.PhoneInformationV2PhoneId
    public int getPhoneId() {
        return this.phoneId;
    }

    @Override // com.android.phone.settings.hiddenmenu.PhoneInformationV2PhoneId
    public void setPhoneId(int i) {
        this.phoneId = i;
        notifyOtherFragments(i);
    }
}
