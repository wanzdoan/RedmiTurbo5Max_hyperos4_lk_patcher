package com.google.android.appfunctions.schema.common.v1.navigation;

import java.util.List;

/* JADX INFO: compiled from: Navigation.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class Route {
    public abstract List getAdditionalInfo();

    public abstract Distance getDistance();

    public abstract Duration getDuration();

    public abstract Fare getFare();

    public abstract String getId();

    public abstract List getLegs();

    public abstract String getNamespace();

    public abstract String getRouteLabel();

    public abstract String getRouteToken();

    public abstract String getSummary();

    public abstract String getTransportationType();
}
