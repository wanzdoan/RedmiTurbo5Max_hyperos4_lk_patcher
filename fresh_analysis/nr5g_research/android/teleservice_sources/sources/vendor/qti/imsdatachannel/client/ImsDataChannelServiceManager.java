package vendor.qti.imsdatachannel.client;

import android.content.Context;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes2.dex */
public interface ImsDataChannelServiceManager {
    void closeDataChannelTransport(ImsDataChannelTransport imsDataChannelTransport);

    void connectImsDataChannelService(ImsDataChannelServiceConnectionCallback imsDataChannelServiceConnectionCallback);

    ImsDataChannelTransport createDataChannelTransport(int i, String str, ImsDataChannelEventListener imsDataChannelEventListener);

    void getAvailability(int i, ImsDataChannelServiceAvailabilityCallback imsDataChannelServiceAvailabilityCallback);

    void initialize(Context context, Executor executor);
}
