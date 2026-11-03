package com.xiaomi.modem;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import com.xiaomi.telephony.XmTelephonyManagerInner;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import vendor.xiaomi.hardware.misys.V1_0.IResultValue;

/* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
/* JADX INFO: loaded from: classes.dex */
public class ModemUtils {
    public static final String BC_ACTION_SHUTDOWN = "android.intent.action.ACTION_SHUTDOWN";
    public static final String BUNDLE_RETURN_VAL = "RETURN_VAL";
    private static final int BYTE_SIZE = 1;
    public static final int COMMON_SWITCH_TYPE_DEF = 2;
    public static final int COMMON_SWITCH_TYPE_OFF = 0;
    public static final int COMMON_SWITCH_TYPE_ON = 1;
    public static final int DATA_TYPE_DECIMALISM = 0;
    public static final int DATA_TYPE_HEX = 1;
    public static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DEVICE_ESIM_SUPPORT_LIST = "diting,nuwa,fuxi,ziyi";
    public static final int DMS_OP_MODE_FACTORY_TEST_MODE_V01 = 2;
    public static final int DMS_OP_MODE_LOW_POWER_V01 = 1;
    public static final int DMS_OP_MODE_MODE_ONLY_LOW_POWER_V01 = 7;
    public static final int DMS_OP_MODE_NET_TEST_GW_V01 = 8;
    public static final int DMS_OP_MODE_NOT_INIT_V01 = -1;
    public static final int DMS_OP_MODE_OFFLINE_V01 = 3;
    public static final int DMS_OP_MODE_ONLINE_V01 = 0;
    public static final int DMS_OP_MODE_PERSISTENT_LOW_POWER_V01 = 6;
    public static final int DMS_OP_MODE_RESETTING_V01 = 4;
    public static final int DMS_OP_MODE_SHUTTING_DOWN_V01 = 5;
    private static final int DUMP_STACKTRACE_DEFAULT_MAX_COUNT = 20;
    public static final String EFS_PATH_ASDIV_73841 = "/nv/item_files/mcs/trm/trm_ant_port_override_mode";
    public static final String EFS_PATH_ASDIV_73971 = "/nv/item_files/mcs/trm/ant_div_switch_master";
    public static final byte EFS_VALUE_ASDIV_73841_CONFIG0 = 0;
    public static final byte EFS_VALUE_ASDIV_73841_DYNAMIC_SWITCH = -1;
    public static final int IMEI_PROTECT_CLOSE_VALUE = 305419896;
    private static final int INT_SIZE = 4;
    private static final String LOG_TAG = "ModemUtils";
    private static final int LONG_SIZE = 8;
    private static final int MAX_PHONE_COUNT_DUAL_SIM = 2;
    private static final int MAX_PHONE_COUNT_SINGLE_SIM = 1;
    private static final int MAX_PHONE_COUNT_TRI_SIM = 3;
    public static final int MODEM_STATS_DATA_INVALID = 65535;
    public static final int MODEM_STATS_DATA_INVALID_1 = -1;
    public static final float MODEM_STATS_DATA_INVALID_2 = -1.0f;
    public static final int MODEM_STATS_DATA_INVALID_3 = 255;
    public static final float MODEM_STATS_DATA_INVALID_4 = 255.0f;
    public static final int MODEM_STATS_DATA_INVALID_5 = -1;
    public static final long MODEM_STATS_DATA_INVALID_6 = Long.MAX_VALUE;
    public static final long MTB_DIAG_DEADLINE_INNER_DAY_NUM = 7;
    public static final long MTB_DIAG_DEADLINE_LIMIT = -1;
    public static final long MTB_DIAG_DEADLINE_NO_LIMIT = 0;
    public static final int NV_ID_453 = 453;
    public static final int NV_ID_6858 = 6858;
    public static final int NV_ID_6859 = 6859;
    public static final int OEM_NV_BYTE_LEN = 124;
    public static final int OEM_NV_BYTE_NO_SMS_AUTO_REG = 20;
    public static final int OEM_PROJECT_FLAG_INIT = 0;
    public static final int OEM_PROJECT_FLAG_NO = 2;
    public static final int OEM_PROJECT_FLAG_YES = 1;
    public static final long ONE_DAY_MS = 86400000;
    public static final String PROPERTY_DEVICE_INFO_HW = "ro.boot.hwc";
    public static final String PROPERTY_DEVICE_INFO_HW_LEVEL = "ro.boot.hwlevel";
    public static final String PROPERTY_DEVICE_INFO_HW_LEVEL_MP = "MP";
    public static final String PROPERTY_DEVICE_INFO_HW_LEVEL_NULL = "NULL";
    public static final String PROPERTY_DEVICE_INFO_HW_NULL = "NULL";
    public static final String PROPERTY_DEVICE_INFO_SW = "ro.product.mod_device";
    public static final String PROPERTY_DEVICE_INFO_SW_NULL = "NULL";
    public static final String PROPERTY_DEVICE_NAME = "ro.product.device";
    public static final String PROPERTY_DEVICE_NAME_VENDOR = "ro.product.vendor.device";
    private static final String PROPERTY_MULTI_SIM_CONFIG = "persist.radio.multisim.config";
    public static final String PROP_BOOL_VALUE_OFF = "false";
    public static final String PROP_BOOL_VALUE_ON = "true";
    public static final String PROP_BUILD_PROFILE_FACTORY = "factory";
    public static final String PROP_BUILD_PROFILE_MIUI = "miui";
    public static final String PROP_BUILD_PROFILE_NATIVE = "native";
    public static final String PROP_BUILD_PROFILE_NULL = "NULL";
    public static final String PROP_BUILD_PROFILE_VENDOR = "ro.vendor.radio.build_profile";
    public static final String PROP_BUILD_TAGS = "ro.build.tags";
    public static final String PROP_BUILD_TAGS_NULL = "NULL";
    public static final String PROP_BUILD_TAGS_RELE = "release-keys";
    public static final String PROP_BUILD_TAGS_TEST = "test-keys";
    public static final String PROP_BUILD_TYPE = "ro.build.type";
    public static final String PROP_BUILD_TYPE_USER = "user";
    public static final String PROP_BUILD_VERSION = "ro.build.version.incremental";
    public static final String PROP_BUILD_VERSION_MI_OS = "ro.mi.os.version.incremental";
    public static final String PROP_BUILD_VERSION_NULL = "";
    public static final String PROP_CERT_TYPE = "ro.cust.test";
    public static final String PROP_CERT_TYPE_CM = "cm";
    public static final String PROP_CERT_TYPE_CT = "ct";
    public static final String PROP_CERT_TYPE_CU = "cu";
    public static final String PROP_CERT_TYPE_NONE = "none";
    public static final String PROP_DEVICE_MOD_NAME_SUFFIX_DEV = "_dev";
    public static final String PROP_DEVICE_MOD_NAME_SUFFIX_FACTORY = "_factory";
    public static final String PROP_DEVICE_MOD_NAME_SUFFIX_NATIVE = "_native";
    public static final String PROP_DEVICE_MOD_NAME_SUFFIX_NULL = "NULL";
    public static final String PROP_DEVICE_MOD_NAME_SUFFIX_PRE = "_pre";
    public static final String PROP_DEVICE_MOD_NAME_SUFFIX_STABLE = "_stable";
    public static final String PROP_DEVICE_NAME_K9 = "renoir";
    public static final String PROP_DEVICE_NAME_L10 = "ingres";
    public static final String PROP_DEVICE_NAME_L11R = "munch";
    public static final String PROP_DEVICE_NAME_L2 = "zeus";
    public static final String PROP_DEVICE_NAME_NULL = "NULL";
    public static final String PROP_DIAG_STATE_CLOSE = "CLOSE";
    public static final String PROP_DIAG_STATE_DEFAULT = "persist.radio.diag.default";
    public static final String PROP_DIAG_STATE_IDLE = "IDLE";
    public static final String PROP_DIAG_STATE_OPEN = "OPEN";
    public static final String PROP_DISPLAY_MIPI_CONTROL = "persist.vendor.radio.display_mipi";
    public static final String PROP_DISPLAY_MIPI_CONTROL_INVALID = "2";
    public static final String PROP_DISPLAY_MIPI_CONTROL_OFF = "0";
    public static final String PROP_DISPLAY_MIPI_CONTROL_ON = "1";
    public static final String PROP_HOOK_TEMP_PRINT_SWITCH = "persist.vendor.radio.hook_tmplog";
    public static final String PROP_HYDRA_AP_PRINT = "persist.vendor.radio.print_stats";
    public static final String PROP_IMS_DEF_STATE = "persist.vendor.radio.ims_state_def";
    public static final String PROP_IMS_DEF_VALUE_DEF = "";
    public static final String PROP_IMS_DEF_VALUE_OFF = "0";
    public static final String PROP_IMS_DEF_VALUE_ON = "1";
    public static final String PROP_MIRIL_PRINT = "persist.vendor.radio.adb_log_mi";
    public static final String PROP_MIRIL_PRINT_OFF = "0";
    public static final String PROP_MIRIL_PRINT_ON = "65535";
    public static final String PROP_MIUI_BUILD_REGION = "ro.miui.build.region";
    public static final String PROP_MIUI_BUILD_REGION_CN = "cn";
    public static final String PROP_MIUI_BUILD_REGION_DEF = "none";
    public static final String PROP_MIUI_BUILD_REGION_VENDOR = "ro.vendor.miui.build.region";
    public static final String PROP_MI_CHECK_URL_PRINT = "persist.radio.url_check_print";
    public static final String PROP_MI_CHECK_URL_PRINT_OFF = "0";
    public static final String PROP_MI_CHECK_URL_PRINT_ON = "1";
    public static final String PROP_MODEM_AP_PRINT_OFF = "2";
    public static final String PROP_MODEM_AP_PRINT_ON = "1";
    public static final String PROP_MODEM_MBN_FEATURE_FLAG = "persist.vendor.radio.mbn_feature";
    public static final int PROP_MODEM_MBN_FEATURE_FLAG_NULL = 0;
    public static final int PROP_MODEM_MBN_SELFREPARING_OFF = 65503;
    public static final int PROP_MODEM_MBN_SELFREPARING_ON = 32;
    public static final int PROP_MODEM_MBN_SIM_HW_AUTO_SELECT_DISABLE = 8;
    public static final int PROP_MODEM_MBN_UPDATE_FLAG_HW = 1;
    public static final int PROP_MODEM_MBN_UPDATE_FLAG_MBN_LIST_SIZE = 4;
    public static final int PROP_MODEM_MBN_UPDATE_FLAG_SW = 2;
    public static final int PROP_MODEM_MBN_UPDATE_RFS_PATH_DISCOVER = 16;
    public static final String PROP_MODEM_STATS_MASK_KEEP_DIAG = "2";
    public static final String PROP_MODEM_STATS_MASK_KEEP_EVENT = "1";
    public static final String PROP_MODEM_STATS_MASK_KEEP_OFF = "0";
    public static final String PROP_MODEM_STATS_MASK_KEEP_ON = "3";
    public static final String PROP_MODEM_STATS_MASK_KEEP_SWITCH = "persist.vendor.radio.stats_mask_keep";
    public static final String PROP_MODEM_STATS_SWITCH = "persist.vendor.radio.stats_switch";
    public static final String PROP_MODEM_STATS_SWITCH_INVALID = "2";
    public static final String PROP_MODEM_STATS_SWITCH_OFF = "0";
    public static final String PROP_MODEM_STATS_SWITCH_ON = "1";
    public static final String PROP_MTB_DIAG_DEADLINE = "persist.radio.mtb_diag_deadline";
    public static final String PROP_MTB_DIAG_DEADLINE_DEF = "0";
    public static final String PROP_NAME_FACTORY_BUILD = "ro.boot.factorybuild";
    public static final String PROP_NAME_FACTORY_BUILD_NULL = "NULL";
    public static final String PROP_NR_ONLY_FLAG = "persist.vendor.radio.nr_only";
    public static final String PROP_NR_ONLY_FLAG_OFF = "off";
    public static final String PROP_NR_ONLY_FLAG_ON = "on";
    public static final String PROP_NTN_LOG_LEVEL = "persist.odm.radio.ntn.log.level";
    public static final String PROP_NTN_LOG_LEVEL_DEBUG = "2";
    public static final String PROP_NTN_LOG_LEVEL_DEFAULT = "default";
    public static final String PROP_NTN_LOG_LEVEL_ERROR = "4";
    public static final String PROP_NTN_LOG_LEVEL_INFO = "1";
    public static final String PROP_NTN_LOG_LEVEL_NULL = "";
    public static final String PROP_NTN_LOG_LEVEL_VERBOSE = "0";
    public static final String PROP_NTN_LOG_LEVEL_WARNING = "3";
    public static final String PROP_PRINT_IDLE = "0";
    public static final String PROP_PRINT_OFF = "2";
    public static final String PROP_PRINT_ON = "1";
    public static final String PROP_PRINT_STATS = "persist.vendor.radio.print_stats";
    public static final String PROP_QCRIL_PRINT = "persist.vendor.radio.adb_log_on";
    public static final String PROP_QCRIL_PRINT_OFF = "0";
    public static final String PROP_QCRIL_PRINT_ON = "1";
    public static final String PROP_RADIO_BUILD_REGION_VENDOR = "ro.vendor.radio.build_region";
    public static final String PROP_RADIO_HYPER_AIDL_SAR_SWITCH = "persist.vendor.radio.hyper_aidl_sar";
    public static final String PROP_RADIO_HYPER_AIDL_SWITCH = "persist.vendor.radio.hyper_aidl";
    private static final String PROP_RADIO_MANUFACTURER_ID = "ro.vendor.radio.manufacturer_id";
    public static final String PROP_RADIO_OPERATE_MODE = "persist.radio.operating_mode";
    public static final String PROP_RADIO_XIAOMI_BP_CHIP_ID = "ro.vendor.radio.bp_chip_id";
    public static final String PROP_RADIO_XIAOMI_HAL_CHIP_ID = "ro.vendor.radio.hal_chip_id";
    private static final String PROP_RADIO_XIAOMI_MANUFACTURER_ID_HUAQIN = "4";
    private static final String PROP_RADIO_XIAOMI_MANUFACTURER_ID_LONGQI = "3";
    private static final String PROP_RADIO_XIAOMI_MANUFACTURER_ID_NULL = "0";
    private static final String PROP_RADIO_XIAOMI_MANUFACTURER_ID_WENTAI = "2";
    private static final String PROP_RADIO_XIAOMI_MANUFACTURER_ID_XIAOMI = "1";
    private static final String PROP_RADIO_XIAOMI_MANUFACTURER_ID_ZHONGKE = "5";
    public static final String PROP_RILD_BC_PRINT_SWITCH = "persist.vendor.radio.print_bc";
    public static final String PROP_RILD_HOOK_PRINT_SWITCH = "persist.vendor.radio.print_hook";
    public static final String PROP_RILD_PROP_PRINT_SWITCH = "persist.vendor.radio.print_prop";
    public static final String PROP_ROOT_TYPE = "ro.debuggable";
    public static final String PROP_ROOT_TYPE_NO = "0";
    public static final String PROP_ROOT_TYPE_NULL = "NULL";
    public static final String PROP_ROOT_TYPE_YES = "1";
    public static final String PROP_SAR_LOG_SWITCH = "persist.vendor.radio.dynamic_sar_log";
    public static final String PROP_SAR_LOG_SWITCH_CLOSE = "1";
    public static final String PROP_SAR_LOG_SWITCH_OPEN = "3";
    public static final String PROP_SAR_SWITCH_MAIN = "persist.vendor.radio.dynamic_sar";
    public static final String PROP_SAR_SWITCH_MAIN_CLOSE = "0";
    public static final String PROP_SAR_SWITCH_MAIN_DEF = "1";
    public static final String PROP_SAR_SWITCH_MAIN_OPEN = "2";
    public static final String PROP_SIM_STATE = "gsm.sim.state";
    public static final String PROP_SMS_AUTO_REG = "persist.radio.autosms";
    public static final String PROP_SMS_AUTO_REG_OFF = "disabled";
    public static final String PROP_SMS_AUTO_REG_ON = "enabled";
    public static final String PROP_SSR_LEVEL = "persist.vendor.ssr.restart_level";
    public static final String PROP_SSR_LEVEL_ALL_DISABLE = "ALL_DISABLE";
    public static final String PROP_SSR_LEVEL_ALL_ENABLE = "ALL_ENABLE";
    public static final String PROP_SSR_LEVEL_BAK = "persist.vendor.ssr.restart_level_bak";
    public static final String PROP_SSR_LEVEL_NULL = "";
    public static final String PROP_USB_CONFIG_HIGH28 = "persist.vendor.usb.config";
    public static final String PROP_USB_CONFIG_LOW28 = "persist.sys.usb.config";
    public static final String PROP_USB_CONFIG_NONROOT = "sys.usb.config";
    public static final String PROP_USB_CONFIG_NULL = "null";
    public static final String PROP_WENKONG_FLAG = "persist.vendor.radio.wenkong";
    public static final String PROP_WENKONG_FLAG_OFF = "off";
    public static final String PROP_WENKONG_FLAG_ON = "on";
    public static final String PROP_XIAOMI_SSR_WAIT_S = "persist.radio.ssr_wait_s";
    public static final String PROP_XRING_HAL_FLAG = "persist.vendor.radio.xring";
    public static final int RET_DO = 4;
    public static final int RET_ERROR = 2;
    public static final int RET_FALSE = 0;
    public static final int RET_NOTHING = 3;
    public static final int RET_TRUE = 1;
    public static final int SIGNED_DATA = 0;
    public static final String SIM_UNKNOWN = "unknown";
    public static final String STR_COMMON_SWITCH_TYPE_DEF = "";
    public static final String STR_COMMON_SWITCH_TYPE_OFF = "OFF";
    public static final String STR_COMMON_SWITCH_TYPE_ON = "ON";
    public static final int SUB_0 = 0;
    public static final int SUB_1 = 1;
    public static final int SUB_ALL = 2;
    public static final int SUB_INVALID = -1;
    public static final int SUB_MASK_0 = 1;
    public static final int SUB_MASK_0_1 = 3;
    public static final int SUB_MASK_1 = 2;
    public static final int SUB_MASK_ALL = 3;
    public static final int UNSIGNED_DATA = 1;
    public static final int XIAOMI_MANUFACTURER_ID_HUAQIN = 4;
    public static final int XIAOMI_MANUFACTURER_ID_LONGQI = 3;
    public static final int XIAOMI_MANUFACTURER_ID_NULL = 0;
    public static final int XIAOMI_MANUFACTURER_ID_WENTAI = 2;
    public static final int XIAOMI_MANUFACTURER_ID_XIAOMI = 1;
    public static final int XIAOMI_MANUFACTURER_ID_ZHONGKE = 5;
    public static final int XIAOMI_MODEM_BP_CHIP_ID_INIT = -1;
    public static final int XIAOMI_MODEM_BP_CHIP_ID_INVALID = 0;
    public static final int XIAOMI_MODEM_BP_CHIP_ID_MTK_START = 1001;
    public static final int XIAOMI_MODEM_BP_CHIP_ID_QC_ODM = 100;
    public static final int XIAOMI_MODEM_BP_CHIP_ID_QC_ODM_HQ_DE311 = 101;
    public static final int XIAOMI_MODEM_BP_CHIP_ID_QC_ODM_LQ_CYS10 = 103;
    public static final int XIAOMI_MODEM_BP_CHIP_ID_QC_ODM_LQ_DE311 = 102;
    public static final int XIAOMI_MODEM_BP_CHIP_ID_QC_ODM_LQ_HI434 = 104;
    public static final int XIAOMI_MODEM_BP_CHIP_ID_QC_SM7435 = 6;
    public static final int XIAOMI_MODEM_BP_CHIP_ID_QC_SM7635 = 8;
    public static final int XIAOMI_MODEM_BP_CHIP_ID_QC_SM8350 = 1;
    public static final int XIAOMI_MODEM_BP_CHIP_ID_QC_SM8450 = 2;
    public static final int XIAOMI_MODEM_BP_CHIP_ID_QC_SM8450_DE20 = 3;
    public static final int XIAOMI_MODEM_BP_CHIP_ID_QC_SM8550 = 4;
    public static final int XIAOMI_MODEM_BP_CHIP_ID_QC_SM8635 = 7;
    public static final int XIAOMI_MODEM_BP_CHIP_ID_QC_SM8650 = 5;
    public static final int XIAOMI_MODEM_BP_CHIP_ID_QC_SM8750 = 9;
    public static final int XIAOMI_MODEM_BP_CHIP_ID_QC_START = 1;
    public static final int XIAOMI_MODEM_HAL_CHIP_ID_HQ_DE311 = 101;
    public static final int XIAOMI_MODEM_HAL_CHIP_ID_INIT = -1;
    public static final int XIAOMI_MODEM_HAL_CHIP_ID_LQ_CYS10 = 103;
    public static final int XIAOMI_MODEM_HAL_CHIP_ID_LQ_DE311 = 102;
    public static final int XIAOMI_MODEM_HAL_CHIP_ID_LQ_HI434 = 104;
    public static final int XIAOMI_MODEM_HAL_CHIP_ID_MAX = 2000;
    public static final int XIAOMI_MODEM_HAL_CHIP_ID_MTK_MT6899 = 1001;
    public static final int XIAOMI_MODEM_HAL_CHIP_ID_MTK_ODM = 1100;
    public static final int XIAOMI_MODEM_HAL_CHIP_ID_MTK_ODM_LQ_123 = 1101;
    public static final int XIAOMI_MODEM_HAL_CHIP_ID_MTK_START = 1001;
    public static final int XIAOMI_MODEM_HAL_CHIP_ID_NULL = 0;
    public static final int XIAOMI_MODEM_HAL_CHIP_ID_QC_ODM = 100;
    public static final int XIAOMI_MODEM_HAL_CHIP_ID_SM8350 = 1;
    public static final int XIAOMI_MODEM_HAL_CHIP_ID_SM8450 = 2;
    public static final int XIAOMI_MODEM_HAL_CHIP_ID_SM8550 = 3;
    public static final int XIAOMI_MODEM_HAL_CHIP_ID_SM8650 = 4;
    public static final int XMRADIO_CAUSE_ABORTED = 65;
    public static final int XMRADIO_CAUSE_CANCELLED = 7;
    public static final int XMRADIO_CAUSE_DEVICE_IN_USE = 64;
    public static final int XMRADIO_CAUSE_DIAL_MODIFIED_TO_DIAL = 20;
    public static final int XMRADIO_CAUSE_DIAL_MODIFIED_TO_SS = 19;
    public static final int XMRADIO_CAUSE_DIAL_MODIFIED_TO_USSD = 18;
    public static final int XMRADIO_CAUSE_EMPTY_RECORD = 55;
    public static final int XMRADIO_CAUSE_ENCODING_ERR = 57;
    public static final int XMRADIO_CAUSE_FDN_CHECK_FAILURE = 14;
    public static final int XMRADIO_CAUSE_GENERIC_FAILURE = 2;
    public static final int XMRADIO_CAUSE_ILLEGAL_SIM_OR_ME = 15;
    public static final int XMRADIO_CAUSE_INIT = -168;
    public static final int XMRADIO_CAUSE_INTERNAL_ERR = 38;
    public static final int XMRADIO_CAUSE_INVALID_ARGUMENTS = 44;
    public static final int XMRADIO_CAUSE_INVALID_CALL_ID = 47;
    public static final int XMRADIO_CAUSE_INVALID_MODEM_STATE = 46;
    public static final int XMRADIO_CAUSE_INVALID_RESPONSE = 66;
    public static final int XMRADIO_CAUSE_INVALID_SIM_STATE = 45;
    public static final int XMRADIO_CAUSE_INVALID_SMSC_ADDRESS = 58;
    public static final int XMRADIO_CAUSE_INVALID_SMS_FORMAT = 56;
    public static final int XMRADIO_CAUSE_INVALID_STATE = 41;
    public static final int XMRADIO_CAUSE_LCE_NOT_SUPPORTED = 36;
    public static final int XMRADIO_CAUSE_MISSING_RESOURCE = 16;
    public static final int XMRADIO_CAUSE_MODEM_ERR = 40;
    public static final int XMRADIO_CAUSE_MODE_NOT_SUPPORTED = 13;
    public static final int XMRADIO_CAUSE_NETWORK_ERR = 49;
    public static final int XMRADIO_CAUSE_NETWORK_NOT_READY = 60;
    public static final int XMRADIO_CAUSE_NETWORK_REJECT = 53;
    public static final int XMRADIO_CAUSE_NONE = 0;
    public static final int XMRADIO_CAUSE_NOT_CONFIG = -169;
    public static final int XMRADIO_CAUSE_NOT_PROVISIONED = 61;
    public static final int XMRADIO_CAUSE_NO_MEMORY = 37;
    public static final int XMRADIO_CAUSE_NO_NETWORK_FOUND = 63;
    public static final int XMRADIO_CAUSE_NO_RESOURCES = 42;
    public static final int XMRADIO_CAUSE_NO_SMS_TO_ACK = 48;
    public static final int XMRADIO_CAUSE_NO_SUBSCRIPTION = 62;
    public static final int XMRADIO_CAUSE_NO_SUCH_ELEMENT = 17;
    public static final int XMRADIO_CAUSE_NO_SUCH_ENTRY = 59;
    public static final int XMRADIO_CAUSE_OPERATION_NOT_ALLOWED = 54;
    public static final int XMRADIO_CAUSE_OP_NOT_ALLOWED_BEFORE_REG_TO_NW = 9;
    public static final int XMRADIO_CAUSE_OP_NOT_ALLOWED_DURING_VOICE_CALL = 8;
    public static final int XMRADIO_CAUSE_PASSWORD_INCORRECT = 3;
    public static final int XMRADIO_CAUSE_RADIO_NOT_AVAILABLE = 1;
    public static final int XMRADIO_CAUSE_REQUEST_NOT_SUPPORTED = 6;
    public static final int XMRADIO_CAUSE_REQUEST_RATE_LIMITED = 50;
    public static final int XMRADIO_CAUSE_SIM_ABSENT = 11;
    public static final int XMRADIO_CAUSE_SIM_BUSY = 51;
    public static final int XMRADIO_CAUSE_SIM_ERR = 43;
    public static final int XMRADIO_CAUSE_SIM_FULL = 52;
    public static final int XMRADIO_CAUSE_SIM_PIN2 = 4;
    public static final int XMRADIO_CAUSE_SIM_PUK2 = 5;
    public static final int XMRADIO_CAUSE_SMS_SEND_FAIL_RETRY = 10;
    public static final int XMRADIO_CAUSE_SS_MODIFIED_TO_DIAL = 24;
    public static final int XMRADIO_CAUSE_SS_MODIFIED_TO_SS = 27;
    public static final int XMRADIO_CAUSE_SS_MODIFIED_TO_USSD = 25;
    public static final int XMRADIO_CAUSE_SUBSCRIPTION_NOT_AVAILABLE = 12;
    public static final int XMRADIO_CAUSE_SUBSCRIPTION_NOT_SUPPORTED = 26;
    public static final int XMRADIO_CAUSE_SUB_INVALID = -170;
    public static final int XMRADIO_CAUSE_SYSTEM_ERR = 39;
    public static final int XMRADIO_CAUSE_USSD_MODIFIED_TO_DIAL = 21;
    public static final int XMRADIO_CAUSE_USSD_MODIFIED_TO_SS = 22;
    public static final int XMRADIO_CAUSE_USSD_MODIFIED_TO_USSD = 23;
    private static final int XMRADIO_COM_REQ_MSG_ID_CMN_ACCELERATE_SEARCH_NETWORK = 11;
    private static final int XMRADIO_COM_REQ_MSG_ID_CMN_DEACTIVE_SCELL = 12;
    private static final int XMRADIO_COM_REQ_MSG_ID_CMN_HAL_PROP_GET = 1;
    private static final int XMRADIO_COM_REQ_MSG_ID_CMN_HAL_PROP_SET = 2;
    private static final int XMRADIO_COM_REQ_MSG_ID_CMN_MAX = 10000;
    private static final int XMRADIO_COM_REQ_MSG_ID_CMN_MIN = 1;
    private static final int XMRADIO_COM_REQ_MSG_ID_CMN_VENUE_MODE_SET = 10;
    protected static Context mContext = null;
    protected static OemHookAgent mHookAgent = null;
    protected static Handler mHandler = null;
    private static boolean mTmpLogPrintFlag = false;
    private static int mNvEfsDataPrintMaxNum = 20;
    public static final byte[] EFS_VALUE_ASDIV_73971_OPEN = {2, 1, 1, 1, 1, 1, 1, 2, 1, 1, 0, 0, 0, 0};
    public static final byte[] EFS_VALUE_ASDIV_73971_CLOSE = {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0};
    protected static String mBuildTags = "NULL";
    protected static String mBuildVersion = "";
    protected static String mRootType = "NULL";
    protected static String mCertType = "none";
    protected static String mBuildProfile = "NULL";
    protected static String mBuildRegion = "none";
    public static final int NETWORK_TYPE_NR_CA = 30;
    protected static String mFactoryBuild = "NULL";
    protected static String mDeviceHwInfo = "NULL";
    protected static String mDeviceModName = "NULL";
    protected static String mDeviceHwLevel = "NULL";
    protected static int mHalChipId = -1;
    public static boolean mXringHalFlag = false;
    public static boolean mIsHalDecoupled = false;
    protected static int mBpChipId = -1;
    protected static int mManufacturerId = 0;
    protected static int mOemProjectFlag = 0;
    protected static int mSdkVersion = Build.VERSION.SDK_INT;
    protected static int mAndroidVersion = SystemProperties.getInt("ro.build.version.release", 0);
    protected static String mDeviceName = "NULL";
    private static String mProcessName = null;
    private static final Object GET_CUR_PROCESS_NAME_LOCK = new Object();
    private static int mProcessPid = -1;
    private static final HashMap<String, LinkedList<String>> sDumpStacktraceRingMap = new HashMap<>();
    private static final HashMap<String, Object> sDumpStacktraceLockMap = new HashMap<>();

