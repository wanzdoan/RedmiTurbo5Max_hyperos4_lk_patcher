package com.android.phone.module;

import android.content.Context;
import android.os.Bundle;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public interface IModemModule {
    default void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    default void dumpModemInfo(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    default void dumpModemTcpKa(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    default long getCellPingPongCounts() {
        return 0L;
    }

    default void getDataStallInfoFromModem(int i, HashMap map) {
    }

    default void getModemSimAbnormalInfo(int i, ArrayList arrayList) {
    }

    default void getRfFatalDeviceStatus() {
    }

    default void imsErrorCausedByMBN(String str, int i) {
    }

    default boolean isModemDumpFeatureSupported(int i) {
        return false;
    }

    default void modemHydraProcess(int i, Object obj) {
    }

    default void modemHydraSendDelayMessage(int i, Object obj, int i2) {
    }

    default void onModemTcpKaCloudChanged(boolean z) {
    }

    default void performModemLogUploadDailyJob() {
    }

    default void processTopAppChanged(String str) {
    }

    default void registerModemTcpKaCallback(Object obj) {
    }

    default void reportModemAiStats() {
    }

    default void reportModemHydraStat() {
    }

    default void reportModemTcpKaStatistics() {
    }

    default void reportRfDeviceStatus() {
    }

    default void reportRfMainChipStatus() {
    }

    default void reportTxFbrxFault(Bundle bundle) {
    }

    default void setFastDormancyConfig(Context context, int i) {
    }

    default void startCaptureModemLog(String str, String str2) {
    }

    default int startTcpKeepAlive(String str, int i, String str2, int i2, int i3, int i4) {
        return 0;
    }

    default int startTcpMonitorRegister(String str, int i, String str2, int i2, int i3) {
        return 0;
    }

    default int stopTcpKeepAlive(int i) {
        return 0;
    }

    default int stopTcpMonitorRegister(int i) {
        return 0;
    }

    default void unregisterModemTcpKaCallback(Object obj) {
    }

    default void updateAppScene(String str, boolean z) {
    }

    default void updateModemHydraStat(String str, boolean z, int i) {
    }

    default void updatePingpongCellStat(int i) {
    }

    default void updateScreenState(boolean z) {
    }

    default String processUeNoSaInfo(int i) {
        return "";
    }

    default ArrayList getRachFailRateInfo() {
        return new ArrayList();
    }

    default JSONObject getAftersaleFromHydra() {
        return new JSONObject();
    }

    default StringBuilder getModemDumpAftersaleInfo() {
        return new StringBuilder();
    }
}
