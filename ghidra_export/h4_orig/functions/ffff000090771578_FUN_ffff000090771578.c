// ffff000090771578  size=228  FUN_ffff000090771578

long * FUN_ffff000090771578(long *param_1)

{
  long lVar1;
  long *plVar2;
  long *plVar3;
  long *plVar4;
  int iVar5;
  long lVar6;
  long *plVar7;
  
  plVar3 = (long *)*param_1;
  if ((plVar3 == (long *)0x0) || (plVar3[1] == 0)) {
    plVar3 = (long *)thunk_FUN_ffff00009077d514(0x8018);
    lVar6 = 0;
    plVar4 = plVar3 + 1;
    do {
      lVar1 = lVar6 + 0x40;
      plVar2 = (long *)((long)plVar3 + lVar6 + 0x38);
      *plVar4 = (long)plVar3 + lVar6 + 0x18;
      *(long **)((long)plVar3 + lVar6 + 0x18) = plVar2;
      lVar6 = lVar1;
      plVar4 = plVar2;
    } while (lVar1 != 0x8000);
    iVar5 = 1;
    *(undefined4 *)(plVar3 + 2) = 0;
    plVar3[0xfff] = 0;
    lVar6 = *param_1;
    *param_1 = (long)plVar3;
    *plVar3 = lVar6;
    if (lVar6 == 0) {
      param_1[1] = (long)plVar3;
    }
  }
  else {
    iVar5 = (int)plVar3[2] + 1;
  }
  plVar4 = (long *)plVar3[1];
  *(int *)(plVar3 + 2) = iVar5;
  lVar6 = *plVar4;
  plVar3[1] = lVar6;
  if ((lVar6 == 0) && (*(long **)param_1[1] != plVar3)) {
    plVar7 = (long *)*param_1;
    lVar6 = *plVar7;
    *param_1 = lVar6;
    plVar2 = param_1;
    if (lVar6 != 0) {
      plVar2 = (long *)param_1[1];
    }
    *plVar7 = 0;
    *plVar3 = 0;
    *plVar2 = (long)plVar3;
    param_1[1] = (long)plVar3;
  }
  return plVar4;
}


