package com.xiaomi.internal.telephony;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* JADX INFO: loaded from: classes.dex */
public class XMLContentHandler extends DefaultHandler {
    public static final String LOG_TAG = "XMLContentHandler";
    private DeviceSarConfig mCurrenDevice;
    private List<DeviceSarConfig> mSarConfigList = null;
    private String mSarVersion = null;
    private String mTagName = null;
    private String mTagAttrValue = null;

    public List<DeviceSarConfig> getDeviceSarConfig() {
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
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (localName.equals("deviceSarConfig")) {
            this.mCurrenDevice = new DeviceSarConfig();
            if (attributes.getValue("device_name") != null) {
                this.mCurrenDevice.setDeviceName(attributes.getValue("device_name"));
            }
        } else if (localName.equals("prop") || localName.equals("extendProp")) {
            this.mTagAttrValue = attributes.getValue("prop_name");
        } else if (localName.equals("dsimap")) {
            this.mTagAttrValue = attributes.getValue("value");
        }
        this.mTagName = localName;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.mTagName != null) {
            String data = new String(ch, start, length);
            if (this.mTagName.equals("listeners")) {
                this.mCurrenDevice.setListeners(data);
                return;
            }
            if (this.mTagName.equals("prop")) {
                this.mCurrenDevice.setPropList(this.mTagAttrValue, data);
                return;
            }
            if (this.mTagName.equals("extendProp")) {
                this.mCurrenDevice.setExtendPropList(this.mTagAttrValue, data);
                return;
            }
            if (this.mTagName.equals("dsimap")) {
                this.mCurrenDevice.setDsiList(this.mTagAttrValue, data);
                return;
            }
            if (this.mTagName.equals("sarVersion")) {
                this.mSarVersion = data;
                return;
            }
            if (this.mTagName.equals("antennaNums")) {
                this.mCurrenDevice.setAntennaNums(data);
                return;
            }
            if (this.mTagName.equals("sensorAntennaGroup")) {
                this.mCurrenDevice.setAntennaGroup(data);
                return;
            }
            if (this.mTagName.equals("defaultAntennaGroup")) {
                this.mCurrenDevice.setDefaultGroup(data);
            } else if (this.mTagName.equals("rxDefaultDsi")) {
                this.mCurrenDevice.setRxDefaultDsi(data);
            } else if (this.mTagName.equals("simMccList")) {
                this.mCurrenDevice.setSimMccList(data);
            }
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("deviceSarConfig")) {
            this.mSarConfigList.add(this.mCurrenDevice);
            this.mCurrenDevice = null;
        }
        this.mTagName = null;
        this.mTagAttrValue = null;
    }
}
