package miuix.appcompat.app;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface ShortcutsCallback {
    static boolean dispatchKeyEvent(FragmentManager fragmentManager, KeyEvent keyEvent) {
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment.isAdded() && !fragment.isHidden()) {
                fragment.isResumed();
            }
        }
        return false;
    }

    static boolean dispatchKeyShortcutEvent(FragmentManager fragmentManager, KeyEvent keyEvent) {
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment.isAdded() && !fragment.isHidden()) {
                fragment.isResumed();
            }
        }
        return false;
    }

    static boolean dispatchTouchEvent(FragmentManager fragmentManager, MotionEvent motionEvent) {
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment.isAdded() && !fragment.isHidden()) {
                fragment.isResumed();
            }
        }
        return false;
    }

    static boolean dispatchTrackballEvent(FragmentManager fragmentManager, MotionEvent motionEvent) {
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment.isAdded() && !fragment.isHidden()) {
                fragment.isResumed();
            }
        }
        return false;
    }

    static boolean dispatchGenericMotionEvent(FragmentManager fragmentManager, MotionEvent motionEvent) {
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment.isAdded() && !fragment.isHidden()) {
                fragment.isResumed();
            }
        }
        return false;
    }

    static void dispatchProvideKeyboardShortcuts(FragmentManager fragmentManager, List list, Menu menu, int i) {
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment.isAdded() && !fragment.isHidden()) {
                fragment.isResumed();
            }
        }
    }

    static boolean dispatchKeyDown(FragmentManager fragmentManager, int i, KeyEvent keyEvent) {
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment.isAdded() && !fragment.isHidden()) {
                fragment.isResumed();
            }
        }
        return false;
    }

    static boolean dispatchKeyLongPress(FragmentManager fragmentManager, int i, KeyEvent keyEvent) {
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment.isAdded() && !fragment.isHidden()) {
                fragment.isResumed();
            }
        }
        return false;
    }

    static boolean dispatchKeyUp(FragmentManager fragmentManager, int i, KeyEvent keyEvent) {
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment.isAdded() && !fragment.isHidden()) {
                fragment.isResumed();
            }
        }
        return false;
    }

    static boolean dispatchKeyMultiple(FragmentManager fragmentManager, int i, int i2, KeyEvent keyEvent) {
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment.isAdded() && !fragment.isHidden()) {
                fragment.isResumed();
            }
        }
        return false;
    }
}
