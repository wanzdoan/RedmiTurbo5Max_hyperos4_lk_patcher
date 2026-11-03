package com.android.phone;

import android.app.Activity;
import android.app.Dialog;
import android.app.StatusBarManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DialerKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.android.phone.CustomView.EmergencyDialerKeyPadEditText;
import com.android.phone.utils.FlipUtils;
import com.android.phone.utils.FontUtil;
import com.android.phone.utils.MaterialUtils;
import com.android.phone.utils.MiuiKoreaOperatorCustimizedUtils;
import com.android.phone.utils.PressLightAnimator;
import com.android.phone.utils.Utils;
import com.miui.miwallpaper.MiuiWallpaperManager;
import com.ot.pubsub.a.a;
import gov.nist.javax.sip.address.GenericURI;
import java.text.DecimalFormat;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.TelephonyManagerEx;
import miuix.appcompat.app.AlertDialog;
import miuix.core.util.HyperMaterialUtils;
import miuix.core.util.MaterialConfig;
import miuix.core.util.MiuiBlurUtils;
import miuix.miuixbasewidget.widget.CallOvalImageButton;
import miuix.theme.token.GlassToken;

/* JADX INFO: loaded from: classes.dex */
public class EmergencyDialer extends Activity implements View.OnClickListener, View.OnLongClickListener, View.OnKeyListener, TextWatcher, View.OnHoverListener, DialpadButton.OnButtonClickListener, DialpadButton.OnButtonHoverListener {
    private static final int[] DIALER_KEYS = {R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine, R.id.star, R.id.zero, R.id.pound};
    private AccessibilityManager mAccessibilityManager;
    private boolean mDTMFToneEnabled;
    private ImageView mDelete;
    private CallOvalImageButton mDialButton;
    private PressLightAnimator mDialButtonPressLightAnimator;
    private boolean mDialButtonPressLightApplied;
    EmergencyDialerKeyPadEditText mDigits;
    private EmergencyActionGroup mEmergencyActionGroup;
    Button mHealthButton;
    private boolean mIsSCDialer;
    private String mLastNumber;
    private int mScreenLayoutSize;
    private StatusBarManager mStatusBarManager;
    private TelephonyManager mTm;
    private ToneGenerator mToneGenerator;
    private String mHealthAction = "";
    private int mHealthResult = -1;
    private String mDigitsNumber = "";
    private Object mToneGeneratorLock = new Object();
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.EmergencyDialer.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                EmergencyDialer.this.finish();
            }
        }
    };
    private Handler mHealthHandler = new Handler() { // from class: com.android.phone.EmergencyDialer.7
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (EmergencyDialer.this.mHealthResult != 1 || TextUtils.isEmpty(EmergencyDialer.this.mHealthAction)) {
                return;
            }
            EmergencyDialer.this.mHealthButton.setVisibility(0);
            EmergencyDialer.this.mHealthButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.phone.EmergencyDialer.7.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(EmergencyDialer.this.mHealthAction);
                        intent.putExtra("origin", GenericURI.TEL);
                        EmergencyDialer.this.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    protected void maybeAddNumberFormatting() {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        if (SpecialCharSequenceMgr.handleCharsForLockedDevice(this, editable.toString(), this)) {
            this.mDigits.getText().clear();
            this.mDigitsNumber = "";
        }
        FontUtil.setTextTypeface(this.mDigits);
        updateDialAndDeleteButtonStateEnabledAttr();
        if (this.mIsSCDialer) {
            String strStripSeparators = PhoneNumberUtils.stripSeparators(this.mDigitsNumber);
            if (handleSecretCode(strStripSeparators) || handleIMEIDisplay(strStripSeparators)) {
                this.mDigitsNumber = "";
                this.mDigits.getText().clear();
            }
        }
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        String numberFromIntent;
        super.onCreate(bundle);
        if (MiuiKoreaOperatorCustimizedUtils.isCustForKrOps() && !"kr".equals(getIntent().getStringExtra("EmergencyList"))) {
            startEmergencyListActivity();
            finish();
        }
        this.mStatusBarManager = (StatusBarManager) getSystemService("statusbar");
        this.mAccessibilityManager = (AccessibilityManager) getSystemService("accessibility");
        this.mTm = (TelephonyManager) getSystemService("phone");
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.flags |= 67108864;
        attributes.extraFlags |= 4096;
        attributes.layoutInDisplayCutoutMode = 1;
        getWindow().setAttributes(attributes);
        if (Build.IS_TABLET) {
            Log.d("EmergencyDialer", "Create for full screen.");
            setTheme(R.style.Miui_Theme_EmergencyDialerFullScreen);
            getWindow().getDecorView().setSystemUiVisibility(1536);
        } else {
            Log.d("EmergencyDialer", "Create for layout stable.");
            getWindow().getDecorView().setSystemUiVisibility(768);
        }
        getWindow().addFlags(131072);
        int i = getResources().getConfiguration().screenLayout & 15;
        this.mScreenLayoutSize = i;
        updateRequestedOrientation(i);
        setContentView(R.layout.miui_emergency_dialer);
        MaterialUtils.updateIsHyperMaterialEnable(this);
        EmergencyDialerKeyPadEditText emergencyDialerKeyPadEditText = (EmergencyDialerKeyPadEditText) findViewById(R.id.digits);
        this.mDigits = emergencyDialerKeyPadEditText;
        emergencyDialerKeyPadEditText.setKeyListener(DialerKeyListener.getInstance());
        this.mDigits.setOnClickListener(this);
        this.mDigits.setOnKeyListener(this);
        this.mDigits.setLongClickable(false);
        if (this.mAccessibilityManager.isEnabled()) {
            this.mDigits.setSelected(true);
        }
        if ("volume_down_up_three_time".equals(getIntent().getStringExtra("shortcut"))) {
            Log.d("EmergencyDialer", "Start secretCodeActivity");
            this.mDigits.setHint((CharSequence) null);
            this.mIsSCDialer = true;
        }
        queryHealthLockEnter();
        maybeAddNumberFormatting();
        if (findViewById(R.id.one) != null) {
            setupKeypad();
        }
        ImageView imageView = (ImageView) findViewById(R.id.deleteButton);
        this.mDelete = imageView;
        imageView.setOnClickListener(this);
        this.mDelete.setOnLongClickListener(this);
        MiuiAnimUtil.defaultTouch(this.mDelete);
        this.mDelete.requestFocus();
        ImageView imageView2 = (ImageView) findViewById(R.id.backButton);
        imageView2.setOnClickListener(this);
        MiuiAnimUtil.defaultTouch(imageView2);
        imageView2.requestFocus();
        this.mDialButton = (CallOvalImageButton) findViewById(R.id.dialButton);
        boolean z = getResources().getBoolean(R.bool.config_show_onscreen_dial_button);
        CallOvalImageButton callOvalImageButton = this.mDialButton;
        if (z) {
            callOvalImageButton.setOnClickListener(this);
        } else {
            callOvalImageButton.setVisibility(8);
        }
        CallOvalImageButton callOvalImageButton2 = this.mDialButton;
        if (callOvalImageButton2 != null) {
            callOvalImageButton2.setMaterial(MaterialUtils.Glass_Green_Light);
        }
        EmergencyActionGroup emergencyActionGroup = (EmergencyActionGroup) findViewById(R.id.emergency_action_group);
        this.mEmergencyActionGroup = emergencyActionGroup;
        emergencyActionGroup.setVisibility(isShowEmergencyAction() ? 0 : 4);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.emergency_dialer);
        if (relativeLayout != null) {
            MiuiBlurUtils.setBackgroundBlur(relativeLayout, (int) ((getResources().getDisplayMetrics().density * 60.0f) + 0.5f));
            MiuiBlurUtils.setViewBlurMode(relativeLayout, 0);
        }
        if (bundle != null) {
            super.onRestoreInstanceState(bundle);
            if (!TextUtils.isEmpty(this.mDigits.getText().toString()) && TextUtils.isEmpty(this.mDigitsNumber)) {
                this.mDigitsNumber = this.mDigits.getText().toString();
            }
        }
        updateWallpaper();
        Uri data = getIntent().getData();
        if (data != null && GenericURI.TEL.equals(data.getScheme()) && (numberFromIntent = miui.telephony.PhoneNumberUtils.getNumberFromIntent(getIntent(), this)) != null) {
            this.mDigitsNumber = numberFromIntent;
            this.mDigits.setText(Utils.localizeNumber(numberFromIntent));
        }
        new Thread(new Runnable() { // from class: com.android.phone.EmergencyDialer.2
            @Override // java.lang.Runnable
            public void run() {
                synchronized (EmergencyDialer.this.mToneGeneratorLock) {
                    if (EmergencyDialer.this.mToneGenerator == null) {
                        try {
                            EmergencyDialer.this.mToneGenerator = new ToneGenerator(8, 80);
                        } catch (RuntimeException e) {
                            Log.w("EmergencyDialer", "Exception caught while creating local tone generator: " + e);
                            EmergencyDialer.this.mToneGenerator = null;
                        }
                    }
                }
            }
        }).start();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
        if (isFoldDialerEnabled()) {
            updateLayout();
        }
        postUpdateLayoutAndWallpaperIfNeeded(this.mScreenLayoutSize);
    }

    public void updateWallpaper() {
        MiuiWallpaperManager.init(this, new MiuiWallpaperManager.MiuiWallpaperManagerCallback() { // from class: com.android.phone.EmergencyDialer.3
            @Override // com.miui.miwallpaper.MiuiWallpaperManager.MiuiWallpaperManagerCallback
            public void onMiuiWallpaperInitialized(MiuiWallpaperManager miuiWallpaperManager) {
                Bitmap fitScreenWallpaperPreview;
                MaterialConfig materialConfigCreate;
                if (miuiWallpaperManager == null || (fitScreenWallpaperPreview = miuiWallpaperManager.getFitScreenWallpaperPreview(EmergencyDialer.this, 2)) == null) {
                    return;
                }
                EmergencyDialer.this.getWindow().setBackgroundDrawable(new BitmapDrawable(EmergencyDialer.this.getResources(), ImageUtils.getEmergencyBackground(EmergencyDialer.this, fitScreenWallpaperPreview, MaterialUtils.isHyperMaterialEnable())));
                View viewFindViewById = EmergencyDialer.this.findViewById(R.id.top);
                if (viewFindViewById == null || !MaterialUtils.isHyperMaterialEnable()) {
                    return;
                }
                if (MaterialUtils.isHyperGlassEnable()) {
                    materialConfigCreate = MaterialConfig.create(MaterialUtils.Mask_Glass_Light);
                } else {
                    materialConfigCreate = MaterialConfig.create(MaterialUtils.Mask_Light);
                }
                if (materialConfigCreate != null) {
                    HyperMaterialUtils.applyContainer(viewFindViewById, materialConfigCreate.getBlurConfig());
                }
            }
        });
    }

    public boolean isShowEmergencyAction() {
        return (FlipUtils.isSecondaryScreen() || !Build.IS_INTERNATIONAL_BUILD || SystemProperties.getBoolean("ro.radio.noril", false)) ? false : true;
    }

    public void updateLayout() {
        RelativeLayout.LayoutParams layoutParams;
        LinearLayout.LayoutParams layoutParams2;
        LinearLayout.LayoutParams layoutParams3;
        LinearLayout.LayoutParams layoutParams4;
        RelativeLayout.LayoutParams layoutParams5;
        RelativeLayout.LayoutParams layoutParams6;
        int dimensionPixelSize;
        EmergencyActionGroup emergencyActionGroup = this.mEmergencyActionGroup;
        if (emergencyActionGroup != null) {
            emergencyActionGroup.setVisibility(isShowEmergencyAction() ? 0 : 4);
        }
        EmergencyDialerKeyPadEditText emergencyDialerKeyPadEditText = this.mDigits;
        if (emergencyDialerKeyPadEditText != null && (layoutParams6 = (RelativeLayout.LayoutParams) emergencyDialerKeyPadEditText.getLayoutParams()) != null) {
            layoutParams6.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.emergency_dialer_margin_top), 0, 0);
            layoutParams6.height = getResources().getDimensionPixelOffset(R.dimen.emergency_digits_height);
            this.mDigits.setLayoutParams(layoutParams6);
            boolean z = this.mDigits.length() != 0;
            EmergencyDialerKeyPadEditText emergencyDialerKeyPadEditText2 = this.mDigits;
            if (z) {
                dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.emergency_input_digits_size);
            } else {
                dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.emergency_input_text_size);
            }
            emergencyDialerKeyPadEditText2.setTextSize(0, dimensionPixelSize);
        }
        Button button = this.mHealthButton;
        if (button != null && (layoutParams5 = (RelativeLayout.LayoutParams) button.getLayoutParams()) != null) {
            layoutParams5.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.emergency_dialer_margin_top) - getResources().getDimensionPixelOffset(R.dimen.adjust_mihealth_marginTop), 0, 0);
            this.mHealthButton.setLayoutParams(layoutParams5);
        }
        ImageView imageView = (ImageView) findViewById(R.id.backButton);
        if (imageView != null && (layoutParams4 = (LinearLayout.LayoutParams) imageView.getLayoutParams()) != null) {
            layoutParams4.width = getResources().getDimensionPixelOffset(R.dimen.dial_additional_button_height);
            layoutParams4.height = getResources().getDimensionPixelOffset(R.dimen.dial_additional_button_height);
            imageView.setLayoutParams(layoutParams4);
            imageView.setImageResource(R.drawable.ic_emergency_back);
        }
        CallOvalImageButton callOvalImageButton = (CallOvalImageButton) findViewById(R.id.dialButton);
        if (callOvalImageButton != null && (layoutParams3 = (LinearLayout.LayoutParams) callOvalImageButton.getLayoutParams()) != null) {
            layoutParams3.width = getResources().getDimensionPixelOffset(R.dimen.dial_additional_button_height);
            layoutParams3.height = getResources().getDimensionPixelOffset(R.dimen.dial_additional_button_height);
            callOvalImageButton.setLayoutParams(layoutParams3);
        }
        ImageView imageView2 = (ImageView) findViewById(R.id.deleteButton);
        if (imageView2 != null && (layoutParams2 = (LinearLayout.LayoutParams) imageView2.getLayoutParams()) != null) {
            layoutParams2.width = getResources().getDimensionPixelOffset(R.dimen.dial_additional_button_height);
            layoutParams2.height = getResources().getDimensionPixelOffset(R.dimen.dial_additional_button_height);
            layoutParams2.setMargins(0, 0, 0, 0);
            imageView2.setLayoutParams(layoutParams2);
            imageView2.setImageResource(R.drawable.ic_emergency_delete);
        }
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.dialpadAdditionalButtons);
        if (linearLayout != null && (layoutParams = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams()) != null) {
            int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.dialpadAdditionalButtons_padding_bottom);
            layoutParams.width = getResources().getDimensionPixelOffset(R.dimen.dial_panel_button_line_width);
            layoutParams.height = getResources().getDimensionPixelOffset(R.dimen.dial_additional_button_height);
            layoutParams.setMargins(0, 0, 0, dimensionPixelOffset);
            linearLayout.setLayoutParams(layoutParams);
        }
        updateDialer(R.id.dialer_1);
        updateDialer(R.id.dialer_2);
        updateDialer(R.id.dialer_3);
        updateDialer(R.id.dialer_4);
        LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.dtmf_twelve_key_dialer_view);
        if (linearLayout2 != null) {
            linearLayout2.setPadding(0, 0, 0, getResources().getDimensionPixelOffset(R.dimen.dialer_dialpad_padding_bottom));
        }
        for (int i : DIALER_KEYS) {
            DialpadButton dialpadButton = (DialpadButton) findViewById(i);
            if (dialpadButton != null) {
                dialpadButton.updateButtonSize(this);
                dialpadButton.updateButtonTextSize(this);
            }
        }
        updateWallpaper();
    }

    private boolean isFoldDialerEnabled() {
        boolean z = FlipUtils.isFold() && getResources().getBoolean(R.bool.config_fold_emergency_dialer_enabled);
        Log.d("EmergencyDialer", "isFoldDialerEnabled=" + z);
        return z;
    }

    private boolean shouldRotate() {
        return HyperOSCustFeatureResolve.getBoolean("config_emergency_dialer_rotate", false);
    }

    private void updateRequestedOrientation(int i) {
        if (shouldRotate()) {
            boolean zIsFoldDialerEnabled = isFoldDialerEnabled();
            boolean z = zIsFoldDialerEnabled && i != 2;
            int i2 = z ? -1 : 1;
            Log.i("EmergencyDialer", "updateRequestedOrientation, foldDialerEnabled=" + zIsFoldDialerEnabled + ", innerScreen=" + z + ", screenLayoutSize=" + i + ", orientation=" + i2);
            setRequestedOrientation(i2);
        }
    }

    private void postUpdateLayoutAndWallpaperIfNeeded(int i) {
        boolean zIsFoldDialerEnabled = isFoldDialerEnabled();
        if (shouldRotate() && zIsFoldDialerEnabled) {
            getWindow().getDecorView().post(new Runnable() { // from class: com.android.phone.EmergencyDialer$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$postUpdateLayoutAndWallpaperIfNeeded$0();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$postUpdateLayoutAndWallpaperIfNeeded$0() {
        updateLayout();
        updateWallpaper();
    }

    public void updateDialer(int i) {
        LinearLayout.LayoutParams layoutParams;
        LinearLayout linearLayout = (LinearLayout) findViewById(i);
        if (linearLayout == null || (layoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams()) == null) {
            return;
        }
        if (linearLayout.getId() == R.id.dialer_4) {
            layoutParams.setMargins(0, 0, 0, getResources().getDimensionPixelOffset(R.dimen.dial_panel_margin));
        } else {
            layoutParams.setMargins(0, 0, 0, getResources().getDimensionPixelOffset(R.dimen.dial_panel_button_margin_bottom));
        }
        layoutParams.width = getResources().getDimensionPixelOffset(R.dimen.dial_panel_button_line_width);
        layoutParams.height = getResources().getDimensionPixelOffset(R.dimen.dial_panel_button_height);
        linearLayout.setLayoutParams(layoutParams);
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int i = configuration.screenLayout & 15;
        Log.i("EmergencyDialer", "onConfigurationChanged, screenLayoutSize=" + i);
        if (FlipUtils.isFlip() && this.mScreenLayoutSize != i) {
            updateLayout();
            this.mScreenLayoutSize = i;
            Log.i("EmergencyDialer", "onConfigurationChanged, mScreenLayoutSize=" + this.mScreenLayoutSize);
        }
        if (Build.IS_TABLET || isFoldDialerEnabled()) {
            updateLayout();
            if (!isFoldDialerEnabled() || this.mScreenLayoutSize == i) {
                return;
            }
            this.mScreenLayoutSize = i;
            Log.i("EmergencyDialer", "onConfigurationChanged, mScreenLayoutSize=" + this.mScreenLayoutSize);
            if (i == 2) {
                setRequestedOrientation(1);
            } else {
                setRequestedOrientation(-1);
            }
        }
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        new Thread(new Runnable() { // from class: com.android.phone.EmergencyDialer.4
            @Override // java.lang.Runnable
            public void run() {
                synchronized (EmergencyDialer.this.mToneGeneratorLock) {
                    try {
                        if (EmergencyDialer.this.mToneGenerator != null) {
                            EmergencyDialer.this.mToneGenerator.release();
                            EmergencyDialer.this.mToneGenerator = null;
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        }).start();
        unregisterReceiver(this.mBroadcastReceiver);
        Log.e("EmergencyDialer", "EmergencyDialer onDestroy");
        MiuiWallpaperManager.staticDestroy();
    }

    @Override // android.app.Activity
    protected void onRestoreInstanceState(Bundle bundle) {
        this.mLastNumber = bundle.getString("lastNumber");
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("lastNumber", this.mLastNumber);
    }

    @Override // android.app.Activity
    protected void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        this.mDigits.addTextChangedListener(this);
    }

    private void setupKeypad() {
        for (int i : DIALER_KEYS) {
            DialpadButton dialpadButton = (DialpadButton) findViewById(i);
            dialpadButton.setButtonClickListener(this);
            dialpadButton.setOnButtonHoverListener(this);
        }
        findViewById(R.id.zero).setOnLongClickListener(this);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 5) {
            if (i == 67) {
                keyPressed(67);
                return true;
            }
            return super.onKeyDown(i, keyEvent);
        }
        if (TextUtils.isEmpty(this.mDigits.getText().toString())) {
            finish();
        } else {
            placeCall();
        }
        return true;
    }

    private void keyPressed(int i) {
        this.mDigits.performHapticFeedback(1);
        KeyEvent keyEvent = new KeyEvent(0, i);
        String str = this.mDigitsNumber;
        if (i == 67) {
            if (str.length() > 0) {
                String str2 = this.mDigitsNumber;
                this.mDigitsNumber = str2.substring(0, str2.length() - 1);
            }
        } else {
            this.mDigitsNumber = str + keyEvent.getNumber();
        }
        this.mDigits.setText(Utils.localizeNumber(this.mDigitsNumber));
        this.mDigits.setSelection(this.mDigitsNumber.length());
    }

    @Override // android.view.View.OnKeyListener
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (view.getId() != R.id.digits || i != 66 || keyEvent.getAction() != 1) {
            return false;
        }
        placeCall();
        return true;
    }

    @Override // com.android.phone.DialpadButton.OnButtonClickListener
    public void onButtonClick(View view) {
        handleNumberPadClick(view.getId());
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        handleNumberPadClick(view.getId());
    }

    public void handleNumberPadClick(int i) {
        if (i == R.id.one) {
            playTone(1);
            keyPressed(8);
            return;
        }
        if (i == R.id.two) {
            playTone(2);
            keyPressed(9);
            return;
        }
        if (i == R.id.three) {
            playTone(3);
            keyPressed(10);
            return;
        }
        if (i == R.id.four) {
            playTone(4);
            keyPressed(11);
            return;
        }
        if (i == R.id.five) {
            playTone(5);
            keyPressed(12);
            return;
        }
        if (i == R.id.six) {
            playTone(6);
            keyPressed(13);
            return;
        }
        if (i == R.id.seven) {
            playTone(7);
            keyPressed(14);
            return;
        }
        if (i == R.id.eight) {
            playTone(8);
            keyPressed(15);
            return;
        }
        if (i == R.id.nine) {
            playTone(9);
            keyPressed(16);
            return;
        }
        if (i == R.id.zero) {
            playTone(0);
            keyPressed(7);
            return;
        }
        if (i == R.id.pound) {
            playTone(11);
            keyPressed(18);
            return;
        }
        if (i == R.id.star) {
            playTone(10);
            keyPressed(17);
            return;
        }
        if (i == R.id.dialButton) {
            this.mDigits.performHapticFeedback(1);
            placeCall();
            return;
        }
        if (i == R.id.backButton) {
            onBackPressed();
            return;
        }
        if (i == R.id.deleteButton) {
            keyPressed(67);
            return;
        }
        if (i == R.id.digits) {
            if (this.mDigits.length() != 0) {
                this.mDigits.setCursorVisible(true);
            }
        } else if (i == 70 || i == 81) {
            keyPressed(81);
        }
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override // com.android.phone.DialpadButton.OnButtonHoverListener
    public boolean onButtonHover(View view, MotionEvent motionEvent) {
        return onHover(view, motionEvent);
    }

    @Override // android.view.View.OnHoverListener
    public boolean onHover(View view, MotionEvent motionEvent) {
        if (this.mAccessibilityManager.isEnabled() && this.mAccessibilityManager.isTouchExplorationEnabled()) {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 9) {
                view.setClickable(false);
            } else if (actionMasked == 10) {
                int paddingLeft = view.getPaddingLeft();
                int width = view.getWidth() - view.getPaddingRight();
                int paddingTop = view.getPaddingTop();
                int height = view.getHeight() - view.getPaddingBottom();
                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                if (x > paddingLeft && x < width && y > paddingTop && y < height) {
                    view.performClick();
                }
                view.setClickable(true);
            }
        }
        return false;
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View view) {
        int id = view.getId();
        if (id == R.id.deleteButton) {
            this.mDigits.getText().clear();
            this.mDigitsNumber = "";
            return true;
        }
        if (id != R.id.zero) {
            return false;
        }
        keyPressed(81);
        return true;
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        this.mDTMFToneEnabled = Settings.System.getInt(getContentResolver(), "dtmf_tone", 1) == 1;
        new Thread(new Runnable() { // from class: com.android.phone.EmergencyDialer.5
            @Override // java.lang.Runnable
            public void run() {
                synchronized (EmergencyDialer.this.mToneGeneratorLock) {
                    if (EmergencyDialer.this.mToneGenerator == null) {
                        try {
                            EmergencyDialer.this.mToneGenerator = new ToneGenerator(8, 80);
                        } catch (RuntimeException e) {
                            Log.w("EmergencyDialer", "Exception caught while creating local tone generator: " + e);
                            EmergencyDialer.this.mToneGenerator = null;
                        }
                    }
                }
            }
        }).start();
        Log.d("EmergencyDialer", "disabling status bar, set to long timeout");
        this.mStatusBarManager.disable(65536);
        updateDialAndDeleteButtonStateEnabledAttr();
    }

    @Override // android.app.Activity
    public void onPause() {
        Log.d("EmergencyDialer", "reenabling status bar and closing the dialer");
        this.mStatusBarManager.disable(0);
        super.onPause();
    }

    private void placeCall() {
        if (this.mIsSCDialer) {
            showDialog(0);
            this.mDigits.getText().clear();
            this.mDigitsNumber = "";
            return;
        }
        String str = this.mDigitsNumber;
        this.mLastNumber = str;
        boolean zIsLocalEmergencyNumber = TelephonyManagerEx.isLocalEmergencyNumber(this, str);
        String str2 = this.mLastNumber;
        if (zIsLocalEmergencyNumber) {
            Log.d("EmergencyDialer", "placing call to " + str2);
            String str3 = this.mLastNumber;
            if (str3 == null || !TextUtils.isGraphic(str3)) {
                playTone(26);
                return;
            }
            ((TelecomManager) getSystemService("telecom")).placeCall(Uri.fromParts(GenericURI.TEL, this.mLastNumber, null), null);
        } else {
            Log.d("EmergencyDialer", "rejecting bad requested number " + str2);
            showDialog(0);
        }
        this.mDigits.getText().delete(0, this.mDigits.getText().length());
        this.mDigitsNumber = "";
    }

    void playTone(int i) {
        int ringerMode;
        if (!this.mDTMFToneEnabled || (ringerMode = ((AudioManager) getSystemService("audio")).getRingerMode()) == 0 || ringerMode == 1) {
            return;
        }
        synchronized (this.mToneGeneratorLock) {
            try {
                ToneGenerator toneGenerator = this.mToneGenerator;
                if (toneGenerator == null) {
                    Log.w("EmergencyDialer", "playTone: mToneGenerator == null, tone: " + i);
                    return;
                }
                toneGenerator.startTone(i, 150);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private CharSequence createErrorMessage(String str) {
        if (this.mIsSCDialer) {
            return getText(R.string.invalid_secretcode_format_txt).toString();
        }
        if (!TextUtils.isEmpty(str)) {
            return getString(R.string.dial_emergency_error, new Object[]{Utils.localizeNumber(this.mLastNumber)});
        }
        return getText(R.string.dial_emergency_empty_error).toString();
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int i) {
        AlertDialog alertDialogCreate;
        if (i == 0) {
            alertDialogCreate = new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight).setTitle(this.mIsSCDialer ? null : getText(R.string.emergency_enable_radio_dialog_title)).setMessage(createErrorMessage(this.mLastNumber)).setPositiveButton(R.string.ok, (DialogInterface.OnClickListener) null).setCancelable(true).create();
            alertDialogCreate.getWindow().addFlags(524288);
        } else {
            alertDialogCreate = null;
        }
        if (i == 1) {
            Log.d("EmergencyDialer", "showDeviceIdPanel()...");
            alertDialogCreate = new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight).setMessage(this.mTm.getDeviceId()).setPositiveButton(R.string.ok, (DialogInterface.OnClickListener) null).setCancelable(true).create();
            alertDialogCreate.getWindow().addFlags(524288);
        }
        if (i != 2) {
            return alertDialogCreate;
        }
        Log.d("EmergencyDialer", "showSARPanel()...");
        float f = HyperOSCustFeatureResolve.getFloat("config_device_body_sar", 0.0f);
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        AlertDialog alertDialogCreate2 = new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight).setTitle("SAR").setMessage("India SAR 1g limit: 1.6W/Kg\nBody SAR: " + decimalFormat.format(f) + "W/Kg").setPositiveButton(R.string.ok, (DialogInterface.OnClickListener) null).setCancelable(true).create();
        alertDialogCreate2.getWindow().addFlags(524288);
        return alertDialogCreate2;
    }

    @Override // android.app.Activity
    protected void onPrepareDialog(int i, Dialog dialog) {
        super.onPrepareDialog(i, dialog);
        if (i == 0) {
            ((AlertDialog) dialog).setMessage(createErrorMessage(this.mLastNumber));
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        onPreTouchEvent(motionEvent);
        updateDialButtonPressLightEffect(motionEvent);
        boolean zDispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        onPostTouchEvent(motionEvent);
        return zDispatchTouchEvent;
    }

    private void updateDialButtonPressLightEffect(MotionEvent motionEvent) {
        if (MaterialUtils.isHyperGlassEnable()) {
            CallOvalImageButton callOvalImageButton = this.mDialButton;
            if (callOvalImageButton == null) {
                Log.i("EmergencyDialer", "updateDialButtonPressLightEffect skip, mDialButton is null, action=" + MotionEvent.actionToString(motionEvent.getAction()));
                return;
            }
            if (!callOvalImageButton.isEnabled()) {
                if (this.mDialButtonPressLightApplied) {
                    Log.i("EmergencyDialer", "updateDialButtonPressLightEffect reset, dial button disabled");
                    resetDialButtonPressLightEffect();
                    return;
                }
                return;
            }
            int action = motionEvent.getAction();
            if (action == 0) {
                if (isTouchInView(this.mDialButton, motionEvent)) {
                    Log.i("EmergencyDialer", "dial button press light down");
                    this.mDialButtonPressLightApplied = true;
                    startDialButtonPressLightEffect(true);
                    return;
                }
                return;
            }
            if (action != 1) {
                if (action == 2) {
                    if (!this.mDialButtonPressLightApplied || isTouchInView(this.mDialButton, motionEvent)) {
                        return;
                    }
                    Log.i("EmergencyDialer", "dial button press light move out");
                    this.mDialButtonPressLightApplied = false;
                    startDialButtonPressLightEffect(false);
                    return;
                }
                if (action != 3) {
                    return;
                }
            }
            if (this.mDialButtonPressLightApplied) {
                Log.i("EmergencyDialer", "dial button press light up/cancel, action=" + MotionEvent.actionToString(motionEvent.getAction()));
                this.mDialButtonPressLightApplied = false;
                startDialButtonPressLightEffect(false);
            }
        }
    }

    private boolean isTouchInView(View view, MotionEvent motionEvent) {
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        int rawX = (int) motionEvent.getRawX();
        int rawY = (int) motionEvent.getRawY();
        int i = iArr[0];
        int i2 = iArr[1];
        return rawX >= i && rawX <= view.getWidth() + i && rawY >= i2 && rawY <= view.getHeight() + i2;
    }

    private void startDialButtonPressLightEffect(boolean z) {
        getDialButtonPressLightAnimator().animate(z);
    }

    private void resetDialButtonPressLightEffect() {
        this.mDialButtonPressLightApplied = false;
        PressLightAnimator pressLightAnimator = this.mDialButtonPressLightAnimator;
        if (pressLightAnimator != null) {
            pressLightAnimator.reset();
        }
    }

    private PressLightAnimator getDialButtonPressLightAnimator() {
        if (this.mDialButtonPressLightAnimator == null) {
            this.mDialButtonPressLightAnimator = new PressLightAnimator(new PressLightAnimator.TokenProvider() { // from class: com.android.phone.EmergencyDialer.6
                @Override // com.android.phone.utils.PressLightAnimator.TokenProvider
                public CallOvalImageButton getGlassView() {
                    return EmergencyDialer.this.mDialButton;
                }

                @Override // com.android.phone.utils.PressLightAnimator.TokenProvider
                public GlassToken getNormalToken() {
                    return MaterialUtils.Glass_Tint_Green_Motion;
                }

                @Override // com.android.phone.utils.PressLightAnimator.TokenProvider
                public GlassToken getPressToken() {
                    return MaterialUtils.Glass_Tint_Green_Motion_Press;
                }
            });
        }
        return this.mDialButtonPressLightAnimator;
    }

    private void updateDialAndDeleteButtonStateEnabledAttr() {
        int dimensionPixelSize;
        RelativeLayout.LayoutParams layoutParams;
        boolean z = this.mDigits.length() != 0;
        if (getResources().getConfiguration().locale.getLanguage().equals("bo") && (layoutParams = (RelativeLayout.LayoutParams) this.mDigits.getLayoutParams()) != null) {
            layoutParams.height = getResources().getDimensionPixelOffset(R.dimen.emergency_digits_tibetan_height);
            this.mDigits.setLayoutParams(layoutParams);
        }
        EmergencyDialerKeyPadEditText emergencyDialerKeyPadEditText = this.mDigits;
        if (z) {
            dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.emergency_input_digits_size);
        } else {
            dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.emergency_input_text_size);
        }
        emergencyDialerKeyPadEditText.setTextSize(0, dimensionPixelSize);
        this.mDialButton.setEnabled(z);
        this.mDelete.setEnabled(z);
    }

    private void startEmergencyListActivity() {
        Intent intent = new Intent();
        intent.setClass(this, MiuiKrEmergencyListActivity.class);
        intent.setFlags(268435456);
        startActivity(intent);
    }

    private boolean handleIMEIDisplay(String str) {
        if (!str.equals("*#06#") || SystemProperties.getBoolean("ro.radio.noril", false)) {
            return false;
        }
        showDialog(1);
        return true;
    }

    private boolean showRegulatoryInfoPanel() {
        if (!Build.checkRegion("IN") || !HyperOSCustFeatureResolve.getBoolean("show_sar_value_in", false)) {
            return false;
        }
        showDialog(2);
        return true;
    }

    private boolean handleSecretCode(String str) {
        int length = str.length();
        if (length > 8 && str.startsWith("*#*#") && str.endsWith("#*#*")) {
            this.mTm.sendDialerSpecialCode(str.substring(4, length - 4));
            return true;
        }
        if (str.equals("*#07#")) {
            return showRegulatoryInfoPanel();
        }
        return false;
    }

    private void queryHealthLockEnter() {
        this.mHealthButton = (Button) findViewById(R.id.mihealth);
        if (Build.IS_INTERNATIONAL_BUILD) {
            return;
        }
        try {
            if (getPackageManager().getApplicationInfo("com.mi.health", 0) == null) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        final Uri uri = Uri.parse("content://com.mi.health.provider.main/firstaid/queryEmergencyEnter");
        new Thread(new Runnable() { // from class: com.android.phone.EmergencyDialer.8
            @Override // java.lang.Runnable
            public void run() {
                Cursor cursorQuery;
                if (EmergencyDialer.this.getContentResolver().getType(uri) == null || (cursorQuery = EmergencyDialer.this.getContentResolver().query(uri, null, null, null, null)) == null) {
                    return;
                }
                while (true) {
                    try {
                        try {
                            boolean zMoveToNext = cursorQuery.moveToNext();
                            EmergencyDialer emergencyDialer = EmergencyDialer.this;
                            if (zMoveToNext) {
                                emergencyDialer.mHealthResult = cursorQuery.getInt(cursorQuery.getColumnIndex(a.L));
                                EmergencyDialer.this.mHealthAction = cursorQuery.getString(cursorQuery.getColumnIndex("action"));
                            } else {
                                emergencyDialer.mHealthHandler.sendEmptyMessage(0);
                                cursorQuery.close();
                                return;
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            cursorQuery.close();
                            return;
                        }
                    } catch (Throwable th) {
                        cursorQuery.close();
                        throw th;
                    }
                    cursorQuery.close();
                    throw th;
                }
            }
        }).start();
    }

    private void onPreTouchEvent(MotionEvent motionEvent) {
        this.mEmergencyActionGroup.onPreTouchEvent(motionEvent);
    }

    private void onPostTouchEvent(MotionEvent motionEvent) {
        this.mEmergencyActionGroup.onPostTouchEvent(motionEvent);
    }
}
