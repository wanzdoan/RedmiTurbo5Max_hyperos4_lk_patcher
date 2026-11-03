package ai.onnxruntime;

/* JADX INFO: loaded from: classes.dex */
public class NodeInfo {
    private final ValueInfo info;
    private final String name;

    public NodeInfo(String str, ValueInfo valueInfo) {
        this.name = str;
        this.info = valueInfo;
    }

    public String getName() {
        return this.name;
    }

    public ValueInfo getInfo() {
        return this.info;
    }

    public String toString() {
        return "NodeInfo(name=" + this.name + ",info=" + this.info.toString() + ")";
    }
}
