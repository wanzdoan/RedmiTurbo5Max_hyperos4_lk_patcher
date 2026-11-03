package kotlin.internal;

import kotlin.random.FallbackThreadLocalRandom;
import kotlin.random.Random;

/* JADX INFO: compiled from: PlatformImplementations.kt */
/* JADX INFO: loaded from: classes2.dex */
public abstract class PlatformImplementations {
    public Random defaultPlatformRandom() {
        return new FallbackThreadLocalRandom();
    }
}
