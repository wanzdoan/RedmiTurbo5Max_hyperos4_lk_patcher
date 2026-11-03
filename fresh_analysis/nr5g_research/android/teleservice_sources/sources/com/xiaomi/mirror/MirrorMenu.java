package com.xiaomi.mirror;

import android.app.PendingIntent;
import android.net.Uri;
import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public class MirrorMenu {
    static final int TYPE_COMMON = 0;
    static final int TYPE_NEW_DISPLAY_OPEN = 1;
    static final int TYPE_PC_OPEN = 2;

    public class Builder {
        public MirrorMenu build() {
            throw new UnsupportedOperationException("Stub!");
        }

        public Builder setCallback(Callback callback) {
            throw new UnsupportedOperationException("Stub!");
        }

        public Builder setLabel(CharSequence charSequence) {
            throw new UnsupportedOperationException("Stub!");
        }
    }

    public interface Callback {
        void onClick(View view, MirrorMenu mirrorMenu);
    }

    public class NewDisplayOpenBuilder {
        public MirrorMenu build() {
            throw new UnsupportedOperationException("Stub!");
        }

        public NewDisplayOpenBuilder setLabel(CharSequence charSequence) {
            throw new UnsupportedOperationException("Stub!");
        }

        public NewDisplayOpenBuilder setPendingIntent(PendingIntent pendingIntent) {
            throw new UnsupportedOperationException("Stub!");
        }
    }

    public class PcOpenBuilder {
        public MirrorMenu build() {
            throw new UnsupportedOperationException("Stub!");
        }

        public PcOpenBuilder setExtra(String str) {
            throw new UnsupportedOperationException("Stub!");
        }

        public PcOpenBuilder setLabel(CharSequence charSequence) {
            throw new UnsupportedOperationException("Stub!");
        }

        public PcOpenBuilder setUri(Uri uri) {
            throw new UnsupportedOperationException("Stub!");
        }
    }

    public CharSequence getLabel() {
        throw new UnsupportedOperationException("Stub!");
    }

    public Callback getListener() {
        throw new UnsupportedOperationException("Stub!");
    }

    public PendingIntent getPendingIntent() {
        throw new UnsupportedOperationException("Stub!");
    }

    public int getType() {
        throw new UnsupportedOperationException("Stub!");
    }

    public Uri getUri() {
        throw new UnsupportedOperationException("Stub!");
    }

    public boolean needCallRemote() {
        throw new UnsupportedOperationException("Stub!");
    }
}
