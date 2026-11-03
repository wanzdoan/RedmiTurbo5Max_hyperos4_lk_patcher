package com.ot.pubsub.j;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* JADX INFO: loaded from: classes.dex */
class e extends BroadcastReceiver {
    final /* synthetic */ d a;

    e(d dVar) {
        this.a = dVar;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        com.ot.pubsub.e.a.a(new f(this, intent.getAction()));
    }
}
