package miuix.pickerwidget.widget.Calendar;

import miuix.pickerwidget.date.Calendar;

/* JADX INFO: loaded from: classes2.dex */
abstract class SpecialHolidayParser {
    static String parseSpecialHoliday(int i, int i2, int i3, Calendar calendar) {
        int i4 = calendar.get(5);
        int i5 = calendar.get(6);
        if (i4 == 4 && i == 1 && i2 == 6) {
            return "母亲节";
        }
        if (i4 == 5 && i == 2 && i2 == 6) {
            return "父亲节";
        }
        if (i4 == 10 && i2 == 3) {
            if ((i3 < 4 ? 3 : 4) == i) {
                return "感恩节";
            }
        }
        if (i5 != 11) {
            return null;
        }
        if (calendar.get(10) == calendar.getActualMaximum(10)) {
            return "除夕";
        }
        return null;
    }
}
