package com.android.phone.beidou;

import android.net.Uri;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class BdSmsInfo {
    private Uri fileUri;
    private int idx;
    private Uri keyUri;
    private double latitude;
    private double longitude;
    private String message;
    private String phoneNumber;
    private List reciveNumberList;
    private int slotId;
    private BdSmsType type;
    private int[] uris;

    public enum BdSmsType {
        BD_TEXT,
        BD_PICTURE,
        BD_VOICE
    }

    public BdSmsInfo(int i, int[] iArr, Uri uri, Uri uri2, int i2, List list, String str, BdSmsType bdSmsType) {
        this.idx = i;
        this.uris = iArr;
        this.keyUri = uri;
        this.fileUri = uri2;
        this.slotId = i2;
        this.reciveNumberList = list;
        this.longitude = -1.0d;
        this.latitude = -1.0d;
        this.phoneNumber = str;
        this.type = bdSmsType;
    }

    public BdSmsInfo(int i, int[] iArr, String str, int i2, List list, String str2, BdSmsType bdSmsType) {
        this.idx = i;
        this.uris = iArr;
        this.message = str;
        this.slotId = i2;
        this.reciveNumberList = list;
        this.longitude = -1.0d;
        this.latitude = -1.0d;
        this.phoneNumber = str2;
        this.type = bdSmsType;
    }

    public BdSmsInfo(int i, int[] iArr, String str, int i2, List list, String str2, double d, double d2, BdSmsType bdSmsType) {
        this.idx = i;
        this.uris = iArr;
        this.message = str;
        this.slotId = i2;
        this.reciveNumberList = list;
        this.longitude = d;
        this.latitude = d2;
        this.phoneNumber = str2;
        this.type = bdSmsType;
    }

    public BdSmsInfo(int i, int[] iArr, Uri uri, Uri uri2, int i2, List list, String str, double d, double d2, BdSmsType bdSmsType) {
        this.idx = i;
        this.uris = iArr;
        this.keyUri = uri;
        this.fileUri = uri2;
        this.slotId = i2;
        this.reciveNumberList = list;
        this.longitude = d;
        this.latitude = d2;
        this.phoneNumber = str;
        this.type = bdSmsType;
    }

    public boolean hasLocation() {
        return (this.longitude == -1.0d && this.latitude == -1.0d) ? false : true;
    }

    public int getIdx() {
        return this.idx;
    }

    public String getMessage() {
        return this.message;
    }

    public int getSlotId() {
        return this.slotId;
    }

    public List getReciveNumberList() {
        return this.reciveNumberList;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public Uri getFileUri() {
        return this.fileUri;
    }

    public BdSmsType getType() {
        return this.type;
    }

    public Uri getKeyUri() {
        return this.keyUri;
    }

    public boolean isMms() {
        return BdSmsType.BD_VOICE.equals(this.type) || BdSmsType.BD_PICTURE.equals(this.type);
    }

    public String toString() {
        return "BdSmsInfo{idx=" + this.idx + ", type=" + this.type.toString() + ", uris=" + Arrays.toString(this.uris) + ", slotId=" + this.slotId + '}';
    }
}
