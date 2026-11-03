package com.google.android.appfunctions.schema.common.v1.navigation;

/* JADX INFO: compiled from: Navigation.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class Leg {
    public abstract Distance getDistance();

    public abstract Duration getDuration();

    public abstract LocationInfo getEndLocation();

    public abstract String getId();

    public abstract String getNamespace();

    public abstract LocationInfo getStartLocation();
}
