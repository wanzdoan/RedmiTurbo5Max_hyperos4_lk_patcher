
undefined8
FUN_ffff0000b8047fb0(long param_1,undefined4 param_2,undefined8 param_3,undefined4 *param_4)

{
  uint uVar1;
  undefined8 uVar2;
  
  uVar1 = (*(uint *)(param_1 + 0x14) & 0xff00ff00) >> 8 |
          (*(uint *)(param_1 + 0x14) & 0xff00ff) << 8;
  if ((uVar1 >> 0x10 | uVar1 << 0x10) < 0x10) {
    if (param_4 != (undefined4 *)0x0) {
      *param_4 = 0xfffffff6;
    }
    return 0;
  }
  uVar2 = FUN_ffff0000b804d3cc(param_3);
  uVar2 = FUN_ffff0000b8047e0c(param_1,param_2,param_3,uVar2,param_4,0);
  return uVar2;
}


