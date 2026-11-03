package androidx.core.view;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.View;
import android.view.WindowInsets;
import androidx.core.graphics.Insets;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Preconditions;
import com.nxp.id.tp.common.HexString$$ExternalSyntheticBUOutline1;
import com.xiaomi.internal.telephony.DynamicSarService;
import java.util.ArrayList;
import java.util.Objects;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public class WindowInsetsCompat {
    public static final WindowInsetsCompat CONSUMED = Impl34.CONSUMED;
    private final Impl mImpl;

    private WindowInsetsCompat(WindowInsets windowInsets) {
        this.mImpl = new Impl35(this, windowInsets);
    }

    public WindowInsetsCompat(WindowInsetsCompat windowInsetsCompat) {
        if (windowInsetsCompat != null) {
            Impl impl = windowInsetsCompat.mImpl;
            if (impl instanceof Impl35) {
                this.mImpl = new Impl35(this, (Impl35) impl);
            } else if (impl instanceof Impl34) {
                this.mImpl = new Impl34(this, (Impl34) impl);
            } else if (impl instanceof Impl31) {
                this.mImpl = new Impl31(this, (Impl31) impl);
            } else if (impl instanceof Impl30) {
                this.mImpl = new Impl30(this, (Impl30) impl);
            } else if (impl instanceof Impl29) {
                this.mImpl = new Impl29(this, (Impl29) impl);
            } else if (impl instanceof Impl28) {
                this.mImpl = new Impl28(this, (Impl28) impl);
            } else if (impl instanceof Impl21) {
                this.mImpl = new Impl21(this, (Impl21) impl);
            } else if (impl instanceof Impl20) {
                this.mImpl = new Impl20(this, (Impl20) impl);
            } else {
                this.mImpl = new Impl(this);
            }
            impl.copyWindowDataInto(this);
            return;
        }
        this.mImpl = new Impl(this);
    }

    public static WindowInsetsCompat toWindowInsetsCompat(WindowInsets windowInsets) {
        return toWindowInsetsCompat(windowInsets, null);
    }

    public static WindowInsetsCompat toWindowInsetsCompat(WindowInsets windowInsets, View view) {
        WindowInsetsCompat windowInsetsCompat = new WindowInsetsCompat((WindowInsets) Preconditions.checkNotNull(windowInsets));
        if (view != null && view.isAttachedToWindow()) {
            windowInsetsCompat.setRootWindowInsets(ViewCompat.getRootWindowInsets(view));
            windowInsetsCompat.init(view.getRootView());
            windowInsetsCompat.setSystemUiVisibility(view.getWindowSystemUiVisibility());
        }
        return windowInsetsCompat;
    }

    public int getSystemWindowInsetLeft() {
        return this.mImpl.getSystemWindowInsets().left;
    }

    public int getSystemWindowInsetTop() {
        return this.mImpl.getSystemWindowInsets().top;
    }

    public int getSystemWindowInsetRight() {
        return this.mImpl.getSystemWindowInsets().right;
    }

    public int getSystemWindowInsetBottom() {
        return this.mImpl.getSystemWindowInsets().bottom;
    }

    public boolean isConsumed() {
        return this.mImpl.isConsumed();
    }

    public boolean isRound() {
        return this.mImpl.isRound();
    }

    public WindowInsetsCompat consumeSystemWindowInsets() {
        return this.mImpl.consumeSystemWindowInsets();
    }

    public WindowInsetsCompat replaceSystemWindowInsets(int i, int i2, int i3, int i4) {
        return new Builder(this).setSystemWindowInsets(Insets.of(i, i2, i3, i4)).build();
    }

    public WindowInsetsCompat consumeStableInsets() {
        return this.mImpl.consumeStableInsets();
    }

    public DisplayCutoutCompat getDisplayCutout() {
        return this.mImpl.getDisplayCutout();
    }

    public WindowInsetsCompat consumeDisplayCutout() {
        return this.mImpl.consumeDisplayCutout();
    }

    public Insets getSystemWindowInsets() {
        return this.mImpl.getSystemWindowInsets();
    }

    public Insets getStableInsets() {
        return this.mImpl.getStableInsets();
    }

    public WindowInsetsCompat inset(int i, int i2, int i3, int i4) {
        return this.mImpl.inset(i, i2, i3, i4);
    }

    public Insets getInsets(int i) {
        return this.mImpl.getInsets(i);
    }

    public Insets getInsetsIgnoringVisibility(int i) {
        return this.mImpl.getInsetsIgnoringVisibility(i);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof WindowInsetsCompat) {
            return ObjectsCompat.equals(this.mImpl, ((WindowInsetsCompat) obj).mImpl);
        }
        return false;
    }

    public int hashCode() {
        Impl impl = this.mImpl;
        if (impl == null) {
            return 0;
        }
        return impl.hashCode();
    }

    public WindowInsets toWindowInsets() {
        Impl impl = this.mImpl;
        if (impl instanceof Impl20) {
            return ((Impl20) impl).mPlatformInsets;
        }
        return null;
    }

    class Impl {
        static final WindowInsetsCompat CONSUMED = new Builder().build().consumeDisplayCutout().consumeStableInsets().consumeSystemWindowInsets();
        final WindowInsetsCompat mHost;

        void copyRootViewBounds(View view) {
        }

        void copyWindowDataInto(WindowInsetsCompat windowInsetsCompat) {
        }

        DisplayCutoutCompat getDisplayCutout() {
            return null;
        }

        void initDisplayShape(View view) {
        }

        void initTypeBoundingRectsMaps() {
        }

        boolean isConsumed() {
            return false;
        }

        boolean isRound() {
            return false;
        }

        public void setDisplayShape(DisplayShapeCompat displayShapeCompat) {
        }

        public void setOverriddenInsets(Insets[] insetsArr) {
        }

        void setRootViewData(Insets insets) {
        }

        void setRootWindowInsets(WindowInsetsCompat windowInsetsCompat) {
        }

        void setSystemUiVisibility(int i) {
        }

        void setTypeBoundingRectsMap(Rect[][] rectArr) {
        }

        void setTypeMaxBoundingRectsMap(Rect[][] rectArr) {
        }

        Impl(WindowInsetsCompat windowInsetsCompat) {
            this.mHost = windowInsetsCompat;
        }

        WindowInsetsCompat consumeSystemWindowInsets() {
            return this.mHost;
        }

        WindowInsetsCompat consumeStableInsets() {
            return this.mHost;
        }

        WindowInsetsCompat consumeDisplayCutout() {
            return this.mHost;
        }

        Insets getSystemWindowInsets() {
            return Insets.NONE;
        }

        Insets getStableInsets() {
            return Insets.NONE;
        }

        Insets getSystemGestureInsets() {
            return getSystemWindowInsets();
        }

        Insets getMandatorySystemGestureInsets() {
            return getSystemWindowInsets();
        }

        Insets getTappableElementInsets() {
            return getSystemWindowInsets();
        }

        WindowInsetsCompat inset(int i, int i2, int i3, int i4) {
            return CONSUMED;
        }

        Insets getInsets(int i) {
            return Insets.NONE;
        }

        Insets getInsetsIgnoringVisibility(int i) {
            if ((i & 8) != 0) {
                Folme$$ExternalSyntheticBUOutline0.m("Unable to query the maximum insets for IME");
                return null;
            }
            return Insets.NONE;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Impl)) {
                return false;
            }
            Impl impl = (Impl) obj;
            return isRound() == impl.isRound() && isConsumed() == impl.isConsumed() && ObjectsCompat.equals(getSystemWindowInsets(), impl.getSystemWindowInsets()) && ObjectsCompat.equals(getStableInsets(), impl.getStableInsets()) && ObjectsCompat.equals(getDisplayCutout(), impl.getDisplayCutout());
        }

        public int hashCode() {
            return ObjectsCompat.hash(Boolean.valueOf(isRound()), Boolean.valueOf(isConsumed()), getSystemWindowInsets(), getStableInsets(), getDisplayCutout());
        }
    }

    class Impl20 extends Impl {
        DisplayShapeCompat mDisplayShapeCompat;
        private Insets[] mOverriddenInsets;
        final WindowInsets mPlatformInsets;
        int mRootViewHeight;
        Insets mRootViewVisibleInsets;
        int mRootViewWidth;
        private WindowInsetsCompat mRootWindowInsets;
        int mSystemUiVisibility;
        private Insets mSystemWindowInsets;
        private Rect[][] mTypeBoundingRectsMap;
        private Rect[][] mTypeMaxBoundingRectsMap;

        static boolean systemBarVisibilityEquals(int i, int i2) {
            return (i & 6) == (i2 & 6);
        }

        Impl20(WindowInsetsCompat windowInsetsCompat, WindowInsets windowInsets) {
            super(windowInsetsCompat);
            this.mSystemWindowInsets = null;
            this.mTypeBoundingRectsMap = new Rect[10][];
            this.mTypeMaxBoundingRectsMap = new Rect[10][];
            this.mPlatformInsets = windowInsets;
        }

        Impl20(WindowInsetsCompat windowInsetsCompat, Impl20 impl20) {
            this(windowInsetsCompat, new WindowInsets(impl20.mPlatformInsets));
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        boolean isRound() {
            return this.mPlatformInsets.isRound();
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public Insets getInsets(int i) {
            return getInsets(i, false);
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public Insets getInsetsIgnoringVisibility(int i) {
            return getInsets(i, true);
        }

        private Insets getInsets(int i, boolean z) {
            Insets insetsMax = Insets.NONE;
            for (int i2 = 1; i2 <= 512; i2 <<= 1) {
                if ((i & i2) != 0) {
                    insetsMax = Insets.max(insetsMax, getInsetsForType(i2, z));
                }
            }
            return insetsMax;
        }

        protected Insets getInsetsForType(int i, boolean z) {
            Insets stableInsets;
            int i2;
            DisplayCutoutCompat displayCutout;
            if (i == 1) {
                if (z) {
                    return Insets.of(0, Math.max(getRootStableInsets().top, getSystemWindowInsets().top), 0, 0);
                }
                if ((this.mSystemUiVisibility & 4) != 0) {
                    return Insets.NONE;
                }
                return Insets.of(0, getSystemWindowInsets().top, 0, 0);
            }
            if (i == 2) {
                if (z) {
                    Insets rootStableInsets = getRootStableInsets();
                    Insets stableInsets2 = getStableInsets();
                    return Insets.of(Math.max(rootStableInsets.left, stableInsets2.left), 0, Math.max(rootStableInsets.right, stableInsets2.right), Math.max(rootStableInsets.bottom, stableInsets2.bottom));
                }
                if ((this.mSystemUiVisibility & 2) != 0) {
                    return Insets.NONE;
                }
                Insets systemWindowInsets = getSystemWindowInsets();
                WindowInsetsCompat windowInsetsCompat = this.mRootWindowInsets;
                stableInsets = windowInsetsCompat != null ? windowInsetsCompat.getStableInsets() : null;
                int iMin = systemWindowInsets.bottom;
                if (stableInsets != null) {
                    iMin = Math.min(iMin, stableInsets.bottom);
                }
                return Insets.of(systemWindowInsets.left, 0, systemWindowInsets.right, iMin);
            }
            if (i != 8) {
                if (i == 16) {
                    return getSystemGestureInsets();
                }
                if (i == 32) {
                    return getMandatorySystemGestureInsets();
                }
                if (i == 64) {
                    return getTappableElementInsets();
                }
                if (i == 128) {
                    WindowInsetsCompat windowInsetsCompat2 = this.mRootWindowInsets;
                    if (windowInsetsCompat2 != null) {
                        displayCutout = windowInsetsCompat2.getDisplayCutout();
                    } else {
                        displayCutout = getDisplayCutout();
                    }
                    if (displayCutout != null) {
                        return Insets.of(displayCutout.getSafeInsetLeft(), displayCutout.getSafeInsetTop(), displayCutout.getSafeInsetRight(), displayCutout.getSafeInsetBottom());
                    }
                    return Insets.NONE;
                }
                return Insets.NONE;
            }
            Insets[] insetsArr = this.mOverriddenInsets;
            stableInsets = insetsArr != null ? insetsArr[Type.indexOf(8)] : null;
            if (stableInsets != null) {
                return stableInsets;
            }
            Insets systemWindowInsets2 = getSystemWindowInsets();
            Insets rootStableInsets2 = getRootStableInsets();
            int i3 = systemWindowInsets2.bottom;
            if (i3 > rootStableInsets2.bottom) {
                return Insets.of(0, 0, 0, i3);
            }
            Insets insets = this.mRootViewVisibleInsets;
            if (insets != null && !insets.equals(Insets.NONE) && (i2 = this.mRootViewVisibleInsets.bottom) > rootStableInsets2.bottom) {
                return Insets.of(0, 0, 0, i2);
            }
            return Insets.NONE;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        final Insets getSystemWindowInsets() {
            if (this.mSystemWindowInsets == null) {
                this.mSystemWindowInsets = Insets.of(this.mPlatformInsets.getSystemWindowInsetLeft(), this.mPlatformInsets.getSystemWindowInsetTop(), this.mPlatformInsets.getSystemWindowInsetRight(), this.mPlatformInsets.getSystemWindowInsetBottom());
            }
            return this.mSystemWindowInsets;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        WindowInsetsCompat inset(int i, int i2, int i3, int i4) {
            Builder builder = new Builder(WindowInsetsCompat.toWindowInsetsCompat(this.mPlatformInsets));
            builder.setSystemWindowInsets(WindowInsetsCompat.insetInsets(getSystemWindowInsets(), i, i2, i3, i4));
            builder.setStableInsets(WindowInsetsCompat.insetInsets(getStableInsets(), i, i2, i3, i4));
            return builder.build();
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        void copyWindowDataInto(WindowInsetsCompat windowInsetsCompat) {
            windowInsetsCompat.setRootWindowInsets(this.mRootWindowInsets);
            windowInsetsCompat.setRootViewData(this.mRootViewVisibleInsets);
            windowInsetsCompat.setSystemUiVisibility(this.mSystemUiVisibility);
            windowInsetsCompat.setDisplayShape(this.mDisplayShapeCompat);
            windowInsetsCompat.setTypeBoundingRectsMap(this.mTypeBoundingRectsMap);
            windowInsetsCompat.setTypeMaxBoundingRectsMap(this.mTypeMaxBoundingRectsMap);
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        void setRootWindowInsets(WindowInsetsCompat windowInsetsCompat) {
            this.mRootWindowInsets = windowInsetsCompat;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        void setRootViewData(Insets insets) {
            this.mRootViewVisibleInsets = insets;
        }

        private Insets getRootStableInsets() {
            WindowInsetsCompat windowInsetsCompat = this.mRootWindowInsets;
            if (windowInsetsCompat != null) {
                return windowInsetsCompat.getStableInsets();
            }
            return Insets.NONE;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        void copyRootViewBounds(View view) {
            this.mRootViewWidth = view.getWidth();
            this.mRootViewHeight = view.getHeight();
            Insets visibleInsets = getVisibleInsets(view);
            if (visibleInsets == null) {
                visibleInsets = Insets.NONE;
            }
            setRootViewData(visibleInsets);
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        void initDisplayShape(View view) {
            this.mDisplayShapeCompat = createDisplayShape(view);
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        void initTypeBoundingRectsMaps() {
            for (int i = 1; i <= 512; i <<= 1) {
                int iIndexOf = Type.indexOf(i);
                this.mTypeBoundingRectsMap[iIndexOf] = getBoundingRectsFromInsets(getInsets(i));
                if (i != 8) {
                    this.mTypeMaxBoundingRectsMap[iIndexOf] = getBoundingRectsFromInsets(getInsetsIgnoringVisibility(i));
                }
            }
        }

        private Rect[] getBoundingRectsFromInsets(Insets insets) {
            ArrayList arrayList = new ArrayList();
            if (insets.left != 0) {
                arrayList.add(new Rect(0, 0, insets.left, this.mRootViewHeight));
            }
            if (insets.top != 0) {
                arrayList.add(new Rect(0, 0, this.mRootViewWidth, insets.top));
            }
            if (insets.right != 0) {
                int i = this.mRootViewWidth;
                arrayList.add(new Rect(i - insets.right, 0, i, this.mRootViewHeight));
            }
            if (insets.bottom != 0) {
                int i2 = this.mRootViewHeight;
                arrayList.add(new Rect(0, i2 - insets.bottom, this.mRootViewWidth, i2));
            }
            return (Rect[]) arrayList.toArray(new Rect[arrayList.size()]);
        }

        private DisplayShapeCompat createDisplayShape(View view) {
            Display display;
            if (view == null || (display = view.getDisplay()) == null) {
                return null;
            }
            Point point = new Point();
            display.getRealSize(point);
            if (this.mHost.isRound()) {
                return DisplayShapeCompat.create(point.x, point.y, true, 0, 0, 0, 0);
            }
            RoundedCornerCompat roundedCorner = DisplayCompat.getRoundedCorner(display, 0);
            RoundedCornerCompat roundedCorner2 = DisplayCompat.getRoundedCorner(display, 1);
            RoundedCornerCompat roundedCorner3 = DisplayCompat.getRoundedCorner(display, 2);
            RoundedCornerCompat roundedCorner4 = DisplayCompat.getRoundedCorner(display, 3);
            return DisplayShapeCompat.create(point.x, point.y, false, roundedCorner != null ? roundedCorner.getRadius() : 0, roundedCorner2 != null ? roundedCorner2.getRadius() : 0, roundedCorner3 != null ? roundedCorner3.getRadius() : 0, roundedCorner4 != null ? roundedCorner4.getRadius() : 0);
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public void setDisplayShape(DisplayShapeCompat displayShapeCompat) {
            this.mDisplayShapeCompat = displayShapeCompat;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        void setTypeBoundingRectsMap(Rect[][] rectArr) {
            Objects.requireNonNull(rectArr);
            this.mTypeBoundingRectsMap = (Rect[][]) rectArr.clone();
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        void setTypeMaxBoundingRectsMap(Rect[][] rectArr) {
            Objects.requireNonNull(rectArr);
            this.mTypeMaxBoundingRectsMap = (Rect[][]) rectArr.clone();
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        void setSystemUiVisibility(int i) {
            this.mSystemUiVisibility = i;
        }

        private Insets getVisibleInsets(View view) {
            throw new UnsupportedOperationException("getVisibleInsets() should not be called on API >= 30. Use WindowInsets.isVisible() instead.");
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public void setOverriddenInsets(Insets[] insetsArr) {
            this.mOverriddenInsets = insetsArr;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public boolean equals(Object obj) {
            if (!super.equals(obj)) {
                return false;
            }
            Impl20 impl20 = (Impl20) obj;
            return Objects.equals(this.mRootViewVisibleInsets, impl20.mRootViewVisibleInsets) && systemBarVisibilityEquals(this.mSystemUiVisibility, impl20.mSystemUiVisibility);
        }
    }

    class Impl21 extends Impl20 {
        private Insets mStableInsets;

        Impl21(WindowInsetsCompat windowInsetsCompat, WindowInsets windowInsets) {
            super(windowInsetsCompat, windowInsets);
            this.mStableInsets = null;
        }

        Impl21(WindowInsetsCompat windowInsetsCompat, Impl21 impl21) {
            super(windowInsetsCompat, impl21);
            this.mStableInsets = null;
            this.mStableInsets = impl21.mStableInsets;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        boolean isConsumed() {
            return this.mPlatformInsets.isConsumed();
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        WindowInsetsCompat consumeStableInsets() {
            return WindowInsetsCompat.toWindowInsetsCompat(this.mPlatformInsets.consumeStableInsets());
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        WindowInsetsCompat consumeSystemWindowInsets() {
            return WindowInsetsCompat.toWindowInsetsCompat(this.mPlatformInsets.consumeSystemWindowInsets());
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        final Insets getStableInsets() {
            if (this.mStableInsets == null) {
                this.mStableInsets = Insets.of(this.mPlatformInsets.getStableInsetLeft(), this.mPlatformInsets.getStableInsetTop(), this.mPlatformInsets.getStableInsetRight(), this.mPlatformInsets.getStableInsetBottom());
            }
            return this.mStableInsets;
        }
    }

    class Impl28 extends Impl21 {
        Impl28(WindowInsetsCompat windowInsetsCompat, WindowInsets windowInsets) {
            super(windowInsetsCompat, windowInsets);
        }

        Impl28(WindowInsetsCompat windowInsetsCompat, Impl28 impl28) {
            super(windowInsetsCompat, impl28);
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        DisplayCutoutCompat getDisplayCutout() {
            return DisplayCutoutCompat.wrap(this.mPlatformInsets.getDisplayCutout());
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        WindowInsetsCompat consumeDisplayCutout() {
            return WindowInsetsCompat.toWindowInsetsCompat(this.mPlatformInsets.consumeDisplayCutout());
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl20, androidx.core.view.WindowInsetsCompat.Impl
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Impl28)) {
                return false;
            }
            Impl28 impl28 = (Impl28) obj;
            return Objects.equals(this.mPlatformInsets, impl28.mPlatformInsets) && Objects.equals(this.mRootViewVisibleInsets, impl28.mRootViewVisibleInsets) && Impl20.systemBarVisibilityEquals(this.mSystemUiVisibility, impl28.mSystemUiVisibility);
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public int hashCode() {
            return this.mPlatformInsets.hashCode();
        }
    }

    class Impl29 extends Impl28 {
        private Insets mMandatorySystemGestureInsets;
        private Insets mSystemGestureInsets;
        private Insets mTappableElementInsets;

        Impl29(WindowInsetsCompat windowInsetsCompat, WindowInsets windowInsets) {
            super(windowInsetsCompat, windowInsets);
            this.mSystemGestureInsets = null;
            this.mMandatorySystemGestureInsets = null;
            this.mTappableElementInsets = null;
        }

        Impl29(WindowInsetsCompat windowInsetsCompat, Impl29 impl29) {
            super(windowInsetsCompat, impl29);
            this.mSystemGestureInsets = null;
            this.mMandatorySystemGestureInsets = null;
            this.mTappableElementInsets = null;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        Insets getSystemGestureInsets() {
            if (this.mSystemGestureInsets == null) {
                this.mSystemGestureInsets = Insets.toCompatInsets(this.mPlatformInsets.getSystemGestureInsets());
            }
            return this.mSystemGestureInsets;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        Insets getMandatorySystemGestureInsets() {
            if (this.mMandatorySystemGestureInsets == null) {
                this.mMandatorySystemGestureInsets = Insets.toCompatInsets(this.mPlatformInsets.getMandatorySystemGestureInsets());
            }
            return this.mMandatorySystemGestureInsets;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        Insets getTappableElementInsets() {
            if (this.mTappableElementInsets == null) {
                this.mTappableElementInsets = Insets.toCompatInsets(this.mPlatformInsets.getTappableElementInsets());
            }
            return this.mTappableElementInsets;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl20, androidx.core.view.WindowInsetsCompat.Impl
        WindowInsetsCompat inset(int i, int i2, int i3, int i4) {
            return WindowInsetsCompat.toWindowInsetsCompat(this.mPlatformInsets.inset(i, i2, i3, i4));
        }
    }

    static Insets insetInsets(Insets insets, int i, int i2, int i3, int i4) {
        int iMax = Math.max(0, insets.left - i);
        int iMax2 = Math.max(0, insets.top - i2);
        int iMax3 = Math.max(0, insets.right - i3);
        int iMax4 = Math.max(0, insets.bottom - i4);
        return (iMax == i && iMax2 == i2 && iMax3 == i3 && iMax4 == i4) ? insets : Insets.of(iMax, iMax2, iMax3, iMax4);
    }

    class Impl30 extends Impl29 {
        static final WindowInsetsCompat CONSUMED = WindowInsetsCompat.toWindowInsetsCompat(WindowInsets.CONSUMED);

        @Override // androidx.core.view.WindowInsetsCompat.Impl20, androidx.core.view.WindowInsetsCompat.Impl
        final void copyRootViewBounds(View view) {
        }

        Impl30(WindowInsetsCompat windowInsetsCompat, WindowInsets windowInsets) {
            super(windowInsetsCompat, windowInsets);
        }

        Impl30(WindowInsetsCompat windowInsetsCompat, Impl30 impl30) {
            super(windowInsetsCompat, impl30);
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl20, androidx.core.view.WindowInsetsCompat.Impl
        public Insets getInsets(int i) {
            return Insets.toCompatInsets(this.mPlatformInsets.getInsets(TypeImpl30.toPlatformType(i)));
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl20, androidx.core.view.WindowInsetsCompat.Impl
        public Insets getInsetsIgnoringVisibility(int i) {
            return Insets.toCompatInsets(this.mPlatformInsets.getInsetsIgnoringVisibility(TypeImpl30.toPlatformType(i)));
        }
    }

    class Impl31 extends Impl30 {
        Impl31(WindowInsetsCompat windowInsetsCompat, WindowInsets windowInsets) {
            super(windowInsetsCompat, windowInsets);
        }

        Impl31(WindowInsetsCompat windowInsetsCompat, Impl31 impl31) {
            super(windowInsetsCompat, impl31);
        }
    }

    class Impl34 extends Impl31 {
        static final WindowInsetsCompat CONSUMED = WindowInsetsCompat.toWindowInsetsCompat(WindowInsets.CONSUMED);

        @Override // androidx.core.view.WindowInsetsCompat.Impl20, androidx.core.view.WindowInsetsCompat.Impl
        void initDisplayShape(View view) {
        }

        Impl34(WindowInsetsCompat windowInsetsCompat, WindowInsets windowInsets) {
            super(windowInsetsCompat, windowInsets);
        }

        Impl34(WindowInsetsCompat windowInsetsCompat, Impl34 impl34) {
            super(windowInsetsCompat, impl34);
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl30, androidx.core.view.WindowInsetsCompat.Impl20, androidx.core.view.WindowInsetsCompat.Impl
        public Insets getInsets(int i) {
            return Insets.toCompatInsets(this.mPlatformInsets.getInsets(TypeImpl34.toPlatformType(i)));
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl30, androidx.core.view.WindowInsetsCompat.Impl20, androidx.core.view.WindowInsetsCompat.Impl
        public Insets getInsetsIgnoringVisibility(int i) {
            return Insets.toCompatInsets(this.mPlatformInsets.getInsetsIgnoringVisibility(TypeImpl34.toPlatformType(i)));
        }
    }

    class Impl35 extends Impl34 {
        @Override // androidx.core.view.WindowInsetsCompat.Impl20, androidx.core.view.WindowInsetsCompat.Impl
        void initTypeBoundingRectsMaps() {
        }

        Impl35(WindowInsetsCompat windowInsetsCompat, WindowInsets windowInsets) {
            super(windowInsetsCompat, windowInsets);
        }

        Impl35(WindowInsetsCompat windowInsetsCompat, Impl35 impl35) {
            super(windowInsetsCompat, impl35);
        }
    }

    public final class Builder {
        private final BuilderImpl mImpl;

        public Builder() {
            this.mImpl = new BuilderImpl36();
        }

        public Builder(WindowInsetsCompat windowInsetsCompat) {
            this.mImpl = new BuilderImpl36(windowInsetsCompat);
        }

        public Builder setSystemWindowInsets(Insets insets) {
            this.mImpl.setSystemWindowInsets(insets);
            return this;
        }

        public Builder setStableInsets(Insets insets) {
            this.mImpl.setStableInsets(insets);
            return this;
        }

        public WindowInsetsCompat build() {
            return this.mImpl.build();
        }
    }

    abstract class BuilderImpl {
        DisplayShapeCompat mDisplayShapeCompat;
        private final WindowInsetsCompat mInsets;
        Insets[] mInsetsTypeMask;
        Rect[][] mTypeBoundingRectsMap;
        Rect[][] mTypeMaxBoundingRectsMap;

        abstract WindowInsetsCompat build();

        abstract void initTypeBoundingRects(WindowInsetsCompat windowInsetsCompat);

        abstract void setMandatorySystemGestureInsets(Insets insets);

        abstract void setStableInsets(Insets insets);

        abstract void setSystemGestureInsets(Insets insets);

        abstract void setSystemWindowInsets(Insets insets);

        abstract void setTappableElementInsets(Insets insets);

        BuilderImpl() {
            this(new WindowInsetsCompat((WindowInsetsCompat) null));
        }

        BuilderImpl(WindowInsetsCompat windowInsetsCompat) {
            this.mTypeBoundingRectsMap = new Rect[10][];
            this.mTypeMaxBoundingRectsMap = new Rect[10][];
            this.mInsets = windowInsetsCompat;
            initTypeBoundingRects(windowInsetsCompat);
        }

        protected final void applyInsetTypes() {
            Insets[] insetsArr = this.mInsetsTypeMask;
            if (insetsArr != null) {
                Insets insets = insetsArr[Type.indexOf(1)];
                Insets insets2 = this.mInsetsTypeMask[Type.indexOf(2)];
                if (insets2 == null) {
                    insets2 = this.mInsets.getInsets(2);
                }
                if (insets == null) {
                    insets = this.mInsets.getInsets(1);
                }
                setSystemWindowInsets(Insets.max(insets, insets2));
                Insets insets3 = this.mInsetsTypeMask[Type.indexOf(16)];
                if (insets3 != null) {
                    setSystemGestureInsets(insets3);
                }
                Insets insets4 = this.mInsetsTypeMask[Type.indexOf(32)];
                if (insets4 != null) {
                    setMandatorySystemGestureInsets(insets4);
                }
                Insets insets5 = this.mInsetsTypeMask[Type.indexOf(64)];
                if (insets5 != null) {
                    setTappableElementInsets(insets5);
                }
            }
        }
    }

    void setOverriddenInsets(Insets[] insetsArr) {
        this.mImpl.setOverriddenInsets(insetsArr);
    }

    void setDisplayShape(DisplayShapeCompat displayShapeCompat) {
        this.mImpl.setDisplayShape(displayShapeCompat);
    }

    void setTypeBoundingRectsMap(Rect[][] rectArr) {
        this.mImpl.setTypeBoundingRectsMap(rectArr);
    }

    void setTypeMaxBoundingRectsMap(Rect[][] rectArr) {
        this.mImpl.setTypeMaxBoundingRectsMap(rectArr);
    }

    abstract class BuilderImpl29 extends BuilderImpl {
        final WindowInsets.Builder mPlatBuilder;

        BuilderImpl29() {
            this.mPlatBuilder = new WindowInsets.Builder();
        }

        BuilderImpl29(WindowInsetsCompat windowInsetsCompat) {
            WindowInsets.Builder builder;
            super(windowInsetsCompat);
            WindowInsets windowInsets = windowInsetsCompat.toWindowInsets();
            if (windowInsets != null) {
                builder = new WindowInsets.Builder(windowInsets);
            } else {
                builder = new WindowInsets.Builder();
            }
            this.mPlatBuilder = builder;
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        void setSystemWindowInsets(Insets insets) {
            this.mPlatBuilder.setSystemWindowInsets(insets.toPlatformInsets());
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        void setSystemGestureInsets(Insets insets) {
            this.mPlatBuilder.setSystemGestureInsets(insets.toPlatformInsets());
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        void setMandatorySystemGestureInsets(Insets insets) {
            this.mPlatBuilder.setMandatorySystemGestureInsets(insets.toPlatformInsets());
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        void setTappableElementInsets(Insets insets) {
            this.mPlatBuilder.setTappableElementInsets(insets.toPlatformInsets());
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        void setStableInsets(Insets insets) {
            this.mPlatBuilder.setStableInsets(insets.toPlatformInsets());
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        WindowInsetsCompat build() {
            applyInsetTypes();
            WindowInsetsCompat windowInsetsCompat = WindowInsetsCompat.toWindowInsetsCompat(this.mPlatBuilder.build());
            windowInsetsCompat.setOverriddenInsets(this.mInsetsTypeMask);
            windowInsetsCompat.setDisplayShape(this.mDisplayShapeCompat);
            windowInsetsCompat.setTypeBoundingRectsMap(this.mTypeBoundingRectsMap);
            windowInsetsCompat.setTypeMaxBoundingRectsMap(this.mTypeMaxBoundingRectsMap);
            return windowInsetsCompat;
        }
    }

    abstract class BuilderImpl30 extends BuilderImpl29 {
        BuilderImpl30() {
        }

        BuilderImpl30(WindowInsetsCompat windowInsetsCompat) {
            super(windowInsetsCompat);
        }
    }

    abstract class BuilderImpl31 extends BuilderImpl30 {
        BuilderImpl31() {
        }

        BuilderImpl31(WindowInsetsCompat windowInsetsCompat) {
            super(windowInsetsCompat);
        }
    }

    abstract class BuilderImpl34 extends BuilderImpl31 {
        BuilderImpl34() {
        }

        BuilderImpl34(WindowInsetsCompat windowInsetsCompat) {
            super(windowInsetsCompat);
        }
    }

    abstract class BuilderImpl35 extends BuilderImpl34 {
        private boolean mSetInsetsCalled;
        private boolean mSetInsetsIgnoringVisibilityCalled;

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        void initTypeBoundingRects(WindowInsetsCompat windowInsetsCompat) {
        }

        BuilderImpl35() {
            this.mSetInsetsCalled = false;
            this.mSetInsetsIgnoringVisibilityCalled = false;
        }

        BuilderImpl35(WindowInsetsCompat windowInsetsCompat) {
            super(windowInsetsCompat);
            this.mSetInsetsCalled = false;
            this.mSetInsetsIgnoringVisibilityCalled = false;
            if (windowInsetsCompat.isConsumed()) {
                return;
            }
            this.mSetInsetsCalled = true;
            this.mSetInsetsIgnoringVisibilityCalled = true;
        }
    }

    class BuilderImpl36 extends BuilderImpl35 {
        BuilderImpl36() {
        }

        BuilderImpl36(WindowInsetsCompat windowInsetsCompat) {
            super(windowInsetsCompat);
        }
    }

    public abstract class Type {
        public static int captionBar() {
            return 4;
        }

        public static int displayCutout() {
            return DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY;
        }

        public static int ime() {
            return 8;
        }

        public static int navigationBars() {
            return 2;
        }

        public static int systemBars() {
            return 519;
        }

        static int indexOf(int i) {
            if (i == 1) {
                return 0;
            }
            if (i == 2) {
                return 1;
            }
            if (i == 4) {
                return 2;
            }
            if (i == 8) {
                return 3;
            }
            if (i == 16) {
                return 4;
            }
            if (i == 32) {
                return 5;
            }
            if (i == 64) {
                return 6;
            }
            if (i == 128) {
                return 7;
            }
            if (i == 256) {
                return 8;
            }
            if (i == 512) {
                return 9;
            }
            HexString$$ExternalSyntheticBUOutline1.m("type needs to be >= FIRST and <= LAST, type=", i);
            return 0;
        }
    }

    abstract class TypeImpl30 {
        static int toPlatformType(int i) {
            int iStatusBars;
            int i2 = 0;
            for (int i3 = 1; i3 <= 512; i3 <<= 1) {
                if ((i & i3) != 0) {
                    if (i3 == 1) {
                        iStatusBars = WindowInsets.Type.statusBars();
                    } else if (i3 == 2) {
                        iStatusBars = WindowInsets.Type.navigationBars();
                    } else if (i3 == 4) {
                        iStatusBars = WindowInsets.Type.captionBar();
                    } else if (i3 == 8) {
                        iStatusBars = WindowInsets.Type.ime();
                    } else if (i3 == 16) {
                        iStatusBars = WindowInsets.Type.systemGestures();
                    } else if (i3 == 32) {
                        iStatusBars = WindowInsets.Type.mandatorySystemGestures();
                    } else if (i3 == 64) {
                        iStatusBars = WindowInsets.Type.tappableElement();
                    } else if (i3 == 128) {
                        iStatusBars = WindowInsets.Type.displayCutout();
                    }
                    i2 |= iStatusBars;
                }
            }
            return i2;
        }
    }

    abstract class TypeImpl34 {
        static int toPlatformType(int i) {
            int iStatusBars;
            int i2 = 0;
            for (int i3 = 1; i3 <= 512; i3 <<= 1) {
                if ((i & i3) != 0) {
                    if (i3 == 1) {
                        iStatusBars = WindowInsets.Type.statusBars();
                    } else if (i3 == 2) {
                        iStatusBars = WindowInsets.Type.navigationBars();
                    } else if (i3 == 4) {
                        iStatusBars = WindowInsets.Type.captionBar();
                    } else if (i3 == 8) {
                        iStatusBars = WindowInsets.Type.ime();
                    } else if (i3 == 16) {
                        iStatusBars = WindowInsets.Type.systemGestures();
                    } else if (i3 == 32) {
                        iStatusBars = WindowInsets.Type.mandatorySystemGestures();
                    } else if (i3 == 64) {
                        iStatusBars = WindowInsets.Type.tappableElement();
                    } else if (i3 == 128) {
                        iStatusBars = WindowInsets.Type.displayCutout();
                    } else if (i3 == 512) {
                        iStatusBars = WindowInsets.Type.systemOverlays();
                    }
                    i2 |= iStatusBars;
                }
            }
            return i2;
        }
    }

    void setRootWindowInsets(WindowInsetsCompat windowInsetsCompat) {
        this.mImpl.setRootWindowInsets(windowInsetsCompat);
    }

    void setRootViewData(Insets insets) {
        this.mImpl.setRootViewData(insets);
    }

    void init(View view) {
        this.mImpl.copyRootViewBounds(view);
        this.mImpl.initDisplayShape(view);
        this.mImpl.initTypeBoundingRectsMaps();
    }

    void setSystemUiVisibility(int i) {
        this.mImpl.setSystemUiVisibility(i);
    }
}
