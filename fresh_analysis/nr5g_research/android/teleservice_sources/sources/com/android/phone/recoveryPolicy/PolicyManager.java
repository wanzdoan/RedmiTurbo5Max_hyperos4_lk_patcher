package com.android.phone.recoveryPolicy;

import android.content.Context;
import android.util.Log;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.ot.pubsub.a.a;
import java.io.IOException;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes.dex */
public class PolicyManager {
    private Context mContext;
    private static final HashMap policies = new HashMap();
    private static volatile PolicyManager mInstance = null;

    private PolicyManager() {
        init();
    }

    private void init() {
        this.mContext = PhoneGlobals.getInstance();
        createPolicy();
    }

    public static PolicyManager getInstance() {
        if (mInstance == null) {
            synchronized (PolicyManager.class) {
                try {
                    if (mInstance == null) {
                        mInstance = new PolicyManager();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return mInstance;
    }

    public Policy getPolicy(String str) {
        return (Policy) policies.get(str);
    }

    private void createPolicy() {
        Log.i("PolicyManager", "createPolicy");
        policies.put("ims", loadPolicyXml(this.mContext.getResources().getXml(R.xml.ImsPolicy), new ImsPolicy()));
    }

    private Policy loadPolicyXml(XmlPullParser xmlPullParser, Policy policy) {
        if (xmlPullParser == null) {
            Log.i("PolicyManager", " loadPolicyXml none file");
            return policy;
        }
        try {
            xmlPullParser.next();
            parsePolicyXML(xmlPullParser, policy);
            return policy;
        } catch (Exception e) {
            e.printStackTrace();
            return policy;
        }
    }

    private void parsePolicyXML(XmlPullParser xmlPullParser, Policy policy) throws XmlPullParserException, IOException {
        Log.i("PolicyManager", "parsePolicyXML");
        while (xmlPullParser.nextTag() != 1) {
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                Log.i("PolicyManager", "parsePolicyXML name=" + xmlPullParser.getName());
                if (name.equals("policy")) {
                    Policy.Action action = new Policy.Action();
                    action.name = xmlPullParser.getAttributeValue(null, "name");
                    Log.i("PolicyManager", "parsePolicyXML action = " + action.name);
                    readAction(xmlPullParser, policy, action);
                }
            } else if (xmlPullParser.getEventType() == 3 && "policies".equals(xmlPullParser.getName())) {
                return;
            }
        }
    }

    private void readAction(XmlPullParser xmlPullParser, Policy policy, Policy.Action action) throws XmlPullParserException, IOException {
        while (xmlPullParser.nextTag() != 3) {
            if (xmlPullParser.getName().equals("condition")) {
                String attributeValue = xmlPullParser.getAttributeValue(null, "key");
                String attributeValue2 = xmlPullParser.getAttributeValue(null, a.p);
                Log.i("PolicyManager", "readAction: key = " + attributeValue + " value=" + attributeValue2);
                action.conditions.put(attributeValue, attributeValue2);
                xmlPullParser.nextTag();
            }
        }
        policy.addAction(action);
    }
}
