package com.android.internal.telephony.satellite;

import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

/* JADX INFO: loaded from: classes.dex */
public final class SatellitePlmnNetworkInfo {
    private List mAllowedPlmns;
    private List mDisallowedPlmns;

    public SatellitePlmnNetworkInfo(List<NetworkInfo> list, List<NetworkInfo> list2) {
        this.mAllowedPlmns = new ArrayList();
        this.mDisallowedPlmns = new ArrayList();
        Objects.requireNonNull(list);
        this.mAllowedPlmns = Collections.unmodifiableList(list);
        Objects.requireNonNull(list2);
        this.mDisallowedPlmns = Collections.unmodifiableList(list2);
    }

    public List<NetworkInfo> getAllowedPlmns() {
        return this.mAllowedPlmns;
    }

    public List<NetworkInfo> getDisallowedPlmns() {
        return this.mDisallowedPlmns;
    }

    public NetworkInfo[] getAllowedPlmnsAsArray() {
        return (NetworkInfo[]) this.mAllowedPlmns.toArray(new NetworkInfo[0]);
    }

    public NetworkInfo[] getDisallowedPlmnsAsArray() {
        return (NetworkInfo[]) this.mDisallowedPlmns.toArray(new NetworkInfo[0]);
    }

    public static final class NetworkInfo {
        private final int mAccessNetwork;
        private final int[] mArfcns;
        private final boolean mHasSamePriorityAsTn;
        private final String mPlmn;
        private final int mSatelliteTechnology;

        private NetworkInfo(Builder builder) {
            this.mPlmn = builder.mPlmn;
            this.mArfcns = builder.mArfcns;
            this.mAccessNetwork = builder.mAccessNetwork;
            this.mSatelliteTechnology = builder.mSatelliteTechnology;
            this.mHasSamePriorityAsTn = builder.mHasSamePriorityAsTn;
        }

        public String getPlmn() {
            return this.mPlmn;
        }

        public int[] getArfcns() {
            return this.mArfcns;
        }

        public int getAccessNetwork() {
            return this.mAccessNetwork;
        }

        public int getSatelliteTechnology() {
            return this.mSatelliteTechnology;
        }

        public boolean hasSamePriorityAsTn() {
            return this.mHasSamePriorityAsTn;
        }

        public static NetworkInfo createWithDefaults(String str) {
            return new Builder(str).build();
        }

        public static final class Builder {
            private int mAccessNetwork;
            private int[] mArfcns;
            private boolean mHasSamePriorityAsTn;
            private final String mPlmn;
            private int mSatelliteTechnology;

            public Builder(String str) {
                this.mArfcns = new int[0];
                this.mAccessNetwork = 0;
                this.mSatelliteTechnology = 0;
                this.mHasSamePriorityAsTn = false;
                Objects.requireNonNull(str);
                this.mPlmn = str;
            }

            public Builder setArfcns(int[] iArr) {
                Objects.requireNonNull(iArr);
                this.mArfcns = iArr;
                return this;
            }

            public Builder setAccessNetwork(int i) {
                this.mAccessNetwork = i;
                return this;
            }

            public Builder setSatelliteTechnology(int i) {
                this.mSatelliteTechnology = i;
                return this;
            }

            public Builder setHasSamePriorityAsTn(boolean z) {
                this.mHasSamePriorityAsTn = z;
                return this;
            }

            public NetworkInfo build() {
                return new NetworkInfo(this);
            }

            public Builder(NetworkInfo networkInfo) {
                this.mArfcns = new int[0];
                this.mAccessNetwork = 0;
                this.mSatelliteTechnology = 0;
                this.mHasSamePriorityAsTn = false;
                this.mPlmn = networkInfo.mPlmn;
                this.mArfcns = networkInfo.mArfcns;
                this.mAccessNetwork = networkInfo.mAccessNetwork;
                this.mSatelliteTechnology = networkInfo.mSatelliteTechnology;
                this.mHasSamePriorityAsTn = networkInfo.mHasSamePriorityAsTn;
            }
        }

        public android.hardware.radio.network.NetworkInfo toHalNetworkInfo() {
            android.hardware.radio.network.NetworkInfo networkInfo = new android.hardware.radio.network.NetworkInfo();
            networkInfo.plmn = this.mPlmn;
            networkInfo.arfcns = this.mArfcns;
            networkInfo.accessNetwork = this.mAccessNetwork;
            networkInfo.satelliteTechnology = toHalSatelliteTechnology(this.mSatelliteTechnology);
            networkInfo.hasSamePriorityAsTn = this.mHasSamePriorityAsTn;
            return networkInfo;
        }

        private static int toHalSatelliteTechnology(int i) {
            if (i == 1) {
                return 1;
            }
            if (i == 2) {
                return 3;
            }
            if (i == 5 || i == 6) {
                return 2;
            }
            SatellitePlmnNetworkInfo.logd("Handle unsupported or unknown technologies as SAT_TECH_NONE or default");
            return 0;
        }

