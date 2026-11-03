// ffff00009077d670  size=408  FUN_ffff00009077d670

ulong FUN_ffff00009077d670(byte *param_1,undefined8 *param_2,uint param_3)

{
  byte bVar1;
  undefined1 auVar2 [16];
  undefined1 auVar3 [16];
  int iVar4;
  undefined4 *puVar5;
  ulong uVar6;
  uint uVar7;
  ulong uVar8;
  byte *pbVar9;
  byte bVar10;
  
  if ((param_3 < 0x25) && (param_3 != 1)) {
    do {
      pbVar9 = param_1;
      param_1 = pbVar9 + 1;
      iVar4 = FUN_ffff00009077d808(*pbVar9);
    } while (iVar4 != 0);
    if (*pbVar9 == 0x2b) {
      bVar10 = 0;
    }
    else if (*pbVar9 == 0x2d) {
      bVar10 = 1;
    }
    else {
      bVar10 = 0;
      param_1 = pbVar9;
    }
    if ((param_3 | 0x10) == 0x10) {
      bVar1 = *param_1;
      if ((bVar1 == 0x30) && (param_1[1] == 0x78)) {
        param_1 = param_1 + 2;
        param_3 = 0x10;
      }
      else if (param_3 == 0) {
        if (bVar1 == 0x30) {
          param_1 = param_1 + 1;
        }
        param_3 = 8;
        if (bVar1 != 0x30) {
          param_3 = 10;
        }
      }
    }
    uVar6 = 0;
    while( true ) {
      bVar1 = *param_1;
      uVar7 = (uint)bVar1;
      if (bVar1 - 0x41 < 0x1a) {
        uVar7 = uVar7 - 0x37;
      }
      else if (bVar1 - 0x61 < 0x1a) {
        uVar7 = uVar7 - 0x57;
      }
      else {
        uVar7 = uVar7 - 0x30;
        if (9 < uVar7) {
          uVar7 = 0xffffffff;
        }
      }
      if (((int)uVar7 < 0) || ((int)param_3 <= (int)uVar7)) break;
      auVar2._8_8_ = 0;
      auVar2._0_8_ = CONCAT44(0,param_3);
      auVar3._8_8_ = 0;
      auVar3._0_8_ = uVar6;
      if (SUB168(auVar2 * auVar3,8) == 0) {
        uVar8 = CONCAT44(0,param_3) * uVar6;
        if ((uVar6 == 0xffffffffffffffff) || (uVar6 = uVar8 + uVar7, CARRY8(uVar8,(ulong)uVar7)))
        goto LAB_ffff00009077d750;
      }
      else {
LAB_ffff00009077d750:
        puVar5 = (undefined4 *)FUN_ffff00009077d8f0();
        uVar6 = 0xffffffffffffffff;
        *puVar5 = 0x22;
      }
      param_1 = param_1 + 1;
    }
    if (param_2 != (undefined8 *)0x0) {
      *param_2 = param_1;
    }
    uVar8 = -uVar6;
    if (!(bool)(bVar10 & uVar6 != 0xffffffffffffffff)) {
      uVar8 = uVar6;
    }
  }
  else {
    puVar5 = (undefined4 *)FUN_ffff00009077d8f0();
    *puVar5 = 0x16;
    uVar8 = 0;
  }
  return uVar8;
}


