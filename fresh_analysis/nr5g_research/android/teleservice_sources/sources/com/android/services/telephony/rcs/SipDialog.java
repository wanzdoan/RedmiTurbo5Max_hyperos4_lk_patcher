package com.android.services.telephony.rcs;

import android.telephony.ims.SipMessage;
import android.text.TextUtils;
import com.android.internal.telephony.SipMessageParsingUtils;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class SipDialog {
    private final Set mAcceptContactFeatureTags;
    private final String mBranchId;
    private final String mCallId;
    private final String mFromTag;
    private String mToTag;
    private int mState = 0;
    private Instant mLastInteraction = Instant.now();

    public static SipDialog fromSipMessage(SipMessage sipMessage) {
        if (!SipMessageParsingUtils.isSipRequest(sipMessage.getStartLine())) {
            return null;
        }
        return new SipDialog(sipMessage.getViaBranchParameter(), sipMessage.getCallIdParameter(), SipMessageParsingUtils.getFromTag(sipMessage.getHeaderSection()), SipMessageParsingUtils.getAcceptContactFeatureTags(sipMessage.getHeaderSection()));
    }

    private SipDialog(String str, String str2, String str3, Set set) {
        this.mBranchId = str;
        this.mCallId = str2;
        this.mFromTag = str3;
        this.mAcceptContactFeatureTags = set;
    }

    public String getCallId() {
        return this.mCallId;
    }

    public int getState() {
        return this.mState;
    }

    public String getToTag() {
        return this.mToTag;
    }

    public Set getAcceptContactFeatureTags() {
        return this.mAcceptContactFeatureTags;
    }

    public SipDialog forkDialog() {
        return new SipDialog(this.mBranchId, this.mCallId, this.mFromTag, this.mAcceptContactFeatureTags);
    }

    public void earlyResponse(String str) {
        if (TextUtils.isEmpty(str) || this.mState != 0) {
            return;
        }
        this.mLastInteraction = Instant.now();
        if (this.mToTag == null) {
            this.mToTag = str;
        }
    }

    public void confirm(String str) {
        if (this.mState != 0) {
            return;
        }
        this.mLastInteraction = Instant.now();
        this.mState = 1;
        if (this.mToTag == null) {
            this.mToTag = str;
        }
    }

    public void close() {
        this.mLastInteraction = Instant.now();
        this.mState = 2;
    }

    public boolean isResponseAssociatedWithDialog(SipMessage sipMessage) {
        if (!this.mBranchId.equals(sipMessage.getViaBranchParameter()) || !this.mCallId.equals(sipMessage.getCallIdParameter())) {
            return false;
        }
        return this.mFromTag.equals(SipMessageParsingUtils.getFromTag(sipMessage.getHeaderSection()));
    }

    public boolean isRequestAssociatedWithDialog(SipMessage sipMessage) {
        if (!this.mCallId.equals(sipMessage.getCallIdParameter())) {
            return false;
        }
        String fromTag = SipMessageParsingUtils.getFromTag(sipMessage.getHeaderSection());
        String toTag = SipMessageParsingUtils.getToTag(sipMessage.getHeaderSection());
        if (fromTag == null || toTag == null || this.mToTag == null) {
            return false;
        }
        return (this.mFromTag.equals(fromTag) || this.mFromTag.equals(toTag)) && (this.mToTag.equals(toTag) || this.mToTag.equals(fromTag));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("SipDialog[");
        int i = this.mState;
        if (i == 0) {
            sb.append("early");
        } else if (i == 1) {
            sb.append("confirmed");
        } else if (i == 2) {
            sb.append("closed");
        } else {
            sb.append(i);
        }
        sb.append("] bId=");
        sb.append(this.mBranchId);
        sb.append(", cId=");
        sb.append(this.mCallId);
        sb.append(", f=");
        sb.append(this.mFromTag);
        sb.append(", t=");
        sb.append(this.mToTag);
        sb.append(", Last Interaction: ");
        sb.append(this.mLastInteraction);
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            SipDialog sipDialog = (SipDialog) obj;
            if (this.mBranchId.equals(sipDialog.mBranchId) && Objects.equals(this.mCallId, sipDialog.mCallId) && Objects.equals(this.mFromTag, sipDialog.mFromTag) && Objects.equals(this.mToTag, sipDialog.mToTag)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.mBranchId, this.mCallId, this.mFromTag, this.mToTag);
    }
}
