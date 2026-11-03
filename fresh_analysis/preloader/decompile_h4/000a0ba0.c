// File offset entry: 0xa0ba0

void FUN_020a0aa0(void)

{
  byte *pbVar1;
  byte bVar2;
  undefined1 uVar3;
  bool bVar4;
  undefined1 uVar5;
  bool bVar6;
  uint uVar7;
  int iVar8;
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
  byte *pbVar13;
  byte *pbVar14;
  int iVar15;
  undefined1 auVar16 [16];
  
  uRam00000000180100c0 = 0xffffffff;
  uRam0000000018010030 = 3;
  func_0x020a0a5c();
  iVar15 = -1000000;
  do {
    iVar8 = iVar15;
    if ((uRam0000000018010020 & 1) != 0) break;
    iVar15 = iVar8 + 1;
  } while (iVar8 != 0);
  if (iVar8 != 0) {
    return;
  }
  lVar9 = *plRam0000000002103280;
  FUN_0208ae24(0x20b512f,&stack0xffffffffffffffa8);
  if (lRam0000000002100880 == lVar9) {
    return;
  }
  auVar16 = FUN_02091090();
  lVar9 = auVar16._8_8_;
  pbVar14 = auVar16._0_8_;
  do {
    while( true ) {
      pbVar13 = pbVar14;
      bVar2 = *pbVar13;
      uVar7 = (uint)bVar2;
      if (bVar2 == 0) {
        return;
      }
      pbVar1 = pbVar13 + 1;
      pbVar14 = pbVar1;
      if (bVar2 != 10) break;
LAB_0208af10:
      FUN_0208ac88(0xd);
      uVar7 = 10;
LAB_0208af1c:
      FUN_0208ac88(uVar7);
    }
    if (bVar2 == 0xd) {
      pbVar14 = pbVar13 + 2;
      if (pbVar13[1] != 10) {
        pbVar14 = pbVar1;
      }
      goto LAB_0208af10;
    }
    if (bVar2 != 0x25) goto LAB_0208af1c;
    bVar2 = pbVar13[1];
    pbVar14 = pbVar13 + 2;
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
      uVar7 = 0x20;
      goto LAB_0208af1c;
    case 3:
      if (*(int *)(lVar9 + 0x18) < 0) {
        FUN_0208b33c();
        *(undefined4 *)(lVar9 + 0x18) = extraout_w9_01;
        if ((bool)uVar3 && !(bool)uVar5) goto code_r0x0208b054;
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
        if ((bool)uVar3 && !(bool)uVar5) goto code_r0x0208b064;
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
        bVar4 = 0x47 < bVar2;
        bVar6 = bVar2 == 0x48;
        if (bVar6) {
          if (*(int *)(lVar9 + 0x18) < 0) {
            FUN_0208b33c();
            *(undefined4 *)(lVar9 + 0x18) = extraout_w9_04;
            if (bVar4 && !bVar6) goto LAB_0208b088;
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
          bVar4 = 0x57 < bVar2;
          bVar6 = bVar2 == 0x58;
          if (bVar6) {
            if (*(int *)(lVar9 + 0x18) < 0) {
              FUN_0208b33c();
              *(undefined4 *)(lVar9 + 0x18) = extraout_w9_05;
              if (bVar4 && !bVar6) goto LAB_0208b09c;
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
            bVar4 = 0x62 < bVar2;
            bVar6 = bVar2 == 99;
            if (bVar6) {
              if (*(int *)(lVar9 + 0x18) < 0) {
                FUN_0208b33c();
                *(undefined4 *)(lVar9 + 0x18) = extraout_w9_06;
                if (bVar4 && !bVar6) goto LAB_0208b0b0;
                FUN_0208b328();
                puVar10 = extraout_x8_07;
              }
              else {
LAB_0208b0b0:
                FUN_0208b310();
                puVar10 = extraout_x8_14;
              }
              uVar7 = *puVar10;
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
                piVar11 = extraout_x8_08;
              }
              else {
LAB_0208b0bc:
                FUN_0208b310();
                piVar11 = extraout_x8_15;
              }
              iVar15 = *piVar11;
              if (iVar15 < 0) {
                FUN_0208ac88(0x2d);
                iVar15 = -iVar15;
              }
              FUN_0208abb0(iVar15);
            }
            else {
              if (bVar2 != 0x6c) goto code_r0x0208af78;
              bVar2 = *pbVar14;
              if (bVar2 == 0x6c) {
                bVar4 = 0x77 < pbVar13[3];
                bVar6 = pbVar13[3] == 0x78;
                if (bVar6) {
                  if (*(int *)(lVar9 + 0x18) < 0) {
                    FUN_0208b33c();
                    *(undefined4 *)(lVar9 + 0x18) = extraout_w9_08;
                    if (bVar4 && !bVar6) goto LAB_0208b150;
                    FUN_0208b328();
                    puVar12 = extraout_x8_16;
                  }
                  else {
LAB_0208b150:
                    FUN_0208b310();
                    puVar12 = extraout_x8_20;
                  }
                  FUN_0208b180(*puVar12,0x10);
                  pbVar14 = pbVar13 + 4;
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
                    puVar12 = extraout_x8_17;
                  }
                  else {
LAB_0208b13c:
                    FUN_0208b310();
                    puVar12 = extraout_x8_19;
                  }
                  FUN_0208b1d8(*puVar12);
                  pbVar14 = pbVar13 + 3;
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
                      puVar12 = extraout_x8;
                    }
                    else {
LAB_0208b124:
                      FUN_0208b310();
                      puVar12 = extraout_x8_18;
                    }
                    FUN_0208b180(*puVar12,0);
                    pbVar14 = pbVar13 + 3;
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


