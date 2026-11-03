// ffff0000907a7cac  size=1228  FUN_ffff0000907a7cac

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907a7cac(long *param_1,long *param_2,ulong *param_3,ulong *param_4,uint param_5)

{
  ulong *puVar1;
  long *plVar2;
  ulong *puVar3;
  ulong uVar4;
  long lVar5;
  long *plVar6;
  ulong *puVar7;
  ulong uVar8;
  ulong uVar9;
  ulong uVar10;
  ulong uVar11;
  ulong uVar12;
  ulong local_80 [6];
  
  uVar11 = (ulong)param_5;
  local_80[4] = 0;
  local_80[5] = 0;
  local_80[2] = 0;
  local_80[3] = 0;
  local_80[0] = 0;
  local_80[1] = 0;
  if (param_5 != 0) {
    uVar4 = 0;
    puVar3 = local_80;
    puVar7 = param_3;
    plVar2 = param_1;
    uVar12 = uVar11;
    do {
      uVar8 = *plVar2 + uVar4;
      if (uVar8 != 0) {
        uVar4 = (ulong)(*puVar7 < uVar8);
      }
      uVar12 = uVar12 - 1;
      *puVar3 = *puVar7 - uVar8;
      puVar3 = puVar3 + 1;
      puVar7 = puVar7 + 1;
      plVar2 = plVar2 + 1;
    } while (uVar12 != 0);
    if (uVar4 != 0) {
      uVar4 = 0;
      uVar12 = uVar11;
      plVar2 = _DAT_ffff00009090af00;
      puVar3 = local_80;
      do {
        uVar9 = *puVar3;
        uVar8 = uVar9 + uVar4 + *plVar2;
        if (uVar8 != uVar9) {
          uVar4 = (ulong)(uVar8 < uVar9);
        }
        uVar12 = uVar12 - 1;
        *puVar3 = uVar8;
        plVar2 = plVar2 + 1;
        puVar3 = puVar3 + 1;
      } while (uVar12 != 0);
    }
  }
  FUN_ffff0000907a9150(local_80,local_80,uVar11);
  FUN_ffff0000907a8888(param_1,param_1,local_80,uVar11);
  FUN_ffff0000907a8888(param_3,param_3,local_80,uVar11);
  plVar2 = _DAT_ffff00009090af00;
  if (param_5 != 0) {
    lVar5 = 0;
    uVar4 = 0;
    uVar12 = (ulong)param_5;
    do {
      uVar8 = *(long *)((long)param_2 + lVar5) + uVar4;
      if (uVar8 != 0) {
        uVar4 = (ulong)(*(ulong *)((long)param_4 + lVar5) < uVar8);
      }
      *(ulong *)((long)param_4 + lVar5) = *(ulong *)((long)param_4 + lVar5) - uVar8;
      lVar5 = lVar5 + 8;
    } while (uVar11 << 3 != lVar5);
    if (uVar4 != 0) {
      uVar4 = 0;
      puVar3 = param_4;
      uVar8 = uVar12;
      do {
        uVar10 = *puVar3;
        uVar9 = uVar10 + uVar4 + *plVar2;
        if (uVar9 != uVar10) {
          uVar4 = (ulong)(uVar9 < uVar10);
        }
        uVar8 = uVar8 - 1;
        *puVar3 = uVar9;
        plVar2 = plVar2 + 1;
        puVar3 = puVar3 + 1;
      } while (uVar8 != 0);
    }
    FUN_ffff0000907a9150(local_80,param_4,uVar11);
    plVar2 = _DAT_ffff00009090af00;
    uVar11 = 0;
    plVar6 = param_1;
    uVar4 = uVar12;
    puVar3 = local_80;
    do {
      uVar8 = *plVar6 + uVar11;
      if (uVar8 != 0) {
        uVar11 = (ulong)(*puVar3 < uVar8);
      }
      uVar4 = uVar4 - 1;
      *puVar3 = *puVar3 - uVar8;
      plVar6 = plVar6 + 1;
      puVar3 = puVar3 + 1;
    } while (uVar4 != 0);
    if (uVar11 != 0) {
      uVar11 = 0;
      puVar3 = local_80;
      plVar6 = _DAT_ffff00009090af00;
      uVar4 = uVar12;
      do {
        uVar9 = *puVar3;
        uVar8 = uVar9 + uVar11 + *plVar6;
        if (uVar8 != uVar9) {
          uVar11 = (ulong)(uVar8 < uVar9);
        }
        uVar4 = uVar4 - 1;
        *puVar3 = uVar8;
        puVar3 = puVar3 + 1;
        plVar6 = plVar6 + 1;
      } while (uVar4 != 0);
    }
    uVar11 = 0;
    uVar4 = 0;
    do {
      uVar8 = uVar4 + 1;
      uVar9 = param_3[uVar4] + uVar11;
      if (uVar9 != 0) {
        uVar11 = (ulong)(local_80[uVar4] < uVar9);
      }
      local_80[uVar4] = local_80[uVar4] - uVar9;
      uVar4 = uVar8;
    } while (uVar12 != uVar8);
    if (uVar11 != 0) {
      uVar11 = 0;
      puVar3 = local_80;
      plVar6 = _DAT_ffff00009090af00;
      uVar4 = uVar12;
      do {
        uVar9 = *puVar3;
        uVar8 = uVar9 + uVar11 + *plVar6;
        if (uVar8 != uVar9) {
          uVar11 = (ulong)(uVar8 < uVar9);
        }
        uVar4 = uVar4 - 1;
        *puVar3 = uVar8;
        puVar3 = puVar3 + 1;
        plVar6 = plVar6 + 1;
      } while (uVar4 != 0);
    }
    uVar11 = 0;
    uVar4 = 0;
    do {
      uVar8 = uVar4 + 1;
      uVar9 = param_1[uVar4] + uVar11;
      if (uVar9 != 0) {
        uVar11 = (ulong)(param_3[uVar4] < uVar9);
      }
      param_3[uVar4] = param_3[uVar4] - uVar9;
      uVar4 = uVar8;
    } while (uVar12 != uVar8);
    if (uVar11 != 0) {
      uVar11 = 0;
      puVar3 = param_3;
      uVar4 = uVar12;
      do {
        uVar9 = *puVar3;
        uVar8 = uVar9 + uVar11 + *plVar2;
        if (uVar8 != uVar9) {
          uVar11 = (ulong)(uVar8 < uVar9);
        }
        uVar4 = uVar4 - 1;
        *puVar3 = uVar8;
        plVar2 = plVar2 + 1;
        puVar3 = puVar3 + 1;
      } while (uVar4 != 0);
    }
    FUN_ffff0000907a8888(param_2,param_2,param_3,param_5);
    plVar2 = _DAT_ffff00009090af00;
    uVar11 = 0;
    uVar4 = 0;
    do {
      uVar8 = uVar4 + 1;
      uVar9 = local_80[uVar4] + uVar11;
      if (uVar9 != 0) {
        uVar11 = (ulong)((ulong)param_1[uVar4] < uVar9);
      }
      param_3[uVar4] = param_1[uVar4] - uVar9;
      uVar4 = uVar8;
    } while (uVar12 != uVar8);
    if (uVar11 != 0) {
      uVar11 = 0;
      puVar3 = param_3;
      uVar4 = uVar12;
      do {
        uVar9 = *puVar3;
        uVar8 = uVar9 + uVar11 + *plVar2;
        if (uVar8 != uVar9) {
          uVar11 = (ulong)(uVar8 < uVar9);
        }
        uVar4 = uVar4 - 1;
        *puVar3 = uVar8;
        plVar2 = plVar2 + 1;
        puVar3 = puVar3 + 1;
      } while (uVar4 != 0);
    }
    FUN_ffff0000907a8888(param_4,param_4,param_3,param_5);
    plVar2 = _DAT_ffff00009090af00;
    uVar11 = 0;
    puVar3 = param_4;
    uVar4 = uVar12;
    do {
      uVar8 = *param_2 + uVar11;
      if (uVar8 != 0) {
        uVar11 = (ulong)(*puVar3 < uVar8);
      }
      uVar4 = uVar4 - 1;
      *puVar3 = *puVar3 - uVar8;
      puVar3 = puVar3 + 1;
      param_2 = param_2 + 1;
    } while (uVar4 != 0);
    if (uVar11 != 0) {
      uVar11 = 0;
      uVar4 = uVar12;
      do {
        uVar9 = *param_4;
        uVar8 = uVar9 + uVar11 + *plVar2;
        if (uVar8 != uVar9) {
          uVar11 = (ulong)(uVar8 < uVar9);
        }
        uVar4 = uVar4 - 1;
        *param_4 = uVar8;
        plVar2 = plVar2 + 1;
        param_4 = param_4 + 1;
      } while (uVar4 != 0);
    }
    if (param_5 < 2) {
      uVar4 = 0;
    }
    else {
      uVar4 = uVar12 & 0xfffffffe;
      puVar3 = local_80 + 1;
      puVar7 = param_3 + 1;
      uVar11 = uVar4;
      do {
        puVar1 = puVar3 + -1;
        uVar8 = *puVar3;
        puVar3 = puVar3 + 2;
        uVar11 = uVar11 - 2;
        puVar7[-1] = *puVar1;
        *puVar7 = uVar8;
        puVar7 = puVar7 + 2;
      } while (uVar11 != 0);
      if (uVar4 == uVar12) {
        return;
      }
    }
    lVar5 = uVar12 - uVar4;
    puVar3 = param_3 + uVar4;
    puVar7 = local_80 + uVar4;
    do {
      lVar5 = lVar5 + -1;
      *puVar3 = *puVar7;
      puVar3 = puVar3 + 1;
      puVar7 = puVar7 + 1;
    } while (lVar5 != 0);
    return;
  }
  FUN_ffff0000907a9150(local_80,param_4,0);
  FUN_ffff0000907a8888(param_2,param_2,param_3,0);
  FUN_ffff0000907a8888(param_4,param_4,param_3,0);
  return;
}


