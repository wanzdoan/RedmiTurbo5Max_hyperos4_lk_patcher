package miuix.animation.property;

import java.util.Objects;

/* JADX INFO: loaded from: classes2.dex */
public class ColorProperty extends FloatProperty implements IIntValueProperty {
    private int mColorValue;

    @Override // miuix.animation.property.FloatProperty
    public float getValue(Object obj) {
        return 0.0f;
    }

    @Override // miuix.animation.property.FloatProperty
    public void setValue(Object obj, float f) {
    }

    public ColorProperty(String str) {
        super(str);
    }

    @Override // miuix.animation.property.IIntValueProperty
    public void setIntValue(Object obj, int i) {
        this.mColorValue = i;
        if (obj instanceof ValueTargetObject) {
            ((ValueTargetObject) obj).setPropertyValue(getName(), Integer.TYPE, Integer.valueOf(i));
        }
    }

    @Override // miuix.animation.property.IIntValueProperty
    public int getIntValue(Object obj) {
        if (obj instanceof ValueTargetObject) {
            this.mColorValue = ((Integer) ((ValueTargetObject) obj).getPropertyValue(getName(), Integer.TYPE)).intValue();
        }
        return this.mColorValue;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this.mPropertyName.equals(((ColorProperty) obj).mPropertyName);
    }

    public int hashCode() {
        return Objects.hash(this.mPropertyName);
    }
}
