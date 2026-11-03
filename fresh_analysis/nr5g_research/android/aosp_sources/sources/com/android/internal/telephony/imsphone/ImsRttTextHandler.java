package com.android.internal.telephony.imsphone;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telecom.Connection;
import com.android.telephony.Rlog;
import java.io.IOException;
import java.nio.channels.ClosedByInterruptException;
import java.util.concurrent.CountDownLatch;

/* JADX INFO: loaded from: classes.dex */
public class ImsRttTextHandler extends Handler {
    public static final int MAX_BUFFERED_CHARACTER_COUNT = 5;
    public static final int MAX_BUFFERING_DELAY_MILLIS = 200;
    public static final int MAX_CODEPOINTS_PER_SECOND = 30;
    private StringBuffer mBufferedTextToIncall;
    private StringBuffer mBufferedTextToNetwork;
    private int mCodepointsAvailableForTransmission;
    private final NetworkWriter mNetworkWriter;
    private CountDownLatch mReadNotifier;
    private InCallReaderThread mReaderThread;
    private Connection.RttTextStream mRttTextStream;

    public interface NetworkWriter {
        void write(String str);
    }

    public int getSendToIncall() {
        return 3;
    }

    private class InCallReaderThread extends Thread {
        private final Connection.RttTextStream mReaderThreadRttTextStream;

        public InCallReaderThread(Connection.RttTextStream rttTextStream) {
            this.mReaderThreadRttTextStream = rttTextStream;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (true) {
                try {
                    String str = this.mReaderThreadRttTextStream.read();
                    if (str == null) {
                        Rlog.e("ImsRttTextHandler", "RttReaderThread - Stream closed unexpectedly. Attempt to reinitialize.");
                        ImsRttTextHandler.this.obtainMessage(9999).sendToTarget();
                        return;
                    } else if (str.length() != 0) {
                        ImsRttTextHandler.this.obtainMessage(2, str).sendToTarget();
                        if (ImsRttTextHandler.this.mReadNotifier != null) {
                            ImsRttTextHandler.this.mReadNotifier.countDown();
                        }
                    }
                } catch (ClosedByInterruptException unused) {
                    Rlog.i("ImsRttTextHandler", "RttReaderThread - Thread interrupted. Finishing.");
                    return;
                } catch (IOException e) {
                    Rlog.e("ImsRttTextHandler", "RttReaderThread - IOException encountered reading from in-call: ", e);
                    ImsRttTextHandler.this.obtainMessage(9999).sendToTarget();
                    return;
                }
            }
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i == 1) {
            if (this.mRttTextStream != null || this.mReaderThread != null) {
                Rlog.e("ImsRttTextHandler", "RTT text stream already initialized. Ignoring.");
                return;
            }
            Connection.RttTextStream rttTextStream = (Connection.RttTextStream) message.obj;
            this.mRttTextStream = rttTextStream;
            if (rttTextStream == null) {
                Rlog.e("ImsRttTextHandler", "RTT text stream is null");
                return;
            }
            InCallReaderThread inCallReaderThread = new InCallReaderThread(this.mRttTextStream);
            this.mReaderThread = inCallReaderThread;
            inCallReaderThread.start();
            return;
        }
        if (i == 2) {
            this.mBufferedTextToNetwork.append((String) message.obj);
            StringBuffer stringBuffer = this.mBufferedTextToNetwork;
            if (stringBuffer.codePointCount(0, stringBuffer.length()) >= 5) {
                sendMessage(obtainMessage(4));
                return;
            } else {
                sendEmptyMessageDelayed(4, 200L);
                return;
            }
        }
        if (i == 3) {
            Object obj = message.obj;
            if (obj == null) {
                Rlog.e("ImsRttTextHandler", "RTT msg.obj is null. Ignoring.");
                return;
            }
            String str = (String) obj;
            Connection.RttTextStream rttTextStream2 = this.mRttTextStream;
            if (rttTextStream2 == null) {
                Rlog.e("ImsRttTextHandler", "RTT text stream is null. Writing to in-call buffer.");
                this.mBufferedTextToIncall.append(str);
                return;
            }
            try {
                rttTextStream2.write(str);
                return;
            } catch (IOException e) {
                Rlog.e("ImsRttTextHandler", "IOException encountered writing to in-call: %s", e);
                obtainMessage(9999).sendToTarget();
                this.mBufferedTextToIncall.append(str);
                return;
            }
        }
        if (i == 4) {
            StringBuffer stringBuffer2 = this.mBufferedTextToNetwork;
            int iMin = Math.min(stringBuffer2.codePointCount(0, stringBuffer2.length()), this.mCodepointsAvailableForTransmission);
            if (iMin == 0) {
                return;
            }
            int iOffsetByCodePoints = this.mBufferedTextToNetwork.offsetByCodePoints(0, iMin);
            String strSubstring = this.mBufferedTextToNetwork.substring(0, iOffsetByCodePoints);
            this.mBufferedTextToNetwork.delete(0, iOffsetByCodePoints);
            this.mNetworkWriter.write(strSubstring);
            this.mCodepointsAvailableForTransmission -= iMin;
            sendMessageDelayed(obtainMessage(5, iMin, 0), 1000L);
            return;
        }
        if (i == 5) {
            int i2 = this.mCodepointsAvailableForTransmission + message.arg1;
            this.mCodepointsAvailableForTransmission = i2;
            if (i2 > 0) {
                sendMessage(obtainMessage(4));
                return;
            }
            return;
        }
        if (i != 9999) {
            return;
        }
        try {
            InCallReaderThread inCallReaderThread2 = this.mReaderThread;
            if (inCallReaderThread2 != null) {
                inCallReaderThread2.interrupt();
                this.mReaderThread.join(1000L);
            }
        } catch (InterruptedException unused) {
        }
        this.mReaderThread = null;
        this.mRttTextStream = null;
    }

    public ImsRttTextHandler(Looper looper, NetworkWriter networkWriter) {
        super(looper);
        this.mCodepointsAvailableForTransmission = 30;
        this.mBufferedTextToNetwork = new StringBuffer();
        this.mBufferedTextToIncall = new StringBuffer();
        this.mNetworkWriter = networkWriter;
    }

    public void sendToInCall(String str) {
        obtainMessage(3, str).sendToTarget();
    }

    public void initialize(Connection.RttTextStream rttTextStream) {
        Rlog.i("ImsRttTextHandler", "Initializing: " + this);
        obtainMessage(1, rttTextStream).sendToTarget();
    }

    public void tearDown() {
        obtainMessage(9999).sendToTarget();
    }

    public void setReadNotifier(CountDownLatch countDownLatch) {
        this.mReadNotifier = countDownLatch;
    }

    public StringBuffer getBufferedTextToIncall() {
        return this.mBufferedTextToIncall;
    }

    public void setRttTextStream(Connection.RttTextStream rttTextStream) {
        this.mRttTextStream = rttTextStream;
    }

    public String getNetworkBufferText() {
        return this.mBufferedTextToNetwork.toString();
    }

    public void setCPS(int i) {
        Rlog.i("ImsRttTextHandler", "setCPS: " + i);
        if (this.mRttTextStream != null || this.mReaderThread != null) {
            Rlog.w("ImsRttTextHandler", "setCPS failed, RTT text stream already initialized");
        } else if (i > 0) {
            this.mCodepointsAvailableForTransmission = i;
        } else {
            Rlog.w("ImsRttTextHandler", "setCPS failed, invalid cps");
        }
    }
}
