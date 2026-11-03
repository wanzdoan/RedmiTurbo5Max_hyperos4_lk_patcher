package com.android.internal.telephony.cat;

import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public class SequentialParser {
    private ComprehensionTlv mCurrentCtlv;
    private final Iterator mIter;

    @FunctionalInterface
    public interface ParseFunction<T> {
        T apply(ComprehensionTlv comprehensionTlv) throws ResultException;
    }

    public SequentialParser(Iterator<ComprehensionTlv> it) {
        this.mIter = it;
    }

    public <T> T parseMandatory(ComprehensionTlvTag comprehensionTlvTag, ParseFunction<T> parseFunction, boolean z) throws ResultException {
        ComprehensionTlv currentCtlv = getCurrentCtlv();
        if (currentCtlv == null) {
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD, comprehensionTlvTag.toString() + " tag not found: End of TLV stream");
        }
        if (currentCtlv.getTag() == comprehensionTlvTag.value()) {
            if (z) {
                consumeCurrentCtlv();
            }
            if (parseFunction != null) {
                return parseFunction.apply(currentCtlv);
            }
            return null;
        }
        throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD, comprehensionTlvTag.toString() + " tag not found: Found " + currentCtlv.getTag() + " instead");
    }

    public <T> T parseMandatory(ComprehensionTlvTag comprehensionTlvTag, ParseFunction<T> parseFunction) throws ResultException {
        return (T) parseMandatory(comprehensionTlvTag, parseFunction, true);
    }

    public <T> T parseOptional(ComprehensionTlvTag comprehensionTlvTag, ParseFunction<T> parseFunction, T t, boolean z) throws ResultException {
        ComprehensionTlv currentCtlv = getCurrentCtlv();
        if (currentCtlv == null || currentCtlv.getTag() != comprehensionTlvTag.value()) {
            return t;
        }
        if (z) {
            consumeCurrentCtlv();
        }
        if (parseFunction != null) {
            return parseFunction.apply(currentCtlv);
        }
        return null;
    }

    public <T> T parseOptional(ComprehensionTlvTag comprehensionTlvTag, ParseFunction<T> parseFunction, T t) throws ResultException {
        return (T) parseOptional(comprehensionTlvTag, parseFunction, t, true);
    }

    private void consumeCurrentCtlv() {
        this.mCurrentCtlv = null;
    }

    private ComprehensionTlv getCurrentCtlv() {
        if (this.mCurrentCtlv == null && this.mIter.hasNext()) {
            this.mCurrentCtlv = (ComprehensionTlv) this.mIter.next();
        }
        return this.mCurrentCtlv;
    }
}
