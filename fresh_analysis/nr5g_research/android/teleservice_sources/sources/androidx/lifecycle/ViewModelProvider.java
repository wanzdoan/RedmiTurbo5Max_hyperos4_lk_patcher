package androidx.lifecycle;

import android.app.Application;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.lifecycle.viewmodel.internal.JvmViewModelProviders;
import androidx.lifecycle.viewmodel.internal.ViewModelProviderImpl;
import androidx.lifecycle.viewmodel.internal.ViewModelProviders;
import androidx.savedstate.Recreator$$ExternalSyntheticBUOutline0;
import java.lang.reflect.InvocationTargetException;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;
import kotlin.text.CharsKt;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;
import miuix.animation.motion.MergeMotion$$ExternalSyntheticBUOutline0;

/* JADX INFO: compiled from: ViewModelProvider.android.kt */
/* JADX INFO: loaded from: classes.dex */
public class ViewModelProvider {
    public static final Companion Companion = new Companion(null);
    public static final CreationExtras.Key VIEW_MODEL_KEY;
    private final ViewModelProviderImpl impl;

    /* JADX INFO: compiled from: ViewModelProvider.android.kt */
    public abstract class OnRequeryFactory {
        public abstract void onRequery(ViewModel viewModel);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public ViewModelProvider(ViewModelStore viewModelStore, Factory factory) {
        this(viewModelStore, factory, null, 4, null);
        viewModelStore.getClass();
        factory.getClass();
    }

    private ViewModelProvider(ViewModelProviderImpl viewModelProviderImpl) {
        this.impl = viewModelProviderImpl;
    }

    public /* synthetic */ ViewModelProvider(ViewModelStore viewModelStore, Factory factory, CreationExtras creationExtras, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(viewModelStore, factory, (i & 4) != 0 ? CreationExtras.Empty.INSTANCE : creationExtras);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public ViewModelProvider(ViewModelStore viewModelStore, Factory factory, CreationExtras creationExtras) {
        this(new ViewModelProviderImpl(viewModelStore, factory, creationExtras));
        viewModelStore.getClass();
        factory.getClass();
        creationExtras.getClass();
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public ViewModelProvider(ViewModelStoreOwner viewModelStoreOwner) {
        this(viewModelStoreOwner.getViewModelStore(), ViewModelStoreOwnerDefaults.getViewModelProviderFactory(viewModelStoreOwner), ViewModelStoreOwnerDefaults.getViewModelCreationExtras(viewModelStoreOwner));
        viewModelStoreOwner.getClass();
    }

    public final ViewModel get(KClass kClass) {
        kClass.getClass();
        return ViewModelProviderImpl.getViewModel$lifecycle_viewmodel$default(this.impl, kClass, null, 2, null);
    }

    /* JADX INFO: compiled from: ViewModelProvider.android.kt */
    public final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static /* synthetic */ ViewModelProvider create$default(Companion companion, ViewModelStoreOwner viewModelStoreOwner, Factory factory, CreationExtras creationExtras, int i, Object obj) {
            if ((i & 2) != 0) {
                factory = ViewModelStoreOwnerDefaults.getViewModelProviderFactory(viewModelStoreOwner);
            }
            if ((i & 4) != 0) {
                creationExtras = ViewModelStoreOwnerDefaults.getViewModelCreationExtras(viewModelStoreOwner);
            }
            return companion.create(viewModelStoreOwner, factory, creationExtras);
        }

        private Companion() {
        }

        public final ViewModelProvider create(ViewModelStoreOwner viewModelStoreOwner, Factory factory, CreationExtras creationExtras) {
            viewModelStoreOwner.getClass();
            factory.getClass();
            creationExtras.getClass();
            return new ViewModelProvider(viewModelStoreOwner.getViewModelStore(), factory, creationExtras);
        }
    }

    public ViewModel get(Class cls) {
        cls.getClass();
        return get(JvmClassMappingKt.getKotlinClass(cls));
    }

    public final ViewModel get(String str, KClass kClass) {
        str.getClass();
        kClass.getClass();
        return this.impl.getViewModel$lifecycle_viewmodel(kClass, str);
    }

    /* JADX INFO: compiled from: ViewModelProvider.android.kt */
    public interface Factory {
        public static final Companion Companion = Companion.$$INSTANCE;

        default ViewModel create(Class cls) {
            cls.getClass();
            return ViewModelProviders.INSTANCE.unsupportedCreateViewModel$lifecycle_viewmodel();
        }

        default ViewModel create(Class cls, CreationExtras creationExtras) {
            cls.getClass();
            creationExtras.getClass();
            return create(cls);
        }

        default ViewModel create(KClass kClass, CreationExtras creationExtras) {
            kClass.getClass();
            creationExtras.getClass();
            return create(JvmClassMappingKt.getJavaClass(kClass), creationExtras);
        }

        /* JADX INFO: compiled from: ViewModelProvider.android.kt */
        public final class Companion {
            static final /* synthetic */ Companion $$INSTANCE = new Companion();

            private Companion() {
            }
        }
    }

    /* JADX INFO: compiled from: ViewModelProvider.android.kt */
    public class NewInstanceFactory implements Factory {
        public static final Companion Companion = new Companion(null);
        public static final CreationExtras.Key VIEW_MODEL_KEY = ViewModelProvider.VIEW_MODEL_KEY;
        private static NewInstanceFactory _instance;

        @Override // androidx.lifecycle.ViewModelProvider.Factory
        public ViewModel create(Class cls) {
            cls.getClass();
            return JvmViewModelProviders.INSTANCE.createViewModel(cls);
        }

        @Override // androidx.lifecycle.ViewModelProvider.Factory
        public ViewModel create(Class cls, CreationExtras creationExtras) {
            cls.getClass();
            creationExtras.getClass();
            return create(cls);
        }

        @Override // androidx.lifecycle.ViewModelProvider.Factory
        public ViewModel create(KClass kClass, CreationExtras creationExtras) {
            kClass.getClass();
            creationExtras.getClass();
            return create(JvmClassMappingKt.getJavaClass(kClass), creationExtras);
        }

        /* JADX INFO: compiled from: ViewModelProvider.android.kt */
        public final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }

            public final NewInstanceFactory getInstance() {
                if (NewInstanceFactory._instance == null) {
                    NewInstanceFactory._instance = new NewInstanceFactory();
                }
                NewInstanceFactory newInstanceFactory = NewInstanceFactory._instance;
                newInstanceFactory.getClass();
                return newInstanceFactory;
            }
        }
    }

