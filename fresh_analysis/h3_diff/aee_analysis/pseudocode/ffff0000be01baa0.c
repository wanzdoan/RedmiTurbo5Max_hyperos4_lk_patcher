
void FUN_ffff0000be01baa0(long param_1)

{
  int iVar1;
  long *plVar2;
  long lVar3;
  long *plVar4;
  
  if ((long **)DAT_ffff0000be10cc48 != &DAT_ffff0000be10cc40) {
    iVar1 = (int)DAT_ffff0000be10cc48[2] - *(int *)(param_1 + 0x18);
    plVar4 = DAT_ffff0000be10cc48;
    while( true ) {
      if (0 < iVar1) {
        plVar2 = (long *)(param_1 + 8);
        *plVar2 = *plVar4;
        lVar3 = *plVar4;
        *(long **)(param_1 + 0x10) = plVar4;
        *plVar4 = (long)plVar2;
        *(long **)(lVar3 + 8) = plVar2;
        return;
      }
      plVar4 = (long *)plVar4[1];
      if ((long **)plVar4 == &DAT_ffff0000be10cc40) break;
      iVar1 = (int)plVar4[2] - *(int *)(param_1 + 0x18);
    }
  }
  plVar4 = (long *)(param_1 + 8);
  *plVar4 = (long)DAT_ffff0000be10cc40;
  *(long ***)(param_1 + 0x10) = &DAT_ffff0000be10cc40;
  *(long **)((long)DAT_ffff0000be10cc40 + 8) = plVar4;
  DAT_ffff0000be10cc40 = plVar4;
  return;
}


