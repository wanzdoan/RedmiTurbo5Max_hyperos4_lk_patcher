package com.google.common.util.concurrent;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.AbstractOwnableSynchronizer;

/* JADX INFO: loaded from: classes.dex */
abstract class InterruptibleTask extends AtomicReference implements Runnable {

    final class Blocker extends AbstractOwnableSynchronizer implements Runnable {
        private final InterruptibleTask task;

        @Override // java.lang.Runnable
        public void run() {
        }

        Thread getOwner() {
            return super.getExclusiveOwnerThread();
        }

        public String toString() {
            throw null;
        }
    }
}
