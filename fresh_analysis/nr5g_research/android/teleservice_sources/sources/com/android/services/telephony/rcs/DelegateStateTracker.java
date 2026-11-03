package com.android.services.telephony.rcs;

import android.app.compat.CompatChanges;
import android.os.RemoteException;
import android.telephony.ims.DelegateRegistrationState;
import android.telephony.ims.FeatureTagState;
import android.telephony.ims.SipDelegateConfiguration;
import android.telephony.ims.SipDelegateImsConfiguration;
import android.telephony.ims.aidl.ISipDelegate;
import android.telephony.ims.aidl.ISipDelegateConnectionStateCallback;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.metrics.RcsStats;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class DelegateStateTracker implements DelegateBinderStateManager.StateCallback {
    public static final long SUPPORT_DEREGISTERING_LOSING_PDN_STATE = 201522903;
    public static final long SUPPORT_REGISTERING_DELEGATE_STATE = 205194548;
    private final ISipDelegateConnectionStateCallback mAppStateCallback;
    private CompatChangesFactory mCompatChangesFactory;
    private List mDelegateDeniedTags;
    private Set mDelegateSupportedTags;
    private DelegateRegistrationState mLastRegState;
    private final ISipDelegate mLocalDelegateImpl;
    private final RcsStats mRcsStats;
    private final int mSubId;
    private final int mUid;
    private final LocalLog mLocalLog = new LocalLog(50);
    private boolean mCreatedCalled = false;
    private int mRegistrationStateOverride = -1;

    public interface CompatChangesFactory {
        boolean isChangeEnabled(long j, int i);
    }

    private int getDeregisteringReasonForCompatibility(int i) {
        if (i == 7) {
            return 3;
        }
        return i;
    }

    public DelegateStateTracker(int i, int i2, ISipDelegateConnectionStateCallback iSipDelegateConnectionStateCallback, ISipDelegate iSipDelegate, RcsStats rcsStats) {
        this.mSubId = i;
        this.mUid = i2;
        this.mAppStateCallback = iSipDelegateConnectionStateCallback;
        this.mLocalDelegateImpl = iSipDelegate;
        this.mRcsStats = rcsStats;
        setCompatChangesFactory(new CompatChangesFactory() { // from class: com.android.services.telephony.rcs.DelegateStateTracker$$ExternalSyntheticLambda0
            @Override // com.android.services.telephony.rcs.DelegateStateTracker.CompatChangesFactory
            public final boolean isChangeEnabled(long j, int i3) {
                return CompatChanges.isChangeEnabled(j, i3);
            }
        });
    }

    protected void setCompatChangesFactory(CompatChangesFactory compatChangesFactory) {
        this.mCompatChangesFactory = compatChangesFactory;
    }

    public void sipDelegateConnected(Set set, Set set2) {
        logi("SipDelegate connected with denied tags:" + set2);
        if (!this.mCreatedCalled) {
            this.mCreatedCalled = true;
            notifySipDelegateCreated();
            this.mDelegateSupportedTags = set;
            this.mRcsStats.createSipDelegateStats(this.mSubId, set);
        }
        this.mRegistrationStateOverride = -1;
        this.mDelegateDeniedTags = new ArrayList(set2);
    }

    public void sipDelegateChanging(int i) {
        logi("SipDelegate Changing");
        this.mRegistrationStateOverride = i;
        if (this.mLastRegState == null) {
            logw("sipDelegateChanging: invalid state, onRegistrationStateChanged never called.");
            this.mLastRegState = new DelegateRegistrationState.Builder().build();
        }
        onRegistrationStateChanged(this.mLastRegState);
    }

    public void sipDelegateDestroyed(int i) {
        logi("SipDelegate destroyed:" + i);
        this.mRegistrationStateOverride = -1;
        try {
            this.mAppStateCallback.onDestroyed(i);
            this.mRcsStats.onSipDelegateStats(this.mSubId, this.mDelegateSupportedTags, i);
        } catch (RemoteException e) {
            logw("sipDelegateDestroyed: IMS application is dead: " + e);
        }
    }

    @Override // com.android.services.telephony.rcs.DelegateBinderStateManager.StateCallback
    public void onRegistrationStateChanged(DelegateRegistrationState delegateRegistrationState) {
        if (!this.mCompatChangesFactory.isChangeEnabled(SUPPORT_DEREGISTERING_LOSING_PDN_STATE, this.mUid)) {
            delegateRegistrationState = overrideDeregisteringStateForCompatibility(delegateRegistrationState);
        }
        if (!this.mCompatChangesFactory.isChangeEnabled(SUPPORT_REGISTERING_DELEGATE_STATE, this.mUid)) {
            delegateRegistrationState = overrideRegistrationForCompatibility(delegateRegistrationState);
        }
        if (this.mRegistrationStateOverride > 0) {
            logi("onRegistrationStateChanged: overriding registered state to " + this.mRegistrationStateOverride);
            delegateRegistrationState = overrideRegistrationForDelegateChange(this.mRegistrationStateOverride, delegateRegistrationState);
        }
        if (delegateRegistrationState.equals(this.mLastRegState)) {
            logi("onRegistrationStateChanged: skipping notification, state is the same.");
            return;
        }
        this.mLastRegState = delegateRegistrationState;
        logi("onRegistrationStateChanged: sending reg state " + delegateRegistrationState);
        try {
            this.mAppStateCallback.onFeatureTagStatusChanged(delegateRegistrationState, this.mDelegateDeniedTags);
            this.mRcsStats.onSipTransportFeatureTagStats(this.mSubId, new ArraySet(this.mDelegateDeniedTags), delegateRegistrationState.getDeregisteredFeatureTags(), delegateRegistrationState.getRegisteredFeatureTags());
        } catch (RemoteException e) {
            logw("onRegistrationStateChanged: IMS application is dead: " + e);
        }
    }

    @Override // com.android.services.telephony.rcs.DelegateBinderStateManager.StateCallback
    public void onImsConfigurationChanged(SipDelegateImsConfiguration sipDelegateImsConfiguration) {
        logi("onImsConfigurationChanged: Sending new IMS configuration.");
        try {
            this.mAppStateCallback.onImsConfigurationChanged(sipDelegateImsConfiguration);
        } catch (RemoteException e) {
            logw("onImsConfigurationChanged: IMS application is dead: " + e);
        }
    }

    @Override // com.android.services.telephony.rcs.DelegateBinderStateManager.StateCallback
    public void onConfigurationChanged(SipDelegateConfiguration sipDelegateConfiguration) {
        logi("onImsConfigurationChanged: Sending new IMS configuration.");
        try {
            this.mAppStateCallback.onConfigurationChanged(sipDelegateConfiguration);
        } catch (RemoteException e) {
            logw("onImsConfigurationChanged: IMS application is dead: " + e);
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("Last reg state: " + this.mLastRegState);
        printWriter.println("Denied tags: " + this.mDelegateDeniedTags);
        printWriter.println();
        printWriter.println("Most recent logs: ");
        this.mLocalLog.dump(printWriter);
    }

    private DelegateRegistrationState overrideRegistrationForDelegateChange(int i, DelegateRegistrationState delegateRegistrationState) {
        Set registeredFeatureTags = delegateRegistrationState.getRegisteredFeatureTags();
        Set registeringFeatureTags = delegateRegistrationState.getRegisteringFeatureTags();
        DelegateRegistrationState.Builder builder = new DelegateRegistrationState.Builder();
        for (FeatureTagState featureTagState : delegateRegistrationState.getDeregisteringFeatureTags()) {
            builder.addDeregisteringFeatureTag(featureTagState.getFeatureTag(), featureTagState.getState());
        }
        for (FeatureTagState featureTagState2 : delegateRegistrationState.getDeregisteredFeatureTags()) {
            builder.addDeregisteredFeatureTag(featureTagState2.getFeatureTag(), featureTagState2.getState());
        }
        Iterator it = registeringFeatureTags.iterator();
        while (it.hasNext()) {
            builder.addDeregisteringFeatureTag((String) it.next(), i);
        }
        Iterator it2 = registeredFeatureTags.iterator();
        while (it2.hasNext()) {
            builder.addDeregisteringFeatureTag((String) it2.next(), i);
        }
        return builder.build();
    }

    private DelegateRegistrationState overrideRegistrationForCompatibility(DelegateRegistrationState delegateRegistrationState) {
        Set registeredFeatureTags = delegateRegistrationState.getRegisteredFeatureTags();
        Set registeringFeatureTags = delegateRegistrationState.getRegisteringFeatureTags();
        DelegateRegistrationState.Builder builder = new DelegateRegistrationState.Builder();
        for (FeatureTagState featureTagState : delegateRegistrationState.getDeregisteringFeatureTags()) {
            builder.addDeregisteringFeatureTag(featureTagState.getFeatureTag(), featureTagState.getState());
        }
        for (FeatureTagState featureTagState2 : delegateRegistrationState.getDeregisteredFeatureTags()) {
            builder.addDeregisteredFeatureTag(featureTagState2.getFeatureTag(), featureTagState2.getState());
        }
        builder.addRegisteredFeatureTags(registeredFeatureTags);
        Iterator it = registeringFeatureTags.iterator();
        while (it.hasNext()) {
            builder.addDeregisteredFeatureTag((String) it.next(), 2);
        }
        return builder.build();
    }

    private DelegateRegistrationState overrideDeregisteringStateForCompatibility(DelegateRegistrationState delegateRegistrationState) {
        Set registeredFeatureTags = delegateRegistrationState.getRegisteredFeatureTags();
        Set registeringFeatureTags = delegateRegistrationState.getRegisteringFeatureTags();
        DelegateRegistrationState.Builder builder = new DelegateRegistrationState.Builder();
        for (FeatureTagState featureTagState : delegateRegistrationState.getDeregisteredFeatureTags()) {
            builder.addDeregisteredFeatureTag(featureTagState.getFeatureTag(), featureTagState.getState());
        }
        builder.addRegisteredFeatureTags(registeredFeatureTags);
        builder.addRegisteringFeatureTags(registeringFeatureTags);
        for (FeatureTagState featureTagState2 : delegateRegistrationState.getDeregisteringFeatureTags()) {
            builder.addDeregisteringFeatureTag(featureTagState2.getFeatureTag(), getDeregisteringReasonForCompatibility(featureTagState2.getState()));
        }
        return builder.build();
    }

    private void notifySipDelegateCreated() {
        try {
            this.mAppStateCallback.onCreated(this.mLocalDelegateImpl);
        } catch (RemoteException e) {
            logw("notifySipDelegateCreated: IMS application is dead: " + e);
        }
    }

    private void logi(String str) {
        Log.i("SipTransportC", "DelegateST[" + this.mSubId + "] " + str);
        LocalLog localLog = this.mLocalLog;
        StringBuilder sb = new StringBuilder("[I] ");
        sb.append(str);
        localLog.log(sb.toString());
    }

    private void logw(String str) {
        Log.w("SipTransportC", "DelegateST[" + this.mSubId + "] " + str);
        LocalLog localLog = this.mLocalLog;
        StringBuilder sb = new StringBuilder("[W] ");
        sb.append(str);
        localLog.log(sb.toString());
    }
}
