package miuix.pickerwidget.date;

import android.content.Context;
import android.text.format.DateFormat;
import com.android.services.telephony.relay.network.SmartNetworkRelayScore;
import com.xiaomi.internal.telephony.DynamicSarService;
import java.util.TimeZone;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;
import miuix.core.util.Pools;
import miuix.pickerwidget.R$string;

/* JADX INFO: loaded from: classes2.dex */
public abstract class DateUtils {
    private static final Pools.Pool CALENDAR_POOL = Pools.createSoftReferencePool(new Pools.Manager() { // from class: miuix.pickerwidget.date.DateUtils.1
        @Override // miuix.core.util.Pools.Manager
        public Calendar createInstance() {
            return new Calendar();
        }
    }, 1);

    public static String formatDateTime(Context context, long j, int i) {
        StringBuilder sb = (StringBuilder) Pools.getStringBuilderPool().acquire();
        String string = formatDateTime(context, sb, j, i, null).toString();
        Pools.getStringBuilderPool().release(sb);
        return string;
    }

    public static StringBuilder formatDateTime(Context context, StringBuilder sb, long j, int i, TimeZone timeZone) {
        if ((i & 16) == 0 && (i & 32) == 0) {
            i |= DateFormat.is24HourFormat(context) ? 32 : 16;
        }
        String string = context.getString(getFormatResId(i));
        StringBuilder sb2 = (StringBuilder) Pools.getStringBuilderPool().acquire();
        Calendar calendar = (Calendar) CALENDAR_POOL.acquire();
        calendar.setTimeZone(timeZone);
        calendar.setTimeInMillis(j);
        int length = string.length();
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = string.charAt(i2);
            if (cCharAt == 'D') {
                sb2.append(context.getString(getDatePatternResId(i)));
            } else if (cCharAt == 'T') {
                sb2.append(context.getString(getTimePatternResId(calendar, i)));
            } else if (cCharAt == 'W') {
                sb2.append(context.getString(getWeekdayPatternResId(i)));
            } else {
                sb2.append(cCharAt);
            }
        }
        calendar.format(context, sb, sb2);
        Pools.getStringBuilderPool().release(sb2);
        CALENDAR_POOL.release(calendar);
        return sb;
    }

    /* JADX WARN: Code duplicated, block: B:21:0x003b A[PHI: r0
  0x003b: PHI (r0v8 int) = (r0v6 int), (r0v7 int), (r0v7 int), (r0v6 int) binds: [B:15:0x0028, B:17:0x0032, B:19:0x0036, B:13:0x0024] A[DONT_GENERATE, DONT_INLINE]] */
    private static int getTimePatternResId(Calendar calendar, int i) {
        if ((i & 16384) == 16384 && (((i & 1) != 1 || calendar.get(22) == 0) && (i & 14) != 0)) {
            int i2 = i & (-2);
            if (((i & 2) != 2 || calendar.get(21) == 0) && (i & 12) != 0) {
                i2 = i & (-4);
                if (calendar.get(20) != 0 || (i & 8) == 0) {
                    i = i2;
                } else {
                    i &= -8;
                }
            } else {
                i = i2;
            }
        }
        if ((i & 8) != 8) {
            if ((i & 4) == 4) {
                if ((i & 2) != 2) {
                    return R$string.fmt_time_minute;
                }
                if ((i & 1) == 1) {
                    return R$string.fmt_time_minute_second_millis;
                }
                return R$string.fmt_time_minute_second;
            }
            if ((i & 2) == 2) {
                if ((i & 1) == 1) {
                    return R$string.fmt_time_second_millis;
                }
                return R$string.fmt_time_second;
            }
            if ((i & 1) == 1) {
                return R$string.fmt_time_millis;
            }
            Folme$$ExternalSyntheticBUOutline0.m("no any time date");
            return 0;
        }
        if ((i & 16) != 16) {
            if ((i & 4) != 4) {
                return R$string.fmt_time_24hour;
            }
            if ((i & 2) != 2) {
                return R$string.fmt_time_24hour_minute;
            }
            if ((i & 1) == 1) {
                return R$string.fmt_time_24hour_minute_second_millis;
            }
            return R$string.fmt_time_24hour_minute_second;
        }
        if ((i & 64) == 64) {
            if ((i & 4) != 4) {
                return R$string.fmt_time_12hour;
            }
            if ((i & 2) != 2) {
                return R$string.fmt_time_12hour_minute;
            }
            if ((i & 1) == 1) {
                return R$string.fmt_time_12hour_minute_second_millis;
            }
            return R$string.fmt_time_12hour_minute_second;
        }
        if ((i & 4) != 4) {
            return R$string.fmt_time_12hour_pm;
        }
        if ((i & 2) != 2) {
            return R$string.fmt_time_12hour_minute_pm;
        }
        if ((i & 1) == 1) {
            return R$string.fmt_time_12hour_minute_second_millis_pm;
        }
        return R$string.fmt_time_12hour_minute_second_pm;
    }

    private static int getDatePatternResId(int i) {
        if ((i & 32768) == 32768) {
            if ((i & 512) == 512) {
                if ((i & DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER) != 256) {
                    return R$string.fmt_date_numeric_year;
                }
                if ((i & DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY) == 128) {
                    return R$string.fmt_date_numeric_year_month_day;
                }
                return R$string.fmt_date_numeric_year_month;
            }
            if ((i & DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER) == 256) {
                if ((i & DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY) == 128) {
                    return R$string.fmt_date_numeric_month_day;
                }
                return R$string.fmt_date_numeric_month;
            }
            if ((i & DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY) == 128) {
                return R$string.fmt_date_numeric_day;
            }
            Folme$$ExternalSyntheticBUOutline0.m("no any time date");
            return 0;
        }
        if ((i & 4096) == 4096) {
            if ((i & 512) == 512) {
                if ((i & DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER) != 256) {
                    return R$string.fmt_date_year;
                }
                if ((i & DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY) == 128) {
                    return R$string.fmt_date_short_year_month_day;
                }
                return R$string.fmt_date_short_year_month;
            }
            if ((i & DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER) == 256) {
                if ((i & DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY) == 128) {
                    return R$string.fmt_date_short_month_day;
                }
                return R$string.fmt_date_short_month;
            }
            if ((i & DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY) == 128) {
                return R$string.fmt_date_day;
            }
            Folme$$ExternalSyntheticBUOutline0.m("no any time date");
            return 0;
        }
        if ((i & 512) == 512) {
            if ((i & DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER) != 256) {
                return R$string.fmt_date_year;
            }
            if ((i & DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY) == 128) {
                return R$string.fmt_date_long_year_month_day;
            }
            return R$string.fmt_date_long_year_month;
        }
        if ((i & DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER) == 256) {
            if ((i & DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY) == 128) {
                return R$string.fmt_date_long_month_day;
            }
            return R$string.fmt_date_long_month;
        }
        if ((i & DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY) == 128) {
            return R$string.fmt_date_day;
        }
        Folme$$ExternalSyntheticBUOutline0.m("no any time date");
        return 0;
    }

    private static int getWeekdayPatternResId(int i) {
        if ((i & 8192) == 8192) {
            return R$string.fmt_weekday_short;
        }
        return R$string.fmt_weekday_long;
    }

    private static int getFormatResId(int i) {
        if ((i & SmartNetworkRelayScore.TRAFFIC_UNIT) == 1024) {
            if ((i & 896) != 0) {
                if ((i & 15) != 0) {
                    return (i & 2048) == 2048 ? R$string.fmt_weekday_date_time_timezone : R$string.fmt_weekday_date_time;
                }
                return (i & 2048) == 2048 ? R$string.fmt_weekday_date_timezone : R$string.fmt_weekday_date;
            }
            if ((i & 15) != 0) {
                return (i & 2048) == 2048 ? R$string.fmt_weekday_time_timezone : R$string.fmt_weekday_time;
            }
            return (i & 2048) == 2048 ? R$string.fmt_weekday_timezone : R$string.fmt_weekday;
        }
        if ((i & 896) != 0) {
            if ((i & 15) != 0) {
                return (i & 2048) == 2048 ? R$string.fmt_date_time_timezone : R$string.fmt_date_time;
            }
            return (i & 2048) == 2048 ? R$string.fmt_date_timezone : R$string.fmt_date;
        }
        if ((i & 15) != 0) {
            return (i & 2048) == 2048 ? R$string.fmt_time_timezone : R$string.fmt_time;
        }
        return (i & 2048) == 2048 ? R$string.fmt_timezone : R$string.empty;
    }
}
