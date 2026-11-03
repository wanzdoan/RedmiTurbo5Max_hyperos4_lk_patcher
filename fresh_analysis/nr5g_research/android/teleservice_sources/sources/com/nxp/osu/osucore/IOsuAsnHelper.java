package com.nxp.osu.osucore;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
interface IOsuAsnHelper {
    int getAmdhResumeIdx(int i);

    List getApdus(int i);

    List getCCIApdus(boolean z);

    int getDERFSN();

    int getDERRSN();

    List getJCIApdus(boolean z);

    int getMatchingAction(int i, int i2, int i3);

    int getNumOfSteps();

    int getOsuType();

    byte[] getSHA256Hash();

    byte[] getSHA384Hash();

    byte[] getSignaturePlain();

    byte[] getSignaturePlainECC384();

    boolean isResetRequiredAfterAction(int i);

    int loadFile(String str);

    byte[] toByteArray(String str);
}
