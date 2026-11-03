package kotlin.jvm.internal;

import kotlin.reflect.KClass;

/* JADX INFO: loaded from: classes2.dex */
public class ReflectionFactory {
    public KClass getOrCreateKotlinClass(Class cls) {
        return new ClassReference(cls);
    }

    public String renderLambdaToString(FunctionBase functionBase) {
        String string = functionBase.getClass().getGenericInterfaces()[0].toString();
        return string.startsWith("kotlin.jvm.functions.") ? string.substring(21) : string;
    }
}
