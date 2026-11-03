package com.xiaomi.mirror;

import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.InputEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import com.xiaomi.mirror.opensdk.R;
import com.xiaomi.mirror.widget.ImmersionAdapter;
import com.xiaomi.mirror.widget.ImmersionListPopupWindow;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class MirrorManagerImpl {
    private static volatile MirrorManagerImpl INSTANCE = null;
    private static final String PACKAGE_NAME_MIRROR = "com.xiaomi.mirror";
    private static final String TAG = "MirrorManagerImpl";
    private ImmersionListPopupWindow mImmersionListPopupWindow;
    private IMirrorManager mMirrorManager;

    private MirrorManagerImpl(Context context) {
        if (context == null) {
            return;
        }
        try {
            IMirrorManager iMirrorManager = (IMirrorManager) MirrorManager.get(context.getApplicationContext());
            this.mMirrorManager = iMirrorManager;
            if (iMirrorManager != null) {
                iMirrorManager.setOnMirrorMenuClickListener(new IMirrorManager.OnMirrorMenuClickListener() { // from class: com.xiaomi.mirror.MirrorManagerImpl.1
                    @Override // com.xiaomi.mirror.IMirrorManager.OnMirrorMenuClickListener
                    public boolean onMirrorMenuClick(View view) {
                        ArrayList arrayListOnMirrorMenuQuery;
                        OnMirrorMenuQueryListener onMirrorMenuQueryListener = MirrorManagerImpl.getOnMirrorMenuQueryListener(view);
                        if (onMirrorMenuQueryListener == null || (arrayListOnMirrorMenuQuery = onMirrorMenuQueryListener.onMirrorMenuQuery(view)) == null || arrayListOnMirrorMenuQuery.size() == 0) {
                            return false;
                        }
                        MirrorManagerImpl.this.showPopupWindow(view, arrayListOnMirrorMenuQuery);
                        return true;
                    }
                });
            }
        } catch (Throwable th) {
            Log.v(TAG, "MirrorManagerImpl " + th.toString());
        }
    }

    public static MirrorManagerImpl get(Context context) {
        if (INSTANCE == null) {
            synchronized (MirrorManagerImpl.class) {
                try {
                    if (INSTANCE == null) {
                        INSTANCE = new MirrorManagerImpl(context);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static OnMirrorMenuQueryListener getOnMirrorMenuQueryListener(View view) {
        if (view == null) {
            return null;
        }
        Object tag = view.getTag(R.id.tag_mirror_menu_query_listener);
        if ((tag == null || (tag instanceof OnMirrorMenuQueryListener)) && tag != null) {
            return (OnMirrorMenuQueryListener) tag;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPopupWindow(final View view, final ArrayList arrayList) {
        if (view == null || arrayList == null || arrayList.size() < 1) {
            return;
        }
        ImmersionListPopupWindow immersionListPopupWindow = this.mImmersionListPopupWindow;
        if (immersionListPopupWindow == null || !immersionListPopupWindow.isShowing()) {
            ArrayList arrayList2 = new ArrayList();
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                MirrorMenu mirrorMenu = (MirrorMenu) obj;
                if (mirrorMenu.getType() == 1) {
                    arrayList2.add(ImmersionAdapter.buildSettingViewEntry(view.getContext().getString(R.string.menu_new_display_open)));
                } else if (mirrorMenu.getType() == 2) {
                    arrayList2.add(ImmersionAdapter.buildSettingViewEntry(view.getContext().getString(R.string.menu_pc_open)));
                } else {
                    arrayList2.add(ImmersionAdapter.buildSettingViewEntry(mirrorMenu.getLabel()));
                }
            }
            ImmersionAdapter immersionAdapter = new ImmersionAdapter(view.getContext(), arrayList2);
            ImmersionListPopupWindow immersionListPopupWindow2 = new ImmersionListPopupWindow(view.getContext());
            this.mImmersionListPopupWindow = immersionListPopupWindow2;
            immersionListPopupWindow2.setAdapter(immersionAdapter);
            this.mImmersionListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.xiaomi.mirror.MirrorManagerImpl.2
                @Override // android.widget.AdapterView.OnItemClickListener
                public void onItemClick(AdapterView adapterView, View view2, int i2, long j) {
                    MirrorManagerImpl.this.mImmersionListPopupWindow.dismiss();
                    MirrorMenu mirrorMenu2 = (MirrorMenu) arrayList.get(i2);
                    if (mirrorMenu2.needCallRemote()) {
                        if (mirrorMenu2.getUri() != null) {
                            view.getContext().grantUriPermission("com.xiaomi.mirror", mirrorMenu2.getUri(), 3);
                        }
                        try {
                            View view3 = view;
                            MirrorManagerImpl.this.mMirrorManager.onRemoteMenuActionCall(mirrorMenu2, (view3 == null || view3.getDisplay() == null) ? 0 : view.getDisplay().getDisplayId());
                            return;
                        } catch (Throwable th) {
                            Log.e(MirrorManagerImpl.TAG, "onRemoteMenuActionCall", th);
                            return;
                        }
                    }
                    if (mirrorMenu2.getListener() != null) {
                        mirrorMenu2.getListener().onClick(view, mirrorMenu2);
                    }
                    if (mirrorMenu2.getPendingIntent() != null) {
                        try {
                            mirrorMenu2.getPendingIntent().send(null, 0, null, null, null, null, ActivityOptions.makeBasic().setPendingIntentBackgroundActivityStartMode(1).toBundle());
                        } catch (PendingIntent.CanceledException e) {
                            Log.e(MirrorManagerImpl.TAG, "", e);
                        }
                    }
                }
            });
            this.mImmersionListPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.xiaomi.mirror.MirrorManagerImpl.3
                @Override // android.widget.PopupWindow.OnDismissListener
                public void onDismiss() {
                    OnMirrorMenuQueryListener onMirrorMenuQueryListener = MirrorManagerImpl.getOnMirrorMenuQueryListener(view);
                    if (onMirrorMenuQueryListener != null) {
                        onMirrorMenuQueryListener.onMirrorMenuShow(view, false);
                    }
                }
            });
            this.mImmersionListPopupWindow.show(view, null);
            OnMirrorMenuQueryListener onMirrorMenuQueryListener = getOnMirrorMenuQueryListener(view);
            if (onMirrorMenuQueryListener != null) {
                onMirrorMenuQueryListener.onMirrorMenuShow(view, true);
            }
        }
    }

    public boolean isCurrentClickFromMirror() {
        IMirrorManager iMirrorManager = this.mMirrorManager;
        if (iMirrorManager == null) {
            return false;
        }
        try {
            return iMirrorManager.isCurrentClickFromMirror();
        } catch (Throwable th) {
            Log.e(TAG, "isCurrentClickFromMirror", th);
            return false;
        }
    }

    public boolean isEventFromMirror(InputEvent inputEvent) {
        IMirrorManager iMirrorManager = this.mMirrorManager;
        if (iMirrorManager == null) {
            return false;
        }
        try {
            return iMirrorManager.isEventFromMirror(inputEvent);
        } catch (Throwable th) {
            Log.e(TAG, "isEventFromMirror", th);
            return false;
        }
    }

    public boolean isSupport() {
        IMirrorManager iMirrorManager = this.mMirrorManager;
        if (iMirrorManager == null) {
            return false;
        }
        try {
            return iMirrorManager.isModelSupport();
        } catch (Throwable th) {
            Log.e(TAG, "isSupport", th);
            return false;
        }
    }

    public boolean isWorking() {
        IMirrorManager iMirrorManager = this.mMirrorManager;
        if (iMirrorManager == null) {
            return false;
        }
        try {
            return iMirrorManager.isWorking();
        } catch (Throwable th) {
            Log.e(TAG, "isWorking", th);
            return false;
        }
    }

    public void notifyStartActivity(Intent intent) {
        IMirrorManager iMirrorManager = this.mMirrorManager;
        if (iMirrorManager == null) {
            return;
        }
        try {
            iMirrorManager.notifyStartActivity(intent);
        } catch (Throwable th) {
            Log.e(TAG, "notifyStartActivity", th);
        }
    }

    public void notifyStartActivityFromRecents(int i, Bundle bundle) {
        IMirrorManager iMirrorManager = this.mMirrorManager;
        if (iMirrorManager == null) {
            return;
        }
        try {
            iMirrorManager.notifyStartActivityFromRecents(i, bundle);
        } catch (Throwable th) {
            Log.e(TAG, "notifyStartActivityFromRecents", th);
        }
    }

    public void setOnMirrorMenuQueryListener(View view, OnMirrorMenuQueryListener onMirrorMenuQueryListener) {
        if (view == null) {
            Log.e(TAG, "setOnMirrorMenuQueryListener view is null");
            return;
        }
        if (onMirrorMenuQueryListener != null) {
            Object tag = view.getTag(R.id.tag_mirror_menu_query_listener);
            if (tag == null || (tag instanceof OnMirrorMenuQueryListener)) {
                view.setTag(R.id.tag_mirror_menu_query_listener, onMirrorMenuQueryListener);
                return;
            }
            Log.e(TAG, "the tag is busy." + tag);
            return;
        }
        Object tag2 = view.getTag(R.id.tag_mirror_menu_query_listener);
        if (tag2 == null || (tag2 instanceof OnMirrorMenuQueryListener)) {
            view.setTag(R.id.tag_mirror_menu_query_listener, null);
            return;
        }
        Log.e(TAG, "[remove]the tag is busy." + tag2);
    }
}
