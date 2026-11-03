package com.android.phone.eventhub.events;

import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import miui.process.ForegroundInfo;

/* JADX INFO: loaded from: classes.dex */
public abstract class DeviceStateEvent extends BaseEvent {
    public DeviceStateEvent(String str) {
        super(DeviceStateKt.DEVICE_STATE_CATEGORY, str);
    }

    public DeviceStateEvent(String str, boolean z) {
        super(DeviceStateKt.DEVICE_STATE_CATEGORY, str, z);
    }

    public class DisplayEvent extends DeviceStateEvent {
        private int mDisplayId;
        private int mDisplayState;

        public DisplayEvent(String str, int i, int i2) {
            super(str, true);
            this.mDisplayId = i;
            this.mDisplayState = i2;
        }

        public int getDisplayState() {
            return this.mDisplayState;
        }

        public boolean isScreenOn() {
            return this.mDisplayState == 2;
        }

        public boolean isScreenOff() {
            return this.mDisplayState == 1;
        }

        public boolean isDefaultDisplay() {
            return this.mDisplayId == 0;
        }
    }

    public class AppStateEvent extends DeviceStateEvent {
        private final ForegroundInfo mForegroundInfo;

        public AppStateEvent(ForegroundInfo foregroundInfo) {
            super("foreground_changed");
            this.mForegroundInfo = foregroundInfo;
        }

        public ForegroundInfo getForegroundInfo() {
            return this.mForegroundInfo;
        }
    }
}
