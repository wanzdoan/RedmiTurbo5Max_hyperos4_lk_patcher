package miuix.appcompat.internal.view;

import android.content.Context;
import miuix.appcompat.R$attr;
import miuix.internal.util.AttributeResolver;

/* JADX INFO: loaded from: classes2.dex */
public class ActionBarPolicy {
    private Context mContext;

    public boolean showsOverflowMenuButton() {
        return true;
    }

    public static ActionBarPolicy get(Context context) {
        return new ActionBarPolicy(context);
    }

    private ActionBarPolicy(Context context) {
        this.mContext = context;
    }

    public boolean hasEmbeddedTabs() {
        return AttributeResolver.resolveBoolean(this.mContext, R$attr.actionBarEmbedTabs, false);
    }

    public boolean isTightTitle() {
        return AttributeResolver.resolveBoolean(this.mContext, R$attr.actionBarTightTitle, false);
    }

    public boolean isTitleEnableEllipsis() {
        return AttributeResolver.resolveBoolean(this.mContext, R$attr.actionBarTitleEnableEllipsis, false);
    }

    public boolean enableHomeButtonByDefault() {
        return this.mContext.getApplicationInfo().targetSdkVersion < 14;
    }
}
