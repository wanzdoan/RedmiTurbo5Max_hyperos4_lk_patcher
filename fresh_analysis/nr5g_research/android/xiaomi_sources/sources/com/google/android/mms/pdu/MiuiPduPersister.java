package com.google.android.mms.pdu;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteException;
import android.drm.DrmManagerClient;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.Telephony;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.MiuiIccProvider;
import com.google.android.collect.Lists;
import com.google.android.mms.ContentType;
import com.google.android.mms.InvalidHeaderValueException;
import com.google.android.mms.MmsException;
import com.google.android.mms.util.DownloadDrmHelper;
import com.google.android.mms.util.DrmConvertSession;
import com.google.android.mms.util.PduCache;
import com.google.android.mms.util.PduCacheEntry;
import com.google.android.mms.util.SqliteWrapper;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.mirilhook.MiRilHookBase;
import com.xiaomi.modem.ModemUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import miui.telephony.DefaultSimManager;

/* JADX INFO: loaded from: classes.dex */
public class MiuiPduPersister {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final HashMap<Integer, Integer> CHARSET_COLUMN_INDEX_MAP;
    private static final HashMap<Integer, String> CHARSET_COLUMN_NAME_MAP;
    private static final boolean DEBUG = false;
    private static final HashMap<Integer, Integer> ENCODED_STRING_COLUMN_INDEX_MAP;
    private static final HashMap<Integer, String> ENCODED_STRING_COLUMN_NAME_MAP;
    private static final boolean LOCAL_LOGV = false;
    private static final HashMap<Integer, Integer> LONG_COLUMN_INDEX_MAP;
    private static final HashMap<Integer, String> LONG_COLUMN_NAME_MAP;
    private static final HashMap<Integer, Integer> OCTET_COLUMN_INDEX_MAP;
    private static final HashMap<Integer, String> OCTET_COLUMN_NAME_MAP;
    private static final int PART_COLUMN_CHARSET = 1;
    private static final int PART_COLUMN_CONTENT_DISPOSITION = 2;
    private static final int PART_COLUMN_CONTENT_ID = 3;
    private static final int PART_COLUMN_CONTENT_LOCATION = 4;
    private static final int PART_COLUMN_CONTENT_TYPE = 5;
    private static final int PART_COLUMN_FILENAME = 6;
    private static final int PART_COLUMN_ID = 0;
    private static final int PART_COLUMN_NAME = 7;
    private static final int PART_COLUMN_TEXT = 8;
    private static final PduCache PDU_CACHE_INSTANCE;
    private static final int PDU_COLUMN_CONTENT_CLASS = 11;
    private static final int PDU_COLUMN_CONTENT_LOCATION = 5;
    private static final int PDU_COLUMN_CONTENT_TYPE = 6;
    private static final int PDU_COLUMN_DATE = 21;
    private static final int PDU_COLUMN_DELIVERY_REPORT = 12;
    private static final int PDU_COLUMN_DELIVERY_TIME = 22;
    private static final int PDU_COLUMN_EXPIRY = 23;
    private static final int PDU_COLUMN_ID = 0;
    private static final int PDU_COLUMN_MESSAGE_BOX = 1;
    private static final int PDU_COLUMN_MESSAGE_CLASS = 7;
    private static final int PDU_COLUMN_MESSAGE_ID = 8;
    private static final int PDU_COLUMN_MESSAGE_SIZE = 24;
    private static final int PDU_COLUMN_MESSAGE_TYPE = 13;
    private static final int PDU_COLUMN_MMS_VERSION = 14;
    private static final int PDU_COLUMN_PRIORITY = 15;
    private static final int PDU_COLUMN_READ_REPORT = 16;
    private static final int PDU_COLUMN_READ_STATUS = 17;
    private static final int PDU_COLUMN_REPORT_ALLOWED = 18;
    private static final int PDU_COLUMN_RESPONSE_TEXT = 9;
    private static final int PDU_COLUMN_RETRIEVE_STATUS = 19;
    private static final int PDU_COLUMN_RETRIEVE_TEXT = 3;
    private static final int PDU_COLUMN_RETRIEVE_TEXT_CHARSET = 26;
    private static final int PDU_COLUMN_STATUS = 20;
    private static final int PDU_COLUMN_SUBJECT = 4;
    private static final int PDU_COLUMN_SUBJECT_CHARSET = 25;
    private static final int PDU_COLUMN_THREAD_ID = 2;
    private static final int PDU_COLUMN_TRANSACTION_ID = 10;
    public static final int PROC_STATUS_COMPLETED = 3;
    public static final int PROC_STATUS_PERMANENTLY_FAILURE = 2;
    public static final int PROC_STATUS_TRANSIENT_FAILURE = 1;
    private static final String TAG = "MiuiPduPersister";
    public static final String TEMPORARY_DRM_OBJECT_URI = "content://mms/9223372036854775807/part";
    private static final HashMap<Integer, Integer> TEXT_STRING_COLUMN_INDEX_MAP;
    private static final HashMap<Integer, String> TEXT_STRING_COLUMN_NAME_MAP;
    private static MiuiPduPersister sPersister;
    private final ContentResolver mContentResolver;
    private final Context mContext;
    private final DrmManagerClient mDrmManagerClient;
    private static final int[] ADDRESS_FIELDS = {129, DefaultSimManager.EVENT_VOLTE_TEMP_DDS, 137, 151};
    private static final String[] PDU_PROJECTION = {MiuiIccProvider.STR_ID, "msg_box", "thread_id", "retr_txt", "sub", "ct_l", "ct_t", "m_cls", "m_id", "resp_txt", "tr_id", "ct_cls", "d_rpt", "m_type", "v", "pri", "rr", "read_status", "rpt_a", "retr_st", "st", "date", "d_tm", "exp", "m_size", "sub_cs", "retr_txt_cs"};
    private static final String[] PART_PROJECTION = {MiuiIccProvider.STR_ID, "chset", "cd", "cid", "cl", ModemUtils.PROP_CERT_TYPE_CT, "fn", MiuiIccProvider.STR_NAME, "text"};
    private static final HashMap<Uri, Integer> MESSAGE_BOX_MAP = new HashMap<>();

