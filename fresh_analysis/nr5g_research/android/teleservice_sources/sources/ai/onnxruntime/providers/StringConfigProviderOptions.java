package ai.onnxruntime.providers;

import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtProviderOptions;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.tensorflow.lite.DataType$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
abstract class StringConfigProviderOptions extends OrtProviderOptions {
    protected final Map options;

    protected abstract void add(long j, long j2, String str, String str2) throws OrtException;

    protected StringConfigProviderOptions(long j) {
        super(j);
        this.options = new LinkedHashMap();
    }

    public void add(String str, String str2) throws OrtException {
        Objects.requireNonNull(str, "Key must not be null");
        Objects.requireNonNull(str2, "Value must not be null");
        this.options.put(str, str2);
        add(OrtProviderOptions.getApiHandle(), this.nativeHandle, str, str2);
    }

    public void parseOptionsString(String str) throws OrtException {
        for (String str2 : str.split(";")) {
            if (!str2.isEmpty() && str2.contains("=")) {
                String[] strArrSplit = str2.split("=");
                if (strArrSplit.length == 2 && !strArrSplit[0].isEmpty() && !strArrSplit[1].isEmpty()) {
                    add(strArrSplit[0], strArrSplit[1]);
                } else {
                    DataType$$ExternalSyntheticBUOutline0.m("Failed to parse option from string '", str2, "'");
                    return;
                }
            }
        }
    }

    public String toString() {
        return getClass().getSimpleName() + "(" + getOptionsString() + ")";
    }

    public String getOptionsString() {
        return (String) this.options.entrySet().stream().map(new Function() { // from class: ai.onnxruntime.providers.StringConfigProviderOptions$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return StringConfigProviderOptions.m1$r8$lambda$uUoSjccx0dE5lr6PGHJOe63z_g((Map.Entry) obj);
            }
        }).collect(Collectors.joining(";", "", ";"));
    }

    /* JADX INFO: renamed from: $r8$lambda$uUoSjccx0dE5lr6PG-HJOe63z_g, reason: not valid java name */
    public static /* synthetic */ String m1$r8$lambda$uUoSjccx0dE5lr6PGHJOe63z_g(Map.Entry entry) {
        return ((String) entry.getKey()) + "=" + ((String) entry.getValue());
    }
}
