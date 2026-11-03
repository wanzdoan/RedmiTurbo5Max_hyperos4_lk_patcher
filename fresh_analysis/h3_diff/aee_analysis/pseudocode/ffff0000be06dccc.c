
undefined4
FUN_ffff0000be06dccc(long *param_1,ulong param_2,uint param_3,undefined4 param_4,char param_5)

{
  long lVar1;
  long lVar2;
  ulong uVar3;
  undefined4 local_58;
  undefined4 uStack_54;
  int local_50;
  undefined4 local_4c;
  undefined8 local_48;
  
  local_48 = 0;
  local_58 = 2;
  if (param_5 != '\0') {
    local_58 = 4;
  }
  local_50 = (param_3 & 0xff) << 0x10;
  uStack_54 = (undefined4)param_2;
  lVar1 = *param_1;
  local_4c = param_4;
  if ((*(uint *)(lVar1 + 0x30) >> 3 & 1) != 0) {
    uVar3 = 0xffffffffffffff9c;
    param_1[10] = (long)&local_58;
    *(undefined4 *)(lVar1 + 0x94) = uStack_54;
    *(int *)(lVar1 + 0x98) = local_50;
    *(undefined4 *)(lVar1 + 0x9c) = param_4;
    *(undefined4 *)(lVar1 + 0x90) = local_58;
    do {
      lVar1 = *param_1;
      if ((*(uint *)(lVar1 + 0x20) >> 10 & 1) != 0) goto LAB_ffff0000be06ddb0;
      FUN_ffff0000be05ede4(100);
      uVar3 = uVar3 + 100;
    } while (uVar3 < 0x7a0bd);
    lVar1 = *param_1;
    if ((*(uint *)(lVar1 + 0x20) >> 10 & 1) != 0) {
LAB_ffff0000be06ddb0:
      *(undefined4 *)(lVar1 + 0x20) = 0x400;
      lVar2 = param_1[10];
      if (lVar2 != 0) {
        *(uint *)(lVar2 + 8) = *(uint *)(lVar2 + 8) | *(uint *)(lVar1 + 0x98) & 0xff;
        *(undefined4 *)(lVar2 + 0xc) = *(undefined4 *)(lVar1 + 0x9c);
      }
      param_1[10] = 0;
      return 0;
    }
    param_1[10] = 0;
  }
  FUN_ffff0000be05fd98
            (s__UFS__err__attr_id_0x_x_val_0x_x_ffff0000be0b277d,(param_2 & 0xffffffff) >> 0x10,
             param_4,0xffffffff,param_5);
  return 0xffffffff;
}


