package com.android.internal.telephony.server.updates;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.util.EventLog;
import android.util.Slog;
import com.android.internal.telephony.HexDump;
import com.android.internal.telephony.SomeArgs$$ExternalSyntheticBUOutline0;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import libcore.io.IoUtils;
import libcore.io.Streams;

/* JADX INFO: loaded from: classes.dex */
public class ConfigUpdateInstallReceiver extends BroadcastReceiver {
    protected final File updateContent;
    protected final File updateDir;
    protected final File updateVersion;

    protected void postInstall(Context context, Intent intent) {
    }

    protected boolean verifyVersion(int i, int i2) {
        return i < i2;
    }

    public ConfigUpdateInstallReceiver(String str, String str2, String str3, String str4) {
        this.updateDir = new File(str);
        this.updateContent = new File(str, str2);
        this.updateVersion = new File(new File(str, str3), str4);
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(final Context context, final Intent intent) {
        new Thread() { // from class: com.android.internal.telephony.server.updates.ConfigUpdateInstallReceiver.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    int versionFromIntent = ConfigUpdateInstallReceiver.this.getVersionFromIntent(intent);
                    String requiredHashFromIntent = ConfigUpdateInstallReceiver.this.getRequiredHashFromIntent(intent);
                    int currentVersion = ConfigUpdateInstallReceiver.this.getCurrentVersion();
                    String currentHash = ConfigUpdateInstallReceiver.getCurrentHash(ConfigUpdateInstallReceiver.this.getCurrentContent());
                    if (!ConfigUpdateInstallReceiver.this.verifyVersion(currentVersion, versionFromIntent)) {
                        Slog.i("ConfigUpdateInstallReceiver", "Not installing, new version is <= current version");
                        return;
                    }
                    if (ConfigUpdateInstallReceiver.this.verifyPreviousHash(currentHash, requiredHashFromIntent)) {
                        Slog.i("ConfigUpdateInstallReceiver", "Found new update, installing...");
                        BufferedInputStream altContent = ConfigUpdateInstallReceiver.this.getAltContent(context, intent);
                        try {
                            ConfigUpdateInstallReceiver.this.install(altContent, versionFromIntent);
                            if (altContent != null) {
                                altContent.close();
                            }
                            Slog.i("ConfigUpdateInstallReceiver", "Installation successful");
                            ConfigUpdateInstallReceiver.this.postInstall(context, intent);
                            return;
                        } catch (Throwable th) {
                            if (altContent != null) {
                                try {
                                    altContent.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            }
                            throw th;
                        }
                    }
                    EventLog.writeEvent(51300, "Current hash did not match required value");
                } catch (Exception e) {
                    Slog.e("ConfigUpdateInstallReceiver", "Could not update content!", e);
                    String string = e.toString();
                    if (string.length() > 100) {
                        string = string.substring(0, 99);
                    }
                    EventLog.writeEvent(51300, string);
                }
            }
        }.start();
    }

    private Uri getContentFromIntent(Intent intent) {
        Uri data = intent.getData();
        if (data != null) {
            return data;
        }
        SomeArgs$$ExternalSyntheticBUOutline0.m("Missing required content path, ignoring.");
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getVersionFromIntent(Intent intent) {
        String stringExtra = intent.getStringExtra("VERSION");
        if (stringExtra == null) {
            SomeArgs$$ExternalSyntheticBUOutline0.m("Missing required version number, ignoring.");
            return 0;
        }
        return Integer.parseInt(stringExtra.trim());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getRequiredHashFromIntent(Intent intent) {
        String stringExtra = intent.getStringExtra("REQUIRED_HASH");
        if (stringExtra == null) {
            SomeArgs$$ExternalSyntheticBUOutline0.m("Missing required previous hash, ignoring.");
            return null;
        }
        return stringExtra.trim();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCurrentVersion() {
        try {
            return Integer.parseInt(IoUtils.readFileAsString(this.updateVersion.getCanonicalPath()).trim());
        } catch (IOException unused) {
            Slog.i("ConfigUpdateInstallReceiver", "Couldn't find current metadata, assuming first update");
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BufferedInputStream getAltContent(Context context, Intent intent) {
        Uri contentFromIntent = getContentFromIntent(intent);
        Binder.allowBlockingForCurrentThread();
        try {
            return new BufferedInputStream(context.getContentResolver().openInputStream(contentFromIntent));
        } finally {
            Binder.defaultBlockingForCurrentThread();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public byte[] getCurrentContent() {
        try {
            return IoUtils.readFileAsByteArray(this.updateContent.getCanonicalPath());
        } catch (IOException unused) {
            Slog.i("ConfigUpdateInstallReceiver", "Failed to read current content, assuming first update!");
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getCurrentHash(byte[] bArr) {
        if (bArr == null) {
            return "0";
        }
        try {
            return HexDump.toHexString(MessageDigest.getInstance("SHA512").digest(bArr), false);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean verifyPreviousHash(String str, String str2) {
        if (str2.equals("NONE")) {
            return true;
        }
        return str.equals(str2);
    }

    protected void writeUpdate(File file, File file2, InputStream inputStream) throws Throwable {
        FileOutputStream fileOutputStream;
        File file3 = null;
        try {
            File parentFile = file2.getParentFile();
            parentFile.mkdirs();
            if (!parentFile.exists()) {
                throw new IOException("Failed to create directory " + parentFile.getCanonicalPath());
            }
            while (!parentFile.equals(this.updateDir)) {
                parentFile.setExecutable(true, false);
                parentFile = parentFile.getParentFile();
            }
            File fileCreateTempFile = File.createTempFile("journal", "", file);
            try {
                fileCreateTempFile.setReadable(true, false);
                fileOutputStream = new FileOutputStream(fileCreateTempFile);
                try {
                    Streams.copy(inputStream, fileOutputStream);
                    fileOutputStream.getFD().sync();
                    if (!fileCreateTempFile.renameTo(file2)) {
                        throw new IOException("Failed to atomically rename " + file2.getCanonicalPath());
                    }
                    fileCreateTempFile.delete();
                    IoUtils.closeQuietly(fileOutputStream);
                } catch (Throwable th) {
                    file3 = fileCreateTempFile;
                    th = th;
                    if (file3 != null) {
                        file3.delete();
                    }
                    IoUtils.closeQuietly(fileOutputStream);
                    throw th;
                }
            } catch (Throwable th2) {
                file3 = fileCreateTempFile;
                th = th2;
                fileOutputStream = null;
            }
        } catch (Throwable th3) {
            th = th3;
            fileOutputStream = null;
        }
    }

    protected void install(InputStream inputStream, int i) throws Throwable {
        writeUpdate(this.updateDir, this.updateContent, inputStream);
        writeUpdate(this.updateDir, this.updateVersion, new ByteArrayInputStream(Long.toString(i).getBytes()));
    }
}
