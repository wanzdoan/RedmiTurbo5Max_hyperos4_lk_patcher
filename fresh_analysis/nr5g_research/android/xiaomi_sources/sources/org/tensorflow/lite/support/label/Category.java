package org.tensorflow.lite.support.label;

import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class Category {
    private final String displayName;
    private final String label;
    private final float score;

    public static Category create(String label, String displayName, float score) {
        return new Category(label, displayName, score);
    }

    public Category(String label, float score) {
        this(label, "", score);
    }

    private Category(String label, String displayName, float score) {
        this.label = label;
        this.displayName = displayName;
        this.score = score;
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

    public boolean equals(Object o) {
        if (!(o instanceof Category)) {
            return false;
        }
        Category other = (Category) o;
        return other.getLabel().equals(this.label) && other.getDisplayName().equals(this.displayName) && other.getScore() == this.score;
    }

    public int hashCode() {
        return Objects.hash(this.label, this.displayName, Float.valueOf(this.score));
    }

    public String toString() {
        return "<Category \"" + this.label + "\" (displayName=" + this.displayName + "\" (score=" + this.score + ")>";
    }
}
