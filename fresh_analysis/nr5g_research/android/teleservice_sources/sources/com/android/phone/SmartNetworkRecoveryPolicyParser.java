package com.android.phone;

import android.os.Environment;
import android.util.Slog;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/* JADX INFO: loaded from: classes.dex */
public abstract class SmartNetworkRecoveryPolicyParser {
    public static List getRecoveryPolicyCroup() {
        String str;
        int i;
        String str2 = "bPStatusLevel";
        String str3 = "aPStatusLevel";
        String str4 = "bPCauseSet";
        ArrayList arrayList = new ArrayList();
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(Environment.getProductDirectory(), "etc/smart-network-recovery-policy-config.xml"));
            document.getDocumentElement().normalize();
            NodeList elementsByTagName = document.getElementsByTagName("RecoveryPolicyGroup");
            int i2 = 0;
            int i3 = 0;
            while (i3 < elementsByTagName.getLength()) {
                Element element = (Element) elementsByTagName.item(i3);
                RecoveryPolicyGroup recoveryPolicyGroup = new RecoveryPolicyGroup();
                if (isElementExist(element, "isSwitchOn")) {
                    recoveryPolicyGroup.setIsSwitchOn(Integer.parseInt(element.getElementsByTagName("isSwitchOn").item(i2).getTextContent()));
                }
                if (isElementExist(element, "aPCauseSet")) {
                    recoveryPolicyGroup.setApCauseSet(element.getElementsByTagName("aPCauseSet").item(i2).getTextContent());
                }
                if (isElementExist(element, str4)) {
                    recoveryPolicyGroup.setBpCauseSet(element.getElementsByTagName(str4).item(i2).getTextContent());
                }
                if (isElementExist(element, str3)) {
                    recoveryPolicyGroup.setApStatusLevel(Integer.parseInt(element.getElementsByTagName(str3).item(i2).getTextContent()));
                }
                if (isElementExist(element, str2)) {
                    recoveryPolicyGroup.setBpStatusLevel(Integer.parseInt(element.getElementsByTagName(str2).item(i2).getTextContent()));
                }
                NodeList elementsByTagName2 = element != null ? element.getElementsByTagName("RecoveryPolicy") : null;
                if (elementsByTagName2 != null) {
                    int i4 = i2;
                    while (i4 < elementsByTagName2.getLength()) {
                        Element element2 = (Element) elementsByTagName2.item(i4);
                        String str5 = str2;
                        RecoveryPolicy recoveryPolicy = new RecoveryPolicy();
                        if (isElementExist(element2, "isSwitchOn")) {
                            recoveryPolicy.setIsSwitchOn(Integer.parseInt(element2.getElementsByTagName("isSwitchOn").item(0).getTextContent()));
                        }
                        if (isElementExist(element2, "policy")) {
                            recoveryPolicy.setRecoveryPolicy(Integer.parseInt(element2.getElementsByTagName("policy").item(0).getTextContent()));
                        }
                        if (isElementExist(element2, "retryTimes")) {
                            recoveryPolicy.setRetryTimes(Integer.parseInt(element2.getElementsByTagName("retryTimes").item(0).getTextContent()));
                        }
                        if (isElementExist(element2, "aPCauseSubset")) {
                            recoveryPolicy.setApCauseSubset(element2.getElementsByTagName("aPCauseSubset").item(0).getTextContent());
                        }
                        if (isElementExist(element2, "bPCauseSubset")) {
                            recoveryPolicy.setBpCauseSubset(element2.getElementsByTagName("bPCauseSubset").item(0).getTextContent());
                        }
                        recoveryPolicyGroup.addRecoveryPolicy(recoveryPolicy);
                        i4++;
                        str2 = str5;
                        str3 = str3;
                        str4 = str4;
                    }
                    str = str4;
                    i = 0;
                } else {
                    str = str4;
                    i = i2;
                }
                String str6 = str2;
                String str7 = str3;
                arrayList.add(recoveryPolicyGroup);
                i3++;
                i2 = i;
                str2 = str6;
                str3 = str7;
                str4 = str;
            }
        } catch (FileNotFoundException unused) {
            Slog.d("SmartNetworkRecoveryPolicyParser", "can not found /product/etc/smart-network-recovery-policy-config.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    private static boolean isElementExist(Element element, String str) {
        return (element == null || element.getElementsByTagName(str).item(0) == null) ? false : true;
    }
}
