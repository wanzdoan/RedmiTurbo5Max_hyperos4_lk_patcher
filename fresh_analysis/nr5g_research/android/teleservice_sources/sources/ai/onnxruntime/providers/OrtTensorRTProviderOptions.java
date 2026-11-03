package ai.onnxruntime.providers;

import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtProvider;
import ai.onnxruntime.OrtProviderOptions;
import com.nxp.id.tp.common.HexString$$ExternalSyntheticBUOutline1;

/* JADX INFO: loaded from: classes.dex */
public final class OrtTensorRTProviderOptions extends StringConfigProviderOptions {
    private static final OrtProvider PROVIDER = OrtProvider.TENSOR_RT;

    /* JADX INFO: Access modifiers changed from: private */
    public static native long create(long j) throws OrtException;

    @Override // ai.onnxruntime.providers.StringConfigProviderOptions
    protected native void add(long j, long j2, String str, String str2) throws OrtException;

    @Override // ai.onnxruntime.OrtProviderOptions
    protected native void close(long j, long j2);

    @Override // ai.onnxruntime.providers.StringConfigProviderOptions
    public /* bridge */ /* synthetic */ void add(String str, String str2) throws OrtException {
        super.add(str, str2);
    }

    @Override // ai.onnxruntime.providers.StringConfigProviderOptions
    public /* bridge */ /* synthetic */ String getOptionsString() {
        return super.getOptionsString();
    }

    @Override // ai.onnxruntime.providers.StringConfigProviderOptions
    public /* bridge */ /* synthetic */ void parseOptionsString(String str) throws OrtException {
        super.parseOptionsString(str);
    }

    @Override // ai.onnxruntime.providers.StringConfigProviderOptions
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public OrtTensorRTProviderOptions() throws OrtException {
        this(0);
    }

    public OrtTensorRTProviderOptions(int i) throws OrtException {
        super(OrtProviderOptions.loadLibraryAndCreate(PROVIDER, new OrtProviderOptions.OrtProviderSupplier() { // from class: ai.onnxruntime.providers.OrtTensorRTProviderOptions$$ExternalSyntheticLambda0
            @Override // ai.onnxruntime.OrtProviderOptions.OrtProviderSupplier
            public final long create() {
                return OrtTensorRTProviderOptions.create(OrtProviderOptions.getApiHandle());
            }
        }));
        if (i < 0) {
            close();
            HexString$$ExternalSyntheticBUOutline1.m("Device id must be non-negative, received ", i);
            throw null;
        }
        String str = "" + i;
        this.options.put("device_id", str);
        add(OrtProviderOptions.getApiHandle(), this.nativeHandle, "device_id", str);
    }

    @Override // ai.onnxruntime.OrtProviderOptions
    public OrtProvider getProvider() {
        return PROVIDER;
    }
}
