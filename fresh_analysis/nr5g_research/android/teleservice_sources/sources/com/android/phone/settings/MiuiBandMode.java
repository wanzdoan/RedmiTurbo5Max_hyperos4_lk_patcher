package com.android.phone.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.R;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.util.ArrayList;
import java.util.Iterator;
import miuix.appcompat.app.AppCompatActivity;

/* JADX INFO: loaded from: classes.dex */
public class MiuiBandMode extends AppCompatActivity {
    private static final String[] BAND_NAMES = {"LTE B1 Preferred", "LTE B2 Preferred", "LTE B3 Preferred", "LTE B4 Preferred", "LTE B5 Preferred", "LTE B7 Preferred", "LTE B8 Preferred", "LTE B20 Preferred", "LTE B28 Preferred", "LTE B38 Preferred", "LTE B40 Preferred", "LTE B41 Preferred", "GSM 900", "GSM 1800", "GSM 1900", "WCDMA I 2100", "WCDMA II 1900", "WCDMA IV 1700", "WCDMA V 850", "WCDMA V VIII 900", "LTE B1", "LTE B2", "LTE B3", "LTE B4", "LTE B5", "LTE B7", "LTE B8", "LTE B20", "LTE B28", "LTE B38", "LTE B40", "LTE B41"};
    public static final long[] BAND_VALUES = {0, 1, 2, 3, 4, 6, 7, 25, 38, 20, 22, 29, 512, 128, 2097152, 4194304, 8388608, 33554432, 67108864, 562949953421312L, 1, 2, 4, 8, 16, 64, 128, 524288, 134217728, 137438953472L, 549755813888L, 1099511627776L};
    private ListView mBandList;
    private CustimizedArrayAdapter mBandListAdapter;
    private Switch mSelection;
    ArrayList mBandsToSet = new ArrayList();
    private Phone mPhone = null;
    private AdapterView.OnItemClickListener mBandSelectionHandler = new AdapterView.OnItemClickListener() { // from class: com.android.phone.settings.MiuiBandMode.3
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView adapterView, View view, int i, long j) {
            BandListItem bandListItem = (BandListItem) adapterView.getAdapter().getItem(i);
            boolean zContains = MiuiBandMode.this.mBandsToSet.contains(bandListItem);
            MiuiBandMode miuiBandMode = MiuiBandMode.this;
            if (zContains) {
                miuiBandMode.mBandsToSet.remove(bandListItem);
                bandListItem.mChecked = false;
            } else {
                miuiBandMode.mBandsToSet.add(bandListItem);
                bandListItem.mChecked = true;
            }
        }
    };
    private Handler mHandler = new Handler() { // from class: com.android.phone.settings.MiuiBandMode.4
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 200 && !MiuiBandMode.this.isFinishing()) {
                MiuiBandMode.this.displayBandSelectionResult(null);
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.miui_band_mode);
        this.mPhone = PhoneFactory.getPhone(getIntent().getIntExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, 0));
        this.mBandList = (ListView) findViewById(R.id.miui_band);
        this.mBandListAdapter = new CustimizedArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice);
        for (int i = 0; i < BAND_NAMES.length; i++) {
            this.mBandListAdapter.add(new BandListItem(i, BAND_VALUES[i], false));
        }
        this.mBandList.setAdapter((ListAdapter) this.mBandListAdapter);
        this.mBandList.setChoiceMode(2);
        this.mBandList.setOnItemClickListener(this.mBandSelectionHandler);
        Switch r7 = (Switch) findViewById(R.id.btn_select);
        this.mSelection = r7;
        r7.setOnClickListener(new View.OnClickListener() { // from class: com.android.phone.settings.MiuiBandMode.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                boolean zIsChecked = MiuiBandMode.this.mSelection.isChecked();
                MiuiBandMode miuiBandMode = MiuiBandMode.this;
                if (!zIsChecked) {
                    miuiBandMode.resetBands();
                    MiuiBandMode.this.mBandList.clearChoices();
                    MiuiBandMode.this.mBandsToSet.clear();
                    MiuiBandMode.this.mBandListAdapter.notifyDataSetChanged();
                    return;
                }
                if (miuiBandMode.mBandsToSet.isEmpty()) {
                    return;
                }
                new Thread(new Runnable() { // from class: com.android.phone.settings.MiuiBandMode.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        MiuiBandMode.this.handleBandsSet();
                        MiuiBandMode.this.mPhone.setRadioPower(false);
                        MiuiBandMode.this.mPhone.setRadioPower(true);
                        MiuiBandMode.this.mHandler.sendMessageDelayed(MiuiBandMode.this.mHandler.obtainMessage(200), 500L);
                    }
                }).start();
            }
        });
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String string = defaultSharedPreferences.getString("BAND_INFO_STORE" + this.mPhone.getPhoneId(), "");
        boolean z = defaultSharedPreferences.getBoolean("BAND_INFO_SELECTION" + this.mPhone.getPhoneId(), false);
        if (!string.isEmpty()) {
            Log.i("MiuiBandMode", "bandInfoStored: ".concat(string));
            for (String str : string.split(",")) {
                this.mBandList.setItemChecked(Integer.parseInt(str), true);
                this.mBandListAdapter.notifyDataSetChanged();
            }
        }
        this.mSelection.setChecked(z);
    }

    private boolean isLtePrefBandSetBefore() {
        ArrayList arrayList = this.mBandsToSet;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            int i2 = ((BandListItem) obj).mPosition;
            Log.i("MiuiBandMode", "isLtePrefBandSetBefore position: " + i2);
            if (i2 >= 0 && i2 <= 11) {
                Log.i("MiuiBandMode", "isLtePrefBandSetBefore Yes");
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetBands() {
        long j = 0;
        int i = 0;
        while (true) {
            long[] jArr = BAND_VALUES;
            if (i >= jArr.length) {
                final int[] iArr = {0};
                final long[] jArr2 = {1152921504606846975L, j};
                final boolean zIsLtePrefBandSetBefore = isLtePrefBandSetBefore();
                Log.i("MiuiBandMode", "resetBands  shouldResetLtePref:" + zIsLtePrefBandSetBefore);
                new Thread(new Runnable() { // from class: com.android.phone.settings.MiuiBandMode.2
                    @Override // java.lang.Runnable
                    public void run() {
                        if (zIsLtePrefBandSetBefore) {
                            Log.i("MiuiBandMode", "resetBands  setLteBandPref");
                            VendorUtilsBase.getInstance().setLteBandPref(iArr, MiuiBandMode.this.mPhone.getPhoneId());
                        }
                        VendorUtilsBase.getInstance().setXiaomiBandMode(jArr2, MiuiBandMode.this.mPhone.getPhoneId());
                    }
                }).start();
                return;
            }
            if (i > 19) {
                j |= jArr[i];
            }
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBandsSet() {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = this.mBandsToSet;
        int size = arrayList2.size();
        long j = 0;
        long j2 = 0;
        int i = 0;
        while (i < size) {
            Object obj = arrayList2.get(i);
            i++;
            BandListItem bandListItem = (BandListItem) obj;
            int i2 = bandListItem.mPosition;
            long j3 = bandListItem.mBandMode;
            Log.i("MiuiBandMode", "handleBandsSet  position:" + i2 + " band:" + bandListItem.toString() + " mode:" + j3);
            if (i2 >= 0 && i2 <= 11) {
                arrayList.add(Long.valueOf(j3));
            } else if (i2 < 12 || i2 > 19) {
                j2 |= j3;
            } else {
                j |= j3;
            }
        }
        Log.i("MiuiBandMode", "=============BANDSETBEGIN======================");
        if (!arrayList.isEmpty()) {
            int[] iArr = new int[arrayList.size() + 1];
            iArr[0] = arrayList.size();
            int i3 = 0;
            while (i3 < arrayList.size()) {
                int i4 = i3 + 1;
                iArr[i4] = (int) ((Long) arrayList.get(i3)).longValue();
                Log.i("MiuiBandMode", "handleBandsSet LtePrefBandMode " + iArr[i4]);
                i3 = i4;
            }
            VendorUtilsBase.getInstance().setLteBandPref(iArr, this.mPhone.getPhoneId());
        }
        if (j != 0 || j2 != 0) {
            VendorUtilsBase.getInstance().setXiaomiBandMode(new long[]{j, j2}, this.mPhone.getPhoneId());
            Log.i("MiuiBandMode", "handleBandsSet gwBand " + j + " lteBand:" + j2);
        }
        Log.i("MiuiBandMode", "==============BANDSETEND=====================");
    }

    class BandListItem {
        public long mBandMode;
        public boolean mChecked;
        public int mPosition;

        public BandListItem(int i, long j, boolean z) {
            this.mPosition = i;
            this.mBandMode = j;
            this.mChecked = z;
        }

        public String toString() {
            if (this.mPosition < MiuiBandMode.BAND_NAMES.length) {
                return MiuiBandMode.BAND_NAMES[this.mPosition];
            }
            return "Band mode " + this.mBandMode;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void displayBandSelectionResult(Throwable th) {
        String str;
        String string = getString(R.string.band_mode_set);
        if (th != null) {
            str = string + getString(R.string.band_mode_failed);
        } else {
            str = string + getString(R.string.band_mode_succeeded);
        }
        Toast.makeText(this, str, 0).show();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        Iterator it = this.mBandsToSet.iterator();
        while (it.hasNext()) {
            this.mBandList.setItemChecked(((BandListItem) it.next()).mPosition, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        saveBandPosition();
    }

    private void saveBandPosition() {
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(this).edit();
        Iterator it = this.mBandsToSet.iterator();
        String str = "";
        while (it.hasNext()) {
            str = str + ((BandListItem) it.next()).mPosition + ",";
        }
        Log.i("MiuiBandMode", "saveBandPosition bandPosition:".concat(str));
        editorEdit.putBoolean("BAND_INFO_SELECTION" + this.mPhone.getPhoneId(), this.mSelection.isChecked());
        editorEdit.putString("BAND_INFO_STORE" + this.mPhone.getPhoneId(), str);
        editorEdit.commit();
    }

    class CustimizedArrayAdapter extends ArrayAdapter {
        public CustimizedArrayAdapter(Context context, int i) {
            super(context, i);
        }

        @Override // android.widget.BaseAdapter, android.widget.ListAdapter
        public boolean isEnabled(int i) {
            if (MiuiBandMode.this.mSelection == null || !MiuiBandMode.this.mSelection.isChecked()) {
                return super.isEnabled(i);
            }
            return false;
        }

        @Override // android.widget.BaseAdapter, android.widget.ListAdapter
        public boolean areAllItemsEnabled() {
            if (MiuiBandMode.this.mSelection == null || !MiuiBandMode.this.mSelection.isChecked()) {
                return super.areAllItemsEnabled();
            }
            return false;
        }
    }
}
