package com.android.internal.telephony.imsphone;

import com.android.internal.telephony.Call;
import com.android.internal.telephony.Phone;
import com.android.telephony.Rlog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

/* JADX INFO: loaded from: classes.dex */
public class ImsCallInfoTracker {
    private final Phone mPhone;
    private final List mQueue = new ArrayList();
    private int mNextIndex = 1;
    private final Map mImsCallInfo = new HashMap();

    public ImsCallInfoTracker(Phone phone) {
        this.mPhone = phone;
    }

    public void addImsCallStatus(ImsPhoneConnection imsPhoneConnection) {
        synchronized (this.mImsCallInfo) {
            try {
                if (this.mQueue.isEmpty()) {
                    List list = this.mQueue;
                    int i = this.mNextIndex;
                    this.mNextIndex = i + 1;
                    list.add(new ImsCallInfo(i));
                }
                ImsCallInfo imsCallInfo = (ImsCallInfo) this.mQueue.iterator().next();
                this.mQueue.remove(imsCallInfo);
                imsCallInfo.init(imsPhoneConnection);
                this.mImsCallInfo.put(imsPhoneConnection, imsCallInfo);
                if (!imsCallInfo.shouldIgnoreUpdate()) {
                    notifyImsCallStatus();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void updateImsCallStatus(ImsPhoneConnection imsPhoneConnection) {
        updateImsCallStatus(imsPhoneConnection, false, false);
    }

    public void updateImsCallStatus(ImsPhoneConnection imsPhoneConnection, boolean z, boolean z2) {
        synchronized (this.mImsCallInfo) {
            try {
                ImsCallInfo imsCallInfo = (ImsCallInfo) this.mImsCallInfo.get(imsPhoneConnection);
                if (imsCallInfo == null) {
                    return;
                }
                if (imsCallInfo.update(imsPhoneConnection, z, z2)) {
                    notifyImsCallStatus();
                }
                Call.State state = imsPhoneConnection.getState();
                if (state == Call.State.DISCONNECTED || state == Call.State.IDLE) {
                    this.mImsCallInfo.remove(imsPhoneConnection);
                    imsCallInfo.reset();
                    int index = imsCallInfo.getIndex();
                    int i = this.mNextIndex;
                    if (index < i - 1) {
                        this.mQueue.add(imsCallInfo);
                        sort(this.mQueue);
                    } else {
                        this.mNextIndex = i - 1;
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void clearAllOrphanedConnections() {
        this.mImsCallInfo.values().stream().forEach(new Consumer() { // from class: com.android.internal.telephony.imsphone.ImsCallInfoTracker$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ImsCallInfo) obj).onDisconnect();
            }
        });
        notifyImsCallStatus();
        clearAllCallInfo();
    }

    public void notifySrvccCompleted() {
        clearAllCallInfo();
        notifyImsCallStatus();
    }

    private void clearAllCallInfo() {
        try {
            this.mImsCallInfo.values().stream().forEach(new Consumer() { // from class: com.android.internal.telephony.imsphone.ImsCallInfoTracker$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((ImsCallInfo) obj).reset();
                }
            });
            this.mImsCallInfo.clear();
            this.mQueue.clear();
            this.mNextIndex = 1;
        } catch (UnsupportedOperationException e) {
            Rlog.e("ImsCallInfoTracker", "e=" + e);
        }
    }

    private void notifyImsCallStatus() {
        ArrayList arrayList = new ArrayList(this.mImsCallInfo.values().stream().filter(new Predicate() { // from class: com.android.internal.telephony.imsphone.ImsCallInfoTracker$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ImsCallInfoTracker.$r8$lambda$D_tz1UDB94fY5UBCEc9noYsbYaU((ImsCallInfo) obj);
            }
        }).toList());
        sort(arrayList);
        this.mPhone.updateImsCallStatus(arrayList, null);
    }

    public static /* synthetic */ boolean $r8$lambda$D_tz1UDB94fY5UBCEc9noYsbYaU(ImsCallInfo imsCallInfo) {
        return !imsCallInfo.shouldIgnoreUpdate();
    }

    public static void sort(List<ImsCallInfo> list) {
        Collections.sort(list, new Comparator<ImsCallInfo>() { // from class: com.android.internal.telephony.imsphone.ImsCallInfoTracker.1
            @Override // java.util.Comparator
            public int compare(ImsCallInfo imsCallInfo, ImsCallInfo imsCallInfo2) {
                if (imsCallInfo.getIndex() > imsCallInfo2.getIndex()) {
                    return 1;
                }
                return imsCallInfo.getIndex() < imsCallInfo2.getIndex() ? -1 : 0;
            }
        });
    }
}
