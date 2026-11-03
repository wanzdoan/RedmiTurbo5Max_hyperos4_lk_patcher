// ffff0000907529dc  size=172  FUN_ffff0000907529dc

bool FUN_ffff0000907529dc(int param_1)

{
  long *plVar1;
  long lVar2;
  long lVar3;
  uint uVar4;
  long *plVar5;
  ulong uVar6;
  int iVar7;
  ulong uVar8;
  int iVar9;
  ulong uVar10;
  ulong uVar11;
  
  uVar4 = 0;
  if (param_1 != 0x2b) {
    uVar4 = (uint)(param_1 != 0x2d);
  }
  uVar10 = (ulong)uVar4;
  lVar3 = 1;
  if (param_1 != 0x2b) {
    lVar3 = 2;
  }
  uVar6 = lVar3 - uVar10;
  iVar7 = 0;
  if (1 < uVar6) {
    uVar8 = uVar6 & 0xfffffffffffffffe;
    iVar9 = 0;
    plVar5 = &DAT_ffff00009080cf98 + uVar10;
    uVar11 = uVar8;
    do {
      plVar1 = plVar5 + -1;
      lVar2 = *plVar5;
      plVar5 = plVar5 + 2;
      uVar11 = uVar11 - 2;
      iVar7 = iVar7 - (*(int *)(*plVar1 + 0xc) >> 0x1f);
      iVar9 = iVar9 - (*(int *)(lVar2 + 0xc) >> 0x1f);
    } while (uVar11 != 0);
    iVar7 = iVar9 + iVar7;
    uVar10 = uVar8 | uVar10;
    if (uVar6 == uVar8) goto LAB_ffff000090752a78;
  }
  lVar3 = lVar3 - uVar10;
  plVar5 = &DAT_ffff00009080cf90 + uVar10;
  do {
    lVar3 = lVar3 + -1;
    iVar7 = iVar7 - (*(int *)(*plVar5 + 0xc) >> 0x1f);
    plVar5 = plVar5 + 1;
  } while (lVar3 != 0);
LAB_ffff000090752a78:
  return iVar7 == 0;
}


