package com.android.phone.settings;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlagsImpl;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.phone.R;
import com.android.phone.satellite.accesscontrol.SatelliteAccessConfigurationParser;
import com.android.phone.satellite.accesscontrol.SatelliteAccessController;
import java.io.File;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteConfigViewer extends Activity {
    private static final String TAG = "SatelliteConfigViewer";
    private TextView mAllowAccess;
    private TextView mConfigAccessJson;
    private TextView mCountryCodes;
    private TextView mMaxAllowedDataMode;
    private SatelliteAccessController mSatelliteAccessController;
    private SatelliteController mSatelliteController;
    private TextView mSatelliteProviderPlmnList;
    private TextView mServiceType;
    private TextView mSizeOfSats2;
    private TextView mSkyloVersion;
    private TextView mStarlinkVersion;
    private int mSubId = -1;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.satellite_config_viewer);
        String str = TAG;
        Log.d(str, "SatelliteConfigViewer: onCreate");
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        this.mSubId = getIntent().getIntExtra("mSubId", SubscriptionManager.getDefaultSubscriptionId());
        Log.d(str, "SatelliteConfigViewer: mSubId: " + this.mSubId);
        this.mStarlinkVersion = (TextView) findViewById(R.id.starlink_version);
        this.mServiceType = (TextView) findViewById(R.id.svc_type);
        this.mMaxAllowedDataMode = (TextView) findViewById(R.id.max_allowed_data_mode);
        this.mSatelliteProviderPlmnList = (TextView) findViewById(R.id.satelltie_provider_plmn_list);
        this.mSkyloVersion = (TextView) findViewById(R.id.skylo_version);
        this.mAllowAccess = (TextView) findViewById(R.id.allow_access);
        this.mCountryCodes = (TextView) findViewById(R.id.country_codes);
        this.mSizeOfSats2 = (TextView) findViewById(R.id.size_of_sats2);
        this.mConfigAccessJson = (TextView) findViewById(R.id.config_json);
        this.mSatelliteController = SatelliteController.getInstance();
        this.mSatelliteAccessController = SatelliteAccessController.getOrCreateInstance(getApplicationContext(), new FeatureFlagsImpl());
        this.mStarlinkVersion.setText(getSatelliteConfigVersionForStarlink());
        this.mServiceType.setText(getSatelliteCarrierConfigUpdateData());
        this.mMaxAllowedDataMode.setText(getMaxAllowedDataMode());
        this.mSatelliteProviderPlmnList.setText(getSatelliteProviderPlmnList());
        this.mSkyloVersion.setText(getSatelliteConfigVersionForSkylo());
        this.mAllowAccess.setText(getSatelliteAllowAccess());
        this.mCountryCodes.setText(getSatelliteConfigCountryCodes());
        this.mSizeOfSats2.setText(getSatelliteS2SatFileSize(getApplicationContext()));
        this.mConfigAccessJson.setText(getSatelliteConfigJsonFile(getApplicationContext()));
    }

    private String getSatelliteConfigVersionForStarlink() {
        logd("getSatelliteConfigVersionForStarlink");
        return Integer.toString(this.mSatelliteController.getSatelliteConfig() != null ? this.mSatelliteController.getSatelliteConfig().getSatelliteConfigDataVersion() : 0);
    }

    private String getSatelliteConfigVersionForSkylo() {
        logd("getSatelliteConfigVersionForSkylo");
        return Integer.toString(this.mSatelliteAccessController.getSatelliteAccessConfigVersion());
    }

    private String getMaxAllowedDataMode() {
        logd("getMaxAllowedDataMode");
        int maxAllowedDataMode = this.mSatelliteController.getMaxAllowedDataMode();
        if (maxAllowedDataMode == 0) {
            return maxAllowedDataMode + " (SATELLITE_DATA_SUPPORT_ONLY_RESTRICTED)";
        }
        if (maxAllowedDataMode == 1) {
            return maxAllowedDataMode + " (SATELLITE_DATA_SUPPORT_BANDWIDTH_CONSTRAINED)";
        }
        if (maxAllowedDataMode == 2) {
            return maxAllowedDataMode + " (SATELLITE_DATA_SUPPORT_ALL)";
        }
        return "Not ready to show mSatelliteController.getMaxAllowedDataMode()";
    }

    private String getSatelliteProviderPlmnList() {
        logd("getSatelliteProviderPlmnList");
        return String.join(", ", this.mSatelliteController.getAllPlmnSet());
    }

    private String getSatelliteCarrierConfigUpdateData() {
        logd("getSatelliteCarrierConfigUpdateData");
        HashMap map = new HashMap();
        for (String str : this.mSatelliteController.getSatellitePlmnsForCarrier(this.mSubId)) {
            map.put(str, this.mSatelliteController.getSupportedSatelliteServicesForPlmn(this.mSubId, str));
        }
        logd("getSatelliteCarrierConfigUpdateData: subId: " + this.mSubId + ": " + map);
        return "subId: " + this.mSubId + ": " + map;
    }

    private String getSatelliteAllowAccess() {
        logd("getSatelliteAllowAccess");
        return Boolean.toString(this.mSatelliteAccessController.isSatelliteAllowAccessControl());
    }

    private String getSatelliteConfigCountryCodes() {
        logd("getSatelliteConfigCountryCodes");
        return String.join(",", this.mSatelliteAccessController.getSatelliteCountryCodes());
    }

    private String getSatelliteConfigJsonFile(Context context) {
        logd("getSatelliteConfigJsonFile");
        File satelliteAccessConfigFile = this.mSatelliteAccessController.getSatelliteAccessConfigFile();
        if (satelliteAccessConfigFile == null) {
            loge("getSatelliteConfigJsonFile: satellite access config json file is null");
            return "satellite access config json file is not ready";
        }
        return SatelliteAccessConfigurationParser.readJsonStringFromFile(satelliteAccessConfigFile.getAbsolutePath());
    }

    private String getSatelliteS2SatFileSize(Context context) {
        logd("getSatelliteS2SatFileSize");
        File satelliteS2CellFile = this.mSatelliteAccessController.getSatelliteS2CellFile();
        if (satelliteS2CellFile == null) {
            loge("getSatelliteS2SatFileSize: s2satFile is null");
            return "s2satFile is null";
        }
        return Long.toString(satelliteS2CellFile.length());
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private static void logd(String str) {
        Log.d(TAG, str);
    }

    private static void loge(String str) {
        Log.e(TAG, str);
    }
}
