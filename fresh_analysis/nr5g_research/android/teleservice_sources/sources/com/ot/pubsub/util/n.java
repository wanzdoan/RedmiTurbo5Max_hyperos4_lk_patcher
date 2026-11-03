package com.ot.pubsub.util;

/* JADX INFO: loaded from: classes.dex */
final class n implements m.a {
    final /* synthetic */ boolean a;

    n(boolean z) {
        this.a = z;
    }

    @Override // com.ot.pubsub.util.m.a
    public boolean a(Object obj) {
        if (this.a) {
            return m.a(obj);
        }
        return m.b(obj);
    }
}
