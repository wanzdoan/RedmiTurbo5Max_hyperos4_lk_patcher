package com.android.phone.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.provider.MiuiSettings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.UIDataLoadTask;
import gov.nist.javax.sip.address.GenericURI;
import java.util.HashMap;
import java.util.Map;
import miui.telephony.PhoneNumberUtils;
import miuix.appcompat.R$attr;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.LayoutUiModeHelper;
import miuix.internal.util.AttributeResolver;
import miuix.recyclerview.card.CardItemDecoration;
import miuix.theme.token.ContainerToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class AutoRecordWhiteListSetting extends RecyclerActivity {
    private static final String[] CALLER_ID_PROJECTION = {"data1", "display_name"};
    private int mAllowAddedSize;
    private Menu mMenu;

    public static String getSummary(Context context) {
        HashMap mapLoadNumbers = loadNumbers(context);
        if (mapLoadNumbers.size() == 0) {
            return context.getString(R.string.undefined);
        }
        return context.getResources().getQuantityString(R.plurals.call_record_auto_whitelist_summary, mapLoadNumbers.size(), Integer.valueOf(mapLoadNumbers.size()));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.auto_record_whitelist_setting);
        LayoutUiModeHelper.autoSetLayoutUiMode(this);
        getWindow().addFlags(134217728);
        this.mList = (EditableRecyclerView) findViewById(android.R.id.list);
        this.mEmptyText = (TextView) findViewById(R.id.summary);
        this.mList.setLayoutManager(new LinearLayoutManager(this));
        setExtraHorizontalPaddingEnable(true);
        if (bundle == null) {
            new UIDataLoadTask(this) { // from class: com.android.phone.settings.AutoRecordWhiteListSetting.1
                @Override // com.android.phone.UIDataLoadTask
                protected void doPrepare() {
                    ((AutoRecordWhiteListSetting) getReferenceObject()).mEmptyText.setText(R.string.call_record_loading_content);
                }

                @Override // com.android.phone.UIDataLoadTask
                protected Object doTask(Object[] objArr) {
                    return AutoRecordWhiteListSetting.loadNumbers(PhoneGlobals.getInstance());
                }

                @Override // com.android.phone.UIDataLoadTask
                protected void doUI(Object obj) {
                    AutoRecordWhiteListSetting autoRecordWhiteListSetting = (AutoRecordWhiteListSetting) getReferenceObject();
                    HashMap map = (HashMap) obj;
                    autoRecordWhiteListSetting.mNumbers = map;
                    if (map == null || map.size() <= 0) {
                        autoRecordWhiteListSetting.mEmptyText.setText(R.string.call_record_auto_whitelist_no_item);
                        AutoRecordWhiteListSetting.this.updateAllowAddedSize(0);
                    } else {
                        AutoRecordWhiteListSetting.this.updateAllowAddedSize(autoRecordWhiteListSetting.mNumbers.size());
                    }
                    autoRecordWhiteListSetting.updateAdapterData();
                    autoRecordWhiteListSetting.updateMenu();
                }
            }.execute(new Object[0]);
        }
        this.mList.addItemDecoration(new CardItemDecoration(this));
        RecyclerView.ItemDecoration itemDecorationAt = this.mList.getItemDecorationAt(0);
        if (itemDecorationAt instanceof CardItemDecoration) {
            int extraHorizontalPadding = (int) (getExtraHorizontalPadding() + (ContainerToken.PADDING_BASE_DP * 3 * getResources().getDisplayMetrics().density));
            CardItemDecoration cardItemDecoration = (CardItemDecoration) itemDecorationAt;
            cardItemDecoration.setCardMarginStart(extraHorizontalPadding);
            cardItemDecoration.setCardMarginEnd(extraHorizontalPadding);
        }
    }

    @Override // miuix.appcompat.app.AppCompatActivity, miuix.container.ExtraPaddingObserver
    public void onExtraPaddingChanged(int i) {
        super.onExtraPaddingChanged(i);
        RecyclerView.ItemDecoration itemDecorationAt = this.mList.getItemDecorationAt(0);
        if (itemDecorationAt instanceof CardItemDecoration) {
            int i2 = (int) (i + (ContainerToken.PADDING_BASE_DP * 3 * getResources().getDisplayMetrics().density));
            CardItemDecoration cardItemDecoration = (CardItemDecoration) itemDecorationAt;
            cardItemDecoration.setCardMarginStart(i2);
            cardItemDecoration.setCardMarginEnd(i2);
            if (this.mList.getAdapter() != null) {
                this.mList.getAdapter().notifyDataSetChanged();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("AllowAddedSize", this.mAllowAddedSize);
        bundle.putSerializable("CallRecordData", this.mNumbers);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, android.app.Activity
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        this.mAllowAddedSize = bundle.getInt("AllowAddedSize");
        this.mNumbers = (HashMap) bundle.getSerializable("CallRecordData");
        updateAdapterData();
        updateMenu();
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        Drawable drawableResolveDrawable = AttributeResolver.resolveDrawable(this, R$attr.actionBarNewIcon);
        this.mMenu = menu;
        menu.add(0, 1, 0, R.string.call_record_add_numbers).setIcon(drawableResolveDrawable).setShowAsAction(1);
        updateMenu();
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            finish();
            return true;
        }
        if (itemId == 1) {
            startAddNumbers();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 0 && intent != null) {
            processPickResult(intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMenu() {
        Menu menu = this.mMenu;
        if (menu != null) {
            menu.findItem(1).setEnabled(this.mAllowAddedSize > 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAllowAddedSize(int i) {
        int i2 = 400 - i;
        if (i2 <= 0) {
            i2 = 0;
        }
        this.mAllowAddedSize = i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startAddNumbers() {
        Intent intent = new Intent("com.android.contacts.action.GET_MULTIPLE_PHONES");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("vnd.android.cursor.dir/phone_v2");
        intent.putExtra("com.android.contacts.extra.MAX_COUNT", 400);
        intent.putExtra("android.intent.extra.include_unknown_numbers", true);
        startActivityForResult(intent, 0);
    }

    private void processPickResult(Intent intent) {
        Parcelable[] parcelableArrayExtra = intent.getParcelableArrayExtra("com.android.contacts.extra.PHONE_URIS");
        if (parcelableArrayExtra == null || !canSaveNumbers(parcelableArrayExtra) || parcelableArrayExtra.length <= 0) {
            return;
        }
        new UIDataLoadTask(this) { // from class: com.android.phone.settings.AutoRecordWhiteListSetting.2
            @Override // com.android.phone.UIDataLoadTask
            protected void doPrepare() {
                ((AutoRecordWhiteListSetting) getReferenceObject()).mEmptyText.setText(R.string.call_record_loading_content);
            }

            @Override // com.android.phone.UIDataLoadTask
            protected Object doTask(Object[] objArr) {
                AutoRecordWhiteListSetting autoRecordWhiteListSetting = (AutoRecordWhiteListSetting) getReferenceObject();
                Parcelable[] parcelableArr = (Parcelable[]) objArr;
                for (Parcelable parcelable : parcelableArr) {
                    Uri uri = (Uri) parcelable;
                    if (GenericURI.TEL.equals(uri.getScheme())) {
                        String schemeSpecificPart = uri.getSchemeSpecificPart();
                        if (autoRecordWhiteListSetting != null) {
                            AutoRecordWhiteListSetting.insertIntoNumbers(autoRecordWhiteListSetting.mNumbers, new RecyclerActivity.Pair(schemeSpecificPart, schemeSpecificPart));
                        }
                    }
                }
                StringBuilder sb = new StringBuilder();
                boolean z = true;
                for (Parcelable parcelable2 : parcelableArr) {
                    Uri uri2 = (Uri) parcelable2;
                    if ("content".equals(uri2.getScheme())) {
                        if (z) {
                            sb.append(uri2.getLastPathSegment());
                            z = false;
                        } else {
                            sb.append(',');
                            sb.append(uri2.getLastPathSegment());
                        }
                    }
                }
                if (!z && sb.length() > 0) {
                    Cursor cursorQuery = AutoRecordWhiteListSetting.this.getContentResolver().query(ContactsContract.Data.CONTENT_URI, AutoRecordWhiteListSetting.CALLER_ID_PROJECTION, "_id IN (" + sb.toString() + ")", null, null);
                    if (cursorQuery != null) {
                        while (cursorQuery.moveToNext()) {
                            try {
                                if (autoRecordWhiteListSetting != null) {
                                    AutoRecordWhiteListSetting.insertIntoNumbers(autoRecordWhiteListSetting.mNumbers, new RecyclerActivity.Pair(cursorQuery.getString(0), cursorQuery.getString(1)));
                                }
                            } catch (Throwable th) {
                                cursorQuery.close();
                                throw th;
                            }
                        }
                        cursorQuery.close();
                    }
                }
                AutoRecordWhiteListSetting.this.saveNumbers();
                return null;
            }

            @Override // com.android.phone.UIDataLoadTask
            protected void doUI(Object obj) {
                AutoRecordWhiteListSetting autoRecordWhiteListSetting = (AutoRecordWhiteListSetting) getReferenceObject();
                autoRecordWhiteListSetting.updateAdapterData();
                autoRecordWhiteListSetting.updateMenu();
            }
        }.execute(parcelableArrayExtra);
    }

    private boolean canSaveNumbers(Parcelable[] parcelableArr) {
        if (parcelableArr.length <= this.mAllowAddedSize) {
            return true;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Resources resources = getResources();
        int i = R.plurals.call_record_add_white_list_over_reminder;
        int i2 = this.mAllowAddedSize;
        builder.setMessage(resources.getQuantityString(i, i2, Integer.valueOf(i2))).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.AutoRecordWhiteListSetting.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i3) {
                AutoRecordWhiteListSetting.this.startAddNumbers();
            }
        }).setNegativeButton(android.R.string.cancel, (DialogInterface.OnClickListener) null).show();
        return false;
    }

    @Override // com.android.phone.settings.RecyclerActivity
    protected void updateAdapterData() {
        super.updateAdapterData();
        updateAllowAddedSize(this.mNumbers.size());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void insertIntoNumbers(HashMap map, RecyclerActivity.Pair pair) {
        PhoneNumberUtils.PhoneNumber phoneNumber = PhoneNumberUtils.PhoneNumber.parse((CharSequence) pair.first);
        String normalizedNumber = phoneNumber.getNormalizedNumber(false, true);
        phoneNumber.recycle();
        map.put(normalizedNumber, pair);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static HashMap loadNumbers(Context context) {
        HashMap map = new HashMap();
        HashMap map2 = new HashMap();
        String recordWhiteList = MiuiSettings.Telephony.getRecordWhiteList(context.getContentResolver());
        if (!TextUtils.isEmpty(recordWhiteList)) {
            try {
                JSONArray jSONArray = new JSONArray(recordWhiteList);
                for (int i = 0; i < jSONArray.length(); i++) {
                    map2.put(jSONArray.getJSONObject(i).getString("num"), jSONArray.getJSONObject(i).getString("name"));
                }
            } catch (JSONException e) {
                Log.e("AutoRecordWhiteListSetting", "white list string parsed to json error: %s" + recordWhiteList, e);
            }
            for (Map.Entry entry : map2.entrySet()) {
                if (entry.getValue() instanceof String) {
                    insertIntoNumbers(map, new RecyclerActivity.Pair((String) entry.getKey(), (String) entry.getValue()));
                }
            }
        }
        return map;
    }

    @Override // com.android.phone.settings.RecyclerActivity
    protected void saveNumbers() {
        String string;
        try {
            JSONArray jSONArray = new JSONArray();
            for (RecyclerActivity.Pair pair : this.mNumbers.values()) {
                jSONArray.put(new JSONObject(String.format("{num:\"%s\", name:\"%s\"}", pair.first, pair.second)));
            }
            string = jSONArray.toString();
        } catch (JSONException e) {
            Log.e("AutoRecordWhiteListSetting", "perse json error", e);
            string = "";
        }
        MiuiSettings.Telephony.setRecordWhiteList(getContentResolver(), string);
    }

    @Override // com.android.phone.settings.RecyclerActivity
    protected void setSummary() {
        if (this.mNumbers.size() == 0) {
            this.mEmptyText.setText(R.string.call_record_auto_whitelist_no_item);
        }
    }
}
