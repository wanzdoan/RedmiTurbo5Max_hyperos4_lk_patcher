package kotlin.collections;

import java.util.Collection;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: Collections.kt */
/* JADX INFO: loaded from: classes2.dex */
public abstract class CollectionsKt__CollectionsKt extends CollectionsKt__CollectionsJVMKt {
    public static final Collection asCollection(Object[] objArr, boolean z) {
        objArr.getClass();
        return new ArrayAsCollection(objArr, z);
    }

    public static /* synthetic */ Collection asCollection$default(Object[] objArr, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        return asCollection(objArr, z);
    }

    public static List emptyList() {
        return EmptyList.INSTANCE;
    }

    public static List listOf(Object... objArr) {
        objArr.getClass();
        return objArr.length > 0 ? ArraysKt___ArraysJvmKt.asList(objArr) : emptyList();
    }

    public static int getLastIndex(List list) {
        list.getClass();
        return list.size() - 1;
    }

    public static final List optimizeReadOnlyList(List list) {
        list.getClass();
        int size = list.size();
        if (size != 0) {
            return size != 1 ? list : CollectionsKt__CollectionsJVMKt.listOf(list.get(0));
        }
        return emptyList();
    }

    public static void throwIndexOverflow() {
        throw new ArithmeticException("Index overflow has happened.");
    }
}
