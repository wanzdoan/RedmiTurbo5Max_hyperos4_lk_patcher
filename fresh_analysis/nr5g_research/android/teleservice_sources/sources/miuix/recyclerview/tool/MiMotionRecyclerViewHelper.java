package miuix.recyclerview.tool;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import miuix.core.util.SystemProperties;
import miuix.mimotion.MiMotionCloudConfig;
import miuix.mimotion.MiMotionHelper;
import miuix.recyclerview.widget.RecyclerView;

/* JADX INFO: loaded from: classes2.dex */
public class MiMotionRecyclerViewHelper {
    private static final boolean DEBUG = Boolean.parseBoolean(SystemProperties.get("persist.mimotion.debug", "false"));
    private Context mContext;
    private int mCurrentRefreshRate;
    private float mDensity;
    private Handler mHandler;
    private String mPackageName;
    private int[] mRefreshRateList = null;
    private int[] mRefreshRateSpeedLimits = null;
    private int[] mRefreshRateSpeedLimitsDp = null;
    private int[] mTouchRefreshRateList = null;
    private int[] mTouchRefreshRateSpeedLimits = null;
    private volatile boolean mIsTouch = false;
    private boolean mNeedAbandon = false;
    private boolean mHasFocus = false;
    private int mOldScrollState = 0;

    public boolean initMiMotion(RecyclerView recyclerView) {
        this.mPackageName = recyclerView.getContext().getPackageName();
        this.mContext = recyclerView.getContext();
        if (!MiMotionHelper.getInstance().isEnabled()) {
            return false;
        }
        this.mRefreshRateList = new int[]{120, 60, 40, 30, 24, 0};
        int[] refreshRateSpeedLimitsDp = MiMotionCloudConfig.getInstance().getRefreshRateSpeedLimitsDp();
        this.mRefreshRateSpeedLimitsDp = refreshRateSpeedLimitsDp;
        if (refreshRateSpeedLimitsDp == null) {
            this.mRefreshRateSpeedLimitsDp = new int[]{135, 35, 15, 5, 1, 0};
        }
        adjustRefreshRateSpeedLimits(this.mContext.getResources().getDisplayMetrics().density);
        if (DEBUG) {
            Log.d("MiMotionHelper", "===========RefreshRateSpeedLimits===========");
            for (int i = 0; i < this.mRefreshRateSpeedLimits.length; i++) {
                Log.d("MiMotionHelper", "RefreshRateSpeedLimits[" + i + "] = " + this.mRefreshRateSpeedLimits[i]);
            }
            Log.d("MiMotionHelper", "===========RefreshRateSpeedLimits===========");
        }
        this.mTouchRefreshRateList = new int[]{120, 60, 40, 30, 24};
        this.mTouchRefreshRateSpeedLimits = new int[]{480, 95, 48, 10, 0};
        Handler handler = recyclerView.getHandler();
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        this.mHandler = handler;
        return true;
    }

    public void calculateSpeed(int i, int i2, int i3, int i4) {
        final int iCalculateRefreshRate;
        if (DEBUG) {
            Log.d("MiMotionHelper", "calculateSpeed---> velocityX:" + i + " velocityY:" + i2 + " isTouch:" + this.mIsTouch);
        }
        if (!this.mIsTouch) {
            iCalculateRefreshRate = calculateRefreshRate(Math.max(Math.abs(i), Math.abs(i2)));
        } else {
            iCalculateRefreshRate = this.mRefreshRateList[0];
        }
        this.mHandler.post(new Runnable() { // from class: miuix.recyclerview.tool.MiMotionRecyclerViewHelper$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                MiMotionRecyclerViewHelper.$r8$lambda$yfKZMvFBjhnT1b0MjiGk7spt9CA(this.f$0, iCalculateRefreshRate);
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$yfKZMvFBjhnT1b0MjiGk7spt9CA(MiMotionRecyclerViewHelper miMotionRecyclerViewHelper, int i) {
        miMotionRecyclerViewHelper.getClass();
        MiMotionHelper.getInstance().setPreferredRefreshRate(miMotionRecyclerViewHelper, i);
    }

    public void touchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0) {
            this.mIsTouch = true;
            int i = this.mCurrentRefreshRate;
            int i2 = this.mRefreshRateList[0];
            if (i != i2) {
                this.mCurrentRefreshRate = i2;
                MiMotionHelper.getInstance().setPreferredRefreshRate(this, this.mRefreshRateList[0]);
            }
            this.mHasFocus = true;
            this.mNeedAbandon = false;
            adjustRefreshRateSpeedLimits(this.mContext.getResources().getDisplayMetrics().density);
            return;
        }
        if (motionEvent.getActionMasked() == 1) {
            this.mHandler.postDelayed(new Runnable() { // from class: miuix.recyclerview.tool.MiMotionRecyclerViewHelper$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.mIsTouch = false;
                }
            }, 800L);
        }
    }

    public int calculateRefreshRate(int i) {
        int[] iArr = this.mRefreshRateList;
        int i2 = iArr[iArr.length - 1];
        if (!this.mHasFocus || this.mNeedAbandon) {
            return this.mCurrentRefreshRate;
        }
        if (i == 0) {
            return i2;
        }
        int i3 = 0;
        while (true) {
            int[] iArr2 = this.mRefreshRateSpeedLimits;
            if (i3 >= iArr2.length) {
                break;
            }
            if (i > iArr2[i3]) {
                i2 = this.mRefreshRateList[i3];
                break;
            }
            i3++;
        }
        int i4 = this.mCurrentRefreshRate;
        if (i2 >= i4) {
            int[] iArr3 = this.mRefreshRateList;
            if (i4 != iArr3[iArr3.length - 1] || i2 != iArr3[0]) {
                return i2;
            }
        }
        this.mCurrentRefreshRate = i2;
        return i2;
    }

    public void scrollState(RecyclerView recyclerView, int i) {
        if (this.mNeedAbandon || this.mIsTouch || this.mOldScrollState != 2) {
            this.mOldScrollState = i;
        } else {
            this.mOldScrollState = i;
        }
    }

    public void onFocusChange(boolean z) {
        this.mHasFocus = z;
        this.mNeedAbandon = true;
        MiMotionHelper.getInstance().setPreferredRefreshRate(this, this.mRefreshRateList[0]);
    }

    private void adjustRefreshRateSpeedLimits(float f) {
        if (this.mRefreshRateSpeedLimits == null) {
            this.mRefreshRateSpeedLimits = new int[this.mRefreshRateSpeedLimitsDp.length];
        }
        if (f == this.mDensity) {
            return;
        }
        this.mDensity = f;
        int i = 0;
        while (true) {
            int[] iArr = this.mRefreshRateSpeedLimits;
            if (i >= iArr.length) {
                return;
            }
            iArr[i] = (int) (this.mRefreshRateSpeedLimitsDp[i] * f);
            i++;
        }
    }
}
