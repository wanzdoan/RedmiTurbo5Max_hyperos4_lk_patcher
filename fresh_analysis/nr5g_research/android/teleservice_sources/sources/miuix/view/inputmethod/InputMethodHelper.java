package miuix.view.inputmethod;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import miuix.core.util.SoftReferenceSingleton;

/* JADX INFO: loaded from: classes2.dex */
public class InputMethodHelper {
    private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() { // from class: miuix.view.inputmethod.InputMethodHelper.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // miuix.core.util.SoftReferenceSingleton
        public InputMethodHelper createInstance(Object obj) {
            return new InputMethodHelper((Context) obj);
        }
    };
    private InputMethodManager mManager;

    private InputMethodHelper(Context context) {
        this.mManager = (InputMethodManager) context.getApplicationContext().getSystemService("input_method");
    }

    public static InputMethodHelper getInstance(Context context) {
        return (InputMethodHelper) INSTANCE.get(context);
    }

    public void showKeyBoard(EditText editText) {
        editText.requestFocus();
        this.mManager.viewClicked(editText);
        this.mManager.showSoftInput(editText, 0);
    }
}
