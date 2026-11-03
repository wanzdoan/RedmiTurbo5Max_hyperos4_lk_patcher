
undefined8 FUN_ffff00009076d25c(long param_1,undefined8 param_2,uint param_3)

{
  uint uVar1;
  undefined8 uVar2;
  
  if (-1 < (int)param_3) {
    uVar2 = FUN_ffff00009076ca9c();
    if ((int)uVar2 < 0) {
      return uVar2;
    }
    uVar1 = (*(uint *)(param_1 + 4) & 0xff00ff00) >> 8 | (*(uint *)(param_1 + 4) & 0xff00ff) << 8;
    if ((uVar1 >> 0x10 | uVar1 << 0x10) <= param_3) {
      FUN_ffff00009077ec38(param_2,param_1);
      return 0;
    }
  }
  return 0xfffffffd;
}


