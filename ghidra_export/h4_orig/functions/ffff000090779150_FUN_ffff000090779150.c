// ffff000090779150  size=2160  FUN_ffff000090779150

void FUN_ffff000090779150(long *param_1,long param_2,long param_3,int param_4)

{
  int iVar1;
  uint uVar2;
  undefined1 uVar3;
  ushort uVar4;
  ulong uVar5;
  undefined *puVar6;
  long *plVar7;
  int iVar8;
  uint uVar9;
  undefined4 uVar10;
  uint uVar11;
  uint uVar12;
  long lVar13;
  ushort uVar14;
  int iVar15;
  ulong uVar16;
  ulong uVar17;
  ushort *puVar18;
  byte *pbVar19;
  uint uVar20;
  int iVar21;
  
  if (*(int *)((long)param_1 + 0xc4) < 1) {
    iVar8 = 0;
    uVar16 = param_3 + 5;
LAB_ffff0000907795ac:
    uVar5 = uVar16;
  }
  else {
    if (*(int *)(*param_1 + 0x58) == 2) {
      lVar13 = 0;
      uVar16 = 0xf3ffc07f;
      do {
        if (((uVar16 & 1) != 0) && (*(short *)((long)param_1 + lVar13 + 0xd4) != 0)) {
          uVar10 = 0;
          goto LAB_ffff0000907791f4;
        }
        uVar16 = uVar16 >> 1;
        lVar13 = lVar13 + 4;
      } while (lVar13 != 0x80);
      if ((((short)param_1[0x1f] == 0) && (*(short *)((long)param_1 + 0xfc) == 0)) &&
         ((short)param_1[0x21] == 0)) {
        lVar13 = 0;
        do {
          if (*(short *)((long)param_1 + lVar13 + 0x154) != 0) goto LAB_ffff0000907791f0;
          lVar13 = lVar13 + 4;
        } while (lVar13 != 0x380);
        uVar10 = 0;
      }
      else {
LAB_ffff0000907791f0:
        uVar10 = 1;
      }
LAB_ffff0000907791f4:
      *(undefined4 *)(*param_1 + 0x58) = uVar10;
    }
    FUN_ffff0000907799c0(param_1,param_1 + 0x16b);
    FUN_ffff0000907799c0(param_1,param_1 + 0x16e);
    lVar13 = param_1[0x16c];
    uVar14 = *(ushort *)((long)param_1 + 0xd6);
    uVar9 = (int)lVar13 + 1;
    uVar16 = (ulong)uVar9;
    *(undefined2 *)((long)param_1 + (long)(int)uVar9 * 4 + 0xd6) = 0xffff;
    if (-1 < (int)lVar13) {
      iVar21 = 0;
      iVar8 = 3;
      if (uVar14 != 0) {
        iVar8 = 4;
      }
      iVar15 = 0x8a;
      if (uVar14 != 0) {
        iVar15 = 7;
      }
      uVar5 = 0xffffffff;
      puVar18 = (ushort *)((long)param_1 + 0xda);
      do {
        uVar17 = (ulong)uVar14;
        iVar1 = iVar21 + 1;
        uVar4 = *puVar18;
        uVar9 = (uint)uVar4;
        uVar20 = (uint)uVar14;
        if ((iVar15 <= iVar1) || (uVar20 != uVar9)) {
          if (iVar1 < iVar8) {
            *(short *)((long)param_1 + uVar17 * 4 + 0xabc) =
                 *(short *)((long)param_1 + uVar17 * 4 + 0xabc) + (short)iVar1;
joined_r0xffff0000907792e4:
            if (uVar4 == 0) {
LAB_ffff0000907792e8:
              iVar8 = 3;
              iVar15 = 0x8a;
              uVar5 = uVar17;
              iVar1 = 0;
              goto LAB_ffff00009077925c;
            }
          }
          else {
            if (uVar20 != 0) {
              if (uVar20 != (uint)uVar5) {
                *(short *)((long)param_1 + uVar17 * 4 + 0xabc) =
                     *(short *)((long)param_1 + uVar17 * 4 + 0xabc) + 1;
              }
              *(short *)((long)param_1 + 0xafc) = *(short *)((long)param_1 + 0xafc) + 1;
              goto joined_r0xffff0000907792e4;
            }
            if (iVar21 < 10) {
              *(short *)(param_1 + 0x160) = (short)param_1[0x160] + 1;
              if (uVar9 != 0) goto LAB_ffff0000907792a8;
              goto LAB_ffff0000907792e8;
            }
            *(short *)((long)param_1 + 0xb04) = *(short *)((long)param_1 + 0xb04) + 1;
            if (uVar9 == 0) goto LAB_ffff0000907792e8;
          }
LAB_ffff0000907792a8:
          iVar15 = 6;
          if (uVar20 != uVar4) {
            iVar15 = 7;
          }
          iVar8 = 3;
          uVar5 = uVar17;
          iVar1 = 0;
          if (uVar20 != uVar4) {
            iVar8 = 4;
          }
        }
LAB_ffff00009077925c:
        iVar21 = iVar1;
        uVar16 = uVar16 - 1;
        puVar18 = puVar18 + 2;
        uVar14 = uVar4;
      } while (uVar16 != 0);
    }
    lVar13 = param_1[0x16f];
    uVar14 = *(ushort *)((long)param_1 + 0x9ca);
    uVar9 = (int)lVar13 + 1;
    uVar16 = (ulong)uVar9;
    *(undefined2 *)((long)param_1 + (long)(int)uVar9 * 4 + 0x9ca) = 0xffff;
    if (-1 < (int)lVar13) {
      iVar21 = 0;
      iVar8 = 3;
      if (uVar14 != 0) {
        iVar8 = 4;
      }
      iVar15 = 0x8a;
      if (uVar14 != 0) {
        iVar15 = 7;
      }
      uVar5 = 0xffffffff;
      puVar18 = (ushort *)((long)param_1 + 0x9ce);
      do {
        uVar17 = (ulong)uVar14;
        iVar1 = iVar21 + 1;
        uVar4 = *puVar18;
        uVar9 = (uint)uVar4;
        uVar20 = (uint)uVar14;
        if ((iVar15 <= iVar1) || (uVar20 != uVar9)) {
          if (iVar1 < iVar8) {
            *(short *)((long)param_1 + uVar17 * 4 + 0xabc) =
                 *(short *)((long)param_1 + uVar17 * 4 + 0xabc) + (short)iVar1;
joined_r0xffff0000907793fc:
            if (uVar4 == 0) {
LAB_ffff000090779400:
              iVar8 = 3;
              iVar15 = 0x8a;
              uVar5 = uVar17;
              iVar1 = 0;
              goto LAB_ffff000090779374;
            }
          }
          else {
            if (uVar20 != 0) {
              if (uVar20 != (uint)uVar5) {
                *(short *)((long)param_1 + uVar17 * 4 + 0xabc) =
                     *(short *)((long)param_1 + uVar17 * 4 + 0xabc) + 1;
              }
              *(short *)((long)param_1 + 0xafc) = *(short *)((long)param_1 + 0xafc) + 1;
              goto joined_r0xffff0000907793fc;
            }
            if (iVar21 < 10) {
              *(short *)(param_1 + 0x160) = (short)param_1[0x160] + 1;
              if (uVar9 != 0) goto LAB_ffff0000907793c0;
              goto LAB_ffff000090779400;
            }
            *(short *)((long)param_1 + 0xb04) = *(short *)((long)param_1 + 0xb04) + 1;
            if (uVar9 == 0) goto LAB_ffff000090779400;
          }
LAB_ffff0000907793c0:
          iVar15 = 6;
          if (uVar20 != uVar4) {
            iVar15 = 7;
          }
          iVar8 = 3;
          uVar5 = uVar17;
          iVar1 = 0;
          if (uVar20 != uVar4) {
            iVar8 = 4;
          }
        }
LAB_ffff000090779374:
        iVar21 = iVar1;
        uVar16 = uVar16 - 1;
        puVar18 = puVar18 + 2;
        uVar14 = uVar4;
      } while (uVar16 != 0);
    }
    FUN_ffff0000907799c0(param_1,param_1 + 0x171);
    if (*(short *)((long)param_1 + 0xafa) == 0) {
      if (*(short *)((long)param_1 + 0xac2) == 0) {
        if (*(short *)((long)param_1 + 0xaf6) == 0) {
          if (*(short *)((long)param_1 + 0xac6) == 0) {
            if (*(short *)((long)param_1 + 0xaf2) == 0) {
              if (*(short *)((long)param_1 + 0xaca) == 0) {
                if (*(short *)((long)param_1 + 0xaee) == 0) {
                  if (*(short *)((long)param_1 + 0xace) == 0) {
                    if (*(short *)((long)param_1 + 0xaea) == 0) {
                      if (*(short *)((long)param_1 + 0xad2) == 0) {
                        if (*(short *)((long)param_1 + 0xae6) == 0) {
                          if (*(short *)((long)param_1 + 0xad6) == 0) {
                            if (*(short *)((long)param_1 + 0xae2) == 0) {
                              if (*(short *)((long)param_1 + 0xada) == 0) {
                                if (*(short *)((long)param_1 + 0xade) == 0) {
                                  iVar8 = 2;
                                  if (*(short *)((long)param_1 + 0xabe) != 0) {
                                    iVar8 = 3;
                                  }
                                }
                                else {
                                  iVar8 = 4;
                                }
                              }
                              else {
                                iVar8 = 5;
                              }
                            }
                            else {
                              iVar8 = 6;
                            }
                          }
                          else {
                            iVar8 = 7;
                          }
                        }
                        else {
                          iVar8 = 8;
                        }
                      }
                      else {
                        iVar8 = 9;
                      }
                    }
                    else {
                      iVar8 = 10;
                    }
                  }
                  else {
                    iVar8 = 0xb;
                  }
                }
                else {
                  iVar8 = 0xc;
                }
              }
              else {
                iVar8 = 0xd;
              }
            }
            else {
              iVar8 = 0xe;
            }
          }
          else {
            iVar8 = 0xf;
          }
        }
        else {
          iVar8 = 0x10;
        }
      }
      else {
        iVar8 = 0x11;
      }
    }
    else {
      iVar8 = 0x12;
    }
    lVar13 = param_1[0x2e3] + (ulong)(iVar8 * 3 + 0x11);
    uVar5 = lVar13 + 10U >> 3;
    uVar16 = param_1[0x2e4] + 10U >> 3;
    param_1[0x2e3] = lVar13;
    if ((uVar16 <= uVar5) || ((int)param_1[0x19] == 4)) goto LAB_ffff0000907795ac;
  }
  if ((param_2 == 0) || (uVar5 < param_3 + 4U)) {
    uVar9 = *(uint *)((long)param_1 + 0x1734);
    if (uVar16 == uVar5) {
      uVar20 = param_4 + 2;
      if ((int)uVar9 < 0xe) {
        uVar14 = *(ushort *)(param_1 + 0x2e6) | (ushort)(uVar20 << (ulong)(uVar9 & 0x1f));
        iVar8 = uVar9 + 3;
      }
      else {
        lVar13 = param_1[5];
        uVar9 = (uint)*(ushort *)(param_1 + 0x2e6) | uVar20 << (ulong)(uVar9 & 0x1f);
        *(short *)(param_1 + 0x2e6) = (short)uVar9;
        param_1[5] = lVar13 + 1;
        *(char *)(param_1[2] + lVar13) = (char)uVar9;
        lVar13 = param_1[5];
        param_1[5] = lVar13 + 1;
        *(undefined1 *)(param_1[2] + lVar13) = *(undefined1 *)((long)param_1 + 0x1731);
        uVar14 = (ushort)((uVar20 & 0xffff) >>
                         (ulong)(0x10U - *(int *)((long)param_1 + 0x1734) & 0x1f));
        iVar8 = *(int *)((long)param_1 + 0x1734) + -0xd;
      }
      *(ushort *)(param_1 + 0x2e6) = uVar14;
      puVar6 = &DAT_ffff0000908117b6;
      *(int *)((long)param_1 + 0x1734) = iVar8;
      plVar7 = (long *)&DAT_ffff000090811c36;
    }
    else {
      uVar20 = param_4 + 4;
      if ((int)uVar9 < 0xe) {
        uVar11 = uVar9 + 3;
        uVar9 = (uint)*(ushort *)(param_1 + 0x2e6) | uVar20 << (ulong)(uVar9 & 0x1f);
      }
      else {
        lVar13 = param_1[5];
        uVar9 = (uint)*(ushort *)(param_1 + 0x2e6) | uVar20 << (ulong)(uVar9 & 0x1f);
        *(short *)(param_1 + 0x2e6) = (short)uVar9;
        param_1[5] = lVar13 + 1;
        *(char *)(param_1[2] + lVar13) = (char)uVar9;
        lVar13 = param_1[5];
        param_1[5] = lVar13 + 1;
        *(undefined1 *)(param_1[2] + lVar13) = *(undefined1 *)((long)param_1 + 0x1731);
        uVar11 = *(int *)((long)param_1 + 0x1734) - 0xd;
        uVar9 = (uVar20 & 0xffff) >> (ulong)(0x10U - *(int *)((long)param_1 + 0x1734) & 0x1f);
      }
      uVar2 = *(uint *)(param_1 + 0x16f);
      *(uint *)((long)param_1 + 0x1734) = uVar11;
      uVar20 = (int)param_1[0x16c] + 0xff00;
      if ((int)uVar11 < 0xc) {
        uVar12 = uVar11 + 5;
        uVar9 = uVar9 | uVar20 << (ulong)(uVar11 & 0x1f);
      }
      else {
        lVar13 = param_1[5];
        uVar9 = uVar9 | uVar20 << (ulong)(uVar11 & 0x1f);
        *(short *)(param_1 + 0x2e6) = (short)uVar9;
        param_1[5] = lVar13 + 1;
        *(char *)(param_1[2] + lVar13) = (char)uVar9;
        lVar13 = param_1[5];
        param_1[5] = lVar13 + 1;
        *(undefined1 *)(param_1[2] + lVar13) = *(undefined1 *)((long)param_1 + 0x1731);
        uVar12 = *(int *)((long)param_1 + 0x1734) - 0xb;
        uVar9 = (uVar20 & 0xffff) >> (ulong)(0x10U - *(int *)((long)param_1 + 0x1734) & 0x1f);
      }
      *(uint *)((long)param_1 + 0x1734) = uVar12;
      if ((int)uVar12 < 0xc) {
        uVar9 = uVar9 | uVar2 << (ulong)(uVar12 & 0x1f);
        uVar12 = uVar12 + 5;
      }
      else {
        lVar13 = param_1[5];
        uVar9 = uVar9 | uVar2 << (ulong)(uVar12 & 0x1f);
        *(short *)(param_1 + 0x2e6) = (short)uVar9;
        param_1[5] = lVar13 + 1;
        *(char *)(param_1[2] + lVar13) = (char)uVar9;
        lVar13 = param_1[5];
        param_1[5] = lVar13 + 1;
        *(undefined1 *)(param_1[2] + lVar13) = *(undefined1 *)((long)param_1 + 0x1731);
        uVar9 = (uVar2 & 0xffff) >> (ulong)(0x10U - *(int *)((long)param_1 + 0x1734) & 0x1f);
        uVar12 = *(int *)((long)param_1 + 0x1734) - 0xb;
      }
      uVar16 = (ulong)(iVar8 + 1);
      uVar20 = iVar8 + 0xfffd;
      *(uint *)((long)param_1 + 0x1734) = uVar12;
      if ((int)uVar12 < 0xd) {
        uVar9 = uVar9 | uVar20 << (ulong)(uVar12 & 0x1f);
        uVar12 = uVar12 + 4;
      }
      else {
        lVar13 = param_1[5];
        uVar9 = uVar9 | uVar20 << (ulong)(uVar12 & 0x1f);
        *(short *)(param_1 + 0x2e6) = (short)uVar9;
        param_1[5] = lVar13 + 1;
        *(char *)(param_1[2] + lVar13) = (char)uVar9;
        lVar13 = param_1[5];
        param_1[5] = lVar13 + 1;
        *(undefined1 *)(param_1[2] + lVar13) = *(undefined1 *)((long)param_1 + 0x1731);
        uVar12 = *(int *)((long)param_1 + 0x1734) - 0xc;
        uVar9 = (uVar20 & 0xffff) >> (ulong)(0x10U - *(int *)((long)param_1 + 0x1734) & 0x1f);
      }
      *(uint *)((long)param_1 + 0x1734) = uVar12;
      pbVar19 = &DAT_ffff000090811de8;
      do {
        uVar14 = *(ushort *)((long)param_1 + (ulong)*pbVar19 * 4 + 0xabe);
        uVar9 = uVar9 | (uint)uVar14 << (ulong)(uVar12 & 0x1f);
        *(short *)(param_1 + 0x2e6) = (short)uVar9;
        if ((int)uVar12 < 0xe) {
          uVar12 = uVar12 + 3;
        }
        else {
          lVar13 = param_1[5];
          param_1[5] = lVar13 + 1;
          *(char *)(param_1[2] + lVar13) = (char)uVar9;
          lVar13 = param_1[5];
          param_1[5] = lVar13 + 1;
          *(undefined1 *)(param_1[2] + lVar13) = *(undefined1 *)((long)param_1 + 0x1731);
          uVar12 = *(int *)((long)param_1 + 0x1734) - 0xd;
          uVar14 = uVar14 >> (ulong)(0x10U - *(int *)((long)param_1 + 0x1734) & 0x1f);
          uVar9 = (uint)uVar14;
          *(ushort *)(param_1 + 0x2e6) = uVar14;
        }
        uVar16 = uVar16 - 1;
        *(uint *)((long)param_1 + 0x1734) = uVar12;
        pbVar19 = pbVar19 + 1;
      } while (uVar16 != 0);
      puVar6 = (undefined *)((long)param_1 + 0xd4);
      FUN_ffff00009077a58c(param_1,puVar6);
      plVar7 = param_1 + 0x139;
      FUN_ffff00009077a58c(param_1,plVar7,uVar2);
    }
    FUN_ffff00009077a1c8(param_1,puVar6,plVar7);
  }
  else {
    FUN_ffff000090778dd4(param_1,param_2,param_3,param_4);
  }
  FUN_ffff000090778cd0(param_1);
  if (param_4 == 0) {
    return;
  }
  if (*(int *)((long)param_1 + 0x1734) < 9) {
    if (*(int *)((long)param_1 + 0x1734) < 1) goto LAB_ffff0000907799a0;
    lVar13 = param_1[5];
    uVar3 = (undefined1)param_1[0x2e6];
  }
  else {
    lVar13 = param_1[5];
    param_1[5] = lVar13 + 1;
    *(char *)(param_1[2] + lVar13) = (char)param_1[0x2e6];
    lVar13 = param_1[5];
    uVar3 = *(undefined1 *)((long)param_1 + 0x1731);
  }
  param_1[5] = lVar13 + 1;
  *(undefined1 *)(param_1[2] + lVar13) = uVar3;
LAB_ffff0000907799a0:
  *(undefined2 *)(param_1 + 0x2e6) = 0;
  *(undefined4 *)((long)param_1 + 0x1734) = 0;
  return;
}


