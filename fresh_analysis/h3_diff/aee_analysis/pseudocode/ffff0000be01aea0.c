
int FUN_ffff0000be01aea0(long param_1,uint param_2,undefined4 param_3)

{
  long lVar1;
  uint uVar2;
  undefined8 *puVar3;
  undefined8 uVar4;
  undefined8 *puVar5;
  long *plVar6;
  long lVar7;
  long *plVar8;
  int iVar9;
  
  lVar1 = tpidr_el1;
  if (((param_2 & 1) != 0) && (0 < *(int *)(param_1 + 0x18))) {
    lVar7 = (long)(int)*(uint *)(lVar1 + 0x28);
    uVar4 = (&DAT_ffff0000be10c738)[lVar7 * 2];
    DAT_ffff0000be10cc00 = DAT_ffff0000be10cc00 | 1 << (ulong)(*(uint *)(lVar1 + 0x28) & 0x1f);
    puVar3 = (undefined8 *)(lVar1 + 0x18);
    *puVar3 = &DAT_ffff0000be10c730 + lVar7 * 2;
    *(undefined8 *)(lVar1 + 0x20) = uVar4;
    puVar5 = (undefined8 *)(&DAT_ffff0000be10c738)[lVar7 * 2];
    *(undefined4 *)(lVar1 + 0x2c) = 1;
    (&DAT_ffff0000be10c738)[lVar7 * 2] = puVar3;
    *puVar5 = puVar3;
  }
  if (*(long **)(param_1 + 0x10) == (long *)(param_1 + 8)) {
    iVar9 = 0;
  }
  else {
    iVar9 = 0;
    plVar6 = *(long **)(param_1 + 0x10);
    do {
      uVar2 = DAT_ffff0000be10cc00;
      plVar8 = (long *)plVar6[1];
      *plVar8 = *plVar6;
      *(long **)(*plVar6 + 8) = plVar8;
      *plVar6 = 0;
      plVar6[1] = 0;
      if (plVar6 == (long *)0x18) break;
      iVar9 = iVar9 + 1;
      lVar1 = (long)(int)*(uint *)(plVar6 + 2);
      DAT_ffff0000be10cc00 = uVar2 | 1 << (ulong)(*(uint *)(plVar6 + 2) & 0x1f);
      *(int *)(param_1 + 0x18) = *(int *)(param_1 + 0x18) + -1;
      lVar7 = (&DAT_ffff0000be10c738)[lVar1 * 2];
      *plVar6 = (long)(&DAT_ffff0000be10c730 + lVar1 * 2);
      *(undefined4 *)((long)plVar6 + 0x14) = 1;
      plVar6[1] = lVar7;
      puVar3 = (undefined8 *)(&DAT_ffff0000be10c738)[lVar1 * 2];
      (&DAT_ffff0000be10c738)[lVar1 * 2] = plVar6;
      plVar8 = *(long **)(param_1 + 0x10);
      *(undefined4 *)(plVar6 + 6) = param_3;
      plVar6[5] = 0;
      *puVar3 = plVar6;
      plVar6 = plVar8;
    } while (plVar8 != (long *)(param_1 + 8));
  }
  if ((iVar9 != 0) && ((param_2 & 1) != 0)) {
    FUN_ffff0000be01b0ec();
  }
  return iVar9;
}


