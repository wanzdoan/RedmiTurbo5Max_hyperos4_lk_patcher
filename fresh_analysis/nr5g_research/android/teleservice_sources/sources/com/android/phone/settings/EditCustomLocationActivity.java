package com.android.phone.settings;

import android.app.Dialog;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.android.phone.R;
import miui.provider.Telocation;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.AppCompatActivity;

/* JADX INFO: loaded from: classes.dex */
public class EditCustomLocationActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnDelete;
    private Button mBtnDone;
    private TextView mLocationView;
    private TextView mNumberView;
    private QueryHandler mQueryHandler;
    private RadioGroup mType;
    private boolean isCreateNew = false;
    private int mId = 0;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.edit_custom_location_layout);
        setTitle(R.string.telocation_custom_location_title);
        this.mNumberView = (TextView) findViewById(R.id.custom_number);
        this.mLocationView = (TextView) findViewById(R.id.custom_location);
        TextChangeListener textChangeListener = new TextChangeListener();
        this.mNumberView.addTextChangedListener(textChangeListener);
        this.mLocationView.addTextChangedListener(textChangeListener);
        Button button = (Button) findViewById(R.id.btn_done);
        this.mBtnDone = button;
        button.setOnClickListener(this);
        this.mBtnDone.setEnabled(false);
        findViewById(R.id.btn_discard).setOnClickListener(this);
        Button button2 = (Button) findViewById(R.id.btn_delete);
        this.mBtnDelete = button2;
        button2.setOnClickListener(this);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.type_group);
        this.mType = radioGroup;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.android.phone.settings.EditCustomLocationActivity.1
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup2, int i) {
                EditCustomLocationActivity.this.resetDoneButtonEnabled();
            }
        });
        ((RadioButton) findViewById(R.id.type_mob)).setChecked(true);
        Intent intent = getIntent();
        boolean zEquals = TextUtils.equals(intent.getAction(), "action_add_custom_location");
        this.isCreateNew = zEquals;
        this.mBtnDelete.setVisibility(zEquals ? 8 : 0);
        if (!this.isCreateNew) {
            this.mId = intent.getIntExtra("_id", 0);
            this.mNumberView.setText(intent.getStringExtra("number"));
            this.mLocationView.setText(intent.getStringExtra("location"));
            int intExtra = intent.getIntExtra("type", 0);
            if (intExtra == 0) {
                ((RadioButton) findViewById(R.id.type_tel)).setChecked(true);
            } else if (intExtra == 1) {
                ((RadioButton) findViewById(R.id.type_mob)).setChecked(true);
            } else if (intExtra == 2) {
                ((RadioButton) findViewById(R.id.type_jituan)).setChecked(true);
            } else {
                ((RadioButton) findViewById(R.id.type_tel)).setChecked(true);
            }
        }
        this.mQueryHandler = new QueryHandler(this);
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int i) {
        if (i == 0) {
            return new AlertDialog.Builder(this).setTitle(R.string.telocation_countrycode_alert_message).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(this) { // from class: com.android.phone.settings.EditCustomLocationActivity.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                }
            }).create();
        }
        if (i != 1) {
            return null;
        }
        return new AlertDialog.Builder(this).setIconAttribute(android.R.attr.alertDialogIcon).setTitle(R.string.telocation_confim_delete_title).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.EditCustomLocationActivity.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                EditCustomLocationActivity.this.getContentResolver().delete(Telocation.CONTENT_CUSTOM_LOCATION_URI, "_id=?", new String[]{String.valueOf(EditCustomLocationActivity.this.mId)});
                EditCustomLocationActivity.this.finish();
            }
        }).setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null).create();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        String string;
        if (view.getId() == R.id.btn_done) {
            CharSequence text = this.mNumberView.getText();
            if (text.charAt(0) == '+' || (text.length() > 1 && text.charAt(0) == '0' && text.charAt(1) == '0')) {
                showDialog(0);
                return;
            }
            int typeId = getTypeId();
            for (int i = 0; i < text.length(); i++) {
                char cCharAt = text.charAt(i);
                if ((cCharAt < '0' || cCharAt > '9') && !(typeId == 2 && cCharAt == '#')) {
                    if (typeId == 2) {
                        string = getString(R.string.telocation_jituan_error_message_number, new Object[]{getString(R.string.telocation_number_type_jituan)});
                    } else if (typeId == 0) {
                        string = getString(R.string.telocation_error_message_number, new Object[]{getString(R.string.telocation_number_type_tel)});
                    } else {
                        string = typeId == 1 ? getString(R.string.telocation_error_message_number, new Object[]{getString(R.string.telocation_number_type_mob)}) : null;
                    }
                    if (string != null) {
                        this.mNumberView.setError(string);
                        return;
                    }
                    return;
                }
            }
            save();
            return;
        }
        if (view.getId() == R.id.btn_delete) {
            showDialog(1);
        } else if (view.getId() == R.id.btn_discard) {
            finish();
        }
    }

    private int getTypeId() {
        if (this.mType.getCheckedRadioButtonId() == R.id.type_tel) {
            return 0;
        }
        if (this.mType.getCheckedRadioButtonId() == R.id.type_mob) {
            return 1;
        }
        return this.mType.getCheckedRadioButtonId() == R.id.type_jituan ? 2 : -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetDoneButtonEnabled() {
        if (this.mNumberView.getText().length() > 0 && this.mLocationView.getText().length() > 0 && getTypeId() >= 0) {
            this.mBtnDone.setEnabled(true);
        } else {
            this.mBtnDone.setEnabled(false);
        }
    }

    private void save() {
        int i;
        String string = this.mNumberView.getText().toString();
        String string2 = this.mLocationView.getText().toString();
        int typeId = getTypeId();
        ContentValues contentValues = new ContentValues();
        contentValues.put("number", string.trim());
        contentValues.put("location", string2.trim());
        contentValues.put("type", Integer.valueOf(typeId));
        if (!this.isCreateNew && (i = this.mId) > 0) {
            this.mQueryHandler.startUpdate(1, null, Telocation.CONTENT_CUSTOM_LOCATION_URI, contentValues, "_id=?", new String[]{String.valueOf(i)});
        } else {
            this.mQueryHandler.startInsert(0, null, Telocation.CONTENT_CUSTOM_LOCATION_URI, contentValues);
        }
    }

    class QueryHandler extends AsyncQueryHandler {
        public QueryHandler(Context context) {
            super(context.getContentResolver());
        }

        @Override // android.content.AsyncQueryHandler
        protected void onInsertComplete(int i, Object obj, Uri uri) {
            super.onInsertComplete(i, obj, uri);
            EditCustomLocationActivity.this.finish();
        }

        @Override // android.content.AsyncQueryHandler
        protected void onUpdateComplete(int i, Object obj, int i2) {
            super.onUpdateComplete(i, obj, i2);
            EditCustomLocationActivity.this.finish();
        }
    }

    class TextChangeListener implements TextWatcher {
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        private TextChangeListener() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            EditCustomLocationActivity.this.resetDoneButtonEnabled();
        }
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
