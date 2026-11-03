package com.android.phone;

import android.os.FileObserver;
import android.util.Log;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes.dex */
public class FileObserverController {
    private static FileObserverController sInstance = new FileObserverController();
    private final ConcurrentHashMap mWrappers = new ConcurrentHashMap();

    public static FileObserverController getInstance() {
        return sInstance;
    }

    void register(FileEventListener fileEventListener) {
        FileObserverWrapper fileObserverWrapper = (FileObserverWrapper) this.mWrappers.get(fileEventListener.path);
        if (fileObserverWrapper == null) {
            fileObserverWrapper = new FileObserverWrapper(this, fileEventListener.path);
            this.mWrappers.put(fileEventListener.path, fileObserverWrapper);
        }
        fileObserverWrapper.addFileEventListener(fileEventListener);
    }

    void unregister(FileEventListener fileEventListener) {
        FileObserverWrapper fileObserverWrapper = (FileObserverWrapper) this.mWrappers.get(fileEventListener.path);
        if (fileObserverWrapper != null) {
            fileObserverWrapper.removeFileEventListener(fileEventListener);
            if (fileObserverWrapper.release()) {
                this.mWrappers.remove(fileEventListener.path);
            }
        }
    }

    public class FileObserverWrapper {
        private FileStateListener mFileObserver;
        private List mListeners = new CopyOnWriteArrayList();

        public FileObserverWrapper(FileObserverController fileObserverController, String str) {
            FileStateListener fileStateListener = new FileStateListener(str);
            this.mFileObserver = fileStateListener;
            fileStateListener.startWatching();
            Log.d("FileObserverWrapper", "startWatching " + this.mFileObserver + " path " + str);
        }

        public void addFileEventListener(FileEventListener fileEventListener) {
            this.mListeners.add(fileEventListener);
            Log.d("FileObserverWrapper", "addFileEventListener " + fileEventListener + " size " + this.mListeners.size());
        }

        public void removeFileEventListener(FileEventListener fileEventListener) {
            this.mListeners.remove(fileEventListener);
            Log.d("FileObserverWrapper", "removeFileEventListener " + fileEventListener + " size " + this.mListeners.size());
        }

        public boolean release() {
            if (!this.mListeners.isEmpty()) {
                return false;
            }
            this.mFileObserver.stopWatching();
            Log.d("FileObserverWrapper", "stopWatching " + this.mFileObserver);
            return true;
        }

        class FileStateListener extends FileObserver {
            public FileStateListener(String str) {
                super(str, 2);
            }

            @Override // android.os.FileObserver
            public void onEvent(int i, String str) {
                Log.d("FileObserverWrapper", "onEvent " + i + " mListeners " + FileObserverWrapper.this.mListeners.size());
                Iterator it = FileObserverWrapper.this.mListeners.iterator();
                while (it.hasNext()) {
                    ((FileEventListener) it.next()).onEvent(i, str);
                }
            }
        }
    }
}
