package android.internal.telephony.sysprop;

import android.os.SystemProperties;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public final class TelephonyProperties {
    /* JADX INFO: Access modifiers changed from: private */
    public static Boolean tryParseBoolean(String str) {
        if (str == null) {
            return null;
        }
        String lowerCase = str.toLowerCase(Locale.US);
        lowerCase.hashCode();
        switch (lowerCase) {
            case "0":
            case "false":
                return Boolean.FALSE;
            case "1":
            case "true":
                return Boolean.TRUE;
            default:
                return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Integer tryParseInteger(String str) {
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    private static Long tryParseLong(String str) {
        try {
            return Long.valueOf(str);
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String tryParseString(String str) {
        if ("".equals(str)) {
            return null;
        }
        return str;
    }

    private static List tryParseList(Function function, String str) {
        if ("".equals(str)) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (true) {
            StringBuilder sb = new StringBuilder();
            while (i < str.length() && str.charAt(i) != ',') {
                if (str.charAt(i) == '\\') {
                    i++;
                }
                if (i == str.length()) {
                    break;
                }
                sb.append(str.charAt(i));
                i++;
            }
            arrayList.add(function.apply(sb.toString()));
            if (i == str.length()) {
                return arrayList;
            }
            i++;
        }
    }

    private static String escape(String str) {
        return str.replaceAll("([\\\\,])", "\\\\$1");
    }

    private static String formatList(List list) {
        StringJoiner stringJoiner = new StringJoiner(",");
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Object next = it.next();
            stringJoiner.add(next == null ? "" : escape(next.toString()));
        }
        return stringJoiner.toString();
    }

    public static Optional<Boolean> airplane_mode_on() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("persist.radio.airplane_mode_on")));
    }

    public static void airplane_mode_on(Boolean bool) {
        String str;
        if (bool == null) {
            str = "";
        } else {
            str = bool.booleanValue() ? "1" : "0";
        }
        SystemProperties.set("persist.radio.airplane_mode_on", str);
    }

    public static List<String> baseband_version() {
        return tryParseList(new Function() { // from class: android.internal.telephony.sysprop.TelephonyProperties$$ExternalSyntheticLambda11
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return TelephonyProperties.tryParseString((String) obj);
            }
        }, SystemProperties.get("gsm.version.baseband"));
    }

    public static void baseband_version(List<String> list) {
        SystemProperties.set("gsm.version.baseband", list == null ? "" : formatList(list));
    }

    public static Optional<String> ril_impl() {
        return Optional.ofNullable(tryParseString(SystemProperties.get("gsm.version.ril-impl")));
    }

    public static List<String> operator_alpha() {
        return tryParseList(new Function() { // from class: android.internal.telephony.sysprop.TelephonyProperties$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return TelephonyProperties.tryParseString((String) obj);
            }
        }, SystemProperties.get("gsm.operator.alpha"));
    }

    public static void operator_alpha(List<String> list) {
        SystemProperties.set("gsm.operator.alpha", list == null ? "" : formatList(list));
    }

    public static List<String> operator_numeric() {
        return tryParseList(new Function() { // from class: android.internal.telephony.sysprop.TelephonyProperties$$ExternalSyntheticLambda13
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return TelephonyProperties.tryParseString((String) obj);
            }
        }, SystemProperties.get("gsm.operator.numeric"));
    }

    public static void operator_numeric(List<String> list) {
        SystemProperties.set("gsm.operator.numeric", list == null ? "" : formatList(list));
    }

    public static Optional<Boolean> operator_is_manual() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("operator.ismanual")));
    }

    public static List<Boolean> operator_is_roaming() {
        return tryParseList(new Function() { // from class: android.internal.telephony.sysprop.TelephonyProperties$$ExternalSyntheticLambda9
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return TelephonyProperties.tryParseBoolean((String) obj);
            }
        }, SystemProperties.get("gsm.operator.isroaming"));
    }

    public static void operator_is_roaming(List<Boolean> list) {
        SystemProperties.set("gsm.operator.isroaming", list == null ? "" : formatList(list));
    }

    public static List<String> operator_iso_country() {
        return tryParseList(new Function() { // from class: android.internal.telephony.sysprop.TelephonyProperties$$ExternalSyntheticLambda16
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return TelephonyProperties.tryParseString((String) obj);
            }
        }, SystemProperties.get("gsm.operator.iso-country"));
    }

    public static void operator_iso_country(List<String> list) {
        SystemProperties.set("gsm.operator.iso-country", list == null ? "" : formatList(list));
    }

    public static Optional<String> lte_on_cdma_product_type() {
        return Optional.ofNullable(tryParseString(SystemProperties.get("telephony.lteOnCdmaProductType")));
    }

    public static Optional<Integer> lte_on_cdma_device() {
        return Optional.ofNullable(tryParseInteger(SystemProperties.get("telephony.lteOnCdmaDevice")));
    }

    public static List<Integer> current_active_phone() {
        return tryParseList(new Function() { // from class: android.internal.telephony.sysprop.TelephonyProperties$$ExternalSyntheticLambda7
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return TelephonyProperties.tryParseInteger((String) obj);
            }
        }, SystemProperties.get("gsm.current.phone-type"));
    }

    public static void current_active_phone(List<Integer> list) {
        SystemProperties.set("gsm.current.phone-type", list == null ? "" : formatList(list));
    }

    public static List<String> sim_state() {
        return tryParseList(new Function() { // from class: android.internal.telephony.sysprop.TelephonyProperties$$ExternalSyntheticLambda12
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return TelephonyProperties.tryParseString((String) obj);
            }
        }, SystemProperties.get("gsm.sim.state"));
    }

    public static void sim_state(List<String> list) {
        SystemProperties.set("gsm.sim.state", list == null ? "" : formatList(list));
    }

    public static List<String> icc_operator_numeric() {
        return tryParseList(new Function() { // from class: android.internal.telephony.sysprop.TelephonyProperties$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return TelephonyProperties.tryParseString((String) obj);
            }
        }, SystemProperties.get("gsm.sim.operator.numeric"));
    }

    public static void icc_operator_numeric(List<String> list) {
        SystemProperties.set("gsm.sim.operator.numeric", list == null ? "" : formatList(list));
    }

    public static List<String> icc_operator_alpha() {
        return tryParseList(new Function() { // from class: android.internal.telephony.sysprop.TelephonyProperties$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return TelephonyProperties.tryParseString((String) obj);
            }
        }, SystemProperties.get("gsm.sim.operator.alpha"));
    }

    public static void icc_operator_alpha(List<String> list) {
        SystemProperties.set("gsm.sim.operator.alpha", list == null ? "" : formatList(list));
    }

    public static List<String> operator_orig_alpha() {
        return tryParseList(new Function() { // from class: android.internal.telephony.sysprop.TelephonyProperties$$ExternalSyntheticLambda5
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return TelephonyProperties.tryParseString((String) obj);
            }
        }, SystemProperties.get("gsm.operator.orig.alpha"));
    }

    public static void operator_orig_alpha(List<String> list) {
        SystemProperties.set("gsm.operator.orig.alpha", list == null ? "" : formatList(list));
    }

    public static List<String> icc_operator_orig_alpha() {
        return tryParseList(new Function() { // from class: android.internal.telephony.sysprop.TelephonyProperties$$ExternalSyntheticLambda17
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return TelephonyProperties.tryParseString((String) obj);
            }
        }, SystemProperties.get("gsm.sim.operator.orig.alpha"));
    }

    public static void icc_operator_orig_alpha(List<String> list) {
        SystemProperties.set("gsm.sim.operator.orig.alpha", list == null ? "" : formatList(list));
    }

    public static List<String> gsm_apn_sim_operator_numeric() {
        return tryParseList(new Function() { // from class: android.internal.telephony.sysprop.TelephonyProperties$$ExternalSyntheticLambda6
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return TelephonyProperties.tryParseString((String) obj);
            }
        }, SystemProperties.get("gsm.apn.sim.operator.numeric"));
    }

    public static void gsm_apn_sim_operator_numeric(List<String> list) {
        SystemProperties.set("gsm.apn.sim.operator.numeric", list == null ? "" : formatList(list));
    }

    public static List<String> icc_operator_iso_country() {
        return tryParseList(new Function() { // from class: android.internal.telephony.sysprop.TelephonyProperties$$ExternalSyntheticLambda8
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return TelephonyProperties.tryParseString((String) obj);
            }
        }, SystemProperties.get("gsm.sim.operator.iso-country"));
    }

    public static void icc_operator_iso_country(List<String> list) {
        SystemProperties.set("gsm.sim.operator.iso-country", list == null ? "" : formatList(list));
    }

    public static List<String> data_network_type() {
        return tryParseList(new Function() { // from class: android.internal.telephony.sysprop.TelephonyProperties$$ExternalSyntheticLambda10
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return TelephonyProperties.tryParseString((String) obj);
            }
        }, SystemProperties.get("gsm.network.type"));
    }

    public static void data_network_type(List<String> list) {
        SystemProperties.set("gsm.network.type", list == null ? "" : formatList(list));
    }

    public static Optional<Boolean> in_ecm_mode() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ril.cdma.inecmmode")));
    }

    public static void in_ecm_mode(Boolean bool) {
        SystemProperties.set("ril.cdma.inecmmode", bool == null ? "" : bool.toString());
    }

    public static Optional<Long> ecm_exit_timer() {
        return Optional.ofNullable(tryParseLong(SystemProperties.get("ro.cdma.ecmexittimer")));
    }

    public static Optional<String> operator_idp_string() {
        return Optional.ofNullable(tryParseString(SystemProperties.get("gsm.operator.idpstring")));
    }

    public static void operator_idp_string(String str) {
        SystemProperties.set("gsm.operator.idpstring", str == null ? "" : str.toString());
    }

    public static List<String> otasp_num_schema() {
        return tryParseList(new Function() { // from class: android.internal.telephony.sysprop.TelephonyProperties$$ExternalSyntheticLambda14
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return TelephonyProperties.tryParseString((String) obj);
            }
        }, SystemProperties.get("ro.cdma.otaspnumschema"));
    }

    public static Optional<Boolean> disable_call() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.telephony.disable-call")));
    }

    public static Optional<Boolean> ril_sends_multiple_call_ring() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.telephony.call_ring.multiple")));
    }

    public static Optional<Integer> call_ring_delay() {
        return Optional.ofNullable(tryParseInteger(SystemProperties.get("ro.telephony.call_ring.delay")));
    }

    public static Optional<Integer> cdma_msg_id() {
        return Optional.ofNullable(tryParseInteger(SystemProperties.get("persist.radio.cdma.msgid")));
    }

    public static void cdma_msg_id(Integer num) {
        SystemProperties.set("persist.radio.cdma.msgid", num == null ? "" : num.toString());
    }

    public static Optional<Integer> wake_lock_timeout() {
        return Optional.ofNullable(tryParseInteger(SystemProperties.get("ro.ril.wake_lock_timeout")));
    }

    public static Optional<Boolean> reset_on_radio_tech_change() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("persist.radio.reset_on_switch")));
    }

    public static List<Boolean> sms_receive() {
        return tryParseList(new Function() { // from class: android.internal.telephony.sysprop.TelephonyProperties$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return TelephonyProperties.tryParseBoolean((String) obj);
            }
        }, SystemProperties.get("telephony.sms.receive"));
    }

    public static List<Boolean> sms_send() {
        return tryParseList(new Function() { // from class: android.internal.telephony.sysprop.TelephonyProperties$$ExternalSyntheticLambda15
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return TelephonyProperties.tryParseBoolean((String) obj);
            }
        }, SystemProperties.get("telephony.sms.send"));
    }

    public static Optional<Boolean> test_csim() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("persist.radio.test-csim")));
    }

    public static Optional<Boolean> ignore_nitz() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("telephony.test.ignore.nitz")));
    }

    public static Optional<String> multi_sim_config() {
        return Optional.ofNullable(tryParseString(SystemProperties.get("persist.radio.multisim.config")));
    }

    public static void multi_sim_config(String str) {
        SystemProperties.set("persist.radio.multisim.config", str == null ? "" : str.toString());
    }

    public static Optional<Boolean> reboot_on_modem_change() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("persist.radio.reboot_on_modem_change")));
    }

    public static Optional<Integer> videocall_audio_output() {
        return Optional.ofNullable(tryParseInteger(SystemProperties.get("persist.radio.call.audio.output")));
    }

    public static Optional<Boolean> enable_esim_ui_by_default() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("esim.enable_esim_system_ui_by_default")));
    }

    public static List<Integer> default_network() {
        return tryParseList(new Function() { // from class: android.internal.telephony.sysprop.TelephonyProperties$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return TelephonyProperties.tryParseInteger((String) obj);
            }
        }, SystemProperties.get("ro.telephony.default_network"));
    }

    public static Optional<Boolean> data_roaming() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.com.android.dataroaming")));
    }

    public static Optional<Boolean> mobile_data() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.com.android.mobiledata")));
    }

    public static Optional<Integer> wps_info() {
        return Optional.ofNullable(tryParseInteger(SystemProperties.get("wifidirect.wps")));
    }

    public static Optional<Integer> max_active_modems() {
        return Optional.ofNullable(tryParseInteger(SystemProperties.get("telephony.active_modems.max_count")));
    }

    public static Optional<Integer> sim_slots_count() {
        return Optional.ofNullable(tryParseInteger(SystemProperties.get("ro.telephony.sim_slots.count")));
    }

    public static Optional<Integer> multi_sim_voice_capability() {
        return Optional.ofNullable(tryParseInteger(SystemProperties.get("ril.multisim.voice_capability")));
    }

    public static void multi_sim_voice_capability(Integer num) {
        SystemProperties.set("ril.multisim.voice_capability", num == null ? "" : num.toString());
    }

    public static Optional<Boolean> in_scbm() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ril.inscbm")));
    }

    public static void in_scbm(Boolean bool) {
        SystemProperties.set("ril.inscbm", bool == null ? "" : bool.toString());
    }

    public static Optional<Boolean> dsds_transition_supported() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ril.multisim.dsds_transition_supported")));
    }

    public static void dsds_transition_supported(Boolean bool) {
        SystemProperties.set("ril.multisim.dsds_transition_supported", bool == null ? "" : bool.toString());
    }
}
