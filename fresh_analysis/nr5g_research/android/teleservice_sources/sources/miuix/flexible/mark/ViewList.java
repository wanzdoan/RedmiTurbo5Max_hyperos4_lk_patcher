package miuix.flexible.mark;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class ViewList extends ViewNode {
    private int orientation = 1;
    private final List list = new ArrayList();

    public List getList() {
        return this.list;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void setOrientation(int i) {
        this.orientation = i;
    }
}
