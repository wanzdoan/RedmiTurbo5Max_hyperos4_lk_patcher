package com.google.android.appfunctions.schema.common.v1.navigation;

import java.util.List;

/* JADX INFO: compiled from: Navigation.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class FindDirectionsParams {
    public abstract List getAvoidTypes();

    public abstract LocationQuery getDestination();

    public abstract String getId();

    public abstract String getNamespace();

    public abstract LocationQuery getOrigin();

    public abstract String getTransportationType();

    public abstract List getWaypoints();
}
