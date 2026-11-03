package com.aios.apptoolsdk;

import android.content.Context;
import android.util.Log;
import com.ot.pubsub.a.a;
import com.xiaomi.settingsdk.backup.data.KeyStringSettingItem;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class AnnotatedToolService extends AppToolProviderService {
    @Override // com.aios.apptoolsdk.AppToolProviderService
    protected List onCreateTools() {
        String strLoadToolsJson = loadToolsJson();
        if (strLoadToolsJson == null) {
            Log.e("AnnotatedToolService", "未找到 app_tools.json，请确认已添加 apptoolsdk-compiler 注解处理器依赖");
            return Collections.EMPTY_LIST;
        }
        try {
            JSONArray jSONArray = new JSONArray(strLoadToolsJson);
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < jSONArray.length(); i++) {
                AppTool appToolCreateReflectiveTool = createReflectiveTool(jSONArray.getJSONObject(i));
                if (appToolCreateReflectiveTool != null) {
                    arrayList.add(appToolCreateReflectiveTool);
                }
            }
            Log.i("AnnotatedToolService", "从注解加载了 " + arrayList.size() + " 个工具");
            return arrayList;
        } catch (Exception e) {
            Log.e("AnnotatedToolService", "解析 app_tools.json 失败", e);
            return Collections.EMPTY_LIST;
        }
    }

    private String loadToolsJson() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open("app_tools.json"), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    sb.append(line);
                } else {
                    bufferedReader.close();
                    return sb.toString();
                }
            }
        } catch (Exception e) {
            Log.e("AnnotatedToolService", "读取 app_tools.json 失败", e);
            return null;
        }
    }

    private AppTool createReflectiveTool(JSONObject jSONObject) {
        AppTool appTool;
        try {
            String string = jSONObject.getString("name");
            String strResolveDescription = resolveDescription(jSONObject);
            String string2 = jSONObject.getString("method");
            String string3 = jSONObject.getString(a.r);
            int i = jSONObject.getInt("paramCount");
            JSONArray jSONArray = jSONObject.getJSONArray("params");
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            int i2 = 0;
            while (i2 < jSONArray.length()) {
                JSONObject jSONObject2 = jSONArray.getJSONObject(i2);
                ParamMeta paramMeta = parseParamMeta(jSONObject2);
                arrayList.add(paramMeta);
                appTool = null;
                try {
                    arrayList2.add(createAppParam(paramMeta.name, paramMeta.type, resolveDescription(jSONObject2), paramMeta.required));
                    i2++;
                    string = string;
                } catch (Exception e) {
                    e = e;
                    Log.e("AnnotatedToolService", "创建工具失败: " + jSONObject.optString("name", "?"), e);
                    return appTool;
                }
            }
            String str = string;
            Class<?> cls = Class.forName(string3);
            Method methodFindMethod = findMethod(cls, string2, i);
            if (methodFindMethod == null) {
                Log.e("AnnotatedToolService", "未找到方法: " + string3 + "." + string2 + "(" + i + " params)");
                return null;
            }
            methodFindMethod.setAccessible(true);
            Object objResolveTargetObject = resolveTargetObject(cls);
            if (objResolveTargetObject == null) {
                Log.e("AnnotatedToolService", "无法创建目标对象: " + string3);
                return null;
            }
            return new ReflectiveAppTool(str, strResolveDescription, arrayList2, methodFindMethod, objResolveTargetObject, arrayList, i);
        } catch (Exception e2) {
            e = e2;
            appTool = null;
        }
    }

    private ParamMeta parseParamMeta(JSONObject jSONObject) throws JSONException {
        ArrayList arrayList;
        String string = jSONObject.getString("name");
        String string2 = jSONObject.getString("type");
        boolean zOptBoolean = jSONObject.optBoolean("required", false);
        int iOptInt = jSONObject.optInt("index", -1);
        String string3 = jSONObject.has(a.r) ? jSONObject.getString(a.r) : null;
        String string4 = jSONObject.has("field") ? jSONObject.getString("field") : null;
        if (jSONObject.has("params")) {
            JSONArray jSONArray = jSONObject.getJSONArray("params");
            ArrayList arrayList2 = new ArrayList();
            for (int i = 0; i < jSONArray.length(); i++) {
                arrayList2.add(parseParamMeta(jSONArray.getJSONObject(i)));
            }
            arrayList = arrayList2;
        } else {
            arrayList = null;
        }
        return new ParamMeta(string, string2, zOptBoolean, iOptInt, string3, string4, arrayList, jSONObject.has("items") ? parseItemsMeta(jSONObject.getJSONObject("items")) : null);
    }

    private ItemsMeta parseItemsMeta(JSONObject jSONObject) throws JSONException {
        String string = jSONObject.getString("type");
        ArrayList arrayList = null;
        String string2 = jSONObject.has(a.r) ? jSONObject.getString(a.r) : null;
        if (jSONObject.has("params")) {
            JSONArray jSONArray = jSONObject.getJSONArray("params");
            arrayList = new ArrayList();
            for (int i = 0; i < jSONArray.length(); i++) {
                arrayList.add(parseParamMeta(jSONArray.getJSONObject(i)));
            }
        }
        return new ItemsMeta(string, string2, arrayList);
    }

    private String resolveDescription(JSONObject jSONObject) {
        JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("descriptions");
        if (jSONObjectOptJSONObject != null) {
            String language = Locale.getDefault().getLanguage();
            if (jSONObjectOptJSONObject.has(language)) {
                return jSONObjectOptJSONObject.optString(language);
            }
            if (jSONObjectOptJSONObject.has("zh")) {
                return jSONObjectOptJSONObject.optString("zh");
            }
            Iterator<String> itKeys = jSONObjectOptJSONObject.keys();
            if (itKeys.hasNext()) {
                return jSONObjectOptJSONObject.optString(itKeys.next());
            }
        }
        return jSONObject.optString("description", "");
    }

    private Method findMethod(Class cls, String str, int i) {
        for (Method method : cls.getDeclaredMethods()) {
            if (method.getName().equals(str) && method.getParameterTypes().length == i) {
                return method;
            }
        }
        Class superclass = cls.getSuperclass();
        if (superclass == null || superclass == Object.class) {
            return null;
        }
        return findMethod(superclass, str, i);
    }

    private Object resolveTargetObject(Class cls) {
        if (cls.isAssignableFrom(getClass())) {
            return this;
        }
        try {
            Class[] clsArr = new Class[0];
            return cls.getDeclaredConstructor(null).newInstance(null);
        } catch (Exception unused) {
            try {
                return cls.getDeclaredConstructor(Context.class).newInstance(this);
            } catch (Exception unused2) {
                return null;
            }
        }
    }

    private AppParam createAppParam(String str, String str2, String str3, boolean z) {
        str2.getClass();
        switch (str2) {
            case "number":
                if (z) {
                    return AppParam.number(str, str3, true);
                }
                return AppParam.number(str, str3);
            case "string":
                if (z) {
                    return AppParam.string(str, str3, true);
                }
                return AppParam.string(str, str3);
            case "boolean":
                if (z) {
                    return AppParam.bool(str, str3, true);
                }
                return AppParam.bool(str, str3);
            case "integer":
                if (z) {
                    return AppParam.integer(str, str3, true);
                }
                return AppParam.integer(str, str3);
            default:
                return AppParam.string(str, str3, z);
        }
    }

    class ReflectiveAppTool extends AppTool {
        private final Method method;
        private final int paramCount;
        private final List paramMetas;
        private final Object target;

        ReflectiveAppTool(String str, String str2, List list, Method method, Object obj, List list2, int i) {
            super(str, str2, list);
            this.method = method;
            this.target = obj;
            this.paramMetas = list2;
            this.paramCount = i;
        }

        @Override // com.aios.apptoolsdk.AppTool
        public AppResult execute(ToolArgs toolArgs) {
            try {
                Object objInvoke = this.method.invoke(this.target, buildInvokeArgs(toolArgs));
                if (objInvoke instanceof AppResult) {
                    return (AppResult) objInvoke;
                }
                return AppResult.error("方法返回类型不是 AppResult");
            } catch (Exception e) {
                Log.e("AnnotatedToolService", "反射调用 " + this.method.getName() + " 失败", e);
                Throwable cause = e.getCause();
                Exception cause2 = e;
                if (cause != null) {
                    cause2 = e.getCause();
                }
                return AppResult.error("执行异常: " + cause2.getMessage());
            }
        }

        private Object[] buildInvokeArgs(ToolArgs toolArgs) {
            Object[] objArr = new Object[this.paramCount];
            Class<?>[] parameterTypes = this.method.getParameterTypes();
            boolean[] zArr = new boolean[this.paramCount];
            for (ParamMeta paramMeta : this.paramMetas) {
                int i = paramMeta.index;
                if (i >= 0 && i < this.paramCount) {
                    zArr[i] = true;
                    objArr[i] = extractValue(toolArgs, paramMeta, parameterTypes[i]);
                }
            }
            for (int i2 = 0; i2 < this.paramCount; i2++) {
                if (!zArr[i2]) {
                    objArr[i2] = injectParam(parameterTypes[i2]);
                }
            }
            return objArr;
        }

        private Object extractValue(ToolArgs toolArgs, ParamMeta paramMeta, Class cls) {
            if (!toolArgs.has(paramMeta.name)) {
                return null;
            }
            String str = paramMeta.type;
            str.getClass();
            switch (str) {
                case "number":
                    if (cls == Float.TYPE || cls == Float.class) {
                        Double dNumber = toolArgs.number(paramMeta.name);
                        if (dNumber != null) {
                            return Float.valueOf(dNumber.floatValue());
                        }
                        return null;
                    }
                    return toolArgs.number(paramMeta.name);
                case "object":
                    return deserializeObject(toolArgs.object(paramMeta.name), paramMeta);
                case "string":
                    return toolArgs.string(paramMeta.name);
                case "boolean":
                    return toolArgs.bool(paramMeta.name);
                case "array":
                    return deserializeArray(toolArgs.array(paramMeta.name), paramMeta, cls);
                case "integer":
                    if (cls == Long.TYPE || cls == Long.class) {
                        return toolArgs.longValue(paramMeta.name);
                    }
                    return toolArgs.integer(paramMeta.name);
                default:
                    return toolArgs.string(paramMeta.name);
            }
        }

        private Object deserializeObject(JSONObject jSONObject, ParamMeta paramMeta) {
            String str;
            if (jSONObject != null && (str = paramMeta.className) != null && paramMeta.nestedParams != null) {
                try {
                    Class<?> cls = Class.forName(str);
                    Class[] clsArr = new Class[0];
                    Object objNewInstance = cls.getDeclaredConstructor(null).newInstance(null);
                    for (ParamMeta paramMeta2 : paramMeta.nestedParams) {
                        if (paramMeta2.fieldName != null && jSONObject.has(paramMeta2.name)) {
                            Field fieldFindField = findField(cls, paramMeta2.fieldName);
                            if (fieldFindField == null) {
                                Log.w("AnnotatedToolService", "未找到字段: " + cls.getSimpleName() + "." + paramMeta2.fieldName);
                            } else {
                                fieldFindField.setAccessible(true);
                                Object objExtractFieldValue = extractFieldValue(jSONObject, paramMeta2, fieldFindField.getType());
                                if (objExtractFieldValue != null) {
                                    fieldFindField.set(objNewInstance, objExtractFieldValue);
                                }
                            }
                        }
                    }
                    return objNewInstance;
                } catch (Exception e) {
                    Log.e("AnnotatedToolService", "反序列化对象失败: " + paramMeta.className, e);
                }
            }
            return null;
        }

        private Object extractFieldValue(JSONObject jSONObject, ParamMeta paramMeta, Class cls) {
            if (!jSONObject.has(paramMeta.name) || jSONObject.isNull(paramMeta.name)) {
                return null;
            }
            String str = paramMeta.type;
            str.getClass();
            switch (str) {
                case "number":
                    if (cls == Float.TYPE || cls == Float.class) {
                        return Float.valueOf((float) jSONObject.optDouble(paramMeta.name));
                    }
                    return Double.valueOf(jSONObject.optDouble(paramMeta.name));
                case "object":
                    return deserializeObject(jSONObject.optJSONObject(paramMeta.name), paramMeta);
                case "string":
                    return jSONObject.optString(paramMeta.name, null);
                case "boolean":
                    return Boolean.valueOf(jSONObject.optBoolean(paramMeta.name));
                case "array":
                    return deserializeArray(jSONObject.optJSONArray(paramMeta.name), paramMeta, cls);
                case "integer":
                    if (cls == Long.TYPE || cls == Long.class) {
                        return Long.valueOf(jSONObject.optLong(paramMeta.name));
                    }
                    return Integer.valueOf(jSONObject.optInt(paramMeta.name));
                default:
                    return jSONObject.optString(paramMeta.name, null);
            }
        }

        private Object deserializeArray(JSONArray jSONArray, ParamMeta paramMeta, Class cls) {
            if (jSONArray == null || paramMeta.itemsMeta == null) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < jSONArray.length(); i++) {
                Object objDeserializeArrayElement = deserializeArrayElement(jSONArray, i, paramMeta.itemsMeta);
                if (objDeserializeArrayElement != null) {
                    arrayList.add(objDeserializeArrayElement);
                }
            }
            if (!cls.isArray()) {
                return arrayList;
            }
            Object objNewInstance = Array.newInstance(cls.getComponentType(), arrayList.size());
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                Array.set(objNewInstance, i2, arrayList.get(i2));
            }
            return objNewInstance;
        }

        /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
        private Object deserializeArrayElement(JSONArray jSONArray, int i, ItemsMeta itemsMeta) {
            String str;
            List list;
            try {
                String str2 = itemsMeta.type;
                switch (str2.hashCode()) {
                    case -1034364087:
                        if (str2.equals("number")) {
                            return Double.valueOf(jSONArray.optDouble(i));
                        }
                        break;
                    case -1023368385:
                        if (str2.equals("object")) {
                            JSONObject jSONObjectOptJSONObject = jSONArray.optJSONObject(i);
                            if (jSONObjectOptJSONObject == null || (str = itemsMeta.className) == null || (list = itemsMeta.nestedParams) == null) {
                                return null;
                            }
                            return deserializeObject(jSONObjectOptJSONObject, new ParamMeta("", "object", false, -1, str, null, list, null));
                        }
                        break;
                    case -891985903:
                        if (str2.equals(KeyStringSettingItem.TYPE)) {
                            return jSONArray.optString(i);
                        }
                        break;
                    case 64711720:
                        if (str2.equals("boolean")) {
                            return Boolean.valueOf(jSONArray.optBoolean(i));
                        }
                        break;
                    case 1958052158:
                        if (str2.equals("integer")) {
                            return Integer.valueOf(jSONArray.optInt(i));
                        }
                        break;
                }
                return jSONArray.optString(i);
            } catch (Exception e) {
                Log.w("AnnotatedToolService", "反序列化数组元素失败: index=" + i, e);
                return null;
            }
        }

        private Field findField(Class cls, String str) {
            while (cls != null && cls != Object.class) {
                try {
                    return cls.getDeclaredField(str);
                } catch (NoSuchFieldException unused) {
                    cls = cls.getSuperclass();
                }
            }
            return null;
        }

        private Object injectParam(Class cls) {
            if (Context.class.isAssignableFrom(cls)) {
                return AnnotatedToolService.this;
            }
            return null;
        }
    }

    class ParamMeta {
        final String className;
        final String fieldName;
        final int index;
        final ItemsMeta itemsMeta;
        final String name;
        final List nestedParams;
        final boolean required;
        final String type;

        ParamMeta(String str, String str2, boolean z, int i, String str3, String str4, List list, ItemsMeta itemsMeta) {
            this.name = str;
            this.type = str2;
            this.required = z;
            this.index = i;
            this.className = str3;
            this.fieldName = str4;
            this.nestedParams = list;
            this.itemsMeta = itemsMeta;
        }
    }

    class ItemsMeta {
        final String className;
        final List nestedParams;
        final String type;

        ItemsMeta(String str, String str2, List list) {
            this.type = str;
            this.className = str2;
            this.nestedParams = list;
        }
    }
}
