package com.android.phone.recoveryPolicy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public abstract class Policy {
    protected List actionList = new ArrayList();

    public class Action {
        public HashMap conditions = new HashMap();
        public String name;

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Action{name='");
            sb.append(this.name);
            sb.append("', conditions={");
            for (Map.Entry entry : this.conditions.entrySet()) {
                sb.append((String) entry.getKey());
                sb.append("='");
                sb.append((String) entry.getValue());
                sb.append("', ");
            }
            if (!this.conditions.isEmpty()) {
                sb.setLength(sb.length() - 2);
            }
            sb.append("}}");
            return sb.toString();
        }
    }

    public void addAction(Action action) {
        this.actionList.add(action);
    }
}
