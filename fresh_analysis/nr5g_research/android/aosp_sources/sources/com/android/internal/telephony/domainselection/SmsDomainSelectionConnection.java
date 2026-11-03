package com.android.internal.telephony.domainselection;

import android.telephony.DomainSelectionService;
import com.android.internal.telephony.Phone;
import java.util.concurrent.CompletableFuture;

/* JADX INFO: loaded from: classes.dex */
public class SmsDomainSelectionConnection extends DomainSelectionConnection {
    private DomainSelectionConnection.DomainSelectionConnectionCallback mCallback;

    public SmsDomainSelectionConnection(Phone phone, DomainSelectionController domainSelectionController) {
        this(phone, domainSelectionController, false);
        this.mTag = "DomainSelectionConnection-SMS";
    }

    protected SmsDomainSelectionConnection(Phone phone, DomainSelectionController domainSelectionController, boolean z) {
        super(phone, 2, z, domainSelectionController);
    }

    @Override // com.android.internal.telephony.domainselection.DomainSelectionConnection
    public void onWlanSelected() {
        super.onDomainSelected(2);
    }

    @Override // com.android.internal.telephony.domainselection.DomainSelectionConnection
    public void onSelectionTerminated(int i) {
        DomainSelectionConnection.DomainSelectionConnectionCallback domainSelectionConnectionCallback = this.mCallback;
        if (domainSelectionConnectionCallback != null) {
            domainSelectionConnectionCallback.onSelectionTerminated(i);
        }
    }

    public CompletableFuture<Integer> requestDomainSelection(DomainSelectionService.SelectionAttributes selectionAttributes, DomainSelectionConnection.DomainSelectionConnectionCallback domainSelectionConnectionCallback) {
        this.mCallback = domainSelectionConnectionCallback;
        selectDomain(selectionAttributes);
        return getCompletableFuture();
    }
}
