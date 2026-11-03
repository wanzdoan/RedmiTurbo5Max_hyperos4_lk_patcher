
void FUN_ffff0000907a8438(ulong *param_1,ulong *param_2,ulong *param_3,ulong param_4)

{
  ulong *puVar1;
  uint uVar2;
  uint uVar3;
  long lVar4;
  long *plVar5;
  ulong *puVar6;
  ulong *puVar7;
  ulong uVar8;
  ulong uVar9;
  long *plVar10;
  ulong uVar11;
  ulong uVar12;
  ulong uVar13;
  ulong uVar14;
  ulong uVar15;
  ulong local_a0 [12];
  
  local_a0[10] = 0;
  local_a0[0xb] = 0;
  local_a0[8] = 0;
  local_a0[9] = 0;
  local_a0[6] = 0;
  local_a0[7] = 0;
  local_a0[4] = 0;
  local_a0[5] = 0;
  local_a0[2] = 0;
  local_a0[3] = 0;
  local_a0[0] = 0;
  local_a0[1] = 0;
  uVar3 = (uint)param_4;
  if (uVar3 == 0) {
    return;
  }
  uVar14 = param_4 & 0xffffffff;
  lVar4 = 0;
  uVar15 = param_4 & 0xffffffff;
  while (*(long *)((long)param_3 + lVar4) == 0) {
    lVar4 = lVar4 + 8;
    if (uVar14 << 3 == lVar4) {
      return;
    }
  }
  FUN_ffff0000907a9410(local_a0 + 6,param_2,uVar14);
  FUN_ffff0000907a8b48(local_a0,param_1,local_a0 + 6,uVar14);
  FUN_ffff0000907a9410(local_a0 + 6,local_a0 + 6,uVar14);
  FUN_ffff0000907a8b48(param_2,param_2,param_3,uVar14);
  FUN_ffff0000907a9410(param_3,param_3,uVar14);
  plVar5 = DAT_ffff00009090afc0;
  lVar4 = 0;
  uVar8 = 0;
  do {
    uVar11 = *(ulong *)((long)param_1 + lVar4);
    uVar9 = uVar11 + uVar8 + *(long *)((long)param_3 + lVar4);
    *(ulong *)((long)param_1 + lVar4) = uVar9;
    if (uVar9 != uVar11) {
      uVar8 = (ulong)(uVar9 < uVar11);
    }
    lVar4 = lVar4 + 8;
  } while (uVar15 * 8 - lVar4 != 0);
  uVar2 = uVar3;
  if (uVar8 == 0) {
    do {
      uVar2 = uVar2 - 1;
      if ((int)uVar2 < 0) goto LAB_ffff0000907a8540;
      if ((ulong)plVar5[uVar2] < param_1[uVar2]) goto LAB_ffff0000907a8540;
    } while ((ulong)plVar5[uVar2] <= param_1[uVar2]);
  }
  else {
LAB_ffff0000907a8540:
    uVar8 = 0;
    puVar7 = param_1;
    plVar10 = plVar5;
    uVar9 = uVar15;
    do {
      uVar11 = *plVar10 + uVar8;
      if (uVar11 != 0) {
        uVar8 = (ulong)(*puVar7 < uVar11);
      }
      uVar9 = uVar9 - 1;
      *puVar7 = *puVar7 - uVar11;
      puVar7 = puVar7 + 1;
      plVar10 = plVar10 + 1;
    } while (uVar9 != 0);
  }
  uVar8 = 0;
  uVar9 = 0;
  do {
    uVar11 = uVar9 + 1;
    uVar12 = param_3[uVar9];
    uVar13 = uVar8 + uVar12 * 2;
    param_3[uVar9] = uVar13;
    if (uVar13 != uVar12) {
      uVar8 = (ulong)(uVar13 < uVar12);
    }
    uVar9 = uVar11;
  } while (uVar15 != uVar11);
  uVar2 = uVar3;
  if (uVar8 == 0) {
    do {
      uVar2 = uVar2 - 1;
      if ((int)uVar2 < 0) goto LAB_ffff0000907a85ac;
      if ((ulong)plVar5[uVar2] < param_3[uVar2]) goto LAB_ffff0000907a85ac;
    } while ((ulong)plVar5[uVar2] <= param_3[uVar2]);
  }
  else {
LAB_ffff0000907a85ac:
    uVar8 = 0;
    puVar7 = param_3;
    plVar10 = plVar5;
    uVar9 = uVar15;
    do {
      uVar11 = *plVar10 + uVar8;
      if (uVar11 != 0) {
        uVar8 = (ulong)(*puVar7 < uVar11);
      }
      uVar9 = uVar9 - 1;
      *puVar7 = *puVar7 - uVar11;
      puVar7 = puVar7 + 1;
      plVar10 = plVar10 + 1;
    } while (uVar9 != 0);
  }
  uVar8 = 0;
  uVar9 = 0;
  do {
    uVar11 = uVar9 + 1;
    uVar13 = param_3[uVar9] + uVar8;
    if (uVar13 != 0) {
      uVar8 = (ulong)(param_1[uVar9] < uVar13);
    }
    param_3[uVar9] = param_1[uVar9] - uVar13;
    uVar9 = uVar11;
  } while (uVar15 != uVar11);
  if (uVar8 != 0) {
    uVar8 = 0;
    puVar7 = param_3;
    uVar9 = uVar15;
    do {
      uVar13 = *puVar7;
      uVar11 = uVar13 + uVar8 + *plVar5;
      if (uVar11 != uVar13) {
        uVar8 = (ulong)(uVar11 < uVar13);
      }
      uVar9 = uVar9 - 1;
      *puVar7 = uVar11;
      plVar5 = plVar5 + 1;
      puVar7 = puVar7 + 1;
    } while (uVar9 != 0);
  }
  FUN_ffff0000907a8b48(param_1,param_1,param_3,uVar14);
  plVar5 = DAT_ffff00009090afc0;
  uVar8 = 0;
  uVar9 = 0;
  do {
    uVar11 = uVar9 + 1;
    uVar12 = param_1[uVar9];
    uVar13 = uVar8 + uVar12 * 2;
    param_3[uVar9] = uVar13;
    if (uVar13 != uVar12) {
      uVar8 = (ulong)(uVar13 < uVar12);
    }
    uVar9 = uVar11;
  } while (uVar15 != uVar11);
  uVar2 = uVar3;
  if (uVar8 == 0) {
    do {
      uVar2 = uVar2 - 1;
      if ((int)uVar2 < 0) goto LAB_ffff0000907a86a0;
      if ((ulong)plVar5[uVar2] < param_3[uVar2]) goto LAB_ffff0000907a86a0;
    } while ((ulong)plVar5[uVar2] <= param_3[uVar2]);
  }
  else {
LAB_ffff0000907a86a0:
    uVar8 = 0;
    puVar7 = param_3;
    plVar10 = plVar5;
    uVar9 = uVar15;
    do {
      uVar11 = *plVar10 + uVar8;
      if (uVar11 != 0) {
        uVar8 = (ulong)(*puVar7 < uVar11);
      }
      uVar9 = uVar9 - 1;
      *puVar7 = *puVar7 - uVar11;
      puVar7 = puVar7 + 1;
      plVar10 = plVar10 + 1;
    } while (uVar9 != 0);
  }
  uVar8 = 0;
  uVar9 = 0;
  do {
    uVar11 = uVar9 + 1;
    uVar12 = param_1[uVar9];
    uVar13 = uVar12 + uVar8 + param_3[uVar9];
    param_1[uVar9] = uVar13;
    if (uVar13 != uVar12) {
      uVar8 = (ulong)(uVar13 < uVar12);
    }
    uVar9 = uVar11;
  } while (uVar15 != uVar11);
  uVar2 = uVar3;
  if (uVar8 == 0) {
    do {
      uVar2 = uVar2 - 1;
      if ((int)uVar2 < 0) goto LAB_ffff0000907a8714;
      if ((ulong)plVar5[uVar2] < param_1[uVar2]) goto LAB_ffff0000907a8714;
    } while ((ulong)plVar5[uVar2] <= param_1[uVar2]);
  }
  else {
LAB_ffff0000907a8714:
    uVar8 = 0;
    puVar7 = param_1;
    plVar10 = plVar5;
    uVar9 = uVar15;
    do {
      uVar11 = *plVar10 + uVar8;
      if (uVar11 != 0) {
        uVar8 = (ulong)(*puVar7 < uVar11);
      }
      uVar9 = uVar9 - 1;
      *puVar7 = *puVar7 - uVar11;
      puVar7 = puVar7 + 1;
      plVar10 = plVar10 + 1;
    } while (uVar9 != 0);
  }
  if ((*param_1 & 1) == 0) {
    uVar8 = 0;
    puVar7 = param_1 + uVar15;
    do {
      puVar7 = puVar7 + -1;
      uVar9 = uVar8 | *puVar7 >> 1;
      uVar8 = *puVar7 << 0x3f;
      *puVar7 = uVar9;
    } while (param_1 < puVar7);
  }
  else {
    uVar8 = 0;
    uVar9 = 0;
    do {
      uVar11 = uVar9 + 1;
      uVar12 = param_1[uVar9];
      uVar13 = uVar12 + uVar8 + plVar5[uVar9];
      param_1[uVar9] = uVar13;
      if (uVar13 != uVar12) {
        uVar8 = (ulong)(uVar13 < uVar12);
      }
      uVar9 = uVar11;
    } while (uVar15 != uVar11);
    uVar9 = 0;
    puVar7 = param_1 + uVar15;
    do {
      puVar7 = puVar7 + -1;
      uVar11 = uVar9 | *puVar7 >> 1;
      uVar9 = *puVar7 << 0x3f;
      *puVar7 = uVar11;
    } while (param_1 < puVar7);
    param_1[uVar3 - 1] = param_1[uVar3 - 1] | uVar8 << 0x3f;
  }
  FUN_ffff0000907a9410(param_3,param_1,uVar14);
  plVar5 = DAT_ffff00009090afc0;
  uVar8 = 0;
  uVar9 = 0;
  do {
    uVar11 = uVar9 + 1;
    uVar13 = local_a0[uVar9] + uVar8;
    if (uVar13 != 0) {
      uVar8 = (ulong)(param_3[uVar9] < uVar13);
    }
    param_3[uVar9] = param_3[uVar9] - uVar13;
    uVar9 = uVar11;
  } while (uVar15 != uVar11);
  if (uVar8 != 0) {
    uVar8 = 0;
    puVar7 = param_3;
    plVar10 = plVar5;
    uVar9 = uVar15;
    do {
      uVar13 = *puVar7;
      uVar11 = uVar13 + uVar8 + *plVar10;
      if (uVar11 != uVar13) {
        uVar8 = (ulong)(uVar11 < uVar13);
      }
      uVar9 = uVar9 - 1;
      *puVar7 = uVar11;
      puVar7 = puVar7 + 1;
      plVar10 = plVar10 + 1;
    } while (uVar9 != 0);
  }
  uVar8 = 0;
  uVar9 = 0;
  do {
    uVar11 = uVar9 + 1;
    uVar13 = local_a0[uVar9] + uVar8;
    if (uVar13 != 0) {
      uVar8 = (ulong)(param_3[uVar9] < uVar13);
    }
    param_3[uVar9] = param_3[uVar9] - uVar13;
    uVar9 = uVar11;
  } while (uVar15 != uVar11);
  if (uVar8 != 0) {
    uVar8 = 0;
    puVar7 = param_3;
    plVar10 = plVar5;
    uVar9 = uVar15;
    do {
      uVar13 = *puVar7;
      uVar11 = uVar13 + uVar8 + *plVar10;
      if (uVar11 != uVar13) {
        uVar8 = (ulong)(uVar11 < uVar13);
      }
      uVar9 = uVar9 - 1;
      *puVar7 = uVar11;
      puVar7 = puVar7 + 1;
      plVar10 = plVar10 + 1;
    } while (uVar9 != 0);
  }
  uVar8 = 0;
  uVar9 = 0;
  do {
    uVar11 = uVar9 + 1;
    uVar13 = param_3[uVar9] + uVar8;
    if (uVar13 != 0) {
      uVar8 = (ulong)(local_a0[uVar9] < uVar13);
    }
    local_a0[uVar9] = local_a0[uVar9] - uVar13;
    uVar9 = uVar11;
  } while (uVar15 != uVar11);
  if (uVar8 != 0) {
    uVar8 = 0;
    puVar7 = local_a0;
    uVar9 = uVar15;
    do {
      uVar13 = *puVar7;
      uVar11 = uVar13 + uVar8 + *plVar5;
      if (uVar11 != uVar13) {
        uVar8 = (ulong)(uVar11 < uVar13);
      }
      uVar9 = uVar9 - 1;
      *puVar7 = uVar11;
      plVar5 = plVar5 + 1;
      puVar7 = puVar7 + 1;
    } while (uVar9 != 0);
  }
  FUN_ffff0000907a8b48(param_1,param_1,local_a0,uVar14);
  uVar14 = 0;
  puVar7 = local_a0 + 6;
  puVar6 = param_1;
  uVar8 = uVar15;
  do {
    uVar9 = *puVar7 + uVar14;
    if (uVar9 != 0) {
      uVar14 = (ulong)(*puVar6 < uVar9);
    }
    uVar8 = uVar8 - 1;
    *puVar7 = *puVar6 - uVar9;
    puVar7 = puVar7 + 1;
    puVar6 = puVar6 + 1;
  } while (uVar8 != 0);
  puVar7 = param_3;
  uVar8 = uVar15;
  if (uVar14 != 0) {
    uVar14 = 0;
    plVar5 = DAT_ffff00009090afc0;
    puVar6 = local_a0 + 6;
    uVar9 = uVar15;
    do {
      uVar13 = *puVar6;
      uVar11 = uVar13 + uVar14 + *plVar5;
      if (uVar11 != uVar13) {
        uVar14 = (ulong)(uVar11 < uVar13);
      }
      uVar9 = uVar9 - 1;
      *puVar6 = uVar11;
      plVar5 = plVar5 + 1;
      puVar6 = puVar6 + 1;
    } while (uVar9 != 0);
  }
  do {
    uVar8 = uVar8 - 1;
    *param_1 = *puVar7;
    puVar6 = param_2;
    puVar7 = puVar7 + 1;
    uVar14 = uVar15;
    param_1 = param_1 + 1;
  } while (uVar8 != 0);
  do {
    uVar14 = uVar14 - 1;
    *param_3 = *puVar6;
    puVar6 = puVar6 + 1;
    param_3 = param_3 + 1;
  } while (uVar14 != 0);
  if (uVar3 < 2) {
    param_4 = 0;
  }
  else {
    param_4 = param_4 & 0xfffffffe;
    puVar7 = local_a0 + 7;
    puVar6 = param_2 + 1;
    uVar14 = param_4;
    do {
      puVar1 = puVar7 + -1;
      uVar8 = *puVar7;
      puVar7 = puVar7 + 2;
      uVar14 = uVar14 - 2;
      puVar6[-1] = *puVar1;
      *puVar6 = uVar8;
      puVar6 = puVar6 + 2;
    } while (uVar14 != 0);
    if (param_4 == uVar15) {
      return;
    }
  }
  lVar4 = uVar15 - param_4;
  puVar7 = param_2 + param_4;
  puVar6 = local_a0 + param_4 + 6;
  do {
    lVar4 = lVar4 + -1;
    *puVar7 = *puVar6;
    puVar7 = puVar7 + 1;
    puVar6 = puVar6 + 1;
  } while (lVar4 != 0);
  return;
}


