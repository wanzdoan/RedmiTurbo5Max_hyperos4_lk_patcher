package com.android.internal.telephony.uicc;

import android.os.Environment;
import android.util.Xml;
import com.android.internal.telephony.util.XmlUtils;
import com.android.telephony.Rlog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes.dex */
public class VoiceMailConstants {
    private HashMap CarrierVmMap = new HashMap();

    VoiceMailConstants() {
        loadVoiceMail();
    }

    public boolean containsCarrier(String str) {
        return this.CarrierVmMap.containsKey(str);
    }

    String getVoiceMailNumber(String str) {
        return ((String[]) this.CarrierVmMap.get(str))[1];
    }

    String getVoiceMailTag(String str) {
        return ((String[]) this.CarrierVmMap.get(str))[2];
    }

    private void loadVoiceMail() {
        try {
            try {
                FileReader fileReader = new FileReader(new File(Environment.getRootDirectory(), "etc/voicemail-conf.xml"));
                try {
                    try {
                        try {
                            XmlPullParser xmlPullParserNewPullParser = Xml.newPullParser();
                            xmlPullParserNewPullParser.setInput(fileReader);
                            XmlUtils.beginDocument(xmlPullParserNewPullParser, "voicemail");
                            while (true) {
                                XmlUtils.nextElement(xmlPullParserNewPullParser);
                                if ("voicemail".equals(xmlPullParserNewPullParser.getName())) {
                                    this.CarrierVmMap.put(xmlPullParserNewPullParser.getAttributeValue(null, "numeric"), new String[]{xmlPullParserNewPullParser.getAttributeValue(null, "carrier"), xmlPullParserNewPullParser.getAttributeValue(null, "vmnumber"), xmlPullParserNewPullParser.getAttributeValue(null, "vmtag")});
                                } else {
                                    fileReader.close();
                                    return;
                                }
                            }
                        } catch (IOException e) {
                            Rlog.w("VoiceMailConstants", "Exception in Voicemail parser " + e);
                            fileReader.close();
                        }
                    } catch (XmlPullParserException e2) {
                        Rlog.w("VoiceMailConstants", "Exception in Voicemail parser " + e2);
                        fileReader.close();
                    }
                } catch (Throwable th) {
                    try {
                        fileReader.close();
                    } catch (IOException unused) {
                    }
                    throw th;
                }
            } catch (IOException unused2) {
            }
        } catch (FileNotFoundException unused3) {
            Rlog.w("VoiceMailConstants", "Can't open " + Environment.getRootDirectory() + "/etc/voicemail-conf.xml");
        }
    }
}
