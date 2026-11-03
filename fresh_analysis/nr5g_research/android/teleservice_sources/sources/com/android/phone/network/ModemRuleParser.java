package com.android.phone.network;

import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* JADX INFO: loaded from: classes.dex */
public abstract class ModemRuleParser {
    public static HashMap parseNetworkRuleConfig() {
        HashMap map = new HashMap();
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(Environment.getProductDirectory(), "etc/smart-modem-schedule-config.xml"));
            document.getDocumentElement().normalize();
            NodeList elementsByTagName = document.getElementsByTagName("typeQoeConfig");
            for (int i = 0; i < elementsByTagName.getLength(); i++) {
                Node nodeItem = elementsByTagName.item(i);
                if (nodeItem.getNodeType() == 1) {
                    Element element = (Element) nodeItem;
                    String attribute = element.getAttribute("typeName");
                    Log.d("ModemRuleParser", "Parsing QoE Config for: " + attribute);
                    NodeList elementsByTagName2 = element.getElementsByTagName("ModemRule");
                    ArrayList arrayList = new ArrayList();
                    for (int i2 = 0; i2 < elementsByTagName2.getLength(); i2++) {
                        Element element2 = (Element) elementsByTagName2.item(i2);
                        ModemRule modemRule = new ModemRule();
                        modemRule.setSwitchOn(Integer.parseInt(element2.getElementsByTagName("isSwitchOn").item(0).getTextContent()) == 1);
                        modemRule.setModemRuleCause(Integer.parseInt(element2.getElementsByTagName("causeVaule").item(0).getTextContent()));
                        NodeList elementsByTagName3 = element2.getElementsByTagName("ul_grant_ratio");
                        if (elementsByTagName3.getLength() > 0) {
                            modemRule.setUlGrantRatio(Integer.parseInt(elementsByTagName3.item(0).getTextContent()));
                        }
                        NodeList elementsByTagName4 = element2.getElementsByTagName("dl_grant_ratio");
                        if (elementsByTagName4.getLength() > 0) {
                            modemRule.setDlGrantRatio(Integer.parseInt(elementsByTagName4.item(0).getTextContent()));
                        }
                        NodeList elementsByTagName5 = element2.getElementsByTagName("ul_speed");
                        if (elementsByTagName5.getLength() > 0) {
                            modemRule.setUlSpeed(Integer.parseInt(elementsByTagName5.item(0).getTextContent()));
                        }
                        NodeList elementsByTagName6 = element2.getElementsByTagName("dl_speed");
                        if (elementsByTagName6.getLength() > 0) {
                            modemRule.setDlSpeed(Integer.parseInt(elementsByTagName6.item(0).getTextContent()));
                        }
                        NodeList elementsByTagName7 = element2.getElementsByTagName("ul_bler");
                        if (elementsByTagName7.getLength() > 0) {
                            modemRule.setUlBler(Integer.parseInt(elementsByTagName7.item(0).getTextContent()));
                        }
                        NodeList elementsByTagName8 = element2.getElementsByTagName("dl_bler");
                        if (elementsByTagName8.getLength() > 0) {
                            modemRule.setDlBler(Integer.parseInt(elementsByTagName8.item(0).getTextContent()));
                        }
                        NodeList elementsByTagName9 = element2.getElementsByTagName("weak_rsrp");
                        if (elementsByTagName9.getLength() > 0) {
                            String[] strArrSplit = elementsByTagName9.item(0).getTextContent().split(",");
                            if (strArrSplit.length != 2) {
                                Log.d("ModemRuleParser", "invalid paramter weakRsrpNodes.");
                            } else {
                                modemRule.setWeakRsrp(new int[]{Integer.parseInt(strArrSplit[0]), Integer.parseInt(strArrSplit[1])});
                            }
                        }
                        NodeList elementsByTagName10 = element2.getElementsByTagName("weak_snr");
                        if (elementsByTagName10.getLength() > 0) {
                            String[] strArrSplit2 = elementsByTagName10.item(0).getTextContent().split(",");
                            if (strArrSplit2.length != 2) {
                                Log.d("ModemRuleParser", "invalid paramter weakSnrNodes.");
                            } else {
                                modemRule.setWeakSnr(new int[]{Integer.parseInt(strArrSplit2[0]), Integer.parseInt(strArrSplit2[1])});
                            }
                        }
                        arrayList.add(modemRule);
                    }
                    map.put(attribute, arrayList);
                }
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return map;
        }
    }
}
