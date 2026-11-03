// ffff00009079ebdc  size=1528  FUN_ffff00009079ebdc

int FUN_ffff00009079ebdc(char *param_1,char *param_2,long param_3)

{
  int iVar1;
  char cVar2;
  char cVar3;
  uint uVar4;
  long lVar5;
  int iVar6;
  uint uVar7;
  ulong uVar8;
  ulong uVar9;
  long lVar10;
  uint uVar11;
  ulong uVar12;
  undefined2 *puVar13;
  int iVar14;
  ushort *puVar15;
  uint uVar16;
  ulong uVar17;
  ulong uVar18;
  ulong uVar19;
  ushort *puVar20;
  ushort *puVar21;
  ushort *puVar22;
  undefined8 local_58;
  uint local_50;
  undefined4 uStack_4c;
  undefined2 *local_48;
  
  local_50 = 0;
  uStack_4c = 0;
  local_48 = (undefined2 *)0x0;
  local_58 = 0;
  if (((param_1 == (char *)0x0) || (param_2 == (char *)0x0)) || (param_3 == 0)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_mt6991_sboot_c_ffff0000907e064d,0xd80,
               s_x______void__0_____m______void___ffff0000907dd58e);
  }
  uVar11 = *(uint *)(param_2 + 8);
  if ((char *)&local_58 == param_1) {
    uVar9 = 0;
  }
  else {
    local_48 = (undefined2 *)FUN_ffff00009077d5a8(*(undefined4 *)(param_1 + 8),2);
    if (local_48 == (undefined2 *)0x0) {
      return -2;
    }
    FUN_ffff00009077eb20
              (local_48,*(undefined8 *)(param_1 + 0x10),(ulong)*(uint *)(param_1 + 8) << 1);
    local_50 = *(uint *)(param_1 + 8);
    uVar9 = (ulong)local_50;
    local_58 = CONCAT44(local_50,(undefined4)local_58);
    local_58 = CONCAT71(local_58._1_7_,*param_1);
  }
  if (uVar11 != 1) {
    uVar16 = uVar11 - 1;
    uVar7 = (uint)uVar9;
    if (uVar16 < uVar7) {
      lVar10 = uVar9 - uVar16;
      puVar13 = local_48;
      do {
        lVar10 = lVar10 + -1;
        *puVar13 = puVar13[uVar16];
        puVar13 = puVar13 + 1;
      } while (lVar10 != 0);
      if (uVar16 <= uVar7) {
        uVar12 = (ulong)((uVar7 + 1) - uVar11);
        uVar8 = uVar9;
        if (uVar9 < uVar12 + 1) {
          uVar8 = uVar12 + 1;
        }
        uVar8 = uVar8 - uVar12;
        if (1 < uVar8) {
          uVar17 = uVar8 & 0xfffffffffffffffe;
          puVar13 = local_48 + uVar12 + 1;
          uVar18 = uVar17;
          do {
            *(undefined4 *)(puVar13 + -1) = 0;
            uVar18 = uVar18 - 2;
            puVar13 = puVar13 + 2;
          } while (uVar18 != 0);
          uVar12 = uVar17 + uVar12;
          if (uVar8 == uVar17) goto joined_r0xffff00009079ed34;
        }
        do {
          uVar8 = uVar12 + 1;
          local_48[uVar12] = 0;
          uVar12 = uVar8;
        } while (uVar8 < uVar9);
      }
joined_r0xffff00009079ed34:
      while (1 < uVar7) {
        if (local_48[uVar9 - 1] != 0) goto LAB_ffff00009079ed54;
        uVar9 = uVar9 - 1;
        uVar7 = (uint)uVar9;
      }
      uVar9 = 1;
LAB_ffff00009079ed54:
      local_50 = (uint)uVar9;
    }
    else {
      FUN_ffff00009077ee50(local_48,0,uVar9 << 1);
      local_58 = local_58 & 0xffffffffffffff00;
      local_50 = 1;
    }
  }
  iVar6 = FUN_ffff00009079cae4(&local_58,param_3);
  if (iVar6 != 0) goto LAB_ffff00009079efc4;
  uVar16 = uVar11 + 1;
  uVar9 = (ulong)uVar16;
  if (uVar11 != 0xffffffff) {
    uVar8 = (ulong)local_50;
    if (uVar16 < local_50) {
      lVar10 = uVar8 - uVar9;
      puVar13 = local_48;
      do {
        lVar10 = lVar10 + -1;
        *puVar13 = puVar13[uVar9];
        puVar13 = puVar13 + 1;
      } while (lVar10 != 0);
      if (uVar16 <= local_50) {
        uVar18 = (ulong)(local_50 + ~uVar11);
        uVar12 = uVar8;
        if (uVar8 < uVar18 + 1) {
          uVar12 = uVar18 + 1;
        }
        uVar12 = uVar12 - uVar18;
        if (1 < uVar12) {
          uVar19 = uVar12 & 0xfffffffffffffffe;
          puVar13 = local_48 + uVar18 + 1;
          uVar17 = uVar19;
          do {
            *(undefined4 *)(puVar13 + -1) = 0;
            uVar17 = uVar17 - 2;
            puVar13 = puVar13 + 2;
          } while (uVar17 != 0);
          uVar18 = uVar19 + uVar18;
          if (uVar12 == uVar19) goto joined_r0xffff00009079ee30;
        }
        do {
          uVar12 = uVar18 + 1;
          local_48[uVar18] = 0;
          uVar18 = uVar12;
        } while (uVar12 < uVar8);
      }
joined_r0xffff00009079ee30:
      while (1 < local_50) {
        if (local_48[uVar8 - 1] != 0) goto LAB_ffff00009079ee50;
        uVar8 = uVar8 - 1;
        local_50 = (uint)uVar8;
      }
      uVar8 = 1;
LAB_ffff00009079ee50:
      local_50 = (uint)uVar8;
    }
    else {
      FUN_ffff00009077ee50(local_48,0,(local_58 >> 0x20) << 1);
      local_58 = local_58 & 0xffffffffffffff00;
      local_50 = 1;
    }
  }
  uVar7 = *(uint *)(param_1 + 8);
  uVar8 = (ulong)uVar7;
  uVar16 = uVar16 & 0xfff;
  if (uVar16 < uVar7) {
    lVar10 = *(long *)(param_1 + 0x10);
    uVar12 = (ulong)(uVar16 + 1);
    *(undefined2 *)(lVar10 + (ulong)uVar16 * 2) = 0;
    if (uVar16 + 1 < uVar7) {
      uVar4 = (uVar7 - 2) - uVar16;
      if (uVar4 != 0) {
        uVar18 = (ulong)uVar4 + 1;
        uVar19 = uVar18 & 0x1fffffffe;
        uVar12 = uVar19 + uVar12;
        uVar17 = uVar19;
        lVar5 = lVar10 + ((ulong)(uVar11 + 1) & 0xfff) * 2;
        do {
          *(undefined4 *)(lVar5 + 2) = 0;
          uVar17 = uVar17 - 2;
          lVar5 = lVar5 + 4;
        } while (uVar17 != 0);
        if (uVar18 == uVar19) goto LAB_ffff00009079eed0;
      }
      iVar6 = uVar7 - (int)uVar12;
      puVar13 = (undefined2 *)(lVar10 + uVar12 * 2);
      do {
        iVar6 = iVar6 + -1;
        *puVar13 = 0;
        puVar13 = puVar13 + 1;
      } while (iVar6 != 0);
    }
LAB_ffff00009079eed0:
    if (1 < uVar7) {
      do {
        if (*(short *)(lVar10 + -2 + uVar8 * 2) != 0) goto LAB_ffff00009079eef4;
        uVar8 = uVar8 - 1;
      } while (1 < (uint)uVar8);
    }
    uVar8 = 1;
LAB_ffff00009079eef4:
    *(int *)(param_1 + 8) = (int)uVar8;
  }
  iVar6 = FUN_ffff00009079cae4(&local_58,param_2);
  if (iVar6 != 0) goto LAB_ffff00009079efc4;
  uVar8 = (ulong)local_50;
  if (uVar16 < local_50) {
    uVar12 = (ulong)(uVar16 + 1);
    local_48[uVar16] = 0;
    if (uVar16 + 1 < local_50) {
      uVar16 = (local_50 - 2) - uVar16;
      if (uVar16 != 0) {
        uVar18 = (ulong)uVar16 + 1;
        uVar19 = uVar18 & 0x1fffffffe;
        uVar12 = uVar19 + uVar12;
        uVar17 = uVar19;
        puVar13 = local_48 + ((ulong)(uVar11 + 1) & 0xfff);
        do {
          *(undefined4 *)(puVar13 + 1) = 0;
          uVar17 = uVar17 - 2;
          puVar13 = puVar13 + 2;
        } while (uVar17 != 0);
        if (uVar18 == uVar19) goto joined_r0xffff00009079ef88;
      }
      iVar6 = local_50 - (int)uVar12;
      puVar13 = local_48 + uVar12;
      do {
        iVar6 = iVar6 + -1;
        *puVar13 = 0;
        puVar13 = puVar13 + 1;
      } while (iVar6 != 0);
    }
joined_r0xffff00009079ef88:
    while (1 < local_50) {
      if (local_48[uVar8 - 1] != 0) goto LAB_ffff00009079efa8;
      uVar8 = uVar8 - 1;
      local_50 = (uint)uVar8;
    }
    uVar8 = 1;
LAB_ffff00009079efa8:
    local_50 = (uint)uVar8;
  }
  iVar6 = FUN_ffff00009079d5c4(param_1,&local_58,param_1);
  if (iVar6 == 0) {
    cVar2 = *param_1;
    if (cVar2 == '\x01') {
      FUN_ffff00009077ee50(local_48,0,(local_58 >> 0x20) << 1);
      local_58 = local_58 & 0xffffffffffffff00;
      local_50 = 1;
      *local_48 = 1;
      iVar6 = FUN_ffff00009079eaf0(&local_58,uVar9);
      if ((iVar6 != 0) || (iVar6 = FUN_ffff00009079ce30(param_1,&local_58,param_1), iVar6 != 0))
      goto LAB_ffff00009079efc4;
      cVar2 = *param_1;
    }
    cVar3 = *param_2;
    iVar6 = -3;
    do {
      if (cVar2 == cVar3) {
        uVar16 = *(uint *)(param_1 + 8);
        uVar11 = *(uint *)(param_2 + 8);
        if (uVar11 <= uVar16 && uVar16 != uVar11) {
LAB_ffff00009079f098:
          iVar14 = 1;
        }
        else {
          if (uVar11 <= uVar16) {
            if (-1 < (int)(uVar16 - 1)) {
              lVar10 = (ulong)(uVar16 - 1) * 2;
              puVar15 = (ushort *)(*(long *)(param_2 + 0x10) + lVar10);
              puVar22 = (ushort *)(*(long *)(param_1 + 0x10) + lVar10);
              do {
                if (*puVar15 < *puVar22) goto LAB_ffff00009079f098;
                if (*puVar22 < *puVar15) goto LAB_ffff00009079f0b0;
                puVar22 = puVar22 + -1;
                puVar15 = puVar15 + -1;
                uVar16 = uVar16 - 1;
              } while (0 < (int)uVar16);
            }
            goto LAB_ffff00009079f0c0;
          }
LAB_ffff00009079f0b0:
          iVar14 = -1;
        }
        iVar1 = -iVar14;
        if (cVar2 == '\0') {
          iVar1 = iVar14;
        }
        if (iVar1 < 0) {
LAB_ffff00009079f1b4:
          iVar6 = 0;
          break;
        }
      }
      else {
        if (cVar2 != '\0') goto LAB_ffff00009079f1b4;
        uVar11 = *(uint *)(param_2 + 8);
      }
LAB_ffff00009079f0c0:
      puVar15 = *(ushort **)(param_1 + 0x10);
      if (uVar11 == 0) {
        uVar16 = 0;
        puVar22 = puVar15;
      }
      else {
        iVar14 = 0;
        puVar20 = *(ushort **)(param_2 + 0x10);
        puVar21 = puVar15;
        uVar16 = uVar11;
        do {
          uVar7 = (iVar14 - (uint)*puVar20) + (uint)*puVar21 + 0x10000;
          iVar14 = -(uint)(uVar7 < 0x10000);
          uVar16 = uVar16 - 1;
          puVar22 = puVar21 + 1;
          *puVar21 = (ushort)uVar7;
          puVar20 = puVar20 + 1;
          puVar21 = puVar22;
        } while (uVar16 != 0);
        uVar16 = (uint)(uVar7 < 0x10000);
      }
      uVar7 = *(uint *)(param_1 + 8);
      uVar9 = (ulong)uVar7;
      if (uVar11 < uVar7) {
        iVar14 = uVar7 - uVar11;
        do {
          uVar11 = (*puVar22 | 0x10000) - uVar16;
          uVar16 = (uint)(uVar11 < 0x10000);
          iVar14 = iVar14 + -1;
          *puVar22 = (ushort)uVar11;
          puVar22 = puVar22 + 1;
        } while (iVar14 != 0);
      }
      if (1 < uVar7) {
        do {
          if (puVar15[uVar9 - 1] != 0) goto LAB_ffff00009079f078;
          uVar9 = uVar9 - 1;
        } while (1 < (uint)uVar9);
        uVar9 = 1;
      }
LAB_ffff00009079f078:
      *(int *)(param_1 + 8) = (int)uVar9;
    } while (uVar16 == 0);
  }
LAB_ffff00009079efc4:
  if ((local_48 != (undefined2 *)0x0) &&
     (FUN_ffff00009077ee50(local_48,0,(local_58 >> 0x20) << 1), local_48 != (undefined2 *)0x0)) {
    thunk_FUN_ffff0000907c314c();
  }
  return iVar6;
}