    /* JADX INFO: compiled from: ViewModelProvider.android.kt */
    public class AndroidViewModelFactory extends NewInstanceFactory {
        public static final CreationExtras.Key APPLICATION_KEY;
        public static final Companion Companion = new Companion(null);
        private static AndroidViewModelFactory _instance;
        private final Application application;

        private AndroidViewModelFactory(Application application, int i) {
            this.application = application;
        }

        public AndroidViewModelFactory() {
            this(null, 0);
        }

        /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
        public AndroidViewModelFactory(Application application) {
            this(application, 0);
            application.getClass();
        }

        @Override // androidx.lifecycle.ViewModelProvider.NewInstanceFactory, androidx.lifecycle.ViewModelProvider.Factory
        public ViewModel create(Class cls, CreationExtras creationExtras) {
            cls.getClass();
            creationExtras.getClass();
            if (this.application != null) {
                return create(cls);
            }
            Application application = (Application) creationExtras.get(APPLICATION_KEY);
            if (application != null) {
                return create(cls, application);
            }
            if (AndroidViewModel.class.isAssignableFrom(cls)) {
                Folme$$ExternalSyntheticBUOutline0.m("CreationExtras must have an application by `APPLICATION_KEY`");
                return null;
            }
            return super.create(cls);
        }

        @Override // androidx.lifecycle.ViewModelProvider.NewInstanceFactory, androidx.lifecycle.ViewModelProvider.Factory
        public ViewModel create(Class cls) {
            cls.getClass();
            Application application = this.application;
            if (application == null) {
                MergeMotion$$ExternalSyntheticBUOutline0.m("AndroidViewModelFactory constructed with empty constructor works only with create(modelClass: Class<T>, extras: CreationExtras).");
                return null;
            }
            return create(cls, application);
        }

        private final ViewModel create(Class cls, Application application) {
            if (AndroidViewModel.class.isAssignableFrom(cls)) {
                try {
                    ViewModel viewModel = (ViewModel) cls.getConstructor(Application.class).newInstance(application);
                    viewModel.getClass();
                    return viewModel;
                } catch (IllegalAccessException e) {
                    Recreator$$ExternalSyntheticBUOutline0.m("Cannot create an instance of ", cls, e);
                    return null;
                } catch (InstantiationException e2) {
                    Recreator$$ExternalSyntheticBUOutline0.m("Cannot create an instance of ", cls, e2);
                    return null;
                } catch (NoSuchMethodException e3) {
                    Recreator$$ExternalSyntheticBUOutline0.m("Cannot create an instance of ", cls, e3);
                    return null;
                } catch (InvocationTargetException e4) {
                    Recreator$$ExternalSyntheticBUOutline0.m("Cannot create an instance of ", cls, e4);
                    return null;
                }
            }
            return super.create(cls);
        }

        /* JADX INFO: compiled from: ViewModelProvider.android.kt */
        public final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }

            public final AndroidViewModelFactory getInstance(Application application) {
                application.getClass();
                if (AndroidViewModelFactory._instance == null) {
                    AndroidViewModelFactory._instance = new AndroidViewModelFactory(application);
                }
                AndroidViewModelFactory androidViewModelFactory = AndroidViewModelFactory._instance;
                androidViewModelFactory.getClass();
                return androidViewModelFactory;
            }
        }

        static {
            CreationExtras.Companion companion = CreationExtras.Companion;
            APPLICATION_KEY = new CreationExtras.Key() { // from class: androidx.lifecycle.ViewModelProvider$AndroidViewModelFactory$special$$inlined$Key$1
                public String toString() {
                    String string = Integer.toString(hashCode(), CharsKt.checkRadix(16));
                    string.getClass();
                    return "CreationExtras.Key@" + string + '<' + Reflection.getOrCreateKotlinClass(Application.class).getSimpleName() + '>';
                }
            };
        }
    }

    static {
        CreationExtras.Companion companion = CreationExtras.Companion;
        VIEW_MODEL_KEY = new CreationExtras.Key() { // from class: androidx.lifecycle.ViewModelProvider$special$$inlined$Key$1
            public String toString() {
                String string = Integer.toString(hashCode(), CharsKt.checkRadix(16));
                string.getClass();
                return "CreationExtras.Key@" + string + '<' + Reflection.getOrCreateKotlinClass(String.class).getSimpleName() + '>';
            }
        };
    }
}
