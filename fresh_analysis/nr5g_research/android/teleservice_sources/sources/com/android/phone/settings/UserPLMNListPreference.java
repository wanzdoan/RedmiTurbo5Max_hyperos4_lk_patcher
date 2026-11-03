package com.android.phone.settings;

import android.content.Intent;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.R;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.utils.Utils;
import com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import miui.telephony.ServiceProviderUtils;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.app.ActionBar;
import miuix.internal.util.AttributeResolver;
import miuix.preference.R$attr;

/* JADX INFO: loaded from: classes.dex */
public class UserPLMNListPreference extends TimeConsumingPreferenceActivity {
    private UserPLMNListPreferenceFragment mUserPLMNListPreferenceFragment;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.TimeConsumingPreferenceActivity, com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getApplicationInfo().targetSdkVersion >= 35) {
            getWindow().addFlags(134217728);
        }
        UserPLMNListPreferenceFragment userPLMNListPreferenceFragment = (UserPLMNListPreferenceFragment) getSupportFragmentManager().findFragmentByTag("miuix.preference.PreferenceFragment.MAIN_FRAGMENT_TAG");
        this.mUserPLMNListPreferenceFragment = userPLMNListPreferenceFragment;
        if (bundle == null && userPLMNListPreferenceFragment == null) {
            UserPLMNListPreferenceFragment userPLMNListPreferenceFragment2 = new UserPLMNListPreferenceFragment();
            this.mUserPLMNListPreferenceFragment = userPLMNListPreferenceFragment2;
            loadFragment(userPLMNListPreferenceFragment2);
        }
    }

    @Override // com.android.phone.settings.BaseActivity
    protected void setActionBar() {
        super.setActionBar();
        ActionBar appCompatActionBar = getAppCompatActionBar();
        if (appCompatActionBar != null) {
            appCompatActionBar.setTitle(R.string.uplmn_list_setting_title);
        }
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, R.string.uplmn_list_setting_add_plmn).setIcon(AttributeResolver.resolveDrawable(this, R$attr.actionBarNewIcon)).setShowAsAction(1);
        return true;
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            menu.setGroupEnabled(0, !this.mUserPLMNListPreferenceFragment.isAirplaneOn());
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override // com.android.phone.settings.BaseActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 1) {
            Intent intent = new Intent(this, (Class<?>) UPLMNEditor.class);
            intent.putExtra("uplmn_code", "");
            intent.putExtra("uplmn_priority", 0);
            intent.putExtra("uplmn_service", 0);
            intent.putExtra("uplmn_add", true);
            intent.putExtra("uplmn_size", this.mUserPLMNListPreferenceFragment.mUPLMNList.size());
            intent.putExtra(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID, this.mUserPLMNListPreferenceFragment.mSlotId);
            startActivityForResult(intent, 101);
        } else if (menuItem.getItemId() == 16908332) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public class UserPLMNListPreferenceFragment extends TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment {
        private Utils.UPLMNInfoWithEf mOldInfo;
        private Phone mPhone;
        private int mSlotId;
        private List mUPLMNList;
        private PreferenceCategory mUPLMNListContainer;
        private IccFileHandler mIccFileHandler = null;
        private Map mPreferenceMap = new LinkedHashMap();
        private MyHandler mHandler = new MyHandler();

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            super.onCreatePreferences(bundle, str);
            int slotIdExtra = SubscriptionManager.getSlotIdExtra(getActivity().getIntent(), SubscriptionManager.getDefault().getDefaultSlotId());
            this.mSlotId = slotIdExtra;
            this.mPhone = MiuiPhoneUtils.getPhone(slotIdExtra);
            addPreferencesFromResource(R.xml.uplmn_list);
            this.mUPLMNListContainer = (PreferenceCategory) findPreference("button_uplmn_list_key");
            loadIccFileHandler();
        }

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, androidx.fragment.app.Fragment
        public void onDestroy() {
            super.onDestroy();
        }

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            init(this, false);
        }

        private void init(TimeConsumingPreferenceListener timeConsumingPreferenceListener, boolean z) {
            Log.d("UserPLMNListPreference", "init ... ...");
            if (z) {
                return;
            }
            getUPLMNInfoFromEf();
            if (timeConsumingPreferenceListener != null) {
                timeConsumingPreferenceListener.onStarted(this.mUPLMNListContainer, true);
            }
        }

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, com.android.phone.settings.TimeConsumingPreferenceListener
        public void onFinished(Preference preference, boolean z) {
            super.onFinished(preference, z);
            setScreenEnabled();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void getUPLMNInfoFromEf() {
            ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getPlmnList(this.mPhone, this.mIccFileHandler, this.mHandler.obtainMessage(0));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void refreshUPLMNListPreference(ArrayList arrayList) {
            if (this.mUPLMNListContainer.getPreferenceCount() != 0) {
                this.mUPLMNListContainer.removeAll();
            }
            Map map = this.mPreferenceMap;
            if (map != null) {
                map.clear();
            }
            List list = this.mUPLMNList;
            if (list != null) {
                list.clear();
            }
            this.mUPLMNList = arrayList;
            if (arrayList == null) {
                Log.d("UserPLMNListPreference", "refreshUPLMNListPreference : NULL UPLMN list!");
            } else {
                Log.d("UserPLMNListPreference", "refreshUPLMNListPreference : list.size() = " + arrayList.size());
            }
            if (arrayList == null || arrayList.size() == 0) {
                Log.d("UserPLMNListPreference", "refreshUPLMNListPreference : NULL UPLMN list!");
                if (arrayList == null) {
                    this.mUPLMNList = new ArrayList();
                    return;
                }
                return;
            }
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                Utils.UPLMNInfoWithEf uPLMNInfoWithEf = (Utils.UPLMNInfoWithEf) obj;
                addUPLMNPreference(uPLMNInfoWithEf);
                Log.d("UserPLMNListPreference", uPLMNInfoWithEf.toString());
            }
        }

        class PriorityCompare implements Comparator {
            PriorityCompare(UserPLMNListPreferenceFragment userPLMNListPreferenceFragment) {
            }

            @Override // java.util.Comparator
            public int compare(Utils.UPLMNInfoWithEf uPLMNInfoWithEf, Utils.UPLMNInfoWithEf uPLMNInfoWithEf2) {
                return uPLMNInfoWithEf.getPriority() - uPLMNInfoWithEf2.getPriority();
            }
        }

        private void addUPLMNPreference(Utils.UPLMNInfoWithEf uPLMNInfoWithEf) {
            Preference preference = new Preference(getPreferenceManager().getContext());
            preference.setTitle(uPLMNInfoWithEf.getOperatorAlphaNumeric() + "(" + getNetWorkModeString(uPLMNInfoWithEf.getNetworkMode(), uPLMNInfoWithEf.getOperatorNumeric()) + ")");
            this.mUPLMNListContainer.addPreference(preference);
            this.mPreferenceMap.put(preference, uPLMNInfoWithEf);
        }

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, miuix.preference.PreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.OnPreferenceTreeClickListener
        public boolean onPreferenceTreeClick(Preference preference) {
            Intent intent = new Intent(getContext(), (Class<?>) UPLMNEditor.class);
            Utils.UPLMNInfoWithEf uPLMNInfoWithEf = (Utils.UPLMNInfoWithEf) this.mPreferenceMap.get(preference);
            this.mOldInfo = uPLMNInfoWithEf;
            intent.putExtra("uplmn_code", uPLMNInfoWithEf.getOperatorNumeric());
            intent.putExtra("uplmn_priority", uPLMNInfoWithEf.getPriority());
            intent.putExtra("uplmn_service", uPLMNInfoWithEf.getNetworkMode());
            intent.putExtra("uplmn_add", false);
            intent.putExtra("uplmn_size", this.mUPLMNList.size());
            startActivityForResult(intent, 102);
            return true;
        }

        private Utils.UPLMNInfoWithEf createNetworkInfofromIntent(Intent intent) {
            String stringExtra = intent.getStringExtra("uplmn_code");
            int intExtra = intent.getIntExtra("uplmn_priority", 0);
            return new Utils.UPLMNInfoWithEf(this.mSlotId, stringExtra, intent.getIntExtra("uplmn_service", 0), intExtra);
        }

        private void handleSetUPLMN(ArrayList arrayList) {
            onStarted(this.mUPLMNListContainer, false);
            ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setPlmnList(this.mPhone, this.mIccFileHandler, arrayList, this.mHandler.obtainMessage(1));
        }

        private void loadIccFileHandler() {
            Log.d("UserPLMNListPreference", "loadIccFileHandler()");
            UiccController uiccController = UiccController.getInstance();
            UiccCard uiccCard = uiccController != null ? uiccController.getUiccCard(this.mSlotId) : null;
            Log.d("UserPLMNListPreference", "newCard = " + uiccCard);
            if (uiccCard != null) {
                UiccCardApplication application = uiccCard.getUiccPortForPhone(this.mSlotId).getApplication(1);
                Log.d("UserPLMNListPreference", "newUiccApplication = " + application);
                if (application != null) {
                    this.mIccFileHandler = application.getIccFileHandler();
                    Log.d("UserPLMNListPreference", "fh = " + this.mIccFileHandler);
                }
            }
        }

        private void handleAddList(Utils.UPLMNInfoWithEf uPLMNInfoWithEf) {
            Log.d("UserPLMNListPreference", "handleAddList: add new network: " + uPLMNInfoWithEf);
            dumpUPLMNInfo(this.mUPLMNList);
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < this.mUPLMNList.size(); i++) {
                arrayList.add((Utils.UPLMNInfoWithEf) this.mUPLMNList.get(i));
            }
            int iBinarySearch = Collections.binarySearch(this.mUPLMNList, uPLMNInfoWithEf, new PriorityCompare(this));
            if (iBinarySearch > 0) {
                arrayList.add(iBinarySearch, uPLMNInfoWithEf);
            } else {
                arrayList.add(uPLMNInfoWithEf);
            }
            updateListPriority(arrayList);
            dumpUPLMNInfo(arrayList);
            handleSetUPLMN(arrayList);
        }

        private void dumpUPLMNInfo(List list) {
            for (int i = 0; i < list.size(); i++) {
                Log.d("UserPLMNListPreference", "dumpUPLMNInfo : " + ((Utils.UPLMNInfoWithEf) list.get(i)).toString());
            }
        }

        private ArrayList handleModifiedList(Utils.UPLMNInfoWithEf uPLMNInfoWithEf, Utils.UPLMNInfoWithEf uPLMNInfoWithEf2) {
            Log.d("UserPLMNListPreference", "handleModifiedList: change old info: " + uPLMNInfoWithEf2.toString() + "-------new info: " + uPLMNInfoWithEf.toString());
            dumpUPLMNInfo(this.mUPLMNList);
            PriorityCompare priorityCompare = new PriorityCompare(this);
            int iBinarySearch = Collections.binarySearch(this.mUPLMNList, uPLMNInfoWithEf2, priorityCompare);
            int iBinarySearch2 = Collections.binarySearch(this.mUPLMNList, uPLMNInfoWithEf, priorityCompare);
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < this.mUPLMNList.size(); i++) {
                arrayList.add((Utils.UPLMNInfoWithEf) this.mUPLMNList.get(i));
            }
            if (iBinarySearch > iBinarySearch2) {
                arrayList.remove(iBinarySearch);
                arrayList.add(iBinarySearch2, uPLMNInfoWithEf);
            } else if (iBinarySearch < iBinarySearch2) {
                arrayList.add(iBinarySearch2 + 1, uPLMNInfoWithEf);
                arrayList.remove(iBinarySearch);
            } else {
                arrayList.remove(iBinarySearch);
                arrayList.add(iBinarySearch, uPLMNInfoWithEf);
            }
            updateListPriority(arrayList);
            dumpUPLMNInfo(arrayList);
            return arrayList;
        }

        private void updateListPriority(ArrayList arrayList) {
            int size = arrayList.size();
            int i = 0;
            int i2 = 0;
            while (i2 < size) {
                Object obj = arrayList.get(i2);
                i2++;
                ((Utils.UPLMNInfoWithEf) obj).setPriority(i);
                i++;
            }
        }

        private ArrayList handleDeleteList(Utils.UPLMNInfoWithEf uPLMNInfoWithEf) {
            Log.d("UserPLMNListPreference", "handleDeleteList : " + uPLMNInfoWithEf.toString());
            dumpUPLMNInfo(this.mUPLMNList);
            ArrayList arrayList = new ArrayList();
            int iBinarySearch = Collections.binarySearch(this.mUPLMNList, uPLMNInfoWithEf, new PriorityCompare(this));
            for (int i = 0; i < this.mUPLMNList.size(); i++) {
                arrayList.add((Utils.UPLMNInfoWithEf) this.mUPLMNList.get(i));
            }
            arrayList.remove(iBinarySearch);
            uPLMNInfoWithEf.setOperatorNumeric(null);
            arrayList.add(uPLMNInfoWithEf);
            updateListPriority(arrayList);
            dumpUPLMNInfo(arrayList);
            return arrayList;
        }

        class MyHandler extends Handler {
            private MyHandler() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i = message.what;
                if (i == 0) {
                    handleGetUPLMNList(message);
                } else {
                    if (i != 1) {
                        return;
                    }
                    handleSetPLMNDone(message);
                }
            }

            public void handleGetUPLMNList(Message message) {
                Log.d("UserPLMNListPreference", "handleGetUPLMNList: done");
                int i = message.arg2;
                UserPLMNListPreferenceFragment userPLMNListPreferenceFragment = UserPLMNListPreferenceFragment.this;
                if (i == 0) {
                    userPLMNListPreferenceFragment.onFinished(userPLMNListPreferenceFragment.mUPLMNListContainer, true);
                } else {
                    userPLMNListPreferenceFragment.onFinished(userPLMNListPreferenceFragment.mUPLMNListContainer, false);
                }
                AsyncResult asyncResult = (AsyncResult) message.obj;
                if (asyncResult.exception != null) {
                    Log.d("UserPLMNListPreference", "handleGetUPLMNList with exception = " + asyncResult.exception);
                    if (UserPLMNListPreferenceFragment.this.mUPLMNList == null) {
                        UserPLMNListPreferenceFragment.this.mUPLMNList = new ArrayList();
                        return;
                    }
                    return;
                }
                UserPLMNListPreferenceFragment.this.refreshUPLMNListPreference(((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).convertPlmnList(UserPLMNListPreferenceFragment.this.mSlotId, asyncResult.result));
            }

            public void handleSetPLMNDone(Message message) {
                Log.d("UserPLMNListPreference", "handleSetEFDone: done");
                AsyncResult asyncResult = (AsyncResult) message.obj;
                if (asyncResult.exception != null) {
                    Log.d("UserPLMNListPreference", "handleSetEFDone with exception = " + asyncResult.exception);
                } else {
                    Log.d("UserPLMNListPreference", "handleSetEFDone: with OK result!");
                }
                UserPLMNListPreferenceFragment.this.getUPLMNInfoFromEf();
            }
        }

        private String getNetWorkModeString(int i, String str) {
            int iConvertEFMode2Ap = UPLMNEditor.UPLMNEditorFragment.convertEFMode2Ap(i);
            if (ServiceProviderUtils.isChinaMobile(str)) {
                return getResources().getStringArray(R.array.uplmn_prefer_network_mode_td_choices)[iConvertEFMode2Ap];
            }
            if (ServiceProviderUtils.isChinaUnicom(str)) {
                return getResources().getStringArray(R.array.uplmn_prefer_network_mode_w_choices)[iConvertEFMode2Ap];
            }
            Log.w("UserPLMNListPreference", "getNetWorkModeString: operatorNumeric is unavailable, operatorNumeric = " + str);
            return "";
        }

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment
        protected void setScreenEnabled() {
            super.setScreenEnabled();
            if (getActivity() != null) {
                getActivity().invalidateOptionsMenu();
            }
        }

        @Override // androidx.fragment.app.Fragment
        public void onActivityResult(int i, int i2, Intent intent) {
            Log.d("UserPLMNListPreference", "resultCode = " + i2);
            Log.d("UserPLMNListPreference", "requestCode = " + i);
            if (intent != null) {
                Utils.UPLMNInfoWithEf uPLMNInfoWithEfCreateNetworkInfofromIntent = createNetworkInfofromIntent(intent);
                if (i2 == 102) {
                    handleSetUPLMN(handleDeleteList(this.mOldInfo));
                    return;
                }
                if (i2 == 101) {
                    if (i == 101) {
                        handleAddList(uPLMNInfoWithEfCreateNetworkInfofromIntent);
                    } else if (i == 102) {
                        handleSetUPLMN(handleModifiedList(uPLMNInfoWithEfCreateNetworkInfofromIntent, this.mOldInfo));
                    }
                }
            }
        }
    }
}
