package com.android.internal.telephony.metrics;

import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccPort;
import com.android.internal.telephony.uicc.UiccProfile;
import com.android.internal.telephony.uicc.UiccSlot;
import com.android.telephony.Rlog;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/* JADX INFO: loaded from: classes.dex */
public class SimSlotState {
    private static final String TAG = "SimSlotState";
    public final int numActiveEsimSlots;
    public final int numActiveEsims;
    public final int numActiveMepSlots;
    public final int numActiveSims;
    public final int numActiveSlots;
    public final int numEsimSlots;

    public static SimSlotState getCurrentState() {
        UiccController uiccController = UiccController.getInstance();
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        for (int i7 = 0; i7 < uiccController.getUiccSlots().length; i7++) {
            UiccSlot uiccSlot = uiccController.getUiccSlot(i7);
            if (uiccSlot != null) {
                if (uiccSlot.isEuicc()) {
                    i6++;
                }
                if (uiccSlot.isActive()) {
                    i++;
                    if (uiccSlot.isEuicc()) {
                        i4++;
                    }
                    if (uiccSlot.getCardState() == IccCardStatus.CardState.CARDSTATE_PRESENT) {
                        if (uiccSlot.isEuicc()) {
                            UiccCard uiccCard = uiccSlot.getUiccCard();
                            if (uiccCard != null) {
                                int iCount = (int) Arrays.stream(uiccCard.getUiccPortList()).filter(new Predicate() { // from class: com.android.internal.telephony.metrics.SimSlotState$$ExternalSyntheticLambda0
                                    @Override // java.util.function.Predicate
                                    public final boolean test(Object obj) {
                                        return Objects.nonNull((UiccPort) obj);
                                    }
                                }).map(new Function() { // from class: com.android.internal.telephony.metrics.SimSlotState$$ExternalSyntheticLambda1
                                    @Override // java.util.function.Function
                                    public final Object apply(Object obj) {
                                        return ((UiccPort) obj).getUiccProfile();
                                    }
                                }).filter(new Predicate() { // from class: com.android.internal.telephony.metrics.SimSlotState$$ExternalSyntheticLambda2
                                    @Override // java.util.function.Predicate
                                    public final boolean test(Object obj) {
                                        return Objects.nonNull((UiccProfile) obj);
                                    }
                                }).filter(new Predicate() { // from class: com.android.internal.telephony.metrics.SimSlotState$$ExternalSyntheticLambda3
                                    @Override // java.util.function.Predicate
                                    public final boolean test(Object obj) {
                                        return SimSlotState.m1502$r8$lambda$8zQi_2eVNm0vfTkwDtP81RZ1as((UiccProfile) obj);
                                    }
                                }).count();
                                i2 += iCount;
                                i3 += iCount;
                                if (iCount > 1) {
                                    i5++;
                                }
                            }
                        } else {
                            i2++;
                        }
                    }
                }
            }
        }
        return new SimSlotState(i, i2, i3, i4, i5, i6);
    }

    /* JADX INFO: renamed from: $r8$lambda$8zQi_2eV-Nm0vfTkwDtP81RZ1as, reason: not valid java name */
    public static /* synthetic */ boolean m1502$r8$lambda$8zQi_2eVNm0vfTkwDtP81RZ1as(UiccProfile uiccProfile) {
        return uiccProfile.getNumApplications() > 0;
    }

    private SimSlotState(int i, int i2, int i3, int i4, int i5, int i6) {
        this.numActiveSlots = i;
        this.numActiveSims = i2;
        this.numActiveEsims = i3;
        this.numActiveEsimSlots = i4;
        this.numActiveMepSlots = i5;
        this.numEsimSlots = i6;
    }

    public static boolean isEsim(int i) {
        UiccSlot uiccSlotForPhone = UiccController.getInstance().getUiccSlotForPhone(i);
        if (uiccSlotForPhone != null) {
            return uiccSlotForPhone.isEuicc();
        }
        Rlog.e(TAG, "isEsim: slot=null for phone " + i);
        return false;
    }

    public static boolean isMultiSim() {
        return getCurrentState().numActiveSims > 1;
    }
}
