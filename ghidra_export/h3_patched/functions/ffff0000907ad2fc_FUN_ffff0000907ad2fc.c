// ffff0000907ad2fc  size=576  FUN_ffff0000907ad2fc

undefined8 FUN_ffff0000907ad2fc(int *param_1,uint param_2,undefined4 *param_3)

{
  int iVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  uint uVar5;
  int iVar6;
  undefined8 uVar7;
  int *piVar8;
  
  uVar7 = 0x100006;
  if ((param_1 != (int *)0x0) && (param_3 != (undefined4 *)0x0)) {
    iVar1 = *param_1;
    if ((iVar1 == 0x58881688) && (param_1[0xc] == 0x58891689)) {
      if (param_2 != 0x200) {
        return 0x100009;
      }
      piVar8 = param_1 + 2;
      uVar7 = 0x20;
      *param_3 = 1;
      param_3[1] = param_1[0xd];
      uVar2 = param_1[0x11];
      uVar3 = 0;
      if (uVar2 != 0) {
        uVar3 = ((param_1[1] + uVar2) - 1) / uVar2;
      }
      param_3[3] = uVar3 * uVar2;
      param_3[2] = param_1[0xf];
      param_3[4] = param_1[0x10];
    }
    else {
      uVar7 = FUN_ffff00009077ef88(s_ANDROID__ffff0000907ebde1);
      iVar6 = FUN_ffff00009077e820(param_1,s_ANDROID__ffff0000907ebde1,uVar7);
      if (iVar6 == 0) {
        if (param_2 != 0x67c) {
          return 0x100009;
        }
        uVar2 = param_1[9];
        iVar1 = param_1[10];
        uVar3 = 0;
        if (uVar2 != 0) {
          uVar3 = ((param_1[2] + uVar2) - 1) / uVar2;
        }
        uVar4 = 0;
        if (uVar2 != 0) {
          uVar4 = ((param_1[4] + uVar2) - 1) / uVar2;
        }
        uVar5 = 0;
        if (uVar2 != 0) {
          uVar5 = ((param_1[6] + uVar2) - 1) / uVar2;
        }
        *param_3 = 2;
        param_3[1] = uVar2;
        iVar6 = uVar3 * uVar2 + uVar4 * uVar2 + uVar5 * uVar2;
        param_3[3] = iVar6;
        if (iVar1 != 0) {
          uVar3 = 0;
          if (uVar2 != 0) {
            uVar3 = ((param_1[0x198] + uVar2) - 1) / uVar2;
          }
          iVar6 = iVar6 + uVar3 * uVar2;
          param_3[3] = iVar6;
          if (iVar1 != 1) {
            uVar3 = 0;
            if (uVar2 != 0) {
              uVar3 = ((param_1[0x19c] + uVar2) - 1) / uVar2;
            }
            param_3[3] = iVar6 + uVar3 * uVar2;
          }
        }
        piVar8 = param_1 + 0xc;
        param_3[2] = 0;
        uVar7 = 0x10;
        param_3[4] = 0;
      }
      else {
        if (iVar1 == 0x1eabb7d7) {
          if (param_2 < 0x20) {
            return 0x100009;
          }
          uVar2 = param_1[1];
          uVar3 = param_1[2];
          param_3[4] = 0;
          piVar8 = (int *)&DAT_ffff0000907f24b9;
          uVar3 = (uVar3 & 0xff00ff00) >> 8 | (uVar3 & 0xff00ff) << 8;
          uVar3 = uVar3 >> 0x10 | uVar3 << 0x10;
          uVar2 = (uVar2 & 0xff00ff00) >> 8 | (uVar2 & 0xff00ff) << 8;
          *param_3 = 3;
          param_3[1] = uVar3;
          param_3[2] = 0;
          param_3[3] = ((uVar2 >> 0x10 | uVar2 << 0x10) - uVar3) + 0xf & 0xfffffff0;
        }
        else {
          piVar8 = (int *)&DAT_ffff0000907dc8cb;
          *param_3 = 0;
          *(undefined8 *)(param_3 + 2) = 0;
          param_3[4] = 1;
        }
        uVar7 = 4;
      }
    }
    FUN_ffff00009077e858(param_3 + 5,piVar8,uVar7);
    uVar7 = 0;
  }
  return uVar7;
}


