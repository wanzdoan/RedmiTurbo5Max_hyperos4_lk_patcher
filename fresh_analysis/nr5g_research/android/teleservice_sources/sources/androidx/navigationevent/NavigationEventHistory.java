package androidx.navigationevent;

import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: NavigationEventHistory.kt */
/* JADX INFO: loaded from: classes.dex */
public final class NavigationEventHistory {
    private List _mergedHistory;
    private final List backInfo;
    private final int currentIndex;
    private final NavigationEventInfo currentInfo;
    private final List forwardInfo;

    private NavigationEventHistory(NavigationEventInfo navigationEventInfo, List list, List list2, int i) {
        this.currentInfo = navigationEventInfo;
        this.backInfo = list;
        this.forwardInfo = list2;
        this.currentIndex = i;
    }

    /* synthetic */ NavigationEventHistory(NavigationEventInfo navigationEventInfo, List list, List list2, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? null : navigationEventInfo, (i2 & 2) != 0 ? CollectionsKt.emptyList() : list, (i2 & 4) != 0 ? CollectionsKt.emptyList() : list2, i);
    }

    public NavigationEventHistory() {
        this(null, null, null, -1, 7, null);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public NavigationEventHistory(NavigationEventInfo navigationEventInfo, List list, List list2) {
        this(navigationEventInfo, list, list2, list.size());
        navigationEventInfo.getClass();
        list.getClass();
        list2.getClass();
    }

    public final List getMergedHistory() {
        if (this._mergedHistory == null) {
            List listCreateListBuilder = CollectionsKt.createListBuilder();
            listCreateListBuilder.addAll(this.backInfo);
            NavigationEventInfo navigationEventInfo = this.currentInfo;
            if (navigationEventInfo != null) {
                listCreateListBuilder.add(navigationEventInfo);
            }
            listCreateListBuilder.addAll(this.forwardInfo);
            this._mergedHistory = CollectionsKt.build(listCreateListBuilder);
        }
        List list = this._mergedHistory;
        list.getClass();
        return list;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || NavigationEventHistory.class != obj.getClass()) {
            return false;
        }
        NavigationEventHistory navigationEventHistory = (NavigationEventHistory) obj;
        return this.currentIndex == navigationEventHistory.currentIndex && Intrinsics.areEqual(getMergedHistory(), navigationEventHistory.getMergedHistory());
    }

    public int hashCode() {
        return (this.currentIndex * 31) + getMergedHistory().hashCode();
    }

    public String toString() {
        return "NavigationEventHistory(currentIndex=" + this.currentIndex + ", mergedHistory=" + getMergedHistory() + ')';
    }
}
