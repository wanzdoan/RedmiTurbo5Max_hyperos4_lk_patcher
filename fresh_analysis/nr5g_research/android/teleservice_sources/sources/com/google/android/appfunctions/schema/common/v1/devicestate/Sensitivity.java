package com.google.android.appfunctions.schema.common.v1.devicestate;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: compiled from: DeviceStateMetadata.kt */
/* JADX INFO: loaded from: classes.dex */
@Retention(RetentionPolicy.SOURCE)
public @interface Sensitivity {
    public static final Companion Companion = Companion.$$INSTANCE;
    public static final String MUST_PROVIDE_UNDO = "MUST_PROVIDE_UNDO";
    public static final String REQUIRES_CONFIRMATION = "REQUIRES_CONFIRMATION";

    /* JADX INFO: compiled from: DeviceStateMetadata.kt */
    public final class Companion {
        static final /* synthetic */ Companion $$INSTANCE = new Companion();
        public static final String MUST_PROVIDE_UNDO = "MUST_PROVIDE_UNDO";
        public static final String REQUIRES_CONFIRMATION = "REQUIRES_CONFIRMATION";

        private Companion() {
        }
    }
}
