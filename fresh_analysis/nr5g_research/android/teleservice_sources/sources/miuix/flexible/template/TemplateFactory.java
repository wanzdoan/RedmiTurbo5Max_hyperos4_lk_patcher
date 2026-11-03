package miuix.flexible.template;

import android.content.Context;
import android.text.TextUtils;
import com.ot.pubsub.a.a;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import miuix.preference.TextPreference$$ExternalSyntheticBUOutline0;
import org.tensorflow.lite.DataType$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes2.dex */
public abstract class TemplateFactory {
    private static final HashMap TEMPLATE_MAP;

    static {
        HashMap map = new HashMap();
        TEMPLATE_MAP = map;
        map.put("mark", SimpleMarkTemplate.class);
        map.put(a.m, MessageTemplate.class);
        map.put("standard", StandardTemplate.class);
        map.put("standard_button", StandardButtonTemplate.class);
        map.put("notification", NotificationTemplate.class);
        map.put("head_button", HeadButtonTemplate.class);
        map.put("infoOperation", InfoOperationMarkTemplate.class);
        map.put("summary", SummaryMarkTemplate.class);
        map.put("simpleItem", SimpleItemMarkTemplate.class);
        map.put("settingInfo", SettingInfoMarkTemplate.class);
        map.put("ternary", TernaryLayoutTemplate.class);
    }

    public static void registerTemplate(String str, Class cls) {
        HashMap map = TEMPLATE_MAP;
        if (map.containsKey(str)) {
            DataType$$ExternalSyntheticBUOutline0.m("Template name '", str, "' has been registered! Please do not register repeatedly.");
        } else {
            map.put(str, cls);
        }
    }

    public static IHyperCellTemplate get(Context context, String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                Class clsAsSubclass = (Class) TEMPLATE_MAP.get(str);
                if (clsAsSubclass == null) {
                    clsAsSubclass = context.getClassLoader().loadClass(str).asSubclass(IHyperCellTemplate.class);
                }
                Class[] clsArr = new Class[0];
                Constructor constructor = clsAsSubclass.getConstructor(null);
                constructor.setAccessible(true);
                return (IHyperCellTemplate) constructor.newInstance(null);
            } catch (ClassNotFoundException e) {
                TextPreference$$ExternalSyntheticBUOutline0.m("Can't find template: ", str, e);
                return null;
            } catch (IllegalAccessException e2) {
                TextPreference$$ExternalSyntheticBUOutline0.m("Can't access non-public constructor ", str, e2);
                return null;
            } catch (InstantiationException | InvocationTargetException e3) {
                TextPreference$$ExternalSyntheticBUOutline0.m("Could not instantiate the template: ", str, e3);
            } catch (NoSuchMethodException e4) {
                TextPreference$$ExternalSyntheticBUOutline0.m("Error creating template ", str, e4);
                return null;
            }
        }
        return null;
    }
}
