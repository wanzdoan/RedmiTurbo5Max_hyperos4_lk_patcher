package gov.nist.core;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.ot.pubsub.c.d$$ExternalSyntheticBUOutline0;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes2.dex */
public abstract class GenericObject implements Serializable, Cloneable {
    protected static final String AND = "&";
    protected static final String AT = "@";
    protected static final String COLON = ":";
    protected static final String COMMA = ",";
    protected static final String DOT = ".";
    protected static final String DOUBLE_QUOTE = "\"";
    protected static final String EQUALS = "=";
    protected static final String GREATER_THAN = ">";
    protected static final String HT = "\t";
    protected static final String LESS_THAN = "<";
    protected static final String LPAREN = "(";
    protected static final String NEWLINE = "\r\n";
    protected static final String PERCENT = "%";
    protected static final String POUND = "#";
    protected static final String QUESTION = "?";
    protected static final String QUOTE = "'";
    protected static final String RETURN = "\n";
    protected static final String RPAREN = ")";
    protected static final String SEMICOLON = ";";
    protected static final String SLASH = "/";
    protected static final String SP = " ";
    protected static final String STAR = "*";
    protected Match matchExpression;
    protected static final Set immutableClasses = new HashSet(10);
    static final String[] immutableClassNames = {"String", "Character", "Boolean", "Byte", "Short", "Integer", "Long", "Float", "Double"};
    protected int indentation = 0;
    protected String stringRepresentation = "";

    public abstract String encode();

    public abstract StringBuffer encode(StringBuffer stringBuffer);

    public Match getMatcher() {
        return null;
    }

    static {
        int i = 0;
        while (true) {
            try {
                String[] strArr = immutableClassNames;
                if (i >= strArr.length) {
                    return;
                }
                immutableClasses.add(Class.forName("java.lang." + strArr[i]));
                i++;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Internal error", e);
            }
        }
    }

    public void setMatcher(Match match) {
        if (match != null) {
            return;
        }
        Folme$$ExternalSyntheticBUOutline0.m("null arg!");
    }

    public static Class getClassFromName(String str) {
        try {
            return Class.forName(str);
        } catch (Exception e) {
            InternalErrorHandler.handleException(e);
            return null;
        }
    }

    public static boolean isMySubclass(Class cls) {
        return GenericObject.class.isAssignableFrom(cls);
    }

