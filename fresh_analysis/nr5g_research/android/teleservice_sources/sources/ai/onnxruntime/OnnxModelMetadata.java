package ai.onnxruntime;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/* JADX INFO: loaded from: classes.dex */
public final class OnnxModelMetadata {
    private final Map customMetadata;
    private final String description;
    private final String domain;
    private final String graphDescription;
    private final String graphName;
    private final String producerName;
    private final long version;

    OnnxModelMetadata(String str, String str2, String str3, String str4, String str5, long j, String[] strArr) {
        this.producerName = str == null ? "" : str;
        this.graphName = str2 == null ? "" : str2;
        this.graphDescription = str3 == null ? "" : str3;
        this.domain = str4 == null ? "" : str4;
        this.description = str5 == null ? "" : str5;
        this.version = j;
        if (strArr != null && strArr.length > 0) {
            this.customMetadata = new HashMap();
            if (strArr.length % 2 == 1) {
                OrtUtil$$ExternalSyntheticBUOutline0.m("Asked for keys and values, but received an odd number of elements.");
                throw null;
            }
            for (int i = 0; i < strArr.length; i += 2) {
                this.customMetadata.put(strArr[i], strArr[i + 1]);
            }
            return;
        }
        this.customMetadata = Collections.EMPTY_MAP;
    }

    OnnxModelMetadata(String str, String str2, String str3, String str4, String str5, long j, Map map) {
        this.producerName = str == null ? "" : str;
        this.graphName = str2 == null ? "" : str2;
        this.graphDescription = str3 == null ? "" : str3;
        this.domain = str4 == null ? "" : str4;
        this.description = str5 == null ? "" : str5;
        this.version = j;
        this.customMetadata = map == null ? Collections.EMPTY_MAP : map;
    }

    public OnnxModelMetadata(OnnxModelMetadata onnxModelMetadata) {
        Map map;
        this.producerName = onnxModelMetadata.producerName;
        this.graphName = onnxModelMetadata.graphName;
        this.graphDescription = onnxModelMetadata.graphDescription;
        this.domain = onnxModelMetadata.domain;
        this.description = onnxModelMetadata.description;
        this.version = onnxModelMetadata.version;
        if (onnxModelMetadata.customMetadata.isEmpty()) {
            map = Collections.EMPTY_MAP;
        } else {
            map = new HashMap(getCustomMetadata());
        }
        this.customMetadata = map;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && OnnxModelMetadata.class == obj.getClass()) {
            OnnxModelMetadata onnxModelMetadata = (OnnxModelMetadata) obj;
            if (this.version == onnxModelMetadata.version && this.producerName.equals(onnxModelMetadata.producerName) && this.graphName.equals(onnxModelMetadata.graphName) && this.graphDescription.equals(onnxModelMetadata.graphDescription) && this.domain.equals(onnxModelMetadata.domain) && this.description.equals(onnxModelMetadata.description) && this.customMetadata.equals(onnxModelMetadata.customMetadata)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.producerName, this.graphName, this.graphDescription, this.domain, this.description, Long.valueOf(this.version), this.customMetadata);
    }

    public String getProducerName() {
        return this.producerName;
    }

    public String getGraphName() {
        return this.graphName;
    }

    public String getGraphDescription() {
        return this.graphDescription;
    }

    public String getDomain() {
        return this.domain;
    }

    public String getDescription() {
        return this.description;
    }

    public long getVersion() {
        return this.version;
    }

    public Map getCustomMetadata() {
        return Collections.unmodifiableMap(this.customMetadata);
    }

    public Optional getCustomMetadataValue(String str) {
        return Optional.ofNullable((String) this.customMetadata.get(str));
    }

    public String toString() {
        return "OnnxModelMetadata{producerName='" + this.producerName + "', graphName='" + this.graphName + "', graphDescription='" + this.graphDescription + "', domain='" + this.domain + "', description='" + this.description + "', version=" + this.version + ", customMetadata=" + this.customMetadata + '}';
    }
}
