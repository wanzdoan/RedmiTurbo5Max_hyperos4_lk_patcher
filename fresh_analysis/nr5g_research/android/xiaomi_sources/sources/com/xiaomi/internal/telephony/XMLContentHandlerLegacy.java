package com.xiaomi.internal.telephony;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* JADX INFO: loaded from: classes.dex */
public class XMLContentHandlerLegacy extends DefaultHandler {
    public static final String TAG = "XMLContentHandlerLegacy";
    private DeviceSarConfigLegacy mCurrenDevice;
    private List<DeviceSarConfigLegacy> mSarConfigList = null;
    private String tagName = null;
    private String tagAttrValue = null;

    public List<DeviceSarConfigLegacy> getDeviceSarConfig() {
        return this.mSarConfigList;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        this.mSarConfigList = new ArrayList();
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (localName.equals("deviceSarConfig")) {
            this.mCurrenDevice = new DeviceSarConfigLegacy();
            if (attributes.getValue("device_name") != null) {
                this.mCurrenDevice.setDeviceName(attributes.getValue("device_name"));
            }
        } else if (localName.equals("prop")) {
            this.tagAttrValue = attributes.getValue("prop_name");
        } else if (localName.equals("dsimap")) {
            this.tagAttrValue = attributes.getValue("value");
        }
        this.tagName = localName;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.tagName != null) {
            String data = new String(ch, start, length);
            if (this.tagName.equals("listeners")) {
                this.mCurrenDevice.setListeners(data);
            } else if (this.tagName.equals("prop")) {
                this.mCurrenDevice.setPropList(this.tagAttrValue, data);
            } else if (this.tagName.equals("dsimap")) {
                this.mCurrenDevice.setDsiList(this.tagAttrValue, data);
            }
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("deviceSarConfig")) {
            this.mSarConfigList.add(this.mCurrenDevice);
            this.mCurrenDevice = null;
        }
        this.tagName = null;
        this.tagAttrValue = null;
    }
}
