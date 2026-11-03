
void FUN_ffff0000907a66c0(long param_1,long param_2,ulong *param_3,ulong param_4)

{
  ulong *puVar1;
  uint uVar2;
  bool bVar3;
  uint uVar4;
  uint uVar5;
  ulong uVar6;
  long lVar7;
  ulong *puVar8;
  ulong uVar9;
  long *plVar10;
  ulong *puVar11;
  ulong uVar12;
  int iVar13;
  ulong *puVar14;
  ulong *puVar15;
  ulong uVar16;
  ulong uVar17;
  long local_1d0;
  ulong local_1b8 [43];
  
  uVar17 = param_4 & 0xffffffff;
  puVar11 = local_1b8 + 0x18;
  local_1b8[0x1c] = 0;
  local_1b8[0x1d] = 0;
  local_1b8[0x1a] = 0;
  local_1b8[0x1b] = 0;
  local_1b8[0x18] = 0;
  local_1b8[0x19] = 0;
  local_1b8[0x16] = 0;
  local_1b8[0x17] = 0;
  local_1b8[0x14] = 0;
  local_1b8[0x15] = 0;
  local_1b8[0x12] = 0;
  local_1b8[0x13] = 0;
  local_1b8[0x10] = 0;
  local_1b8[0x11] = 0;
  local_1b8[0xe] = 0;
  local_1b8[0xf] = 0;
  local_1b8[0xc] = 0;
  local_1b8[0xd] = 0;
  local_1b8[10] = 0;
  local_1b8[0xb] = 0;
  local_1b8[8] = 0;
  local_1b8[9] = 0;
  local_1b8[6] = 0;
  local_1b8[7] = 0;
  local_1b8[4] = 0;
  local_1b8[5] = 0;
  local_1b8[2] = 0;
  local_1b8[3] = 0;
  local_1b8[0] = 0;
  local_1b8[1] = 0;
  uVar4 = (uint)param_4;
  if (uVar4 == 0) {
    local_1b8[0x22] = 0;
    local_1b8[0x23] = 0;
    local_1b8[0x20] = 0;
    local_1b8[0x21] = 0;
    local_1b8[0x1f] = 0;
  }
  else {
    if (uVar4 == 1) {
      uVar6 = 0;
LAB_ffff0000907a67a0:
      lVar7 = uVar17 - uVar6;
      puVar8 = local_1b8 + uVar6 + 0x18;
      puVar15 = (ulong *)(param_2 + uVar6 * 8);
      do {
        lVar7 = lVar7 + -1;
        *puVar8 = *puVar15;
        puVar8 = puVar8 + 1;
        puVar15 = puVar15 + 1;
      } while (lVar7 != 0);
    }
    else {
      uVar6 = param_4 & 0xfffffffe;
      puVar8 = (ulong *)(param_2 + 8);
      puVar15 = local_1b8 + 0x19;
      uVar12 = uVar6;
      do {
        puVar14 = puVar8 + -1;
        uVar9 = *puVar8;
        puVar8 = puVar8 + 2;
        uVar12 = uVar12 - 2;
        puVar15[-1] = *puVar14;
        *puVar15 = uVar9;
        puVar15 = puVar15 + 2;
      } while (uVar12 != 0);
      if (uVar6 != uVar17) goto LAB_ffff0000907a67a0;
    }
    if (uVar4 < 2) {
      uVar6 = 0;
LAB_ffff0000907a680c:
      lVar7 = uVar17 - uVar6;
      puVar8 = local_1b8 + uVar6 + 0xc;
      puVar15 = (ulong *)(param_2 + uVar6 * 8 + 0x30);
      do {
        lVar7 = lVar7 + -1;
        *puVar8 = *puVar15;
        puVar8 = puVar8 + 1;
        puVar15 = puVar15 + 1;
      } while (lVar7 != 0);
    }
    else {
      uVar6 = param_4 & 0xfffffffe;
      puVar8 = (ulong *)(param_2 + 0x38);
      puVar15 = local_1b8 + 0xd;
      uVar12 = uVar6;
      do {
        puVar14 = puVar8 + -1;
        uVar9 = *puVar8;
        puVar8 = puVar8 + 2;
        uVar12 = uVar12 - 2;
        puVar15[-1] = *puVar14;
        *puVar15 = uVar9;
        puVar15 = puVar15 + 2;
      } while (uVar12 != 0);
      if (uVar6 != uVar17) goto LAB_ffff0000907a680c;
    }
    uVar12 = 0;
    local_1b8[0x22] = 0;
    local_1b8[0x23] = 0;
    local_1b8[0x20] = 0;
    local_1b8[0x21] = 0;
    local_1b8[0x1f] = 0;
    if (uVar4 < 2) {
LAB_ffff0000907a6874:
      lVar7 = uVar17 - uVar12;
      puVar8 = local_1b8 + uVar12 + 0x12;
      do {
        lVar7 = lVar7 + -1;
        *puVar8 = puVar8[6];
        puVar8 = puVar8 + 1;
      } while (lVar7 != 0);
    }
    else {
      uVar12 = param_4 & 0xfffffffe;
      puVar8 = local_1b8 + 0x19;
      uVar6 = uVar12;
      do {
        uVar6 = uVar6 - 2;
        puVar8[-7] = puVar8[-1];
        puVar8[-6] = *puVar8;
        puVar8 = puVar8 + 2;
      } while (uVar6 != 0);
      if (uVar12 != uVar17) goto LAB_ffff0000907a6874;
    }
    if (uVar4 < 2) {
      uVar6 = 0;
LAB_ffff0000907a68d8:
      lVar7 = uVar17 - uVar6;
      puVar8 = local_1b8 + uVar6 + 6;
      do {
        lVar7 = lVar7 + -1;
        *puVar8 = puVar8[6];
        puVar8 = puVar8 + 1;
      } while (lVar7 != 0);
    }
    else {
      uVar6 = param_4 & 0xfffffffe;
      puVar8 = local_1b8 + 0xd;
      uVar12 = uVar6;
      do {
        uVar12 = uVar12 - 2;
        puVar8[-7] = puVar8[-1];
        puVar8[-6] = *puVar8;
        puVar8 = puVar8 + 2;
      } while (uVar12 != 0);
      if (uVar6 != uVar17) goto LAB_ffff0000907a68d8;
    }
    if (uVar4 < 2) {
      uVar6 = 0;
    }
    else {
      uVar6 = param_4 & 0xfffffffe;
      puVar8 = local_1b8 + 0x1f;
      uVar12 = uVar6;
      do {
        puVar8[-1] = 0;
        *puVar8 = 0;
        puVar8 = puVar8 + 2;
        uVar12 = uVar12 - 2;
      } while (uVar12 != 0);
      if (uVar6 == uVar17) goto LAB_ffff0000907a6944;
    }
    lVar7 = uVar17 - uVar6;
    puVar8 = local_1b8 + uVar6 + 0x1e;
    do {
      lVar7 = lVar7 + -1;
      *puVar8 = 0;
      puVar8 = puVar8 + 1;
    } while (lVar7 != 0);
  }
LAB_ffff0000907a6944:
  local_1d0 = param_2 + 0x30;
  puVar8 = local_1b8 + 0xc;
  local_1b8[0x28] = 0;
  local_1b8[0x29] = 0;
  local_1b8[0x1e] = 1;
  local_1b8[0x26] = 0;
  local_1b8[0x27] = 0;
  local_1b8[0x24] = 0;
  local_1b8[0x25] = 0;
  FUN_ffff0000907a9410(local_1b8 + 0x24,local_1b8 + 0x1e,uVar17);
  FUN_ffff0000907a8b48(puVar11,puVar11,local_1b8 + 0x24,uVar17);
  FUN_ffff0000907a8b48(local_1b8 + 0x24,local_1b8 + 0x24,local_1b8 + 0x1e,uVar17);
  FUN_ffff0000907a8b48(puVar8,puVar8,local_1b8 + 0x24,uVar17);
  FUN_ffff0000907a8438(puVar11,puVar8,local_1b8 + 0x1e,param_4 & 0xffffffff);
  local_1b8[0x28] = 0;
  local_1b8[0x29] = 0;
  local_1b8[0x26] = 0;
  local_1b8[0x27] = 0;
  local_1b8[0x24] = 0;
  local_1b8[0x25] = 0;
  FUN_ffff0000907a9410(local_1b8 + 0x24,local_1b8 + 0x1e,param_4 & 0xffffffff);
  FUN_ffff0000907a8b48(local_1b8 + 0x12,local_1b8 + 0x12,local_1b8 + 0x24,param_4 & 0xffffffff);
  FUN_ffff0000907a8b48(local_1b8 + 0x24,local_1b8 + 0x24,local_1b8 + 0x1e,param_4 & 0xffffffff);
  FUN_ffff0000907a8b48(local_1b8 + 6,local_1b8 + 6,local_1b8 + 0x24,param_4 & 0xffffffff);
  uVar5 = uVar4 - 1;
  if (-2 < (int)uVar5) {
    uVar5 = 0xffffffff;
  }
  uVar12 = param_4 & 0xffffffff;
  do {
    uVar2 = (int)uVar12 - 1;
    uVar12 = (ulong)uVar2;
    if ((int)uVar2 < 0) {
      if (uVar4 < 0x80000001) goto LAB_ffff0000907a6af8;
      uVar6 = param_3[uVar5];
      uVar2 = uVar5;
      if (uVar6 == 0) {
        iVar13 = 0;
        goto LAB_ffff0000907a6a68;
      }
      break;
    }
    uVar6 = param_3[uVar12];
  } while (uVar6 == 0);
  uVar5 = uVar2;
  iVar13 = 0;
  do {
    iVar13 = iVar13 + 1;
    bVar3 = 1 < uVar6;
    uVar6 = uVar6 >> 1;
  } while (bVar3);
LAB_ffff0000907a6a68:
  if (0 < (int)(uVar5 * 0x40 + iVar13 + -2)) {
    uVar12 = (ulong)((iVar13 + uVar5 * 0x40) - 1);
    do {
      uVar5 = (int)uVar12 - 1;
      uVar12 = (ulong)uVar5;
      bVar3 = (param_3[uVar5 >> 6] & 1L << (uVar12 & 0x3f)) == 0;
      uVar6 = (ulong)!bVar3;
      uVar9 = (ulong)bVar3;
      FUN_ffff0000907a8c98
                (local_1b8 + uVar6 * 6 + 0x12,local_1b8 + uVar6 * 6 + 6,local_1b8 + uVar9 * 6 + 0x12
                 ,local_1b8 + uVar9 * 6 + 6,uVar17);
      FUN_ffff0000907a7f6c
                (local_1b8 + uVar9 * 6 + 0x12,local_1b8 + uVar9 * 6 + 6,local_1b8 + uVar6 * 6 + 0x12
                 ,local_1b8 + uVar6 * 6 + 6,uVar17);
    } while (1 < uVar5);
  }
LAB_ffff0000907a6af8:
  uVar6 = *param_3 & 1;
  uVar12 = uVar6 | uVar6 << 1;
  uVar6 = uVar6 ^ 1 | (uVar6 ^ 1) << 1;
  puVar11 = local_1b8 + uVar12 * 2 + 0x12;
  puVar8 = local_1b8 + uVar12 * 2 + 6;
  FUN_ffff0000907a8c98(puVar11,puVar8,local_1b8 + uVar6 * 2 + 0x12,local_1b8 + uVar6 * 2 + 6,uVar17)
  ;
  if (uVar4 != 0) {
    uVar9 = param_4 & 0xffffffff;
    uVar17 = 0;
    puVar15 = local_1b8 + 0x12;
    puVar14 = local_1b8;
    uVar12 = uVar9;
    do {
      uVar16 = *puVar15;
      puVar1 = puVar15 + 6;
      puVar15 = puVar15 + 1;
      uVar16 = uVar16 + uVar17;
      if (uVar16 != 0) {
        uVar17 = (ulong)(*puVar1 < uVar16);
      }
      uVar12 = uVar12 - 1;
      *puVar14 = *puVar1 - uVar16;
      puVar14 = puVar14 + 1;
    } while (uVar12 != 0);
    if (uVar17 != 0) {
      uVar17 = 0;
      plVar10 = DAT_ffff00009090afc0;
      puVar15 = local_1b8;
      do {
        uVar16 = *puVar15;
        uVar12 = uVar16 + uVar17 + *plVar10;
        if (uVar12 != uVar16) {
          uVar17 = (ulong)(uVar12 < uVar16);
        }
        uVar9 = uVar9 - 1;
        *puVar15 = uVar12;
        plVar10 = plVar10 + 1;
        puVar15 = puVar15 + 1;
      } while (uVar9 != 0);
    }
  }
  FUN_ffff0000907a8b48(local_1b8,local_1b8,puVar8,param_4 & 0xffffffff);
  FUN_ffff0000907a8b48(local_1b8,local_1b8,param_2,param_4 & 0xffffffff);
  FUN_ffff0000907a7860(local_1b8,local_1b8,DAT_ffff00009090afc0,param_4 & 0xffffffff);
  FUN_ffff0000907a8b48(local_1b8,local_1b8,local_1d0,param_4 & 0xffffffff);
  FUN_ffff0000907a8b48(local_1b8,local_1b8,puVar11,param_4 & 0xffffffff);
  FUN_ffff0000907a7f6c
            (local_1b8 + uVar6 * 2 + 0x12,local_1b8 + uVar6 * 2 + 6,puVar11,puVar8,
             param_4 & 0xffffffff);
  local_1b8[0x28] = 0;
  local_1b8[0x29] = 0;
  local_1b8[0x26] = 0;
  local_1b8[0x27] = 0;
  local_1b8[0x24] = 0;
  local_1b8[0x25] = 0;
  FUN_ffff0000907a9410(local_1b8 + 0x24,local_1b8,param_4 & 0xffffffff);
  FUN_ffff0000907a8b48(local_1b8 + 0x12,local_1b8 + 0x12,local_1b8 + 0x24,param_4 & 0xffffffff);
  FUN_ffff0000907a8b48(local_1b8 + 0x24,local_1b8 + 0x24,local_1b8,param_4 & 0xffffffff);
  FUN_ffff0000907a8b48(local_1b8 + 6,local_1b8 + 6,local_1b8 + 0x24,param_4 & 0xffffffff);
  if (uVar4 == 0) {
    return;
  }
  uVar17 = param_4 & 0xffffffff;
  if (uVar4 == 1) {
    uVar6 = 0;
  }
  else {
    uVar6 = param_4 & 0xfffffffe;
    puVar11 = local_1b8 + 0x13;
    puVar8 = (ulong *)(param_1 + 8);
    uVar12 = uVar6;
    do {
      puVar15 = puVar11 + -1;
      uVar9 = *puVar11;
      puVar11 = puVar11 + 2;
      uVar12 = uVar12 - 2;
      puVar8[-1] = *puVar15;
      *puVar8 = uVar9;
      puVar8 = puVar8 + 2;
    } while (uVar12 != 0);
    if (uVar6 == uVar17) goto LAB_ffff0000907a6d0c;
  }
  lVar7 = uVar17 - uVar6;
  puVar11 = (ulong *)(param_1 + uVar6 * 8);
  puVar8 = local_1b8 + uVar6 + 0x12;
  do {
    lVar7 = lVar7 + -1;
    *puVar11 = *puVar8;
    puVar11 = puVar11 + 1;
    puVar8 = puVar8 + 1;
  } while (lVar7 != 0);
LAB_ffff0000907a6d0c:
  if (uVar4 < 2) {
    param_4 = 0;
  }
  else {
    param_4 = param_4 & 0xfffffffe;
    puVar11 = local_1b8 + 7;
    puVar8 = (ulong *)(param_1 + 0x38);
    uVar12 = param_4;
    do {
      puVar15 = puVar11 + -1;
      uVar6 = *puVar11;
      puVar11 = puVar11 + 2;
      uVar12 = uVar12 - 2;
      puVar8[-1] = *puVar15;
      *puVar8 = uVar6;
      puVar8 = puVar8 + 2;
    } while (uVar12 != 0);
    if (param_4 == uVar17) {
      return;
    }
  }
  lVar7 = uVar17 - param_4;
  puVar11 = (ulong *)(param_4 * 8 + param_1 + 0x30);
  puVar8 = local_1b8 + param_4 + 6;
  do {
    lVar7 = lVar7 + -1;
    *puVar11 = *puVar8;
    puVar11 = puVar11 + 1;
    puVar8 = puVar8 + 1;
  } while (lVar7 != 0);
  return;
}


