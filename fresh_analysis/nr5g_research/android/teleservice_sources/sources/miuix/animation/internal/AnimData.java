package miuix.animation.internal;

import miuix.animation.base.AnimConfig;
import miuix.animation.base.AnimSpecialConfig;
import miuix.animation.listener.UpdateInfo;
import miuix.animation.property.FloatProperty;
import miuix.animation.utils.EaseManager;

/* JADX INFO: loaded from: classes2.dex */
public class AnimData {
    public long delay;
    public EaseManager.EaseStyle ease;
    public int frameCount;
    public double frameInterval;
    public long initTime;
    public boolean isCompleted;
    boolean justEnd;
    boolean logEnabled;
    public byte op;
    public double progress;
    public FloatProperty property;
    public long startTime;
    public double velocity;
    public int tintMode = -1;
    public double startValue = Double.MAX_VALUE;
    public double targetValue = Double.MAX_VALUE;
    public double value = Double.MAX_VALUE;
    public double duration = 0.0d;
    boolean justStart = true;

    void reset() {
        this.isCompleted = false;
        this.frameCount = 0;
        this.justStart = true;
        this.justEnd = false;
        this.frameInterval = 0.0d;
        this.duration = 0.0d;
    }

    void clear() {
        this.property = null;
        this.ease = null;
        this.frameCount = 0;
        this.frameInterval = 0.0d;
        this.duration = 0.0d;
    }

    public void setOp(byte b) {
        this.op = b;
        this.isCompleted = b == 0 || b > 2;
    }

    void from(UpdateInfo updateInfo, AnimConfig animConfig, AnimSpecialConfig animSpecialConfig) {
        this.property = updateInfo.property;
        this.velocity = updateInfo.velocity;
        this.frameCount = updateInfo.frameCount;
        this.op = updateInfo.animInfo.op;
        AnimInfo animInfo = updateInfo.animInfo;
        this.frameInterval = animInfo.frameInterval;
        this.duration = animInfo.duration;
        this.initTime = animInfo.initTime;
        this.startTime = animInfo.startTime;
        this.progress = animInfo.progress;
        this.startValue = animInfo.startValue;
        this.targetValue = animInfo.targetValue;
        this.value = animInfo.value;
        this.isCompleted = updateInfo.isCompleted;
        this.justStart = updateInfo.justStart;
        this.justEnd = animInfo.justEnd;
        this.tintMode = AnimConfigUtils.getTintMode(animConfig, animSpecialConfig);
        this.ease = AnimConfigUtils.getEase(animConfig, animSpecialConfig);
        this.delay = AnimConfigUtils.getDelay(animConfig, animSpecialConfig);
    }

    void to(UpdateInfo updateInfo) {
        updateInfo.frameCount = this.frameCount;
        updateInfo.animInfo.op = this.op;
        AnimInfo animInfo = updateInfo.animInfo;
        animInfo.delay = this.delay;
        animInfo.tintMode = this.tintMode;
        animInfo.initTime = this.initTime;
        animInfo.startTime = this.startTime;
        animInfo.progress = this.progress;
        animInfo.startValue = this.startValue;
        animInfo.targetValue = this.targetValue;
        updateInfo.isCompleted = this.isCompleted;
        animInfo.value = this.value;
        updateInfo.velocity = this.velocity;
        updateInfo.justStart = this.justStart;
        animInfo.justEnd = this.justEnd;
        animInfo.frameInterval = this.frameInterval;
        animInfo.duration = this.duration;
        clear();
    }
}
