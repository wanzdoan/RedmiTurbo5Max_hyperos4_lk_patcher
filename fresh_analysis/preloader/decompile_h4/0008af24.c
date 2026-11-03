// File offset entry: 0x8af24

void FUN_0208ae24(byte *param_1,long param_2)

{
  byte *pbVar1;
  byte bVar2;
  undefined1 uVar3;
  bool bVar4;
  undefined1 uVar5;
  bool bVar6;
  uint uVar7;
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
  uint *puVar8;
  int *extraout_x8_15;
  int *piVar9;
  undefined8 *extraout_x8_16;
  undefined8 *extraout_x8_17;
  undefined8 *extraout_x8_18;
  undefined8 *puVar10;
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
  byte *pbVar11;
  int iVar12;
  
  do {
    while( true ) {
      pbVar11 = param_1;
      bVar2 = *pbVar11;
      uVar7 = (uint)bVar2;
      if (bVar2 == 0) {
        return;
      }
      pbVar1 = pbVar11 + 1;
      param_1 = pbVar1;
      if (bVar2 != 10) break;
LAB_0208af10:
      func_0x0208ac88(0xd);
      uVar7 = 10;
LAB_0208af1c:
      func_0x0208ac88(uVar7);
    }
    if (bVar2 == 0xd) {
      param_1 = pbVar11 + 2;
      if (pbVar11[1] != 10) {
        param_1 = pbVar1;
      }
      goto LAB_0208af10;
    }
    if (bVar2 != 0x25) goto LAB_0208af1c;
    bVar2 = pbVar11[1];
    param_1 = pbVar11 + 2;
    uVar7 = bVar2 - 0x70;
    uVar3 = 7 < uVar7;
    uVar5 = uVar7 == 8;
    switch(uVar7) {
    case 0:
      func_0x0208ab60(0x20bef68);
    case 8:
      if (*(int *)(param_2 + 0x18) < 0) {
        func_0x0208b33c();
        *(undefined4 *)(param_2 + 0x18) = extraout_w9_00;
        if ((bool)uVar3 && !(bool)uVar5) goto code_r0x0208af60;
        func_0x0208b328();
        puVar10 = extraout_x8_00;
      }
      else {
code_r0x0208af60:
        func_0x0208b310();
        puVar10 = extraout_x8_01;
      }
      func_0x0208b180(*puVar10,0);
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
      if (*(int *)(param_2 + 0x18) < 0) {
        func_0x0208b33c();
        *(undefined4 *)(param_2 + 0x18) = extraout_w9_01;
        if ((bool)uVar3 && !(bool)uVar5) goto code_r0x0208b054;
        func_0x0208b328();
        puVar10 = extraout_x8_02;
      }
      else {
code_r0x0208b054:
        func_0x0208b310();
        puVar10 = extraout_x8_09;
      }
      func_0x0208ab60(*puVar10);
      break;
    case 5:
      if (*(int *)(param_2 + 0x18) < 0) {
        func_0x0208b33c();
        *(undefined4 *)(param_2 + 0x18) = extraout_w9_02;
        if ((bool)uVar3 && !(bool)uVar5) goto code_r0x0208b064;
        func_0x0208b328();
        puVar10 = extraout_x8_03;
      }
      else {
code_r0x0208b064:
        func_0x0208b310();
        puVar10 = extraout_x8_10;
      }
      func_0x0208abb0(*puVar10);
      break;
    default:
      if (bVar2 == 0x25) {
        uVar7 = 0x25;
        goto LAB_0208af1c;
      }
      bVar4 = 0x41 < bVar2;
      bVar6 = bVar2 == 0x42;
      if (bVar6) {
        if (*(int *)(param_2 + 0x18) < 0) {
          func_0x0208b33c();
          *(undefined4 *)(param_2 + 0x18) = extraout_w9_03;
          if (bVar4 && !bVar6) goto LAB_0208b074;
          func_0x0208b328();
          puVar10 = extraout_x8_04;
        }
        else {
LAB_0208b074:
          func_0x0208b310();
          puVar10 = extraout_x8_11;
        }
        func_0x0208b180(*puVar10,2);
      }
      else {
        bVar4 = 0x47 < bVar2;
        bVar6 = bVar2 == 0x48;
        if (bVar6) {
          if (*(int *)(param_2 + 0x18) < 0) {
            func_0x0208b33c();
            *(undefined4 *)(param_2 + 0x18) = extraout_w9_04;
            if (bVar4 && !bVar6) goto LAB_0208b088;
            func_0x0208b328();
            puVar10 = extraout_x8_05;
          }
          else {
LAB_0208b088:
            func_0x0208b310();
            puVar10 = extraout_x8_12;
          }
          func_0x0208b180(*puVar10,4);
        }
        else {
          bVar4 = 0x57 < bVar2;
          bVar6 = bVar2 == 0x58;
          if (bVar6) {
            if (*(int *)(param_2 + 0x18) < 0) {
              func_0x0208b33c();
              *(undefined4 *)(param_2 + 0x18) = extraout_w9_05;
              if (bVar4 && !bVar6) goto LAB_0208b09c;
              func_0x0208b328();
              puVar10 = extraout_x8_06;
            }
            else {
LAB_0208b09c:
              func_0x0208b310();
              puVar10 = extraout_x8_13;
            }
            func_0x0208b180(*puVar10,8);
          }
          else {
            bVar4 = 0x62 < bVar2;
            bVar6 = bVar2 == 99;
            if (bVar6) {
              if (*(int *)(param_2 + 0x18) < 0) {
                func_0x0208b33c();
                *(undefined4 *)(param_2 + 0x18) = extraout_w9_06;
                if (bVar4 && !bVar6) goto LAB_0208b0b0;
                func_0x0208b328();
                puVar8 = extraout_x8_07;
              }
              else {
LAB_0208b0b0:
                func_0x0208b310();
                puVar8 = extraout_x8_14;
              }
              uVar7 = *puVar8;
              goto LAB_0208af1c;
            }
            bVar4 = 99 < bVar2;
            bVar6 = bVar2 == 100;
            if (bVar6) {
              if (*(int *)(param_2 + 0x18) < 0) {
                func_0x0208b33c();
                *(undefined4 *)(param_2 + 0x18) = extraout_w9_07;
                if (bVar4 && !bVar6) goto LAB_0208b0bc;
                func_0x0208b328();
                piVar9 = extraout_x8_08;
              }
              else {
LAB_0208b0bc:
                func_0x0208b310();
                piVar9 = extraout_x8_15;
              }
              iVar12 = *piVar9;
              if (iVar12 < 0) {
                func_0x0208ac88(0x2d);
                iVar12 = -iVar12;
              }
              func_0x0208abb0(iVar12);
            }
            else {
              if (bVar2 != 0x6c) goto code_r0x0208af78;
              bVar2 = *param_1;
              if (bVar2 == 0x6c) {
                bVar4 = 0x77 < pbVar11[3];
                bVar6 = pbVar11[3] == 0x78;
                if (bVar6) {
                  if (*(int *)(param_2 + 0x18) < 0) {
                    func_0x0208b33c();
                    *(undefined4 *)(param_2 + 0x18) = extraout_w9_08;
                    if (bVar4 && !bVar6) goto LAB_0208b150;
                    func_0x0208b328();
                    puVar10 = extraout_x8_16;
                  }
                  else {
LAB_0208b150:
                    func_0x0208b310();
                    puVar10 = extraout_x8_20;
                  }
                  func_0x0208b180(*puVar10,0x10);
                  param_1 = pbVar11 + 4;
                }
              }
              else {
                bVar4 = 0x74 < bVar2;
                bVar6 = bVar2 == 0x75;
                if (bVar6) {
                  if (*(int *)(param_2 + 0x18) < 0) {
                    func_0x0208b33c();
                    *(undefined4 *)(param_2 + 0x18) = extraout_w9_09;
                    if (bVar4 && !bVar6) goto LAB_0208b13c;
                    func_0x0208b328();
                    puVar10 = extraout_x8_17;
                  }
                  else {
LAB_0208b13c:
                    func_0x0208b310();
                    puVar10 = extraout_x8_19;
                  }
                  func_0x0208b1d8(*puVar10);
                  param_1 = pbVar11 + 3;
                }
                else {
                  bVar4 = 0x77 < bVar2;
                  bVar6 = bVar2 == 0x78;
                  if (bVar6) {
                    if (*(int *)(param_2 + 0x18) < 0) {
                      func_0x0208b33c();
                      *(undefined4 *)(param_2 + 0x18) = extraout_w9;
                      if (bVar4 && !bVar6) goto LAB_0208b124;
                      func_0x0208b328();
                      puVar10 = extraout_x8;
                    }
                    else {
LAB_0208b124:
                      func_0x0208b310();
                      puVar10 = extraout_x8_18;
                    }
                    func_0x0208b180(*puVar10,0);
                    param_1 = pbVar11 + 3;
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


