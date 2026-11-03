package com.google.android.appfunctions.schema.common.v1.video;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.video.$$__AppSearch__Transcript, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Transcript implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Transcript transcript) {
        GenericDocument.Builder builder = new GenericDocument.Builder(transcript.getNamespace(), transcript.getId(), "com.google.android.appfunctions.schema.common.v1.video.Transcript");
        transcript.getContent();
        String text = transcript.getText();
        if (text != null) {
            builder.setPropertyString("text", text);
        }
        List timedText = transcript.getTimedText();
        if (timedText != null) {
            GenericDocument[] genericDocumentArr = new GenericDocument[timedText.size()];
            Iterator it = timedText.iterator();
            int i = 0;
            while (it.hasNext()) {
                genericDocumentArr[i] = GenericDocument.fromDocumentClass((TimedText) it.next());
                i++;
            }
            builder.setPropertyDocument("timedText", genericDocumentArr);
        }
        return builder.build();
    }
}