    public static Object makeClone(Object obj) throws CloneNotSupportedException {
        Object objClone;
        if (obj == null) {
            GenericObject$$ExternalSyntheticBUOutline0.m("null obj!");
            return null;
        }
        Class<?> cls = obj.getClass();
        if (!immutableClasses.contains(cls)) {
            if (cls.isArray()) {
                Class<?> componentType = cls.getComponentType();
                if (!componentType.isPrimitive()) {
                    return ((Object[]) obj).clone();
                }
                if (componentType == Character.TYPE) {
                    objClone = ((char[]) obj).clone();
                } else {
                    objClone = componentType == Boolean.TYPE ? ((boolean[]) obj).clone() : obj;
                }
                if (componentType == Byte.TYPE) {
                    return ((byte[]) obj).clone();
                }
                if (componentType == Short.TYPE) {
                    return ((short[]) obj).clone();
                }
                if (componentType == Integer.TYPE) {
                    return ((int[]) obj).clone();
                }
                if (componentType == Long.TYPE) {
                    return ((long[]) obj).clone();
                }
                if (componentType == Float.TYPE) {
                    return ((float[]) obj).clone();
                }
                return componentType == Double.TYPE ? ((double[]) obj).clone() : objClone;
            }
            if (GenericObject.class.isAssignableFrom(cls)) {
                return ((GenericObject) obj).clone();
            }
            if (GenericObjectList.class.isAssignableFrom(cls)) {
                ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(obj);
                throw null;
            }
            if (Cloneable.class.isAssignableFrom(cls)) {
                try {
                    return cls.getMethod("clone", null).invoke(obj, null);
                } catch (IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException unused) {
                } catch (IllegalArgumentException e) {
                    InternalErrorHandler.handleException(e);
                }
            }
        }
        return obj;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException unused) {
            d$$ExternalSyntheticBUOutline0.m("Internal error");
            return null;
        }
    }

    public void merge(Object obj) {
        if (obj == null) {
            return;
        }
        if (!obj.getClass().equals(getClass())) {
            Folme$$ExternalSyntheticBUOutline0.m("Bad override object");
            return;
        }
        Class<?> superclass = getClass();
        do {
            for (Field field : superclass.getDeclaredFields()) {
                int modifiers = field.getModifiers();
                if (!Modifier.isPrivate(modifiers) && !Modifier.isStatic(modifiers) && !Modifier.isInterface(modifiers)) {
                    Class<?> type = field.getType();
                    String string = type.toString();
                    try {
                        if (type.isPrimitive()) {
                            if (string.compareTo("int") == 0) {
                                field.setInt(this, field.getInt(obj));
                            } else if (string.compareTo("short") == 0) {
                                field.setShort(this, field.getShort(obj));
                            } else if (string.compareTo("char") == 0) {
                                field.setChar(this, field.getChar(obj));
                            } else if (string.compareTo("long") == 0) {
                                field.setLong(this, field.getLong(obj));
                            } else if (string.compareTo("boolean") == 0) {
                                field.setBoolean(this, field.getBoolean(obj));
                            } else if (string.compareTo("double") == 0) {
                                field.setDouble(this, field.getDouble(obj));
                            } else if (string.compareTo("float") == 0) {
                                field.setFloat(this, field.getFloat(obj));
                            }
                        } else {
                            Object obj2 = field.get(this);
                            Object obj3 = field.get(obj);
                            if (obj3 != null) {
                                if (obj2 == null) {
                                    field.set(this, obj3);
                                } else if (obj2 instanceof GenericObject) {
                                    ((GenericObject) obj2).merge(obj3);
                                } else {
                                    field.set(this, obj3);
                                }
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            superclass = superclass.getSuperclass();
        } while (!superclass.equals(GenericObject.class));
    }

    protected GenericObject() {
    }

    protected String getIndentation() {
        char[] cArr = new char[this.indentation];
        Arrays.fill(cArr, ' ');
        return new String(cArr);
    }

    protected void sprint(String str) {
        if (str == null) {
            this.stringRepresentation += getIndentation();
            this.stringRepresentation += "<null>\n";
            return;
        }
        if (str.compareTo("}") == 0 || str.compareTo("]") == 0) {
            this.indentation--;
        }
        this.stringRepresentation += getIndentation();
        this.stringRepresentation += str;
        this.stringRepresentation += RETURN;
        if (str.compareTo("{") == 0 || str.compareTo("[") == 0) {
            this.indentation++;
        }
    }

    protected void sprint(Object obj) {
        sprint(obj.toString());
    }

    protected void sprint(int i) {
        sprint(String.valueOf(i));
    }

    protected void sprint(short s) {
        sprint(String.valueOf((int) s));
    }

    protected void sprint(char c) {
        sprint(String.valueOf(c));
    }

    protected void sprint(long j) {
        sprint(String.valueOf(j));
    }

    protected void sprint(boolean z) {
        sprint(String.valueOf(z));
    }

    protected void sprint(double d) {
        sprint(String.valueOf(d));
    }

    protected void sprint(float f) {
        sprint(String.valueOf(f));
    }

    protected void dbgPrint() {
        Debug.println(debugDump());
    }

    protected void dbgPrint(String str) {
        Debug.println(str);
    }

    public boolean match(Object obj) {
        if (obj == null) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        GenericObject genericObject = (GenericObject) obj;
        Field[] declaredFields = getClass().getDeclaredFields();
        Field[] declaredFields2 = obj.getClass().getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];
            Field field2 = declaredFields2[i];
            if ((field.getModifiers() & 2) != 2) {
                Class<?> type = field.getType();
                String name = field.getName();
                if (name.compareTo("stringRepresentation") != 0 && name.compareTo("indentation") != 0) {
                    try {
                        if (type.isPrimitive()) {
                            String string = type.toString();
                            if (string.compareTo("int") == 0) {
                                if (field.getInt(this) != field2.getInt(genericObject)) {
                                    return false;
                                }
                            } else if (string.compareTo("short") == 0) {
                                if (field.getShort(this) != field2.getShort(genericObject)) {
                                    return false;
                                }
                            } else if (string.compareTo("char") == 0) {
                                if (field.getChar(this) != field2.getChar(genericObject)) {
                                    return false;
                                }
                            } else if (string.compareTo("long") == 0) {
                                if (field.getLong(this) != field2.getLong(genericObject)) {
                                    return false;
                                }
                            } else if (string.compareTo("boolean") == 0) {
                                if (field.getBoolean(this) != field2.getBoolean(genericObject)) {
                                    return false;
                                }
                            } else if (string.compareTo("double") == 0) {
                                if (field.getDouble(this) != field2.getDouble(genericObject)) {
                                    return false;
                                }
                            } else if (string.compareTo("float") == 0 && field.getFloat(this) != field2.getFloat(genericObject)) {
                                return false;
                            }
                        } else {
                            Object obj2 = field.get(this);
                            Object obj3 = field2.get(genericObject);
                            if (obj3 != null && obj2 == null) {
                                return false;
                            }
                            if ((obj3 != null || obj2 == null) && (obj3 != null || obj2 != null)) {
                                if ((obj3 instanceof String) && (obj2 instanceof String)) {
                                    if (!((String) obj3).trim().equals("") && ((String) obj2).compareToIgnoreCase((String) obj3) != 0) {
                                        return false;
                                    }
                                } else {
                                    if (isMySubclass(obj2.getClass()) && !((GenericObject) obj2).match(obj3)) {
                                        return false;
                                    }
                                    if (GenericObjectList.isMySubclass(obj2.getClass())) {
                                        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(obj2);
                                        throw null;
                                    }
                                }
                            }
                        }
                    } catch (IllegalAccessException e) {
                        InternalErrorHandler.handleException(e);
                    }
                }
            }
        }
        return true;
    }

    public String debugDump() {
        this.stringRepresentation = "";
        Class<?> cls = getClass();
        sprint(cls.getName());
        sprint("{");
        for (Field field : cls.getDeclaredFields()) {
            if ((field.getModifiers() & 2) != 2) {
                Class<?> type = field.getType();
                String name = field.getName();
                if (name.compareTo("stringRepresentation") != 0 && name.compareTo("indentation") != 0) {
                    sprint(name.concat(COLON));
                    try {
                        try {
                            if (type.isPrimitive()) {
                                String string = type.toString();
                                sprint(string + COLON);
                                if (string.compareTo("int") == 0) {
                                    sprint(field.getInt(this));
                                } else if (string.compareTo("short") == 0) {
                                    sprint(field.getShort(this));
                                } else if (string.compareTo("char") == 0) {
                                    sprint(field.getChar(this));
                                } else if (string.compareTo("long") == 0) {
                                    sprint(field.getLong(this));
                                } else if (string.compareTo("boolean") == 0) {
                                    sprint(field.getBoolean(this));
                                } else if (string.compareTo("double") == 0) {
                                    sprint(field.getDouble(this));
                                } else if (string.compareTo("float") == 0) {
                                    sprint(field.getFloat(this));
                                }
                            } else if (GenericObject.class.isAssignableFrom(type)) {
                                if (field.get(this) != null) {
                                    sprint(((GenericObject) field.get(this)).debugDump(this.indentation + 1));
                                } else {
                                    sprint("<null>");
                                }
                            } else if (GenericObjectList.class.isAssignableFrom(type)) {
                                if (field.get(this) != null) {
                                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(field.get(this));
                                    throw null;
                                }
                                sprint("<null>");
                            } else {
                                if (field.get(this) != null) {
                                    sprint(field.get(this).getClass().getName() + COLON);
                                } else {
                                    sprint(type.getName() + COLON);
                                }
                                sprint("{");
                                if (field.get(this) != null) {
                                    sprint(field.get(this).toString());
                                } else {
                                    sprint("<null>");
                                }
                                sprint("}");
                            }
                        } catch (Exception e) {
                            InternalErrorHandler.handleException(e);
                        }
                    } catch (IllegalAccessException unused) {
                        continue;
                    }
                }
            }
        }
        sprint("}");
        return this.stringRepresentation;
    }

    public String debugDump(int i) {
        this.indentation = i;
        String strDebugDump = debugDump();
        this.indentation = 0;
        return strDebugDump;
    }
}
