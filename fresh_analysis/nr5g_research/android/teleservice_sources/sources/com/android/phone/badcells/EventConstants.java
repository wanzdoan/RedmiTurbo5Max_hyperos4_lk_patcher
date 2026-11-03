package com.android.phone.badcells;

import com.android.services.telephony.relay.phonecall.message.response.ResponseObject;
import com.ot.pubsub.i.a;

/* JADX INFO: compiled from: BadCellDynamicWeight.java */
/* JADX INFO: loaded from: classes.dex */
abstract class EventConstants {
    public static int getEventRat(int i) {
        switch (i) {
            case 402:
            case a.c /* 404 */:
            case 406:
            case ResponseObject.RELAY_TIMEOUT_CODE /* 408 */:
            case 412:
                return 1;
            case 403:
            case 405:
            case 407:
            case 409:
            case 413:
                return 0;
            case 410:
            case 411:
            default:
                return -1;
        }
    }
}
