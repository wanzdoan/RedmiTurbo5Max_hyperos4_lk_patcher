// ffff00009076ee78  size=92  FUN_ffff00009076ee78

undefined8 FUN_ffff00009076ee78(undefined4 *param_1,uint param_2)

{
  uint uVar1;
  
  FUN_ffff00009077eb88(param_1,0,param_2);
  uVar1 = (param_2 & 0xff00ff00) >> 8 | (param_2 & 0xff00ff) << 8;
  param_1[6] = 0;
  *param_1 = 0x1201f22f;
  param_1[1] = uVar1 >> 0x10 | uVar1 << 0x10;
  *(undefined8 *)(param_1 + 2) = 0x30000000;
  *(undefined8 *)(param_1 + 4) = 0x1100000030000000;
  return 0;
}


