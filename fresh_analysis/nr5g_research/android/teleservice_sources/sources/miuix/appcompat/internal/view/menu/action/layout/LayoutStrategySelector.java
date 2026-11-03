package miuix.appcompat.internal.view.menu.action.layout;

import android.content.Context;
import com.nxp.id.tp.common.HexString$$ExternalSyntheticBUOutline1;
import com.xiaomi.internal.telephony.DynamicSarService;
import java.util.HashMap;
import java.util.Map;
import miuix.core.util.MiuixUIUtils;

/* JADX INFO: loaded from: classes2.dex */
public class LayoutStrategySelector {
    private final Context context;
    private final Map strategyCache = new HashMap();
    private final int wideThresholdPx;

    public LayoutStrategySelector(Context context) {
        this.context = context;
        this.wideThresholdPx = MiuixUIUtils.dp2px(context, 400.0f);
    }

    public LayoutStrategy selectStrategy(int i, int i2) {
        if (i < 0) {
            HexString$$ExternalSyntheticBUOutline1.m("Item count cannot be negative: ", i);
            return null;
        }
        boolean z = (i == 4 || i == 5) && i2 > this.wideThresholdPx;
        int i3 = z ? i | DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER : i;
        LayoutStrategy layoutStrategy = (LayoutStrategy) this.strategyCache.get(Integer.valueOf(i3));
        if (layoutStrategy != null) {
            return layoutStrategy;
        }
        LayoutStrategy layoutStrategyCreateStrategy = createStrategy(i, z);
        this.strategyCache.put(Integer.valueOf(i3), layoutStrategyCreateStrategy);
        return layoutStrategyCreateStrategy;
    }

    private LayoutStrategy createStrategy(int i, boolean z) {
        if (i == 1) {
            return new SingleItemLayoutStrategy(this.context);
        }
        if (i == 2) {
            return new TwoItemLayoutStrategy(this.context);
        }
        if (i == 3) {
            return new ThreeItemLayoutStrategy(this.context);
        }
        if (i == 4 || i == 5) {
            Context context = this.context;
            if (z) {
                return new WideMultiItemLayoutStrategy(context);
            }
            return new MultiItemLayoutStrategy(context);
        }
        return new MultiItemLayoutStrategy(this.context);
    }
}
