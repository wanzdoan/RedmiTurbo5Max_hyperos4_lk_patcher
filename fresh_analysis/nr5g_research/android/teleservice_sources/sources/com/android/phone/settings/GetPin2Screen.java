package com.android.phone.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.android.phone.R;
import miuix.appcompat.app.AppCompatActivity;

/* JADX INFO: loaded from: classes.dex */
public class GetPin2Screen extends AppCompatActivity implements TextView.OnEditorActionListener {
    private final View.OnClickListener mClicked = new View.OnClickListener() { // from class: com.android.phone.settings.GetPin2Screen.1
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (TextUtils.isEmpty(GetPin2Screen.this.mPin2Field.getText())) {
                return;
            }
            GetPin2Screen.this.returnResult();
        }
    };
    private Button mOkButton;
    private EditText mPin2Field;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.get_pin2_screen);
        EditText editText = (EditText) findViewById(R.id.pin);
        this.mPin2Field = editText;
        editText.setKeyListener(DigitsKeyListener.getInstance());
        this.mPin2Field.setMovementMethod(null);
        this.mPin2Field.setOnEditorActionListener(this);
        Button button = (Button) findViewById(R.id.ok);
        this.mOkButton = button;
        button.setOnClickListener(this.mClicked);
    }

    private String getPin2() {
        return this.mPin2Field.getText().toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void returnResult() {
        Bundle bundle = new Bundle();
        bundle.putString("pin2", getPin2());
        Uri data = getIntent().getData();
        Intent intent = new Intent();
        if (data != null) {
            intent.setAction(data.toString());
        }
        setResult(-1, intent.putExtras(bundle));
        finish();
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 6) {
            return false;
        }
        this.mOkButton.performClick();
        return true;
    }
}
