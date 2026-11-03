
void FUN_ffff0000b80662cc(ulong *param_1,ulong *param_2,ulong *param_3,ulong *param_4,uint param_5)

{
  uint uVar1;
  uint uVar2;
  long *plVar3;
  ulong *puVar4;
  ulong uVar5;
  ulong uVar6;
  long lVar7;
  ulong uVar8;
  ulong *puVar9;
  long *plVar10;
  ulong uVar11;
  ulong *puVar12;
  ulong uVar13;
  ulong uVar14;
  ulong local_e0 [18];
  
  uVar14 = (ulong)param_5;
  local_e0[0x10] = 0;
  local_e0[0x11] = 0;
  local_e0[0xe] = 0;
  local_e0[0xf] = 0;
  local_e0[0xc] = 0;
  local_e0[0xd] = 0;
  local_e0[10] = 0;
  local_e0[0xb] = 0;
  local_e0[8] = 0;
  local_e0[9] = 0;
  local_e0[6] = 0;
  local_e0[7] = 0;
  local_e0[4] = 0;
  local_e0[5] = 0;
  local_e0[2] = 0;
  local_e0[3] = 0;
  local_e0[0] = 0;
  local_e0[1] = 0;
  if (param_5 != 0) {
    uVar5 = 0;
    puVar4 = local_e0 + 0xc;
    puVar9 = param_3;
    puVar12 = param_1;
    uVar6 = uVar14;
    do {
      uVar8 = *puVar12 + uVar5;
      if (uVar8 != 0) {
        uVar5 = (ulong)(*puVar9 < uVar8);
      }
      uVar6 = uVar6 - 1;
      *puVar4 = *puVar9 - uVar8;
      puVar4 = puVar4 + 1;
      puVar9 = puVar9 + 1;
      puVar12 = puVar12 + 1;
    } while (uVar6 != 0);
    if (uVar5 != 0) {
      uVar5 = 0;
      uVar6 = uVar14;
      plVar3 = DAT_ffff0000b81b7200;
      puVar4 = local_e0 + 0xc;
      do {
        uVar11 = *puVar4;
        uVar8 = uVar11 + uVar5 + *plVar3;
        if (uVar8 != uVar11) {
          uVar5 = (ulong)(uVar8 < uVar11);
        }
        uVar6 = uVar6 - 1;
        *puVar4 = uVar8;
        plVar3 = plVar3 + 1;
        puVar4 = puVar4 + 1;
      } while (uVar6 != 0);
    }
  }
  FUN_ffff0000b8066a44(local_e0 + 0xc,local_e0 + 0xc,uVar14);
  FUN_ffff0000b806617c(param_1,param_1,local_e0 + 0xc,uVar14);
  FUN_ffff0000b806617c(param_3,param_3,local_e0 + 0xc,uVar14);
  plVar3 = DAT_ffff0000b81b7200;
  uVar1 = param_5;
  uVar2 = param_5;
  if (param_5 == 0) {
LAB_ffff0000b806646c:
    do {
      uVar1 = uVar1 - 1;
      if ((int)uVar1 < 0) goto LAB_ffff0000b8066428;
      if ((ulong)DAT_ffff0000b81b7200[uVar1] < local_e0[(ulong)uVar1 + 0xc])
      goto LAB_ffff0000b8066428;
    } while ((ulong)DAT_ffff0000b81b7200[uVar1] <= local_e0[(ulong)uVar1 + 0xc]);
LAB_ffff0000b8066490:
    if (param_5 == 0) goto LAB_ffff0000b8066610;
    lVar7 = 0;
    uVar5 = 0;
    uVar6 = (ulong)param_5;
    do {
      uVar8 = *(long *)((long)param_2 + lVar7) + uVar5;
      if (uVar8 != 0) {
        uVar5 = (ulong)(*(ulong *)((long)param_4 + lVar7) < uVar8);
      }
      *(ulong *)((long)param_4 + lVar7) = *(ulong *)((long)param_4 + lVar7) - uVar8;
      lVar7 = lVar7 + 8;
    } while (uVar14 << 3 != lVar7);
    if (uVar5 != 0) {
      uVar5 = 0;
      puVar4 = param_4;
      plVar10 = plVar3;
      uVar8 = uVar6;
      do {
        uVar13 = *puVar4;
        uVar11 = uVar13 + uVar5 + *plVar10;
        if (uVar11 != uVar13) {
          uVar5 = (ulong)(uVar11 < uVar13);
        }
        uVar8 = uVar8 - 1;
        *puVar4 = uVar11;
        puVar4 = puVar4 + 1;
        plVar10 = plVar10 + 1;
      } while (uVar8 != 0);
    }
    uVar5 = 0;
    uVar8 = 0;
    do {
      uVar11 = uVar8 + 1;
      uVar13 = param_1[uVar8] + uVar5;
      if (uVar13 != 0) {
        uVar5 = (ulong)(param_3[uVar8] < uVar13);
      }
      local_e0[uVar8 + 6] = param_3[uVar8] - uVar13;
      uVar8 = uVar11;
    } while (uVar6 != uVar11);
    if (uVar5 != 0) {
      uVar5 = 0;
      puVar4 = local_e0 + 6;
      uVar8 = uVar6;
      do {
        uVar13 = *puVar4;
        uVar11 = uVar13 + uVar5 + *plVar3;
        if (uVar11 != uVar13) {
          uVar5 = (ulong)(uVar11 < uVar13);
        }
        uVar8 = uVar8 - 1;
        *puVar4 = uVar11;
        plVar3 = plVar3 + 1;
        puVar4 = puVar4 + 1;
      } while (uVar8 != 0);
    }
    FUN_ffff0000b806617c(param_2,param_2,local_e0 + 6,param_5);
    uVar5 = 0;
    puVar4 = param_1;
    puVar9 = param_3;
    puVar12 = local_e0 + 6;
    do {
      uVar11 = *puVar4;
      uVar8 = uVar11 + uVar5 + *puVar9;
      if (uVar8 != uVar11) {
        uVar5 = (ulong)(uVar8 < uVar11);
      }
      uVar6 = uVar6 - 1;
      *puVar12 = uVar8;
      puVar4 = puVar4 + 1;
      puVar9 = puVar9 + 1;
      puVar12 = puVar12 + 1;
    } while (uVar6 != 0);
    if (uVar5 == 0) goto LAB_ffff0000b8066630;
LAB_ffff0000b80665d4:
    if (param_5 == 0) {
      FUN_ffff0000b8066a44(param_3,param_4,0);
      goto LAB_ffff0000b8066990;
    }
    uVar5 = 0;
    uVar6 = (ulong)param_5;
    plVar3 = DAT_ffff0000b81b7200;
    puVar4 = local_e0 + 6;
    do {
      uVar8 = *plVar3 + uVar5;
      if (uVar8 != 0) {
        uVar5 = (ulong)(*puVar4 < uVar8);
      }
      uVar6 = uVar6 - 1;
      *puVar4 = *puVar4 - uVar8;
      plVar3 = plVar3 + 1;
      puVar4 = puVar4 + 1;
    } while (uVar6 != 0);
  }
  else {
    uVar5 = 0;
    uVar6 = (ulong)param_5;
    puVar4 = local_e0 + 0xc;
    puVar9 = param_4;
    puVar12 = param_2;
    do {
      uVar11 = *puVar9;
      uVar8 = uVar11 + uVar5 + *puVar12;
      if (uVar8 != uVar11) {
        uVar5 = (ulong)(uVar8 < uVar11);
      }
      uVar6 = uVar6 - 1;
      *puVar4 = uVar8;
      puVar4 = puVar4 + 1;
      puVar9 = puVar9 + 1;
      puVar12 = puVar12 + 1;
    } while (uVar6 != 0);
    if (uVar5 == 0) goto LAB_ffff0000b806646c;
LAB_ffff0000b8066428:
    if (param_5 != 0) {
      uVar5 = 0;
      uVar6 = (ulong)param_5;
      puVar4 = local_e0 + 0xc;
      plVar10 = DAT_ffff0000b81b7200;
      do {
        uVar8 = *plVar10 + uVar5;
        if (uVar8 != 0) {
          uVar5 = (ulong)(*puVar4 < uVar8);
        }
        uVar6 = uVar6 - 1;
        *puVar4 = *puVar4 - uVar8;
        puVar4 = puVar4 + 1;
        plVar10 = plVar10 + 1;
      } while (uVar6 != 0);
      goto LAB_ffff0000b8066490;
    }
LAB_ffff0000b8066610:
    FUN_ffff0000b806617c(param_2,param_2,local_e0 + 6,param_5);
LAB_ffff0000b8066630:
    do {
      uVar2 = uVar2 - 1;
      if ((int)uVar2 < 0) goto LAB_ffff0000b80665d4;
      if ((ulong)DAT_ffff0000b81b7200[uVar2] < local_e0[(ulong)uVar2 + 6])
      goto LAB_ffff0000b80665d4;
    } while ((ulong)DAT_ffff0000b81b7200[uVar2] <= local_e0[(ulong)uVar2 + 6]);
  }
  FUN_ffff0000b8066a44(param_3,param_4,uVar14);
  plVar3 = DAT_ffff0000b81b7200;
  if (param_5 != 0) {
    lVar7 = 0;
    uVar14 = 0;
    uVar5 = (ulong)param_5;
    do {
      uVar6 = *(long *)((long)local_e0 + lVar7 + 0x30) + uVar14;
      if (uVar6 != 0) {
        uVar14 = (ulong)(*(ulong *)((long)param_3 + lVar7) < uVar6);
      }
      *(ulong *)((long)param_3 + lVar7) = *(ulong *)((long)param_3 + lVar7) - uVar6;
      lVar7 = lVar7 + 8;
    } while ((ulong)param_5 << 3 != lVar7);
    if (uVar14 != 0) {
      uVar14 = 0;
      puVar4 = param_3;
      plVar10 = plVar3;
      uVar6 = uVar5;
      do {
        uVar11 = *puVar4;
        uVar8 = uVar11 + uVar14 + *plVar10;
        if (uVar8 != uVar11) {
          uVar14 = (ulong)(uVar8 < uVar11);
        }
        uVar6 = uVar6 - 1;
        *puVar4 = uVar8;
        puVar4 = puVar4 + 1;
        plVar10 = plVar10 + 1;
      } while (uVar6 != 0);
    }
    uVar14 = 0;
    uVar6 = 0;
    do {
      uVar8 = uVar6 + 1;
      uVar11 = param_3[uVar6] + uVar14;
      if (uVar11 != 0) {
        uVar14 = (ulong)(param_1[uVar6] < uVar11);
      }
      local_e0[uVar6] = param_1[uVar6] - uVar11;
      uVar6 = uVar8;
    } while (uVar5 != uVar8);
    if (uVar14 != 0) {
      uVar14 = 0;
      puVar4 = local_e0;
      uVar6 = uVar5;
      do {
        uVar11 = *puVar4;
        uVar8 = uVar11 + uVar14 + *plVar3;
        if (uVar8 != uVar11) {
          uVar14 = (ulong)(uVar8 < uVar11);
        }
        uVar6 = uVar6 - 1;
        *puVar4 = uVar8;
        plVar3 = plVar3 + 1;
        puVar4 = puVar4 + 1;
      } while (uVar6 != 0);
    }
    FUN_ffff0000b806617c(param_4,param_4,local_e0,param_5);
    plVar3 = DAT_ffff0000b81b7200;
    uVar14 = 0;
    puVar4 = param_4;
    puVar9 = param_2;
    uVar6 = uVar5;
    do {
      uVar8 = *puVar9 + uVar14;
      if (uVar8 != 0) {
        uVar14 = (ulong)(*puVar4 < uVar8);
      }
      uVar6 = uVar6 - 1;
      *puVar4 = *puVar4 - uVar8;
      puVar4 = puVar4 + 1;
      puVar9 = puVar9 + 1;
    } while (uVar6 != 0);
    if (uVar14 != 0) {
      uVar14 = 0;
      uVar6 = uVar5;
      do {
        uVar11 = *param_4;
        uVar8 = uVar11 + uVar14 + *plVar3;
        if (uVar8 != uVar11) {
          uVar14 = (ulong)(uVar8 < uVar11);
        }
        uVar6 = uVar6 - 1;
        *param_4 = uVar8;
        plVar3 = plVar3 + 1;
        param_4 = param_4 + 1;
      } while (uVar6 != 0);
    }
    FUN_ffff0000b8066a44(local_e0,local_e0 + 0xc,param_5);
    uVar14 = 0;
    uVar6 = 0;
    do {
      uVar8 = uVar6 + 1;
      uVar11 = local_e0[uVar6 + 6] + uVar14;
      if (uVar11 != 0) {
        uVar14 = (ulong)(local_e0[uVar6] < uVar11);
      }
      local_e0[uVar6] = local_e0[uVar6] - uVar11;
      uVar6 = uVar8;
    } while (uVar5 != uVar8);
    if (uVar14 != 0) {
      uVar14 = 0;
      puVar4 = local_e0;
      plVar3 = DAT_ffff0000b81b7200;
      uVar6 = uVar5;
      do {
        uVar11 = *puVar4;
        uVar8 = uVar11 + uVar14 + *plVar3;
        if (uVar8 != uVar11) {
          uVar14 = (ulong)(uVar8 < uVar11);
        }
        uVar6 = uVar6 - 1;
        *puVar4 = uVar8;
        puVar4 = puVar4 + 1;
        plVar3 = plVar3 + 1;
      } while (uVar6 != 0);
    }
    uVar14 = 0;
    uVar6 = 0;
    do {
      uVar8 = uVar6 + 1;
      uVar11 = param_1[uVar6] + uVar14;
      if (uVar11 != 0) {
        uVar14 = (ulong)(local_e0[uVar6] < uVar11);
      }
      local_e0[uVar6 + 6] = local_e0[uVar6] - uVar11;
      uVar6 = uVar8;
    } while (uVar5 != uVar8);
    if (uVar14 != 0) {
      uVar14 = 0;
      plVar3 = DAT_ffff0000b81b7200;
      puVar4 = local_e0 + 6;
      uVar6 = uVar5;
      do {
        uVar11 = *puVar4;
        uVar8 = uVar11 + uVar14 + *plVar3;
        if (uVar8 != uVar11) {
          uVar14 = (ulong)(uVar8 < uVar11);
        }
        uVar6 = uVar6 - 1;
        *puVar4 = uVar8;
        plVar3 = plVar3 + 1;
        puVar4 = puVar4 + 1;
      } while (uVar6 != 0);
    }
    FUN_ffff0000b806617c(local_e0 + 6,local_e0 + 6,local_e0 + 0xc,param_5);
    plVar3 = DAT_ffff0000b81b7200;
    uVar14 = 0;
    puVar4 = param_2;
    uVar6 = uVar5;
    puVar9 = local_e0 + 6;
    do {
      uVar8 = *puVar4 + uVar14;
      if (uVar8 != 0) {
        uVar14 = (ulong)(*puVar9 < uVar8);
      }
      uVar6 = uVar6 - 1;
      *puVar4 = *puVar9 - uVar8;
      puVar4 = puVar4 + 1;
      puVar9 = puVar9 + 1;
    } while (uVar6 != 0);
    if (uVar14 != 0) {
      uVar14 = 0;
      uVar6 = uVar5;
      do {
        uVar11 = *param_2;
        uVar8 = uVar11 + uVar14 + *plVar3;
        if (uVar8 != uVar11) {
          uVar14 = (ulong)(uVar8 < uVar11);
        }
        uVar6 = uVar6 - 1;
        *param_2 = uVar8;
        plVar3 = plVar3 + 1;
        param_2 = param_2 + 1;
      } while (uVar6 != 0);
    }
    if (param_5 < 2) {
      uVar6 = 0;
    }
    else {
      uVar6 = uVar5 & 0xfffffffe;
      puVar4 = local_e0 + 1;
      puVar9 = param_1 + 1;
      uVar14 = uVar6;
      do {
        puVar12 = puVar4 + -1;
        uVar8 = *puVar4;
        puVar4 = puVar4 + 2;
        uVar14 = uVar14 - 2;
        puVar9[-1] = *puVar12;
        *puVar9 = uVar8;
        puVar9 = puVar9 + 2;
      } while (uVar14 != 0);
      if (uVar6 == uVar5) {
        return;
      }
    }
    lVar7 = uVar5 - uVar6;
    puVar4 = param_1 + uVar6;
    puVar9 = local_e0 + uVar6;
    do {
      lVar7 = lVar7 + -1;
      *puVar4 = *puVar9;
      puVar4 = puVar4 + 1;
      puVar9 = puVar9 + 1;
    } while (lVar7 != 0);
    return;
  }
LAB_ffff0000b8066990:
  FUN_ffff0000b806617c(param_4,param_4,local_e0,param_5);
  FUN_ffff0000b8066a44(local_e0,local_e0 + 0xc,param_5);
  FUN_ffff0000b806617c(local_e0 + 6,local_e0 + 6,local_e0 + 0xc,param_5);
  return;
}


