package com.android.internal.telephony;

import android.os.SystemClock;
import android.telephony.ClientRequestStats;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ClientWakelockTracker {
    public static final String LOG_TAG = "ClientWakelockTracker";
    public HashMap<String, ClientWakelockAccountant> mClients = new HashMap<>();
    public ArrayList<ClientWakelockAccountant> mActiveClients = new ArrayList<>();

    public void startTracking(String str, int i, int i2, int i3) {
        ClientWakelockAccountant clientWakelockAccountant = getClientWakelockAccountant(str);
        long jUptimeMillis = SystemClock.uptimeMillis();
        clientWakelockAccountant.startAttributingWakelock(i, i2, i3, jUptimeMillis);
        updateConcurrentRequests(i3, jUptimeMillis);
        synchronized (this.mActiveClients) {
            try {
                if (!this.mActiveClients.contains(clientWakelockAccountant)) {
                    this.mActiveClients.add(clientWakelockAccountant);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void stopTracking(String str, int i, int i2, int i3) {
        ClientWakelockAccountant clientWakelockAccountant = getClientWakelockAccountant(str);
        long jUptimeMillis = SystemClock.uptimeMillis();
        clientWakelockAccountant.stopAttributingWakelock(i, i2, jUptimeMillis);
        if (clientWakelockAccountant.getPendingRequestCount() == 0) {
            synchronized (this.mActiveClients) {
                this.mActiveClients.remove(clientWakelockAccountant);
            }
        }
        updateConcurrentRequests(i3, jUptimeMillis);
    }

    public void stopTrackingAll() {
        long jUptimeMillis = SystemClock.uptimeMillis();
        synchronized (this.mActiveClients) {
            try {
                ArrayList<ClientWakelockAccountant> arrayList = this.mActiveClients;
                int size = arrayList.size();
                int i = 0;
                while (i < size) {
                    ClientWakelockAccountant clientWakelockAccountant = arrayList.get(i);
                    i++;
                    clientWakelockAccountant.stopAllPendingRequests(jUptimeMillis);
                }
                this.mActiveClients.clear();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    List getClientRequestStats() {
        ArrayList arrayList;
        long jUptimeMillis = SystemClock.uptimeMillis();
        synchronized (this.mClients) {
            try {
                arrayList = new ArrayList(this.mClients.size());
                Iterator<String> it = this.mClients.keySet().iterator();
                while (it.hasNext()) {
                    ClientWakelockAccountant clientWakelockAccountant = this.mClients.get(it.next());
                    clientWakelockAccountant.updatePendingRequestWakelockTime(jUptimeMillis);
                    arrayList.add(new ClientRequestStats(clientWakelockAccountant.mRequestStats));
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return arrayList;
    }

    private ClientWakelockAccountant getClientWakelockAccountant(String str) {
        ClientWakelockAccountant clientWakelockAccountant;
        synchronized (this.mClients) {
            try {
                if (this.mClients.containsKey(str)) {
                    clientWakelockAccountant = this.mClients.get(str);
                } else {
                    ClientWakelockAccountant clientWakelockAccountant2 = new ClientWakelockAccountant(str);
                    this.mClients.put(str, clientWakelockAccountant2);
                    clientWakelockAccountant = clientWakelockAccountant2;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return clientWakelockAccountant;
    }

    private void updateConcurrentRequests(int i, long j) {
        if (i != 0) {
            synchronized (this.mActiveClients) {
                try {
                    ArrayList<ClientWakelockAccountant> arrayList = this.mActiveClients;
                    int size = arrayList.size();
                    int i2 = 0;
                    while (i2 < size) {
                        ClientWakelockAccountant clientWakelockAccountant = arrayList.get(i2);
                        i2++;
                        clientWakelockAccountant.changeConcurrentRequests(i, j);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public boolean isClientActive(String str) {
        ClientWakelockAccountant clientWakelockAccountant = getClientWakelockAccountant(str);
        synchronized (this.mActiveClients) {
            try {
                return this.mActiveClients.contains(clientWakelockAccountant);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    void dumpClientRequestTracker(PrintWriter printWriter) {
        printWriter.println("-------mClients---------------");
        synchronized (this.mClients) {
            try {
                for (String str : this.mClients.keySet()) {
                    printWriter.println("Client : " + str);
                    printWriter.println(this.mClients.get(str).toString());
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
