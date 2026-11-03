
void FUN_ffff000090718718(long *param_1,int param_2,undefined4 param_3,long param_4,long param_5)

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
    FUN_ffff00009077cee0(s_timer__p_already_in_list_ffff0000907ddb37,param_1);
  }
  iVar3 = FUN_ffff000090716918();
  param_1[4] = param_4;
  param_1[5] = param_5;
  *(int *)(param_1 + 3) = iVar3 + param_2;
  *(undefined4 *)((long)param_1 + 0x1c) = param_3;
  uVar1 = daif;
  if (((uint)uVar1 >> 7 & 1) == 0) {
    uVar2 = daif;
    daif = uVar2 | 0x80;
  }
  DAT_ffff000090843bd0 = 1;
  if ((long **)DAT_ffff000090843c08 == &DAT_ffff000090843c00) {
LAB_ffff0000907187e8:
    param_1[1] = (long)DAT_ffff000090843c00;
    param_1[2] = (long)&DAT_ffff000090843c00;
    DAT_ffff000090843c00[1] = (long)plVar6;
    DAT_ffff000090843c00 = plVar6;
  }
  else {
    iVar3 = (int)DAT_ffff000090843c08[2] - (int)param_1[3];
    plVar4 = DAT_ffff000090843c08;
    while (iVar3 < 1) {
      plVar4 = (long *)plVar4[1];
      if ((long **)plVar4 == &DAT_ffff000090843c00) goto LAB_ffff0000907187e8;
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
  if ((long **)DAT_ffff000090843c08 != &DAT_ffff000090843c00) {
    plVar6 = DAT_ffff000090843c08;
  }
  plVar4 = (long *)0x0;
  if (plVar6 != (long *)0x0) {
    plVar4 = plVar6 + -1;
  }
  if (plVar4 == param_1) {
    FUN_ffff0000907167ec(&LAB_ffff000090718974,0,param_2);
  }
  if (((uint)uVar1 >> 7 & 1) == 0) {
    uVar2 = daif;
    daif = uVar2 & 0xffffffffffffff7f;
  }
  DAT_ffff000090843bd0 = 0;
  return;
}


