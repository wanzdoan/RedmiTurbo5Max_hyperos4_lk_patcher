package com.google.common.util.concurrent;

/* JADX INFO: loaded from: classes.dex */
public abstract class Striped {

    class LargeLazyStriped extends PowerOfTwoStriped {
    }

    abstract class PowerOfTwoStriped extends Striped {
    }

    class SmallLazyStriped extends PowerOfTwoStriped {
    }
}
