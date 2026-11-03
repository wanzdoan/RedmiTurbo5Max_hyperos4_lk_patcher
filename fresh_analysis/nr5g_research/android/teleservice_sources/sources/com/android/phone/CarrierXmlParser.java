package com.android.phone;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;
import android.util.Log;
import com.ot.pubsub.a.a;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes.dex */
public class CarrierXmlParser {
    static String sParserFormat = "";
    HashMap mFeatureMaps;

    public class UssdParser {
        private Vector mParserStr = new Vector();
        private Pattern mPatternSuppServiceResponse;

        public UssdParser(String str) {
            this.mPatternSuppServiceResponse = Pattern.compile(str);
        }

        public void newFromResponseString(String str) {
            Matcher matcher = this.mPatternSuppServiceResponse.matcher(str);
            if (matcher.matches()) {
                this.mParserStr.clear();
                int iGroupCount = matcher.groupCount();
                for (int i = 0; i <= iGroupCount; i++) {
                    boolean zIsEmpty = TextUtils.isEmpty(matcher.group(i));
                    Vector vector = this.mParserStr;
                    if (!zIsEmpty) {
                        vector.add(matcher.group(i));
                    } else {
                        vector.add("");
                    }
                }
                return;
            }
            Log.d("CarrierXmlParser", "no match");
        }

        public Vector getResult() {
            return this.mParserStr;
        }
    }

    public class SsEntry {
        public String actionCode;
        public HashMap commandParameter = new HashMap();
        public HashMap responseFormat = new HashMap();
        public String serviceCode;
        public SSAction ssAction;

        public enum SSAction {
            UNKNOWN,
            QUERY,
            UPDATE_ACTIVATE,
            UPDATE_DEACTIVATE
        }

        public SsEntry(String str) {
            this.ssAction = SSAction.UNKNOWN;
            if (str.equals("query")) {
                this.ssAction = SSAction.QUERY;
            } else if (str.equals("activate")) {
                this.ssAction = SSAction.UPDATE_ACTIVATE;
            } else if (str.equals("deactivate")) {
                this.ssAction = SSAction.UPDATE_DEACTIVATE;
            }
        }

        public String toString() {
            return "SsEntry serviceCode:" + this.serviceCode + ", ssAction:" + this.ssAction + ", actionCode:" + this.actionCode + ", commandParameter:" + this.commandParameter.toString() + ", responseFormat:" + this.responseFormat.toString();
        }

        public String getCommandStructure() {
            String str = this.actionCode + this.serviceCode;
            int size = this.commandParameter.size();
            int i = 0;
            while (i < size) {
                i++;
                if (this.commandParameter.containsKey(Integer.valueOf(i)) && this.commandParameter.get(Integer.valueOf(i)) != null) {
                    str = str + "*" + ((String) this.commandParameter.get(Integer.valueOf(i)));
                }
            }
            String str2 = str + "#";
            Log.d("CarrierXmlParser", "getCommandStructure result:".concat(str2));
            return str2;
        }

        public String makeCommand(Map map) {
            String str;
            String strConcat = this.actionCode + this.serviceCode;
            int size = this.commandParameter.size();
            int i = 0;
            int i2 = 1;
            while (i < size) {
                if (this.commandParameter.containsKey(Integer.valueOf(i2))) {
                    if (map != null && map.size() > 0 && map.containsKey(this.commandParameter.get(Integer.valueOf(i2)))) {
                        str = (String) map.get(this.commandParameter.get(Integer.valueOf(i2)));
                    } else {
                        str = "";
                    }
                    if (TextUtils.isEmpty(str)) {
                        strConcat = strConcat + "*" + ((String) this.commandParameter.get(Integer.valueOf(i2)));
                    } else {
                        strConcat = strConcat + "*" + ((String) map.get(this.commandParameter.get(Integer.valueOf(i2))));
                    }
                    i++;
                } else {
                    strConcat = strConcat.concat("*");
                }
                i2++;
            }
            return strConcat.concat("#");
        }

