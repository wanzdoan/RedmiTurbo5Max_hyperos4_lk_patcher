package com.android.internal.telephony;

import java.io.Writer;

/* JADX INFO: loaded from: classes.dex */
public class IndentingPrintWriter extends AndroidUtilIndentingPrintWriter {
    public IndentingPrintWriter(Writer writer, String str) {
        super(writer, str, -1);
    }

    @Override // com.android.internal.telephony.AndroidUtilIndentingPrintWriter
    public IndentingPrintWriter increaseIndent() {
        super.increaseIndent();
        return this;
    }

    @Override // com.android.internal.telephony.AndroidUtilIndentingPrintWriter
    public IndentingPrintWriter decreaseIndent() {
        super.decreaseIndent();
        return this;
    }
}
