// File offset entry: 0x66530

undefined8 FUN_02066430(int *param_1,undefined8 param_2,undefined4 *param_3)

{
  int iVar1;
  uint uVar2;
  int iVar3;
  uint uVar4;
  
  FUN_02091664(param_3,0,0x8c);
  if (*param_1 != 0x58881688) {
    *param_3 = 0;
    return 0x101001;
  }
  *param_3 = 1;
  iVar3 = param_1[0xc];
  param_3[1] = 0x200;
  if (iVar3 == 0x58891689) {
    param_3[2] = 1;
    param_3[3] = param_1[0xf];
    param_3[0xb] = param_1[0x14];
    param_3[4] = param_1[0xb];
    param_3[5] = param_1[10];
    param_3[6] = param_1[0x13];
    iVar3 = param_1[0x12];
    param_3[10] = iVar3;
    if (iVar3 == -1) {
      return 0x100002;
    }
    uVar4 = param_1[0x11];
    if (0x1000 < uVar4) {
      return 0x100003;
    }
  }
  else {
    param_3[2] = 0;
    param_3[3] = 0;
    iVar3 = 0;
    param_3[0xb] = 0;
    param_3[4] = param_1[0xb];
    iVar1 = param_1[10];
    param_3[10] = 0;
    param_3[5] = iVar1;
    param_3[6] = 0;
    uVar4 = 0x1000;
  }
  if (-uVar4 < (uint)param_3[9]) {
    param_3[10] = iVar3 + 1;
  }
  param_3[7] = param_1[1];
  uVar2 = 0;
  if (uVar4 != 0) {
    uVar2 = ((param_1[1] + uVar4) - 1) / uVar4;
  }
  param_3[9] = uVar2 * uVar4;
  FUN_02091664(param_3 + 0x1b,0,0x20);
  FUN_020915ac(param_3 + 0x1b,param_1 + 2,0x20);
  return 0;
}


