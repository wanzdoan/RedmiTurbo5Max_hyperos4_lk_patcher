package miuix.os;

import android.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.util.HashMap;
import miuix.appcompat.app.ProgressDialog;

/* JADX INFO: loaded from: classes2.dex */
public abstract class AsyncTaskWithProgress extends AsyncTask {
    private static final HashMap sAllTasks = new HashMap();
    private final FragmentManager mFragmentManager;
    private int mTheme = 0;
    private int mTitleId = 0;
    private CharSequence mTitle = null;
    private int mMessageId = 0;
    private CharSequence mMessage = null;
    private boolean mCancelable = false;
    private boolean mIndeterminate = false;
    private int mMaxProgress = 0;
    private int mProgressStyle = 0;
    private int mCurrentProgress = 0;
    private volatile ProgressDialogFragment mFragment = null;
    private final Listeners mListeners = new Listeners();

    public AsyncTaskWithProgress(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }

    public AsyncTaskWithProgress setMessage(int i) {
        this.mMessageId = i;
        this.mMessage = null;
        return this;
    }

    public AsyncTaskWithProgress setCancelable(boolean z) {
        this.mCancelable = z;
        return this;
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        String str = "AsyncTaskWithProgress@" + hashCode();
        sAllTasks.put(str, this);
        if (this.mFragmentManager != null) {
            this.mFragment = ProgressDialogFragment.newInstance(str);
            this.mFragment.setCancelable(this.mCancelable);
            Activity activity = getActivity();
            if ((activity == null || !(activity.isFinishing() || activity.isDestroyed())) && !this.mFragmentManager.isDestroyed()) {
                this.mFragment.show(this.mFragmentManager, str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onProgressUpdate(Integer... numArr) {
        super.onProgressUpdate((Object[]) numArr);
        this.mCurrentProgress = numArr[0].intValue();
        if (this.mFragment != null) {
            this.mFragment.setProgress(this.mCurrentProgress);
        }
    }

    @Override // android.os.AsyncTask
    protected void onPostExecute(Object obj) {
        sAllTasks.remove("AsyncTaskWithProgress@" + hashCode());
        dismissDialog();
    }

    @Override // android.os.AsyncTask
    public void onCancelled() {
        sAllTasks.remove("AsyncTaskWithProgress@" + hashCode());
        dismissDialog();
    }

    public Activity getActivity() {
        if (this.mFragment != null) {
            return this.mFragment.getActivity();
        }
        return null;
    }

    private void dismissDialog() {
        ProgressDialogFragment progressDialogFragment = (ProgressDialogFragment) this.mFragmentManager.findFragmentByTag("AsyncTaskWithProgress@" + hashCode());
        if (progressDialogFragment != null) {
            progressDialogFragment.dismissAllowingStateLoss();
        } else if (this.mFragment != null) {
            this.mFragment.dismissAllowingStateLoss();
        }
    }

    class Listeners implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {
        private Listeners() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            Dialog dialog;
            if (AsyncTaskWithProgress.this.mFragment == null || (dialog = AsyncTaskWithProgress.this.mFragment.getDialog()) == null || dialogInterface != dialog || i != -2) {
                return;
            }
            AsyncTaskWithProgress.this.cancel(true);
        }

        @Override // android.content.DialogInterface.OnCancelListener
        public void onCancel(DialogInterface dialogInterface) {
            onClick(dialogInterface, -2);
        }
    }

    public class ProgressDialogFragment extends DialogFragment {
        private AsyncTaskWithProgress mTask;

        static ProgressDialogFragment newInstance(String str) {
            ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString("task", str);
            progressDialogFragment.setArguments(bundle);
            return progressDialogFragment;
        }

        @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            AsyncTaskWithProgress asyncTaskWithProgress = (AsyncTaskWithProgress) AsyncTaskWithProgress.sAllTasks.get(getArguments().getString("task"));
            this.mTask = asyncTaskWithProgress;
            if (asyncTaskWithProgress == null) {
                FragmentTransaction fragmentTransactionBeginTransaction = getFragmentManager().beginTransaction();
                fragmentTransactionBeginTransaction.remove(this);
                fragmentTransactionBeginTransaction.commit();
            }
        }

        @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
        public void onStart() {
            super.onStart();
            AsyncTaskWithProgress asyncTaskWithProgress = this.mTask;
            if (asyncTaskWithProgress != null) {
                asyncTaskWithProgress.mFragment = this;
            }
        }

        @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
        public void onStop() {
            AsyncTaskWithProgress asyncTaskWithProgress = this.mTask;
            if (asyncTaskWithProgress != null) {
                asyncTaskWithProgress.mFragment = null;
            }
            super.onStop();
        }

        void setProgress(int i) {
            Dialog dialog = getDialog();
            if (dialog instanceof ProgressDialog) {
                ((ProgressDialog) dialog).setProgress(i);
            }
        }

        @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnCancelListener
        public void onCancel(DialogInterface dialogInterface) {
            AsyncTaskWithProgress asyncTaskWithProgress = this.mTask;
            if (asyncTaskWithProgress != null && asyncTaskWithProgress.mCancelable) {
                this.mTask.mListeners.onCancel(dialogInterface);
            }
            super.onCancel(dialogInterface);
        }

        @Override // androidx.fragment.app.DialogFragment
        public Dialog onCreateDialog(Bundle bundle) {
            if (this.mTask == null) {
                return super.onCreateDialog(bundle);
            }
            ProgressDialog progressDialog = new ProgressDialog(getActivity(), this.mTask.mTheme);
            int i = this.mTask.mTitleId;
            AsyncTaskWithProgress asyncTaskWithProgress = this.mTask;
            if (i != 0) {
                progressDialog.setTitle(asyncTaskWithProgress.mTitleId);
            } else {
                progressDialog.setTitle(asyncTaskWithProgress.mTitle);
            }
            if (this.mTask.mMessageId != 0) {
                progressDialog.setMessage(getActivity().getText(this.mTask.mMessageId));
            } else {
                progressDialog.setMessage(this.mTask.mMessage);
            }
            progressDialog.setProgressStyle(this.mTask.mProgressStyle);
            progressDialog.setIndeterminate(this.mTask.mIndeterminate);
            if (!this.mTask.mIndeterminate) {
                progressDialog.setMax(this.mTask.mMaxProgress);
                progressDialog.setProgress(this.mTask.mCurrentProgress);
            }
            if (this.mTask.mCancelable) {
                progressDialog.setButton(-2, progressDialog.getContext().getText(R.string.cancel), this.mTask.mListeners);
                progressDialog.setCancelable(true);
                return progressDialog;
            }
            progressDialog.setButton(-2, null, null);
            progressDialog.setCancelable(false);
            return progressDialog;
        }
    }
}
