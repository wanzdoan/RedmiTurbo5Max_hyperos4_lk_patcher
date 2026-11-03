// ffff0000907a2168  size=176  FUN_ffff0000907a2168

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff0000907a2168(undefined8 param_1,undefined8 param_2,undefined8 param_3)

{
  undefined4 uVar1;
  
  FUN_ffff00009077eb88(&DAT_ffff00009090aac8,0,0x100);
  _DAT_ffff00009090aac8 = 1;
  DAT_ffff00009090aaca = 1;
  FUN_ffff0000907ad8cc(&DAT_ffff00009090aac8,0x100);
  FUN_ffff0000907ad8cc(param_1,0x100);
  FUN_ffff0000907ad8cc(param_2,0x100);
  uVar1 = FUN_ffff0000907aad74(param_1,&DAT_ffff00009090aac8,param_2,param_3);
  FUN_ffff0000907ad8cc(param_2,0x100);
  FUN_ffff0000907ad8cc(param_3,0x100);
  return uVar1;
}