        public HashMap getResponseSet(String str, HashMap map) {
            HashMap map2 = new HashMap();
            if (!TextUtils.isEmpty(CarrierXmlParser.sParserFormat)) {
                UssdParser ussdParser = new UssdParser(CarrierXmlParser.sParserFormat);
                ussdParser.newFromResponseString(str);
                Vector result = ussdParser.getResult();
                if (result != null) {
                    for (int i = 0; i < result.size(); i++) {
                        if (this.responseFormat.containsKey(Integer.valueOf(i))) {
                            String definitionByCompareValue = "";
                            if (map.containsKey(this.responseFormat.get(Integer.valueOf(i)))) {
                                for (int i2 = 0; i2 < ((ArrayList) map.get(this.responseFormat.get(Integer.valueOf(i)))).size(); i2++) {
                                    definitionByCompareValue = ((SsResultEntry) ((ArrayList) map.get(this.responseFormat.get(Integer.valueOf(i)))).get(i2)).getDefinitionByCompareValue((String) result.get(i));
                                    if (!TextUtils.isEmpty(definitionByCompareValue)) {
                                        break;
                                    }
                                }
                                if ("status_code".equals(this.responseFormat.get(Integer.valueOf(i))) && TextUtils.isEmpty(definitionByCompareValue)) {
                                    map2.put("RESPONSE_ERROR", "RESPONSE_ERROR");
                                }
                            }
                            boolean zIsEmpty = TextUtils.isEmpty(definitionByCompareValue);
                            HashMap map3 = this.responseFormat;
                            if (zIsEmpty) {
                                map2.put((String) map3.get(Integer.valueOf(i)), (String) result.get(i));
                            } else {
                                map2.put((String) map3.get(Integer.valueOf(i)), definitionByCompareValue);
                            }
                        }
                    }
                }
            }
            return map2;
        }
    }

    public class SsResultEntry {
        String mCompareValue;
        String mDefinition;

        public String toString() {
            return "SsResultEntry mDefinition:" + this.mDefinition + ", mCompareValue:" + this.mCompareValue;
        }

        public String getDefinitionByCompareValue(String str) {
            if (this.mCompareValue.equals(str)) {
                return this.mDefinition;
            }
            return null;
        }
    }

    public class SsFeature {
        public HashMap ssEntryHashMap = new HashMap();
        public HashMap responseCode = new HashMap();

        public SsFeature(CarrierXmlParser carrierXmlParser) {
        }

        private String getResponseCodeString() {
            Iterator it = this.responseCode.entrySet().iterator();
            String str = "";
            while (it.hasNext()) {
                ArrayList arrayList = (ArrayList) ((Map.Entry) it.next()).getValue();
                for (int i = 0; i < arrayList.size(); i++) {
                    str = str + "value of i is " + ((SsResultEntry) arrayList.get(i)).toString();
                }
            }
            return str;
        }

        public String toString() {
            return getResponseCodeString();
        }

        public String getCommandStructure(SsEntry.SSAction sSAction) {
            return ((SsEntry) this.ssEntryHashMap.get(sSAction)).getCommandStructure();
        }

        public String makeCommand(SsEntry.SSAction sSAction, Map map) {
            return ((SsEntry) this.ssEntryHashMap.get(sSAction)).makeCommand(map);
        }

        public HashMap getResponseSet(SsEntry.SSAction sSAction, String str) {
            return ((SsEntry) this.ssEntryHashMap.get(sSAction)).getResponseSet(str, this.responseCode);
        }
    }

    public CarrierXmlParser(Context context, int i) {
        int identifier;
        if (i != -1) {
            try {
                identifier = context.getResources().getIdentifier("carrier_ss_string_" + i, "xml", context.getPackageName());
            } catch (Exception e) {
                Log.d("CarrierXmlParser", "Error parsing XML " + e.toString());
                return;
            }
        } else {
            identifier = 0;
        }
        identifier = identifier == 0 ? R.xml.carrier_ss_string : identifier;
        Log.d("CarrierXmlParser", "carrierId: " + i);
        this.mFeatureMaps = parseXml(context.getResources().getXml(identifier));
    }

