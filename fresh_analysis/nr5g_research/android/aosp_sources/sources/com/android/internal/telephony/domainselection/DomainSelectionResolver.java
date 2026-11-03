package com.android.internal.telephony.domainselection;

import android.content.ComponentName;
import android.content.Context;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.AndroidUtilIndentingPrintWriter;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.SomeArgs$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.util.TelephonyUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* JADX INFO: loaded from: classes.dex */
public class DomainSelectionResolver {
    protected static final String PACKAGE_NAME_NONE = "none";
    private static final String TAG = "DomainSelectionResolver";
    private final Context mContext;
    private DomainSelectionController mController;
    private final ComponentName mDefaultComponentName;
    private DomainSelectionControllerFactory mDomainSelectionControllerFactory = new DomainSelectionControllerFactory(this) { // from class: com.android.internal.telephony.domainselection.DomainSelectionResolver.1
        @Override // com.android.internal.telephony.domainselection.DomainSelectionResolver.DomainSelectionControllerFactory
        public DomainSelectionController create(Context context) {
            return new DomainSelectionController(context);
        }
    };
    private final LocalLog mEventLog = new LocalLog(10);
    private static final boolean DBG = TelephonyUtils.IS_DEBUGGABLE;
    private static DomainSelectionResolver sInstance = null;

    public interface DomainSelectionControllerFactory {
        DomainSelectionController create(Context context);
    }

    public static void make(Context context, String str) {
        Log.i(TAG, "make useOem=true");
        if (sInstance == null) {
            sInstance = new DomainSelectionResolver(context, str);
        }
    }

    public static DomainSelectionResolver getInstance() {
        DomainSelectionResolver domainSelectionResolver = sInstance;
        if (domainSelectionResolver != null) {
            return domainSelectionResolver;
        }
        SomeArgs$$ExternalSyntheticBUOutline0.m("DomainSelectionResolver is not ready!");
        return null;
    }

    public static void setDomainSelectionResolver(DomainSelectionResolver domainSelectionResolver) {
        sInstance = domainSelectionResolver;
    }

    public DomainSelectionResolver(Context context, String str) {
        this.mContext = context;
        str = str == null ? "" : str;
        this.mDefaultComponentName = ComponentName.unflattenFromString(str);
        logi("DomainSelectionResolver created: componentName=[" + str + "]");
    }

    public boolean isDomainSelectionSupported() {
        if (!DBG || !SystemProperties.getBoolean("telephony.test.disable_domain_selection", false)) {
            return this.mDefaultComponentName != null && PhoneFactory.getDefaultPhone().getHalVersion(4).greaterOrEqual(RIL.RADIO_HAL_VERSION_2_1);
        }
        logi("Disabled for test");
        return false;
    }

    public DomainSelectionConnection getDomainSelectionConnection(Phone phone, int i, boolean z) {
        if (this.mController == null) {
            SomeArgs$$ExternalSyntheticBUOutline0.m("DomainSelection is not supported!");
            return null;
        }
        if (phone == null || phone.getImsPhone() == null || !((z && i == 1) || phone.isImsAvailable())) {
            return null;
        }
        return this.mController.getDomainSelectionConnection(phone, i, z);
    }

    public void setDomainSelectionControllerFactory(DomainSelectionControllerFactory domainSelectionControllerFactory) {
        this.mDomainSelectionControllerFactory = domainSelectionControllerFactory;
    }

    public void initialize() {
        logi("Initialize");
        DomainSelectionController domainSelectionControllerCreate = this.mDomainSelectionControllerFactory.create(this.mContext);
        this.mController = domainSelectionControllerCreate;
        ComponentName componentName = this.mDefaultComponentName;
        if (componentName != null) {
            domainSelectionControllerCreate.bind(componentName);
        } else {
            logi("No component name specified for domain selection service.");
        }
    }

    public boolean setDomainSelectionServiceOverride(ComponentName componentName) {
        if (this.mController == null) {
            logd("Controller is not initialized.");
            return false;
        }
        logi("setDomainSelectionServiceOverride: " + componentName);
        if (TextUtils.isEmpty(componentName.getPackageName()) || TextUtils.equals(PACKAGE_NAME_NONE, componentName.getPackageName())) {
            this.mController.unbind();
            return true;
        }
        return this.mController.bind(componentName);
    }

    public boolean clearDomainSelectionServiceOverride() {
        if (this.mController == null) {
            logd("Controller is not initialized.");
            return false;
        }
        logi("clearDomainSelectionServiceOverride");
        this.mController.unbind();
        return this.mController.bind(this.mDefaultComponentName);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
        androidUtilIndentingPrintWriter.println("Resolver:");
        androidUtilIndentingPrintWriter.increaseIndent();
        androidUtilIndentingPrintWriter.println("Event Log:");
        androidUtilIndentingPrintWriter.increaseIndent();
        this.mEventLog.dump(androidUtilIndentingPrintWriter);
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println("Controller:");
        androidUtilIndentingPrintWriter.increaseIndent();
        DomainSelectionController domainSelectionController = this.mController;
        if (domainSelectionController == null) {
            androidUtilIndentingPrintWriter.println("no active controller");
        } else {
            domainSelectionController.dump(androidUtilIndentingPrintWriter);
        }
        androidUtilIndentingPrintWriter.decreaseIndent();
    }

    private void logd(String str) {
        Log.d(TAG, str);
    }

    private void logi(String str) {
        Log.i(TAG, str);
        this.mEventLog.log(str);
    }
}
