package com.android.phone;

import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public abstract class ParserHexStr {
    static Map CPSTInfo = Map.ofEntries(Map.entry("1", "正在搜网"), Map.entry("2", "搜网成功"), Map.entry("3", "搜网失败"), Map.entry("4", "正在RRC链接建立"), Map.entry("5", "RRC链接建立成功"), Map.entry("6", "RRC链接建立失败"), Map.entry("7", "正在初始直传"), Map.entry("8", "正在身份认证"), Map.entry("9", "身份认证成功"), Map.entry("10", "身份认证失败"), Map.entry("11", "正在CS鉴权"), Map.entry("12", "终端CS鉴权成功"), Map.entry("13", "CS鉴权失败"), Map.entry("14", "正在PS鉴权"), Map.entry("15", "终端PS鉴权成功"), Map.entry("16", "PS鉴权失败"), Map.entry("17", "正在安全配置"), Map.entry("18", "安全配置成功"), Map.entry("19", "安全配置失败"), Map.entry("20", "CS注册成功"), Map.entry("21", "PS注册成功"), Map.entry("22", "双域注册成功"), Map.entry("23", "CS注册失败"), Map.entry("24", "PS注册失败"), Map.entry("25", "双域注册失败"), Map.entry("26", "正在释放RRC连接"), Map.entry("27", "待机状态"), Map.entry("28", "IMSI去注册"), Map.entry("29", "GPRS去注册"), Map.entry("30", "双域去注册"), Map.entry("31", "发起SERVICE请求"), Map.entry("32", "SERVICE请求拒绝"), Map.entry("33", "主叫接通中"), Map.entry("34", "主叫接通"), Map.entry("35", "终端请求挂机"), Map.entry("36", "网络请求挂机"), Map.entry("37", "紧急呼叫接通中"), Map.entry("38", "紧急呼叫接通"), Map.entry("39", "收到高穿透振铃"), Map.entry("40", "收到寻呼"), Map.entry("41", "发送寻呼响应"), Map.entry("42", "被叫接通中"), Map.entry("43", "被叫接通"), Map.entry("44", "终端请求PDP激活"), Map.entry("45", "网侧请求PDP激活"), Map.entry("46", "PDP激活成功"), Map.entry("47", "PDP激活失败"), Map.entry("48", "终端请求PDP去激活"), Map.entry("49", "网侧请求PDP去激活"), Map.entry("50", "PDP去激活成功"), Map.entry("51", "PDP去激活失败"), Map.entry("52", "SIM卡自检中"), Map.entry("53", "正在读取SIM卡"), Map.entry("54", "终端发短信中"), Map.entry("55", "网侧发短信中"), Map.entry("56", "信令连接释放"), Map.entry("57", "双域未注册"), Map.entry("58", "正在UE能力查询"), Map.entry("59", "UE能力上报"), Map.entry("60", "正在小区更新"), Map.entry("61", "小区更新成功"), Map.entry("62", "正在GRA更新"), Map.entry("63", "GRA更新成功"), Map.entry("64", "正在物联网注册"), Map.entry("65", "物联网鉴权完成"), Map.entry("66", "物联网注册成功"), Map.entry("67", "物联网注册失败"), Map.entry("68", "HPA待机"), Map.entry("69", "物理层无线链路失败"), Map.entry("70", "RLC链路失败"), Map.entry("71", "短消息主叫"), Map.entry("72", "语音主叫"), Map.entry("73", "紧急呼叫主叫"), Map.entry("74", "重选过程"), Map.entry("75", "短消息发送成功"), Map.entry("76", "短消息接收成功"));

    public static Map CPSTInfoDir() {
        return CPSTInfo;
    }

    public static int hexStr0(String str) {
        if (str == null || str.length() < 2) {
            return -1;
        }
        return Integer.parseInt(str, 16);
    }

    public static int[] hexStr2(String str) {
        int[] iArr = new int[2];
        if (str != null && str.length() >= 4) {
            iArr[0] = Integer.parseInt(str.substring(0, 2), 16);
            iArr[1] = Integer.parseInt(str.substring(2, 4), 16);
        }
        return iArr;
    }

    public static int hexStr14(String str) {
        if (str == null || str.length() < 16) {
            return -9999;
        }
        int i = Integer.parseInt(str.substring(4, 8), 16);
        return str.substring(4, 5) == "F" ? -i : i;
    }

    public static int[] hexStr58(String str) {
        int[] iArr = new int[6];
        if (str != null && str.length() >= 4) {
            iArr[0] = Integer.parseInt(str.substring(0, 8), 16);
            iArr[1] = Integer.parseInt(str.substring(8, 10), 16);
            iArr[2] = Integer.parseInt(str.substring(10, 12), 16);
            iArr[3] = Integer.parseInt(str.substring(12, 14), 16);
            iArr[4] = Integer.parseInt(str.substring(14, 16), 16);
            iArr[5] = Integer.parseInt(str.substring(16, 18), 16);
        }
        return iArr;
    }

    public static int[] hexStr60(String str) {
        int[] iArr = new int[2];
        if (str != null && str.length() >= 112) {
            iArr[0] = Integer.parseInt(str.substring(42, 50), 16);
            iArr[1] = Integer.parseInt(str.substring(50, 58), 16);
        }
        return iArr;
    }
}
