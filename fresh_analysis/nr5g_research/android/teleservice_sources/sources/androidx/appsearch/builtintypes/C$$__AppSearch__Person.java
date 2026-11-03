package androidx.appsearch.builtintypes;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: androidx.appsearch.builtintypes.$$__AppSearch__Person, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Person implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Person person) {
        GenericDocument.Builder builder = new GenericDocument.Builder(person.getNamespace(), person.getId(), "builtin:Person");
        builder.setScore(person.getDocumentScore());
        builder.setCreationTimestampMillis(person.getCreationTimestampMillis());
        builder.setTtlMillis(person.getDocumentTtlMillis());
        String name = person.getName();
        if (name != null) {
            builder.setPropertyString("name", name);
        }
        List alternateNames = person.getAlternateNames();
        int i = 0;
        if (alternateNames != null) {
            builder.setPropertyString("alternateNames", (String[]) alternateNames.toArray(new String[0]));
        }
        String description = person.getDescription();
        if (description != null) {
            builder.setPropertyString("description", description);
        }
        String image = person.getImage();
        if (image != null) {
            builder.setPropertyString("image", image);
        }
        String url = person.getUrl();
        if (url != null) {
            builder.setPropertyString("url", url);
        }
        List potentialActions = person.getPotentialActions();
        if (potentialActions != null) {
            GenericDocument[] genericDocumentArr = new GenericDocument[potentialActions.size()];
            Iterator it = potentialActions.iterator();
            int i2 = 0;
            while (it.hasNext()) {
                genericDocumentArr[i2] = GenericDocument.fromDocumentClass((PotentialAction) it.next());
                i2++;
            }
            builder.setPropertyDocument("potentialActions", genericDocumentArr);
        }
        String givenName = person.getGivenName();
        if (givenName != null) {
            builder.setPropertyString("givenName", givenName);
        }
        String middleName = person.getMiddleName();
        if (middleName != null) {
            builder.setPropertyString("middleName", middleName);
        }
        String familyName = person.getFamilyName();
        if (familyName != null) {
            builder.setPropertyString("familyName", familyName);
        }
        String str = person.mExternalUri;
        if (str != null) {
            builder.setPropertyString("externalUri", str);
        }
        String str2 = person.mImageUri;
        if (str2 != null) {
            builder.setPropertyString("imageUri", str2);
        }
        builder.setPropertyBoolean("isImportant", person.mIsImportant);
        builder.setPropertyBoolean("isBot", person.mIsBot);
        List notes = person.getNotes();
        if (notes != null) {
            builder.setPropertyString("notes", (String[]) notes.toArray(new String[0]));
        }
        List list = person.mAdditionalNameTypes;
        if (list != null) {
            long[] jArr = new long[list.size()];
            Iterator it2 = list.iterator();
            int i3 = 0;
            while (it2.hasNext()) {
                jArr[i3] = ((Long) it2.next()).longValue();
                i3++;
            }
            builder.setPropertyLong("additionalNameTypes", jArr);
        }
        List list2 = person.mAdditionalNames;
        if (list2 != null) {
            builder.setPropertyString("additionalNames", (String[]) list2.toArray(new String[0]));
        }
        List affiliations = person.getAffiliations();
        if (affiliations != null) {
            builder.setPropertyString("affiliations", (String[]) affiliations.toArray(new String[0]));
        }
        List relations = person.getRelations();
        if (relations != null) {
            builder.setPropertyString("relations", (String[]) relations.toArray(new String[0]));
        }
        List contactPoints = person.getContactPoints();
        if (contactPoints != null) {
            GenericDocument[] genericDocumentArr2 = new GenericDocument[contactPoints.size()];
            Iterator it3 = contactPoints.iterator();
            while (it3.hasNext()) {
                genericDocumentArr2[i] = GenericDocument.fromDocumentClass((ContactPoint) it3.next());
                i++;
            }
            builder.setPropertyDocument("contactPoints", genericDocumentArr2);
        }
        return builder.build();
    }
}
