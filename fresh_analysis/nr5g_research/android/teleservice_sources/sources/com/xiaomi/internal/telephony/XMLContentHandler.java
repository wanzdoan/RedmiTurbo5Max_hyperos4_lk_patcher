package com.xiaomi.internal.telephony;

import com.ot.pubsub.a.a;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* JADX INFO: loaded from: classes.dex */
public class XMLContentHandler extends DefaultHandler {
    public static final String LOG_TAG = "XMLContentHandler";
    private DeviceSarConfig mCurrenDevice;
    private List mSarConfigList = null;
    private String mSarVersion = null;
    private String mTagName = null;
    private String mTagAttrValue = null;

    public List getDeviceSarConfig() {
        ModemSarLog.i(LOG_TAG, "getDeviceConfig, mConfigList = " + this.mSarConfigList);
        return this.mSarConfigList;
    }

    public String getSarVersion() {
        ModemSarLog.i(LOG_TAG, "getVersion, version = " + this.mSarVersion);
        return this.mSarVersion;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        this.mSarConfigList = new ArrayList();
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
        if (str2.equals("deviceSarConfig")) {
            this.mCurrenDevice = new DeviceSarConfig();
            if (attributes.getValue("device_name") != null) {
                this.mCurrenDevice.setDeviceName(attributes.getValue("device_name"));
            }
        } else if (str2.equals("prop") || str2.equals("extendProp")) {
            this.mTagAttrValue = attributes.getValue("prop_name");
        } else if (str2.equals("dsimap")) {
            this.mTagAttrValue = attributes.getValue(a.p);
        }
        this.mTagName = str2;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] cArr, int i, int i2) throws SAXException {
        if (this.mTagName != null) {
            String str = new String(cArr, i, i2);
            if (this.mTagName.equals("listeners")) {
                this.mCurrenDevice.setListeners(str);
                return;
            }
            if (this.mTagName.equals("prop")) {
                this.mCurrenDevice.setPropList(this.mTagAttrValue, str);
                return;
            }
            if (this.mTagName.equals("extendProp")) {
                this.mCurrenDevice.setExtendPropList(this.mTagAttrValue, str);
                return;
            }
            if (this.mTagName.equals("dsimap")) {
                this.mCurrenDevice.setDsiList(this.mTagAttrValue, str);
                return;
            }
            if (this.mTagName.equals("sarVersion")) {
                this.mSarVersion = str;
                return;
            }
            if (this.mTagName.equals("antennaNums")) {
                this.mCurrenDevice.setAntennaNums(str);
                return;
            }
            if (this.mTagName.equals("sensorAntennaGroup")) {
                this.mCurrenDevice.setAntennaGroup(str);
                return;
            }
            if (this.mTagName.equals("defaultAntennaGroup")) {
                this.mCurrenDevice.setDefaultGroup(str);
            } else if (this.mTagName.equals("rxDefaultDsi")) {
                this.mCurrenDevice.setRxDefaultDsi(str);
            } else if (this.mTagName.equals("simMccList")) {
                this.mCurrenDevice.setSimMccList(str);
            }
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String str, String str2, String str3) throws SAXException {
        if (str2.equals("deviceSarConfig")) {
            this.mSarConfigList.add(this.mCurrenDevice);
            this.mCurrenDevice = null;
        }
        this.mTagName = null;
        this.mTagAttrValue = null;
    }
}
