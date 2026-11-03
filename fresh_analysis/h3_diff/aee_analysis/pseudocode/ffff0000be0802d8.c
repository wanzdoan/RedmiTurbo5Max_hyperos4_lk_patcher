
undefined8 FUN_ffff0000be0802d8(ulong *param_1,long param_2,ulong param_3)

{
  undefined8 uVar1;
  ulong uVar2;
  ulong uVar3;
  
  if (param_1 == (ulong *)0x0) {
    uVar1 = 0xffffffff;
  }
  else if (param_2 == 0) {
    uVar1 = 0xfffffffe;
  }
  else if (param_1[10] < 0x81) {
    param_1[0xb] = 0x80;
    if (param_3 != 0) {
      do {
        uVar2 = param_1[10];
        if ((uVar2 == 0) && (0x7f < param_3)) {
          FUN_ffff0000be080418(param_1,param_2);
          uVar2 = *param_1;
          *param_1 = uVar2 + 0x400;
          if (0xfffffffffffffbff < uVar2) {
            param_1[1] = param_1[1] + 1;
          }
          uVar3 = param_1[0xb];
          param_2 = param_2 + uVar3;
        }
        else {
          uVar3 = param_3;
          if (0x80 - uVar2 <= param_3) {
            uVar3 = 0x80 - uVar2;
          }
          FUN_ffff0000be0607b0((long)(param_1 + 0xc) + uVar2,param_2,uVar3);
          uVar2 = param_1[10];
          param_2 = param_2 + uVar3;
          param_1[10] = uVar2 + uVar3;
          if (uVar2 + uVar3 == 0x80) {
            param_1[0xb] = 0x80;
            FUN_ffff0000be080418(param_1,param_1 + 0xc);
            uVar2 = *param_1;
            *param_1 = uVar2 + 0x400;
            if (0xfffffffffffffbff < uVar2) {
              param_1[1] = param_1[1] + 1;
            }
            param_1[10] = 0;
          }
        }
        param_3 = param_3 - uVar3;
      } while (param_3 != 0);
    }
    uVar1 = 0;
  }
  else {
    uVar1 = 0xfffffffd;
  }
  return uVar1;
}


