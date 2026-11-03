package com.android.services.telephony.relay.phonecall;

import com.xiaomi.continuity.channel.Channel;

/* JADX INFO: loaded from: classes.dex */
public class RelayChannelInfo {
    protected Channel mChannel;
    protected int mChannelId;
    protected String mDeviceId;

    public RelayChannelInfo(String str, int i, Channel channel) {
        this.mDeviceId = str;
        this.mChannelId = i;
        this.mChannel = channel;
    }
}
