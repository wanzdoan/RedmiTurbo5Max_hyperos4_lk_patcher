package miuix.view.animation;

/* JADX INFO: loaded from: classes2.dex */
public abstract class AnimationUtils extends android.view.animation.AnimationUtils {
    private static ThreadLocal sAnimationNanoState = new ThreadLocal() { // from class: miuix.view.animation.AnimationUtils.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public AnimationNanoState initialValue() {
            return new AnimationNanoState();
        }
    };

    class AnimationNanoState {
        long lastReportedTimeNanos;

        private AnimationNanoState() {
        }
    }

    public static long currentAnimationTimeNanos() {
        AnimationNanoState animationNanoState = (AnimationNanoState) sAnimationNanoState.get();
        long jNanoTime = System.nanoTime();
        animationNanoState.lastReportedTimeNanos = jNanoTime;
        return jNanoTime;
    }
}
