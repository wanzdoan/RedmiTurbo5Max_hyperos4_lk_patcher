
undefined8 FUN_ffff0000b802f804(int param_1,undefined8 param_2,int *param_3)

{
  int iVar1;
  int iVar2;
  int iVar3;
  int iVar4;
  int iVar5;
  int iVar6;
  uint uVar7;
  uint *puVar8;
  bool bVar9;
  long lVar10;
  int iVar11;
  int iVar12;
  int iVar13;
  int iVar14;
  uint uVar15;
  uint uVar16;
  uint uVar17;
  ulong uVar18;
  uint uVar19;
  uint uVar20;
  uint uVar21;
  uint uVar22;
  uint uVar23;
  uint uVar24;
  int *piVar25;
  long lVar26;
  long *plVar27;
  uint local_74;
  uint local_70;
  int local_6c;
  int local_68;
  int local_64;
  
  local_64 = param_3[0x31];
  if (local_64 == 0) {
    local_64 = param_3[0x30] << 1;
  }
  iVar11 = 0x40c;
  if (param_1 != 0x2c) {
    iVar11 = 0x612;
  }
  if ((uint)param_3[0xc] < 8) {
    local_6c = *(int *)((long)&DAT_ffff0000b80a25c8 + (long)param_3[0xc] * 4);
  }
  else {
    local_6c = 0;
  }
  uVar7 = param_3[0xd] - 8;
  if (uVar7 < 5) {
    if (uVar7 < 5) {
      local_6c = *(int *)(&DAT_ffff0000b80a576c + (long)(int)uVar7 * 4);
    }
    else {
      local_6c = 0;
    }
  }
  iVar2 = param_3[0x1a];
  local_68 = param_3[0x15];
  if (param_3[0x3c] == 0) {
    uVar7 = local_6c * iVar2;
  }
  else {
    uVar7 = param_3[0x53] + param_3[0x53] * param_3[0x47];
  }
  uVar16 = 0;
  if (param_1 != 0x2b) {
    uVar16 = (uint)(param_1 != 0x2d);
  }
  uVar18 = (ulong)uVar16;
  lVar26 = 1;
  if (param_1 != 0x2b) {
    lVar26 = 2;
  }
  iVar3 = *param_3;
  lVar26 = lVar26 - uVar18;
  local_70 = uVar7 * local_68 >> 4;
  if ((uVar7 * local_68 & 0xf) != 0) {
    local_70 = local_70 + 1;
  }
  iVar5 = local_64 * 0x46;
  uVar16 = uVar7 >> 4;
  if ((uVar7 & 0xf) != 0) {
    uVar16 = uVar16 + 1;
  }
  iVar6 = local_64 * 0x23;
  local_6c = local_6c * 0xd;
  local_74 = uVar16 * local_68;
  plVar27 = (long *)(s_0123456789abcdef_ffff0000b80a2a80 + uVar18 * 8 + 0x10);
  piVar25 = &DAT_ffff0000b8126110 + uVar18 * 0x2f6;
  do {
    iVar4 = *piVar25;
    if (iVar3 == 0) {
      iVar12 = param_3[0x36];
      uVar16 = *(uint *)(*plVar27 + 0x30);
      *(uint *)(*plVar27 + 0x30) =
           uVar16 & 0xfe000000 | uVar16 & 0xffffff | (uint)(iVar12 != 0) << 0x18;
      puVar8 = &local_70;
      if (iVar12 != 0) {
        puVar8 = &local_74;
      }
      uVar16 = *puVar8;
      if (iVar4 == 0) {
        uVar18 = (ulong)((uint)(iVar6 * param_3[5]) >> 8);
      }
      else {
        uVar18 = (ulong)((uint)(iVar5 * param_3[5]) >> 5) / 7;
      }
    }
    else {
      uVar18 = 0;
      uVar16 = local_70;
    }
    FUN_ffff0000b804c33c
              (s__s_mode_0x_x_theshold_0x_x_ps_wc_ffff0000b8093768,s_DSI_tx_buf_rw_ffff0000b808ab71,
               iVar3,uVar18,uVar7,iVar2,local_68,uVar16,param_3[0x36],iVar11);
    lVar10 = *plVar27;
    *(uint *)(lVar10 + 0x304) = *(uint *)(lVar10 + 0x304) & 0xffff8000 | (uint)uVar18 & 0x7fff;
    *(uint *)(lVar10 + 0x224) = *(uint *)(lVar10 + 0x224) | 0x400;
    *(uint *)(lVar10 + 0x3f8) = *(uint *)(lVar10 + 0x3f8) & 0xdfffffff;
    iVar3 = *param_3;
    bVar9 = iVar3 != 0;
    uVar24 = param_3[5] * local_64;
    iVar12 = 0x29;
    if (bVar9) {
      iVar12 = 0x51;
    }
    iVar13 = 0x28;
    if (bVar9) {
      iVar13 = 0x48;
    }
    iVar1 = 0x25;
    if (bVar9) {
      iVar1 = 0x23;
    }
    iVar14 = 0x11;
    if (bVar9) {
      iVar14 = 0x1e;
    }
    if (iVar4 == 0) {
      uVar17 = uVar24 * 0x1c >> 8;
      uVar21 = uVar24 * 9 >> 6 & 0xffffff;
      uVar20 = uVar24 * 0x23 >> 8;
      uVar15 = uVar24 >> 8;
      uVar19 = uVar24 * iVar12 >> 8;
      uVar22 = uVar24 * iVar13 >> 8;
      uVar23 = uVar24 * iVar1 >> 8;
      uVar24 = uVar24 * iVar14 >> 8;
    }
    else {
      iVar4 = uVar24 * 2;
      uVar17 = (uVar24 * 0x38) / 0xe0;
      uVar21 = (uVar24 * 9 >> 2 & 0x7ffffff) / 7;
      uVar20 = (uVar24 * 0x46) / 0xe0;
      uVar15 = (uVar24 >> 4 & 0x7ffffff) / 7;
      uVar19 = (uint)(iVar4 * iVar12) / 0xe0;
      uVar22 = (uint)(iVar4 * iVar13) / 0xe0;
      uVar23 = (uint)(iVar4 * iVar1) / 0xe0;
      uVar24 = (uint)(iVar4 * iVar14) / 0xe0;
    }
    *(uint *)(lVar10 + 0x314) = iVar11 - ((local_6c - uVar15) * 0xc) / 10;
    *(uint *)(lVar10 + 0x318) = uVar17;
    *(uint *)(lVar10 + 0x324) = uVar21;
    *(uint *)(lVar10 + 0x328) = uVar20;
    *(uint *)(lVar10 + 0x32c) = uVar19;
    *(uint *)(lVar10 + 0x330) = uVar22;
    *(uint *)(lVar10 + 0x334) = uVar23;
    *(uint *)(lVar10 + 0x338) = uVar24;
    *(uint *)(lVar10 + 0x310) = uVar16;
    *(uint *)(lVar10 + 0x300) = *(uint *)(lVar10 + 0x300) | 1;
    if (iVar3 != 0) {
      *(uint *)(lVar10 + 0x300) = *(uint *)(lVar10 + 0x300) & 0xfffffff7;
      iVar4 = param_3[0x12] + param_3[0x11] + param_3[0x13] + param_3[0x15];
      if ((iVar4 == 0) || (1000000000 < (uint)(iVar4 * 0x3c))) {
        *(undefined4 *)(lVar10 + 0x33c) = 0;
        *(uint *)(lVar10 + 0x300) = *(uint *)(lVar10 + 0x300) | 2;
        uVar16 = *(uint *)(lVar10 + 0x300) & 0xffefffff;
      }
      else {
        uVar16 = 0;
        if (param_3[0x11] + param_3[0x12] != 0) {
          uVar16 = 0xc;
        }
        *(uint *)(lVar10 + 0x300) = *(uint *)(lVar10 + 0x300) & 0xffdfffff;
        *(uint *)(lVar10 + 0x33c) = *(uint *)(lVar10 + 0x33c) & 0xffff0000 | uVar16;
        *(uint *)(lVar10 + 0x300) = *(uint *)(lVar10 + 0x300) & 0xfffffffd;
        uVar16 = *(uint *)(lVar10 + 0x300) | 0x100000;
      }
      *(uint *)(lVar10 + 0x300) = uVar16;
    }
    piVar25 = piVar25 + 0x2f6;
    plVar27 = plVar27 + 1;
    lVar26 = lVar26 + -1;
  } while (lVar26 != 0);
  return 0;
}


