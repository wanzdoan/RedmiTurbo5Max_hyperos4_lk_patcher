// ffff00009077eb88  size=372  FUN_ffff00009077eb88

void FUN_ffff00009077eb88(byte *param_1,byte param_2,ulong param_3)

{
  bool bVar1;
  ulong uVar2;
  byte *pbVar3;
  byte *pbVar4;
  ulong uVar5;
  undefined8 uVar6;
  ulong uVar7;
  
  uVar2 = (ulong)(uint)-(int)param_1 & 7;
  if (param_3 <= uVar2) goto joined_r0xffff00009077ebd0;
  param_3 = param_3 - uVar2;
  pbVar4 = param_1;
  if ((-(int)param_1 & 7U) != 0) {
    pbVar4 = param_1 + 1;
    *param_1 = param_2;
    if (uVar2 != 1) {
      param_1[1] = param_2;
      if (uVar2 == 2) {
        pbVar4 = param_1 + 2;
      }
      else {
        param_1[2] = param_2;
        if (uVar2 == 3) {
          pbVar4 = param_1 + 3;
        }
        else {
          param_1[3] = param_2;
          if (uVar2 == 4) {
            pbVar4 = param_1 + 4;
          }
          else {
            param_1[4] = param_2;
            if (uVar2 == 5) {
              pbVar4 = param_1 + 5;
            }
            else {
              param_1[5] = param_2;
              if (uVar2 == 6) {
                pbVar4 = param_1 + 6;
              }
              else {
                pbVar4 = param_1 + 7;
                param_1[6] = param_2;
              }
            }
          }
        }
      }
    }
  }
  if (7 < param_3) {
    uVar2 = param_3 >> 3;
    uVar6 = CONCAT44((uint)param_2 * 0x1010101,(uint)param_2 * 0x1010101);
    if (0xf < param_3) {
      uVar7 = uVar2 & 0x1ffffffffffffffe;
      pbVar3 = pbVar4 + uVar7 * 8;
      pbVar4 = pbVar4 + 8;
      uVar5 = uVar7;
      do {
        *(undefined8 *)(pbVar4 + -8) = uVar6;
        *(undefined8 *)pbVar4 = uVar6;
        pbVar4 = pbVar4 + 0x10;
        uVar5 = uVar5 - 2;
      } while (uVar5 != 0);
      bVar1 = uVar2 == uVar7;
      pbVar4 = pbVar3;
      uVar2 = param_3 >> 3 & 1;
      if (bVar1) goto LAB_ffff00009077eca0;
    }
    do {
      uVar2 = uVar2 - 1;
      pbVar3 = pbVar4 + 8;
      *(undefined8 *)pbVar4 = uVar6;
      pbVar4 = pbVar3;
    } while (uVar2 != 0);
  }
LAB_ffff00009077eca0:
  param_3 = param_3 & 7;
  param_1 = pbVar4;
joined_r0xffff00009077ebd0:
  if (param_3 != 0) {
    if (param_3 == 1) {
      uVar5 = 1;
      pbVar4 = param_1;
    }
    else {
      uVar7 = param_3 & 0xfffffffffffffffe;
      uVar5 = param_3 & 1;
      pbVar4 = param_1 + uVar7;
      pbVar3 = param_1 + 1;
      uVar2 = uVar7;
      do {
        uVar2 = uVar2 - 2;
        pbVar3[-1] = param_2;
        *pbVar3 = param_2;
        pbVar3 = pbVar3 + 2;
      } while (uVar2 != 0);
      if (param_3 == uVar7) {
        return;
      }
    }
    do {
      uVar5 = uVar5 - 1;
      *pbVar4 = param_2;
      pbVar4 = pbVar4 + 1;
    } while (uVar5 != 0);
  }
  return;
}