    static {
        MESSAGE_BOX_MAP.put(Telephony.Mms.Inbox.CONTENT_URI, 1);
        MESSAGE_BOX_MAP.put(Telephony.Mms.Sent.CONTENT_URI, 2);
        MESSAGE_BOX_MAP.put(Telephony.Mms.Draft.CONTENT_URI, 3);
        MESSAGE_BOX_MAP.put(Telephony.Mms.Outbox.CONTENT_URI, 4);
        CHARSET_COLUMN_INDEX_MAP = new HashMap<>();
        CHARSET_COLUMN_INDEX_MAP.put(150, 25);
        CHARSET_COLUMN_INDEX_MAP.put(154, 26);
        CHARSET_COLUMN_NAME_MAP = new HashMap<>();
        CHARSET_COLUMN_NAME_MAP.put(150, "sub_cs");
        CHARSET_COLUMN_NAME_MAP.put(154, "retr_txt_cs");
        ENCODED_STRING_COLUMN_INDEX_MAP = new HashMap<>();
        ENCODED_STRING_COLUMN_INDEX_MAP.put(154, 3);
        ENCODED_STRING_COLUMN_INDEX_MAP.put(150, 4);
        ENCODED_STRING_COLUMN_NAME_MAP = new HashMap<>();
        ENCODED_STRING_COLUMN_NAME_MAP.put(154, "retr_txt");
        ENCODED_STRING_COLUMN_NAME_MAP.put(150, "sub");
        TEXT_STRING_COLUMN_INDEX_MAP = new HashMap<>();
        TEXT_STRING_COLUMN_INDEX_MAP.put(131, 5);
        TEXT_STRING_COLUMN_INDEX_MAP.put(132, 6);
        TEXT_STRING_COLUMN_INDEX_MAP.put(138, 7);
        TEXT_STRING_COLUMN_INDEX_MAP.put(139, 8);
        TEXT_STRING_COLUMN_INDEX_MAP.put(147, 9);
        TEXT_STRING_COLUMN_INDEX_MAP.put(152, 10);
        TEXT_STRING_COLUMN_NAME_MAP = new HashMap<>();
        TEXT_STRING_COLUMN_NAME_MAP.put(131, "ct_l");
        TEXT_STRING_COLUMN_NAME_MAP.put(132, "ct_t");
        TEXT_STRING_COLUMN_NAME_MAP.put(138, "m_cls");
        TEXT_STRING_COLUMN_NAME_MAP.put(139, "m_id");
        TEXT_STRING_COLUMN_NAME_MAP.put(147, "resp_txt");
        TEXT_STRING_COLUMN_NAME_MAP.put(152, "tr_id");
        OCTET_COLUMN_INDEX_MAP = new HashMap<>();
        OCTET_COLUMN_INDEX_MAP.put(186, 11);
        OCTET_COLUMN_INDEX_MAP.put(Integer.valueOf(MiRilHookBase.EVENT_OEMHOOK_DSDA_HW_MBN_CFG), 12);
        OCTET_COLUMN_INDEX_MAP.put(140, 13);
        OCTET_COLUMN_INDEX_MAP.put(141, 14);
        OCTET_COLUMN_INDEX_MAP.put(143, 15);
        OCTET_COLUMN_INDEX_MAP.put(144, 16);
        OCTET_COLUMN_INDEX_MAP.put(155, 17);
        OCTET_COLUMN_INDEX_MAP.put(145, 18);
        OCTET_COLUMN_INDEX_MAP.put(153, 19);
        OCTET_COLUMN_INDEX_MAP.put(149, 20);
        OCTET_COLUMN_NAME_MAP = new HashMap<>();
        OCTET_COLUMN_NAME_MAP.put(186, "ct_cls");
        OCTET_COLUMN_NAME_MAP.put(Integer.valueOf(MiRilHookBase.EVENT_OEMHOOK_DSDA_HW_MBN_CFG), "d_rpt");
        OCTET_COLUMN_NAME_MAP.put(140, "m_type");
        OCTET_COLUMN_NAME_MAP.put(141, "v");
        OCTET_COLUMN_NAME_MAP.put(143, "pri");
        OCTET_COLUMN_NAME_MAP.put(144, "rr");
        OCTET_COLUMN_NAME_MAP.put(155, "read_status");
        OCTET_COLUMN_NAME_MAP.put(145, "rpt_a");
        OCTET_COLUMN_NAME_MAP.put(153, "retr_st");
        OCTET_COLUMN_NAME_MAP.put(149, "st");
        LONG_COLUMN_INDEX_MAP = new HashMap<>();
        LONG_COLUMN_INDEX_MAP.put(133, 21);
        LONG_COLUMN_INDEX_MAP.put(135, 22);
        LONG_COLUMN_INDEX_MAP.put(136, 23);
        LONG_COLUMN_INDEX_MAP.put(142, 24);
        LONG_COLUMN_NAME_MAP = new HashMap<>();
        LONG_COLUMN_NAME_MAP.put(133, "date_sent");
        LONG_COLUMN_NAME_MAP.put(135, "d_tm");
        LONG_COLUMN_NAME_MAP.put(136, "exp");
        LONG_COLUMN_NAME_MAP.put(142, "m_size");
        PDU_CACHE_INSTANCE = PduCache.getInstance();
    }

    private MiuiPduPersister(Context context) {
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        this.mDrmManagerClient = new DrmManagerClient(context);
    }

    public static MiuiPduPersister getPduPersister(Context context) {
        if (sPersister == null || !context.equals(sPersister.mContext)) {
            sPersister = new MiuiPduPersister(context);
        }
        return sPersister;
    }

    private void setEncodedStringValueToHeaders(Cursor c, int columnIndex, PduHeaders headers, int mapColumn) {
        String s = c.getString(columnIndex);
        if (s != null && s.length() > 0) {
            int charsetColumnIndex = CHARSET_COLUMN_INDEX_MAP.get(Integer.valueOf(mapColumn)).intValue();
            int charset = c.getInt(charsetColumnIndex);
            EncodedStringValue value = new EncodedStringValue(charset, getBytes(s));
            headers.setEncodedStringValue(value, mapColumn);
        }
    }

    private void setTextStringToHeaders(Cursor c, int columnIndex, PduHeaders headers, int mapColumn) {
        String s = c.getString(columnIndex);
        if (s != null) {
            headers.setTextString(getBytes(s), mapColumn);
        }
    }

    private void setOctetToHeaders(Cursor c, int columnIndex, PduHeaders headers, int mapColumn) throws InvalidHeaderValueException {
        if (!c.isNull(columnIndex)) {
            int b = c.getInt(columnIndex);
            headers.setOctet(b, mapColumn);
        }
    }

    private void setLongToHeaders(Cursor c, int columnIndex, PduHeaders headers, int mapColumn) {
        if (!c.isNull(columnIndex)) {
            long l = c.getLong(columnIndex);
            headers.setLongInteger(l, mapColumn);
        }
    }

    private Integer getIntegerFromPartColumn(Cursor c, int columnIndex) {
        if (!c.isNull(columnIndex)) {
            return Integer.valueOf(c.getInt(columnIndex));
        }
        return null;
    }

    private byte[] getByteArrayFromPartColumn(Cursor c, int columnIndex) {
        if (!c.isNull(columnIndex)) {
            return getBytes(c.getString(columnIndex));
        }
        return null;
    }

