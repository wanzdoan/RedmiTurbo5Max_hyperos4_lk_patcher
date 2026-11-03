package com.android.internal.telephony.phonenumbers.metadata.source;

import com.android.internal.telephony.phonenumbers.MetadataLoader;
import com.android.internal.telephony.phonenumbers.Phonemetadata$PhoneMetadata;
import com.android.internal.telephony.phonenumbers.metadata.init.MetadataParser;
import com.android.internal.telephony.phonenumbers.metadata.source.MetadataContainer;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
final class BlockingMetadataBootstrappingGuard<T extends MetadataContainer> implements MetadataBootstrappingGuard<T> {
    private final Map loadedFiles = new ConcurrentHashMap();
    private final MetadataContainer metadataContainer;
    private final MetadataLoader metadataLoader;
    private final MetadataParser metadataParser;

    BlockingMetadataBootstrappingGuard(MetadataLoader metadataLoader, MetadataParser metadataParser, MetadataContainer metadataContainer) {
        this.metadataLoader = metadataLoader;
        this.metadataParser = metadataParser;
        this.metadataContainer = metadataContainer;
    }

    @Override // com.android.internal.telephony.phonenumbers.metadata.source.MetadataBootstrappingGuard
    public T getOrBootstrap(String str) {
        if (!this.loadedFiles.containsKey(str)) {
            bootstrapMetadata(str);
        }
        return (T) this.metadataContainer;
    }

    private synchronized void bootstrapMetadata(String str) {
        try {
            if (this.loadedFiles.containsKey(str)) {
                return;
            }
            Iterator it = read(str).iterator();
            while (it.hasNext()) {
                this.metadataContainer.accept((Phonemetadata$PhoneMetadata) it.next());
            }
            this.loadedFiles.put(str, str);
        } catch (Throwable th) {
            throw th;
        }
    }

    private Collection read(String str) {
        try {
            return this.metadataParser.parse(this.metadataLoader.loadMetadata(str));
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new IllegalStateException("Failed to read file " + str, e);
        }
    }
}
