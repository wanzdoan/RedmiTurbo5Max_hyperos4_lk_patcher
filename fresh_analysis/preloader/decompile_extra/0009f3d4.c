// File offset entry: 0x9f3d4

/* WARNING: Possible PIC construction at 0x0209f360: Changing call to branch */
/* WARNING: Removing unreachable block (ram,0x0209f364) */
/* WARNING: Removing unreachable block (ram,0x0209f38c) */
/* WARNING: Removing unreachable block (ram,0x0209f41c) */

undefined8 FUN_0209f2d4(uint param_1,undefined1 *param_2,uint param_3)

{
  byte *pbVar1;
  byte bVar2;
  undefined1 uVar3;
  bool bVar4;
  undefined1 uVar5;
  bool bVar6;
  uint uVar7;
  uint *puVar8;
  long lVar9;
  undefined8 ****in_x6;
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
  int *extraout_x8_15;
  int *piVar10;
  undefined8 *extraout_x8_16;
  undefined8 *extraout_x8_17;
  undefined8 *extraout_x8_18;
  undefined8 *puVar11;
  undefined8 *extraout_x8_19;
  undefined8 *extraout_x8_20;
  ulong uVar12;
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
  undefined1 *puVar13;
  byte *pbVar14;
  byte *pbVar15;
  int iVar16;
  undefined8 uVar17;
  undefined1 auVar18 [16];
  undefined8 ****appppuStack_108 [4];
  undefined8 uStack_e8;
  undefined8 uStack_e0;
  undefined1 *puStack_d8;
  undefined8 ****ppppuStack_d0;
  long lStack_c8;
  undefined1 *puStack_c0;
  long lStack_b8;
  undefined1 *puStack_b0;
  undefined8 uStack_a8;
  undefined1 auStack_a0 [52];
  uint uStack_6c;
  long lStack_68;
  
  lStack_68 = *plRam0000000002103280;
  uStack_6c = 0;
  if ((param_2 == (undefined1 *)0x0) || (3 < param_1 && param_1 - 10 < 0xfffffffb)) {
    FUN_0208ada0(0x20c6920,0x10200f);
    if (param_3 != 0) {
      uVar12 = (ulong)param_3;
      puVar13 = param_2;
      do {
        uVar12 = uVar12 - 1;
        param_2 = puVar13 + 1;
        *puVar13 = 0;
        puVar13 = param_2;
      } while (uVar12 != 0);
    }
    if (lRam0000000002100880 == lStack_68) {
      return 0x10200f;
    }
    uVar17 = 0x209f428;
    puVar8 = (uint *)FUN_02091090();
  }
  else {
    puVar8 = &uStack_6c;
    uVar17 = 0x209f364;
  }
  lStack_c8 = *plRam0000000002103280;
  puStack_c0 = &stack0xffffffffffffffa0;
  lStack_b8 = uVar17;
  puStack_b0 = param_2;
  FUN_020915ac(appppuStack_108,0x20e3c04);
  uVar7 = func_0x0209b1dc();
  *puVar8 = uVar7;
  if ((uVar7 < 8) && ((1 << (ulong)(uVar7 & 0x1f) & 0xabU) != 0)) {
    uVar17 = FUN_0208ada0(0x20b2fe8,uVar7,appppuStack_108 + uVar7);
    if (*plRam0000000002103280 == lStack_c8) {
      return uVar17;
    }
  }
  else if (*plRam0000000002103280 == lStack_c8) {
    puStack_b0 = puStack_c0;
    uStack_a8 = lStack_b8;
    appppuStack_108[3] = appppuStack_108 + 2;
    lStack_b8 = *plRam0000000002103280;
    lStack_c8 = 0;
    puStack_c0 = (undefined1 *)0xffffffc8;
    uStack_e8 = 0;
    uStack_e0 = 0xffffffc8;
    appppuStack_108[0] = in_x6;
    appppuStack_108[1] = (undefined8 ****)in_x7;
    appppuStack_108[2] = (undefined8 ****)auStack_a0;
    puStack_d8 = auStack_a0;
    ppppuStack_d0 = appppuStack_108[3];
    uVar17 = FUN_0208ae24(0x20c5563,appppuStack_108 + 2);
    if (lRam0000000002100880 == lStack_b8) {
      return uVar17;
    }
    auVar18 = FUN_02091090();
    lVar9 = auVar18._8_8_;
    pbVar15 = auVar18._0_8_;
    do {
      while( true ) {
        pbVar14 = pbVar15;
        bVar2 = *pbVar14;
        uVar7 = (uint)bVar2;
        if (bVar2 == 0) {
          return 0;
        }
        pbVar1 = pbVar14 + 1;
        pbVar15 = pbVar1;
        if (bVar2 != 10) break;
LAB_0208af10:
        FUN_0208ac88(0xd);
        uVar7 = 10;
LAB_0208af1c:
        FUN_0208ac88(uVar7);
      }
      if (bVar2 == 0xd) {
        pbVar15 = pbVar14 + 2;
        if (pbVar14[1] != 10) {
          pbVar15 = pbVar1;
        }
        goto LAB_0208af10;
      }
      if (bVar2 != 0x25) goto LAB_0208af1c;
      bVar2 = pbVar14[1];
      pbVar15 = pbVar14 + 2;
      uVar7 = bVar2 - 0x70;
      uVar3 = 7 < uVar7;
      uVar5 = uVar7 == 8;
      switch(uVar7) {
      case 0:
        func_0x0208ab60(0x20bef68);
      case 8:
        if (*(int *)(lVar9 + 0x18) < 0) {
          FUN_0208b33c();
          *(undefined4 *)(lVar9 + 0x18) = extraout_w9_00;
          if ((bool)uVar3 && !(bool)uVar5) goto code_r0x0208af60;
          FUN_0208b328();
          puVar11 = extraout_x8_00;
        }
        else {
code_r0x0208af60:
          FUN_0208b310();
          puVar11 = extraout_x8_01;
        }
        FUN_0208b180(*puVar11,0);
        break;
      case 1:
      case 2:
      case 4:
      case 6:
      case 7:
code_r0x0208af78:
        uVar7 = 0x20;
        goto LAB_0208af1c;
      case 3:
        if (*(int *)(lVar9 + 0x18) < 0) {
          FUN_0208b33c();
          *(undefined4 *)(lVar9 + 0x18) = extraout_w9_01;
          if ((bool)uVar3 && !(bool)uVar5) goto code_r0x0208b054;
          FUN_0208b328();
          puVar11 = extraout_x8_02;
        }
        else {
code_r0x0208b054:
          FUN_0208b310();
          puVar11 = extraout_x8_09;
        }
        func_0x0208ab60(*puVar11);
        break;
      case 5:
        if (*(int *)(lVar9 + 0x18) < 0) {
          FUN_0208b33c();
          *(undefined4 *)(lVar9 + 0x18) = extraout_w9_02;
          if ((bool)uVar3 && !(bool)uVar5) goto code_r0x0208b064;
          FUN_0208b328();
          puVar11 = extraout_x8_03;
        }
        else {
code_r0x0208b064:
          FUN_0208b310();
          puVar11 = extraout_x8_10;
        }
        FUN_0208abb0(*puVar11);
        break;
      default:
        if (bVar2 == 0x25) {
          uVar7 = 0x25;
          goto LAB_0208af1c;
        }
        bVar4 = 0x41 < bVar2;
        bVar6 = bVar2 == 0x42;
        if (bVar6) {
          if (*(int *)(lVar9 + 0x18) < 0) {
            FUN_0208b33c();
            *(undefined4 *)(lVar9 + 0x18) = extraout_w9_03;
            if (bVar4 && !bVar6) goto LAB_0208b074;
            FUN_0208b328();
            puVar11 = extraout_x8_04;
          }
          else {
LAB_0208b074:
            FUN_0208b310();
            puVar11 = extraout_x8_11;
          }
          FUN_0208b180(*puVar11,2);
        }
        else {
          bVar4 = 0x47 < bVar2;
          bVar6 = bVar2 == 0x48;
          if (bVar6) {
            if (*(int *)(lVar9 + 0x18) < 0) {
              FUN_0208b33c();
              *(undefined4 *)(lVar9 + 0x18) = extraout_w9_04;
              if (bVar4 && !bVar6) goto LAB_0208b088;
              FUN_0208b328();
              puVar11 = extraout_x8_05;
            }
            else {
LAB_0208b088:
              FUN_0208b310();
              puVar11 = extraout_x8_12;
            }
            FUN_0208b180(*puVar11,4);
          }
          else {
            bVar4 = 0x57 < bVar2;
            bVar6 = bVar2 == 0x58;
            if (bVar6) {
              if (*(int *)(lVar9 + 0x18) < 0) {
                FUN_0208b33c();
                *(undefined4 *)(lVar9 + 0x18) = extraout_w9_05;
                if (bVar4 && !bVar6) goto LAB_0208b09c;
                FUN_0208b328();
                puVar11 = extraout_x8_06;
              }
              else {
LAB_0208b09c:
                FUN_0208b310();
                puVar11 = extraout_x8_13;
              }
              FUN_0208b180(*puVar11,8);
            }
            else {
              bVar4 = 0x62 < bVar2;
              bVar6 = bVar2 == 99;
              if (bVar6) {
                if (*(int *)(lVar9 + 0x18) < 0) {
                  FUN_0208b33c();
                  *(undefined4 *)(lVar9 + 0x18) = extraout_w9_06;
                  if (bVar4 && !bVar6) goto LAB_0208b0b0;
                  FUN_0208b328();
                  puVar8 = extraout_x8_07;
                }
                else {
LAB_0208b0b0:
                  FUN_0208b310();
                  puVar8 = extraout_x8_14;
                }
                uVar7 = *puVar8;
                goto LAB_0208af1c;
              }
              bVar4 = 99 < bVar2;
              bVar6 = bVar2 == 100;
              if (bVar6) {
                if (*(int *)(lVar9 + 0x18) < 0) {
                  FUN_0208b33c();
                  *(undefined4 *)(lVar9 + 0x18) = extraout_w9_07;
                  if (bVar4 && !bVar6) goto LAB_0208b0bc;
                  FUN_0208b328();
                  piVar10 = extraout_x8_08;
                }
                else {
LAB_0208b0bc:
                  FUN_0208b310();
                  piVar10 = extraout_x8_15;
                }
                iVar16 = *piVar10;
                if (iVar16 < 0) {
                  FUN_0208ac88(0x2d);
                  iVar16 = -iVar16;
                }
                FUN_0208abb0(iVar16);
              }
              else {
                if (bVar2 != 0x6c) goto code_r0x0208af78;
                bVar2 = *pbVar15;
                if (bVar2 == 0x6c) {
                  bVar4 = 0x77 < pbVar14[3];
                  bVar6 = pbVar14[3] == 0x78;
                  if (bVar6) {
                    if (*(int *)(lVar9 + 0x18) < 0) {
                      FUN_0208b33c();
                      *(undefined4 *)(lVar9 + 0x18) = extraout_w9_08;
                      if (bVar4 && !bVar6) goto LAB_0208b150;
                      FUN_0208b328();
                      puVar11 = extraout_x8_16;
                    }
                    else {
LAB_0208b150:
                      FUN_0208b310();
                      puVar11 = extraout_x8_20;
                    }
                    FUN_0208b180(*puVar11,0x10);
                    pbVar15 = pbVar14 + 4;
                  }
                }
                else {
                  bVar4 = 0x74 < bVar2;
                  bVar6 = bVar2 == 0x75;
                  if (bVar6) {
                    if (*(int *)(lVar9 + 0x18) < 0) {
                      FUN_0208b33c();
                      *(undefined4 *)(lVar9 + 0x18) = extraout_w9_09;
                      if (bVar4 && !bVar6) goto LAB_0208b13c;
                      FUN_0208b328();
                      puVar11 = extraout_x8_17;
                    }
                    else {
LAB_0208b13c:
                      FUN_0208b310();
                      puVar11 = extraout_x8_19;
                    }
                    FUN_0208b1d8(*puVar11);
                    pbVar15 = pbVar14 + 3;
                  }
                  else {
                    bVar4 = 0x77 < bVar2;
                    bVar6 = bVar2 == 0x78;
                    if (bVar6) {
                      if (*(int *)(lVar9 + 0x18) < 0) {
                        FUN_0208b33c();
                        *(undefined4 *)(lVar9 + 0x18) = extraout_w9;
                        if (bVar4 && !bVar6) goto LAB_0208b124;
                        FUN_0208b328();
                        puVar11 = extraout_x8;
                      }
                      else {
LAB_0208b124:
                        FUN_0208b310();
                        puVar11 = extraout_x8_18;
                      }
                      FUN_0208b180(*puVar11,0);
                      pbVar15 = pbVar14 + 3;
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
  auVar18 = FUN_02091090();
  uVar7 = 1 << (ulong)(auVar18._8_4_ & 0x1f);
  FUN_0208ada0(0x20cee15,uVar7,*(undefined4 *)(auVar18._0_8_ + 0x24));
  if ((*(uint *)(auVar18._0_8_ + 0x24) & uVar7) == 0) {
    FUN_0208ada0(0x20b510e,auVar18._8_8_ & 0xffffffff);
    uVar17 = 0x10200e;
  }
  else {
    uVar17 = 0;
  }
  return uVar17;
}


