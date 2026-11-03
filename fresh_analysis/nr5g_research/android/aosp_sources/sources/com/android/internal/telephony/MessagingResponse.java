package com.android.internal.telephony;

import android.hardware.radio.RadioResponseInfo;
import android.hardware.radio.messaging.CdmaBroadcastSmsConfigInfo;
import android.hardware.radio.messaging.GsmBroadcastSmsConfigInfo;
import android.hardware.radio.messaging.IRadioMessagingResponse;
import android.hardware.radio.messaging.SendSmsResult;
import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class MessagingResponse extends IRadioMessagingResponse.Stub {
    private final RIL mRil;

    public int getInterfaceVersion() {
        return 5;
    }

    public MessagingResponse(RIL ril) {
        this.mRil = ril;
    }

    private void responseSms(RadioResponseInfo radioResponseInfo, SendSmsResult sendSmsResult) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(2, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            SmsResponse smsResponse = new SmsResponse(sendSmsResult.messageRef, sendSmsResult.ackPDU, sendSmsResult.errorCode, RIL.getOutgoingSmsMessageId(rILRequestProcessResponse.mResult));
            if (radioResponseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, smsResponse);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, smsResponse);
        }
    }

    public void acknowledgeRequest(int i) {
        this.mRil.processRequestAck(i);
    }

    public void acknowledgeIncomingGsmSmsWithPduResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(2, this.mRil, radioResponseInfo);
    }

    public void acknowledgeLastIncomingCdmaSmsResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(2, this.mRil, radioResponseInfo);
    }

    public void acknowledgeLastIncomingGsmSmsResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(2, this.mRil, radioResponseInfo);
    }

    public void deleteSmsOnRuimResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(2, this.mRil, radioResponseInfo);
    }

    public void deleteSmsOnSimResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(2, this.mRil, radioResponseInfo);
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public void getCdmaBroadcastConfigResponse(RadioResponseInfo radioResponseInfo, CdmaBroadcastSmsConfigInfo[] cdmaBroadcastSmsConfigInfoArr) {
        int[] iArr;
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(2, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            int length = cdmaBroadcastSmsConfigInfoArr.length;
            int i = 0;
            int i2 = 1;
            if (length == 0) {
                iArr = new int[94];
                iArr[0] = 31;
                for (int i3 = 1; i3 < 94; i3 += 3) {
                    iArr[i3] = i3 / 3;
                    iArr[i3 + 1] = 1;
                    iArr[i3 + 2] = 0;
                }
            } else {
                int[] iArr2 = new int[(length * 3) + 1];
                iArr2[0] = length;
                while (i < cdmaBroadcastSmsConfigInfoArr.length) {
                    CdmaBroadcastSmsConfigInfo cdmaBroadcastSmsConfigInfo = cdmaBroadcastSmsConfigInfoArr[i2];
                    iArr2[i2] = cdmaBroadcastSmsConfigInfo.serviceCategory;
                    iArr2[i2 + 1] = cdmaBroadcastSmsConfigInfo.language;
                    iArr2[i2 + 2] = cdmaBroadcastSmsConfigInfo.selected ? 1 : 0;
                    i++;
                    i2 += 3;
                }
                iArr = iArr2;
            }
            if (radioResponseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, iArr);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, iArr);
        }
    }

    public void getGsmBroadcastConfigResponse(RadioResponseInfo radioResponseInfo, GsmBroadcastSmsConfigInfo[] gsmBroadcastSmsConfigInfoArr) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(2, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            ArrayList arrayList = new ArrayList();
            for (GsmBroadcastSmsConfigInfo gsmBroadcastSmsConfigInfo : gsmBroadcastSmsConfigInfoArr) {
                arrayList.add(new SmsBroadcastConfigInfo(gsmBroadcastSmsConfigInfo.fromServiceId, gsmBroadcastSmsConfigInfo.toServiceId, gsmBroadcastSmsConfigInfo.fromCodeScheme, gsmBroadcastSmsConfigInfo.toCodeScheme, gsmBroadcastSmsConfigInfo.selected));
            }
            if (radioResponseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, arrayList);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, arrayList);
        }
    }

    public void getSmscAddressResponse(RadioResponseInfo radioResponseInfo, String str) {
        RadioResponse.responseString(2, this.mRil, radioResponseInfo, MiuiSmsControllerStub.get(this.mRil.mPhoneId.intValue()).getSmscAddressResponse(this.mRil, str, new Object[0]));
    }

    public void reportSmsMemoryStatusResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(2, this.mRil, radioResponseInfo);
    }

    public void sendCdmaSmsExpectMoreResponse(RadioResponseInfo radioResponseInfo, SendSmsResult sendSmsResult) {
        responseSms(radioResponseInfo, sendSmsResult);
    }

    public void sendCdmaSmsResponse(RadioResponseInfo radioResponseInfo, SendSmsResult sendSmsResult) {
        responseSms(radioResponseInfo, sendSmsResult);
    }

    public void sendImsSmsResponse(RadioResponseInfo radioResponseInfo, SendSmsResult sendSmsResult) {
        responseSms(radioResponseInfo, sendSmsResult);
    }

    public void sendSmsExpectMoreResponse(RadioResponseInfo radioResponseInfo, SendSmsResult sendSmsResult) {
        responseSms(radioResponseInfo, sendSmsResult);
    }

    public void sendSmsResponse(RadioResponseInfo radioResponseInfo, SendSmsResult sendSmsResult) {
        responseSms(radioResponseInfo, sendSmsResult);
    }

    public void setCdmaBroadcastActivationResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(2, this.mRil, radioResponseInfo);
    }

    public void setCdmaBroadcastConfigResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(2, this.mRil, radioResponseInfo);
    }

    public void setGsmBroadcastActivationResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(2, this.mRil, radioResponseInfo);
    }

    public void setGsmBroadcastConfigResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(2, this.mRil, radioResponseInfo);
    }

    public void setSmscAddressResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(2, this.mRil, radioResponseInfo);
    }

    public void writeSmsToRuimResponse(RadioResponseInfo radioResponseInfo, int i) {
        RadioResponse.responseInts(2, this.mRil, radioResponseInfo, i);
    }

    public void writeSmsToSimResponse(RadioResponseInfo radioResponseInfo, int i) {
        RadioResponse.responseInts(2, this.mRil, radioResponseInfo, i);
    }

    public String getInterfaceHash() {
        return "4305c82b7f2dad0f9664f0be75e4b3e3a3b19475";
    }
}
