
void FUN_ffff0000be09645c(long param_1)

{
  bool bVar1;
  uint uVar2;
  long *plVar3;
  long lVar4;
  uint local_28;
  
  local_28 = 0xffffffc8;
  plVar3 = (long *)register0x00000008;
  do {
    if (param_1 == 0) {
      return;
    }
    while( true ) {
      FUN_ffff0000be05fd98(&DAT_ffff0000be0ac42b,param_1);
      lVar4 = (long)(int)local_28;
      if ((-1 < (int)local_28) ||
         (uVar2 = local_28 + 8, bVar1 = 0xfffffff8 < local_28, local_28 = uVar2, bVar1)) break;
      param_1 = *(long *)(&stack0xffffffffffffffc0 + lVar4);
      if (param_1 == 0) {
        return;
      }
    }
    param_1 = *plVar3;
    plVar3 = plVar3 + 1;
  } while( true );
}


