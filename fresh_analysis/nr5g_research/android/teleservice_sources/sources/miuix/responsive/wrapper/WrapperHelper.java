package miuix.responsive.wrapper;

import android.view.LayoutInflater;
import androidx.core.view.LayoutInflaterCompat;
import java.lang.reflect.InvocationTargetException;
import miuix.reflect.ReflectionHelper;
import miuix.reflect.Reflects$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes2.dex */
public abstract class WrapperHelper {
    public static void setFactory2(LayoutInflater layoutInflater, LayoutInflater.Factory2 factory2) {
        LayoutInflater.Factory2 factory3 = layoutInflater.getFactory2();
        if (factory3 != null || layoutInflater.getFactory() != null) {
            if (factory3 instanceof Factory2Wrapper) {
                ((Factory2Wrapper) layoutInflater.getFactory2()).setOriginFactory2(factory2);
                return;
            }
            if (factory2 instanceof Factory2Wrapper) {
                ((Factory2Wrapper) factory2).setOriginFactory2(factory3);
            }
            try {
                ReflectionHelper.setFieldValue(layoutInflater.getClass().getSuperclass(), layoutInflater, "mFactory2", factory2);
                return;
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                Reflects$$ExternalSyntheticBUOutline0.m(e);
                return;
            }
        }
        LayoutInflaterCompat.setFactory2(layoutInflater, factory2);
    }
}
