
undefined8
FUN_ffff0000b807fe68
          (undefined8 param_1,ushort *param_2,uint param_3,long *param_4,uint *param_5,
          undefined *param_6)

{
  undefined1 *puVar1;
  bool bVar2;
  uint uVar3;
  undefined1 uVar4;
  uint uVar5;
  short sVar6;
  short sVar7;
  short sVar8;
  short sVar9;
  short sVar10;
  short sVar11;
  bool bVar12;
  int iVar13;
  long lVar14;
  uint uVar15;
  uint uVar16;
  long lVar17;
  uint uVar18;
  ulong uVar19;
  undefined4 *puVar20;
  uint uVar21;
  uint uVar22;
  uint uVar23;
  ushort *puVar24;
  uint uVar25;
  uint uVar26;
  ulong uVar27;
  undefined *puVar28;
  ulong uVar29;
  undefined *puVar30;
  uint uVar31;
  uint uVar32;
  uint uVar33;
  ushort uVar34;
  undefined1 uVar35;
  uint uVar36;
  ushort local_a0 [5];
  short local_96;
  short local_94;
  short local_92;
  short local_90;
  short sStack_8e;
  short sStack_8c;
  short sStack_8a;
  short local_88;
  short local_86;
  short local_84;
  short local_82;
  ushort local_80 [16];
  
  local_80[0] = 0;
  local_80[1] = 0;
  local_80[2] = 0;
  local_80[3] = 0;
  local_80[4] = 0;
  local_80[5] = 0;
  local_80[6] = 0;
  local_80[7] = 0;
  local_80[8] = 0;
  local_80[9] = 0;
  local_80[10] = 0;
  local_80[0xb] = 0;
  local_80[0xc] = 0;
  local_80[0xd] = 0;
  local_80[0xe] = 0;
  local_80[0xf] = 0;
  if (param_3 == 0) {
    uVar21 = *param_5;
LAB_ffff0000b807fed4:
    if (local_80[0xe] == 0) {
      if (local_80[0xd] == 0) {
        if (local_80[0xc] == 0) {
          if (local_80[0xb] == 0) {
            if (local_80[10] == 0) {
              if (local_80[9] == 0) {
                if (local_80[8] == 0) {
                  if (local_80[7] == 0) {
                    if (local_80[6] == 0) {
                      if (local_80[5] == 0) {
                        if (local_80[4] == 0) {
                          if (local_80[3] == 0) {
                            if (local_80[2] == 0) {
                              if (local_80[1] == 0) {
                                puVar20 = (undefined4 *)*param_4;
                                *param_4 = (long)(puVar20 + 1);
                                uVar21 = 1;
                                *puVar20 = 0x140;
                                puVar20 = (undefined4 *)*param_4;
                                *param_4 = (long)(puVar20 + 1);
                                *puVar20 = 0x140;
LAB_ffff0000b8080408:
                                *param_5 = uVar21;
                                return 0;
                              }
                              bVar12 = false;
                              uVar25 = 0;
                              uVar21 = (uint)(uVar21 != 0);
                              uVar18 = 1;
                              uVar19 = 1;
                              goto LAB_ffff0000b808000c;
                            }
                            uVar25 = 0;
                            uVar18 = 2;
                          }
                          else {
                            uVar25 = 0;
                            uVar18 = 3;
                          }
                        }
                        else {
                          uVar25 = 0;
                          uVar18 = 4;
                        }
                      }
                      else {
                        uVar25 = 0;
                        uVar18 = 5;
                      }
                    }
                    else {
                      uVar25 = 0;
                      uVar18 = 6;
                    }
                  }
                  else {
                    uVar25 = 0;
                    uVar18 = 7;
                  }
                }
                else {
                  uVar25 = 0;
                  uVar18 = 8;
                }
              }
              else {
                uVar25 = 0;
                uVar18 = 9;
              }
            }
            else {
              uVar25 = 0;
              uVar18 = 10;
            }
          }
          else {
            uVar25 = 0;
            uVar18 = 0xb;
          }
        }
        else {
          uVar25 = 0;
          uVar18 = 0xc;
        }
      }
      else {
        uVar25 = 0;
        uVar18 = 0xd;
      }
    }
    else {
      uVar25 = 0;
      uVar18 = 0xe;
    }
  }
  else {
    uVar19 = (ulong)param_3;
    puVar24 = param_2;
    do {
      uVar19 = uVar19 - 1;
      local_80[*puVar24] = local_80[*puVar24] + 1;
      puVar24 = puVar24 + 1;
    } while (uVar19 != 0);
    uVar25 = (uint)local_80[0xf];
    uVar21 = *param_5;
    if (local_80[0xf] == 0) goto LAB_ffff0000b807fed4;
    uVar18 = 0xf;
  }
  uVar19 = (ulong)uVar18;
  if (uVar18 <= uVar21) {
    uVar21 = uVar18;
  }
  uVar27 = 1;
  do {
    if (local_80[uVar27] != 0) {
      bVar12 = true;
      uVar19 = uVar27 & 0xffffffff;
      goto LAB_ffff0000b808000c;
    }
    uVar27 = uVar27 + 1;
  } while (uVar19 != uVar27);
  bVar12 = true;
LAB_ffff0000b808000c:
  if (uVar21 <= (uint)uVar19) {
    uVar21 = (uint)uVar19;
  }
  if (local_80[1] < 3) {
    uVar26 = (uint)local_80[2] + (uint)local_80[1] * 2;
    if (uVar26 < 5) {
      uVar26 = (uint)local_80[3] + uVar26 * 2;
      if (uVar26 < 9) {
        uVar26 = (uint)local_80[4] + uVar26 * 2;
        if (uVar26 < 0x11) {
          iVar13 = 0x20 - ((uint)local_80[5] + uVar26 * 2);
          if (-1 < iVar13) {
            iVar13 = iVar13 * 2 - (uint)local_80[6];
            if (-1 < iVar13) {
              iVar13 = iVar13 * 2 - (uint)local_80[7];
              if (-1 < iVar13) {
                iVar13 = iVar13 * 2 - (uint)local_80[8];
                if (-1 < iVar13) {
                  iVar13 = iVar13 * 2 - (uint)local_80[9];
                  if (-1 < iVar13) {
                    iVar13 = iVar13 * 2 - (uint)local_80[10];
                    if (-1 < iVar13) {
                      iVar13 = iVar13 * 2 - (uint)local_80[0xb];
                      if (-1 < iVar13) {
                        iVar13 = iVar13 * 2 - (uint)local_80[0xc];
                        if (-1 < iVar13) {
                          iVar13 = iVar13 * 2 - (uint)local_80[0xd];
                          if (-1 < iVar13) {
                            iVar13 = iVar13 * 2 - (uint)local_80[0xe];
                            if ((-1 < iVar13) && (uVar26 = iVar13 * 2, uVar25 <= uVar26)) {
                              iVar13 = (int)param_1;
                              if (iVar13 == 0) {
                                bVar12 = true;
                              }
                              if ((uVar26 == uVar25) || (!bVar12)) {
                                sVar11 = local_80[3] + local_80[2] + local_80[1];
                                sVar10 = local_80[4] + sVar11;
                                sVar9 = local_80[5] + sVar10;
                                sVar8 = local_80[6] + sVar9;
                                sVar7 = local_80[7] + sVar8;
                                sVar6 = local_80[8] + sVar7;
                                stack0xffffffffffffff68 =
                                     CONCAT26(sVar8,CONCAT24(sVar9,CONCAT22(sVar10,sVar11)));
                                sVar11 = local_80[9] + sVar6;
                                sVar8 = local_80[10] + sVar11;
                                sVar10 = local_80[0xb] + sVar8;
                                sVar9 = local_80[0xc] + sVar10;
                                _local_90 = CONCAT26(sVar8,CONCAT24(sVar11,CONCAT22(sVar6,sVar7)));
                                sVar8 = local_80[0xd] + sVar9;
                                _local_88 = CONCAT26(local_80[0xe] + sVar8,
                                                     CONCAT24(sVar8,CONCAT22(sVar9,sVar10)));
                                if (param_3 != 0) {
                                  uVar27 = 0;
                                  do {
                                    uVar29 = (ulong)param_2[uVar27];
                                    if (uVar29 != 0) {
                                      uVar34 = local_a0[uVar29];
                                      *(short *)(param_6 + (ulong)uVar34 * 2) = (short)uVar27;
                                      local_a0[uVar29] = uVar34 + 1;
                                    }
                                    uVar27 = uVar27 + 1;
                                  } while (param_3 != uVar27);
                                }
                                if (iVar13 == 0) {
                                  bVar12 = false;
                                  uVar25 = 0;
                                  uVar26 = 0x14;
                                  puVar28 = param_6;
                                  puVar30 = param_6;
                                }
                                else if (iVar13 == 1) {
                                  if (9 < uVar21) {
                                    return param_1;
                                  }
                                  bVar12 = false;
                                  uVar26 = 0x101;
                                  puVar28 = &DAT_ffff0000b80ab548;
                                  puVar30 = &DAT_ffff0000b80ab50a;
                                  uVar25 = 1;
                                }
                                else {
                                  bVar12 = iVar13 == 2;
                                  uVar26 = 0;
                                  puVar28 = (undefined *)0xffff0000b80ab5c6;
                                  puVar30 = (undefined *)0xffff0000b80ab586;
                                  uVar25 = uVar26;
                                  if ((bVar12) && (uVar25 = 0, 9 < uVar21)) {
                                    return 1;
                                  }
                                }
                                uVar15 = 0;
                                uVar31 = 1 << (ulong)(uVar21 & 0x1f);
                                uVar32 = 0;
                                uVar5 = uVar31 - 1;
                                lVar17 = *param_4;
                                uVar16 = 0;
                                uVar33 = 0xffffffff;
                                uVar22 = uVar21;
                                do {
                                  uVar3 = 1 << (ulong)(uVar22 & 0x1f);
                                  do {
                                    uVar34 = *(ushort *)(param_6 + (ulong)uVar15 * 2);
                                    uVar22 = (uint)uVar19;
                                    if (uVar34 + 1 < uVar26) {
                                      uVar35 = 0;
                                    }
                                    else if (uVar26 < uVar34 || uVar26 == uVar34) {
                                      lVar14 = (ulong)(uVar34 - uVar26) * 2;
                                      uVar35 = puVar28[lVar14];
                                      uVar34 = *(ushort *)(puVar30 + lVar14);
                                    }
                                    else {
                                      uVar34 = 0;
                                      uVar35 = 0x60;
                                    }
                                    iVar13 = -1 << (ulong)(uVar22 - uVar16 & 0x1f);
                                    uVar23 = uVar3;
                                    do {
                                      uVar36 = (uVar32 >> (ulong)(uVar16 & 0x1f)) + iVar13 + uVar23;
                                      uVar23 = uVar23 + iVar13;
                                      puVar1 = (undefined1 *)(lVar17 + (ulong)uVar36 * 4);
                                      *puVar1 = uVar35;
                                      uVar4 = (undefined1)(uVar22 - uVar16);
                                      puVar1[1] = uVar4;
                                      *(ushort *)(puVar1 + 2) = uVar34;
                                    } while (uVar23 != 0);
                                    uVar23 = 1 << (ulong)(uVar22 - 1 & 0x1f);
                                    do {
                                      uVar36 = uVar23;
                                      uVar23 = uVar36 >> 1;
                                    } while ((uVar36 & uVar32) != 0);
                                    uVar23 = uVar36 - 1 & uVar32;
                                    uVar15 = uVar15 + 1;
                                    uVar34 = local_80[uVar19];
                                    uVar32 = 0;
                                    if (uVar36 != 0) {
                                      uVar32 = uVar23 + uVar36;
                                    }
                                    local_80[uVar19] = uVar34 - 1;
                                    if ((ushort)(uVar34 - 1) == 0) {
                                      if (uVar22 == uVar18) {
                                        if (uVar32 != 0) {
                                          puVar1 = (undefined1 *)(lVar17 + (ulong)uVar32 * 4);
                                          *puVar1 = 0x40;
                                          puVar1[1] = uVar4;
                                          *(undefined2 *)(puVar1 + 2) = 0;
                                        }
                                        *param_4 = *param_4 + (ulong)uVar31 * 4;
                                        goto LAB_ffff0000b8080408;
                                      }
                                      uVar19 = (ulong)param_2[*(ushort *)
                                                               (param_6 + (ulong)uVar15 * 2)];
                                    }
                                    uVar23 = (uint)uVar19;
                                  } while ((uVar23 <= uVar21) ||
                                          (uVar36 = uVar32 & uVar5, uVar36 == uVar33));
                                  uVar33 = uVar21;
                                  if (uVar16 != 0) {
                                    uVar33 = uVar16;
                                  }
                                  uVar22 = uVar23 - uVar33;
                                  iVar13 = 1 << (ulong)(uVar22 & 0x1f);
                                  if (uVar23 < uVar18) {
                                    uVar22 = uVar18 - uVar33;
                                    uVar27 = uVar19;
                                    do {
                                      if ((int)(iVar13 - (uint)local_80[uVar27]) < 1) {
                                        uVar22 = (int)uVar27 - uVar33;
                                        break;
                                      }
                                      iVar13 = (iVar13 - (uint)local_80[uVar27]) * 2;
                                      uVar16 = (int)uVar27 + 1;
                                      uVar27 = (ulong)uVar16;
                                    } while (uVar16 < uVar18);
                                    iVar13 = 1 << (ulong)(uVar22 & 0x1f);
                                  }
                                  uVar31 = iVar13 + uVar31;
                                  uVar16 = 0;
                                  if (0x354 < uVar31) {
                                    uVar16 = uVar25;
                                  }
                                  bVar2 = false;
                                  if (0x250 < uVar31) {
                                    bVar2 = bVar12;
                                  }
                                  if (uVar16 != 0) {
                                    return 1;
                                  }
                                  if (bVar2) {
                                    return 1;
                                  }
                                  lVar14 = *param_4;
                                  lVar17 = lVar17 + (ulong)uVar3 * 4;
                                  puVar1 = (undefined1 *)(lVar14 + (ulong)uVar36 * 4);
                                  *puVar1 = (char)uVar22;
                                  puVar1[1] = (char)uVar21;
                                  *(short *)(puVar1 + 2) =
                                       (short)((uint)((int)lVar17 - (int)lVar14) >> 2);
                                  uVar16 = uVar33;
                                  uVar33 = uVar36;
                                } while( true );
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  return 0xffffffff;
}


