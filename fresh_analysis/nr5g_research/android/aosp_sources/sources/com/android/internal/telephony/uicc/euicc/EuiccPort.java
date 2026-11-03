package com.android.internal.telephony.uicc.euicc;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.service.carrier.CarrierIdentifier;
import android.service.euicc.EuiccProfileInfo;
import android.telephony.SubscriptionInfo;
import android.telephony.UiccAccessRule;
import android.telephony.euicc.EuiccNotification;
import android.telephony.euicc.EuiccRulesAuthTable;
import android.telephony.gsm.SmsMessage;
import android.text.TextUtils;
import com.android.internal.telephony.AndroidUtilIndentingPrintWriter;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccIoResult;
import com.android.internal.telephony.uicc.IccSlotStatus;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.PortUtils;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccPort;
import com.android.internal.telephony.uicc.asn1.Asn1Decoder;
import com.android.internal.telephony.uicc.asn1.Asn1Node;
import com.android.internal.telephony.uicc.asn1.InvalidAsn1DataException;
import com.android.internal.telephony.uicc.asn1.TagNotFoundException;
import com.android.internal.telephony.uicc.euicc.apdu.ApduException;
import com.android.internal.telephony.uicc.euicc.apdu.ApduSender;
import com.android.internal.telephony.uicc.euicc.apdu.ApduSenderResultCallback;
import com.android.internal.telephony.uicc.euicc.apdu.RequestBuilder;
import com.android.internal.telephony.uicc.euicc.apdu.RequestProvider;
import com.android.internal.telephony.uicc.euicc.async.AsyncResultCallback;
import com.android.internal.telephony.uicc.euicc.async.AsyncResultHelper;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class EuiccPort extends UiccPort {
    private static final EuiccSpecVersion SGP22_V_2_0 = new EuiccSpecVersion(2, 0, 0);
    private static final EuiccSpecVersion SGP22_V_2_1 = new EuiccSpecVersion(2, 1, 0);
    private final ApduSender mApduSender;
    private volatile String mEid;
    private EuiccSpecVersion mSpecVersion;
    public IccSlotStatus.MultipleEnabledProfilesMode mSupportedMepMode;

    /* JADX INFO: Access modifiers changed from: private */
    interface ApduExceptionHandler {
        void handleException(Throwable th);
    }

    /* JADX INFO: Access modifiers changed from: private */
    interface ApduIntermediateResultHandler {
        boolean shouldContinue(IccIoResult iccIoResult);
    }

    /* JADX INFO: Access modifiers changed from: private */
    interface ApduRequestBuilder {
        void build(RequestBuilder requestBuilder) throws TagNotFoundException, InvalidAsn1DataException, EuiccCardException;
    }

    /* JADX INFO: Access modifiers changed from: private */
    interface ApduResponseHandler<T> {
        T handleResult(byte[] bArr) throws TagNotFoundException, InvalidAsn1DataException, EuiccCardException;
    }

    public static /* synthetic */ byte[] $r8$lambda$cX1IMnQ1WDgUWeH0JuepU0bkF1Q(byte[] bArr) {
        return bArr;
    }

    /* JADX INFO: renamed from: $r8$lambda$yg2d-ZXIUjGtnOew9Uc7TnFvQak, reason: not valid java name */
    public static /* synthetic */ void m2023$r8$lambda$yg2dZXIUjGtnOew9Uc7TnFvQak(RequestBuilder requestBuilder) {
    }

    /* JADX INFO: renamed from: $r8$lambda$zAmf1sNlPu-q7i_7B1dM9SNX1hc, reason: not valid java name */
    public static /* synthetic */ byte[] m2024$r8$lambda$zAmf1sNlPuq7i_7B1dM9SNX1hc(byte[] bArr) {
        return bArr;
    }

    public EuiccPort(Context context, CommandsInterface commandsInterface, IccCardStatus iccCardStatus, int i, Object obj, UiccCard uiccCard, IccSlotStatus.MultipleEnabledProfilesMode multipleEnabledProfilesMode) {
        super(context, commandsInterface, iccCardStatus, i, obj, uiccCard);
        this.mApduSender = new ApduSender(context, i, commandsInterface, "A0000005591010FFFFFFFF8900000100", false);
        if (TextUtils.isEmpty(iccCardStatus.eid)) {
            loge("no eid given in constructor for phone " + i);
        } else {
            this.mEid = iccCardStatus.eid;
            this.mCardId = iccCardStatus.eid;
        }
        this.mSupportedMepMode = multipleEnabledProfilesMode;
    }

    public void getSpecVersion(AsyncResultCallback<EuiccSpecVersion> asyncResultCallback, Handler handler) {
        EuiccSpecVersion euiccSpecVersion = this.mSpecVersion;
        if (euiccSpecVersion != null) {
            AsyncResultHelper.returnResult(euiccSpecVersion, asyncResultCallback, handler);
        } else {
            sendApdu(newRequestProvider(new ApduRequestBuilder() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda43
                @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduRequestBuilder
                public final void build(RequestBuilder requestBuilder) {
                    EuiccPort.m2023$r8$lambda$yg2dZXIUjGtnOew9Uc7TnFvQak(requestBuilder);
                }
            }), new ApduResponseHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda44
                @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduResponseHandler
                public final Object handleResult(byte[] bArr) {
                    return this.f$0.lambda$getSpecVersion$1(bArr);
                }
            }, asyncResultCallback, handler);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ EuiccSpecVersion lambda$getSpecVersion$1(byte[] bArr) {
        return this.mSpecVersion;
    }

    @Override // com.android.internal.telephony.uicc.UiccPort
    public void update(Context context, CommandsInterface commandsInterface, IccCardStatus iccCardStatus, UiccCard uiccCard) {
        synchronized (this.mLock) {
            try {
                if (!TextUtils.isEmpty(iccCardStatus.eid)) {
                    this.mEid = iccCardStatus.eid;
                }
                super.update(context, commandsInterface, iccCardStatus, uiccCard);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void updateSupportedMepMode(IccSlotStatus.MultipleEnabledProfilesMode multipleEnabledProfilesMode) {
        logd("updateSupportedMepMode");
        this.mSupportedMepMode = multipleEnabledProfilesMode;
    }

    public void getAllProfiles(AsyncResultCallback<EuiccProfileInfo[]> asyncResultCallback, Handler handler) {
        final byte[] bArr = this.mSupportedMepMode.isMepMode() ? Tags.EUICC_PROFILE_MEP_TAGS : Tags.EUICC_PROFILE_TAGS;
        sendApdu(newRequestProvider(new ApduRequestBuilder() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda32
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduRequestBuilder
            public final void build(RequestBuilder requestBuilder) {
                requestBuilder.addStoreData(Asn1Node.newBuilder(48941).addChildAsBytes(92, bArr).build().toHex());
            }
        }), new ApduResponseHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda33
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduResponseHandler
            public final Object handleResult(byte[] bArr2) {
                return EuiccPort.$r8$lambda$WIb2UjiHffDTpSR2jGk0qszb5IM(bArr2);
            }
        }, asyncResultCallback, handler);
    }

    public static /* synthetic */ EuiccProfileInfo[] $r8$lambda$WIb2UjiHffDTpSR2jGk0qszb5IM(byte[] bArr) {
        List children = new Asn1Decoder(bArr).nextNode().getChild(SmsMessage.MAX_USER_DATA_SEPTETS, new int[0]).getChildren(NtnRILConstants.MI_NTN_RIL_REQUEST_SET_USAGE_SETTING);
        int size = children.size();
        EuiccProfileInfo[] euiccProfileInfoArr = new EuiccProfileInfo[size];
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            Asn1Node asn1Node = (Asn1Node) children.get(i2);
            if (!asn1Node.hasChild(90, new int[0])) {
                loge("Profile must have an ICCID.");
            } else {
                EuiccProfileInfo.Builder builder = new EuiccProfileInfo.Builder(stripTrailingFs(asn1Node.getChild(90, new int[0]).asBytes()));
                buildProfile(asn1Node, builder);
                euiccProfileInfoArr[i] = builder.build();
                i++;
            }
        }
        return euiccProfileInfoArr;
    }

    public final void getProfile(final String str, AsyncResultCallback<EuiccProfileInfo> asyncResultCallback, Handler handler) {
        final byte[] bArr = this.mSupportedMepMode.isMepMode() ? Tags.EUICC_PROFILE_MEP_TAGS : Tags.EUICC_PROFILE_TAGS;
        sendApdu(newRequestProvider(new ApduRequestBuilder() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda10
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduRequestBuilder
            public final void build(RequestBuilder requestBuilder) {
                requestBuilder.addStoreData(Asn1Node.newBuilder(48941).addChild(Asn1Node.newBuilder(SmsMessage.MAX_USER_DATA_SEPTETS).addChildAsBytes(90, IccUtils.bcdToBytes(EuiccPort.padTrailingFs(str))).build()).addChildAsBytes(92, bArr).build().toHex());
            }
        }), new ApduResponseHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda11
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduResponseHandler
            public final Object handleResult(byte[] bArr2) {
                return EuiccPort.$r8$lambda$DlR4TDc8AhjnxWZ3d44nUFp_iV0(bArr2);
            }
        }, asyncResultCallback, handler);
    }

    public static /* synthetic */ EuiccProfileInfo $r8$lambda$DlR4TDc8AhjnxWZ3d44nUFp_iV0(byte[] bArr) {
        List children = new Asn1Decoder(bArr).nextNode().getChild(SmsMessage.MAX_USER_DATA_SEPTETS, new int[0]).getChildren(NtnRILConstants.MI_NTN_RIL_REQUEST_SET_USAGE_SETTING);
        if (children.isEmpty()) {
            return null;
        }
        Asn1Node asn1Node = (Asn1Node) children.get(0);
        EuiccProfileInfo.Builder builder = new EuiccProfileInfo.Builder(stripTrailingFs(asn1Node.getChild(90, new int[0]).asBytes()));
        buildProfile(asn1Node, builder);
        return builder.build();
    }

    public void disableProfile(final String str, final boolean z, AsyncResultCallback<Void> asyncResultCallback, Handler handler) {
        sendApduWithSimResetErrorWorkaround(newRequestProvider(new ApduRequestBuilder() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda22
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduRequestBuilder
            public final void build(RequestBuilder requestBuilder) {
                requestBuilder.addStoreData(Asn1Node.newBuilder(48946).addChild(Asn1Node.newBuilder(SmsMessage.MAX_USER_DATA_SEPTETS).addChildAsBytes(90, IccUtils.bcdToBytes(EuiccPort.padTrailingFs(str)))).addChildAsBoolean(129, z).build().toHex());
            }
        }), new ApduResponseHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda23
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduResponseHandler
            public final Object handleResult(byte[] bArr) {
                return EuiccPort.$r8$lambda$uoPKM6SB24EIJBhxTYM6KlB7w3c(str, bArr);
            }
        }, asyncResultCallback, handler);
    }

    public static /* synthetic */ Void $r8$lambda$uoPKM6SB24EIJBhxTYM6KlB7w3c(String str, byte[] bArr) throws EuiccCardErrorException {
        int simpleResult = parseSimpleResult(bArr);
        if (simpleResult == 0) {
            return null;
        }
        if (simpleResult == 2) {
            logd("Profile is already disabled, iccid: " + SubscriptionInfo.getPrintableId(str));
            return null;
        }
        throw new EuiccCardErrorException(11, simpleResult);
    }

    public void switchToProfile(final String str, final boolean z, AsyncResultCallback<Void> asyncResultCallback, Handler handler) {
        sendApduWithSimResetErrorWorkaround(newRequestProvider(new ApduRequestBuilder() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda34
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduRequestBuilder
            public final void build(RequestBuilder requestBuilder) {
                this.f$0.lambda$switchToProfile$8(str, z, requestBuilder);
            }
        }), new ApduResponseHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda35
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduResponseHandler
            public final Object handleResult(byte[] bArr) {
                return EuiccPort.$r8$lambda$1ATmEr4p4fYk69DuGgUniIUrPfE(str, bArr);
            }
        }, asyncResultCallback, handler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$switchToProfile$8(String str, boolean z, RequestBuilder requestBuilder) {
        Asn1Node.Builder builderAddChildAsBoolean = Asn1Node.newBuilder(48945).addChild(Asn1Node.newBuilder(SmsMessage.MAX_USER_DATA_SEPTETS).addChildAsBytes(90, IccUtils.bcdToBytes(padTrailingFs(str)))).addChildAsBoolean(129, z);
        if (this.mSupportedMepMode.isMepA1Mode()) {
            builderAddChildAsBoolean.addChildAsInteger(NtnRILConstants.MI_NTN_RIL_REQUEST_GET_RADIO_CAPABILITY, PortUtils.convertToHalPortIndex(this.mSupportedMepMode, super.getPortIdx()));
        }
        requestBuilder.addStoreData(builderAddChildAsBoolean.build().toHex());
    }

    public static /* synthetic */ Void $r8$lambda$1ATmEr4p4fYk69DuGgUniIUrPfE(String str, byte[] bArr) throws EuiccCardErrorException {
        int simpleResult = parseSimpleResult(bArr);
        if (simpleResult == 0) {
            return null;
        }
        if (simpleResult == 2) {
            logd("Profile is already enabled, iccid: " + SubscriptionInfo.getPrintableId(str));
            return null;
        }
        throw new EuiccCardErrorException(10, simpleResult);
    }

    public String getEid() {
        return this.mEid;
    }

    public void getEid(AsyncResultCallback<String> asyncResultCallback, Handler handler) {
        if (this.mEid != null) {
            AsyncResultHelper.returnResult(this.mEid, asyncResultCallback, handler);
        } else {
            sendApdu(newRequestProvider(new ApduRequestBuilder() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda41
                @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduRequestBuilder
                public final void build(RequestBuilder requestBuilder) {
                    requestBuilder.addStoreData(Asn1Node.newBuilder(48958).addChildAsBytes(92, new byte[]{90}).build().toHex());
                }
            }), new ApduResponseHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda42
                @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduResponseHandler
                public final Object handleResult(byte[] bArr) {
                    return this.f$0.lambda$getEid$11(bArr);
                }
            }, asyncResultCallback, handler);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$getEid$11(byte[] bArr) {
        String strBytesToHexString = IccUtils.bytesToHexString(parseResponse(bArr).getChild(90, new int[0]).asBytes());
        synchronized (this.mLock) {
            this.mEid = strBytesToHexString;
            this.mCardId = strBytesToHexString;
        }
        return strBytesToHexString;
    }

    public void setNickname(final String str, final String str2, AsyncResultCallback<Void> asyncResultCallback, Handler handler) {
        sendApdu(newRequestProvider(new ApduRequestBuilder() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda18
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduRequestBuilder
            public final void build(RequestBuilder requestBuilder) {
                requestBuilder.addStoreData(Asn1Node.newBuilder(48937).addChildAsBytes(90, IccUtils.bcdToBytes(EuiccPort.padTrailingFs(str))).addChildAsString(NtnRILConstants.MI_NTN_RIL_REQUEST_START_KEEPALIVE, str2).build().toHex());
            }
        }), new ApduResponseHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda19
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduResponseHandler
            public final Object handleResult(byte[] bArr) {
                return EuiccPort.$r8$lambda$odndDAScngwf6BOQennSmaEdZ4Y(bArr);
            }
        }, asyncResultCallback, handler);
    }

    public static /* synthetic */ Void $r8$lambda$odndDAScngwf6BOQennSmaEdZ4Y(byte[] bArr) throws EuiccCardErrorException {
        int simpleResult = parseSimpleResult(bArr);
        if (simpleResult == 0) {
            return null;
        }
        throw new EuiccCardErrorException(7, simpleResult);
    }

    public void deleteProfile(final String str, AsyncResultCallback<Void> asyncResultCallback, Handler handler) {
        sendApdu(newRequestProvider(new ApduRequestBuilder() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda12
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduRequestBuilder
            public final void build(RequestBuilder requestBuilder) {
                requestBuilder.addStoreData(Asn1Node.newBuilder(48947).addChildAsBytes(90, IccUtils.bcdToBytes(EuiccPort.padTrailingFs(str))).build().toHex());
            }
        }), new ApduResponseHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda13
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduResponseHandler
            public final Object handleResult(byte[] bArr) {
                return EuiccPort.$r8$lambda$H25WdLLsot1rl7uLjnL13Y6rUDs(bArr);
            }
        }, asyncResultCallback, handler);
    }

    public static /* synthetic */ Void $r8$lambda$H25WdLLsot1rl7uLjnL13Y6rUDs(byte[] bArr) throws EuiccCardErrorException {
        int simpleResult = parseSimpleResult(bArr);
        if (simpleResult == 0) {
            return null;
        }
        throw new EuiccCardErrorException(12, simpleResult);
    }

    public void resetMemory(final int i, AsyncResultCallback<Void> asyncResultCallback, Handler handler) {
        sendApduWithSimResetErrorWorkaround(newRequestProvider(new ApduRequestBuilder() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda14
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduRequestBuilder
            public final void build(RequestBuilder requestBuilder) {
                requestBuilder.addStoreData(Asn1Node.newBuilder(48948).addChildAsBits(NtnRILConstants.MI_NTN_RIL_REQUEST_GET_RADIO_CAPABILITY, i).build().toHex());
            }
        }), new ApduResponseHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda15
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduResponseHandler
            public final Object handleResult(byte[] bArr) {
                return EuiccPort.$r8$lambda$MU5mRlnNwKVTR_GQggVRY_nh50M(bArr);
            }
        }, asyncResultCallback, handler);
    }

    public static /* synthetic */ Void $r8$lambda$MU5mRlnNwKVTR_GQggVRY_nh50M(byte[] bArr) throws EuiccCardErrorException {
        int simpleResult = parseSimpleResult(bArr);
        if (simpleResult == 0 || simpleResult == 1) {
            return null;
        }
        throw new EuiccCardErrorException(13, simpleResult);
    }

    public void getDefaultSmdpAddress(AsyncResultCallback<String> asyncResultCallback, Handler handler) {
        sendApdu(newRequestProvider(new ApduRequestBuilder() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda45
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduRequestBuilder
            public final void build(RequestBuilder requestBuilder) {
                requestBuilder.addStoreData(Asn1Node.newBuilder(48956).build().toHex());
            }
        }), new ApduResponseHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda46
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduResponseHandler
            public final Object handleResult(byte[] bArr) {
                return EuiccPort.parseResponse(bArr).getChild(128, new int[0]).asString();
            }
        }, asyncResultCallback, handler);
    }

    public void getSmdsAddress(AsyncResultCallback<String> asyncResultCallback, Handler handler) {
        sendApdu(newRequestProvider(new ApduRequestBuilder() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda6
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduRequestBuilder
            public final void build(RequestBuilder requestBuilder) {
                requestBuilder.addStoreData(Asn1Node.newBuilder(48956).build().toHex());
            }
        }), new ApduResponseHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda7
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduResponseHandler
            public final Object handleResult(byte[] bArr) {
                return EuiccPort.parseResponse(bArr).getChild(129, new int[0]).asString();
            }
        }, asyncResultCallback, handler);
    }

    public void setDefaultSmdpAddress(final String str, AsyncResultCallback<Void> asyncResultCallback, Handler handler) {
        sendApdu(newRequestProvider(new ApduRequestBuilder() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda24
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduRequestBuilder
            public final void build(RequestBuilder requestBuilder) {
                requestBuilder.addStoreData(Asn1Node.newBuilder(48959).addChildAsString(128, str).build().toHex());
            }
        }), new ApduResponseHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda25
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduResponseHandler
            public final Object handleResult(byte[] bArr) {
                return EuiccPort.$r8$lambda$y7AIHaUC6EMVHjHTQtoP3XZCSHQ(bArr);
            }
        }, asyncResultCallback, handler);
    }

    public static /* synthetic */ Void $r8$lambda$y7AIHaUC6EMVHjHTQtoP3XZCSHQ(byte[] bArr) throws EuiccCardErrorException {
        int simpleResult = parseSimpleResult(bArr);
        if (simpleResult == 0) {
            return null;
        }
        throw new EuiccCardErrorException(14, simpleResult);
    }

    public void getRulesAuthTable(AsyncResultCallback<EuiccRulesAuthTable> asyncResultCallback, Handler handler) {
        sendApdu(newRequestProvider(new ApduRequestBuilder() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda36
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduRequestBuilder
            public final void build(RequestBuilder requestBuilder) {
                requestBuilder.addStoreData(Asn1Node.newBuilder(48963).build().toHex());
            }
        }), new ApduResponseHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda37
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduResponseHandler
            public final Object handleResult(byte[] bArr) {
                return EuiccPort.m2022$r8$lambda$wgrjBH9HHe7gvJSuBtiuKOXfQ4(bArr);
            }
        }, asyncResultCallback, handler);
    }

    /* JADX INFO: renamed from: $r8$lambda$wgrjBH9HHe7gvJSu-BtiuKOXfQ4, reason: not valid java name */
    public static /* synthetic */ EuiccRulesAuthTable m2022$r8$lambda$wgrjBH9HHe7gvJSuBtiuKOXfQ4(byte[] bArr) {
        List children = parseResponse(bArr).getChildren(SmsMessage.MAX_USER_DATA_SEPTETS);
        EuiccRulesAuthTable.Builder builder = new EuiccRulesAuthTable.Builder(children.size());
        int size = children.size();
        for (int i = 0; i < size; i++) {
            Asn1Node asn1Node = (Asn1Node) children.get(i);
            List children2 = asn1Node.getChild(48, new int[]{161}).getChildren();
            int size2 = children2.size();
            CarrierIdentifier[] carrierIdentifierArr = new CarrierIdentifier[size2];
            for (int i2 = 0; i2 < size2; i2++) {
                carrierIdentifierArr[i2] = buildCarrierIdentifier((Asn1Node) children2.get(i2));
            }
            builder.add(asn1Node.getChild(48, new int[]{128}).asBits(), Arrays.asList(carrierIdentifierArr), asn1Node.getChild(48, new int[]{NtnRILConstants.MI_NTN_RIL_REQUEST_GET_RADIO_CAPABILITY}).asBits());
        }
        return builder.build();
    }

    public void getEuiccChallenge(AsyncResultCallback<byte[]> asyncResultCallback, Handler handler) {
        sendApdu(newRequestProvider(new ApduRequestBuilder() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda4
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduRequestBuilder
            public final void build(RequestBuilder requestBuilder) {
                requestBuilder.addStoreData(Asn1Node.newBuilder(48942).build().toHex());
            }
        }), new ApduResponseHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda5
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduResponseHandler
            public final Object handleResult(byte[] bArr) {
                return EuiccPort.parseResponse(bArr).getChild(128, new int[0]).asBytes();
            }
        }, asyncResultCallback, handler);
    }

    public void getEuiccInfo1(AsyncResultCallback<byte[]> asyncResultCallback, Handler handler) {
        sendApdu(newRequestProvider(new ApduRequestBuilder() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda20
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduRequestBuilder
            public final void build(RequestBuilder requestBuilder) {
                requestBuilder.addStoreData(Asn1Node.newBuilder(48928).build().toHex());
            }
        }), new ApduResponseHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda21
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduResponseHandler
            public final Object handleResult(byte[] bArr) {
                return EuiccPort.$r8$lambda$cX1IMnQ1WDgUWeH0JuepU0bkF1Q(bArr);
            }
        }, asyncResultCallback, handler);
    }

    public void getEuiccInfo2(AsyncResultCallback<byte[]> asyncResultCallback, Handler handler) {
        sendApdu(newRequestProvider(new ApduRequestBuilder() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda8
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduRequestBuilder
            public final void build(RequestBuilder requestBuilder) {
                requestBuilder.addStoreData(Asn1Node.newBuilder(48930).build().toHex());
            }
        }), new ApduResponseHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda9
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduResponseHandler
            public final Object handleResult(byte[] bArr) {
                return EuiccPort.m2024$r8$lambda$zAmf1sNlPuq7i_7B1dM9SNX1hc(bArr);
            }
        }, asyncResultCallback, handler);
    }

    public void authenticateServer(final String str, final byte[] bArr, final byte[] bArr2, final byte[] bArr3, final byte[] bArr4, AsyncResultCallback<byte[]> asyncResultCallback, Handler handler) {
        sendApdu(newRequestProvider(new ApduRequestBuilder() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda30
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduRequestBuilder
            public final void build(RequestBuilder requestBuilder) {
                this.f$0.lambda$authenticateServer$32(str, bArr, bArr2, bArr3, bArr4, requestBuilder);
            }
        }), new ApduResponseHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda31
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduResponseHandler
            public final Object handleResult(byte[] bArr5) {
                return EuiccPort.$r8$lambda$QWCG1I_mrqtBU50tBLlLwcWohfc(bArr5);
            }
        }, asyncResultCallback, handler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$authenticateServer$32(String str, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, RequestBuilder requestBuilder) {
        byte[] deviceId = getDeviceId();
        byte[] bArr5 = new byte[4];
        System.arraycopy(deviceId, 0, bArr5, 0, 4);
        Asn1Node.Builder builderNewBuilder = Asn1Node.newBuilder(161);
        String[] stringArray = getResources().getStringArray(R.array.policy_exempt_apps);
        if (stringArray != null) {
            for (String str2 : stringArray) {
                addDeviceCapability(builderNewBuilder, str2);
            }
        } else {
            logd("No device capabilities set.");
        }
        requestBuilder.addStoreData(Asn1Node.newBuilder(48952).addChild(new Asn1Decoder(bArr).nextNode()).addChild(new Asn1Decoder(bArr2).nextNode()).addChild(new Asn1Decoder(bArr3).nextNode()).addChild(new Asn1Decoder(bArr4).nextNode()).addChild(Asn1Node.newBuilder(SmsMessage.MAX_USER_DATA_SEPTETS).addChildAsString(128, str).addChild(Asn1Node.newBuilder(161).addChildAsBytes(128, bArr5).addChild(builderNewBuilder).addChildAsBytes(NtnRILConstants.MI_NTN_RIL_REQUEST_GET_RADIO_CAPABILITY, deviceId))).build().toHex());
    }

    public static /* synthetic */ byte[] $r8$lambda$QWCG1I_mrqtBU50tBLlLwcWohfc(byte[] bArr) throws EuiccCardException {
        Asn1Node response = parseResponse(bArr);
        if (response.hasChild(161, new int[]{2})) {
            throw new EuiccCardErrorException(3, response.getChild(161, new int[]{2}).asInteger());
        }
        return response.toBytes();
    }

    public void prepareDownload(final byte[] bArr, final byte[] bArr2, final byte[] bArr3, final byte[] bArr4, AsyncResultCallback<byte[]> asyncResultCallback, Handler handler) {
        sendApdu(newRequestProvider(new ApduRequestBuilder() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda26
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduRequestBuilder
            public final void build(RequestBuilder requestBuilder) {
                EuiccPort.$r8$lambda$usba0HvwjnmbVsZr6jMHNFVhKxY(bArr2, bArr3, bArr, bArr4, requestBuilder);
            }
        }), new ApduResponseHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda27
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduResponseHandler
            public final Object handleResult(byte[] bArr5) {
                return EuiccPort.$r8$lambda$o6Us1m3vQqBmaX367z5l_68zsGM(bArr5);
            }
        }, asyncResultCallback, handler);
    }

    public static /* synthetic */ void $r8$lambda$usba0HvwjnmbVsZr6jMHNFVhKxY(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, RequestBuilder requestBuilder) {
        Asn1Node.Builder builderAddChild = Asn1Node.newBuilder(48929).addChild(new Asn1Decoder(bArr).nextNode()).addChild(new Asn1Decoder(bArr2).nextNode());
        if (bArr3 != null) {
            builderAddChild.addChildAsBytes(4, bArr3);
        }
        requestBuilder.addStoreData(builderAddChild.addChild(new Asn1Decoder(bArr4).nextNode()).build().toHex());
    }

    public static /* synthetic */ byte[] $r8$lambda$o6Us1m3vQqBmaX367z5l_68zsGM(byte[] bArr) throws EuiccCardException {
        Asn1Node response = parseResponse(bArr);
        if (response.hasChild(161, new int[]{2})) {
            throw new EuiccCardErrorException(2, response.getChild(161, new int[]{2}).asInteger());
        }
        return response.toBytes();
    }

    public void loadBoundProfilePackage(final byte[] bArr, AsyncResultCallback<byte[]> asyncResultCallback, Handler handler) {
        sendApdu(newRequestProvider(new ApduRequestBuilder() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda38
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduRequestBuilder
            public final void build(RequestBuilder requestBuilder) throws EuiccCardException {
                this.f$0.lambda$loadBoundProfilePackage$36(bArr, requestBuilder);
            }
        }), new ApduResponseHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda39
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduResponseHandler
            public final Object handleResult(byte[] bArr2) {
                return EuiccPort.m2021$r8$lambda$uc4emHmiPKsuuqsx2rPC3nldg4(bArr2);
            }
        }, new ApduIntermediateResultHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda40
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduIntermediateResultHandler
            public final boolean shouldContinue(IccIoResult iccIoResult) {
                return EuiccPort.$r8$lambda$Wq1gvyXM7LhC7TZbCMXr_kujSzY(iccIoResult);
            }
        }, asyncResultCallback, handler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadBoundProfilePackage$36(byte[] bArr, RequestBuilder requestBuilder) throws EuiccCardException {
        Asn1Node child;
        Asn1Node asn1NodeNextNode = new Asn1Decoder(bArr).nextNode();
        int dataLength = asn1NodeNextNode.getDataLength();
        Asn1Node child2 = asn1NodeNextNode.getChild(48931, new int[0]);
        int encodedLength = child2.getEncodedLength();
        Asn1Node child3 = asn1NodeNextNode.getChild(SmsMessage.MAX_USER_DATA_SEPTETS, new int[0]);
        int encodedLength2 = encodedLength + child3.getEncodedLength();
        Asn1Node child4 = asn1NodeNextNode.getChild(161, new int[0]);
        List children = child4.getChildren(136);
        int encodedLength3 = encodedLength2 + child4.getEncodedLength();
        if (asn1NodeNextNode.hasChild(NtnRILConstants.MI_NTN_RIL_REQUEST_SEND_AT_COMMAND_FOR_TEST, new int[0])) {
            child = asn1NodeNextNode.getChild(NtnRILConstants.MI_NTN_RIL_REQUEST_SEND_AT_COMMAND_FOR_TEST, new int[0]);
            encodedLength3 += child.getEncodedLength();
        } else {
            child = null;
        }
        Asn1Node child5 = asn1NodeNextNode.getChild(NtnRILConstants.MI_NTN_RIL_REQUEST_SEND_VOICE_CTRL_VERSION, new int[0]);
        List children2 = child5.getChildren(134);
        int encodedLength4 = encodedLength3 + child5.getEncodedLength();
        if (this.mSpecVersion.compareTo(SGP22_V_2_1) >= 0) {
            if (children2 == null || children2.isEmpty()) {
                throw new EuiccCardException("No profile elements in BPP");
            }
            if (dataLength != encodedLength4) {
                throw new EuiccCardException("Actual BPP length (" + dataLength + ") does not match segmented length (" + encodedLength4 + "), this must be due to a malformed BPP");
            }
        }
        requestBuilder.addStoreData(asn1NodeNextNode.getHeadAsHex() + child2.toHex());
        requestBuilder.addStoreData(child3.toHex());
        requestBuilder.addStoreData(child4.getHeadAsHex());
        int size = children.size();
        for (int i = 0; i < size; i++) {
            requestBuilder.addStoreData(((Asn1Node) children.get(i)).toHex());
        }
        if (child != null) {
            requestBuilder.addStoreData(child.toHex());
        }
        requestBuilder.addStoreData(child5.getHeadAsHex());
        int size2 = children2.size();
        for (int i2 = 0; i2 < size2; i2++) {
            requestBuilder.addStoreData(((Asn1Node) children2.get(i2)).toHex());
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$uc4emHmiPKsuuqsx2rPC3n-ldg4, reason: not valid java name */
    public static /* synthetic */ byte[] m2021$r8$lambda$uc4emHmiPKsuuqsx2rPC3nldg4(byte[] bArr) throws EuiccCardException {
        Asn1Node response = parseResponse(bArr);
        if (response.hasChild(48935, new int[]{NtnRILConstants.MI_NTN_RIL_REQUEST_SEND_AT_COMMAND_FOR_TEST, 161, 129})) {
            Asn1Node child = response.getChild(48935, new int[]{NtnRILConstants.MI_NTN_RIL_REQUEST_SEND_AT_COMMAND_FOR_TEST, 161, 129});
            throw new EuiccCardErrorException(5, child.asInteger(), child);
        }
        return response.toBytes();
    }

    public static /* synthetic */ boolean $r8$lambda$Wq1gvyXM7LhC7TZbCMXr_kujSzY(IccIoResult iccIoResult) {
        byte[] bArr = iccIoResult.payload;
        if (bArr != null && bArr.length > 2) {
            if (((bArr[1] & 255) | ((bArr[0] & 255) << 8)) == 48951) {
                logd("loadBoundProfilePackage failed due to an early error.");
                return false;
            }
        }
        return true;
    }

    public void cancelSession(final byte[] bArr, final int i, AsyncResultCallback<byte[]> asyncResultCallback, Handler handler) {
        sendApdu(newRequestProvider(new ApduRequestBuilder() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda47
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduRequestBuilder
            public final void build(RequestBuilder requestBuilder) {
                requestBuilder.addStoreData(Asn1Node.newBuilder(48961).addChildAsBytes(128, bArr).addChildAsInteger(129, i).build().toHex());
            }
        }), new ApduResponseHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda48
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduResponseHandler
            public final Object handleResult(byte[] bArr2) {
                return EuiccPort.parseResponseAndCheckSimpleError(bArr2, 4).toBytes();
            }
        }, asyncResultCallback, handler);
    }

    public void listNotifications(final int i, AsyncResultCallback<EuiccNotification[]> asyncResultCallback, Handler handler) {
        sendApdu(newRequestProvider(new ApduRequestBuilder() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda28
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduRequestBuilder
            public final void build(RequestBuilder requestBuilder) {
                requestBuilder.addStoreData(Asn1Node.newBuilder(48936).addChildAsBits(129, i).build().toHex());
            }
        }), new ApduResponseHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda29
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduResponseHandler
            public final Object handleResult(byte[] bArr) {
                return EuiccPort.m2011$r8$lambda$B4NPltzgzvXPGqsH6_q4pINr0(bArr);
            }
        }, asyncResultCallback, handler);
    }

    /* JADX INFO: renamed from: $r8$lambda$B4NPl-tzgzvXPGq-sH6_q4pINr0, reason: not valid java name */
    public static /* synthetic */ EuiccNotification[] m2011$r8$lambda$B4NPltzgzvXPGqsH6_q4pINr0(byte[] bArr) {
        List children = parseResponseAndCheckSimpleError(bArr, 6).getChild(SmsMessage.MAX_USER_DATA_SEPTETS, new int[0]).getChildren();
        int size = children.size();
        EuiccNotification[] euiccNotificationArr = new EuiccNotification[size];
        for (int i = 0; i < size; i++) {
            euiccNotificationArr[i] = createNotification((Asn1Node) children.get(i));
        }
        return euiccNotificationArr;
    }

    public void retrieveNotificationList(final int i, AsyncResultCallback<EuiccNotification[]> asyncResultCallback, Handler handler) {
        sendApdu(newRequestProvider(new ApduRequestBuilder() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda2
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduRequestBuilder
            public final void build(RequestBuilder requestBuilder) {
                requestBuilder.addStoreData(Asn1Node.newBuilder(48939).addChild(Asn1Node.newBuilder(SmsMessage.MAX_USER_DATA_SEPTETS).addChildAsBits(129, i)).build().toHex());
            }
        }), new ApduResponseHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda3
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduResponseHandler
            public final Object handleResult(byte[] bArr) {
                return EuiccPort.m2018$r8$lambda$afNLL9rQqoO4vmChvNCa9p114w(bArr);
            }
        }, asyncResultCallback, handler);
    }

    /* JADX INFO: renamed from: $r8$lambda$afNLL9rQqoO4vmChvNCa9p114-w, reason: not valid java name */
    public static /* synthetic */ EuiccNotification[] m2018$r8$lambda$afNLL9rQqoO4vmChvNCa9p114w(byte[] bArr) throws EuiccCardException {
        Asn1Node response = parseResponse(bArr);
        if (response.hasChild(129, new int[0])) {
            int iAsInteger = response.getChild(129, new int[0]).asInteger();
            if (iAsInteger == 1) {
                return new EuiccNotification[0];
            }
            throw new EuiccCardErrorException(8, iAsInteger);
        }
        List children = response.getChild(SmsMessage.MAX_USER_DATA_SEPTETS, new int[0]).getChildren();
        int size = children.size();
        EuiccNotification[] euiccNotificationArr = new EuiccNotification[size];
        for (int i = 0; i < size; i++) {
            euiccNotificationArr[i] = createNotification((Asn1Node) children.get(i));
        }
        return euiccNotificationArr;
    }

    public void retrieveNotification(final int i, AsyncResultCallback<EuiccNotification> asyncResultCallback, Handler handler) {
        sendApdu(newRequestProvider(new ApduRequestBuilder() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda0
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduRequestBuilder
            public final void build(RequestBuilder requestBuilder) {
                requestBuilder.addStoreData(Asn1Node.newBuilder(48939).addChild(Asn1Node.newBuilder(SmsMessage.MAX_USER_DATA_SEPTETS).addChildAsInteger(128, i)).build().toHex());
            }
        }), new ApduResponseHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda1
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduResponseHandler
            public final Object handleResult(byte[] bArr) {
                return EuiccPort.$r8$lambda$9nRwiOGlqVjHuqkCTqdflA1V9J4(bArr);
            }
        }, asyncResultCallback, handler);
    }

    public static /* synthetic */ EuiccNotification $r8$lambda$9nRwiOGlqVjHuqkCTqdflA1V9J4(byte[] bArr) {
        List children = parseResponseAndCheckSimpleError(bArr, 8).getChild(SmsMessage.MAX_USER_DATA_SEPTETS, new int[0]).getChildren();
        if (children.size() > 0) {
            return createNotification((Asn1Node) children.get(0));
        }
        return null;
    }

    public void removeNotificationFromList(final int i, AsyncResultCallback<Void> asyncResultCallback, Handler handler) {
        sendApdu(newRequestProvider(new ApduRequestBuilder() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda16
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduRequestBuilder
            public final void build(RequestBuilder requestBuilder) {
                requestBuilder.addStoreData(Asn1Node.newBuilder(48944).addChildAsInteger(128, i).build().toHex());
            }
        }), new ApduResponseHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda17
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduResponseHandler
            public final Object handleResult(byte[] bArr) {
                return EuiccPort.$r8$lambda$pu_eTrNwCXGgwO8CrL09b4K8UoA(bArr);
            }
        }, asyncResultCallback, handler);
    }

    public static /* synthetic */ Void $r8$lambda$pu_eTrNwCXGgwO8CrL09b4K8UoA(byte[] bArr) throws EuiccCardErrorException {
        int simpleResult = parseSimpleResult(bArr);
        if (simpleResult == 0 || simpleResult == 1) {
            return null;
        }
        throw new EuiccCardErrorException(9, simpleResult);
    }

    public void addDeviceCapability(Asn1Node.Builder builder, String str) {
        String[] strArrSplit = str.split(",");
        if (strArrSplit.length != 2) {
            loge("Invalid device capability item: " + Arrays.toString(strArrSplit));
        }
        String strTrim = strArrSplit[0].trim();
        String[] strArrSplit2 = strArrSplit[1].trim().split("\\.");
        try {
            byte[] bArr = {Integer.valueOf(Integer.parseInt(strArrSplit2[0])).byteValue(), (strArrSplit2.length > 1 ? Integer.valueOf(Integer.parseInt(strArrSplit2[1])) : 0).byteValue(), 0};
            strTrim.hashCode();
            switch (strTrim) {
                case "cdma1x":
                    builder.addChildAsBytes(NtnRILConstants.MI_NTN_RIL_REQUEST_GET_RADIO_CAPABILITY, bArr);
                    break;
                case "eutran":
                    builder.addChildAsBytes(133, bArr);
                    break;
                case "eutran5gc":
                    builder.addChildAsBytes(NtnRILConstants.MI_NTN_RIL_REQUEST_SEND_DEVICE_STATE, bArr);
                    break;
                case "crl":
                    builder.addChildAsBytes(135, bArr);
                    break;
                case "gsm":
                    builder.addChildAsBytes(128, bArr);
                    break;
                case "nfc":
                    builder.addChildAsBytes(134, bArr);
                    break;
                case "hrpd":
                    builder.addChildAsBytes(NtnRILConstants.MI_NTN_RIL_REQUEST_SET_RADIO_CAPABILITY, bArr);
                    break;
                case "ehrpd":
                    builder.addChildAsBytes(132, bArr);
                    break;
                case "nr5gc":
                    builder.addChildAsBytes(NtnRILConstants.MI_NTN_RIL_REQUEST_GET_ALLOWED_CARRIERS, bArr);
                    break;
                case "nrepc":
                    builder.addChildAsBytes(136, bArr);
                    break;
                case "utran":
                    builder.addChildAsBytes(129, bArr);
                    break;
                default:
                    loge("Invalid device capability name: ".concat(strTrim));
                    break;
            }
        } catch (NumberFormatException e) {
            loge("Invalid device capability version number.", e);
        }
    }

    protected byte[] getDeviceId() {
        Phone phone = PhoneFactory.getPhone(getPhoneId());
        if (phone == null) {
            return new byte[8];
        }
        return getDeviceId(phone.getDeviceId(), this.mSpecVersion);
    }

    public static byte[] getDeviceId(String str, EuiccSpecVersion euiccSpecVersion) {
        byte[] bArr = new byte[8];
        if (euiccSpecVersion.compareTo(SGP22_V_2_1) >= 0) {
            IccUtils.bcdToBytes(str + 'F', bArr);
            int i = bArr[7] & 255;
            bArr[7] = (byte) ((i >>> 4) | (i << 4));
            return bArr;
        }
        IccUtils.bcdToBytes(str, bArr);
        return bArr;
    }

    protected Resources getResources() {
        return Resources.getSystem();
    }

    private RequestProvider newRequestProvider(final ApduRequestBuilder apduRequestBuilder) {
        return new RequestProvider() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda50
            /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.uicc.asn1.InvalidAsn1DataException */
            /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.uicc.asn1.TagNotFoundException */
            @Override // com.android.internal.telephony.uicc.euicc.apdu.RequestProvider
            public final void buildRequest(byte[] bArr, RequestBuilder requestBuilder) throws TagNotFoundException, InvalidAsn1DataException, EuiccCardException {
                this.f$0.lambda$newRequestProvider$49(apduRequestBuilder, bArr, requestBuilder);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.uicc.asn1.InvalidAsn1DataException */
    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.uicc.asn1.TagNotFoundException */
    public /* synthetic */ void lambda$newRequestProvider$49(ApduRequestBuilder apduRequestBuilder, byte[] bArr, RequestBuilder requestBuilder) throws TagNotFoundException, InvalidAsn1DataException, EuiccCardException {
        EuiccSpecVersion orExtractSpecVersion = getOrExtractSpecVersion(bArr);
        if (orExtractSpecVersion == null) {
            throw new EuiccCardException("Cannot get eUICC spec version.");
        }
        try {
            if (orExtractSpecVersion.compareTo(SGP22_V_2_0) < 0) {
                throw new EuiccCardException("eUICC spec version is unsupported: " + orExtractSpecVersion);
            }
            apduRequestBuilder.build(requestBuilder);
        } catch (InvalidAsn1DataException | TagNotFoundException e) {
            throw new EuiccCardException("Cannot parse ASN1 to build request.", e);
        }
    }

    private EuiccSpecVersion getOrExtractSpecVersion(byte[] bArr) {
        EuiccSpecVersion euiccSpecVersion = this.mSpecVersion;
        if (euiccSpecVersion != null) {
            return euiccSpecVersion;
        }
        EuiccSpecVersion euiccSpecVersionFromOpenChannelResponse = EuiccSpecVersion.fromOpenChannelResponse(bArr);
        if (euiccSpecVersionFromOpenChannelResponse == null) {
            return euiccSpecVersionFromOpenChannelResponse;
        }
        synchronized (this.mLock) {
            try {
                if (this.mSpecVersion == null) {
                    this.mSpecVersion = euiccSpecVersionFromOpenChannelResponse;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return euiccSpecVersionFromOpenChannelResponse;
    }

    private void sendApdu(RequestProvider requestProvider, ApduResponseHandler apduResponseHandler, final AsyncResultCallback asyncResultCallback, Handler handler) {
        sendApdu(requestProvider, apduResponseHandler, new ApduExceptionHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda52
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduExceptionHandler
            public final void handleException(Throwable th) {
                asyncResultCallback.onException(new EuiccCardException("Cannot send APDU.", th));
            }
        }, null, asyncResultCallback, handler);
    }

    private void sendApdu(RequestProvider requestProvider, ApduResponseHandler apduResponseHandler, ApduIntermediateResultHandler apduIntermediateResultHandler, final AsyncResultCallback asyncResultCallback, Handler handler) {
        sendApdu(requestProvider, apduResponseHandler, new ApduExceptionHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda51
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduExceptionHandler
            public final void handleException(Throwable th) {
                asyncResultCallback.onException(new EuiccCardException("Cannot send APDU.", th));
            }
        }, apduIntermediateResultHandler, asyncResultCallback, handler);
    }

    private void sendApduWithSimResetErrorWorkaround(RequestProvider requestProvider, ApduResponseHandler apduResponseHandler, final AsyncResultCallback asyncResultCallback, Handler handler) {
        sendApdu(requestProvider, apduResponseHandler, new ApduExceptionHandler() { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort$$ExternalSyntheticLambda49
            @Override // com.android.internal.telephony.uicc.euicc.EuiccPort.ApduExceptionHandler
            public final void handleException(Throwable th) {
                EuiccPort.m2012$r8$lambda$EGQloVE5ZJwzQr1fb0lrYE5gfE(asyncResultCallback, th);
            }
        }, null, asyncResultCallback, handler);
    }

    /* JADX INFO: renamed from: $r8$lambda$EGQloVE5ZJw-zQr1fb0lrYE5gfE, reason: not valid java name */
    public static /* synthetic */ void m2012$r8$lambda$EGQloVE5ZJwzQr1fb0lrYE5gfE(AsyncResultCallback asyncResultCallback, Throwable th) {
        if ((th instanceof ApduException) && ((ApduException) th).getApduStatus() == 28416) {
            logi("Sim is refreshed after disabling profile, no response got.");
            asyncResultCallback.onResult(null);
        } else {
            asyncResultCallback.onException(new EuiccCardException("Cannot send APDU.", th));
        }
    }

    private void sendApdu(RequestProvider requestProvider, final ApduResponseHandler apduResponseHandler, final ApduExceptionHandler apduExceptionHandler, final ApduIntermediateResultHandler apduIntermediateResultHandler, final AsyncResultCallback asyncResultCallback, Handler handler) {
        this.mApduSender.send(requestProvider, new ApduSenderResultCallback(this) { // from class: com.android.internal.telephony.uicc.euicc.EuiccPort.1
            @Override // com.android.internal.telephony.uicc.euicc.async.AsyncResultCallback
            public void onResult(byte[] bArr) {
                try {
                    asyncResultCallback.onResult(apduResponseHandler.handleResult(bArr));
                } catch (InvalidAsn1DataException | TagNotFoundException e) {
                    asyncResultCallback.onException(new EuiccCardException("Cannot parse response: " + IccUtils.bytesToHexString(bArr), e));
                } catch (EuiccCardException e2) {
                    asyncResultCallback.onException(e2);
                }
            }

            @Override // com.android.internal.telephony.uicc.euicc.apdu.ApduSenderResultCallback
            public boolean shouldContinueOnIntermediateResult(IccIoResult iccIoResult) {
                ApduIntermediateResultHandler apduIntermediateResultHandler2 = apduIntermediateResultHandler;
                if (apduIntermediateResultHandler2 == null) {
                    return true;
                }
                return apduIntermediateResultHandler2.shouldContinue(iccIoResult);
            }

            @Override // com.android.internal.telephony.uicc.euicc.async.AsyncResultCallback
            public void onException(Throwable th) {
                apduExceptionHandler.handleException(th);
            }
        }, handler);
    }

    private static void buildProfile(Asn1Node asn1Node, EuiccProfileInfo.Builder builder) {
        if (asn1Node.hasChild(NtnRILConstants.MI_NTN_RIL_REQUEST_START_KEEPALIVE, new int[0])) {
            builder.setNickname(asn1Node.getChild(NtnRILConstants.MI_NTN_RIL_REQUEST_START_KEEPALIVE, new int[0]).asString());
        }
        if (asn1Node.hasChild(NtnRILConstants.MI_NTN_RIL_REQUEST_STOP_KEEPALIVE, new int[0])) {
            builder.setServiceProviderName(asn1Node.getChild(NtnRILConstants.MI_NTN_RIL_REQUEST_STOP_KEEPALIVE, new int[0]).asString());
        }
        if (asn1Node.hasChild(NtnRILConstants.MI_NTN_RIL_REQUEST_GET_ICCID, new int[0])) {
            builder.setProfileName(asn1Node.getChild(NtnRILConstants.MI_NTN_RIL_REQUEST_GET_ICCID, new int[0]).asString());
        }
        if (asn1Node.hasChild(183, new int[0])) {
            builder.setCarrierIdentifier(buildCarrierIdentifier(asn1Node.getChild(183, new int[0])));
        }
        if (asn1Node.hasChild(40816, new int[0])) {
            if (asn1Node.hasChild(40740, new int[0]) && asn1Node.getChild(40740, new int[0]).asInteger() >= 0) {
                builder.setState(1);
            } else {
                builder.setState(asn1Node.getChild(40816, new int[0]).asInteger());
            }
        } else {
            builder.setState(0);
        }
        if (asn1Node.hasChild(NtnRILConstants.MI_NTN_RIL_REQUEST_SUPPLY_IMSI, new int[0])) {
            builder.setProfileClass(asn1Node.getChild(NtnRILConstants.MI_NTN_RIL_REQUEST_SUPPLY_IMSI, new int[0]).asInteger());
        } else {
            builder.setProfileClass(2);
        }
        if (asn1Node.hasChild(153, new int[0])) {
            builder.setPolicyRules(asn1Node.getChild(153, new int[0]).asBits());
        }
        if (asn1Node.hasChild(49014, new int[0])) {
            UiccAccessRule[] uiccAccessRuleArrBuildUiccAccessRule = buildUiccAccessRule(asn1Node.getChild(49014, new int[0]).getChildren(NtnRILConstants.MI_NTN_RIL_REQUEST_IS_VONR_ENABLED));
            builder.setUiccAccessRule(uiccAccessRuleArrBuildUiccAccessRule != null ? Arrays.asList(uiccAccessRuleArrBuildUiccAccessRule) : null);
        }
    }

    private static CarrierIdentifier buildCarrierIdentifier(Asn1Node asn1Node) {
        return new CarrierIdentifier(asn1Node.getChild(128, new int[0]).asBytes(), asn1Node.hasChild(129, new int[0]) ? IccUtils.bytesToHexString(asn1Node.getChild(129, new int[0]).asBytes()) : null, asn1Node.hasChild(NtnRILConstants.MI_NTN_RIL_REQUEST_GET_RADIO_CAPABILITY, new int[0]) ? IccUtils.bytesToHexString(asn1Node.getChild(NtnRILConstants.MI_NTN_RIL_REQUEST_GET_RADIO_CAPABILITY, new int[0]).asBytes()) : null);
    }

    private static UiccAccessRule[] buildUiccAccessRule(List list) {
        if (list.isEmpty()) {
            return null;
        }
        int size = list.size();
        UiccAccessRule[] uiccAccessRuleArr = new UiccAccessRule[size];
        for (int i = 0; i < size; i++) {
            Asn1Node asn1Node = (Asn1Node) list.get(i);
            Asn1Node child = asn1Node.getChild(NtnRILConstants.MI_NTN_RIL_REQUEST_ENABLE_VONR, new int[0]);
            uiccAccessRuleArr[i] = new UiccAccessRule(child.getChild(193, new int[0]).asBytes(), child.hasChild(NtnRILConstants.MI_NTN_RIL_REQUEST_SET_SIGNAL_STRENGTH_REPORTING_CRITERIA, new int[0]) ? child.getChild(NtnRILConstants.MI_NTN_RIL_REQUEST_SET_SIGNAL_STRENGTH_REPORTING_CRITERIA, new int[0]).asString() : null, asn1Node.hasChild(NtnRILConstants.MI_NTN_RIL_REQUEST_SET_USAGE_SETTING, new int[]{NtnRILConstants.MI_NTN_RIL_REQUEST_GET_SYSTEM_SELECTION_CHANNELS}) ? asn1Node.getChild(NtnRILConstants.MI_NTN_RIL_REQUEST_SET_USAGE_SETTING, new int[]{NtnRILConstants.MI_NTN_RIL_REQUEST_GET_SYSTEM_SELECTION_CHANNELS}).asRawLong() : 0L);
        }
        return uiccAccessRuleArr;
    }

    private static EuiccNotification createNotification(Asn1Node asn1Node) {
        Asn1Node child;
        if (asn1Node.getTag() == 48943) {
            child = asn1Node;
        } else if (asn1Node.getTag() == 48951) {
            child = asn1Node.getChild(48935, new int[]{48943});
        } else {
            child = asn1Node.getChild(48943, new int[0]);
        }
        return new EuiccNotification(child.getChild(128, new int[0]).asInteger(), child.getChild(12, new int[0]).asString(), child.getChild(129, new int[0]).asBits(), asn1Node.getTag() == 48943 ? null : asn1Node.toBytes());
    }

    private static int parseSimpleResult(byte[] bArr) {
        return parseResponse(bArr).getChild(128, new int[0]).asInteger();
    }

    private static Asn1Node parseResponse(byte[] bArr) throws EuiccCardException {
        Asn1Decoder asn1Decoder = new Asn1Decoder(bArr);
        if (!asn1Decoder.hasNextNode()) {
            throw new EuiccCardException("Empty response", null);
        }
        return asn1Decoder.nextNode();
    }

    private static Asn1Node parseResponseAndCheckSimpleError(byte[] bArr, int i) throws EuiccCardException {
        Asn1Node response = parseResponse(bArr);
        if (response.hasChild(129, new int[0])) {
            throw new EuiccCardErrorException(i, response.getChild(129, new int[0]).asInteger());
        }
        return response;
    }

    private static String stripTrailingFs(byte[] bArr) {
        return IccUtils.stripTrailingFs(IccUtils.bchToString(bArr, 0, bArr.length));
    }

    private static String padTrailingFs(String str) {
        if (TextUtils.isEmpty(str) || str.length() >= 20) {
            return str;
        }
        return str + new String(new char[20 - str.length()]).replace((char) 0, 'F');
    }

    private static void loge(String str) {
        Rlog.e("EuiccPort", str);
    }

    private static void loge(String str, Throwable th) {
        Rlog.e("EuiccPort", str, th);
    }

    private static void logi(String str) {
        Rlog.i("EuiccPort", str);
    }

    private static void logd(String str) {
        Rlog.d("EuiccPort", str);
    }

    @Override // com.android.internal.telephony.uicc.UiccPort
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.dump(fileDescriptor, printWriter, strArr);
        AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
        androidUtilIndentingPrintWriter.println("EuiccPort:");
        androidUtilIndentingPrintWriter.increaseIndent();
        androidUtilIndentingPrintWriter.println("mEid=" + this.mEid);
        androidUtilIndentingPrintWriter.println("mSupportedMepMode=" + this.mSupportedMepMode);
        androidUtilIndentingPrintWriter.decreaseIndent();
    }
}
