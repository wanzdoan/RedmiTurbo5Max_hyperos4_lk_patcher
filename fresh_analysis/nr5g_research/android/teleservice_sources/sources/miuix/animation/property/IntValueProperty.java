package miuix.animation.property;

/* JADX INFO: loaded from: classes2.dex */
public class IntValueProperty extends ValueProperty implements IIntValueProperty {
    public IntValueProperty(String str) {
        super(str, 1.0f);
    }

    public IntValueProperty(String str, float f) {
        super(str, f);
    }

    @Override // miuix.animation.property.IIntValueProperty
    public void setIntValue(Object obj, int i) {
        if (obj instanceof ValueTargetObject) {
            ((ValueTargetObject) obj).setPropertyValue(getName(), Integer.TYPE, Integer.valueOf(i));
        }
    }

    @Override // miuix.animation.property.IIntValueProperty
    public int getIntValue(Object obj) {
        Integer num;
        if (!(obj instanceof ValueTargetObject) || (num = (Integer) ((ValueTargetObject) obj).getPropertyValue(getName(), Integer.TYPE)) == null) {
            return Integer.MAX_VALUE;
        }
        return num.intValue();
    }

    @Override // miuix.animation.property.ValueProperty, miuix.animation.property.FloatProperty
    public String toString() {
        return "IntValueProperty@" + hashCode() + "{name='" + getName() + "',min='" + this.mMinVisibleChange + "'}";
    }
}
