package com.ot.pubsub.j;

import com.ot.pubsub.util.k;

/* JADX INFO: loaded from: classes.dex */
class h implements Runnable {
    final /* synthetic */ g a;

    h(g gVar) {
        this.a = gVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.a.a.e();
        try {
            boolean zA = k.a();
            com.ot.pubsub.util.j.a("UploaderEngine", "UploadTimer netReceiver, 网络是否可用=" + zA);
            if (!zA) {
                this.a.a.l();
                return;
            }
            d.a().a(false);
            d.a().a(0, false);
            d.a().a(1, false);
            d.a().a(2, false);
            this.a.a.k();
        } catch (Exception e) {
            com.ot.pubsub.util.j.a("UploaderEngine", "netReceiver: " + e);
        }
    }
}
