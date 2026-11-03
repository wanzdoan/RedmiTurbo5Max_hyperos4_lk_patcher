package com.android.phone.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import com.android.phone.EmergencyDialer;
import com.android.phone.R;

/* JADX INFO: loaded from: classes.dex */
public class EmergencyDialerKeyPadEditText extends EditText {
    public EmergencyDialerKeyPadEditText(Context context) {
        super(context);
    }

    public EmergencyDialerKeyPadEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public EmergencyDialerKeyPadEditText(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public EmergencyDialerKeyPadEditText(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    @Override // android.widget.TextView, android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return onKeyPreIme(i, keyEvent);
    }

    /* JADX WARN: Code duplicated, block: B:42:0x0086  */
    @Override // android.widget.TextView, android.view.View
    public boolean onKeyPreIme(int i, KeyEvent keyEvent) {
        int i2;
        setCursorVisible(false);
        Context context = getContext();
        int action = keyEvent.getAction();
        Log.i("EmergencyDialerKeyPadEditText", "keycode = " + i + ", action = " + action);
        if (action == 0) {
            if (keyEvent.isShiftPressed()) {
                if (i == 10) {
                    i2 = R.id.pound;
                } else if (i != 15) {
                    i2 = 70;
                    if (i != 70) {
                        i2 = -1;
                    }
                } else {
                    i2 = R.id.star;
                }
            } else if (i == 4) {
                i2 = R.id.backButton;
            } else if (i != 67) {
                i2 = 81;
                if (i != 81) {
                    switch (i) {
                        case 7:
                            i2 = R.id.zero;
                            break;
                        case 8:
                            i2 = R.id.one;
                            break;
                        case 9:
                            i2 = R.id.two;
                            break;
                        case 10:
                            i2 = R.id.three;
                            break;
                        case 11:
                            i2 = R.id.four;
                            break;
                        case 12:
                            i2 = R.id.five;
                            break;
                        case 13:
                            i2 = R.id.six;
                            break;
                        case 14:
                            i2 = R.id.seven;
                            break;
                        case 15:
                            i2 = R.id.eight;
                            break;
                        case 16:
                            i2 = R.id.nine;
                            break;
                        case 17:
                            i2 = R.id.star;
                            break;
                        case 18:
                            i2 = R.id.pound;
                            break;
                        default:
                            i2 = -1;
                            break;
                    }
                }
            } else {
                i2 = R.id.deleteButton;
            }
        } else if (action == 1 && i == 66) {
            i2 = R.id.dialButton;
        } else {
            i2 = -1;
        }
        if (context instanceof EmergencyDialer) {
            ((EmergencyDialer) context).handleNumberPadClick(i2);
        }
        return true;
    }
}
