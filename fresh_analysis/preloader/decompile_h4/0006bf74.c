// File offset entry: 0x6bf74

void FUN_0206be74(void)

{
  byte *pbVar1;
  byte bVar2;
  long *plVar3;
  undefined1 uVar4;
  bool bVar5;
  undefined1 uVar6;
  bool bVar7;
  uint uVar8;
  long lVar9;
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
  uint *puVar10;
  int *extraout_x8_15;
  int *piVar11;
  undefined8 *extraout_x8_16;
  undefined8 *extraout_x8_17;
  undefined8 *extraout_x8_18;
  undefined8 *puVar12;
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
  ulong uVar13;
  byte *pbVar14;
  byte *pbVar15;
  int iVar16;
  undefined1 auVar17 [16];
  
  plVar3 = plRam0000000002103338;
  FUN_0208ada0(0x20b5959,*(undefined4 *)(*plRam0000000002103338 + 0x7d6f0));
  for (uVar13 = 0; uVar13 < *(uint *)(*plVar3 + 0x7cae8); uVar13 = uVar13 + 1) {
    FUN_0208ada0(0x20c2d62,uVar13 & 0xffffffff);
  }
  lVar9 = 0x7d700;
  for (uVar13 = 0; uVar13 != 0x80; uVar13 = uVar13 + 1) {
    if (*(long *)(*plVar3 + lVar9) != 0) {
      FUN_0208ada0(0x20b5980,uVar13 & 0xffffffff);
    }
    lVar9 = lVar9 + 0x98;
  }
  lVar9 = *plRam0000000002103280;
  FUN_0208ae24(0x20c8296,&stack0xffffffffffffffa8);
  if (lRam0000000002100880 == lVar9) {
    return;
  }
  auVar17 = FUN_02091090();
  lVar9 = auVar17._8_8_;
  pbVar15 = auVar17._0_8_;
  do {
    while( true ) {
      pbVar14 = pbVar15;
      bVar2 = *pbVar14;
      uVar8 = (uint)bVar2;
      if (bVar2 == 0) {
        return;
      }
      pbVar1 = pbVar14 + 1;
      pbVar15 = pbVar1;
      if (bVar2 != 10) break;
LAB_0208af10:
      FUN_0208ac88(0xd);
      uVar8 = 10;
LAB_0208af1c:
      FUN_0208ac88(uVar8);
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
    uVar8 = bVar2 - 0x70;
    uVar4 = 7 < uVar8;
    uVar6 = uVar8 == 8;
    switch(uVar8) {
    case 0:
      func_0x0208ab60(0x20bef68);
    case 8:
      if (*(int *)(lVar9 + 0x18) < 0) {
        FUN_0208b33c();
        *(undefined4 *)(lVar9 + 0x18) = extraout_w9_00;
        if ((bool)uVar4 && !(bool)uVar6) goto code_r0x0208af60;
        FUN_0208b328();
        puVar12 = extraout_x8_00;
      }
      else {
code_r0x0208af60:
        FUN_0208b310();
        puVar12 = extraout_x8_01;
      }
      FUN_0208b180(*puVar12,0);
      break;
    case 1:
    case 2:
    case 4:
    case 6:
    case 7:
code_r0x0208af78:
      uVar8 = 0x20;
      goto LAB_0208af1c;
    case 3:
      if (*(int *)(lVar9 + 0x18) < 0) {
        FUN_0208b33c();
        *(undefined4 *)(lVar9 + 0x18) = extraout_w9_01;
        if ((bool)uVar4 && !(bool)uVar6) goto code_r0x0208b054;
        FUN_0208b328();
        puVar12 = extraout_x8_02;
      }
      else {
code_r0x0208b054:
        FUN_0208b310();
        puVar12 = extraout_x8_09;
      }
      func_0x0208ab60(*puVar12);
      break;
    case 5:
      if (*(int *)(lVar9 + 0x18) < 0) {
        FUN_0208b33c();
        *(undefined4 *)(lVar9 + 0x18) = extraout_w9_02;
        if ((bool)uVar4 && !(bool)uVar6) goto code_r0x0208b064;
        FUN_0208b328();
        puVar12 = extraout_x8_03;
      }
      else {
code_r0x0208b064:
        FUN_0208b310();
        puVar12 = extraout_x8_10;
      }
      FUN_0208abb0(*puVar12);
      break;
    default:
      if (bVar2 == 0x25) {
        uVar8 = 0x25;
        goto LAB_0208af1c;
      }
      bVar5 = 0x41 < bVar2;
      bVar7 = bVar2 == 0x42;
      if (bVar7) {
        if (*(int *)(lVar9 + 0x18) < 0) {
          FUN_0208b33c();
          *(undefined4 *)(lVar9 + 0x18) = extraout_w9_03;
          if (bVar5 && !bVar7) goto LAB_0208b074;
          FUN_0208b328();
          puVar12 = extraout_x8_04;
        }
        else {
LAB_0208b074:
          FUN_0208b310();
          puVar12 = extraout_x8_11;
        }
        FUN_0208b180(*puVar12,2);
      }
      else {
        bVar5 = 0x47 < bVar2;
        bVar7 = bVar2 == 0x48;
        if (bVar7) {
          if (*(int *)(lVar9 + 0x18) < 0) {
            FUN_0208b33c();
            *(undefined4 *)(lVar9 + 0x18) = extraout_w9_04;
            if (bVar5 && !bVar7) goto LAB_0208b088;
            FUN_0208b328();
            puVar12 = extraout_x8_05;
          }
          else {
LAB_0208b088:
            FUN_0208b310();
            puVar12 = extraout_x8_12;
          }
          FUN_0208b180(*puVar12,4);
        }
        else {
          bVar5 = 0x57 < bVar2;
          bVar7 = bVar2 == 0x58;
          if (bVar7) {
            if (*(int *)(lVar9 + 0x18) < 0) {
              FUN_0208b33c();
              *(undefined4 *)(lVar9 + 0x18) = extraout_w9_05;
              if (bVar5 && !bVar7) goto LAB_0208b09c;
              FUN_0208b328();
              puVar12 = extraout_x8_06;
            }
            else {
LAB_0208b09c:
              FUN_0208b310();
              puVar12 = extraout_x8_13;
            }
            FUN_0208b180(*puVar12,8);
          }
          else {
            bVar5 = 0x62 < bVar2;
            bVar7 = bVar2 == 99;
            if (bVar7) {
              if (*(int *)(lVar9 + 0x18) < 0) {
                FUN_0208b33c();
                *(undefined4 *)(lVar9 + 0x18) = extraout_w9_06;
                if (bVar5 && !bVar7) goto LAB_0208b0b0;
                FUN_0208b328();
                puVar10 = extraout_x8_07;
              }
              else {
LAB_0208b0b0:
                FUN_0208b310();
                puVar10 = extraout_x8_14;
              }
              uVar8 = *puVar10;
              goto LAB_0208af1c;
            }
            bVar5 = 99 < bVar2;
            bVar7 = bVar2 == 100;
            if (bVar7) {
              if (*(int *)(lVar9 + 0x18) < 0) {
                FUN_0208b33c();
                *(undefined4 *)(lVar9 + 0x18) = extraout_w9_07;
                if (bVar5 && !bVar7) goto LAB_0208b0bc;
                FUN_0208b328();
                piVar11 = extraout_x8_08;
              }
              else {
LAB_0208b0bc:
                FUN_0208b310();
                piVar11 = extraout_x8_15;
              }
              iVar16 = *piVar11;
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
                bVar5 = 0x77 < pbVar14[3];
                bVar7 = pbVar14[3] == 0x78;
                if (bVar7) {
                  if (*(int *)(lVar9 + 0x18) < 0) {
                    FUN_0208b33c();
                    *(undefined4 *)(lVar9 + 0x18) = extraout_w9_08;
                    if (bVar5 && !bVar7) goto LAB_0208b150;
                    FUN_0208b328();
                    puVar12 = extraout_x8_16;
                  }
                  else {
LAB_0208b150:
                    FUN_0208b310();
                    puVar12 = extraout_x8_20;
                  }
                  FUN_0208b180(*puVar12,0x10);
                  pbVar15 = pbVar14 + 4;
                }
              }
              else {
                bVar5 = 0x74 < bVar2;
                bVar7 = bVar2 == 0x75;
                if (bVar7) {
                  if (*(int *)(lVar9 + 0x18) < 0) {
                    FUN_0208b33c();
                    *(undefined4 *)(lVar9 + 0x18) = extraout_w9_09;
                    if (bVar5 && !bVar7) goto LAB_0208b13c;
                    FUN_0208b328();
                    puVar12 = extraout_x8_17;
                  }
                  else {
LAB_0208b13c:
                    FUN_0208b310();
                    puVar12 = extraout_x8_19;
                  }
                  FUN_0208b1d8(*puVar12);
                  pbVar15 = pbVar14 + 3;
                }
                else {
                  bVar5 = 0x77 < bVar2;
                  bVar7 = bVar2 == 0x78;
                  if (bVar7) {
                    if (*(int *)(lVar9 + 0x18) < 0) {
                      FUN_0208b33c();
                      *(undefined4 *)(lVar9 + 0x18) = extraout_w9;
                      if (bVar5 && !bVar7) goto LAB_0208b124;
                      FUN_0208b328();
                      puVar12 = extraout_x8;
                    }
                    else {
LAB_0208b124:
                      FUN_0208b310();
                      puVar12 = extraout_x8_18;
                    }
                    FUN_0208b180(*puVar12,0);
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


