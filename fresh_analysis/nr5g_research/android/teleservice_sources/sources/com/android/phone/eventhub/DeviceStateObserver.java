package com.android.phone.eventhub;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.view.Display;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.eventhub.events.EventDispatcher;
import miui.process.ForegroundInfo;
import miui.process.IForegroundInfoListener;
import miui.process.ProcessManager;

/* JADX INFO: loaded from: classes.dex */
public class DeviceStateObserver extends EventObserver {
    private final DisplayManager.DisplayListener mDisplayListener;
    private final DisplayManager mDisplayManager;
    private final IForegroundInfoListener mForegroundInfoListener;

    public DeviceStateObserver(Context context, EventDispatcher eventDispatcher) {
        super(context, eventDispatcher);
        this.mDisplayListener = new DisplayManager.DisplayListener() { // from class: com.android.phone.eventhub.DeviceStateObserver.1
            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayAdded(int i) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayRemoved(int i) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayChanged(int i) {
                Display display = DeviceStateObserver.this.mDisplayManager.getDisplay(i);
                if (display == null) {
                    DeviceStateObserver.this.logd("onDisplayChanged: display not found for id: " + i);
                    return;
                }
                int state = display.getState();
                DeviceStateObserver.this.logd("onDisplayChanged: displayId = " + i + ", displayState: " + state);
                DeviceStateObserver.this.notifyEvent(new DeviceStateEvent.DisplayEvent("display_state_changed", i, state));
            }
        };
        this.mForegroundInfoListener = new IForegroundInfoListener.Stub() { // from class: com.android.phone.eventhub.DeviceStateObserver.2
            public void onForegroundInfoChanged(ForegroundInfo foregroundInfo) {
                if (foregroundInfo != null) {
                    DeviceStateObserver.this.logd("onForegroundInfoChanged: foregroundInfo = " + foregroundInfo);
                    DeviceStateObserver.this.notifyEvent(new DeviceStateEvent.AppStateEvent(foregroundInfo));
                }
            }
        };
        this.mDisplayManager = (DisplayManager) this.mContext.getSystemService("display");
    }

    @Override // com.android.phone.eventhub.EventObserver
    public void startTracking() {
        this.mDisplayManager.registerDisplayListener(this.mDisplayListener, null);
        ProcessManager.registerForegroundInfoListener(this.mForegroundInfoListener);
    }
}
