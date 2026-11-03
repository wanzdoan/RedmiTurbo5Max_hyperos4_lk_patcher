
void FUN_ffff0000be082854(long param_1,long *param_2,long *param_3,uint param_4)

{
  ulong *puVar1;
  ulong *puVar2;
  uint uVar3;
  ulong *puVar4;
  long *plVar5;
  ulong uVar6;
  long lVar7;
  ulong uVar8;
  ulong uVar9;
  undefined8 *puVar10;
  ulong *puVar11;
  ulong *puVar12;
  ulong uVar13;
  ulong uVar14;
  ulong uVar15;
  ulong local_e0 [24];
  
  local_e0[0x16] = 0;
  local_e0[0x17] = 0;
  local_e0[0x14] = 0;
  local_e0[0x15] = 0;
  local_e0[0x12] = 0;
  local_e0[0x13] = 0;
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
  local_e0[7] = 0;
  local_e0[4] = 0;
  local_e0[5] = 0;
  local_e0[2] = 0;
  local_e0[3] = 0;
  local_e0[0] = 0;
  local_e0[1] = 0;
  if (param_4 == 0) {
    return;
  }
  uVar6 = (ulong)param_4;
  plVar5 = param_2;
  uVar9 = uVar6;
  while (*plVar5 == 0) {
    uVar9 = uVar9 - 1;
    plVar5 = plVar5 + 1;
    if (uVar9 == 0) {
      if (param_4 < 2) {
        uVar8 = 0;
      }
      else {
        uVar8 = uVar6 & 0xfffffffe;
        puVar10 = (undefined8 *)(param_1 + 8);
        uVar9 = uVar8;
        do {
          puVar10[-1] = 0;
          *puVar10 = 0;
          puVar10 = puVar10 + 2;
          uVar9 = uVar9 - 2;
        } while (uVar9 != 0);
        if (uVar8 == uVar6) {
          return;
        }
      }
      lVar7 = uVar6 - uVar8;
      puVar10 = (undefined8 *)(param_1 + uVar8 * 8);
      do {
        lVar7 = lVar7 + -1;
        *puVar10 = 0;
        puVar10 = puVar10 + 1;
      } while (lVar7 != 0);
      return;
    }
  }
  if (param_4 < 2) {
    uVar9 = 0;
LAB_ffff0000be082948:
    lVar7 = uVar6 - uVar9;
    puVar11 = local_e0 + uVar9 + 0x12;
    puVar12 = (ulong *)(param_2 + uVar9);
    do {
      lVar7 = lVar7 + -1;
      *puVar11 = *puVar12;
      puVar11 = puVar11 + 1;
      puVar12 = puVar12 + 1;
    } while (lVar7 != 0);
  }
  else {
    uVar9 = uVar6 & 0xfffffffe;
    puVar11 = (ulong *)(param_2 + 1);
    puVar12 = local_e0 + 0x13;
    uVar8 = uVar9;
    do {
      puVar2 = puVar11 + -1;
      uVar13 = *puVar11;
      puVar11 = puVar11 + 2;
      uVar8 = uVar8 - 2;
      puVar12[-1] = *puVar2;
      *puVar12 = uVar13;
      puVar12 = puVar12 + 2;
    } while (uVar8 != 0);
    if (uVar9 != uVar6) goto LAB_ffff0000be082948;
  }
  if (param_4 < 2) {
    uVar8 = 0;
LAB_ffff0000be0829b0:
    lVar7 = uVar6 - uVar8;
    puVar11 = local_e0 + uVar8 + 0xc;
    puVar12 = (ulong *)(param_3 + uVar8);
    do {
      lVar7 = lVar7 + -1;
      *puVar11 = *puVar12;
      puVar11 = puVar11 + 1;
      puVar12 = puVar12 + 1;
    } while (lVar7 != 0);
  }
  else {
    uVar8 = uVar6 & 0xfffffffe;
    puVar11 = (ulong *)(param_3 + 1);
    puVar12 = local_e0 + 0xd;
    uVar9 = uVar8;
    do {
      puVar2 = puVar11 + -1;
      uVar13 = *puVar11;
      puVar11 = puVar11 + 2;
      uVar9 = uVar9 - 2;
      puVar12[-1] = *puVar2;
      *puVar12 = uVar13;
      puVar12 = puVar12 + 2;
    } while (uVar9 != 0);
    if (uVar8 != uVar6) goto LAB_ffff0000be0829b0;
  }
  if (param_4 < 2) {
    uVar8 = 0;
LAB_ffff0000be082a0c:
    lVar7 = uVar6 - uVar8;
    puVar11 = local_e0 + uVar8 + 6;
    do {
      lVar7 = lVar7 + -1;
      *puVar11 = 0;
      puVar11 = puVar11 + 1;
    } while (lVar7 != 0);
  }
  else {
    uVar8 = uVar6 & 0xfffffffe;
    puVar11 = local_e0 + 7;
    uVar9 = uVar8;
    do {
      puVar11[-1] = 0;
      *puVar11 = 0;
      puVar11 = puVar11 + 2;
      uVar9 = uVar9 - 2;
    } while (uVar9 != 0);
    if (uVar8 != uVar6) goto LAB_ffff0000be082a0c;
  }
  local_e0[6] = 1;
  if (param_4 < 2) {
    uVar8 = 0;
  }
  else {
    uVar8 = uVar6 & 0xfffffffe;
    puVar11 = local_e0 + 1;
    uVar9 = uVar8;
    do {
      puVar11[-1] = 0;
      *puVar11 = 0;
      puVar11 = puVar11 + 2;
      uVar9 = uVar9 - 2;
    } while (uVar9 != 0);
    if (uVar8 == uVar6) goto LAB_ffff0000be082a7c;
  }
  lVar7 = uVar6 - uVar8;
  puVar11 = local_e0 + uVar8;
  do {
    lVar7 = lVar7 + -1;
    *puVar11 = 0;
    puVar11 = puVar11 + 1;
  } while (lVar7 != 0);
LAB_ffff0000be082a7c:
  uVar8 = (ulong)(param_4 - 1);
  puVar11 = local_e0 + 0xc;
  puVar12 = local_e0 + 0x12;
  puVar2 = local_e0 + 6;
  uVar9 = (ulong)param_4;
  do {
    do {
      while( true ) {
        do {
          uVar3 = (int)uVar9 - 1;
          uVar9 = (ulong)uVar3;
          if ((int)uVar3 < 0) {
            if (param_4 < 2) {
              uVar8 = 0;
            }
            else {
              uVar8 = uVar6 & 0xfffffffe;
              puVar11 = local_e0 + 7;
              puVar12 = (ulong *)(param_1 + 8);
              uVar9 = uVar8;
              do {
                puVar2 = puVar11 + -1;
                uVar13 = *puVar11;
                puVar11 = puVar11 + 2;
                uVar9 = uVar9 - 2;
                puVar12[-1] = *puVar2;
                *puVar12 = uVar13;
                puVar12 = puVar12 + 2;
              } while (uVar9 != 0);
              if (uVar8 == uVar6) {
                return;
              }
            }
            lVar7 = uVar6 - uVar8;
            puVar11 = (ulong *)(param_1 + uVar8 * 8);
            puVar12 = local_e0 + uVar8 + 6;
            do {
              lVar7 = lVar7 + -1;
              *puVar11 = *puVar12;
              puVar11 = puVar11 + 1;
              puVar12 = puVar12 + 1;
            } while (lVar7 != 0);
            return;
          }
        } while (puVar12[uVar9] == puVar11[uVar9]);
        if ((local_e0[0x12] & 1) != 0) break;
        uVar9 = 0;
        puVar4 = puVar12 + uVar6;
        do {
          puVar4 = puVar4 + -1;
          uVar13 = uVar9 | *puVar4 >> 1;
          uVar9 = *puVar4 << 0x3f;
          *puVar4 = uVar13;
        } while (puVar12 < puVar4);
        if ((local_e0[6] & 1) == 0) {
          uVar13 = 0;
        }
        else {
          uVar13 = 0;
          puVar4 = local_e0 + 6;
          plVar5 = param_3;
          uVar9 = uVar6;
          do {
            uVar14 = *puVar4;
            uVar15 = uVar14 + uVar13 + *plVar5;
            if (uVar15 != uVar14) {
              uVar13 = (ulong)(uVar15 < uVar14);
            }
            uVar9 = uVar9 - 1;
            *puVar4 = uVar15;
            puVar4 = puVar4 + 1;
            plVar5 = plVar5 + 1;
          } while (uVar9 != 0);
        }
        uVar9 = 0;
        puVar4 = puVar2 + uVar6;
        do {
          puVar4 = puVar4 + -1;
          uVar15 = uVar9 | *puVar4 >> 1;
          uVar9 = *puVar4 << 0x3f;
          *puVar4 = uVar15;
        } while (puVar2 < puVar4);
LAB_ffff0000be082bac:
        uVar9 = (ulong)param_4;
        puVar4 = puVar2;
        if (uVar13 != 0) goto LAB_ffff0000be082ab8;
      }
      if ((local_e0[0xc] & 1) == 0) {
        uVar9 = 0;
        puVar4 = puVar11 + uVar6;
        do {
          puVar4 = puVar4 + -1;
          uVar13 = uVar9 | *puVar4 >> 1;
          uVar9 = *puVar4 << 0x3f;
          *puVar4 = uVar13;
        } while (puVar11 < puVar4);
        if ((local_e0[0] & 1) == 0) {
          uVar13 = 0;
        }
        else {
          uVar13 = 0;
          puVar4 = local_e0;
          plVar5 = param_3;
          uVar9 = uVar6;
          do {
            uVar14 = *puVar4;
            uVar15 = uVar14 + uVar13 + *plVar5;
            if (uVar15 != uVar14) {
              uVar13 = (ulong)(uVar15 < uVar14);
            }
            uVar9 = uVar9 - 1;
            *puVar4 = uVar15;
            puVar4 = puVar4 + 1;
            plVar5 = plVar5 + 1;
          } while (uVar9 != 0);
        }
        uVar9 = 0;
        puVar4 = local_e0 + uVar6;
        do {
          puVar4 = puVar4 + -1;
          uVar15 = uVar9 | *puVar4 >> 1;
          uVar9 = *puVar4 << 0x3f;
          *puVar4 = uVar15;
        } while (local_e0 < puVar4);
      }
      else {
        if (puVar11[uVar9] < puVar12[uVar9]) {
          uVar9 = 0;
          uVar13 = 0;
          do {
            uVar15 = uVar13 + 1;
            uVar14 = puVar11[uVar13] + uVar9;
            if (uVar14 != 0) {
              uVar9 = (ulong)(puVar12[uVar13] < uVar14);
            }
            puVar12[uVar13] = puVar12[uVar13] - uVar14;
            uVar13 = uVar15;
          } while (uVar6 != uVar15);
          uVar9 = 0;
          puVar4 = puVar12 + uVar6;
          do {
            puVar4 = puVar4 + -1;
            uVar13 = uVar9 | *puVar4 >> 1;
            uVar9 = *puVar4 << 0x3f;
            *puVar4 = uVar13;
            uVar13 = uVar8;
          } while (puVar12 < puVar4);
          do {
            if ((int)uVar13 < 0) goto LAB_ffff0000be082c7c;
            puVar4 = puVar2 + uVar13;
            puVar1 = local_e0 + uVar13;
            if (*puVar1 < *puVar4) goto LAB_ffff0000be082c7c;
            uVar13 = (ulong)((int)uVar13 - 1);
          } while (*puVar1 <= *puVar4);
          uVar9 = 0;
          puVar4 = local_e0 + 6;
          plVar5 = param_3;
          uVar13 = uVar6;
          do {
            uVar14 = *puVar4;
            uVar15 = uVar14 + uVar9 + *plVar5;
            if (uVar15 != uVar14) {
              uVar9 = (ulong)(uVar15 < uVar14);
            }
            uVar13 = uVar13 - 1;
            *puVar4 = uVar15;
            puVar4 = puVar4 + 1;
            plVar5 = plVar5 + 1;
          } while (uVar13 != 0);
LAB_ffff0000be082c7c:
          uVar9 = 0;
          uVar13 = 0;
          do {
            uVar15 = uVar13 + 1;
            uVar14 = local_e0[uVar13] + uVar9;
            if (uVar14 != 0) {
              uVar9 = (ulong)(puVar2[uVar13] < uVar14);
            }
            puVar2[uVar13] = puVar2[uVar13] - uVar14;
            uVar13 = uVar15;
          } while (uVar6 != uVar15);
          if ((local_e0[6] & 1) == 0) {
            uVar13 = 0;
          }
          else {
            uVar13 = 0;
            puVar4 = local_e0 + 6;
            plVar5 = param_3;
            uVar9 = uVar6;
            do {
              uVar14 = *puVar4;
              uVar15 = uVar14 + uVar13 + *plVar5;
              if (uVar15 != uVar14) {
                uVar13 = (ulong)(uVar15 < uVar14);
              }
              uVar9 = uVar9 - 1;
              *puVar4 = uVar15;
              puVar4 = puVar4 + 1;
              plVar5 = plVar5 + 1;
            } while (uVar9 != 0);
          }
          uVar9 = 0;
          puVar4 = puVar2 + uVar6;
          do {
            puVar4 = puVar4 + -1;
            uVar15 = uVar9 | *puVar4 >> 1;
            uVar9 = *puVar4 << 0x3f;
            *puVar4 = uVar15;
          } while (puVar2 < puVar4);
          goto LAB_ffff0000be082bac;
        }
        uVar9 = 0;
        uVar13 = 0;
        do {
          uVar15 = uVar13 + 1;
          uVar14 = puVar12[uVar13] + uVar9;
          if (uVar14 != 0) {
            uVar9 = (ulong)(puVar11[uVar13] < uVar14);
          }
          puVar11[uVar13] = puVar11[uVar13] - uVar14;
          uVar13 = uVar15;
        } while (uVar6 != uVar15);
        uVar9 = 0;
        puVar4 = puVar11 + uVar6;
        do {
          puVar4 = puVar4 + -1;
          uVar13 = uVar9 | *puVar4 >> 1;
          uVar9 = *puVar4 << 0x3f;
          *puVar4 = uVar13;
          uVar13 = uVar8;
        } while (puVar11 < puVar4);
        do {
          if ((int)uVar13 < 0) goto LAB_ffff0000be082dd8;
          puVar4 = local_e0 + uVar13;
          puVar1 = puVar2 + uVar13;
          if (*puVar1 < *puVar4) goto LAB_ffff0000be082dd8;
          uVar13 = (ulong)((int)uVar13 - 1);
        } while (*puVar1 <= *puVar4);
        uVar9 = 0;
        puVar4 = local_e0;
        plVar5 = param_3;
        uVar13 = uVar6;
        do {
          uVar14 = *puVar4;
          uVar15 = uVar14 + uVar9 + *plVar5;
          if (uVar15 != uVar14) {
            uVar9 = (ulong)(uVar15 < uVar14);
          }
          uVar13 = uVar13 - 1;
          *puVar4 = uVar15;
          puVar4 = puVar4 + 1;
          plVar5 = plVar5 + 1;
        } while (uVar13 != 0);
LAB_ffff0000be082dd8:
        uVar9 = 0;
        uVar13 = 0;
        do {
          uVar15 = uVar13 + 1;
          uVar14 = puVar2[uVar13] + uVar9;
          if (uVar14 != 0) {
            uVar9 = (ulong)(local_e0[uVar13] < uVar14);
          }
          local_e0[uVar13] = local_e0[uVar13] - uVar14;
          uVar13 = uVar15;
        } while (uVar6 != uVar15);
        if ((local_e0[0] & 1) == 0) {
          uVar13 = 0;
        }
        else {
          uVar13 = 0;
          puVar4 = local_e0;
          plVar5 = param_3;
          uVar9 = uVar6;
          do {
            uVar14 = *puVar4;
            uVar15 = uVar14 + uVar13 + *plVar5;
            if (uVar15 != uVar14) {
              uVar13 = (ulong)(uVar15 < uVar14);
            }
            uVar9 = uVar9 - 1;
            *puVar4 = uVar15;
            puVar4 = puVar4 + 1;
            plVar5 = plVar5 + 1;
          } while (uVar9 != 0);
        }
        uVar9 = 0;
        puVar4 = local_e0 + uVar6;
        do {
          puVar4 = puVar4 + -1;
          uVar15 = uVar9 | *puVar4 >> 1;
          uVar9 = *puVar4 << 0x3f;
          *puVar4 = uVar15;
        } while (local_e0 < puVar4);
      }
      uVar9 = (ulong)param_4;
      puVar4 = local_e0;
    } while (uVar13 == 0);
LAB_ffff0000be082ab8:
    uVar9 = (ulong)param_4;
    puVar4[uVar8] = puVar4[uVar8] | 0x8000000000000000;
  } while( true );
}


