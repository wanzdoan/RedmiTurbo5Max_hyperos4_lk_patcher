package com.android.phone.callcomposer;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.OutcomeReceiver;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.telephony.gba.UaSecurityProtocolIdentifier;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import com.android.phone.R;
import com.android.services.telephony.relay.network.SmartNetworkRelayScore;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public class CallComposerPictureManager {
    public static final Location FAKE_LOCATION;
    public static final String FAKE_SERVER_URL = "https://example.com/FAKE.png";
    public static final String FAKE_SUBJECT = "This is a test call subject";
    private static final String TAG = "CallComposerPictureManager";
    private static final String THREE_GPP_BOOTSTRAPPING = "3GPP-bootstrapping";
    private static ScheduledExecutorService sExecutorService;
    private static final SparseArray sInstances = new SparseArray();
    public static boolean sTestMode = false;
    private final Context mContext;
    private final int mSubscriptionId;
    private final TelephonyManager mTelephonyManager;
    private final HashMap mCachedServerUrls = new HashMap();
    private final HashMap mCachedImages = new HashMap();
    private GbaCredentials mCachedCredentials = null;
    private CallLogProxy mCallLogProxy = new CallLogProxy(this) { // from class: com.android.phone.callcomposer.CallComposerPictureManager.1
    };

    static {
        Location location = new Location("");
        FAKE_LOCATION = location;
        location.setLatitude(35.027526d);
        location.setLongitude(-111.021696d);
        sExecutorService = null;
    }

    public static CallComposerPictureManager getInstance(Context context, int i) {
        CallComposerPictureManager callComposerPictureManager;
        SparseArray sparseArray = sInstances;
        synchronized (sparseArray) {
            try {
                if (sExecutorService == null) {
                    sExecutorService = Executors.newSingleThreadScheduledExecutor();
                }
                if (!sparseArray.contains(i)) {
                    sparseArray.put(i, new CallComposerPictureManager(context, i));
                }
                callComposerPictureManager = (CallComposerPictureManager) sparseArray.get(i);
            } catch (Throwable th) {
                throw th;
            }
        }
        return callComposerPictureManager;
    }

    public static void clearInstances() {
        SparseArray sparseArray = sInstances;
        synchronized (sparseArray) {
            try {
                sparseArray.clear();
                ScheduledExecutorService scheduledExecutorService = sExecutorService;
                if (scheduledExecutorService != null) {
                    scheduledExecutorService.shutdown();
                    sExecutorService = null;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public interface CallLogProxy {
        default void storeCallComposerPictureAsUser(Context context, UserHandle userHandle, InputStream inputStream, Executor executor, OutcomeReceiver outcomeReceiver) {
            CallLog.storeCallComposerPicture(context.createContextAsUser(userHandle, 0), inputStream, executor, outcomeReceiver);
        }
    }

    private CallComposerPictureManager(Context context, int i) {
        this.mContext = context;
        this.mSubscriptionId = i;
        this.mTelephonyManager = ((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(i);
    }

    public void handleUploadToServer(CallComposerPictureTransfer.Factory factory, ImageData imageData, Consumer consumer) {
        if (sTestMode) {
            UUID uuidRandomUUID = UUID.randomUUID();
            this.mCachedImages.put(uuidRandomUUID, imageData);
            this.mCachedServerUrls.put(uuidRandomUUID, FAKE_SERVER_URL);
            consumer.accept(Pair.create(uuidRandomUUID, -1));
            return;
        }
        final PersistableBundle carrierConfig = this.mTelephonyManager.getCarrierConfig();
        String string = carrierConfig.getString("call_composer_picture_server_url_string");
        if (TextUtils.isEmpty(string)) {
            Log.e(TAG, "Call composer upload URL not configured in carrier config");
            consumer.accept(Pair.create(null, 0));
            return;
        }
        UUID uuidRandomUUID2 = UUID.randomUUID();
        imageData.setId(uuidRandomUUID2.toString());
        CallComposerPictureTransfer callComposerPictureTransferCreate = factory.create(this.mContext, this.mSubscriptionId, string, sExecutorService);
        callComposerPictureTransferCreate.setCallback(new AnonymousClass2(consumer, new AtomicBoolean(false), carrierConfig, callComposerPictureTransferCreate, imageData, uuidRandomUUID2));
        Log.i(TAG, "handleUploadToServer , call transfer.uploadPicture.");
        callComposerPictureTransferCreate.uploadPicture(imageData, new GbaCredentialsSupplier() { // from class: com.android.phone.callcomposer.CallComposerPictureManager$$ExternalSyntheticLambda0
            @Override // com.android.phone.callcomposer.GbaCredentialsSupplier
            public final CompletableFuture getCredentials(String str, Executor executor) {
                return this.f$0.lambda$handleUploadToServer$0(carrierConfig, str, executor);
            }
        });
    }

    /* JADX INFO: renamed from: com.android.phone.callcomposer.CallComposerPictureManager$2, reason: invalid class name */
    class AnonymousClass2 implements CallComposerPictureTransfer.PictureCallback {
        final /* synthetic */ Consumer val$callback;
        final /* synthetic */ PersistableBundle val$carrierConfig;
        final /* synthetic */ AtomicBoolean val$hasRetried;
        final /* synthetic */ UUID val$id;
        final /* synthetic */ ImageData val$imageData;
        final /* synthetic */ CallComposerPictureTransfer val$transfer;

        AnonymousClass2(Consumer consumer, AtomicBoolean atomicBoolean, PersistableBundle persistableBundle, CallComposerPictureTransfer callComposerPictureTransfer, ImageData imageData, UUID uuid) {
            this.val$callback = consumer;
            this.val$hasRetried = atomicBoolean;
            this.val$carrierConfig = persistableBundle;
            this.val$transfer = callComposerPictureTransfer;
            this.val$imageData = imageData;
            this.val$id = uuid;
        }

        @Override // com.android.phone.callcomposer.CallComposerPictureTransfer.PictureCallback
        public void onError(int i) {
            Log.i(CallComposerPictureManager.TAG, "handleUploadToServer. onError.");
            this.val$callback.accept(Pair.create(null, Integer.valueOf(i)));
        }

        @Override // com.android.phone.callcomposer.CallComposerPictureTransfer.PictureCallback
        public void onRetryNeeded(final boolean z, long j) {
            Log.i(CallComposerPictureManager.TAG, "handleUploadToServer. onRetryNeeded.");
            if (this.val$hasRetried.getAndSet(true)) {
                Log.e(CallComposerPictureManager.TAG, "Giving up on image upload after one retry.");
                this.val$callback.accept(Pair.create(null, 6));
                return;
            }
            final PersistableBundle persistableBundle = this.val$carrierConfig;
            final GbaCredentialsSupplier gbaCredentialsSupplier = new GbaCredentialsSupplier() { // from class: com.android.phone.callcomposer.CallComposerPictureManager$2$$ExternalSyntheticLambda0
                @Override // com.android.phone.callcomposer.GbaCredentialsSupplier
                public final CompletableFuture getCredentials(String str, Executor executor) {
                    return this.f$0.lambda$onRetryNeeded$0(z, persistableBundle, str, executor);
                }
            };
            ScheduledExecutorService scheduledExecutorService = CallComposerPictureManager.sExecutorService;
            final CallComposerPictureTransfer callComposerPictureTransfer = this.val$transfer;
            final ImageData imageData = this.val$imageData;
            scheduledExecutorService.schedule(new Runnable() { // from class: com.android.phone.callcomposer.CallComposerPictureManager$2$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    callComposerPictureTransfer.uploadPicture(imageData, gbaCredentialsSupplier);
                }
            }, j, TimeUnit.MILLISECONDS);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ CompletableFuture lambda$onRetryNeeded$0(boolean z, PersistableBundle persistableBundle, String str, Executor executor) {
            return CallComposerPictureManager.this.getGbaCredentials(z, persistableBundle, executor);
        }

        @Override // com.android.phone.callcomposer.CallComposerPictureTransfer.PictureCallback
        public void onUploadSuccessful(String str) {
            CallComposerPictureManager.this.clearCachedData();
            CallComposerPictureManager.this.mCachedServerUrls.put(this.val$id, str);
            CallComposerPictureManager.this.mCachedImages.put(this.val$id, this.val$imageData);
            Log.i(CallComposerPictureManager.TAG, "Successfully received url: " + str + " associated with " + this.val$id.toString());
            this.val$callback.accept(Pair.create(this.val$id, -1));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ CompletableFuture lambda$handleUploadToServer$0(PersistableBundle persistableBundle, String str, Executor executor) {
        return getGbaCredentials(false, persistableBundle, executor);
    }

    public void handleDownloadFromServer(CallComposerPictureTransfer.Factory factory, String str, final Consumer consumer) {
        if (sTestMode) {
            ImageData imageData = new ImageData(getPlaceholderPictureAsBytes(), "image/png", null);
            UUID uuidRandomUUID = UUID.randomUUID();
            this.mCachedImages.put(uuidRandomUUID, imageData);
            storeUploadedPictureToCallLog(uuidRandomUUID, new Consumer() { // from class: com.android.phone.callcomposer.CallComposerPictureManager$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    consumer.accept(Pair.create((Uri) obj, -1));
                }
            });
            return;
        }
        final PersistableBundle carrierConfig = this.mTelephonyManager.getCarrierConfig();
        CallComposerPictureTransfer callComposerPictureTransferCreate = factory.create(this.mContext, this.mSubscriptionId, str, sExecutorService);
        callComposerPictureTransferCreate.setCallback(new AnonymousClass3(consumer, new AtomicBoolean(false), carrierConfig, callComposerPictureTransferCreate));
        callComposerPictureTransferCreate.downloadPicture(new GbaCredentialsSupplier() { // from class: com.android.phone.callcomposer.CallComposerPictureManager$$ExternalSyntheticLambda2
            @Override // com.android.phone.callcomposer.GbaCredentialsSupplier
            public final CompletableFuture getCredentials(String str2, Executor executor) {
                return this.f$0.lambda$handleDownloadFromServer$2(carrierConfig, str2, executor);
            }
        });
    }

    /* JADX INFO: renamed from: com.android.phone.callcomposer.CallComposerPictureManager$3, reason: invalid class name */
    class AnonymousClass3 implements CallComposerPictureTransfer.PictureCallback {
        final /* synthetic */ Consumer val$callback;
        final /* synthetic */ PersistableBundle val$carrierConfig;
        final /* synthetic */ AtomicBoolean val$hasRetried;
        final /* synthetic */ CallComposerPictureTransfer val$transfer;

        AnonymousClass3(Consumer consumer, AtomicBoolean atomicBoolean, PersistableBundle persistableBundle, CallComposerPictureTransfer callComposerPictureTransfer) {
            this.val$callback = consumer;
            this.val$hasRetried = atomicBoolean;
            this.val$carrierConfig = persistableBundle;
            this.val$transfer = callComposerPictureTransfer;
        }

        @Override // com.android.phone.callcomposer.CallComposerPictureTransfer.PictureCallback
        public void onError(int i) {
            Log.i(CallComposerPictureManager.TAG, "handleDownloadFromServer. onError.");
            this.val$callback.accept(Pair.create(null, Integer.valueOf(i)));
        }

        @Override // com.android.phone.callcomposer.CallComposerPictureTransfer.PictureCallback
        public void onRetryNeeded(final boolean z, long j) {
            if (this.val$hasRetried.getAndSet(true)) {
                Log.e(CallComposerPictureManager.TAG, "Giving up on image download after one retry.");
                this.val$callback.accept(Pair.create(null, 6));
                return;
            }
            final PersistableBundle persistableBundle = this.val$carrierConfig;
            final GbaCredentialsSupplier gbaCredentialsSupplier = new GbaCredentialsSupplier() { // from class: com.android.phone.callcomposer.CallComposerPictureManager$3$$ExternalSyntheticLambda0
                @Override // com.android.phone.callcomposer.GbaCredentialsSupplier
                public final CompletableFuture getCredentials(String str, Executor executor) {
                    return this.f$0.lambda$onRetryNeeded$0(z, persistableBundle, str, executor);
                }
            };
            ScheduledExecutorService scheduledExecutorService = CallComposerPictureManager.sExecutorService;
            final CallComposerPictureTransfer callComposerPictureTransfer = this.val$transfer;
            scheduledExecutorService.schedule(new Runnable() { // from class: com.android.phone.callcomposer.CallComposerPictureManager$3$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    callComposerPictureTransfer.downloadPicture(gbaCredentialsSupplier);
                }
            }, j, TimeUnit.MILLISECONDS);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ CompletableFuture lambda$onRetryNeeded$0(boolean z, PersistableBundle persistableBundle, String str, Executor executor) {
            return CallComposerPictureManager.this.getGbaCredentials(z, persistableBundle, executor);
        }

        @Override // com.android.phone.callcomposer.CallComposerPictureTransfer.PictureCallback
        public void onDownloadSuccessful(ImageData imageData) {
            CallComposerPictureManager.this.mCallLogProxy.storeCallComposerPictureAsUser(CallComposerPictureManager.this.mContext, UserHandle.CURRENT, new ByteArrayInputStream(imageData.getImageBytes()), CallComposerPictureManager.sExecutorService, new OutcomeReceiver() { // from class: com.android.phone.callcomposer.CallComposerPictureManager.3.1
                @Override // android.os.OutcomeReceiver
                public void onResult(Uri uri) {
                    AnonymousClass3.this.val$callback.accept(Pair.create(uri, -1));
                }

                @Override // android.os.OutcomeReceiver
                public void onError(CallLog.CallComposerLoggingException callComposerLoggingException) {
                    AnonymousClass3.this.val$callback.accept(Pair.create(null, 0));
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ CompletableFuture lambda$handleDownloadFromServer$2(PersistableBundle persistableBundle, String str, Executor executor) {
        return getGbaCredentials(false, persistableBundle, executor);
    }

    public void storeUploadedPictureToCallLog(UUID uuid, final Consumer consumer) {
        String str = TAG;
        Log.i(str, "storeUploadedPictureToCallLog: " + uuid);
        ImageData imageData = (ImageData) this.mCachedImages.get(uuid);
        if (imageData == null) {
            Log.e(str, "No picture associated with uuid " + uuid);
            consumer.accept(null);
            return;
        }
        this.mCallLogProxy.storeCallComposerPictureAsUser(this.mContext, UserHandle.CURRENT, new ByteArrayInputStream(imageData.getImageBytes()), sExecutorService, new OutcomeReceiver(this) { // from class: com.android.phone.callcomposer.CallComposerPictureManager.4
            @Override // android.os.OutcomeReceiver
            public void onResult(Uri uri) {
                consumer.accept(uri);
            }

            @Override // android.os.OutcomeReceiver
            public void onError(CallLog.CallComposerLoggingException callComposerLoggingException) {
                Log.e(CallComposerPictureManager.TAG, "Error logging uploaded image: " + callComposerLoggingException.getErrorCode());
                consumer.accept(null);
            }
        });
    }

    public String getServerUrlForImageId(UUID uuid) {
        return (String) this.mCachedServerUrls.get(uuid);
    }

    public void clearCachedData() {
        this.mCachedServerUrls.clear();
        this.mCachedImages.clear();
    }

    private byte[] getPlaceholderPictureAsBytes() {
        try {
            return readBytes(this.mContext.getResources().openRawResource(R.drawable.cupcake));
        } catch (Exception unused) {
            return new byte[0];
        }
    }

    private static byte[] readBytes(InputStream inputStream) throws Exception {
        int i;
        byte[] bArr = new byte[SmartNetworkRelayScore.TRAFFIC_UNIT];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        do {
            i = inputStream.read(bArr);
            if (i > 0) {
                byteArrayOutputStream.write(bArr, 0, i);
            }
        } while (i > 0);
        return byteArrayOutputStream.toByteArray();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public CompletableFuture getGbaCredentials(boolean z, PersistableBundle persistableBundle, Executor executor) {
        synchronized (this) {
            if (!z) {
                try {
                    GbaCredentials gbaCredentials = this.mCachedCredentials;
                    if (gbaCredentials != null) {
                        return CompletableFuture.completedFuture(gbaCredentials);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            if (z) {
                this.mCachedCredentials = null;
            }
            UaSecurityProtocolIdentifier uaSecurityProtocolIdentifierBuild = new UaSecurityProtocolIdentifier.Builder().setOrg(persistableBundle.getInt("gba_ua_security_organization_int")).setProtocol(persistableBundle.getInt("gba_ua_security_protocol_int")).setTlsCipherSuite(persistableBundle.getInt("gba_ua_tls_cipher_suite_int")).build();
            final CompletableFuture completableFuture = new CompletableFuture();
            this.mTelephonyManager.bootstrapAuthenticationRequest(5, getNafUri(persistableBundle), uaSecurityProtocolIdentifierBuild, z, executor, new TelephonyManager.BootstrapAuthenticationCallback() { // from class: com.android.phone.callcomposer.CallComposerPictureManager.5
                public void onKeysAvailable(byte[] bArr, String str) {
                    GbaCredentials gbaCredentials2 = new GbaCredentials(str, bArr);
                    synchronized (CallComposerPictureManager.this) {
                        CallComposerPictureManager.this.mCachedCredentials = gbaCredentials2;
                    }
                    completableFuture.complete(gbaCredentials2);
                }

                public void onAuthenticationFailure(int i) {
                    Log.e(CallComposerPictureManager.TAG, "GBA auth failed: reason=" + i);
                    completableFuture.complete(null);
                }
            });
            return completableFuture;
        }
    }

    private static Uri getNafUri(PersistableBundle persistableBundle) {
        String str;
        Uri uri = Uri.parse(persistableBundle.getString("call_composer_picture_server_url_string"));
        int i = persistableBundle.getInt("gba_mode_int");
        if (i == 2) {
            str = "3GPP-bootstrapping-uicc";
        } else if (i == 3) {
            str = "3GPP-bootstrapping-digest";
        } else {
            str = THREE_GPP_BOOTSTRAPPING;
        }
        Uri uriBuild = new Uri.Builder().scheme(uri.getScheme()).encodedAuthority(str + "@" + uri.getAuthority()).build();
        Log.i(TAG, "using NAF uri " + uriBuild + " for GBA");
        return uriBuild;
    }

    static ScheduledExecutorService getExecutor() {
        return sExecutorService;
    }

    void setCallLogProxy(CallLogProxy callLogProxy) {
        this.mCallLogProxy = callLogProxy;
    }
}
