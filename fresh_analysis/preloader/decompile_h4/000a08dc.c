// File offset entry: 0xa08dc

void FUN_020a07dc(uint *param_1,int *param_2)

{
  byte *pbVar1;
  uint uVar2;
  byte bVar3;
  byte bVar4;
  byte bVar5;
  uint uVar6;
  undefined1 uVar7;
  bool bVar8;
  undefined1 uVar9;
  bool bVar10;
  uint uVar11;
  undefined8 uVar12;
  uint uVar13;
  long lVar14;
  undefined8 *extraout_x8;
  undefined8 *extraout_x8_00;
  undefined8 *extraout_x8_01;
  undefined8 *extraout_x8_02;
  undefined8 *extraout_x8_03;
  undefined8 *extraout_x8_04;
  undefined8 *extraout_x8_05;
  undefined8 *extraout_x8_06;
  uint *extraout_x8_07;
  int *extraout_x8_08;
  undefined8 *extraout_x8_09;
  undefined8 *extraout_x8_10;
  undefined8 *extraout_x8_11;
  undefined8 *extraout_x8_12;
  undefined8 *extraout_x8_13;
  uint *extraout_x8_14;
  uint *puVar15;
  int *extraout_x8_15;
  int *piVar16;
  undefined8 *extraout_x8_16;
  undefined8 *extraout_x8_17;
  undefined8 *extraout_x8_18;
  undefined8 *puVar17;
  undefined8 *extraout_x8_19;
  undefined8 *extraout_x8_20;
  undefined4 extraout_w9;
  undefined4 extraout_w9_00;
  undefined4 extraout_w9_01;
  undefined4 extraout_w9_02;
  undefined4 extraout_w9_03;
  undefined4 extraout_w9_04;
  undefined4 extraout_w9_05;
  undefined4 extraout_w9_06;
  undefined4 extraout_w9_07;
  undefined4 extraout_w9_08;
  undefined4 extraout_w9_09;
  int iVar18;
  int iVar19;
  ulong uVar20;
  byte *pbVar21;
  byte *pbVar22;
  int iVar23;
  undefined1 auVar24 [16];
  
  FUN_02091664(param_1,0,0x48);
  if (param_2 == (int *)0x0) {
    uVar11 = 1;
  }
  else {
    bVar3 = *(byte *)(param_2 + 2);
    bVar4 = *(byte *)((long)param_2 + 5);
    iVar23 = *param_2;
    bVar5 = *(byte *)(param_2 + 1);
    iVar18 = -1000000;
    do {
      iVar19 = iVar18;
      if ((uRam0000000018010020 & 1) != 0) break;
      iVar18 = iVar19 + 1;
    } while (iVar19 != 0);
    uVar2 = *param_1;
    uVar11 = uVar2 & 0xfffff000;
    *param_1 = uVar11 | (uint)(iVar19 == 0) << 2;
    if (iVar19 == 0) {
      uVar12 = 0x20b512f;
code_r0x0208ada4:
      lVar14 = *plRam0000000002103280;
      FUN_0208ae24(uVar12,&stack0xffffffffffffffa8);
      if (lRam0000000002100880 == lVar14) {
        return;
      }
      auVar24 = FUN_02091090();
      lVar14 = auVar24._8_8_;
      pbVar22 = auVar24._0_8_;
      do {
        while( true ) {
          pbVar21 = pbVar22;
          bVar3 = *pbVar21;
          uVar11 = (uint)bVar3;
          if (bVar3 == 0) {
            return;
          }
          pbVar1 = pbVar21 + 1;
          pbVar22 = pbVar1;
          if (bVar3 != 10) break;
LAB_0208af10:
          FUN_0208ac88(0xd);
          uVar11 = 10;
LAB_0208af1c:
          FUN_0208ac88(uVar11);
        }
        if (bVar3 == 0xd) {
          pbVar22 = pbVar21 + 2;
          if (pbVar21[1] != 10) {
            pbVar22 = pbVar1;
          }
          goto LAB_0208af10;
        }
        if (bVar3 != 0x25) goto LAB_0208af1c;
        bVar3 = pbVar21[1];
        pbVar22 = pbVar21 + 2;
        uVar11 = bVar3 - 0x70;
        uVar7 = 7 < uVar11;
        uVar9 = uVar11 == 8;
        switch(uVar11) {
        case 0:
          func_0x0208ab60(0x20bef68);
        case 8:
          if (*(int *)(lVar14 + 0x18) < 0) {
            FUN_0208b33c();
            *(undefined4 *)(lVar14 + 0x18) = extraout_w9_00;
            if ((bool)uVar7 && !(bool)uVar9) goto code_r0x0208af60;
            FUN_0208b328();
            puVar17 = extraout_x8_00;
          }
          else {
code_r0x0208af60:
            FUN_0208b310();
            puVar17 = extraout_x8_01;
          }
          FUN_0208b180(*puVar17,0);
          break;
        case 1:
        case 2:
        case 4:
        case 6:
        case 7:
code_r0x0208af78:
          uVar11 = 0x20;
          goto LAB_0208af1c;
        case 3:
          if (*(int *)(lVar14 + 0x18) < 0) {
            FUN_0208b33c();
            *(undefined4 *)(lVar14 + 0x18) = extraout_w9_01;
            if ((bool)uVar7 && !(bool)uVar9) goto code_r0x0208b054;
            FUN_0208b328();
            puVar17 = extraout_x8_02;
          }
          else {
code_r0x0208b054:
            FUN_0208b310();
            puVar17 = extraout_x8_09;
          }
          func_0x0208ab60(*puVar17);
          break;
        case 5:
          if (*(int *)(lVar14 + 0x18) < 0) {
            FUN_0208b33c();
            *(undefined4 *)(lVar14 + 0x18) = extraout_w9_02;
            if ((bool)uVar7 && !(bool)uVar9) goto code_r0x0208b064;
            FUN_0208b328();
            puVar17 = extraout_x8_03;
          }
          else {
code_r0x0208b064:
            FUN_0208b310();
            puVar17 = extraout_x8_10;
          }
          FUN_0208abb0(*puVar17);
          break;
        default:
          if (bVar3 == 0x25) {
            uVar11 = 0x25;
            goto LAB_0208af1c;
          }
          bVar8 = 0x41 < bVar3;
          bVar10 = bVar3 == 0x42;
          if (bVar10) {
            if (*(int *)(lVar14 + 0x18) < 0) {
              FUN_0208b33c();
              *(undefined4 *)(lVar14 + 0x18) = extraout_w9_03;
              if (bVar8 && !bVar10) goto LAB_0208b074;
              FUN_0208b328();
              puVar17 = extraout_x8_04;
            }
            else {
LAB_0208b074:
              FUN_0208b310();
              puVar17 = extraout_x8_11;
            }
            FUN_0208b180(*puVar17,2);
          }
          else {
            bVar8 = 0x47 < bVar3;
            bVar10 = bVar3 == 0x48;
            if (bVar10) {
              if (*(int *)(lVar14 + 0x18) < 0) {
                FUN_0208b33c();
                *(undefined4 *)(lVar14 + 0x18) = extraout_w9_04;
                if (bVar8 && !bVar10) goto LAB_0208b088;
                FUN_0208b328();
                puVar17 = extraout_x8_05;
              }
              else {
LAB_0208b088:
                FUN_0208b310();
                puVar17 = extraout_x8_12;
              }
              FUN_0208b180(*puVar17,4);
            }
            else {
              bVar8 = 0x57 < bVar3;
              bVar10 = bVar3 == 0x58;
              if (bVar10) {
                if (*(int *)(lVar14 + 0x18) < 0) {
                  FUN_0208b33c();
                  *(undefined4 *)(lVar14 + 0x18) = extraout_w9_05;
                  if (bVar8 && !bVar10) goto LAB_0208b09c;
                  FUN_0208b328();
                  puVar17 = extraout_x8_06;
                }
                else {
LAB_0208b09c:
                  FUN_0208b310();
                  puVar17 = extraout_x8_13;
                }
                FUN_0208b180(*puVar17,8);
              }
              else {
                bVar8 = 0x62 < bVar3;
                bVar10 = bVar3 == 99;
                if (bVar10) {
                  if (*(int *)(lVar14 + 0x18) < 0) {
                    FUN_0208b33c();
                    *(undefined4 *)(lVar14 + 0x18) = extraout_w9_06;
                    if (bVar8 && !bVar10) goto LAB_0208b0b0;
                    FUN_0208b328();
                    puVar15 = extraout_x8_07;
                  }
                  else {
LAB_0208b0b0:
                    FUN_0208b310();
                    puVar15 = extraout_x8_14;
                  }
                  uVar11 = *puVar15;
                  goto LAB_0208af1c;
                }
                bVar8 = 99 < bVar3;
                bVar10 = bVar3 == 100;
                if (bVar10) {
                  if (*(int *)(lVar14 + 0x18) < 0) {
                    FUN_0208b33c();
                    *(undefined4 *)(lVar14 + 0x18) = extraout_w9_07;
                    if (bVar8 && !bVar10) goto LAB_0208b0bc;
                    FUN_0208b328();
                    piVar16 = extraout_x8_08;
                  }
                  else {
LAB_0208b0bc:
                    FUN_0208b310();
                    piVar16 = extraout_x8_15;
                  }
                  iVar23 = *piVar16;
                  if (iVar23 < 0) {
                    FUN_0208ac88(0x2d);
                    iVar23 = -iVar23;
                  }
                  FUN_0208abb0(iVar23);
                }
                else {
                  if (bVar3 != 0x6c) goto code_r0x0208af78;
                  bVar3 = *pbVar22;
                  if (bVar3 == 0x6c) {
                    bVar8 = 0x77 < pbVar21[3];
                    bVar10 = pbVar21[3] == 0x78;
                    if (bVar10) {
                      if (*(int *)(lVar14 + 0x18) < 0) {
                        FUN_0208b33c();
                        *(undefined4 *)(lVar14 + 0x18) = extraout_w9_08;
                        if (bVar8 && !bVar10) goto LAB_0208b150;
                        FUN_0208b328();
                        puVar17 = extraout_x8_16;
                      }
                      else {
LAB_0208b150:
                        FUN_0208b310();
                        puVar17 = extraout_x8_20;
                      }
                      FUN_0208b180(*puVar17,0x10);
                      pbVar22 = pbVar21 + 4;
                    }
                  }
                  else {
                    bVar8 = 0x74 < bVar3;
                    bVar10 = bVar3 == 0x75;
                    if (bVar10) {
                      if (*(int *)(lVar14 + 0x18) < 0) {
                        FUN_0208b33c();
                        *(undefined4 *)(lVar14 + 0x18) = extraout_w9_09;
                        if (bVar8 && !bVar10) goto LAB_0208b13c;
                        FUN_0208b328();
                        puVar17 = extraout_x8_17;
                      }
                      else {
LAB_0208b13c:
                        FUN_0208b310();
                        puVar17 = extraout_x8_19;
                      }
                      FUN_0208b1d8(*puVar17);
                      pbVar22 = pbVar21 + 3;
                    }
                    else {
                      bVar8 = 0x77 < bVar3;
                      bVar10 = bVar3 == 0x78;
                      if (bVar10) {
                        if (*(int *)(lVar14 + 0x18) < 0) {
                          FUN_0208b33c();
                          *(undefined4 *)(lVar14 + 0x18) = extraout_w9;
                          if (bVar8 && !bVar10) goto LAB_0208b124;
                          FUN_0208b328();
                          puVar17 = extraout_x8;
                        }
                        else {
LAB_0208b124:
                          FUN_0208b310();
                          puVar17 = extraout_x8_18;
                        }
                        FUN_0208b180(*puVar17,0);
                        pbVar22 = pbVar21 + 3;
                      }
                    }
                  }
                }
              }
            }
          }
        }
      } while( true );
    }
    uVar6 = (uint)bVar5 | (bVar3 & 0xf) << 8 | (uint)bVar4 << 0xc | iVar23 << 0x14;
    uRam0000000018010010 = uVar6;
    if ((*(byte *)(param_2 + 2) & 0xf) != 0) {
      uVar20 = 0;
      do {
        uRam0000000018010010 = param_2[uVar20 + 3];
        uVar20 = uVar20 + 1;
      } while (uVar20 < ((ulong)*(byte *)(param_2 + 2) & 0xf));
    }
    uRam0000000018010030 = 1;
    iVar23 = -1000000;
    do {
      iVar18 = iVar23;
      if ((uRam0000000018010080 & 1) != 0) break;
      iVar23 = iVar18 + 1;
    } while (iVar18 != 0);
    if (iVar18 == 0) {
      FUN_0208ada0(0x20cdb9e,0x20b5155,0x20b7fcb);
      uVar11 = 4;
    }
    else {
      uVar13 = ~uRam0000000018010070;
      if (uVar6 == uVar13) {
        uVar20 = (ulong)*(byte *)(param_2 + 2) & 0xf;
        if ((int)uVar20 != 0) {
          puVar15 = (uint *)(param_2 + 3);
          do {
            if (*puVar15 != ~uRam0000000018010070) {
              uVar12 = 0x20b104b;
              uVar13 = uVar6;
              goto LAB_020a0a18;
            }
            uVar20 = uVar20 - 1;
            puVar15 = puVar15 + 1;
          } while (uVar20 != 0);
        }
        uRam0000000018010090 = 1;
        *param_1 = uVar11;
        iVar23 = -1000000;
        do {
          iVar18 = iVar23;
          if ((uRam0000000018010050 & 1) != 0) break;
          iVar23 = iVar18 + 1;
        } while (iVar18 != 0);
        if (iVar18 != 0) {
          uVar6 = uRam0000000018010040 >> 8 & 0xf;
          uVar20 = (ulong)uVar6;
          uVar11 = uRam0000000018010040 >> 0xc;
          uVar13 = uRam0000000018010040 >> 8;
          *(char *)(param_1 + 1) = (char)uRam0000000018010040;
          *param_1 = uVar2 & 0xfff00000 | uVar11;
          *(byte *)(param_1 + 2) = (byte)param_1[2] & 0xf0 | (byte)uVar13 & 0xf;
          if (uVar6 != 0) {
            puVar15 = param_1 + 3;
            do {
              uVar20 = uVar20 - 1;
              *puVar15 = uRam0000000018010040;
              puVar15 = puVar15 + 1;
            } while (uVar20 != 0);
          }
          uRam0000000018010060 = 1;
          return;
        }
        uVar12 = 0x20c4650;
        goto code_r0x0208ada4;
      }
      uVar12 = 0x20bd296;
LAB_020a0a18:
      FUN_0208ada0(uVar12,0x20b5155,0x20b7fcb,uVar6,uVar13);
      uVar11 = 3;
    }
  }
  *param_1 = *param_1 & 0xfffff000 | uVar11;
  while ((uRam0000000018010050 & 1) != 0) {
    uRam0000000018010060 = 1;
  }
  while ((uRam0000000018010080 & 1) != 0) {
    uRam0000000018010090 = 1;
  }
  return;
}


