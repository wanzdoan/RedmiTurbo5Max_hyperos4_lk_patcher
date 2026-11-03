package gov.nist.javax.sip.header;

import gov.nist.core.DuplicateNameValueList;
import gov.nist.core.NameValue;
import gov.nist.core.NameValueList;
import java.io.Serializable;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ParametersHeader extends SIPHeader implements Serializable {
    protected DuplicateNameValueList duplicates;
    protected NameValueList parameters;

    protected ParametersHeader(String str) {
        super(str);
        this.parameters = new NameValueList();
        this.duplicates = new DuplicateNameValueList();
    }

    public String getParameter(String str) {
        return this.parameters.getParameter(str);
    }

    public void setParameter(NameValue nameValue) {
        this.parameters.set(nameValue);
    }

    @Override // gov.nist.core.GenericObject
    public Object clone() {
        ParametersHeader parametersHeader = (ParametersHeader) super.clone();
        NameValueList nameValueList = this.parameters;
        if (nameValueList != null) {
            parametersHeader.parameters = (NameValueList) nameValueList.clone();
        }
        return parametersHeader;
    }
}
