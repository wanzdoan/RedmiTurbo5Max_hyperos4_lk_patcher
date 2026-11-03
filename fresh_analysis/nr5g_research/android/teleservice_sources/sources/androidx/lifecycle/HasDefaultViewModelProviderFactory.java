package androidx.lifecycle;

import androidx.lifecycle.viewmodel.CreationExtras;

/* JADX INFO: compiled from: HasDefaultViewModelProviderFactory.kt */
/* JADX INFO: loaded from: classes.dex */
public interface HasDefaultViewModelProviderFactory {
    CreationExtras getDefaultViewModelCreationExtras();

    ViewModelProvider.Factory getDefaultViewModelProviderFactory();
}
