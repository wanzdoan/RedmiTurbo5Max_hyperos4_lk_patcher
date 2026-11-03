package kotlin.reflect;

/* JADX INFO: compiled from: KClass.kt */
/* JADX INFO: loaded from: classes2.dex */
public interface KClass {
    String getQualifiedName();

    String getSimpleName();

    boolean isInstance(Object obj);
}