        public String toString() {
            return "NetworkInfo{mPlmn='" + this.mPlmn + "', mArfcns=" + Arrays.toString(this.mArfcns) + ", mAccessNetwork=" + this.mAccessNetwork + ", mSatelliteTechnology=" + this.mSatelliteTechnology + ", mHasSamePriorityAsTn=" + this.mHasSamePriorityAsTn + '}';
        }
    }

    /* JADX WARN: Code duplicated, block: B:24:0x008b  */
    /* JADX WARN: Code duplicated, block: B:25:0x008f  */
    public static SatellitePlmnNetworkInfo fromPlmn(int i, List<String> list, List<String> list2) {
        logd("fromPlmn: subId=" + i + ", allowedPlmns=" + list + ", disallowedPlmns=" + list2);
        if (i == -1) {
            logd("fromPlmn: Invalid subId");
            return new SatellitePlmnNetworkInfo(new ArrayList(), new ArrayList());
        }
        SatelliteController satelliteController = SatelliteController.getInstance();
        ArrayList arrayList = new ArrayList();
        if (list != null) {
            for (String str : list) {
                if (!TextUtils.isEmpty(str)) {
                    Iterator<Integer> it = satelliteController.getSupportedSatelliteTechnologies(i, str).iterator();
                    while (it.hasNext()) {
                        int iIntValue = it.next().intValue();
                        NetworkInfo.Builder builder = new NetworkInfo.Builder(str);
                        builder.setSatelliteTechnology(iIntValue);
                        if (iIntValue == 1) {
                            builder.setAccessNetwork(3);
                        } else if (iIntValue == 2) {
                            builder.setAccessNetwork(6);
                        } else if (iIntValue == 5) {
                            builder.setAccessNetwork(3);
                        } else if (iIntValue == 6) {
                            builder.setAccessNetwork(6);
                        } else {
                            builder.setAccessNetwork(0);
                        }
                        arrayList.add(builder.build());
                    }
                }
            }
        }
        ArrayList arrayList2 = new ArrayList();
        if (list2 != null) {
            for (String str2 : list2) {
                if (!TextUtils.isEmpty(str2)) {
                    arrayList2.add(NetworkInfo.createWithDefaults(str2));
                }
            }
        }
        return new SatellitePlmnNetworkInfo(arrayList, arrayList2);
    }

    public android.hardware.radio.network.SatelliteNetworkInfo toHalSatelliteNetworkInfo() {
        android.hardware.radio.network.SatelliteNetworkInfo satelliteNetworkInfo = new android.hardware.radio.network.SatelliteNetworkInfo();
        satelliteNetworkInfo.allowedPlmns = (android.hardware.radio.network.NetworkInfo[]) this.mAllowedPlmns.stream().map(new Function() { // from class: com.android.internal.telephony.satellite.SatellitePlmnNetworkInfo$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((SatellitePlmnNetworkInfo.NetworkInfo) obj).toHalNetworkInfo();
            }
        }).toArray(new IntFunction() { // from class: com.android.internal.telephony.satellite.SatellitePlmnNetworkInfo$$ExternalSyntheticLambda1
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return SatellitePlmnNetworkInfo.m1630$r8$lambda$yWJ_Ay_iPKzTsZTmMaZqPwnhjE(i);
            }
        });
        satelliteNetworkInfo.disallowedPlmns = (android.hardware.radio.network.NetworkInfo[]) this.mDisallowedPlmns.stream().map(new Function() { // from class: com.android.internal.telephony.satellite.SatellitePlmnNetworkInfo$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((SatellitePlmnNetworkInfo.NetworkInfo) obj).toHalNetworkInfo();
            }
        }).toArray(new IntFunction() { // from class: com.android.internal.telephony.satellite.SatellitePlmnNetworkInfo$$ExternalSyntheticLambda2
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return SatellitePlmnNetworkInfo.$r8$lambda$dPDZS7egXHuf1GJuHZdlYX5PQb0(i);
            }
        });
        return satelliteNetworkInfo;
    }

    /* JADX INFO: renamed from: $r8$lambda$-yWJ_Ay_iPKzTsZTmMaZqPwnhjE, reason: not valid java name */
    public static /* synthetic */ android.hardware.radio.network.NetworkInfo[] m1630$r8$lambda$yWJ_Ay_iPKzTsZTmMaZqPwnhjE(int i) {
        return new android.hardware.radio.network.NetworkInfo[i];
    }

    public static /* synthetic */ android.hardware.radio.network.NetworkInfo[] $r8$lambda$dPDZS7egXHuf1GJuHZdlYX5PQb0(int i) {
        return new android.hardware.radio.network.NetworkInfo[i];
    }

    public String toString() {
        return "SatellitePlmnNetworkInfo{mAllowedPlmns=" + this.mAllowedPlmns + ", mDisallowedPlmns=" + this.mDisallowedPlmns + '}';
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String str) {
        Log.d("SatellitePlmnNetworkInfo", str);
    }
}
