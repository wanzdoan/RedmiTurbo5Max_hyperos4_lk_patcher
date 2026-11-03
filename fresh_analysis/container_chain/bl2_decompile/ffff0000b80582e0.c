
undefined8 FUN_ffff0000b80582e0(long *param_1,undefined4 param_2,char param_3)

{
  undefined8 uVar1;
  long lVar2;
  long lVar3;
  ulong uVar4;
  undefined4 local_48;
  undefined4 uStack_44;
  undefined8 local_40;
  undefined8 uStack_38;
  
  local_48 = 3;
  if (param_3 == '\0') {
    local_48 = 1;
  }
  lVar2 = *param_1;
  local_40 = 0;
  uStack_38 = 0;
  uStack_44 = 0x15430000;
  if ((*(uint *)(lVar2 + 0x30) >> 3 & 1) != 0) {
    uVar4 = 0xffffffffffffff9c;
    param_1[10] = (long)&local_48;
    *(undefined4 *)(lVar2 + 0x94) = 0x15430000;
    *(undefined4 *)(lVar2 + 0x98) = 0;
    *(undefined4 *)(lVar2 + 0x9c) = 0;
    *(undefined4 *)(lVar2 + 0x90) = local_48;
    do {
      lVar2 = *param_1;
      if ((*(uint *)(lVar2 + 0x20) >> 10 & 1) != 0) goto LAB_ffff0000b8058390;
      FUN_ffff0000b804b704(100);
      uVar4 = uVar4 + 100;
    } while (uVar4 < 0x7a0bd);
    lVar2 = *param_1;
    if ((*(uint *)(lVar2 + 0x20) >> 10 & 1) != 0) {
LAB_ffff0000b8058390:
      *(undefined4 *)(lVar2 + 0x20) = 0x400;
      lVar3 = param_1[10];
      if (lVar3 != 0) {
        *(uint *)(lVar3 + 8) = *(uint *)(lVar3 + 8) | *(uint *)(lVar2 + 0x98) & 0xff;
        *(undefined4 *)(lVar3 + 0xc) = *(undefined4 *)(lVar2 + 0x9c);
      }
      param_1[10] = 0;
      uVar1 = FUN_ffff0000b8053f40(param_1,0x15640000,0,local_40._4_4_,param_2);
      return uVar1;
    }
    param_1[10] = 0;
  }
  return 0xffffffff;
}


