package com.android.phone;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import androidx.preference.PreferenceManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.services.telephony.relay.network.SmartNetworkRelayScore;
import com.nxp.osu.EuiccAccessStub;
import com.nxp.osu.FileFinder;
import com.nxp.osu.ISecOsuCallback;
import com.nxp.osu.ISecOsuInterface;
import com.nxp.osu.NfcNciHandler;
import com.nxp.osu.OSUInfo;
import com.nxp.osu.SPIAccessManager;
import com.nxp.osu.SecOsuAgent;
import com.nxp.osu.SecOsuConfigType;
import com.nxp.osu.SecOsuConfigValue;
import com.nxp.osu.SecOsuStatusCode;
import com.xiaomi.telephony.EuiccOsuManagerBase;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class MiuiOsuManager extends EuiccOsuManagerBase {
    private static String mDerFilePathUserDe;
    private static Context mDeviceProtectedContext;
    private static Date mEndDate;
    private static Handler mHandler;
    private static String mSearchedFilePath;
    private static String[] mSendAPDUResult;
    private static Date mStartDate;
    private volatile boolean mIsEsimEnabledOnSlot0;

    public static EuiccOsuManagerBase internalInit() {
        if (EuiccOsuManagerBase.mInstance == null) {
            synchronized (MiuiOsuManager.class) {
                try {
                    if (EuiccOsuManagerBase.mInstance == null) {
                        EuiccOsuManagerBase.mInstance = new MiuiOsuManager();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return EuiccOsuManagerBase.mInstance;
    }

    private MiuiOsuManager() {
        super(PhoneGlobals.getInstance());
        this.mIsEsimEnabledOnSlot0 = false;
        EuiccOsuManagerBase.logD("MiuiOsuManager init");
        mDeviceProtectedContext = this.mContext.createDeviceProtectedStorageContext();
        mHandler = new Handler(Looper.getMainLooper()) { // from class: com.android.phone.MiuiOsuManager.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                EuiccOsuManagerBase.logD("handleMessage what:" + message.what);
                if (message.what != 1) {
                    return;
                }
                int iIntValue = ((Integer) message.obj).intValue();
                EuiccOsuManagerBase.logD("OSU: status:" + iIntValue);
                if (iIntValue != 0) {
                    EuiccOsuManagerBase.logD("OSU: get file failed");
                    if (((EuiccOsuManagerBase) MiuiOsuManager.this).mIsNeedEnableNfc) {
                        MiuiOsuManager.this.enableNfc(false);
                        ((EuiccOsuManagerBase) MiuiOsuManager.this).mIsNeedEnableNfc = false;
                        return;
                    }
                    return;
                }
                MiuiOsuManager.this.executeUpdateTask();
            }
        };
    }

    @Override // com.xiaomi.telephony.EuiccOsuManagerBase
    protected void executeGetFileTask() {
        new SecOsuGetFileTask().start();
    }

    protected void executeUpdateTask() {
        if (isEsimEnabledOnSlot(0)) {
            this.mIsEsimEnabledOnSlot0 = true;
            setModemDirectModeEx();
        } else {
            setModemDirectMode(true);
        }
        new SecOsuUpdateTask().start();
    }

    public int executeGetFileSync() {
        EuiccOsuManagerBase.logD("SecOsuGetFile START.");
        try {
            int iHandleNfcEnablement = handleNfcEnablement();
            if (iHandleNfcEnablement != 0) {
                return iHandleNfcEnablement;
            }
            File file = new File(this.mContext.getFilesDir().getAbsolutePath() + "/DER_FILES");
            if (!file.exists()) {
                file.mkdirs();
            }
            String[] assetFiles = getAssetFiles();
            if (assetFiles == null) {
                return -1;
            }
            if (hasExistingCachedFile()) {
                return 0;
            }
            OSUInfo info = FileFinder.getInfo(mDeviceProtectedContext);
            if (info != null && info.getOSInfo() != null) {
                return processFileMatching(assetFiles, info);
            }
            EuiccOsuManagerBase.logD("Not Able to determine Information");
            return -2;
        } catch (Exception e) {
            EuiccOsuManagerBase.logD("SecOsuGetFile Exception:" + e.getMessage());
            return -1;
        }
    }

    private int handleNfcEnablement() {
        if (isNfcEnable()) {
            return 0;
        }
        EuiccOsuManagerBase.logD("OSU: NFC is not enabled");
        if (enableNfc(true)) {
            this.mIsNeedEnableNfc = true;
            return 0;
        }
        EuiccOsuManagerBase.logD("OSU: enable NFC failed");
        return -3;
    }

    private String[] getAssetFiles() {
        try {
            String[] list = this.mContext.getAssets().list("");
            if (list == null) {
                EuiccOsuManagerBase.logD("return SecOsuGetFileTask for files is null.");
            }
            return list;
        } catch (IOException e) {
            EuiccOsuManagerBase.logE("Error listing assets: " + e);
            return null;
        }
    }

    private boolean hasExistingCachedFile() {
        String oSUFilePathFromCache = getOSUFilePathFromCache();
        mDerFilePathUserDe = oSUFilePathFromCache;
        if (oSUFilePathFromCache == null) {
            return false;
        }
        EuiccOsuManagerBase.logD("return for - message = Last OS Update Teared Press start osu to continue update");
        return true;
    }

    private int processFileMatching(String[] strArr, OSUInfo oSUInfo) {
        String fileName = oSUInfo.getFileName();
        List listFindMatchedFiles = findMatchedFiles(strArr, fileName);
        if (listFindMatchedFiles.isEmpty()) {
            EuiccOsuManagerBase.logD("No File found for pattern " + fileName);
            return -1;
        }
        String latestMatchedFile = getLatestMatchedFile(listFindMatchedFiles);
        mSearchedFilePath = this.mContext.getFilesDir().getAbsolutePath() + "/DER_FILES/" + latestMatchedFile;
        StringBuilder sb = new StringBuilder("File Path :");
        sb.append(mSearchedFilePath);
        EuiccOsuManagerBase.logD(sb.toString());
        return copyMatchedFile(latestMatchedFile);
    }

    private List findMatchedFiles(String[] strArr, String str) {
        ArrayList arrayList = new ArrayList();
        for (String str2 : strArr) {
            EuiccOsuManagerBase.logD("File Name :" + str2);
            if (str2.startsWith(str)) {
                EuiccOsuManagerBase.logD("Matched File :".concat(str2));
                arrayList.add(str2);
            }
        }
        EuiccOsuManagerBase.logD("Matched Files Count :" + arrayList.size());
        return arrayList;
    }

    private String getLatestMatchedFile(List list) {
        Collections.sort(list, Collections.reverseOrder());
        return (String) list.get(0);
    }

    private int copyMatchedFile(String str) throws Throwable {
        String str2 = this.mContext.getFilesDir().getAbsolutePath() + "/DER_FILES";
        SecOsuGetFileTask secOsuGetFileTask = new SecOsuGetFileTask();
        secOsuGetFileTask.copyAssetsToInternalStorage(str, str2);
        String strCopySearchedDERFiletoDeviceProtectedStorage = secOsuGetFileTask.copySearchedDERFiletoDeviceProtectedStorage(str);
        mDerFilePathUserDe = strCopySearchedDERFiletoDeviceProtectedStorage;
        if (strCopySearchedDERFiletoDeviceProtectedStorage != null) {
            return 0;
        }
        EuiccOsuManagerBase.logD("Not able to Copy File to Device Protected Storage. Continue OSU with Credential Storage file");
        mDerFilePathUserDe = mSearchedFilePath;
        return -1;
    }

    @Override // com.xiaomi.telephony.EuiccOsuManagerBase
    public void executeUpdate() {
        EuiccOsuManagerBase.logD("SecOsuUpdate START.");
        if (executeGetFileSync() != 0) {
            EuiccOsuManagerBase.logD("OSU-APP SecOsuGetFile failed");
            if (this.mIsNeedEnableNfc) {
                enableNfc(false);
                this.mIsNeedEnableNfc = false;
                return;
            }
            return;
        }
        executeUpdateTask();
    }

    @Override // com.xiaomi.telephony.EuiccOsuManagerBase
    public boolean isOsuFileExisted() {
        String[] assetFiles = getAssetFiles();
        if (assetFiles == null) {
            return false;
        }
        String string = PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).getString("osu_file_name", null);
        EuiccOsuManagerBase.logD("OSU-APP osuFileName:" + string);
        if (string == null || string.isEmpty()) {
            EuiccOsuManagerBase.logD("OSU-APP No OSU file name found in preference");
            return false;
        }
        for (String str : assetFiles) {
            if (str.equals(string)) {
                EuiccOsuManagerBase.logD("Found file: ".concat(str));
                return true;
            }
        }
        EuiccOsuManagerBase.logD("No OSU file found in assets");
        return false;
    }

    @Override // com.xiaomi.telephony.EuiccOsuManagerBase
    public String getOsuFileName() {
        sendAtCommand(new String[]{"AT+EXCGLA=0,0,10,10,\"0070000001\",1", "+EXCGLA"}, 1);
        sendAtCommand(new String[]{"AT+EXCGLA=0,1,42,42,\"01a4040010A0000005591010FFFFFFFF8900000100\",1", "+EXCGLA"}, 1);
        String strSendAtCommand = sendAtCommand(new String[]{"AT+EXCGLA=0,1,14,14,\"81CA00FE02DF23\",1", "+EXCGLA"}, 1);
        String strSendAtCommand2 = sendAtCommand(new String[]{"AT+EXCGLA=0,1,14,14,\"81CA00FE02DF43\",1", "+EXCGLA"}, 1);
        sendAtCommand(new String[]{"AT+EXCGLA=0,0,8,8,\"00708001\",1", "+EXCGLA"}, 1);
        String confIdResponse = parseConfIdResponse(strSendAtCommand);
        String rsnResponse = parseRsnResponse(strSendAtCommand2);
        String str = (confIdResponse == null || rsnResponse == null) ? null : String.format(Locale.US, "%s_%s_%s", "SN220", rsnResponse, confIdResponse);
        EuiccOsuManagerBase.logD("OSU-APP fileName: " + str + " rsp1:" + strSendAtCommand + " rsp2:" + strSendAtCommand2);
        return str;
    }

    private String parseConfIdResponse(String str) {
        String strSubstring = null;
        if (str != null && !str.isEmpty()) {
            int iIndexOf = str.indexOf("DF23");
            if (iIndexOf == -1) {
                EuiccOsuManagerBase.logD("parseConfIdResponse: No DF23 found");
                return null;
            }
            int i = iIndexOf + 4;
            int i2 = iIndexOf + 6;
            try {
                strSubstring = str.substring(i2, (Integer.parseInt(str.substring(i, i2), 16) * 2) + i2);
            } catch (Exception e) {
                EuiccOsuManagerBase.logD("parseConfIdResponse: Exception:" + e.getMessage());
            }
            EuiccOsuManagerBase.logD("parseConfIdResponse: confId:" + strSubstring);
        }
        return strSubstring;
    }

    private String parseRsnResponse(String str) {
        if (str != null && !str.isEmpty()) {
            int iIndexOf = str.indexOf("DF43");
            if (iIndexOf == -1) {
                EuiccOsuManagerBase.logD("parseRsnResponse: No DF43 found");
                return null;
            }
            int i = iIndexOf + 8;
            while (i < str.length() - 4) {
                try {
                    int i2 = i + 2;
                    String strSubstring = str.substring(i, i2);
                    int i3 = i + 4;
                    int i4 = Integer.parseInt(str.substring(i2, i3), 16);
                    if ("82".equals(strSubstring)) {
                        String strSubstring2 = str.substring(i3, (i4 * 2) + i3);
                        EuiccOsuManagerBase.logD("parseRsnResponse: rsn:" + strSubstring2);
                        return strSubstring2;
                    }
                    i = i3 + (i4 * 2);
                } catch (Exception e) {
                    EuiccOsuManagerBase.logD("parseRsnResponse: Exception:" + e.getMessage());
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String sendAtCommand(String[] strArr, int i) {
        mSendAPDUResult = new String[]{""};
        Phone phone = getPhone(i);
        if (phone != null) {
            HandlerThread handlerThread = new HandlerThread("OSU");
            handlerThread.start();
            try {
                sendAtCommand(phone, strArr, new Handler(this, handlerThread.getLooper()) { // from class: com.android.phone.MiuiOsuManager.2
                    @Override // android.os.Handler
                    public void handleMessage(Message message) {
                        AsyncResult asyncResult = (AsyncResult) message.obj;
                        if (asyncResult != null && asyncResult.exception == null) {
                            try {
                                MiuiOsuManager.mSendAPDUResult = (String[]) asyncResult.result;
                                if (MiuiOsuManager.mSendAPDUResult == null || MiuiOsuManager.mSendAPDUResult.length <= 0) {
                                    EuiccOsuManagerBase.logD("OSU-APP Resp is null");
                                } else {
                                    EuiccOsuManagerBase.logD("OSU-APP sendAtCommand mSendAPDUResult:" + MiuiOsuManager.mSendAPDUResult[0]);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        getLooper().quit();
                    }
                }.obtainMessage());
                handlerThread.join(2000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String[] strArr2 = mSendAPDUResult;
        if (strArr2 == null || strArr2.length <= 0) {
            return null;
        }
        return strArr2[0];
    }

    public static void sendAtCommand(Phone phone, String[] strArr, Message message) {
        if (phone != null) {
            phone.invokeOemRilRequestStrings(strArr, message);
        }
    }

    public String parseResponse(String str) {
        if (str == null) {
            return null;
        }
        int iIndexOf = str.indexOf(34);
        if (iIndexOf == -1) {
            EuiccOsuManagerBase.logD("parseResponse: No quotes found");
            return str;
        }
        int i = iIndexOf + 1;
        int iIndexOf2 = str.indexOf(34, i);
        if (iIndexOf2 == -1) {
            EuiccOsuManagerBase.logD("parseResponse: Only one quote found");
            return str.substring(i);
        }
        String strSubstring = str.substring(i, iIndexOf2);
        EuiccOsuManagerBase.logD("parseResponse Raw String:".concat(strSubstring));
        return strSubstring;
    }

    public boolean checkResponse(String str) {
        return str != null && str.contains("9000");
    }

    private String toString(byte[] bArr) {
        if (bArr == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        for (byte b : bArr) {
            sb.append(String.format(Locale.US, "%02x", Byte.valueOf(b)));
        }
        return sb.toString();
    }

    private byte[] hexStringToByteArray(String str) {
        if (str == null || str.isEmpty()) {
            return new byte[0];
        }
        if (str.length() % 2 != 0) {
            str = "0".concat(str);
            EuiccOsuManagerBase.logD("hexStringToByteArray: odd length input, padding with leading zero");
        }
        int length = str.length();
        byte[] bArr = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return bArr;
    }

    @Override // com.xiaomi.telephony.EuiccOsuManagerBase
    public byte[] osuOpenChannel(byte[] bArr) {
        String string = toString(bArr);
        EuiccOsuManagerBase.logD("EuiccAccessStub osuOpenChannel aid:" + string);
        int length = string.length();
        int i = length + 10;
        String strSendAtCommand = sendAtCommand(new String[]{String.format(Locale.US, "AT+EXCGLA=0,0,%d,%d,\"00a40400%02x%s\",1", Integer.valueOf(i), Integer.valueOf(i), Integer.valueOf(length / 2), string), "+EXCGLA"}, 1);
        EuiccOsuManagerBase.logD("EuiccAccessStub osuOpenChannel Resp:" + strSendAtCommand);
        if (strSendAtCommand != null) {
            return hexStringToByteArray(parseResponse(strSendAtCommand));
        }
        return null;
    }

    @Override // com.xiaomi.telephony.EuiccOsuManagerBase
    public byte[] osuSend(byte[] bArr) {
        String string = toString(bArr);
        EuiccOsuManagerBase.logD("EuiccAccessStub osuSend data:" + string);
        int length = string.length();
        String strSendAtCommand = sendAtCommand(new String[]{String.format(Locale.US, "AT+EXCGLA=0,0,%d,%d,\"%s\",1", Integer.valueOf(length), Integer.valueOf(length), string), "+EXCGLA"}, 1);
        EuiccOsuManagerBase.logD("EuiccAccessStub osuSend Resp:" + strSendAtCommand);
        if (checkResponse(strSendAtCommand) && strSendAtCommand != null) {
            return hexStringToByteArray(parseResponse(strSendAtCommand));
        }
        return null;
    }

    @Override // com.xiaomi.telephony.EuiccOsuManagerBase
    public void doWarmReset() {
        EuiccOsuManagerBase.logD("doWarmReset result = " + sendAtCommand(new String[]{String.format(Locale.US, "AT+ESIMPOWER=%d", 2), "+ESIMPOWER"}, 1));
    }

    @Override // com.xiaomi.telephony.EuiccOsuManagerBase
    public void setModemDirectMode(boolean z) {
        EuiccOsuManagerBase.logD("EuiccAccessStub setModemDirectMode mode:" + z);
        if (true == z) {
            Locale locale = Locale.US;
            sendAtCommand(new String[]{String.format(locale, "AT+ESIMPOWER=%d", 3), "+ESIMPOWER"}, 1);
            sendAtCommand(new String[]{String.format(locale, "AT+ESIMPOWER=%d", 2), "+ESIMPOWER"}, 1);
            return;
        }
        sendAtCommand(new String[]{String.format(Locale.US, "AT+ESIMPOWER=%d", 4), "+ESIMPOWER"}, 1);
    }

    public void setModemDirectModeEx() {
        EuiccOsuManagerBase.logD("EuiccAccessStub setModemDirectModeEx");
        Locale locale = Locale.US;
        sendAtCommand(new String[]{String.format(locale, "AT+ESIMPOWER=%d", 3), "+ESIMPOWER"}, 1);
        sendAtCommand(new String[]{String.format(locale, "AT+ESIMPOWER=%d", 3), "+ESIMPOWER"}, 0);
        try {
            EuiccOsuManagerBase.logD("setModemDirectModeEx: sleep 500 ms");
            Thread.sleep(500L);
        } catch (Exception e) {
            EuiccOsuManagerBase.logD("setModemDirectModeEx Exception:" + e.getMessage());
            Thread.currentThread().interrupt();
        }
        sendAtCommand(new String[]{String.format(Locale.US, "AT+ESIMPOWER=%d", 2), "+ESIMPOWER"}, 1);
    }

    public class SecOsuGetFileTask extends Thread {
        @Override // java.lang.Thread, java.lang.Runnable
        public void run() throws Throwable {
            String str;
            String str2;
            EuiccOsuManagerBase.logD("SecOsuGetFileTask START.");
            PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
            int i = 0;
            try {
                String str3 = phoneGlobals.getFilesDir().getAbsolutePath() + "/DER_FILES";
                File file = new File(str3);
                if (!file.exists()) {
                    file.mkdirs();
                }
                String[] list = phoneGlobals.getAssets().list("");
                if (list == null) {
                    EuiccOsuManagerBase.logD("return SecOsuGetFileTask for files is null.");
                    i = -1;
                } else {
                    MiuiOsuManager.mDerFilePathUserDe = MiuiOsuManager.getOSUFilePathFromCache();
                    if (MiuiOsuManager.mDerFilePathUserDe != null) {
                        EuiccOsuManagerBase.logD("return for - message = ".concat("Last OS Update Teared  Press start osu to continue update"));
                    } else {
                        OSUInfo info = FileFinder.getInfo(MiuiOsuManager.mDeviceProtectedContext);
                        if (info == null || info.getOSInfo() == null) {
                            str = "Not Able to determine Information";
                            i = -2;
                        } else {
                            String fileName = info.getFileName();
                            String str4 = ("" + info.getOSInfo().toString() + "\n") + "Configuration Id : " + info.getConfigurationIdHexValue() + "\n";
                            ArrayList arrayList = new ArrayList();
                            for (int i2 = 0; i2 < list.length; i2++) {
                                EuiccOsuManagerBase.logD("FileName:" + list[i2]);
                                if (list[i2].startsWith(fileName)) {
                                    EuiccOsuManagerBase.logD("Matched  FileName:" + list[i2]);
                                    arrayList.add(list[i2]);
                                }
                            }
                            EuiccOsuManagerBase.logD("Matched files count  : " + arrayList.size());
                            if (arrayList.size() == 0) {
                                str = "No File found for pattern " + fileName + "\n" + str4;
                                str2 = null;
                                MiuiOsuManager.mSearchedFilePath = null;
                                EuiccOsuManagerBase.logD("return for - message = " + str);
                                i = -1;
                            } else {
                                Collections.sort(arrayList, Collections.reverseOrder());
                                String str5 = (String) arrayList.get(0);
                                MiuiOsuManager.mSearchedFilePath = str3 + "/" + str5;
                                str = "File Path :" + MiuiOsuManager.mSearchedFilePath + "\n" + str4;
                                EuiccOsuManagerBase.logD("return for - message = " + str);
                                str2 = str5;
                            }
                            if (MiuiOsuManager.mSearchedFilePath != null) {
                                copyAssetsToInternalStorage(str2, str3);
                                MiuiOsuManager.mDerFilePathUserDe = copySearchedDERFiletoDeviceProtectedStorage(str2);
                                if (MiuiOsuManager.mDerFilePathUserDe == null) {
                                    EuiccOsuManagerBase.logD("Not able to Copy File to Device Protected Storage Continue OSU with Credantial Storage file");
                                    MiuiOsuManager.mDerFilePathUserDe = MiuiOsuManager.mSearchedFilePath;
                                    i = -1;
                                }
                            }
                        }
                        EuiccOsuManagerBase.logD("OSU-APP SecOsuGetFileTask Response message is " + str + " , error is " + i);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (MiuiOsuManager.mHandler != null) {
                MiuiOsuManager.mHandler.sendMessage(MiuiOsuManager.mHandler.obtainMessage(1, Integer.valueOf(i)));
                EuiccOsuManagerBase.logD("send EVENT_EXECUTE_UPDATE_TYPE msg");
            }
        }

        public void copyAssetsToInternalStorage(String str, String str2) throws Throwable {
            EuiccOsuManagerBase.logD("copyAssetsToInternalStorage fileName: " + str);
            AssetManager assets = MiuiOsuManager.mDeviceProtectedContext.getAssets();
            try {
                String[] list = assets.list("");
                if (list == null || list.length <= 0) {
                    EuiccOsuManagerBase.logD("No files found in assets directory");
                    return;
                }
                for (String str3 : list) {
                    EuiccOsuManagerBase.logD("copyAssetsToInternalStorage cell filename: " + str3);
                    if (str.equals(str3)) {
                        EuiccOsuManagerBase.logD(MiuiOsuManager.mSearchedFilePath + " File will be copied at " + str2);
                        File file = new File(str2, str3);
                        if (file.exists()) {
                            EuiccOsuManagerBase.logD("File already exists: " + file.getAbsolutePath());
                        } else {
                            File file2 = new File(str2);
                            if (!file2.exists() && !file2.mkdirs()) {
                                EuiccOsuManagerBase.logE("Failed to create directory: " + str2);
                            } else {
                                copyFile(assets, str3, file);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                EuiccOsuManagerBase.logE("Error listing assets: " + e);
            }
        }

        private static void copyFile(AssetManager assetManager, String str, File file) throws Throwable {
            FileOutputStream fileOutputStream;
            InputStream inputStream = null;
            try {
                InputStream inputStreamOpen = assetManager.open(str);
                try {
                    fileOutputStream = new FileOutputStream(file);
                    try {
                        byte[] bArr = new byte[SmartNetworkRelayScore.TRAFFIC_UNIT];
                        while (true) {
                            int i = inputStreamOpen.read(bArr);
                            if (i != -1) {
                                fileOutputStream.write(bArr, 0, i);
                            } else {
                                fileOutputStream.flush();
                                inputStreamOpen.close();
                                fileOutputStream.close();
                                return;
                            }
                        }
                    } catch (Throwable th) {
                        th = th;
                        inputStream = inputStreamOpen;
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    fileOutputStream = null;
                }
            } catch (Throwable th3) {
                th = th3;
                fileOutputStream = null;
            }
        }

        /* JADX WARN: Code duplicated, block: B:46:0x00d3 A[Catch: Exception -> 0x00cf, TRY_LEAVE, TryCatch #2 {Exception -> 0x00cf, blocks: (B:42:0x00cb, B:46:0x00d3), top: B:54:0x00cb }] */
        /* JADX WARN: Code duplicated, block: B:54:0x00cb A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Code duplicated, block: B:63:? A[SYNTHETIC] */
        String copySearchedDERFiletoDeviceProtectedStorage(String str) throws Throwable {
            Throwable th;
            Exception exc;
            FileInputStream fileInputStream;
            FileOutputStream fileOutputStream;
            FileInputStream fileInputStream2 = null;
            if (MiuiOsuManager.mSearchedFilePath != null) {
                try {
                    try {
                        if (str != null) {
                            try {
                                String str2 = MiuiOsuManager.mDeviceProtectedContext.getFilesDir().getAbsolutePath() + "/" + str;
                                EuiccOsuManagerBase.logD(MiuiOsuManager.mSearchedFilePath + " File will be copied at " + str2);
                                fileInputStream = new FileInputStream(new File(MiuiOsuManager.mSearchedFilePath));
                                try {
                                    fileOutputStream = new FileOutputStream(new File(str2));
                                    try {
                                        FileChannel channel = fileInputStream.getChannel();
                                        if (channel.transferTo(0L, channel.size(), fileOutputStream.getChannel()) < 0) {
                                            EuiccOsuManagerBase.logE("File Copy failed");
                                            fileInputStream.close();
                                            fileOutputStream.close();
                                            return null;
                                        }
                                        EuiccOsuManagerBase.logD("File Copied SuccessFully .");
                                        try {
                                            fileInputStream.close();
                                            fileOutputStream.close();
                                            return str2;
                                        } catch (Exception e) {
                                            EuiccOsuManagerBase.logD("Exception While closing file after copy : ");
                                            e.printStackTrace();
                                            return str2;
                                        }
                                    } catch (Exception e2) {
                                        exc = e2;
                                        EuiccOsuManagerBase.logD("Exception While coping file to Device Protected Storage ");
                                        exc.printStackTrace();
                                        if (fileInputStream != null) {
                                            fileInputStream.close();
                                        }
                                        if (fileOutputStream != null) {
                                        }
                                        return null;
                                    }
                                } catch (Exception e3) {
                                    exc = e3;
                                    fileOutputStream = null;
                                } catch (Throwable th2) {
                                    th = th2;
                                    fileInputStream2 = fileInputStream;
                                    if (fileInputStream2 != null) {
                                        try {
                                            fileInputStream2.close();
                                        } catch (Exception e4) {
                                            EuiccOsuManagerBase.logD("Exception While closing file after copy : ");
                                            e4.printStackTrace();
                                            throw th;
                                        }
                                    }
                                    if (fileInputStream2 != 0) {
                                        fileInputStream2.close();
                                        throw th;
                                    }
                                    throw th;
                                }
                            } catch (Exception e5) {
                                exc = e5;
                                fileInputStream = null;
                                fileOutputStream = null;
                            } catch (Throwable th3) {
                                th = th3;
                                if (fileInputStream2 != null) {
                                    fileInputStream2.close();
                                }
                                if (fileInputStream2 != 0) {
                                    fileInputStream2.close();
                                    throw th;
                                }
                                throw th;
                            }
                        }
                    } catch (Exception e6) {
                        EuiccOsuManagerBase.logD("Exception While closing file after copy : ");
                        e6.printStackTrace();
                    }
                } catch (Throwable th4) {
                    th = th4;
                }
            }
            EuiccOsuManagerBase.logD("Source File cann't be Null");
            return null;
        }
    }

    public class SecOsuUpdateTask extends Thread implements ISecOsuCallback {
        private SPIAccessManager mSpiAccessManager = null;

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            String str;
            int iSecOsuExecute = -1;
            boolean zBlockAccessOverSPI = false;
            try {
                ISecOsuInterface iSecOsuInterface = new ISecOsuInterface();
                iSecOsuInterface.mAccess = EuiccAccessStub.getInstance();
                iSecOsuInterface.mModem = EuiccAccessStub.getInstance();
                SecOsuAgent secOsuAgent = SecOsuAgent.getInstance(MiuiOsuManager.mDeviceProtectedContext, iSecOsuInterface);
                NfcNciHandler.setContext(MiuiOsuManager.mDeviceProtectedContext);
                SecOsuConfigType secOsuConfigType = SecOsuConfigType.INTEGRITY_CHECK_SUPPORT;
                SecOsuConfigValue secOsuConfigValue = SecOsuConfigValue.DISABLED;
                secOsuAgent.secOsuSetConfig(secOsuConfigType, secOsuConfigValue);
                secOsuAgent.secOsuSetConfig(SecOsuConfigType.NFC_DISABLE_SUPPORT, secOsuConfigValue);
                MiuiOsuManager.mStartDate = new Date();
                String str2 = "Starting update with file : " + MiuiOsuManager.mDerFilePathUserDe;
                SPIAccessManager sPIAccessManager = SPIAccessManager.getInstance(MiuiOsuManager.mDeviceProtectedContext);
                this.mSpiAccessManager = sPIAccessManager;
                zBlockAccessOverSPI = sPIAccessManager.blockAccessOverSPI(secOsuAgent);
                if (!zBlockAccessOverSPI) {
                    EuiccOsuManagerBase.logD("** Failed to control eSE Access over SPI. Not triggering OSU.-1");
                } else {
                    iSecOsuExecute = secOsuAgent.SecOsuExecute(MiuiOsuManager.mDerFilePathUserDe, this);
                }
                EuiccOsuManagerBase.logD("OSU-APP SecOsuUpdateTask status " + iSecOsuExecute + " , message = " + str2);
                if (iSecOsuExecute == 0) {
                    MiuiOsuManager.setOSUFilePathToCache(MiuiOsuManager.mDerFilePathUserDe);
                    str = "Exceute Call Status:  OSU is on going";
                } else if (iSecOsuExecute == 1) {
                    str = "Exceute Call Status:  Failed ";
                } else if (iSecOsuExecute == 2) {
                    str = "Exceute Call Status:  Busy ";
                } else if (iSecOsuExecute == 3) {
                    str = "Exceute Call Status:  File not Found ";
                } else if (iSecOsuExecute == 4) {
                    str = "Exceute Call Status:  Invalid File ";
                } else {
                    str = "Exceute Call Status:  Unknown Error ";
                }
                EuiccOsuManagerBase.logD("OSU-APP SecOsuUpdateTask run message = ".concat(str));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (iSecOsuExecute != SecOsuStatusCode.OSU_STATUS_SUCCESS.getValue() && iSecOsuExecute != SecOsuStatusCode.OSU_STATUS_BUSY.getValue() && zBlockAccessOverSPI) {
                    this.mSpiAccessManager.unBlockAccessOverSPI();
                }
            }
        }

        @Override // com.nxp.osu.ISecOsuCallback
        public void onOsuComplete(int i, String str) {
            String str2;
            EuiccOsuManagerBase.logD("OSU-APP SecOsuUpdateTask onOsuComplete Status: " + i + " , info = " + str);
            MiuiOsuManager.mEndDate = new Date();
            if (MiuiOsuManager.mStartDate != null && MiuiOsuManager.mEndDate != null) {
                long time = MiuiOsuManager.mEndDate.getTime() - MiuiOsuManager.mStartDate.getTime();
                EuiccOsuManagerBase.logD("Start Time : " + MiuiOsuManager.mStartDate.getTime());
                EuiccOsuManagerBase.logD("End Time : " + MiuiOsuManager.mEndDate.getTime());
                str2 = "Total time taken " + (time / 1000) + " S " + (time % 1000) + " MS";
            } else {
                str2 = "";
            }
            SPIAccessManager sPIAccessManager = this.mSpiAccessManager;
            if (sPIAccessManager != null) {
                sPIAccessManager.unBlockAccessOverSPI();
            }
            String strHandleOsuStatus = handleOsuStatus(i);
            if (EuiccOsuManagerBase.getInstance() != null && EuiccOsuManagerBase.getInstance().isNeedEnableNfc()) {
                EuiccOsuManagerBase.getInstance().enableNfc(false);
                EuiccOsuManagerBase.getInstance().setIsNeedEnableNfc(false);
            }
            String str3 = strHandleOsuStatus + " " + str2;
            EuiccOsuManagerBase.logD("OSU-APP SecOsuUpdateTask onOsuComplete message: ".concat(str3));
            MiuiOsuManager miuiOsuManager = (MiuiOsuManager) EuiccOsuManagerBase.getInstance();
            if (miuiOsuManager != null && ((EuiccOsuManagerBase) miuiOsuManager).mCallback != null) {
                ((EuiccOsuManagerBase) miuiOsuManager).mCallback.onComplete(i, str3);
            }
            if (miuiOsuManager == null || !miuiOsuManager.mIsEsimEnabledOnSlot0) {
                return;
            }
            EuiccOsuManagerBase.logD("OSU-APP SecOsuUpdateTask onOsuComplete enable eSIM on slot0");
            Locale locale = Locale.US;
            miuiOsuManager.sendAtCommand(new String[]{String.format(locale, "AT+ESIMPOWER=%d", 4), "+ESIMPOWER"}, 1);
            miuiOsuManager.sendAtCommand(new String[]{String.format(locale, "AT+ESIMPOWER=%d", 4), "+ESIMPOWER"}, 0);
            miuiOsuManager.mIsEsimEnabledOnSlot0 = false;
        }

        private String handleOsuStatus(int i) {
            if (i == 0) {
                cleanupOsuData();
                MiuiOsuManager.updateOsuFileNamePreference();
                return "Update Completed with Status: Success";
            }
            if (i == 1) {
                return "Update Completed with Status: Failed";
            }
            if (i == 2) {
                return "Update Completed with Status: Busy";
            }
            if (i == 3) {
                cleanupOsuData();
                return "Update Completed with Status: Invalid File";
            }
            if (i == 4) {
                cleanupOsuData();
                MiuiOsuManager.updateOsuFileNamePreference();
                return "Update Completed with Status: Already UpDated";
            }
            return "Update Completed with Status: Unknown Error";
        }

        private void cleanupOsuData() {
            MiuiOsuManager.mSearchedFilePath = null;
            MiuiOsuManager.deleteDeviceStorageDerFile();
            MiuiOsuManager.setOSUFilePathToCache(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setOSUFilePathToCache(String str) {
        SharedPreferences.Editor editorEdit = mDeviceProtectedContext.getSharedPreferences("OSUTestAPP_KEY", 0).edit();
        editorEdit.putString("OSU_SRC_FILE", str);
        editorEdit.commit();
    }

    public static void deleteDeviceStorageDerFile() {
        try {
            if (mDerFilePathUserDe != null) {
                if (new File(mDerFilePathUserDe).delete()) {
                    EuiccOsuManagerBase.logD("deleted the Device Protected Storage DER File ");
                } else {
                    EuiccOsuManagerBase.logD("Not Able to delete the Device Protected Storage DER File ");
                }
                mDerFilePathUserDe = null;
            }
        } catch (Exception e) {
            EuiccOsuManagerBase.logE("Exception while deleting DER File :");
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getOSUFilePathFromCache() {
        return mDeviceProtectedContext.getSharedPreferences("OSUTestAPP_KEY", 0).getString("OSU_SRC_FILE", null);
    }

    public static Phone getPhone(int i) {
        try {
            return PhoneFactory.getPhone(i);
        } catch (Exception unused) {
            return null;
        }
    }

    public static void updateOsuFileNamePreference() {
        try {
            if (MiuiPhoneUtils.isSupportOsuFeature()) {
                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance());
                String string = defaultSharedPreferences.getString("osu_file_name", null);
                String osuFileName = EuiccOsuManagerBase.getInstance() != null ? EuiccOsuManagerBase.getInstance().getOsuFileName() : null;
                EuiccOsuManagerBase.logD("osuFileName = " + string + ", osuFileNameFromNxp = " + osuFileName);
                if (string != null && string.equals(osuFileName)) {
                    return;
                }
                defaultSharedPreferences.edit().putString("osu_file_name", osuFileName).apply();
            }
        } catch (Exception e) {
            EuiccOsuManagerBase.logE("updateOsuFileNamePreference " + e);
        }
    }
}
