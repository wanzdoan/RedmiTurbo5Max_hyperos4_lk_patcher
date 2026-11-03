package com.android.phone.callcomposer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.http.multipart.MultipartEntity;
import com.android.internal.http.multipart.Part;
import com.google.common.net.MediaType;
import gov.nist.javax.sip.header.WWWAuthenticate;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import miuix.reflect.Reflects$$ExternalSyntheticBUOutline0;
import org.xml.sax.InputSource;

/* JADX INFO: loaded from: classes.dex */
public class CallComposerPictureTransfer {
    private static final int DEFAULT_BACKOFF_MILLIS = 1000;
    private static final int ERROR_FORBIDDEN = 3;
    private static final int ERROR_HTTP_TIMEOUT = 1;
    private static final int ERROR_NO_AUTH_REQUIRED = 2;
    private static final int ERROR_UNKNOWN = 0;
    private static final int HTTP_TIMEOUT_MILLIS = 20000;
    private static final String TAG = "CallComposerPictureTransfer";
    private static final String THREE_GPP_GBA = "3gpp-gba";
    private PictureCallback mCallback;
    private final Context mContext;
    private final ExecutorService mExecutorService;
    private ConnectivityManager.NetworkCallback mNetworkCallback;
    private final int mSubscriptionId;
    private final String mUrl;

    public interface PictureCallback {
        default void onDownloadSuccessful(ImageData imageData) {
        }

        default void onError(int i) {
        }

        default void onRetryNeeded(boolean z, long j) {
        }

        default void onUploadSuccessful(String str) {
        }
    }

    public interface Factory {
        default CallComposerPictureTransfer create(Context context, int i, String str, ExecutorService executorService) {
            return new CallComposerPictureTransfer(context, i, str, executorService);
        }
    }

    class NetworkAccessException extends RuntimeException {
        final int errorCode;

        NetworkAccessException(int i) {
            this.errorCode = i;
        }
    }

    private CallComposerPictureTransfer(Context context, int i, String str, ExecutorService executorService) {
        this.mContext = context;
        this.mSubscriptionId = i;
        this.mExecutorService = executorService;
        this.mUrl = str;
    }

    public void setCallback(PictureCallback pictureCallback) {
        this.mCallback = pictureCallback;
    }

