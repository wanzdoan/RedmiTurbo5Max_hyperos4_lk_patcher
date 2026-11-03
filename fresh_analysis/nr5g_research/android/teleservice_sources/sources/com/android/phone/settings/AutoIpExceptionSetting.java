package com.android.phone.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.phone.R;
import gov.nist.javax.sip.address.GenericURI;
import java.util.HashMap;
import java.util.Map;
import miui.telephony.PhoneNumberUtils;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.R$attr;
import miuix.internal.util.AttributeResolver;
import miuix.recyclerview.card.CardItemDecoration;
import miuix.theme.token.ContainerToken;

/* JADX INFO: loaded from: classes.dex */
public class AutoIpExceptionSetting extends RecyclerActivity {
    private static final String[] CALLER_ID_PROJECTION = {"data1", "display_name"};
    private int mSlotId = -1;

    public static boolean contains(Context context, String str, int i) {
        if (str != null) {
            PhoneNumberUtils.PhoneNumber phoneNumber = PhoneNumberUtils.PhoneNumber.parse(str);
            String normalizedNumber = phoneNumber.getNormalizedNumber(false, true);
            phoneNumber.recycle();
            for (String str2 : getSharedPreferences(context, i).getAll().keySet()) {
                if (str.equals(str2)) {
                    return true;
                }
                PhoneNumberUtils.PhoneNumber phoneNumber2 = PhoneNumberUtils.PhoneNumber.parse(str2);
                String normalizedNumber2 = phoneNumber2.getNormalizedNumber(false, true);
                phoneNumber2.recycle();
                if (normalizedNumber.equals(normalizedNumber2)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getSummary(Context context, int i) {
        HashMap mapLoadNumbers = loadNumbers(context, i);
        if (mapLoadNumbers.size() == 0) {
            return context.getString(R.string.undefined);
        }
        StringBuilder sb = new StringBuilder();
        int i2 = 0;
        for (RecyclerActivity.Pair pair : mapLoadNumbers.values()) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append((String) pair.second);
            i2++;
            if (i2 == 3) {
                break;
            }
        }
        if (i2 == mapLoadNumbers.size()) {
            sb.append(context.getString(R.string.autoip_exception_summary, Integer.valueOf(mapLoadNumbers.size())));
        } else {
            sb.append(context.getString(R.string.autoip_exception_summary_more, Integer.valueOf(mapLoadNumbers.size())));
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.auto_ip_exception_setting);
        getWindow().addFlags(134217728);
        this.mSlotId = SubscriptionManager.getSlotIdExtra(getIntent(), SubscriptionManager.getDefault().getDefaultSlotId());
        this.mList = (EditableRecyclerView) findViewById(android.R.id.list);
        this.mEmptyText = (TextView) findViewById(R.id.summary);
        this.mList.setLayoutManager(new LinearLayoutManager(this));
        this.mNumbers = loadNumbers(this, this.mSlotId);
        updateAdapterData();
        setExtraHorizontalPaddingEnable(true);
        this.mList.addItemDecoration(new CardItemDecoration(this));
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

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, R.string.call_record_add_numbers).setIcon(AttributeResolver.resolveDrawable(this, R$attr.actionBarNewIcon)).setShowAsAction(1);
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
            Intent intent = new Intent("com.android.contacts.action.GET_MULTIPLE_PHONES");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setType("vnd.android.cursor.dir/phone_v2");
            intent.putExtra("com.android.contacts.extra.MAX_COUNT", 400);
            intent.putExtra("android.intent.extra.include_unknown_numbers", true);
            startActivityForResult(intent, 0);
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

    private void processPickResult(Intent intent) {
        Parcelable[] parcelableArrayExtra = intent.getParcelableArrayExtra("com.android.contacts.extra.PHONE_URIS");
        if (parcelableArrayExtra == null || parcelableArrayExtra.length <= 0) {
            return;
        }
        for (Parcelable parcelable : parcelableArrayExtra) {
            Uri uri = (Uri) parcelable;
            if (GenericURI.TEL.equals(uri.getScheme())) {
                String schemeSpecificPart = uri.getSchemeSpecificPart();
                insertIntoNumbers(this.mNumbers, new RecyclerActivity.Pair(schemeSpecificPart, schemeSpecificPart));
            }
        }
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (Parcelable parcelable2 : parcelableArrayExtra) {
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
            Cursor cursorQuery = getContentResolver().query(ContactsContract.Data.CONTENT_URI, CALLER_ID_PROJECTION, "_id IN (" + sb.toString() + ")", null, null);
            if (cursorQuery != null) {
                while (cursorQuery.moveToNext()) {
                    try {
                        insertIntoNumbers(this.mNumbers, new RecyclerActivity.Pair(cursorQuery.getString(0), cursorQuery.getString(1)));
                    } catch (Throwable th) {
                        cursorQuery.close();
                        throw th;
                    }
                }
                cursorQuery.close();
            }
        }
        saveNumbers();
        updateAdapterData();
    }

    private static void insertIntoNumbers(HashMap map, RecyclerActivity.Pair pair) {
        PhoneNumberUtils.PhoneNumber phoneNumber = PhoneNumberUtils.PhoneNumber.parse((CharSequence) pair.first);
        String normalizedNumber = phoneNumber.getNormalizedNumber(false, true);
        phoneNumber.recycle();
        map.put(normalizedNumber, pair);
    }

    public static HashMap loadNumbers(Context context, int i) {
        HashMap map = new HashMap();
        for (Map.Entry<String, ?> entry : getSharedPreferences(context, i).getAll().entrySet()) {
            if (entry.getValue() instanceof String) {
                insertIntoNumbers(map, new RecyclerActivity.Pair(entry.getKey(), (String) entry.getValue()));
            }
        }
        return map;
    }

    public static void saveNumbers(Context context, int i, HashMap map) {
        SharedPreferences.Editor editorEdit = getSharedPreferences(context, i).edit();
        editorEdit.clear();
        for (RecyclerActivity.Pair pair : map.values()) {
            editorEdit.putString((String) pair.first, (String) pair.second);
        }
        editorEdit.commit();
    }

    @Override // com.android.phone.settings.RecyclerActivity
    protected void saveNumbers() {
        SharedPreferences.Editor editorEdit = getSharedPreferences(this, this.mSlotId).edit();
        editorEdit.clear();
        for (RecyclerActivity.Pair pair : this.mNumbers.values()) {
            editorEdit.putString((String) pair.first, (String) pair.second);
        }
        editorEdit.commit();
    }

    private static SharedPreferences getSharedPreferences(Context context, int i) {
        String str = "_preference_autoip_exceptions";
        if (i >= 0) {
            str = "_preference_autoip_exceptions" + i;
        }
        return context.getSharedPreferences(context.getPackageName() + str, 0);
    }
}
