package androidx.appsearch.app;

import androidx.core.util.ObjectsCompat;
import androidx.core.util.Preconditions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;
import org.tensorflow.lite.DataType$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public class PropertyPath implements Iterable {
    private final List mPathList;

    public PropertyPath(String str) {
        Preconditions.checkNotNull(str);
        this.mPathList = new ArrayList();
        try {
            recursivePathScan(str);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage() + ": " + str);
        }
    }

    private void recursivePathScan(String str) {
        boolean z;
        String strConsumePropertyWithIndex;
        int i = 0;
        while (true) {
            if (i >= str.length()) {
                z = false;
                i = -1;
                break;
            }
            char cCharAt = str.charAt(i);
            if (cCharAt == ']') {
                Folme$$ExternalSyntheticBUOutline0.m("Malformed path (no starting '[')");
                return;
            }
            if (cCharAt == '[' || cCharAt == '.') {
                if (cCharAt != '[') {
                    z = false;
                    break;
                } else {
                    z = true;
                    break;
                }
            }
            i++;
        }
        if (i == 0 || str.isEmpty()) {
            Folme$$ExternalSyntheticBUOutline0.m("Malformed path (blank property name)");
            return;
        }
        if (i == -1) {
            this.mPathList.add(new PathSegment(str, -1));
            return;
        }
        if (!z) {
            String strSubstring = str.substring(0, i);
            strConsumePropertyWithIndex = str.substring(i + 1);
            this.mPathList.add(new PathSegment(strSubstring, -1));
        } else {
            strConsumePropertyWithIndex = consumePropertyWithIndex(str, i);
            if (strConsumePropertyWithIndex == null) {
                return;
            }
        }
        recursivePathScan(strConsumePropertyWithIndex);
    }

    private String consumePropertyWithIndex(String str, int i) {
        Preconditions.checkNotNull(str);
        String strSubstring = str.substring(0, i);
        int iIndexOf = str.indexOf(93, i);
        if (iIndexOf == -1) {
            Folme$$ExternalSyntheticBUOutline0.m("Malformed path (no ending ']')");
            return null;
        }
        int i2 = iIndexOf + 1;
        if (i2 < str.length() && str.charAt(i2) != '.') {
            Folme$$ExternalSyntheticBUOutline0.m("Malformed path (']' not followed by '.'): ".concat(str));
            return null;
        }
        String strSubstring2 = str.substring(i + 1, iIndexOf);
        try {
            int i3 = Integer.parseInt(strSubstring2);
            if (i3 < 0) {
                Folme$$ExternalSyntheticBUOutline0.m("Malformed path (path index less than 0)");
                return null;
            }
            this.mPathList.add(new PathSegment(strSubstring, i3));
            if (i2 < str.length()) {
                return str.substring(iIndexOf + 2);
            }
            return null;
        } catch (NumberFormatException unused) {
            DataType$$ExternalSyntheticBUOutline0.m("Malformed path (\"", strSubstring2, "\" as path index)");
            return null;
        }
    }

    public PathSegment get(int i) {
        return (PathSegment) this.mPathList.get(i);
    }

    public int size() {
        return this.mPathList.size();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.mPathList.size(); i++) {
            sb.append(get(i).toString());
            if (i < this.mPathList.size() - 1) {
                sb.append('.');
            }
        }
        return sb.toString();
    }

    @Override // java.lang.Iterable
    public Iterator iterator() {
        return this.mPathList.iterator();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && (obj instanceof PropertyPath)) {
            return ObjectsCompat.equals(this.mPathList, ((PropertyPath) obj).mPathList);
        }
        return false;
    }

    public int hashCode() {
        return ObjectsCompat.hashCode(this.mPathList);
    }

    public class PathSegment {
        private final int mPropertyIndex;
        private final String mPropertyName;

        PathSegment(String str, int i) {
            this.mPropertyName = (String) ObjectsCompat.requireNonNull(str);
            this.mPropertyIndex = i;
        }

        public String getPropertyName() {
            return this.mPropertyName;
        }

        public int getPropertyIndex() {
            return this.mPropertyIndex;
        }

        public String toString() {
            int i = this.mPropertyIndex;
            String str = this.mPropertyName;
            if (i == -1) {
                return str;
            }
            return str + "[" + this.mPropertyIndex + "]";
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !(obj instanceof PathSegment)) {
                return false;
            }
            PathSegment pathSegment = (PathSegment) obj;
            return this.mPropertyIndex == pathSegment.mPropertyIndex && this.mPropertyName.equals(pathSegment.mPropertyName);
        }

        public int hashCode() {
            return ObjectsCompat.hash(this.mPropertyName, Integer.valueOf(this.mPropertyIndex));
        }
    }
}