    /* JADX WARN: Code duplicated, block: B:134:0x01cd A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:156:? A[Catch: all -> 0x0231, SYNTHETIC, TryCatch #8 {all -> 0x0231, blocks: (B:37:0x009b, B:39:0x00a3, B:41:0x00ac, B:43:0x00b8, B:45:0x00f0, B:47:0x00f6, B:49:0x00fc, B:51:0x010d, B:53:0x0115, B:101:0x01df, B:105:0x01ed, B:106:0x01fa, B:110:0x0216, B:94:0x01cd, B:99:0x01d6, B:97:0x01d2, B:72:0x0168, B:75:0x016d, B:111:0x0223, B:112:0x0230), top: B:140:0x009b, inners: #3, #7 }] */
    private PduPart[] loadParts(long msgId) throws Throwable {
        PduPart[] parts;
        byte[] fileName;
        byte[] fileName2;
        IOException iOException;
        MiuiPduPersister miuiPduPersister = this;
        Cursor c = SqliteWrapper.query(miuiPduPersister.mContext, miuiPduPersister.mContentResolver, Uri.parse("content://mms/" + msgId + "/part"), PART_PROJECTION, (String) null, (String[]) null, (String) null);
        if (c != null) {
            try {
                if (c.getCount() != 0) {
                    int partCount = c.getCount();
                    PduPart[] parts2 = new PduPart[partCount];
                    int partIdx = 0;
                    while (c.moveToNext()) {
                        try {
                            PduPart part = new PduPart();
                            Integer charset = miuiPduPersister.getIntegerFromPartColumn(c, 1);
                            if (charset != null) {
                                part.setCharset(charset.intValue());
                            }
                            byte[] contentDisposition = miuiPduPersister.getByteArrayFromPartColumn(c, 2);
                            if (contentDisposition != null) {
                                part.setContentDisposition(contentDisposition);
                            }
                            byte[] contentId = miuiPduPersister.getByteArrayFromPartColumn(c, 3);
                            if (contentId != null) {
                                part.setContentId(contentId);
                            }
                            byte[] contentLocation = miuiPduPersister.getByteArrayFromPartColumn(c, 4);
                            if (contentLocation != null) {
                                part.setContentLocation(contentLocation);
                            }
                            byte[] contentType = miuiPduPersister.getByteArrayFromPartColumn(c, 5);
                            if (contentType == null) {
                                throw new MmsException("Content-Type must be set.");
                            }
                            part.setContentType(contentType);
                            byte[] fileName3 = miuiPduPersister.getByteArrayFromPartColumn(c, 6);
                            if (fileName3 != null) {
                                parts = parts2;
                                fileName = fileName3;
                                try {
                                    part.setFilename(fileName);
                                } catch (Throwable th) {
                                    th = th;
                                }
                            } else {
                                parts = parts2;
                                fileName = fileName3;
                            }
                            byte[] name = miuiPduPersister.getByteArrayFromPartColumn(c, 7);
                            if (name != null) {
                                fileName2 = name;
                                part.setName(fileName2);
                            } else {
                                fileName2 = name;
                            }
                            long partId = c.getLong(0);
                            int partCount2 = partCount;
                            Uri partURI = Uri.parse("content://mms/part/" + partId);
                            part.setDataUri(partURI);
                            String type = toIsoString(contentType);
                            if (ContentType.isImageType(type)) {
                                partIdx = partIdx;
                            } else if (ContentType.isAudioType(type)) {
                                partIdx = partIdx;
                            } else if (ContentType.isVideoType(type)) {
                                partIdx = partIdx;
                            } else {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                InputStream is = null;
                                if ("text/plain".equals(type) || "application/smil".equals(type) || "text/html".equals(type)) {
                                    String text = c.getString(8);
                                    byte[] blob = new EncodedStringValue(text != null ? text : "").getTextString();
                                    baos.write(blob, 0, blob.length);
                                } else {
                                    try {
                                        is = miuiPduPersister.mContentResolver.openInputStream(partURI);
                                        try {
                                            byte[] buffer = new byte[DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER];
                                            try {
                                                int len = is.read(buffer);
                                                while (len >= 0) {
                                                    String type2 = type;
                                                    ByteArrayOutputStream baos2 = baos;
                                                    int partIdx2 = partIdx;
                                                    try {
                                                        baos2.write(buffer, 0, len);
                                                        len = is.read(buffer);
                                                        partIdx = partIdx2;
                                                        baos = baos2;
                                                        type = type2;
                                                    } catch (IOException e) {
                                                        e = e;
                                                        is = is;
                                                        try {
                                                            Log.e(TAG, "Failed to load part data", e);
                                                            c.close();
                                                            throw new MmsException(e);
                                                        } catch (Throwable e2) {
                                                            iOException = e2;
                                                            if (is != null) {
                                                                throw iOException;
                                                            }
                                                            try {
                                                                is.close();
                                                                throw iOException;
                                                            } catch (IOException e3) {
                                                                Log.e(TAG, "Failed to close stream", e3);
                                                                throw iOException;
                                                            }
                                                        }
                                                    } catch (Throwable th2) {
                                                        is = is;
                                                        iOException = th2;
                                                        if (is != null) {
                                                            throw iOException;
                                                        }
                                                        is.close();
                                                        throw iOException;
                                                    }
                                                }
                                                baos = baos;
                                                partIdx = partIdx;
                                                if (is != null) {
                                                    try {
                                                        is.close();
                                                    } catch (IOException e4) {
                                                        Log.e(TAG, "Failed to close stream", e4);
                                                    }
                                                }
                                            } catch (IOException e5) {
                                                e = e5;
                                                is = is;
                                            } catch (Throwable th3) {
                                                is = is;
                                                iOException = th3;
                                            }
                                        } catch (IOException e6) {
                                            e = e6;
                                        } catch (Throwable th4) {
                                            iOException = th4;
                                        }
                                    } catch (IOException e7) {
                                        e = e7;
                                    } catch (Throwable th5) {
                                        iOException = th5;
                                    }
                                }
                                part.setData(baos.toByteArray());
                            }
                            parts[partIdx] = part;
                            miuiPduPersister = this;
                            parts2 = parts;
                            partCount = partCount2;
                            partIdx++;
                        } catch (Throwable th6) {
                            th = th6;
                        }
                    }
                    PduPart[] parts3 = parts2;
                    if (c != null) {
                        c.close();
                    }
                    return parts3;
                }
            } catch (Throwable th7) {
                th = th7;
            }
            if (c != null) {
                c.close();
            }
            throw th;
        }
        if (c == null) {
            return null;
        }
        c.close();
        return null;
    }

