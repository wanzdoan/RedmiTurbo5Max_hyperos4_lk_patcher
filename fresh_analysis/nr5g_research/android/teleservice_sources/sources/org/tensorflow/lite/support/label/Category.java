package org.tensorflow.lite.support.label;

import java.util.Objects;

/* JADX INFO: loaded from: classes2.dex */
public final class Category {
    private final String displayName;
    private final String label;
    private final float score;

    public static Category create(String str, String str2, float f) {
        return new Category(str, str2, f);
    }

    public Category(String str, float f) {
        this(str, "", f);
    }

    private Category(String str, String str2, float f) {
        this.label = str;
        this.displayName = str2;
        this.score = f;
    }

    public String getLabel() {
        return this.label;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public float getScore() {
        return this.score;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Category) {
            Category category = (Category) obj;
            if (category.getLabel().equals(this.label) && category.getDisplayName().equals(this.displayName) && category.getScore() == this.score) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.label, this.displayName, Float.valueOf(this.score));
    }

    public String toString() {
        return "<Category \"" + this.label + "\" (displayName=" + this.displayName + "\" (score=" + this.score + ")>";
    }
}
