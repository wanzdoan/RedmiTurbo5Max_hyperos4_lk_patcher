package com.android.internal.telephony.cat;

/* JADX INFO: loaded from: classes.dex */
public class SendSMSParams extends CommandParams {
    TextMessage mDestAddress;
    DisplayTextParams mDisplayText;
    byte[] mRawTpdu;
    String mSmscAddress;
    TextMessage mTextSmsMsg;

    public SendSMSParams(CommandDetails commandDetails, TextMessage textMessage, TextMessage textMessage2, DisplayTextParams displayTextParams, String str, byte[] bArr) {
        super(commandDetails);
        this.mTextSmsMsg = textMessage;
        this.mDestAddress = textMessage2;
        this.mDisplayText = displayTextParams;
        this.mSmscAddress = str;
        this.mRawTpdu = bArr;
    }

    public SendSMSParams(CommandDetails commandDetails, TextMessage textMessage, TextMessage textMessage2, DisplayTextParams displayTextParams, byte[] bArr) {
        this(commandDetails, textMessage, textMessage2, displayTextParams, null, bArr);
    }
}
