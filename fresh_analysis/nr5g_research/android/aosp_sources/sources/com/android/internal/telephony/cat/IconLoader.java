package com.android.internal.telephony.cat;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.android.internal.telephony.uicc.IccFileHandler;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class IconLoader extends Handler {
    protected static final int CLUT_ENTRY_SIZE = 3;
    protected static final int CLUT_LOCATION_OFFSET = 4;
    protected static final int EVENT_READ_CLUT_DONE = 3;
    protected static final int EVENT_READ_EF_IMG_RECOED_DONE = 1;
    protected static final int EVENT_READ_ICON_DONE = 2;
    protected static final int STATE_MULTI_ICONS = 2;
    protected static final int STATE_SINGLE_ICON = 1;
    private static HashMap sLoader;
    private static HashMap sThread;
    protected Bitmap mCurrentIcon;
    protected int mCurrentRecordIndex;
    protected Message mEndMsg;
    protected byte[] mIconData;
    protected Bitmap[] mIcons;
    protected HashMap<Integer, Bitmap> mIconsCache;
    protected ImageDescriptor mId;
    protected int mRecordNumber;
    protected int[] mRecordNumbers;
    protected IccFileHandler mSimFH;
    protected int mState;

    private static int bitToBnW(int i) {
        return i == 1 ? -1 : -16777216;
    }

    private static int getMask(int i) {
        switch (i) {
            case 1:
                return 1;
            case 2:
                return 3;
            case 3:
                return 7;
            case 4:
                return 15;
            case 5:
                return 31;
            case 6:
                return 63;
            case 7:
                return 127;
            case 8:
                return 255;
            default:
                return 0;
        }
    }

    public IconLoader(Looper looper, IccFileHandler iccFileHandler) {
        super(looper);
        this.mState = 1;
        this.mId = null;
        this.mCurrentIcon = null;
        this.mEndMsg = null;
        this.mIconData = null;
        this.mRecordNumbers = null;
        this.mCurrentRecordIndex = 0;
        this.mIcons = null;
        this.mIconsCache = null;
        this.mSimFH = iccFileHandler;
        this.mIconsCache = new HashMap<>(50);
    }

    static IconLoader getInstance(Handler handler, IccFileHandler iccFileHandler) {
        HashMap map = sLoader;
        if (map != null && map.containsKey(iccFileHandler)) {
            return (IconLoader) sLoader.get(iccFileHandler);
        }
        if (iccFileHandler == null) {
            return null;
        }
        if (sThread == null) {
            sThread = new HashMap(4);
        }
        HandlerThread handlerThread = new HandlerThread("Cat Icon Loader");
        sThread.put(iccFileHandler, handlerThread);
        handlerThread.start();
        if (sLoader == null) {
            sLoader = new HashMap(4);
        }
        IconLoader iconLoader = new IconLoader(handlerThread.getLooper(), iccFileHandler);
        sLoader.put(iccFileHandler, iconLoader);
        return iconLoader;
    }

    public void loadIcons(int[] iArr, Message message) {
        if (iArr == null || iArr.length == 0 || message == null) {
            return;
        }
        this.mEndMsg = message;
        this.mIcons = new Bitmap[iArr.length];
        this.mRecordNumbers = iArr;
        this.mCurrentRecordIndex = 0;
        this.mState = 2;
        startLoadingIcon(iArr[0]);
    }

    public void loadIcon(int i, Message message) {
        if (message == null) {
            return;
        }
        this.mEndMsg = message;
        this.mState = 1;
        startLoadingIcon(i);
    }

    protected void startLoadingIcon(int i) {
        this.mId = null;
        this.mIconData = null;
        this.mCurrentIcon = null;
        this.mRecordNumber = i;
        HashMap<Integer, Bitmap> map = this.mIconsCache;
        if (map != null && map.containsKey(Integer.valueOf(i))) {
            this.mCurrentIcon = this.mIconsCache.get(Integer.valueOf(i));
            postIcon();
        } else {
            readId();
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        try {
            int i = message.what;
            if (i == 1) {
                Object obj = message.obj;
                if (obj == null) {
                    CatLog.e(this, "load EFimg done: msg.obj is null.");
                    return;
                } else {
                    if (handleImageDescriptor((byte[]) ((AsyncResult) obj).result)) {
                        readIconData();
                        return;
                    }
                    throw new Exception("Unable to parse image descriptor");
                }
            }
            if (i != 2) {
                if (i != 3) {
                    return;
                }
                byte[] bArr = (byte[]) ((AsyncResult) message.obj).result;
                byte[] bArr2 = this.mIconData;
                this.mCurrentIcon = parseToRGB(bArr2, bArr2.length, false, bArr);
                this.mIconsCache.put(Integer.valueOf(this.mRecordNumber), this.mCurrentIcon);
                postIcon();
                return;
            }
            CatLog.d(this, "load icon done");
            byte[] bArr3 = (byte[]) ((AsyncResult) message.obj).result;
            int i2 = this.mId.mCodingScheme;
            if (i2 == 17) {
                this.mCurrentIcon = parseToBnW(bArr3, bArr3.length);
                this.mIconsCache.put(Integer.valueOf(this.mRecordNumber), this.mCurrentIcon);
                postIcon();
            } else if (i2 == 33) {
                this.mIconData = bArr3;
                readClut();
            } else {
                CatLog.d(this, "else  /postIcon ");
                postIcon();
            }
        } catch (Exception e) {
            CatLog.d(this, "Icon load failed");
            e.printStackTrace();
            postIcon();
        }
    }

    protected boolean handleImageDescriptor(byte[] bArr) {
        ImageDescriptor imageDescriptor = ImageDescriptor.parse(bArr, 1);
        this.mId = imageDescriptor;
        return imageDescriptor != null;
    }

    protected void readClut() {
        int i = this.mIconData[3] * 3;
        Message messageObtainMessage = obtainMessage(3);
        IccFileHandler iccFileHandler = this.mSimFH;
        int i2 = this.mId.mImageId;
        byte[] bArr = this.mIconData;
        iccFileHandler.loadEFImgTransparent(i2, bArr[4], bArr[5], i, messageObtainMessage);
    }

    protected void readId() {
        if (this.mRecordNumber < 0) {
            this.mCurrentIcon = null;
            postIcon();
        } else {
            this.mSimFH.loadEFImgLinearFixed(this.mRecordNumber, obtainMessage(1));
        }
    }

    protected void readIconData() {
        Message messageObtainMessage = obtainMessage(2);
        IccFileHandler iccFileHandler = this.mSimFH;
        ImageDescriptor imageDescriptor = this.mId;
        iccFileHandler.loadEFImgTransparent(imageDescriptor.mImageId, 0, 0, imageDescriptor.mLength, messageObtainMessage);
    }

    protected void postIcon() {
        int i = this.mState;
        if (i == 1) {
            Message message = this.mEndMsg;
            message.obj = this.mCurrentIcon;
            message.sendToTarget();
        } else if (i == 2) {
            Bitmap[] bitmapArr = this.mIcons;
            int i2 = this.mCurrentRecordIndex;
            int i3 = i2 + 1;
            this.mCurrentRecordIndex = i3;
            bitmapArr[i2] = this.mCurrentIcon;
            int[] iArr = this.mRecordNumbers;
            if (i3 < iArr.length) {
                startLoadingIcon(iArr[i3]);
                return;
            }
            Message message2 = this.mEndMsg;
            message2.obj = bitmapArr;
            message2.sendToTarget();
        }
    }

    public static Bitmap parseToBnW(byte[] bArr, int i) {
        int i2 = 0;
        int i3 = bArr[0] & 255;
        int i4 = bArr[1] & 255;
        int i5 = i3 * i4;
        int[] iArr = new int[i5];
        int i6 = 2;
        int i7 = 7;
        byte b = 0;
        while (i2 < i5) {
            if (i2 % 8 == 0) {
                int i8 = i6 + 1;
                byte b2 = bArr[i6];
                i6 = i8;
                b = b2;
                i7 = 7;
            }
            iArr[i2] = bitToBnW((b >> i7) & 1);
            i2++;
            i7--;
        }
        if (i2 != i5) {
            CatLog.d("IconLoader", "parseToBnW; size error");
        }
        return Bitmap.createBitmap(iArr, i3, i4, Bitmap.Config.ARGB_8888);
    }

    public static Bitmap parseToRGB(byte[] bArr, int i, boolean z, byte[] bArr2) {
        int i2 = 0;
        int i3 = bArr[0] & 255;
        int i4 = bArr[1] & 255;
        char c = 2;
        int i5 = bArr[2] & 255;
        int i6 = 3;
        int i7 = bArr[3] & 255;
        if (true == z) {
            bArr2[i7 - 1] = 0;
        }
        int i8 = i3 * i4;
        int[] iArr = new int[i8];
        int i9 = 8 - i5;
        byte b = bArr[6];
        int mask = getMask(i5);
        boolean z2 = 8 % i5 == 0;
        int i10 = 7;
        int i11 = i9;
        while (i2 < i8) {
            if (i11 < 0) {
                int i12 = i10 + 1;
                byte b2 = bArr[i10];
                i11 = z2 ? i9 : i11 * (-1);
                i10 = i12;
                b = b2;
            }
            int i13 = ((b >> i11) & mask) * i6;
            iArr[i2] = Color.rgb((int) bArr2[i13], (int) bArr2[i13 + 1], (int) bArr2[i13 + 2]);
            i11 -= i5;
            c = c;
            i2++;
            i6 = 3;
        }
        return Bitmap.createBitmap(iArr, i3, i4, Bitmap.Config.ARGB_8888);
    }

    public void dispose() {
        HashMap map = sThread;
        if (map != null && map.containsKey(this.mSimFH)) {
            HandlerThread handlerThread = (HandlerThread) sThread.get(this.mSimFH);
            if (handlerThread != null) {
                handlerThread.quit();
            }
            sThread.remove(this.mSimFH);
        }
        this.mIconsCache = null;
        HashMap map2 = sLoader;
        if (map2 != null && map2.containsKey(this.mSimFH)) {
            sLoader.remove(this.mSimFH);
        }
        this.mSimFH = null;
    }
}
