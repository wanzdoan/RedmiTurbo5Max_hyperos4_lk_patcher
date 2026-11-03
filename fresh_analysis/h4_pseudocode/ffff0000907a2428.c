
undefined4 FUN_ffff0000907a2428(undefined8 param_1,undefined8 param_2,undefined8 param_3)

{
  undefined4 uVar1;
  
  FUN_ffff00009077ee50(&DAT_ffff00009090ab88,0,0x100);
  DAT_ffff00009090ab88 = 1;
  DAT_ffff00009090ab8a = 1;
  FUN_ffff0000907adb8c(&DAT_ffff00009090ab88,0x100);
  FUN_ffff0000907adb8c(param_1,0x100);
  FUN_ffff0000907adb8c(param_2,0x100);
  uVar1 = FUN_ffff0000907ab034(param_1,&DAT_ffff00009090ab88,param_2,param_3);
  FUN_ffff0000907adb8c(param_2,0x100);
  FUN_ffff0000907adb8c(param_3,0x100);
  return uVar1;
}


