// ffff000090718ae0  size=140  FUN_ffff000090718ae0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090718ae0(long param_1)

{
  int iVar1;
  long *plVar2;
  long lVar3;
  long *plVar4;
  
  if (_DAT_ffff000090843c08 != (long *)&DAT_ffff000090843c00) {
    iVar1 = (int)_DAT_ffff000090843c08[2] - *(int *)(param_1 + 0x18);
    plVar4 = _DAT_ffff000090843c08;
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
      if (plVar4 == (long *)&DAT_ffff000090843c00) break;
      iVar1 = (int)plVar4[2] - *(int *)(param_1 + 0x18);
    }
  }
  plVar4 = (long *)(param_1 + 8);
  *plVar4 = (long)_DAT_ffff000090843c00;
  *(undefined1 **)(param_1 + 0x10) = &DAT_ffff000090843c00;
  *(long **)((long)_DAT_ffff000090843c00 + 8) = plVar4;
  _DAT_ffff000090843c00 = plVar4;
  return;
}


