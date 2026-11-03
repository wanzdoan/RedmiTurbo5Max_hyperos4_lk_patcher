package com.ot.pubsub.j;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* JADX INFO: loaded from: classes.dex */
class g extends BroadcastReceiver {
    final /* synthetic */ d a;

    g(d dVar) {
        this.a = dVar;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (!this.a.j) {
            com.ot.pubsub.e.a.a(new h(this));
        }
        this.a.j = false;
    }
}
