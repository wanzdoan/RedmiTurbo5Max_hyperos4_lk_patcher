package com.nxp.id.jsbl.uai.api.objects;

import com.nxp.id.jsbl.uai.api.util.Utils;
import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.StringUtil;
import com.nxp.id.tp.common.coding.asn1.Asn1Encodable;
import com.nxp.id.tp.common.coding.asn1.Asn1Sequence;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class UpdateScripts implements Asn1Encodable {
    private final List updateScriptList;

    public UpdateScripts(List list) {
        this.updateScriptList = new LinkedList(list);
    }

    public static UpdateScripts decodeFromDER(Asn1Encodable asn1Encodable) {
        Utils.checkParamNotNull(asn1Encodable);
        LinkedList linkedList = new LinkedList();
        Iterator it = ((Asn1Sequence) asn1Encodable).getChildren().iterator();
        while (it.hasNext()) {
            UpdateScript updateScriptDecodeFromDER = UpdateScript.decodeFromDER((Asn1Encodable) it.next());
            if (updateScriptDecodeFromDER != null) {
                linkedList.add(updateScriptDecodeFromDER);
            }
        }
        return new UpdateScripts(linkedList);
    }

    public UpdateScript getUpdateScript(int i) {
        return (UpdateScript) getUpdateScriptList().get(i - 1);
    }

    public String toString() {
        return toString(0);
    }

    public List getUpdateScriptList() {
        return this.updateScriptList;
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public String toString(int i) {
        StringBuilder sb = new StringBuilder();
        String strRepeat = StringUtil.repeat(' ', i);
        sb.append(strRepeat);
        sb.append("UpdateScripts\n");
        Iterator it = this.updateScriptList.iterator();
        while (it.hasNext()) {
            sb.append(((UpdateScript) it.next()).toString(i + 2));
            sb.append("\n");
        }
        sb.append(strRepeat);
        sb.append("End of UpdateScripts");
        return sb.toString();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public UpdateScripts m4775clone() {
        return new UpdateScripts(this.updateScriptList);
    }

    private Asn1Sequence buildObject() {
        return new Asn1Sequence(new LinkedList() { // from class: com.nxp.id.jsbl.uai.api.objects.UpdateScripts.1
            private static final long serialVersionUID = 1;

            {
                addAll(UpdateScripts.this.updateScriptList);
            }
        });
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public HexString encode() {
        return buildObject().encode();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public HexString getValue() {
        return buildObject().getValue();
    }
}
