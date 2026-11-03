package com.android.phone;

import android.content.ContentProvider;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Process;
import android.os.UserHandle;
import android.preference.Preference;
import android.util.Log;
import android.view.MenuItem;
import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.Phone;
import com.ot.pubsub.a.a;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class CdmaCallForwardOptions extends TimeConsumingPreferenceActivity implements PhoneGlobals.SubInfoUpdateListener {
    private static final String[] NUM_PROJECTION = {"data1"};
    private CallForwardEditPreference mButtonCFB;
    private CallForwardEditPreference mButtonCFNRc;
    private CallForwardEditPreference mButtonCFNRy;
    private CallForwardEditPreference mButtonCFU;
    private boolean mCallForwardByUssd;
    private boolean mFirstResume;
    private Bundle mIcicle;
    private Phone mPhone;
    private boolean mReplaceInvalidCFNumbers;
    private SubscriptionInfoHelper mSubscriptionInfoHelper;
    private final ArrayList mPreferences = new ArrayList();
    private int mInitIndex = 0;

    @Override // android.preference.PreferenceActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // com.android.phone.TimeConsumingPreferenceActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if (this.mFirstResume) {
            if (this.mIcicle == null) {
                Log.d("CdmaCallForwardOptions", "start to init ");
                CallForwardEditPreference callForwardEditPreference = (CallForwardEditPreference) this.mPreferences.get(this.mInitIndex);
                callForwardEditPreference.init(this, this.mPhone, this.mReplaceInvalidCFNumbers, this.mCallForwardByUssd);
                callForwardEditPreference.startCallForwardOptionsQuery();
            } else {
                this.mInitIndex = this.mPreferences.size();
                ArrayList arrayList = this.mPreferences;
                int size = arrayList.size();
                int i = 0;
                while (i < size) {
                    Object obj = arrayList.get(i);
                    i++;
                    CallForwardEditPreference callForwardEditPreference2 = (CallForwardEditPreference) obj;
                    Bundle bundle = (Bundle) this.mIcicle.getParcelable(callForwardEditPreference2.getKey());
                    callForwardEditPreference2.setEnabled(bundle.getBoolean("enable"));
                    CallForwardInfo callForwardInfo = new CallForwardInfo();
                    callForwardInfo.number = bundle.getString("number");
                    callForwardInfo.status = bundle.getInt(a.J);
                    callForwardEditPreference2.init(this, this.mPhone, this.mReplaceInvalidCFNumbers, this.mCallForwardByUssd);
                    callForwardEditPreference2.restoreCallForwardInfo(callForwardInfo);
                }
            }
            this.mFirstResume = false;
            this.mIcicle = null;
        }
    }

    @Override // android.preference.PreferenceActivity, android.app.Activity
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        ArrayList arrayList = this.mPreferences;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            CallForwardEditPreference callForwardEditPreference = (CallForwardEditPreference) obj;
            Bundle bundle2 = new Bundle();
            bundle2.putBoolean("toggle", callForwardEditPreference.isToggled());
            bundle2.putBoolean("enable", callForwardEditPreference.isEnabled());
            CallForwardInfo callForwardInfo = callForwardEditPreference.callForwardInfo;
            if (callForwardInfo != null) {
                bundle2.putString("number", callForwardInfo.number);
                bundle2.putInt(a.J, callForwardEditPreference.callForwardInfo.status);
            }
            bundle.putParcelable(callForwardEditPreference.getKey(), bundle2);
        }
    }

    @Override // com.android.phone.TimeConsumingPreferenceActivity, com.android.phone.TimeConsumingPreferenceListener
    public void onFinished(Preference preference, boolean z) {
        if (this.mInitIndex < this.mPreferences.size() - 1 && !isFinishing()) {
            int i = this.mInitIndex + 1;
            this.mInitIndex = i;
            CallForwardEditPreference callForwardEditPreference = (CallForwardEditPreference) this.mPreferences.get(i);
            callForwardEditPreference.init(this, this.mPhone, this.mReplaceInvalidCFNumbers, this.mCallForwardByUssd);
            callForwardEditPreference.startCallForwardOptionsQuery();
        }
        super.onFinished(preference, z);
    }

    @Override // android.preference.PreferenceActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        Log.d("CdmaCallForwardOptions", "onActivityResult: done");
        if (i2 != -1) {
            Log.d("CdmaCallForwardOptions", "onActivityResult: contact picker result not OK.");
            return;
        }
        Cursor cursor = null;
        try {
            int userId = UserHandle.getUserId(Process.myUid());
            if (userId != ContentProvider.getUserIdFromUri(intent.getData(), userId)) {
                Log.w("CdmaCallForwardOptions", "onActivityResult: Contact data of different user, cannot access");
                return;
            }
            Cursor cursorQuery = getContentResolver().query(intent.getData(), NUM_PROJECTION, null, null, null);
            if (cursorQuery != null && cursorQuery.moveToFirst()) {
                if (i == 0) {
                    this.mButtonCFU.onPickActivityResult(cursorQuery.getString(0));
                } else if (i == 1) {
                    this.mButtonCFB.onPickActivityResult(cursorQuery.getString(0));
                } else if (i == 2) {
                    this.mButtonCFNRy.onPickActivityResult(cursorQuery.getString(0));
                } else if (i == 3) {
                    this.mButtonCFNRc.onPickActivityResult(cursorQuery.getString(0));
                }
                cursorQuery.close();
                return;
            }
            Log.d("CdmaCallForwardOptions", "onActivityResult: bad contact data, no results found.");
            if (cursorQuery != null) {
                cursorQuery.close();
            }
        } catch (Throwable th) {
            if (0 != 0) {
                cursor.close();
                throw th;
            }
            throw th;
        }
    }

    @Override // android.preference.PreferenceActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            CallFeaturesSetting.goUpToTopLevelSetting(this, this.mSubscriptionInfoHelper);
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // com.android.phone.PhoneGlobals.SubInfoUpdateListener
    public void handleSubInfoUpdate() {
        finish();
    }
}