    private HashMap parseXml(XmlResourceParser xmlResourceParser) {
        HashMap map = new HashMap();
        try {
            int eventType = xmlResourceParser.getEventType();
            while (eventType != 1) {
                if (eventType == 2) {
                    if ("regular_parser".equals(xmlResourceParser.getName())) {
                        sParserFormat = readText(xmlResourceParser);
                        Log.d("CarrierXmlParser", "sParserFormat " + sParserFormat);
                    } else if (a.n.equals(xmlResourceParser.getName())) {
                        String specificAttributeValue = getSpecificAttributeValue(xmlResourceParser, "name");
                        if (!TextUtils.isEmpty(specificAttributeValue)) {
                            SsFeature ssFeatureGenerateFeatureList = generateFeatureList(xmlResourceParser);
                            map.put(specificAttributeValue, ssFeatureGenerateFeatureList);
                            Log.d("CarrierXmlParser", "add " + specificAttributeValue + " to map:" + ssFeatureGenerateFeatureList.toString());
                        }
                    }
                }
                xmlResourceParser.next();
                eventType = xmlResourceParser.getEventType();
            }
            return map;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return map;
        }
    }

    private SsFeature generateFeatureList(XmlResourceParser xmlResourceParser) {
        SsFeature ssFeature = new SsFeature(this);
        int depth = xmlResourceParser.getDepth();
        Log.d("CarrierXmlParser", "generateFeatureList outerDepth" + depth);
        while (xmlResourceParser.next() != 1) {
            Log.d("CarrierXmlParser", "generateFeatureList parser.getDepth()" + xmlResourceParser.getDepth());
            int eventType = xmlResourceParser.getEventType();
            if (eventType == 3 && depth == xmlResourceParser.getDepth()) {
                break;
            }
            if (eventType == 2) {
                String name = xmlResourceParser.getName();
                if ("command".equals(name)) {
                    SsEntry commandEntry = readCommandEntry(xmlResourceParser);
                    ssFeature.ssEntryHashMap.put(commandEntry.ssAction, commandEntry);
                } else if ("command_result".equals(name)) {
                    readCommandResultEntry(xmlResourceParser, ssFeature);
                }
            }
        }
        return ssFeature;
    }

    private void readCommandResultEntry(XmlResourceParser xmlResourceParser, SsFeature ssFeature) {
        while (xmlResourceParser.next() != 1) {
            int eventType = xmlResourceParser.getEventType();
            if (eventType == 3 && "command_result".equals(xmlResourceParser.getName())) {
                return;
            }
            if (eventType == 2 && "entry".equals(xmlResourceParser.getName())) {
                String specificAttributeValue = getSpecificAttributeValue(xmlResourceParser, "key");
                if (!TextUtils.isEmpty(specificAttributeValue)) {
                    SsResultEntry ssResultEntry = new SsResultEntry();
                    ssResultEntry.mDefinition = getSpecificAttributeValue(xmlResourceParser, "definition");
                    ssResultEntry.mCompareValue = readText(xmlResourceParser);
                    if (ssFeature.responseCode.containsKey(specificAttributeValue)) {
                        ((ArrayList) ssFeature.responseCode.get(specificAttributeValue)).add(ssResultEntry);
                    } else {
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(ssResultEntry);
                        ssFeature.responseCode.put(specificAttributeValue, arrayList);
                    }
                }
            }
        }
    }

    private SsEntry readCommandEntry(XmlResourceParser xmlResourceParser) {
        int eventType;
        int depth = xmlResourceParser.getDepth();
        SsEntry ssEntry = new SsEntry(getSpecificAttributeValue(xmlResourceParser, "name"));
        while (xmlResourceParser.next() != 1 && ((eventType = xmlResourceParser.getEventType()) != 3 || depth != xmlResourceParser.getDepth())) {
            if (eventType == 2) {
                String name = xmlResourceParser.getName();
                if ("service_code".equals(name)) {
                    ssEntry.serviceCode = readText(xmlResourceParser);
                } else if ("action_code".equals(name)) {
                    ssEntry.actionCode = readText(xmlResourceParser);
                } else if ("parameter".equals(name)) {
                    String specificAttributeValue = getSpecificAttributeValue(xmlResourceParser, "number");
                    if (!TextUtils.isEmpty(specificAttributeValue)) {
                        readParameters(xmlResourceParser, ssEntry, Integer.valueOf(specificAttributeValue).intValue(), "parameter");
                    }
                } else if ("response_format".equals(name)) {
                    String specificAttributeValue2 = getSpecificAttributeValue(xmlResourceParser, "number");
                    if (!TextUtils.isEmpty(specificAttributeValue2)) {
                        readParameters(xmlResourceParser, ssEntry, Integer.valueOf(specificAttributeValue2).intValue(), "response_format");
                    }
                }
            }
        }
        Log.d("CarrierXmlParser", "ssEntry:" + ssEntry.toString());
        return ssEntry;
    }

