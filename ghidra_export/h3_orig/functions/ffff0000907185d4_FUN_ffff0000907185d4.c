// ffff0000907185d4  size=344  FUN_ffff0000907185d4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907185d4(long *param_1,int param_2,undefined4 param_3,long param_4,long param_5)

{
  undefined8 uVar1;
  ulong uVar2;
  int iVar3;
  long *plVar4;
  long lVar5;
  long *plVar6;
  
  plVar6 = param_1 + 1;
  if ((*plVar6 != 0) || (param_1[2] != 0)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cc18(s_timer__p_already_in_list_ffff0000907dda94,param_1);
  }
  iVar3 = FUN_ffff0000907167d4();
  param_1[4] = param_4;
  param_1[5] = param_5;
  *(int *)(param_1 + 3) = iVar3 + param_2;
  *(undefined4 *)((long)param_1 + 0x1c) = param_3;
  uVar1 = daif;
  if (((uint)uVar1 >> 7 & 1) == 0) {
    uVar2 = daif;
    daif = uVar2 | 0x80;
  }
  _DAT_ffff000090843b98 = 1;
  if (_DAT_ffff000090843bc8 == (long *)&DAT_ffff000090843bc0) {
LAB_ffff0000907186a4:
    param_1[1] = (long)_DAT_ffff000090843bc0;
    param_1[2] = (long)&DAT_ffff000090843bc0;
    _DAT_ffff000090843bc0[1] = (long)plVar6;
    _DAT_ffff000090843bc0 = plVar6;
  }
  else {
    iVar3 = (int)_DAT_ffff000090843bc8[2] - (int)param_1[3];
    plVar4 = _DAT_ffff000090843bc8;
    while (iVar3 < 1) {
      plVar4 = (long *)plVar4[1];
      if (plVar4 == (long *)&DAT_ffff000090843bc0) goto LAB_ffff0000907186a4;
      iVar3 = (int)plVar4[2] - (int)param_1[3];
    }
    lVar5 = *plVar4;
    param_1[2] = (long)plVar4;
    param_1[1] = lVar5;
    lVar5 = *plVar4;
    *plVar4 = (long)plVar6;
    *(long **)(lVar5 + 8) = plVar6;
  }
  plVar6 = (long *)0x0;
  if (_DAT_ffff000090843bc8 != (long *)&DAT_ffff000090843bc0) {
    plVar6 = _DAT_ffff000090843bc8;
  }
  plVar4 = (long *)0x0;
  if (plVar6 != (long *)0x0) {
    plVar4 = plVar6 + -1;
  }
  if (plVar4 == param_1) {
    FUN_ffff0000907166a8(FUN_ffff000090718830,0,param_2);
  }
  if (((uint)uVar1 >> 7 & 1) == 0) {
    uVar2 = daif;
    daif = uVar2 & 0xffffffffffffff7f;
  }
  _DAT_ffff000090843b98 = 0;
  return;
}


