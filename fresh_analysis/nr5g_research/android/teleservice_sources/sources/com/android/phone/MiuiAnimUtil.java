package com.android.phone;

import android.view.View;
import miuix.animation.Folme;
import miuix.animation.ITouchStyle;
import miuix.animation.base.AnimConfig;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiuiAnimUtil {
    public static void defaultTouch(View view) {
        Folme.useAt(view).touch().setTint(0.0f, 0.0f, 0.0f, 0.0f).handleTouchOf(view, new AnimConfig[0]);
    }

    public static void alphaTouch(View view) {
        ITouchStyle iTouchStyle = Folme.useAt(view).touch();
        ITouchStyle.TouchType touchType = ITouchStyle.TouchType.DOWN;
        ITouchStyle scale = iTouchStyle.setScale(1.0f, touchType);
        ITouchStyle.TouchType touchType2 = ITouchStyle.TouchType.UP;
        scale.setScale(1.0f, touchType2).setAlpha(0.5f, touchType).setAlpha(1.0f, touchType2).setTint(0.0f, 0.0f, 0.0f, 0.0f).handleTouchOf(view, new AnimConfig[0]);
    }
}