    private void readParameters(XmlResourceParser xmlResourceParser, SsEntry ssEntry, int i, String str) {
        Log.d("CarrierXmlParser", "readParameters() nume:" + i);
        int i2 = 0;
        while (i2 < i) {
            if (xmlResourceParser.next() == 2 && "entry".equals(xmlResourceParser.getName())) {
                String specificAttributeValue = getSpecificAttributeValue(xmlResourceParser, "position");
                if (!TextUtils.isEmpty(specificAttributeValue)) {
                    if ("parameter".equals(str)) {
                        String text = readText(xmlResourceParser);
                        if (!TextUtils.isEmpty(text)) {
                            ssEntry.commandParameter.put(Integer.valueOf(specificAttributeValue), text);
                        }
                    } else if ("response_format".equals(str)) {
                        String specificAttributeValue2 = getSpecificAttributeValue(xmlResourceParser, "key");
                        if (!TextUtils.isEmpty(specificAttributeValue2)) {
                            ssEntry.responseFormat.put(Integer.valueOf(specificAttributeValue), specificAttributeValue2);
                        }
                    }
                    i2++;
                }
            }
        }
    }

    private String getSpecificAttributeValue(XmlResourceParser xmlResourceParser, String str) {
        String attributeValue = "";
        for (int i = 0; i < xmlResourceParser.getAttributeCount(); i++) {
            if (str.equals(xmlResourceParser.getAttributeName(i))) {
                attributeValue = xmlResourceParser.getAttributeValue(i);
            }
        }
        return attributeValue;
    }

    private String readText(XmlResourceParser xmlResourceParser) {
        if (xmlResourceParser.next() == 4) {
            String text = xmlResourceParser.getText();
            xmlResourceParser.nextTag();
            return text;
        }
        return "";
    }

    public SsFeature getFeature(String str) {
        return (SsFeature) this.mFeatureMaps.get(str);
    }

    public SsEntry.SSAction getCallerIdUssdCommandAction(String str) {
        if (isCallerIdActivate(str)) {
            return SsEntry.SSAction.UPDATE_ACTIVATE;
        }
        if (isCallerIdDeactivate(str)) {
            return SsEntry.SSAction.UPDATE_DEACTIVATE;
        }
        return SsEntry.SSAction.UNKNOWN;
    }

    private String getCallerIdActivateCommandFromXml() {
        return getFeature("callerid").getCommandStructure(SsEntry.SSAction.UPDATE_ACTIVATE);
    }

    private String getCallerIdDeactivateCommandFromXml() {
        return getFeature("callerid").getCommandStructure(SsEntry.SSAction.UPDATE_DEACTIVATE);
    }

    private boolean isCallerIdActivate(String str) {
        return compareCommand(getCallerIdActivateCommandFromXml(), str);
    }

    private boolean isCallerIdDeactivate(String str) {
        return compareCommand(getCallerIdDeactivateCommandFromXml(), str);
    }

    private boolean compareCommand(String str, String str2) {
        String[] strArrSplit = str.split("\\*");
        String[] strArrSplit2 = str2.split("\\*");
        if (strArrSplit.length == 0 || strArrSplit2.length == 0) {
            return false;
        }
        for (int i = 0; i < strArrSplit.length; i++) {
            if (!strArrSplit[i].startsWith("tag_") && !strArrSplit[i].equals(strArrSplit2[i])) {
                Log.d("CarrierXmlParser", "compare fails:" + str + "," + str2);
                return false;
            }
        }
        Log.d("CarrierXmlParser", "compare success");
        return true;
    }
}
