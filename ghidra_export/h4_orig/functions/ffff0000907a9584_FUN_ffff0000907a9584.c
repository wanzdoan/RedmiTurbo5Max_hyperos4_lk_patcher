// ffff0000907a9584  size=716  FUN_ffff0000907a9584

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907a9584(ulong *param_1,ulong *param_2,uint param_3)

{
  uint uVar1;
  long *plVar2;
  ulong uVar3;
  ulong *puVar4;
  ulong uVar5;
  ulong *puVar6;
  long lVar7;
  long lVar8;
  ulong uVar9;
  ulong uVar10;
  ulong uVar11;
  long *plVar12;
  ulong local_30 [6];
  
  local_30[4] = 0;
  local_30[5] = 0;
  local_30[2] = 0;
  local_30[3] = 0;
  if (param_3 != 0) {
    uVar3 = (ulong)param_3;
    puVar4 = param_2;
    puVar6 = param_1;
    do {
      uVar3 = uVar3 - 1;
      *puVar6 = *puVar4;
      puVar4 = puVar4 + 1;
      puVar6 = puVar6 + 1;
    } while (uVar3 != 0);
  }
  uVar5 = param_2[2];
  uVar11 = param_2[3];
  local_30[0] = uVar5;
  local_30[1] = uVar11 & 0x1ffffffff | uVar5 << 0x21;
  uVar3 = 0;
  if (param_3 != 0) {
    uVar5 = (ulong)param_3;
    puVar4 = local_30;
    puVar6 = param_1;
    do {
      uVar9 = *puVar6;
      uVar11 = uVar9 + uVar3 + *puVar4;
      if (uVar11 != uVar9) {
        uVar3 = (ulong)(uVar11 < uVar9);
      }
      uVar5 = uVar5 - 1;
      *puVar6 = uVar11;
      puVar4 = puVar4 + 1;
      puVar6 = puVar6 + 1;
    } while (uVar5 != 0);
    uVar5 = param_2[2];
    uVar11 = param_2[3];
  }
  local_30[0] = uVar5 >> 0x1f | uVar11 << 0x21;
  local_30[1] = (uVar5 & 0x3fffffff80000000) << 2 | uVar11 >> 0x1f;
  uVar9 = 0;
  if (param_3 != 0) {
    uVar5 = (ulong)param_3;
    puVar4 = local_30;
    puVar6 = param_1;
    do {
      uVar10 = *puVar6;
      uVar11 = uVar10 + uVar9 + *puVar4;
      if (uVar11 != uVar10) {
        uVar9 = (ulong)(uVar11 < uVar10);
      }
      uVar5 = uVar5 - 1;
      *puVar6 = uVar11;
      puVar4 = puVar4 + 1;
      puVar6 = puVar6 + 1;
    } while (uVar5 != 0);
    uVar5 = param_2[2];
    uVar11 = param_2[3];
  }
  lVar8 = uVar9 + uVar3;
  local_30[0] = uVar5 >> 0x3e | uVar11 << 2;
  local_30[1] = uVar5 >> 0x1d & 0x600000000 | uVar11 >> 0x3e | uVar11 << 0x23;
  if (param_3 != 0) {
    lVar7 = 0;
    uVar3 = 0;
    do {
      uVar11 = *(ulong *)((long)param_1 + lVar7);
      uVar5 = uVar11 + uVar3 + *(long *)((long)local_30 + lVar7);
      *(ulong *)((long)param_1 + lVar7) = uVar5;
      if (uVar5 != uVar11) {
        uVar3 = (ulong)(uVar5 < uVar11);
      }
      lVar7 = lVar7 + 8;
    } while ((ulong)param_3 << 3 != lVar7);
    lVar8 = lVar8 + uVar3;
    local_30[0] = param_2[3] >> 0x1d;
    local_30[1] = (param_2[3] & 0xfffffffe0000000) << 4;
    if (param_3 != 0) {
      lVar7 = 0;
      uVar3 = 0;
      uVar5 = (ulong)param_3;
      do {
        uVar9 = *(ulong *)((long)param_1 + lVar7);
        uVar11 = uVar9 + uVar3 + *(long *)((long)local_30 + lVar7);
        *(ulong *)((long)param_1 + lVar7) = uVar11;
        if (uVar11 != uVar9) {
          uVar3 = (ulong)(uVar11 < uVar9);
        }
        lVar7 = lVar7 + 8;
      } while ((ulong)param_3 << 3 != lVar7);
      lVar7 = 0;
      uVar11 = 0;
      local_30[0] = param_2[3] >> 0x3c;
      local_30[1] = param_2[3] & 0xfffffffe00000000;
      do {
        uVar10 = *(ulong *)((long)param_1 + lVar7);
        uVar9 = uVar10 + uVar11 + *(long *)((long)local_30 + lVar7);
        *(ulong *)((long)param_1 + lVar7) = uVar9;
        if (uVar9 != uVar10) {
          uVar11 = (ulong)(uVar9 < uVar10);
        }
        lVar7 = lVar7 + 8;
      } while (uVar5 * 8 - lVar7 != 0);
      lVar8 = uVar11 + uVar3 + lVar8;
      uVar3 = 0;
      local_30[0] = 0;
      local_30[1] = param_2[3] >> 0x1b & 0x1e00000000;
      puVar4 = local_30;
      puVar6 = param_1;
      uVar11 = uVar5;
      do {
        uVar10 = *puVar6;
        uVar9 = uVar10 + uVar3 + *puVar4;
        if (uVar9 != uVar10) {
          uVar3 = (ulong)(uVar9 < uVar10);
        }
        uVar11 = uVar11 - 1;
        *puVar6 = uVar9;
        puVar4 = puVar4 + 1;
        puVar6 = puVar6 + 1;
      } while (uVar11 != 0);
      goto LAB_ffff0000907a97b8;
    }
  }
  uVar5 = 0;
  uVar3 = 0;
LAB_ffff0000907a97b8:
  plVar2 = _DAT_ffff00009090afc0;
  lVar8 = uVar3 + lVar8;
  do {
    uVar1 = param_3;
    if ((int)lVar8 == 0) {
      do {
        uVar1 = uVar1 - 1;
        if ((int)uVar1 < 0) break;
        uVar3 = plVar2[uVar1];
        if (param_1[uVar1] < uVar3) {
          return;
        }
      } while (param_1[uVar1] <= uVar3);
    }
    uVar3 = 0;
    puVar4 = param_1;
    plVar12 = plVar2;
    uVar11 = uVar5;
    if (param_3 != 0) {
      do {
        uVar9 = *plVar12 + uVar3;
        if (uVar9 != 0) {
          uVar3 = (ulong)(*puVar4 < uVar9);
        }
        uVar11 = uVar11 - 1;
        *puVar4 = *puVar4 - uVar9;
        puVar4 = puVar4 + 1;
        plVar12 = plVar12 + 1;
      } while (uVar11 != 0);
    }
    lVar8 = (long)(int)lVar8 - uVar3;
  } while( true );
}


