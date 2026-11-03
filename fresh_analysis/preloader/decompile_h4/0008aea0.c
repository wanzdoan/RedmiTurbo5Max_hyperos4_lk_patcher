// File offset entry: 0x8aea0

/* WARNING: Possible PIC construction at 0x0208adf4: Changing call to branch */
/* WARNING: Removing unreachable block (ram,0x0208adfc) */
/* WARNING: Removing unreachable block (ram,0x0208ae20) */
/* WARNING: Removing unreachable block (ram,0x0208ae10) */

void FUN_0208ada0(byte *param_1)

{
  byte *pbVar1;
  byte bVar2;
  int iVar3;
  undefined1 uVar4;
  bool bVar5;
  undefined1 uVar6;
  bool bVar7;
  uint uVar8;
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
  uint *puVar9;
  int *extraout_x8_15;
  int *piVar10;
  undefined8 *extraout_x8_16;
  undefined8 *extraout_x8_17;
  undefined8 *extraout_x8_18;
  undefined8 *puVar11;
  undefined8 *extraout_x8_19;
  undefined8 *extraout_x8_20;
  int extraout_w9;
  int extraout_w9_00;
  int extraout_w9_01;
  int extraout_w9_02;
  int extraout_w9_03;
  int extraout_w9_04;
  int extraout_w9_05;
  int extraout_w9_06;
  int extraout_w9_07;
  int extraout_w9_08;
  int extraout_w9_09;
  byte *pbVar12;
  int iVar13;
  
  iVar3 = -0x38;
  do {
    while( true ) {
      pbVar12 = param_1;
      bVar2 = *pbVar12;
      uVar8 = (uint)bVar2;
      if (bVar2 == 0) {
        return;
      }
      pbVar1 = pbVar12 + 1;
      param_1 = pbVar1;
      if (bVar2 != 10) break;
LAB_0208af10:
      func_0x0208ac88(0xd);
      uVar8 = 10;
LAB_0208af1c:
      func_0x0208ac88(uVar8);
    }
    if (bVar2 == 0xd) {
      param_1 = pbVar12 + 2;
      if (pbVar12[1] != 10) {
        param_1 = pbVar1;
      }
      goto LAB_0208af10;
    }
    if (bVar2 != 0x25) goto LAB_0208af1c;
    bVar2 = pbVar12[1];
    param_1 = pbVar12 + 2;
    uVar8 = bVar2 - 0x70;
    uVar4 = 7 < uVar8;
    uVar6 = uVar8 == 8;
    switch(uVar8) {
    case 0:
      func_0x0208ab60(0x20bef68);
    case 8:
      if ((-1 < iVar3) || (func_0x0208b33c(), iVar3 = extraout_w9_00, (bool)uVar4 && !(bool)uVar6))
      {
        func_0x0208b310();
        puVar11 = extraout_x8_01;
      }
      else {
        func_0x0208b328();
        puVar11 = extraout_x8_00;
      }
      func_0x0208b180(*puVar11,0);
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
      if ((-1 < iVar3) || (func_0x0208b33c(), iVar3 = extraout_w9_01, (bool)uVar4 && !(bool)uVar6))
      {
        func_0x0208b310();
        puVar11 = extraout_x8_09;
      }
      else {
        func_0x0208b328();
        puVar11 = extraout_x8_02;
      }
      func_0x0208ab60(*puVar11);
      break;
    case 5:
      if ((-1 < iVar3) || (func_0x0208b33c(), iVar3 = extraout_w9_02, (bool)uVar4 && !(bool)uVar6))
      {
        func_0x0208b310();
        puVar11 = extraout_x8_10;
      }
      else {
        func_0x0208b328();
        puVar11 = extraout_x8_03;
      }
      func_0x0208abb0(*puVar11);
      break;
    default:
      if (bVar2 == 0x25) {
        uVar8 = 0x25;
        goto LAB_0208af1c;
      }
      bVar5 = 0x41 < bVar2;
      bVar7 = bVar2 == 0x42;
      if (bVar7) {
        if ((-1 < iVar3) || (func_0x0208b33c(), iVar3 = extraout_w9_03, bVar5 && !bVar7)) {
          func_0x0208b310();
          puVar11 = extraout_x8_11;
        }
        else {
          func_0x0208b328();
          puVar11 = extraout_x8_04;
        }
        func_0x0208b180(*puVar11,2);
      }
      else {
        bVar5 = 0x47 < bVar2;
        bVar7 = bVar2 == 0x48;
        if (bVar7) {
          if ((-1 < iVar3) || (func_0x0208b33c(), iVar3 = extraout_w9_04, bVar5 && !bVar7)) {
            func_0x0208b310();
            puVar11 = extraout_x8_12;
          }
          else {
            func_0x0208b328();
            puVar11 = extraout_x8_05;
          }
          func_0x0208b180(*puVar11,4);
        }
        else {
          bVar5 = 0x57 < bVar2;
          bVar7 = bVar2 == 0x58;
          if (bVar7) {
            if ((-1 < iVar3) || (func_0x0208b33c(), iVar3 = extraout_w9_05, bVar5 && !bVar7)) {
              func_0x0208b310();
              puVar11 = extraout_x8_13;
            }
            else {
              func_0x0208b328();
              puVar11 = extraout_x8_06;
            }
            func_0x0208b180(*puVar11,8);
          }
          else {
            bVar5 = 0x62 < bVar2;
            bVar7 = bVar2 == 99;
            if (bVar7) {
              if ((-1 < iVar3) || (func_0x0208b33c(), iVar3 = extraout_w9_06, bVar5 && !bVar7)) {
                func_0x0208b310();
                puVar9 = extraout_x8_14;
              }
              else {
                func_0x0208b328();
                puVar9 = extraout_x8_07;
              }
              uVar8 = *puVar9;
              goto LAB_0208af1c;
            }
            bVar5 = 99 < bVar2;
            bVar7 = bVar2 == 100;
            if (bVar7) {
              if ((-1 < iVar3) || (func_0x0208b33c(), iVar3 = extraout_w9_07, bVar5 && !bVar7)) {
                func_0x0208b310();
                piVar10 = extraout_x8_15;
              }
              else {
                func_0x0208b328();
                piVar10 = extraout_x8_08;
              }
              iVar13 = *piVar10;
              if (iVar13 < 0) {
                func_0x0208ac88(0x2d);
                iVar13 = -iVar13;
              }
              func_0x0208abb0(iVar13);
            }
            else {
              if (bVar2 != 0x6c) goto code_r0x0208af78;
              bVar2 = *param_1;
              if (bVar2 == 0x6c) {
                bVar5 = 0x77 < pbVar12[3];
                bVar7 = pbVar12[3] == 0x78;
                if (bVar7) {
                  if ((-1 < iVar3) || (func_0x0208b33c(), iVar3 = extraout_w9_08, bVar5 && !bVar7))
                  {
                    func_0x0208b310();
                    puVar11 = extraout_x8_20;
                  }
                  else {
                    func_0x0208b328();
                    puVar11 = extraout_x8_16;
                  }
                  func_0x0208b180(*puVar11,0x10);
                  param_1 = pbVar12 + 4;
                }
              }
              else {
                bVar5 = 0x74 < bVar2;
                bVar7 = bVar2 == 0x75;
                if (bVar7) {
                  if ((-1 < iVar3) || (func_0x0208b33c(), iVar3 = extraout_w9_09, bVar5 && !bVar7))
                  {
                    func_0x0208b310();
                    puVar11 = extraout_x8_19;
                  }
                  else {
                    func_0x0208b328();
                    puVar11 = extraout_x8_17;
                  }
                  func_0x0208b1d8(*puVar11);
                  param_1 = pbVar12 + 3;
                }
                else {
                  bVar5 = 0x77 < bVar2;
                  bVar7 = bVar2 == 0x78;
                  if (bVar7) {
                    if ((-1 < iVar3) || (func_0x0208b33c(), iVar3 = extraout_w9, bVar5 && !bVar7)) {
                      func_0x0208b310();
                      puVar11 = extraout_x8_18;
                    }
                    else {
                      func_0x0208b328();
                      puVar11 = extraout_x8;
                    }
                    func_0x0208b180(*puVar11,0);
                    param_1 = pbVar12 + 3;
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


