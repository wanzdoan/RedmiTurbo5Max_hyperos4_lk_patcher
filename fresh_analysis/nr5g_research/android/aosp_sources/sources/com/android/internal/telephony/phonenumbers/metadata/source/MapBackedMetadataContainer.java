package com.android.internal.telephony.phonenumbers.metadata.source;

import com.android.internal.telephony.phonenumbers.Phonemetadata$PhoneMetadata;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/* JADX INFO: loaded from: classes.dex */
final class MapBackedMetadataContainer<T> implements MetadataContainer {
    private final KeyProvider keyProvider;
    private final ConcurrentMap metadataMap = new ConcurrentHashMap();

    interface KeyProvider<T> {
        T getKeyOf(Phonemetadata$PhoneMetadata phonemetadata$PhoneMetadata);
    }

    static MapBackedMetadataContainer byRegionCode() {
        return new MapBackedMetadataContainer(new KeyProvider<String>() { // from class: com.android.internal.telephony.phonenumbers.metadata.source.MapBackedMetadataContainer.1
            @Override // com.android.internal.telephony.phonenumbers.metadata.source.MapBackedMetadataContainer.KeyProvider
            public String getKeyOf(Phonemetadata$PhoneMetadata phonemetadata$PhoneMetadata) {
                return phonemetadata$PhoneMetadata.getId();
            }
        });
    }

    static MapBackedMetadataContainer byCountryCallingCode() {
        return new MapBackedMetadataContainer(new KeyProvider<Integer>() { // from class: com.android.internal.telephony.phonenumbers.metadata.source.MapBackedMetadataContainer.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.android.internal.telephony.phonenumbers.metadata.source.MapBackedMetadataContainer.KeyProvider
            public Integer getKeyOf(Phonemetadata$PhoneMetadata phonemetadata$PhoneMetadata) {
                return Integer.valueOf(phonemetadata$PhoneMetadata.getCountryCode());
            }
        });
    }

    private MapBackedMetadataContainer(KeyProvider keyProvider) {
        this.keyProvider = keyProvider;
    }

    Phonemetadata$PhoneMetadata getMetadataBy(Object obj) {
        if (obj != null) {
            return (Phonemetadata$PhoneMetadata) this.metadataMap.get(obj);
        }
        return null;
    }

    KeyProvider getKeyProvider() {
        return this.keyProvider;
    }

    @Override // com.android.internal.telephony.phonenumbers.metadata.source.MetadataContainer
    public void accept(Phonemetadata$PhoneMetadata phonemetadata$PhoneMetadata) {
        this.metadataMap.put(this.keyProvider.getKeyOf(phonemetadata$PhoneMetadata), phonemetadata$PhoneMetadata);
    }
}
