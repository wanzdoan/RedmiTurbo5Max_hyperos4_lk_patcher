package miuix.animation.listener;

import java.util.Collection;
import java.util.Iterator;
import miuix.animation.IAnimTarget;
import miuix.animation.base.AnimSpecialConfig;
import miuix.animation.internal.AnimInfo;
import miuix.animation.internal.AnimTask;
import miuix.animation.property.FloatProperty;
import miuix.animation.property.IIntValueProperty;
import miuix.animation.utils.LogUtils;

/* JADX INFO: loaded from: classes2.dex */
public class UpdateInfo {
    public int frameCount;
    public boolean isCompleted;
    public boolean justStart;
    public final FloatProperty property;
    public final boolean useInt;
    public double velocity;
    public Integer preparedTransitionId = null;
    public final AnimInfo animInfo = new AnimInfo();

    public static UpdateInfo findByName(Collection collection, String str) {
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            UpdateInfo updateInfo = (UpdateInfo) it.next();
            if (updateInfo.property.getName().equals(str)) {
                return updateInfo;
            }
        }
        return null;
    }

    public static UpdateInfo findBy(Collection collection, FloatProperty floatProperty) {
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            UpdateInfo updateInfo = (UpdateInfo) it.next();
            if (updateInfo.property.equals(floatProperty)) {
                return updateInfo;
            }
        }
        return null;
    }

    public UpdateInfo(FloatProperty floatProperty) {
        this.property = floatProperty;
        this.useInt = floatProperty instanceof IIntValueProperty;
    }

    public Class getType() {
        return this.property instanceof IIntValueProperty ? Integer.TYPE : Float.TYPE;
    }

    public Object getValue(Class cls) {
        if (cls == Float.class || cls == Float.TYPE) {
            return Float.valueOf(getFloatValue());
        }
        if (cls == Double.class || cls == Double.TYPE) {
            return Double.valueOf(this.animInfo.value);
        }
        return Integer.valueOf(getIntValue());
    }

    public float getFloatValue() {
        AnimInfo animInfo = this.animInfo;
        double d = animInfo.setToValue;
        if (d != Double.MAX_VALUE) {
            return (float) d;
        }
        double d2 = animInfo.value;
        if (d2 >= 3.4028234663852886E38d) {
            d2 = 3.4028234663852886E38d;
        }
        float f = (float) d2;
        if (f == Float.MAX_VALUE) {
            LogUtils.debug("warning value is Float.MAX_VALUE !! correct to startValue " + this.animInfo.startValue + " " + this, new Object[0]);
            AnimInfo animInfo2 = this.animInfo;
            double d3 = animInfo2.startValue;
            animInfo2.value = d3;
            return (float) d3;
        }
        return Math.max(-3.4028235E38f, f);
    }

    public int getIntValue() {
        AnimInfo animInfo = this.animInfo;
        double d = animInfo.setToValue;
        if (d != Double.MAX_VALUE) {
            return (int) d;
        }
        double d2 = animInfo.value;
        int i = d2 >= Double.MAX_VALUE ? Integer.MAX_VALUE : (int) d2;
        if (i == Integer.MAX_VALUE) {
            LogUtils.debug("warning value is Integer.MAX_VALUE !! correct to startValue " + this.animInfo.startValue + " " + this, new Object[0]);
            AnimInfo animInfo2 = this.animInfo;
            double d3 = animInfo2.startValue;
            animInfo2.value = d3;
            return (int) d3;
        }
        return Math.max(-2147483647, i);
    }

    public String toString() {
        boolean z = this.useInt;
        AnimInfo animInfo = this.animInfo;
        return "UpdateInfo{id=" + hashCode() + " " + this.property.getName() + "=" + this.animInfo.value + ", v_format=" + (z ? Integer.toHexString((int) animInfo.value) : Double.toString(animInfo.value)) + ", p=" + this.property + ", op=" + ((int) this.animInfo.op) + ", v=" + this.animInfo.value + ", start-v=" + this.animInfo.startValue + ", target-v=" + this.animInfo.targetValue + ", useInt=" + this.useInt + ", completed=" + this.isCompleted + ", setTo-v=" + this.animInfo.setToValue + ", velocity=" + this.velocity + ", start-t=" + this.animInfo.startTime + ", frameCount=" + this.frameCount + ", frameInterval=" + this.animInfo.frameInterval + '}';
    }

    public void reset() {
        this.isCompleted = false;
        this.frameCount = 0;
    }

    public void setOp(byte b) {
        boolean z = b == 0 || b > 2;
        this.isCompleted = z;
        if (z && AnimTask.isRunning(this.animInfo.op)) {
            this.animInfo.justEnd = true;
        }
        this.animInfo.op = b;
        if (LogUtils.isLogMoreEnable()) {
            LogUtils.debug("---- UpdateInfo id=" + hashCode(), "name=" + this.property.getName(), "setOp=" + ((int) b), "justEnd=" + this.animInfo.justEnd, "completed=" + this.isCompleted);
        }
    }

    public boolean isValid() {
        return this.property != null;
    }

    public void skipToTargetValue(IAnimTarget iAnimTarget) {
        AnimInfo animInfo = this.animInfo;
        double d = animInfo.targetValue;
        if (d != Double.MAX_VALUE) {
            animInfo.value = d;
        }
        this.velocity = 0.0d;
        setTargetValue(iAnimTarget, false);
        setOp((byte) 3);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void setTargetValue(IAnimTarget iAnimTarget, boolean z) {
        boolean z2 = this.useInt;
        if (z) {
            FloatProperty floatProperty = this.property;
            if (z2) {
                iAnimTarget.doSetIntValue((IIntValueProperty) floatProperty, getIntValue());
                return;
            } else {
                iAnimTarget.doSetValue(floatProperty, getFloatValue());
                return;
            }
        }
        FloatProperty floatProperty2 = this.property;
        if (z2) {
            iAnimTarget.setIntValue((IIntValueProperty) floatProperty2, getIntValue());
        } else {
            iAnimTarget.setValue(floatProperty2, getFloatValue());
        }
    }

    public static int getSafeIntValue(int i, AnimSpecialConfig animSpecialConfig) {
        if (animSpecialConfig == null || !animSpecialConfig.hasSetSafeValue) {
            return i;
        }
        double d = animSpecialConfig.minValue;
        if (i < ((int) d)) {
            return (int) d;
        }
        double d2 = animSpecialConfig.maxValue;
        return i > ((int) d2) ? (int) d2 : i;
    }

    public static float getSafeFloatValue(float f, AnimSpecialConfig animSpecialConfig) {
        if (animSpecialConfig == null || !animSpecialConfig.hasSetSafeValue) {
            return f;
        }
        double d = animSpecialConfig.minValue;
        if (f < ((float) d)) {
            return (float) d;
        }
        double d2 = animSpecialConfig.maxValue;
        return f > ((float) d2) ? (float) d2 : f;
    }
}
