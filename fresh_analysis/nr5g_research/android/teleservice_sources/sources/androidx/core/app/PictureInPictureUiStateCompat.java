package androidx.core.app;

import android.app.PictureInPictureUiState;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: compiled from: PictureInPictureUiStateCompat.kt */
/* JADX INFO: loaded from: classes.dex */
public final class PictureInPictureUiStateCompat {
    public static final Companion Companion = new Companion(null);
    private final boolean isStashed;
    private final boolean isTransitioningToPip;

    public PictureInPictureUiStateCompat(boolean z, boolean z2) {
        this.isStashed = z;
        this.isTransitioningToPip = z2;
    }

    /* JADX INFO: compiled from: PictureInPictureUiStateCompat.kt */
    public final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final PictureInPictureUiStateCompat fromPictureInPictureUiState(PictureInPictureUiState pictureInPictureUiState) {
            pictureInPictureUiState.getClass();
            return new PictureInPictureUiStateCompat(pictureInPictureUiState.isStashed(), pictureInPictureUiState.isTransitioningToPip());
        }
    }
}
