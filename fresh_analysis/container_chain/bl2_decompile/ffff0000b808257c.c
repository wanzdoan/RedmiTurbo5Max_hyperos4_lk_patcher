
void FUN_ffff0000b808257c(long *param_1)

{
  long *plVar1;
  long lVar2;
  long *plVar3;
  long lVar4;
  long *plVar5;
  long lVar6;
  undefined8 *puVar7;
  
  FUN_ffff0000b800ad90(&DAT_ffff0000b81b9b08,0xffffffff);
  lVar2 = param_1[2];
  DAT_ffff0000b81b9af8 = DAT_ffff0000b81b9af8 + lVar2;
  plVar3 = &DAT_ffff0000b81b9b40;
  plVar5 = DAT_ffff0000b81b9b48;
  if (DAT_ffff0000b81b9b48 != &DAT_ffff0000b81b9b40) {
    do {
      plVar3 = plVar5;
      if (param_1 < plVar5) break;
      plVar1 = plVar5 + 1;
      plVar3 = &DAT_ffff0000b81b9b40;
      plVar5 = (long *)*plVar1;
    } while ((long *)*plVar1 != &DAT_ffff0000b81b9b40);
  }
  lVar4 = *plVar3;
  param_1[1] = (long)plVar3;
  *param_1 = lVar4;
  lVar4 = *plVar3;
  *plVar3 = (long)param_1;
  plVar5 = (long *)*param_1;
  *(long **)(lVar4 + 8) = param_1;
  if (((plVar5 != &DAT_ffff0000b81b9b40) && (plVar5 != (long *)0x0)) &&
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
  FUN_ffff0000b800ae28(&DAT_ffff0000b81b9b08);
  return;
}


