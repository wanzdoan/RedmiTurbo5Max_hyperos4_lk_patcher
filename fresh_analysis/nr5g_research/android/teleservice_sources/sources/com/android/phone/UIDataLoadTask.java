package com.android.phone;

import android.app.Activity;
import android.os.AsyncTask;
import androidx.fragment.app.Fragment;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes.dex */
public abstract class UIDataLoadTask extends AsyncTask {
    private WeakReference mReference;

    protected void doCancelled() {
    }

    protected void doCancelled(Object obj) {
    }

    protected abstract void doPrepare();

    protected void doProgressUpdate(Object[] objArr) {
    }

    protected abstract Object doTask(Object[] objArr);

    protected abstract void doUI(Object obj);

    public UIDataLoadTask(Object obj) {
        this.mReference = new WeakReference(obj);
    }

    @Override // android.os.AsyncTask
    protected Object doInBackground(Object[] objArr) {
        if (isExpired(this.mReference.get())) {
            return null;
        }
        return doTask(objArr);
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        if (isExpired(this.mReference.get())) {
            return;
        }
        doPrepare();
    }

    @Override // android.os.AsyncTask
    protected void onPostExecute(Object obj) {
        if (isExpired(this.mReference.get())) {
            return;
        }
        doUI(obj);
    }

    @Override // android.os.AsyncTask
    protected void onCancelled(Object obj) {
        if (isExpired(this.mReference.get())) {
            return;
        }
        doCancelled(obj);
    }

    @Override // android.os.AsyncTask
    protected void onCancelled() {
        if (isExpired(this.mReference.get())) {
            return;
        }
        doCancelled();
    }

    @Override // android.os.AsyncTask
    protected void onProgressUpdate(Object[] objArr) {
        if (isExpired(this.mReference.get())) {
            return;
        }
        doProgressUpdate(objArr);
    }

    public Object getReferenceObject() {
        return this.mReference.get();
    }

    private boolean isExpired(Object obj) {
        if (obj == null || isCancelled()) {
            return true;
        }
        if (obj instanceof Activity) {
            return ((Activity) obj).isFinishing();
        }
        if (!(obj instanceof Fragment)) {
            return false;
        }
        Fragment fragment = (Fragment) obj;
        return fragment.isDetached() || fragment.isRemoving();
    }
}
