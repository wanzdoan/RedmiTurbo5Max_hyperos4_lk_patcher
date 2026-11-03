package miuix.animation.property;

import android.util.Property;

/* JADX INFO: loaded from: classes2.dex */
public abstract class FloatProperty extends Property {
    float mMinVisibleChange;
    final String mPropertyName;

    public abstract float getValue(Object obj);

    public abstract void setValue(Object obj, float f);

    public FloatProperty(String str) {
        this(str, -1.0f);
    }

    public FloatProperty(String str, float f) {
        super(Float.class, str);
        this.mPropertyName = str;
        this.mMinVisibleChange = f;
    }

    @Override // android.util.Property
    public Float get(Object obj) {
        if (obj == null) {
            return Float.valueOf(0.0f);
        }
        return Float.valueOf(getValue(obj));
    }

    @Override // android.util.Property
    public final void set(Object obj, Float f) {
        if (obj != null) {
            setValue(obj, f.floatValue());
        }
    }

    public void setMinVisibleChange(float f) {
        this.mMinVisibleChange = f;
    }

    public float getMinVisibleChange() {
        return this.mMinVisibleChange;
    }

    public String toString() {
        return getClass().getSimpleName() + "@" + hashCode() + "{name='" + this.mPropertyName + "',min='" + this.mMinVisibleChange + "'}";
    }
}
