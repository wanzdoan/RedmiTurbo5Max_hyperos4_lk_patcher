package com.android.internal.telephony.test;

import android.os.Bundle;
import android.telephony.ims.ImsConferenceState;
import android.util.Log;
import android.util.Xml;
import com.android.internal.telephony.util.XmlUtils;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes.dex */
public class TestConferenceEventPackageParser {
    private InputStream mInputStream;

    public TestConferenceEventPackageParser(InputStream inputStream) {
        this.mInputStream = inputStream;
    }

    public ImsConferenceState parse() {
        ImsConferenceState imsConferenceState = new ImsConferenceState();
        try {
            try {
                XmlPullParser xmlPullParserNewPullParser = Xml.newPullParser();
                xmlPullParserNewPullParser.setInput(this.mInputStream, null);
                xmlPullParserNewPullParser.nextTag();
                int depth = xmlPullParserNewPullParser.getDepth();
                while (XmlUtils.nextElementWithin(xmlPullParserNewPullParser, depth)) {
                    if (xmlPullParserNewPullParser.getName().equals("participant")) {
                        Log.v("TestConferenceEventPackageParser", "Found participant.");
                        Bundle participant = parseParticipant(xmlPullParserNewPullParser);
                        imsConferenceState.mParticipants.put(participant.getString("endpoint"), participant);
                    }
                }
                try {
                    this.mInputStream.close();
                    return imsConferenceState;
                } catch (IOException e) {
                    Log.e("TestConferenceEventPackageParser", "Failed to close test conference event package InputStream", e);
                    return null;
                }
            } catch (IOException | XmlPullParserException e2) {
                Log.e("TestConferenceEventPackageParser", "Failed to read test conference event package from XML file", e2);
                try {
                    this.mInputStream.close();
                    return null;
                } catch (IOException e3) {
                    Log.e("TestConferenceEventPackageParser", "Failed to close test conference event package InputStream", e3);
                    return null;
                }
            }
        } catch (Throwable th) {
            try {
                this.mInputStream.close();
                throw th;
            } catch (IOException e4) {
                Log.e("TestConferenceEventPackageParser", "Failed to close test conference event package InputStream", e4);
                return null;
            }
        }
    }

    private Bundle parseParticipant(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        Bundle bundle = new Bundle();
        int depth = xmlPullParser.getDepth();
        String text = "";
        String text2 = "";
        String text3 = text2;
        String text4 = text3;
        while (XmlUtils.nextElementWithin(xmlPullParser, depth)) {
            if (xmlPullParser.getName().equals("user")) {
                xmlPullParser.next();
                text = xmlPullParser.getText();
            } else if (xmlPullParser.getName().equals("display-text")) {
                xmlPullParser.next();
                text2 = xmlPullParser.getText();
            } else if (xmlPullParser.getName().equals("endpoint")) {
                xmlPullParser.next();
                text3 = xmlPullParser.getText();
            } else if (xmlPullParser.getName().equals("status")) {
                xmlPullParser.next();
                text4 = xmlPullParser.getText();
            }
        }
        Log.v("TestConferenceEventPackageParser", "User: " + text);
        Log.v("TestConferenceEventPackageParser", "DisplayText: " + text2);
        Log.v("TestConferenceEventPackageParser", "Endpoint: " + text3);
        Log.v("TestConferenceEventPackageParser", "Status: " + text4);
        bundle.putString("user", text);
        bundle.putString("display-text", text2);
        bundle.putString("endpoint", text3);
        bundle.putString("status", text4);
        return bundle;
    }
}
