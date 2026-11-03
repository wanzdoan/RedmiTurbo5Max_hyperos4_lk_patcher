package com.android.internal.telephony.cat;

/* JADX INFO: compiled from: CommandParams.java */
/* JADX INFO: loaded from: classes.dex */
class SendUssdParams extends CommandParams {
    public byte mCodingScheme;
    public TextMessage mTextMsg;
    public String mUssdString;

    SendUssdParams(CommandDetails commandDetails, TextMessage textMessage, String str, byte b) {
        super(commandDetails);
        this.mTextMsg = textMessage;
        this.mUssdString = str;
        this.mCodingScheme = b;
    }
}
