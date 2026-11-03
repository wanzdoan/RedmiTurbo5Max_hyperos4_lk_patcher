// ffff0000907c3170  size=268  FUN_ffff0000907c3170

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907c3170(long *param_1)

{
  long *plVar1;
  long lVar2;
  long *plVar3;
  long lVar4;
  long *plVar5;
  long lVar6;
  undefined8 *puVar7;
  
  FUN_ffff000090717738(0xffff00009090d918,0xffffffff);
  lVar2 = param_1[2];
  _DAT_ffff00009090d908 = _DAT_ffff00009090d908 + lVar2;
  plVar3 = (long *)&DAT_ffff00009090d950;
  plVar5 = _DAT_ffff00009090d958;
  if (_DAT_ffff00009090d958 != (long *)&DAT_ffff00009090d950) {
    do {
      plVar3 = plVar5;
      if (param_1 < plVar5) break;
      plVar1 = plVar5 + 1;
      plVar3 = (long *)&DAT_ffff00009090d950;
      plVar5 = (long *)*plVar1;
    } while ((long *)*plVar1 != (long *)&DAT_ffff00009090d950);
  }
  lVar4 = *plVar3;
  param_1[1] = (long)plVar3;
  *param_1 = lVar4;
  lVar4 = *plVar3;
  *plVar3 = (long)param_1;
  plVar5 = (long *)*param_1;
  *(long **)(lVar4 + 8) = param_1;
  if (((plVar5 != (long *)&DAT_ffff00009090d950) && (plVar5 != (long *)0x0)) &&
     ((long *)(plVar5[2] + (long)plVar5) == param_1)) {
    puVar7 = (undefined8 *)param_1[1];
    lVar2 = plVar5[2] + lVar2;
    *puVar7 = plVar5;
    lVar4 = *param_1;
    plVar5[2] = lVar2;
    *(undefined8 **)(lVar4 + 8) = puVar7;
    *param_1 = 0;
    param_1[1] = 0;
    param_1 = plVar5;
  }
  if ((long *)(lVar2 + (long)param_1) == plVar3) {
    plVar5 = (long *)plVar3[1];
    lVar4 = plVar3[2];
    *plVar5 = *plVar3;
    lVar6 = *plVar3;
    param_1[2] = lVar4 + lVar2;
    *(long **)(lVar6 + 8) = plVar5;
    *plVar3 = 0;
    plVar3[1] = 0;
  }
  FUN_ffff0000907177d0(0xffff00009090d918);
  return;
}


