// ffff000090752934  size=168  FUN_ffff000090752934

int FUN_ffff000090752934(int param_1)

{
  long *plVar1;
  long lVar2;
  long lVar3;
  uint uVar4;
  long *plVar5;
  ulong uVar6;
  ulong uVar7;
  ulong uVar8;
  int iVar9;
  int iVar10;
  ulong uVar11;
  
  uVar4 = 0;
  if (param_1 != 0x2b) {
    uVar4 = (uint)(param_1 != 0x2d);
  }
  uVar11 = (ulong)uVar4;
  lVar3 = 1;
  if (param_1 != 0x2b) {
    lVar3 = 2;
  }
  uVar7 = lVar3 - uVar11;
  if (uVar7 < 2) {
    iVar10 = 0;
    uVar6 = uVar11;
  }
  else {
    uVar8 = uVar7 & 0xfffffffffffffffe;
    iVar9 = 0;
    uVar6 = uVar8 | uVar11;
    iVar10 = 0;
    plVar5 = &DAT_ffff00009080cf98 + uVar11;
    uVar11 = uVar8;
    do {
      plVar1 = plVar5 + -1;
      lVar2 = *plVar5;
      plVar5 = plVar5 + 2;
      uVar11 = uVar11 - 2;
      iVar9 = iVar9 - (*(int *)(*plVar1 + 0xc) >> 0x1f);
      iVar10 = iVar10 - (*(int *)(lVar2 + 0xc) >> 0x1f);
    } while (uVar11 != 0);
    iVar10 = iVar10 + iVar9;
    if (uVar7 == uVar8) {
      return iVar10;
    }
  }
  lVar3 = lVar3 - uVar6;
  plVar5 = &DAT_ffff00009080cf90 + uVar6;
  do {
    lVar3 = lVar3 + -1;
    iVar10 = iVar10 - (*(int *)(*plVar5 + 0xc) >> 0x1f);
    plVar5 = plVar5 + 1;
  } while (lVar3 != 0);
  return iVar10;
}