    public static void onInitContext(Context cnt) {
        mContext = cnt;
        log("onInitContext, mContext = " + mContext);
    }

    public static void onInitHook(OemHookAgent hook) {
        log("onInitHook, hook = " + hook);
        mHookAgent = hook;
    }

    public static void onInitHandler(Handler hdl) {
        log("onInitHandler, hdl = " + hdl);
        mHandler = hdl;
    }

    public static byte[] bufferToBytes(ByteBuffer buffer) {
        if (buffer == null) {
            log("buffer is null, bufferToBytes do nothing");
            return null;
        }
        buffer.flip();
        int len = buffer.remaining();
        log("bufferToBytes, len = " + len);
        byte[] bytes1 = new byte[len];
        buffer.get(bytes1);
        return bytes1;
    }

    public static ByteBuffer bytesToBuffer(byte[] bytes) {
        if (bytes == null) {
            log("bytes is null, bytesToBuffer do nothing");
            return null;
        }
        ByteBuffer byteBuf = ByteBuffer.wrap(bytes);
        byteBuf.order(ByteOrder.nativeOrder());
        return byteBuf;
    }

    public static String exeAdbCmd(String command) {
        log("exeAdbCmd command: " + command);
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while (true) {
                int read = reader.read(buffer);
                if (read > 0) {
                    output.append(buffer, 0, read);
                } else {
                    reader.close();
                    process.waitFor();
                    String outputStr = output.toString();
                    log("outputStr: " + outputStr);
                    return outputStr;
                }
            }
        } catch (IOException e) {
            String strLog = "RuntimeException: " + e;
            log(strLog);
            e.printStackTrace();
            return "";
        } catch (InterruptedException e2) {
            String strLog2 = "InterruptedException: " + e2;
            log(strLog2);
            e2.printStackTrace();
            return "";
        }
    }

    public static Process rootExecProgram(String args, boolean isWaitFor) {
        try {
            Process process = Runtime.getRuntime().exec("/system/xbin/su");
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            String cmd = "" + args;
            log("rootExecProgram command: " + cmd);
            dataOutputStream.writeBytes(cmd + "\n");
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            if (isWaitFor) {
                process.waitFor();
                return null;
            }
            log("process: " + process);
            return process;
        } catch (IOException e) {
            String strLog = "RuntimeException: " + e;
            log(strLog);
            e.printStackTrace();
            return null;
        } catch (InterruptedException e2) {
            String strLog2 = "InterruptedException: " + e2;
            log(strLog2);
            e2.printStackTrace();
            return null;
        }
    }

    public ByteBuffer getByteBufferFromAR(AsyncResult ar) {
        if (ar == null) {
            log("getByteBufferFromAR, ar is null");
            return null;
        }
        if (ar.exception == null) {
            log("getByteBufferFromAR, ar.exception" + ar.exception);
            return null;
        }
        if (ar.result == null) {
            log("getByteBufferFromAR, Null Response");
            return null;
        }
        byte[] response = (byte[]) ar.result;
        ByteBuffer byteBuf = ByteBuffer.wrap(response);
        byteBuf.order(ByteOrder.nativeOrder());
        return byteBuf;
    }

    public static String trimZero(String str) {
        if (str == null) {
            log("trimZero, str is null, will set empty sting");
            return "";
        }
        byte[] strEdit = str.getBytes();
        int len = str.length();
        if (len <= 0) {
            log("trimZero, len <= 0, will set empty sting");
            return "";
        }
        int i = 0;
        for (int j = 0; j < len; j++) {
            if (strEdit[j] != 0) {
                i++;
            }
        }
        if (i <= 0) {
            log("trimZero, all zero, will set empty sting");
            return "";
        }
        byte[] newByteStr = new byte[i];
        int i2 = 0;
        for (int j2 = 0; j2 < len; j2++) {
            if (strEdit[j2] != 0) {
                newByteStr[i2] = strEdit[j2];
                i2++;
            }
        }
        ByteBuffer strBuf = ByteBuffer.wrap(newByteStr);
        strBuf.order(ByteOrder.nativeOrder());
        String strNew = byteBufferToString(strBuf);
        return strNew;
    }

    public static String byteBufferToString(ByteBuffer buffer) {
        try {
            Charset charset = Charset.forName("UTF-8");
            CharsetDecoder decoder = charset.newDecoder();
            CharBuffer charBuffer = decoder.decode(buffer);
            buffer.flip();
            return charBuffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            log("byteBufferToString Exception: " + ex);
            return null;
        }
    }

    public static String bytesToString(byte[] bytes) {
        if (bytes == null) {
            log("bytesToString, bytes is null");
            return null;
        }
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        buf.order(ByteOrder.nativeOrder());
        return byteBufferToString(buf);
    }

    public static boolean putByteToBuffer(ByteBuffer buf, byte[] value, int len) {
        log("putByteToBuffer, len: " + len);
        if (value == null) {
            log("putByteToBuffer, value is null");
            return false;
        }
        if (buf == null) {
            log("putByteToBuffer, buf is null");
            return false;
        }
        if (len <= 0) {
            log("len can not <= 0");
            return false;
        }
        for (int i = 0; i < len; i++) {
            buf.put(value[i]);
        }
        for (int i2 = 0; i2 < mNvEfsDataPrintMaxNum && i2 < len; i2++) {
            log("value[" + i2 + "] = " + ((int) value[i2]));
        }
        return true;
    }

    public static void showToast(Context context, String msg) {
        try {
            if (context != null && msg != null) {
                Toast.makeText(context, msg, 0).show();
                log("Success to show toast: " + msg);
            } else {
                log("context is null, fail to show toast: " + msg);
            }
        } catch (Exception e) {
            log("Fail to show toast: " + msg);
            log("showToast, Exception = " + e);
            e.printStackTrace();
        }
    }

    public static String getStringFromResources(Context context, int resId) {
        if (context != null) {
            String str = context.getResources().getString(resId);
            log("Success to getString: " + str);
            return str;
        }
        log("context is null, fail to getString for resId: " + resId);
        return null;
    }

    public static void rebootSystem(Context context) {
        if (context != null) {
            Intent i = new Intent("android.intent.action.REBOOT");
            i.putExtra("nowait", 1);
            i.putExtra("interval", 1);
            i.putExtra("window", 0);
            context.sendBroadcast(i);
            return;
        }
        log("context is null, fail to rebootSystem");
    }

    public static String getIpStringByApnType(LinkProperties link) {
        String strAddr;
        if (link == null) {
            log("getIpStringByApnType, link is null");
            return null;
        }
        String strAddr2 = null;
        for (LinkAddress address : link.getLinkAddresses()) {
            if (strAddr2 != null) {
                strAddr = strAddr2 + "/";
            } else {
                strAddr = "";
            }
            InetAddress addr = address.getAddress();
            String strTmp = "" + addr;
            if (strTmp.length() > 0) {
                strTmp = strTmp.substring(1);
            }
            strAddr2 = strAddr + strTmp;
        }
        return strAddr2;
    }

    public static String getIpStringByApnType(Phone phone, String apnType) {
        if (phone == null) {
            log("getIpStringByApnType, phone is null");
            return null;
        }
        if (apnType != null) {
            return null;
        }
        log("getIpStringByApnType, apnType is null");
        return null;
    }

    public static String getCurrentTimeString() {
        Calendar c = Calendar.getInstance();
        int year = c.get(1);
        int month = c.get(2);
        int date = c.get(5);
        int hour = c.get(11);
        int minute = c.get(12);
        int second = c.get(13);
        return "" + year + "." + month + "." + date + "." + hour + "." + minute + "." + second;
    }

    public static Date getCurrentTimeWithData(String strFormat) {
        new SimpleDateFormat(strFormat);
        return new Date(System.currentTimeMillis());
    }

    public static Date getCurrentTimeWithData() {
        new SimpleDateFormat(DEFAULT_TIME_FORMAT);
        return new Date(System.currentTimeMillis());
    }

    public static long getCurrentTimeWithLong() {
        return timeDateToLong(getCurrentTimeWithData());
    }

    public static long getCurrentTimeWithLongEx() {
        return timeDateToLong(new Date(System.currentTimeMillis()));
    }

    public static Date timeLongToDate(long timeLong) {
        return new Date(timeLong);
    }

    public static String timeLongToString(long timeLong, String strFormat) {
        Date date = timeLongToDate(timeLong);
        String strTime = timeDateToString(date, strFormat);
        return strTime;
    }

    public static String timeLongToString(long timeLong) {
        return timeLongToString(timeLong, DEFAULT_TIME_FORMAT);
    }

    public static String timeLongToStringEx(long time_ms) {
        Date date = new Date(time_ms);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
        String strTime = format.format(gc.getTime());
        return strTime;
    }

    public static String timeDateToString(Date data, String strFormat) {
        return new SimpleDateFormat(strFormat).format(data);
    }

    public static String timeDateToString(Date data) {
        return timeDateToString(data, DEFAULT_TIME_FORMAT);
    }

    public static long timeDateToLong(Date timeDate) {
        return timeDate.getTime();
    }

    public static long timeStringToLong(String strTime) {
        return timeStringToLong(strTime, DEFAULT_TIME_FORMAT);
    }

    public static long timeStringToLong(String strTime, String strFormat) {
        try {
            Date date = new SimpleDateFormat(strFormat).parse(strTime);
            return timeDateToLong(date);
        } catch (Exception e) {
            log("timeStringToLong, Exception = " + e);
            e.printStackTrace();
            return 0L;
        }
    }

    public static String bytesToHexString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            int b = (bytes[i] >> 4) & 15;
            ret.append("0123456789abcdef".charAt(b));
            int b2 = bytes[i] & 15;
            ret.append("0123456789abcdef".charAt(b2));
        }
        return ret.toString();
    }

    public static String getExternalStorageDirectory() {
        return Environment.getExternalStorageDirectory().toString() + "/";
    }

    public static boolean isUserBuild() {
        String val = SystemProperties.get(PROP_BUILD_TYPE, PROP_BUILD_TYPE_USER);
        boolean ret = false;
        if (TextUtils.equals(val, PROP_BUILD_TYPE_USER)) {
            ret = true;
        }
        log("isUserBuild, ret = " + ret);
        return ret;
    }

    public static String toHtmlFormat(String strSrc) {
        if (strSrc == null) {
            return strSrc;
        }
        String strTgt = strSrc.replace(" ", "&nbsp;");
        return strTgt.replace("\n", "<br />");
    }

    public static String toHtmlFormatColor(String strSrc, String strColor) {
        if (strSrc == null || strColor == null) {
            return strSrc;
        }
        String strTgt = "<font color='" + strColor + "'><b>" + strSrc + "</b></font>";
        return strTgt;
    }

    public static String toHtmlFormatBigAndColor(String strSrc, String strColor) {
        if (strSrc == null || strColor == null) {
            return strSrc;
        }
        String strTgt = "<font color='" + strColor + "'><b><big>" + strSrc + "</big></b></font>";
        return strTgt;
    }

    public static void sleep(int ms) {
        log("Sleep, ms = " + ms);
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            log("Sleep failed");
        }
    }

    public static void removeStringListAt(List<String> strList, int idx) {
        if (strList == null) {
            log("removeStringListAt, strList is null");
            return;
        }
        if (idx < 0 || idx >= strList.size()) {
            log("removeStringListAt, idx is invalid, idx = " + idx + ", size = " + strList.size());
            return;
        }
        int i = 0;
        Iterator<String> iterator = strList.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            if (i == idx) {
                iterator.remove();
                return;
            }
            i++;
        }
    }

    public static String getStringByMapAndList(HashMap<String, String> strMap, List<String> strList, String split) {
        if (split == null) {
            log("getStringByMapAndList, split is null");
            return null;
        }
        if (strMap == null) {
            log("getStringByMapAndList, strMap is null");
            return null;
        }
        if (strList == null) {
            log("getStringByMapAndList, strList is null");
            return null;
        }
        String str = "";
        for (int i = 0; i < strList.size(); i++) {
            if (strMap.get(strList.get(i)) != null) {
                if (!"".equals(str)) {
                    str = str + split;
                }
                str = (str + strList.get(i) + split) + strMap.get(strList.get(i));
            }
        }
        return str;
    }

    public static void getMapAndListByString(String strAll, HashMap<String, String> strMap, List<String> strList, String split) {
        if (split == null) {
            log("getMapAndListByString, split is null");
            return;
        }
        if (strAll == null) {
            log("getMapAndListByString, strAll is null");
            return;
        }
        if (strMap == null) {
            log("getMapAndListByString, strMap is null");
            return;
        }
        if (strList == null) {
            log("getMapAndListByString, strList is null");
            return;
        }
        String[] strArryAll = getStrArrayBySplitWithoutSpace(strAll, split);
        if (strArryAll == null) {
            log("getMapAndListByString, strArryAll is null");
            return;
        }
        if (strArryAll.length % 2 != 0) {
            log("getMapAndListByString, strArryAll.length = " + strArryAll.length + ", should be even number");
            return;
        }
        for (int i = 0; i < strArryAll.length; i += 2) {
            strList.add(strArryAll[i]);
            strMap.put(strArryAll[i], strArryAll[i + 1]);
        }
    }

    public static boolean findStringList(ArrayList<String> list, String str) {
        if (list == null) {
            log("findStringList, list is null");
            return false;
        }
        if (str == null) {
            log("findStringList, str is null");
            return false;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (str.equals(list.get(i))) {
                return true;
            }
        }
        return false;
    }

    public static void addStringListWithNoRepeat(ArrayList<String> list, String str) {
        if (list == null) {
            log("addStringListWithNoRepeat, list is null");
        } else if (str == null) {
            log("addStringListWithNoRepeat, str is null");
        } else if (!findStringList(list, str)) {
            list.add(str);
        }
    }

    public static void addStringListWithNoRepeat(ArrayList<String> list, String[] arrStr) {
        if (list == null) {
            log("addStringListWithNoRepeat, list is null");
            return;
        }
        if (arrStr == null) {
            log("addStringListWithNoRepeat, arrStr is null");
            return;
        }
        for (String str : arrStr) {
            addStringListWithNoRepeat(list, str);
        }
    }

    public static void addStringList(ArrayList<String> ogiStrList, ArrayList<String> newStrList) {
        if (ogiStrList == null || newStrList == null) {
            return;
        }
        for (int i = 0; i < newStrList.size(); i++) {
            ogiStrList.add(newStrList.get(i));
        }
    }

    public static void addStringList(ArrayList<String> ogiStrList, String newStr) {
        if (ogiStrList == null || newStr == null) {
            return;
        }
        ogiStrList.add(newStr);
    }

    public static int countString(String str, String sToFind) {
        if (sToFind == null || str == null) {
            return 0;
        }
        int num = 0;
        while (str.contains(sToFind)) {
            str = str.substring(str.indexOf(sToFind) + sToFind.length());
            num++;
        }
        return num;
    }

    public static boolean isInStringListEx(ArrayList<String> strList, String str) {
        if (strList == null || str == null) {
            return false;
        }
        for (int i = 0; i < strList.size(); i++) {
            if (strList.get(i) != null && strList.get(i).equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInStringList(ArrayList<String> strList, String str) {
        if (strList == null || str == null) {
            return false;
        }
        for (int i = 0; i < strList.size(); i++) {
            if (str.equals(strList.get(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean stringContainStringList(ArrayList<String> strList, String str) {
        if (strList == null || str == null) {
            return false;
        }
        for (int i = 0; i < strList.size(); i++) {
            if (str.contains(strList.get(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean stringContainStringList(String strList, String str) {
        return stringContainStringList(getStringListBySplitWithoutSpace(strList, SarDeviceStateListener.SIM_MCC_DEFAULT_STATE), str);
    }

    public static boolean stringListContainsString(ArrayList<String> strList, String str) {
        if (strList == null || str == null) {
            return false;
        }
        for (int i = 0; i < strList.size(); i++) {
            if (strList.get(i) != null && strList.get(i).contains(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean stringListContainsString(String strList, String str) {
        return stringListContainsString(getStringListBySplitWithoutSpace(strList, SarDeviceStateListener.SIM_MCC_DEFAULT_STATE), str);
    }

    public static String[] stringListToStringArray(ArrayList<String> strList) {
        if (strList == null) {
            return null;
        }
        return (String[]) strList.toArray(new String[strList.size()]);
    }

    public static ArrayList<String> stringArrayToStringList(String[] strArray) {
        if (strArray == null) {
            return null;
        }
        ArrayList<String> strList = new ArrayList<>();
        for (String str : strArray) {
            strList.add(str);
        }
        return strList;
    }

    public static String[] getStrArrayBySplitWithoutSpace(String strSrc, String strSplit) {
        return stringListToStringArray(getStringListBySplitWithoutSpace(strSrc, strSplit));
    }

    public static ArrayList<String> getStringListBySplitWithoutSpace(String strSrc, String strSplit) {
        if (strSrc == null) {
            log("getStringListBySplitWithoutSpace, strSrc is null");
            return null;
        }
        if (strSplit == null) {
            log("getStringListBySplitWithoutSpace, strSplit is null");
            return null;
        }
        String[] strTgt = strSrc.split(strSplit);
        ArrayList<String> strList = new ArrayList<>();
        for (String str : strTgt) {
            String ele = str.trim();
            if (!"".equals(ele)) {
                strList.add(ele);
            }
        }
        return strList;
    }

    public static String getStringBySplit(String[] strSrc, String strSplit) {
        if (strSrc == null) {
            log("getStringBySplit, strSrc is null");
            return null;
        }
        if (strSplit == null) {
            log("getStringBySplit, strSplit is null");
            return null;
        }
        int size = strSrc.length;
        String strTgt = strSrc[0];
        for (int i = 1; i < size; i++) {
            strTgt = strTgt + strSplit + strSrc[i];
        }
        return strTgt;
    }

    public static String removeStringBySplit(String strSrc, String strDel, String strSplit) {
        String[] strArr = getStrArrayBySplitWithoutSpace(strSrc, strSplit);
        if (strArr == null) {
            log("removeStringBySplit, getStrArrayBySplitWithoutSpace fail");
            return null;
        }
        int size = strArr.length;
        String strTgt = "";
        for (int i = 0; i < size; i++) {
            if (strArr[i] != null && !strArr[i].equals(strDel)) {
                if (!"".equals(strTgt)) {
                    strTgt = strTgt + ";";
                }
                strTgt = strTgt + strArr[i];
            }
        }
        return strTgt;
    }

    public static String addStringFrontBySplit(String strSrc, String strAdd, String strSplit) {
        if (strSrc == null || "".equals(strSrc)) {
            return strAdd;
        }
        String strTgt = strAdd + ";" + strSrc;
        return strTgt;
    }

    public static ByteBuffer getHexDataBufferFromString(String str, boolean bPrint) {
        int iStart;
        String byteStr;
        if (str == null) {
            log("getHexDataBufferFromString, str is null");
            return null;
        }
        String strNewValue = str.trim();
        byte[] bArry = new byte[20];
        int lenStr = strNewValue.length();
        int i = 0;
        int j = 0;
        do {
            if (i + 2 < lenStr) {
                iStart = (lenStr - 2) - i;
                int iStart2 = lenStr - i;
                String byteStr2 = strNewValue.substring(iStart, iStart2);
                if (bPrint) {
                    tmpLog("byteStr = " + byteStr2);
                }
                byteStr = byteStr2;
            } else {
                int iEnd = lenStr - i;
                String byteStr3 = strNewValue.substring(0, iEnd);
                if (bPrint) {
                    tmpLog("end, byteStr = " + byteStr3);
                }
                iStart = 0;
                byteStr = byteStr3;
            }
            try {
                int iVal = Integer.valueOf(byteStr, 16).intValue();
                byte bVal = (byte) iVal;
                if (bPrint) {
                    tmpLog("bVal[" + j + "] = " + ((int) bVal) + ", iVal = " + iVal + ", byteStr = " + byteStr);
                }
                bArry[j] = bVal;
                i += 2;
                j++;
                if (i >= lenStr) {
                    break;
                }
            } catch (ArithmeticException ex) {
                log("ArithmeticException, str = " + str);
                log("ArithmeticException: " + ex);
                ex.printStackTrace();
                return null;
            } catch (NumberFormatException ex2) {
                log("NumberFormatException, str = " + str);
                log("NumberFormatException: " + ex2);
                ex2.printStackTrace();
                return null;
            }
        } while (j < 20);
        while (j < 20) {
            bArry[j] = 0;
            j++;
        }
        ByteBuffer strBuf = ByteBuffer.wrap(bArry);
        strBuf.order(ByteOrder.nativeOrder());
        return strBuf;
    }

    public static byte getByte(ByteBuffer data, byte def) {
        return data != null ? data.get() : def;
    }

    public static short getShort(ByteBuffer data, short def) {
        return data != null ? data.getShort() : def;
    }

    public static int getInt(ByteBuffer data, int def) {
        return data != null ? data.getInt() : def;
    }

    public static long getLong(ByteBuffer data, long def) {
        return data != null ? data.getLong() : def;
    }

    public static float getFloat(ByteBuffer data, float def) {
        return data != null ? data.getFloat() : def;
    }

    public static double getDouble(ByteBuffer data, double def) {
        return data != null ? data.getDouble() : def;
    }

    public static String getString(byte[] val, int splitBytes, String splitChar) {
        String str = "";
        if (val == null) {
            return "";
        }
        for (int i = 0; i < val.length; i++) {
            if (i != 0 && splitBytes != 0 && i % splitBytes == 0 && splitChar != null) {
                str = str + splitChar;
            }
            str = str + getString(val[i], true, true, true, true);
        }
        return str;
    }

    public static String getString(byte val, boolean unSigned, boolean hexNotDec) {
        return getString(val, unSigned, hexNotDec, true, true);
    }

    public static String getString(byte val, boolean unSigned, boolean hexNotDec, boolean bPrint, boolean fillHexZero) {
        String strVal;
        if (bPrint) {
            tmpLog("getString, byte, val = " + ((int) val) + ", unSigned = " + unSigned + ", bPrint = " + bPrint + ", hexNotDec = " + hexNotDec + ", fillHexZero = " + fillHexZero);
        }
        if (hexNotDec) {
            strVal = Integer.toHexString(val & EFS_VALUE_ASDIV_73841_DYNAMIC_SWITCH);
            if (1 == strVal.length() && fillHexZero) {
                strVal = "0" + strVal;
            }
            if (bPrint) {
                tmpLog("HEX, strVal = " + strVal);
            }
        } else {
            strVal = "" + ((int) val);
            if (unSigned && val < 0) {
                byte lowVal = (byte) (val & 127);
                BigDecimal bigVal = BigDecimal.valueOf(lowVal).add(BigDecimal.valueOf(127L)).add(BigDecimal.valueOf(1L));
                strVal = bigVal.toString();
                if (bPrint) {
                    tmpLog("val < 0, new strVal = " + strVal);
                }
            }
        }
        if (strVal != null) {
            return strVal.toUpperCase();
        }
        return strVal;
    }

    public static byte getByteFromString(String str) {
        return getByteFromString(str, true);
    }

    public static byte getByteFromString(String str, boolean hexNotDec) {
        return getByteFromString(str, true, hexNotDec);
    }

    public static byte getByteFromString(String strIn, boolean bPrint, boolean hexNotDec) {
        if (bPrint) {
            tmpLog("getByteFromString, byte, strIn = " + strIn + ", bPrint = " + bPrint + ", hexNotDec = " + hexNotDec);
        }
        if (strIn == null) {
            tmpLog("strIn is null, return 0");
            return (byte) 0;
        }
        String str = strIn.trim();
        if ("".equals(str)) {
            tmpLog("str is empty, return 0");
            return (byte) 0;
        }
        byte val = 0;
        try {
            if (hexNotDec) {
                ByteBuffer buf = getHexDataBufferFromString(str, bPrint);
                if (buf != null) {
                    val = buf.get();
                }
            } else {
                BigDecimal data = new BigDecimal(str);
                val = data.byteValue();
            }
        } catch (ArithmeticException ex) {
            log("ArithmeticException, str = " + str);
            log("ArithmeticException: " + ex);
            ex.printStackTrace();
        } catch (NumberFormatException ex2) {
            log("NumberFormatException, str = " + str);
            log("NumberFormatException: " + ex2);
            ex2.printStackTrace();
        }
        if (bPrint) {
            tmpLog("val = " + ((int) val));
        }
        return val;
    }

    public static String getString(short val, boolean unSigned, boolean hexNotDec) {
        String strVal;
        tmpLog("getString, short, val = " + ((int) val) + ", unSigned = " + unSigned);
        if (hexNotDec) {
            strVal = Integer.toHexString(65535 & val);
            tmpLog("HEX, strVal = " + strVal);
        } else {
            strVal = "" + ((int) val);
            if (unSigned && val < 0) {
                short lowVal = (short) (val & Short.MAX_VALUE);
                BigDecimal bigVal = BigDecimal.valueOf(lowVal).add(BigDecimal.valueOf(32767L)).add(BigDecimal.valueOf(1L));
                strVal = bigVal.toString();
                tmpLog("val < 0, new strVal = " + strVal);
            }
        }
        if (strVal != null) {
            return strVal.toUpperCase();
        }
        return strVal;
    }

    public static short getShortFromString(String str, boolean hexNotDec) {
        tmpLog("getShortFromString, str = " + str + ", hexNotDec = " + hexNotDec);
        if (str == null) {
            tmpLog("str is null, return 0");
            return (short) 0;
        }
        str.trim();
        if ("".equals(str)) {
            tmpLog("str is empty, return 0");
            return (short) 0;
        }
        short val = 0;
        try {
            if (hexNotDec) {
                ByteBuffer buf = getHexDataBufferFromString(str, true);
                if (buf != null) {
                    val = buf.getShort();
                }
            } else {
                BigDecimal data = new BigDecimal(str);
                val = data.shortValue();
            }
        } catch (ArithmeticException ex) {
            log("ArithmeticException, str = " + str);
            log("ArithmeticException: " + ex);
            ex.printStackTrace();
        } catch (NumberFormatException ex2) {
            log("NumberFormatException, str = " + str);
            log("NumberFormatException: " + ex2);
            ex2.printStackTrace();
        }
        tmpLog("val = " + ((int) val));
        return val;
    }

    public static String getString(int val, boolean unSigned, boolean hexNotDec) {
        String strVal;
        tmpLog("getString, int, val = " + val + ", unSigned = " + unSigned);
        if (hexNotDec) {
            strVal = Integer.toHexString(val);
            tmpLog("HEX, strVal = " + strVal);
        } else {
            strVal = "" + val;
            if (unSigned && val < 0) {
                int lowVal = val & Integer.MAX_VALUE;
                BigDecimal bigVal = BigDecimal.valueOf(lowVal).add(BigDecimal.valueOf(2147483647L)).add(BigDecimal.valueOf(1L));
                strVal = bigVal.toString();
                tmpLog("val < 0, new strVal = " + strVal);
            }
        }
        if (strVal != null) {
            return strVal.toUpperCase();
        }
        return strVal;
    }

    public static int getIntFromString(String str, boolean hexNotDec) {
        tmpLog("getIntFromString, str = " + str + ", hexNotDec = " + hexNotDec);
        if (str == null) {
            tmpLog("str is null, return 0");
            return 0;
        }
        str.trim();
        if ("".equals(str)) {
            tmpLog("str is empty, return 0");
            return 0;
        }
        int val = 0;
        try {
            if (hexNotDec) {
                ByteBuffer buf = getHexDataBufferFromString(str, true);
                if (buf != null) {
                    val = buf.getInt();
                }
            } else {
                BigDecimal data = new BigDecimal(str);
                val = data.intValue();
            }
        } catch (ArithmeticException ex) {
            log("ArithmeticException, str = " + str);
            log("ArithmeticException: " + ex);
            ex.printStackTrace();
        } catch (NumberFormatException ex2) {
            log("NumberFormatException, str = " + str);
            log("NumberFormatException: " + ex2);
            ex2.printStackTrace();
        }
        tmpLog("val = " + val);
        return val;
    }

    public static String getString(long val, boolean unSigned, boolean hexNotDec) {
        String strVal;
        tmpLog("getString, long, val = " + val + ", unSigned = " + unSigned + ", hexNotDec = " + hexNotDec);
        if (hexNotDec) {
            strVal = Long.toHexString(val);
            tmpLog("HEX, strVal = " + strVal);
        } else {
            strVal = "" + val;
            if (unSigned && val < 0) {
                long lowVal = val & MODEM_STATS_DATA_INVALID_6;
                BigDecimal bigVal = BigDecimal.valueOf(lowVal).add(BigDecimal.valueOf(MODEM_STATS_DATA_INVALID_6)).add(BigDecimal.valueOf(1L));
                strVal = bigVal.toString();
                tmpLog("val < 0, new strVal = " + strVal);
            }
        }
        if (strVal != null) {
            return strVal.toUpperCase();
        }
        return strVal;
    }

    public static long getLongFromString(String str, boolean hexNotDec) {
        tmpLog("getLongFromString, str = " + str + ", hexNotDec = " + hexNotDec);
        if (str == null) {
            tmpLog("str is null, return 0");
            return 0L;
        }
        str.trim();
        if ("".equals(str)) {
            tmpLog("str is empty, return 0");
            return 0L;
        }
        long val = 0;
        try {
            if (hexNotDec) {
                ByteBuffer buf = getHexDataBufferFromString(str, true);
                if (buf != null) {
                    val = buf.getLong();
                }
            } else {
                BigDecimal data = new BigDecimal(str);
                val = data.longValue();
            }
        } catch (ArithmeticException ex) {
            log("ArithmeticException, str = " + str);
            log("ArithmeticException: " + ex);
            ex.printStackTrace();
        } catch (NumberFormatException ex2) {
            log("NumberFormatException, str = " + str);
            log("NumberFormatException: " + ex2);
            ex2.printStackTrace();
        }
        tmpLog("val = " + val);
        return val;
    }

    public static String getString(float val, boolean unSigned, boolean hexNotDec) {
        String strVal;
        tmpLog("getString, float, val = " + val + ", unSigned = " + unSigned + ", hexNotDec = " + hexNotDec);
        if (hexNotDec) {
            strVal = Float.toHexString(val);
            tmpLog("HEX, strVal = " + strVal);
        } else {
            strVal = "" + val;
            if (unSigned && val < 0.0f) {
                BigDecimal bigVal = BigDecimal.valueOf(0.0f).add(BigDecimal.valueOf(3.4028234663852886E38d)).add(BigDecimal.valueOf(1L));
                strVal = bigVal.toString();
                tmpLog("val < 0, new strVal = " + strVal);
            }
        }
        if (strVal != null) {
            return strVal.toUpperCase();
        }
        return strVal;
    }

    public static String getString(float val) {
        tmpLog("getString, float, val = " + val);
        return "" + val;
    }

    public static float getFloatFromString(String str, boolean hexNotDec) {
        tmpLog("getFloatFromString, str = " + str + ", hexNotDec = " + hexNotDec);
        if (str == null) {
            tmpLog("str is null, return 0");
            return 0.0f;
        }
        str.trim();
        if ("".equals(str)) {
            tmpLog("str is empty, return 0");
            return 0.0f;
        }
        float val = 0.0f;
        try {
            if (hexNotDec) {
                ByteBuffer buf = getHexDataBufferFromString(str, true);
                if (buf != null) {
                    val = buf.getFloat();
                }
            } else {
                BigDecimal data = new BigDecimal(str);
                val = data.floatValue();
            }
        } catch (ArithmeticException ex) {
            log("ArithmeticException, str = " + str);
            log("ArithmeticException: " + ex);
            ex.printStackTrace();
        } catch (NumberFormatException ex2) {
            log("NumberFormatException, str = " + str);
            log("NumberFormatException: " + ex2);
            ex2.printStackTrace();
        }
        tmpLog("val = " + val);
        return val;
    }

    public static String getString(double val, boolean unSigned, boolean hexNotDec) {
        String strVal;
        tmpLog("getString, double, val = " + val + ", unSigned = " + unSigned + ", hexNotDec = " + hexNotDec);
        if (hexNotDec) {
            strVal = Double.toHexString(val);
            tmpLog("HEX, strVal = " + strVal);
        } else {
            strVal = "" + val;
            if (unSigned && val < 0.0d) {
                BigDecimal bigVal = BigDecimal.valueOf(0.0d).add(BigDecimal.valueOf(Double.MAX_VALUE)).add(BigDecimal.valueOf(1L));
                strVal = bigVal.toString();
                tmpLog("val < 0, new strVal = " + strVal);
            }
        }
        if (strVal != null) {
            return strVal.toUpperCase();
        }
        return strVal;
    }

    public static String getString(double val) {
        tmpLog("getString, double, val = " + val);
        return "" + val;
    }

    public static double getDoubleFromString(String str, boolean hexNotDec) {
        tmpLog("getDoubleFromString, str = " + str + ", hexNotDec = " + hexNotDec);
        if (str == null) {
            tmpLog("str is null, return 0");
            return 0.0d;
        }
        str.trim();
        if ("".equals(str)) {
            tmpLog("str is empty, return 0");
            return 0.0d;
        }
        double val = 0.0d;
        try {
            if (hexNotDec) {
                ByteBuffer buf = getHexDataBufferFromString(str, true);
                if (buf != null) {
                    val = buf.getDouble();
                }
            } else {
                BigDecimal data = new BigDecimal(str);
                val = data.doubleValue();
            }
        } catch (ArithmeticException ex) {
            log("ArithmeticException, str = " + str);
            log("ArithmeticException: " + ex);
            ex.printStackTrace();
        } catch (NumberFormatException ex2) {
            log("NumberFormatException, str = " + str);
            log("NumberFormatException: " + ex2);
            ex2.printStackTrace();
        }
        tmpLog("val = " + val);
        return val;
    }

    public static String getStringForDouble(ByteBuffer data) {
        if (data == null) {
            return "-";
        }
        return "" + data.getDouble();
    }

    public static String getStringForFloat(ByteBuffer data) {
        if (data == null) {
            return "-";
        }
        return "" + String.format("%.2f", Float.valueOf(data.getFloat()));
    }

    public static String getStringForLong(ByteBuffer data, boolean unSigned, boolean hexNotDec) {
        if (data == null) {
            return "-";
        }
        return getString(data.getLong(), unSigned, hexNotDec);
    }

    public static String getStringForInt(ByteBuffer data, boolean unSigned, boolean hexNotDec) {
        if (data == null) {
            return "-";
        }
        return getString(data.getInt(), unSigned, hexNotDec);
    }

    public static String getStringForShort(ByteBuffer data, boolean unSigned, boolean hexNotDec) {
        if (data == null) {
            return "-";
        }
        return getString(data.getShort(), unSigned, hexNotDec);
    }

    public static String getStringForByte(ByteBuffer data, boolean unSigned, boolean hexNotDec) {
        if (data == null) {
            return "-";
        }
        return getString(data.get(), unSigned, hexNotDec);
    }

    public static String getStringForBytes(ByteBuffer data, int byteSize) {
        if (data == null || byteSize <= 0) {
            return "-";
        }
        byte[] bytes = new byte[byteSize];
        data.get(bytes);
        return bytesToString(bytes);
    }

    public static byte revertHigh4AndLow4(byte val) {
        int val1 = (val << 4) & 240;
        int val2 = (val >> 4) & 15;
        return (byte) (val1 + val2);
    }

    public static void skipData(ByteBuffer data, int byteNum) {
        if (data == null) {
            return;
        }
        for (int i = 0; i < byteNum; i++) {
            data.get();
        }
    }

    public static int min(int val, int min) {
        if (val <= min) {
            return val;
        }
        return min;
    }

    public static int max(int val, int max) {
        if (val >= max) {
            return val;
        }
        return max;
    }

    public static int mid(int val, int min, int max) {
        int newVal = val;
        if (newVal > max) {
            newVal = max;
        }
        if (newVal < min) {
            return min;
        }
        return newVal;
    }

    public static boolean isNumber(String filed) {
        if (filed == null) {
            log("isNumber, filed is null");
            return false;
        }
        String newFild = filed.trim();
        if (true == TextUtils.equals(newFild, "")) {
            log("isNumber, filed is empty");
            return false;
        }
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(filed);
        return m.matches();
    }

    public static boolean isMobileDataEnabled(Context context) {
        TelephonyManager teleMgr = (TelephonyManager) context.getSystemService("phone");
        if (teleMgr != null) {
            int subId = SubscriptionManager.getDefaultDataSubscriptionId();
            TelephonyManager dataTeleMgr = teleMgr.createForSubscriptionId(subId);
            if (dataTeleMgr != null) {
                return dataTeleMgr.isDataEnabled();
            }
            return false;
        }
        return false;
    }

    public static int getInsertedSimCount() {
        int insertedSimCount = 0;
        Phone[] phones = PhoneFactory.getPhones();
        if (phones != null) {
            for (Phone phone : phones) {
                if (phone.getIccCard().hasIccCard()) {
                    insertedSimCount++;
                }
            }
        }
        return insertedSimCount;
    }

    public static int getOperateMode(OemHookAgent hookAgent) {
        if (hookAgent == null) {
            log("getOperateMode, hookAgent is null");
            return -1;
        }
        String strOperate = hookAgent.onHookPropGetSync(PROP_RADIO_OPERATE_MODE, "-1");
        if (!isNumber(strOperate)) {
            return -1;
        }
        return Integer.parseInt(strOperate);
    }

    public static String getOperateModeString(OemHookAgent hookAgent) {
        String str;
        int operate = getOperateMode(hookAgent);
        if (operate == 0) {
            str = "Online";
        } else if (1 == operate) {
            str = "Lower Power Mode";
        } else if (2 == operate) {
            str = "Factory Test Mode";
        } else if (3 == operate) {
            str = "Offline";
        } else if (4 == operate) {
            str = "Resetting";
        } else if (5 == operate) {
            str = "ShuttingDown";
        } else if (6 == operate) {
            str = "Persistent Low Power Mode";
        } else if (7 == operate) {
            str = "Mode-only Low Power Mode";
        } else if (8 == operate) {
            str = "Conducting Network Test for GSM/WCDMA";
        } else {
            str = "UNKnown";
        }
        return "[" + operate + "]" + str;
    }

    public static String getTeleNetworkTypeString(int rat) {
        if (rat == 0) {
            return "UNKNOWN";
        }
        if (1 == rat) {
            return "GPRS";
        }
        if (2 == rat) {
            return "EDGE";
        }
        if (3 == rat) {
            return "UMTS";
        }
        if (4 == rat) {
            return "CDMA";
        }
        if (5 == rat) {
            return "EVDO_0";
        }
        if (6 == rat) {
            return "EVDO_A";
        }
        if (7 == rat) {
            return "1xRTT";
        }
        if (8 == rat) {
            return "HSDPA";
        }
        if (9 == rat) {
            return "HSUPA";
        }
        if (10 == rat) {
            return "HSPA";
        }
        if (11 == rat) {
            return "IDEN";
        }
        if (12 == rat) {
            return "EVDO_B";
        }
        if (13 == rat) {
            return "LTE";
        }
        if (14 == rat) {
            return "EHRPD";
        }
        if (15 == rat) {
            return "HSPAP";
        }
        if (16 == rat) {
            return "GSM";
        }
        if (17 == rat) {
            return "NETWORK_TYPE_TD_SCDMA";
        }
        if (18 == rat) {
            return "IWLAN";
        }
        if (19 == rat) {
            return "LTE_CA";
        }
        if (20 == rat) {
            return "NR";
        }
        if (NETWORK_TYPE_NR_CA == rat) {
            return "NR_CA";
        }
        String str = "[" + rat + "]UNKnown";
        return str;
    }

    public static String getEnbCellString(int ecgi) {
        int cell = (ecgi >> 2) & 63;
        int eNB = (ecgi >> 8) & 1048575;
        return eNB + "/" + cell;
    }

    public static String getSimStateString() {
        return SystemProperties.get(PROP_SIM_STATE, SIM_UNKNOWN);
    }

    public static String getCardSlotStateString(OemHookAgent hookAgent) {
        if (hookAgent == null) {
            log("getCardSlotStateString, hookAgent is null");
            return "UNKnown";
        }
        ByteBuffer buf = hookAgent.onHookCommonMsgSync(39, 0);
        if (buf == null) {
            log("getCardSlotStateString, byteBuf is null");
            return "UNKnown";
        }
        int r = buf.getInt();
        log("getCardSlotStateString, r = " + r);
        if (r != 0) {
            return "UNKnown";
        }
        int t = buf.getInt();
        int l = buf.getInt();
        log("getCardSlotStateString, t = " + t + ", l = " + l);
        int v1 = buf.getInt();
        log("getCardSlotStateString, v1 = " + v1);
        if (v1 == 0) {
            return "ABSENT";
        }
        return "PRESENT";
    }

    public static String sizeByteToString(long byteSize) {
        double dSize = byteSize;
        String strUnit = " B";
        double dNew = dSize;
        if (dNew >= 1000.0d) {
            dNew = dSize / 1024.0d;
            strUnit = " K";
            if (dNew >= 1000.0d) {
                dNew = dSize / (1024.0d * 1024.0d);
                strUnit = " M";
                if (dNew >= 1000.0d) {
                    dNew = dSize / ((1024.0d * 1024.0d) * 1024.0d);
                    strUnit = " G";
                    if (dNew >= 1000.0d) {
                        dNew = dSize / (((1024.0d * 1024.0d) * 1024.0d) * 1024.0d);
                        strUnit = " T";
                    }
                }
            }
        }
        BigDecimal bg = new BigDecimal(dNew);
        return "" + bg.setScale(2, 4).doubleValue() + strUnit;
    }

    public static long getDiagDeadlineFromProp(OemHookAgent hookAgent) {
        log("getDiagDeadlineFromProp, hookAgent: " + hookAgent);
        if (hookAgent == null) {
            log("getDiagDeadlineFromProp, hookAgent is null");
            return 0L;
        }
        String ret = hookAgent.onHookPropGetSync(PROP_MTB_DIAG_DEADLINE, "0");
        long diagDeadline = getLongFromString(ret, false);
        log("getDiagDeadlineFromProp, ret: " + ret + ", diagDeadline: " + diagDeadline + ", deadlineStr: " + timeLongToString(diagDeadline));
        return diagDeadline;
    }

    public static long getDiagDeadlineFromProp() {
        log("getDiagDeadlineFromProp");
        String str = SystemProperties.get(PROP_MTB_DIAG_DEADLINE, "0");
        long deadline = onGetLongFromString(str, 0);
        log("getDiagDeadlineFromProp, deadline = " + deadline + ", deadlineStr: " + timeLongToString(deadline));
        return deadline;
    }

    public static boolean onModemDiagIsOpen() {
        log("onModemDiagIsOpen");
        if (mHookAgent == null) {
            log("onModemDiagIsOpen, mHookAgent is null");
            return false;
        }
        String usb_prop = exeAdbCmd("getprop sys.usb.config");
        log("usb_prop: " + usb_prop);
        if (usb_prop.contains("diag")) {
            log("diag has been opened");
            return true;
        }
        log("diag has been closed");
        return false;
    }

    public static boolean fileExistEx(String fileName) {
        try {
            log("fileExistEx, fileName: " + fileName);
            File file = new File(fileName);
            if (!file.exists()) {
                log("fileExistEx: file not exist");
                return false;
            }
            if (!file.isFile()) {
                log("fileExistEx:  file not file");
                return false;
            }
            if (!file.canRead()) {
                log("fileExistEx:  file cannot read");
                return false;
            }
            return true;
        } catch (Exception e) {
            log("fileExistEx, Exception: " + e);
            e.printStackTrace();
            return false;
        }
    }

    public static boolean fileExist(String fileName) {
        try {
            log("fileExist, fileName: " + fileName);
            File file = new File(fileName);
            if (!file.exists()) {
                log("fileExist: file not exist");
                return false;
            }
            if (!file.isFile()) {
                log("fileExist:  file not file");
                return false;
            }
            return true;
        } catch (Exception e) {
            log("fileExist, Exception: " + e);
            e.printStackTrace();
            return false;
        }
    }

    public static boolean createDir(String dirPath) {
        try {
            log("createDir, dirPath: " + dirPath);
            File file = new File(dirPath);
            if (file.getParentFile().exists()) {
                file.mkdir();
                return true;
            }
            createDir(file.getParentFile().getAbsolutePath());
            file.mkdir();
            return true;
        } catch (Exception e) {
            log("createDir, Exception: " + e);
            e.printStackTrace();
            return false;
        }
    }

    public static boolean writeStrToFile(String filePath, String str) {
        OutputStreamWriter outputStrStream = null;
        try {
            try {
                log("writeStrToFile, filePath: " + filePath);
                if (str == null) {
                    log("str is null");
                    if (0 != 0) {
                        try {
                            outputStrStream.close();
                        } catch (Exception e) {
                            log("writeStrToFile, close Exception: " + e);
                            e.printStackTrace();
                        }
                    }
                    return false;
                }
                log("str size: " + str.length());
                if (filePath == null) {
                    log("filePath is null");
                    if (0 != 0) {
                        try {
                            outputStrStream.close();
                        } catch (Exception e2) {
                            log("writeStrToFile, close Exception: " + e2);
                            e2.printStackTrace();
                        }
                    }
                    return false;
                }
                String path = getFilePathFromFullName(filePath);
                if (path == null) {
                    log("file path get failed, do nothing");
                    if (0 != 0) {
                        try {
                            outputStrStream.close();
                        } catch (Exception e3) {
                            log("writeStrToFile, close Exception: " + e3);
                            e3.printStackTrace();
                        }
                    }
                    return false;
                }
                createDir(path);
                FileOutputStream outputStream = new FileOutputStream(filePath);
                OutputStreamWriter outputStrStream2 = new OutputStreamWriter(outputStream);
                outputStrStream2.write(str);
                outputStrStream2.close();
                try {
                    outputStrStream2.close();
                    return true;
                } catch (Exception e4) {
                    log("writeStrToFile, close Exception: " + e4);
                    e4.printStackTrace();
                    return true;
                }
            } catch (Throwable th) {
                if (0 != 0) {
                    try {
                        outputStrStream.close();
                    } catch (Exception e5) {
                        log("writeStrToFile, close Exception: " + e5);
                        e5.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (Exception e6) {
            log("writeStrToFile, Exception: " + e6);
            e6.printStackTrace();
            if (0 != 0) {
                try {
                    outputStrStream.close();
                } catch (Exception e7) {
                    log("writeStrToFile, close Exception: " + e7);
                    e7.printStackTrace();
                }
            }
            return false;
        }
    }

    public static boolean writeStrToFileEx(String filePath, String str) {
        FileWriter writer = null;
        try {
            try {
                log("writeStrToFileEx, filePath: " + filePath);
                if (str == null) {
                    log("str is null");
                    if (0 != 0) {
                        try {
                            writer.close();
                        } catch (Exception e) {
                            log("writeStrToFileEx, close Exception: " + e);
                            e.printStackTrace();
                        }
                    }
                    return false;
                }
                log("str size: " + str.length());
                if (filePath == null) {
                    log("filePath is null");
                    if (0 != 0) {
                        try {
                            writer.close();
                        } catch (Exception e2) {
                            log("writeStrToFileEx, close Exception: " + e2);
                            e2.printStackTrace();
                        }
                    }
                    return false;
                }
                String path = getFilePathFromFullName(filePath);
                if (path == null) {
                    log("file path get failed, do nothing");
                    if (0 != 0) {
                        try {
                            writer.close();
                        } catch (Exception e3) {
                            log("writeStrToFileEx, close Exception: " + e3);
                            e3.printStackTrace();
                        }
                    }
                    return false;
                }
                createDir(path);
                FileWriter writer2 = new FileWriter(filePath, false);
                writer2.write(str);
                writer2.close();
                try {
                    writer2.close();
                    return true;
                } catch (Exception e4) {
                    log("writeStrToFileEx, close Exception: " + e4);
                    e4.printStackTrace();
                    return true;
                }
            } catch (Exception e5) {
                log("writeStrToFileEx, Exception: " + e5);
                e5.printStackTrace();
                if (0 != 0) {
                    try {
                        writer.close();
                    } catch (Exception e6) {
                        log("writeStrToFileEx, close Exception: " + e6);
                        e6.printStackTrace();
                    }
                }
                return false;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    writer.close();
                } catch (Exception e7) {
                    log("writeStrToFileEx, close Exception: " + e7);
                    e7.printStackTrace();
                }
            }
            throw th;
        }
    }

    public static boolean writeStrToFileAppend(String filePath, String str) {
        PrintWriter writer = null;
        try {
            try {
                log("writeStrToFileAppend, filePath: " + filePath);
                if (str == null) {
                    log("str is null");
                    if (0 != 0) {
                        try {
                            writer.close();
                        } catch (Exception e) {
                            log("writeStrToFileAppend, close Exception: " + e);
                            e.printStackTrace();
                        }
                    }
                    return false;
                }
                log("str size: " + str.length());
                if (filePath == null) {
                    log("filePath is null");
                    if (0 != 0) {
                        try {
                            writer.close();
                        } catch (Exception e2) {
                            log("writeStrToFileAppend, close Exception: " + e2);
                            e2.printStackTrace();
                        }
                    }
                    return false;
                }
                String path = getFilePathFromFullName(filePath);
                if (path == null) {
                    log("file path get failed, do nothing");
                    if (0 != 0) {
                        try {
                            writer.close();
                        } catch (Exception e3) {
                            log("writeStrToFileAppend, close Exception: " + e3);
                            e3.printStackTrace();
                        }
                    }
                    return false;
                }
                createDir(path);
                PrintWriter writer2 = new PrintWriter(filePath, StandardCharsets.UTF_8);
                writer2.println(str);
                writer2.close();
                try {
                    writer2.close();
                    return true;
                } catch (Exception e4) {
                    log("writeStrToFileAppend, close Exception: " + e4);
                    e4.printStackTrace();
                    return true;
                }
            } catch (Exception e5) {
                log("writeStrToFileAppend, Exception: " + e5);
                e5.printStackTrace();
                if (0 != 0) {
                    try {
                        writer.close();
                    } catch (Exception e6) {
                        log("writeStrToFileAppend, close Exception: " + e6);
                        e6.printStackTrace();
                    }
                }
                return false;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    writer.close();
                } catch (Exception e7) {
                    log("writeStrToFileAppend, close Exception: " + e7);
                    e7.printStackTrace();
                }
            }
            throw th;
        }
    }

    public static boolean writeStrToFileAppendEx(String filePath, String str) {
        FileWriter writer = null;
        try {
            try {
                log("writeStrToFileAppendEx, filePath: " + filePath);
                if (str == null) {
                    log("str is null");
                    if (0 != 0) {
                        try {
                            writer.close();
                        } catch (Exception e) {
                            log("writeStrToFileAppendEx, close Exception: " + e);
                            e.printStackTrace();
                        }
                    }
                    return false;
                }
                log("str size: " + str.length());
                if (filePath == null) {
                    log("filePath is null");
                    if (0 != 0) {
                        try {
                            writer.close();
                        } catch (Exception e2) {
                            log("writeStrToFileAppendEx, close Exception: " + e2);
                            e2.printStackTrace();
                        }
                    }
                    return false;
                }
                String path = getFilePathFromFullName(filePath);
                if (path == null) {
                    log("file path get failed, do nothing");
                    if (0 != 0) {
                        try {
                            writer.close();
                        } catch (Exception e3) {
                            log("writeStrToFileAppendEx, close Exception: " + e3);
                            e3.printStackTrace();
                        }
                    }
                    return false;
                }
                createDir(path);
                FileWriter writer2 = new FileWriter(filePath, true);
                writer2.write(str);
                writer2.close();
                try {
                    writer2.close();
                } catch (Exception e4) {
                    log("writeStrToFileAppendEx, close Exception: " + e4);
                    e4.printStackTrace();
                }
                return true;
            } catch (Exception e5) {
                log("writeStrToFileAppendEx, Exception: " + e5);
                e5.printStackTrace();
                if (0 != 0) {
                    try {
                        writer.close();
                    } catch (Exception e6) {
                        log("writeStrToFileAppendEx, close Exception: " + e6);
                        e6.printStackTrace();
                    }
                }
                return false;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    writer.close();
                } catch (Exception e7) {
                    log("writeStrToFileAppendEx, close Exception: " + e7);
                    e7.printStackTrace();
                }
            }
            throw th;
        }
    }

    public static boolean writeBytesToFile(String filePath, byte[] bytes) {
        FileOutputStream outputStream = null;
        try {
            try {
                log("writeBytesToFile, filePath: " + filePath);
                if (bytes == null) {
                    log("bytes is null");
                    if (0 != 0) {
                        try {
                            outputStream.close();
                        } catch (Exception e) {
                            log("writeBytesToFile, close Exception: " + e);
                            e.printStackTrace();
                        }
                    }
                    return false;
                }
                log("bytes size: " + bytes.length);
                if (filePath == null) {
                    log("filePath is null");
                    if (0 != 0) {
                        try {
                            outputStream.close();
                        } catch (Exception e2) {
                            log("writeBytesToFile, close Exception: " + e2);
                            e2.printStackTrace();
                        }
                    }
                    return false;
                }
                String path = getFilePathFromFullName(filePath);
                if (path == null) {
                    log("file path get failed, do nothing");
                    if (0 != 0) {
                        try {
                            outputStream.close();
                        } catch (Exception e3) {
                            log("writeBytesToFile, close Exception: " + e3);
                            e3.printStackTrace();
                        }
                    }
                    return false;
                }
                createDir(path);
                FileOutputStream outputStream2 = new FileOutputStream(filePath);
                outputStream2.write(bytes);
                outputStream2.close();
                try {
                    outputStream2.close();
                    return true;
                } catch (Exception e4) {
                    log("writeBytesToFile, close Exception: " + e4);
                    e4.printStackTrace();
                    return true;
                }
            } catch (Exception e5) {
                log("writeBytesToFile, Exception: " + e5);
                e5.printStackTrace();
                if (0 != 0) {
                    try {
                        outputStream.close();
                    } catch (Exception e6) {
                        log("writeBytesToFile, close Exception: " + e6);
                        e6.printStackTrace();
                    }
                }
                return false;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    outputStream.close();
                } catch (Exception e7) {
                    log("writeBytesToFile, close Exception: " + e7);
                    e7.printStackTrace();
                }
            }
            throw th;
        }
    }

    public static byte[] readFileToBytes(String filePath) {
        FileInputStream inputStream = null;
        try {
            try {
                log("readFileToBytes, filePath: " + filePath);
                if (filePath == null) {
                    log("filePath is null");
                    if (0 != 0) {
                        try {
                            inputStream.close();
                        } catch (Exception e) {
                            log("readFileToBytes, close Exception: " + e);
                            e.printStackTrace();
                        }
                    }
                    return null;
                }
                FileInputStream inputStream2 = new FileInputStream(filePath);
                int lenAll = 0;
                byte[] retBufTmp = null;
                byte[] retBuf = null;
                byte[] buffer = new byte[IResultValue.MISYS_UNKNOWN];
                int len = inputStream2.read(buffer);
                log("len: " + len);
                while (len > 0) {
                    if (lenAll > 0) {
                        byte[] retBufTmp2 = new byte[lenAll];
                        for (int i = 0; i < lenAll; i++) {
                            retBufTmp2[i] = retBuf[i];
                        }
                        retBufTmp = retBufTmp2;
                    }
                    retBuf = new byte[lenAll + len];
                    for (int i2 = 0; i2 < lenAll; i2++) {
                        retBuf[i2] = retBufTmp[i2];
                    }
                    for (int i3 = 0; i3 < len; i3++) {
                        retBuf[lenAll + i3] = buffer[i3];
                    }
                    lenAll += len;
                    len = inputStream2.read(buffer);
                    log("lenAll: " + lenAll + ", len: " + len);
                }
                inputStream2.close();
                try {
                    inputStream2.close();
                } catch (Exception e2) {
                    log("readFileToBytes, close Exception: " + e2);
                    e2.printStackTrace();
                }
                return retBuf;
            } catch (Exception e3) {
                log("readFileToBytes, Exception: " + e3);
                e3.printStackTrace();
                if (0 != 0) {
                    try {
                        inputStream.close();
                    } catch (Exception e4) {
                        log("readFileToBytes, close Exception: " + e4);
                        e4.printStackTrace();
                    }
                }
                return null;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    inputStream.close();
                } catch (Exception e5) {
                    log("readFileToBytes, close Exception: " + e5);
                    e5.printStackTrace();
                }
            }
            throw th;
        }
    }

    public static byte[] readFileToBytes(int resId) {
        InputStream inputStream = null;
        try {
            log("readFileToBytes, resId: " + resId);
            inputStream = mContext.getResources().openRawResource(resId);
            int lenAll = 0;
            byte[] retBufTmp = null;
            byte[] retBuf = null;
            byte[] buffer = new byte[IResultValue.MISYS_UNKNOWN];
            int len = inputStream.read(buffer);
            log("len: " + len);
            while (len > 0) {
                if (lenAll > 0) {
                    byte[] retBufTmp2 = new byte[lenAll];
                    for (int i = 0; i < lenAll; i++) {
                        retBufTmp2[i] = retBuf[i];
                    }
                    retBufTmp = retBufTmp2;
                }
                retBuf = new byte[lenAll + len];
                for (int i2 = 0; i2 < lenAll; i2++) {
                    retBuf[i2] = retBufTmp[i2];
                }
                for (int i3 = 0; i3 < len; i3++) {
                    retBuf[lenAll + i3] = buffer[i3];
                }
                lenAll += len;
                len = inputStream.read(buffer);
                log("lenAll: " + lenAll + ", len: " + len);
            }
            inputStream.close();
            return retBuf;
        } catch (Exception e) {
            log("readFileToBytes, Exception: " + e);
            e.printStackTrace();
            if (inputStream == null) {
                return null;
            }
            try {
                return null;
            } catch (Exception e2) {
                return null;
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e3) {
                    log("readFileToBytes, close Exception: " + e3);
                    e3.printStackTrace();
                }
            }
        }
    }

    public static boolean deleteFile(String filePath) {
        try {
            log("deleteFile, filePath: " + filePath);
            if (filePath == null) {
                log("deleteFile: filePath is null");
                return true;
            }
            File file = new File(filePath);
            if (!file.isFile() || !file.exists()) {
                return true;
            }
            return file.delete();
        } catch (Exception e) {
            log("deleteFile, Exception: " + e);
            e.printStackTrace();
            return false;
        }
    }

    public static boolean copyFile(String oldPathName, String newPathName) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            try {
                log("copyFile, oldPathName: " + oldPathName + ", newPathName: " + newPathName);
                if (oldPathName == null) {
                    log("copyFile: oldPathName is null");
                    if (0 != 0) {
                        try {
                            fileInputStream.close();
                        } catch (Exception e) {
                            log("copyFile, fileInputStream close Exception: " + e);
                            e.printStackTrace();
                        }
                    }
                    if (0 != 0) {
                        try {
                            fileOutputStream.close();
                        } catch (Exception e2) {
                            log("copyFile, fileOutputStream close Exception: " + e2);
                            e2.printStackTrace();
                        }
                    }
                    return false;
                }
                if (newPathName == null) {
                    log("copyFile: newPathName is null");
                    if (0 != 0) {
                        try {
                            fileInputStream.close();
                        } catch (Exception e3) {
                            log("copyFile, fileInputStream close Exception: " + e3);
                            e3.printStackTrace();
                        }
                    }
                    if (0 != 0) {
                        try {
                            fileOutputStream.close();
                        } catch (Exception e4) {
                            log("copyFile, fileOutputStream close Exception: " + e4);
                            e4.printStackTrace();
                        }
                    }
                    return false;
                }
                String oldPathName2 = oldPathName.trim();
                String newPathName2 = newPathName.trim();
                File oldFile = new File(oldPathName2);
                String path = getFilePathFromFullName(newPathName2);
                if (path == null) {
                    log("file path get failed, do nothing");
                    if (0 != 0) {
                        try {
                            fileInputStream.close();
                        } catch (Exception e5) {
                            log("copyFile, fileInputStream close Exception: " + e5);
                            e5.printStackTrace();
                        }
                    }
                    if (0 != 0) {
                        try {
                            fileOutputStream.close();
                        } catch (Exception e6) {
                            log("copyFile, fileOutputStream close Exception: " + e6);
                            e6.printStackTrace();
                        }
                    }
                    return false;
                }
                createDir(path);
                File newFile = new File(newPathName2);
                if (!oldFile.exists()) {
                    log("copyFile: oldFile not exist");
                    if (0 != 0) {
                        try {
                            fileInputStream.close();
                        } catch (Exception e7) {
                            log("copyFile, fileInputStream close Exception: " + e7);
                            e7.printStackTrace();
                        }
                    }
                    if (0 != 0) {
                        try {
                            fileOutputStream.close();
                        } catch (Exception e8) {
                            log("copyFile, fileOutputStream close Exception: " + e8);
                            e8.printStackTrace();
                        }
                    }
                    return false;
                }
                if (!oldFile.isFile()) {
                    log("copyFile:  oldFile not file");
                    if (0 != 0) {
                        try {
                            fileInputStream.close();
                        } catch (Exception e9) {
                            log("copyFile, fileInputStream close Exception: " + e9);
                            e9.printStackTrace();
                        }
                    }
                    if (0 != 0) {
                        try {
                            fileOutputStream.close();
                        } catch (Exception e10) {
                            log("copyFile, fileOutputStream close Exception: " + e10);
                            e10.printStackTrace();
                        }
                    }
                    return false;
                }
                if (!oldFile.canRead()) {
                    log("copyFile:  oldFile cannot read");
                    if (0 != 0) {
                        try {
                            fileInputStream.close();
                        } catch (Exception e11) {
                            log("copyFile, fileInputStream close Exception: " + e11);
                            e11.printStackTrace();
                        }
                    }
                    if (0 != 0) {
                        try {
                            fileOutputStream.close();
                        } catch (Exception e12) {
                            log("copyFile, fileOutputStream close Exception: " + e12);
                            e12.printStackTrace();
                        }
                    }
                    return false;
                }
                FileInputStream fileInputStream2 = new FileInputStream(oldFile);
                FileOutputStream fileOutputStream2 = new FileOutputStream(newFile);
                byte[] buffer = new byte[IResultValue.MISYS_UNKNOWN];
                while (true) {
                    int byteRead = fileInputStream2.read(buffer);
                    if (-1 == byteRead) {
                        break;
                    }
                    fileOutputStream2.write(buffer, 0, byteRead);
                }
                fileInputStream2.close();
                fileOutputStream2.flush();
                fileOutputStream2.close();
                try {
                    fileInputStream2.close();
                } catch (Exception e13) {
                    log("copyFile, fileInputStream close Exception: " + e13);
                    e13.printStackTrace();
                }
                try {
                    fileOutputStream2.close();
                    return true;
                } catch (Exception e14) {
                    log("copyFile, fileOutputStream close Exception: " + e14);
                    e14.printStackTrace();
                    return true;
                }
            } catch (Exception e15) {
                log("copyFile, Exception: " + e15);
                e15.printStackTrace();
                if (0 != 0) {
                    try {
                        fileInputStream.close();
                    } catch (Exception e16) {
                        log("copyFile, fileInputStream close Exception: " + e16);
                        e16.printStackTrace();
                    }
                }
                if (0 != 0) {
                    try {
                        fileOutputStream.close();
                    } catch (Exception e17) {
                        log("copyFile, fileOutputStream close Exception: " + e17);
                        e17.printStackTrace();
                    }
                }
                return false;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    fileInputStream.close();
                } catch (Exception e18) {
                    log("copyFile, fileInputStream close Exception: " + e18);
                    e18.printStackTrace();
                }
            }
            if (0 == 0) {
                throw th;
            }
            try {
                fileOutputStream.close();
                throw th;
            } catch (Exception e19) {
                log("copyFile, fileOutputStream close Exception: " + e19);
                e19.printStackTrace();
                throw th;
            }
        }
    }

    public static boolean deleteDirectory(String dir) {
        try {
            log("deleteDirectory, dir: " + dir);
            if (dir == null) {
                log("deleteDirectory: dir is null");
                return true;
            }
            String dir2 = dir.trim();
            if (!dir2.endsWith(File.separator)) {
                dir2 = dir2 + File.separator;
            }
            File dirFile = new File(dir2);
            if (!dirFile.exists()) {
                log("deleteDirectory: dir is not exists");
                return true;
            }
            if (!dirFile.isDirectory()) {
                log("deleteDirectory: dir is not a directory");
                return false;
            }
            File[] files = dirFile.listFiles();
            if (files == null) {
                log("deleteDirectory: files is null, dir: " + dir2);
                return false;
            }
            boolean flag = true;
            for (int i = 0; i < files.length; i++) {
                String name = files[i].getAbsolutePath();
                if (files[i].isFile()) {
                    flag = deleteFile(name);
                    if (!flag) {
                        log("deleteFile fail: " + name);
                        break;
                    }
                } else {
                    if (files[i].isDirectory() && !(flag = deleteDirectory(name))) {
                        log("deleteDirectory fail: " + name);
                        break;
                    }
                }
            }
            if (!flag) {
                log("deleteDirectory sub-dir fail: " + dir2);
                return false;
            }
            log("deleteDirectory sub-dir ok: " + dir2);
            if (dirFile.delete()) {
                log("deleteDirectory sub-root ok: " + dir2);
                return true;
            }
            log("deleteDirectory sub-root fail: " + dir2);
            return false;
        } catch (Exception e) {
            log("deleteDirectory, Exception, dir: " + dir);
            log("deleteDirectory, Exception, e: " + e);
            e.printStackTrace();
            return false;
        }
    }

    public static boolean copyDirectory(String oldDir, String newDir) {
        try {
            log("copyDirectory, oldDir: " + oldDir + ", newDir: " + newDir);
            if (oldDir == null) {
                log("copyDirectory: oldDir is null");
                return false;
            }
            if (newDir == null) {
                log("copyDirectory: newDir is null");
                return false;
            }
            String oldDir2 = oldDir.trim();
            String newDir2 = newDir.trim();
            if (!oldDir2.endsWith(File.separator)) {
                oldDir2 = oldDir2 + File.separator;
            }
            if (!newDir2.endsWith(File.separator)) {
                newDir2 = newDir2 + File.separator;
            }
            File oldDirFile = new File(oldDir2);
            if (!oldDirFile.exists()) {
                log("copyDirectory: oldDir is not exists");
                return false;
            }
            if (!oldDirFile.isDirectory()) {
                log("copyDirectory: oldDir is not a directory");
                return false;
            }
            if (createDir(newDir2)) {
                log("createDir sub-root ok: " + newDir2);
                File[] files = oldDirFile.listFiles();
                if (files == null) {
                    log("copyDirectory: files is null, oldDir: " + oldDir2);
                    return false;
                }
                boolean flag = true;
                for (int i = 0; i < files.length; i++) {
                    String oldName = files[i].getAbsolutePath();
                    String newName = oldName.replace(oldDir2, newDir2);
                    if (files[i].isFile()) {
                        flag = copyFile(oldName, newName);
                        if (!flag) {
                            log("copyFile fail, oldName: " + oldName + ", newName: " + newName);
                            break;
                        }
                    } else {
                        if (files[i].isDirectory() && !(flag = copyDirectory(oldName, newName))) {
                            log("copyDirectory fail, oldName: " + oldName + ", newName: " + newName);
                            break;
                        }
                    }
                }
                if (!flag) {
                    log("copyDirectory sub-dir fail, oldDir: " + oldDir2 + ", newDir: " + newDir2);
                    return false;
                }
                log("copyDirectory sub-dir ok, oldDir: " + oldDir2 + ", newDir: " + newDir2);
                return true;
            }
            log("createDir sub-root fail: " + newDir2);
            return false;
        } catch (Exception e) {
            log("copyDirectory, Exception, oldDir: " + oldDir + ", newDir: " + newDir);
            log("copyDirectory, Exception, e: " + e);
            e.printStackTrace();
            return false;
        }
    }

    public static String getFileNameFromFullName(String fullName, int dirDeep) {
        if (fullName == null) {
            log("getFileNameFromFullName, fullName is null");
            return null;
        }
        String fullNameNew = fullName;
        for (int i = 0; i < dirDeep; i++) {
            fullNameNew = getFilePathFromFullName(fullNameNew);
            if (fullNameNew == null) {
                log("getFileNameFromFullName, dirDeep is invalid, dirDeep: " + dirDeep + ", fullName: " + fullName);
                return null;
            }
        }
        int idxS = fullNameNew.lastIndexOf("/");
        String fileName = null;
        if (idxS > 0) {
            fileName = fullName.substring(idxS);
        } else {
            log("getFileNameFromFullName, filePath is invalid, dirDeep: " + dirDeep + ", fullName: " + fullName);
        }
        log("getFileNameFromFullName, dirDeep: " + dirDeep + ", fullName: " + fullName + ", fileName: " + fileName);
        return fileName;
    }

    public static String getFilePathFromFullName(String fullName) {
        if (fullName == null) {
            log("getFilePathFromFullName, fullName is null");
            return null;
        }
        int idxS = fullName.lastIndexOf("/");
        String filePath = null;
        if (idxS > 0) {
            filePath = fullName.substring(0, idxS);
        } else {
            log("getFilePathFromFullName, filePath is invalid");
        }
        log("getFilePathFromFullName, fullName: " + fullName + ", filePath: " + filePath);
        return filePath;
    }

    public static void copyFileFromAssetsList(ArrayList<String> fileAssetsPathList, String newPath) {
        log("copyFileFromAssetsList, fileAssetsPathList = " + fileAssetsPathList + ", newPath: " + newPath);
        if (fileAssetsPathList == null) {
            log("fileAssetsPathList is null");
            return;
        }
        for (String fileAssetsPath : fileAssetsPathList) {
            String newName = getFileNameFromFullName(fileAssetsPath, 1);
            copyFileFromAssets(fileAssetsPath, newPath + newName);
        }
    }

    /* JADX WARN: Code duplicated, block: B:18:0x0052 A[Catch: IOException -> 0x004e, DONT_GENERATE, TryCatch #6 {IOException -> 0x004e, blocks: (B:14:0x004a, B:18:0x0052, B:20:0x0057), top: B:96:0x004a }] */
    /* JADX WARN: Code duplicated, block: B:20:0x0057 A[Catch: IOException -> 0x004e, DONT_GENERATE, TRY_LEAVE, TryCatch #6 {IOException -> 0x004e, blocks: (B:14:0x004a, B:18:0x0052, B:20:0x0057), top: B:96:0x004a }] */
    /* JADX WARN: Code duplicated, block: B:33:0x008b A[Catch: IOException -> 0x0087, DONT_GENERATE, TryCatch #0 {IOException -> 0x0087, blocks: (B:29:0x0083, B:33:0x008b, B:35:0x0090), top: B:86:0x0083 }] */
    /* JADX WARN: Code duplicated, block: B:35:0x0090 A[Catch: IOException -> 0x0087, DONT_GENERATE, TRY_LEAVE, TryCatch #0 {IOException -> 0x0087, blocks: (B:29:0x0083, B:33:0x008b, B:35:0x0090), top: B:86:0x0083 }] */
    /* JADX WARN: Code duplicated, block: B:51:0x00f2 A[Catch: IOException -> 0x00ee, DONT_GENERATE, TryCatch #5 {IOException -> 0x00ee, blocks: (B:47:0x00ea, B:51:0x00f2, B:53:0x00f7), top: B:94:0x00ea }] */
    /* JADX WARN: Code duplicated, block: B:53:0x00f7 A[Catch: IOException -> 0x00ee, DONT_GENERATE, TRY_LEAVE, TryCatch #5 {IOException -> 0x00ee, blocks: (B:47:0x00ea, B:51:0x00f2, B:53:0x00f7), top: B:94:0x00ea }] */
    /* JADX WARN: Code duplicated, block: B:67:0x013c A[Catch: IOException -> 0x0138, DONT_GENERATE, TryCatch #2 {IOException -> 0x0138, blocks: (B:63:0x0134, B:67:0x013c, B:69:0x0141), top: B:89:0x0134 }] */
    /* JADX WARN: Code duplicated, block: B:69:0x0141 A[Catch: IOException -> 0x0138, DONT_GENERATE, TRY_LEAVE, TryCatch #2 {IOException -> 0x0138, blocks: (B:63:0x0134, B:67:0x013c, B:69:0x0141), top: B:89:0x0134 }] */
    /* JADX WARN: Code duplicated, block: B:79:0x0169 A[Catch: IOException -> 0x0165, FINALLY_INSNS, TryCatch #3 {IOException -> 0x0165, blocks: (B:75:0x0161, B:79:0x0169, B:81:0x016e), top: B:91:0x0161 }] */
    /* JADX WARN: Code duplicated, block: B:81:0x016e A[Catch: IOException -> 0x0165, FINALLY_INSNS, TRY_LEAVE, TryCatch #3 {IOException -> 0x0165, blocks: (B:75:0x0161, B:79:0x0169, B:81:0x016e), top: B:91:0x0161 }] */
    public static boolean copyFileFromAssets(String assetsFilePath, String newPathName) {
        log("copyFileFromAssets, assetsFilePath: " + assetsFilePath + ", newPathName: " + newPathName);
        if (assetsFilePath == null) {
            log("assetsFilePath is null");
            return false;
        }
        if (newPathName == null) {
            log("newPathName is null");
            return false;
        }
        InputStream inputStream = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            File file = new File(newPathName);
            if (file.exists()) {
                log("file exists, do nothing");
                return true;
            }
            String path = getFilePathFromFullName(newPathName);
            if (path == null) {
                log("file path get failed, do nothing");
                return false;
            }
            createDir(path);
            File newFile = new File(newPathName);
            FileOutputStream fos2 = new FileOutputStream(newFile);
            BufferedOutputStream bos2 = new BufferedOutputStream(fos2);
            InputStream inputStream2 = mContext.getAssets().open(assetsFilePath);
            byte[] bytes = new byte[IResultValue.MISYS_UNKNOWN];
            while (inputStream2.read(bytes) > 0) {
                bos2.write(bytes, 0, bytes.length);
            }
            inputStream2.close();
            bos2.close();
            fos2.close();
            inputStream = null;
            bos = null;
            fos = null;
            return true;
        } catch (IOException e) {
            log("catch IOException e: " + e);
            e.printStackTrace();
            if (inputStream != null) {
                try {
                } catch (IOException e2) {
                    return false;
                }
            }
            return false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                    if (bos != null) {
                        bos.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e3) {
                    log("finally IOException e: " + e3);
                    e3.printStackTrace();
                }
            } else {
                if (bos != null) {
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            }
        }
    }

    public static ByteBuffer getBuffer(int bufLen) {
        if (bufLen <= 0) {
            log("getBuffer, bufLen is invalid, len = " + bufLen);
            return null;
        }
        ByteBuffer buf = ByteBuffer.allocate(bufLen);
        buf.order(ByteOrder.nativeOrder());
        return buf;
    }

    public static ByteBuffer getBuffer(byte[] bytes) {
        if (bytes == null) {
            tmpLog("bytes is null, getBuffer do nothing");
            return null;
        }
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        buf.order(ByteOrder.nativeOrder());
        return buf;
    }

    public static ByteBuffer getBuffer(byte[] bytes, int length) {
        if (bytes == null) {
            tmpLog("bytes is null, getBuffer do nothing");
            return null;
        }
        if (length > bytes.length) {
            tmpLog("length is larger than bytes.length, getBuffer do nothing");
            return null;
        }
        ByteBuffer buf = ByteBuffer.wrap(bytes, 0, length);
        buf.order(ByteOrder.nativeOrder());
        return buf;
    }

    public static byte[] getBytes(double val) {
        byte[] bytes = new byte[8];
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        buf.order(ByteOrder.nativeOrder());
        buf.putDouble(val);
        return bytes;
    }

    public static byte[] getBytes(float val) {
        byte[] bytes = new byte[4];
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        buf.order(ByteOrder.nativeOrder());
        buf.putFloat(val);
        return bytes;
    }

    public static byte[] getBytes(long val) {
        byte[] bytes = new byte[8];
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        buf.order(ByteOrder.nativeOrder());
        buf.putLong(val);
        return bytes;
    }

    public static byte[] getBytes(int val) {
        byte[] bytes = new byte[4];
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        buf.order(ByteOrder.nativeOrder());
        buf.putInt(val);
        return bytes;
    }

    public static byte[] getBytes(short val) {
        byte[] bytes = new byte[2];
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        buf.order(ByteOrder.nativeOrder());
        buf.putShort(val);
        return bytes;
    }

    public static byte[] getBytes(byte val) {
        byte[] bytes = new byte[1];
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        buf.order(ByteOrder.nativeOrder());
        buf.put(val);
        return bytes;
    }

    public static short getShortFromByteArray(byte[] byteArray, int start) {
        try {
            short s0 = (short) (byteArray[start] & EFS_VALUE_ASDIV_73841_DYNAMIC_SWITCH);
            short s1 = (short) (byteArray[start + 1] & EFS_VALUE_ASDIV_73841_DYNAMIC_SWITCH);
            short val = (short) (s0 | ((short) (s1 << 8)));
            return val;
        } catch (Exception e) {
            log("Get exception while getShortFromByteArray" + e);
            e.printStackTrace();
            return (short) 0;
        }
    }

    public static int getIntFromByteArray(byte[] byteArray, int start) {
        int val = 0;
        try {
            int val2 = byteArray[start] & EFS_VALUE_ASDIV_73841_DYNAMIC_SWITCH;
            val = val2 | ((byteArray[start + 1] << 8) & 65280) | ((byteArray[start + 2] << 16) & 16711680);
            return val | ((byteArray[start + 3] << 24) & (-16777216));
        } catch (Exception e) {
            log("Get exception while getIntFromByteArray" + e);
            e.printStackTrace();
            return val;
        }
    }

    public static long getLongFromByteArray(byte[] byteArray, int start) {
        try {
            long s0 = byteArray[start] & EFS_VALUE_ASDIV_73841_DYNAMIC_SWITCH;
            long s1 = byteArray[start + 1] & EFS_VALUE_ASDIV_73841_DYNAMIC_SWITCH;
            long s2 = byteArray[start + 2] & EFS_VALUE_ASDIV_73841_DYNAMIC_SWITCH;
            long s3 = byteArray[start + 3] & EFS_VALUE_ASDIV_73841_DYNAMIC_SWITCH;
            long s4 = byteArray[start + 4] & EFS_VALUE_ASDIV_73841_DYNAMIC_SWITCH;
            long s5 = byteArray[start + 5] & EFS_VALUE_ASDIV_73841_DYNAMIC_SWITCH;
            long s6 = byteArray[start + 6] & EFS_VALUE_ASDIV_73841_DYNAMIC_SWITCH;
            try {
                long s7 = byteArray[start + 7] & EFS_VALUE_ASDIV_73841_DYNAMIC_SWITCH;
                long s8 = s6 << 48;
                long s9 = s7 << 56;
                long val = s0 | (s1 << 8) | (s2 << 16) | (s3 << 24) | (s4 << 32) | (s5 << 40) | s8 | s9;
                return val;
            } catch (Exception e) {
                e = e;
                log("Get exception while getLongFromByteArray" + e);
                e.printStackTrace();
                return 0L;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    public static ByteBuffer onGetHexDataBufferFromString(String str, boolean bPrint) {
        int iStart;
        String byteStr;
        if (str == null) {
            log("onGetHexDataBufferFromString, str is null");
            return null;
        }
        String strNewValue = str.trim();
        byte[] bArry = new byte[20];
        int lenStr = strNewValue.length();
        int i = 0;
        int j = 0;
        do {
            if (i + 2 < lenStr) {
                iStart = (lenStr - 2) - i;
                int iStart2 = lenStr - i;
                String byteStr2 = strNewValue.substring(iStart, iStart2);
                if (bPrint) {
                    tmpLog("byteStr = " + byteStr2);
                }
                byteStr = byteStr2;
            } else {
                int iEnd = lenStr - i;
                String byteStr3 = strNewValue.substring(0, iEnd);
                if (bPrint) {
                    tmpLog("end, byteStr = " + byteStr3);
                }
                iStart = 0;
                byteStr = byteStr3;
            }
            try {
                int iVal = Integer.valueOf(byteStr, 16).intValue();
                byte bVal = (byte) iVal;
                if (bPrint) {
                    tmpLog("bVal[" + j + "] = " + ((int) bVal) + ", iVal = " + iVal + ", byteStr = " + byteStr);
                }
                bArry[j] = bVal;
                i += 2;
                j++;
                if (i >= lenStr) {
                    break;
                }
            } catch (ArithmeticException ex) {
                log("ArithmeticException, str = " + str);
                log("ArithmeticException: " + ex);
                ex.printStackTrace();
                return null;
            } catch (NumberFormatException ex2) {
                log("NumberFormatException, str = " + str);
                log("NumberFormatException: " + ex2);
                ex2.printStackTrace();
                return null;
            }
        } while (j < 20);
        while (j < 20) {
            bArry[j] = 0;
            j++;
        }
        ByteBuffer strBuf = ByteBuffer.wrap(bArry);
        strBuf.order(ByteOrder.nativeOrder());
        return strBuf;
    }

    public static String onGetStringByDateType(byte val, int uFlag, boolean bPrint, int hexOrDec, boolean fillHexZero) {
        String strVal;
        if (bPrint) {
            tmpLog("onGetStringByDateType, byte, val = " + ((int) val) + ", uFlag = " + uFlag + ", bPrint = " + bPrint + ", hexOrDec = " + hexOrDec + ", fillHexZero = " + fillHexZero);
        }
        if (1 == hexOrDec) {
            strVal = Integer.toHexString(val & EFS_VALUE_ASDIV_73841_DYNAMIC_SWITCH);
            if (1 == strVal.length() && fillHexZero) {
                strVal = "0" + strVal;
            }
            if (bPrint) {
                tmpLog("HEX, strVal = " + strVal);
            }
        } else {
            strVal = "" + ((int) val);
            if (1 == uFlag && val < 0) {
                byte lowVal = (byte) (val & 127);
                BigDecimal bigVal = BigDecimal.valueOf(lowVal).add(BigDecimal.valueOf(127L)).add(BigDecimal.valueOf(1L));
                strVal = bigVal.toString();
                if (bPrint) {
                    tmpLog("val < 0, new strVal = " + strVal);
                }
            }
        }
        if (strVal != null) {
            return strVal.toUpperCase();
        }
        return strVal;
    }

    public static byte onGetByteFromString(String str, int hexOrDec) {
        return onGetByteFromString(str, true, hexOrDec);
    }

    public static byte onGetByteFromString(String strIn, boolean bPrint, int hexOrDec) {
        if (bPrint) {
            tmpLog("onGetByteFromString, byte, strIn = " + strIn + ", bPrint = " + bPrint + ", hexOrDec = " + hexOrDec);
        }
        if (strIn == null) {
            tmpLog("strIn is null, return 0");
            return (byte) 0;
        }
        String str = strIn.trim();
        if ("".equals(str)) {
            tmpLog("str is empty, return 0");
            return (byte) 0;
        }
        byte val = 0;
        if (1 == hexOrDec) {
            ByteBuffer buf = onGetHexDataBufferFromString(str, bPrint);
            if (buf != null) {
                val = buf.get();
            }
        } else {
            BigDecimal data = new BigDecimal(str);
            val = data.byteValue();
        }
        if (bPrint) {
            tmpLog("val = " + ((int) val));
        }
        return val;
    }

    public static String onGetStringByDateType(short val, int uFlag, int hexOrDec) {
        String strVal;
        tmpLog("onGetStringByDateType, short, val = " + ((int) val) + ", uFlag = " + uFlag + ", hexOrDec = " + hexOrDec);
        if (1 == hexOrDec) {
            strVal = Integer.toHexString(65535 & val);
            tmpLog("HEX, strVal = " + strVal);
        } else {
            strVal = "" + ((int) val);
            if (1 == uFlag && val < 0) {
                short lowVal = (short) (val & Short.MAX_VALUE);
                BigDecimal bigVal = BigDecimal.valueOf(lowVal).add(BigDecimal.valueOf(32767L)).add(BigDecimal.valueOf(1L));
                strVal = bigVal.toString();
                tmpLog("val < 0, new strVal = " + strVal);
            }
        }
        if (strVal != null) {
            return strVal.toUpperCase();
        }
        return strVal;
    }

    public static short onGetShortFromString(String strIn, int hexOrDec) {
        tmpLog("onGetShortFromString, strIn = " + strIn + ", hexOrDec = " + hexOrDec);
        if (strIn == null) {
            tmpLog("strIn is null, return 0");
            return (short) 0;
        }
        String str = strIn.trim();
        if ("".equals(str)) {
            tmpLog("str is empty, return 0");
            return (short) 0;
        }
        short val = 0;
        if (1 == hexOrDec) {
            ByteBuffer buf = onGetHexDataBufferFromString(str, true);
            if (buf != null) {
                val = buf.getShort();
            }
        } else {
            BigDecimal data = new BigDecimal(str);
            val = data.shortValue();
        }
        tmpLog("val = " + ((int) val));
        return val;
    }

    public static String onGetStringByDateType(int val, int uFlag, int hexOrDec) {
        String strVal;
        tmpLog("onGetStringByDateType, int, val = " + val + ", uFlag = " + uFlag + ", hexOrDec = " + hexOrDec);
        if (1 == hexOrDec) {
            strVal = Integer.toHexString(val);
            tmpLog("HEX, strVal = " + strVal);
        } else {
            strVal = "" + val;
            if (1 == uFlag && val < 0) {
                int lowVal = val & Integer.MAX_VALUE;
                BigDecimal bigVal = BigDecimal.valueOf(lowVal).add(BigDecimal.valueOf(2147483647L)).add(BigDecimal.valueOf(1L));
                strVal = bigVal.toString();
                tmpLog("val < 0, new strVal = " + strVal);
            }
        }
        if (strVal != null) {
            return strVal.toUpperCase();
        }
        return strVal;
    }

    public static int onGetIntFromString(String strIn, int hexOrDec) {
        tmpLog("onGetIntFromString, strIn = " + strIn + ", hexOrDec = " + hexOrDec);
        if (strIn == null) {
            tmpLog("strIn is null, return 0");
            return 0;
        }
        String str = strIn.trim();
        if ("".equals(str)) {
            tmpLog("str is empty, return 0");
            return 0;
        }
        int val = 0;
        if (1 == hexOrDec) {
            ByteBuffer buf = onGetHexDataBufferFromString(str, true);
            if (buf != null) {
                val = buf.getInt();
            }
        } else {
            BigDecimal data = new BigDecimal(str);
            val = data.intValue();
        }
        tmpLog("val = " + val);
        return val;
    }

    public static String onGetStringByDateType(long val, int uFlag, int hexOrDec) {
        String strVal;
        tmpLog("onGetStringByDateType, long, val = " + val + ", uFlag = " + uFlag + ", hexOrDec = " + hexOrDec);
        if (1 == hexOrDec) {
            strVal = Long.toHexString(val);
            tmpLog("HEX, strVal = " + strVal);
        } else {
            strVal = "" + val;
            if (1 == uFlag && val < 0) {
                long lowVal = val & MODEM_STATS_DATA_INVALID_6;
                BigDecimal bigVal = BigDecimal.valueOf(lowVal).add(BigDecimal.valueOf(MODEM_STATS_DATA_INVALID_6)).add(BigDecimal.valueOf(1L));
                strVal = bigVal.toString();
                tmpLog("val < 0, new strVal = " + strVal);
            }
        }
        if (strVal != null) {
            return strVal.toUpperCase();
        }
        return strVal;
    }

    public static long onGetLongFromString(String strIn, int hexOrDec) {
        tmpLog("onGetLongFromString, strIn = " + strIn + ", hexOrDec = " + hexOrDec);
        if (strIn == null) {
            tmpLog("strIn is null, return 0");
            return 0L;
        }
        String str = strIn.trim();
        if ("".equals(str)) {
            tmpLog("str is empty, return 0");
            return 0L;
        }
        long val = 0;
        if (1 == hexOrDec) {
            ByteBuffer buf = onGetHexDataBufferFromString(str, true);
            if (buf != null) {
                val = buf.getLong();
            }
        } else {
            BigDecimal data = new BigDecimal(str);
            val = data.longValue();
        }
        tmpLog("val = " + val);
        return val;
    }

    public static String onGetStringByDateType(float val) {
        tmpLog("onGetStringByDateType, float, val = " + val);
        String strVal = "" + val;
        return strVal.toUpperCase();
    }

    public static float onGetFloatFromString(String strIn, int hexOrDec) {
        tmpLog("onGetFloatFromString, strIn = " + strIn + ", hexOrDec = " + hexOrDec);
        if (strIn == null) {
            tmpLog("strIn is null, return 0.0");
            return 0.0f;
        }
        String str = strIn.trim();
        if ("".equals(str)) {
            tmpLog("str is empty, return 0.0");
            return 0.0f;
        }
        BigDecimal data = new BigDecimal(str);
        float val = data.floatValue();
        tmpLog("val = " + val);
        return val;
    }

    public static String onGetStringByDateType(double val) {
        tmpLog("onGetStringByDateType, double, val = " + val);
        String strVal = "" + val;
        return strVal.toUpperCase();
    }

    public static double onGetDoubleFromString(String strIn, int hexOrDec) {
        tmpLog("onGetDoubleFromString, strIn = " + strIn + ", hexOrDec = " + hexOrDec);
        if (strIn == null) {
            tmpLog("strIn is null, return 0.0");
            return 0.0d;
        }
        String str = strIn.trim();
        if ("".equals(str)) {
            tmpLog("str is empty, return 0.0");
            return 0.0d;
        }
        BigDecimal data = new BigDecimal(str);
        double val = data.doubleValue();
        tmpLog("val = " + val);
        return val;
    }

    public static boolean onIsNumber(String filed) {
        if (filed == null) {
            log("onIsNumber, filed is null");
            return false;
        }
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(filed);
        if (!m.matches()) {
            tmpLog("onIsNumber, false");
            return false;
        }
        tmpLog("onIsNumber, true");
        return true;
    }

    public static boolean onIsBoolNumber(String filed) {
        if (filed == null) {
            log("onIsBoolNumber, filed is null");
            return false;
        }
        Pattern p = Pattern.compile("[0-1]*");
        Matcher m = p.matcher(filed);
        if (!m.matches()) {
            tmpLog("onIsBoolNumber, false");
            return false;
        }
        tmpLog("onIsBoolNumber, true");
        return true;
    }

    public static boolean onIsSignedNumber(String filed) {
        if (filed == null) {
            log("onIsSignedNumber, filed is null");
            return false;
        }
        Pattern p = Pattern.compile("[0-9-]*");
        Matcher m = p.matcher(filed);
        if (!m.matches()) {
            tmpLog("onIsSignedNumber, false");
            return false;
        }
        tmpLog("onIsSignedNumber, true");
        return true;
    }

    public static boolean onIsFloatNumber(String filed) {
        if (filed == null) {
            log("onIsFloatNumber, filed is null");
            return false;
        }
        Pattern p = Pattern.compile("[0-9-.]*");
        Matcher m = p.matcher(filed);
        if (!m.matches()) {
            tmpLog("onIsFloatNumber, false");
            return false;
        }
        tmpLog("onIsFloatNumber, true");
        return true;
    }

    public static String getCurProcessName(Context context) {
        return getCurProcessName(context, "Unknown");
    }

    public static String getCurProcessName(Context context, String defName) {
        if (mProcessName != null) {
            return mProcessName;
        }
        synchronized (GET_CUR_PROCESS_NAME_LOCK) {
            try {
                try {
                    if (context == null) {
                        Log.e(LOG_TAG, "getCurProcessName, context is null, defName: " + defName);
                        return defName;
                    }
                    int pid = Process.myPid();
                    ActivityManager mActivityManager = (ActivityManager) context.getSystemService("activity");
                    for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
                        if (appProcess.pid == pid) {
                            mProcessName = appProcess.processName;
                            Log.d(LOG_TAG, "getCurProcessName, context: " + context + ", pid: " + pid + ", defName: " + defName + ", mProcessName: " + mProcessName);
                            return mProcessName;
                        }
                    }
                    Log.e(LOG_TAG, "getCurProcessName, not find pid, context: " + context + ", pid: " + pid + ", defName: " + defName);
                    return defName;
                } catch (Exception e) {
                    Log.e(LOG_TAG, "getCurProcessName, Exception: " + e + ", context: " + context + ", defName: " + defName);
                    e.printStackTrace();
                    return defName;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static int getCurProcessPid() {
        if (-1 != mProcessPid) {
            return mProcessPid;
        }
        mProcessPid = Process.myPid();
        return mProcessPid;
    }

    public static boolean setSpVal(Context context, String spName, String paraName, String paraVal) {
        if (context == null) {
            log("getSpVal, context is null");
            return false;
        }
        if (spName == null) {
            log("getSpVal, spName is null");
            return false;
        }
        if (paraName == null) {
            log("getSpVal, paraName is null");
            return false;
        }
        if (paraVal == null) {
            log("getSpVal, paraVal is null");
            return false;
        }
        SharedPreferences sp = context.getSharedPreferences(spName, 0);
        if (sp == null) {
            log("getSpVal, sp is null");
            return false;
        }
        sp.edit().putString(paraName, paraVal).commit();
        return true;
    }

    public static String getSpVal(Context context, String spName, String paraName) {
        if (context == null) {
            log("getSpVal, context is null");
            return null;
        }
        if (spName == null) {
            log("getSpVal, spName is null");
            return null;
        }
        if (paraName == null) {
            log("getSpVal, paraName is null");
            return null;
        }
        SharedPreferences sp = context.getSharedPreferences(spName, 0);
        if (sp == null) {
            log("getSpVal, sp is null");
            return null;
        }
        return sp.getString(paraName, null);
    }

    public static SharedPreferences modemGetSharedPreferences(Context context, String spName) {
        if (context == null) {
            log("modemGetSharedPreferences, context is null");
            return null;
        }
        if (spName == null) {
            log("modemGetSharedPreferences, spName is null");
            return null;
        }
        try {
            return context.getSharedPreferences(spName, 0);
        } catch (Exception e) {
            log("modemGetSharedPreferences happen Exception, Exception info: " + e + ", context: " + context + ", spName: " + spName);
            e.printStackTrace();
            return null;
        }
    }

    public static boolean getStatsGetPrintFlag() {
        String str = SystemProperties.get("persist.vendor.radio.print_stats");
        log("getStatsGetPrintFlag, str = " + str);
        if ("1".equals(str)) {
            return true;
        }
        return false;
    }

    public static boolean isXringHal() {
        mXringHalFlag = SystemProperties.getBoolean(PROP_XRING_HAL_FLAG, false);
        if (mXringHalFlag) {
            log("Hal is Xring");
        } else {
            log("Hal is non Xring");
        }
        return mXringHalFlag;
    }

    public static int getModemHalChipId(OemHookAgent hook) {
        if (-1 != mHalChipId) {
            log("getModemHalChipId, mHalChipId = " + mHalChipId);
            return mHalChipId;
        }
        mHalChipId = 0;
        if (hook == null) {
            log("getModemHalChipId, hook is null");
            return mHalChipId;
        }
        ByteBuffer buf = hook.onHookCommonMsgSync(82);
        if (buf == null) {
            log("getModemHalChipId, buf is null");
            return mHalChipId;
        }
        int ret = buf.getInt();
        if (ret != 0) {
            log("getModemHalChipId, ret is invalid, ret = " + ret);
            return mHalChipId;
        }
        mHalChipId = buf.getInt();
        if (mHalChipId <= 0) {
            log("getModemHalChipId, mHalChipId is invalid, mHalChipId = " + mHalChipId);
            mHalChipId = 0;
            return mHalChipId;
        }
        log("getModemHalChipId, mHalChipId = " + mHalChipId);
        return mHalChipId;
    }

    public static int getModemChipId(OemHookAgent hook) {
        if (-1 != mBpChipId) {
            log("getModemChipId, mBpChipId = " + mBpChipId);
            return mBpChipId;
        }
        mBpChipId = 0;
        if (hook == null) {
            log("getModemChipId, hook is null");
            return mBpChipId;
        }
        mBpChipId = hook.getXiaomiModemBpChipId();
        log("getModemChipId, mBpChipId = " + mBpChipId);
        return mBpChipId;
    }

    public static int getManufacturerId(OemHookAgent hook) {
        if (mManufacturerId != 0) {
            log("getManufacturerId, mManufacturerId = " + mManufacturerId);
            return mManufacturerId;
        }
        if (hook == null) {
            log("getManufacturerId, hook is null, mManufacturerId = " + mManufacturerId);
            return mManufacturerId;
        }
        mManufacturerId = hook.getXiaomiManufacturerId();
        log("getManufacturerId, mManufacturerId = " + mManufacturerId);
        return mManufacturerId;
    }

    public static boolean isHalDecoupled(OemHookAgent hook) {
        if (hook == null) {
            log("isHalDecoupled, hook is null, mIsHalDecoupled = " + mIsHalDecoupled);
            return mIsHalDecoupled;
        }
        mIsHalDecoupled = hook.isHalDecoupled();
        log("isHalDecoupled, mIsHalDecoupled = " + mIsHalDecoupled);
        return mIsHalDecoupled;
    }

    public static int getOemProductFlag(OemHookAgent hook) {
        if (mOemProjectFlag != 0) {
            log("getOemProductFlag, mOemProjectFlag = " + mOemProjectFlag);
            return mOemProjectFlag;
        }
        if (hook == null) {
            log("getOemProductFlag, hook is null");
            return mOemProjectFlag;
        }
        if (true == hook.isOemProduct()) {
            mOemProjectFlag = 1;
        } else {
            mOemProjectFlag = 2;
        }
        log("getOemProductFlag, mOemProjectFlag = " + mOemProjectFlag);
        return mOemProjectFlag;
    }

    public static void onInitVerInfo() {
        log("onInitVerInfo");
        mDeviceName = getDeviceName();
        mDeviceHwInfo = getDeviceHwInfo();
        mDeviceModName = getDeviceSwInfo();
        mDeviceHwLevel = getDeviceHwLevel();
        mBuildRegion = getBuildRegion();
        mCertType = getCertType();
        mFactoryBuild = getFactoryBuild();
        mRootType = getRootType();
        mBuildVersion = getBuildVersion();
        mBuildTags = getBuildTags();
        mSdkVersion = getSdkVersion();
        mAndroidVersion = getAndroidVersion();
        mManufacturerId = getManufacturerId();
        mHalChipId = getHalChipId();
        mBpChipId = getBpChipId();
        mBuildProfile = getBuildProfile();
        mXringHalFlag = isXringHal();
    }

    public static void onInitVerInfo(OemHookAgent hook) {
        log("onInitVerInfo, hook = " + hook);
        if (hook == null) {
            log("onInitVerInfo, hook is null");
            return;
        }
        mHalChipId = getModemHalChipId(hook);
        mBpChipId = getModemChipId(hook);
        mBuildProfile = getBuildProfile(hook);
        mManufacturerId = getManufacturerId(hook);
        mOemProjectFlag = getOemProductFlag(hook);
        mIsHalDecoupled = isHalDecoupled(hook);
    }

    public static String getVerInfoWithName() {
        return "mDeviceName = " + mDeviceName + ", mManufacturerId = " + mManufacturerId + ", mHalChipId = " + mHalChipId + ", mBpChipId = " + mBpChipId + ", mDeviceHwInfo = " + mDeviceHwInfo + ", mDeviceModName = " + mDeviceModName + ", mDeviceHwLevel = " + mDeviceHwLevel + ", mBuildRegion = " + mBuildRegion + ", mBuildProfile = " + mBuildProfile + ", mCertType = " + mCertType + ", mFactoryBuild = " + mFactoryBuild + ", mRootType = " + mRootType + ", mOemProjectFlag = " + mOemProjectFlag + ", mBuildTags = " + mBuildTags + ", mSdkVersion = " + mSdkVersion + ", mAndroidVersion = " + mAndroidVersion + ", mBuildVersion = " + mBuildVersion + ", mXringHalFlag = " + mXringHalFlag + ", mIsHalDecoupled = " + mIsHalDecoupled;
    }

    public static String getVerInfoWithoutName() {
        return mDeviceName + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + mManufacturerId + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + mHalChipId + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + mBpChipId + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + mDeviceHwInfo + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + mDeviceModName + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + mDeviceHwLevel + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + mBuildRegion + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + mBuildProfile + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + mCertType + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + mFactoryBuild + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + mRootType + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + mOemProjectFlag + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + mBuildTags + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + mSdkVersion + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + mAndroidVersion + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + mBuildVersion + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + mXringHalFlag + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + mIsHalDecoupled;
    }

    public static boolean isSupporteSIM() {
        if (DEVICE_ESIM_SUPPORT_LIST.contains(getDeviceName()) && TextUtils.equals(getDeviceHwInfo(), "GL")) {
            return true;
        }
        return false;
    }

    public static String getDeviceName() {
        mDeviceName = SystemProperties.get("ro.product.device");
        return mDeviceName;
    }

    public static String getDeviceHwInfo() {
        mDeviceHwInfo = SystemProperties.get("ro.boot.hwc");
        return mDeviceHwInfo;
    }

    public static String getDeviceSwInfo() {
        mDeviceModName = SystemProperties.get("ro.product.mod_device");
        return mDeviceModName;
    }

    public static String getDeviceHwLevel() {
        mDeviceHwLevel = SystemProperties.get(PROPERTY_DEVICE_INFO_HW_LEVEL);
        return mDeviceHwLevel;
    }

    public static String getBuildRegion() {
        mBuildRegion = SystemProperties.get(PROP_MIUI_BUILD_REGION);
        return mBuildRegion;
    }

    public static String getCertType() {
        mCertType = SystemProperties.get(PROP_CERT_TYPE);
        return mCertType;
    }

    public static String getFactoryBuild() {
        mFactoryBuild = SystemProperties.get("ro.boot.factorybuild");
        return mFactoryBuild;
    }

    public static String getRootType() {
        mRootType = SystemProperties.get(PROP_ROOT_TYPE);
        return mRootType;
    }

    public static String getBuildVersion() {
        mBuildVersion = SystemProperties.get(PROP_BUILD_VERSION);
        return mBuildVersion;
    }

    public static String getBuildTags() {
        mBuildTags = SystemProperties.get(PROP_BUILD_TAGS);
        return mBuildTags;
    }

    public static int getSdkVersion() {
        mSdkVersion = Build.VERSION.SDK_INT;
        return mSdkVersion;
    }

    public static int getAndroidVersion() {
        mAndroidVersion = SystemProperties.getInt("ro.build.version.release", 0);
        return mAndroidVersion;
    }

    public static boolean isTestBuild() {
        String val = SystemProperties.get(PROP_BUILD_TAGS, PROP_BUILD_TAGS_TEST);
        boolean ret = true;
        if (TextUtils.equals(val, PROP_BUILD_TAGS_RELE)) {
            ret = false;
        }
        log("isTestBuild, ret = " + ret + ", val = " + val);
        return ret;
    }

    public static String getBuildProfile(OemHookAgent hook) {
        if (hook == null) {
            log("getBuildProfile, hook is null");
            return null;
        }
        mBuildProfile = hook.onHookPropGetSync(PROP_BUILD_PROFILE_VENDOR, "NULL");
        log("getBuildProfile, val = " + mBuildProfile);
        return mBuildProfile;
    }

    public static boolean isStableBuild() {
        String version = SystemProperties.get(PROP_BUILD_VERSION, "");
        String versionMiOs = SystemProperties.get(PROP_BUILD_VERSION_MI_OS, "");
        boolean ret = false;
        log("isStableBuild, version = " + version + ", versionMiOs = " + versionMiOs);
        if (!isTestBuild() && !isChinaCertBuild() && version != null && version.length() > 1) {
            ret = true;
        }
        log("isStableBuild, ret = " + ret);
        return ret;
    }

    public static boolean isMpHw() {
        String val = SystemProperties.get(PROPERTY_DEVICE_INFO_HW_LEVEL, "NULL");
        boolean ret = false;
        if (PROPERTY_DEVICE_INFO_HW_LEVEL_MP.equals(val)) {
            log("MP version");
            ret = true;
        }
        log("isMpHw, ret = " + ret + ", val = " + val);
        return ret;
    }

    public static boolean isRootBuild() {
        String val = SystemProperties.get(PROP_ROOT_TYPE, "0");
        boolean ret = false;
        if (TextUtils.equals(val, "1")) {
            ret = true;
        }
        log("isRootBuild, ret = " + ret + ", val = " + val);
        return ret;
    }

    public static boolean isFactoryBuild() {
        String val = SystemProperties.get("ro.boot.factorybuild", "0");
        boolean ret = false;
        if (TextUtils.equals(val, "1")) {
            ret = true;
        }
        log("isFactoryBuild, ret = " + ret + ", val = " + val);
        return ret;
    }

    public static boolean isChinaCertBuild() {
        String val = SystemProperties.get(PROP_CERT_TYPE, "none");
        boolean ret = false;
        if (TextUtils.equals(val, PROP_CERT_TYPE_CT) || TextUtils.equals(val, PROP_CERT_TYPE_CM) || TextUtils.equals(val, PROP_CERT_TYPE_CU)) {
            ret = true;
        }
        log("isChinaCertBuild, ret = " + ret + ", val = " + val);
        return ret;
    }

    public static boolean isChinaBuild() {
        String val = SystemProperties.get(PROP_MIUI_BUILD_REGION, "none");
        boolean ret = false;
        if (TextUtils.equals(val, PROP_MIUI_BUILD_REGION_CN)) {
            ret = true;
        }
        log("isChinaBuild, ret = " + ret + ", val = " + val);
        return ret;
    }

    public static boolean isChinaBuild(OemHookAgent hook) {
        boolean ret = false;
        if (hook == null) {
            log("isChinaBuild, hook is null");
            return false;
        }
        String val = hook.onHookPropGetSync(PROP_RADIO_BUILD_REGION_VENDOR, "none");
        if (TextUtils.equals(val, PROP_MIUI_BUILD_REGION_CN)) {
            ret = true;
        }
        log("isChinaBuild, ret = " + ret + ", val = " + val);
        return ret;
    }

    public static int getManufacturerId() {
        String str = SystemProperties.get(PROP_RADIO_MANUFACTURER_ID, "0");
        mManufacturerId = 0;
        if ("1".equals(str)) {
            mManufacturerId = 1;
        } else if ("2".equals(str)) {
            mManufacturerId = 2;
        } else if ("3".equals(str)) {
            mManufacturerId = 3;
        } else if ("4".equals(str)) {
            mManufacturerId = 4;
        } else if (PROP_RADIO_XIAOMI_MANUFACTURER_ID_ZHONGKE.equals(str)) {
            mManufacturerId = 5;
        }
        log("getManufacturerId, mManufacturerId = " + mManufacturerId);
        return mManufacturerId;
    }

    public static int getHalChipId() {
        String str = SystemProperties.get("ro.vendor.radio.hal_chip_id", "0");
        mHalChipId = getIntFromString(str, false);
        log("getHalChipId, mHalChipId = " + mHalChipId);
        return mHalChipId;
    }

    public static int getBpChipId() {
        String str = SystemProperties.get(PROP_RADIO_XIAOMI_BP_CHIP_ID, "0");
        mBpChipId = getIntFromString(str, false);
        log("getBpChipId, mBpChipId = " + mBpChipId);
        return mBpChipId;
    }

    public static String getBuildProfile() {
        mBuildProfile = SystemProperties.get(PROP_BUILD_PROFILE_VENDOR, "NULL");
        log("getBuildProfile, mBuildProfile = " + mBuildProfile);
        return mBuildProfile;
    }

    public static boolean propGet(String propName, String defaultValue, ResultReceiver rr) {
        return propGetEx(0, propName, defaultValue, rr);
    }

    public static boolean propGetEx(int sub, String propName, String defaultValue, ResultReceiver rr) {
        log("propGet, sub = " + sub + ", propName = " + propName + ", defaultValue = " + defaultValue + ", rr = " + rr);
        if (propName == null) {
            log("propName is null");
            return false;
        }
        if (defaultValue == null) {
            log("defaultValue is null");
            return false;
        }
        if (rr == null) {
            log("rr is null");
            return false;
        }
        int msg_len = propName.length() + 8 + defaultValue.length();
        byte[] reqBytes = new byte[msg_len + 1];
        ByteBuffer reqBuffer = getBuffer(reqBytes);
        if (reqBuffer == null) {
            log("reqBuffer is null, will not send this hook msg");
            return false;
        }
        reqBuffer.putInt(propName.length());
        reqBuffer.put(propName.getBytes());
        reqBuffer.putInt(defaultValue.length());
        reqBuffer.put(defaultValue.getBytes());
        boolean ret = XmTelephonyManagerInner.getDefault().sendCommMsg(sub, 1, reqBytes, rr);
        log("propGet, ret = " + ret);
        return ret;
    }

    public static boolean propSet(String propName, String propValue, ResultReceiver rr) {
        return propSetEx(0, propName, propValue, rr);
    }

    public static boolean propSetEx(int sub, String propName, String propValue, ResultReceiver rr) {
        log("propSet, sub = " + sub + ", propName = " + propName + ", propValue = " + propValue + ", rr = " + rr);
        if (propName == null) {
            log("propName is null");
            return false;
        }
        if (propValue == null) {
            log("propValue is null");
            return false;
        }
        if (rr == null) {
            log("rr is null");
            return false;
        }
        int msg_len = propName.length() + 8 + propValue.length();
        byte[] reqBytes = new byte[msg_len + 1];
        ByteBuffer reqBuffer = getBuffer(reqBytes);
        if (reqBuffer == null) {
            log("reqBuffer is null, will not send this hook msg");
            return false;
        }
        reqBuffer.putInt(propName.length());
        reqBuffer.put(propName.getBytes());
        reqBuffer.putInt(propValue.length());
        reqBuffer.put(propValue.getBytes());
        boolean ret = XmTelephonyManagerInner.getDefault().sendCommMsg(sub, 2, reqBytes, rr);
        log("propSet, ret = " + ret);
        return ret;
    }

    private static String getXmRadioCauseString(int cause) {
        if (cause == -170) {
            return "SUB_INVALID";
        }
        if (cause == -169) {
            return "NOT_CONFIG";
        }
        if (cause == -168) {
            return "INIT";
        }
        if (cause == 0) {
            return "NONE";
        }
        if (cause == 1) {
            return "RADIO_NOT_AVAILABLE";
        }
        if (cause == 2) {
            return "GENERIC_FAILURE";
        }
        if (cause == 3) {
            return "PASSWORD_INCORRECT";
        }
        if (cause == 4) {
            return "SIM_PIN2";
        }
        if (cause == 5) {
            return "SIM_PUK2";
        }
        if (cause == 6) {
            return "REQUEST_NOT_SUPPORTED";
        }
        if (cause == 7) {
            return "CANCELLED";
        }
        if (cause == 8) {
            return "OP_NOT_ALLOWED_DURING_VOICE_CALL";
        }
        if (cause == 9) {
            return "OP_NOT_ALLOWED_BEFORE_REG_TO_NW";
        }
        if (cause == 10) {
            return "SMS_SEND_FAIL_RETRY";
        }
        if (cause == 11) {
            return "SIM_ABSENT";
        }
        if (cause == 12) {
            return "SUBSCRIPTION_NOT_AVAILABLE";
        }
        if (cause == 13) {
            return "MODE_NOT_SUPPORTED";
        }
        if (cause == 14) {
            return "FDN_CHECK_FAILURE";
        }
        if (cause == 15) {
            return "ILLEGAL_SIM_OR_ME";
        }
        if (cause == 16) {
            return "MISSING_RESOURCE";
        }
        if (cause == 17) {
            return "NO_SUCH_ELEMENT";
        }
        if (cause == 18) {
            return "DIAL_MODIFIED_TO_USSD";
        }
        if (cause == 19) {
            return "DIAL_MODIFIED_TO_SS";
        }
        if (cause == 20) {
            return "DIAL_MODIFIED_TO_DIAL";
        }
        if (cause == 21) {
            return "USSD_MODIFIED_TO_DIAL";
        }
        if (cause == 22) {
            return "USSD_MODIFIED_TO_SS";
        }
        if (cause == 23) {
            return "USSD_MODIFIED_TO_USSD";
        }
        if (cause == 24) {
            return "SS_MODIFIED_TO_DIAL";
        }
        if (cause == 25) {
            return "SS_MODIFIED_TO_USSD";
        }
        if (cause == 26) {
            return "SUBSCRIPTION_NOT_SUPPORTED";
        }
        if (cause == 27) {
            return "SS_MODIFIED_TO_SS";
        }
        if (cause == 36) {
            return "LCE_NOT_SUPPORTED";
        }
        if (cause == 37) {
            return "NO_MEMORY";
        }
        if (cause == 38) {
            return "INTERNAL_ERR";
        }
        if (cause == 39) {
            return "SYSTEM_ERR";
        }
        if (cause == 40) {
            return "MODEM_ERR";
        }
        if (cause == 41) {
            return "INVALID_STATE";
        }
        if (cause == 42) {
            return "NO_RESOURCES";
        }
        if (cause == 43) {
            return "SIM_ERR";
        }
        if (cause == 44) {
            return "INVALID_ARGUMENTS";
        }
        if (cause == 45) {
            return "INVALID_SIM_STATE";
        }
        if (cause == 46) {
            return "INVALID_MODEM_STATE";
        }
        if (cause == 47) {
            return "INVALID_CALL_ID";
        }
        if (cause == 48) {
            return "NO_SMS_TO_ACK";
        }
        if (cause == 49) {
            return "NETWORK_ERR";
        }
        if (cause == 50) {
            return "REQUEST_RATE_LIMITED";
        }
        if (cause == 51) {
            return "SIM_BUSY";
        }
        if (cause == 52) {
            return "SIM_FULL";
        }
        if (cause == 53) {
            return "NETWORK_REJECT";
        }
        if (cause == 54) {
            return "OPERATION_NOT_ALLOWED";
        }
        if (cause == 55) {
            return "EMPTY_RECORD";
        }
        if (cause == 56) {
            return "INVALID_SMS_FORMAT";
        }
        if (cause == 57) {
            return "ENCODING_ERR";
        }
        if (cause == 58) {
            return "INVALID_SMSC_ADDRESS";
        }
        if (cause == 59) {
            return "NO_SUCH_ENTRY";
        }
        if (cause == 60) {
            return "NETWORK_NOT_READY";
        }
        if (cause == 61) {
            return "NOT_PROVISIONED";
        }
        if (cause == 62) {
            return "NO_SUBSCRIPTION";
        }
        if (cause == 63) {
            return "NO_NETWORK_FOUND";
        }
        if (cause == 64) {
            return "DEVICE_IN_USE";
        }
        if (cause == 65) {
            return "ABORTED";
        }
        return cause == 66 ? "INVALID_RESPONSE" : "UNKNOWN";
    }

    public static String getXmRadioCauseStringEx(int cause) {
        String str = "[" + cause + "]" + getXmRadioCauseString(cause);
        log("getXmRadioCauseStringEx, cause = " + cause + ", string = " + str);
        return str;
    }

    public static void onModemNtnLogSet(Context cnt) {
        log("onModemNtnLogSet, cnt = " + cnt);
        if (cnt == null) {
            log("cnt is null");
            showToast(cnt, "NTN LOG setting failed");
            return;
        }
        mHookAgent = OemHookAgent.getHook();
        if (mHookAgent == null) {
            log("mHookAgent is null");
            showToast(cnt, "NTN LOG setting failed");
        } else if (onModemNtnLogIsOpen()) {
            log("buf: " + mHookAgent.onHookPropSetSync(PROP_NTN_LOG_LEVEL, ""));
            showToast(cnt, "NTN LOG disable");
        } else {
            log("buf: " + mHookAgent.onHookPropSetSync(PROP_NTN_LOG_LEVEL, "2"));
            showToast(cnt, "NTN LOG enable");
        }
    }

    private static boolean onModemNtnLogIsOpen() {
        log("onModemNtnLogIsOpen");
        if (mHookAgent == null) {
            log("mHookAgent is null");
            return false;
        }
        String str = mHookAgent.onHookPropGetSync(PROP_NTN_LOG_LEVEL, PROP_NTN_LOG_LEVEL_DEFAULT);
        log("str: " + str);
        if ("2".equals(str)) {
            log("ntn log has been opened");
            return true;
        }
        log("ntn log has been closed");
        return false;
    }

    public static Map<String, Integer> getCommands(String fileFullPath) throws XmlPullParserException, IOException {
        log("PullParseXMLFromPath, fileFullPath = " + fileFullPath);
        Map<String, Integer> commands = new HashMap<>();
        if (fileFullPath == null) {
            log("fileFullPath is null");
            return null;
        }
        if (mContext == null) {
            log("mContext is null");
            return null;
        }
        try {
            InputStream inputStream = mContext.getResources().getAssets().open(fileFullPath);
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setInput(inputStream, "utf-8");
            for (int eventType = xmlPullParser.getEventType(); eventType != 1; eventType = xmlPullParser.next()) {
                switch (eventType) {
                    case 2:
                        if (xmlPullParser.getName().equals("ATItemData")) {
                            String currentcommand = xmlPullParser.getAttributeValue(null, "command");
                            String currentTime = xmlPullParser.getAttributeValue(null, "time");
                            commands.put(currentcommand.toLowerCase(), Integer.valueOf(Integer.parseInt(currentTime)));
                        }
                        break;
                }
            }
        } catch (Exception e) {
            log("Get exception while reading command list" + e);
            e.printStackTrace();
        }
        return commands;
    }

    public static boolean startHydraData(OemHookAgent hook) {
        log("startHydraData, hook: " + hook);
        if (hook == null) {
            log("hook is null");
            return false;
        }
        ByteBuffer bufRet = hook.onHookHydraStatsSetSync(getCurrentClassName(), 1);
        if (bufRet == null) {
            log("bufRet is null");
            return false;
        }
        int ret = bufRet.getInt();
        log("ret: " + ret);
        if (ret == 0) {
            return true;
        }
        log("startHydraData fail");
        return false;
    }

    public static boolean closeHydraData(OemHookAgent hook) {
        log("closeHydraData, hook: " + hook);
        if (hook != null) {
            ByteBuffer bufRet = hook.onHookHydraStatsSetSync(getCurrentClassName(), 0);
            if (bufRet == null) {
                log("bufRet is null");
                return false;
            }
            int ret = bufRet.getInt();
            log("ret: " + ret);
            if (ret != 0) {
                log("closeHydraData fail");
                return false;
            }
            return true;
        }
        log("hook is null");
        return false;
    }

    public static ByteBuffer getHydraData(OemHookAgent hook, int sub, int id) {
        tmpLog("getHydraData, sub = " + sub + ", id: " + id + ", hook: " + hook);
        if (hook == null) {
            log("getHydraData fail, sub = " + sub + ", id: " + id + ", hook: " + hook);
            return null;
        }
        ByteBuffer bufHook = getBuffer(12);
        if (bufHook == null) {
            log("getHydraData fail, sub = " + sub + ", id: " + id + ", bufHook: " + bufHook);
            return null;
        }
        bufHook.putInt(sub);
        bufHook.putInt(1);
        bufHook.putInt(id);
        ByteBuffer bufRet = hook.onHookCommonMsgSync(62, bufHook.array());
        if (bufRet == null) {
            log("getHydraData fail, sub = " + sub + ", id: " + id + ", bufRet: " + bufRet);
            return null;
        }
        int ret = bufRet.getInt();
        if (ret != 0) {
            log("getHydraData fail, sub = " + sub + ", id: " + id + ", ret: " + ret);
            return null;
        }
        int idNum = bufRet.getInt();
        if (1 != idNum) {
            log("getHydraData fail, sub = " + sub + ", id: " + id + ", idNum: " + idNum);
            return null;
        }
        int idRet = bufRet.getInt();
        if (id != idRet) {
            log("getHydraData fail, sub = " + sub + ", id: " + id + ", idRet: " + idRet);
            return null;
        }
        int valueLen = bufRet.getInt();
        if (valueLen <= 0 || valueLen > 700) {
            log("getHydraData fail, sub = " + sub + ", id: " + id + ", valueLen: " + valueLen);
            return null;
        }
        return bufRet;
    }

    public static String onGetModSwVer(OemHookAgent hook) {
        log("onGetModSwVer, hook = " + hook);
        if (hook == null) {
            log("hook is null");
            return null;
        }
        ByteBuffer byteBuf = hook.onHookModSwVerGetSync();
        if (byteBuf == null) {
            log("byteBuf is null");
            return null;
        }
        int ret = byteBuf.getInt();
        log("ret = " + ret);
        if (ret != 0) {
            log("0 != ret");
            return null;
        }
        String str = byteBufferToString(byteBuf);
        log("ModSwVer: " + str);
        return str;
    }

    public static String getCurrentClassName() {
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        String methodName = stacks[1].getClassName();
        return methodName;
    }

    public static String getCurrentMethodName() {
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        String methodName = stacks[1].getMethodName();
        return methodName;
    }

    public static int getDefaultPhoneId() {
        int newDefaultDataSub = SubscriptionManager.getDefaultDataSubscriptionId();
        int phoneId = SubscriptionManager.getPhoneId(newDefaultDataSub);
        return phoneId;
    }

    public static int getPhoneCount() {
        String mSimConfig = SystemProperties.get(PROPERTY_MULTI_SIM_CONFIG);
        if (mSimConfig.equals("dsds") || mSimConfig.equals("dsda")) {
            return 2;
        }
        if (mSimConfig.equals("tsts")) {
            return 3;
        }
        return 1;
    }

    public static int getPhoneCount(Context cnt) {
        if (cnt == null) {
            log("cnt is null");
            return 0;
        }
        TelephonyManager tm = (TelephonyManager) cnt.getSystemService("phone");
        return tm.getActiveModemCount();
    }

    public static boolean subMaskIsValid(int subMask) {
        return 1 == subMask || 2 == subMask || 3 == subMask;
    }

    public static boolean subMask0IsOn(int subMask) {
        return 1 == (subMask & 1);
    }

    public static boolean subMask1IsOn(int subMask) {
        return 2 == (subMask & 2);
    }

    public static boolean subMaskxIsOn(int subMask, int sub) {
        if (sub == 0) {
            return subMask0IsOn(subMask);
        }
        if (1 == sub) {
            return subMask1IsOn(subMask);
        }
        return false;
    }

    public static boolean subIsValid(int sub) {
        return sub == 0 || 1 == sub;
    }

    public static boolean subIsValid(int subMax, int sub) {
        if (subMax <= 0 || subMax > 2 || sub >= subMax || sub < 0) {
            log("sub is invalid, subMax = " + subMax + ", sub = " + sub);
            return false;
        }
        return true;
    }

    public static boolean subMaxIsValid(int subMax) {
        if (subMax <= 0 || subMax > 2) {
            log("subMax is invalid, subMax = " + subMax);
            return false;
        }
        return true;
    }

    public static Message obtainMessage(Handler hdl, int what, int arg1, int arg2, Object obj) {
        if (hdl == null) {
            log("obtainMessage, hdl is null, what = " + what + ", arg1 = " + arg1 + ", arg2 = " + arg2 + ", obj = " + obj);
            return null;
        }
        return hdl.obtainMessage(what, arg1, arg2, obj);
    }

    public static Message obtainMessage(Handler hdl, int what, int arg1, int arg2) {
        if (hdl == null) {
            log("obtainMessage, hdl is null, what = " + what + ", arg1 = " + arg1 + ", arg2 = " + arg2);
            return null;
        }
        return hdl.obtainMessage(what, arg1, arg2);
    }

    public static Message obtainMessage(Handler hdl, int what, Object obj) {
        if (hdl == null) {
            log("obtainMessage, hdl is null, what = " + what + ", obj = " + obj);
            return null;
        }
        return hdl.obtainMessage(what, obj);
    }

    public static Message obtainMessage(Handler hdl, int what) {
        if (hdl == null) {
            log("obtainMessage, hdl is null, what = " + what);
            return null;
        }
        return hdl.obtainMessage(what);
    }

    public static Message obtainMessage(Handler hdl) {
        if (hdl == null) {
            log("obtainMessage, hdl is null");
            return null;
        }
        return hdl.obtainMessage();
    }

    public static void onSendMsg(Handler hdl, int msg_id) {
        if (hdl == null) {
            log("onSendMsg, hdl is null, hdl = " + hdl + ", msg_id = " + msg_id);
        } else {
            hdl.sendEmptyMessage(msg_id);
        }
    }

    public static void onSendMsg(Handler hdl, Message msg) {
        if (hdl == null) {
            log("onSendMsg, hdl is null, hdl = " + hdl + ", msg = " + msg);
        } else if (msg == null) {
            log("onSendMsg, msg is null, hdl = " + hdl + ", msg = " + msg);
        } else {
            hdl.sendMessage(msg);
        }
    }

    public static void onSendMsg(Handler hdl, int msg_id, long delayMillis) {
        if (hdl == null) {
            log("onSendMsg, hdl is null, hdl = " + hdl + ", msg_id = " + msg_id + ", delayMillis = " + delayMillis);
        } else {
            hdl.sendEmptyMessageDelayed(msg_id, delayMillis);
        }
    }

    public static void onSendMsgEx(Handler hdl, int msg_id, long delayMillis) {
        if (hdl == null) {
            log("onSendMsgEx, hdl is null, hdl = " + hdl + ", msg_id = " + msg_id + ", delayMillis = " + delayMillis);
        } else {
            onRemoveMsg(hdl, msg_id);
            hdl.sendEmptyMessageDelayed(msg_id, delayMillis);
        }
    }

    public static void onSendMsg(Handler hdl, Message msg, long delayMillis) {
        if (hdl == null) {
            log("onSendMsg, hdl is null, hdl = " + hdl + ", msg = " + msg + ", delayMillis = " + delayMillis);
        } else if (msg == null) {
            log("onSendMsg, msg is null, hdl = " + hdl + ", msg = " + msg + ", delayMillis = " + delayMillis);
        } else {
            hdl.sendMessageDelayed(msg, delayMillis);
        }
    }

    public static void onRemoveMsg(Handler hdl, int msg_id) {
        if (hdl == null) {
            log("onRemoveMsg, hdl is null, hdl = " + hdl + ", msg_id = " + msg_id);
        } else {
            hdl.removeMessages(msg_id);
        }
    }

    public static void dumpLteBandpref(OemHookAgent hook, PrintWriter pw) {
        if (pw == null) {
            log("dumpLteBandpref, pw is null");
            return;
        }
        String dumpInfoSub0 = "LTE user bandpref 1_64(SUB0): inactive";
        String dumpInfoSub1 = "LTE user bandpref 1_64(SUB1): inactive";
        if (hook == null) {
            log("dumpLteBandpref, hook is null");
        } else {
            try {
                ByteBuffer buf = hook.onHookEfsOptSync(0, "/nv/item_files/modem/mmode/lte_bandpref", 4);
                if (buf == null) {
                    log("dumpLteBandpref, buf is null, SUB0");
                } else {
                    int ret = buf.getInt();
                    int len = buf.getInt();
                    if (ret != 0) {
                        log("dumpLteBandpref, ret is invalid, can not read efs for SUB0");
                    } else if (8 != len) {
                        log("dumpLteBandpref, len is invalid, can not read efs for SUB0");
                    } else {
                        byte[] bytes = new byte[8];
                        buf.get(bytes);
                        dumpInfoSub0 = "LTE user bandpref 1_64(SUB0): 0x" + getString(bytes, 4, " ");
                        ByteBuffer buf2 = hook.onHookEfsOptSync(1, "/nv/item_files/modem/mmode/lte_bandpref", 4);
                        if (buf2 == null) {
                            log("dumpLteBandpref, buf is null, SUB_1");
                        } else {
                            int ret2 = buf2.getInt();
                            int len2 = buf2.getInt();
                            if (ret2 != 0) {
                                log("dumpLteBandpref, ret is invalid, can not read efs for SUB_1");
                            } else if (8 != len2) {
                                log("dumpLteBandpref, len is invalid, can not read efs for SUB_1");
                            } else {
                                byte[] bytes2 = new byte[8];
                                buf2.get(bytes2);
                                dumpInfoSub1 = "LTE user bandpref 1_64(SUB_1): 0x" + getString(bytes2, 4, " ");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log("dumpLteBandpref, Happen Exception, Exception info: " + e);
                e.printStackTrace();
            }
        }
        dump(pw, "------ Modem lte_bandpref dump start ------");
        dump(pw, dumpInfoSub0);
        dump(pw, dumpInfoSub1);
        dump(pw, "------ Modem lte_bandpref dump end ------");
    }

    public static String getComplexItemStringForRffeScan(ByteBuffer buffer, int hexOrDec) {
        log("getComplexItemStringForRffeScan, buffer = " + buffer);
        if (buffer == null) {
            log("getComplexItemStringForRffeScan, buffer is null");
            return "Data Buffer is empty";
        }
        String dataString = "Header version: " + onGetStringByDateType(getInt(buffer, 0), 1, hexOrDec) + "\n";
        String dataString2 = ((dataString + "Header size: " + onGetStringByDateType(getInt(buffer, 0), 1, hexOrDec) + "\n") + "Body size: " + onGetStringByDateType(getInt(buffer, 0), 1, hexOrDec) + "\n") + "HWID: " + onGetStringByDateType(getInt(buffer, 0), 1, hexOrDec) + "\n";
        int NumDevices = getInt(buffer, 0);
        String dataString3 = ((dataString2 + "Num Devices: " + onGetStringByDateType(NumDevices, 1, hexOrDec) + "\n") + "Num Devices present: " + onGetStringByDateType(getInt(buffer, 0), 1, hexOrDec) + "\n") + "Error Code: " + onGetStringByDateType(getInt(buffer, 0), 1, hexOrDec) + "\n";
        for (int i = 0; i < NumDevices; i++) {
            String dataString4 = (((((((((dataString3 + "RF Device ID[" + i + "]: " + onGetStringByDateType(getInt(buffer, 0), 1, hexOrDec) + "\n") + "Phy Dev Instance[" + i + "]: " + onGetStringByDateType(getInt(buffer, 0), 1, hexOrDec) + "\n") + "Alt Part[" + i + "]: " + onGetStringByDateType(getInt(buffer, 0), 1, hexOrDec) + "\n") + "Bus ID[" + i + "]: " + onGetStringByDateType(getInt(buffer, 0), 1, hexOrDec) + "\n") + "Mfg ID[" + i + "]: " + onGetStringByDateType(getInt(buffer, 0), 1, hexOrDec) + "\n") + "Prod ID[" + i + "]: " + onGetStringByDateType(getInt(buffer, 0), 1, hexOrDec) + "\n") + "Prod Revision[" + i + "]: " + onGetStringByDateType(getInt(buffer, 0), 1, hexOrDec) + "\n") + "Default USID[" + i + "]: " + onGetStringByDateType(getInt(buffer, 0), 1, hexOrDec) + "\n") + "Default USID[" + i + "]: " + onGetStringByDateType(getInt(buffer, 0), 1, hexOrDec) + "\n") + "Assigned USID[" + i + "]: " + onGetStringByDateType(getInt(buffer, 0), 1, hexOrDec) + "\n";
            int Status = getInt(buffer, 0);
            if (Status == 0) {
                dataString3 = dataString4 + "Status[" + i + "]: (" + Status + ")RFC_DEVICE_PRESENT\n";
            } else if (1 == Status) {
                dataString3 = dataString4 + "Status[" + i + "]: (" + Status + ")RFC_DEVICE_MISSING_NON_FATAL\n";
            } else if (2 == Status) {
                dataString3 = dataString4 + "Status[" + i + "]: (" + Status + ")RFC_DEVICE_MISSING_FATAL\n";
            } else {
                dataString3 = 3 == Status ? dataString4 + "Status[" + i + "]: (" + Status + ")RFC_DEVICE_SCAN_INCOMPLETE\n" : dataString4 + "Status[" + i + "]: (" + Status + ")Unkown\n";
            }
        }
        return dataString3;
    }

    public static void dumpRffeScanResult(OemHookAgent hook, PrintWriter pw) {
        if (pw == null) {
            log("dumpRffeScanResult, pw is null");
            return;
        }
        String dumpStr = "Empty dump";
        if (hook == null) {
            log("dumpRffeScanResult, hook is null");
        } else {
            try {
                ByteBuffer buf = hook.onHookBigEfsReadSync(0, "/rfc/rffe_scan/rffe_scan.dat");
                if (buf == null) {
                    log("dumpRffeScanResult, buf is null");
                } else {
                    int ret = buf.getInt();
                    int len = buf.getInt();
                    log("dumpRffeScanResult, read config, ret = " + ret + ", len = " + len);
                    if (len > 0 && ret == 0) {
                        byte[] bytes = new byte[len];
                        buf.get(bytes);
                        dumpStr = getComplexItemStringForRffeScan(getBuffer(bytes), 0);
                    } else {
                        log("dumpRffeScanResult, maybe is empty");
                    }
                }
            } catch (Exception e) {
                log("dumpRffeScanResult, Happen Exception, Exception info: " + e);
                e.printStackTrace();
            }
        }
        dump(pw, "------ RF rffe_scan dump start ------");
        dump(pw, dumpStr);
        dump(pw, "------ RF rffe_scan dump end ------");
    }

    public static boolean onSendDiagCmdForRfdeviceReport(OemHookAgent hook) {
        log("onSendDiagCmdForRfdeviceReport");
        if (hook == null) {
            log("onSendDiagCmdForRfdeviceReport, hook is null");
            return false;
        }
        byte[] diagCmd = {75, 11, 20, 0, 16, 4, 13, 0, 0, 0, 0};
        ByteBuffer buf = hook.onHookDiagSendSync(diagCmd);
        SystemClock.sleep(50L);
        if (buf == null) {
            log("onSendDiagCmdForRfdeviceReport, onHookDiagSendSync failed");
            return false;
        }
        return true;
    }

    public static void dumpRfdeviceReportResult(OemHookAgent hook, PrintWriter pw) {
        if (pw == null) {
            log("dumpRfdeviceReportResult, pw is null");
            return;
        }
        String dumpStr = "Empty dump";
        if (hook == null) {
            log("dumpRfdeviceReportResult, hook is null");
        } else if (!onSendDiagCmdForRfdeviceReport(hook)) {
            log("dumpRfdeviceReportResult, onSendDiagCmdForRfdeviceReport failed");
        } else {
            try {
                ByteBuffer buf = hook.onHookBigEfsReadSync(0, "/rfc/rfdevice_report.dat");
                if (buf == null) {
                    log("dumpRfdeviceReportResult, buf is null");
                } else {
                    int ret = buf.getInt();
                    int len = buf.getInt();
                    log("dumpRfdeviceReportResult, read config, ret = " + ret + ", len = " + len);
                    if (len > 0 && ret == 0) {
                        byte[] bytes = new byte[len];
                        buf.get(bytes);
                        dumpStr = byteBufferToString(getBuffer(bytes));
                    } else {
                        log("dumpRfdeviceReportResult, maybe is empty");
                    }
                }
            } catch (Exception e) {
                log("dumpRfdeviceReportResult, Happen Exception, Exception info: " + e);
                e.printStackTrace();
            }
        }
        dump(pw, "------ RF rfdevice_report dump start ------");
        dump(pw, dumpStr);
        dump(pw, "------ RF rfdevice_report dump end ------");
    }

    public static List<String> getKeyStringListFromHashMap(HashMap<String, List<String>> mapFilterList) {
        if (mapFilterList == null) {
            log("getKeyStringListFromHashMap, mapFilterList is null");
            return null;
        }
        return new ArrayList(mapFilterList.keySet());
    }

    public static void tmpLog(String msg) {
        if (mTmpLogPrintFlag) {
            log(msg);
        }
    }

    public static void log(String msg) {
        Log.d("ModemUtils(" + getCurProcessName(mContext) + ")", msg);
    }

    public static void logd(String logTag, String msg) {
        Log.d(logTag + "(" + getCurProcessName(mContext) + ")", msg);
    }

    public static void logi(String logTag, String msg) {
        Log.i(logTag + "(" + getCurProcessName(mContext) + ")", msg);
    }

    public static void logv(String logTag, String msg) {
        Log.v(logTag + "(" + getCurProcessName(mContext) + ")", msg);
    }

    public static void logw(String logTag, String msg) {
        Log.w(logTag + "(" + getCurProcessName(mContext) + ")", msg);
    }

    public static void loge(String logTag, String msg) {
        Log.e(logTag + "(" + getCurProcessName(mContext) + ")", msg);
    }

    public static void dump(String msg) {
        Log.d("MODEM_DUMP(" + getCurProcessName(mContext) + ")", msg);
    }

    public static void dump(PrintWriter pw, String msg) {
        dump(msg);
        if (pw != null) {
            pw.println(msg);
        } else {
            log("pw is null, println do nothing");
        }
    }

    public static String getObjectTag(Object obj) {
        if (obj == null) {
            return "null";
        }
        String name = obj.getClass().getName();
        if (Proxy.isProxyClass(obj.getClass())) {
            try {
                InvocationHandler h = Proxy.getInvocationHandler(obj);
                name = h.getClass().getName();
            } catch (Exception e) {
            }
        }
        return name + "@" + Integer.toHexString(System.identityHashCode(obj));
    }

    public static String getListSummary(List<?> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(getObjectTag(list.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }

    public static String getObjectDetail(Object obj, boolean includeStack) {
        if (obj == null) {
            log("getObjectDetail: obj is null");
            return "null";
        }
        String detail = "class=" + obj.getClass().getName() + ", identity=" + System.identityHashCode(obj);
        if (Proxy.isProxyClass(obj.getClass())) {
            try {
                InvocationHandler handler = Proxy.getInvocationHandler(obj);
                detail = detail + ", proxyHandler=" + handler + ", handlerClass=" + handler.getClass().getName();
            } catch (Exception e) {
                log("getObjectDetail: getInvocationHandler failed, e=" + e);
                detail = detail + ", proxyHandler=error:" + e;
            }
        }
        if (includeStack) {
            return detail + ", caller=" + Log.getStackTraceString(new Throwable()).replace("\n", " | ");
        }
        return detail;
    }

    public static void dumpCallerStacktrace(Context context, String reason, String fileName) {
        dumpCallerStacktrace(context, reason, fileName, 20);
    }

    public static void dumpCallerStacktrace(Context context, String reason, String fileName, int maxCount) {
        List<String> snapshot;
        Object fileLock;
        String str;
        Throwable t = new Throwable(reason);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        pw.flush();
        String stacktrace = sw.toString();
        log(reason + " caller stacktrace:\n" + stacktrace);
        if (context == null) {
            Log.e(LOG_TAG, "dumpCallerStacktrace: context is null, skip file dump");
            return;
        }
        if (maxCount <= 0) {
            log("dumpCallerStacktrace: maxCount=" + maxCount + ", skip file dump");
            return;
        }
        String entry = "=== " + reason + " @ " + SystemClock.elapsedRealtime() + "ms ===\n" + stacktrace + "\n";
        synchronized (sDumpStacktraceRingMap) {
            LinkedList<String> ring = sDumpStacktraceRingMap.get(fileName);
            if (ring == null) {
                ring = new LinkedList<>();
                sDumpStacktraceRingMap.put(fileName, ring);
            }
            ring.addLast(entry);
            while (ring.size() > maxCount) {
                ring.removeFirst();
            }
            snapshot = new ArrayList<>(ring);
            fileLock = sDumpStacktraceLockMap.get(fileName);
            if (fileLock == null) {
                fileLock = new Object();
                sDumpStacktraceLockMap.put(fileName, fileLock);
            }
        }
        synchronized (fileLock) {
            FileWriter fw = null;
            try {
                try {
                    File file = new File(context.getFilesDir(), fileName);
                    fw = new FileWriter(file, false);
                    for (String e : snapshot) {
                        fw.write(e);
                    }
                    log("stacktrace saved to: " + file.getAbsolutePath() + ", entries=" + snapshot.size() + "/" + maxCount);
                    try {
                        fw.close();
                    } catch (IOException e2) {
                        str = "failed to close FileWriter: " + e2;
                        log(str);
                    }
                } catch (IOException e3) {
                    log("failed to save stacktrace for " + reason + ": " + e3);
                    if (fw != null) {
                        try {
                            fw.close();
                        } catch (IOException e4) {
                            str = "failed to close FileWriter: " + e4;
                            log(str);
                        }
                    }
                }
            } catch (Throwable e5) {
                if (fw != null) {
                    try {
                        fw.close();
                    } catch (IOException e6) {
                        log("failed to close FileWriter: " + e6);
                    }
                }
                throw e5;
            }
        }
    }
}
