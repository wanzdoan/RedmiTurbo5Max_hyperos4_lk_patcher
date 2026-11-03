// ffff0000907c0ee4  size=4  FUN_ffff0000907c0ee4

void FUN_ffff0000907c0ee4(long param_1)

{
  bool bVar1;
  uint uVar2;
  long *plVar3;
  long lVar4;
  uint uStack_28;
  
  uStack_28 = 0xffffffc8;
  plVar3 = (long *)register0x00000008;
  do {
    if (param_1 == 0) {
      return;
    }
    while( true ) {
      FUN_ffff00009077e11c(&DAT_ffff0000907dbf7c,param_1);
      lVar4 = (long)(int)uStack_28;
      if ((-1 < (int)uStack_28) ||
         (uVar2 = uStack_28 + 8, bVar1 = 0xfffffff8 < uStack_28, uStack_28 = uVar2, bVar1)) break;
      param_1 = *(long *)(&stack0xffffffffffffffc0 + lVar4);
      if (param_1 == 0) {
        return;
      }
    }
    param_1 = *plVar3;
    plVar3 = plVar3 + 1;
  } while( true );
}