    public void uploadPicture(final ImageData imageData, final GbaCredentialsSupplier gbaCredentialsSupplier) {
        CompletableFuture networkForCallComposer = getNetworkForCallComposer();
        CompletableFuture completableFutureThenApplyAsync = networkForCallComposer.thenApplyAsync(new Function() { // from class: com.android.phone.callcomposer.CallComposerPictureTransfer$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$uploadPicture$0((Network) obj);
            }
        }, (Executor) this.mExecutorService).thenComposeAsync(new Function() { // from class: com.android.phone.callcomposer.CallComposerPictureTransfer$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.obtainAuthenticateHeader((HttpURLConnection) obj);
            }
        }, (Executor) this.mExecutorService).thenApplyAsync((Function) new CallComposerPictureTransfer$$ExternalSyntheticLambda2());
        networkForCallComposer.thenCombineAsync((CompletionStage) completableFutureThenApplyAsync.thenCombineAsync((CompletionStage) completableFutureThenApplyAsync.thenComposeAsync(new Function() { // from class: com.android.phone.callcomposer.CallComposerPictureTransfer$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$uploadPicture$1(gbaCredentialsSupplier, (WWWAuthenticate) obj);
            }
        }, (Executor) this.mExecutorService), new BiFunction() { // from class: com.android.phone.callcomposer.CallComposerPictureTransfer$$ExternalSyntheticLambda4
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return this.f$0.lambda$uploadPicture$2((WWWAuthenticate) obj, (GbaCredentials) obj2);
            }
        }, (Executor) this.mExecutorService).whenCompleteAsync(new BiConsumer() { // from class: com.android.phone.callcomposer.CallComposerPictureTransfer$$ExternalSyntheticLambda5
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                this.f$0.lambda$uploadPicture$3((String) obj, (Throwable) obj2);
            }
        }, (Executor) this.mExecutorService), new BiFunction() { // from class: com.android.phone.callcomposer.CallComposerPictureTransfer$$ExternalSyntheticLambda6
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return this.f$0.lambda$uploadPicture$4(imageData, (Network) obj, (String) obj2);
            }
        }, (Executor) this.mExecutorService).thenAcceptAsync(new Consumer() { // from class: com.android.phone.callcomposer.CallComposerPictureTransfer$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$uploadPicture$5((String) obj);
            }
        }, (Executor) this.mExecutorService).exceptionally(new Function() { // from class: com.android.phone.callcomposer.CallComposerPictureTransfer$$ExternalSyntheticLambda8
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return CallComposerPictureTransfer.$r8$lambda$LplOzc_5K2dGHxyfgpajjoR5Xx8((Throwable) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ HttpURLConnection lambda$uploadPicture$0(Network network) {
        return prepareInitialPost(network, this.mUrl);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ CompletionStage lambda$uploadPicture$1(GbaCredentialsSupplier gbaCredentialsSupplier, WWWAuthenticate wWWAuthenticate) {
        if (wWWAuthenticate != null) {
            return gbaCredentialsSupplier.getCredentials(wWWAuthenticate.getRealm(), this.mExecutorService);
        }
        Log.i(TAG, "credsFuture, return CompletableFuture.completedFuture(null)");
        return CompletableFuture.completedFuture(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$uploadPicture$2(WWWAuthenticate wWWAuthenticate, GbaCredentials gbaCredentials) {
        if (wWWAuthenticate == null || gbaCredentials == null) {
            Log.i(TAG, "authorizationFuture, return null");
            return null;
        }
        return DigestAuthUtils.generateAuthorizationHeader(wWWAuthenticate, gbaCredentials, "POST", this.mUrl);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$uploadPicture$3(String str, Throwable th) {
        handleExceptionalCompletion(th);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$uploadPicture$5(String str) {
        Log.i(TAG, "networkUrlFuture.thenAcceptAsync  result = " + str);
        if (str != null) {
            this.mCallback.onUploadSuccessful(str);
            unregisterNetworkCallback();
        }
    }

    public static /* synthetic */ Void $r8$lambda$LplOzc_5K2dGHxyfgpajjoR5Xx8(Throwable th) {
        Log.i(TAG, "UploadPicture  Exception uploading image");
        logException("Exception uploading image", th);
        return null;
    }

    public void downloadPicture(final GbaCredentialsSupplier gbaCredentialsSupplier) {
        final CompletableFuture networkForCallComposer = getNetworkForCallComposer();
        CompletableFuture completableFutureThenApplyAsync = networkForCallComposer.thenApplyAsync(new Function() { // from class: com.android.phone.callcomposer.CallComposerPictureTransfer$$ExternalSyntheticLambda9
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$downloadPicture$7((Network) obj);
            }
        }, (Executor) this.mExecutorService);
        final CompletableFuture completableFutureThenComposeAsync = completableFutureThenApplyAsync.thenComposeAsync(new Function() { // from class: com.android.phone.callcomposer.CallComposerPictureTransfer$$ExternalSyntheticLambda10
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$downloadPicture$8((HttpURLConnection) obj);
            }
        }, (Executor) this.mExecutorService);
        final CompletableFuture completableFutureThenComposeAsync2 = completableFutureThenApplyAsync.thenComposeAsync(new Function() { // from class: com.android.phone.callcomposer.CallComposerPictureTransfer$$ExternalSyntheticLambda11
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$downloadPicture$12(gbaCredentialsSupplier, networkForCallComposer, (HttpURLConnection) obj);
            }
        }, (Executor) this.mExecutorService);
        CompletableFuture.allOf(completableFutureThenComposeAsync, completableFutureThenComposeAsync2).thenRun(new Runnable() { // from class: com.android.phone.callcomposer.CallComposerPictureTransfer$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$downloadPicture$13(completableFutureThenComposeAsync, completableFutureThenComposeAsync2);
            }
        }).exceptionally(new Function() { // from class: com.android.phone.callcomposer.CallComposerPictureTransfer$$ExternalSyntheticLambda13
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return CallComposerPictureTransfer.$r8$lambda$BBc3rZrx9NqKBXaohWOblt6iMys((Throwable) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ HttpURLConnection lambda$downloadPicture$7(Network network) {
        return prepareImageDownloadRequest(network, this.mUrl);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ CompletionStage lambda$downloadPicture$8(HttpURLConnection httpURLConnection) {
        try {
            if (httpURLConnection.getResponseCode() != 200) {
                return CompletableFuture.completedFuture(null);
            }
            return CompletableFuture.completedFuture(downloadImageFromConnection(httpURLConnection));
        } catch (IOException e) {
            logException("IOException obtaining return code: ", e);
            throw new NetworkAccessException(1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ CompletionStage lambda$downloadPicture$12(final GbaCredentialsSupplier gbaCredentialsSupplier, CompletableFuture completableFuture, HttpURLConnection httpURLConnection) {
        try {
            if (httpURLConnection.getResponseCode() == 200) {
                return CompletableFuture.completedFuture(null);
            }
            CompletableFuture completableFutureThenApply = obtainAuthenticateHeader(httpURLConnection).thenApply((Function) new CallComposerPictureTransfer$$ExternalSyntheticLambda2());
            return completableFuture.thenCombineAsync((CompletionStage) completableFutureThenApply.thenCombineAsync((CompletionStage) completableFutureThenApply.thenComposeAsync(new Function() { // from class: com.android.phone.callcomposer.CallComposerPictureTransfer$$ExternalSyntheticLambda14
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return this.f$0.lambda$downloadPicture$9(gbaCredentialsSupplier, (WWWAuthenticate) obj);
                }
            }, (Executor) this.mExecutorService), new BiFunction() { // from class: com.android.phone.callcomposer.CallComposerPictureTransfer$$ExternalSyntheticLambda15
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    return this.f$0.lambda$downloadPicture$10((WWWAuthenticate) obj, (GbaCredentials) obj2);
                }
            }, (Executor) this.mExecutorService).whenCompleteAsync(new BiConsumer() { // from class: com.android.phone.callcomposer.CallComposerPictureTransfer$$ExternalSyntheticLambda16
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    this.f$0.lambda$downloadPicture$11((String) obj, (Throwable) obj2);
                }
            }, (Executor) this.mExecutorService), new BiFunction() { // from class: com.android.phone.callcomposer.CallComposerPictureTransfer$$ExternalSyntheticLambda17
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    return this.f$0.downloadImageWithAuth((Network) obj, (String) obj2);
                }
            }, (Executor) this.mExecutorService);
        } catch (IOException e) {
            logException("IOException obtaining return code: ", e);
            throw new NetworkAccessException(1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ CompletionStage lambda$downloadPicture$9(GbaCredentialsSupplier gbaCredentialsSupplier, WWWAuthenticate wWWAuthenticate) {
        return gbaCredentialsSupplier.getCredentials(wWWAuthenticate.getRealm(), this.mExecutorService);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$downloadPicture$10(WWWAuthenticate wWWAuthenticate, GbaCredentials gbaCredentials) {
        return DigestAuthUtils.generateAuthorizationHeader(wWWAuthenticate, gbaCredentials, "GET", this.mUrl);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$downloadPicture$11(String str, Throwable th) {
        handleExceptionalCompletion(th);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$downloadPicture$13(CompletableFuture completableFuture, CompletableFuture completableFuture2) {
        ImageData imageData = (ImageData) completableFuture.getNow(null);
        ImageData imageData2 = (ImageData) completableFuture2.getNow(null);
        if (imageData2 == null && imageData == null) {
            Log.w(TAG, "No result from download -- error happened sometime earlier");
        }
        if (imageData2 != null) {
            this.mCallback.onDownloadSuccessful(imageData2);
        }
        this.mCallback.onDownloadSuccessful(imageData);
        unregisterNetworkCallback();
    }

    public static /* synthetic */ Void $r8$lambda$BBc3rZrx9NqKBXaohWOblt6iMys(Throwable th) {
        logException("Exception downloading image", th);
        return null;
    }

    private void unregisterNetworkCallback() {
        String str = TAG;
        Log.i(str, "unregisterNetworkCallback enter");
        ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService(ConnectivityManager.class);
        if (this.mNetworkCallback != null) {
            Log.i(str, "call unregisterNetworkCallback");
            connectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
            this.mNetworkCallback = null;
        }
    }

    private CompletableFuture getNetworkForCallComposer() {
        String str = TAG;
        Log.i(str, "getNetworkForCallComposer enter");
        ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService(ConnectivityManager.class);
        NetworkRequest networkRequestBuild = new NetworkRequest.Builder().addCapability(12).build();
        final CompletableFuture completableFuture = new CompletableFuture();
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback(this) { // from class: com.android.phone.callcomposer.CallComposerPictureTransfer.1
            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onAvailable(Network network) {
                Log.i(CallComposerPictureTransfer.TAG, "NetworkCallback onAvailable ");
                completableFuture.complete(network);
            }
        };
        this.mNetworkCallback = networkCallback;
        connectivityManager.requestNetwork(networkRequestBuild, networkCallback);
        Log.i(str, "getNetworkForCallComposer resultFuture = " + completableFuture);
        return completableFuture;
    }

    private HttpURLConnection prepareInitialPost(Network network, String str) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) network.openConnection(new URL(str));
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.setConnectTimeout(HTTP_TIMEOUT_MILLIS);
            httpURLConnection.setReadTimeout(HTTP_TIMEOUT_MILLIS);
            httpURLConnection.setRequestProperty("User-Agent", getUserAgent());
            return httpURLConnection;
        } catch (MalformedURLException e) {
            Log.e(TAG, "Malformed URL: " + str);
            Reflects$$ExternalSyntheticBUOutline0.m(e);
            return null;
        } catch (IOException e2) {
            logException("IOException opening network: ", e2);
            Reflects$$ExternalSyntheticBUOutline0.m(e2);
            return null;
        }
    }

    private HttpURLConnection prepareImageDownloadRequest(Network network, String str) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) network.openConnection(new URL(str));
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(HTTP_TIMEOUT_MILLIS);
            httpURLConnection.setReadTimeout(HTTP_TIMEOUT_MILLIS);
            httpURLConnection.setRequestProperty("User-Agent", getUserAgent());
            return httpURLConnection;
        } catch (MalformedURLException e) {
            Log.e(TAG, "Malformed URL: " + str);
            Reflects$$ExternalSyntheticBUOutline0.m(e);
            return null;
        } catch (IOException e2) {
            logException("IOException opening network: ", e2);
            Reflects$$ExternalSyntheticBUOutline0.m(e2);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public CompletableFuture obtainAuthenticateHeader(final HttpURLConnection httpURLConnection) {
        return CompletableFuture.supplyAsync(new Supplier() { // from class: com.android.phone.callcomposer.CallComposerPictureTransfer$$ExternalSyntheticLambda18
            @Override // java.util.function.Supplier
            public final Object get() {
                return CallComposerPictureTransfer.m2385$r8$lambda$rTzaj2a0_c1cgsEPwsmua_7e48(httpURLConnection);
            }
        }, this.mExecutorService);
    }

    /* JADX INFO: renamed from: $r8$lambda$rTzaj2a0_c1cgsEPwsmua_7-e48, reason: not valid java name */
    public static /* synthetic */ String m2385$r8$lambda$rTzaj2a0_c1cgsEPwsmua_7e48(HttpURLConnection httpURLConnection) {
        try {
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 204) {
                return null;
            }
            if (responseCode == 403) {
                throw new NetworkAccessException(3);
            }
            if (responseCode != 401) {
                Log.w(TAG, "Received unexpected response in auth request, code= " + responseCode);
                throw new NetworkAccessException(0);
            }
            return httpURLConnection.getHeaderField(DigestAuthUtils.WWW_AUTHENTICATE);
        } catch (IOException e) {
            logException("IOException obtaining auth header: ", e);
            throw new NetworkAccessException(1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ImageData downloadImageWithAuth(Network network, String str) {
        HttpURLConnection httpURLConnectionPrepareImageDownloadRequest = prepareImageDownloadRequest(network, this.mUrl);
        httpURLConnectionPrepareImageDownloadRequest.addRequestProperty("Authorization", str);
        return downloadImageFromConnection(httpURLConnectionPrepareImageDownloadRequest);
    }

    private ImageData downloadImageFromConnection(HttpURLConnection httpURLConnection) {
        try {
            if (httpURLConnection.getResponseCode() != 200) {
                String str = TAG;
                Log.w(str, "Got response code " + httpURLConnection.getResponseCode() + " when trying to download image");
                if (httpURLConnection.getResponseCode() == 401) {
                    Log.i(str, "Got 401 even with auth -- key refresh needed?");
                    this.mCallback.onRetryNeeded(true, 0L);
                    unregisterNetworkCallback();
                }
                return null;
            }
            String contentType = httpURLConnection.getContentType();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[4096];
            try {
                InputStream inputStream = httpURLConnection.getInputStream();
                while (true) {
                    int i = inputStream.read(bArr);
                    if (i >= 0) {
                        byteArrayOutputStream.write(bArr, 0, i);
                    } else {
                        return new ImageData(byteArrayOutputStream.toByteArray(), contentType, null);
                    }
                }
            } catch (IOException e) {
                logException("IOException reading from image body: ", e);
                return null;
            }
        } catch (IOException e2) {
            logException("IOException obtaining return code: ", e2);
            throw new NetworkAccessException(1);
        }
    }

    private void handleExceptionalCompletion(Throwable th) {
        Log.i(TAG, "handleExceptionalCompletion enter  error = " + th);
        if (th != null) {
            if (th.getCause() instanceof NetworkAccessException) {
                int i = ((NetworkAccessException) th.getCause()).errorCode;
                if (i == 0 || i == 1) {
                    scheduleRetry();
                    return;
                } else {
                    deliverFailure(i == 3 ? 3 : 0);
                    return;
                }
            }
            deliverFailure(0);
        }
    }

    private void scheduleRetry() {
        unregisterNetworkCallback();
        this.mCallback.onRetryNeeded(false, 1000L);
    }

    private void deliverFailure(int i) {
        unregisterNetworkCallback();
        this.mCallback.onError(i);
    }

    private static Part makeUploadPart(final String str, final String str2, final String str3, final byte[] bArr) {
        return new Part() { // from class: com.android.phone.callcomposer.CallComposerPictureTransfer.2
            @Override // com.android.internal.http.multipart.Part
            public String getCharSet() {
                return null;
            }

            @Override // com.android.internal.http.multipart.Part
            public String getTransferEncoding() {
                return null;
            }

            @Override // com.android.internal.http.multipart.Part
            public String getName() {
                return str;
            }

            @Override // com.android.internal.http.multipart.Part
            public String getContentType() {
                return str2;
            }

            @Override // com.android.internal.http.multipart.Part
            public void sendDispositionHeader(OutputStream outputStream) throws IOException {
                super.sendDispositionHeader(outputStream);
                if (str3 != null) {
                    outputStream.write(("; filename=\"" + str3 + "\"").getBytes());
                }
            }

            @Override // com.android.internal.http.multipart.Part
            protected void sendData(OutputStream outputStream) throws IOException {
                outputStream.write(bArr);
            }

            @Override // com.android.internal.http.multipart.Part
            protected long lengthOfData() throws IOException {
                return bArr.length;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: sendActualImageUpload, reason: merged with bridge method [inline-methods] */
    public String lambda$uploadPicture$4(Network network, String str, ImageData imageData) {
        MultipartEntity multipartEntity = new MultipartEntity(new Part[]{makeUploadPart("tid", "text/plain", null, imageData.getId().getBytes()), makeUploadPart("File", imageData.getMimeType(), imageData.getId(), imageData.getImageBytes())});
        String str2 = TAG;
        Log.i(str2, "sendActualImageUpload enter ");
        HttpURLConnection httpURLConnectionPrepareInitialPost = prepareInitialPost(network, this.mUrl);
        httpURLConnectionPrepareInitialPost.setDoOutput(true);
        if (str != null) {
            httpURLConnectionPrepareInitialPost.addRequestProperty("Authorization", str);
        }
        httpURLConnectionPrepareInitialPost.addRequestProperty("Content-Length", String.valueOf(multipartEntity.getContentLength()));
        httpURLConnectionPrepareInitialPost.addRequestProperty("Content-Type", multipartEntity.getContentType().getValue());
        httpURLConnectionPrepareInitialPost.addRequestProperty("Accept-Encoding", "*");
        try {
            OutputStream outputStream = httpURLConnectionPrepareInitialPost.getOutputStream();
            try {
                multipartEntity.writeTo(outputStream);
                if (outputStream != null) {
                    outputStream.close();
                }
                try {
                    int responseCode = httpURLConnectionPrepareInitialPost.getResponseCode();
                    Log.i(str2, "Received response code: " + responseCode + ", message=" + httpURLConnectionPrepareInitialPost.getResponseMessage());
                    if (responseCode != 401 && responseCode != 403) {
                        if (responseCode == 503) {
                            scheduleRetry();
                            return null;
                        }
                        if (responseCode != 200) {
                            scheduleRetry();
                            return null;
                        }
                        String imageUploadResponseXmlForUrl = parseImageUploadResponseXmlForUrl(readResponseBody(httpURLConnectionPrepareInitialPost));
                        Log.i(str2, "Parsed URL as upload result: " + imageUploadResponseXmlForUrl);
                        return imageUploadResponseXmlForUrl;
                    }
                    deliverFailure(3);
                    return null;
                } catch (IOException e) {
                    logException("IOException getting response to image upload: ", e);
                    deliverFailure(0);
                    return null;
                }
            } catch (Throwable th) {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (IOException e2) {
            logException("IOException making request to upload image: ", e2);
            Reflects$$ExternalSyntheticBUOutline0.m(e2);
            return null;
        }
    }

    private static String parseImageUploadResponseXmlForUrl(String str) {
        NamespaceContext namespaceContext = new NamespaceContext() { // from class: com.android.phone.callcomposer.CallComposerPictureTransfer.3
            @Override // javax.xml.namespace.NamespaceContext
            public String getNamespaceURI(String str2) {
                return "urn:gsma:params:xml:ns:rcs:rcs:fthttp";
            }

            @Override // javax.xml.namespace.NamespaceContext
            public String getPrefix(String str2) {
                throw new UnsupportedOperationException();
            }

            @Override // javax.xml.namespace.NamespaceContext
            public Iterator getPrefixes(String str2) {
                throw new UnsupportedOperationException();
            }
        };
        XPath xPathNewXPath = XPathFactory.newInstance().newXPath();
        xPathNewXPath.setNamespaceContext(namespaceContext);
        try {
            return (String) xPathNewXPath.evaluate("/a:file/a:file-info[@type='file']/a:data/@url", new InputSource(new StringReader(str)), XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            logException("Error parsing response XML:", e);
            return null;
        }
    }

    private static String readResponseBody(HttpURLConnection httpURLConnection) {
        Charset charset = (Charset) MediaType.parse(httpURLConnection.getContentType()).charset().or(Charset.defaultCharset());
        StringBuilder sb = new StringBuilder();
        try {
            InputStream inputStream = httpURLConnection.getInputStream();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset));
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line);
                    logException("IOException reading request body: ", e);
                    return null;
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                return sb.toString();
            } catch (Throwable th) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (IOException e) {
            logException("IOException reading request body: ", e);
            return null;
        }
    }

    private String getUserAgent() {
        return String.format("%s %s %s %s %s %s %s", ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(this.mSubscriptionId).getSimOperatorName(), Build.ID, DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault()).format(Instant.ofEpochMilli(Build.TIME)), "Android", Build.VERSION.RELEASE_OR_CODENAME, Build.DEVICE, THREE_GPP_GBA);
    }

    private static void logException(String str, Throwable th) {
        StringWriter stringWriter = new StringWriter();
        stringWriter.append((CharSequence) str);
        stringWriter.append((CharSequence) ":\n");
        stringWriter.append((CharSequence) th.getMessage());
        th.printStackTrace(new PrintWriter(stringWriter));
        Log.e(TAG, stringWriter.toString());
    }
}
