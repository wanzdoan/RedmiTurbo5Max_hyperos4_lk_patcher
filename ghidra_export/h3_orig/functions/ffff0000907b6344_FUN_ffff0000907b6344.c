// ffff0000907b6344  size=1112  FUN_ffff0000907b6344

int FUN_ffff0000907b6344
              (int param_1,char *param_2,int param_3,char *param_4,int param_5,long param_6,
              uint param_7)

{
  char *pcVar1;
  long lVar2;
  int iVar3;
  int iVar4;
  char *pcVar5;
  char *pcVar6;
  char *pcVar7;
  long lVar8;
  ulong uVar9;
  ulong uVar10;
  char *pcVar11;
  ulong uVar12;
  char *pcVar13;
  char *pcVar14;
  ulong uVar15;
  undefined8 *puVar16;
  char cVar17;
  char local_a0 [64];
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  
  pcVar5 = local_a0;
  pcVar7 = local_a0;
  if (param_2 == (char *)0x0) {
    return -1;
  }
  if (param_4 == (char *)0x0) {
    return -1;
  }
  if (param_6 == 0) {
    return -1;
  }
  if (param_1 == 2) {
    local_50 = 0;
    uStack_48 = 0;
    local_60 = 0;
    uStack_58 = 0;
    local_a0[0x30] = '\0';
    local_a0[0x31] = '\0';
    local_a0[0x32] = '\0';
    local_a0[0x33] = '\0';
    local_a0[0x34] = '\0';
    local_a0[0x35] = '\0';
    local_a0[0x36] = '\0';
    local_a0[0x37] = '\0';
    local_a0[0x38] = '\0';
    local_a0[0x39] = '\0';
    local_a0[0x3a] = '\0';
    local_a0[0x3b] = '\0';
    local_a0[0x3c] = '\0';
    local_a0[0x3d] = '\0';
    local_a0[0x3e] = '\0';
    local_a0[0x3f] = '\0';
    local_a0[0x20] = '\0';
    local_a0[0x21] = '\0';
    local_a0[0x22] = '\0';
    local_a0[0x23] = '\0';
    local_a0[0x24] = '\0';
    local_a0[0x25] = '\0';
    local_a0[0x26] = '\0';
    local_a0[0x27] = '\0';
    local_a0[0x28] = '\0';
    local_a0[0x29] = '\0';
    local_a0[0x2a] = '\0';
    local_a0[0x2b] = '\0';
    local_a0[0x2c] = '\0';
    local_a0[0x2d] = '\0';
    local_a0[0x2e] = '\0';
    local_a0[0x2f] = '\0';
    local_a0[0x10] = '\0';
    local_a0[0x11] = '\0';
    local_a0[0x12] = '\0';
    local_a0[0x13] = '\0';
    local_a0[0x14] = '\0';
    local_a0[0x15] = '\0';
    local_a0[0x16] = '\0';
    local_a0[0x17] = '\0';
    local_a0[0x18] = '\0';
    local_a0[0x19] = '\0';
    local_a0[0x1a] = '\0';
    local_a0[0x1b] = '\0';
    local_a0[0x1c] = '\0';
    local_a0[0x1d] = '\0';
    local_a0[0x1e] = '\0';
    local_a0[0x1f] = '\0';
    local_a0[8] = '\0';
    local_a0[9] = '\0';
    local_a0[10] = '\0';
    local_a0[0xb] = '\0';
    local_a0[0xc] = '\0';
    local_a0[0xd] = '\0';
    local_a0[0xe] = '\0';
    local_a0[0xf] = '\0';
    if ((param_7 & 0xf) != 0) {
      return 0xf;
    }
    if ((param_5 + param_3) - 0x3dU < 0xffffffc0) {
      return 0xf;
    }
    pcVar5 = (char *)((ulong)local_a0 | 1);
    local_a0[0] = '\x01';
    local_a0[1] = '\0';
    local_a0[2] = '\0';
    local_a0[3] = '\0';
    local_a0[4] = '\0';
    local_a0[5] = '\0';
    local_a0[6] = '\0';
    local_a0[7] = '\0';
    pcVar7 = pcVar5;
    if (0 < param_3) {
      pcVar13 = (char *)((ulong)local_a0 | 2);
      if (pcVar13 < local_a0 + (long)param_3 + 1) {
        pcVar13 = local_a0 + (long)param_3 + 1;
      }
      pcVar13 = pcVar13 + ~(ulong)local_a0;
      pcVar6 = pcVar5;
      if ((char *)0x1 < pcVar13) {
        pcVar11 = (char *)((ulong)pcVar13 & 0xfffffffffffffffe);
        uVar9 = (ulong)local_a0 | 2;
        pcVar7 = param_2 + 1;
        pcVar6 = pcVar11;
        do {
          pcVar14 = pcVar7 + -1;
          pcVar7 = pcVar7 + 2;
          pcVar6 = pcVar6 + -2;
          *(undefined2 *)(uVar9 - 1) = *(undefined2 *)pcVar14;
          uVar9 = uVar9 + 2;
        } while (pcVar6 != (char *)0x0);
        pcVar7 = pcVar5 + (long)pcVar11;
        pcVar6 = pcVar5 + (long)pcVar11;
        param_2 = param_2 + (long)pcVar11;
        if (pcVar13 == pcVar11) goto LAB_ffff0000907b65a4;
      }
      do {
        pcVar7 = pcVar6 + 1;
        *pcVar6 = *param_2;
        pcVar6 = pcVar7;
        param_2 = param_2 + 1;
      } while (pcVar7 < pcVar5 + param_3);
    }
LAB_ffff0000907b65a4:
    lVar8 = (long)((int)pcVar7 - (int)pcVar5);
    pcVar13 = pcVar5 + lVar8 + 1;
    pcVar5[lVar8] = '\0';
    pcVar7 = pcVar13;
    if (0 < param_5) {
      pcVar7 = local_a0 + lVar8 + 3;
      pcVar5 = local_a0 + lVar8 + param_5 + 2;
      if (local_a0 + lVar8 + param_5 + 2 <= pcVar7) {
        pcVar5 = pcVar7;
      }
      pcVar5 = pcVar5 + (-2 - (long)(local_a0 + lVar8));
      pcVar6 = pcVar13;
      if ((char *)0x1 < pcVar5) {
        pcVar14 = (char *)((ulong)pcVar5 & 0xfffffffffffffffe);
        pcVar6 = param_4 + 1;
        param_4 = param_4 + (long)pcVar14;
        pcVar11 = pcVar14;
        do {
          pcVar1 = pcVar6 + -1;
          pcVar6 = pcVar6 + 2;
          pcVar11 = pcVar11 + -2;
          *(undefined2 *)(pcVar7 + -1) = *(undefined2 *)pcVar1;
          pcVar7 = pcVar7 + 2;
        } while (pcVar11 != (char *)0x0);
        pcVar7 = pcVar13 + (long)pcVar14;
        pcVar6 = pcVar13 + (long)pcVar14;
        if (pcVar5 == pcVar14) goto LAB_ffff0000907b66e0;
      }
      do {
        pcVar7 = pcVar6 + 1;
        *pcVar6 = *param_4;
        pcVar6 = pcVar7;
        param_4 = param_4 + 1;
      } while (pcVar7 < pcVar13 + param_5);
    }
LAB_ffff0000907b66e0:
    iVar3 = ((int)(pcVar13 + ((int)pcVar7 - (int)pcVar13)) - (int)local_a0) + 1;
    pcVar13[(int)pcVar7 - (int)pcVar13] = (char)(param_7 << 3);
    iVar4 = FUN_ffff0000907aabe8();
    if (iVar4 == 0) {
      FUN_ffff00009077de54(s_real_key_in_keyslot_ffff0000907e06cd);
      iVar4 = FUN_ffff0000907aac00(local_a0,iVar3,0,param_6);
      if (iVar4 != 0) {
        FUN_ffff00009077de54(s_aes128_cmac_ret___x_ffff0000907df029,iVar4);
        FUN_ffff0000907aabb8();
        FUN_ffff0000907aabd0();
      }
      puVar16 = (undefined8 *)0x0;
    }
    else {
      iVar4 = 0;
      puVar16 = &local_60;
    }
    if (param_7 != 0) {
      cVar17 = '\x01';
      do {
        local_a0[0] = cVar17;
        iVar4 = FUN_ffff0000907aac00(local_a0,iVar3,puVar16,param_6);
        if (iVar4 != 0) {
          FUN_ffff0000907aabb8();
          FUN_ffff0000907aabd0();
          return iVar4;
        }
        cVar17 = cVar17 + '\x01';
        param_6 = param_6 + 0x10;
        param_7 = param_7 - 0x10;
      } while (param_7 != 0);
      return 0;
    }
    return iVar4;
  }
  if (param_1 != 1) {
    return 0xe;
  }
  local_a0[0x30] = '\0';
  local_a0[0x31] = '\0';
  local_a0[0x32] = '\0';
  local_a0[0x33] = '\0';
  local_a0[0x34] = '\0';
  local_a0[0x35] = '\0';
  local_a0[0x36] = '\0';
  local_a0[0x37] = '\0';
  local_a0[0x38] = '\0';
  local_a0[0x39] = '\0';
  local_a0[0x3a] = '\0';
  local_a0[0x3b] = '\0';
  local_a0[0x3c] = '\0';
  local_a0[0x3d] = '\0';
  local_a0[0x3e] = '\0';
  local_a0[0x3f] = '\0';
  local_a0[0x20] = '\0';
  local_a0[0x21] = '\0';
  local_a0[0x22] = '\0';
  local_a0[0x23] = '\0';
  local_a0[0x24] = '\0';
  local_a0[0x25] = '\0';
  local_a0[0x26] = '\0';
  local_a0[0x27] = '\0';
  local_a0[0x28] = '\0';
  local_a0[0x29] = '\0';
  local_a0[0x2a] = '\0';
  local_a0[0x2b] = '\0';
  local_a0[0x2c] = '\0';
  local_a0[0x2d] = '\0';
  local_a0[0x2e] = '\0';
  local_a0[0x2f] = '\0';
  local_a0[0x10] = '\0';
  local_a0[0x11] = '\0';
  local_a0[0x12] = '\0';
  local_a0[0x13] = '\0';
  local_a0[0x14] = '\0';
  local_a0[0x15] = '\0';
  local_a0[0x16] = '\0';
  local_a0[0x17] = '\0';
  local_a0[0x18] = '\0';
  local_a0[0x19] = '\0';
  local_a0[0x1a] = '\0';
  local_a0[0x1b] = '\0';
  local_a0[0x1c] = '\0';
  local_a0[0x1d] = '\0';
  local_a0[0x1e] = '\0';
  local_a0[0x1f] = '\0';
  local_a0[0] = '\0';
  local_a0[1] = '\0';
  local_a0[2] = '\0';
  local_a0[3] = '\0';
  local_a0[4] = '\0';
  local_a0[5] = '\0';
  local_a0[6] = '\0';
  local_a0[7] = '\0';
  local_a0[8] = '\0';
  local_a0[9] = '\0';
  local_a0[10] = '\0';
  local_a0[0xb] = '\0';
  local_a0[0xc] = '\0';
  local_a0[0xd] = '\0';
  local_a0[0xe] = '\0';
  local_a0[0xf] = '\0';
  if (((param_7 & 0xf) != 0) || ((param_5 + param_3) - 0x3eU < 0xffffffc0)) {
    return 0xf;
  }
  if (0 < param_3) {
    pcVar13 = local_a0 + param_3;
    pcVar7 = pcVar13;
    if (pcVar13 <= (char *)((ulong)local_a0 | 1)) {
      pcVar7 = (char *)((ulong)local_a0 | 1);
    }
    uVar9 = (long)pcVar7 - (long)local_a0;
    if (1 < uVar9) {
      uVar10 = uVar9 & 0xfffffffffffffffe;
      pcVar7 = local_a0 + uVar10;
      uVar12 = (ulong)local_a0 | 1;
      pcVar5 = param_2 + 1;
      uVar15 = uVar10;
      do {
        pcVar6 = pcVar5 + -1;
        pcVar5 = pcVar5 + 2;
        uVar15 = uVar15 - 2;
        *(undefined2 *)(uVar12 - 1) = *(undefined2 *)pcVar6;
        uVar12 = uVar12 + 2;
      } while (uVar15 != 0);
      pcVar5 = pcVar7;
      param_2 = param_2 + uVar10;
      if (uVar9 == uVar10) goto LAB_ffff0000907b64ec;
    }
    do {
      pcVar7 = pcVar5 + 1;
      *pcVar5 = *param_2;
      pcVar5 = pcVar7;
      param_2 = param_2 + 1;
    } while (pcVar7 < pcVar13);
  }
LAB_ffff0000907b64ec:
  lVar8 = (long)((int)pcVar7 - (int)local_a0);
  pcVar5 = local_a0 + lVar8 + 1;
  local_a0[lVar8] = '\0';
  pcVar7 = pcVar5;
  if (0 < param_5) {
    lVar2 = lVar8 + (long)param_5 + -0xa0;
    pcVar7 = local_a0 + lVar8 + 2;
    pcVar13 = pcVar7;
    if (pcVar7 < local_a0 + lVar2 + 0xa1) {
      pcVar13 = local_a0 + lVar2 + 0xa1;
    }
    pcVar13 = pcVar13 + (~(ulong)local_a0 - lVar8);
    pcVar6 = pcVar5;
    if ((char *)0x1 < pcVar13) {
      pcVar14 = (char *)((ulong)pcVar13 & 0xfffffffffffffffe);
      pcVar6 = param_4 + 1;
      param_4 = param_4 + (long)pcVar14;
      pcVar11 = pcVar14;
      do {
        pcVar1 = pcVar6 + -1;
        pcVar6 = pcVar6 + 2;
        pcVar11 = pcVar11 + -2;
        *(undefined2 *)(pcVar7 + -1) = *(undefined2 *)pcVar1;
        pcVar7 = pcVar7 + 2;
      } while (pcVar11 != (char *)0x0);
      pcVar7 = pcVar5 + (long)pcVar14;
      pcVar6 = pcVar5 + (long)pcVar14;
      if (pcVar13 == pcVar14) goto LAB_ffff0000907b6650;
    }
    do {
      pcVar7 = pcVar6 + 1;
      *pcVar6 = *param_4;
      pcVar6 = pcVar7;
      param_4 = param_4 + 1;
    } while (pcVar7 < pcVar5 + param_5);
  }
LAB_ffff0000907b6650:
  pcVar5[(int)pcVar7 - (int)pcVar5] = (char)(param_7 << 3);
  iVar3 = FUN_ffff0000907aa63c
                    (0xffff000018003000,4,0,0,local_a0,
                     pcVar5 + ((int)pcVar7 - (int)pcVar5) + (1 - (long)local_a0),param_6,param_7);
  return iVar3;
}


