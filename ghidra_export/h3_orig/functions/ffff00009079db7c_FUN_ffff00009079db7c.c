// ffff00009079db7c  size=2040  FUN_ffff00009079db7c

int FUN_ffff00009079db7c(undefined1 *param_1,ulong *param_2)

{
  uint uVar1;
  ushort uVar2;
  ushort uVar3;
  uint uVar4;
  bool bVar5;
  bool bVar6;
  short sVar7;
  int iVar8;
  undefined2 *puVar9;
  ushort *puVar10;
  undefined2 *puVar11;
  int iVar12;
  ulong uVar13;
  long lVar14;
  uint uVar15;
  short *psVar16;
  ulong uVar17;
  ulong uVar18;
  undefined2 *puVar19;
  uint uVar20;
  uint uVar21;
  ushort *puVar22;
  undefined2 *puVar23;
  ulong uVar24;
  ushort *puVar25;
  uint uVar26;
  uint uVar27;
  uint uVar28;
  ulong uVar29;
  uint uVar30;
  ulong uVar31;
  uint local_b0;
  uint uStack_ac;
  uint local_a8;
  undefined4 uStack_a4;
  ushort *local_a0;
  undefined8 local_98;
  ulong local_90;
  undefined2 *local_88;
  undefined2 local_80;
  undefined1 local_7e;
  ulong local_78;
  ulong uStack_70;
  undefined2 *local_68;
  
  local_7e = 0;
  local_80 = 0;
  local_90 = 0;
  local_88 = (undefined2 *)0x0;
  local_98 = 0;
  local_b0 = 0;
  uStack_ac = 0;
  local_a8 = 0;
  uStack_a4 = 0;
  if ((param_1 == (undefined1 *)0x0) || (param_2 == (ulong *)0x0)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_mt6991_sboot_c_ffff0000907e05aa,0xe8e,
               s_a______void__0_____b______void___ffff0000907eefa3);
  }
  iVar8 = (int)param_2[1];
  if (((char)*param_2 != '\x01') && (iVar8 == 1)) {
    if (*(short *)param_2[2] == 0) {
      return -3;
    }
    iVar8 = 1;
  }
  uVar13 = (ulong)(iVar8 - 1U);
  iVar12 = 0;
  uVar2 = *(ushort *)(param_2[2] + uVar13 * 2);
  uVar27 = (uint)uVar2;
  if ((uVar2 != 0) && ((uVar2 & 1) == 0)) {
    iVar12 = 0;
    uVar18 = (ulong)uVar2;
    do {
      uVar27 = (uint)(uVar18 >> 1);
      iVar12 = iVar12 + 1;
      uVar26 = (uint)uVar18;
      if (uVar26 < 2) break;
      uVar18 = uVar18 >> 1;
    } while ((uVar26 >> 1 & 1) == 0);
  }
  if (uVar27 == 1) {
    if (-1 < (int)(iVar8 - 2U)) {
      psVar16 = (short *)(param_2[2] + (ulong)(iVar8 - 2U) * 2);
      do {
        if (*psVar16 != 0) goto LAB_ffff00009079dd3c;
        psVar16 = psVar16 + -1;
        uVar27 = (int)uVar13 - 1;
        uVar13 = (ulong)uVar27;
      } while (0 < (int)uVar27);
    }
    uVar27 = iVar12 + (iVar8 - 1U) * 0x10;
    if (-1 < (int)uVar27) {
      iVar8 = FUN_ffff00009079bf14(param_1,param_2);
      if (iVar8 != 0) {
        return iVar8;
      }
      FUN_ffff00009079c5f8(param_1,uVar27);
      uVar26 = (uint)param_2[1];
      uVar13 = (ulong)uVar26;
      uVar28 = uVar27 >> 4 & 0xfff;
      if (uVar26 <= uVar28) {
        return 0;
      }
      uVar17 = param_2[2];
      uVar18 = (ulong)(uVar28 + 1);
      *(ushort *)(uVar17 + (ulong)uVar28 * 2) =
           *(ushort *)(uVar17 + (ulong)uVar28 * 2) &
           ((ushort)(-1 << (ulong)(uVar27 & 0xf)) ^ 0xffff);
      if (uVar28 + 1 < uVar26) {
        uVar28 = (uVar26 - 2) - uVar28;
        if (uVar28 != 0) {
          uVar31 = (ulong)uVar28 + 1;
          uVar24 = uVar31 & 0x1fffffffe;
          uVar18 = uVar24 + uVar18;
          uVar29 = uVar24;
          lVar14 = ((ulong)(uVar27 >> 3) & 0x1ffe) + uVar17;
          do {
            *(undefined4 *)(lVar14 + 2) = 0;
            uVar29 = uVar29 - 2;
            lVar14 = lVar14 + 4;
          } while (uVar29 != 0);
          if (uVar31 == uVar24) goto LAB_ffff00009079dd0c;
        }
        iVar8 = uVar26 - (int)uVar18;
        puVar9 = (undefined2 *)(uVar17 + uVar18 * 2);
        do {
          iVar8 = iVar8 + -1;
          *puVar9 = 0;
          puVar9 = puVar9 + 1;
        } while (iVar8 != 0);
      }
LAB_ffff00009079dd0c:
      if (1 < uVar26) {
        do {
          if (*(short *)((uVar17 - 2) + uVar13 * 2) != 0) goto LAB_ffff00009079dd30;
          uVar13 = uVar13 - 1;
        } while (1 < (uint)uVar13);
      }
      uVar13 = 1;
LAB_ffff00009079dd30:
      *(int *)(param_2 + 1) = (int)uVar13;
      return 0;
    }
  }
