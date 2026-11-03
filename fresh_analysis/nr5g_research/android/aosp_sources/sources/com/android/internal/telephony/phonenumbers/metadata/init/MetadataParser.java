package com.android.internal.telephony.phonenumbers.metadata.init;

import com.android.internal.telephony.Phone$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.phonenumbers.Phonemetadata$PhoneMetadata;
import com.android.internal.telephony.phonenumbers.Phonemetadata$PhoneMetadataCollection;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* JADX INFO: loaded from: classes.dex */
public final class MetadataParser {
    private static final Logger logger = Logger.getLogger(MetadataParser.class.getName());
    private final boolean strictMode;

    public static MetadataParser newLenientParser() {
        return new MetadataParser(false);
    }

    public static MetadataParser newStrictParser() {
        return new MetadataParser(true);
    }

    private MetadataParser(boolean z) {
        this.strictMode = z;
    }

    /* JADX WARN: Code duplicated, block: B:25:0x0046  */
    /* JADX WARN: Code duplicated, block: B:26:0x004a  */
    public Collection<Phonemetadata$PhoneMetadata> parse(InputStream inputStream) throws Throwable {
        Throwable th;
        IOException e;
        if (inputStream == null) {
            return handleNullSource();
        }
        try {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                try {
                    Phonemetadata$PhoneMetadataCollection phonemetadata$PhoneMetadataCollection = new Phonemetadata$PhoneMetadataCollection();
                    phonemetadata$PhoneMetadataCollection.readExternal(objectInputStream);
                    if (phonemetadata$PhoneMetadataCollection.getMetadataList().isEmpty()) {
                        throw new IllegalStateException("Empty metadata");
                    }
                    List<Phonemetadata$PhoneMetadata> metadataList = phonemetadata$PhoneMetadataCollection.getMetadataList();
                    close(objectInputStream);
                    return metadataList;
                } catch (IOException e2) {
                    e = e2;
                    throw new IllegalStateException("Unable to parse metadata file", e);
                }
            } catch (Throwable th2) {
                th = th2;
                if (0 != 0) {
                    close(null);
                } else {
                    close(inputStream);
                }
                throw th;
            }
        } catch (IOException e3) {
            e = e3;
        } catch (Throwable th3) {
            th = th3;
            if (0 != 0) {
                close(null);
            } else {
                close(inputStream);
            }
            throw th;
        }
    }

    private List handleNullSource() {
        if (this.strictMode) {
            Phone$$ExternalSyntheticBUOutline0.m("Source cannot be null");
            return null;
        }
        return Collections.EMPTY_LIST;
    }

    private void close(InputStream inputStream) {
        try {
            inputStream.close();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error closing input stream (ignored)", (Throwable) e);
        }
    }
}
