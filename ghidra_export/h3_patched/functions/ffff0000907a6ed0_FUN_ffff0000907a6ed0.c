// ffff0000907a6ed0  size=1744  FUN_ffff0000907a6ed0

void FUN_ffff0000907a6ed0(long *param_1,long param_2,long param_3,ulong *param_4,uint param_5)

{
  bool bVar1;
  ulong uVar2;
  uint uVar3;
  undefined1 auVar4 [16];
  undefined1 auVar5 [16];
  bool bVar6;
  ulong *puVar7;
  ulong uVar8;
  ulong *puVar9;
  ulong uVar10;
  long *plVar11;
  ulong *puVar12;
  ulong uVar13;
  uint uVar14;
  ulong uVar15;
  long lVar16;
  ulong *puVar17;
  ulong uVar18;
  ulong *puVar19;
  uint uVar20;
  int iVar21;
  uint uVar22;
  ulong uVar23;
  uint uVar24;
  ulong uVar25;
  ulong uVar26;
  ulong uVar27;
  ulong uVar28;
  ulong uVar29;
  ulong local_e0 [24];
  
  puVar17 = local_e0;
  local_e0[0x17] = 0;
  uVar3 = param_5 - 1;
  local_e0[0x15] = 0;
  local_e0[0x16] = 0;
  uVar20 = uVar3;
  if (-2 < (int)uVar3) {
    uVar20 = 0xffffffff;
  }
  local_e0[0x13] = 0;
  local_e0[0x14] = 0;
  local_e0[0x11] = 0;
  local_e0[0x12] = 0;
  local_e0[0xf] = 0;
  local_e0[0x10] = 0;
  local_e0[0xd] = 0;
  local_e0[0xe] = 0;
  local_e0[0xb] = 0;
  local_e0[0xc] = 0;
  local_e0[9] = 0;
  local_e0[10] = 0;
  local_e0[7] = 0;
  local_e0[8] = 0;
  local_e0[5] = 0;
  local_e0[6] = 0;
  local_e0[3] = 0;
  local_e0[4] = 0;
  local_e0[1] = 0;
  local_e0[2] = 0;
  local_e0[0] = 0;
  uVar14 = param_5;
  do {
    uVar14 = uVar14 - 1;
    if ((int)uVar14 < 0) {
      if (param_5 < 0x80000001) {
        uVar14 = 0;
        goto LAB_ffff0000907a6f70;
      }
      uVar18 = param_4[uVar20];
      uVar14 = uVar20;
      if (uVar18 == 0) {
        iVar21 = 0;
        goto LAB_ffff0000907a6f6c;
      }
      break;
    }
    uVar18 = param_4[uVar14];
  } while (uVar18 == 0);
  iVar21 = 0;
  do {
    iVar21 = iVar21 + 1;
    bVar1 = 1 < uVar18;
    uVar18 = uVar18 >> 1;
  } while (bVar1);
LAB_ffff0000907a6f6c:
  uVar14 = iVar21 + uVar14 * 0x40;
LAB_ffff0000907a6f70:
  uVar23 = (ulong)(param_5 * 2 - 1);
  uVar15 = (ulong)param_5;
  uVar27 = 0;
  uVar18 = 0;
  uVar8 = 0;
  do {
    uVar25 = uVar8;
    uVar2 = uVar27 + 1;
    uVar24 = 0;
    if (uVar15 <= uVar27) {
      uVar24 = (int)uVar2 - param_5;
    }
    uVar10 = (ulong)uVar24;
    uVar8 = 0;
    if (uVar24 < param_5 && uVar10 <= uVar27) {
      uVar13 = uVar27 - uVar10;
      do {
        uVar26 = *(ulong *)(param_2 + uVar10 * 8);
        uVar28 = *(ulong *)(param_3 + (uVar13 & 0xffffffff) * 8);
        uVar29 = uVar28 * uVar26;
        auVar4._8_8_ = 0;
        auVar4._0_8_ = uVar28;
        auVar5._8_8_ = 0;
        auVar5._0_8_ = uVar26;
        uVar28 = SUB168(auVar4 * auVar5,8);
        bVar6 = CARRY8(uVar29,uVar18);
        uVar18 = uVar29 + uVar18;
        uVar26 = uVar28 + uVar25;
        bVar1 = CARRY8(uVar28,uVar25);
        uVar25 = uVar28 + uVar25 + (ulong)bVar6;
        if (bVar1 || CARRY8(uVar26,(ulong)bVar6)) {
          uVar8 = uVar8 + 1;
        }
        if (uVar27 <= uVar10) break;
        uVar10 = uVar10 + 1;
        uVar13 = uVar13 - 1;
      } while (uVar10 < uVar15);
    }
    local_e0[uVar27 + 0xc] = uVar18;
    uVar27 = uVar2;
    uVar18 = uVar25;
  } while (uVar2 != uVar23);
  puVar19 = local_e0 + uVar15 + 0xc;
  local_e0[uVar23 + 0xc] = uVar25;
  uVar24 = param_5;
  do {
    uVar24 = uVar24 - 1;
    uVar22 = param_5;
    if ((int)uVar24 < 0) {
      if (param_5 < 0x80000001) goto LAB_ffff0000907a7078;
      uVar18 = puVar19[uVar20];
      uVar24 = uVar20;
      if (uVar18 == 0) {
        iVar21 = 0;
        goto LAB_ffff0000907a705c;
      }
      break;
    }
    uVar18 = puVar19[uVar24];
  } while (uVar18 == 0);
  iVar21 = 0;
  do {
    iVar21 = iVar21 + 1;
    bVar1 = 1 < uVar18;
    uVar18 = uVar18 >> 1;
  } while (bVar1);
LAB_ffff0000907a705c:
  iVar21 = iVar21 + uVar24 * 0x40;
  if (iVar21 == 0) {
LAB_ffff0000907a7078:
    do {
      uVar22 = uVar22 - 1;
      if ((int)uVar22 < 0) {
        if (param_5 < 0x80000001) {
          uVar20 = 0;
          uVar24 = -uVar14;
          if (uVar14 != 0) goto LAB_ffff0000907a7210;
          goto LAB_ffff0000907a70a8;
        }
        uVar18 = local_e0[(ulong)uVar20 + 0xc];
        uVar22 = uVar20;
        if (uVar18 == 0) {
          iVar21 = 0;
          goto LAB_ffff0000907a7204;
        }
        break;
      }
      uVar18 = local_e0[(ulong)uVar22 + 0xc];
    } while (uVar18 == 0);
    uVar20 = uVar22;
    iVar21 = 0;
    do {
      iVar21 = iVar21 + 1;
      bVar1 = 1 < uVar18;
      uVar18 = uVar18 >> 1;
    } while (bVar1);
LAB_ffff0000907a7204:
    uVar20 = iVar21 + uVar20 * 0x40;
    uVar24 = uVar20 - uVar14;
  }
  else {
    uVar20 = iVar21 + param_5 * 0x40;
    uVar24 = uVar20 - uVar14;
  }
  if (uVar20 < uVar14) {
LAB_ffff0000907a7210:
    if (param_5 != 0) {
      if (param_5 == 1) {
        uVar8 = 0;
      }
      else {
        uVar8 = uVar15 & 0xfffffffe;
        puVar17 = local_e0 + 0xd;
        puVar19 = (ulong *)(param_1 + 1);
        uVar18 = uVar8;
        do {
          puVar7 = puVar17 + -1;
          uVar27 = *puVar17;
          puVar17 = puVar17 + 2;
          uVar18 = uVar18 - 2;
          puVar19[-1] = *puVar7;
          *puVar19 = uVar27;
          puVar19 = puVar19 + 2;
        } while (uVar18 != 0);
        if (uVar8 == uVar15) {
          return;
        }
      }
      lVar16 = uVar15 - uVar8;
      puVar17 = (ulong *)(param_1 + uVar8);
      puVar19 = local_e0 + uVar8 + 0xc;
      do {
        lVar16 = lVar16 + -1;
        *puVar17 = *puVar19;
        puVar17 = puVar17 + 1;
        puVar19 = puVar19 + 1;
      } while (lVar16 != 0);
    }
    return;
  }
LAB_ffff0000907a70a8:
  if (param_5 == 0) {
    puVar7 = local_e0;
    if ((uVar24 & 0x3f) == 0) goto LAB_ffff0000907a72d0;
    uVar18 = 0;
LAB_ffff0000907a71b8:
    local_e0[(uVar24 >> 6) + param_5] = uVar18;
  }
  else {
    if (param_5 == 1) {
      uVar8 = 0;
LAB_ffff0000907a7100:
      lVar16 = uVar15 - uVar8;
      puVar17 = local_e0 + uVar8;
      do {
        lVar16 = lVar16 + -1;
        *puVar17 = 0;
        puVar17 = puVar17 + 1;
      } while (lVar16 != 0);
    }
    else {
      uVar8 = uVar15 & 0xfffffffe;
      puVar17 = local_e0 + 1;
      uVar18 = uVar8;
      do {
        puVar17[-1] = 0;
        *puVar17 = 0;
        puVar17 = puVar17 + 2;
        uVar18 = uVar18 - 2;
      } while (uVar18 != 0);
      if (uVar8 != uVar15) goto LAB_ffff0000907a7100;
    }
    puVar17 = local_e0 + uVar15;
    if (param_5 < 2) {
      uVar8 = 0;
LAB_ffff0000907a7154:
      lVar16 = uVar15 - uVar8;
      puVar7 = local_e0 + uVar15 + uVar8;
      do {
        lVar16 = lVar16 + -1;
        *puVar7 = 0;
        puVar7 = puVar7 + 1;
      } while (lVar16 != 0);
    }
    else {
      uVar8 = uVar15 & 0xfffffffe;
      puVar7 = local_e0 + uVar15 + 1;
      uVar18 = uVar8;
      do {
        puVar7[-1] = 0;
        *puVar7 = 0;
        puVar7 = puVar7 + 2;
        uVar18 = uVar18 - 2;
      } while (uVar18 != 0);
      if (uVar8 != uVar15) goto LAB_ffff0000907a7154;
    }
    uVar8 = (ulong)(uVar24 >> 6);
    uVar14 = uVar24 & 0x3f;
    if (uVar14 != 0) {
      uVar18 = 0;
      if (param_5 != 0) {
        puVar7 = param_4;
        uVar27 = uVar15;
        puVar9 = local_e0 + uVar8;
        do {
          uVar27 = uVar27 - 1;
          uVar8 = *puVar7 << uVar14 | uVar18;
          uVar18 = *puVar7 >> ((ulong)(0x40 - uVar14) & 0x3f);
          *puVar9 = uVar8;
          puVar7 = puVar7 + 1;
          puVar9 = puVar9 + 1;
        } while (uVar27 != 0);
      }
      goto LAB_ffff0000907a71b8;
    }
    puVar7 = puVar17;
    if (param_5 == 0) goto LAB_ffff0000907a72d0;
    if (param_5 == 1) {
      uVar27 = 0;
LAB_ffff0000907a7574:
      lVar16 = uVar15 - uVar27;
      puVar7 = local_e0 + uVar8 + uVar27;
      puVar9 = param_4 + uVar27;
      do {
        lVar16 = lVar16 + -1;
        *puVar7 = *puVar9;
        puVar7 = puVar7 + 1;
        puVar9 = puVar9 + 1;
      } while (lVar16 != 0);
    }
    else {
      uVar27 = uVar15 & 0xfffffffe;
      puVar7 = param_4 + 1;
      puVar9 = local_e0 + uVar8 + 1;
      uVar18 = uVar27;
      do {
        puVar12 = puVar7 + -1;
        uVar23 = *puVar7;
        puVar7 = puVar7 + 2;
        uVar18 = uVar18 - 2;
        puVar9[-1] = *puVar12;
        *puVar9 = uVar23;
        puVar9 = puVar9 + 2;
      } while (uVar18 != 0);
      if (uVar27 != uVar15) goto LAB_ffff0000907a7574;
    }
  }
  puVar7 = puVar17;
  if (param_5 != 0) {
    if (param_5 == 1) {
      uVar8 = 0;
    }
    else {
      uVar8 = uVar15 & 0xfffffffe;
      plVar11 = param_1 + 1;
      uVar18 = uVar8;
      do {
        plVar11[-1] = 0;
        *plVar11 = 0;
        plVar11 = plVar11 + 2;
        uVar18 = uVar18 - 2;
      } while (uVar18 != 0);
      if (uVar8 == uVar15) goto LAB_ffff0000907a72d0;
    }
    lVar16 = uVar15 - uVar8;
    plVar11 = param_1 + uVar8;
    do {
      lVar16 = lVar16 + -1;
      *plVar11 = 0;
      plVar11 = plVar11 + 1;
    } while (lVar16 != 0);
  }
LAB_ffff0000907a72d0:
  puVar17 = local_e0 + uVar15;
  uVar14 = uVar3;
  if ((int)uVar3 < 1) {
    uVar14 = 0;
  }
  *param_1 = 1;
LAB_ffff0000907a7314:
  uVar18 = (ulong)uVar3;
  uVar24 = uVar3;
  if (uVar20 <= param_5 * 0x40) {
    while (-1 < (int)uVar24) {
      uVar8 = (ulong)uVar24;
      uVar18 = (ulong)uVar24;
      if (param_4[uVar18] < local_e0[uVar8]) break;
      uVar24 = uVar24 - 1;
      if (local_e0[uVar8] < param_4[uVar18]) {
        if (param_5 == 0) {
          return;
        }
        if (param_5 == 1) {
          uVar18 = 0;
        }
        else {
          uVar18 = uVar15 & 0xfffffffe;
          puVar17 = local_e0 + 0xd;
          puVar19 = (ulong *)(param_1 + 1);
          uVar8 = uVar18;
          do {
            puVar7 = puVar17 + -1;
            uVar27 = *puVar17;
            puVar17 = puVar17 + 2;
            uVar8 = uVar8 - 2;
            puVar19[-1] = *puVar7;
            *puVar19 = uVar27;
            puVar19 = puVar19 + 2;
          } while (uVar8 != 0);
          if (uVar18 == uVar15) {
            return;
          }
        }
        lVar16 = uVar15 - uVar18;
        puVar17 = (ulong *)(param_1 + uVar18);
        puVar19 = local_e0 + uVar18 + 0xc;
        do {
          lVar16 = lVar16 + -1;
          *puVar17 = *puVar19;
          puVar17 = puVar17 + 1;
          puVar19 = puVar19 + 1;
        } while (lVar16 != 0);
        return;
      }
    }
    uVar18 = (ulong)uVar3;
  }
  do {
    uVar24 = uVar3;
    if ((int)uVar18 < 0) goto LAB_ffff0000907a737c;
    puVar9 = puVar17 + uVar18;
    puVar12 = puVar19 + uVar18;
    if (*puVar12 < *puVar9) goto LAB_ffff0000907a7450;
    uVar18 = (ulong)((int)uVar18 - 1);
  } while (*puVar12 <= *puVar9);
  goto LAB_ffff0000907a73a0;
  while( true ) {
    uVar8 = (ulong)uVar24;
    uVar18 = (ulong)uVar24;
    if (local_e0[uVar18 + 0xc] < local_e0[uVar8]) goto LAB_ffff0000907a7450;
    uVar24 = uVar24 - 1;
    if (local_e0[uVar8] < local_e0[uVar18 + 0xc]) break;
LAB_ffff0000907a737c:
    if ((int)uVar24 < 0) break;
  }
LAB_ffff0000907a73a0:
  if (param_5 == 0) {
    uVar18 = *puVar17;
  }
  else {
    uVar18 = 0;
    puVar9 = local_e0;
    puVar12 = local_e0 + 0xc;
    uVar8 = uVar15;
    do {
      uVar27 = *puVar9 + uVar18;
      if (uVar27 != 0) {
        uVar18 = (ulong)(*puVar12 < uVar27);
      }
      uVar8 = uVar8 - 1;
      *puVar12 = *puVar12 - uVar27;
      puVar9 = puVar9 + 1;
      puVar12 = puVar12 + 1;
    } while (uVar8 != 0);
    if (uVar18 != 0) {
      uVar18 = 0;
      puVar9 = puVar19;
      plVar11 = param_1;
      uVar8 = uVar15;
      do {
        uVar27 = *plVar11 + uVar18;
        if (uVar27 != 0) {
          uVar18 = (ulong)(*puVar9 < uVar27);
        }
        uVar8 = uVar8 - 1;
        *puVar9 = *puVar9 - uVar27;
        puVar9 = puVar9 + 1;
        plVar11 = plVar11 + 1;
      } while (uVar8 != 0);
    }
    uVar18 = 0;
    puVar9 = puVar19;
    puVar12 = puVar17;
    uVar8 = uVar15;
    do {
      uVar27 = *puVar12 + uVar18;
      if (uVar27 != 0) {
        uVar18 = (ulong)(*puVar9 < uVar27);
      }
      uVar8 = uVar8 - 1;
      *puVar9 = *puVar9 - uVar27;
      puVar9 = puVar9 + 1;
      puVar12 = puVar12 + 1;
    } while (uVar8 != 0);
LAB_ffff0000907a7450:
    uVar18 = *puVar17;
    if (param_5 != 0) {
      uVar8 = 0;
      puVar9 = puVar17 + uVar15;
      do {
        puVar9 = puVar9 + -1;
        uVar27 = uVar8 | *puVar9 >> 1;
        uVar8 = *puVar9 << 0x3f;
        *puVar9 = uVar27;
      } while (puVar7 < puVar9);
      uVar8 = 0;
      puVar9 = puVar7;
      do {
        puVar9 = puVar9 + -1;
        uVar27 = uVar8 | *puVar9 >> 1;
        uVar8 = *puVar9 << 0x3f;
        *puVar9 = uVar27;
      } while (local_e0 < puVar9);
    }
  }
  uVar20 = uVar20 - 1;
  local_e0[uVar14] = local_e0[uVar14] | uVar18 << 0x3f;
  goto LAB_ffff0000907a7314;
}