LAB_ffff00009079dd3c:
  uVar27 = *(uint *)(param_1 + 8);
  uVar13 = (ulong)uVar27;
  if (uVar27 == 0) {
LAB_ffff00009079e344:
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_mt6991_sboot_c_ffff0000907e05aa,0x137,
               s_mp______void__0_____prec_>_0_ffff0000907ea213);
  }
  puVar9 = (undefined2 *)FUN_ffff00009077d2e0(uVar13,2);
  if (puVar9 == (undefined2 *)0x0) {
    return -2;
  }
  uVar26 = *(uint *)(param_1 + 8);
  if (uVar26 == 0) goto LAB_ffff00009079e344;
  puVar10 = (ushort *)FUN_ffff00009077d2e0(uVar26,2);
  local_a0 = puVar10;
  if (puVar10 == (ushort *)0x0) {
    iVar8 = -2;
    goto LAB_ffff00009079e2f8;
  }
  iVar8 = *(int *)(param_1 + 8);
  local_b0 = local_b0 & 0xffffff00;
  local_a8 = 1;
  uStack_ac = uVar26;
  if (iVar8 == 0) goto LAB_ffff00009079e344;
  local_88 = (undefined2 *)FUN_ffff00009077d2e0(iVar8,2);
  if (local_88 == (undefined2 *)0x0) {
    iVar8 = -2;
LAB_ffff00009079e2c8:
    FUN_ffff00009077eb88(puVar10,0,(ulong)uStack_ac << 1);
    if (local_a0 != (ushort *)0x0) {
      thunk_FUN_ffff0000907c2ad8();
    }
    local_a0 = (ushort *)0x0;
  }
  else {
    uVar18 = 1;
    local_98 = CONCAT44(iVar8,(int)local_98) & 0xffffffffffffff00;
    local_90 = CONCAT44(local_90._4_4_,1);
    sVar7 = FUN_ffff00009079f388(param_1,param_2);
    uVar26 = *(int *)(param_1 + 8) - 1;
    if (-1 < (int)uVar26) {
      uVar18 = 1;
LAB_ffff00009079ddcc:
      uVar17 = local_90 & 0xffffffff;
      uVar28 = (uint)param_2[1];
      puVar19 = puVar9;
      if (uVar28 < (uint)local_90) {
LAB_ffff00009079dddc:
        uVar30 = (uint)uVar17;
      }
      else {
        do {
          uVar27 = (uint)uVar17;
          puVar9 = puVar19;
          if (uVar28 <= uVar27) {
            uVar30 = uVar27 - 1;
            if ((int)uVar30 < 0) goto LAB_ffff00009079dddc;
            lVar14 = 0;
            while( true ) {
              uVar2 = *(ushort *)(param_2[2] + (ulong)uVar30 * 2 + lVar14 * 2);
              if (uVar2 < (ushort)local_88[(ulong)uVar30 + lVar14]) goto LAB_ffff00009079dddc;
              if ((ushort)local_88[(ulong)uVar30 + lVar14] < uVar2) break;
              lVar14 = lVar14 + -1;
              if ((int)(uVar27 + (int)lVar14) < 1) goto LAB_ffff00009079dddc;
            }
          }
          if ((int)uVar26 < 0) goto LAB_ffff00009079dddc;
          uVar28 = uVar27 + 1;
          uVar31 = (ulong)uVar28;
          if (uVar27 != 0xffffffff) {
            uVar29 = (ulong)local_98._4_4_;
            if (uVar28 <= local_98._4_4_) {
LAB_ffff00009079e088:
              uVar29 = (ulong)(uVar27 - 1);
              local_90 = CONCAT44(local_90._4_4_,uVar28);
              if (-1 < (int)(uVar27 - 1)) {
                do {
                  puVar23 = local_88 + uVar29;
                  iVar8 = (int)uVar29;
                  uVar29 = uVar29 - 1;
                  uVar27 = (int)uVar17 - 1;
                  uVar17 = (ulong)uVar27;
                  local_88[iVar8 + 1] = *puVar23;
                } while (0 < (int)uVar27);
              }
              goto LAB_ffff00009079e0c0;
            }
            uVar30 = 0;
            if (DAT_ffff000090837c58 != 0) {
              uVar30 = (DAT_ffff000090837c58 + uVar27) / DAT_ffff000090837c58;
            }
            iVar8 = uVar30 * DAT_ffff000090837c58;
            puVar11 = (undefined2 *)FUN_ffff00009077d2e0(iVar8,2);
            puVar23 = local_88;
            if (puVar11 != (undefined2 *)0x0) {
              FUN_ffff00009077e858(puVar11,local_88,uVar17 << 1);
              FUN_ffff00009077eb88(puVar23,0,uVar29 << 1);
              if (puVar23 != (undefined2 *)0x0) {
                thunk_FUN_ffff0000907c2ad8(puVar23);
              }
              local_98 = CONCAT44(iVar8,(undefined4)local_98);
              local_88 = puVar11;
              goto LAB_ffff00009079e088;
            }
LAB_ffff00009079e33c:
            iVar8 = -2;
            puVar9 = puVar19;
            goto LAB_ffff00009079e2b4;
          }
          uVar31 = 0xffffffff;
LAB_ffff00009079e0c0:
          puVar23 = local_88;
          iVar8 = (int)uVar18;
          *local_88 = 0;
          uVar29 = uVar13;
          if (iVar8 == -1) {
            uVar18 = 0xffffffff;
          }
          else {
            if ((uint)uVar13 < iVar8 + 1U) {
              uVar27 = 0;
              if (DAT_ffff000090837c58 != 0) {
                uVar27 = (DAT_ffff000090837c58 + iVar8) / DAT_ffff000090837c58;
              }
              uVar29 = (ulong)(uVar27 * DAT_ffff000090837c58);
              puVar9 = (undefined2 *)FUN_ffff00009077d2e0(uVar29,2);
              if (puVar9 == (undefined2 *)0x0) goto LAB_ffff00009079e33c;
              FUN_ffff00009077e858(puVar9,puVar19,uVar18 << 1);
              FUN_ffff00009077eb88(puVar19,0,uVar13 << 1);
              thunk_FUN_ffff0000907c2ad8(puVar19);
            }
            uVar13 = (ulong)(iVar8 - 1U);
            if (-1 < (int)(iVar8 - 1U)) {
              do {
                puVar19 = puVar9 + uVar13;
                iVar12 = (int)uVar13;
                uVar13 = uVar13 - 1;
                uVar27 = (int)uVar18 - 1;
                uVar18 = (ulong)uVar27;
                puVar9[iVar12 + 1] = *puVar19;
              } while (0 < (int)uVar27);
            }
            uVar18 = (ulong)(iVar8 + 1U);
          }
          *puVar9 = 0;
          *puVar23 = *(undefined2 *)(*(long *)(param_1 + 0x10) + (ulong)uVar26 * 2);
          if (1 < (uint)uVar31) {
            do {
              if (puVar23[uVar31 - 1] != 0) goto LAB_ffff00009079df8c;
              uVar31 = uVar31 - 1;
            } while (1 < (uint)uVar31);
            uVar31 = 1;
          }
LAB_ffff00009079df8c:
          uVar28 = (uint)param_2[1];
          uVar26 = uVar26 - 1;
          uVar30 = (uint)uVar31;
          uVar17 = uVar31 & 0xffffffff;
          local_90 = CONCAT44(local_90._4_4_,uVar30);
          puVar19 = puVar9;
          uVar13 = uVar29;
        } while (uVar30 <= uVar28);
      }
      uVar27 = (uint)uVar13;
      iVar8 = FUN_ffff00009079d1b8(&local_98,param_2);
      puVar19 = local_88;
      if (-1 < iVar8) {
        uVar2 = local_88[uVar30 - 1];
        uVar15 = (uint)uVar2;
        uVar3 = *(ushort *)(param_2[2] + (ulong)(uVar28 - 1) * 2);
        if (uVar2 <= uVar3 && 1 < uVar30) {
          uVar15 = CONCAT22(uVar2,local_88[uVar30 - 2]);
        }
        iVar8 = FUN_ffff00009079bf14(param_2,&local_b0);
        if (iVar8 == 0) {
          uVar28 = 0;
          if (uVar3 != 0) {
            uVar28 = uVar15 / uVar3;
          }
          if (0xfffe < uVar28) {
            uVar28 = 0xffff;
          }
          iVar8 = FUN_ffff00009079c504(&local_b0,uVar28);
          if (iVar8 == 0) {
            uVar17 = (ulong)local_a8;
            do {
              uVar15 = (uint)uVar17;
              if (uVar15 <= uVar30) {
                if ((uVar15 < uVar30) || (uVar31 = (ulong)(uVar15 - 1), (int)(uVar15 - 1) < 0))
                goto LAB_ffff00009079df68;
                while( true ) {
                  if ((ushort)puVar19[uVar31] < local_a0[uVar31]) break;
                  bVar6 = false;
                  bVar5 = true;
                  if ((ushort)puVar19[uVar31] <= local_a0[uVar31]) {
                    bVar6 = (int)uVar31 < 0;
                    bVar5 = (int)uVar31 == 0;
                  }
                  uVar31 = uVar31 - 1;
                  if (bVar5 || bVar6) goto LAB_ffff00009079df68;
                }
              }
              uVar4 = (uint)param_2[1];
              if (uVar4 == 0) {
                uVar20 = 0;
                uVar21 = 0;
                puVar10 = local_a0;
                if (uVar15 != 0) goto LAB_ffff00009079df18;
              }
              else {
                iVar8 = 0;
                puVar22 = (ushort *)param_2[2];
                puVar25 = local_a0;
                uVar21 = uVar4;
                do {
                  uVar1 = (iVar8 - (uint)*puVar22) + (uint)*puVar25 + 0x10000;
                  iVar8 = -(uint)(uVar1 < 0x10000);
                  uVar21 = uVar21 - 1;
                  puVar10 = puVar25 + 1;
                  *puVar25 = (ushort)uVar1;
                  puVar22 = puVar22 + 1;
                  puVar25 = puVar10;
                } while (uVar21 != 0);
                uVar20 = (uint)(uVar1 < 0x10000);
                uVar21 = (uint)(uVar1 < 0x10000);
                if (uVar4 < uVar15) {
LAB_ffff00009079df18:
                  iVar8 = uVar15 - uVar4;
                  uVar21 = uVar20;
                  do {
                    uVar4 = (*puVar10 | 0x10000) - uVar21;
                    uVar21 = (uint)(uVar4 < 0x10000);
                    iVar8 = iVar8 + -1;
                    *puVar10 = (ushort)uVar4;
                    puVar10 = puVar10 + 1;
                  } while (iVar8 != 0);
                }
              }
              uVar28 = uVar28 - 1;
              if (1 < uVar15) {
                uVar17 = uVar17 & 0xffffffff;
                do {
                  if (local_a0[uVar17 - 1] != 0) goto LAB_ffff00009079de6c;
                  uVar17 = uVar17 - 1;
                } while (1 < (uint)uVar17);
                uVar17 = 1;
              }
LAB_ffff00009079de6c:
              local_a8 = (uint)uVar17;
              if (uVar21 != 0) {
                iVar8 = -3;
                break;
              }
            } while( true );
          }
        }
        goto LAB_ffff00009079e2b4;
      }
    }
LAB_ffff00009079e1c8:
    if (sVar7 != 0) {
      FUN_ffff00009079c5f8(&local_98);
    }
    if (1 < (uint)uVar18) {
      do {
        if (puVar9[uVar18 - 1] != 0) goto LAB_ffff00009079e228;
        uVar18 = uVar18 - 1;
      } while (1 < (uint)uVar18);
      uVar18 = 1;
    }
LAB_ffff00009079e228:
    uVar13 = local_90 & 0xffffffff;
    if (1 < (uint)local_90) {
      do {
        if (local_88[uVar13 - 1] != 0) goto LAB_ffff00009079e254;
        uVar13 = uVar13 - 1;
      } while (1 < (uint)uVar13);
      uVar13 = 1;
    }
LAB_ffff00009079e254:
    *param_1 = 0;
    *(undefined4 *)(param_1 + 0xc) = 0;
    local_90 = CONCAT44(local_90._4_4_,(int)uVar13);
    *(undefined2 *)(param_1 + 1) = local_80;
    uVar26 = *(uint *)(param_1 + 4);
    param_1[3] = local_7e;
    puVar19 = *(undefined2 **)(param_1 + 0x10);
    *(uint *)(param_1 + 4) = uVar27;
    *(int *)(param_1 + 8) = (int)uVar18;
    iVar8 = 0;
    *(undefined2 **)(param_1 + 0x10) = puVar9;
    uVar13 = (ulong)uVar26;
    puVar23 = (undefined2 *)param_2[2];
    local_68 = local_88;
    uVar18 = *param_2;
    uVar17 = param_2[1];
    param_2[2] = (ulong)local_88;
    local_78 = local_98;
    uStack_70 = local_90;
    *param_2 = local_98;
    param_2[1] = local_90;
    puVar9 = puVar19;
    local_98 = uVar18;
    local_90 = uVar17;
    local_88 = puVar23;
LAB_ffff00009079e2b4:
    FUN_ffff00009079bebc(&local_98);
    puVar10 = local_a0;
    if (local_a0 != (ushort *)0x0) goto LAB_ffff00009079e2c8;
  }
  uStack_ac = 0;
  local_a8 = 0;
  if (puVar9 == (undefined2 *)0x0) {
    return iVar8;
  }
LAB_ffff00009079e2f8:
  FUN_ffff00009077eb88(puVar9,0,uVar13 << 1);
  thunk_FUN_ffff0000907c2ad8(puVar9);
  return iVar8;
LAB_ffff00009079df68:
  iVar8 = FUN_ffff00009079d238(&local_98,&local_b0);
  if (iVar8 != 0) goto LAB_ffff00009079e2b4;
  *puVar9 = (short)uVar28;
  if ((int)uVar26 < 0) goto LAB_ffff00009079e1c8;
  goto LAB_ffff00009079ddcc;
}


