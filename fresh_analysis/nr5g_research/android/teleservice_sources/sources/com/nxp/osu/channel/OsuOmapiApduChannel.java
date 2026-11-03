package com.nxp.osu.channel;

import android.content.Context;
import android.se.omapi.Channel;
import android.se.omapi.Reader;
import android.se.omapi.SEService;
import android.se.omapi.Session;
import android.util.Log;
import com.nxp.osu.SecOsuException;
import com.nxp.osu.osucore.IAROperation;
import com.nxp.osu.util.OSULogger;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class OsuOmapiApduChannel implements IAccess {
    private static Channel sChannel;
    private static Context sContext;
    private static Channel sIARChannel;
    private Timer mConnectionTimer;
    private static final boolean IS_DEBUG = OSULogger.isDebug();
    private static SEService sSEService = null;
    private static Session sSession = null;
    private static String sTerminal = "";
    private static OsuOmapiApduChannel sOmapiChannel = null;
    private static Reader mReader = null;
    public static boolean sIsRefreshRequired = false;
    private static final byte[] SUCCESS_RESP = {-112, 0};
    private static final byte[] IARAppletAid = {-96, 0, 0, 3, -106, 84, 83, 0, 0, 0, 1, 0, 82};
    private static final byte[] IARSetFlag = {-128, 16, -91, 0, 0};
    private static final byte[] IARResetFlag = {-128, 16, 0, 0, 0};
    private Object mServiceMutex = new Object();
    private boolean mFlagServiceMutex = false;
    private ServiceConnectionTimerTask mTimerTask = new ServiceConnectionTimerTask();
    private SynchronousExecutor mExecutor = new SynchronousExecutor();
    private boolean mIsConnected = false;
    private BindToSEService mBindService = null;
    private final SEService.OnConnectedListener mListener = new SEService.OnConnectedListener() { // from class: com.nxp.osu.channel.OsuOmapiApduChannel.1
        @Override // android.se.omapi.SEService.OnConnectedListener
        public void onConnected() {
            if (OsuOmapiApduChannel.IS_DEBUG) {
                Log.d("OSU-OsuApduChannel", " OnConnectedListener successfully onConnected");
            }
            synchronized (OsuOmapiApduChannel.this.mServiceMutex) {
                OsuOmapiApduChannel.this.mIsConnected = true;
                OsuOmapiApduChannel.this.mFlagServiceMutex = true;
                OsuOmapiApduChannel.this.mServiceMutex.notify();
            }
        }
    };

    public static OsuOmapiApduChannel getInstance(Context context, boolean z, int i) {
        SEService sEService;
        sContext = context;
        OSULogger.setNFCDisableRequired(!z);
        OsuOmapiHelper.setTerminalSmb(z);
        sTerminal = "eSE" + String.valueOf(i);
        if (sOmapiChannel == null || (sEService = sSEService) == null || sSession == null || sChannel == null || !sEService.isConnected() || sSession.isClosed() || !sChannel.isOpen()) {
            SEService sEService2 = sSEService;
            if (sEService2 != null) {
                sEService2.shutdown();
                sSEService = null;
            }
            sSession = null;
            sOmapiChannel = new OsuOmapiApduChannel();
        } else if (IS_DEBUG) {
            Log.d("OSU-OsuApduChannel", "Already Initialized");
        }
        return sOmapiChannel;
    }

    private OsuOmapiApduChannel() throws SecOsuException {
        if (sSEService == null || sSession == null) {
            boolean z = IS_DEBUG;
            if (z) {
                Log.d("OSU-OsuApduChannel", " Initialize OMAPI APDU channel");
            }
            bindSEService();
            if (z) {
                Log.d("OSU-OsuApduChannel", " openSession & get readers");
            }
            getSession();
            return;
        }
        if (IS_DEBUG) {
            Log.d("OSU-OsuApduChannel", " OMAPI session already initialized");
        }
    }

    class SynchronousExecutor implements Executor {
        SynchronousExecutor() {
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            if (OsuOmapiApduChannel.IS_DEBUG) {
                Log.d("OSU-OsuApduChannel", " From SynchronousExecutor");
            }
            runnable.run();
        }
    }

    private void bindSEService() {
        Timer timer = new Timer();
        this.mConnectionTimer = timer;
        timer.schedule(this.mTimerTask, 3000L);
        BindToSEService bindToSEService = new BindToSEService();
        this.mBindService = bindToSEService;
        bindToSEService.start();
    }

    private void getSession() throws SecOsuException {
        BindToSEService bindToSEService;
        waitForConnection();
        while (sSEService == null && (bindToSEService = this.mBindService) != null && bindToSEService.isAlive()) {
            try {
                Log.e("OSU-OsuApduChannel", "Retry on SeService connection failure");
                new Thread();
                Thread.sleep(500L);
            } catch (Exception unused) {
                Log.e("OSU-OsuApduChannel", "getSession Thread interruption exception received");
            }
        }
        SEService sEService = sSEService;
        if (sEService == null) {
            Log.e("OSU-OsuApduChannel", "Unable to establish connection - exiting");
            return;
        }
        try {
            for (Reader reader : sEService.getReaders()) {
                if (reader.getName().equals(sTerminal)) {
                    mReader = reader;
                    break;
                }
            }
            Reader reader2 = mReader;
            if (reader2 == null) {
                throw new SecOsuException("Terminal not available");
            }
            Session sessionOpenSession = reader2.openSession();
            sSession = sessionOpenSession;
            if (sessionOpenSession == null) {
                throw new SecOsuException("Not available to intialize session");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SecOsuException(e.getMessage());
        }
    }

    @Override // com.nxp.osu.channel.IAccess
    public byte[] open() throws IOException {
        Reader reader = mReader;
        if (reader == null) {
            throw new IOException("Terminal not initialized");
        }
        try {
            reader.reset();
            Session session = sSession;
            if (session == null || session.isClosed()) {
                sSession = mReader.openSession();
            }
            return SUCCESS_RESP;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            System.exit(0);
            return null;
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new IOException("open session failed");
        }
    }

    @Override // com.nxp.osu.channel.IAccess
    public byte[] openChannel(byte[] bArr) throws IOException {
        if (mReader == null) {
            throw new IOException("Terminal not initialized");
        }
        Session session = sSession;
        if (session == null || session.isClosed()) {
            throw new IOException("Terminal Session not opened");
        }
        try {
            Channel channel = sChannel;
            if (channel != null && channel.isOpen()) {
                Log.w("OSU-OsuApduChannel", "Channel Already Opened Please close & re-open if required");
                return SUCCESS_RESP;
            }
            Channel channelOpenBasicChannel = sSession.openBasicChannel(bArr);
            sChannel = channelOpenBasicChannel;
            if (channelOpenBasicChannel == null) {
                Log.w("OSU-OsuApduChannel", "Basic Channel is in use Resetting Session");
                mReader.reset();
                Session sessionOpenSession = mReader.openSession();
                sSession = sessionOpenSession;
                sChannel = sessionOpenSession.openBasicChannel(bArr);
            }
            if (sChannel != null) {
                return SUCCESS_RESP;
            }
            return null;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            System.exit(0);
            return null;
        } catch (SecurityException unused) {
            throw new IOException("Security Exception");
        } catch (NoSuchElementException unused2) {
            return new byte[]{106, -126};
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new IOException("Open channel failed");
        }
    }

    @Override // com.nxp.osu.channel.IAccess
    public byte[] getAtr() throws IOException {
        Session session = sSession;
        if (session == null || session.isClosed()) {
            throw new IOException("Terminal Session not opened");
        }
        return sSession.getATR();
    }

    @Override // com.nxp.osu.channel.IAccess
    public byte[] send(byte[] bArr) throws IOException {
        if (sChannel == null) {
            throw new IOException("Channel not initialized");
        }
        int i = 0;
        int i2 = 0;
        do {
            try {
                return sChannel.transmit(bArr);
            } catch (IOException e) {
                e.printStackTrace();
                if (i2 <= 1) {
                    i2++;
                    if (IS_DEBUG) {
                        Log.d("OSU-OsuApduChannel", "Transmit failed with IOException, retry transmit");
                    }
                    try {
                        Thread.sleep(500L);
                    } catch (Exception e2) {
                        if (IS_DEBUG) {
                            Log.d("OSU-OsuApduChannel", "Exception is " + e2);
                        }
                    }
                } else {
                    throw new IOException("transmit on APDU channel failed");
                }
            } catch (IllegalStateException e3) {
                e3.printStackTrace();
                System.exit(i);
            }
        } while (i2 <= 1);
        return null;
    }

    @Override // com.nxp.osu.channel.IAccess
    public void close() {
        if (sChannel != null) {
            if (IS_DEBUG) {
                Log.d("OSU-OsuApduChannel", "Closing Omapi channel");
            }
            sChannel.close();
        }
        if (mReader != null && sIsRefreshRequired) {
            if (IS_DEBUG) {
                Log.d("OSU-OsuApduChannel", "Reseting OMAPI session");
            }
            mReader.reset();
            sSession = null;
            sIsRefreshRequired = false;
        }
        Session session = sSession;
        if (session == null || session.isClosed()) {
            return;
        }
        sSession.close();
    }

    class ServiceConnectionTimerTask extends TimerTask {
        ServiceConnectionTimerTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            synchronized (OsuOmapiApduChannel.this.mServiceMutex) {
                OsuOmapiApduChannel.this.mFlagServiceMutex = true;
                OsuOmapiApduChannel.this.mServiceMutex.notifyAll();
            }
        }
    }

    private void waitForConnection() {
        synchronized (this.mServiceMutex) {
            if (!this.mIsConnected) {
                while (!this.mFlagServiceMutex) {
                    try {
                        this.mServiceMutex.wait();
                    } catch (InterruptedException unused) {
                        Log.e("OSU-OsuApduChannel", "Connection to eSE interrupted");
                    }
                }
                this.mFlagServiceMutex = false;
            }
            if (!this.mIsConnected) {
                throw new SecOsuException("Service could not be connected after 3000 ms");
            }
            Timer timer = this.mConnectionTimer;
            if (timer != null) {
                timer.cancel();
            }
        }
    }

    class BindToSEService extends Thread {
        BindToSEService() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            int i = 0;
            while (true) {
                OsuOmapiApduChannel.sSEService = new SEService(OsuOmapiApduChannel.sContext, OsuOmapiApduChannel.this.mExecutor, OsuOmapiApduChannel.this.mListener);
                if (OsuOmapiApduChannel.sSEService == null) {
                    try {
                        new Thread();
                        Thread.sleep(500L);
                        Log.e("OSU-OsuApduChannel", "Bind to SE service fails" + i);
                    } catch (Exception unused) {
                        Log.e("OSU-OsuApduChannel", "BindToSEService Thread interruption exception received");
                    }
                }
                if (OsuOmapiApduChannel.sSEService != null) {
                    break;
                }
                int i2 = i + 1;
                if (i >= 3) {
                    break;
                } else {
                    i = i2;
                }
            }
            if (OsuOmapiApduChannel.sSEService == null || !OsuOmapiApduChannel.IS_DEBUG) {
                return;
            }
            Log.d("OSU-OsuApduChannel", "Bind to SE service Success");
        }
    }

    @Override // com.nxp.osu.channel.IAccess
    public void reset() throws IOException {
        send(new byte[]{-128, -33, -17, 0, 0});
    }

    public static byte[] doIARSpecific(IAROperation.IAR_SPECIFIC_OPERATION iar_specific_operation) throws IOException {
        boolean z;
        boolean z2;
        byte[] bArrTransmit;
        if (sOmapiChannel == null) {
            try {
                sOmapiChannel = new OsuOmapiApduChannel();
            } catch (Exception e) {
                Log.e("OSU-OsuApduChannel", "Caught exception while OsuOmapiApduChannel()");
                e.printStackTrace();
            }
        }
        if (mReader == null) {
            throw new IOException("Terminal not initialized");
        }
        try {
            try {
                Session session = sSession;
                if (session == null || session.isClosed()) {
                    z2 = true;
                    try {
                        sSession = mReader.openSession();
                    } catch (IllegalStateException e2) {
                        z = true;
                        e = e2;
                        e.printStackTrace();
                        System.exit(0);
                        z2 = z;
                    }
                } else {
                    z2 = false;
                }
            } catch (Exception e3) {
                e3.printStackTrace();
                throw new IOException("open session failed");
            }
        } catch (IllegalStateException e4) {
            e = e4;
            z = false;
        }
        if (iar_specific_operation == IAROperation.IAR_SPECIFIC_OPERATION.ISJCOPMODE) {
            OsuOmapiHelper.parseATR(null);
            OsuOmapiHelper.parseATR(sSession.getATR());
            if (OsuOmapiHelper.isJcopInNormalMode()) {
                return new byte[]{-112, 0};
            }
            return null;
        }
        try {
            Channel channel = sIARChannel;
            if (channel == null || !channel.isOpen()) {
                sIARChannel = sSession.openLogicalChannel(IARAppletAid);
            }
            Channel channel2 = sIARChannel;
            if (channel2 != null && channel2.isOpen()) {
                int i = 0;
                do {
                    try {
                        if (iar_specific_operation == IAROperation.IAR_SPECIFIC_OPERATION.PRECONDITION) {
                            bArrTransmit = sIARChannel.transmit(IARSetFlag);
                        } else {
                            bArrTransmit = iar_specific_operation == IAROperation.IAR_SPECIFIC_OPERATION.POSTCONDITION ? sIARChannel.transmit(IARResetFlag) : null;
                        }
                        sIARChannel.close();
                        if (z2) {
                            sSession.close();
                        }
                        return bArrTransmit;
                    } catch (IOException e5) {
                        try {
                            e5.printStackTrace();
                            if (i <= 3) {
                                i++;
                                if (IS_DEBUG) {
                                    Log.d("OSU-OsuApduChannel", "Transmit failed with IOException, retry transmit");
                                }
                                try {
                                    Thread.sleep(500L);
                                } catch (Exception e6) {
                                    if (IS_DEBUG) {
                                        Log.d("OSU-OsuApduChannel", "Exception is " + e6);
                                    }
                                }
                                sIARChannel.close();
                                if (z2) {
                                    sSession.close();
                                }
                            } else {
                                throw new IOException("transmit on APDU channel failed");
                            }
                        } catch (Throwable th) {
                            sIARChannel.close();
                            if (z2) {
                                sSession.close();
                            }
                            throw th;
                        }
                    } catch (IllegalStateException e7) {
                        e7.printStackTrace();
                        System.exit(0);
                        sIARChannel.close();
                        if (z2) {
                            sSession.close();
                        }
                    }
                } while (i <= 3);
                return null;
            }
        } catch (IllegalStateException e8) {
            e8.printStackTrace();
            System.exit(0);
        } catch (SecurityException unused) {
            throw new IOException("Security Exception");
        } catch (NoSuchElementException unused2) {
            return new byte[]{106, -126};
        } catch (Exception e9) {
            e9.printStackTrace();
            throw new IOException("Open channel failed");
        }
        return null;
    }
}
