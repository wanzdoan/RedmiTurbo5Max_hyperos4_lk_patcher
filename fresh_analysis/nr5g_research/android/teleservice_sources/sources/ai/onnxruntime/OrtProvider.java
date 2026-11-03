package ai.onnxruntime;

import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public enum OrtProvider {
    CPU("CPUExecutionProvider"),
    CUDA("CUDAExecutionProvider"),
    DNNL("DnnlExecutionProvider"),
    OPEN_VINO("OpenVINOExecutionProvider"),
    VITIS_AI("VitisAIExecutionProvider"),
    TENSOR_RT("TensorrtExecutionProvider"),
    NNAPI("NnapiExecutionProvider"),
    RK_NPU("RknpuExecutionProvider"),
    DIRECT_ML("DmlExecutionProvider"),
    MI_GRAPH_X("MIGraphXExecutionProvider"),
    ACL("ACLExecutionProvider"),
    ARM_NN("ArmNNExecutionProvider"),
    ROCM("ROCMExecutionProvider"),
    CORE_ML("CoreMLExecutionProvider"),
    XNNPACK("XnnpackExecutionProvider"),
    AZURE("AzureExecutionProvider");

    private static final Map valueMap = new HashMap(values().length);
    private final String name;

    static {
        for (OrtProvider ortProvider : values()) {
            valueMap.put(ortProvider.name, ortProvider);
        }
    }

    OrtProvider(String str) {
        this.name = str;
    }

    public String getName() {
        return this.name;
    }

    public static OrtProvider mapFromName(String str) {
        OrtProvider ortProvider = (OrtProvider) valueMap.get(str);
        if (ortProvider != null) {
            return ortProvider;
        }
        OrtProvider$$ExternalSyntheticBUOutline0.m("Unknown execution provider - ", str);
        return null;
    }
}