    private void loadAddress(long msgId, PduHeaders headers) {
        Cursor c = SqliteWrapper.query(this.mContext, this.mContentResolver, Uri.parse("content://mms/" + msgId + "/addr"), new String[]{"address", "charset", "type"}, (String) null, (String[]) null, (String) null);
        if (c != null) {
            while (c.moveToNext()) {
                try {
                    String addr = c.getString(0);
                    if (!TextUtils.isEmpty(addr)) {
                        int addrType = c.getInt(2);
                        switch (addrType) {
                            case 129:
                            case DefaultSimManager.EVENT_VOLTE_TEMP_DDS /* 130 */:
                            case 151:
                                headers.appendEncodedStringValue(new EncodedStringValue(c.getInt(1), getBytes(addr)), addrType);
                                break;
                            case 137:
                                headers.setEncodedStringValue(new EncodedStringValue(c.getInt(1), getBytes(addr)), addrType);
                                break;
                            default:
                                Log.e(TAG, "Unknown address type: " + addrType);
                                break;
                        }
                    }
                } catch (Throwable th) {
                    c.close();
                    throw th;
                }
            }
            c.close();
        }
    }

    public GenericPdu load(Uri uri) throws Throwable {
        PduCacheEntry cacheEntry;
        GenericPdu pdu;
        try {
            synchronized (PDU_CACHE_INSTANCE) {
                try {
                    if (PDU_CACHE_INSTANCE.isUpdating(uri)) {
                        try {
                            PDU_CACHE_INSTANCE.wait();
                        } catch (InterruptedException e) {
                            Log.e(TAG, "load: ", e);
                        }
                        PduCacheEntry cacheEntry2 = (PduCacheEntry) PDU_CACHE_INSTANCE.get(uri);
                        if (cacheEntry2 != null) {
                            GenericPdu pdu2 = cacheEntry2.getPdu();
                            synchronized (PDU_CACHE_INSTANCE) {
                                if (0 != 0) {
                                    PDU_CACHE_INSTANCE.put(uri, new PduCacheEntry((GenericPdu) null, 0, -1L));
                                    PDU_CACHE_INSTANCE.setUpdating(uri, false);
                                    PDU_CACHE_INSTANCE.notifyAll();
                                } else {
                                    PDU_CACHE_INSTANCE.setUpdating(uri, false);
                                    PDU_CACHE_INSTANCE.notifyAll();
                                }
                                throw th;
                            }
                            return pdu2;
                        }
                        cacheEntry = cacheEntry2;
                    } else {
                        cacheEntry = null;
                    }
                    try {
                        PDU_CACHE_INSTANCE.setUpdating(uri, true);
                        try {
                            Cursor c = SqliteWrapper.query(this.mContext, this.mContentResolver, uri, PDU_PROJECTION, (String) null, (String[]) null, (String) null);
                            PduHeaders headers = new PduHeaders();
                            long msgId = ContentUris.parseId(uri);
                            if (c != null) {
                                try {
                                    if (c.getCount() == 1 && c.moveToFirst()) {
                                        int msgBox = c.getInt(1);
                                        long threadId = c.getLong(2);
                                        Set<Map.Entry<Integer, Integer>> set = ENCODED_STRING_COLUMN_INDEX_MAP.entrySet();
                                        for (Map.Entry<Integer, Integer> e2 : set) {
                                            setEncodedStringValueToHeaders(c, e2.getValue().intValue(), headers, e2.getKey().intValue());
                                        }
                                        Set<Map.Entry<Integer, Integer>> set2 = TEXT_STRING_COLUMN_INDEX_MAP.entrySet();
                                        for (Map.Entry<Integer, Integer> e3 : set2) {
                                            setTextStringToHeaders(c, e3.getValue().intValue(), headers, e3.getKey().intValue());
                                        }
                                        Set<Map.Entry<Integer, Integer>> set3 = OCTET_COLUMN_INDEX_MAP.entrySet();
                                        for (Map.Entry<Integer, Integer> e4 : set3) {
                                            setOctetToHeaders(c, e4.getValue().intValue(), headers, e4.getKey().intValue());
                                        }
                                        Set<Map.Entry<Integer, Integer>> set4 = LONG_COLUMN_INDEX_MAP.entrySet();
                                        for (Map.Entry<Integer, Integer> e5 : set4) {
                                            setLongToHeaders(c, e5.getValue().intValue(), headers, e5.getKey().intValue());
                                        }
                                        if (c != null) {
                                            c.close();
                                        }
                                        if (msgId == -1) {
                                            throw new MmsException("Error! ID of the message: -1.");
                                        }
                                        loadAddress(msgId, headers);
                                        int msgType = headers.getOctet(140);
                                        PduBody body = new PduBody();
                                        if (msgType == 132 || msgType == 128) {
                                            PduPart[] parts = loadParts(msgId);
                                            if (parts != null) {
                                                int partsNum = parts.length;
                                                int msgType2 = 0;
                                                while (msgType2 < partsNum) {
                                                    int i = msgType2;
                                                    body.addPart(parts[i]);
                                                    msgType2 = i + 1;
                                                }
                                            }
                                        }
                                        switch (msgType) {
                                            case DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY /* 128 */:
                                                GenericPdu pdu3 = new SendReq(headers, body);
                                                pdu = pdu3;
                                                break;
                                            case 129:
                                            case 137:
                                            case 138:
                                            case 139:
                                            case 140:
                                            case 141:
                                            case 142:
                                            case 143:
                                            case 144:
                                            case 145:
                                            case 146:
                                            case 147:
                                            case 148:
                                            case 149:
                                            case 150:
                                            case 151:
                                                throw new MmsException("Unsupported PDU type: " + Integer.toHexString(msgType));
                                            case DefaultSimManager.EVENT_VOLTE_TEMP_DDS /* 130 */:
                                                GenericPdu pdu4 = new NotificationInd(headers);
                                                pdu = pdu4;
                                                break;
                                            case 131:
                                                GenericPdu pdu5 = new NotifyRespInd(headers);
                                                pdu = pdu5;
                                                break;
                                            case 132:
                                                GenericPdu pdu6 = new RetrieveConf(headers, body);
                                                pdu = pdu6;
                                                break;
                                            case 133:
                                                GenericPdu pdu7 = new AcknowledgeInd(headers);
                                                pdu = pdu7;
                                                break;
                                            case MiRilHookBase.EVENT_OEMHOOK_DSDA_HW_MBN_CFG /* 134 */:
                                                GenericPdu pdu8 = new DeliveryInd(headers);
                                                pdu = pdu8;
                                                break;
                                            case 135:
                                                GenericPdu pdu9 = new ReadRecInd(headers);
                                                pdu = pdu9;
                                                break;
                                            case 136:
                                                GenericPdu pdu10 = new ReadOrigInd(headers);
                                                pdu = pdu10;
                                                break;
                                            default:
                                                throw new MmsException("Unrecognized PDU type: " + Integer.toHexString(msgType));
                                        }
                                        synchronized (PDU_CACHE_INSTANCE) {
                                            PDU_CACHE_INSTANCE.put(uri, new PduCacheEntry(pdu, msgBox, threadId));
                                            PDU_CACHE_INSTANCE.setUpdating(uri, false);
                                            PDU_CACHE_INSTANCE.notifyAll();
                                        }
                                        return pdu;
                                    }
                                } catch (Throwable th) {
                                    if (c != null) {
                                        c.close();
                                    }
                                    throw th;
                                }
                            }
                            throw new MmsException("Bad uri: " + uri);
                        } catch (Throwable th2) {
                            th = th2;
                            synchronized (PDU_CACHE_INSTANCE) {
                                if (0 != 0) {
                                    PDU_CACHE_INSTANCE.put(uri, new PduCacheEntry((GenericPdu) null, 0, -1L));
                                    PDU_CACHE_INSTANCE.setUpdating(uri, false);
                                    PDU_CACHE_INSTANCE.notifyAll();
                                } else {
                                    PDU_CACHE_INSTANCE.setUpdating(uri, false);
                                    PDU_CACHE_INSTANCE.notifyAll();
                                }
                                throw th;
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                }
            }
        } catch (Throwable th5) {
            th = th5;
        }
    }

    private void persistAddress(ArrayList<ContentProviderOperation> operations, long msgId, int type, EncodedStringValue[] array) {
        ContentValues values = new ContentValues(3);
        for (EncodedStringValue addr : array) {
            values.clear();
            values.put("address", toIsoString(addr.getTextString()));
            values.put("charset", Integer.valueOf(addr.getCharacterSet()));
            values.put("type", Integer.valueOf(type));
            Uri uri = Uri.parse("content://mms/" + msgId + "/addr");
            operations.add(ContentProviderOperation.newInsert(uri).withValues(values).build());
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.google.android.mms.MmsException */
    public Uri persistPart(PduPart part, long msgId) throws Throwable {
        Uri uri = Uri.parse("content://mms/" + msgId + "/part");
        ContentValues values = new ContentValues(8);
        int charset = part.getCharset();
        if (charset != 0) {
            values.put("chset", Integer.valueOf(charset));
        }
        if (part.getContentType() != null) {
            String contentType = toIsoString(part.getContentType());
            if ("text/x-vCard".equalsIgnoreCase(contentType)) {
                Log.e(TAG, contentType + " -> text/x-vCard");
                contentType = "text/x-vCard";
            }
            if (part.getContentLocation() != null && ("text/plain".equalsIgnoreCase(contentType) || "application/oct-stream".equalsIgnoreCase(contentType))) {
                String cl = new String(part.getContentLocation());
                if (cl.endsWith(".vcf") || cl.endsWith(".VCF")) {
                    Log.e(TAG, contentType + " -> text/x-vCard");
                    contentType = "text/x-vCard";
                }
                if (cl.endsWith(".ogg") || cl.endsWith(".OGG")) {
                    Log.e(TAG, contentType + " -> application/ogg");
                    contentType = "application/ogg";
                }
            }
            if (part.getContentLocation() != null && "text/plain".equalsIgnoreCase(contentType)) {
                String cl2 = new String(part.getContentLocation());
                if ((cl2.endsWith(".txt") || cl2.endsWith(".TXT")) && part.getCharset() == 0) {
                    Log.i(TAG, "Set default text charset to UTF_8");
                    values.put("chset", (Integer) 106);
                }
            }
            if ("image/jpg".equals(contentType)) {
                contentType = "image/jpeg";
            }
            values.put(ModemUtils.PROP_CERT_TYPE_CT, contentType);
            if ("application/smil".equals(contentType)) {
                values.put("seq", (Integer) (-1));
            }
            if (part.getFilename() != null) {
                String fileName = toIsoString(part.getFilename());
                values.put("fn", fileName);
            }
            if (part.getName() != null) {
                String name = toIsoString(part.getName());
                values.put(MiuiIccProvider.STR_NAME, name);
            }
            if (part.getContentDisposition() != null) {
                Object value = toIsoString(part.getContentDisposition());
                values.put("cd", (String) value);
            }
            if (part.getContentId() != null) {
                Object value2 = toIsoString(part.getContentId());
                values.put("cid", (String) value2);
            }
            if (part.getContentLocation() != null) {
                Object value3 = toIsoString(part.getContentLocation());
                values.put("cl", (String) value3);
            }
            Uri res = SqliteWrapper.insert(this.mContext, this.mContentResolver, uri, values);
            if (res == null) {
                throw new MmsException("Failed to persist part, return null.");
            }
            persistData(part, res, contentType);
            part.setDataUri(res);
            return res;
        }
        throw new MmsException("MIME type of the part must be set.");
    }

    /* JADX WARN: Code duplicated, block: B:163:0x0370  */
    /* JADX WARN: Code duplicated, block: B:169:0x033a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:173:0x0355 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:203:? A[SYNTHETIC] */
    private void persistData(PduPart part, Uri uri, String contentType) throws Throwable {
        Throwable th;
        Uri dataUri;
        OutputStream os = null;
        InputStream is = null;
        DrmConvertSession drmConvertSession = null;
        String path = null;
        try {
            try {
                byte[] data = part.getData();
                if ("text/plain".equals(contentType) || "application/smil".equals(contentType) || "text/html".equals(contentType)) {
                    ContentValues cv = new ContentValues();
                    if (data == null) {
                        data = new String("").getBytes("utf-8");
                    }
                    cv.put("text", new EncodedStringValue(data).getString());
                    if (this.mContentResolver.update(uri, cv, null, null) != 1) {
                        throw new MmsException("unable to update " + uri.toString());
                    }
                } else {
                    boolean isDrm = DownloadDrmHelper.isDrmConvertNeeded(contentType);
                    if (isDrm) {
                        if (uri != null) {
                            try {
                                path = convertUriToPath(this.mContext, uri);
                                File f = new File(path);
                                if (f.length() > 0) {
                                    if (0 != 0) {
                                        try {
                                            os.close();
                                        } catch (IOException e) {
                                            Log.e(TAG, "IOException while closing: " + ((Object) null), e);
                                        }
                                    }
                                    if (0 != 0) {
                                        try {
                                            is.close();
                                        } catch (IOException e2) {
                                            Log.e(TAG, "IOException while closing: " + ((Object) null), e2);
                                        }
                                    }
                                    if (0 != 0) {
                                        drmConvertSession.close(path);
                                        File f2 = new File(path);
                                        ContentValues values = new ContentValues(0);
                                        SqliteWrapper.update(this.mContext, this.mContentResolver, Uri.parse("content://mms/resetFilePerm/" + f2.getName()), values, (String) null, (String[]) null);
                                        return;
                                    }
                                    return;
                                }
                            } catch (Exception e3) {
                                Log.e(TAG, "Can't get file info for: " + part.getDataUri(), e3);
                            }
                        }
                        drmConvertSession = DrmConvertSession.open(this.mContext, contentType);
                        if (drmConvertSession == null) {
                            throw new MmsException("Mimetype " + contentType + " can not be converted.");
                        }
                    }
                    os = this.mContentResolver.openOutputStream(uri);
                    if (data == null) {
                        Uri dataUri2 = part.getDataUri();
                        if (dataUri2 == null || dataUri2 == uri) {
                            try {
                                Log.w(TAG, "Can't find data for this part.");
                                if (os != null) {
                                    try {
                                        os.close();
                                    } catch (IOException e4) {
                                        Log.e(TAG, "IOException while closing: " + os, e4);
                                    }
                                }
                                if (0 != 0) {
                                    try {
                                        is.close();
                                    } catch (IOException e5) {
                                        Log.e(TAG, "IOException while closing: " + ((Object) null), e5);
                                    }
                                }
                                if (drmConvertSession != null) {
                                    drmConvertSession.close(path);
                                    File f3 = new File(path);
                                    ContentValues values2 = new ContentValues(0);
                                    SqliteWrapper.update(this.mContext, this.mContentResolver, Uri.parse("content://mms/resetFilePerm/" + f3.getName()), values2, (String) null, (String[]) null);
                                    return;
                                }
                                return;
                            } catch (FileNotFoundException e6) {
                                e = e6;
                            } catch (IOException e7) {
                                e = e7;
                                Log.e(TAG, "Failed to read/write data.", e);
                                throw new MmsException(e);
                            } catch (Throwable th2) {
                                th = th2;
                                if (os != null) {
                                    try {
                                        os.close();
                                    } catch (IOException e8) {
                                        Log.e(TAG, "IOException while closing: " + os, e8);
                                    }
                                }
                                if (is != null) {
                                    try {
                                        is.close();
                                    } catch (IOException e9) {
                                        Log.e(TAG, "IOException while closing: " + is, e9);
                                    }
                                }
                                if (drmConvertSession == null) {
                                    throw th;
                                }
                                drmConvertSession.close(path);
                                File f4 = new File(path);
                                ContentValues values3 = new ContentValues(0);
                                SqliteWrapper.update(this.mContext, this.mContentResolver, Uri.parse("content://mms/resetFilePerm/" + f4.getName()), values3, (String) null, (String[]) null);
                                throw th;
                            }
                        } else {
                            try {
                                is = this.mContentResolver.openInputStream(dataUri2);
                                try {
                                    byte[] buffer = new byte[8192];
                                    while (true) {
                                        int len = is.read(buffer);
                                        InputStream is2 = is;
                                        if (len == -1) {
                                            is = is2;
                                            break;
                                        }
                                        if (isDrm) {
                                            try {
                                                byte[] convertedData = drmConvertSession.convert(buffer, len);
                                                if (convertedData == null) {
                                                    dataUri = dataUri2;
                                                    throw new MmsException("Error converting drm data.");
                                                }
                                                dataUri = dataUri2;
                                                try {
                                                    os.write(convertedData, 0, convertedData.length);
                                                    is = is2;
                                                    dataUri2 = dataUri;
                                                } catch (FileNotFoundException e10) {
                                                    e = e10;
                                                } catch (IOException e11) {
                                                    e = e11;
                                                    Log.e(TAG, "Failed to read/write data.", e);
                                                    throw new MmsException(e);
                                                } catch (Throwable th3) {
                                                    th = th3;
                                                    is = is2;
                                                    if (os != null) {
                                                        os.close();
                                                    }
                                                    if (is != null) {
                                                        is.close();
                                                    }
                                                    if (drmConvertSession == null) {
                                                        throw th;
                                                    }
                                                    drmConvertSession.close(path);
                                                    File f5 = new File(path);
                                                    ContentValues values4 = new ContentValues(0);
                                                    SqliteWrapper.update(this.mContext, this.mContentResolver, Uri.parse("content://mms/resetFilePerm/" + f5.getName()), values4, (String) null, (String[]) null);
                                                    throw th;
                                                }
                                            } catch (FileNotFoundException e12) {
                                                e = e12;
                                            } catch (IOException e13) {
                                                e = e13;
                                            } catch (Throwable th4) {
                                                th = th4;
                                                is = is2;
                                            }
                                            Log.e(TAG, "Failed to open Input/Output stream.", e);
                                            throw new MmsException(e);
                                        }
                                        try {
                                            os.write(buffer, 0, len);
                                            dataUri = dataUri2;
                                            is = is2;
                                            dataUri2 = dataUri;
                                        } catch (FileNotFoundException e14) {
                                            e = e14;
                                        } catch (IOException e15) {
                                            e = e15;
                                            Log.e(TAG, "Failed to read/write data.", e);
                                            throw new MmsException(e);
                                        } catch (Throwable th5) {
                                            th = th5;
                                            is = is2;
                                            if (os != null) {
                                                os.close();
                                            }
                                            if (is != null) {
                                                is.close();
                                            }
                                            if (drmConvertSession == null) {
                                                throw th;
                                            }
                                            drmConvertSession.close(path);
                                            File f6 = new File(path);
                                            ContentValues values5 = new ContentValues(0);
                                            SqliteWrapper.update(this.mContext, this.mContentResolver, Uri.parse("content://mms/resetFilePerm/" + f6.getName()), values5, (String) null, (String[]) null);
                                            throw th;
                                        }
                                    }
                                } catch (FileNotFoundException e16) {
                                    e = e16;
                                } catch (IOException e17) {
                                    e = e17;
                                } catch (Throwable th6) {
                                    th = th6;
                                }
                            } catch (FileNotFoundException e18) {
                                e = e18;
                            } catch (IOException e19) {
                                e = e19;
                            } catch (Throwable th7) {
                                th = th7;
                            }
                        }
                    } else if (isDrm) {
                        byte[] convertedData2 = drmConvertSession.convert(data, data.length);
                        if (convertedData2 == null) {
                            throw new MmsException("Error converting drm data.");
                        }
                        os.write(convertedData2, 0, convertedData2.length);
                    } else {
                        os.write(data);
                    }
                }
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e20) {
                        Log.e(TAG, "IOException while closing: " + os, e20);
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e21) {
                        Log.e(TAG, "IOException while closing: " + is, e21);
                    }
                }
                if (drmConvertSession != null) {
                    drmConvertSession.close(path);
                    File f7 = new File(path);
                    ContentValues values6 = new ContentValues(0);
                    SqliteWrapper.update(this.mContext, this.mContentResolver, Uri.parse("content://mms/resetFilePerm/" + f7.getName()), values6, (String) null, (String[]) null);
                }
            } catch (Throwable th8) {
                th = th8;
            }
        } catch (FileNotFoundException e22) {
            e = e22;
        } catch (IOException e23) {
            e = e23;
        }
    }

    /* JADX WARN: Code duplicated, block: B:43:0x0084  */
    /* JADX WARN: Code duplicated, block: B:54:? A[SYNTHETIC] */
    public static String convertUriToPath(Context context, Uri uri) {
        SQLiteException e;
        if (uri == null) {
            return null;
        }
        String scheme = uri.getScheme();
        if (scheme == null || scheme.equals("") || scheme.equals("file")) {
            return uri.getPath();
        }
        if (scheme.equals("http")) {
            return uri.toString();
        }
        if (!scheme.equals("content")) {
            throw new IllegalArgumentException("Given Uri scheme is not supported");
        }
        String[] projection = {"_data"};
        Cursor cursor = null;
        try {
            try {
                try {
                    Cursor cursor2 = context.getContentResolver().query(uri, projection, null, null, null);
                    if (cursor2 == null || cursor2.getCount() == 0 || !cursor2.moveToFirst()) {
                        throw new IllegalArgumentException("Given Uri could not be found in media store");
                    }
                    int pathIndex = cursor2.getColumnIndexOrThrow("_data");
                    String path = cursor2.getString(pathIndex);
                    if (cursor2 == null) {
                        return path;
                    }
                    cursor2.close();
                    return path;
                } catch (SQLiteException e2) {
                    throw new IllegalArgumentException("Given Uri is not formatted in a way so that it can be found in media store.");
                }
            } catch (Throwable th) {
                e = th;
                if (0 != 0) {
                    throw e;
                }
                cursor.close();
                throw e;
            }
        } catch (SQLiteException e3) {
        } catch (Throwable th2) {
            e = th2;
            if (0 != 0) {
                throw e;
            }
            cursor.close();
            throw e;
        }
    }

    private void updateAddress(long msgId, int type, EncodedStringValue[] array) {
        ArrayList<ContentProviderOperation> operations = Lists.newArrayList();
        operations.add(ContentProviderOperation.newDelete(Uri.parse("content://mms/" + msgId + "/addr")).withSelection("type=" + type, null).build());
        persistAddress(operations, msgId, type, array);
        try {
            this.mContentResolver.applyBatch("mms", operations);
        } catch (OperationApplicationException e) {
            Log.e(TAG, "Error while applying batch", e);
        } catch (RemoteException e2) {
            Log.e(TAG, "Error while applying batch", e2);
        }
    }

    public void updateHeaders(Uri uri, SendReq sendReq) {
        EncodedStringValue[] array;
        PduHeaders headers;
        int[] iArr;
        int i;
        synchronized (PDU_CACHE_INSTANCE) {
            if (PDU_CACHE_INSTANCE.isUpdating(uri)) {
                try {
                    PDU_CACHE_INSTANCE.wait();
                } catch (InterruptedException e) {
                    Log.e(TAG, "updateHeaders: ", e);
                }
            }
        }
        PDU_CACHE_INSTANCE.purge(uri);
        ContentValues values = new ContentValues(10);
        byte[] contentType = sendReq.getContentType();
        if (contentType != null) {
            values.put("ct_t", toIsoString(contentType));
        }
        long date = sendReq.getDate();
        if (date != -1) {
            values.put("date", Long.valueOf(date));
        }
        int deliveryReport = sendReq.getDeliveryReport();
        if (deliveryReport != 0) {
            values.put("d_rpt", Integer.valueOf(deliveryReport));
        }
        long expiry = sendReq.getExpiry();
        if (expiry != -1) {
            values.put("exp", Long.valueOf(expiry));
        }
        byte[] msgClass = sendReq.getMessageClass();
        if (msgClass != null) {
            values.put("m_cls", toIsoString(msgClass));
        }
        int priority = sendReq.getPriority();
        if (priority != 0) {
            values.put("pri", Integer.valueOf(priority));
        }
        int readReport = sendReq.getReadReport();
        if (readReport != 0) {
            values.put("rr", Integer.valueOf(readReport));
        }
        byte[] transId = sendReq.getTransactionId();
        if (transId != null) {
            values.put("tr_id", toIsoString(transId));
        }
        EncodedStringValue subject = sendReq.getSubject();
        if (subject != null) {
            values.put("sub", toIsoString(subject.getTextString()));
            values.put("sub_cs", Integer.valueOf(subject.getCharacterSet()));
        } else {
            values.put("sub", "");
        }
        long messageSize = sendReq.getMessageSize();
        if (messageSize > 0) {
            values.put("m_size", Long.valueOf(messageSize));
        }
        PduHeaders headers2 = sendReq.getPduHeaders();
        HashSet<String> recipients = new HashSet<>();
        int[] iArr2 = ADDRESS_FIELDS;
        int length = iArr2.length;
        int addrType = 0;
        while (addrType < length) {
            int i2 = addrType;
            int addrType2 = iArr2[i2];
            EncodedStringValue[] array2 = null;
            if (addrType2 == 137) {
                EncodedStringValue v = headers2.getEncodedStringValue(addrType2);
                if (v != null) {
                    EncodedStringValue[] array3 = {v};
                    array2 = array3;
                }
                array = array2;
            } else {
                EncodedStringValue[] array4 = headers2.getEncodedStringValues(addrType2);
                array = array4;
            }
            if (array != null) {
                iArr = iArr2;
                i = length;
                long msgId = ContentUris.parseId(uri);
                updateAddress(msgId, addrType2, array);
                headers = headers2;
                if (addrType2 == 151) {
                    for (EncodedStringValue v2 : array) {
                        if (v2 != null) {
                            recipients.add(v2.getString());
                        }
                    }
                }
            } else {
                headers = headers2;
                iArr = iArr2;
                i = length;
            }
            addrType = i2 + 1;
            iArr2 = iArr;
            length = i;
            headers2 = headers;
        }
        if (!recipients.isEmpty()) {
            long threadId = Telephony.Threads.getOrCreateThreadId(this.mContext, recipients);
            values.put("thread_id", Long.valueOf(threadId));
        }
        SqliteWrapper.update(this.mContext, this.mContentResolver, uri, values, (String) null, (String[]) null);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.google.android.mms.MmsException */
    private void updatePart(Uri uri, PduPart part) throws Throwable {
        ContentValues values = new ContentValues(7);
        int charset = part.getCharset();
        if (charset != 0) {
            values.put("chset", Integer.valueOf(charset));
        }
        if (part.getContentType() == null) {
            throw new MmsException("MIME type of the part must be set.");
        }
        String contentType = toIsoString(part.getContentType());
        values.put(ModemUtils.PROP_CERT_TYPE_CT, contentType);
        if (part.getFilename() != null) {
            String fileName = new String(part.getFilename());
            values.put("fn", fileName);
        }
        if (part.getName() != null) {
            String name = new String(part.getName());
            values.put(MiuiIccProvider.STR_NAME, name);
        }
        Object value = null;
        if (part.getContentDisposition() != null) {
            value = toIsoString(part.getContentDisposition());
            values.put("cd", (String) value);
        }
        if (part.getContentId() != null) {
            value = toIsoString(part.getContentId());
            values.put("cid", (String) value);
        }
        if (part.getContentLocation() != null) {
            Object value2 = toIsoString(part.getContentLocation());
            values.put("cl", (String) value2);
        }
        SqliteWrapper.update(this.mContext, this.mContentResolver, uri, values, (String) null, (String[]) null);
        if (part.getData() != null || uri != part.getDataUri()) {
            persistData(part, uri, contentType);
        }
    }

    public void updateParts(Uri uri, PduBody body) throws MmsException {
        try {
            synchronized (PDU_CACHE_INSTANCE) {
                if (PDU_CACHE_INSTANCE.isUpdating(uri)) {
                    try {
                        PDU_CACHE_INSTANCE.wait();
                    } catch (InterruptedException e) {
                        Log.e(TAG, "updateParts: ", e);
                    }
                    PduCacheEntry cacheEntry = (PduCacheEntry) PDU_CACHE_INSTANCE.get(uri);
                    if (cacheEntry != null) {
                        cacheEntry.getPdu().setBody(body);
                    }
                    PDU_CACHE_INSTANCE.setUpdating(uri, true);
                } else {
                    PDU_CACHE_INSTANCE.setUpdating(uri, true);
                }
                throw th;
            }
            ArrayList<PduPart> toBeCreated = new ArrayList<>();
            HashMap<Uri, PduPart> toBeUpdated = new HashMap<>();
            int partsNum = body.getPartsNum();
            StringBuilder filter = new StringBuilder().append('(');
            for (int i = 0; i < partsNum; i++) {
                PduPart part = body.getPart(i);
                Uri partUri = part.getDataUri();
                if (partUri == null || !partUri.getAuthority().startsWith("mms")) {
                    toBeCreated.add(part);
                } else {
                    toBeUpdated.put(partUri, part);
                    if (filter.length() > 1) {
                        filter.append(" AND ");
                    }
                    filter.append(MiuiIccProvider.STR_ID);
                    filter.append("!=");
                    DatabaseUtils.appendEscapedSQLString(filter, partUri.getLastPathSegment());
                }
            }
            filter.append(')');
            long msgId = ContentUris.parseId(uri);
            SqliteWrapper.delete(this.mContext, this.mContentResolver, Uri.parse(Telephony.Mms.CONTENT_URI + "/" + msgId + "/part"), filter.length() > 2 ? filter.toString() : null, (String[]) null);
            Iterator<PduPart> it = toBeCreated.iterator();
            while (it.hasNext()) {
                persistPart(it.next(), msgId);
            }
            for (Map.Entry<Uri, PduPart> e2 : toBeUpdated.entrySet()) {
                updatePart(e2.getKey(), e2.getValue());
            }
            synchronized (PDU_CACHE_INSTANCE) {
                PDU_CACHE_INSTANCE.setUpdating(uri, false);
                PDU_CACHE_INSTANCE.notifyAll();
            }
        } catch (Throwable th) {
            synchronized (PDU_CACHE_INSTANCE) {
                PDU_CACHE_INSTANCE.setUpdating(uri, false);
                PDU_CACHE_INSTANCE.notifyAll();
                throw th;
            }
        }
    }

    public Uri persist(GenericPdu pdu, Uri uri, Uri oldUri, long threadId) throws MmsException {
        return persist(pdu, uri, oldUri, threadId, 0);
    }

    public Uri persist(GenericPdu pdu, Uri uri, Uri oldUri, long threadId, int blockType) throws MmsException {
        return persist(pdu, uri, oldUri, threadId, blockType, -1L);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.google.android.mms.MmsException */
    /* JADX WARN: Bottom block not found for handler: all -> 0x0434 */
    /* JADX WARN: Code duplicated, block: B:108:0x02bd  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.net.Uri persist(com.google.android.mms.pdu.GenericPdu r28, android.net.Uri r29, android.net.Uri r30, long r31, int r33, long r34) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1100
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.mms.pdu.MiuiPduPersister.persist(com.google.android.mms.pdu.GenericPdu, android.net.Uri, android.net.Uri, long, int, long):android.net.Uri");
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.google.android.mms.MmsException */
    public Uri move(Uri from, Uri to) throws MmsException {
        long msgId = ContentUris.parseId(from);
        if (msgId == -1) {
            throw new MmsException("Error! ID of the message: -1.");
        }
        Integer msgBox = MESSAGE_BOX_MAP.get(to);
        if (msgBox == null) {
            throw new MmsException("Bad destination, must be one of content://mms/inbox, content://mms/sent, content://mms/drafts, content://mms/outbox, content://mms/temp.");
        }
        ContentValues values = new ContentValues(1);
        values.put("msg_box", msgBox);
        SqliteWrapper.update(this.mContext, this.mContentResolver, from, values, (String) null, (String[]) null);
        return ContentUris.withAppendedId(to, msgId);
    }

    public static String toIsoString(byte[] bytes) {
        try {
            return new String(bytes, "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "ISO_8859_1 must be supported!", e);
            return "";
        }
    }

    public static byte[] getBytes(String data) {
        try {
            return data.getBytes("iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "ISO_8859_1 must be supported!", e);
            return new byte[0];
        }
    }

    public void release() {
        Uri uri = Uri.parse(TEMPORARY_DRM_OBJECT_URI);
        SqliteWrapper.delete(this.mContext, this.mContentResolver, uri, (String) null, (String[]) null);
    }

    public Cursor getPendingMessages(long dueTime) {
        Uri.Builder uriBuilder = Telephony.MmsSms.PendingMessages.CONTENT_URI.buildUpon();
        uriBuilder.appendQueryParameter("protocol", "mms");
        String[] selectionArgs = {String.valueOf(10), String.valueOf(dueTime)};
        return SqliteWrapper.query(this.mContext, this.mContentResolver, uriBuilder.build(), (String[]) null, "err_type < ? AND due_time <= ?", selectionArgs, "due_time");
    }
}
