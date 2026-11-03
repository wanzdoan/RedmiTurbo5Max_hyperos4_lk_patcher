package com.aios.apptoolsdk;

import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class AppTool {
    private final String description;
    private final String name;
    private final List parameters;

    public abstract AppResult execute(ToolArgs toolArgs);

    protected AppTool(String str, String str2, List list) {
        this.name = str;
        this.description = str2;
        this.parameters = list == null ? Collections.EMPTY_LIST : list;
    }

    public String getName() {
        return this.name;
    }
}
