package kotlinx.coroutines.scheduling;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.locks.LockSupport;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$ObjectRef;
import kotlin.ranges.RangesKt;
import kotlinx.atomicfu.AtomicBoolean;
import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicInt;
import kotlinx.atomicfu.AtomicLong;
import kotlinx.coroutines.AbstractTimeSourceKt;
import kotlinx.coroutines.DebugStringsKt;
import kotlinx.coroutines.internal.ResizableAtomicArray;
import kotlinx.coroutines.internal.Symbol;

/* JADX INFO: compiled from: CoroutineScheduler.kt */
/* JADX INFO: loaded from: classes2.dex */
public final class CoroutineScheduler implements Executor, Closeable {
    public static final Companion Companion = new Companion(null);
    public static final Symbol NOT_IN_STACK = new Symbol("NOT_IN_STACK");
    private final AtomicBoolean _isTerminated;
    private final AtomicLong controlState;
    public final int corePoolSize;
    public final GlobalQueue globalBlockingQueue;
    public final GlobalQueue globalCpuQueue;
    public final long idleWorkerKeepAliveNs;
    public final int maxPoolSize;
    private final AtomicLong parkedWorkersStack;
    public final String schedulerName;
    public final ResizableAtomicArray workers;

    /* JADX INFO: compiled from: CoroutineScheduler.kt */
    public abstract /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[WorkerState.values().length];
            try {
                iArr[WorkerState.PARKING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[WorkerState.BLOCKING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[WorkerState.CPU_ACQUIRED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[WorkerState.DORMANT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                iArr[WorkerState.TERMINATED.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    /* JADX INFO: compiled from: CoroutineScheduler.kt */
    public enum WorkerState {
        CPU_ACQUIRED,
        BLOCKING,
        PARKING,
        DORMANT,
        TERMINATED;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());
    }

    private final boolean addToGlobalQueue(Task task) {
        if (task.taskContext) {
            return this.globalBlockingQueue.addLast(task);
        }
        return this.globalCpuQueue.addLast(task);
    }

    public CoroutineScheduler(int i, int i2, long j, String str) {
        str.getClass();
        this.corePoolSize = i;
        this.maxPoolSize = i2;
        this.idleWorkerKeepAliveNs = j;
        this.schedulerName = str;
        if (i < 1) {
            throw new IllegalArgumentException(("Core pool size " + i + " should be at least 1").toString());
        }
        if (i2 < i) {
            throw new IllegalArgumentException(("Max pool size " + i2 + " should be greater than or equals to core pool size " + i).toString());
        }
        if (i2 > 2097150) {
            throw new IllegalArgumentException(("Max pool size " + i2 + " should not exceed maximal supported number of threads 2097150").toString());
        }
        if (j <= 0) {
            throw new IllegalArgumentException(("Idle worker keep alive time " + j + " must be positive").toString());
        }
        this.globalCpuQueue = new GlobalQueue();
        this.globalBlockingQueue = new GlobalQueue();
        this.parkedWorkersStack = AtomicFU.atomic(0L);
        this.workers = new ResizableAtomicArray((i + 1) * 2);
        this.controlState = AtomicFU.atomic(((long) i) << 42);
        this._isTerminated = AtomicFU.atomic(false);
    }

    public final void parkedWorkersStackTopUpdate(Worker worker, int i, int i2) {
        worker.getClass();
        AtomicLong atomicLong = this.parkedWorkersStack;
        while (true) {
            long value = atomicLong.getValue();
            int iParkedWorkersStackNextIndex = (int) (2097151 & value);
            long j = (2097152 + value) & (-2097152);
            if (iParkedWorkersStackNextIndex == i) {
                iParkedWorkersStackNextIndex = i2 == 0 ? parkedWorkersStackNextIndex(worker) : i2;
            }
            if (iParkedWorkersStackNextIndex >= 0 && this.parkedWorkersStack.compareAndSet(value, j | ((long) iParkedWorkersStackNextIndex))) {
                return;
            }
        }
    }

    public final boolean parkedWorkersStackPush(Worker worker) {
        long value;
        int indexInArray;
        worker.getClass();
        if (worker.getNextParkedWorker() != NOT_IN_STACK) {
            return false;
        }
        AtomicLong atomicLong = this.parkedWorkersStack;
        do {
            value = atomicLong.getValue();
            indexInArray = worker.getIndexInArray();
            worker.setNextParkedWorker(this.workers.get((int) (2097151 & value)));
        } while (!this.parkedWorkersStack.compareAndSet(value, ((2097152 + value) & (-2097152)) | ((long) indexInArray)));
        return true;
    }

    private final Worker parkedWorkersStackPop() {
        AtomicLong atomicLong = this.parkedWorkersStack;
        while (true) {
            long value = atomicLong.getValue();
            Worker worker = (Worker) this.workers.get((int) (2097151 & value));
            if (worker == null) {
                return null;
            }
            long j = (2097152 + value) & (-2097152);
            int iParkedWorkersStackNextIndex = parkedWorkersStackNextIndex(worker);
            if (iParkedWorkersStackNextIndex >= 0 && this.parkedWorkersStack.compareAndSet(value, j | ((long) iParkedWorkersStackNextIndex))) {
                worker.setNextParkedWorker(NOT_IN_STACK);
                return worker;
            }
        }
    }

    private final int parkedWorkersStackNextIndex(Worker worker) {
        Object nextParkedWorker = worker.getNextParkedWorker();
        while (nextParkedWorker != NOT_IN_STACK) {
            if (nextParkedWorker == null) {
                return 0;
            }
            Worker worker2 = (Worker) nextParkedWorker;
            int indexInArray = worker2.getIndexInArray();
            if (indexInArray != 0) {
                return indexInArray;
            }
            nextParkedWorker = worker2.getNextParkedWorker();
        }
        return -1;
    }

    public final boolean isTerminated() {
        return this._isTerminated.getValue();
    }

    /* JADX INFO: compiled from: CoroutineScheduler.kt */
    public final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable runnable) {
        runnable.getClass();
        dispatch$default(this, runnable, false, false, 6, null);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws InterruptedException {
        shutdown(10000L);
    }

    /* JADX WARN: Code duplicated, block: B:24:0x005d  */
    public final void shutdown(long j) throws InterruptedException {
        int value;
        Task taskFindTask;
        if (this._isTerminated.compareAndSet(false, true)) {
            Worker workerCurrentWorker = currentWorker();
            synchronized (this.workers) {
                value = (int) (this.controlState.getValue() & 2097151);
            }
            if (1 <= value) {
                int i = 1;
                while (true) {
                    Object obj = this.workers.get(i);
                    obj.getClass();
                    Worker worker = (Worker) obj;
                    if (worker != workerCurrentWorker) {
                        while (worker.getState() != Thread.State.TERMINATED) {
                            LockSupport.unpark(worker);
                            worker.join(j);
                        }
                        worker.localQueue.offloadAllWorkTo(this.globalBlockingQueue);
                    }
                    if (i == value) {
                        break;
                    } else {
                        i++;
                    }
                }
            }
            this.globalBlockingQueue.close();
            this.globalCpuQueue.close();
            while (true) {
                if (workerCurrentWorker != null) {
                    taskFindTask = workerCurrentWorker.findTask(true);
                    if (taskFindTask == null) {
                        taskFindTask = (Task) this.globalCpuQueue.removeFirstOrNull();
                        if (taskFindTask == null) {
                            continue;
                        }
                    } else {
                        continue;
                    }
                } else {
                    taskFindTask = (Task) this.globalCpuQueue.removeFirstOrNull();
                    if (taskFindTask == null && (taskFindTask = (Task) this.globalBlockingQueue.removeFirstOrNull()) == null) {
                        break;
                    }
                }
                runSafely(taskFindTask);
            }
            if (workerCurrentWorker != null) {
                workerCurrentWorker.tryReleaseCpu(WorkerState.TERMINATED);
            }
            this.parkedWorkersStack.setValue(0L);
            this.controlState.setValue(0L);
        }
    }

    public static /* synthetic */ void dispatch$default(CoroutineScheduler coroutineScheduler, Runnable runnable, boolean z, boolean z2, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        if ((i & 4) != 0) {
            z2 = false;
        }
        coroutineScheduler.dispatch(runnable, z, z2);
    }

    public final void dispatch(Runnable runnable, boolean z, boolean z2) {
        runnable.getClass();
        AbstractTimeSourceKt.access$getTimeSource$p();
        Task taskCreateTask = createTask(runnable, z);
        boolean z3 = taskCreateTask.taskContext;
        long jAddAndGet = z3 ? this.controlState.addAndGet(2097152L) : 0L;
        Task taskSubmitToLocalQueue = submitToLocalQueue(currentWorker(), taskCreateTask, z2);
        if (taskSubmitToLocalQueue == null || addToGlobalQueue(taskSubmitToLocalQueue)) {
            if (z3) {
                signalBlockingWork(jAddAndGet);
                return;
            } else {
                signalCpuWork();
                return;
            }
        }
        throw new RejectedExecutionException(this.schedulerName + " was terminated");
    }

    public final Task createTask(Runnable runnable, boolean z) {
        runnable.getClass();
        long jNanoTime = TasksKt.schedulerTimeSource.nanoTime();
        if (runnable instanceof Task) {
            Task task = (Task) runnable;
            task.submissionTime = jNanoTime;
            task.taskContext = z;
            return task;
        }
        return TasksKt.asTask(runnable, jNanoTime, z);
    }

    private final void signalBlockingWork(long j) {
        if (tryUnpark() || tryCreateWorker(j)) {
            return;
        }
        tryUnpark();
    }

    public final void signalCpuWork() {
        if (tryUnpark() || tryCreateWorker$default(this, 0L, 1, null)) {
            return;
        }
        tryUnpark();
    }

    static /* synthetic */ boolean tryCreateWorker$default(CoroutineScheduler coroutineScheduler, long j, int i, Object obj) {
        if ((i & 1) != 0) {
            j = coroutineScheduler.controlState.getValue();
        }
        return coroutineScheduler.tryCreateWorker(j);
    }

    private final boolean tryCreateWorker(long j) {
        if (RangesKt.coerceAtLeast(((int) (2097151 & j)) - ((int) ((j & 4398044413952L) >> 21)), 0) < this.corePoolSize) {
            int iCreateNewWorker = createNewWorker();
            if (iCreateNewWorker == 1 && this.corePoolSize > 1) {
                createNewWorker();
            }
            if (iCreateNewWorker > 0) {
                return true;
            }
        }
        return false;
    }

    private final boolean tryUnpark() {
        Worker workerParkedWorkersStackPop;
        do {
            workerParkedWorkersStackPop = parkedWorkersStackPop();
            if (workerParkedWorkersStackPop == null) {
                return false;
            }
        } while (!workerParkedWorkersStackPop.getWorkerCtl().compareAndSet(-1, 0));
        LockSupport.unpark(workerParkedWorkersStackPop);
        return true;
    }

    private final int createNewWorker() {
        synchronized (this.workers) {
            try {
                if (isTerminated()) {
                    return -1;
                }
                long value = this.controlState.getValue();
                int i = (int) (value & 2097151);
                int iCoerceAtLeast = RangesKt.coerceAtLeast(i - ((int) ((value & 4398044413952L) >> 21)), 0);
                if (iCoerceAtLeast >= this.corePoolSize) {
                    return 0;
                }
                if (i >= this.maxPoolSize) {
                    return 0;
                }
                int value2 = ((int) (this.controlState.getValue() & 2097151)) + 1;
                if (value2 <= 0 || this.workers.get(value2) != null) {
                    throw new IllegalArgumentException("Failed requirement.");
                }
                Worker worker = new Worker(this, value2);
                this.workers.setSynchronized(value2, worker);
                if (value2 != ((int) (2097151 & this.controlState.incrementAndGet()))) {
                    throw new IllegalArgumentException("Failed requirement.");
                }
                int i2 = iCoerceAtLeast + 1;
                worker.start();
                return i2;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private final Task submitToLocalQueue(Worker worker, Task task, boolean z) {
        WorkerState workerState;
        if (worker == null || (workerState = worker.state) == WorkerState.TERMINATED) {
            return task;
        }
        if (!task.taskContext && workerState == WorkerState.BLOCKING) {
            return task;
        }
        worker.mayHaveLocalTasks = true;
        return worker.localQueue.add(task, z);
    }

    private final Worker currentWorker() {
        Thread threadCurrentThread = Thread.currentThread();
        Worker worker = threadCurrentThread instanceof Worker ? (Worker) threadCurrentThread : null;
        if (worker == null || !Intrinsics.areEqual(CoroutineScheduler.this, this)) {
            return null;
        }
        return worker;
    }

    public String toString() {
        ArrayList arrayList = new ArrayList();
        int iCurrentLength = this.workers.currentLength();
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        for (int i6 = 1; i6 < iCurrentLength; i6++) {
            Worker worker = (Worker) this.workers.get(i6);
            if (worker != null) {
                int size$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host = worker.localQueue.getSize$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
                int i7 = WhenMappings.$EnumSwitchMapping$0[worker.state.ordinal()];
                if (i7 == 1) {
                    i3++;
                } else if (i7 == 2) {
                    i2++;
                    arrayList.add(size$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host + "b");
                } else if (i7 == 3) {
                    i++;
                    arrayList.add(size$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host + "c");
                } else if (i7 == 4) {
                    i4++;
                    if (size$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host > 0) {
                        arrayList.add(size$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host + "d");
                    }
                } else {
                    if (i7 != 5) {
                        throw new NoWhenBranchMatchedException();
                    }
                    i5++;
                }
            }
        }
        long value = this.controlState.getValue();
        return this.schedulerName + "@" + DebugStringsKt.getHexAddress(this) + "[Pool Size {core = " + this.corePoolSize + ", max = " + this.maxPoolSize + "}, Worker States {CPU = " + i + ", blocking = " + i2 + ", parked = " + i3 + ", dormant = " + i4 + ", terminated = " + i5 + "}, running workers queues = " + arrayList + ", global CPU queue size = " + this.globalCpuQueue.getSize() + ", global blocking queue size = " + this.globalBlockingQueue.getSize() + ", Control State {created workers= " + ((int) (value & 2097151)) + ", blocking tasks = " + ((int) ((value & 4398044413952L) >> 21)) + ", CPUs acquired = " + (this.corePoolSize - ((int) ((value & 9223367638808264704L) >> 42))) + "}]";
    }

    public final void runSafely(Task task) {
        task.getClass();
        try {
            task.run();
            AbstractTimeSourceKt.access$getTimeSource$p();
        } catch (Throwable th) {
            try {
                Thread threadCurrentThread = Thread.currentThread();
                threadCurrentThread.getUncaughtExceptionHandler().uncaughtException(threadCurrentThread, th);
            } finally {
                AbstractTimeSourceKt.access$getTimeSource$p();
            }
        }
    }

    /* JADX INFO: compiled from: CoroutineScheduler.kt */
    public final class Worker extends Thread {
        private volatile int indexInArray;
        public final WorkQueue localQueue;
        public boolean mayHaveLocalTasks;
        private long minDelayUntilStealableTaskNs;
        private volatile Object nextParkedWorker;
        private int rngState;
        public WorkerState state;
        private final Ref$ObjectRef stolenTask;
        private long terminationDeadline;
        private final AtomicInt workerCtl;

        private Worker() {
            setDaemon(true);
            setContextClassLoader(CoroutineScheduler.this.getClass().getClassLoader());
            this.localQueue = new WorkQueue();
            this.stolenTask = new Ref$ObjectRef();
            this.state = WorkerState.DORMANT;
            this.workerCtl = AtomicFU.atomic(0);
            this.nextParkedWorker = CoroutineScheduler.NOT_IN_STACK;
            int iNanoTime = (int) System.nanoTime();
            this.rngState = iNanoTime == 0 ? 42 : iNanoTime;
        }

        public final int getIndexInArray() {
            return this.indexInArray;
        }

        public final void setIndexInArray(int i) {
            setName(CoroutineScheduler.this.schedulerName + "-worker-" + (i == 0 ? "TERMINATED" : String.valueOf(i)));
            this.indexInArray = i;
        }

        public Worker(CoroutineScheduler coroutineScheduler, int i) {
            this();
            setIndexInArray(i);
        }

        public final AtomicInt getWorkerCtl() {
            return this.workerCtl;
        }

        public final Object getNextParkedWorker() {
            return this.nextParkedWorker;
        }

        public final void setNextParkedWorker(Object obj) {
            this.nextParkedWorker = obj;
        }

        private final boolean tryAcquireCpuPermit() {
            long value;
            if (this.state == WorkerState.CPU_ACQUIRED) {
                return true;
            }
            CoroutineScheduler coroutineScheduler = CoroutineScheduler.this;
            AtomicLong atomicLong = coroutineScheduler.controlState;
            do {
                value = atomicLong.getValue();
                if (((int) ((9223367638808264704L & value) >> 42)) == 0) {
                    return false;
                }
            } while (!coroutineScheduler.controlState.compareAndSet(value, value - 4398046511104L));
            this.state = WorkerState.CPU_ACQUIRED;
            return true;
        }

        public final boolean tryReleaseCpu(WorkerState workerState) {
            workerState.getClass();
            WorkerState workerState2 = this.state;
            boolean z = workerState2 == WorkerState.CPU_ACQUIRED;
            if (z) {
                CoroutineScheduler.this.controlState.addAndGet(4398046511104L);
            }
            if (workerState2 != workerState) {
                this.state = workerState;
            }
            return z;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            runWorker();
        }

        private final void runWorker() {
            loop0: while (true) {
                boolean z = false;
                while (true) {
                    if (CoroutineScheduler.this.isTerminated() || this.state == WorkerState.TERMINATED) {
                        break loop0;
                    }
                    Task taskFindTask = findTask(this.mayHaveLocalTasks);
                    if (taskFindTask != null) {
                        this.minDelayUntilStealableTaskNs = 0L;
                        executeTask(taskFindTask);
                        break;
                    }
                    this.mayHaveLocalTasks = false;
                    if (this.minDelayUntilStealableTaskNs == 0) {
                        tryPark();
                    } else {
                        if (z) {
                            tryReleaseCpu(WorkerState.PARKING);
                            Thread.interrupted();
                            LockSupport.parkNanos(this.minDelayUntilStealableTaskNs);
                            this.minDelayUntilStealableTaskNs = 0L;
                            break;
                        }
                        z = true;
                    }
                }
            }
            tryReleaseCpu(WorkerState.TERMINATED);
        }

        private final void tryPark() {
            if (!inStack()) {
                CoroutineScheduler.this.parkedWorkersStackPush(this);
                return;
            }
            this.workerCtl.setValue(-1);
            while (inStack() && this.workerCtl.getValue() == -1 && !CoroutineScheduler.this.isTerminated() && this.state != WorkerState.TERMINATED) {
                tryReleaseCpu(WorkerState.PARKING);
                Thread.interrupted();
                park();
            }
        }

        private final boolean inStack() {
            return this.nextParkedWorker != CoroutineScheduler.NOT_IN_STACK;
        }

        private final void executeTask(Task task) {
            this.terminationDeadline = 0L;
            if (this.state == WorkerState.PARKING) {
                this.state = WorkerState.BLOCKING;
            }
            if (task.taskContext) {
                if (tryReleaseCpu(WorkerState.BLOCKING)) {
                    CoroutineScheduler.this.signalCpuWork();
                }
                CoroutineScheduler.this.runSafely(task);
                CoroutineScheduler.this.controlState.addAndGet(-2097152L);
                if (this.state != WorkerState.TERMINATED) {
                    this.state = WorkerState.DORMANT;
                    return;
                }
                return;
            }
            CoroutineScheduler.this.runSafely(task);
        }

        public final int nextInt(int i) {
            int i2 = this.rngState;
            int i3 = i2 ^ (i2 << 13);
            int i4 = i3 ^ (i3 >> 17);
            int i5 = i4 ^ (i4 << 5);
            this.rngState = i5;
            int i6 = i - 1;
            return (i6 & i) == 0 ? i6 & i5 : (Integer.MAX_VALUE & i5) % i;
        }

        private final void park() {
            if (this.terminationDeadline == 0) {
                this.terminationDeadline = System.nanoTime() + CoroutineScheduler.this.idleWorkerKeepAliveNs;
            }
            LockSupport.parkNanos(CoroutineScheduler.this.idleWorkerKeepAliveNs);
            if (System.nanoTime() - this.terminationDeadline >= 0) {
                this.terminationDeadline = 0L;
                tryTerminateWorker();
            }
        }

        private final void tryTerminateWorker() {
            CoroutineScheduler coroutineScheduler = CoroutineScheduler.this;
            synchronized (coroutineScheduler.workers) {
                try {
                    if (coroutineScheduler.isTerminated()) {
                        return;
                    }
                    if (((int) (coroutineScheduler.controlState.getValue() & 2097151)) <= coroutineScheduler.corePoolSize) {
                        return;
                    }
                    if (this.workerCtl.compareAndSet(-1, 1)) {
                        int i = this.indexInArray;
                        setIndexInArray(0);
                        coroutineScheduler.parkedWorkersStackTopUpdate(this, i, 0);
                        int andDecrement = (int) (coroutineScheduler.controlState.getAndDecrement() & 2097151);
                        if (andDecrement != i) {
                            Object obj = coroutineScheduler.workers.get(andDecrement);
                            obj.getClass();
                            Worker worker = (Worker) obj;
                            coroutineScheduler.workers.setSynchronized(i, worker);
                            worker.setIndexInArray(i);
                            coroutineScheduler.parkedWorkersStackTopUpdate(worker, andDecrement, i);
                        }
                        coroutineScheduler.workers.setSynchronized(andDecrement, null);
                        Unit unit = Unit.INSTANCE;
                        this.state = WorkerState.TERMINATED;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        public final Task findTask(boolean z) {
            return tryAcquireCpuPermit() ? findAnyTask(z) : findBlockingTask();
        }

        private final Task findBlockingTask() {
            Task taskPollBlocking = this.localQueue.pollBlocking();
            return (taskPollBlocking == null && (taskPollBlocking = (Task) CoroutineScheduler.this.globalBlockingQueue.removeFirstOrNull()) == null) ? trySteal(1) : taskPollBlocking;
        }

        private final Task findAnyTask(boolean z) {
            Task taskPollGlobalQueues;
            Task taskPollGlobalQueues2;
            if (z) {
                boolean z2 = nextInt(CoroutineScheduler.this.corePoolSize * 2) == 0;
                if (z2 && (taskPollGlobalQueues2 = pollGlobalQueues()) != null) {
                    return taskPollGlobalQueues2;
                }
                Task taskPoll = this.localQueue.poll();
                if (taskPoll != null) {
                    return taskPoll;
                }
                if (!z2 && (taskPollGlobalQueues = pollGlobalQueues()) != null) {
                    return taskPollGlobalQueues;
                }
            } else {
                Task taskPollGlobalQueues3 = pollGlobalQueues();
                if (taskPollGlobalQueues3 != null) {
                    return taskPollGlobalQueues3;
                }
            }
            return trySteal(3);
        }

        private final Task pollGlobalQueues() {
            int iNextInt = nextInt(2);
            CoroutineScheduler coroutineScheduler = CoroutineScheduler.this;
            if (iNextInt == 0) {
                Task task = (Task) coroutineScheduler.globalCpuQueue.removeFirstOrNull();
                return task != null ? task : (Task) CoroutineScheduler.this.globalBlockingQueue.removeFirstOrNull();
            }
            Task task2 = (Task) coroutineScheduler.globalBlockingQueue.removeFirstOrNull();
            return task2 != null ? task2 : (Task) CoroutineScheduler.this.globalCpuQueue.removeFirstOrNull();
        }

        private final Task trySteal(int i) {
            int value = (int) (CoroutineScheduler.this.controlState.getValue() & 2097151);
            if (value < 2) {
                return null;
            }
            int iNextInt = nextInt(value);
            CoroutineScheduler coroutineScheduler = CoroutineScheduler.this;
            long jMin = Long.MAX_VALUE;
            for (int i2 = 0; i2 < value; i2++) {
                iNextInt++;
                if (iNextInt > value) {
                    iNextInt = 1;
                }
                Worker worker = (Worker) coroutineScheduler.workers.get(iNextInt);
                if (worker != null && worker != this) {
                    long jTrySteal = worker.localQueue.trySteal(i, this.stolenTask);
                    if (jTrySteal == -1) {
                        Ref$ObjectRef ref$ObjectRef = this.stolenTask;
                        Task task = (Task) ref$ObjectRef.element;
                        ref$ObjectRef.element = null;
                        return task;
                    }
                    if (jTrySteal > 0) {
                        jMin = Math.min(jMin, jTrySteal);
                    }
                }
            }
            if (jMin == Long.MAX_VALUE) {
                jMin = 0;
            }
            this.minDelayUntilStealableTaskNs = jMin;
            return null;
        }
    }
}
