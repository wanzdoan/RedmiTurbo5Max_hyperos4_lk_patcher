package com.xiaomi.internal.telephony;

import com.ot.pubsub.a.a;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* JADX INFO: loaded from: classes.dex */
public class XMLContentHandlerLegacy extends DefaultHandler {
    public static final String TAG = "XMLContentHandlerLegacy";
    private DeviceSarConfigLegacy mCurrenDevice;
    private List mSarConfigList = null;
    private String tagName = null;
    private String tagAttrValue = null;

    public List getDeviceSarConfig() {
        return this.mSarConfigList;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        this.mSarConfigList = new ArrayList();
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
        if (str2.equals("deviceSarConfig")) {
            this.mCurrenDevice = new DeviceSarConfigLegacy();
            if (attributes.getValue("device_name") != null) {
                this.mCurrenDevice.setDeviceName(attributes.getValue("device_name"));
            }
        } else if (str2.equals("prop")) {
            this.tagAttrValue = attributes.getValue("prop_name");
        } else if (str2.equals("dsimap")) {
            this.tagAttrValue = attributes.getValue(a.p);
        }
        this.tagName = str2;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] cArr, int i, int i2) throws SAXException {
        if (this.tagName != null) {
            String str = new String(cArr, i, i2);
            if (this.tagName.equals("listeners")) {
                this.mCurrenDevice.setListeners(str);
            } else if (this.tagName.equals("prop")) {
                this.mCurrenDevice.setPropList(this.tagAttrValue, str);
            } else if (this.tagName.equals("dsimap")) {
                this.mCurrenDevice.setDsiList(this.tagAttrValue, str);
            }
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String str, String str2, String str3) throws SAXException {
        if (str2.equals("deviceSarConfig")) {
            this.mSarConfigList.add(this.mCurrenDevice);
            this.mCurrenDevice = null;
        }
        this.tagName = null;
        this.tagAttrValue = null;
    }
}
