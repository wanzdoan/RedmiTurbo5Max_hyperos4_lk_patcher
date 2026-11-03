package com.ot.pubsub.a;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* JADX INFO: loaded from: classes.dex */
class k extends BroadcastReceiver {
    final /* synthetic */ d a;

    k(d dVar) {
        this.a = dVar;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        com.ot.pubsub.e.a.a(new l(this, intent.getAction()));
    }
}
