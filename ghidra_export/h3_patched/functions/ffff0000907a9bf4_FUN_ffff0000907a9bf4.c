// ffff0000907a9bf4  size=960  FUN_ffff0000907a9bf4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907a9bf4(ulong *param_1,ulong *param_2,uint param_3)

{
  ulong *puVar1;
  uint uVar2;
  long *plVar3;
  ulong uVar4;
  ulong *puVar5;
  long *plVar6;
  ulong uVar7;
  ulong uVar8;
  long lVar9;
  ulong uVar10;
  ulong uVar11;
  ulong *puVar12;
  ulong *puVar13;
  ulong uVar14;
  ulong uVar15;
  ulong local_d0 [18];
  
  uVar4 = (ulong)param_3;
  local_d0[10] = 0;
  local_d0[0xb] = 0;
  local_d0[8] = 0;
  local_d0[9] = 0;
  local_d0[6] = 0;
  local_d0[7] = 0;
  local_d0[4] = 0;
  local_d0[5] = 0;
  local_d0[2] = 0;
  local_d0[3] = 0;
  local_d0[0] = 0;
  local_d0[1] = 0;
  plVar3 = _DAT_ffff00009090af00;
  if (param_3 != 0) {
    uVar8 = 0;
    puVar5 = param_2 + param_3;
    uVar7 = uVar4 & 0xfffffffe;
    puVar1 = local_d0 + 1;
    do {
      while (puVar5[uVar8] != 0) {
        uVar8 = uVar7;
        puVar12 = puVar1;
        if (param_3 < 2) {
          uVar8 = 0;
LAB_ffff0000907a9cb8:
          lVar9 = uVar4 - uVar8;
          puVar12 = local_d0 + uVar8;
          do {
            lVar9 = lVar9 + -1;
            *puVar12 = 0;
            puVar12 = puVar12 + 1;
          } while (lVar9 != 0);
        }
        else {
          do {
            puVar12[-1] = 0;
            *puVar12 = 0;
            uVar8 = uVar8 - 2;
            puVar12 = puVar12 + 2;
          } while (uVar8 != 0);
          uVar8 = uVar7;
          if (uVar7 != uVar4) goto LAB_ffff0000907a9cb8;
        }
        uVar8 = uVar7;
        puVar12 = local_d0 + uVar4 + 1;
        if (param_3 < 2) {
          uVar8 = 0;
LAB_ffff0000907a9d00:
          lVar9 = uVar4 - uVar8;
          puVar12 = local_d0 + uVar4 + uVar8;
          do {
            lVar9 = lVar9 + -1;
            *puVar12 = 0;
            puVar12 = puVar12 + 1;
          } while (lVar9 != 0);
        }
        else {
          do {
            puVar12[-1] = 0;
            *puVar12 = 0;
            uVar8 = uVar8 - 2;
            puVar12 = puVar12 + 2;
          } while (uVar8 != 0);
          uVar8 = uVar7;
          if (uVar7 != uVar4) goto LAB_ffff0000907a9d00;
        }
        local_d0[0x10] = 0;
        local_d0[0x11] = 0;
        local_d0[0xe] = 0;
        local_d0[0xf] = 0;
        local_d0[0xc] = 0;
        local_d0[0xd] = 0;
        uVar8 = uVar7;
        uVar10 = 0;
        puVar12 = puVar1;
        puVar13 = param_2 + uVar4 + 1;
        if (param_3 < 2) {
LAB_ffff0000907a9d5c:
          lVar9 = uVar4 - uVar10;
          puVar12 = local_d0 + uVar10;
          puVar13 = param_2 + uVar4 + uVar10;
          do {
            lVar9 = lVar9 + -1;
            *puVar12 = *puVar13;
            puVar12 = puVar12 + 1;
            puVar13 = puVar13 + 1;
          } while (lVar9 != 0);
        }
        else {
          do {
            uVar10 = *puVar13;
            uVar8 = uVar8 - 2;
            puVar12[-1] = puVar13[-1];
            *puVar12 = uVar10;
            puVar12 = puVar12 + 2;
            puVar13 = puVar13 + 2;
          } while (uVar8 != 0);
          uVar10 = uVar7;
          if (uVar7 != uVar4) goto LAB_ffff0000907a9d5c;
        }
        uVar10 = 0;
        uVar8 = 0;
        do {
          uVar11 = uVar8 + 1;
          uVar14 = uVar10 | puVar5[uVar8] << 0x20;
          uVar10 = puVar5[uVar8] >> 0x20;
          local_d0[uVar8 + 0xc] = uVar14;
          uVar8 = uVar11;
        } while (uVar4 != uVar11);
        lVar9 = 0;
        uVar8 = 0;
        do {
          uVar14 = *(ulong *)((long)puVar1 + lVar9);
          uVar11 = uVar14 + uVar8 + *(long *)((long)local_d0 + lVar9 + 0x60);
          *(ulong *)((long)puVar1 + lVar9) = uVar11;
          if (uVar11 != uVar14) {
            uVar8 = (ulong)(uVar11 < uVar14);
          }
          lVar9 = lVar9 + 8;
        } while (uVar4 * 8 - lVar9 != 0);
        uVar11 = 0;
        local_d0[param_3 + 1] = uVar8 + uVar10;
        puVar12 = local_d0 + 2;
        puVar13 = puVar5;
        uVar8 = uVar4;
        do {
          uVar15 = *puVar12;
          uVar14 = uVar15 + uVar11 + *puVar13;
          if (uVar14 != uVar15) {
            uVar11 = (ulong)(uVar14 < uVar15);
          }
          uVar8 = uVar8 - 1;
          *puVar12 = uVar14;
          puVar12 = puVar12 + 1;
          puVar13 = puVar13 + 1;
        } while (uVar8 != 0);
        lVar9 = 0;
        uVar8 = 0;
        local_d0[param_3 + 2] = uVar11;
        do {
          uVar11 = *(long *)((long)local_d0 + lVar9 + 0x60) + uVar8;
          if (uVar11 != 0) {
            uVar8 = (ulong)(*(ulong *)((long)local_d0 + lVar9) < uVar11);
          }
          *(ulong *)((long)local_d0 + lVar9) = *(ulong *)((long)local_d0 + lVar9) - uVar11;
          lVar9 = lVar9 + 8;
        } while (uVar4 * 8 - lVar9 != 0);
        uVar14 = local_d0[uVar4];
        uVar11 = (ulong)(param_3 + 1);
        if (uVar14 < uVar8 + uVar10) {
          do {
            uVar15 = local_d0[uVar11];
            local_d0[uVar11] = uVar15 - 1;
            uVar11 = (ulong)((int)uVar11 + 1);
          } while (uVar15 == 0);
        }
        local_d0[uVar4] = uVar14 - (uVar8 + uVar10);
        uVar8 = uVar7;
        puVar12 = param_2 + uVar4 + 1;
        if (param_3 < 2) {
          uVar8 = 0;
LAB_ffff0000907a9eb4:
          lVar9 = uVar4 - uVar8;
          puVar12 = param_2 + uVar4 + uVar8;
          do {
            lVar9 = lVar9 + -1;
            *puVar12 = 0;
            puVar12 = puVar12 + 1;
          } while (lVar9 != 0);
        }
        else {
          do {
            puVar12[-1] = 0;
            *puVar12 = 0;
            uVar8 = uVar8 - 2;
            puVar12 = puVar12 + 2;
          } while (uVar8 != 0);
          uVar8 = uVar7;
          if (uVar7 != uVar4) goto LAB_ffff0000907a9eb4;
        }
        uVar8 = 0;
        if (param_3 + 3 != 0) {
          uVar8 = 0;
          puVar12 = local_d0;
          uVar10 = (ulong)(param_3 + 3);
          puVar13 = param_2;
          do {
            uVar14 = *puVar13;
            uVar11 = uVar14 + uVar8 + *puVar12;
            if (uVar11 != uVar14) {
              uVar8 = (ulong)(uVar11 < uVar14);
            }
            uVar10 = uVar10 - 1;
            *puVar13 = uVar11;
            puVar12 = puVar12 + 1;
            puVar13 = puVar13 + 1;
          } while (uVar10 != 0);
          uVar8 = 0;
        }
      }
      uVar8 = uVar8 + 1;
      plVar3 = _DAT_ffff00009090af00;
    } while (uVar8 != uVar4);
  }
  do {
    uVar8 = (ulong)param_3;
LAB_ffff0000907a9f24:
    do {
      uVar2 = (int)uVar8 - 1;
      uVar8 = (ulong)uVar2;
      if ((int)uVar2 < 0) {
LAB_ffff0000907a9f84:
        if (param_3 != 0) {
          do {
            uVar4 = uVar4 - 1;
            *param_1 = *param_2;
            param_1 = param_1 + 1;
            param_2 = param_2 + 1;
          } while (uVar4 != 0);
        }
        return;
      }
      if (param_2[uVar8] <= (ulong)plVar3[uVar8]) {
        if (param_2[uVar8] < (ulong)plVar3[uVar8]) goto LAB_ffff0000907a9f84;
        goto LAB_ffff0000907a9f24;
      }
      uVar8 = (ulong)param_3;
    } while (param_3 == 0);
    uVar8 = 0;
    puVar5 = param_2;
    plVar6 = plVar3;
    uVar7 = uVar4;
    do {
      uVar10 = *plVar6 + uVar8;
      if (uVar10 != 0) {
        uVar8 = (ulong)(*puVar5 < uVar10);
      }
      uVar7 = uVar7 - 1;
      *puVar5 = *puVar5 - uVar10;
      puVar5 = puVar5 + 1;
      plVar6 = plVar6 + 1;
    } while (uVar7 != 0);
  } while( true );
}


