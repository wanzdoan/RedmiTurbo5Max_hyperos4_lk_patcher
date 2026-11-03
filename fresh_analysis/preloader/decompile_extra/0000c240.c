// File offset entry: 0xc240

void FUN_0200c140(ulong param_1)

{
  byte *pbVar1;
  byte bVar2;
  undefined1 uVar3;
  bool bVar4;
  undefined1 uVar5;
  bool bVar6;
  int iVar7;
  int iVar8;
  undefined4 uVar9;
  uint uVar10;
  undefined8 uVar11;
  undefined8 uVar12;
  undefined8 uVar13;
  long lVar14;
  undefined8 in_x6;
  undefined8 in_x7;
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
  long lVar18;
  undefined8 unaff_x19;
  ulong unaff_x20;
  long unaff_x21;
  byte *pbVar19;
  byte *pbVar20;
  ulong unaff_x22;
  undefined8 unaff_x23;
  undefined1 auVar21 [16];
  undefined8 uStack_c8;
  undefined8 uStack_c0;
  undefined1 *puStack_b8;
  undefined1 **ppuStack_b0;
  long lStack_a8;
  undefined1 *puStack_a0;
  undefined1 *puStack_98;
  undefined1 **ppuStack_90;
  undefined8 uStack_88;
  ulong uStack_80;
  long lStack_78;
  undefined1 *puStack_70;
  undefined8 uStack_68;
  undefined1 auStack_60 [4];
  int iStack_5c;
  long lStack_58;
  
  lStack_58 = lRam0000000002100880;
  if (iRam00000000020e5674 == 1) {
    unaff_x20 = param_1 & 0xffffffff;
    unaff_x19 = FUN_020036c8(1);
    unaff_x23 = 0x210f000;
    lVar14 = (long)iRam000000000210fa14;
    if (cRam000000000210f210 == '\x01') {
      iRam000000000210fa14 = iRam000000000210fa14 + 1;
      *(char *)(lVar14 + 0x210f211) = (char)param_1;
      if (iRam000000000210fa14 == 0x800) {
        unaff_x20 = 0x210fa18;
        iRam000000000210fa14 = 0;
        cRam000000000210f210 = '\0';
        iStack_5c = func_0x0206a0d0(0x210f211,(long)(int)uRam0000000002110378 + 0x210fa24);
        lVar14 = lRam0000000002110380;
        if (iStack_5c == 0) {
          unaff_x23 = 0x2110000;
          unaff_x22 = (ulong)uRam0000000002110378;
          FUN_02091294(3);
          FUN_02003ad0(unaff_x19,lVar14,unaff_x22,0x210fa18);
          lVar18 = (long)(int)uRam0000000002110378;
          uRam0000000002110378 = 0;
          lRam0000000002110380 = lRam0000000002110380 + lVar18;
          FUN_02091664(0x210fa18,0,0x960);
          iStack_5c = func_0x0206a0d0(0x210f211,(long)(int)uRam0000000002110378 + 0x210fa24);
          unaff_x21 = lVar14;
        }
        if (-1 < iStack_5c) {
          FUN_020915ac((long)(int)uRam0000000002110378 + 0x210fa18,0x20cbfe2,8);
          uRam0000000002110378 = uRam0000000002110378 + 8;
          FUN_020915ac((long)(int)uRam0000000002110378 + 0x210fa18,&iStack_5c);
          lVar14 = lRam0000000002110380;
          uVar10 = uRam0000000002110378 + iStack_5c + 4;
          unaff_x20 = (ulong)uVar10;
          uRam0000000002110378 = uVar10;
          if (0x560 < (int)uVar10) {
            unaff_x23 = 0x2110000;
            FUN_02091294(3);
            unaff_x22 = 0x210fa18;
            FUN_02003ad0(unaff_x19,lVar14,uVar10);
            lVar18 = (long)(int)uRam0000000002110378;
            uRam0000000002110378 = 0;
            lRam0000000002110380 = lRam0000000002110380 + lVar18;
            FUN_02091664(0x210fa18,0);
            unaff_x21 = lVar14;
          }
          cRam000000000210f210 = '\x01';
        }
      }
    }
    else if (iRam000000000210fa14 != 0) {
      unaff_x20 = 0x210fa18;
      iStack_5c = func_0x0206a0d0(0x210f211,(long)(int)uRam0000000002110378 + 0x210fa24);
      lVar14 = lRam0000000002110380;
      if (iStack_5c == 0) {
        unaff_x22 = (ulong)uRam0000000002110378;
        FUN_02091294(3);
        FUN_02003ad0(unaff_x19,lVar14,unaff_x22,0x210fa18);
        lRam0000000002110380 = lRam0000000002110380 + (int)uRam0000000002110378;
        FUN_02091664(0x210fa18,0,0x960);
        uRam0000000002110378 = 0;
        iStack_5c = func_0x0206a0d0(0x210f211,0x210fa24);
        unaff_x21 = lVar14;
      }
      if (-1 < iStack_5c) {
        FUN_020915ac((long)(int)uRam0000000002110378 + 0x210fa18,0x20cbfe2,8);
        uRam0000000002110378 = uRam0000000002110378 + 8;
        FUN_020915ac((long)(int)uRam0000000002110378 + 0x210fa18,&iStack_5c,4);
        lVar14 = (long)iStack_5c + (long)(int)(uRam0000000002110378 + 4);
        uRam0000000002110378 = (uint)lVar14;
        FUN_020915ac(lVar14 + 0x210fa18,0x20cde7d);
        lVar14 = lRam0000000002110380;
        uVar10 = uRam0000000002110378 + 8;
        unaff_x20 = (ulong)uVar10;
        uRam0000000002110378 = uVar10;
        if (0 < (int)uVar10) {
          FUN_02091294(3);
          unaff_x22 = 0x210fa18;
          FUN_02003ad0(unaff_x19,lVar14,uVar10);
          lRam0000000002110380 = lRam0000000002110380 + (int)uRam0000000002110378;
          FUN_02091664(0x210fa18,0);
          uRam0000000002110378 = 0;
          unaff_x21 = lVar14;
        }
        iRam000000000210fa14 = 0;
      }
    }
  }
  if (lRam0000000002100880 == lStack_58) {
    return;
  }
  iVar7 = FUN_02091090();
  puStack_98 = (undefined1 *)0x200c4cc;
  lStack_a8 = *plRam0000000002103280;
  iRam000000000210fa14 = 0;
  puStack_a0 = &stack0xffffffffffffffb0;
  ppuStack_90 = (undefined1 **)unaff_x23;
  uStack_80 = unaff_x22;
  lStack_78 = unaff_x21;
  puStack_70 = (undefined1 *)unaff_x20;
  uStack_68 = unaff_x19;
  iVar8 = func_0x0205c5e4(0x20b42d8,0);
  uVar5 = iVar8 == 1;
  if ((bool)uVar5) {
    lVar14 = func_0x0205d014(0x20b0314);
    if (lVar14 == 0) {
      func_0x0200c740();
      if ((bool)uVar5) {
        uVar13 = 0x20c6c94;
        puStack_70 = puStack_a0;
code_r0x0208ada4:
        ppuStack_b0 = &puStack_b8;
        lStack_78 = *plRam0000000002103280;
        uStack_88 = 0;
        uStack_80 = 0xffffffc8;
        lStack_a8 = 0;
        puStack_a0 = (undefined1 *)0xffffffc8;
        uStack_c8 = in_x6;
        uStack_c0 = in_x7;
        puStack_b8 = auStack_60;
        uStack_68 = puStack_98;
        puStack_98 = auStack_60;
        ppuStack_90 = ppuStack_b0;
        FUN_0208ae24(uVar13,&puStack_b8);
        if (lRam0000000002100880 == lStack_78) {
          return;
        }
        auVar21 = FUN_02091090();
        lVar14 = auVar21._8_8_;
        pbVar20 = auVar21._0_8_;
        do {
          while( true ) {
            pbVar19 = pbVar20;
            bVar2 = *pbVar19;
            uVar10 = (uint)bVar2;
            if (bVar2 == 0) {
              return;
            }
            pbVar1 = pbVar19 + 1;
            pbVar20 = pbVar1;
            if (bVar2 != 10) break;
LAB_0208af10:
            FUN_0208ac88(0xd);
            uVar10 = 10;
LAB_0208af1c:
            FUN_0208ac88(uVar10);
          }
          if (bVar2 == 0xd) {
            pbVar20 = pbVar19 + 2;
            if (pbVar19[1] != 10) {
              pbVar20 = pbVar1;
            }
            goto LAB_0208af10;
          }
          if (bVar2 != 0x25) goto LAB_0208af1c;
          bVar2 = pbVar19[1];
          pbVar20 = pbVar19 + 2;
          uVar10 = bVar2 - 0x70;
          uVar3 = 7 < uVar10;
          uVar5 = uVar10 == 8;
          switch(uVar10) {
          case 0:
            func_0x0208ab60(0x20bef68);
          case 8:
            if (*(int *)(lVar14 + 0x18) < 0) {
              FUN_0208b33c();
              *(undefined4 *)(lVar14 + 0x18) = extraout_w9_00;
              if ((bool)uVar3 && !(bool)uVar5) goto code_r0x0208af60;
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
            uVar10 = 0x20;
            goto LAB_0208af1c;
          case 3:
            if (*(int *)(lVar14 + 0x18) < 0) {
              FUN_0208b33c();
              *(undefined4 *)(lVar14 + 0x18) = extraout_w9_01;
              if ((bool)uVar3 && !(bool)uVar5) goto code_r0x0208b054;
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
              if ((bool)uVar3 && !(bool)uVar5) goto code_r0x0208b064;
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
            if (bVar2 == 0x25) {
              uVar10 = 0x25;
              goto LAB_0208af1c;
            }
            bVar4 = 0x41 < bVar2;
            bVar6 = bVar2 == 0x42;
            if (bVar6) {
              if (*(int *)(lVar14 + 0x18) < 0) {
                FUN_0208b33c();
                *(undefined4 *)(lVar14 + 0x18) = extraout_w9_03;
                if (bVar4 && !bVar6) goto LAB_0208b074;
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
              bVar4 = 0x47 < bVar2;
              bVar6 = bVar2 == 0x48;
              if (bVar6) {
                if (*(int *)(lVar14 + 0x18) < 0) {
                  FUN_0208b33c();
                  *(undefined4 *)(lVar14 + 0x18) = extraout_w9_04;
                  if (bVar4 && !bVar6) goto LAB_0208b088;
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
                bVar4 = 0x57 < bVar2;
                bVar6 = bVar2 == 0x58;
                if (bVar6) {
                  if (*(int *)(lVar14 + 0x18) < 0) {
                    FUN_0208b33c();
                    *(undefined4 *)(lVar14 + 0x18) = extraout_w9_05;
                    if (bVar4 && !bVar6) goto LAB_0208b09c;
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
                  bVar4 = 0x62 < bVar2;
                  bVar6 = bVar2 == 99;
                  if (bVar6) {
                    if (*(int *)(lVar14 + 0x18) < 0) {
                      FUN_0208b33c();
                      *(undefined4 *)(lVar14 + 0x18) = extraout_w9_06;
                      if (bVar4 && !bVar6) goto LAB_0208b0b0;
                      FUN_0208b328();
                      puVar15 = extraout_x8_07;
                    }
                    else {
LAB_0208b0b0:
                      FUN_0208b310();
                      puVar15 = extraout_x8_14;
                    }
                    uVar10 = *puVar15;
                    goto LAB_0208af1c;
                  }
                  bVar4 = 99 < bVar2;
                  bVar6 = bVar2 == 100;
                  if (bVar6) {
                    if (*(int *)(lVar14 + 0x18) < 0) {
                      FUN_0208b33c();
                      *(undefined4 *)(lVar14 + 0x18) = extraout_w9_07;
                      if (bVar4 && !bVar6) goto LAB_0208b0bc;
                      FUN_0208b328();
                      piVar16 = extraout_x8_08;
                    }
                    else {
LAB_0208b0bc:
                      FUN_0208b310();
                      piVar16 = extraout_x8_15;
                    }
                    iVar7 = *piVar16;
                    if (iVar7 < 0) {
                      FUN_0208ac88(0x2d);
                      iVar7 = -iVar7;
                    }
                    FUN_0208abb0(iVar7);
                  }
                  else {
                    if (bVar2 != 0x6c) goto code_r0x0208af78;
                    bVar2 = *pbVar20;
                    if (bVar2 == 0x6c) {
                      bVar4 = 0x77 < pbVar19[3];
                      bVar6 = pbVar19[3] == 0x78;
                      if (bVar6) {
                        if (*(int *)(lVar14 + 0x18) < 0) {
                          FUN_0208b33c();
                          *(undefined4 *)(lVar14 + 0x18) = extraout_w9_08;
                          if (bVar4 && !bVar6) goto LAB_0208b150;
                          FUN_0208b328();
                          puVar17 = extraout_x8_16;
                        }
                        else {
LAB_0208b150:
                          FUN_0208b310();
                          puVar17 = extraout_x8_20;
                        }
                        FUN_0208b180(*puVar17,0x10);
                        pbVar20 = pbVar19 + 4;
                      }
                    }
                    else {
                      bVar4 = 0x74 < bVar2;
                      bVar6 = bVar2 == 0x75;
                      if (bVar6) {
                        if (*(int *)(lVar14 + 0x18) < 0) {
                          FUN_0208b33c();
                          *(undefined4 *)(lVar14 + 0x18) = extraout_w9_09;
                          if (bVar4 && !bVar6) goto LAB_0208b13c;
                          FUN_0208b328();
                          puVar17 = extraout_x8_17;
                        }
                        else {
LAB_0208b13c:
                          FUN_0208b310();
                          puVar17 = extraout_x8_19;
                        }
                        FUN_0208b1d8(*puVar17);
                        pbVar20 = pbVar19 + 3;
                      }
                      else {
                        bVar4 = 0x77 < bVar2;
                        bVar6 = bVar2 == 0x78;
                        if (bVar6) {
                          if (*(int *)(lVar14 + 0x18) < 0) {
                            FUN_0208b33c();
                            *(undefined4 *)(lVar14 + 0x18) = extraout_w9;
                            if (bVar4 && !bVar6) goto LAB_0208b124;
                            FUN_0208b328();
                            puVar17 = extraout_x8;
                          }
                          else {
LAB_0208b124:
                            FUN_0208b310();
                            puVar17 = extraout_x8_18;
                          }
                          FUN_0208b180(*puVar17,0);
                          pbVar20 = pbVar19 + 3;
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
      goto LAB_0200c6cc;
    }
    if (iVar7 == 1) {
      lRam0000000002110380 = 0x100000;
    }
    else {
      if (iVar7 != 2) {
        if (*plRam0000000002103280 == lStack_a8) {
          uVar13 = 0x20c07cc;
          puStack_70 = puStack_a0;
          goto code_r0x0208ada4;
        }
        goto LAB_0200c6cc;
      }
      lRam0000000002110380 = 0xb00020;
    }
    lRam0000000002110380 = lVar14 + lRam0000000002110380;
    iRam00000000020e5674 = iVar7;
    FUN_02091664(0x210f211,0,0x800);
    puStack_b8 = (undefined1 *)0x0;
    ppuStack_b0 = (undefined1 **)0x0;
    uStack_c8 = 0;
    uStack_c0 = 0;
    uVar11 = FUN_020036c8(1);
    FUN_02091664(&uStack_c8,0,0x20);
    uVar13 = 0x20b6459;
    if (iRam00000000020e5674 != 1) {
      uVar13 = 0x20c7be8;
    }
    uVar12 = FUN_02091524(uVar13);
    FUN_020915ac(&uStack_c8,uVar13,uVar12);
    lVar14 = func_0x0205d014(0x20b0314);
    uVar9 = FUN_02091294(3);
    FUN_02003ad0(uVar11,lVar14 + 0xb00000,0x20,&uStack_c8,uVar9);
    func_0x02099d18();
  }
  else {
    iRam00000000020e5674 = 0;
  }
  uVar5 = iVar8 == 1;
  cRam000000000210f210 = uVar5;
  FUN_0208ada0(0x20cf2a4,iRam00000000020e5674,lRam0000000002110380);
  func_0x0200c740();
  if ((bool)uVar5) {
    return;
  }
LAB_0200c6cc:
  FUN_02091090();
  uVar5 = cRam000000000210f210 == '\x01';
  if ((bool)uVar5) {
    iRam00000000020e5674 = 0;
    FUN_0208ada0(0x20c48a7,0);
    cRam000000000210f210 = 0;
    if ((((lRam0000000002121fc8 == 0) &&
         (lRam0000000002121fc8 = func_0x02006dbc(0x20b92ab), lRam0000000002121fc8 == 0)) &&
        (lRam0000000002121fc8 = func_0x02006dbc(0x20b6fed), lRam0000000002121fc8 == 0)) ||
       (func_0x0209abec(), !(bool)uVar5)) {
      func_0x02099c6c();
      uRam000000001c010000 = uRam000000001c010000 | 0x22000001;
    }
    return;
  }
  return;
}


