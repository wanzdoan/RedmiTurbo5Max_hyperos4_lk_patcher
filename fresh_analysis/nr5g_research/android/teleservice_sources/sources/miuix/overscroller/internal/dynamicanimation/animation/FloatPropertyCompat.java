package miuix.overscroller.internal.dynamicanimation.animation;

/* JADX INFO: loaded from: classes2.dex */
public abstract class FloatPropertyCompat {
    final String mPropertyName;

    public abstract float getValue(Object obj);

    public abstract void setValue(Object obj, float f);

    public FloatPropertyCompat(String str) {
        this.mPropertyName = str;
    }
}
