package com.android.internal.telephony.ntnphone;

import com.android.internal.telephony.RIL$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteConfig implements Comparable<SatelliteConfig> {
    public static final int CAP_AUDIO_AP = 2;
    public static final int CAP_AUDIO_BP = 0;
    public static final int CAP_BBIC_COUPLING = 4;
    public static final int CAP_BBIC_SEPARATE = 0;
    public static final int CAP_SIM_HARD_SWAP = 0;
    public static final int CAP_SIM_SOFT_SWAP = 1;
    public int capability;
    public Chip chipType;
    public Type satelliteType;

    public enum Chip {
        SYLINCOM_701,
        CETC54,
        SYLINCOM_702,
        CYGNUSEMI_6810
    }

    public enum SimSwitchType {
        HARD_SWITCH,
        SOFT_SWITCH
    }

    public enum Type {
        TIANTONG_1,
        CSNG_1,
        CSNG_2
    }

    public static boolean isBitSet(int i, int i2) {
        return (i & (1 << i2)) != 0;
    }

    public static Type typeFromConfig(int i) throws RuntimeException {
        if (i == 0) {
            return Type.TIANTONG_1;
        }
        if (i == 1) {
            return Type.CSNG_1;
        }
        if (i == 2) {
            return Type.CSNG_2;
        }
        RIL$$ExternalSyntheticBUOutline0.m("illegal satellite xingzuo config ", i);
        return null;
    }

    public static Chip chipFromConfig(int i) throws RuntimeException {
        if (i == 0) {
            return Chip.SYLINCOM_701;
        }
        if (i == 1) {
            return Chip.CETC54;
        }
        if (i == 2) {
            return Chip.SYLINCOM_702;
        }
        if (i == 3) {
            return Chip.CYGNUSEMI_6810;
        }
        RIL$$ExternalSyntheticBUOutline0.m("illegal satellite chip type config ", i);
        return null;
    }

    public SimSwitchType getSimSwitchType() {
        return isBitSet(this.capability, 0) ? SimSwitchType.SOFT_SWITCH : SimSwitchType.HARD_SWITCH;
    }

    public boolean isSeparateBBICPower() {
        return !isBitSet(this.capability, 2);
    }

    public int getChipType() {
        return this.chipType.ordinal();
    }

    @Override // java.lang.Comparable
    public int compareTo(SatelliteConfig satelliteConfig) {
        return this.satelliteType.ordinal() - satelliteConfig.satelliteType.ordinal();
    }

    public String toString() {
        return "satelliteType: " + this.satelliteType + "; chipType: " + this.chipType + "; capability: " + this.capability;
    }
}
