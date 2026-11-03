
void FUN_ffff0000907a9a64(ulong *param_1,ulong *param_2,uint param_3)

{
  ulong uVar1;
  uint uVar2;
  long *plVar3;
  ulong *puVar4;
  ulong uVar5;
  ulong uVar6;
  ulong *puVar7;
  ulong uVar8;
  ulong uVar9;
  ulong uVar10;
  ulong uVar11;
  ulong uVar12;
  long *plVar13;
  ulong uVar14;
  long lVar15;
  ulong uVar16;
  ulong local_30 [4];
  undefined8 local_10;
  undefined8 uStack_8;
  
  local_10 = 0;
  uStack_8 = 0;
  if (param_3 != 0) {
    uVar5 = (ulong)param_3;
    puVar4 = param_2;
    puVar7 = param_1;
    do {
      uVar5 = uVar5 - 1;
      *puVar7 = *puVar4;
      puVar4 = puVar4 + 1;
      puVar7 = puVar7 + 1;
    } while (uVar5 != 0);
  }
  local_30[2] = param_2[6];
  uVar8 = param_2[7];
  local_30[0] = 0;
  local_30[1] = (ulong)*(uint *)((long)param_2 + 0x2c) << 0x20;
  uVar5 = 0;
  local_30[3] = uVar8;
  if (param_3 != 0) {
    uVar6 = (ulong)param_3;
    puVar4 = local_30;
    uVar8 = uVar6;
    do {
      uVar8 = uVar8 - 1;
      uVar10 = uVar5 | *puVar4 << 1;
      uVar5 = -((long)*puVar4 >> 0x3f);
      *puVar4 = uVar10;
      puVar4 = puVar4 + 1;
    } while (uVar8 != 0);
    uVar10 = 0;
    puVar4 = local_30;
    puVar7 = param_1;
    do {
      uVar11 = *puVar7;
      uVar8 = uVar11 + uVar10 + *puVar4;
      if (uVar8 != uVar11) {
        uVar10 = (ulong)(uVar8 < uVar11);
      }
      uVar6 = uVar6 - 1;
      *puVar7 = uVar8;
      puVar4 = puVar4 + 1;
      puVar7 = puVar7 + 1;
    } while (uVar6 != 0);
    local_30[2] = param_2[6];
    uVar8 = param_2[7];
    uVar5 = uVar10 + uVar5;
  }
  local_30[1] = local_30[2] << 0x20;
  local_30[2] = local_30[2] >> 0x20 | uVar8 << 0x20;
  local_30[3] = uVar8 >> 0x20;
  if (param_3 == 0) {
    uVar6 = 0;
  }
  else {
    uVar10 = (ulong)param_3;
    uVar11 = 0;
    puVar4 = local_30;
    uVar8 = uVar10;
    do {
      uVar8 = uVar8 - 1;
      uVar6 = uVar11 | *puVar4 << 1;
      uVar11 = -((long)*puVar4 >> 0x3f);
      *puVar4 = uVar6;
      puVar4 = puVar4 + 1;
    } while (uVar8 != 0);
    uVar6 = 0;
    uVar5 = uVar11 + uVar5;
    puVar4 = local_30;
    puVar7 = param_1;
    do {
      uVar11 = *puVar7;
      uVar8 = uVar11 + uVar6 + *puVar4;
      if (uVar8 != uVar11) {
        uVar6 = (ulong)(uVar8 < uVar11);
      }
      uVar10 = uVar10 - 1;
      *puVar7 = uVar8;
      puVar4 = puVar4 + 1;
      puVar7 = puVar7 + 1;
    } while (uVar10 != 0);
    uVar8 = param_2[7];
  }
  uVar14 = param_2[4];
  uVar11 = param_2[5];
  local_30[2] = 0;
  local_30[3] = uVar8;
  local_30[0] = uVar14;
  local_30[1] = uVar11 & 0xffffffff;
  uVar10 = 0;
  if (param_3 != 0) {
    uVar8 = (ulong)param_3;
    puVar4 = local_30;
    puVar7 = param_1;
    do {
      uVar14 = *puVar7;
      uVar11 = uVar14 + uVar10 + *puVar4;
      if (uVar11 != uVar14) {
        uVar10 = (ulong)(uVar11 < uVar14);
      }
      uVar8 = uVar8 - 1;
      *puVar7 = uVar11;
      puVar4 = puVar4 + 1;
      puVar7 = puVar7 + 1;
    } while (uVar8 != 0);
    uVar14 = param_2[4];
    uVar11 = param_2[5];
    uVar8 = param_2[7];
  }
  uVar16 = param_2[6];
  local_30[2] = uVar8;
  local_30[3] = uVar16 >> 0x20 | uVar14 << 0x20;
  uVar8 = 0;
  local_30[0] = uVar14 >> 0x20 | uVar11 << 0x20;
  local_30[1] = uVar16 & 0xffffffff00000000 | uVar11 >> 0x20;
  if (param_3 != 0) {
    uVar11 = (ulong)param_3;
    puVar4 = local_30;
    puVar7 = param_1;
    do {
      uVar16 = *puVar7;
      uVar14 = uVar16 + uVar8 + *puVar4;
      if (uVar14 != uVar16) {
        uVar8 = (ulong)(uVar14 < uVar16);
      }
      uVar11 = uVar11 - 1;
      *puVar7 = uVar14;
      puVar4 = puVar4 + 1;
      puVar7 = puVar7 + 1;
    } while (uVar11 != 0);
    uVar11 = param_2[5];
    uVar16 = param_2[6];
    uVar14 = param_2[4];
  }
  local_30[0] = uVar11 >> 0x20 | uVar16 << 0x20;
  local_30[1] = uVar16 >> 0x20;
  uVar9 = 0;
  local_30[2] = 0;
  local_30[3] = uVar14 & 0xffffffff | uVar11 << 0x20;
  if (param_3 != 0) {
    uVar11 = (ulong)param_3;
    puVar4 = local_30;
    puVar7 = param_1;
    do {
      uVar14 = *puVar4 + uVar9;
      if (uVar14 != 0) {
        uVar9 = (ulong)(*puVar7 < uVar14);
      }
      uVar11 = uVar11 - 1;
      *puVar7 = *puVar7 - uVar14;
      puVar4 = puVar4 + 1;
      puVar7 = puVar7 + 1;
    } while (uVar11 != 0);
    uVar11 = param_2[5];
    uVar16 = param_2[6];
    uVar14 = param_2[4];
  }
  local_30[1] = param_2[7];
  local_30[0] = uVar16;
  local_30[2] = 0;
  local_30[3] = uVar11 & 0xffffffff00000000 | uVar14 >> 0x20;
  uVar12 = 0;
  if (param_3 != 0) {
    uVar11 = (ulong)param_3;
    puVar4 = local_30;
    puVar7 = param_1;
    do {
      uVar14 = *puVar4 + uVar12;
      if (uVar14 != 0) {
        uVar12 = (ulong)(*puVar7 < uVar14);
      }
      uVar11 = uVar11 - 1;
      *puVar7 = *puVar7 - uVar14;
      puVar4 = puVar4 + 1;
      puVar7 = puVar7 + 1;
    } while (uVar11 != 0);
    uVar16 = param_2[6];
    local_30[1] = param_2[7];
    uVar14 = param_2[4];
    uVar11 = param_2[5];
  }
  local_30[0] = uVar16 >> 0x20 | local_30[1] << 0x20;
  local_30[1] = local_30[1] >> 0x20 | uVar14 << 0x20;
  local_30[2] = uVar14 >> 0x20 | uVar11 << 0x20;
  local_30[3] = uVar16 << 0x20;
  if (param_3 == 0) {
    uVar14 = 0;
    uVar11 = 0;
  }
  else {
    lVar15 = 0;
    uVar14 = 0;
    do {
      uVar11 = *(long *)((long)local_30 + lVar15) + uVar14;
      if (uVar11 != 0) {
        uVar14 = (ulong)(*(ulong *)((long)param_1 + lVar15) < uVar11);
      }
      *(ulong *)((long)param_1 + lVar15) = *(ulong *)((long)param_1 + lVar15) - uVar11;
      lVar15 = lVar15 + 8;
    } while ((ulong)param_3 << 3 != lVar15);
    local_30[0] = param_2[7];
    local_30[1] = (ulong)*(uint *)((long)param_2 + 0x24) << 0x20;
    uVar11 = 0;
    local_30[2] = param_2[5];
    local_30[3] = (ulong)*(uint *)((long)param_2 + 0x34) << 0x20;
    if (param_3 != 0) {
      uVar16 = (ulong)param_3;
      puVar4 = param_1;
      puVar7 = local_30;
      do {
        uVar1 = *puVar7 + uVar11;
        if (uVar1 != 0) {
          uVar11 = (ulong)(*puVar4 < uVar1);
        }
        uVar16 = uVar16 - 1;
        *puVar4 = *puVar4 - uVar1;
        puVar4 = puVar4 + 1;
        puVar7 = puVar7 + 1;
      } while (uVar16 != 0);
    }
  }
  plVar3 = DAT_ffff00009090afc0;
  uVar11 = ((uVar6 + uVar5 + uVar10 + uVar8) - (uVar9 + uVar12 + uVar14)) - uVar11;
  if ((int)uVar11 < 0) {
    do {
      uVar5 = 0;
      puVar4 = param_1;
      plVar13 = plVar3;
      uVar8 = (ulong)param_3;
      if (param_3 != 0) {
        do {
          uVar10 = *puVar4;
          uVar6 = uVar10 + uVar5 + *plVar13;
          if (uVar6 != uVar10) {
            uVar5 = (ulong)(uVar6 < uVar10);
          }
          uVar8 = uVar8 - 1;
          *puVar4 = uVar6;
          puVar4 = puVar4 + 1;
          plVar13 = plVar13 + 1;
        } while (uVar8 != 0);
      }
      uVar11 = uVar5 + (long)(int)uVar11;
    } while ((int)uVar11 < 0);
    return;
  }
  do {
    uVar2 = param_3;
    if ((int)uVar11 == 0) {
      do {
        uVar2 = uVar2 - 1;
        if ((int)uVar2 < 0) break;
        if (param_1[uVar2] < (ulong)plVar3[uVar2]) {
          return;
        }
      } while (param_1[uVar2] <= (ulong)plVar3[uVar2]);
    }
    uVar5 = 0;
    puVar4 = param_1;
    plVar13 = plVar3;
    uVar8 = (ulong)param_3;
    if (param_3 != 0) {
      do {
        uVar6 = *plVar13 + uVar5;
        if (uVar6 != 0) {
          uVar5 = (ulong)(*puVar4 < uVar6);
        }
        uVar8 = uVar8 - 1;
        *puVar4 = *puVar4 - uVar6;
        puVar4 = puVar4 + 1;
        plVar13 = plVar13 + 1;
      } while (uVar8 != 0);
    }
    uVar11 = (ulong)(uint)((int)uVar11 - (int)uVar5);
  } while( true );
}


