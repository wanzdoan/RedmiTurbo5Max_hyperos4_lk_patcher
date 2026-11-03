package com.google.common.collect;

/* JADX INFO: loaded from: classes.dex */
class EmptyImmutableListMultimap extends ImmutableListMultimap {
    static final EmptyImmutableListMultimap INSTANCE = new EmptyImmutableListMultimap();
    private static final long serialVersionUID = 0;

    private EmptyImmutableListMultimap() {
        super(ImmutableMap.of(), 0);
    }

    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public ImmutableMap asMap() {
        return super.asMap();
    }

    private Object readResolve() {
        return INSTANCE;
    }
}
